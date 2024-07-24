package com.kob.back.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kob.back.dto.UserDto;
import com.kob.back.mapper.UserMapper;
import com.kob.back.pojo.User;
import com.kob.back.service.UserService;
import com.kob.back.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public Map<String, String> getToken(User user) {
       UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
       Authentication authentication = authenticationManager.authenticate(authenticationToken);
       UserDetailsImpl loginUser = (UserDetailsImpl) authentication.getPrincipal();
       User nuser = loginUser.getUser();
       String jwt = JwtUtil.createJWT(nuser.getId().toString());
       Map<String, String> map = new HashMap<>();
       map.put("token", jwt);
       map.put("error_message", "success");
       return map;
    }

    @Override
    public Map<String, String> register(UserDto userDto) {
        Map<String, String> map = new HashMap<>();
        if (userDto.getUsername() == null || userDto.getUsername().isEmpty()) {
            map.put("error_message", "用户名不能为空");
            return map;
        }
        if (userDto.getPassword() == null || userDto.getPassword().isEmpty() || userDto.getConfirmPassword() == null) {
            map.put("error_message", "密码不能为空");
            return map;
        }

        String username = userDto.getUsername().trim();
        if (username.isEmpty()) {
            map.put("error_message", "用户名不能为空格");
            return map;
        }

        if (username.length() > 100) {
            map.put("error_message", "用户名太长");
            return map;
        }
        userDto.setUsername(username);

        if (!userDto.getPassword().equals(userDto.getConfirmPassword())) {
            map.put("error_message", "两次密码不一致");
            return map;
        }

        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, userDto.getUsername());

        List<User> list = list(queryWrapper);
        if (!list.isEmpty()) {
            map.put("error_message", "用户名存在");
            return map;
        }


        String photo = "https://img0.baidu.com/it/u=3086087017,712520284&fm=253&fmt=auto&app=120&f=JPEG?w=500&h=500";
        this.save(new User(null, userDto.getUsername(), passwordEncoder.encode(userDto.getPassword()), photo));
        map.put("error_message", "success");
        return map;
    }

    @Override
    public Map<String, String> getinfo() {
        UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();
        User nuser = loginUser.getUser();

        Map<String, String> map = new HashMap<>();
        map.put("error_message", "success");
        map.put("id", nuser.getId().toString());
        map.put("username", nuser.getUsername());
        map.put("photo", nuser.getPhoto());
        return map;
    }

}
