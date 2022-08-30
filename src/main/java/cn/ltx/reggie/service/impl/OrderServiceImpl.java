package cn.ltx.reggie.service.impl;

import cn.ltx.reggie.common.BaseContext;
import cn.ltx.reggie.common.CustomException;
import cn.ltx.reggie.common.R;
import cn.ltx.reggie.dto.OrdersDto;
import cn.ltx.reggie.entity.*;
import cn.ltx.reggie.mapper.OrderMapper;
import cn.ltx.reggie.service.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @PROJECT_NAME: reggie_take_out
 * @PACKAGE_NAME: cn.ltx.reggie.service.impl
 * @CLASS_NAME: OrderServiceImpl
 * @Author: 21130
 * @CreateTime: 2022-08-28  15:36
 * @Description:
 * @Version: 1.0
 */
@Service
@SuppressWarnings("ALL")
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Orders> implements OrderService {
    @Autowired
    private AddressBookService addressBookService;
    @Autowired
    private ShoppingCartService shoppingCartService;
    @Autowired
    private UserService userService;
    @Autowired
    private OrderDetailService orderDetailService;
    @Override
    public R<String> submitOrders(Orders orders) {
        Long userId = BaseContext.getCurrentId();
        orders.setUserId(userId);//设置用户id
        User user = userService.getById(userId);
        //查询当前用户的购物车数据
        QueryWrapper<ShoppingCart> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", orders.getUserId());
        List<ShoppingCart> shoppingCartList = shoppingCartService.list(wrapper);
        if (shoppingCartList == null || shoppingCartList.size() == 0) {
            throw new CustomException("购物车为空，不能下单");
        }
        AddressBook addressBook = addressBookService.getById(orders.getAddressBookId());
        if(addressBook == null){
            throw new CustomException("收货地址有误，不能下单");
        }

        //生成一个订单号
        long orderId = IdWorker.getId();

        //计算购物车金额
        AtomicInteger amount = new AtomicInteger(0);
        //匿名内部类使用外部变量只能引用最终
        //我们给匿名内部类传递参数的时候，若该形参在内部类中需要被使用，那么该形参必须要为final。
        // 就是说：当所在的方法的形参需要被内部类里面使用时，该形参必须为final。
        List<OrderDetail> orderDetailList = shoppingCartList.stream().map((item) -> {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(orderId);//订单id
            orderDetail.setNumber(item.getNumber());//数量
            orderDetail.setDishFlavor(item.getDishFlavor());//口味
            orderDetail.setDishId(item.getDishId());//菜品id
            orderDetail.setSetmealId(item.getSetmealId());//套餐id
            orderDetail.setName(item.getName());//菜品名称或者菜品名称（餐品名称）
            orderDetail.setImage(item.getImage());//图片名称
            orderDetail.setAmount(item.getAmount());//餐品单份金额
            amount.addAndGet(item.getAmount().multiply/*单份金额乘以份数*/(new BigDecimal(item.getNumber())).intValue());
            return orderDetail;
        }).collect(Collectors.toList());
        //添加订单
        orders.setId(orderId);//订单id
        orders.setOrderTime(LocalDateTime.now());//下单时间
        orders.setCheckoutTime(LocalDateTime.now());//结账时间
        orders.setStatus(2);//订单状态 1待付款，2待派送，3已派送，4已完成，5已取消
        orders.setAmount(new BigDecimal(amount.get()));//实收金额
        orders.setNumber(String.valueOf(orderId));//订单号
        orders.setUserName(user.getName());//用户名
        orders.setConsignee(addressBook.getConsignee());//用户名从地址簿里拿 这里使用收货人
        orders.setPhone(addressBook.getPhone());//收货人电话
        //拼接详细收货地址
        String address = (addressBook.getProvinceName() == null ? "" : addressBook.getProvinceName())//省级名称
                        +(addressBook.getDistrictName() == null ? "" : addressBook.getDistrictName())//区级名称
                        +(addressBook.getDetail() == null ? "" : addressBook.getDetail());//详细地址
        orders.setAddress(address);//收货地址
        //向订单表插入数据 一条数据
        this.save(orders);
        //向订单明细表插入数据 多条数据
        orderDetailService.saveBatch(orderDetailList);
        //清空购物车
        shoppingCartService.remove(wrapper);

        return R.success("订单提交成功");
    }

    /**
     * 订单信息分页查询
     * @param page
     * @param pageSize
     * @return
     */
    @Override
    public R<IPage<OrdersDto>> userPage(int page, int pageSize) {
        IPage<Orders> pageInfo = new Page<>(page, pageSize);
        QueryWrapper<Orders> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", BaseContext.getCurrentId()).orderByDesc("order_time");
        pageInfo = this.page(pageInfo, wrapper);
        IPage<OrdersDto> ordersDtoIPage = new Page<>();
        BeanUtils.copyProperties(pageInfo, ordersDtoIPage,"records");
        List<OrdersDto> ordersDtoList = pageInfo.getRecords().stream().map((item) -> {
            OrdersDto ordersDto = new OrdersDto();
            BeanUtils.copyProperties(item, ordersDto);
            Long orderId = item.getId();
            List<OrderDetail> orderDetailList = orderDetailService.list(new QueryWrapper<OrderDetail>().eq("order_id", orderId));
            if (orderDetailList != null && orderDetailList.size() > 0) {
                ordersDto.setOrderDetails(orderDetailList);
            }
            return ordersDto;
        }).collect(Collectors.toList());
        ordersDtoIPage.setRecords(ordersDtoList);
        return R.success(ordersDtoIPage);
    }

    @Override
    public R<String> again(Orders orders) {
        Long userId = BaseContext.getCurrentId();
        orders.setUserId(userId);//设置用户id
        User user = userService.getById(userId);

        //生成一个订单号
        long newOrderId = IdWorker.getId();

        //计算购物车金额
        AtomicInteger amount = new AtomicInteger(0);

        //获得曾经订单的id
        Long oldOrderId = orders.getId();
        Orders order = this.getById(oldOrderId);
        order.setId(newOrderId);
        order.setOrderTime(LocalDateTime.now());//下单时间
        order.setCheckoutTime(LocalDateTime.now());//结账时间
        order.setStatus(2);//订单状态 1待付款，2待派送，3已派送，4已完成，5已取消

        //根据订单id查询出对应的订单明细
        List<OrderDetail> orderDetailList = orderDetailService.list(new QueryWrapper<OrderDetail>().eq("order_id", oldOrderId)).stream().map((item)->{
            item.setId(newOrderId);
            item.setOrderId(newOrderId);
            return item;
        }).collect(Collectors.toList());
        //向订单表插入数据 一条数据
        this.save(order);
        //向订单明细表插入数据 多条数据
        orderDetailService.saveBatch(orderDetailList);

        return R.success("再来一旦成功");
    }
}
