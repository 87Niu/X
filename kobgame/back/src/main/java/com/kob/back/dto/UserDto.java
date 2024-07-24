package com.kob.back.dto;

import com.kob.back.pojo.User;
import lombok.Data;

@Data
public class UserDto extends User {
    private String confirmPassword;
}
