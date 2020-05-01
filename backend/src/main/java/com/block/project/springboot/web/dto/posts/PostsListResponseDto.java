package com.block.project.springboot.web.dto.posts;

import com.block.project.springboot.domain.posts.Posts;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class PostsListResponseDto {
    private Long id;
    private String title;
    private String author;
    private LocalDateTime modifiedDate;
    private Long donationGoal;
    private Long donationNow;
    private LocalDate endDate;
    private String image;
    private int peopleNum;
    private int ddays;

    public PostsListResponseDto(Posts entity, int ddays){
        this.id = entity.getPostsId();
        this.title = entity.getTitle();
        this.author = entity.getAuthor();
        this.modifiedDate = entity.getModifiedDate();
        this.donationGoal = entity.getDonationGoal();
        this.donationNow = entity.getDonationNow();
        this.endDate = entity.getEndDate();
        this.image = entity.getImage();
        this.peopleNum = entity.getPeopleNum();
        this.ddays = ddays;
    }
}
