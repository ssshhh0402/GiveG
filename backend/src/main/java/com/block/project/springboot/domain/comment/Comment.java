package com.block.project.springboot.domain.comment;

import com.block.project.springboot.domain.BaseTimeEntity;
import com.block.project.springboot.domain.posts.Posts;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    // User 이메일 입력하고 비교하자
    @Column(nullable = false)
    private String author;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @ManyToOne(cascade = CascadeType.ALL)
    @JsonManagedReference
    private Posts posts;

    @Builder
    public Comment(Posts posts, String author, String content){
        this.posts = posts;
        this.author = author;
        this.content = content;
    }

    public void update(String content){
        this.content = content;
    }
}
