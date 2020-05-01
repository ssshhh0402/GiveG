package com.block.project.springboot.web.dto.kakao;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Data
public class RefundAmount {
    private Integer total;      // 전체 결제 금액
    private Integer tax_free;   // 비과세 금액
    private Integer vat;        // 부가세 금액
    private Integer discount;   // 할인금액
}
