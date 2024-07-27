package com.kob.back.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kob.back.mapper.RecordMapper;
import com.kob.back.service.RecordService;
import org.springframework.stereotype.Service;


@Service
public class RecordServiceImpl extends ServiceImpl<RecordMapper, Record> implements RecordService {
}
