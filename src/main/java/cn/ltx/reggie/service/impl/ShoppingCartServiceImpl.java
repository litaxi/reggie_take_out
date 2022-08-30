package cn.ltx.reggie.service.impl;

import cn.ltx.reggie.common.BaseContext;
import cn.ltx.reggie.common.R;
import cn.ltx.reggie.entity.ShoppingCart;
import cn.ltx.reggie.mapper.ShoppingCartMapper;
import cn.ltx.reggie.service.ShoppingCartService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @PROJECT_NAME: reggie_take_out
 * @PACKAGE_NAME: cn.ltx.reggie.service.impl
 * @CLASS_NAME: ShoppingCartServiceImpl
 * @Author: 21130
 * @CreateTime: 2022-08-28  15:34
 * @Description:
 * @Version: 1.0
 */
@Service
@SuppressWarnings("ALL")
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
    /**
     * 往购物车里添加餐品
     *
     * @param shoppingCart
     * @return
     */
    @Override
    public R<ShoppingCart> add(ShoppingCart shoppingCart) {
        //设置用户
        shoppingCart.setUserId(BaseContext.getCurrentId());
        Long setmealId = shoppingCart.getSetmealId();
        Long dishId = shoppingCart.getDishId();
        QueryWrapper<ShoppingCart> wrapper = new QueryWrapper<>();
        //加入的为套餐
        if (setmealId != null) {
            wrapper.eq("setmeal_id", setmealId);
        }
        //加入的为菜品
        else if (dishId != null) {
            wrapper.eq("dish_id", dishId);
        }
        //判断购物车中是否有该餐品
        ShoppingCart setmealOrDish = this.getOne(wrapper);

        if (setmealOrDish != null) {
            //已存在，直接数量+1
            Integer number = setmealOrDish.getNumber();
            setmealOrDish.setNumber(number + 1);
            //并更新
            this.updateById(setmealOrDish);

        } else {
            //不存在直接加入购物车
            this.save(shoppingCart);
            setmealOrDish = shoppingCart;
        }

        return R.success(setmealOrDish);
    }

    /**
     * 购物车删减餐品
     *
     * @param shoppingCart
     * @return
     */
    @Override
    public R<ShoppingCart> sub(ShoppingCart shoppingCart) {
        //设置用户
        shoppingCart.setUserId(BaseContext.getCurrentId());
        Long setmealId = shoppingCart.getSetmealId();
        Long dishId = shoppingCart.getDishId();
        QueryWrapper<ShoppingCart> wrapper = new QueryWrapper<>();
        //删减的为套餐
        if (setmealId != null) {
            wrapper.eq("setmeal_id", setmealId);
        }
        //删减的为菜品
        else if (dishId != null) {
            wrapper.eq("dish_id", dishId);
        }
        //判断购物车中该餐品的数量
        ShoppingCart setmealOrDish = this.getOne(wrapper);
        Integer number = setmealOrDish.getNumber();
        if (number > 1) {
            //已存在直接数量+1
            setmealOrDish.setNumber(number - 1);
            //并更新
            this.updateById(setmealOrDish);
        } else if (number == 1) {
            //=1
            this.removeById(setmealOrDish.getId());
            setmealOrDish = shoppingCart;
        }

        return R.success(setmealOrDish);
    }
}
