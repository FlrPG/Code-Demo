package com.lmzz.system.io.MultiplexingThreads;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.Channel;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.util.concurrent.atomic.AtomicInteger;

public class SelectorThreadGroup {


    SelectorThread[] sts;

    AtomicInteger xid = new AtomicInteger(0);

    SelectorThreadGroup worker;


    public SelectorThreadGroup(int num) {
        sts = new SelectorThread[num];
        for (int i = 0; i < num; i++) {
            sts[i] = new SelectorThread(this);
            new Thread(sts[i]).start();
        }

    }

    public void bind(int port) {
        //方法是在主线程上执行
        try {
            ServerSocketChannel ssc = ServerSocketChannel.open();
            ssc.configureBlocking(false);
            ssc.bind(new InetSocketAddress(port));
            //注册在哪个select 上？
//            nextSelector(ssc);
//            nextSelectorV2(ssc);
            //有一个selector 固定只accept client链接
//            nextSelectorV3(ssc);
            nextSelectorV4(ssc);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setWorker(SelectorThreadGroup worker) {
        this.worker = worker;
    }

    public void nextSelectorV4(Channel c) {
        //boss 负责accept，worker 负责 RW
        if (c instanceof ServerSocketChannel) {
            //==> boss
            SelectorThread st = next();
            //通过队列传递数据
            st.lbq.add(c);
            //selector 一定会阻塞，打断阻塞，让对应的打断selector 自己处理
            st.selector.wakeup();
        } else {
            //==> worker
            SelectorThread st = nextV3();
            st.lbq.add(c);
            st.selector.wakeup();
        }
    }

    public void nextSelectorV3(Channel c) {
        //有一个selector 固定只accept client链接
        if (c instanceof ServerSocketChannel) {
            sts[0].lbq.add(c);
            sts[0].selector.wakeup();
        } else {
            SelectorThread st = nextV2();
            //通过队列传递数据
            st.lbq.add(c);
            //selector 一定会阻塞，打断阻塞，让对应的打断selector 自己处理
            st.selector.wakeup();
        }
    }

    public void nextSelectorV2(Channel c) {
        SelectorThread st = next();

        //通过队列传递数据
        st.lbq.add(c);
        //selector 一定会阻塞，打断阻塞，让对应的打断selector 自己处理
        st.selector.wakeup();
    }

    /**
     * @param c channel 可能是Server 或者是 client
     */
    public void nextSelector(Channel c) {
        SelectorThread st = next();
        ServerSocketChannel ssc = (ServerSocketChannel) c;
        //方案有问题，channel 类型强转，注册唤醒机制问题：应该是selector自己注册
        try {
            ssc.register(st.selector, SelectionKey.OP_ACCEPT);//会被阻塞
            st.selector.wakeup();
        } catch (ClosedChannelException e) {
            throw new RuntimeException(e);
        }
    }

    private SelectorThread next() {
        int index = xid.incrementAndGet() % sts.length;
        return sts[index];
    }

    private SelectorThread nextV2() {
        int index = xid.incrementAndGet() % (sts.length - 1);
        return sts[index + 1];
    }

    private SelectorThread nextV3() {
        int index = xid.incrementAndGet() % (worker.sts.length);
        return worker.sts[index];
    }
}
