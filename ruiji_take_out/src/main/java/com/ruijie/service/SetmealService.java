package com.ruijie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruijie.dto.SetmealDto;
import com.ruijie.pojo.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {
    public void saveWithDish(SetmealDto setmealDto);


    public void removeWithDish(List<Long> ids);
}
