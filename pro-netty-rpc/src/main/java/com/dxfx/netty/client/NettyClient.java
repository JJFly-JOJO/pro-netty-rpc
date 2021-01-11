package com.dxfx.netty.client;

import com.dxfx.netty.handler.SimpleClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.AttributeKey;

/**
 * @author zzj
 * @version 1.0
 * @date 2021/1/11 22:28
 * @description
 */
public class NettyClient {

    public static void main(String[] args) {
        String host="localhost";
        int port=8080;
        EventLoopGroup workerGroup=new NioEventLoopGroup();
        try {
            Bootstrap b=new Bootstrap();
            b.group(workerGroup)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE,true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new DelimiterBasedFrameDecoder(Integer.MAX_VALUE, Delimiters.lineDelimiter()[0]));
                            ch.pipeline().addLast(new StringDecoder());
                            ch.pipeline().addLast(new SimpleClientHandler());
                            ch.pipeline().addLast(new StringEncoder());
                        }
                    });
            //开启客户端 这里同步阻塞 等待客户端连接成功
            ChannelFuture f=b.connect(host,port).sync();
            f.channel().writeAndFlush("hello server"+"\r\n");
            //等待客户端另一个线程执行完服务端发送回的数据的逻辑 并且调用channel.close()之后 当前阻塞线程被唤醒
            f.channel().closeFuture().sync();
            Object res=f.channel().attr(AttributeKey.valueOf("msg")).get();
            System.out.println("获取到服务端发送回的数据->"+res.toString());
            //成功后 客户端向服务端发送数据
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            workerGroup.shutdownGracefully();
        }
    }

}
