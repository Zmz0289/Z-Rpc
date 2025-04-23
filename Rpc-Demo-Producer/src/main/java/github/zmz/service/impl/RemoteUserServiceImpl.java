package github.zmz.service.impl;

import github.zmz.annotation.ZService;
import github.zmz.domain.User;
import github.zmz.service.RemoteUserService;

import java.math.BigDecimal;

public class RemoteUserServiceImpl implements RemoteUserService {

    @Override
    public User get(String name) {
        if ("error".equals(name)) {
            throw new IndexOutOfBoundsException();
        }

        System.out.println("name = " + name);

        User user = new User();
        user.setName(name);
        user.setAge(88);
        user.setMoney(BigDecimal.ONE);

        return user;
    }
}
