package cn.ltx.reggie.web.controller;

import cn.ltx.reggie.common.R;
import cn.ltx.reggie.dto.OrdersDto;
import cn.ltx.reggie.entity.Orders;
import cn.ltx.reggie.service.OrderService;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @PROJECT_NAME: reggie_take_out
 * @PACKAGE_NAME: cn.ltx.reggie.web.controller
 * @CLASS_NAME: OrderController
 * @Author: 21130
 * @CreateTime: 2022-08-28  15:26
 * @Description:
 * @Version: 1.0
 */
@RestController
@RequestMapping("/order")
@Slf4j
@SuppressWarnings("ALL")
public class OrderController {
    @Autowired
    private OrderService service;

    /**
     * 提交订单信息
     * @param orders
     * @return
     */
    @PostMapping("/submit")
    public R<String> submitOrders(@RequestBody Orders orders){
        log.info("orders => {}", orders);
        return service.submitOrders(orders);
    }

    /**
     * 订单列表分页查询
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/userPage")
    public R<IPage<OrdersDto>> userPage(int page, int pageSize){
        log.info("page => {},pageSize => {}", page, pageSize);
        return service.userPage(page, pageSize);
    }

    /**
     * 再来一单
     * @param orders
     * @return
     */
    @PostMapping("/again")
    public R<String>again(@RequestBody Orders orders){
        log.info("orders => {}", orders);
        return service.again(orders);
    }

    /**
     * 后台订单列表条件分页查询
     * @param page
     * @param pageSize
     * @param number
     * @param beginTime
     * @param endTime
     * @return
     */
    @GetMapping("/page")
    public R<IPage<OrdersDto>> page(int page, int pageSize, String number, String beginTime, String endTime){
        log.info("page => {},pageSize => {}, number => {}, beginTime => {}, endTime => {}", page, pageSize, number, beginTime, endTime);
        return service.page(page, pageSize, number, beginTime, endTime);
    }
    @PutMapping
    public R<String> updateStatus(@RequestBody Orders orders){
        log.info("orders => {}", orders);
        service.update(new UpdateWrapper<Orders>().eq("id", orders.getId()).set("status", orders.getStatus()));
        return R.success("success");
    }


}
