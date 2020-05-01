package com.dauction.application;

import com.dauction.Application;
import com.dauction.domain.Wallet;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class WalletServiceTest
{
	@Autowired
	private IWalletService walletService;
	private static String testAddress = "fakeaddress00";
	private static String tempAddress = "fakeaddress01";

	@Transactional
	@Test
	public void testRegister(){
		Wallet w = new Wallet();
		w.setAddress(tempAddress);
		w.setOwnerId(15);
		w.setBalance(BigDecimal.valueOf(1000));

		Wallet walletRegistered = this.walletService.register(w);

		assert walletRegistered != null;
		assert walletRegistered.getOwnerId() == w.getOwnerId();
		assert walletRegistered.getAddress().equals(tempAddress);
	}

	@Test
	public void testGetAndSyncBalance(){
		Wallet w = this.walletService.getAndSyncBalance(testAddress);

		assert w != null;
		assert w.getAddress().equals(testAddress);
	}

	@Test
	public void testList(){
		List<Wallet> walletList = this.walletService.list();

		assert walletList.size() > 0;
	}
}
