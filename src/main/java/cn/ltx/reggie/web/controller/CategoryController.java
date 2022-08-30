package cn.ltx.reggie.web.controller;

import cn.ltx.reggie.common.R;
import cn.ltx.reggie.entity.Category;
import cn.ltx.reggie.service.CategoryService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @PROJECT_NAME: reggie_take_out
 * @PACKAGE_NAME: cn.ltx.reggie.entity
 * @CLASS_NAME: CategoryController
 * @Author: 21130
 * @CreateTime: 2022-08-24  16:51
 * @Description:
 * @Version: 1.0
 */
@RestController
@RequestMapping("/category")
@Slf4j
@SuppressWarnings("ALL")
public class CategoryController {
    @Autowired
    private CategoryService service;
    /**
     * 修改
     * @param category
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody Category category){
        log.info("category => {}", category);
        service.updateById(category);
        return R.success("分类信息修改成功");
    }

    /**
     * 分页查询
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public R<IPage<Category>> page(int page, int pageSize){
        log.info("page => {}, pageSize => {}", page, pageSize);
        return service.page(page, pageSize);
    }

    /**
     * 新增分类信息
     * @param category
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody Category category){
        log.info("category => {}", category);
        service.save(category);
        return R.success("新增分类信息成功");
    }

    /**
     * 根据id删除分类信息
     * @param id
     * @return
     */
    @DeleteMapping()
    public R<String> delete(Long id){
        log.info("id => {}", id);
        service.remove(id);
        return R.success("分类信息删除成功");
    }


    /**
     * 根据id查询菜品套餐分类列表
     * @param category
     * @return
     */
    @GetMapping("/list")
    public R<List<Category>> list(Category category){
        log.info("category => {}", category);
        return service.list(category);
    }

}
