package com.xu.blog.service;


import com.xu.blog.vo.CategoryVo;
import com.xu.blog.vo.Result;

public interface CategoryService {
    CategoryVo findCategoryById(Long categoryId);

    Result findAll();
    /**
     * 文章分类
     * @return
     */
    Result findAllDetail();


    Result categoryDetailById(Long id);

}
