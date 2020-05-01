package com.block.project.springboot.web.dto.kakao;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class KakaoUserInfo {
    String id;
    String nickname;
    String profileImage;
    String email;
}