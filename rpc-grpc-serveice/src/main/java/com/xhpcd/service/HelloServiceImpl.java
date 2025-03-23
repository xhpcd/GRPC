package com.xhpcd.service;

import com.xhpcd.HelloProto;
import com.xhpcd.HelloServiceGrpc;
import io.grpc.stub.StreamObserver;

public class HelloServiceImpl extends HelloServiceGrpc.HelloServiceImplBase {
    @Override
    public void hello(HelloProto.HelloRequest request, StreamObserver<HelloProto.HelloResponse> responseObserver) {
        super.hello(request, responseObserver);
    }
}
