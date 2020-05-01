package com.block.project.springboot.web.dto.donation;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PayInfoRequestDto {
    private Long donationId;
    private String pgToken;
}
