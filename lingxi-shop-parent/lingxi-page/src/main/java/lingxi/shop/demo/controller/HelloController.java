package lingxi.shop.demo.controller;

import lingxi.shop.demo.pojo.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HelloController {
    @RequestMapping(value = "/hello")
    public String tole(Model model) {
       // model.addAttribute("msg","dsdsdsd");
        User user=new User();
        user.setAge(21);
        user.setName("xu");
        model.addAttribute(user);
       return "hello";
    }

}
