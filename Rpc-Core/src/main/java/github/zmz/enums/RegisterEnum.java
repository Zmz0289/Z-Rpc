package github.zmz.enums;

import github.zmz.config.NacosConfig;
import github.zmz.config.ZookeeperConfig;
import github.zmz.register.RegisterConfig;
import lombok.Getter;

@Getter
public enum RegisterEnum {
    ZOOKEEPER("ZOOKEEPER", new ZookeeperConfig()),
    NACOS("NACOS", new NacosConfig());

    String name;

    RegisterConfig registerConfig;

    RegisterEnum(String name, RegisterConfig registerConfig) {
        this.name = name;
        this.registerConfig = registerConfig;
    }

    public static RegisterEnum getByName(String name) {
        for (RegisterEnum registerEnum : values()) {
            if (registerEnum.getName().equalsIgnoreCase(name)) {
                return registerEnum;
            }
        }
        return ZOOKEEPER;
    }
}
