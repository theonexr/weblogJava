package com.xu.blog.service;

import com.xu.blog.vo.Result;
import com.xu.blog.vo.params.CommentParam;

public interface CommentService {
    /**
     * 根据文章id查找评论
     * @param articleId
     * @return
     */
    Result commentsByArticleId(Long articleId);
    /**
     * 评论
     * @param commentParam
     * @return
     */
    Result comment(CommentParam commentParam);
}
