package com.flr.consts;

/**
 * 统一管理Redis Key
 *
 */
public final class RedisKeyConst {
    /**
     * 分布式锁的KEY
     */
    public static final String RESUME_PULL_TASK_LOCK = "resume_pull_task_lock";
    /**
     * 简历异步解析任务队列
     */
    public static final String RESUME_PARSE_TASK_QUEUE = "resume_parse_task_queue";
}