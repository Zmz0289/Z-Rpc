package github.zmz.protocol;

import lombok.Data;

@Data
public class ProtocolHeader {
    /**
     * 版本号
     */
    private Byte version;

    /**
     * 时间戳
     */
    private Long timestamp;

    /**
     * 请求体长度
     */
    private Integer bodyLength;
}