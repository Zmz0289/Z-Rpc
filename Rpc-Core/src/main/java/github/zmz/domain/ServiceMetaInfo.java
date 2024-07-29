package github.zmz.domain;

import lombok.Data;

/**
 * 服务基础信息
 */
@Data
public class ServiceMetaInfo {

    /**
     * 服务名称
     */
    String serviceName;

    /**
     * 服务版本
     */
    String serviceVersion;

    /**
     * ip地址
     */
    String serviceHost;

    /**
     * 端口号
     */
    String servicePort;


}
