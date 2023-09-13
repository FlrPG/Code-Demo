package com.flr.task;

import cn.hutool.core.util.IdUtil;
import com.flr.third.AsyncResumeParser;
import com.flr.consts.RedisKeyConst;
import com.flr.service.RedisService;
import com.flr.pojo.ResumeCollectionDO;
import com.flr.pojo.ResumeCollectionDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Component
@EnableScheduling
public class ResumeCollectionTask implements ApplicationListener<ContextRefreshedEvent> {

    /**
     * 当这份代码被部署到不同的服务器，启动时为每台机器分配一个唯一的机器ID
     */
    private static final String MACHINE_ID = IdUtil.randomUUID();

    @Autowired
    private RedisService redisService;
    @Autowired
    private AsyncResumeParser asyncResumeParser;

    @Scheduled(cron = "0 */1 * * * ?")
//    @Scheduled(fixedDelay = 60 * 1000L)
    public void resumeSchedule() {
        // 尝试上锁，返回true或false，锁的过期时间设置为10分钟（实际要根据项目调整，这也是自己实现Redis分布式锁的难点之一）
        boolean lock = redisService.tryLock(RedisKeyConst.RESUME_PULL_TASK_LOCK, MACHINE_ID, 10, TimeUnit.MINUTES);

        // 如果当前节点成功获取锁，那么整个系统只允许当前程序去MySQL拉取待执行任务
        if (lock) {
            log.info("节点:{}获取锁成功，定时任务启动", MACHINE_ID);
            try {
                collectResume();
            } catch (Exception e) {
                log.info("定时任务异常:", e);
            } finally {
                redisService.unLock(RedisKeyConst.RESUME_PULL_TASK_LOCK, MACHINE_ID);
                log.info("节点:{}释放锁，定时任务结束", MACHINE_ID);
            }
        } else {
            log.info("节点:{}获取锁失败，放弃定时任务", MACHINE_ID);
        }
    }

    /**
     * 任务主体：
     * 1.从数据库拉取符合条件的HR邮箱
     * 2.从HR邮箱拉取附件简历
     * 3.调用远程服务异步解析简历
     * 4.插入待处理任务到数据库，作为记录留存
     * 5.把待处理任务的id丢到Redis Message Queue，让Consumer去异步处理
     */
    private void collectResume() throws InterruptedException {
        // 跳过1、2两步，假设已经拉取到简历
        log.info("节点:{}从数据库拉取任务简历", MACHINE_ID);
        List<ResumeCollectionDO> resumeCollectionList = new ArrayList<>();
        resumeCollectionList.add(new ResumeCollectionDO(1L, "张三的简历.pdf"));
        resumeCollectionList.add(new ResumeCollectionDO(2L, "李四的简历.html"));
        resumeCollectionList.add(new ResumeCollectionDO(3L, "王五的简历.doc"));
        // 模拟数据库查询耗时
        TimeUnit.SECONDS.sleep(3);

        log.info("提交任务到消息队列:{}", resumeCollectionList.stream().map(ResumeCollectionDO::getName).collect(Collectors.joining(",")));

        for (ResumeCollectionDO resumeCollectionDO : resumeCollectionList) {
            // 上传简历异步解析，得到异步结果id
            Long asyncPredictId = asyncResumeParser.asyncParse(resumeCollectionDO);

            // 把任务插入数据库
            // 略...

            // 把任务丢到Redis Message Queue
            ResumeCollectionDTO resumeCollectionDTO = new ResumeCollectionDTO();
            BeanUtils.copyProperties(resumeCollectionDO, resumeCollectionDTO);
            resumeCollectionDTO.setAsyncPredictId(asyncPredictId);
            redisService.pushQueue(RedisKeyConst.RESUME_PARSE_TASK_QUEUE, resumeCollectionDTO);
        }

    }


    /**
     * 项目重启后先尝试删除之前的锁（如果存在），防止死锁等待
     *
     * @param event the event to respond to
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        redisService.releaseLock(RedisKeyConst.RESUME_PULL_TASK_LOCK, MACHINE_ID);
    }

}