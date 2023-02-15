package com.xu.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xu.blog.Dao.dos.Archives;
import com.xu.blog.Dao.mapper.ArticleBodyMapper;
import com.xu.blog.Dao.mapper.ArticleMapper;
import com.xu.blog.Dao.mapper.ArticleTagMapper;
import com.xu.blog.Dao.pojo.Article;
import com.xu.blog.Dao.pojo.ArticleBody;
import com.xu.blog.Dao.pojo.ArticleTag;
import com.xu.blog.Dao.pojo.SysUser;
import com.xu.blog.service.*;
import com.xu.blog.utils.UserThreadLocal;
import com.xu.blog.vo.ArticleBodyVo;
import com.xu.blog.vo.ArticleVo;
import com.xu.blog.vo.Result;
import com.xu.blog.vo.TagVo;
import com.xu.blog.vo.params.ArticleParam;
import com.xu.blog.vo.params.PageParams;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ArticleServiceImpl implements ArticleService {
    @Resource
    private ArticleMapper articleMapper;
    @Resource
    private TagService tagService;
    @Resource
    private SysUserService sysUserService;
    @Resource
    private ArticleBodyMapper articleBodyMapper;
    @Resource
    private CategoryService categoryService;
    @Resource
    private ThreadService threadService;
    @Resource
    private ArticleTagMapper articleTagMapper;
    @Override
    public Result listArticle(PageParams pageParams){
            Page<Article> page = new Page<>(pageParams.getPage(),pageParams.getPageSize());
            IPage<Article> articleIPage = articleMapper.listAticle(
                    page,
                    pageParams.getCategoryId(),
                    pageParams.getTagId(),
                    pageParams.getYear(),
                    pageParams.getMonth());
            List<Article> recordes = articleIPage.getRecords();
            return Result.success(copyList(recordes,true,true));
    }

//    @Override
//    public Result listArticle(PageParams pageParams){
//        //1. 这个是分页查询的类（代表着分离模式），要传入的是页面的页数和页面总数
//        Page<Article> page = new Page<Article>(pageParams.getPage(),pageParams.getPageSize());
//        //2. LambdaQueryWrapper是MybatisPlus提供的，需要就导入这个包就可以了
//        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
//        //and category_id = #{categoryId}
//        if(pageParams.getCategoryId() != null){
//            queryWrapper.eq(Article::getCategoryId,pageParams.getCategoryId());
//        }
//
//        List<Long> articleIdList = new ArrayList<>();
//        if (pageParams.getTagId() !=null) {
//            //加入标签  条件查询
//            //article表中 并没有tag字段一篇文章有多个标签
//            //article_tag 中一个articleId对应多个tagId
//            LambdaQueryWrapper<ArticleTag> articleTagLambdaQueryWrapper = new LambdaQueryWrapper<>();
//            articleTagLambdaQueryWrapper.eq(ArticleTag::getTagId,pageParams.getTagId());
//            List<ArticleTag> articleTags = articleTagMapper.selectList(articleTagLambdaQueryWrapper);
//            //查出所有的文章标签放入数组
//            for (ArticleTag articleTag : articleTags) {
//                articleIdList.add(articleTag.getArticleId());
//                //将文章标签id和标签id相等的放入数组
//            }
//            if (articleIdList.size() > 0){
//                //如果有文章id则查询文章id是否在符合条件数组中
//                queryWrapper.in(Article::getId,articleIdList);
//            }
//        }
//
//
//        //3. 这里是根据字体排序
//        //queryWrapper.orderByDesc(Article::getWeight);
//        //4. 这里设置的是根据时间排序
//        //queryWrapper.orderByDesc(Article::getCreateDate);
//        //5. 这个方法    default Children orderByDesc(boolean condition, R column, R... columns) {是可变长参数的
//        queryWrapper.orderByDesc(Article::getWeight,Article::getCreateDate);
//        // 因为articleMapper继承了BaseMapper了的，所有设查询的参数和查询出来的排序方式
//        Page<Article> articlePage = articleMapper.selectPage(page, queryWrapper);
//        //这个就是我们查询出来的数据的数组了
//        List<Article> records = articlePage.getRecords();
//        //因为页面展示出来的数据不一定和数据库一样，所有我们要做一个转换
//        //将在查出数据库的数组复制到articleVo中实现解耦合,vo和页面数据交互
//        List<ArticleVo> articleVoList = copyList(records,true,true);
//        return Result.success(articleVoList);
//    }

    private List<ArticleVo> copyList(List<Article> records,boolean isTag,boolean isAuthor) {
        List<ArticleVo> articleVoList = new ArrayList<>();
        for (Article record : records) {
            articleVoList.add(copy(record,isTag,isAuthor,false,false));
        }
        return articleVoList;

    }
//重载
    private List<ArticleVo> copyList(List<Article> records,boolean isTag,boolean isAuthor,boolean isBody,boolean isCategory) {
        List<ArticleVo> articleVoList = new ArrayList<>();
        for (Article record : records) {
            articleVoList.add(copy(record,isTag,isAuthor,isBody,isCategory));
        }
        return articleVoList;
    }

    //这个方法是主要点是BeanUtils，又Spring提供的，专门用来拷贝的，想Article和articlevo相同属性的拷贝过来返回
    private ArticleVo copy(Article article,boolean isTag,boolean isAuthor,boolean isBody,boolean isCategory){
        ArticleVo articleVo = new ArticleVo();
        BeanUtils.copyProperties(article,articleVo);

        articleVo.setCreateDate(new DateTime(article.getCreateDate()).toString("yyyy-MM-dd HH:mm"));
               /*
        并不是所有的接口都需要标签,作者信息
        增加两个参数boolean isTag,boolean isAuthor
         */
        //需要开发tagService
        if (isTag) {
            Long articleId = article.getId();
            articleVo.setTags(tagService.findTagsByArticleId(articleId));
        }
        //需要开发authorService
        if (isAuthor) {
            Long authorId = article.getAuthorId();
            articleVo.setAuthor(sysUserService.findUserById(authorId).getNickname());
        }
        //需要开发findArticleBodyById
        if(isBody){
            Long bodyId = article.getBodyId();
            articleVo.setBody(findArticleBodyById(bodyId));
        }
        if(isCategory){
            String categoryId = article.getCategoryId();
            articleVo.setCategory(categoryService.findCategoryById(Long.valueOf(categoryId)));
        }
        return articleVo;
    }

    private ArticleBodyVo findArticleBodyById(Long bodyId) {
        ArticleBody articleBody = articleBodyMapper.selectById(bodyId);
        ArticleBodyVo articleBodyVo = new ArticleBodyVo();
        articleBodyVo.setContent(articleBody.getContent());

        return articleBodyVo;

    }

    @Override
    public Result hotArticle(int limit) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Article::getViewCounts);
        queryWrapper.select(Article::getId,Article::getTitle);
        queryWrapper.last("limit "+limit);
        //select id,title from article order by view_counts desc limit 5
        List<Article> articles = articleMapper.selectList(queryWrapper);

        return Result.success(copyList(articles,false,false));
    }
    @Override
    public Result newArticle(int limit) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Article::getCreateDate);
        queryWrapper.select(Article::getId,Article::getTitle);
        queryWrapper.last("limit "+limit);
        //select id,title from article order by CreateDate desc limit 5
        List<Article> articles = articleMapper.selectList(queryWrapper);

        return Result.success(copyList(articles,false,false));
    }
    /*文章归档*/
    @Override
    public Result listArchives() {
        List<Archives> archivesList = articleMapper.listArchives();
        return Result.success(archivesList);
    }

    @Override
    public Result findArticleById(Long articleId) {
      /**
         * 1.根据id查询文章信息
         * 2.根据bodyId和categoryId 去做关联查询
         */

        Article article = this.articleMapper.selectById(articleId);
        ArticleVo articleVo = copy(article, true, true,true,true);
//线程池，把更新操作扔到线程池中执行，不影响主线程
        threadService.updateArticleViewCount(articleMapper,article);
        return Result.success(articleVo);
    }
    /**
     * 1.发布文章 目的构建Article对象
     * 2. 作者id 当前登陆用户
     * 3. 标签 将标签加入关联表中
     * 4. body内容存储 article bodyId
     * @param articleParam
     * @return
     * 此接口要加入登陆拦截中
     */
    @Override
    public Result publish(ArticleParam articleParam) {
        SysUser sysUser = UserThreadLocal.get();

        Article article = new Article();
        article.setAuthorId(sysUser.getId());
        article.setWeight(Article.Article_Common);
        article.setViewCounts(0);
        article.setTitle(articleParam.getTitle());
        article.setSummary(articleParam.getSummary());
        article.setCommentCounts(0);
        article.setCreateDate(System.currentTimeMillis());
        article.setCategoryId((articleParam.getCategory().getId()));
        //插入之后 会生成一个文章id
        this.articleMapper.insert(article);
        //tag
        List<TagVo> tags = articleParam.getTags();
        if (tags != null){
            for (TagVo tag : tags) {
                Long articleId = article.getId();
                ArticleTag articleTag = new ArticleTag();
                articleTag.setTagId(Long.valueOf((tag.getId())));
                articleTag.setArticleId(articleId);
                articleTagMapper.insert(articleTag);
            }
        }
        //body
        ArticleBody articleBody  = new ArticleBody();
        articleBody.setArticleId(article.getId());
        articleBody.setContent(articleParam.getBody().getContent());
        articleBody.setContentHtml(articleParam.getBody().getContentHtml());
        articleBodyMapper.insert(articleBody);

        article.setBodyId(articleBody.getId());
        articleMapper.updateById(article);
        //将id转换成string放入map
        Map<String,String> map = new HashMap<>();
        map.put("id",article.getId().toString());
        return Result.success(map);
    }


}

