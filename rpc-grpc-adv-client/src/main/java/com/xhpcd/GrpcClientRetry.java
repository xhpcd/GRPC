package com.xhpcd;

import com.google.gson.Gson;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public class GrpcClientRetry {
    public static void main(String[] args) {

        Map<String, ?> serviceConfig = getServiceConfig();
        ManagedChannel channel = ManagedChannelBuilder.forAddress("127.0.0.1", 9000)
                .usePlaintext()
                .defaultServiceConfig(serviceConfig)
                .enableRetry()
                .build();

        HelloServiceGrpc.HelloServiceBlockingStub helloServiceBlockingStub = HelloServiceGrpc.newBlockingStub(channel);
        HelloProto.HelloResponse helloResponse = helloServiceBlockingStub.hello(HelloProto.HelloRequest.newBuilder().setName("xhpcd").build());
        System.out.println("response: " + helloResponse.getResult());

    }

    private static Map<String,?> getServiceConfig(){
        File configFile = new File("/Users/hb33059hsj/JavaCode/GRPC/rpc-grpc-adv-client/src/main/resources/service_config.json");

        Path path = configFile.toPath();

        try {
            byte[] bytes = Files.readAllBytes(path);
            Map map = new Gson().fromJson(new String(bytes), Map.class);
            return map;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
