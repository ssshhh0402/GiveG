package com.block.project.springboot.domain.comment;

import com.block.project.springboot.domain.posts.Posts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

//    @Query("SELECT c FROM  Comment c WHERE c.postsId = ?1 ORDER BY c.commentId DESC")
//    List<Comment> findAllDesc(Long postsId);

    @Query("SELECT c FROM  Comment c WHERE c.posts = ?1 ORDER BY c.commentId DESC")
    List<Comment> findAllDesc(Posts posts);
}
