package cn.ltx.reggie.web.controller;

import cn.ltx.reggie.common.BaseContext;
import cn.ltx.reggie.common.R;
import cn.ltx.reggie.entity.AddressBook;
import cn.ltx.reggie.service.AddressBookService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @PROJECT_NAME: reggie_take_out
 * @PACKAGE_NAME: cn.ltx.reggie.web.controller
 * @CLASS_NAME: AddressBookController
 * @Author: 21130
 * @CreateTime: 2022-08-28  15:22
 * @Description:
 * @Version: 1.0
 */
@RestController
@RequestMapping("/addressBook")
@Slf4j
@SuppressWarnings("ALL")
public class AddressBookController {
    @Autowired
    private AddressBookService service;

    /**
     * 查询指定用户的全部地址
     * @return
     */
    @GetMapping("/list")
    public R<List<AddressBook>> list(AddressBook addressBook) {
        log.info("addressBook = {}", addressBook);
        return service.list(addressBook);
    }

    /**
     * 变更默认收货地址
     * @param addressBook
     * @return
     */
    @PutMapping("/default")
    public R<AddressBook> setDefaultAddress(@RequestBody AddressBook addressBook) {
        log.info("addressBook = {}", addressBook);
        return service.setDefaultAddress(addressBook);
    }

    /**
     * 新增收货地址
     * @param addressBook
     * @return
     */
    @PostMapping
    public R<AddressBook> save(@RequestBody AddressBook addressBook){
        log.info("addressBook = {}", addressBook);
        addressBook.setUserId(BaseContext.getCurrentId());
        service.save(addressBook);
        return R.success(addressBook);
    }

    /**
     * 根据id查询地址信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<AddressBook> getById(@PathVariable Long id){
        log.info("id = {}", id);
        AddressBook addressBook = service.getById(id);
        return R.success(addressBook);
    }

    /**
     * 修改收货地址信息
     * @param addressBook
     * @return
     */
    @PutMapping
    public R<AddressBook> update(@RequestBody AddressBook addressBook){
        log.info("addressBook = {}", addressBook);
        service.updateById(addressBook);
        return R.success(addressBook);
    }
    @GetMapping("/default")
    public R<AddressBook> getDefault() {
        log.info("default = {}","获取默认收货地址信息");
        QueryWrapper<AddressBook> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", BaseContext.getCurrentId()).eq("is_default", 1);
        AddressBook addressBook = service.getOne(wrapper);
        return R.success(addressBook);
    }
}
