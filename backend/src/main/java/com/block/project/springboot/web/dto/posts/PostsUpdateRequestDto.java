package com.block.project.springboot.web.dto.posts;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostsUpdateRequestDto {

    private String title;
    private String content;
    private String image;
    private Long donationNow;
    private int peopleNum;
    private String state;

    @Builder
    public PostsUpdateRequestDto(String title, String content, String image, Long donationNow, int peopleNum, String state){
        this.title = title;
        this.content = content;
        this.image = image;
        this.donationNow = donationNow;
        this.peopleNum = peopleNum;
        this.state = state;
    }
}