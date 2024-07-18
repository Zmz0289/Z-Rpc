package github.zmz.handler;

import github.zmz.domain.User;
import github.zmz.factory.ProxyFactory;
import github.zmz.protocol.BaseProtocol;
import github.zmz.protocol.RpcData;
import github.zmz.service.UserService;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.buffer.impl.BufferImpl;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.concurrent.CompletableFuture;

/**
 * @author zmz
 * @create 2024-07-18
 */
public class RpcInvocationHandler implements InvocationHandler {

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Class<?>[] interfaces = proxy.getClass().getInterfaces();
        if (interfaces.length <= 0) {
            throw new RuntimeException("需要至少继承一个接口");
        }

        // 在此处发起 Rpc 调用
        CompletableFuture<RpcData> completableFuture = new CompletableFuture<>();

        Vertx vertx = Vertx.vertx();

        WebClient webClient = WebClient.create(vertx);

        // 传输的数据
        RpcData rpcData = new RpcData();
        rpcData.setFullName(interfaces[0].getName());
        rpcData.setServiceName("UserService");
        rpcData.setMethodName(method.getName());
        rpcData.setArgs(new Object[]{"123"});

        // 协议
        BaseProtocol protocol = new BaseProtocol();
        protocol.setVersion((byte) 1);
        protocol.setTimestamp(new Date().getTime());
        protocol.setBodyLength(100);
        protocol.setData(rpcData);

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
                .sendBuffer(buffer, res -> {
                    HttpResponse<Buffer> result = res.result();
                    Buffer body = result.body();

                    byte[] bytes = body.getBytes(13, body.length());

                    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);

                    try {
                        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
                        completableFuture.complete((RpcData) objectInputStream.readObject());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                });

        return completableFuture.get().getData();
    }

    public static void main(String[] args) {

        UserService userService = ProxyFactory.newInstance(UserService.class);

        User user = userService.get("123");

        System.out.println("user = " + user);

    }
}
