package cn.ltx.reggie.web.controller;

import cn.ltx.reggie.common.R;
import cn.ltx.reggie.dto.DishDto;
import cn.ltx.reggie.dto.SetmealDto;
import cn.ltx.reggie.entity.Setmeal;
import cn.ltx.reggie.service.CategoryService;
import cn.ltx.reggie.service.SetmealService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @PROJECT_NAME: reggie_take_out
 * @PACKAGE_NAME: cn.ltx.reggie.web.controller
 * @CLASS_NAME: SetmealController
 * @Author: 21130
 * @CreateTime: 2022-08-25  19:05
 * @Description:
 * @Version: 1.0
 */
@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetmealController {
    @Autowired
    private SetmealService service;
    @Autowired
    private CategoryService categoryService;
    /**
     * 分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<IPage<SetmealDto>> page(int page, int pageSize, String name) {
        log.info("page => {}, pageSize => {}, name => {}", page, pageSize, name);
        return service.page(page, pageSize, name);
    }
    /**
     * 批量修改状态
     * @param status
     * @param ids
     * @return
     */
    @PostMapping("/status/{status}")
    public R<String> updateStatus(@PathVariable int status, @RequestParam List<Long> ids){
        log.info("status => {},ids => {}",status, ids);
        service.updateStatus(status,ids);
        return R.success("套餐状态已经修改成功");
    }

    /**
     * 根据id批量删除
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids){
        log.info("ids => {}",ids);
        service.delete(ids);
        return R.success("套餐删除成功");
    }

    /**
     * 保存套餐菜品信息
     * @param setmealDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto){
        log.warn("setmealDto => {}", setmealDto);
        service.saveWithDish(setmealDto);
        return R.success("新增套餐成功");
    }

    /**
     * 根据id查询套餐信息封装菜品信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<SetmealDto> getById(@PathVariable Long id){
        log.info("id => {}", id);
        SetmealDto setmealDto = service.getByIdWithDish(id);
        return R.success(setmealDto);
    }

    /**
     * 修改套
     * @param setmealDto
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody SetmealDto setmealDto){
        log.info("setmealDto => {}", setmealDto);
        service.updateWithDish(setmealDto);
        return R.success("套餐修改成功");
    }
    //http://localhost/setmeal/list?categoryId=1413386191767674881&status=1
    @GetMapping("/list")
    public R<List<SetmealDto>> list(Setmeal setmeal){
        log.info("setmeal => {}", setmeal);
        return service.list(setmeal);
    }

    /**
     * 根据套餐id查询出对应的所有菜品信息及其口味信息
     * @param id
     * @return
     */
    @GetMapping("/dish/{id}")
    public R<List<DishDto>> setmealDishList(@PathVariable Long id){
        log.info("id => {}", id);
        return service.setmealDishList(id);
    }
}
