package github.zmz.factory;

import github.zmz.handler.RpcInvocationHandler;

import java.lang.reflect.Proxy;

/**
 * 代理工厂
 * 用于创建代理对象
 *
 * @author zmz
 * @create 2024-07-18
 */
public class ProxyFactory {

    @SuppressWarnings("unchecked")
    public static <T> T newInstance(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, new RpcInvocationHandler());
    }


}
