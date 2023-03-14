package com.lmzz.system.io;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * 多路复用器
 */
public class SocketMultiplexingSingleThread {

    private Selector selector = null;

    private void initServer() {
        try {
            ServerSocketChannel ss = ServerSocketChannel.open();
            ss.bind(new InetSocketAddress(9999));
            ss.configureBlocking(false);
            /**
             * select|poll
             * epoll： epoll_create -> fd3
             */
            selector = Selector.open();
            /*
            server 约等于 listen 状态的 fd4
            select|poll：jvm内开辟一个数据 fd4 放进去
            epoll： epoll_ctl(fd3,ADD,fd4,EPOLLIN)
             */
            ss.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void start() {
        initServer();
        System.out.println("server 启动。。。");
        try {
            while (true) {
                Set<SelectionKey> keys = selector.keys();
                System.out.println(keys.size());

                while (true) {
                    /*
                    select()是什么：
                    1. select|poll：其实是内核的select（fd4），poll（fd4）
                    2. epoll：是内核的epoll_wait()

                    select参数可以带时间
                    没有时间返回 0 阻塞
                    有时间，设置一个超时

                    懒加载：
                    ss.register() 时系统调用的epoll_ctl（）不一定会马上执行，
                    等到select（）时才会触发 epoll_ctl
                     */
                    if (!(selector.select() > 0)) {
                        Set<SelectionKey> selectionKeys = selector.selectedKeys();
                        Iterator<SelectionKey> iterator = selectionKeys.iterator();

                        if (iterator.hasNext()) {
                            SelectionKey key = iterator.next();
                            iterator.remove();
                            if (key.isAcceptable()) {
                                /*
                                接收新的连接，accept 接收新的连接，返回新连接的fd
                                select|poll,因为在内核没有空间，保存在之前jvm 生成的listen同一空间
                                epoll,希望通过epoll_ctrl 把新的fd 存在内核空间
                                 */
                                acceptHandler(key);
                            } else if (key.isReadable()) {
                                readHandler(key);
                                /*
                                当前线程read方法可能会阻塞
                                所以 为什么提出了 IO Threads
                                 */
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void readHandler(SelectionKey key) {
        SocketChannel client = (SocketChannel) key.channel();
        ByteBuffer buffer = (ByteBuffer) key.attachment();
        buffer.clear();
        int read;
        try {
            while (true) {
                read = client.read(buffer);
                if (read > 0) {
                    buffer.flip();
                    while (buffer.hasRemaining()) {
                        client.write(buffer);
                    }
                    buffer.clear();
                } else if (read == 0) {
                    break;
                } else {
                    client.close();
                    break;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void acceptHandler(SelectionKey key) {
        try {
            ServerSocketChannel ss = (ServerSocketChannel) key.channel();
            SocketChannel client = ss.accept();
            client.configureBlocking(false);

            ByteBuffer byteBuffer = ByteBuffer.allocate(4096);

            /*
            select|poll:jvm 开辟数组 fd7 放进去
            epoll： epoll_ctl(fd3,add,fd7,epollin
             */
            client.register(selector, SelectionKey.OP_READ, byteBuffer);
            System.out.println("----------------------新客户端：" + client.getRemoteAddress());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
