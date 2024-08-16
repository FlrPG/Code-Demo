package com.flr.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

@Configuration
public class RedisSubscriber {

    private final RedisTemplate<String, String> redisTemplate;
    private final RedisMessageListenerContainer container;

    public RedisSubscriber(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisTemplate.getConnectionFactory());
    }

    public void subscribe(String channel) {
        container.addMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message, byte[] pattern) {
                String msg = new String(message.getBody());
                System.out.println("Received a message on channel " + channel + ": " + msg);
            }
        }, new ChannelTopic(channel));
        container.afterPropertiesSet();
        container.start();
    }
}
