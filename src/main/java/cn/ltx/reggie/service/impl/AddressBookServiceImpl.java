package cn.ltx.reggie.service.impl;

import cn.ltx.reggie.common.BaseContext;
import cn.ltx.reggie.common.R;
import cn.ltx.reggie.entity.AddressBook;
import cn.ltx.reggie.mapper.AddressBookMapper;
import cn.ltx.reggie.service.AddressBookService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @PROJECT_NAME: reggie_take_out
 * @PACKAGE_NAME: cn.ltx.reggie.service.impl
 * @CLASS_NAME: AddressBookServiceImpl
 * @Author: 21130
 * @CreateTime: 2022-08-28  15:35
 * @Description:
 * @Version: 1.0
 */
@Service
@SuppressWarnings("ALL")
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {
    @Override
    public R<List<AddressBook>> list(AddressBook addressBook) {
        Long userId = BaseContext.getCurrentId();
        addressBook.setUserId(userId);
        QueryWrapper<AddressBook> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", addressBook.getUserId()).orderByDesc("update_time");
        List<AddressBook> list = this.list(wrapper);
        return R.success(list);
    }

    /**
     * 设置默认收获地址
     * @param addressBook
     * @return
     */
    @Override
    public R<AddressBook> setDefaultAddress(AddressBook addressBook) {
        Long userId = BaseContext.getCurrentId();
        addressBook.setUserId(userId);
        //先把所有地址状态更改非默认地址
        UpdateWrapper<AddressBook> wrapper = new UpdateWrapper<>();
        wrapper.eq("user_id", addressBook.getUserId()).set("is_default",0);
        this.update(wrapper);
        //再根据当前地址的id设置为默认地址
        addressBook.setIsDefault(1);
        this.updateById(addressBook);
        return R.success(addressBook);
    }
}
