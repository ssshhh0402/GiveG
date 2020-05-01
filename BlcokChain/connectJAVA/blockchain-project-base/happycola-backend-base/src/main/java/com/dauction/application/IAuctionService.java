package com.dauction.application;

import com.dauction.domain.Auction;
import com.dauction.domain.Bid;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.List;

public interface IAuctionService
{
	List<Auction> list();
	Auction get(long auctionId);
	Auction get(String contractAddress);
	Auction getByItemId(long itemId);
	Bid getWinnerId(long auctionId);

	@Transactional
	Auction create(Auction auction);
	@Transactional
	Bid bid(Bid bid);
	@Transactional
	Bid purchase(Bid bid);
	@Transactional
	Auction end(long auctionId, long userId); // 현재 최고가에서 끝내기, 소유권 이전
	@Transactional
	Auction cancel(long auctionId, long userId); // 환불 후 옥션 끝내기

    List<Auction> listByUser(long userId);

	List<Auction> listByBidder(int id);
}
