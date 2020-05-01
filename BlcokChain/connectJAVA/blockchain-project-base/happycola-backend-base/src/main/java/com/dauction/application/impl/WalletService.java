package com.dauction.application.impl;

import com.dauction.application.IEthereumService;
import com.dauction.application.IWalletService;
import com.dauction.domain.Address;
import com.dauction.domain.Wallet;
import com.dauction.domain.exception.ApplicationException;
import com.dauction.domain.exception.NotFoundException;
import com.dauction.domain.repository.IWalletRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * TODO
 * 2주차 과제1: 지갑 관련 기능 구현
 */
@Service
public class WalletService implements IWalletService
{
	private static final Logger log = LoggerFactory.getLogger(WalletService.class);

	private IWalletRepository walletRepository;
	private IEthereumService ethereumService;

	@Autowired
	public WalletService(IWalletRepository walletRepository,
						 IEthereumService ethereumService) {
		this.walletRepository = walletRepository;
		this.ethereumService = ethereumService;
	}

	@Override
	public List<Wallet> list()
	{
		return this.walletRepository.list();
	}

	/**
	 * DB에 저장된 지갑주소의 정보와 이더리움으로부터 조회된 잔액 정보를 동기화한다.
	 * @param walletAddress
	 * @return Wallet
	 */
	@Override
	public Wallet getAndSyncBalance(final String walletAddress)
	{
		// TODO
		return null;
	}

	/**
	 * DB로부터 사용자 지갑을 조회하여 지갑 정보를 반환한다.
	 * 지갑 잔액 정보의 동기화가 필요하면 업데이트하여 정보를 반환할 수 있어야한다.
	 * @param userId
	 * @return Wallet
	 */
	@Override
	public Wallet get(final long userId)
	{
		// TODO
		return null;
	}

	/**
	 * DB에 사용자 지갑 정보를 저장한다.
	 * @param wallet
	 * @return Wallet
	 */
	@Override
	public Wallet register(final Wallet wallet)
	{
		// TODO
		return null;
	}

	/**
	 * [지갑주소]로 이더를 송금하는 충전 기능을 구현한다.
	 * 무한정 충전을 요청할 수 없도록 조건을 두어도 좋다.
	 * @param walletAddress
	 * @return Wallet
	 */
	@Override
	public Wallet requestEth(String walletAddress) {
		// TODO
		return null;
	}
}
