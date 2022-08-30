package cn.ltx.reggie.service.impl;

import cn.ltx.reggie.common.R;
import cn.ltx.reggie.entity.Employee;
import cn.ltx.reggie.mapper.EmployeeMapper;
import cn.ltx.reggie.service.EmployeeService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

/**
 * @PROJECT_NAME: first_project_reggie_take_out
 * @PACKAGE_NAME: cn.ltx.reggie.service.impl
 * @CLASS_NAME: EmployeeServiceImpl
 * @Author: 21130
 * @CreateTime: 2022-08-21  18:09
 * @Description:
 * @Version: 1.0
 */
@Service
@Slf4j
@SuppressWarnings("ALL")
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
    /**
     * 员工后台登录
     * @param request
     * @param employee
     * @return
     */
    @Override
    public R<Employee> login(HttpServletRequest request, Employee employee) {
        //1、先获取用户名判断员工是否存在
        Employee exists = this.getOne(new QueryWrapper<Employee>().eq("username", employee.getUsername()));
        //不存在
        if (exists == null) {
            return R.error("用户名不存在");
        } else {
            //存在
            //2、判断查看员工状态，如果为已禁用状态，则返回员工已禁用结果
            if (exists.getStatus() == 0) {
                return R.error("该员工号已禁用");
            } else {
                //员工状态正常
                //3、判断密码是否正确
                String password = DigestUtils.md5DigestAsHex(employee.getPassword().getBytes());
                if (!password.equals(exists.getPassword())) {
                    //密码不匹配
                    return R.error("登录失败，账号和密码不匹配");
                } else {
                    //密码匹配
                    //4、存入Session
                    request.getSession().setAttribute("employee", exists.getId());
                    //5、登录成功，返回数据
                    return R.success(exists);
                }

            }
        }
    }

    /**
     * 员工列表分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @Override
    public R<IPage<Employee>> page(int page, int pageSize, String name) {
        QueryWrapper<Employee> wrapper = new QueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(StringUtils.trim(name)), "name", name);
        wrapper.orderByDesc("update_time");
        IPage<Employee> pageInfo = new Page<>(page, pageSize);
        pageInfo = this.page(pageInfo, wrapper);
        log.info("total => {}", pageInfo.getTotal());
        log.info("list => {}", pageInfo.getRecords());
        return R.success(pageInfo);
    }

    /**
     * 修改员工信息启用禁用员工账号
     * @param request
     * @param employee
     * @return
     */
    @Override
    public R<String> update(HttpServletRequest request, Employee employee) {
        Long empId = (Long) request.getSession().getAttribute("employee");
        employee.setUpdateTime(LocalDateTime.now());
        employee.setUpdateUser(empId);
        this.updateById(employee);
        return R.success("员工信息修改成功");
    }

    /**
     * 添加员工信息
     * @param request
     * @param employee
     * @return
     */
    @Override
    public R<String> save(HttpServletRequest request, Employee employee) {
        Long empId = (Long) request.getSession().getAttribute("employee");
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes(StandardCharsets.UTF_8)));
        employee.setCreateTime(LocalDateTime.now());
        employee.setCreateUser(empId);
        employee.setUpdateTime(LocalDateTime.now());
        employee.setUpdateUser(empId);
        this.save(employee);
        return R.success("添加成功");
    }
}
