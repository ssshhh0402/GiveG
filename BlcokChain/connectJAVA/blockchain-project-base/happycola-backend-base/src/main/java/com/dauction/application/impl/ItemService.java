package com.dauction.application.impl;

import com.dauction.application.IAuctionContractService;
import com.dauction.application.IAuctionService;
import com.dauction.application.IFabricCCService;
import com.dauction.application.IItemService;
import com.dauction.domain.Auction;
import com.dauction.domain.Bid;
import com.dauction.domain.Item;
import com.dauction.domain.Wallet;
import com.dauction.domain.exception.ApplicationException;
import com.dauction.domain.exception.NotFoundException;
import com.dauction.domain.repository.IBidRepository;
import com.dauction.domain.repository.IItemRepository;
import com.dauction.domain.repository.IWalletRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ItemService implements IItemService
{
	public static final Logger logger = LoggerFactory.getLogger(ItemService.class);

	private IItemRepository itemRepository;
	private IAuctionService auctionService;
	private IAuctionContractService auctionContractService;
	private IFabricCCService fabricCCService;
	private IBidRepository bidRepository;
	private IWalletRepository walletRepository;

	@Autowired
	public ItemService(IFabricCCService fabricCCService,
					   IAuctionService auctionService,
					   IAuctionContractService auctionContractService,
					   IBidRepository bidRepository,
					   IItemRepository itemRepository,
					   IWalletRepository walletRepository) {
		this.fabricCCService = fabricCCService;
		this.auctionService = auctionService;
		this.auctionContractService = auctionContractService;
		this.bidRepository = bidRepository;
		this.itemRepository = itemRepository;
		this.walletRepository = walletRepository;
	}

	/**
	 * 2주차 과제2
	 * 상품 등록 시 상품 정보를 저장하고
	 * 패브릭에 상품이 등록되었음을 기록한다.
	 * @param item
	 * @return Item
	 */
	@Override
	public Item register(final Item item) {
		// TODO
		return null;
	}

	/**
	 * 4주차 과제1
	 * 상품 업데이트
	 * @param item
	 * @return
	 */
	@Override
	public Item update(final Item item) {
		// TODO
		return null;
	}

	/**
	 * 4주차 과제1
	 * 상품 삭제 시, 상품의 상태를 업데이트하고(state: X)
	 * 패브릭에 상품 삭제 이력을 추가한다.
	 * @param id 상품 id
	 * @return Item
	 */
	@Override
	public Item delete(final long id)
	{
		// TODO
		return null;
	}

	/**
	 * 4주차 과제1
	 * 구매자 구매 확정 시, 상품의 상태를 업데이트하고(state: C)
	 * 패브릭에 구매 확정 이력을 추가한다.
	 * @param id 상품 id
	 * @param uid 회원 id
	 * @return Item
	 */
	@Override
	public Item confirm(long id, long uid)
	{
		// TODO
		return null;
	}

	@Override
	public Item get(final long id)
	{
		return this.itemRepository.get(id);
	}

	/**
	 * 4주차 과제1
	 * @return List<Item>
	 */
	@Override
	public List<Item> list()
	{
		return this.itemRepository.list();
	}

	/**
	 * 4주차 과제3
	 * [마이페이지] 특정회원이 판매/구매하거나 입찰한 상품의 목록
	 */
	@Override
	public List<Item> getBySeller(final long id)
	{
		// TODO
		return null;
	}

	@Override
	public List<Item> getByBuyer(long id) {
		// TODO
		return null;
	}

	@Override
	public List<Item> getByBidder(int bidderId) {
		// TODO
		return null;
	}
}
