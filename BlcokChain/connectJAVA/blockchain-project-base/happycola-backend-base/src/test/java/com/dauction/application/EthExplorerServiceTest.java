package com.dauction.application;

import com.dauction.domain.wrapper.Block;
import com.dauction.domain.wrapper.EthereumTransaction;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EthExplorerServiceTest {

    @Autowired
    private IEthereumService ethExplorerService;

    @Test
    public void testGetBlock()
    {
        String blockNo = "503";
        Block block = this.ethExplorerService.getBlock(blockNo);

        assert String.valueOf(block.getBlockNo()).equals(blockNo);
    }

    @Test
    public void testGetLatestBlock()
    {
        List<Block> blockList = this.ethExplorerService.getLatestBlock();

        assert blockList.size() == 20;
    }

    @Test
    public void testGetLatestTxs()
    {
        List<EthereumTransaction> txList = this.ethExplorerService.getLatestTransactions();
        String blockNo = (txList.size() > 0) ? txList.get(0).getBlockId() : "0";

        if(txList.size() > 0)
            assert txList.get(0).getBlockId() != null;
    }

    @Test
    public void testGetTransaction()
    {
        EthereumTransaction tx = this.ethExplorerService.getTransaction("0xca00d3af3e89495eba77b7a2e44a5d80bab716695570ab63eff17c12e40ac7f2");

        assert tx != null;
        assert Integer.valueOf(tx.getBlockId()) > 0;
    }

}
