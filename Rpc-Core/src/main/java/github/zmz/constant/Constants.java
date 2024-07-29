package github.zmz.constant;

/**
 * 常量
 */
public interface Constants {

    /**
     * 协议
     */
    interface Protocol {
        /**
         * Rpc 协议头部长度
         */
        int protocolHeaderLength = 13;

        /**
         * Rpc 协议当前版本
         */
        byte currentProtocolVersion = 1;
    }

    /**
     * 服务信息
     */
    interface ServiceInfo {
        /**
         * 服务版本
         */
        String serviceVersion = "1.0";

    }

}
