package github.zmz.handler;

import github.zmz.constant.Constants;
import github.zmz.domain.ServiceMetaInfo;
import github.zmz.protocol.ProtocolData;
import github.zmz.register.Register;
import github.zmz.register.RegisterSelector;
import github.zmz.utils.ProtocolHelper;
import github.zmz.utils.VertxUtil;
import io.vertx.core.buffer.Buffer;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
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

        // 使用 CompletableFuture 异步接收，同步等待结果
        CompletableFuture<ProtocolData> completableFuture = new CompletableFuture<>();

        // 在此处发起 Rpc 调用
        WebClient webClient = VertxUtil.newWebClient();

        // 传输的数据
        ProtocolData protocolData = new ProtocolData();
        protocolData.setServiceName(interfaces[0].getSimpleName());
        protocolData.setMethodName(method.getName());
        protocolData.setArgs(args);

        // 协议
        Buffer buffer = ProtocolHelper.generateAndToBuffer(protocolData);

        // 寻找具体服务
        Register register = RegisterSelector.get();
        List<ServiceMetaInfo> serviceMetaInfos = register.serviceDiscover(protocolData.getServiceName());
        if (serviceMetaInfos == null || serviceMetaInfos.size() == 0) {
            throw new RuntimeException("No specified service");
        }

        // 随机抽取一个服务进行请求
        Collections.shuffle(serviceMetaInfos);
        ServiceMetaInfo serviceMetaInfo = serviceMetaInfos.get(0);

        // 发起请求
        webClient.post(Integer.parseInt(serviceMetaInfo.getServicePort()), serviceMetaInfo.getServiceHost(), "/")
                .sendBuffer(buffer, res -> {
                    HttpResponse<Buffer> result = res.result();
                    Buffer body = result.body();

                    byte[] bytes = body.getBytes(Constants.Protocol.protocolHeaderLength, body.length());

                    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);

                    try {
                        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
                        completableFuture.complete((ProtocolData) objectInputStream.readObject());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                });

        // 远程调用执行时出现异常，抛出该异常
        ProtocolData responseData = completableFuture.get();
        if (responseData.getErrorOccurred()) {
            throw responseData.getException();
        }

        return responseData.getData();
    }

//    public static void main(String[] args) {
//
//        UserService userService = ProxyFactory.newInstance(UserService.class);
//
//        User user = userService.get("123");
//
//        System.out.println("user = " + user);
//
//    }
}
