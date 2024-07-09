package github.zmz.server;

import io.vertx.core.Handler;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerRequest;

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
                req.response().send(buffer);
            });
        }
    }
}
