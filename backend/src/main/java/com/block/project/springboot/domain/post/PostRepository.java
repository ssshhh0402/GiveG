package com.block.project.springboot.domain.post;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    Optional<Post> findByPostsId(Long postId);

    List<Post> findByUserId(Long userId);
}
