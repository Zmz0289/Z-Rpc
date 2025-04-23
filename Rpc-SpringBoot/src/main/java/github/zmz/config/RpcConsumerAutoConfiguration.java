package github.zmz.config;

import github.zmz.annotation.ZReference;
import github.zmz.factory.ProxyFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Field;

@Configuration
public class RpcConsumerAutoConfiguration {

    @Bean
    public BeanPostProcessor beanPostProcessor() {
        return new BeanPostProcessor() {

            @Override
            public Object postProcessBeforeInitialization(Object bean, String beanName)
                    throws BeansException {
                Class<?> objClz;
                if (AopUtils.isAopProxy(bean)) {
                    objClz = AopUtils.getTargetClass(bean);
                } else {
                    objClz = bean.getClass();
                }

                try {
                    for (Field field : objClz.getDeclaredFields()) {
                        // 判断该字段是否有 Reference 注解
                        ZReference reference = field.getAnnotation(ZReference.class);
                        if (reference != null) {
                            Class<?> type = field.getType();

                            Object o = ProxyFactory.newInstance(type);

                            field.setAccessible(true);
                            field.set(bean, o);
                        }
                    }
                } catch (Exception e) {
                    throw new BeanCreationException(beanName, e);
                }
                return bean;
            }
        };
    }
}
