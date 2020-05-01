package com.dauction.infrastructure.repository;

import com.dauction.domain.Bid;
import com.dauction.domain.exception.RepositoryException;
import com.dauction.domain.repository.IBidRepository;
import com.dauction.infrastructure.repository.factory.BidFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class BidRepository implements IBidRepository
{
	private JdbcTemplate jdbcTemplate;
	private SimpleJdbcInsert simpleJdbcInsert;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public List<Bid> list() {
		StringBuilder sbSql =  new StringBuilder("SELECT * FROM bids");
		try {
			return this.jdbcTemplate.query(sbSql.toString(),
							   new Object[]{}, (rs, rowNum) -> BidFactory.create(rs));
		} catch (Exception e) {
			throw new RepositoryException(e, e.getMessage());
		}
	}

	@Override
	public Bid get(final long id) {
		StringBuilder sbSql =  new StringBuilder("SELECT * FROM bids WHERE id=?");
		try {
			return this.jdbcTemplate.queryForObject(sbSql.toString(),
								new Object[] { id }, (rs, rowNum) -> BidFactory.create(rs) );
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			throw new RepositoryException(e, e.getMessage());
		}
	}

	@Override
	public Bid get(final Bid bid)
	{
		StringBuilder sbSql =  new StringBuilder("SELECT * FROM bids WHERE participant=? AND auction_id=? AND created_at=? AND amount=?");
		try {
			return this.jdbcTemplate.queryForObject(sbSql.toString(),
								new Object[] {
										bid.getParticipant(),
										bid.getAuctionId(),
										bid.getCreatedAt(),
										bid.getAmount() },
								(rs, rowNum) -> BidFactory.create(rs) );
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			throw new RepositoryException(e, e.getMessage());
		}
	}

	@Override
	public Bid get(final long auctionId, final long winnerId, final BigInteger highestValue) {
		StringBuilder sbSql =  new StringBuilder("SELECT * FROM bids WHERE participant=? AND auction_id=? AND amount=?");
		try {
			return this.jdbcTemplate.queryForObject(sbSql.toString(),
								new Object[] {winnerId, auctionId, highestValue }, (rs, rowNum) -> BidFactory.create(rs) );
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			throw new RepositoryException(e, e.getMessage());
		}
	}

	@Override
	public Bid getWinnerBid(long auctionId) {
		StringBuilder sbSql =  new StringBuilder("SELECT * FROM bids WHERE auction_id=? AND is_winner=?");
		try {
			return this.jdbcTemplate.queryForObject(sbSql.toString(),
					new Object[] {auctionId, "Y"}, (rs, rowNum) -> BidFactory.create(rs) );
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			throw new RepositoryException(e, e.getMessage());
		}
	}

	@Override
	public List<Bid> getByParticipant(int userId) {
		StringBuilder sbSql =  new StringBuilder("SELECT * FROM bids WHERE participant=?");
		try {
			return this.jdbcTemplate.query(sbSql.toString(),
					new Object[]{userId}, (rs, rowNum) -> BidFactory.create(rs));
		} catch (Exception e) {
			throw new RepositoryException(e, e.getMessage());
		}
	}

	@Override
	public long create(final Bid bid) {
		try {
			Map<String, Object> paramMap = new HashMap<>();
			paramMap.put("participant", bid.getParticipant());
			paramMap.put("auction_id", bid.getAuctionId());
			paramMap.put("created_at", bid.getCreatedAt());
			paramMap.put("amount", bid.getAmount());
			paramMap.put("is_winner", bid.getIsWinner());

			this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
					.withTableName("bids")
					.usingGeneratedKeyColumns("id");

			Number newId = simpleJdbcInsert.executeAndReturnKey(paramMap);
			return newId.longValue();

		} catch (Exception e) {
			throw new RepositoryException(e, e.getMessage());
		}
	}

	@Override
	public int update(final Bid bid){
		StringBuilder sbSql =  new StringBuilder("UPDATE bids ");
		sbSql.append("SET is_winner=? ");
		sbSql.append("WHERE participant=? AND auction_id=? AND amount=?");
		try {
			return this.jdbcTemplate.update(sbSql.toString(),
							new Object[] {
									bid.getIsWinner(),
									bid.getParticipant(),
									bid.getAuctionId(),
									bid.getAmount()
							});
		} catch (Exception e) {
			throw new RepositoryException(e, e.getMessage());
		}
	}

	@Override
	public int update(final long auctionId, final long winnerId, final BigInteger highestValue) {
		StringBuilder sbSql =  new StringBuilder("UPDATE bids ");
		sbSql.append("SET is_winner=? ");
		sbSql.append("WHERE auction_id=? AND participant=? AND amount=?");
		try {
			return this.jdbcTemplate.update(sbSql.toString(),
								new Object[] { "Y", auctionId, winnerId, highestValue });
		} catch (Exception e) {
			throw new RepositoryException(e, e.getMessage());
		}
	}

	@Override
	public int delete(final long id) {
		StringBuilder sbSql =  new StringBuilder("DELETE FROM bids WHERE id=?");
		try {
			return this.jdbcTemplate.update(sbSql.toString(), new Object[] { id });
		} catch (Exception e) {
			throw new RepositoryException(e, e.getMessage());
		}
	}
}