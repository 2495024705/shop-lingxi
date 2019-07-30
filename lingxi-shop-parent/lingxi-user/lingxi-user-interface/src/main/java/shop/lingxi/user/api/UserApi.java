package shop.lingxi.user.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import shop.lingxi.user.pojo.User;

public interface UserApi {

        @GetMapping("/query")
        User queryUserByUser(@RequestParam("username") String username, @RequestParam("password") String password);


}
