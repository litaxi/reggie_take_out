package cn.ltx.reggie.service;

import cn.ltx.reggie.common.R;
import cn.ltx.reggie.entity.Employee;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
 * @PROJECT_NAME: first_project_reggie_take_out
 * @PACKAGE_NAME: cn.ltx.reggie.service
 * @CLASS_NAME: EmployeeService
 * @Author: 21130
 * @CreateTime: 2022-08-21  18:08
 * @Description:
 * @Version: 1.0
 */
public interface EmployeeService extends IService<Employee> {
    /**
     * 员工后台登录
     * @param request
     * @param employee
     * @return
     */
    R<Employee> login(HttpServletRequest request, Employee employee);

    /**
     * 员工列表分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    R<IPage<Employee>> page(int page, int pageSize, String name);

    /**
     * 修改员工信息 启用禁用员工账号
     * @param request
     * @param employee
     * @return
     */
    R<String> update(HttpServletRequest request, Employee employee);

    /**
     * 添加员工信息
     * @param request
     * @param employee
     * @return
     */
    R<String> save(HttpServletRequest request, Employee employee);
}
