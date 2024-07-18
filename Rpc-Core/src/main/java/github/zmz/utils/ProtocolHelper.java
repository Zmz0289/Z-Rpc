package github.zmz.utils;

import github.zmz.constant.Constants;
import github.zmz.protocol.BaseProtocol;
import github.zmz.protocol.RpcData;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.buffer.impl.BufferImpl;

import java.util.Date;

public class ProtocolHelper {

    public static BaseProtocol generate(RpcData rpcData, int bodyLength) {
        BaseProtocol protocol = new BaseProtocol();
        protocol.setVersion((byte) 1);
        protocol.setTimestamp(new Date().getTime());
        protocol.setBodyLength(bodyLength);
        protocol.setData(rpcData);

        return protocol;
    }


    /**
     * 生成基础协议并转换成 Buffer
     *
     * @param rpcData 请求体数据
     * @return
     */
    public static Buffer generateAndToBuffer(RpcData rpcData) {
        byte[] serializeArr = ObjectUtil.serialize(rpcData);

        BaseProtocol protocol = new BaseProtocol();
        protocol.setVersion(Constants.currentProtocolVersion);
        protocol.setTimestamp(new Date().getTime());
        protocol.setBodyLength(serializeArr.length);
        protocol.setData(rpcData);

        Buffer responseBuffer = new BufferImpl();
        responseBuffer.appendByte(protocol.getVersion());
        responseBuffer.appendLong(protocol.getTimestamp());
        responseBuffer.appendInt(protocol.getBodyLength());
        responseBuffer.appendBytes(serializeArr);

        return responseBuffer;
    }


}
