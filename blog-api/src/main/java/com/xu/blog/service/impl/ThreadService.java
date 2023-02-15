package com.xu.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.xu.blog.Dao.mapper.ArticleMapper;
import com.xu.blog.Dao.pojo.Article;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class ThreadService {
//操作在线程池中进行
    @Async("taskExecutor")
    public void updateArticleViewCount(ArticleMapper articleMapper, Article article) {
        int viewCounts = article.getViewCounts();
        Article articleUpdate = new Article();
        articleUpdate.setViewCounts(viewCounts+1);

        LambdaUpdateWrapper<Article> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Article::getId,article.getId());
        // 设置一个为了在多线程的条件下 线程安全
        updateWrapper.eq(Article::getViewCounts,viewCounts);
        // update article set view_count = 100 where view_count =99 and id = 1
        articleMapper.update(articleUpdate, updateWrapper);

            try {
                Thread.sleep(2000);
                System.out.println("更新完成!");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
    }
}
