package com.lmzz.system.io.MultiplexingThreads;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

/*
每个线程对应一个 selector
多线程下，程序的客户端分配到多个select上
类似于 Netty NioEventLoop
 */
public class SelectorThread implements Runnable {
    Selector selector;
    LinkedBlockingQueue<Channel> lbq = new LinkedBlockingQueue<>();

    SelectorThreadGroup stg;

    public SelectorThread(SelectorThreadGroup selectorThreadGroup) {
        this.stg = selectorThreadGroup;
        try {
            selector = Selector.open();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                //1. select()
//                System.out.println(Thread.currentThread().getName() + " before select ...." + selector.keys().size());
                int num = selector.select();//阻塞， select 有wakeUp（）方法，可以由其他线程唤醒
//                Thread.sleep(1000);
//                System.out.println(Thread.currentThread().getName() + " after select ...." + selector.keys().size());
                //2.处理selectKeys
                if (num > 0) {
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    Iterator<SelectionKey> iter = selectionKeys.iterator();
                    while (iter.hasNext()) {
                        SelectionKey key = iter.next();
                        iter.remove();
                        if (key.isAcceptable()) {
                            acceptHandler(key);
                        } else if (key.isReadable()) {
                            readHandler(key);
                        } else if (key.isWritable()) {

                        }
                    }
                }
                //3.runTask处理Task
                if (!lbq.isEmpty()) {
                    Channel c = lbq.take();
                    if (c instanceof ServerSocketChannel) {
                        ServerSocketChannel server = (ServerSocketChannel) c;
                        server.register(selector, SelectionKey.OP_ACCEPT);
                        System.out.println(Thread.currentThread().getName() + " register listen");
                    } else if (c instanceof SocketChannel) {
                        SocketChannel client = (SocketChannel) c;
                        ByteBuffer buffer = ByteBuffer.allocateDirect(4096);
                        client.register(selector, SelectionKey.OP_READ, buffer);
                        System.out.println(Thread.currentThread().getName() + " register client: " + client.getRemoteAddress());
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void readHandler(SelectionKey key) {
        System.out.println(Thread.currentThread().getName() + " read......");
        ByteBuffer buffer = (ByteBuffer) key.attachment();
        SocketChannel client = (SocketChannel) key.channel();
        buffer.clear();
        while (true) {
            try {
                int num = client.read(buffer);
                if (num > 0) {
                    buffer.flip();
                    while (buffer.hasRemaining()) {
                        client.write(buffer);
                    }
                } else if (num == 0) {
                    break;
                } else if (num < 0) {
                    System.out.println("client： " + client.getRemoteAddress() + " close......");
                    key.cancel();
                    break;
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void acceptHandler(SelectionKey key) {
        System.out.println(Thread.currentThread().getName() + " acceptHandler......");

        ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
        try {
            SocketChannel client = ssc.accept();
            client.configureBlocking(false);

            //!!!!!!!!!!!!!!!!!!!!!!!
            //多线程下注册到哪个selector下：当前 selector 持有group
//            stg.nextSelectorV2(client);
//            stg.nextSelectorV3(client);
            stg.nextSelectorV4(client);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
