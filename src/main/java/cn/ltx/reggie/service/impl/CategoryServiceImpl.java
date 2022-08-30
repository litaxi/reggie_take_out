package cn.ltx.reggie.service.impl;

import cn.ltx.reggie.common.CustomException;
import cn.ltx.reggie.common.R;
import cn.ltx.reggie.entity.Category;
import cn.ltx.reggie.entity.Dish;
import cn.ltx.reggie.entity.Setmeal;
import cn.ltx.reggie.mapper.CategoryMapper;
import cn.ltx.reggie.service.CategoryService;
import cn.ltx.reggie.service.DishService;
import cn.ltx.reggie.service.SetmealService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @PROJECT_NAME: reggie_take_out
 * @PACKAGE_NAME: cn.ltx.reggie.service.impl
 * @CLASS_NAME: CategoryServiceImpl
 * @Author: 21130
 * @CreateTime: 2022-08-24  16:57
 * @Description:
 * @Version: 1.0
 */
@SuppressWarnings("ALL")
@Service
@Slf4j
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    private DishService dishService;
    @Autowired
    private SetmealService setmealService;

    /**
     * 删除分类信息，需要判断该分类下是否有关联菜品和套餐，如都没有关联即可直接删除
     * @param categoryId
     */
    @Override
    public void remove(Long categoryId)  {
        int count;
        //1、判断该分类下是否有关联菜品
        count = dishService.count(new QueryWrapper<Dish>().eq("category_id", categoryId));
        if(count > 0) {
            throw new CustomException("该分类下关联的有菜品，请先删除该分类下的菜品，再来删除该分类");
        }
        count = setmealService.count(new QueryWrapper<Setmeal>().eq("category_id", categoryId));
        if(count > 0) {
            throw new CustomException("该分类下关联的有套餐，请先删除该分类下的套餐，再来删除该分类");
        }
        super.removeById(categoryId);
    }

    /**
     * 菜品套餐分类查询
     * @param page
     * @param pageSize
     * @return
     */
    @Override
    public R<IPage<Category>> page(int page, int pageSize) {
        QueryWrapper<Category> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("sort");
        IPage<Category> pageInfo = new Page<>(page,pageSize);
        pageInfo = this.page(pageInfo, wrapper);
        log.info("total => {}", pageInfo.getTotal());
        log.info("list => {}", pageInfo.getRecords());
        return R.success(pageInfo);
    }

    /**
     * 根据id查询菜品套餐分类列表
     * @param category
     * @return
     */
    @Override
    public R<List<Category>> list(Category category) {
        List<Category> list = this.list(new QueryWrapper<Category>().eq(category.getType()!=null,"type", category.getType()).orderByDesc("sort").orderByDesc("update_time"));
        return R.success(list);
    }
}
