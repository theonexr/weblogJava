package com.xu.blog.Dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xu.blog.Dao.pojo.Tag;

import java.util.List;

public interface TagMapper extends BaseMapper<Tag> {
    /**
 * 根据文章id查询标签列表
 * @param articleId
 * @return
 */
List<Tag> findTagsByArticleId(Long articleId);

List<Long> findHotsTagId(int limit);

    /**
     * 根据tagId查询Tag对象
     * @param tagIds
     * @return
     */
    List<Tag> findTagsByIds(List<Long> tagIds);

}
