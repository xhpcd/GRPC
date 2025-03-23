package com.xhpcd.service;

import com.xhpcd.TestProto;
import com.xhpcd.TestServiceGrpc;
import io.grpc.stub.StreamObserver;

public class TestServiceImpl extends TestServiceGrpc.TestServiceImplBase {
    @Override
    public void testFuture(TestProto.TestRequest request, StreamObserver<TestProto.TestResponse> responseObserver) {
        responseObserver.onNext(TestProto.TestResponse.newBuilder().setResult("server receive "+request.getName()).build());
        responseObserver.onCompleted();
    }
}
