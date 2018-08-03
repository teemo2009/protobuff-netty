package com.ga.wyc.controller;

import com.ga.wyc.domain.entity.MEasy;
import io.netty.handler.codec.protobuf.ProtobufEncoder;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class MSocket {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("127.0.0.1", 7000);
        OutputStream outputStream = socket.getOutputStream();
        MEasy.Easy e = MEasy.Easy.newBuilder().setId(1).build();
        byte buffer[] = e.toByteArray();
        outputStream.write(e.toByteArray());
        //outputStream.flush();
    }
}
