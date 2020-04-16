package com.block.project.springboot.service;

import com.block.project.springboot.domain.comment.Comment;
import com.block.project.springboot.domain.comment.CommentRepository;
import com.block.project.springboot.web.dto.CommentListResponseDto;
import com.block.project.springboot.web.dto.CommentSaveRequestDto;
import com.block.project.springboot.web.dto.CommentUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CommentService {
    private final CommentRepository commentRepository;

    @Transactional
    public Long save(CommentSaveRequestDto requestDto){
        return commentRepository.save(requestDto.toEntity()).getId();
    }

    @Transactional
    public Long update(Long id, CommentUpdateRequestDto requestDto){
        Comment comment = commentRepository.findById(id).
                orElseThrow(() -> new IllegalArgumentException("해당 댓글이 없습니다. id=" + id));

        comment.update(requestDto.getContent());

        return id;
    }

    @Transactional
    public void delete(Long id){
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 없습니다. id=" + id));

        commentRepository.delete(comment);
    }

    @Transactional(readOnly = true)
    public List<CommentListResponseDto> findAllDesc(Long postsId){
        return commentRepository.findAllDesc(postsId).stream()
                .map(CommentListResponseDto::new)
                .collect(Collectors.toList());
    }
}
