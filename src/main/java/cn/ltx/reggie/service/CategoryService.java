package cn.ltx.reggie.service;

import cn.ltx.reggie.common.R;
import cn.ltx.reggie.entity.Category;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @PROJECT_NAME: reggie_take_out
 * @PACKAGE_NAME: cn.ltx.reggie.service
 * @CLASS_NAME: CategoryService
 * @Author: 21130
 * @CreateTime: 2022-08-24  16:57
 * @Description:
 * @Version: 1.0
 */
@SuppressWarnings("ALL")
public interface CategoryService extends IService<Category> {
    /**
     * 删除分类信息
     * @param categoryId
     */
    @Transactional
    void remove(Long categoryId);
    R<IPage<Category>> page(int page, int pageSize);

    /**
     * 根据id查询菜品套餐分类列表
     * @param category
     * @return
     */
    R<List<Category>> list(Category category);
}
