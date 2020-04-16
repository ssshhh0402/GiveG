package com.block.project.springboot.web.dto;

import com.block.project.springboot.domain.comment.Comment;
import com.block.project.springboot.domain.posts.Posts;
import lombok.Getter;

@Getter
public class CommentResponseDto {

    private Long id;
    private Long postsId;
    private String content;
    private String author;

    public CommentResponseDto(Comment entity){
        this.id = entity.getId();
        this.postsId = entity.getPostsId();
        this.content = entity.getContent();
        this.author = entity.getAuthor();
    }
}
