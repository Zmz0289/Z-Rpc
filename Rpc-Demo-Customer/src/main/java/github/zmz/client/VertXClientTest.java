package github.zmz.client;

import github.zmz.domain.User;
import github.zmz.factory.ProxyFactory;
import github.zmz.service.RemoteUserService;

public class VertXClientTest {

    public static void main(String[] args) {

        RemoteUserService remoteUserService = ProxyFactory.newInstance(RemoteUserService.class);

        User user = remoteUserService.get("error1");

        System.out.println("user = " + user);

    }
}
