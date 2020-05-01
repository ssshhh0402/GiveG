package com.dauction.application;

import com.dauction.domain.Address;
import com.dauction.domain.wrapper.Block;
import com.dauction.domain.wrapper.EthereumTransaction;

import java.math.BigInteger;
import java.util.List;

public interface IEthereumService {
    BigInteger getBalance(String address);

    List<Block> getLatestBlock();
    List<EthereumTransaction> getLatestTransactions();

    Block getBlock(String blockNo);
    EthereumTransaction getTransaction(String txHash);

    String requestEth(String address);

    Address getAddress(String address);
}
