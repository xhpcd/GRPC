package com.xhpcd;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;

/**
 * 创建对象的stub使用futureStub
 * 这个future只支持一元rpc
 */
public class GrpcClient7 {
    public static void main(String[] args) {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        ManagedChannel managedChannel = ManagedChannelBuilder.forAddress("localhost", 9000).usePlaintext().build();
        try {
            TestServiceGrpc.TestServiceFutureStub testServiceFutureStub = TestServiceGrpc.newFutureStub(managedChannel);
            ListenableFuture<TestProto.TestResponse> future = testServiceFutureStub.testFuture(TestProto.TestRequest.newBuilder().setName("xhpcd").build());
       /*     future.addListener(()->{
                try {
                    System.out.println("异步线程收到监听"+future.get());
                    countDownLatch.countDown();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                }
            }, Executors.newCachedThreadPool());
            //异步操作
            */
           //异步
            Futures.addCallback(future, new FutureCallback<TestProto.TestResponse>() {
                @Override
                public void onSuccess(TestProto.TestResponse result) {
                    System.out.println(result.getResult());
                    countDownLatch.countDown();
                }

                @Override
                public void onFailure(Throwable t) {

                }
            },Executors.newCachedThreadPool());
            countDownLatch.await();
           // TestProto.TestResponse testResponse = future.get();  同步操作
        }catch (Exception e){
            e.printStackTrace();
        }finally {
             managedChannel.shutdown();
        }
    }
}
