package com.xhpcd.streamTeacer;

import io.grpc.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomerClientInterceptor  implements ClientInterceptor {
    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(MethodDescriptor<ReqT, RespT> methodDescriptor, CallOptions callOptions, Channel channel) {
        log.debug(" 执行客户端拦截器");

        callOptions = callOptions.withStreamTracerFactory(new CustomClientStreamTeacerFactory());
        return channel.newCall(methodDescriptor,callOptions);
    }
}


class CustomClientStreamTeacerFactory extends ClientStreamTracer.Factory {
    @Override
    public ClientStreamTracer newClientStreamTracer(ClientStreamTracer.StreamInfo info, Metadata headers) {
        return new CustomerClientStreamTracer<>();
    }
}


@Slf4j
class CustomerClientStreamTracer<ReqT,RespT> extends ClientStreamTracer {

    @Override
    public void outboundHeaders() {
        log.debug("client: 用于输出请求头......");
        super.outboundHeaders();
    }

    //outbound 针对客户端请求拦截

    @Override
    public void outboundMessage(int seqNo) {
        log.debug("client 设置流消息的编号{}", seqNo);
        super.outboundMessage(seqNo);
    }

    @Override
    public void outboundUncompressedSize(long bytes) {
        log.debug("client: 未压缩消息的大小{}", bytes);
        super.outboundUncompressedSize(bytes);
    }

    @Override
    public void outboundWireSize(long bytes) {
        log.debug("获得输出消息的大小 {}", bytes);
        super.outboundWireSize(bytes);
    }

    @Override
    public void outboundMessageSent(int seqNo, long optionalWireSize, long optionalUncompressedSize) {
        log.debug("outboundMessageSent no "+ seqNo);
        super.outboundMessageSent(seqNo, optionalWireSize, optionalUncompressedSize);
    }



    //inbound 针对于客户端收到的内容拦截


    @Override
    public void inboundHeaders() {
        log.debug("client获取响应头");
        super.inboundHeaders();
    }

    @Override
    public void inboundMessage(int seqNo) {
        log.debug("获取响应编号{}", seqNo);
        super.inboundMessage(seqNo);
    }

    @Override
    public void inboundWireSize(long bytes) {
        log.debug("获得消息大小{}", bytes);
        super.inboundWireSize(bytes);
    }

    @Override
    public void inboundMessageRead(int seqNo, long optionalWireSize, long optionalUncompressedSize) {
        log.debug("集中获取收到消息信息");
        super.inboundMessageRead(seqNo, optionalWireSize, optionalUncompressedSize);
    }

    @Override
    public void inboundUncompressedSize(long bytes) {
        log.debug("获得响应消息未压缩大小{}", bytes);
        super.inboundUncompressedSize(bytes);
    }

    @Override
    public void inboundTrailers(Metadata trailers) {
        log.debug("响应消息获取结束");
        super.inboundTrailers(trailers);
    }
}
