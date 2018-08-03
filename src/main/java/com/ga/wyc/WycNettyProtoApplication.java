package com.ga.wyc;

import io.netty.channel.ChannelFuture;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WycNettyProtoApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(WycNettyProtoApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		MyServerBoot myServerBoot=new MyServerBoot();
		ChannelFuture future =myServerBoot.start();
		Runtime.getRuntime().addShutdownHook(new Thread(){
			@Override
			public void run() {
				myServerBoot.destroy();
			}
		});
		future.channel().closeFuture().syncUninterruptibly();
	}
}
