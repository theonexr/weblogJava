package com.xu.blog.vo.params;

import lombok.Data;

@Data
public class CommentParam {
    private Long articleId;

    private String content;

    private Long parentId;

    private Long toUserId;
}
