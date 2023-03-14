package com.lmzz.system.io.MultiplexingThreads;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.Channel;
import java.nio.channels.ServerSocketChannel;
import java.util.concurrent.atomic.AtomicInteger;

public class SelectorThreadGroup {


    SelectorThread[] sts;

    AtomicInteger xid = new AtomicInteger(0);


    public SelectorThreadGroup(int num) {
        sts = new SelectorThread[num];
        for (int i = 0; i < num; i++) {
            sts[i] = new SelectorThread();
            new Thread(sts[i]).start();
        }

    }

    public void bind(int port) {
        try {
            ServerSocketChannel ssc = ServerSocketChannel.open();
            ssc.configureBlocking(false);
            ssc.bind(new InetSocketAddress(port));

            //注册在哪个select 上？
            nextSelector(ssc);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * @param c channel 可能是Server 或者是 client
     */
    private void nextSelector(Channel c) {
        SelectorThread st = next();



    }

    private SelectorThread next() {
        int index = xid.incrementAndGet() % sts.length;
        return sts[index];
    }
}
