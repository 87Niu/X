package com.ruijie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruijie.mapper.ShoppingCartMapper;
import com.ruijie.pojo.ShoppingCart;
import com.ruijie.service.ShoppingCartService;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {

}
