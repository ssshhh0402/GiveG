package com.dauction.application;

import com.dauction.Application;
import com.dauction.domain.Auction;
import com.dauction.domain.Bid;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class AuctionServiceTest
{
	private static final Logger log = LoggerFactory.getLogger(AuctionServiceTest.class);
	@Autowired
	private IAuctionService auctionService;

	@Test
	public void testAuctionCreation() {
		Auction auction = new Auction();
		auction.setCreatorId(1);
		auction.setItemId(1);
		auction.setEndTime(LocalDateTime.of(2019, 12,12,23,59,59));
		auction.setMinPrice(BigInteger.valueOf(1000_000_000_000_000_000L));
		auction.setPurchasePrice(BigInteger.valueOf(3000_000_000_000_000_000L));
		auction.setContractAddress("0xCD6BF8C7F0F6bBf1d1E6F51C785BA6723cA20e0F");

		Auction newAuction = this.auctionService.create(auction);
		assert newAuction.getContractAddress().equals("0xCD6BF8C7F0F6bBf1d1E6F51C785BA6723cA20e0F");
	}

	@Test
	public void testBid() {
		Bid bid = new Bid();
		bid.setParticipant(1);
		bid.setAuctionId(2);
		bid.setCreatedAt(LocalDateTime.of(2019, 11, 26, 16, 38, 30));
		bid.setAmount(BigInteger.valueOf(1500_000_000_000_000_000L));

		Bid bidResult = this.auctionService.bid(bid);

		log.debug(bidResult.toString());
		assert bidResult != null;
	}

	@Test
	public void testList() {
		List<Auction> list = this.auctionService.list();

		assert list.size() > 0;
	}

	@Test
	public void testGet(){
		int aid = 2;
		Auction auction = this.auctionService.get(aid);

		log.debug(auction.getContractAddress());
		assert auction != null;
		assert auction.getContractAddress() != null;
		assert auction.getId() == aid;
	}

	@Test
	public void testGetByItemId() {
		Auction auction = this.auctionService.get(1);
		log.debug(auction.toString());
	}

}
