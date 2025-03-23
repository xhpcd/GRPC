package com.xhpcd;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.Iterator;

/**
 * 此种阻塞方式存在问题 导致代码只是同步接收返回值而不能做其它事情
 */
public class GrpcClient3 {
    public static void main(String[] args) {
        ManagedChannel managedChannel = ManagedChannelBuilder.forAddress("localhost", 9000).usePlaintext().build();
        try {
            HelloServiceGrpc.HelloServiceBlockingStub helloServiceBlockingStub = HelloServiceGrpc.newBlockingStub(managedChannel);
            HelloProto.HelloRequest.Builder helloRequestBuilder = HelloProto.HelloRequest.newBuilder();
            helloRequestBuilder.setName("xhpcd");
            HelloProto.HelloRequest helloRequest = helloRequestBuilder.build();
            Iterator<HelloProto.HelloResponse> helloResponseIterator = helloServiceBlockingStub.c2ss(helloRequest);
            while (helloResponseIterator.hasNext()){
                HelloProto.HelloResponse helloResponse = helloResponseIterator.next();
                System.out.println("helloResponse.getResult() ="+helloResponse.getResult());
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            managedChannel.shutdown();
        }
    }
}
