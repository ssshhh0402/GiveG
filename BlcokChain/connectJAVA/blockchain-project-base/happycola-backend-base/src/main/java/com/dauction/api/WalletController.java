package com.dauction.api;

import com.dauction.application.IWalletService;
import com.dauction.domain.Wallet;
import com.dauction.domain.exception.EmptyListException;
import com.dauction.domain.exception.NotFoundException;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class WalletController {
	public static final Logger logger = LoggerFactory.getLogger(WalletController.class);

	private IWalletService walletService;

	@Autowired
	public WalletController(IWalletService walletService) {
		Assert.notNull(walletService, "walletService 개체가 반드시 필요!");
		this.walletService = walletService;
	}


	@ApiOperation(value = "Fetch all wallets")
	@RequestMapping(value = "/wallets", method = RequestMethod.GET)
	public List<Wallet> list() {
		List<Wallet> list = walletService.list();

		if (list == null || list.isEmpty() )
			throw new EmptyListException("NO DATA");

		return list;
	}

	/**
	 * 2주차 과제1: 지갑 등록
	 * @param wallet
	 * @return Wallet
	 */
	@ApiOperation(value = "Register wallet of user")
	@RequestMapping(value = "/wallets", method = RequestMethod.POST)
	public Wallet register(@Valid @RequestBody Wallet wallet) {
		logger.debug(wallet.getAddress());
		logger.debug(String.valueOf(wallet.getOwnerId()));

		this.walletService.register(wallet);
		Wallet newWallet = walletService.getAndSyncBalance(wallet.getAddress());

		if(newWallet == null)
			throw new NotFoundException(wallet.getAddress() + " 해당 주소 지갑을 찾을 수 없습니다.");

		return newWallet;
	}

	/**
	 * 2주차 과제1: 지갑 조회(주소로 조회)
	 * @return Wallet
	 */
	@ApiOperation(value = "Fetch wallet by address")
	@RequestMapping(value = "/wallets/{address}", method = RequestMethod.GET)
	public Wallet get(@PathVariable String address) {
		return walletService.getAndSyncBalance(address);
	}

	/**
	 * 2주차 과제1: 지갑 조회(사용자로 조회)
	 * @return Wallet
	 */
	@ApiOperation(value = "Fetch wallet of user")
	@RequestMapping(value = "/wallets/of/{uid}", method = RequestMethod.GET)
	public Wallet getByUser(@PathVariable long uid) {
		Wallet wallet = this.walletService.get(uid);
		if(wallet == null)
			throw new EmptyListException("[UserId] " + uid + " 해당 지갑을 찾을 수 없습니다.");

		return walletService.getAndSyncBalance(wallet.getAddress());
	}

	/**
	 * 2주차 과제1: 이더 충전
	 * @param address
	 * @return Wallet
	 */
	@ApiOperation(value = "Request ether")
	@RequestMapping(value ="/wallets/{address}", method = RequestMethod.PUT)
	public Wallet requestEth(@PathVariable String address){ // 테스트 가능하도록 일정 개수의 코인을 충전해준다.

		return this.walletService.requestEth(address);
	}
}
