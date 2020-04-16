package com.block.project.springboot.domain.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("SELECT c FROM  Comment c WHERE c.postsId = ?1 ORDER BY c.id DESC")
    List<Comment> findAllDesc(Long postsId);
}
