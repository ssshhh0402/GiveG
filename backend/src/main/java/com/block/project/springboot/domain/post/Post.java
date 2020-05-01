package com.block.project.springboot.domain.post;

import com.block.project.springboot.domain.user.User;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JsonManagedReference
    private User user;

    @Column(nullable = false)
    private Long postsId;

    @Builder
    public Post(User user, Long postsId){
        this.user = user;
        this.postsId = postsId;
    }
}
