package cn.ltx.reggie.dto;

import cn.ltx.reggie.entity.Dish;
import cn.ltx.reggie.entity.DishFlavor;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class DishDto extends Dish implements Serializable {

    private static final long serialVersionUID = 6062471678390781482L;
    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;
}
