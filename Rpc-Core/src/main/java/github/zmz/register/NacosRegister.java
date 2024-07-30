package github.zmz.register;

import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import github.zmz.config.NacosConfig;
import github.zmz.delegate.ServiceMetaInfoDelegate;
import github.zmz.domain.ServiceMetaInfo;
import github.zmz.enums.RegisterEnum;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

@Slf4j
public class NacosRegister implements Register {

    /**
     * 负责服务注册和服务发现
     */
    private NamingService namingService;

    private final static String PAYLOAD_NAME = "PAYLOAD";

    @Override
    public void init() {
        NacosConfig config = (NacosConfig) RegisterEnum.NACOS.getRegisterConfig();

        try {
            Properties prop = System.getProperties();
            prop.put("serverAddr", config.getAddr());
            prop.put("namespace", config.getNamespace());

            namingService = NacosFactory.createNamingService(prop);
        } catch (NacosException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean registerService(ServiceMetaInfo serviceMetaInfo) {
        Instance instance = buildInstance(serviceMetaInfo);
        try {
            namingService.registerInstance(instance.getServiceName(), instance);
        } catch (NacosException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public boolean unRegisterService(ServiceMetaInfo serviceMetaInfo) {
        Instance instance = buildInstance(serviceMetaInfo);
        try {
            namingService.deregisterInstance(instance.getServiceName(), instance.getIp(), instance.getPort());
        } catch (NacosException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public List<ServiceMetaInfo> serviceDiscover(String serviceName) {
        try {
            String fullServiceName = ServiceMetaInfoDelegate.getServiceName(getFlag(), serviceName);

            List<Instance> instances = namingService.getAllInstances(fullServiceName);
            return instances.stream().map(instance -> {
                String metaInfoJson = instance.getMetadata().get(PAYLOAD_NAME);
                return JSON.parseObject(metaInfoJson, ServiceMetaInfo.class);
            }).collect(Collectors.toList());
        } catch (NacosException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    @Override
    public String getFlag() {
        return RegisterEnum.NACOS.getName();
    }

    private Instance buildInstance(ServiceMetaInfo serviceMetaInfo) {
        ServiceMetaInfoDelegate delegate = ServiceMetaInfoDelegate.getInstance(serviceMetaInfo);

        Instance instance = new Instance();
        instance.setServiceName(delegate.getServiceName(getFlag()));
        instance.setIp(delegate.getServiceAddress());
        instance.setPort(delegate.getServicePort());
        instance.setInstanceId(delegate.getServiceId(serviceDiscover(serviceMetaInfo.getServiceName()).size() + 1));
        instance.addMetadata(PAYLOAD_NAME, JSON.toJSONString(serviceMetaInfo));

        return instance;
    }

}
