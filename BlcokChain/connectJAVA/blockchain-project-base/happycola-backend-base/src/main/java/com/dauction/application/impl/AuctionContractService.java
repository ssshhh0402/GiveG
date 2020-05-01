package com.dauction.application.impl;

import com.dauction.application.IAuctionContractService;
import com.dauction.domain.*;
import com.dauction.domain.exception.ApplicationException;
import com.dauction.domain.exception.DomainException;
import com.dauction.domain.repository.IAuctionRepository;
import com.dauction.domain.repository.IWalletRepository;
import com.dauction.domain.wrapper.AuctionContract;
import com.dauction.domain.wrapper.AuctionFactoryContract;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.tuples.generated.Tuple8;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.DefaultGasProvider;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 4주차 과제2 & 과제4
 * AuctionContractService
 * 작성, 배포한 AuctionFactory.sol Auction.sol 스마트 컨트랙트를 이용한다.
 */
@Service
public class AuctionContractService implements IAuctionContractService {
	private static final Logger log = LoggerFactory.getLogger(AuctionContractService.class);

	@Value("${eth.auction.factory.contract}")
	private String AUCTION_FACTORY_CONTRACT;

	@Value("${eth.admin.address}")
	private String ADMIN_ADDRESS;

	@Value("${eth.admin.wallet.filename}")
	private String WALLET_RESOURCE;

	@Value("${eth.encrypted.password}")
	private String PASSWORD;

	private AuctionFactoryContract auctionFactoryContract;
	private ContractGasProvider contractGasProvider = new DefaultGasProvider();
	private Credentials credentials;

	@Autowired
	private Web3j web3j;

	private IWalletRepository walletRepository;
	private IAuctionRepository auctionRepository;

	@Autowired
	public AuctionContractService(IWalletRepository walletRepository, IAuctionRepository auctionRepository) {
		this.walletRepository = walletRepository;
		this.auctionRepository = auctionRepository;
	}

	private void getCredentials(){
		if(this.credentials != null) return;
		CryptoUtil cryptoUtil = CryptoUtil.of(ADMIN_ADDRESS);
		this.credentials = CommonUtil.getCredential(WALLET_RESOURCE, cryptoUtil.decryptBase64(PASSWORD));
	}

	/**
	 * 이더리움 컨트랙트 주소를 이용하여 경매 정보를 조회한다.
	 * @param contractAddress
	 * @return AuctionInfo
	 * 1. web3j API를 이용하여 해당 컨트랙트주소의 스마트 컨트랙트를 로드(load)한다.
	 * 2. info의 highestBidder의 정보를 가지고 최고입찰자 회원의 id를 찾아
	 *    AuctionInfo의 인스턴스를 생성하여 반환한다.
	 * */
	@Override
	public AuctionInfo fetchAuctionInfo(final String contractAddress) {
		return null;
	}

	/**
	 * 이더리움 컨트랙트 주소를 이용하여 해당 경매의 현재 최고 입찰가를 조회한다.
	 * @param contractAddress
	 * @return BigInteger 현재최고가
	 * */
	@Override
	public BigInteger getHighestValue(final String contractAddress)
	{
		return null;
	}

	/**
	 * 이더리움 컨트랙트 주소를 이용하여 해당 경매의 현재 최고 입찰 주소를 조회한다.
	 * @param contractAddress
	 * @return String 최고 입찰한 이더리움 주소(EOA)
	 * */
	@Override
	public String getHighestBidderAddress(final String contractAddress)
	{
		return null;
	}

	/**
	 * 이더리움 컨트랙트 주소를 이용하여 생성된 모든 경매 컨트랙트의 주소 목록을 조회한다.
	 * @return List<String> 경매 컨트랙트의 주소 리스트
	 * */
	@Override
	public List<String> fetchAll()
	{
		return null;
	}

}
