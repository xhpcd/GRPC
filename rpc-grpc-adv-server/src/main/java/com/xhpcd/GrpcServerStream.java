package com.xhpcd;

import com.xhpcd.interceptor.CustomServerInterceptor;
import com.xhpcd.service.HelloServiceImpl;
import com.xhpcd.streamTracer.CustomServerStreamFactory;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class GrpcServerStream {
    public static void main(String[] args) throws IOException, InterruptedException {
        //1. 绑定端口
        ServerBuilder<?> serverBuilder = ServerBuilder.forPort(9000);
        serverBuilder.addStreamTracerFactory(new CustomServerStreamFactory());
        //2. 发布服务
        serverBuilder.addService(new HelloServiceImpl());
        //3. 创建服务对象
        Server server = serverBuilder.build();

        server.start();
        server.awaitTermination();
    }
}
