package com.block.project.springboot.service;

import com.block.project.springboot.domain.goal.Goal;
import com.block.project.springboot.domain.goal.GoalRepository;
import com.block.project.springboot.domain.posts.Posts;
import com.block.project.springboot.domain.posts.PostsRepository;
import com.block.project.springboot.web.dto.goal.GoalUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@RequiredArgsConstructor
@Service
public class GoalService {
    private final GoalRepository goalRepository;
    private final PostsRepository postsRepository;

    public static final String SAVE_FOLDER = "/home/ubuntu/image/";
    public static final String IMAGE_URL = "http://i02b202.p.ssafy.io:8081/image/";

    @Transactional
    public Long save(Goal requestDto){
        return goalRepository.save(requestDto).getGoalId();
    }

    @Transactional
    public Long update(Long goalId, GoalUpdateRequestDto requestDto, MultipartFile image) throws Exception{
        Goal goal = goalRepository.findById(goalId)
                .orElseThrow(() -> new IllegalArgumentException("해당 목표가 없습니다. id=" + goalId));

        String saveUrl = "none";
        if(!image.isEmpty()){
            String imageName = image.getOriginalFilename();
            String imageExtension = FilenameUtils.getExtension(imageName).toLowerCase();
            File destinationImage;
            String destinationImageName;
            String imageUrl = SAVE_FOLDER;

            SimpleDateFormat timeFormat = new SimpleDateFormat("yyMMddHHmmss");
            destinationImageName = timeFormat.format(new Date()) + "." + imageExtension;
            destinationImage = new File(imageUrl + destinationImageName);

            log.info("Image uploaded : {}", destinationImageName);

            image.transferTo(destinationImage);
            saveUrl = IMAGE_URL + destinationImageName;
        }

        goal.update(requestDto.getResultTile(),
                requestDto.getResultContent(),
                saveUrl,
                requestDto.getComplete());

        return goalId;
    }

    @Transactional
    public int countComplete(Posts posts){

        return goalRepository.countByPostsAndComplete(posts, 1);
    }

    @Transactional
    public Posts findPosts(Long goalId){

        Posts posts = goalRepository.findByGoalId(goalId)
                .orElseThrow(() -> new IllegalArgumentException("해당 목표가 없습니다. id=" + goalId)).getPosts();

        return posts;
    }
}
