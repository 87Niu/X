package com.ruijie.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruijie.pojo.Category;
import com.ruijie.pojo.Setmeal;
import com.ruijie.service.CategoryService;
import com.ruijie.service.SetmealDishService;
import com.ruijie.service.SetmealService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruijie.common.R;
import com.ruijie.dto.SetmealDto;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SetmealService setmealService;
    @Autowired
    private SetmealDishService setmealDishService;

    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto) {
        setmealService.saveWithDish(setmealDto);
        return R.success("套餐添加成功");
    }


    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, @RequestParam(value = "name", required = false) String name) {
        Page<Setmeal> pageInfo = new Page<>(page, pageSize);
        Page<SetmealDto> dtoPage = new Page<>();
        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(name != null, Setmeal::getName, name);

        wrapper.orderByDesc(Setmeal::getUpdateTime);

        setmealService.page(pageInfo, wrapper);

        BeanUtils.copyProperties(pageInfo, dtoPage.getRecords(), "records");

        List<Setmeal> records = pageInfo.getRecords();

        List<SetmealDto> list = records.stream().map((item) -> {
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(item, setmealDto);
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if (category != null) {
                String categoryName = category.getName();
                setmealDto.setCategoryName(categoryName);
            }
            return setmealDto;
        }).collect(Collectors.toList());

        dtoPage.setRecords(list);
        return R.success(dtoPage);
    }

    @PostMapping("/status/0")
    public R<String> stop(@RequestParam List<Long> ids) {
        for (Long id : ids) {
            Setmeal setmeal=setmealService.getById(id);
            setmeal.setStatus(0);
            LambdaQueryWrapper<Setmeal> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(Setmeal::getId, id);
            setmealService.update(setmeal, lambdaQueryWrapper);
        }
        return R.success("更新状态为停售");
    }

    @PostMapping("/status/1")
    public R<String> startMail(@RequestParam List<Long> ids) {
        for (Long id : ids) {
            Setmeal setmeal=setmealService.getById(id);
            setmeal.setStatus(1);
            LambdaQueryWrapper<Setmeal> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(Setmeal::getId, id);
            setmealService.update(setmeal, lambdaQueryWrapper);
        }
        return R.success("更新状态为起售");
    }

    @DeleteMapping()
    public R<String> deleteSetmeal(@RequestParam List<Long> ids){
        setmealService.removeWithDish(ids);
        return R.success("删除成功");
    }

    @GetMapping("/list")
    public R<List<Setmeal>> list(Setmeal setmeal) {
        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(setmeal.getCategoryId() != null, Setmeal::getCategoryId, setmeal.getCategoryId());
        wrapper.eq(setmeal.getStatus() != null, Setmeal::getStatus, setmeal.getStatus());
        wrapper.orderByDesc(Setmeal::getUpdateTime);
        List<Setmeal> list = setmealService.list(wrapper);
        return R.success(list);
    }

}
