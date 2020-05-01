package com.block.project.springboot.web.dto.comment;

import com.block.project.springboot.domain.comment.Comment;
import com.block.project.springboot.domain.posts.Posts;
import com.block.project.springboot.domain.user.User;
import lombok.Getter;

@Getter
public class CommentResponseDto {

    private Long id;
    private String content;
    private String author;
    private String userPicture;
    private Posts posts;

    public CommentResponseDto(Comment entity, User user){
        this.id = entity.getCommentId();
        this.posts = entity.getPosts();
        this.content = entity.getContent();
        this.author = user.getEmail();
        this.userPicture = user.getPicture();
    }
}
