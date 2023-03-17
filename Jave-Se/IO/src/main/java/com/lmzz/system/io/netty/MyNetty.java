package com.lmzz.system.io.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;
import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;

public class MyNetty {

    @Test
    public void byteBuf() {
//        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer(8, 20);
        //pool
        ByteBuf buf = UnpooledByteBufAllocator.DEFAULT.heapBuffer(8, 20);
//        ByteBuf buf = PooledByteBufAllocator.DEFAULT.heapBuffer(8, 20);

        print(buf);

        buf.writeBytes(new byte[]{1, 2, 3, 4});
        print(buf);
        buf.writeBytes(new byte[]{1, 2, 3, 4});
        print(buf);
        buf.writeBytes(new byte[]{1, 2, 3, 4});
        print(buf);
        buf.writeBytes(new byte[]{1, 2, 3, 4});
        print(buf);
        buf.writeBytes(new byte[]{1, 2, 3, 4});
        print(buf);
        buf.writeBytes(new byte[]{1, 2, 3, 4});
        print(buf);
    }

    private void print(ByteBuf buf) {
        System.out.println("buf.isReadable()    " + buf.isReadable());
        System.out.println("buf.readerIndex()   " + buf.readerIndex());
        System.out.println("buf.readableBytes() " + buf.readableBytes());
        System.out.println("buf.isWritable()    " + buf.isWritable());
        System.out.println("buf.writerIndex()   " + buf.writerIndex());
        System.out.println("buf.writableBytes() " + buf.writableBytes());
        System.out.println("buf.capacity()      " + buf.capacity());
        System.out.println("buf.maxCapacity()   " + buf.maxCapacity());
        System.out.println("buf.isDirect()      " + buf.isDirect());
        System.out.println("--------------------");
    }

    /*
    客户端，链接server
    1. 主动发送数据
    2. 别人什么给我发数据
     */
    @Test
    public void loopTest() throws IOException {
        NioEventLoopGroup selector = new NioEventLoopGroup(1);
        selector.execute(() -> {
            try {
                for (; ; ) {
                    System.out.println("loop1");
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        selector.execute(() -> {
            try {
                for (; ; ) {
                    System.out.println("loop2");
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        /*
        1. selector.execute(); 这个过程是异步，需要阻塞停止
        2. NioEventGroup execute 按规定线程数执行
         */
        //
        System.in.read();
    }

    @Test
    public void clientModel() throws IOException, InterruptedException {
        NioEventLoopGroup selector = new NioEventLoopGroup(1);

        NioSocketChannel client = new NioSocketChannel();
        selector.register(client);

        //io 事件第一，Rw第二
        ChannelPipeline pipeline = client.pipeline();
        pipeline.addLast(new AcceptHandle());

        ChannelFuture connect = client.connect(new InetSocketAddress("192.168.208.130", 9090));
        ChannelFuture sync = connect.sync();

        ByteBuf buf = Unpooled.copiedBuffer("testClient".getBytes());
        ChannelFuture write = client.writeAndFlush(buf);
        write.sync();

        sync.channel().closeFuture().sync();
        System.out.println("client end....");

    }

    /*
    服务端：server
     */
    @Test
    public void serverModel() throws InterruptedException {
        NioEventLoopGroup selector = new NioEventLoopGroup(1);

        //192.168.1.204
        NioServerSocketChannel server = new NioServerSocketChannel();
        ChannelPipeline p = server.pipeline();
        p.addLast(new MyAcceptHandle(selector, new AcceptHandle()));//接收 client ，注册client
        selector.register(server);

        ChannelFuture future = server.bind(new InetSocketAddress("192.168.1.204", 9090));

        future.sync().channel().closeFuture().sync();
        System.out.println("server close....");
    }
}

class MyAcceptHandle extends ChannelInboundHandlerAdapter {
    private final NioEventLoopGroup selector;
    private final ChannelHandler handler;

    public MyAcceptHandle(NioEventLoopGroup selector, ChannelHandler handler) {
        this.selector = selector;
        this.handler = handler;
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("server register..");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        SocketChannel client = (SocketChannel) msg;//accept ？ netty做了直接返回client
        //1. 注册
        selector.register(client);
        //2. 响应式 R,W
        ChannelPipeline p = client.pipeline();
        p.addLast(handler);
    }
}

class AcceptHandle extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("register..");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("active....");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
//        CharSequence str = buf.readCharSequence(buf.readableBytes(), CharsetUtil.UTF_8);
        CharSequence str = buf.getCharSequence(0, buf.readableBytes(), CharsetUtil.UTF_8);
        System.out.println(str);
        ctx.writeAndFlush(buf);
    }
}