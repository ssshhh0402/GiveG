package com.block.project.springboot.web.dto.donation;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PayApproveRequestDto {
    String pgToken;
    Long donationId;
}
