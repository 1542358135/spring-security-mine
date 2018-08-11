package com.cli.security.app.controller;

import com.cli.security.app.entity.User;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * 1.@JsonView指定对象返回字段
 * 2.path传参可以用正则校验，失败直接返回
 * 3.@Valid数据校验，可自定义
 * @author lc
 * @date 2018/6/13
 */
@RestController
@RequestMapping("/user")
public class HelloController {

    @GetMapping("/me")
    public Object getCurrentUser(Authentication user) {
        return user;
    }

    /**
     * RequestParam 参数非必须、默认值
     */
    @GetMapping
    @JsonView(User.SimpleUserView.class)
    public List<User> selectAll(@RequestParam(required = false, defaultValue = "lc") String username){
        List<User> users = new ArrayList<>(2);
        users.add(new User(username, "123"));
        users.add(new User(username, "456"));
        return users;
    }

    /**
     * PathVariable URL参数形式（“id:\\d+”代表只接受数字）
     */
    @GetMapping("/{id:\\d+}")
    @JsonView(User.DetialUserView.class)
    public User select(@PathVariable String id){
        return new User(id, "123");
    }

    /**
     * RequestBody入参装入实体，Valid根据实体的声明校验合法性
     */
    @PostMapping
    public User create(@Valid @RequestBody User user, BindingResult errors){
        if(errors.hasErrors()){
            errors.getAllErrors().forEach(error -> {
                FieldError fieldError =(FieldError)error;
                System.out.println(fieldError.getField()  + " " + fieldError.getDefaultMessage());
            });
        }
        System.out.println(user);
        return user;
    }

    @PutMapping("/{id:\\d+}")
    public User update(@Valid @RequestBody User user, BindingResult errors) {
        return user;
    }

    @DeleteMapping("/{id:\\d+}")
    public String delete(@PathVariable String id){
        return id;
    }
}
