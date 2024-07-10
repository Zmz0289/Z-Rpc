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
public class RpcData implements Serializable {

    /**
     * 调用的服务名称
     */
    private String serviceName;

    /**
     * 调用的方法名称
     */
    private String methodName;

    /**
     * 调用方法的形参
     */
    private Object[] requestArgs;

    /**
     * 调用方法的返回值
     */
    private Object responseData;

}
