package com.block.project.springboot.web.dto.kakao;

import lombok.Data;

@Data
public class CanceledAmount {
    int total;      // 전체 취소 금액
    int tax_free;   // 취소된 비과세 금액
    int vat;        // 취소된 부가세 금액
    int discount;   // 할인 금액
}
