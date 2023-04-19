package com.lmzz.system.io.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
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
    nc -l 192.168.208.130 9090
    nc 192.168.1.204 9090
     */

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

    //client demo
    @Test
    public void clientModel() throws InterruptedException {
        NioEventLoopGroup selector = new NioEventLoopGroup(1);

        NioSocketChannel client = new NioSocketChannel();
        selector.register(client);

        //io 事件第一，Rw第二
        ChannelPipeline pipeline = client.pipeline();
        pipeline.addLast(new AcceptInHandle());

        ChannelFuture connect = client.connect(new InetSocketAddress("192.168.208.130", 9090));
        ChannelFuture sync = connect.sync();

        ByteBuf buf = Unpooled.copiedBuffer("testClient".getBytes());
        ChannelFuture write = client.writeAndFlush(buf);
        write.sync();

        sync.channel().closeFuture().sync();
        System.out.println("client end....");
    }

    //netty client
    @Test
    public void nettyClient() throws InterruptedException {
        NioEventLoopGroup group = new NioEventLoopGroup(1);
        Bootstrap bs = new Bootstrap();
        ChannelFuture future = bs.group(group)
                .channel(NioSocketChannel.class)
//                .handler(new InnerHandle())
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ch.pipeline().addLast(new AcceptInHandle());
                    }
                })
                .connect(new InetSocketAddress("192.168.208.130", 9090));
        Channel client = future.sync().channel();
        ByteBuf buf = Unpooled.copiedBuffer("testClient".getBytes());
        ChannelFuture write = client.writeAndFlush(buf);
        write.sync();

        client.closeFuture().sync();
        System.out.println("client end....");
    }


    //server demo
    @Test
    public void serverModel() throws InterruptedException {
        NioEventLoopGroup selector = new NioEventLoopGroup(1);
        //192.168.1.204
        NioServerSocketChannel server = new NioServerSocketChannel();
        selector.register(server);

        ChannelPipeline p = server.pipeline();
        //p.addLast(new MyAcceptHandle(selector, new AcceptInHandle()));//接收 client ，注册client
        //封装 InnerHandle 包装 MyInHandle，用中间类防止自定义实现要单利
        p.addLast(new MyAcceptHandle(selector, new InnerHandle()));//接收 client ，注册client

        ChannelFuture future = server.bind(new InetSocketAddress("192.168.1.204", 9090));
        future.sync().channel().closeFuture().sync();
        System.out.println("server close....");
    }

    //netty server
    @Test
    public void nettyServer() throws InterruptedException {
        NioEventLoopGroup group = new NioEventLoopGroup(1);
        ServerBootstrap sbs = new ServerBootstrap();
        ChannelFuture future = sbs.group(group)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new AcceptInHandle());
                    }
                })
                .bind(new InetSocketAddress("192.168.1.204", 9090));
        future.sync().channel().closeFuture().sync();
    }
}

//----------------------- inner class ----------

class MyAcceptHandle extends ChannelInboundHandlerAdapter {
    private final NioEventLoopGroup selector;
    private final ChannelHandler handler;

    public MyAcceptHandle(NioEventLoopGroup selector, ChannelHandler handler) {
        this.selector = selector;
        this.handler = handler;
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) {
        System.out.println("server register..");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        SocketChannel client = (SocketChannel) msg;//accept ？ netty做了直接返回client
        //1. 注册
        //2. 响应式 R,W
        ChannelPipeline p = client.pipeline();//add [InnerHandle]
        p.addLast(handler);
        selector.register(client);
    }
}


@ChannelHandler.Sharable
class InnerHandle extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRegistered(ChannelHandlerContext ctx) {
        Channel client = ctx.channel();
        ChannelPipeline p = client.pipeline();
        p.addLast(new AcceptInHandle()); //add [InnerHandle,AcceptInHandle]
        ctx.pipeline().remove(this);// [AcceptInHandle]
    }
}

//@ChannelHandler.Sharable
class AcceptInHandle extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) {
        System.out.println("client register..");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println("client active....");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf buf = (ByteBuf) msg;
//        CharSequence str = buf.readCharSequence(buf.readableBytes(), CharsetUtil.UTF_8);
        CharSequence str = buf.getCharSequence(0, buf.readableBytes(), CharsetUtil.UTF_8);
        System.out.println(str);
        ctx.writeAndFlush(buf);
    }
}