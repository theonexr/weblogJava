package com.xu.blog.controller;

import com.xu.blog.service.CategoryService;
import com.xu.blog.vo.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/categorys")
public class CategoryController {
    @Resource
    private CategoryService categoryService;
    @GetMapping
    public Result categories(){
        return categoryService.findAll();
    }
    @GetMapping("/detail")
//    导航分类
    public Result categoriesDetail(){
        return categoryService.findAllDetail();
    }
//    文章分类列表
    @GetMapping("/detail/{id}")
    public Result categoryDetailById(@PathVariable("id") Long id){

        return categoryService.categoryDetailById(id);
    }

}
