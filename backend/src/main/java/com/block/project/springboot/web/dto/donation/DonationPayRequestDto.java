package com.block.project.springboot.web.dto.donation;

import com.block.project.springboot.domain.donation.Donation;
import com.block.project.springboot.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DonationPayRequestDto {
    private String userId;
    private Long postsId;
    private int amount;

    @Builder
    public DonationPayRequestDto(String userId, Long postsId, int amount){
        this.userId = userId;
        this.postsId = postsId;
        this.amount = amount;
    }

    public Donation toEntity(User user){
        return Donation.builder()
                .user(user)
                .postsId(postsId)
                .amount(amount)
                .state("Proceeding")
                .tid("Proceeding")
                .build();
    }
}
