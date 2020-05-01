package com.dauction.application;

import com.dauction.domain.Auction;
import com.dauction.domain.AuctionInfo;

import java.math.BigInteger;
import java.util.List;

public interface IAuctionContractService
{
	AuctionInfo fetchAuctionInfo(String contractAddress);
	BigInteger getHighestValue(String contractAddress);
	String getHighestBidderAddress(String contractAddress);
	List<String> fetchAll(); // 경매컨트랙트주소리스트
}
