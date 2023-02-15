package com.xu.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xu.blog.Dao.mapper.CommentMapper;
import com.xu.blog.Dao.pojo.Comment;
import com.xu.blog.Dao.pojo.SysUser;
import com.xu.blog.service.CommentService;
import com.xu.blog.service.SysUserService;
import com.xu.blog.utils.UserThreadLocal;
import com.xu.blog.vo.CommentVo;
import com.xu.blog.vo.Result;
import com.xu.blog.vo.UserVo;
import com.xu.blog.vo.params.CommentParam;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {
    @Resource
    private CommentMapper commentMapper;
    @Resource
    private SysUserService sysUserService;

    @Override
    public Result commentsByArticleId(Long articleId) {
        /*
          1.根据文章id查询评论列表,从 comment 中查询
          2.根据作者id查询作者信息
          3.如果 level=1,查询有没有子评论,\
          4.如果有  根据评论id进行查询
         */
        LambdaQueryWrapper<Comment> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getArticleId,articleId);
        queryWrapper.eq(Comment::getLevel,1);
        List<Comment> comments = commentMapper.selectList(queryWrapper);
        List<CommentVo> commentVoList= copyList(comments);
        return Result.success(commentVoList);

    }
//发表评论
    @Override
    public Result comment(CommentParam commentParam) {
        SysUser sysUser = UserThreadLocal.get();
        Comment comment = new Comment();
        comment.setArticleId(commentParam.getArticleId());
        comment.setAuthorId(sysUser.getId());
        comment.setContent(commentParam.getContent());
        comment.setCreateDate((System.currentTimeMillis()));

        Long parent = commentParam.getParentId();
        //如果父id为空,则父评论,否则为子评论
        if (parent == null || parent == 0) {
            comment.setLevel(1);
        } else {
            comment.setLevel(2);
        }
        comment.setParentId(parent == null ? 0 : parent);

        Long toUserId = commentParam.getToUserId();
        comment.setToUid(toUserId == null ? 0 : toUserId);
        this.commentMapper.insert(comment);
        //插入到数据库
        return Result.success(null);
    }

    //子评论查询
    private List<CommentVo> copyList(List<Comment> comments) {
        List<CommentVo> commentVoList = new ArrayList<>();
        for (Comment comment : comments) {
            commentVoList.add(copy(comment));
        }
        return commentVoList;
    }

    private CommentVo copy(Comment comment) {
        CommentVo commentVo = new CommentVo();
        //将类型相同的copy到commentVo中
        BeanUtils.copyProperties(comment, commentVo);
        //时间格式化
        commentVo.setCreateDate(new DateTime(comment.getCreateDate()).toString("yyyy-MM-dd HH:mm"));
        //作者信息
        Long authorId = comment.getAuthorId();
//        System.out.println(authorId);
        UserVo userVo = this.sysUserService.findUserVoById(authorId);
        commentVo.setAuthor(userVo);

        //子评论
        Integer level = comment.getLevel();
        if (level == 1){
            Long id = comment.getId();
            List<CommentVo> commentVoList = findCommentByParentId(id);
            commentVo.setChildrens(commentVoList);
        }

        //toUser向谁评论
        if (level > 1) {
            Long toUid = comment.getToUid();
            UserVo toUserVo = this.sysUserService.findUserVoById(toUid);
            commentVo.setToUser(toUserVo);

        }

        return commentVo;
    }
//子评论查询
    private List<CommentVo> findCommentByParentId(Long id) {
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getParentId,id);
        queryWrapper.eq(Comment::getLevel,2);

        return copyList(commentMapper.selectList(queryWrapper));
    }
}
