package github.zmz.protocol;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RpcProtocol {

    /**
     * 请求头
     */
    private ProtocolHeader header;

    /**
     * 携带的数据
     */
    private ProtocolData data;


}
