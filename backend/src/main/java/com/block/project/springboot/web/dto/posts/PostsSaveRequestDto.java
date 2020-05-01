package com.block.project.springboot.web.dto.posts;

import com.block.project.springboot.domain.goal.Goal;
import com.block.project.springboot.domain.posts.Posts;
import com.block.project.springboot.web.dto.goal.GoalSaveRequestDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class PostsSaveRequestDto {
    private String title;
    private String content;
    private String author;
    private Long donationGoal;
    private LocalDate endDate;
    private List<GoalSaveRequestDto> goal = new ArrayList<>();

    @Builder
    public PostsSaveRequestDto(String title, String content, String author, Long donationGoal, LocalDate endDate, List<GoalSaveRequestDto> goal){
        this.title = title;
        this.content = content;
        this.author = author;
        this.donationGoal = donationGoal;
        this.endDate = endDate;
        this.goal = goal;
    }

    public Posts toEntity(String imageUrl){
        return Posts.builder()
                .title(title)
                .content(content)
                .author(author)
                .donationGoal(donationGoal)
                .donationNow(0L)
                .endDate(endDate)
                .image(imageUrl)
                .state("ProjectRegistered")
                .build();
    }
}
