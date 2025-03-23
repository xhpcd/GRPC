package com.xhpcd;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class GrpcClient1 {
    public static void main(String[] args) {
        // 1. 创建通信管道
        ManagedChannel managedChannel = ManagedChannelBuilder.forAddress("127.0.0.1", 9000).usePlaintext().build();
        // 2. 获取代理对象 stub
        try {
            HelloServiceGrpc.HelloServiceBlockingStub helloServiceBlockingStub = HelloServiceGrpc.newBlockingStub(managedChannel);
            // 3. 完成RPC调用
            // 3.1 准备参数
            HelloProto.HelloRequest.Builder helloRequestBuilder = HelloProto.HelloRequest.newBuilder();
            helloRequestBuilder.setName("xhpcd");
            HelloProto.HelloRequest helloRequest = helloRequestBuilder.build();
            // 3.2 完成调用
            HelloProto.HelloResponse helloResponse = helloServiceBlockingStub.hello(helloRequest);
            System.out.println("result " + helloResponse.getResult());
        }catch (Exception e){
            throw new RuntimeException(e);
        }finally {
            managedChannel.shutdown();
        }

    }
}
