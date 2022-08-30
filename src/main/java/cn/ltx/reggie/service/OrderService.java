package cn.ltx.reggie.service;

import cn.ltx.reggie.common.R;
import cn.ltx.reggie.dto.OrdersDto;
import cn.ltx.reggie.entity.Orders;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

/**
 * @PROJECT_NAME: reggie_take_out
 * @PACKAGE_NAME: cn.ltx.reggie.service
 * @CLASS_NAME: OrderService
 * @Author: 21130
 * @CreateTime: 2022-08-28  15:28
 * @Description:
 * @Version: 1.0
 */
@SuppressWarnings("ALL")
public interface OrderService extends IService<Orders> {
    /**
     * 提交订单信息
     * @param orders
     * @return
     */
    @Transactional
    R<String> submitOrders(Orders orders);

    /**
     * 订单列表分页查询
     * @param page
     * @param pageSize
     * @return
     */
    R<IPage<OrdersDto>> userPage(int page, int pageSize);

    /**
     * 再来一单
     * @param orders
     * @return
     */
    R<String> again(Orders orders);

    /**
     * 后台订单列表条件分页查询
     * @param page
     * @param pageSize
     * @param number
     * @param beginTime
     * @param endTime
     * @return
     */
    R<IPage<OrdersDto>> page(int page, int pageSize, String number, String beginTime, String endTime);
}
