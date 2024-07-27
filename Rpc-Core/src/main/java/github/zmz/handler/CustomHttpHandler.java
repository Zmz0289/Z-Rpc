package github.zmz.handler;

import github.zmz.constant.Constants;
import github.zmz.loader.ServiceLoader;
import github.zmz.protocol.ProtocolData;
import github.zmz.utils.ObjectUtil;
import github.zmz.utils.ProtocolHelper;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerRequest;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
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

                ProtocolData protocolData = ObjectUtil.deSerialize(bytes, ProtocolData.class);

                // 请求信息
                String serviceName = protocolData.getServiceName();
                String methodName = protocolData.getMethodName();
                Object[] args = protocolData.getArgs();

                try {
                    Object instance = ServiceLoader.getInstance(serviceName);

                    // 执行方法
                    Method declaredMethod = instance.getClass().getMethod(methodName, Arrays.stream(args).map(Object::getClass).toArray(Class[]::new));
                    Object invoke = declaredMethod.invoke(instance, args);

                    // 响应数据
                    protocolData.setData(invoke);
                } catch (Exception e) {
                    log.error("CustomHttpHandler#handle() has error occurred, msg = {}", e.getMessage(), e);
                    protocolData.setData(null);
                    protocolData.setErrorOccurred(Boolean.TRUE);

                    // 获取具体异常（针对非受检异常和错误）
                    if (e instanceof InvocationTargetException) {
                        InvocationTargetException invocationTargetException = (InvocationTargetException) e;
                        Throwable targetException = invocationTargetException.getTargetException();
                        if (targetException instanceof RuntimeException || targetException instanceof Error) {
                            protocolData.setException(targetException);
                        }
                    }

                    if (protocolData.getException() == null) {
                        protocolData.setException(e);
                    }
                }

                Buffer responseBuffer = ProtocolHelper.generateAndToBuffer(protocolData);
                req.response().send(responseBuffer);
            });
        }
    }
}
