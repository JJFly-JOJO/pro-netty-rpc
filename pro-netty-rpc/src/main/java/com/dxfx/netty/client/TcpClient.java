package com.dxfx.netty.client;

import com.alibaba.fastjson.JSONObject;
import com.dxfx.netty.handler.SimpleClientHandler;
import com.dxfx.netty.util.MyResponse;
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

/**
 * @author zzj
 * @version 1.0
 * @date 2021/1/12 21:40
 * @description -------------客户端TCP长连接-----------
 */
public class TcpClient {

    static final Bootstrap b = new Bootstrap();
    static ChannelFuture f = null;

    static {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        b.group(workerGroup); // (2)
        b.channel(NioSocketChannel.class); // (3)
        b.option(ChannelOption.SO_KEEPALIVE, true); // (4)
        b.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(new DelimiterBasedFrameDecoder(Integer.MAX_VALUE, Delimiters.lineDelimiter()[0]));
                ch.pipeline().addLast(new StringDecoder());
                ch.pipeline().addLast(new SimpleClientHandler());
                ch.pipeline().addLast(new StringEncoder());
            }
        });
        String host = "localhost";
        int port = 8080;
        try {
            f = b.connect(host, port).sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } // (5)
    }

    /**
     * 调用send方法，并且阻塞等待response，这里注意调用send方法的线程与worker线程中执行ClientHandler（处理逻辑是接收response
     * 并且赋值到对应的DefaultFuture属性中）不是同一个线程
     *
     * JSON实现了对List集合的序列化
     * @param request
     * @return
     */
    public static MyResponse send(ClientRequest request) {
        f.channel().writeAndFlush(JSONObject.toJSONString(request));
        f.channel().writeAndFlush("\r\n");
        DefaultFuture df = new DefaultFuture(request);
        //调用当前send方法的线程会由于get()发生阻塞 知道DefaultFuture中response的数据由另外的线程调用receive获得
        return df.get();
    }
}
