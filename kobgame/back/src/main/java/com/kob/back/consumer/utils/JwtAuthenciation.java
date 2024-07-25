package com.kob.back.consumer.utils;

import com.kob.back.utils.JwtUtil;
import io.jsonwebtoken.Claims;

public class JwtAuthenciation {

    public static int getUserId(String token) {
        int userId = -1;
        try {
            Claims claims = JwtUtil.parseJWT(token);
            userId = Integer.parseInt(claims.getSubject());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return userId;
    }
}
