package github.zmz.handler;

import github.zmz.domain.User;
import github.zmz.factory.ProxyFactory;
import github.zmz.service.UserService;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @author zmz
 * @create 2024-07-18
 */
public class RpcInvocationHandler implements InvocationHandler {

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        System.out.println("proxy.getClass().getName() = " + proxy.getClass().getName());

        Class<?>[] interfaces = proxy.getClass().getInterfaces();
        System.out.println(Arrays.toString(interfaces));


        System.out.println("method.getClass().getName() = " + method.getClass().getName());

        System.out.println("method.getName() = " + method.getName());

        System.out.println(Arrays.toString(args));


        // 在此处发起 Rpc 调用

        return null;
    }

    public static void main(String[] args) {

        UserService userService = ProxyFactory.newInstance(UserService.class);

        User user = userService.get("123");

        System.out.println("user = " + user);

    }
}
