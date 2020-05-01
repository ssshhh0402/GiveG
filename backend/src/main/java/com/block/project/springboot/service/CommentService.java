package com.block.project.springboot.service;

import com.block.project.springboot.domain.comment.Comment;
import com.block.project.springboot.domain.comment.CommentRepository;
import com.block.project.springboot.domain.posts.Posts;
import com.block.project.springboot.domain.posts.PostsRepository;
import com.block.project.springboot.domain.user.User;
import com.block.project.springboot.domain.user.UserRepository;
import com.block.project.springboot.web.dto.comment.CommentListResponseDto;
import com.block.project.springboot.web.dto.comment.CommentSaveRequestDto;
import com.block.project.springboot.web.dto.comment.CommentUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostsRepository postsRepository;
    private final UserRepository userRepository;

//    @Transactional
//    public Long save(CommentSaveRequestDto requestDto){
//        return commentRepository.save(requestDto.toEntity()).getPostsId();
//    }
    @Transactional
    public Long save(CommentSaveRequestDto requestDto){
        Posts posts = postsRepository.findById(requestDto.getPostsId())
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + requestDto.getPostsId()));

        return commentRepository.save(requestDto.toEntity(posts)).getPosts().getPostsId();
    }

    @Transactional
    public Long update(Long commentId, CommentUpdateRequestDto requestDto){
        Comment comment = commentRepository.findById(commentId).
                orElseThrow(() -> new IllegalArgumentException("해당 댓글이 없습니다. id=" + commentId));

        comment.update(requestDto.getContent());

        return commentId;
    }

    @Transactional
    public void delete(Long commentId){
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 없습니다. id=" + commentId));

        commentRepository.delete(comment);
    }

    @Transactional(readOnly = true)
    public List<CommentListResponseDto> findAllDesc(Posts posts){
        List<Comment> comments = commentRepository.findAllDesc(posts);

        List<CommentListResponseDto> responseDtoList = new LinkedList<>();

        for (Comment comment: comments) {
            User user = userRepository.findByEmail(comment.getAuthor())
                    .orElseThrow(() -> new IllegalArgumentException("존재하지않는 회원입니다. id=" + comment.getAuthor()));

            CommentListResponseDto responseDto = new CommentListResponseDto(comment, user);
            responseDtoList.add(responseDto);
        }

        return  responseDtoList;
    }
}
