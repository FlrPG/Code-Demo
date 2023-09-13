package com.flr.consumer;

import com.flr.consts.RedisKeyConst;
import com.flr.pojo.ResumeCollectionDO;
import com.flr.pojo.ResumeCollectionDTO;
import com.flr.service.RedisService;
import com.flr.third.AsyncResumeParser;
import com.flr.third.PredictResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;


import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * 消费者，异步获取简历解析结果并存入数据库
 *
 */
@Slf4j
@Component
public class RedisMessageQueueConsumer implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private RedisService redisService;
    @Autowired
    private AsyncResumeParser asyncResumeParser;
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        log.info("开始监听RedisMessageQueue...");
        CompletableFuture.runAsync(() -> {
            // 大循环，不断监听队列任务（阻塞式）
            while (true) {
                // 阻塞监听
                ResumeCollectionDTO resumeCollectionDTO = (ResumeCollectionDTO) redisService.popQueue(RedisKeyConst.RESUME_PARSE_TASK_QUEUE, 5, TimeUnit.SECONDS);
                if (resumeCollectionDTO != null) {
                    int rePullCount = 0;
                    int retryCount = 0;
                    log.info("从队列中取出:{}", resumeCollectionDTO.getName());
                    log.info(">>>>>>>>>>>>>>>>>>>开始拉取简历:{}", resumeCollectionDTO.getName());
                    Long asyncPredictId = resumeCollectionDTO.getAsyncPredictId();
                    // 小循环，针对每一个任务多次调用第三方接口，直到获取最终结果或丢弃任务
                    while (true) {
                        try {
                            PredictResult result = asyncResumeParser.getResult(asyncPredictId);
                            rePullCount++;
                            // 如果已经解析完毕
                            if (result.getStatus() == 2) {
                                // 保存数据库
                                try {
                                    log.info("简历:{}解析成功", resumeCollectionDTO.getName());
                                    log.info("resultJson:{}", result.getResultJson());
                                    ResumeCollectionDO resumeCollectionDO = objectMapper.readValue(result.getResultJson(), ResumeCollectionDO.class);
                                    log.info("<<<<<<<<<<<<<<<<<<<保存简历:{}到数据库", resumeCollectionDO);
                                    // 归零
                                    rePullCount = 0;
                                    retryCount = 0;
                                    break;
                                } catch (Exception e) {
                                    discardTask(resumeCollectionDTO);
                                    log.info("<<<<<<<<<<<<<<<<<<<保存简历失败，丢弃任务");
                                    rePullCount = 0;
                                    retryCount = 0;
                                    break;
                                }
                            }
                            // 远程服务还未解析完毕，重试
                            else {
                                try {
                                    if (rePullCount <= 3) {
                                        // 前3次重试，时间为1s间隔
                                        TimeUnit.SECONDS.sleep(1);
                                        log.info("简历:{}尚未解析完毕, 准备进行第{}次重试, 停顿1s后进行", resumeCollectionDTO.getName(), rePullCount);
                                    } else if (rePullCount > 3 && rePullCount <= 6) {
                                        // 说明任务比较耗时，加长等待时间
                                        TimeUnit.SECONDS.sleep(2);
                                        log.info("简历:{}尚未解析完毕, 准备进行第{}次重试, 停顿2s后进行", resumeCollectionDTO.getName(), rePullCount);
                                    } else if (rePullCount > 6 && rePullCount <= 8) {
                                        // 说明任务比较耗时，加长等待时间
                                        TimeUnit.SECONDS.sleep(3);
                                        log.info("简历:{}尚未解析完毕, 准备进行第{}次重试, 停顿3s后进行", resumeCollectionDTO.getName(), rePullCount);
                                    } else {
                                        discardTask(resumeCollectionDTO);
                                        log.info("<<<<<<<<<<<<<<<<<<<多次拉取仍未得到结果, 丢弃简历:{}", resumeCollectionDTO.getName());
                                        retryCount = 0;
                                        rePullCount = 0;
                                        break;
                                    }
                                } catch (InterruptedException e) {
                                    discardTask(resumeCollectionDTO);
                                    log.info("<<<<<<<<<<<<<<<<<<<任务中断异常, 简历:{}", resumeCollectionDTO.getName());
                                    rePullCount = 0;
                                    retryCount = 0;
                                    break;
                                }
                            }
                        } catch (Exception e) {
                            if (retryCount > 3) {
                                discardTask(resumeCollectionDTO);
                                log.info("<<<<<<<<<<<<<<<<<<<简历:{}重试{}次后放弃, rePullCount:{}, retryCount:{}", resumeCollectionDTO.getName(), retryCount, rePullCount, retryCount);
                                rePullCount = 0;
                                retryCount = 0;
                                break;
                            }
                            retryCount++;
                            log.info("简历:{}远程调用异常, 准备进行第{}次重试...", resumeCollectionDTO.getName(), retryCount);
                        }
                    }
                    log.info("break......");
                }
            }
        });
    }

    private void discardTask(ResumeCollectionDTO task) {
        // 根据asyncPredictId删除任务...
        log.info("丢弃任务:{}...", task.getName());
    }

}