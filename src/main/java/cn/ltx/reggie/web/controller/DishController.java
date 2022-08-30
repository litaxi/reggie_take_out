package cn.ltx.reggie.web.controller;

import cn.ltx.reggie.common.R;
import cn.ltx.reggie.dto.DishDto;
import cn.ltx.reggie.entity.Dish;
import cn.ltx.reggie.service.DishService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @PROJECT_NAME: reggie_take_out
 * @PACKAGE_NAME: cn.ltx.reggie.web.controller
 * @CLASS_NAME: DishController
 * @Author: 21130
 * @CreateTime: 2022-08-25  12:40
 * @Description:
 * @Version: 1.0
 */
@RestController
@RequestMapping("/dish")
@Slf4j
@SuppressWarnings("ALL")
public class DishController {
    @Autowired
    private DishService service;

    /**
     * 分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<IPage<DishDto>> page(int page, int pageSize, String name) {
        log.info("page => {}, pageSize => {}, name => {}", page, pageSize, name);
        return service.page(page, pageSize, name);
    }

    /**
     * 根据id查询菜品信息要求封装口味信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<DishDto> getById(@PathVariable Long id){
        log.info("id => {}",id);
        DishDto dishDto = service.getByIdWithFlavor(id);
        return R.success(dishDto);
    }

    /**
     * 修改菜品信息并更新口味信息
     * @param dishDto
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto){
        log.info("dishDto => {}",dishDto);
        service.updateWithFlavor(dishDto);
        return R.success("修改菜品信息成功");
    }

    /**
     * 批量修改状态
     * @param status
     * @param ids
     * @return
     */
    @PostMapping("/status/{status}")
    public R<String> updateStatus(@PathVariable int status,@RequestParam List<Long> ids){
        log.info("status => {},ids => {}", status, ids);
        service.updateStatus(status,ids);
        return R.success("菜品状态已经修改成功");
    }

    /**
     * 根据id批量删除菜品
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids){
        log.info("ids => {}", ids);
        service.delete(ids);
        return R.success("菜品删除成功");
    }

    /**
     * 根据分类id查询所有菜品信息
     * @param category
     * @return
     */
    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish){
        log.info("dish => {}", dish);
        return service.list(dish);
    }

}
