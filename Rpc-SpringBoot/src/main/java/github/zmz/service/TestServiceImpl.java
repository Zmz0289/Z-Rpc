package github.zmz.service;

import github.zmz.annotation.ZReference;
import github.zmz.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TestServiceImpl {

    @ZReference()
    private RemoteUserService remoteUserService;

    public String test(){
        User user = remoteUserService.get("error");
        log.error("user = {}", user);

        return user.getName();
    }


}
