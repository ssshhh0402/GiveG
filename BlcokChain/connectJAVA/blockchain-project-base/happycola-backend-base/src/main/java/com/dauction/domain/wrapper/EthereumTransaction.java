package com.dauction.domain.wrapper;

import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.Transaction;

import lombok.Data;
import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

@Data
public class EthereumTransaction {

    private String txHash;
    private String Status;
    private String blockId;
    private LocalDateTime timestamp;
    private String from;
    private String to;
    private BigInteger amount;
    private boolean accepted;

    public static EthereumTransaction getEthereumTransaction(final EthBlock.TransactionResult tx,
                                                              final BigInteger timestamp,
                                                              final boolean accepted){
        EthereumTransaction ethTx = new EthereumTransaction();
        if(tx instanceof EthBlock.TransactionHash){
            ethTx.txHash = ((EthBlock.TransactionHash) tx).get();
        } else if(tx instanceof EthBlock.TransactionObject){
            ethTx = EthereumTransaction.convertTransaction(((EthBlock.TransactionObject) tx).get());
        }

        ethTx.accepted = accepted;
        ethTx.timestamp = (timestamp != null) ? LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp.longValue()), TimeZone.getDefault().toZoneId()) : null;

        return ethTx;
    }

    public static List<EthereumTransaction> getEthereumTransactionList(final List<EthBlock.TransactionResult> txs, final BigInteger timestamp, final boolean accepted){
        List<EthereumTransaction> txList = new ArrayList<>();

        for(EthBlock.TransactionResult t: txs) {
            EthereumTransaction etx = EthereumTransaction.getEthereumTransaction(t, timestamp, accepted);
            etx.setStatus("0x01");
            txList.add(etx);
        }

        return txList;
    }

    public static EthereumTransaction convertTransaction(Transaction transaction){
        EthereumTransaction tx = new EthereumTransaction();

        tx.txHash = transaction.getHash();
        tx.blockId = String.valueOf(transaction.getBlockNumber());
        tx.from = transaction.getFrom();
        tx.to = transaction.getTo();
        tx.amount = transaction.getValue();

        return tx;
    }

    public static EthereumTransaction convertTransaction(final com.dauction.domain.Transaction transaction)
    {
        EthereumTransaction tx = new EthereumTransaction();
        tx.txHash = transaction.getHash();
        tx.blockId = transaction.getBlockNumber();
        tx.from = transaction.getFrom();
        tx.to = transaction.getTo();
        if(transaction.getValue() != null)
            tx.amount = new BigInteger(transaction.getValue());

        return tx;
    }
}
