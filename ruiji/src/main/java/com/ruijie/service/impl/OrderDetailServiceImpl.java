package com.ruijie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruijie.mapper.OrderDetailMapper;
import com.ruijie.pojo.OrderDetail;
import com.ruijie.service.OrderDetailService;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {

}