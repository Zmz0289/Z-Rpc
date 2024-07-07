package github.zmz.test1;

import github.zmz.domain.ServiceMetaInfo;
import github.zmz.register.ZookeeperRegister;
import org.junit.Test;

import java.util.List;

public class ZkClientDemo {


    @Test
    public void ZookeeperRegisterTest() {
        ZookeeperRegister zookeeperRegister = new ZookeeperRegister();
        zookeeperRegister.init();

        ServiceMetaInfo metaInfo = new ServiceMetaInfo();
        metaInfo.setServiceName("testService");
        metaInfo.setServiceHost("127.0.0.1");
        metaInfo.setServicePort("8008");
        metaInfo.setServiceVersion("1");

        zookeeperRegister.registerService(metaInfo);

        List<ServiceMetaInfo> testService = zookeeperRegister.serviceDiscover("testService");
        System.out.println("testService = " + testService);


        zookeeperRegister.unRegisterService(metaInfo);

        List<ServiceMetaInfo> testService2 = zookeeperRegister.serviceDiscover("testService");
        System.out.println("testService2 = " + testService2);


    }

}