package github.zmz.factory;

import github.zmz.config.RpcConfig;

/**
 * 获取配置的工厂，获取 Rpc 配置的唯一入口
 *
 * @author zmz
 * @create 2024-07-29
 */
public class RpcConfigFactory {
    private static final RpcConfig rpcConfig;

    private RpcConfigFactory() {
    }

    static {
        rpcConfig = new RpcConfig();
    }

    public static RpcConfig getConfig() {
        return rpcConfig;
    }

}
