package com.xhpcd.controller;


import com.xhpcd.HelloProto;
import com.xhpcd.HelloServiceGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GrpcClient("grpc-server")
    private HelloServiceGrpc.HelloServiceBlockingStub helloServiceBlockingStub;

    @RequestMapping("/grpc")
    private String test(String name){
        HelloProto.HelloResponse helloResponse = helloServiceBlockingStub.hello(HelloProto.HelloRequest.newBuilder().setName(name).build());
        return helloResponse.getResult();
    }

}
