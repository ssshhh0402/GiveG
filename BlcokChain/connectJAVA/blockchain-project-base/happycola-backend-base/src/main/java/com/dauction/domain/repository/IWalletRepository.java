package com.dauction.domain.repository;

import com.dauction.domain.Wallet;

import java.math.BigDecimal;
import java.util.List;

public interface IWalletRepository
{
	List<Wallet> list();
	Wallet get(long id);
	Wallet get(String wAddress);
	
	long create(Wallet wallet);
	int updateBalance(String wAddress, BigDecimal balance);
	int updateRequestNo(String wAddress);
}
