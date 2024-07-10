package github.zmz.protocol;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BaseProtocol<T> {

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

    /**
     * 携带的数据
     */
    private T data;


}
