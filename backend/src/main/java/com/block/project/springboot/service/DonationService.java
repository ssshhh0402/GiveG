package com.block.project.springboot.service;

import com.block.project.springboot.domain.donation.Donation;
import com.block.project.springboot.domain.donation.DonationRepository;
import com.block.project.springboot.domain.post.Post;
import com.block.project.springboot.domain.post.PostRepository;
import com.block.project.springboot.domain.posts.Posts;
import com.block.project.springboot.domain.posts.PostsRepository;
import com.block.project.springboot.domain.user.User;
import com.block.project.springboot.domain.user.UserRepository;
import com.block.project.springboot.web.dto.donation.DonationListResponseDto;
import com.block.project.springboot.web.dto.donation.DonationPayRequestDto;
import com.block.project.springboot.web.dto.posts.PostsListResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class DonationService {
    private final DonationRepository donationRepository;
    private final UserRepository userRepository;
    private final PostsRepository postsRepository;
    private final PostRepository postRepository;

    @Transactional
    public Long save(DonationPayRequestDto requestDto){
        User user = userRepository.findByEmail(requestDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("해당 회원이 없습니다. id=" + requestDto.getUserId()));

        int amount = requestDto.getAmount();

        Posts posts = postsRepository.getOne(requestDto.getPostsId());

        posts.update(posts.getTitle(),
                posts.getContent(),
                posts.getImage(),
                posts.getDonationNow() + amount,
                posts.getPeopleNum() + 1,
                posts.getState()
        );

        return donationRepository.save(requestDto.toEntity(user)).getDonationId();
    }

    @Transactional
    public Long update(Long dontaionId, String tid, String state){
        Donation donation = donationRepository.findById(dontaionId).
                orElseThrow(() -> new IllegalArgumentException("해당 기부 기록이 없습니다. id=" + dontaionId));

        donation.update(state, tid);

        return dontaionId;
    }

    @Transactional(readOnly = true)
    public List<DonationListResponseDto> getDonationList(String email){
        Long userId = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다. email=" + email)).getId();
        
        List<Donation> donationList = donationRepository.findByUserId(userId);
        List<DonationListResponseDto> responseDtoList = new LinkedList<>();

        for (Donation donation : donationList) {
            Long postsId = donation.getPostsId();
            Posts posts = postsRepository.getOne(postsId);

            LocalDate today = LocalDate.now();
            LocalDate dday = posts.getEndDate();

            int ddays = Math.abs(dday.until(today).getDays());

            DonationListResponseDto responseDto = new DonationListResponseDto(posts, ddays, donation);
            responseDtoList.add(responseDto);
        }

        return  responseDtoList;
    }

    @Transactional
    public List<PostsListResponseDto> getPostList(String email){
        Long userId = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다. email=" + email)).getId();

        List<Post> postList = postRepository.findByUserId(userId);
        List<PostsListResponseDto> responseDtoList = new LinkedList<>();

        for (Post post : postList) {
            Long postsId = post.getPostsId();
            Posts posts = postsRepository.findById(postsId)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다. id=" + postsId));

            LocalDate today = LocalDate.now();
            LocalDate dday = posts.getEndDate();

            int ddays = Math.abs(dday.until(today).getDays());

            PostsListResponseDto responseDto = new PostsListResponseDto(posts, ddays);
            responseDtoList.add(responseDto);
        }
        return responseDtoList;
    }

    @Transactional
    public Donation findById(Long donationId){
        return donationRepository.findById(donationId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 기부이력입니다. id=" + donationId));
    }

    @Transactional
    public Donation findByUserAndPostsIdAndState(User user, Long postsId, String state){
        return donationRepository.findByUserAndPostsIdAndState(user, postsId, state);
    }

    @Transactional
    public List<Donation> findByPostsId(Long postsId){
        return donationRepository.findAllByPostsId(postsId);
    }

}
