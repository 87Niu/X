package com.ruijie.dto;

import com.ruijie.pojo.Setmeal;
import com.ruijie.pojo.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
