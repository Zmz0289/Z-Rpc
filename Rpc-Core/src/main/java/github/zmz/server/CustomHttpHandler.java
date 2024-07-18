package github.zmz.server;

import github.zmz.protocol.BaseProtocol;
import github.zmz.protocol.RpcData;
import github.zmz.service.UserService;
import github.zmz.utils.ClassUtil;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.buffer.impl.BufferImpl;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerRequest;

import java.io.*;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 自定义的 http 请求处理器，在此处接收请求并处理响应返回
 *
 * @author zmz
 * @create 2024-07-09
 */
public class CustomHttpHandler implements Handler<HttpServerRequest> {


    @Override
    public void handle(HttpServerRequest req) {
        HttpMethod method = req.method();
        if (method.equals(HttpMethod.GET)) {
            req.response().send("hello");
        } else if (method.equals(HttpMethod.POST)) {
            req.handler(buffer -> {
                byte[] bytes = buffer.getBytes(13, buffer.length());

                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
                Object o = null;
                try {
                    ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);

                    o = objectInputStream.readObject();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                RpcData rpcData = (RpcData) o;

                String fullName = rpcData.getFullName();
                String methodName = rpcData.getMethodName();
                Object[] args = rpcData.getArgs();

                Class<?> aClass = null;
                try {
                    aClass = Class.forName(fullName);
                } catch (Exception e) {
                    e.printStackTrace();
                }


                List<Class> allClassByInterface = ClassUtil.getAllClassByInterface(aClass);
                Class aClass1 = allClassByInterface.get(1);

                UserService userService = null;
                try {
                    userService = (UserService) aClass1.newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                List<? extends Class<?>> classes = Arrays.stream(args).map(Object::getClass).collect(Collectors.toList());

                Object invoke = null;
                try {
                    Method declaredMethod = aClass1.getDeclaredMethod(methodName, classes.toArray(new Class[0]));

                    invoke = declaredMethod.invoke(userService, args);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                System.out.println("invoke = " + invoke);


                // 响应
                BaseProtocol protocol = new BaseProtocol();
                protocol.setVersion((byte) 1);
                protocol.setTimestamp(new Date().getTime());
                protocol.setBodyLength(100);

                rpcData.setData(invoke);
                protocol.setData(rpcData);

                // 序列化
                ByteArrayOutputStream byteArrayOutputStream = null;
                try {
                    byteArrayOutputStream = new ByteArrayOutputStream();
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
                    objectOutputStream.writeObject(protocol.getData());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Buffer responseBuffer = new BufferImpl();
                responseBuffer.appendByte(protocol.getVersion());
                responseBuffer.appendLong(protocol.getTimestamp());
                responseBuffer.appendInt(protocol.getBodyLength());
                responseBuffer.appendBytes(byteArrayOutputStream.toByteArray());

                req.response().send(responseBuffer);
            });
        }
    }
}
