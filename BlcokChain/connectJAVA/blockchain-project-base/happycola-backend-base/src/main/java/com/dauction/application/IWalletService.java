package com.dauction.application;

import com.dauction.domain.Wallet;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * 2주차 과제1: 지갑 관련 기능
 * 아래 인터페이스를 이용하거나 재정의하여 작성합니다.
 */
public interface IWalletService
{
	List<Wallet> list();
	Wallet getAndSyncBalance(String walletAddress);
	Wallet get(long userId);

	@Transactional
	Wallet register(Wallet wallet);

	@Transactional
	Wallet requestEth(String walletAddress);
}
