package com.block.project.springboot.web.dto;

import com.block.project.springboot.domain.comment.Comment;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentListResponseDto {
    private Long id;
    private String content;
    private String author;
    private LocalDateTime modifiedDate;

    public CommentListResponseDto(Comment entity){
        this.id = entity.getId();
        this.content = entity.getContent();
        this.author = entity.getAuthor();
        this.modifiedDate = entity.getModifiedDate();
    }
}
