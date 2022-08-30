package cn.ltx.reggie.service;

import cn.ltx.reggie.common.R;
import cn.ltx.reggie.entity.AddressBook;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @PROJECT_NAME: reggie_take_out
 * @PACKAGE_NAME: cn.ltx.reggie.service
 * @CLASS_NAME: AddressBookService
 * @Author: 21130
 * @CreateTime: 2022-08-28  15:28
 * @Description:
 * @Version: 1.0
 */
public interface AddressBookService extends IService<AddressBook> {
    /**
     * 查询指定用户的地址信息
     * @param addressBook
     * @return
     */
    R<List<AddressBook>> list(AddressBook addressBook);

    /**
     * 设置默认收货地址
     * @param addressBook
     * @return
     */
    @Transactional
    R<AddressBook> setDefaultAddress(AddressBook addressBook);
}
