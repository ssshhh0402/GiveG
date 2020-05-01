package com.block.project.springboot.web.dto.goal;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Getter
@NoArgsConstructor
public class GoalUpdateRequestDto {
    private int complete;
    private String resultTile;
    private String resultContent;

    @Builder
    public GoalUpdateRequestDto(int complete, String resultTile, String resultContent){
        this.complete = complete;
        this.resultTile = resultTile;
        this.resultContent = resultContent;
    }
}
