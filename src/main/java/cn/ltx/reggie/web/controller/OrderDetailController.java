package cn.ltx.reggie.web.controller;

import cn.ltx.reggie.service.OrderDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @PROJECT_NAME: reggie_take_out
 * @PACKAGE_NAME: cn.ltx.reggie.web.controller
 * @CLASS_NAME: OrderDetailController
 * @Author: 21130
 * @CreateTime: 2022-08-28  15:27
 * @Description:
 * @Version: 1.0
 */
@RestController
@RequestMapping("/orderDetail")
@Slf4j
@SuppressWarnings("ALL")
public class OrderDetailController {
    @Autowired
    private OrderDetailService service;
}
