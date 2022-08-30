package cn.ltx.reggie.service.impl;

import cn.ltx.reggie.entity.User;
import cn.ltx.reggie.mapper.UserMapper;
import cn.ltx.reggie.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @PROJECT_NAME: reggie_take_out
 * @PACKAGE_NAME: cn.ltx.reggie.service.impl
 * @CLASS_NAME: UserServiceImpl
 * @Author: 21130
 * @CreateTime: 2022-08-27  20:00
 * @Description:
 * @Version: 1.0
 */
@Service
@SuppressWarnings("ALL")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
