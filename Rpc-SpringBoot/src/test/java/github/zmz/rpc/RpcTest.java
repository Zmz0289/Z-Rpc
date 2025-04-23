package github.zmz.rpc;

import github.zmz.service.TestServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RpcTest {

    @Autowired
    private TestServiceImpl testService;

    @Test
    public void test1() {
        String result = testService.test();
        System.out.println("result = " + result);
    }

}
