package github.zmz.server;

import github.zmz.domain.User;
import github.zmz.protocol.RpcProtocol;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.buffer.impl.BufferImpl;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.util.Date;

public class VertXClient {

    public static void main(String[] args) throws Exception {
        Vertx vertx = Vertx.vertx();

        WebClient webClient = WebClient.create(vertx);

        // 协议
        RpcProtocol<User> protocol =
                RpcProtocol.<User>builder()
                        .version((byte) 1)
                        .timestamp(new Date().getTime())
                        .bodyLength(100)
                        .data(new User("苏锐", 28, new BigDecimal(100000)))
                        .build();

        // 序列化
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(protocol.getData());

        Buffer buffer = new BufferImpl();
        buffer.appendByte(protocol.getVersion());
        buffer.appendLong(protocol.getTimestamp());
        buffer.appendInt(protocol.getBodyLength());
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

    }

}
