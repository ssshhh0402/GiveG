package com.dauction.domain;

import lombok.Data;
import java.math.BigInteger;
import java.time.LocalDateTime;

@Data
public class Auction
{
	private long id;
	private long creatorId; 			// 회원id
	private long itemId;    			// 상품id
	private LocalDateTime createdAt;	// 경매 등록 시각
	private String state = "A"; 		// A (경매중), B (낙찰), C(경매 취소), P(즉시구매), E(기간만료, 종료)
	private LocalDateTime startTime; 	// 경매 시작 시각
	private LocalDateTime endTime;		// 경매 종료 시각
	private BigInteger minPrice;   		// 최저가
	private BigInteger purchasePrice;	// 즉시구매가
	private String contractAddress; 	// 컨트랙트 주소

	@Override
	public String toString()
	{
		return "Auction{" +
				" creatorId=" + creatorId +
				", itemId=" + itemId +
				", createdAt=" + createdAt +
				", state=" + state + '\'' +
				", startTime=" + startTime +
				", endTime=" + endTime +
				", minPrice=" + minPrice +
				", purchasePrice=" + purchasePrice +
				", contractAddress='" + contractAddress + '\'' +
				'}';
	}
}
