package com.dauction.infrastructure.repository.factory;

import com.dauction.domain.Bid;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BidFactory
{
	public static Bid create(ResultSet rs) throws SQLException
	{
		if (rs == null) return null;
		Bid bid = new Bid();
		bid.setId(rs.getLong("id"));
		bid.setAuctionId(rs.getLong("auction_id"));
		bid.setParticipant(rs.getLong("participant"));
		bid.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
		bid.setAmount(rs.getBigDecimal("amount").toBigInteger());
		bid.setIsWinner(rs.getString("is_winner"));

		return bid;
	}
}
