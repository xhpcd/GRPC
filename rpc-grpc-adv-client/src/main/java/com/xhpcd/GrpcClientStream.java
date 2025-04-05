package com.xhpcd;

import com.xhpcd.interceptor.CustomClientInterceptor;
import com.xhpcd.streamTeacer.CustomerClientInterceptor;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;

@Slf4j
public class GrpcClientStream {
    public static void main(String[] args) {
        ManagedChannel managedChannel = ManagedChannelBuilder.forAddress("localhost", 9000)
                .usePlaintext()
                .intercept(new CustomerClientInterceptor())
                .build();
        CountDownLatch countDownLatch = new CountDownLatch(1);
        try {
            HelloServiceGrpc.HelloServiceStub helloServiceStub = HelloServiceGrpc.newStub(managedChannel);
            StreamObserver<HelloProto.HelloRequest>  streamObserver = helloServiceStub.hello1(new StreamObserver<HelloProto.HelloResponse>() {
                @Override
                public void onNext(HelloProto.HelloResponse helloResponse) {
                    log.debug("client received hello response: {}", helloResponse.getResult());
                }

                @Override
                public void onError(Throwable throwable) {

                }

                @Override
                public void onCompleted() {
                    log.debug("client received completed");
                    countDownLatch.countDown();
                }
            });
            for (int i = 0; i < 3; i++) {
                streamObserver.onNext(HelloProto.HelloRequest.newBuilder().setName("xhpcd"+i).build());
            }
            streamObserver.onCompleted();
            countDownLatch.await();

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            managedChannel.shutdown();
        }
    }
}
