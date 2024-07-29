package github.zmz.register;

import github.zmz.delegate.ServiceMetaInfoDelegate;
import github.zmz.domain.ServiceMetaInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryForever;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.details.JsonInstanceSerializer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class ZookeeperRegister implements Register {

    private CuratorFramework client;

    private ServiceDiscovery<ServiceMetaInfo> serviceDiscovery;

    private final String RPC_BASE_PATH = "/z-rpc/";

    private final String SERVICE_FLAG = "ZOOKEEPER";


    @Override
    public void init() {
        client = CuratorFrameworkFactory.builder()
                .connectString("127.0.0.1:2181")
                .sessionTimeoutMs(20000)
                .connectionTimeoutMs(20000)
                // 权限认证
//                .authorization("digest", "user1:123456a".getBytes(StandardCharsets.UTF_8))
                // 重试策略
                .retryPolicy(new RetryForever(1000))
                .build();

        // 构建 serviceDiscovery 实例
        serviceDiscovery = ServiceDiscoveryBuilder.builder(ServiceMetaInfo.class)
                .client(client)
                .basePath(RPC_BASE_PATH)
                .serializer(new JsonInstanceSerializer<>(ServiceMetaInfo.class))
                .build();

        // 启动客户端
        try {
            client.start();
            serviceDiscovery.start();
        } catch (Exception e) {
            log.error("Zookeeper init has error occurred, msg = {}", e.getMessage(), e);
        }
    }

    @Override
    public boolean registerService(ServiceMetaInfo serviceMetaInfo) {
        try {
            serviceDiscovery.registerService(buildServiceInstance(serviceMetaInfo));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }


    @Override
    public boolean unRegisterService(ServiceMetaInfo serviceMetaInfo) {
        try {
            serviceDiscovery.unregisterService(buildServiceInstance(serviceMetaInfo));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    @Override
    public List<ServiceMetaInfo> serviceDiscover(String serviceName) {
        Collection<ServiceInstance<ServiceMetaInfo>> instances = null;
        try {
            String serviceFullName = ServiceMetaInfoDelegate.getServiceName(getFlag(), serviceName);
            instances = serviceDiscovery.queryForInstances(serviceFullName);

            return instances.stream().map(ServiceInstance::getPayload).collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }


    private ServiceInstance<ServiceMetaInfo> buildServiceInstance(ServiceMetaInfo serviceMetaInfo) {
        ServiceMetaInfoDelegate delegate = ServiceMetaInfoDelegate.getInstance(serviceMetaInfo);
        try {
            return ServiceInstance
                    .<ServiceMetaInfo>builder()
                    .id(delegate.getServiceId(serviceDiscover(serviceMetaInfo.getServiceName()).size() + 1))
                    .name(delegate.getServiceName(getFlag()))
                    .address(delegate.getServiceAddress())
                    .port(delegate.getServicePort())
                    .payload(serviceMetaInfo)
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getFlag() {
        return SERVICE_FLAG;
    }
}
