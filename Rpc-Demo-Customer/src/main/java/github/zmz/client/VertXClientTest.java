package github.zmz.client;

import github.zmz.domain.User;
import github.zmz.factory.ProxyFactory;
import github.zmz.service.UserService;

public class VertXClientTest {

    public static void main(String[] args) {

        UserService userService = ProxyFactory.newInstance(UserService.class);

        User user = userService.get("测试");

        System.out.println("user = " + user);

    }
}
