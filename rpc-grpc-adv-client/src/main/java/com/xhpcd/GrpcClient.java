package com.xhpcd;

import com.xhpcd.interceptor.CustomClientInterceptor;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class GrpcClient {
    public static void main(String[] args) {
        ManagedChannel managedChannel = ManagedChannelBuilder.forAddress("localhost", 9000)
                .usePlaintext()
                .intercept(new CustomClientInterceptor())
                .build();
        try {
            HelloServiceGrpc.HelloServiceBlockingStub helloServiceBlockingStub = HelloServiceGrpc.newBlockingStub(managedChannel);
            HelloProto.HelloResponse helloResponse = helloServiceBlockingStub.hello(HelloProto.HelloRequest.newBuilder().setName("xhpcd").build());
            String result = helloResponse.getResult();
            System.out.println("result="+result);

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            managedChannel.shutdown();
        }
    }
}
