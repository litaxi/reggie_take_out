package cn.ltx.reggie.service.impl;

import cn.ltx.reggie.common.CustomException;
import cn.ltx.reggie.common.R;
import cn.ltx.reggie.dto.DishDto;
import cn.ltx.reggie.entity.Category;
import cn.ltx.reggie.entity.Dish;
import cn.ltx.reggie.entity.DishFlavor;
import cn.ltx.reggie.entity.SetmealDish;
import cn.ltx.reggie.mapper.DishMapper;
import cn.ltx.reggie.service.CategoryService;
import cn.ltx.reggie.service.DishFlavorService;
import cn.ltx.reggie.service.DishService;
import cn.ltx.reggie.service.SetmealDishService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @PROJECT_NAME: reggie_take_out
 * @PACKAGE_NAME: cn.ltx.reggie.service.impl
 * @CLASS_NAME: DishServiceImpl
 * @Author: 21130
 * @CreateTime: 2022-08-24  17:03
 * @Description:
 * @Version: 1.0
 */
@SuppressWarnings("ALL")
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    @Autowired
    private DishFlavorService dishFlavorService;
    @Autowired
    private SetmealDishService setmealDishService;
    @Autowired
    private CategoryService categoryService;

    /**
     * 根据菜品id获取菜品信息并封装口味信息
     * @param id
     * @return
     */
    @Override
    public DishDto getByIdWithFlavor(Long id) {
        Dish dish = this.getById(id);
        DishDto dishDto = new DishDto();
        List<DishFlavor> dishFlavorList = dishFlavorService.list(new QueryWrapper<DishFlavor>().eq("dish_id", id));
        BeanUtils.copyProperties(dish, dishDto);
        dishDto.setFlavors(dishFlavorList);
        return dishDto;
    }

    /**
     * 修改菜品信息并更新菜品口味与分类
     * @param dishDto
     */
    @Override
    public void updateWithFlavor(DishDto dishDto) {
        //先更新菜品信息
        this.updateById(dishDto);
        //再移除所有口味信息
        dishFlavorService.removeById(dishDto.getId());
        //为所有口味信心绑定菜品id
        List<DishFlavor> dishFlavorList = dishDto.getFlavors().stream().map((item) -> {
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());
        //添加口味信息
        dishFlavorService.saveBatch(dishFlavorList);
    }

    /**
     * 批量停售起售
     * @param status
     * @param ids
     */
    @Override
    public void updateStatus(int status, List<Long> ids) {
        QueryWrapper<Dish> wrapper = new QueryWrapper<>();
        wrapper.in("id", ids);
        Dish dish = new Dish();
        dish.setStatus(status);
        if(status == 0) {
            //如果停售，从套餐菜品表中移除菜品
            QueryWrapper<SetmealDish> queryWrapper = new QueryWrapper<>();
            queryWrapper.in("dish_id", ids);
            setmealDishService.remove(queryWrapper);
        }
        this.update(dish, wrapper);

    }

    /**
     * 根据id删除菜品信息
     * @param ids
     */
    @Override
    public void delete(@RequestParam List<Long> ids) {
        QueryWrapper<Dish> wrapper = new QueryWrapper<>();
        wrapper.in("id",ids).eq("status", 1);
        int count = this.count(wrapper);
        if(count > 0){
            throw new CustomException("售卖中的菜品无法删除，请先停售该菜品");
        }
        QueryWrapper<Dish> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id" , ids);
        this.remove(queryWrapper);
    }

    /**
     * 菜品分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @Override
    public R<IPage<DishDto>> page(int page, int pageSize, String name) {
        //封装分页参数
        IPage<Dish> pageInfo = new Page<>(page, pageSize);
        IPage<DishDto> dishDtoIPage = new Page<>();

        //添加条件查询
        QueryWrapper<Dish> wrapper = new QueryWrapper<>();
        //name不为空时添加
        wrapper.like(StringUtils.isNotBlank(StringUtils.trim(name)), "name", name).orderByDesc("update_time");
        //菜品分页信息
        pageInfo = this.page(pageInfo, wrapper);
        //属性拷贝
        BeanUtils.copyProperties(pageInfo,dishDtoIPage,"records");
        //封装DishDto对象里面填充分类名称字段用于页面展示
        List<DishDto> records = pageInfo.getRecords().stream().map((item) -> {
            DishDto dishDto = new DishDto();
            //属性拷贝
            BeanUtils.copyProperties(item, dishDto, "records");
            Long categoryId = item.getCategoryId();
            //根据菜品id查询分类信息
            Category category = categoryService.getById(categoryId);
            if (category != null) {
                //填充分类名称
                dishDto.setCategoryName(category.getName());
            }
            return dishDto;
        }).collect(Collectors.toList());
        //封装DishDto分页信息
        dishDtoIPage.setRecords(records);
        return R.success(dishDtoIPage);
    }

    /**
     * 根据id查询所有菜品信息
     * @param dish
     * @return
     */
    @Override
    public R<List<DishDto>> list(Dish dish) {
        QueryWrapper<Dish> wrapper = new QueryWrapper<>();
        //起售状态下的
        wrapper.eq(dish.getCategoryId() != null,"category_id", dish.getCategoryId()).eq("status", 1).like(StringUtils.isNotBlank(StringUtils.trim(dish.getName())),"name", StringUtils.trim(dish.getName()));
        //List<Dish> dishList = this.list(wrapper);
        List<DishDto> dishDtoList = this.list(wrapper).stream().map((item) -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);
            QueryWrapper<DishFlavor> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("dish_id", item.getId());
            List<DishFlavor> dishFlavorList = dishFlavorService.list(queryWrapper);
            dishDto.setFlavors(dishFlavorList);
            return dishDto;
        }).collect(Collectors.toList());
        return R.success(dishDtoList);
    }
}
