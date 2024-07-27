package github.zmz.enums;

import lombok.Getter;

@Getter
public enum RegisterNameEnum {
    ZOOKEEPER("ZOOKEEPER");

    String name;

    RegisterNameEnum(String name) {
        this.name = name;
    }

    public static RegisterNameEnum getByName(String name) {
        for (RegisterNameEnum registerNameEnum : values()) {
            if (registerNameEnum.getName().equalsIgnoreCase(name)) {
                return registerNameEnum;
            }
        }
        return ZOOKEEPER;
    }
}
