package com.dauction.application;

import com.dauction.domain.Item;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IItemService
{
	@Transactional
    Item register(Item item);

	@Transactional
    Item delete(long id);

	@Transactional
	Item confirm(long id, long uid);

	Item get(long id);

	List<Item> list();

	@Transactional
	Item update(Item item);

	List<Item> getBySeller(long id);
	List<Item> getByBuyer(long id);
	List<Item> getByBidder(int id);
}
