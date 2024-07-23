package com.kob.backend.service.account;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kob.backend.pojo.User;

import java.util.Map;

public interface LoginService extends IService<User> {
    Map<String, String> getToken(String username, String password);
}
