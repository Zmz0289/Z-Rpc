package github.zmz.register;

import github.zmz.config.RpcConfig;
import github.zmz.constant.Constants;
import github.zmz.domain.ServiceMetaInfo;
import github.zmz.enums.RegisterEnum;
import github.zmz.exception.RpcException;
import github.zmz.factory.RpcConfigFactory;

/**
 * 服务选择器
 */
public class RegisterSelector {

    private static final Register register;

    static {
        // 读取配置文件
        RpcConfig rpcConfig = RpcConfigFactory.getConfig();

        switch (RegisterEnum.getByName(rpcConfig.getRegisterConfigName())) {
            case NACOS:
                register = new NacosRegister();
                break;
            case ZOOKEEPER:
                register = new ZookeeperRegister();
                break;
            default:
                throw new RpcException("RegisterSelector<init> Rpc config: No corresponding configuration was found");
        }

        register.init();
    }

    /**
     * 注册服务
     * 暂时写死，后续根据注解动态注册
     */
    public static void register() {
        ServiceMetaInfo metaInfo = new ServiceMetaInfo();
        metaInfo.setServiceName("RemoteUserService");
        metaInfo.setServiceHost("127.0.0.1");
        metaInfo.setServicePort("8060");
        metaInfo.setServiceVersion(Constants.ServiceInfo.serviceVersion);

        register.registerService(metaInfo);
    }

    public static Register get() {
        return register;
    }
}
