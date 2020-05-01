package com.block.project.springboot.domain.donation;

import com.block.project.springboot.domain.BaseTimeEntity;
import com.block.project.springboot.domain.user.User;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
public class Donation extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long donationId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JsonManagedReference
    private User user;

    @Column(nullable = false)
    private Long postsId;

    @Column(nullable = false)
    private int amount;

    private String state;

    private String tid; // 결제 결과 나오는 결제 고유 번호

    @Builder
    public Donation(User user, Long postsId, int amount, String state, String tid){
        this.user = user;
        this.postsId = postsId;
        this.amount = amount;
        this.state = state;
        this.tid = tid;
    }

    public void update(String state, String tid){
        this.state = state;
        this.tid = tid;
    }

}
