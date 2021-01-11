package com.dxfx.netty.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.AttributeKey;

/**
 * @author zzj
 * @version 1.0
 * @date 2021/1/11 22:33
 * @description
 */
public class SimpleClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)throws Exception{
        //接收获取到服务端到来的的msg 将msg放入channel中共享的一个map中 key为“msg”
        ctx.channel().attr(AttributeKey.valueOf("msg")).set(msg);
        //注意这里的执行链是异步的 也就是说 当通道channel close之后 会通知主线程（如果主线程等待close时间而阻塞）
        ctx.channel().close();
    }

}
