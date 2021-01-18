package com.dxfx.netty.init;

import com.dxfx.netty.handler.ServerHandler;
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
import io.netty.handler.timeout.IdleStateHandler;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author zzj
 * @version 1.0
 * @date 2021/1/18 20:00
 * @description
 */
@Component

public class NettyInital implements ApplicationListener<ContextRefreshedEvent> {

    public void start() {
        EventLoopGroup parentGroup = new NioEventLoopGroup();
        EventLoopGroup childGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(parentGroup, childGroup);
            bootstrap.option(ChannelOption.SO_BACKLOG, 128)
                    //关闭TCP层的心跳机制 TCP层的心跳包是由服务器定时发送给客户端 查看是否存活的
                    //此心跳包两小时发送一次 时间过长
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
                            //设置心跳参数
                            ch.pipeline().addLast(new IdleStateHandler(60, 45, 20, TimeUnit.SECONDS));
                            ch.pipeline().addLast(new ServerHandler());
                            ch.pipeline().addLast(new StringEncoder());
                        }
                    });

            ChannelFuture f = bootstrap.bind(8080).sync();
            //服务端创建好之后 注册到zookeeper中
            /*CuratorFramework client = ZookeeperFactory.create();
            InetAddress netAddress = InetAddress.getLocalHost();
            //创建的是临时节点 服务器停止 与zookeeper断开连接时 节点删除
            client.create().withMode(CreateMode.EPHEMERAL)
                    .forPath(Constants.SERVER_PATH + netAddress.getHostAddress());*/
            //channel的调用是异步的 这里阻塞 等待channel调用close之后(或者发生异常) 主线程停止挂起 继续往下执行
            f.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            parentGroup.shutdownGracefully();
            childGroup.shutdownGracefully();
        }
    }

    /**
     * 监听Spring容器创建完成（refresh）后的事件，此时启动服务器
     *
     * @param event
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        this.start();
    }
}
