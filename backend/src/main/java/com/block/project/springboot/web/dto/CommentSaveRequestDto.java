package com.block.project.springboot.web.dto;

import com.block.project.springboot.domain.comment.Comment;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentSaveRequestDto {
    private Long postsId;
    private String author;
    private String content;

    @Builder
    public CommentSaveRequestDto(Long postsId, String author, String content){
        this.postsId = postsId;
        this.author = author;
        this.content = content;
    }

    public Comment toEntity(){
        return Comment.builder()
                .postsId(postsId)
                .author(author)
                .content(content)
                .build();
    }
}