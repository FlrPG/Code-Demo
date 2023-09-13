package com.flr.third;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flr.pojo.ResumeCollectionDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 第三方提供给的简历解析服务
 *
 */
@Service
public class AsyncResumeParser {

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 模拟分配异步任务结果id，不用深究，没啥意义，反正每个任务都会得到一个id，稍后根据id返回最终解析结果
     */
    private static final AtomicLong ASYNC_RESULT_ID = new AtomicLong(1000);
    /**
     * 解析结果
     */
    private static final Map<Long, String> results = new HashMap<>();

    /**
     * 模拟第三方服务异步解析，返回解析结果
     *
     * @param resumeCollectionDO
     * @return
     */
    public Long asyncParse(ResumeCollectionDO resumeCollectionDO) {
        long asyncPredictId = ASYNC_RESULT_ID.getAndIncrement();
        try {
            String resultJson = objectMapper.writeValueAsString(resumeCollectionDO);
            results.put(asyncPredictId, resultJson);
            return asyncPredictId;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return -1L;
    }

    /**
     * 根据异步id返回解析结果，但此时未必已经解析成功
     * <p>
     * 解析状态
     * 0 初始化
     * 1 处理中
     * 2 调用成功
     * 3 调用失败
     *
     * @param asyncPredictId
     * @return
     */
    public PredictResult getResult(Long asyncPredictId) throws ParseErrorException, InterruptedException {
        // 随机模拟异步解析的状态
        int value = ThreadLocalRandom.current().nextInt(100);
        if (value >= 85) {
            // 模拟解析完成
            TimeUnit.SECONDS.sleep(1);
            String resultJson = results.get(asyncPredictId);
            return new PredictResult(resultJson, 2);
        } else if (value <= 5) {
            // 模拟解析异常
            TimeUnit.SECONDS.sleep(1);
            throw new ParseErrorException("简历解析异常");
        }
        // 如果时间过短，返回status=1，表示解析中
        TimeUnit.SECONDS.sleep(1);
        return new PredictResult("", 1);
    }

}