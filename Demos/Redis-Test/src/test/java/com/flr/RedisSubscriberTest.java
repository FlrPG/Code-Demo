package com.flr;

import com.flr.config.RedisSubscriber;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;

import static org.mockito.Mockito.verify;

@SpringBootTest
public class RedisSubscriberTest {

    @Autowired
    private RedisSubscriber redisSubscriber;

//    @MockBean
//    private RedisTemplate<String, String> redisTemplate;

    @Resource(name = "GenericJackson2RedisTemplate")
    private RedisTemplate<String, Object> redisTemplate2;

    @Test
    public void testSubscribeToChannel() {
        String channel = "test-channel";
        redisSubscriber.subscribe(channel);

        // 模拟发送消息到频道
        redisTemplate2.convertAndSend(channel, "Test message");


        // 验证是否成功接收消息
        // 这里可以自行设计验证逻辑，比如使用Mockito的verify方法来验证是否执行了预期的逻辑
        verify(redisTemplate2).convertAndSend(channel, "Test message");
    }
}
