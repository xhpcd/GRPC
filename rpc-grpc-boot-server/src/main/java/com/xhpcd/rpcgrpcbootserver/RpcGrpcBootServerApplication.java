package com.xhpcd.rpcgrpcbootserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com")
public class RpcGrpcBootServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(RpcGrpcBootServerApplication.class, args);
    }

}
