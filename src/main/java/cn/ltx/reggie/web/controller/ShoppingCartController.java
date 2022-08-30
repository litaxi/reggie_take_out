package cn.ltx.reggie.web.controller;

import cn.ltx.reggie.common.BaseContext;
import cn.ltx.reggie.common.R;
import cn.ltx.reggie.entity.ShoppingCart;
import cn.ltx.reggie.service.ShoppingCartService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @PROJECT_NAME: reggie_take_out
 * @PACKAGE_NAME: cn.ltx.reggie.web.controller
 * @CLASS_NAME: ShoppingCartController
 * @Author: 21130
 * @CreateTime: 2022-08-28  15:25
 * @Description:
 * @Version: 1.0
 */
@RestController
@RequestMapping("/shoppingCart")
@Slf4j
@SuppressWarnings("ALL")
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService service;

    /**
     * 查看购物车信息
     * @return
     */
    @GetMapping("/list")
    public R<List<ShoppingCart>> list(){
        log.info("用户{}，查看购物车信息", BaseContext.getCurrentId());
        List<ShoppingCart> list = service.list(new QueryWrapper<ShoppingCart>().eq("user_id", BaseContext.getCurrentId()));
        return R.success(list);
    }

    /**
     * 向购物车里添加餐品
     * @param shoppingCart
     * @return
     */
    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart){
        log.info("shoppingCart => {}", shoppingCart);
        return service.add(shoppingCart);
    }
    /**
     * 向购物车里删减餐品
     * @param shoppingCart
     * @return
     */
    @PostMapping("/sub")
    public R<ShoppingCart> sub(@RequestBody ShoppingCart shoppingCart){
        log.info("shoppingCart => {}", shoppingCart);
        return service.sub(shoppingCart);
    }


    @DeleteMapping("/clean")
    public R<String> clean(){
        log.info("用户{}",BaseContext.getCurrentId());
        service.remove(new QueryWrapper<ShoppingCart>().eq("user_id", BaseContext.getCurrentId()));
        return R.success("清空购物车成功");
    }
}
