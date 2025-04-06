package com.xhpcd.service;


import com.xhpcd.HelloProto;
import com.xhpcd.HelloServiceGrpc;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;

import java.util.Random;

@Slf4j
public class HelloServiceImpl extends HelloServiceGrpc.HelloServiceImplBase {
    private final Random random = new Random();

    @Override
    public void hello(HelloProto.HelloRequest request, StreamObserver<HelloProto.HelloResponse> responseObserver) {
        if(random.nextInt(100)>50) {
            System.out.println("name=" + request.getName());
            responseObserver.onNext(HelloProto.HelloResponse.newBuilder().setResult(" this is server").build());
            responseObserver.onCompleted();
        }else {
            //模拟异常
            log.debug("不可达异常");
            responseObserver.onError(Status.UNAVAILABLE.withDescription("for retry").asRuntimeException());
        }
    }

    @Override
    public StreamObserver<HelloProto.HelloRequest> hello1(StreamObserver<HelloProto.HelloResponse> responseObserver) {
        return new StreamObserver<HelloProto.HelloRequest>() {
            @Override
            public void onNext(HelloProto.HelloRequest helloRequest) {
                log.debug("request="+helloRequest.getName());
            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onCompleted() {
                log.debug("request completed");
                responseObserver.onNext(HelloProto.HelloResponse.newBuilder().setResult("response 1").build());
                responseObserver.onNext(HelloProto.HelloResponse.newBuilder().setResult("response 2").build());
                responseObserver.onCompleted();
            }
        };
    }
}
