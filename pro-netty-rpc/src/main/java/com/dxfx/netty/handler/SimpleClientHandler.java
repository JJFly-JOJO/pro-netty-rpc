package com.dxfx.netty.handler;

import com.alibaba.fastjson.JSONObject;
import com.dxfx.netty.client.DefaultFuture;
import com.dxfx.netty.util.MyResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author zzj
 * @version 1.0
 * @date 2021/1/11 22:33
 * @description --------------该handler执行是在worker线程中 而不是在main线程中--------------
 */
public class SimpleClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if ("ping".equals(msg.toString())) {
            //说明当前消息为服务端的心跳包
            ctx.channel().writeAndFlush("ping\r\n");
            return;
        }
        //如果不是心跳包 则将接收到的msg转换为MyResponse对象
        MyResponse response = JSONObject.parseObject(msg.toString(), MyResponse.class);
        DefaultFuture.recieve(response);
        //System.out.println(msg.toString());
        //接收获取到服务端到来的的msg 将msg放入channel中共享的一个map中 key为“msg”
        //ctx.channel().attr(AttributeKey.valueOf("msg")).set(msg);
        //注意这里的执行链是异步的 也就是说 当通道channel close之后 会通知主线程（如果主线程等待close事件而阻塞）
        //ctx.channel().close();
    }

}
