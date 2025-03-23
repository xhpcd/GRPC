package com.xhpcd.service;

import com.xhpcd.HelloProto;
import com.xhpcd.HelloServiceGrpc;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class HelloServeiveImpl extends HelloServiceGrpc.HelloServiceImplBase {
    @Override
    public void hello(HelloProto.HelloRequest request, StreamObserver<HelloProto.HelloResponse> responseObserver) {
        String name = request.getName();
        System.out.println("server received: "+name);
        responseObserver.onNext(HelloProto.HelloResponse.newBuilder().setResult("i am server").build());
        responseObserver.onCompleted();
    }
}
