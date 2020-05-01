package com.dauction.domain;

import lombok.Data;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Data
public class Bid
{
	private long id;
	private long participant;
	private long auctionId;
	private LocalDateTime createdAt;
	private BigInteger amount;
	private String isWinner = "N";

	@Override
	public String toString()
	{
		return "Bid{" + '\n' +
				" participant=" + participant +
				",\n auctionId=" + auctionId +
				",\n createdAt=" + createdAt +
				",\n amount=" + amount  +
				",\n isWinner=" + isWinner +
				"'\n}";
	}
}
