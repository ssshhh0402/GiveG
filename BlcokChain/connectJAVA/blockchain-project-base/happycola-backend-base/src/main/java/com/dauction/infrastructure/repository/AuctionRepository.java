package com.dauction.infrastructure.repository;

import com.dauction.domain.Auction;
import com.dauction.domain.exception.RepositoryException;
import com.dauction.domain.repository.IAuctionRepository;
import com.dauction.infrastructure.repository.factory.AuctionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class AuctionRepository implements IAuctionRepository
{
	private JdbcTemplate jdbcTemplate;
	private SimpleJdbcInsert simpleJdbcInsert;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public List<Auction> getAll()
	{
		StringBuilder sbSql =  new StringBuilder("SELECT * FROM auctions");
		try {
			return this.jdbcTemplate.query(sbSql.toString(),
			                               new Object[]{ }, (rs, rowNum) -> AuctionFactory.create(rs));
		} catch (Exception e) {
			throw new RepositoryException(e, e.getMessage());
		}
	}

	@Override
	public Auction get(final long id)
	{
		StringBuilder sbSql =  new StringBuilder("SELECT * FROM auctions WHERE id=?");
		try {
			return this.jdbcTemplate.queryForObject(sbSql.toString(),
			                                        new Object[] { id }, (rs, rowNum) -> AuctionFactory.create(rs) );
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			throw new RepositoryException(e, e.getMessage());
		}
	}

	@Override
	public Auction get(final String contractAddress)
	{
		StringBuilder sbSql =  new StringBuilder("SELECT * FROM auctions WHERE contract_address=?");
		try {
			return this.jdbcTemplate.queryForObject(sbSql.toString(),
			                                        new Object[] { contractAddress }, (rs, rowNum) -> AuctionFactory.create(rs) );
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			throw new RepositoryException(e, e.getMessage());
		}
	}

	@Override
	public List<Auction> getByUser(long userId) {
		StringBuilder sbSql =  new StringBuilder("SELECT * FROM auctions WHERE creator_id=?");
		try {
			return this.jdbcTemplate.query(sbSql.toString(),
					new Object[]{ userId }, (rs, rowNum) -> AuctionFactory.create(rs));
		} catch (Exception e) {
			throw new RepositoryException(e, e.getMessage());
		}
	}

	@Override
	public List<Auction> getByParticipant(int userId) {
		StringBuilder sbSql =  new StringBuilder("SELECT * FROM auctions WHERE participant=?");
		try {
			return this.jdbcTemplate.query(sbSql.toString(),
					new Object[]{ userId }, (rs, rowNum) -> AuctionFactory.create(rs));
		} catch (Exception e) {
			throw new RepositoryException(e, e.getMessage());
		}
	}

	@Override
	public Auction getByItem(long itemId) {
		StringBuilder sbSql =  new StringBuilder("SELECT * FROM auctions WHERE item_id=?");
		try {
			return this.jdbcTemplate.queryForObject(sbSql.toString(),
					new Object[] { itemId }, (rs, rowNum) -> AuctionFactory.create(rs) );
		} catch (Exception e) {
			throw new RepositoryException(e, e.getMessage());
		}
	}

	@Override
	public long create(final Auction auction) {
		try {
			Map<String, Object> paramMap = new HashMap<>();
			paramMap.put("creator_id", auction.getCreatorId());
			paramMap.put("item_id", auction.getItemId());
			paramMap.put("created_at", auction.getCreatedAt());
			paramMap.put("state", auction.getState());
			paramMap.put("start_time", auction.getStartTime());
			paramMap.put("end_time", auction.getEndTime());
			paramMap.put("min_price", auction.getMinPrice());
			paramMap.put("purchase_price", auction.getPurchasePrice());
			paramMap.put("contract_address", auction.getContractAddress());

			this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
					.withTableName("auctions")
					.usingGeneratedKeyColumns("id");

			Number newId = simpleJdbcInsert.executeAndReturnKey(paramMap);
			return newId.longValue();
		} catch (Exception e) {
			throw new RepositoryException(e, e.getMessage());
		}
	}

	@Override
	public int update(final Auction auction)
	{
		StringBuilder sbSql =  new StringBuilder("UPDATE auctions ");
		sbSql.append("SET state=?, end_time=? ");
		sbSql.append("where id=? AND creator_id=? AND item_id=?");
		try {
			return this.jdbcTemplate.update(sbSql.toString(),
			                                new Object[] {
					                           auction.getState(),
					                           auction.getEndTime(),
					                           auction.getId(),
					                           auction.getCreatorId(),
					                           auction.getItemId()
			                                });
		} catch (Exception e) {
			throw new RepositoryException(e, e.getMessage());
		}
	}

	@Override
	public int delete(final long id)
	{
		StringBuilder sbSql =  new StringBuilder("DELETE FROM auctions WHERE id=?");

		try {
			return this.jdbcTemplate.update(sbSql.toString(),
			                                new Object[] { id });
		} catch (Exception e) {
			throw new RepositoryException(e, e.getMessage());
		}
	}
}
