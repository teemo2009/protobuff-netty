package com.ga.wyc.handler;

import com.ga.wyc.domain.entity.RpcRequest;
import com.ga.wyc.domain.entity.RpcResponse;
import com.ga.wyc.domain.proto.DataBase;
import com.google.common.collect.Maps;
import com.google.protobuf.Any;
import com.google.protobuf.Message;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.Map;

public class RpcClientHandler extends SimpleChannelInboundHandler<Message>{

    Logger logger = LoggerFactory.getLogger(RpcClientHandler.class);


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("客户端发起");
        RpcRequest.MRequest.Builder builder= RpcRequest.MRequest.newBuilder();
        builder.setUrl("/account/login");
        builder.setMessageType(RpcRequest.MRequest.MessageType.BINARY);
        //模拟一个登陆请求
        Map<String,Any> rsMap= Maps.newHashMap();
        DataBase.MString mString=DataBase.MString.newBuilder().setValue("a728ca74cf074fd7b02311618c9b49a5").build();
        rsMap.put("code",Any.pack(mString));
        builder.putAllParams(rsMap);
        RpcRequest.MRequest rpcRequest= builder.build();
        ctx.writeAndFlush(rpcRequest);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Message msg) throws Exception {
        if(msg instanceof  RpcResponse.MResponse){
            System.out.println(((RpcResponse.MResponse) msg).getUrl());
            System.out.println(((RpcResponse.MResponse) msg).getMessage());
        }
    }
}
