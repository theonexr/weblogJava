package com.xu.blog.service;

import com.xu.blog.vo.Result;
import com.xu.blog.vo.params.ArticleParam;
import com.xu.blog.vo.params.PageParams;

public interface ArticleService {
    /**
     * 分页查询 文章列表
     * @param pageParams
     * @return
     */
     Result listArticle(PageParams pageParams);

     Result hotArticle(int limit);

     Result newArticle(int limit);

     Result listArchives();
    /**
     * 查询文章详情
     * @param articleId
     * @return
     */
     Result findArticleById(Long articleId);
    /**
     * 文章发布
     * @param articleParam
     * @return
     */
    Result publish(ArticleParam articleParam);
}
