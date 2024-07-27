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
public class ProtocolData implements Serializable {

    private static final long serialVersionUID = -684979447075466771L;

    /**
     * 调用的服务名称
     */
    private String serviceName;

    /**
     * 调用的方法名称
     */
    private String methodName;

    /**
     * 方法形参
     */
    private Object[] args;

    /**
     * 数据（响应）
     */
    private Object data;

    /**
     * 是否发生错误（响应）
     */
    private Boolean errorOccurred = Boolean.FALSE;

    /**
     * 具体异常（响应）
     */
    private Throwable exception;

}
