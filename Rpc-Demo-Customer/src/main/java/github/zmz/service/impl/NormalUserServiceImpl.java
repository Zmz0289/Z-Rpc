package github.zmz.service.impl;

import github.zmz.domain.User;
import github.zmz.service.UserService;

import java.math.BigDecimal;

/**
 * @author zmz
 * @create 2024-07-18
 */
public class NormalUserServiceImpl implements UserService {

    @Override
    public User get(String name) {
        User user = new User();
        user.setName(name);
        user.setAge(24);
        user.setMoney(BigDecimal.TEN);

        return user;
    }
}
