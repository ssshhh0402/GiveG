package com.block.project.springboot.web.dto.kakao;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

//@Getter
//@RequiredArgsConstructor
@Data
public class PayReadyRequestDto {
    String cid;             // 가맹점 코드(테스트용 제공)
    String partner_order_id;  // 가맹점 주문번호
    String partner_user_id;   // 가맹점 회원 id
    String item_name;        // 상품명
    int quantity;           // 상품 수량
    int total_amount;        // 상품 총액
    int tax_free_amount;      // 상품 비과세 금액
    String approval_url;     // 결제 성공시 redirect url
    String cancel_url;       // 결제 취소시 redirect url
    String fail_url;         // 결제 실패시 redirect url
}
