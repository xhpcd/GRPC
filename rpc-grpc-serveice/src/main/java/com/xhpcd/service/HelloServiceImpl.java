package com.xhpcd.service;

import com.google.protobuf.ProtocolStringList;
import com.xhpcd.HelloProto;
import com.xhpcd.HelloServiceGrpc;
import io.grpc.stub.StreamObserver;

/**
 * 一元rpc。一个请求一个响应
 */
public class HelloServiceImpl extends HelloServiceGrpc.HelloServiceImplBase {
    @Override
    public void hello(HelloProto.HelloRequest request, StreamObserver<HelloProto.HelloResponse> responseObserver) {
        //1. 获取参数
        String name = request.getName();
        //2. 逻辑处理
        System.out.println("name parameter "+name);
        //3.封装响应
        //3.1 创建响应对象的构建者
        HelloProto.HelloResponse.Builder builder = HelloProto.HelloResponse.newBuilder();
        //3.2 数据填充
        builder.setResult("hello method invoke ok "+name);
        //3.3 封装响应
        HelloProto.HelloResponse helloResponse = builder.build();

        responseObserver.onNext(helloResponse);
        // 通知客户端已经发送完成
        responseObserver.onCompleted();

    }

    @Override
    public void hello1(HelloProto.HelloRequest1 request, StreamObserver<HelloProto.HelloResponse1> responseObserver) {
        ProtocolStringList nameList = request.getNameList();
        for (String s : nameList) {
            System.out.println("s = "+s);
        }
        HelloProto.HelloResponse1.Builder builder = HelloProto.HelloResponse1.newBuilder();
        builder.setResult("ok");
        HelloProto.HelloResponse1 helloResponse1 = builder.build();

        responseObserver.onNext(helloResponse1);
        responseObserver.onCompleted();
    }

    // 服务端流式rpc  服务端可以分时间传递多个结果
    @Override
    public void c2ss(HelloProto.HelloRequest request, StreamObserver<HelloProto.HelloResponse> responseObserver) {
        String name = request.getName();
        System.out.println("name "+name);
        for (int i = 0; i < 9; i++) {
            HelloProto.HelloResponse.Builder builder = HelloProto.HelloResponse.newBuilder();
            builder.setResult("处理结果 "+i);
            HelloProto.HelloResponse helloResponse = builder.build();
            responseObserver.onNext(helloResponse);
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        responseObserver.onCompleted();

    }
    //客户端流式rpc 常用于IOT平台


    @Override
    public StreamObserver<HelloProto.HelloRequest> cs2s(StreamObserver<HelloProto.HelloResponse> responseObserver) {
        return new StreamObserver<HelloProto.HelloRequest>() {
            @Override
            public void onNext(HelloProto.HelloRequest helloRequest) {
                System.out.println("server received request "+helloRequest.getName());
                //如果针对于每一个客户端都响应可以在此调用onNext
            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onCompleted() {
                //说明客户端已经完成所有发送
                //当接收所有信息后提供响应
                HelloProto.HelloResponse.Builder builder = HelloProto.HelloResponse.newBuilder();
                builder.setResult("ok");
                responseObserver.onNext(builder.build());
                responseObserver.onCompleted();
            }
        };
    }
}
