package com.block.project.springboot.web.dto.donation;

import com.block.project.springboot.domain.donation.Donation;
import com.block.project.springboot.domain.posts.Posts;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class DonationListResponseDto {
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
    private LocalDate createDate;
    private int amount;
    private String state;

    public DonationListResponseDto(Posts posts, int ddays, Donation donation){
        this.id = posts.getPostsId();
        this.title = posts.getTitle();
        this.author = posts.getAuthor();
        this.modifiedDate = posts.getModifiedDate();
        this.donationGoal = posts.getDonationGoal();
        this.donationNow = posts.getDonationNow();
        this.endDate = posts.getEndDate();
        this.image = posts.getImage();
        this.peopleNum = posts.getPeopleNum();
        this.ddays = ddays;
        this.createDate = donation.getCreateDate().toLocalDate();
        this.amount = donation.getAmount();
        this.state = donation.getState();
    }
}
