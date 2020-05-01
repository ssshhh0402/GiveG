package com.dauction.api;

import com.dauction.application.IAuctionContractService;
import com.dauction.application.IAuctionService;
import com.dauction.domain.*;
import com.dauction.domain.exception.ApplicationException;
import com.dauction.domain.exception.EmptyListException;
import com.dauction.domain.exception.NotFoundException;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 4주차
 * 과제1 & 과제2
 * 이더리움 스마트 컨트랙트를 통한 경매 진행 사항을 백엔드에 동기화하고
 * 상품 상태가 변함에 따라 이력을 패브릭에 기록합니다.
 */
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class AuctionController
{
	public static final Logger logger = LoggerFactory.getLogger(AuctionController.class);

	private IAuctionService auctionService;
	private IAuctionContractService auctionContractService;

	@Autowired
	public AuctionController(IAuctionService auctionService,
	                         IAuctionContractService auctionContractService) {
		Assert.notNull(auctionService, "auctionService 개체가 반드시 필요!");
		Assert.notNull(auctionContractService, "auctionContractService 개체가 반드시 필요!");

		this.auctionService = auctionService;
		this.auctionContractService = auctionContractService;
	}

	@ApiOperation(value = "Register a new auction")
	@RequestMapping(value = "/auctions", method = RequestMethod.POST)
	public Auction create(@RequestBody Auction auction) {
		logger.debug("auction info: " + auction.toString());
		Auction newAuction = auctionService.create(auction);
		if( newAuction == null )
			throw new ApplicationException("경매 정보를 입력할 수 없습니다!");

		return newAuction;
	}

	@ApiOperation(value = "Fetch all auctions")
	@RequestMapping(value = "/auctions", method = RequestMethod.GET)
	public List<Auction> list() {
		List<Auction> auctionList = auctionService.list();

		if (auctionList == null || auctionList.isEmpty() )
			throw new EmptyListException("NO DATA");

		return auctionList;
	}

	@ApiOperation(value = "Fetch auction with id")
	@RequestMapping(value = "/auctions/{id}", method = RequestMethod.GET)
	public AuctionInfo get(@PathVariable long id) {
		Auction auction = this.auctionService.get(id);
		if (auction == null){
			logger.error("NOT FOUND AUCTION: ", id);
			throw new NotFoundException(id + " 해당 경매를 찾을 수 없습니다.");
		}

		AuctionInfo auctionInfo = this.auctionContractService.fetchAuctionInfo(auction.getContractAddress());
		auctionInfo.setAuctionId(id);
		if(auctionInfo == null){
			throw new NotFoundException(id + " 해당 경매 컨트랙트를 찾을 수 없습니다.");
		}

		auctionInfo.setStartTime(auction.getStartTime());
		auctionInfo.setEndTime(auction.getEndTime());

		return auctionInfo;
	}

	@ApiOperation(value = "Fetch auction with item id")
	@RequestMapping(value = "/auctions/item/{id}", method = RequestMethod.GET)
	public AuctionInfo getByItemId(@PathVariable long id) {
		Auction auction = this.auctionService.getByItemId(id);
		if (auction == null){
			logger.error("NOT FOUND AUCTION: ", id);
			throw new NotFoundException(id + " 해당 경매를 찾을 수 없습니다.");
		}
		AuctionInfo auctionInfo = this.auctionContractService.fetchAuctionInfo(auction.getContractAddress());
		auctionInfo.setAuctionId(auction.getId());
		if(auctionInfo == null){
			throw new NotFoundException(id + " 해당 경매 컨트랙트를 찾을 수 없습니다.");
		}

		auctionInfo.setStartTime(auction.getStartTime());
		auctionInfo.setEndTime(auction.getEndTime());

		return auctionInfo;
	}

	@ApiOperation(value = "Cancel auction with id by accessible user")
	@RequestMapping(value = "/auctions/{aid}/by/{uid}", method = RequestMethod.DELETE)
	public Auction cancel(@PathVariable long aid, @PathVariable long uid) {
		return auctionService.cancel(aid, uid);
	}

	@ApiOperation(value = "End auction with id by accessible user")
	@RequestMapping(value = "/auctions/{aid}/by/{uid}", method = RequestMethod.PUT)
	public Auction end(@PathVariable long aid, @PathVariable long uid) { //mid = 최고가 입찰자 id
		return this.auctionService.end(aid, uid);
	}

	@ApiOperation(value = "Bid to auction")
	@RequestMapping(value = "/auctions/bid", method = RequestMethod.POST)
	public Bid bid(@RequestBody Bid bid) {
		return auctionService.bid(bid);
	}

	@ApiOperation(value = "Purchase an item immediately")
	@RequestMapping(value = "/auctions/purchase", method = RequestMethod.PUT)
	public Bid purchaseImmediately(@RequestBody Bid bid){
		return auctionService.purchase(bid);
	}

	@ApiOperation(value = "Fetch ￿auction owned by specific user")
	@RequestMapping(value = "/auctions/owner/{id}", method = RequestMethod.GET)
	public List<Auction> getByUser(@PathVariable int id){
		List<Auction> auctionList = auctionService.listByUser(id);

		if (auctionList == null || auctionList.isEmpty() )
			throw new EmptyListException("사용자가 생성한 경매가 없습니다.");

		return auctionList;
	}
}
