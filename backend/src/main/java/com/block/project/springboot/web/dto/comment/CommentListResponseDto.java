package com.block.project.springboot.web.dto.comment;

import com.block.project.springboot.domain.comment.Comment;
import com.block.project.springboot.domain.user.User;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentListResponseDto {
    private Long id;
    private String content;
    private String author;
    private String userPicture;
    private LocalDateTime modifiedDate;

    public CommentListResponseDto(Comment entity, User user){
        this.id = entity.getCommentId();
        this.content = entity.getContent();
        this.author = entity.getAuthor();
        this.modifiedDate = entity.getModifiedDate();
        this.userPicture = user.getPicture();
    }
}
