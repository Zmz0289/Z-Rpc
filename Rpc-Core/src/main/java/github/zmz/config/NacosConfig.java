package github.zmz.config;

import github.zmz.register.RegisterConfig;
import lombok.Data;

@Data
public class NacosConfig implements RegisterConfig {

    private String addr = "127.0.0.1:8848";

    private String host = "127.0.0.1";

    private String port = "8828";

    private String namespace = "public";

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
