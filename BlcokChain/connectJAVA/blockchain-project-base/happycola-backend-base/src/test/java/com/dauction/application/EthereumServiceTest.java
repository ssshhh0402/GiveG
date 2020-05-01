package com.dauction.application;

import com.dauction.Application;
import com.dauction.domain.Address;
import com.dauction.domain.wrapper.Block;
import com.dauction.domain.wrapper.EthereumTransaction;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class EthereumServiceTest
{
	@Autowired
	private IEthereumService ethereumService;

	@Test
	public void testGetLatestBlock()
	{
		List<Block> blocks = this.ethereumService.getLatestBlock();

		assert blocks != null;
		assert blocks.size() == 20;
	}

	@Test
	public void testGetLatestTransactions()
	{
		List<EthereumTransaction> txs = this.ethereumService.getLatestTransactions();

		assert txs != null;
	}

	@Test
	public void testGetBlock()
	{
		Block b = this.ethereumService.getBlock("100");

		assert b != null;
		assert b.getBlockNo().intValue() == 100;
	}

	@Test
	public void testGetTransaction()
	{
		EthereumTransaction tx = this.ethereumService.getTransaction("0xca00d3af3e89495eba77b7a2e44a5d80bab716695570ab63eff17c12e40ac7f2");

		assert tx != null;
		assert !tx.getBlockId().isEmpty();
		assert Integer.valueOf(tx.getBlockId()) > 0;
		System.out.println(tx.getFrom() + " --> " + tx.getTo() + " : " + tx.getAmount());
	}

	@Test
	public void testRequestEth()
	{
		String toAddress = "0x6190E280834C9a3414EC2b93B268b629206ab65C";
		String txhash = this.ethereumService.requestEth(toAddress);

		System.out.println(txhash);
		assert txhash != null || !txhash.equals("");
	}

	@Test
	public void testGetAddress()
	{
		String addressToBeFetched = "0x66eDaFE1d6073Fb3bB8DD6eCFEE95319FEb2787D";
		Address addr = this.ethereumService.getAddress(addressToBeFetched);

		assert addr != null;
	}
}