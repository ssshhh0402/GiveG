package com.block.project.springboot.web;

import com.block.project.springboot.service.DonationService;
import com.block.project.springboot.service.FabricCCService;
import com.block.project.springboot.service.UserService;
import com.block.project.springboot.web.dto.donation.DonationListResponseDto;
import com.block.project.springboot.web.dto.posts.PostsListResponseDto;
import com.block.project.springboot.web.dto.user.UserResponseDto;
import com.block.project.springboot.web.dto.user.UserSaveRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@CrossOrigin("*")
public class UserApiController {

    private final UserService userService;
    private final DonationService donationService;
    private final FabricCCService fabricCCService;

    @PostMapping("/api/v1/user")
    public String save(@RequestBody UserSaveRequestDto requestDto){
        log.info("User save email : {}",requestDto.getEmail());
        fabricCCService.registerUser(requestDto.getEmail());
        return userService.save(requestDto);
    }

    @GetMapping("/api/v1/user/{id}")
    public UserResponseDto findById(@PathVariable Long id){
        log.info("User findById");
        return userService.findById(id);
    }

    @GetMapping("/api/v1/user/donation/{email}")
    public List<DonationListResponseDto> getDonationList(@PathVariable String email){
        log.info("User getDonationList email : {}", email);
        return donationService.getDonationList(email);
    }

    @GetMapping("/api/v1/user/posts/{email}")
    public List<PostsListResponseDto> getPostsList(@PathVariable String email){
        log.info("User getPostsList email : {}", email);
        return donationService.getPostList(email);
    }
}
