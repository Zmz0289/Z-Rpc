package github.zmz.server;

import github.zmz.constant.Constants;
import github.zmz.protocol.ProtocolData;
import github.zmz.protocol.ProtocolHeader;
import github.zmz.protocol.RpcProtocol;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.buffer.impl.BufferImpl;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.Date;

@Slf4j
public class VertXClient {


    public static void start() {
        try {
            Vertx vertx = Vertx.vertx();

            WebClient webClient = WebClient.create(vertx);

            // 传输的数据
            ProtocolData protocolData = new ProtocolData();
            protocolData.setServiceName("UserService");
            protocolData.setMethodName("get");
            protocolData.setArgs(new Object[]{"123"});

            // 协议
            RpcProtocol protocol = new RpcProtocol();
            ProtocolHeader header = new ProtocolHeader();

            header.setVersion(Constants.currentProtocolVersion);
            header.setTimestamp(new Date().getTime());
            header.setBodyLength(100);
            protocol.setHeader(header);
            protocol.setData(protocolData);

            // 序列化
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(protocol.getData());

            Buffer buffer = new BufferImpl();
            buffer.appendByte(protocol.getHeader().getVersion());
            buffer.appendLong(protocol.getHeader().getTimestamp());
            buffer.appendInt(protocol.getHeader().getBodyLength());
            buffer.appendBytes(byteArrayOutputStream.toByteArray());

            webClient.post(8060, "127.0.0.1", "/")
                    .sendBuffer(buffer, resp -> {
                        if (resp.succeeded()) {
                            HttpResponse<Buffer> result = resp.result();
                            Buffer body = result.body();


                            System.out.println("body.getByte(1) = " + body.getByte(0));
                            System.out.println("body.getLong(2) = " + body.getLong(1));
                            System.out.println("body.getInt(10) = " + body.getInt(9));
                            byte[] bytes = body.getBytes(13, body.length());

                            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
                            Object o = null;
                            try {
                                ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);

                                o = objectInputStream.readObject();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            System.out.println("o.getClass() = " + o.getClass());
                            System.out.println("o = " + o);
                        }
                    });
        } catch (IOException e) {
            log.error("VertXClient has error occurred, msg = {}", e.getMessage());
        }
    }

}
