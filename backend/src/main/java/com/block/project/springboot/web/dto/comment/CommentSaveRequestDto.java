package com.block.project.springboot.web.dto.comment;

import com.block.project.springboot.domain.comment.Comment;
import com.block.project.springboot.domain.posts.Posts;
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

    public Comment toEntity(Posts posts){
        return Comment.builder()
                .posts(posts)
                .author(author)
                .content(content)
                .build();
    }
}