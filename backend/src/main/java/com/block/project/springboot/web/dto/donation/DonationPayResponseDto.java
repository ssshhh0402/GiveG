package com.block.project.springboot.web.dto.donation;

import lombok.Builder;
import lombok.Getter;

@Getter
public class DonationPayResponseDto {
    String redirectUrl;
    Long donationId;

    @Builder
    public DonationPayResponseDto(String redirectUrl, Long donationId){
        this.redirectUrl = redirectUrl;
        this.donationId = donationId;
    }
}
