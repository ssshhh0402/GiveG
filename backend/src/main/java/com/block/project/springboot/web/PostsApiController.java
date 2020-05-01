package com.block.project.springboot.web;

import com.block.project.springboot.domain.donation.Donation;
import com.block.project.springboot.domain.goal.Goal;
import com.block.project.springboot.domain.posts.Posts;
import com.block.project.springboot.service.*;
import com.block.project.springboot.web.dto.goal.GoalSaveRequestDto;
import com.block.project.springboot.web.dto.goal.GoalUpdateRequestDto;
import com.block.project.springboot.web.dto.kakao.PayRefundResponseDto;
import com.block.project.springboot.web.dto.posts.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@CrossOrigin("*")
public class PostsApiController {

    private final GoalService goalService;
    private final PostsService postsService;
    private final FabricCCService fabricCCService;
    private final DonationService donationService;
    private final KakaoApiService kakaoApiService;

    @PostMapping("/api/v1/posts")
    public Long save(@RequestParam(value = "image") MultipartFile image,
                     @RequestParam(value = "title") String title,
                     @RequestParam(value = "content") String content,
                     @RequestParam(value = "author") String author,
                     @RequestParam(value = "donationGoal") Long donationGoal,
                     @RequestParam(value = "endDate") String endDate,
                     @RequestParam(value = "goalContent1") String goalContent1,
                     @RequestParam(value = "goalPercent1") int goalPercent1,
                     @RequestParam(value = "goalContent2") String goalContent2,
                     @RequestParam(value = "goalPercent2") int goalPercent2,
                     @RequestParam(value = "goalContent3") String goalContent3,
                     @RequestParam(value = "goalPercent3") int goalPercent3) throws Exception {

        List<GoalSaveRequestDto> goals = new ArrayList<>();

        log.info("posts save : title : {}, author : {}",title, author);

        goals.add(GoalSaveRequestDto.builder()
                .content(goalContent1)
                .paymentPercent(goalPercent1)
                .build());

        goals.add(GoalSaveRequestDto.builder()
                .content(goalContent2)
                .paymentPercent(goalPercent2)
                .build());

        goals.add(GoalSaveRequestDto.builder()
                .content(goalContent3)
                .paymentPercent(goalPercent3)
                .build());

        LocalDate endDateLocal = LocalDate.parse(endDate);
        PostsSaveRequestDto requestDto = PostsSaveRequestDto.builder()
                .title(title)
                .content(content)
                .author(author)
                .donationGoal(donationGoal)
                .endDate(endDateLocal)
                .goal(goals)
                .build();

        Long postsId = postsService.save(requestDto, image);
        String userId = postsService.findByPostsId(postsId).getEmail();

        Posts posts = postsService.findPosts(postsId);

        String[] goalIdList = new String[3];

        for (int i = 0; i < goals.size(); i++) {
            Goal goal = goals.get(i).toEntitiy(posts);
            Long goalId = goalService.save(goal);
            goalIdList[i] = goalId + "";
        }

        fabricCCService.registerDonationPJT(title,
                userId,
                requestDto.getDonationGoal().toString(),
                requestDto.getEndDate().toString(),
                new String[]{goalIdList[0], goalIdList[1], goalIdList[2]},
                new String[]{goalPercent1 + "", goalPercent2 + "", + goalPercent3 + ""});

        return postsId;
    }
    @PutMapping("/api/v1/posts{postsId}")
    public Long update(@PathVariable Long postsId, @RequestBody PostsUpdateRequestDto requestDto){
        return postsService.update(postsId, requestDto);
    }

    @DeleteMapping("/api/v1/posts/{postsId}")
    public Long delete(@PathVariable Long postsId){
        postsService.delete(postsId);

        return postsId;
    }

    @GetMapping("/api/v1/posts/{postsId}")
    public PostsResponseDto findById(@PathVariable Long postsId){
        log.info("posts get  postsId : {}",postsId);
        return postsService.findById(postsId);
    }

