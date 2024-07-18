package github.zmz.service.impl;

import github.zmz.domain.User;
import github.zmz.service.UserService;

import java.math.BigDecimal;

public class RemoteUserServiceImpl implements UserService {

    @Override
    public User get(String name) {
        System.out.println("name = " + name);

        User user = new User();
        user.setName("远程的user");
        user.setAge(88);
        user.setMoney(BigDecimal.ONE);

        return user;
    }
}
