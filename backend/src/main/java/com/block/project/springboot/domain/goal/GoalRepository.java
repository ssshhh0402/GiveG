package com.block.project.springboot.domain.goal;

import com.block.project.springboot.domain.posts.Posts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface GoalRepository extends JpaRepository<Goal, Long> {

    Optional<Goal> findByGoalId(Long goalId);

    @Query("SELECT g FROM Goal g WHERE g.posts = ?1 ORDER BY g.goalId DESC")
    List<Goal> findByPostsDesc(Posts postsId);

    int countByPostsAndComplete(Posts posts, int complete);
}
