package com.xu.blog.controller;

import com.xu.blog.service.TagService;
import com.xu.blog.vo.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/tags")
public class TagsController {
    @Resource
    private TagService tagService;

    @GetMapping("/hot")
    public Result hot(){
        int limit = 6; //最热6个
        return tagService.hots(limit);
    }
    /**
     * 所有文章标签
     * @return
     */
    @GetMapping
    public Result findAll(){
        return tagService.findAll();
    }
    /**
     * 导航栏所有标签
     * @return
     */
    @GetMapping("/detail")
    public Result findAllDetail() {
        return tagService.findAllDetail();
    }
    /**
     * 标签对应文章
     * @return
     */
    @GetMapping("detail/{id}")
    public Result findDetailById(@PathVariable("id") Long id) {
        return tagService.findDetailById(id);
    }

}
