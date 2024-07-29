package github.zmz.register;

import github.zmz.constant.Constants;
import github.zmz.domain.ServiceMetaInfo;

/**
 * 服务选择器
 */
public class RegisterSelector {

    private static final Register register;

    static {
        // 读取配置文件
        register = new ZookeeperRegister();
        register.init();

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
