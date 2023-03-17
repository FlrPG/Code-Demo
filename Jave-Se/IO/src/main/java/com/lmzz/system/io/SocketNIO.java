package com.lmzz.system.io;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

public class SocketNIO {
    public static void main(String[] args) throws IOException {
        List<SocketChannel> clients = new ArrayList<>();
        ServerSocketChannel ss = ServerSocketChannel.open();
        ss.bind(new InetSocketAddress(9999));
        ss.configureBlocking(false);
        while (true) {
            SocketChannel client = ss.accept();

            if (client == null) {
//                System.out.println("client null ...");
            } else {
                //重点  socket（服务端的listen socket<连接请求三次握手后，往我这里扔，
                // 我去通过accept 得到  连接的socket>，连接socket<连接后的数据读写使用的> ）
                client.configureBlocking(false);
                System.out.println("client: " + client.getRemoteAddress());
                clients.add(client);
            }
            ByteBuffer buffer = ByteBuffer.allocate(4096);
            for (SocketChannel channel : clients) {
                int read = channel.read(buffer);
                if (read > 0) {
                    buffer.flip();
                    byte[] a = new byte[buffer.limit()];
                    buffer.get(a);

                    String s = new String(a);
                    System.out.println(channel.getRemoteAddress() + " : " + s);
                    buffer.clear();
                }
            }
        }
    }
}
