package com.dauction.application.impl;

import com.dauction.application.IEthereumService;
import com.dauction.domain.*;
import com.dauction.domain.exception.ApplicationException;
import com.dauction.domain.repository.ITransactionRepository;
import com.dauction.domain.wrapper.Block;
import com.dauction.domain.wrapper.EthereumTransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.DefaultBlockParameterNumber;
import org.web3j.protocol.core.methods.response.*;
import org.web3j.protocol.exceptions.TransactionException;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class EthereumService implements IEthereumService {

	private static final Logger log = LoggerFactory.getLogger(EthereumService.class);

	public static final BigInteger GAS_PRICE = BigInteger.valueOf(1L);
	public static final BigInteger GAS_LIMIT = BigInteger.valueOf(21_000L);

	// TODO src/resources/application.properties 수정
	// 사용할 이더리움 지갑의 주소
	@Value("${eth.admin.address}")
	private String ADMIN_ADDRESS;
	// 지갑의 암호화된 패스워드, domain 패키지의 CryptoUtil 클래스 사용 가능
	@Value("${eth.encrypted.password}")
	private String PASSWORD;
	// 사용할 이더리움 지갑의 키스토어
	@Value("${eth.admin.wallet.filename}")
	private String ADMIN_WALLET_FILE;

	private ITransactionRepository transactionRepository;

	@Autowired
	private Web3j web3j;

	@Autowired
	public EthereumService(ITransactionRepository transactionRepository) {
		this.transactionRepository = transactionRepository;
	}

	/**
	 * 2주차 과제1
	 * 이더리움으로부터 해당 주소의 잔액을 조회한다.
	 * @param address
	 * @return BigInteger
	 */
	@Override
	public BigInteger getBalance(String address){
		// TODO
		return BigInteger.ZERO;
	}

	/**
	 * 4주차 과제4
	 * 최근 블록 조회
	 * 예) 최근 20개의 블록 조회
	 * @return List<Block>
	 */
	@Override
	public List<Block> getLatestBlock()
	{
		// TODO
		return null;
	}

	/**
	 * 4주차 과제4
	 * 최근 생성된 블록에 포함된 트랜잭션 조회
	 * 이더리움 트랜잭션을 EthereumTransaction으로 변환해야 한다.
	 * @return List<EthereumTransaction>
	 */
	@Override
	public List<EthereumTransaction> getLatestTransactions()
	{
		// TODO
		return null;
	}

	/**
	 * 4주차 과제4
	 * 특정 블록 검색
	 * 조회한 블록을 Block으로 변환해야 한다.
	 * @param blockNo
	 * @return Block
	 */
	@Override
	public Block getBlock(String blockNo)
	{
		// TODO
		return null;
	}

	/**
	 * 4주차 과제4
	 * 특정 hash 값을 갖는 트랜잭션 검색
	 * 조회한 트랜잭션을 EthereumTransaction으로 변환해야 한다.
	 * @param txHash
	 * @return EthereumTransaction
	 */
	@Override
	public EthereumTransaction getTransaction(String txHash)
	{
		// TODO
		return null;
	}


	/**
	 * 4주차 과제4 [추가과제]
	 * 이더리움으로부터 해당 주소의 잔액을 조회하고
	 * 동기화한 트랜잭션 테이블로부터 Address 정보의 trans 필드를 완성하여
	 * 정보를 반환한다.
	 * @param addr
	 * @return Address
	 */
	@Override
	public Address getAddress(String addr)
	{
		// TODO
		return null;
	}

	/**
	 * 2주차 과제1
	 * [주소]로 시스템에서 정한 양 만큼 이더를 송금한다.
	 * 이더를 송금하는 트랜잭션을 생성, 전송한 후 결과인
	 * String형의 트랜잭션 hash 값을 반환한다.
	 * @param address
	 * @return String 생성된 트랜잭션의 hash 반환 (참고, TransactionReceipt)
	 */
	@Override
	public String requestEth(final String address) // 특정 주소로 테스트 특정 양(5Eth) 만큼 충전해준다.
	{
		// TODO
		return null;
	}

}
