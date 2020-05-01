package com.dauction.domain.wrapper;

import org.web3j.protocol.core.methods.response.EthBlock;

import lombok.Data;
import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.TimeZone;

@Data
public class Block {
    private BigInteger blockNo;
    private List<EthereumTransaction> trans;
    private LocalDateTime timestamp;
    private String difficulty;
    private BigInteger size;
    private BigInteger gasUsed;
    private BigInteger gasLimit;
    private String hash;
    private String parentHash;
    private String miner;

    public static Block fromOriginalBlock(final EthBlock.Block currentBlock) {
        Block block = new Block();

        block.blockNo = currentBlock.getNumber();
        block.timestamp = LocalDateTime.ofInstant(Instant.ofEpochSecond(currentBlock.getTimestamp().longValue()), TimeZone.getDefault().toZoneId());
        block.difficulty = String.valueOf(currentBlock.getDifficulty());
        block.size = currentBlock.getSize();
        block.gasLimit = currentBlock.getGasLimit();
        block.gasUsed = currentBlock.getGasUsed();
        block.trans = EthereumTransaction.getEthereumTransactionList(currentBlock.getTransactions(), currentBlock.getTimestamp(), true);
        block.hash = currentBlock.getHash();
        block.parentHash = currentBlock.getParentHash();
        block.miner = currentBlock.getMiner();

        return block;
    }
}
