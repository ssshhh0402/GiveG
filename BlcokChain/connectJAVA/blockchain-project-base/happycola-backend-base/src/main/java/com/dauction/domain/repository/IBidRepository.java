package com.dauction.domain.repository;

import com.dauction.domain.Bid;
import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIDeclaration;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.List;

public interface IBidRepository
{
	List<Bid> list();
	Bid get(long id);
	Bid get(Bid bid);
	Bid get(long auctionId, long winnerId, BigInteger highestValue);
	Bid getWinnerBid(long auctionId);
	List<Bid> getByParticipant(int id);

	@Transactional
	long create(Bid bid);

	@Transactional
	int update(Bid bid);

	@Transactional
	int update(long auctionId, long winnerId, BigInteger highestValue);

	@Transactional
	int delete(long id);
}
