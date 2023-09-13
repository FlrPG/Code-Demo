package com.flr.service;

import java.util.concurrent.TimeUnit;

public interface RedisService {

    /**
     * 向队列插入消息
     *
     * @param queue 自定义队列名称
     * @param obj   要存入的消息
     */
    void pushQueue(String queue, Object obj);

    /**
     * 从队列取出消息
     *
     * @param queue    自定义队列名称
     * @param timeout  最长阻塞等待时间
     * @param timeUnit 时间单位
     * @return
     */
    Object popQueue(String queue, long timeout, TimeUnit timeUnit);

    /**
     * 尝试上锁
     *
     * @param lockKey
     * @param value
     * @param expireTime
     * @param timeUnit
     * @return
     */
    boolean tryLock(String lockKey, String value, long expireTime, TimeUnit timeUnit);

    /**
     * 根据MACHINE_ID解锁（只能解自己的）
     *
     * @param lockKey
     * @param value
     * @return
     */
    boolean unLock(String lockKey, String value);

    /**
     * 释放锁，不管是不是自己的
     *
     * @param lockKey
     * @param value
     * @return
     */
    boolean releaseLock(String lockKey, String value);

}