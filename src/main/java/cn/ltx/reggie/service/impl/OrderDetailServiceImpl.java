package cn.ltx.reggie.service.impl;

import cn.ltx.reggie.entity.OrderDetail;
import cn.ltx.reggie.mapper.OrderDetailMapper;
import cn.ltx.reggie.service.OrderDetailService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @PROJECT_NAME: reggie_take_out
 * @PACKAGE_NAME: cn.ltx.reggie.service.impl
 * @CLASS_NAME: OrderDetailServiceImpl
 * @Author: 21130
 * @CreateTime: 2022-08-28  15:37
 * @Description:
 * @Version: 1.0
 */
@Service
@SuppressWarnings("ALL")
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
