package com.block.project.springboot.web;

import com.block.project.springboot.domain.posts.Posts;
import com.block.project.springboot.service.CommentService;
import com.block.project.springboot.service.PostsService;
import com.block.project.springboot.web.dto.comment.CommentListResponseDto;
import com.block.project.springboot.web.dto.comment.CommentSaveRequestDto;
import com.block.project.springboot.web.dto.comment.CommentUpdateRequestDto;
import com.block.project.springboot.web.dto.posts.PostsResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@CrossOrigin("*")
public class CommentApiController {
    private final CommentService commentService;
    private final PostsService postsService;

    @PostMapping("/api/v1/comment")
    public Long save(@RequestBody CommentSaveRequestDto requestDto){
        log.info("Comment save postId : {}",requestDto.getPostsId());
        return commentService.save(requestDto);
    }

    @PutMapping("/api/v1/comment/{commentId}")
    public Long update(@PathVariable Long commentId, @RequestBody CommentUpdateRequestDto requestDto){
        log.info("Comment update commentId : {}",commentId);
        return commentService.update(commentId, requestDto);
    }

    @GetMapping("/api/v1/comment/{postsId}")
    public List<CommentListResponseDto> findByPostId(@PathVariable Long postsId){
        Posts posts = postsService.findPosts(postsId);

        return commentService.findAllDesc(posts);
    }

    @DeleteMapping("api/v1/comment/{commentId}")
    public Long delete(@PathVariable Long commentId){
        commentService.delete(commentId);
        return commentId;
    }
}
