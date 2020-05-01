package com.dauction.api;


import com.dauction.application.IFabricCCService;
import com.dauction.application.IItemService;
import com.dauction.domain.FabricRecord;
import com.dauction.domain.Item;
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
 * 2주차 과제2 및 4주차 과제1
 * 상품 이력을 패브릭 네트워크에 저장하는 기능과 관련되는 인터페이스입니다.
 */
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class ItemController
{
	public static final Logger logger = LoggerFactory.getLogger(ItemController.class);

	private IItemService itemService;
	private IFabricCCService fabricCCService;

	@Autowired
	public ItemController(IItemService itemService,
						  IFabricCCService fabricCCService) {
		Assert.notNull(itemService, "itemService 개체가 반드시 필요!");
		Assert.notNull(fabricCCService, "fabricService 개체가 반드시 필요!");
		this.itemService = itemService;
		this.fabricCCService = fabricCCService;
	}

	/**
	 * 2주차 과제2
	 * @param item
	 * @return Item
	 */
	@ApiOperation(value = "Register a second-hand item")
	@RequestMapping(value = "/items", method = RequestMethod.POST)
	public Item register(@RequestBody Item item) {
		return itemService.register(item);
	}

	@ApiOperation(value = "Fetch an item with id")
	@RequestMapping(value = "/items/{id}", method = RequestMethod.GET)
	public Item get(@PathVariable int id) {
		Item item = itemService.get(id);
		if (item == null) {
			logger.error("NOT FOUND ID: ", id);
			throw new NotFoundException(id + " 상품 정보를 찾을 수 없습니다.");
		}

		return item;
	}

	@ApiOperation(value = "Fetch all items")
	@RequestMapping(value = "/items", method = RequestMethod.GET)
	public List<Item> list() {
		List<Item> list = itemService.list();

		if (list == null || list.isEmpty())
			throw new EmptyListException("NO DATA");

		return list;
	}

	@ApiOperation(value = "Update an item")
	@RequestMapping(value = "/items", method = RequestMethod.PUT)
	public Item update(@RequestBody Item item) {
		Item itemUpdated = itemService.update(item);
		if (itemUpdated == null) {
			logger.error("NOT FOUND ITEM ID: ", item.getId());
			throw new NotFoundException(item.getId() + " 상품 정보를 찾을 수 없습니다.");
		}

		return itemUpdated;
	}

	/**
	 * 4주차 과제1
	 * @param id 아이템id
	 * @return Item
	 */
	@ApiOperation(value = "Delete an item with id")
	@RequestMapping(value = "/items/{id}", method = RequestMethod.DELETE)
	public Item delete(@PathVariable int id) {
		return  itemService.delete(id);
	}

	/**
	 * 4주차 과제1
	 * @param id 아이템id
	 * @param uid 아이템 구매자 id
	 * @return Item
	 */
	@RequestMapping(value = "/items/{id}/by/{uid}", method = RequestMethod.PUT)
	public Item confirm(@PathVariable int id, @PathVariable int uid) {
		return itemService.confirm(id, uid);
	}

	/**
	 * TODO 4주차 과제3
	 * 아래의 API를 사용하거나 원하는 API를 설계, 구현하여 기능을 완성합니다.
	 */
	/**
	 * 협업과제
	 * 4주차 과제3
	 * [마이페이지] 나의 판매 상품
	 */
	@ApiOperation(value = "Fetch items by seller's id")
	@RequestMapping(value = "/items/seller/{id}", method = RequestMethod.GET)
	public List<Item> getSellerItems(@PathVariable int id){
		// TODO
		return null;
	}

	/**
	 * 협업과제
	 * 4주차 과제3
	 * [마이페이지] 나의 구매 상품
	 */
	@ApiOperation(value = "Fetch items by buyer's id")
	@RequestMapping(value = "/items/buyer/{id}", method = RequestMethod.GET)
	public List<Item> getBuyerItems(@PathVariable int id){
		// TODO
		return null;
	}

	/**
	 * 협업과제
	 * 4주차 과제3
	 * [마이페이지] 나의 입찰 상품
	 */
	@ApiOperation(value = "Fetch items bidded by specific user")
	@RequestMapping(value = "/items/bidder/{id}", method = RequestMethod.GET)
	public List<Item> getByBidder(@PathVariable int id){
		// TODO
		return null;
	}

	/**
	 * 협업과제
	 * 4주차 과제3
	 * 패브릭 네트워크로부터 이력 조회 및 반환
	 * @param id 상품의 id
	 * @return List<FabricRecord>
	 */
	@ApiOperation(value = "Fetch ownership history of an item")
	@RequestMapping(value = "/items/history/{id}", method = RequestMethod.GET)
	public List<FabricRecord> getHistory(@PathVariable int id){
		// TODO
		return null;
	}

}
