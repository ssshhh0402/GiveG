package com.block.project.springboot.domain.comment;

import com.block.project.springboot.domain.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@NoArgsConstructor
@Getter
@Entity
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long postsId;

    private String author;

    private String content;

    @Builder
    public Comment(Long postsId, String author, String content){
        this.postsId = postsId;
        this.author = author;
        this.content = content;
    }

    public void update(String content){
        this.content = content;
    }
}
