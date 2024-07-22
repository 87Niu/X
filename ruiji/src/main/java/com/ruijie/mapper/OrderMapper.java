package com.ruijie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruijie.pojo.Orders;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper extends BaseMapper<Orders> {

}