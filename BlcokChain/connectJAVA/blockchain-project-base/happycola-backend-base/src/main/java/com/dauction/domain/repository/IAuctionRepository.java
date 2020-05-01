package com.dauction.domain.repository;

import com.dauction.domain.Auction;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IAuctionRepository
{
	List<Auction> getAll();
	Auction get(long id);
	Auction get(String contractAddress);

	List<Auction> getByUser(long userId);
	List<Auction> getByParticipant(int userId);

	Auction getByItem(long itemId);

	@Transactional
	long create(Auction auction);

	@Transactional
	int update(Auction auction);

	@Transactional
	int delete(long id);


}
