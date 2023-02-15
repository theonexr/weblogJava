package com.xu.blog.controller;

import com.xu.blog.service.CommentService;
import com.xu.blog.vo.Result;
import com.xu.blog.vo.params.CommentParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/comments")
public class CommentController {
    @Resource
    private CommentService commentService;

    @GetMapping("article/{id}")
    public Result findArticleById(@PathVariable("id") Long articleId){
        return commentService.commentsByArticleId(articleId);
    }

    @PostMapping("create/change")
    public Result comment(@RequestBody CommentParam commentParam){
        return commentService.comment(commentParam);
    }
}
