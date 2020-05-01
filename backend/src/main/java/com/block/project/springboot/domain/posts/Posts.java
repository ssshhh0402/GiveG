package com.block.project.springboot.domain.posts;

import com.block.project.springboot.domain.BaseTimeEntity;
import com.block.project.springboot.domain.comment.Comment;
import com.block.project.springboot.domain.goal.Goal;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Entity
public class Posts extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postsId;

    @Column(length = 500, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(nullable = false)
    private String author;

    @Column(length = 500, nullable = false)
    private String image;

    @Column(nullable = false)
    private Long donationGoal;

    @Column(nullable = false)
    @ColumnDefault("0")
    private Long donationNow;

    @Column(nullable = false)
    private int peopleNum;

    @Column(length = 100, nullable = false)
    private String state;

    @Column(nullable = false)
    private LocalDate endDate;

    @OneToMany(mappedBy = "posts")
    @JsonBackReference
    private List<Comment> comment = new ArrayList<>();

    @OneToMany(mappedBy = "posts")
    @JsonBackReference
    private List<Goal> goal = new ArrayList<>();

    @Builder
    public Posts(String title, String content, String author, String image, Long donationGoal, Long donationNow, int peopleNum, String state, LocalDate endDate){
        this.title = title;
        this.content = content;
        this.author = author;
        this.image = image;
        this.donationGoal = donationGoal;
        this.donationNow = donationNow;
        this.peopleNum = peopleNum;
        this.state = state;
        this.endDate = endDate;
    }

    public void update(String title, String content, String image, Long donationNow, int peopleNum, String state){
        this.title = title;
        this.content = content;
        this.image = image;
        this.donationNow = donationNow;
        this.peopleNum = peopleNum;
        this.state = state;
    }

    public void updateState(String state){
        this.state = state;
    }

    public void refundUpdate(int amount){
        this.donationNow -= amount;
        this.peopleNum -= 1;
    }
}
