package github.zmz.protocol;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BaseProtocol {

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
    private RpcData data;


}
