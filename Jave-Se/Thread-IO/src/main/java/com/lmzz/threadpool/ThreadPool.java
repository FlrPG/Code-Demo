package com.lmzz.threadpool;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

//自定义线程池
public class ThreadPool {

    //工作线程
    private final List<Worker> workers = new ArrayList<>();
    //任务队列
    private BlockingQueue<Runnable> workQueue;
    //核心线程数
    private final int corePoolSize;
    //最大线程数
    private final int maximumPoolSize;
    //非核心线程空闲时间
    private final long keepAliveTime;

    public ThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit timeUnit, BlockingQueue<Runnable> workQueue) {
        this.corePoolSize = corePoolSize;
        this.maximumPoolSize = maximumPoolSize;
        this.workQueue = workQueue;
        this.keepAliveTime = timeUnit.toNanos(keepAliveTime);
    }


    public void execute(Runnable runnable) {
        //使用核心工作线程处理任务
        if (addWorker(runnable, true)) {
            return;
        }

        //核心工作线程不够、加入阻塞队列
        boolean offer = workQueue.offer(runnable);
        if (offer) {
            return;
        }

        //阻塞队列不足，创建非核心线程处理任务
        if (!addWorker(runnable, false)) {
            //非核心线程不够时，拒绝策略
            throw new RuntimeException("拒绝策略");
        }
    }

    public boolean addWorker(Runnable task, boolean isCore) {
        if (workers.size() >= (isCore ? corePoolSize : maximumPoolSize)) {
            return false;
        }


        boolean workStart = false;

        try {
            Worker worker = new Worker(task);
            Thread thread = worker.getThread();
            if (thread != null) {
                workers.add(worker);
                thread.start();
                workStart = true;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {

        }
        return workStart;
    }

    public void runWorker(Worker worker) {
        Runnable task = worker.getTask();

        try {
            // 循环处理任务
            while (task != null || (task = getTask()) != null) {
                task.run();
                task = null;
            }
        } finally {
            // 从循环退出来，意味着当前线程是非核心线程，而且需要被销毁
            // Java的线程，既可以指代Thread对象，也可以指代JVM线程，一个Thread对象绑定一个JVM线程
            // 因此，线程的销毁分为两个维度：1.把Thread对象从workers移除 2.JVM线程执行完当前任务，会自然销毁
            workers.remove(worker);
        }
    }

    public Runnable getTask() {
        //是否超时
        boolean timeOut = false;
        for (; ; ) {
            //判断是否需要检测超时
            boolean timed = workers.size() > corePoolSize;

            if (timed && timeOut) {
                return null;
            }
            try {
                Runnable r = timed ?
                        workQueue.poll(keepAliveTime, TimeUnit.NANOSECONDS) :
                        workQueue.take();
                if (r != null) {
                    return r;
                }
                timeOut = true;
            } catch (InterruptedException e) {
                timeOut = false;
            }
        }
    }

    @Getter
    class Worker implements Runnable {
        private Thread thread;
        private Runnable task;


        public Worker(Runnable task) {
            this.task = task;
            thread = new Thread(this);
        }

        @Override
        public void run() {
            runWorker(this);
        }
    }

}

@Slf4j
class ThreadPoolTest {
    public static void main(String[] args) {

        // 创建线程池，核心线程1，最大线程2
        // 提交4个任务：第1个任务交给核心线程、第2个任务入队、第3个任务交给非核心线程、第4个任务被拒绝
        ThreadPool threadPoolExecutor = new ThreadPool(
                1,
                2,
                1,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(1)
        );

        threadPoolExecutor.execute(() -> {
            log.info("{}:执行第1个任务...", Thread.currentThread().getName());
            sleep(10);
        });

        sleep(1);

        threadPoolExecutor.execute(() -> {
            log.info("{}:执行第2个任务...", Thread.currentThread().getName());
            sleep(10);

        });

        sleep(1);

        threadPoolExecutor.execute(() -> {
            log.info("{}:执行第3个任务...", Thread.currentThread().getName());
            sleep(10);
        });

        sleep(1);

        threadPoolExecutor.execute(() -> {
            log.info("{}:执行第4个任务...", Thread.currentThread().getName());
            sleep(10);
        });

        sleep(1);

        log.info("main结束");
    }

    private static void sleep(int seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
