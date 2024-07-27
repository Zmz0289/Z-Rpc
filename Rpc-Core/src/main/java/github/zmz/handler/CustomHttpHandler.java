package github.zmz.handler;

import github.zmz.constant.Constants;
import github.zmz.loader.ServiceLoader;
import github.zmz.protocol.RpcData;
import github.zmz.utils.ObjectUtil;
import github.zmz.utils.ProtocolHelper;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerRequest;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * 自定义的 http 请求处理器，在此处接收请求并处理响应返回
 *
 * @author zmz
 * @create 2024-07-09
 */
@Slf4j
public class CustomHttpHandler implements Handler<HttpServerRequest> {


    @Override
    public void handle(HttpServerRequest req) {
        HttpMethod method = req.method();
        if (method.equals(HttpMethod.GET)) {
            req.response().send("hello");
        } else if (method.equals(HttpMethod.POST)) {
            req.handler(buffer -> {
                byte[] bytes = buffer.getBytes(Constants.protocolHeaderLength, buffer.length());

                RpcData rpcData = ObjectUtil.deSerialize(bytes, RpcData.class);

                // 请求信息
                String serviceName = rpcData.getServiceName();
                String methodName = rpcData.getMethodName();
                Object[] args = rpcData.getArgs();

                try {
                    Object instance = ServiceLoader.getInstance(serviceName);

                    // 执行方法
                    Method declaredMethod = instance.getClass().getMethod(methodName, Arrays.stream(args).map(Object::getClass).toArray(Class[]::new));
                    Object invoke = declaredMethod.invoke(instance, args);

                    // 响应数据
                    rpcData.setData(invoke);
                } catch (Exception e) {
                    log.error("CustomHttpHandler#handle() has error occurred, msg = {}", e.getMessage(), e);
                    rpcData.setData(null);
                }

                Buffer responseBuffer = ProtocolHelper.generateAndToBuffer(rpcData);
                req.response().send(responseBuffer);
            });
        }
    }
}
