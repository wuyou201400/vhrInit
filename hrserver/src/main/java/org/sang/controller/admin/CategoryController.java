package org.sang.controller.admin;

import org.sang.bean.Category;
import org.sang.bean.RespBean;
import org.sang.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 超级管理员专属Controller
 */
@RestController
@RequestMapping("/admin/category")
public class CategoryController {
    @Autowired
    CategoryService categoryService;

/*
    @InitBinder
    protected void init(ServletRequestDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Timestamp.class, new CustomDateEditor(dateFormat, false));
    }
*/

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public List<Category> getAllCategories() {
        List<Category> all= categoryService.getAllCategories();
        return all;
    }

    @RequestMapping(value = "/{ids}", method = RequestMethod.DELETE)
    public RespBean deleteById(@PathVariable String ids) {
        boolean result = categoryService.deleteCategoryByIds(ids);
        if (result) {
            return RespBean.ok("删除成功!");
        }
        return RespBean.error("删除失败!");
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public RespBean addNewCate(Category category) {
        int result = categoryService.addCategory(category);
        if (result == 1) {
            return RespBean.ok("添加成功!");
        }
        return RespBean.error("添加失败!");
    }

    @RequestMapping(value = "/", method = RequestMethod.PUT)
    public RespBean updateCate(Category category) {
        int i = categoryService.updateCategoryById(category);
        if (i == 1) {
            return RespBean.ok( "修改成功!");
        }
        return RespBean.error("修改失败!");
    }
}
