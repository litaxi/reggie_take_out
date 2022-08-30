package cn.ltx.reggie.service;

import cn.ltx.reggie.common.R;
import cn.ltx.reggie.dto.DishDto;
import cn.ltx.reggie.dto.SetmealDto;
import cn.ltx.reggie.entity.Setmeal;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @PROJECT_NAME: reggie_take_out
 * @PACKAGE_NAME: cn.ltx.reggie.service
 * @CLASS_NAME: SetmealService
 * @Author: 21130
 * @CreateTime: 2022-08-24  17:01
 * @Description:
 * @Version: 1.0
 */
@SuppressWarnings("ALL")
public interface SetmealService extends IService<Setmeal> {
    /**
     * 根据id修改套餐状态
     * @param status
     * @param ids
     */
    @Transactional
    void updateStatus(int status, List<Long> ids);

    /**
     * 根据id删除套餐
     * @param ids
     */
    @Transactional
    void delete(List<Long> ids);

    /**
     * 新增套餐
     * @param setmealDto
     */
    @Transactional
    void saveWithDish(SetmealDto setmealDto);

    /**
     * 根据id获取套餐信息要求分装菜品信息
     * @param id
     * @return
     */
    SetmealDto getByIdWithDish(Long id);

    /**
     * 修改套餐信息更新菜品信息
     * @param setmealDto
     */
    @Transactional
    void updateWithDish(SetmealDto setmealDto);

    /**
     * 套餐信息分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    R<IPage<SetmealDto>> page(int page, int pageSize, String name);

    /**
     * 根据分类id查询所有套餐信息
     * @param setmeal
     * @return
     */
    R<List<SetmealDto>> list(Setmeal setmeal);

    /**
     * 根据套餐id查询出对应的所有菜品信息及其口味信息
     * @param id
     * @return
     */
    R<List<DishDto>> setmealDishList(Long id);
}
