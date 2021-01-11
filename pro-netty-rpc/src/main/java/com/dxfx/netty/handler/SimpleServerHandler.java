package com.dxfx.netty.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author zzj
 * @version 1.0
 * @date 2021/1/11 21:43
 * @description
 */
public class SimpleServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * 服务端处理客户端到来请求的逻辑
     *
     * @param ctx
     * @param msg
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ctx.channel().writeAndFlush("接收到客户端到来的请求\r\n");
    }

}
