package com.xhpcd;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.CountDownLatch;

/**
 * 客户端流式rpc
 */
public class GrpcClient5 {
    public static void main(String[] args) {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        ManagedChannel managedChannel = ManagedChannelBuilder.forAddress("localhost", 9000).usePlaintext().build();

        try {
            HelloServiceGrpc.HelloServiceStub helloServiceStub = HelloServiceGrpc.newStub(managedChannel);

            StreamObserver<HelloProto.HelloRequest> helloRequestStreamObserver = helloServiceStub.cs2s(new StreamObserver<HelloProto.HelloResponse>() {
                @Override
                public void onNext(HelloProto.HelloResponse helloResponse) {
                    System.out.println("client received "+helloResponse.getResult());
                }

                @Override
                public void onError(Throwable throwable) {

                }

                @Override
                public void onCompleted() {
                    System.out.println("client reveived completed");
                    countDownLatch.countDown();
                }
            });

            for (int i = 0; i < 10; i++) {
                HelloProto.HelloRequest.Builder builder = HelloProto.HelloRequest.newBuilder();
                builder.setName("xhpcd"+i);
                helloRequestStreamObserver.onNext(builder.build());
                Thread.sleep(500L);
            }
            helloRequestStreamObserver.onCompleted();
            countDownLatch.await();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            managedChannel.shutdown();
        }
    }
}
