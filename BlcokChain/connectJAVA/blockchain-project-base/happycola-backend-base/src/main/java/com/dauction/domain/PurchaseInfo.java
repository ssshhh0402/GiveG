package com.dauction.domain;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PurchaseInfo {
    private long participant;
    private long auctionId;
    private LocalDateTime when;
    private BigDecimal amount;
}
