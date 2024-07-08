package github.zmz.test1;


import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.buffer.impl.BufferImpl;
import io.vertx.core.http.HttpServer;
import org.junit.Test;

import java.util.Scanner;

public class VertXTest {

    @Test
    public void test1() {

        Vertx vertx = Vertx.vertx();
        //创建 httpServer
        HttpServer server = vertx.createHttpServer().requestHandler(req -> {
            req.response()
                    .putHeader("content-type", "text/plain")
                    .end("Hello from Vert.x!");
        });
        //指定监听端口
        server.listen(8080, res -> {
            if (res.succeeded()) {
                System.out.println("Begin http server !");
            } else {
                System.out.println("Http server occured error " + res.cause());
            }
        });

        new Scanner(System.in).nextBigInteger();
    }

    @Test
    public void bodyResponseTest() {

        Buffer buffer = new BufferImpl();

//        byte[] arr = {1,0,1};
//
//        buffer.appendBytes(arr);
        buffer.appendString("你好", "UTF-8");

        Vertx vertx = Vertx.vertx();

        //创建 httpServer
        HttpServer server = vertx.createHttpServer().requestHandler(req -> {
            Buffer result = req.body().result();
            System.out.println("result = " + result);

            req.response()
                    .putHeader("Content-Type", "application/json;charset=utf-8")
                    .send(buffer);

        });

        //指定监听端口
        server.listen(8080, res -> {
            if (res.succeeded()) {
                System.out.println("Begin http server !");
            } else {
                System.out.println("Http server occured error " + res.cause());
            }
        });

        new Scanner(System.in).nextBigInteger();
    }

}
