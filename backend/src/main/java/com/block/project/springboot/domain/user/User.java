package com.block.project.springboot.domain.user;

import com.block.project.springboot.domain.BaseTimeEntity;
import com.block.project.springboot.domain.donation.Donation;
import com.block.project.springboot.domain.post.Post;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class User extends BaseTimeEntity {
    @Id
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable =  false)
    private String email;

    @Column(nullable = false)
    private String picture;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @OneToMany(mappedBy = "user")
    @JsonBackReference
    private List<Post> post = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    @JsonBackReference
    private List<Donation> donation = new ArrayList<>();

    @Builder
    public User(Long id, String name, String email, String picture, Role role){
        this.id = id;
        this.name = name;
        this.email = email;
        this.picture = picture;
        this.role = role;
    }

    public User update(String name, String picture){
        this.name = name;
        this.picture = picture;

        return this;
    }

    public String getRoleKey(){
        return this.role.getKey();
    }

}
