package com.xhpcd.interceptor;

import io.grpc.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomServerInterceptor implements ServerInterceptor {
    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> serverCall, Metadata metadata, ServerCallHandler<ReqT, RespT> serverCallHandler) {
        log.debug("服务端拦截器生效");
        // serverCallHandler.startCall(serverCall,metadata) 仅仅能完成接收到数据的监听

        // return new CustomServerCallListener<>(serverCallHandler.startCall(serverCall,metadata)); 仅仅只包装了listener只能处理接受数据

        //1. 包装servercall 处理服务端响应数据
        CustomServerCall<ReqT, RespT> reqTRespTCustomServerCall = new CustomServerCall<>(serverCall);
        //2. 包装linstener 处理服务端拦截客户端请求
        return new CustomServerCallListener<>(serverCallHandler.startCall(reqTRespTCustomServerCall,metadata));
    }
}

//处理服务端接收的数据
@Slf4j
class CustomServerCallListener<ReqT> extends ForwardingServerCallListener.SimpleForwardingServerCallListener<ReqT>{

    protected CustomServerCallListener(ServerCall.Listener<ReqT> delegate) {
        super(delegate);
    }

    @Override
    public void onReady() {
        log.debug("onready ...");
        super.onReady();
    }

    @Override
    public void onMessage(ReqT message) {
        log.debug("onmessage ...");
        super.onMessage(message);
    }

    @Override
    public void onHalfClose() {
        log.debug("onhalfclose ...");
        super.onHalfClose();
    }

    @Override
    public void onComplete() {
        log.debug("服务端调用 onComplete");
        super.onComplete();
    }

    @Override
    public void onCancel() {
        log.debug("出现异常后调用");
        super.onCancel();
    }
}

//针对服务端 响应数据做处理
@Slf4j
class CustomServerCall<ReqT,RespT> extends ForwardingServerCall.SimpleForwardingServerCall<ReqT,RespT>{
    protected CustomServerCall(ServerCall<ReqT, RespT> delegate) {
        super(delegate);
    }

    @Override
    // 指定发送信息的数量
    public void request(int numMessages) {
        log.info("服务端设置 响应消息指定数量");
        super.request(numMessages);
    }

    @Override
    public void sendHeaders(Metadata headers) {
        log.info("response 设置响应头");
        super.sendHeaders(headers);
    }

    @Override
    public void sendMessage(RespT message) {
        log.info("响应数据");
        super.sendMessage(message);
    }

    @Override
    public void close(Status status, Metadata trailers) {
        log.info("关闭连接");
        super.close(status, trailers);
    }
}

