package com.block.project.springboot.web.dto.goal;

import com.block.project.springboot.domain.goal.Goal;
import com.block.project.springboot.domain.posts.Posts;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Getter
@NoArgsConstructor
public class GoalSaveRequestDto {
    private String content;
    private int paymentPercent;

    @Builder
    public GoalSaveRequestDto(String content, int paymentPercent){
        this.content = content;
        this.paymentPercent = paymentPercent;
    }

    public Goal toEntitiy(Posts posts){
        return Goal.builder()
                .content(content)
                .paymentPercent(paymentPercent)
                .resultTitle("")
                .resultContent("")
                .resultImage("")
                .complete(0)
                .posts(posts)
                .build();
    }
}
