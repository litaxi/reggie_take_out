package cn.ltx.reggie.service;

import cn.ltx.reggie.common.R;
import cn.ltx.reggie.dto.DishDto;
import cn.ltx.reggie.entity.Dish;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @PROJECT_NAME: reggie_take_out
 * @PACKAGE_NAME: cn.ltx.reggie.service
 * @CLASS_NAME: DishService
 * @Author: 21130
 * @CreateTime: 2022-08-24  17:01
 * @Description:
 * @Version: 1.0
 */
@SuppressWarnings("ALL")
public interface DishService extends IService<Dish> {
    /**
     * 根据菜品id获取菜品信息并封装口味信息
     * @param id
     * @return
     */
    DishDto getByIdWithFlavor(Long id);

    /**
     * 修改菜品信息并更新菜品口味与分类
     * @param dishDto
     */
    @Transactional
    void updateWithFlavor(DishDto dishDto);

    /**
     * 批量停售起售
     * @param status
     * @param ids
     */
    @Transactional
    void updateStatus(int status, List<Long> ids);

    /**
     * 根据id删除菜品信息
     * @param ids
     */
    @Transactional
    void delete(List<Long> ids);

    /**
     * 菜品分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    R<IPage<DishDto>> page(int page, int pageSize, String name);

    /**
     * 根据id查询所有菜品信息
     * @param dish
     * @return
     */
    R<List<DishDto>> list(Dish dish);
}
