package com.dauction.domain.repository;

import com.dauction.domain.Item;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IItemRepository
{
	List<Item> list();
	Item get(long id);
	Item get(final long userId, final String name);

	@Transactional
	long create(Item item);

	@Transactional
	int update(Item item);

	@Transactional
	int updateWithBuyer(Item item);

	@Transactional
	int delete(long id);
}
