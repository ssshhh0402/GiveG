package com.block.project.springboot.web.dto.kakao;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class PayReadyResponseDto {
    String tid;                     // 결제 고유 번호
    String next_redirect_pc_url	;
    LocalDateTime create_at;         // 결제 준비 요청 시간

}
