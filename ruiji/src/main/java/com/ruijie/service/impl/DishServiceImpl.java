package com.ruijie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruijie.dto.DishDto;
import com.ruijie.mapper.DishMapper;
import com.ruijie.pojo.Dish;
import com.ruijie.pojo.DishFlavor;
import com.ruijie.service.DishFlavorService;
import com.ruijie.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private DishFlavorService dishFlavorService;

    @Transactional
    public void saveWithFlavor(DishDto dishDto) {
        this.save(dishDto);

        Long dishId = dishDto.getId();

        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().map((item) -> {
           item.setDishId(dishId);
           return item;
        }).collect(Collectors.toList());
        dishFlavorService.saveBatch(flavors);
    }

    public DishDto getByIdWithFlavor(Long id) {

        Dish dish = this.getById(id);

        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish, dishDto);

        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, dish.getId());
        List<DishFlavor> flavors = dishFlavorService.list(queryWrapper);

        dishDto.setFlavors(flavors);
        return dishDto;
    }

    public void updateWithFlavor(DishDto dishDto) {
         this.updateById(dishDto);

         LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
         queryWrapper.eq(DishFlavor::getDishId, dishDto.getId());

         dishFlavorService.remove(queryWrapper);

         List<DishFlavor> flavors = dishDto.getFlavors();
         flavors = flavors.stream().map((item) -> {
             item.setDishId(dishDto.getId());
             return item;
         }).collect(Collectors.toList());

         dishFlavorService.saveBatch(flavors);
    }


}
