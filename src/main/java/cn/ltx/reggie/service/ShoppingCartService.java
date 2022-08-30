package cn.ltx.reggie.service;

import cn.ltx.reggie.common.R;
import cn.ltx.reggie.entity.ShoppingCart;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

/**
 * @PROJECT_NAME: reggie_take_out
 * @PACKAGE_NAME: cn.ltx.reggie.service
 * @CLASS_NAME: ShoppingCartService
 * @Author: 21130
 * @CreateTime: 2022-08-28  15:29
 * @Description:
 * @Version: 1.0
 */
@SuppressWarnings("ALL")
public interface ShoppingCartService extends IService<ShoppingCart> {
    /**
     * 往购物车里添加餐品
     * @param shoppingCart
     * @return
     */
    @Transactional
    R<ShoppingCart> add(ShoppingCart shoppingCart);

    /**
     * 购物车删减餐品
     * @param shoppingCart
     * @return
     */
    @Transactional
    R<ShoppingCart> sub(ShoppingCart shoppingCart);
}
