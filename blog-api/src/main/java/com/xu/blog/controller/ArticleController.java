package com.xu.blog.controller;

import com.xu.blog.service.ArticleService;
import com.xu.blog.vo.Result;
import com.xu.blog.vo.params.ArticleParam;
import com.xu.blog.vo.params.PageParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

//使用json数据进行交互
@RestController
@RequestMapping("/articles")
//@CrossOrigin(origins ="*",maxAge = 3600)

public class ArticleController {
    @Resource
    private ArticleService articleService;
    /**
     * 首页文章列表
     * @param pageParams
     * return 返回承担返回数据Result的类
     */
    @PostMapping
//    @CrossOrigin(origins ="*",maxAge = 3600)
    public Result listArticle(@RequestBody PageParams pageParams){
        return articleService.listArticle(pageParams);
    }
    /**
     * 首页最热文章
     * @return
     */
    @PostMapping("/hot")
    public Result hotArticle(){
        int limit=5;
        return articleService.hotArticle(limit);
    }
    /**
     * 首页最新文章
     * @return
     */
    @PostMapping("/new")
    public Result newArticle(){
        int limit=5;
        return articleService.newArticle(limit);
    }
    /**
     * 首页文章归档
     * @return
     */
    @PostMapping("/listArchives")
    public Result listArchives(){

        return articleService.listArchives();
    }
    /**
     * 查看文章详情
     * @param articleId
     * @return
     */
    @PostMapping("view/{id}")
    public  Result findArticleById(@PathVariable("id") Long articleId){
        return articleService.findArticleById(articleId);
    }
    /**
     * 发布文章
     * @return
     */
    @PostMapping("/publish")
    public Result publish(@RequestBody ArticleParam articleParam){
        return articleService.publish(articleParam);
    }
}
