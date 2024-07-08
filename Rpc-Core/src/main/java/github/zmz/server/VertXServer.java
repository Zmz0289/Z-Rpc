package github.zmz.server;

import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServer;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.concurrent.atomic.AtomicReference;

public class VertXServer {

    public static void main(String[] args) {


//        Buffer buffer = new BufferImpl();
//
//        buffer.appendString("你好", "UTF-8");

        Vertx vertx = Vertx.vertx();

        final AtomicReference<Buffer> atomicBuff = new AtomicReference<>();

        //创建 httpServer
        HttpServer server = vertx.createHttpServer().requestHandler(req -> {

            req.body(body -> {
                if (body.succeeded()) {
                    atomicBuff.set(body.result());
                    Buffer buffer = atomicBuff.get();

                    System.out.println("body.getByte(1) = " + buffer.getByte(0));
                    System.out.println("body.getLong(2) = " + buffer.getLong(1));
                    System.out.println("body.getInt(10) = " + buffer.getInt(9));
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
                } else {
                    System.out.println("server get buff fail");
                }
            }).response()
                    .send(atomicBuff.get());

        });

        //指定监听端口
        server.listen(8080, res -> {
            if (res.succeeded()) {
                System.out.println("Begin http server !");
            } else {
                System.out.println("Http server occured error " + res.cause());
            }
        });

    }
}
