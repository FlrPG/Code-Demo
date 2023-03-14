package com.lmzz.system.io.MultiplexingThreads;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/*
每个线程对应一个 selector
多线程下，程序的客户端分配到多个select上
 */
public class SelectorThread implements Runnable {
    Selector selector;

    public SelectorThread() {
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
                int num = selector.select();

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
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void readHandler(SelectionKey key) {
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
                } else {
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

            //多线程下注册到哪个selector下 ？？

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
