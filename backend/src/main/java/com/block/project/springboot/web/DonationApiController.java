package com.block.project.springboot.web;

import com.block.project.springboot.domain.donation.Donation;
import com.block.project.springboot.domain.posts.Posts;
import com.block.project.springboot.domain.user.User;
import com.block.project.springboot.service.*;
import com.block.project.springboot.web.dto.donation.*;
import com.block.project.springboot.web.dto.kakao.PayApproveResponseDto;
import com.block.project.springboot.web.dto.kakao.PayReadyResponseDto;
import com.block.project.springboot.web.dto.kakao.PayRefundResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@CrossOrigin("*")
public class DonationApiController {

    private final PostsService postsService;
    private final DonationService donationService;
    private final KakaoApiService kakaoApiService;
    private final UserService userService;
    private final FabricCCService fabricCCService;

    @PostMapping("/api/v1/donation")
    public DonationPayResponseDto saveDonation(@RequestBody DonationPayRequestDto requestDto){
        log.info("Donation save  postsId : {}, userId : {}, price : {}", requestDto.getPostsId(), requestDto.getUserId(), requestDto.getAmount());
        Long donationId =  donationService.save(requestDto);

        Posts posts = postsService.findPosts(requestDto.getPostsId());

        PayReadyResponseDto payReadyResponseDto = kakaoApiService.readyPayment(donationId, requestDto.getUserId(), posts.getTitle(), requestDto.getAmount());

        donationService.update(donationId, payReadyResponseDto.getTid(), "Proceeding");

        DonationPayResponseDto responseDto = DonationPayResponseDto.builder()
                .donationId(donationId)
                .redirectUrl(payReadyResponseDto.getNext_redirect_pc_url())
                .build();

        return responseDto;
    }

    @PutMapping("/api/v1/donation")
    public String approvePayment(@RequestBody PayApproveRequestDto requestDto){
        log.info("approvePayment ");

        Donation donation = donationService.findById(requestDto.getDonationId());

        String userId = donation.getUser().getEmail();

        PayApproveResponseDto approveResponseDto = kakaoApiService.kakaoPayInfo(requestDto.getPgToken(),donation.getTid(),requestDto.getDonationId(),userId);
        log.info("Approve amount : {}", approveResponseDto.getAmount().getTotal());

        if(donation.getAmount() == approveResponseDto.getAmount().getTotal()){
            donationService.update(requestDto.getDonationId(), donation.getTid(), "Funded");
            String title = postsService.findPosts(donation.getPostsId()).getTitle();

            fabricCCService.donateToPJT(userId, title, donation.getAmount() + "");

            return "Success";
        }

        return "Fail";
    }

    @PutMapping("/api/v1/donation/refund")
    public String refundPayment(@RequestBody DonationRefundRequestDto requestDto){
        User user = userService.findByEmail(requestDto.getEmail());

        Donation donation = donationService.findByUserAndPostsIdAndState(user, requestDto.getPostsId(),"Funded");
        log.info("refundPayment donationId : {}", donation.getDonationId());

        int amount = donation.getAmount();
        PayRefundResponseDto refundResponseDto = kakaoApiService.kakaoPayRefund(donation.getTid(), amount);
        log.info("Cancel Amount : {}", refundResponseDto.getCanceled_amount().getTotal());
        if(refundResponseDto.getCanceled_amount().getTotal() == amount)
        {
            donationService.update(donation.getDonationId(), refundResponseDto.getTid(), "Refunded");
            postsService.refundUpdate(requestDto.getPostsId(), amount);

            String title = postsService.findPosts(requestDto.getPostsId()).getTitle();
            fabricCCService.refundToonationPJT(requestDto.getEmail(), title);

            return "Success";
        }
        return "Fail";
    }
}
