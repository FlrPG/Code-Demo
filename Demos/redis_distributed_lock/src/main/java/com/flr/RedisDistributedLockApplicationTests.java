package com.flr;

import com.flr.consts.RedisKeyConst;
import com.flr.pojo.ResumeCollectionDTO;
import com.flr.service.RedisService;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
public class RedisDistributedLockApplicationTests {

    @Resource
    private RedisService redisService;

    /**
     * 作为失败案例（因为不存在777L这个解析任务，AsyncResumeParse.results会返回null）
     * 观察RedisMessageQueueConsumer的处理方式
     */
    @Test
    public void contextLoads() {
        ResumeCollectionDTO resumeCollectionDTO = new ResumeCollectionDTO();
        resumeCollectionDTO.setId(666L);
        resumeCollectionDTO.setAsyncPredictId(777L);
        resumeCollectionDTO.setName("测试1号");

        redisService.pushQueue(RedisKeyConst.RESUME_PARSE_TASK_QUEUE, resumeCollectionDTO);

    }

}