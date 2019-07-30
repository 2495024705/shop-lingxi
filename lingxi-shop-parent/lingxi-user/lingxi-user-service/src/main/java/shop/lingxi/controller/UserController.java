package shop.lingxi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import shop.lingxi.service.UserService;
import shop.lingxi.user.pojo.User;

import javax.validation.Valid;
import java.util.stream.Collectors;

@RestController
public class UserController {
    @Autowired
    private UserService service;
    @GetMapping(value = "/check/{data}/{type}")
    public ResponseEntity<Boolean> checkDate(@PathVariable("data")String data,@PathVariable("type") Integer type){
        return ResponseEntity.ok(service.checkData(data,type));
    }
    @PostMapping("code")
    public ResponseEntity<Void> sendCode(@RequestParam("phone")String phone){
        service.sendCode(phone);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("register")
    public ResponseEntity<Void> register(@Valid User user, BindingResult result, @RequestParam("code")String code){
       if (result.hasFieldErrors()){
throw new RuntimeException(result.getFieldErrors().stream()
        .map(e -> e.getDefaultMessage()).collect(Collectors.joining("|")));
       }

        service.register(user,code);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @GetMapping("/query")
    public ResponseEntity<User> queryUserByUser(@RequestParam("username") String username,@RequestParam("password") String password){

        return ResponseEntity.ok(service.queryUserByUser(username,password));
    }
}
