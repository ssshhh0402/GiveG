package com.dauction.application.impl;

import com.dauction.application.IAuctionContractService;
import com.dauction.application.IAuctionService;
import com.dauction.application.IFabricCCService;
import com.dauction.domain.*;
import com.dauction.domain.exception.ApplicationException;
import com.dauction.domain.exception.DomainException;
import com.dauction.domain.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AuctionService implements IAuctionService
{
	public static final Logger logger = LoggerFactory.getLogger(AuctionService.class);

	private IAuctionContractService auctionContractService;
	private IAuctionRepository auctionRepository;
	private IBidRepository bidRepository;
	private IItemRepository itemRepository;
	private IWalletRepository walletRepository;
	private IFabricCCService fabricCCService;

	@Autowired
	public AuctionService(IAuctionContractService auctionContractService,
						  IAuctionRepository auctionRepository,
						  IBidRepository bidRepository,
						  IItemRepository itemRepository,
						  IWalletRepository walletRepository,
						  IFabricCCService fabricCCService) {
		this.auctionContractService = auctionContractService;
		this.auctionRepository = auctionRepository;
		this.bidRepository = bidRepository;
		this.itemRepository = itemRepository;
		this.walletRepository = walletRepository;
		this.fabricCCService = fabricCCService;
	}

	@Override
	public List<Auction> list() {
		return this.auctionRepository.getAll();
	}

	@Override
	public Auction get(final long auctionId) {
		return this.auctionRepository.get(auctionId);
	}

	@Override
	public Auction get(final String contractAddress) {
		return this.auctionRepository.get(contractAddress);
	}

	@Override
	public Auction getByItemId(long itemId) {
		return this.auctionRepository.getByItem(itemId);
	}

	@Override
	public Bid getWinnerId(long auctionId) {
		return this.bidRepository.getWinnerBid(auctionId);
	}

	/**
	 * 4주차 과제2 [판매 시작]
	 * 프론트엔드에서 스마트 컨트랙트의 start 함수 직접 호출 후
	 * 백엔드에 경매 상태 동기화를 위해 호출되는 메소드
	 * @param auction
	 * @return Auction
	 * 1. 상품의 상태를 업데이트하고(state: A, 경매시작)
	 * 2. 패브릭 네트워크에 이력을 등록한다. TODO FABRIC
	 * 3. 추가된 Auction 정보를 반환한다.
	 */
	@Override
	public Auction create(final Auction auction) {
		// TODO
		return null;
	}

	@Override
	public Bid bid(Bid bid) {
		logger.debug("bid info: " + bid.toString());
		long id = this.bidRepository.create(bid);
		return this.bidRepository.get(id);
	}

	/**
	 * 4주차 과제2
	 * 프론트엔드에서 스마트 컨트랙트의 purchase 함수 직접 호출 후
	 * 백엔드에 경매 상태 동기화를 위해 호출되는 메소드
	 * @param bid
	 * @return Bid
	 * 1. 구매 정보가 입찰로 추가된다. (is_winner: Y)
	 * 2. 해당 경매의 상태가 업데이트 된다. (state: P, purchased)
	 * 3. 해당 상품 상태를 업데이트 한다. (state: D, delivery started)
	 * 4. 패브릭 상에도 소유권 이전 정보가 추가되어야 한다. TODO FABRIC
	 * *** 임의로 두 가지 상태가 추가된다.
	 * ****** 1. 경매 종료 (by admin)
	 * ****** 2. 배송 시작 (by shipping corp. admin)
	 * 5. 구매 정보(Bid)를 반환한다.
	 * */
	@Override
	public Bid purchase(Bid bid) {
		// TODO
		return null;
	}

	/**
	 * 4주차 과제2
	 * 프론트엔드에서 스마트 컨트랙트의 경매종료(endAuction) 함수 직접 호출 후 (상품 등록자에 의해)
	 * 백엔드에 경매 상태 동기화를 위해 호출되는 메소드
	 * @param auctionId
	 * @param userId
	 * @return Auction
	 * 1. 해당 경매의 상태가 업데이트 된다. (state: B,  bid won)
	 * 2. 입찰 정보 중 최고 입찰 정보를 '낙찰'로 업데이트해야 한다.
	 * 3. 해당 상품 상태를 업데이트 한다. (state: D, delivery started)
	 * 4. 패브릭 상에도 소유권 이전 정보가 추가되어야 한다. TODO FABRIC
	 * *** 임의로 두 가지 상태가 추가된다.
	 * ****** 1. 경매 종료 (by item seller)
	 * ****** 2. 배송 시작 (by shipping corp. admin)
	 * 5. 업데이트 된 경매 정보를 반환한다.
	 * */
	@Override
	public Auction end(final long auctionId, final long userId)
	{
		// TODO
		return null;
	}

	/**
	 * 프론트엔드에서 스마트 컨트랙트의 경매취소(cancelAuction) 함수 직접 호출 후
	 * 백엔드에 경매 상태 동기화를 위해 호출되는 메소드
	 * @param auctionId
	 * @param userId
	 * @return Auction
	 * 1. 해당 경매의 상태와(C,canceled) 종료일시를 업데이트 한다.
	 * 2. 상품의 상태를 판매취소(X)로 업데이트한다.
	 * 3. 패브릭에 상품의 경매가 취소되었음을 기록한다. // TODO FABRIC
	 * 4. 업데이트 된 경매 정보를 반환한다.
	 * */
	@Override
	public Auction cancel(final long auctionId, final long userId)
	{
		// TODO
		return null;
	}

	@Override
	public List<Auction> listByUser(final long userId)
	{
		return this.auctionRepository.getByUser(userId);
	}

	@Override
	public List<Auction> listByBidder(int userId) {
		return this.auctionRepository.getByParticipant(userId);
	}
}
