package com.block.project.springboot.domain.goal;

import com.block.project.springboot.domain.posts.Posts;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
public class Goal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long goalId;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    private int paymentPercent;

    private String resultTitle;

    @Column(columnDefinition = "TEXT")
    private String resultContent;

    private String resultImage;

    @Column(nullable = false)
    private int complete;

    @ManyToOne(cascade = CascadeType.ALL)
    @JsonManagedReference
    private Posts posts;

    @Builder
    public Goal(String content, int paymentPercent, String resultTitle, String resultContent, String resultImage, int complete, Posts posts){
        this.content = content;
        this.paymentPercent = paymentPercent;
        this.resultTitle = resultTitle;
        this.resultContent = resultContent;
        this.resultImage = resultImage;
        this.complete = complete;
        this.posts = posts;
    }

    public void update(String resultTitle, String resultContent, String resultImage, int complete){
        this.resultTitle = resultTitle;
        this.resultContent = resultContent;
        this.resultImage = resultImage;
        this.complete = complete;
    }
}
