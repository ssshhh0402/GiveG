package com.dauction.infrastructure.repository;

import com.dauction.domain.Item;
import com.dauction.domain.exception.RepositoryException;
import com.dauction.domain.repository.IItemRepository;
import com.dauction.infrastructure.repository.factory.ItemFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class ItemRepository implements IItemRepository
{
	private static final Logger log = LoggerFactory.getLogger(ItemRepository.class);

	private JdbcTemplate jdbcTemplate;
	private SimpleJdbcInsert simpleJdbcInsert;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public List<Item> list() {
		StringBuilder sbSql =  new StringBuilder("SELECT * FROM items");
		try {
			return this.jdbcTemplate.query(sbSql.toString(),
							   new Object[]{}, (rs, rowNum) -> ItemFactory.create(rs));
		} catch (Exception e) {
			throw new RepositoryException(e, e.getMessage());
		}
	}

	@Override
	public Item get(final long id) {
		StringBuilder sbSql =  new StringBuilder("SELECT * FROM items WHERE id=?");
		try {
			return this.jdbcTemplate.queryForObject(sbSql.toString(),
								new Object[] { id }, (rs, rowNum) -> ItemFactory.create(rs) );
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			throw new RepositoryException(e, e.getMessage());
		}
	}

	@Override
	public Item get(final long userId, final String name) {
		StringBuilder sbSql =  new StringBuilder("SELECT * FROM items WHERE seller=? AND name=?");
		try {
			return this.jdbcTemplate.queryForObject(sbSql.toString(),
			                                        new Object[] {userId, name}, (rs, rowNum) -> ItemFactory.create(rs) );
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			throw new RepositoryException(e, e.getMessage());
		}
	}

	@Override
	public long create(final Item item) {
		try {
			log.debug(item.toString());
			Map<String, Object> paramMap = new HashMap<>();
			paramMap.put("name", item.getName());
			paramMap.put("category", item.getCategory());
			paramMap.put("explanation", item.getExplanation());
			paramMap.put("state", item.getState());
			paramMap.put("seller", item.getSeller());
			paramMap.put("registered_at", item.getRegisteredAt());

			this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
					.withTableName("items")
					.usingGeneratedKeyColumns("id");

			Number newId = simpleJdbcInsert.executeAndReturnKey(paramMap);
			log.debug("INSERTED: " + newId.longValue());
			return newId.longValue();

		} catch (Exception e) {
			throw new RepositoryException(e, e.getMessage());
		}
	}

	@Override
	public int update(final Item item) {
		StringBuilder sbSql =  new StringBuilder("UPDATE items ");
		sbSql.append("SET name=?, category=?, explanation=?, state=?");
		sbSql.append("where id=?");
		try {
			return this.jdbcTemplate.update(sbSql.toString(),
								new Object[] {
										item.getName(),
										item.getCategory(),
										item.getExplanation(),
										item.getState(),
										item.getId()
								});
		} catch (Exception e) {
			throw new RepositoryException(e, e.getMessage());
		}
	}

	@Override
	public int updateWithBuyer(Item item) {
		StringBuilder sbSql =  new StringBuilder("UPDATE items ");
		sbSql.append("SET name=?, category=?, explanation=?, state=?, buyer=? ");
		sbSql.append("where id=?");
		try {
			return this.jdbcTemplate.update(sbSql.toString(),
					new Object[] {
							item.getName(),
							item.getCategory(),
							item.getExplanation(),
							item.getState(),
							item.getBuyer(),
							item.getId()
					});
		} catch (Exception e) {
			throw new RepositoryException(e, e.getMessage());
		}
	}

	@Override
	public int delete(final long id) { // state를 N으로 업데이트
		StringBuilder sbSql =  new StringBuilder("UPDATE items ");
		sbSql.append("SET state=? ");
		sbSql.append("where id=?");

		try {
			return this.jdbcTemplate.update(sbSql.toString(),
								new Object[] { "N", id });
		} catch (Exception e) {
			throw new RepositoryException(e, e.getMessage());
		}

	}

}
