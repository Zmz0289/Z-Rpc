package github.zmz.config;

import github.zmz.constant.Constants;
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


}
