package com.ga.wyc.handler;

import com.ga.wyc.domain.entity.RpcRequest;
import com.ga.wyc.domain.entity.RpcResponse;
import com.ga.wyc.domain.proto.AccountRequest;
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


public class RpcServerHandler extends SimpleChannelInboundHandler<Message> {
    // log4j日志记录
    Logger logger = LoggerFactory.getLogger(RpcServerHandler.class);

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
        if(msg instanceof RpcRequest.MRequest){
         /*   if (msg.toString().equals("")){
                ctx.close();
                return;
            }*/
            RpcRequest.MRequest  mRequest= (RpcRequest.MRequest) msg;
            System.out.println("34234234");
            /*logger.info("ClassName:{},Method:{},MessageType:{}",
                    mRequest.getClassName(),mRequest.getMethod(),mRequest.getMessageType());*/
            Map<String,Any> rsMap= mRequest.getParamsMap();
             logger.info("username:{}",rsMap.get("username").unpack(DataBase.MString.class));
            logger.info("age:{}",rsMap.get("age").unpack(DataBase.MInteger.class));
            logger.info("mbool:{}",rsMap.get("mbool").unpack(DataBase.MBoolean.class));
            logger.info("money:{}",rsMap.get("money").unpack(DataBase.MDouble.class));
            logger.info("bytes:{}",rsMap.get("bytes").unpack(DataBase.MByteString.class));
            //返回
            RpcResponse.MResponse mResponse=back();
            ctx.writeAndFlush(mResponse);

        }
    }


    private RpcResponse.MResponse back(){
        RpcResponse.MResponse.Builder mResponseBuilder=RpcResponse.MResponse.newBuilder();
        mResponseBuilder.setCode(RpcResponse.MResponse.Code.SUCCESS);
        mResponseBuilder.setMessage("请求成功");
        mResponseBuilder.setResultType(RpcResponse.MResponse.ResultType.BINARY);
        Map<String,Any> rsMap= Maps.newHashMap();
        DataBase.MList.Builder listBuilder=DataBase.MList.newBuilder();
        for (int i=0;i<10;i++){
            listBuilder.addValue(Any.pack(DataBase.MString.newBuilder().setValue("index"+i).build()));
        }
        rsMap.put("username",Any.pack(DataBase.MString.newBuilder().setValue("匿名").build()));
        rsMap.put("list",Any.pack(listBuilder.build()));
        mResponseBuilder.putAllResult(rsMap);
        return mResponseBuilder.build();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close(); //出现异常关闭会话
    }
}
