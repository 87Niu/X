package com.kob.back.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kob.back.dto.UserDto;
import com.kob.back.pojo.User;

import java.util.Map;

public interface UserService extends IService<User> {

    Map<String, String> getToken(User user);

    Map<String, String> register(UserDto userDto);

    Map<String, String> getinfo();

}
