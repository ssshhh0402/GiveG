package com.block.project.springboot.web;

import com.block.project.springboot.service.CommentService;
import com.block.project.springboot.web.dto.CommentListResponseDto;
import com.block.project.springboot.web.dto.CommentSaveRequestDto;
import com.block.project.springboot.web.dto.CommentUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RequiredArgsConstructor
@RestController
public class CommentApiController {
    private final CommentService commentService;

    @PostMapping("/api/v1/comment")
    public Long save(@RequestBody CommentSaveRequestDto requestDto){
        return commentService.save(requestDto);
    }

    @PutMapping("/api/v1/comment/{id}")
    public Long update(@PathVariable Long id, @RequestBody CommentUpdateRequestDto requestDto){
        return commentService.update(id, requestDto);
    }

    @GetMapping("/api/v1/comment/{postsId}")
    public List<CommentListResponseDto> findByPostId(@PathVariable Long postsId){
        return commentService.findAllDesc(postsId);
    }

    @DeleteMapping("api/v1/comment/{id}")
    public Long delete(@PathVariable Long id){
        commentService.delete(id);
        return id;
    }
}
