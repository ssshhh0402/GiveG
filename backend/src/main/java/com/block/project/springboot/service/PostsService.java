package com.block.project.springboot.service;

import com.block.project.springboot.domain.donation.DonationRepository;
import com.block.project.springboot.domain.goal.Goal;
import com.block.project.springboot.domain.goal.GoalRepository;
import com.block.project.springboot.domain.post.Post;
import com.block.project.springboot.domain.post.PostRepository;
import com.block.project.springboot.domain.posts.Posts;
import com.block.project.springboot.domain.posts.PostsRepository;
import com.block.project.springboot.domain.user.User;
import com.block.project.springboot.domain.user.UserRepository;
import com.block.project.springboot.web.dto.goal.GoalSaveRequestDto;
import com.block.project.springboot.web.dto.posts.PostsListResponseDto;
import com.block.project.springboot.web.dto.posts.PostsResponseDto;
import com.block.project.springboot.web.dto.posts.PostsSaveRequestDto;
import com.block.project.springboot.web.dto.posts.PostsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class PostsService {
    private final PostsRepository postsRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final GoalRepository goalRepository;
    private final DonationRepository donationRepository;

    private final GoalService goalService;

    public static final String SAVE_FOLDER = "/home/ubuntu/image/";
    public static final String IMAGE_URL = "http://i02b202.p.ssafy.io:8081/image/";
//    public static final String SAVE_FOLDER = "C:/ssafy/spring_blockchain/src/main/resources/static/image/";
//    public static final String IMAGE_URL = "localhost:8081/image/";

    @Transactional
    public Long save(PostsSaveRequestDto requestDto, MultipartFile image) throws Exception {

        User user = userRepository.findByEmail(requestDto.getAuthor())
                .orElseThrow(() -> new IllegalArgumentException("해당 회원이 없습니다. id=" + requestDto.getAuthor()));

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
        Posts posts = requestDto.toEntity(saveUrl);
        Long postsId = postsRepository.save(posts).getPostsId();

        Post post = Post.builder()
                .user(user)
                .postsId(postsId)
                .build();

        postRepository.save(post);

//        GoalSaveRequestDto[] requestDtoList = requestDto.getGoal();
//
//        for (GoalSaveRequestDto saveRequestDto : requestDtoList) {
//            Goal goal = saveRequestDto.toEntitiy(posts);
//            goalService.save(goal);
//        }

        return postsId;
    }

    @Transactional
    public Long update(Long postsId, PostsUpdateRequestDto requestDto){
        Posts posts = postsRepository.findById(postsId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + postsId));

        posts.update(requestDto.getTitle(),
                    requestDto.getContent(),
                    requestDto.getImage(),
                    requestDto.getDonationNow(),
                    requestDto.getPeopleNum(),
                    requestDto.getState());

        return postsId;
    }

    public PostsResponseDto findById(Long postsId){
        Posts posts = postsRepository.findById(postsId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + postsId));

        LocalDate today = LocalDate.now();
        LocalDate dday = posts.getEndDate();

        int ddays = dday.until(today).getDays();
//        Long donationGap = posts.getDonationGoal() - posts.getDonationNow();
//
//        String state = posts.getState();
//        if(posts.getState().equals("ProjectRegistered") && ddays >= 0 && donationGap <= 0){
//            state = "ProjectStarted";
//        }

        List<Goal> goals = goalRepository.findByPostsDesc(posts);
        return new PostsResponseDto(posts, ddays, goals, true);
    }

    public PostsResponseDto findByIdAndEmail(Long postsId, String email){
        Posts posts = postsRepository.findById(postsId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + postsId));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원이 없습니다. email=" + email));

        LocalDate today = LocalDate.now();
        LocalDate dday = posts.getEndDate();

        int ddays = dday.until(today).getDays();

        boolean funded = true;

        if(donationRepository.countByUserAndPostsIdAndState(user, postsId, "Funded") > 0){
            funded = false;
        }

        List<Goal> goals = goalRepository.findByPostsDesc(posts);
        return new PostsResponseDto(posts, ddays, goals, funded);
    }

    @Transactional(readOnly = true)
    public List<PostsListResponseDto> findAllDesc(){
        List<Posts> postsList = postsRepository.findAllDesc();

        List<PostsListResponseDto> responseDtoList = new LinkedList<>();

        for (Posts posts : postsList) {
            LocalDate today = LocalDate.now();
            LocalDate dday = posts.getEndDate();

            int ddays = dday.until(today).getDays();
//            Long donationGap = posts.getDonationGoal() - posts.getDonationNow();
//
//            String state = posts.getState();
//            if(posts.getState().equals("ProjectRegistered") && ddays >= 0 && donationGap <= 0){
//                state = "ProjectStarted";
//            }

            PostsListResponseDto responseDto = new PostsListResponseDto(posts, ddays);
            responseDtoList.add(responseDto);
        }
        return responseDtoList;
    }

    @Transactional
    public void delete(Long postsId){
        Posts posts = postsRepository.findById(postsId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시물이 없습니다. id=" + postsId));

        postsRepository.delete(posts);
    }

    public Posts findPosts(Long postsId){
        Posts posts = postsRepository.findById(postsId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + postsId));
        return  posts;
    }

    public User findByPostsId(Long postsId){
        Post post = postRepository.findByPostsId(postsId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + postsId));

        User user = post.getUser();

        return user;
    }

    @Transactional
    public List<PostsListResponseDto> searchByTitle(String keyword){
        List<Posts> postsList = postsRepository.findByTitleContaining(keyword);

        List<PostsListResponseDto> responseDtoList = new LinkedList<>();

        for (Posts posts : postsList) {
            LocalDate today = LocalDate.now();
            LocalDate dday = posts.getEndDate();

            int ddays = dday.until(today).getDays();

            PostsListResponseDto responseDto = new PostsListResponseDto(posts, ddays);
            responseDtoList.add(responseDto);
        }
        return responseDtoList;
    }

    @Transactional
    public Long updateState(Long postsId, String state){
        Posts posts = postsRepository.findById(postsId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + postsId));

        posts.updateState(state);

        postsRepository.save(posts);

        return postsId;
    }

    @Transactional
    public Long refundUpdate(Long postsId, int amount){
        Posts posts = postsRepository.findById(postsId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + postsId));

        posts.refundUpdate(amount);

        return postsId;
    }
}
