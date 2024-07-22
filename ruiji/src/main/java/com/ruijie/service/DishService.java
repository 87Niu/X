package com.ruijie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruijie.dto.DishDto;
import com.ruijie.pojo.Dish;

public interface DishService extends IService<Dish> {

    public void saveWithFlavor(DishDto dishDto);

    public DishDto getByIdWithFlavor(Long id);

    public void updateWithFlavor(DishDto dishDto);

}
