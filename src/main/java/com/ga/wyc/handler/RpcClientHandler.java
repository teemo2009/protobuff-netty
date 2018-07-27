package com.ga.wyc.handler;

import com.ga.wyc.domain.entity.RpcRequest;
import com.ga.wyc.domain.entity.RpcResponse;
import com.ga.wyc.domain.proto.DataBase;
import com.google.common.collect.Maps;
import com.google.protobuf.Any;
import com.google.protobuf.ByteString;
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
        builder.setClassName("com.ga.wyc.WycNettyProtoApplication");
        builder.setMethod("main");
        builder.setMessageType(RpcRequest.MRequest.MessageType.BINARY);
        //模拟一个登陆请求
        Map<String,Any> rsMap= Maps.newHashMap();
        DataBase.MString username= DataBase.MString.newBuilder().setValue("luqi").build();
        DataBase.MInteger age=DataBase.MInteger.newBuilder().setValue(26).build();
        DataBase.MBoolean bool=DataBase.MBoolean.newBuilder().setValue(true).build();
        DataBase.MDouble money=DataBase.MDouble.newBuilder().setValue(6.22D).build();
        ByteString bytes=ByteString.copyFrom(new byte[]{1});
        DataBase.MByteString btyes=DataBase.MByteString.newBuilder().setValue(bytes).build();
        rsMap.put("username",Any.pack(username));
        rsMap.put("age",Any.pack(age));
        rsMap.put("bool",Any.pack(bool));
        rsMap.put("money",Any.pack(money));
        rsMap.put("bytes",Any.pack(btyes));
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
            RpcResponse.MResponse  mResponse= (RpcResponse.MResponse) msg;
            logger.info("我来自服务端,code={},message={},resultType={}",mResponse.getCode(),
                    mResponse.getMessage(),mResponse.getResultType());
            Map<String,Any> rsMap= mResponse.getResultMap();
            DataBase.MString username= rsMap.get("username").unpack(DataBase.MString.class);
            logger.info("来自服务端:{}",username.getValue());
            DataBase.MList list=rsMap.get("list").unpack(DataBase.MList.class);
            for (Any any:list.getValueList()){
                logger.info("来自服务端列表:{}",any.unpack(DataBase.MString.class).getValue());
            }
        }
    }
}
