package com.kob.back.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kob.back.pojo.Bot;

import java.util.List;
import java.util.Map;


public interface BotService extends IService<Bot> {

    Map<String, String> add(Bot bot);

    Map<String, String> remove(Bot bot);

    Map<String, String> update(Bot bot);

    List<Bot> getList();
}
