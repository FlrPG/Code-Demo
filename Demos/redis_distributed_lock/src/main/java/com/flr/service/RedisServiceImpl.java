package com.flr.service;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class RedisServiceImpl implements RedisService {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 向队列插入消息
     *
     * @param queue 自定义队列名称
     * @param obj   要存入的消息
     */
    @Override
    public void pushQueue(String queue, Object obj) {
        redisTemplate.opsForList().leftPush(queue, obj);
    }

    /**
     * 从队列取出消息
     *
     * @param queue    自定义队列名称
     * @param timeout  最长阻塞等待时间
     * @param timeUnit 时间单位
     * @return
     */
    @Override
    public Object popQueue(String queue, long timeout, TimeUnit timeUnit) {
        return redisTemplate.opsForList().rightPop(queue, timeout, timeUnit);
    }

    /**
     * 尝试上锁
     *
     * @param lockKey
     * @param value
     * @param expireTime
     * @param timeUnit
     * @return
     */
    @Override
    public boolean tryLock(String lockKey, String value, long expireTime, TimeUnit timeUnit) {
        Boolean lock = redisTemplate.opsForValue().setIfAbsent(lockKey, value);
        if (Boolean.TRUE.equals(lock)) {
            redisTemplate.expire(lockKey, expireTime, timeUnit);
            return true;
        } else {
            return false;
        }
    }


    /**
     * 根据MACHINE_ID解锁（只能解自己的）
     *
     * @param lockKey
     * @param value
     * @return
     */
    @Override
    public boolean unLock(String lockKey, String value) {
        String machineId = (String) redisTemplate.opsForValue().get(lockKey);
        if (StrUtil.isNotEmpty(machineId) && machineId.equals(value)) {
            redisTemplate.delete(lockKey);
            return true;
        }
        return false;
    }

    /**
     * 释放锁，不管是不是自己的
     *
     * @param lockKey
     * @param value
     * @return
     */
    @Override
    public boolean releaseLock(String lockKey, String value) {
        Boolean delete = redisTemplate.delete(lockKey);
        if (Boolean.TRUE.equals(delete)) {
            log.info("Spring启动，节点:{}成功释放上次简历汇聚定时任务锁", value);
            return true;
        }
        return false;
    }

}