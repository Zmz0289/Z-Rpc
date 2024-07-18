package github.zmz.protocol;

import lombok.Data;

import java.io.Serializable;

/**
 * Rpc 传输数据，记录调用目标的具体信息
 *
 * @author zmz
 * @create 2024-07-10
 */
@Data
public class RpcData<T> implements Serializable {

    /**
     * 调用的服务类型
     */
    private Class<T> serviceType;

//    /**
//     * 调用的服务名称
//     */
//    private String serviceName;

    /**
     * 数据（响应）
     */
    private T data;

}
