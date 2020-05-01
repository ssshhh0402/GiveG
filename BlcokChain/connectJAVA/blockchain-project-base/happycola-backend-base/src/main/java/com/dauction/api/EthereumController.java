package com.dauction.api;

import com.dauction.application.IAuctionContractService;
import com.dauction.application.IAuctionService;
import com.dauction.application.IEthereumService;
import com.dauction.domain.Address;
import com.dauction.domain.Auction;
import com.dauction.domain.AuctionInfo;
import com.dauction.domain.repository.IAuctionRepository;
import com.dauction.domain.wrapper.Block;
import com.dauction.domain.wrapper.EthereumTransaction;
import com.dauction.domain.exception.EmptyListException;
import com.dauction.domain.exception.NotFoundException;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/eth")
public class EthereumController {

    public static final Logger log = LoggerFactory.getLogger(EthereumController.class);

    private IEthereumService explorerService;
    private IAuctionContractService auctionContractService;
    private IAuctionService auctionService;

    @Autowired
    public EthereumController(IEthereumService explorerService,
                              IAuctionContractService auctionContractService,
                              IAuctionService auctionService) {
        Assert.notNull(explorerService, "explorerService 개체가 반드시 필요!");
        Assert.notNull(auctionContractService, "auctionContractService 개체가 반드시 필요!");

        this.explorerService = explorerService;
        this.auctionContractService = auctionContractService;
        this.auctionService = auctionService;
    }

    @ApiOperation(value = "Fetch latest blocks")
    @GetMapping("/blocks")
    public List<Block> getLatestBlock()
    {
        List<Block> blockList = this.explorerService.getLatestBlock();

        if (blockList == null || blockList.isEmpty() )
            throw new EmptyListException("NO DATA");

        return blockList;
    }

    @ApiOperation(value = "Fetch latest transactions")
    @GetMapping("/trans")
    public List<EthereumTransaction> getLatestTransactions()
    {
        List<EthereumTransaction> txList = this.explorerService.getLatestTransactions();

        if (txList == null || txList.isEmpty() )
            throw new EmptyListException("NO DATA");

        return txList;
    }

    @ApiOperation(value = "Fetch a block with block number")
    @GetMapping("/blocks/{id}")
    public Block getBlock(@PathVariable String id)
    {
        Block block = this.explorerService.getBlock(id);

        if (block == null)
            throw new NotFoundException(id + " 블록 정보를 찾을 수 없습니다.");

        return block;
    }

    @ApiOperation(value = "Fetch a transaction with tx id")
    @GetMapping("/trans/{id}")
    public EthereumTransaction getTransaction(@PathVariable String id)
    {
        EthereumTransaction tx = this.explorerService.getTransaction(id);

        if (tx == null)
            throw new NotFoundException(id + " 트랜잭션 정보를 찾을 수 없습니다.");

        return tx;
    }

    @ApiOperation(value = "Fetch an address info")
    @GetMapping("/address/{addr}")
    public Address getAddress(@PathVariable String addr)
    {
        Address address = this.explorerService.getAddress(addr);

        if(address == null)
            throw new NotFoundException(addr + " 주소 정보를 찾을 수 없습니다.");

        return address;
    }

    @ApiOperation(value = "Fetch all auctions from Smart Contract")
    @GetMapping("/auctions")
    public List<AuctionInfo> getAuctions(){
        List<String> auctionList = this.auctionContractService.fetchAll();

        if(auctionList == null || auctionList.isEmpty())
            throw new EmptyListException("NO DATA");

        List<AuctionInfo> auctionInfoList = new ArrayList<>();
        auctionList.forEach(a -> {
            AuctionInfo aInfo = this.auctionContractService.fetchAuctionInfo(a);
            Auction auction = this.auctionService.get(a);
            aInfo.setAuctionId(auction.getId());
            auctionInfoList.add(aInfo);
        });

        return auctionInfoList;
    }

    @ApiOperation(value = "Fetch an auction with specific contract address")
    @GetMapping("/auctions/{addr}")
    public AuctionInfo getAuctionInfo(@PathVariable String addr){
        AuctionInfo auctionInfo = this.auctionContractService.fetchAuctionInfo(addr);
        Auction auction = this.auctionService.get(addr);
        auctionInfo.setAuctionId(auction.getId());

        if(auctionInfo == null)
            throw new NotFoundException(addr + " 경매 정보를 찾을 수 없습니다.");

        return auctionInfo;
    }
}
