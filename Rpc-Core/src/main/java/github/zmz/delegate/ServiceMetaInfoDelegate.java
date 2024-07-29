package github.zmz.delegate;

import github.zmz.domain.ServiceMetaInfo;
import github.zmz.factory.RpcConfigFactory;

/**
 * 服务元信息代理类，提供获取服务信息的方法
 *
 * @author zmz
 * @create 2024-07-29
 */
public class ServiceMetaInfoDelegate {

    private ServiceMetaInfo serviceMetaInfo;
    private final static String separator = ":";

    private ServiceMetaInfoDelegate() {
    }

    public static ServiceMetaInfoDelegate getInstance(ServiceMetaInfo serviceMetaInfo) {
        ServiceMetaInfoDelegate delegate = new ServiceMetaInfoDelegate();
        delegate.serviceMetaInfo = serviceMetaInfo;
        return delegate;
    }

    /**
     * 获取完整的服务名称
     *
     * @param prefix     服务标识
     * @param simpleName 简单的服务名称
     */
    public static String getServiceName(String prefix, String simpleName) {
        return prefix + separator +
                simpleName + separator +
                RpcConfigFactory.getConfig().getServiceVersion();
    }

    /**
     * <p>获取服务 id</p>
     * eg.  实例对象序号
     */
    public String getServiceId(int index) {
        return String.valueOf(index);
    }

    /**
     * <p>获取服务全名称</p>
     * eg.  服务前缀:服务名称:服务版本
     *
     * @param prefix 服务前缀
     */
    public String getServiceName(String prefix) {
        return prefix + separator +
                this.serviceMetaInfo.getServiceName() + separator +
                this.serviceMetaInfo.getServiceVersion();
    }

    /**
     * <p>获取服务 ip 地址</p>
     */
    public String getServiceAddress() {
        return this.serviceMetaInfo.getServiceHost();
    }

    /**
     * <p>获取服务端口</p>
     */
    public int getServicePort() {
        try {
            return Integer.parseInt(this.serviceMetaInfo.getServicePort());
        } catch (NumberFormatException e) {

        }
        return -1;
    }

}
