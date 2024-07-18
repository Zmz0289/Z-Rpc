package github.zmz.utils;

import io.vertx.core.Vertx;
import io.vertx.ext.web.client.WebClient;

public class VertxUtil {

    public static WebClient newWebClient() {
        Vertx vertx = Vertx.vertx();
        return WebClient.create(vertx);
    }

}
