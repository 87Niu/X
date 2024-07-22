package com.ruijie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruijie.common.CustomException;
import com.ruijie.mapper.CategoryMapper;
import com.ruijie.pojo.Category;
import com.ruijie.pojo.Dish;
import com.ruijie.pojo.Setmeal;
import com.ruijie.service.CategoryService;
import com.ruijie.service.DishService;
import com.ruijie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private DishService dishService;


    @Autowired
    private SetmealService setmealService;


    @Override
    public void remove(Long id) {

        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Dish::getCategoryId, id);

        int count = (int) dishService.count(wrapper);
        if (count > 0) {
            throw new CustomException("当前分类下有菜品,无法删除");
        }

        LambdaQueryWrapper<Setmeal> wrapper1 = new LambdaQueryWrapper<>();
        wrapper1.eq(Setmeal::getCategoryId, id);

        int count2 = (int) setmealService.count(wrapper1);
        if (count2 > 0) {
            throw new CustomException("当前分类下有套餐,无法删除");
        }

        super.removeById(id);
    }
}