    @GetMapping("/api/v1/posts/{postsId}/{email}")
    public PostsResponseDto findByIdAndEmail(@PathVariable Long postsId,
                                     @PathVariable String email){
        log.info("posts get  postsId : {} / email : {}",postsId, email);
        return postsService.findByIdAndEmail(postsId, email);
    }

    @GetMapping("/api/v1/posts")
    public List<PostsListResponseDto> getList(){
        log.info("posts getList");
        return postsService.findAllDesc();
    }

    @PutMapping("/api/v1/goal/{goalId}")
    public Long update(@PathVariable Long goalId,
                       @RequestParam(value = "resultTitle") String resultTitle,
                       @RequestParam(value = "resultContent") String resultContent,
                       @RequestParam(value = "image") MultipartFile resultImage,
                       @RequestParam(value = "complete") int complete) throws Exception {

        log.info("goal update goalId : {}, resultTitle : {}", goalId, resultTitle);
        GoalUpdateRequestDto requestDto = GoalUpdateRequestDto.builder()
                .complete(complete)
                .resultTile(resultTitle)
                .resultContent(resultContent)
                .build();

        Posts posts = goalService.findPosts(goalId);

        String email = postsService.findByPostsId(posts.getPostsId()).getEmail();

        fabricCCService.compeleteGoalsDonationProject(posts.getPostsId() + "", email, goalId + "", resultTitle);

        int completeCount = goalService.countComplete(posts);
        log.info("goal completeCount : {}", completeCount);
        if(completeCount == 2){
            Long postsId = postsService.updateState(posts.getPostsId(), "ProjectEnd");
            log.info("Project End     id : {}", postsId);
            fabricCCService.endDonationPJT(postsId + "", email);
        }
        return goalService.update(goalId, requestDto, resultImage);
    }

    @GetMapping("/api/v1/posts/search/{keyword}")
    public List<PostsListResponseDto> searchPosts(@PathVariable String keyword){
        log.info("posts searchPosts keyword : {}","");
        return postsService.searchByTitle(keyword);
    }

    @PutMapping("/api/v1/posts/start/{postsId}")
    public Long startPosts(@PathVariable Long postsId){
        log.info("Start posts postsId : {}", postsId);
        String email = postsService.findByPostsId(postsId).getEmail();
        fabricCCService.startDonationPJT(postsId + "", email);

        return postsService.updateState(postsId, "ProjectStarted");
    }

    @PutMapping("/api/v1/posts/stop/{postsId}")
    public String stopPosts(@PathVariable Long postsId){
        log.info("Stop posts postsId : {}", postsId);

        Posts posts = postsService.findPosts(postsId);
        Long donationNow = posts.getDonationNow();
        int peopleNum = posts.getPeopleNum();

        List<Donation> donationList = donationService.findByPostsId(postsId);

        for (Donation donation : donationList) {
            if(donation.getState().equals("Funded")){
                PayRefundResponseDto refundResponseDto = kakaoApiService.kakaoPayRefund(donation.getTid(), donation.getAmount());
                log.info("Cancel Amount : {}", refundResponseDto.getCanceled_amount().getTotal());
                int cancelAmount = refundResponseDto.getCanceled_amount().getTotal();

                if(cancelAmount == donation.getAmount())
                {
                    donationService.update(donation.getDonationId(), refundResponseDto.getTid(), "Refunded");
                    donationNow -= cancelAmount;
                    peopleNum -= 1;
                }
            }
        }

        log.info("Stop posts  잔액 : {} , 인원수 : {}", donationNow, peopleNum);
        if(donationNow == 0 && peopleNum == 0){
            postsService.updateState(postsId, "ProjectStopped");
            fabricCCService.stopDonationPJT(postsId + "");
            return "Success";
        }
        return "Fail";
    }
}
