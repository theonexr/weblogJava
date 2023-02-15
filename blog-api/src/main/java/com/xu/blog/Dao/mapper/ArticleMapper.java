package com.xu.blog.Dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xu.blog.Dao.dos.Archives;
import com.xu.blog.Dao.pojo.Article;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface ArticleMapper extends BaseMapper<Article> {
    List<Archives> listArchives();


    //mybatis-plus分页
    IPage<Article> listAticle(Page<Article> page,
                              Long categoryId,
                              Long tagId,
                              String year,
                              String month);


}
