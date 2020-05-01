package com.dauction.domain;

import org.web3j.tuples.generated.Tuple8;

import java.math.BigInteger;

public class AuctionInfoFactory {

    public static AuctionInfo create(String contractAddress, long walletOwnerId,
                                     Tuple8<BigInteger, BigInteger, BigInteger, BigInteger, BigInteger, String, BigInteger, Boolean> info) {
        AuctionInfo auctionInfo = new AuctionInfo();
        auctionInfo.setContractAddress(contractAddress);
        auctionInfo.setStartTime(CommonUtil.convertEthTimestamp(info.getValue1().longValue()));
        auctionInfo.setEndTime(CommonUtil.convertEthTimestamp(info.getValue2().longValue()));
        auctionInfo.setMinPrice(info.getValue3());
        auctionInfo.setPurchasePrice(info.getValue4());
        auctionInfo.setItemId(info.getValue5().longValue());

        auctionInfo.setHighestBidder(walletOwnerId);
        auctionInfo.setHighestBid(info.getValue7());
        auctionInfo.setEnded(info.getValue8());

        return auctionInfo;
    }
}
