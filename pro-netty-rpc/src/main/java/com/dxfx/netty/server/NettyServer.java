package com.dxfx.netty.server;

import com.dxfx.netty.constant.Constants;
import com.dxfx.netty.factory.ZookeeperFactory;
import com.dxfx.netty.handler.SimpleServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;

import java.net.InetAddress;

/**
 * @author zzj
 * @version 1.0
 * @date 2021/1/11 21:14
 * @description
 */
public class NettyServer {

    public static void main(String[] args) {
        EventLoopGroup parentGroup = new NioEventLoopGroup();
        EventLoopGroup childGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(parentGroup, childGroup);
            bootstrap.option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, false)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        // (4)
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            //解码器 以/r（回车）/n（换行）作为分隔符（拼接符）
                            ch.pipeline().addLast(new DelimiterBasedFrameDecoder(65535, Delimiters.lineDelimiter()[0]));
                            //通过DelimiterBasedFrameDecoder解码后得到的是一串二进制数组 这时通过StringDecoder将二进制数组转换为字符串
                            ch.pipeline().addLast(new StringDecoder());
                            //ch.pipeline().addLast(new IdleStateHandler(60, 45, 20, TimeUnit.SECONDS));
                            ch.pipeline().addLast(new SimpleServerHandler());
                            ch.pipeline().addLast(new StringEncoder());
                        }
                    });

            ChannelFuture f = bootstrap.bind(8080).sync();
            //服务端创建好之后 注册到zookeeper中
            CuratorFramework client = ZookeeperFactory.create();
            InetAddress netAddress = InetAddress.getLocalHost();
            //创建的是临时节点 服务器停止 与zookeeper断开连接时 节点删除
            client.create().withMode(CreateMode.EPHEMERAL)
                    .forPath(Constants.SERVER_PATH + netAddress.getHostAddress());
            //channel的调用是异步的 这里阻塞 等待channel调用close之后(或者发生异常) 主线程停止挂起 继续往下执行
            f.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            parentGroup.shutdownGracefully();
            childGroup.shutdownGracefully();
        }

    }

}
