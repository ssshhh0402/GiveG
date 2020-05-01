package com.block.project.springboot.service;

import com.block.project.springboot.domain.user.User;
import com.block.project.springboot.domain.user.UserRepository;
import com.block.project.springboot.web.dto.user.UserResponseDto;
import com.block.project.springboot.web.dto.user.UserSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public String save(UserSaveRequestDto requestDto){
        return userRepository.save(requestDto.toEntity()).getName();
    }

    @Transactional
    public UserResponseDto findById(Long id){
        User entity = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지않는 회원입니다. id=" + id));
        return new UserResponseDto(entity);
    }

    public User findByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("존재하지않는 회원입니다. email=" + email));
        return  user;
    }
}
