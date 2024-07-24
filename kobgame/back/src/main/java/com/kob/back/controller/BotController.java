package com.kob.back.controller;

import com.kob.back.pojo.Bot;
import com.kob.back.service.BotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user/bot")
public class BotController {

    @Autowired
    private BotService botService;

    @PostMapping("/add")
    public Map<String, String> add(@RequestBody Bot bot) {
        return botService.add(bot);
    }

    @DeleteMapping("/remove")
    public Map<String, String> remove(@RequestBody Bot bot) {
        return botService.remove(bot);
    }

    @PostMapping("/update")
    public Map<String, String> update(@RequestBody Bot bot) {
        return botService.update(bot);
    }

    @GetMapping("/getlist")
    public List<Bot> getList() {
        return botService.getList();
    }



}
