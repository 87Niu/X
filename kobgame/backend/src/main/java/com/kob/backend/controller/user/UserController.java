package com.kob.backend.controller.user;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kob.backend.pojo.User;
import com.kob.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/all")
    public List<User> getAll() {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        return userService.list(queryWrapper);
    }

    @GetMapping("/{userId}")
    public User getUser(@PathVariable int userId) {
        return userService.getById(userId);
    }

    @GetMapping("/add/{userId}/{username}/{password}")
    public String add(@PathVariable int userId, @PathVariable String username, @PathVariable String password) {

        if (password.length() < 6) {
            return "密码太短";
        }
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encoderPassword = passwordEncoder.encode(password);
        User user = new User(userId, username, encoderPassword, null);
        userService.save(user);
        return "Yes";
    }

    @GetMapping("/delete/{userId}")
    public String delete(@PathVariable int userId) {

        userService.removeById(userId);
        return "Yes";
    }






}
