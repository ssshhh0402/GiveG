package com.dauction.infrastructure.repository.factory;

import com.dauction.domain.Item;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ItemFactory
{
	public static Item create(ResultSet rs) throws SQLException
	{
		if (rs == null) return null;
		Item item = new Item();
		item.setId(rs.getLong("id"));
		item.setName(rs.getString("name"));
		item.setCategory(rs.getString("category"));
		item.setExplanation(rs.getString("explanation"));
		item.setState(rs.getString("state"));
		item.setSeller(rs.getLong("seller"));
		item.setBuyer(rs.getLong("buyer"));
		item.setRegisteredAt(rs.getTimestamp("registered_at").toLocalDateTime());

		return item;
	}
}
