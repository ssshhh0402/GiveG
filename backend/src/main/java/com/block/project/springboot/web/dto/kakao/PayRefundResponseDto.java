package com.block.project.springboot.web.dto.kakao;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PayRefundResponseDto {
    private String aid;                                     // 요청 고유 번호
    private String tid;                                     // 결제 고유 번호
    private String cid;                                     // 가맹점 코드
    private String state;                                   // 결제상태
    private String partner_order_id;                        // 가맹점 주문번호
    private String partner_user_id;                         // 가맹점 회원 번호
    private String payment_method_type;                     // 결제 수단
    private RefundAmount amount;                            // 결제 금액 정보
    private CanceledAmount canceled_amount;                 // 이번 요청으로 취소된 금액
    private CancelAvailableAmount cancel_available_amount;  // 남은 취소 가능 금액
    private String item_name;                               // 상품 이름
    private String item_code;                               // 상품 코드
    private int quantity;                                   // 상품 수량
    private LocalDateTime approved_at;                      // 결제 승인 시각
    private LocalDateTime canceled_at;                      // 결제 취소 시각
    private String payload;                                 // 취소 요청 시 전달한 값
}
