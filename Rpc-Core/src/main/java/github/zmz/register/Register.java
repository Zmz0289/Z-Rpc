package github.zmz.register;

import github.zmz.domain.ServiceMetaInfo;

import java.util.List;

/**
 * 服务注册和服务发现接口
 */
public interface Register {

    /**
     * 初始化注册中心
     */
    void init();

    /**
     * 注册服务
     *
     * @param serviceMetaInfo 服务元信息
     */
    boolean registerService(ServiceMetaInfo serviceMetaInfo);

    /**
     * 注销服务
     *
     * @param serviceMetaInfo 服务元信息
     */
    boolean unRegisterService(ServiceMetaInfo serviceMetaInfo);


    /**
     * 服务发现
     * 获取指定服务名称的所有服务信息
     *
     * @param serviceName 服务名称
     * @return 服务元信息列表
     */
    List<ServiceMetaInfo> serviceDiscover(String serviceName);


}
