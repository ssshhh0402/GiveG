package com.dauction.application;

import com.dauction.Application;
import com.dauction.domain.AuctionInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class AuctionContractServiceTest
{
	private static final Logger log = LoggerFactory.getLogger(AuctionContractServiceTest.class);

	@Autowired
	private IAuctionContractService auctionContractService;


	@Test
	public void testFetchAll(){
		List<String> auctions = this.auctionContractService.fetchAll();

		log.info(auctions.size() + "");
		auctions.forEach( a-> log.debug(a + ""));
	}

	@Test
	public void testFetchAuctionInfo(){
		String auctionContractAddress = "0xDcE793F5f8A74F0a6cb70060C9DDDc4Db248E65E";

		AuctionInfo info = this.auctionContractService.fetchAuctionInfo(auctionContractAddress);

		assert info != null;
		assert info.getStartTime() != null;
		assert info.getEndTime() != null;
		assert info.getContractAddress() != null;
		assert info.getItemId() != 0;
		log.info(info.getMinPrice().longValue() + "");
		assert info.getMinPrice().longValue() > 0;
	}

}