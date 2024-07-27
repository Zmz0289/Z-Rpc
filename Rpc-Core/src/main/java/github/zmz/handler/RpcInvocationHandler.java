package github.zmz.handler;

import github.zmz.constant.Constants;
import github.zmz.domain.User;
import github.zmz.factory.ProxyFactory;
import github.zmz.protocol.RpcData;
import github.zmz.service.UserService;
import github.zmz.utils.ProtocolHelper;
import github.zmz.utils.VertxUtil;
import io.vertx.core.buffer.Buffer;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
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
            throw new RuntimeException("At least one interface must be inherited");
        }
        Class<?> useService = interfaces[0];

        // 使用 CompletableFuture 异步接收，同步等待结果
        CompletableFuture<RpcData> completableFuture = new CompletableFuture<>();

        // 在此处发起 Rpc 调用
        WebClient webClient = VertxUtil.newWebClient();

        // 传输的数据
        RpcData rpcData = new RpcData();
        rpcData.setServiceName(useService.getSimpleName());
        rpcData.setMethodName(method.getName());
        rpcData.setArgs(args);

        // 协议
        Buffer buffer = ProtocolHelper.generateAndToBuffer(rpcData);

        webClient.post(8060, "127.0.0.1", "/")
                .sendBuffer(buffer, res -> {
                    HttpResponse<Buffer> result = res.result();
                    Buffer body = result.body();

                    byte[] bytes = body.getBytes(Constants.protocolHeaderLength, body.length());

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
