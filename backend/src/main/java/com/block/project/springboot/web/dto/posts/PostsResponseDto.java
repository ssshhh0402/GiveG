package com.block.project.springboot.web.dto.posts;

import com.block.project.springboot.domain.goal.Goal;
import com.block.project.springboot.domain.posts.Posts;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
public class PostsResponseDto {

    private Long postsId;
    private String title;
    private String content;
    private String author;
    private Long donationGoal;
    private Long donationNow;
    private LocalDate endDate;
    private String image;
    private int peopleNum;
    private int ddays;
    private String state;
    private List<Goal> goals;
    private boolean funded;

    public PostsResponseDto(Posts entity, int ddays, List<Goal> goals, boolean funded){
        this.postsId = entity.getPostsId();
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.author = entity.getAuthor();
        this.donationGoal = entity.getDonationGoal();
        this.donationNow = entity.getDonationNow();
        this.endDate = entity.getEndDate();
        this.image = entity.getImage();
        this.peopleNum = entity.getPeopleNum();
        this.ddays = ddays;
        this.goals = goals;
        this.state = entity.getState();
        this.funded = funded;
    }
}
