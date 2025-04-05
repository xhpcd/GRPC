package com.xhpcd.streamTracer;

import io.grpc.Metadata;
import io.grpc.ServerStreamTracer;
import lombok.extern.slf4j.Slf4j;

public class CustomServerStreamFactory extends ServerStreamTracer.Factory {
    @Override
    public ServerStreamTracer newServerStreamTracer(String s, Metadata metadata) {
        return new CustomServerStreamTracer();
    }
}


@Slf4j
class CustomServerStreamTracer extends ServerStreamTracer{
    //inbound


    @Override
    public void inboundMessage(int seqNo) {
        super.inboundMessage(seqNo);
    }

    @Override
    public void inboundWireSize(long bytes) {
        super.inboundWireSize(bytes);
    }



    @Override
    public void inboundMessageRead(int seqNo, long optionalWireSize, long optionalUncompressedSize) {
        log.debug("获取客户端信息消息 {} {} {}", seqNo, optionalWireSize, optionalUncompressedSize);
        super.inboundMessageRead(seqNo, optionalWireSize, optionalUncompressedSize);
    }


    //outbound

    @Override
    public void outboundMessageSent(int seqNo, long optionalWireSize, long optionalUncompressedSize) {
        super.outboundMessageSent(seqNo, optionalWireSize, optionalUncompressedSize);
    }
}
