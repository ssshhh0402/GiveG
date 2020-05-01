package com.block.project.springboot.web.dto.donation;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DonationRefundRequestDto {
    String email;
    Long postsId;

    @Builder
    public DonationRefundRequestDto(String email, Long postsId){
        this.email = email;
        this.postsId = postsId;
    }
}
