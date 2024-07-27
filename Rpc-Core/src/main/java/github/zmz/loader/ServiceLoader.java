package github.zmz.loader;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.FileReader;
import java.net.URL;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * 加载指定名称的服务到本地
 *
 * @author zmz
 * @create 2024-07-10
 */
@Slf4j
public class ServiceLoader {
    /**
     * 缓存已注册的服务   服务名称 => 服务列表
     */
    public static final Map<String, Class<?>> serviceCacheMap = new HashMap<>();

    /**
     * 缓存已存在的对象   服务名称 => 对象
     */
    public static final Map<String, Object> instanceCacheMap = new HashMap<>();

    /**
     * 待注册的服务信息文件路径
     */
    public static final String RESOURCE_NAME = "SPI.info";

    /**
     * 服务注册文件的分隔符
     */
    public static final String SEPARATOR = "=";


    static {
        try {
            if (log.isDebugEnabled()) {
                log.debug("datetime is {}", new Date());
            }

            log.info("ServiceLoader start registration service...");

            Enumeration<URL> resources = ServiceLoader.class.getClassLoader().getResources(RESOURCE_NAME);

            while (resources.hasMoreElements()) {
                URL url = resources.nextElement();
                BufferedReader bufferedReader = new BufferedReader(new FileReader(url.getFile()));
                String line = bufferedReader.readLine();
                if (line == null || line.equals("")) {
                    continue;
                }

                String[] kvs = line.split(SEPARATOR);
                if (kvs.length != 2) {
                    continue;
                }

                String serviceName = kvs[0];
                String servicePathName = kvs[1];
                Class<?> forName = Class.forName(servicePathName);

                serviceCacheMap.put(serviceName, forName);
            }

            log.info("ServiceLoader registration service finished, number of registered services is {}", serviceCacheMap.size());
        } catch (Exception e) {
            log.error("ServiceLoader registration service has error occurred, msg = {}", e.getMessage());
        }
    }

    /**
     * 获取指定服务的类对象
     *
     * @param serviceName 服务名称
     */
    public static Class<?> getClass(String serviceName) {
        Class<?> clazz = serviceCacheMap.get(serviceName);
        if (clazz == null) {
            log.error("unregistered service, serviceName is {}", serviceName);
        }

        return clazz;
    }

    /**
     * 获取指定服务的实例对象
     *
     * @param serviceName 服务名称
     */
    @SuppressWarnings("unchecked")
    public static <T> T getInstance(String serviceName) {
        Object o = instanceCacheMap.get(serviceName);
        if (o != null) {
            return (T) o;
        }

        Class<?> clazz = serviceCacheMap.get(serviceName);
        if (clazz == null) {
            log.error("unregistered service, serviceName is {}", serviceName);
            return (T) new Object();
        }

        try {
            Object newInstance = clazz.newInstance();
            instanceCacheMap.put(serviceName, newInstance);
            return (T) newInstance;
        } catch (Exception e) {
            log.error("ServiceLoader#getInstance() has error occurred, msg = {}", e.getMessage());
        }

        return (T) new Object();
    }

}
