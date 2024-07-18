package github.zmz.server;

import github.zmz.protocol.BaseProtocol;
import github.zmz.protocol.RpcData;
import github.zmz.service.UserService;
import github.zmz.service.impl.NormalUserServiceImpl;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerRequest;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.Date;

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
                System.out.println("o.getClass() = " + o.getClass());
                System.out.println("o = " + o);

                RpcData rpcData = (RpcData) o;
                Class serviceType = rpcData.getServiceType();


                System.out.println("serviceType.getName() = " + serviceType.getName());

                // 静态创建
                UserService userService = new NormalUserServiceImpl();

                BaseProtocol<UserService> baseProtocol = new BaseProtocol<>();
                baseProtocol.setVersion((byte) 1);
                baseProtocol.setTimestamp(new Date().getTime());
//                baseProtocol.setData();

                req.response().send(buffer);
            });
        }
    }
}
