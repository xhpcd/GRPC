package com.xhpcd;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class GrpcClient2 {
    public static void main(String[] args) {
        // 1. 创建通信管道
        ManagedChannel managedChannel = ManagedChannelBuilder.forAddress("127.0.0.1", 9000).usePlaintext().build();
        // 2. 获取代理对象 stub
        try {
            HelloServiceGrpc.HelloServiceBlockingStub helloServiceBlockingStub = HelloServiceGrpc.newBlockingStub(managedChannel);
            // 3. 完成RPC调用
            // 3.1 准备参数
            HelloProto.HelloRequest1.Builder helloRequestBuilder = HelloProto.HelloRequest1.newBuilder();
            helloRequestBuilder.addName("k1");
            helloRequestBuilder.addName("k2");
            HelloProto.HelloRequest1 helloRequest = helloRequestBuilder.build();
            // 3.2 完成调用
            HelloProto.HelloResponse1 helloResponse = helloServiceBlockingStub.hello1(helloRequest);
            System.out.println("result " + helloResponse.getResult());
        }catch (Exception e){
            throw new RuntimeException(e);
        }finally {
            managedChannel.shutdown();
        }

    }
}
