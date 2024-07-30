package github.zmz.config;

import github.zmz.register.RegisterConfig;
import lombok.Data;
import org.apache.curator.RetryPolicy;
import org.apache.curator.retry.RetryForever;

@Data
public class ZookeeperConfig implements RegisterConfig {

    private String addr = "127.0.0.1:2181";

    private String host = "127.0.0.1";

    private String port = "2181";

    private int sessionTimeoutMs = 20000;

    private int connectionTimeoutMs = 20000;

    private RetryPolicy retryPolicy = new RetryForever(1000);


    @Override
    public String getAddr() {
        return addr;
    }

    @Override
    public String getHost() {
        return host;
    }

    @Override
    public String getPort() {
        return port;
    }
}
