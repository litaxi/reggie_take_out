package cn.ltx.reggie.web.controller;

import cn.ltx.reggie.common.R;
import cn.ltx.reggie.entity.Employee;
import cn.ltx.reggie.service.EmployeeService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @PROJECT_NAME: first_project_reggie_take_out
 * @PACKAGE_NAME: cn.ltx.reggie.web.controller
 * @CLASS_NAME: EmployeeController
 * @Author: 21130
 * @CreateTime: 2022-08-21  18:15
 * @Description:
 * @Version: 1.0
 */
@SuppressWarnings("ALL")
@RestController
@RequestMapping("/employee")
@Slf4j
public class EmployeeController {
    @Autowired
    private EmployeeService service;

    /**
     * 登录
     *
     * @param request
     * @param employee
     * @return
     */
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {
        log.info("employee => :{}", employee);
        return service.login(request,employee);
    }

    /**
     * 退出登录
     *
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request) {
        //清理Session中保存的当前登录员工的id
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }

    /**
     * 员工信息分页查询
     *
     * @param page     当前页
     * @param pageSize 每页条数
     * @param name     名字模糊查询
     * @return
     */
    @GetMapping("/page")
    public R<IPage<Employee>> page(int page, int pageSize, String name) {
        log.info("page => {}, pageSize => {}, name => {}", page, pageSize, name);
        return service.page(page,pageSize,name);
    }


    /**
     * 根据id查询员工信息
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable int id) {
        log.info("id => {}", id);
        Employee employee = service.getById(id);
        return R.success(employee);
    }

    /**
     * 修改员工信息 启用禁用员工账号
     *
     * @param request
     * @param employee
     * @return
     */
    @PutMapping
    public R<String> update(HttpServletRequest request, @RequestBody Employee employee) {
        log.info("employee => :{}", employee);
        return service.update(request,employee);
    }

    /**
     * 添加员工信息
     *
     * @param request
     * @param employee
     * @return
     */
    @PostMapping
    public R<String> save(HttpServletRequest request, @RequestBody Employee employee) {
        log.info("employee => :{}", employee);
        return service.save(request,employee);
    }

}
