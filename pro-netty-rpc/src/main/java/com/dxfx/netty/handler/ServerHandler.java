package com.dxfx.netty.handler;

import com.alibaba.fastjson.JSONObject;
import com.dxfx.netty.handler.param.ServerRequest;
import com.dxfx.netty.medium.Mediu;
import com.dxfx.netty.util.MyResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * @author zzj
 * @version 1.0
 * @date 2021/1/18 21:30
 * @description
 */
public class ServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //解析请求
        ServerRequest request = JSONObject.parseObject(msg.toString(), ServerRequest.class);
        //将请求交给中介者集中处理 这里不存在线程安全问题
        Mediu mediu = Mediu.newInstance();
        MyResponse response = mediu.process(request);
        ctx.channel().writeAndFlush(JSONObject.toJSONString(response) + "\r\n");
    }

    /**
     * 处理监听到的事件 这里针对IdleStateHandler发送的事件实现心跳包活
     *
     * @param ctx
     * @param evt
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            //读空闲 根据设定的读空闲时间 当达到设定时间之后 如果服务器没有调用过read()方法 那么就触发该事件
            // 此时我们认定客户端没有数据发送了 关闭服务端连接
            if (event.state().equals(IdleState.READER_IDLE)) {
                System.out.println("读空闲...");
                ctx.channel().close();
            }
            if (event.state().equals(IdleState.WRITER_IDLE)) {
                System.out.println("写空闲...");
            }
            //读写空闲 发送心跳包
            if (event.state().equals(IdleState.ALL_IDLE)) {
                System.out.println("读写空闲...");
                ctx.channel().writeAndFlush("ping\r\n");
            }
        }
    }

}
