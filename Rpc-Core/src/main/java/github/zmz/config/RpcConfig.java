package github.zmz.config;

import github.zmz.constant.Constants;
import github.zmz.enums.RegisterEnum;
import lombok.Data;

/**
 * 全局配置
 *
 * @author zmz
 * @create 2024-07-29
 */
@Data
public class RpcConfig {

    /**
     * 服务版本
     */
    String serviceVersion = Constants.ServiceInfo.serviceVersion;

    /**
     * 注册中心配置
     */
    String registerConfigName = RegisterEnum.ZOOKEEPER.getName();

}
