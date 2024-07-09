package github.zmz.server;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;

public class VertXServer {

    public static void main(String[] args) {

        Vertx vertx = Vertx.vertx();

        //创建 httpServer
        HttpServer server = vertx.createHttpServer().requestHandler(new CustomHttpHandler());

        //指定监听端口
        server.listen(8060, res -> {
            if (res.succeeded()) {
                System.out.println("Begin http server !");
            } else {
                System.out.println("Http server occured error " + res.cause());
            }
        });

    }
}
