package com.xhpcd;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.CountDownLatch;

/**
 * 异步处理来自服务端的信息
 */

public class GrpcClient4 {
    public static void main(String[] args) throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        ManagedChannel managedChannel = ManagedChannelBuilder.forAddress("localhost", 9000).usePlaintext().build();
        try {
            HelloServiceGrpc.HelloServiceStub helloServiceStub = HelloServiceGrpc.newStub(managedChannel);
            HelloProto.HelloRequest.Builder helloRequestBuilder = HelloProto.HelloRequest.newBuilder();
            HelloProto.HelloRequest helloRequest = helloRequestBuilder.setName("xhpcd").build();
            helloServiceStub.c2ss(helloRequest, new StreamObserver<HelloProto.HelloResponse>() {
                @Override
                public void onNext(HelloProto.HelloResponse helloResponse) {
                    System.out.println("received response: "+ helloResponse.getResult());
                }

                @Override
                public void onError(Throwable throwable) {

                }

                @Override
                public void onCompleted() {
                    System.out.println("server completed");
                    countDownLatch.countDown();
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            countDownLatch.await();
            managedChannel.shutdown();
        }
    }
}
