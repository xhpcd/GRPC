package com.xhpcd.interceptor;

import io.grpc.*;
import lombok.extern.slf4j.Slf4j;

/**
 * 简单一元客户端拦截器
 * 现存问题 只能拦截客户端请求数据不能拦截响应数据
 * 1. 开始阶段 2. 设置消息数量 3. 发送数据阶段 4. 半连接阶段
 * 不能分阶段拦截
 */

@Slf4j
public class CustomClientInterceptor implements ClientInterceptor {
    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(MethodDescriptor<ReqT, RespT> methodDescriptor, CallOptions callOptions, Channel channel) {
        log.debug("客户端拦截器前 进行操作");
        //简单拦截器
        ClientCall<ReqT, RespT> simpleReqTRespTClientCall = channel.newCall(methodDescriptor, callOptions);

        //复杂拦截器
        return new CustomForwardingClientCall<>(simpleReqTRespTClientCall);
    }
}

// 此类主要用于拦截客户端发送数据
@Slf4j
class CustomForwardingClientCall<ReqT, RespT> extends ClientInterceptors.CheckedForwardingClientCall<ReqT, RespT> {

    CustomForwardingClientCall(ClientCall<ReqT, RespT> delegate) {
        super(delegate);
    }

    @Override
    // 请求开始阶段 会回调此方法 目的： 检查此次rpc是否可以被发起
    protected void checkedStart(Listener<RespT> responseListener, Metadata metadata) throws Exception {
      log.debug("发送请求数据之前 进行方法检查");
      //检查完毕允许发送
//      delegate().start(responseListener,metadata);

        delegate().start(new CustomCallListener<>(responseListener),metadata);
    }

    // 指定发送消息数量
    @Override
    public void request(int numMessages) {
        log.debug("request 方法被调用");
        super.request(numMessages);
    }

    @Override
    // 讲数据写入缓冲区
    public void sendMessage(ReqT message) {
        log.debug("sendMessage 方法被调用");
        super.sendMessage(message);
    }

    @Override
    // 半连接状态 不能发送请求 只能收到响应
    public void halfClose() {
        log.debug(" halfClose 方法被调用");
        super.halfClose();
    }
}

//此类处理客户端接受的响应数据
@Slf4j
class CustomCallListener<RespT> extends ForwardingClientCallListener.SimpleForwardingClientCallListener<RespT> {
    CustomCallListener(ClientCall.Listener<RespT> delegate) {
        super(delegate);
    }

    @Override
    public void onHeaders(Metadata headers) {
        log.info("响应头已收到 "+headers.toString());
        super.onHeaders(headers);
    }

    @Override
    public void onMessage(RespT message) {
        log.info("响应数据收到");
        super.onMessage(message);
    }
}