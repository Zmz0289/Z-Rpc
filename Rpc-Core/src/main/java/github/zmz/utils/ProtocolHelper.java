package github.zmz.utils;

import github.zmz.constant.Constants;
import github.zmz.protocol.ProtocolData;
import github.zmz.protocol.ProtocolHeader;
import github.zmz.protocol.RpcProtocol;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.buffer.impl.BufferImpl;

import java.util.Date;

public class ProtocolHelper {

    public static RpcProtocol generate(ProtocolData protocolData, int bodyLength) {
        RpcProtocol protocol = new RpcProtocol();
        ProtocolHeader header = new ProtocolHeader();

        header.setVersion(Constants.currentProtocolVersion);
        header.setTimestamp(new Date().getTime());
        header.setBodyLength(bodyLength);
        protocol.setHeader(header);
        protocol.setData(protocolData);

        return protocol;
    }


    /**
     * 生成基础协议并转换成 Buffer
     *
     * @param protocolData 请求体数据
     * @return
     */
    public static Buffer generateAndToBuffer(ProtocolData protocolData) {
        byte[] serializeArr = ObjectUtil.serialize(protocolData);

        RpcProtocol protocol = new RpcProtocol();
        ProtocolHeader header = new ProtocolHeader();

        header.setVersion(Constants.currentProtocolVersion);
        header.setTimestamp(new Date().getTime());
        header.setBodyLength(serializeArr.length);
        protocol.setHeader(header);
        protocol.setData(protocolData);

        Buffer responseBuffer = new BufferImpl();
        responseBuffer.appendByte(protocol.getHeader().getVersion());
        responseBuffer.appendLong(protocol.getHeader().getTimestamp());
        responseBuffer.appendInt(protocol.getHeader().getBodyLength());
        responseBuffer.appendBytes(serializeArr);

        return responseBuffer;
    }


}
