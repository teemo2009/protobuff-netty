package com.ga.wyc.handler;

import com.ga.wyc.domain.entity.MEasy;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;


public class EasyServerHandler extends SimpleChannelInboundHandler<Message> {
    // log4j日志记录
    Logger logger = LoggerFactory.getLogger(EasyServerHandler.class);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
      logger.info("客户端来了-{}-{}",ctx.channel().id(),
              new SimpleDateFormat("yyyy-MM-dd:HH:mm:ss").format(new Date()));
    }


    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.info("客户端走了-{}-{}",ctx.channel().id(),
                new SimpleDateFormat("yyyy-MM-dd:HH:mm:ss").format(new Date()));
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {
        System.out.println("解析消息成功");
        if(msg instanceof MEasy.Easy){
            System.out.println("解析消息成功");
        }
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close(); //出现异常关闭会话
    }
}
