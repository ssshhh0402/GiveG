package com.dauction.domain;

import lombok.Data;
import java.math.BigInteger;
import java.time.LocalDateTime;

@Data
public class AuctionInfo
{
	private long auctionId;
	private String contractAddress;
	private BigInteger highestBid;
	private long itemId;
	private long highestBidder;
	private LocalDateTime startTime;
	private LocalDateTime endTime;
	private BigInteger minPrice;
	private BigInteger purchasePrice;
	private String state;
	private boolean isEnded;

	@Override
	public String toString()
	{
		return "AuctionInfo {" +
				" auctionId=" + auctionId +
				" contractAddress=" + contractAddress +
				", highestBid=" + highestBid +
				", itemId=" + itemId +
				", highestBidder=" + highestBidder + '\'' +
				", startTime=" + startTime +
				", endTime=" + endTime +
				", minPrice=" + minPrice +
				", purchasePrice=" + purchasePrice +
				", state='" + state + '\'' +
				", isEnded='" + isEnded + '\'' +
				'}';
	}
}
