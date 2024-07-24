package com.kob.back.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kob.back.mapper.BotMapper;
import com.kob.back.pojo.Bot;
import com.kob.back.pojo.User;
import com.kob.back.service.BotService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BotServiceImpl extends ServiceImpl<BotMapper, Bot> implements BotService {

    @Override
    public Map<String, String> add(Bot bot) {
        UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();
        User user = loginUser.getUser();

        Map<String, String> map = new HashMap<>();

        if (bot.getTitle() == null || bot.getTitle().isEmpty()) {
            map.put("error_message", "标题不能为空");
            return map;
        }

        if (bot.getContent() == null || bot.getContent().isEmpty()) {
            map.put("error_message", "内容不能为空");
            return map;
        }

        if (bot.getDescription() == null || bot.getDescription().isEmpty()) {
            bot.setDescription("这个用户很懒,什么也没留下~");
        }

        bot.setCreateTime(new Date());
        bot.setUpdateTime(new Date());
        bot.setUserId(user.getId());
        this.save(bot);

        map.put("error_message", "success");
        return map;
    }

    @Override
    public Map<String, String> remove(Bot bot) {
        UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();
        User user = loginUser.getUser();

        Map<String, String> map = new HashMap<>();

        int botId = Integer.parseInt(String.valueOf(bot.getId()));

        Bot bots = this.getById(botId);
        if (bots == null) {
            map.put("error_message", "咩有这个bot");
            return map;
        }
        if (!bots.getUserId().equals(user.getId())) {
            map.put("error_message", "咩有权限删除");
            return map;
        }
        this.removeById(botId);
        map.put("error_message", "success");
        return map;
    }

    @Override
    public Map<String, String> update(Bot bot) {
        UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();
        User user = loginUser.getUser();

        Map<String, String> map = new HashMap<>();
        int botId = Integer.parseInt(String.valueOf(bot.getId()));
        Bot bots = this.getById(botId);
        if (bots == null) {
            map.put("error_message", "咩有这个bot");
            return map;
        }
        if (!bots.getUserId().equals(user.getId())) {
            map.put("error_message", "咩有权限修改");
            return map;
        }
        bot.setUpdateTime(new Date());
        this.updateById(bot);

        map.put("error_message", "success");
        return map;
    }

    @Override
    public List<Bot> getList() {
        UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();
        User user = loginUser.getUser();

        LambdaQueryWrapper<Bot> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Bot::getUserId, user.getId());
        return this.list(queryWrapper);
    }
}
