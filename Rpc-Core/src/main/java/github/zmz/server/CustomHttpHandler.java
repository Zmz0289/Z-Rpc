package github.zmz.server;

import github.zmz.constant.Constants;
import github.zmz.exception.RpcException;
import github.zmz.protocol.RpcData;
import github.zmz.utils.ClassUtil;
import github.zmz.utils.ObjectUtil;
import github.zmz.utils.ProtocolHelper;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerRequest;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

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
                String fullName = rpcData.getFullName();
                String methodName = rpcData.getMethodName();
                Object[] args = rpcData.getArgs();

                Class<?> aClass;
                try {
                    aClass = Class.forName(fullName);

                    // 查询指定接口同一目录下的所有的实现类
                    List<Class<?>> allClassByInterface = ClassUtil.getAllClassByInterface(aClass);
                    if (allClassByInterface.size() <= 0) {
                        throw new RpcException("No implementation class for the service is specified");
                    }

                    // todo 如果有多个实现类，根据配置进行选择
                    Class<?> implementClass = allClassByInterface.get(1);

                    // 类新建对象
                    Object instance = implementClass.newInstance();

                    // 执行方法
                    Method declaredMethod = implementClass.getDeclaredMethod(methodName, Arrays.stream(args).map(Object::getClass).toArray(Class[]::new));
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
