package com.block.project.springboot.web.dto.kakao;

import lombok.Data;

@Data
public class CancelAvailableAmount {
    int total;      // 전체 취소 가능 금액
    int tax_free;   // 취소 가능한 비과세 금액
    int vat;        // 취소 가능한 부가세 금액
    int discount;   // 할인 금액
}
