package com.ga.wyc;


import com.ga.wyc.domain.entity.MEasy;
import com.ga.wyc.domain.entity.RpcRequest;
import com.ga.wyc.handler.EasyServerHandler;
import com.ga.wyc.handler.HeartBeatServerHandler;
import com.ga.wyc.handler.RpcServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

public class EasyServerBoot {
    private static final int port = 7000;
    private final EventLoopGroup bossGroup = new NioEventLoopGroup();
    private final EventLoopGroup workerGroup = new NioEventLoopGroup();
    private Channel channel;
    private Logger log= LoggerFactory.getLogger(EasyServerBoot.class);

    public ChannelFuture  start() throws InterruptedException{
        ServerBootstrap b=new ServerBootstrap();
        ChannelFuture f=null;
        try{
            b.group(bossGroup,workerGroup);
            b.channel(NioServerSocketChannel.class);
            b.localAddress(new InetSocketAddress(port));
            b.childHandler(new ChannelInitializer<SocketChannel>() {//有连接到达时会创建一个channel
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ChannelPipeline pipeline = ch.pipeline();
                    // ----Protobuf处理器，这里的配置是关键----
                    pipeline.addLast("frameDecoder", new ProtobufVarint32FrameDecoder());// 用于decode前解决半包和粘包问题（利用包头中的包含数组长度来识别半包粘包）
                    //配置Protobuf解码处理器，消息接收到了就会自动解码，ProtobufDecoder是netty自带的，Message是自己定义的Protobuf类
                    pipeline.addLast("protobufDecoder",
                            new ProtobufDecoder(MEasy.Easy.getDefaultInstance()));
                    // 用于在序列化的字节数组前加上一个简单的包头，只包含序列化的字节长度。
                    pipeline.addLast("frameEncoder",
                            new ProtobufVarint32LengthFieldPrepender());
                    //配置Protobuf编码器，发送的消息会先经过编码
                    pipeline.addLast("protobufEncoder", new ProtobufEncoder());
                    // ----Protobuf处理器END----
                    pipeline.addLast("handler", new EasyServerHandler());//自己定义的消息处理器，接收消息会在这个类处理
                }
            }).option(ChannelOption.SO_BACKLOG, 128)//最大客户端连接数为128
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
             f = b.bind().syncUninterruptibly();// 配置完成，开始绑定server，通过调用sync同步方法阻塞直到绑定成功
            //System.out.println(PersonServer.class.getName() + " started and listen on " + f.channel().localAddress());
            channel = f.channel();// 应用程序会一直等待，直到channel关闭
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (f != null && f.isSuccess()) {
                System.out.println("ok");
                //log.info("Netty server listening " + address.getHostName() + " on port " + address.getPort() + " and ready for connections...");
            } else {
                System.out.println("error");
               // log.error("Netty server start up Error!");
            }
        }
        return f;
    }

    public void destroy() {
        log.info("Shutdown Netty Server...");
        if(channel != null) { channel.close();}
        workerGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();
        log.info("Shutdown Netty Server Success!");
    }

    public static void main(String[] args) throws Exception {
        new EasyServerBoot().start();
    }
}
