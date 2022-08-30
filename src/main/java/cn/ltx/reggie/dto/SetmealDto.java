package cn.ltx.reggie.dto;

import cn.ltx.reggie.entity.Setmeal;
import cn.ltx.reggie.entity.SetmealDish;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SetmealDto extends Setmeal implements Serializable {

    private static final long serialVersionUID = 6131948232621912376L;
    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
