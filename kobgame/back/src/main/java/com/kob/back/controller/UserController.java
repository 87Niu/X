package com.kob.back.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kob.back.dto.UserDto;
import com.kob.back.pojo.User;
import com.kob.back.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryAutoConfiguration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @GetMapping("/all")
    public List<User> getAll() {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        return userService.list(queryWrapper);
    }

    @GetMapping("/{userId}")
    public User get(@PathVariable int userId) {
        return userService.getById(userId);
    }

    @PostMapping("/adduser")
    public String addUser(@RequestBody User user) {
        if (user.getPassword().length() < 6) {
            return "密码太短";
        }
        String password = passwordEncoder.encode(user.getPassword());
        user.setPassword(password);
        userService.save(user);
        return "success";
    }

    @GetMapping("/delete/{userId}")
    public String deleteUser(@PathVariable int userId) {
        userService.removeById(userId);
        return "success";
    }


    @PostMapping("/account/token")
    public Map<String, String> getToken(@RequestBody User user) {
        return userService.getToken(user);
    }


    @GetMapping("/account/info")
    public Map<String, String> getInfo() {
        return userService.getinfo();
    }


    @PostMapping("/account/register")
    public Map<String, String> register(@RequestBody UserDto userDto) {
        return userService.register(userDto);
    }




}
