package com.block.project.springboot.domain.donation;

import com.block.project.springboot.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DonationRepository extends JpaRepository<Donation, Long> {
    List<Donation> findByUserId(Long userId);

    Donation findByUserAndPostsIdAndState(User user, Long postsId, String state);

    List<Donation> findAllByPostsId(Long postId);

    @Query("SELECT COUNT(d.donationId) FROM Donation d WHERE d.user = ?1 AND d.postsId = ?2")
    int checkDonate(User user, Long postsId);

    int countByUserAndPostsIdAndState(User user, Long postsId, String state);
}
