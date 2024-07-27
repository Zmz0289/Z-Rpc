package github.zmz.service;

import github.zmz.domain.User;

/**
 * @author zmz
 * @create 2024-07-18
 */
public interface RemoteUserService {

    User get(String name);

}
