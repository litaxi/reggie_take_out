package cn.ltx.reggie.service.impl;

import cn.ltx.reggie.common.CustomException;
import cn.ltx.reggie.common.R;
import cn.ltx.reggie.dto.DishDto;
import cn.ltx.reggie.dto.SetmealDto;
import cn.ltx.reggie.entity.Category;
import cn.ltx.reggie.entity.Setmeal;
import cn.ltx.reggie.entity.SetmealDish;
import cn.ltx.reggie.mapper.SetmealMapper;
import cn.ltx.reggie.service.CategoryService;
import cn.ltx.reggie.service.DishService;
import cn.ltx.reggie.service.SetmealDishService;
import cn.ltx.reggie.service.SetmealService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @PROJECT_NAME: reggie_take_out
 * @PACKAGE_NAME: cn.ltx.reggie.service.impl
 * @CLASS_NAME: SetmealServiceImpl
 * @Author: 21130
 * @CreateTime: 2022-08-24  17:02
 * @Description:
 * @Version: 1.0
 */
@SuppressWarnings("ALL")
@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
    @Autowired
    private SetmealDishService setmealDishService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private DishService dishService;
    @Override
    public void updateStatus(int status, List<Long> ids) {
        QueryWrapper<Setmeal> wrapper = new QueryWrapper<>();
        wrapper.in("id", ids);
        Setmeal setmeal = new Setmeal();
        setmeal.setStatus(status);
        this.update(setmeal,wrapper);
    }

    @Override
    public void delete(List<Long> ids) {
        QueryWrapper<Setmeal> wrapper = new QueryWrapper<>();
        wrapper.in("id",ids).eq("status", 1);
        int count = this.count(wrapper);
        if(count > 0){
            throw new CustomException("售卖中的套餐无法删除，请先停售该套餐");
        }
        QueryWrapper<Setmeal> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id", ids);
        this.remove(queryWrapper);
    }

    @Override
    public void saveWithDish(SetmealDto setmealDto) {
        //先保存套餐信息
        this.save(setmealDto);
        List<SetmealDish> setmealDishList = setmealDto.getSetmealDishes().stream().map((item) -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());
        //保存套餐和菜品的关联信息，操作setmeal_dish,执行insert操作
        setmealDishService.saveBatch(setmealDishList);
    }

    @Override
    public SetmealDto getByIdWithDish(Long id) {
        Setmeal setmeal = this.getById(id);
        SetmealDto setmealDto = new SetmealDto();
        List<SetmealDish> setmealDishList = setmealDishService.list(new QueryWrapper<SetmealDish>().eq("setmeal_id", id));
        BeanUtils.copyProperties(setmeal, setmealDto);
        setmealDto.setSetmealDishes(setmealDishList);
        return setmealDto;
    }

    @Override
    public void updateWithDish(SetmealDto setmealDto) {
        this.updateById(setmealDto);
        setmealDishService.removeById(setmealDto.getId());
        List<SetmealDish> setmealDishList = setmealDto.getSetmealDishes().stream().map((item) -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());
        setmealDishService.saveBatch(setmealDishList);
    }

    @Override
    public R<IPage<SetmealDto>> page(int page, int pageSize, String name) {
        //封装分页参数
        IPage<Setmeal> pageInfo = new Page<>(page, pageSize);
        IPage<SetmealDto> setmealDtoIPage = new Page<>();

        //添加条件查询
        QueryWrapper<Setmeal> wrapper = new QueryWrapper<>();
        //name不为空时添加
        wrapper.like(StringUtils.isNotBlank(StringUtils.trim(name)), "name", name)
                .eq("status",1)
                .orderByDesc("update_time");
        //菜品分页信息
        pageInfo = this.page(pageInfo, wrapper);
        //属性拷贝
        BeanUtils.copyProperties(pageInfo,setmealDtoIPage,"records");
        //封装DishDto对象里面填充分类名称字段用于页面展示
        List<SetmealDto> records = pageInfo.getRecords().stream().map((item) -> {
            SetmealDto setmealDto = new SetmealDto();
            //属性拷贝
            BeanUtils.copyProperties(item, setmealDto, "records");
            Long categoryId = item.getCategoryId();
            //根据菜品id查询分类信息
            Category category = categoryService.getById(categoryId);
            if (category != null) {
                //填充分类名称
                setmealDto.setCategoryName(category.getName());
            }
            return setmealDto;
        }).collect(Collectors.toList());
        //封装DishDto分页信息
        setmealDtoIPage.setRecords(records);
        return R.success(setmealDtoIPage);
    }

    @Override
    public R<List<SetmealDto>> list(Setmeal setmeal) {
        QueryWrapper<Setmeal> wrapper = new QueryWrapper<>();
        wrapper.eq("category_id", setmeal.getCategoryId()).eq("status", setmeal.getStatus());
        //List<Setmeal> list = this.list(wrapper);
        List<SetmealDto> setmealDtoList = this.list(wrapper).stream().map((item) -> {
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(item, setmealDto);
            QueryWrapper<SetmealDish> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("setmeal_id",item.getId());
            List<SetmealDish> setmealDishList = setmealDishService.list(queryWrapper);
            setmealDto.setSetmealDishes(setmealDishList);
            return setmealDto;
        }).collect(Collectors.toList());
        return R.success(setmealDtoList);
    }

    /**
     * 根据套餐id查询出对应的所有菜品信息及其口味信息
     * @param id
     * @return
     */
    @Override
    public R<List<DishDto>> setmealDishList(Long id) {
        //1、先拿到到套餐所对应的菜品集合
        List<SetmealDish> setmealDishList = setmealDishService.list(new QueryWrapper<SetmealDish>().eq("setmeal_id", id));
        //2、获取集合下的菜品id并查询出对应的菜品信息及其口味
        List<DishDto> dishDtoList = setmealDishList.stream().map((item) -> {
            Long dishId = item.getDishId();
            DishDto dishDto = dishService.getByIdWithFlavor(dishId);
            dishDto.setCopies(item.getCopies());
            return dishDto;
        }).collect(Collectors.toList());
        return R.success(dishDtoList);
    }
}
