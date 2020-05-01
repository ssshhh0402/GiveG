package com.dauction.infrastructure.repository.factory;

import com.dauction.domain.Auction;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AuctionFactory
{
	public static Auction create(ResultSet rs) throws SQLException
	{
		if(rs == null) return null;
		Auction auction = new Auction();

		auction.setId(rs.getInt("id"));
		auction.setCreatorId(rs.getLong("creator_id"));
		auction.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
		auction.setItemId(rs.getLong("item_id"));
		auction.setState(rs.getString("state"));
		auction.setStartTime(rs.getTimestamp("start_time").toLocalDateTime());
		auction.setEndTime(rs.getTimestamp("end_time").toLocalDateTime());
		auction.setMinPrice(rs.getBigDecimal("min_price").toBigInteger());
		auction.setPurchasePrice(rs.getBigDecimal("purchase_price").toBigInteger());
		auction.setContractAddress(rs.getString("contract_address"));

		return auction;
	}
}