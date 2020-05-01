package com.dauction.application.impl;

import com.dauction.application.IFabricCCService;
import com.dauction.domain.*;
import com.dauction.domain.exception.ApplicationException;
import com.google.protobuf.ByteString;
import org.hyperledger.fabric.sdk.*;
import org.hyperledger.fabric.sdk.exception.CryptoException;
import org.hyperledger.fabric.sdk.exception.ProposalException;
import org.hyperledger.fabric.sdk.exception.TransactionException;
import org.hyperledger.fabric.sdk.security.CryptoSuite;
import org.hyperledger.fabric_ca.sdk.HFCAClient;
import org.hyperledger.fabric_ca.sdk.exception.EnrollmentException;
import org.hyperledger.fabric_ca.sdk.exception.InvalidArgumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.ByteArrayInputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class FabricCCService implements IFabricCCService
{
	private static final Logger logger = LoggerFactory.getLogger(FabricCCService.class);

	/**
	 * 패브릭 네트워크를 이용하기 위한 정보
	 */
	@Value("${fabric.ca-server.url}")
	private String CA_SERVER_URL;
	@Value("${fabric.ca-server.admin.name}")
	private String CA_SERVER_ADMIN_NAME;
	@Value("${fabric.ca-server.pem.file}")
	private String CA_SERVER_PEM_FILE;
	@Value("${fabric.org.name}")
	private String ORG_NAME;
	@Value("${fabric.org.msp.name}")
	private String ORG_MSP_NAME;
	@Value("${fabric.org.admin.name}")
	private String ORG_ADMIN_NAME;
	@Value("${fabric.peer.name}")
	private String PEER_NAME;
	@Value("${fabric.peer.url}")
	private String PEER_URL;
	@Value("${fabric.peer.pem.file}")
	private String PEER_PEM_FILE;
	@Value("${fabric.orderer.name}")
	private String ORDERER_NAME;
	@Value("${fabric.orderer.url}")
	private String ORDERER_URL;
	@Value("${fabric.orderer.pem.file}")
	private String ORDERER_PEM_FILE;
	@Value("${fabric.org.user.name}")
	private String USER_NAME;
	@Value("${fabric.org.user.secret}")
	private String USER_SECRET;
	@Value("${fabric.channel.name}")
	private String CHANNEL_NAME;

	private HFClient hfClient;
	private Channel channel;
	/**
	 * 2주차 과제2: 채널 접근
	 * 체인코드를 이용하기 위하여
	 * 구축해놓은 패브릭 네트워크의 채널을 가져오는
	 * 기능을 구현한다.
	 */
	private void loadChannel(){
		// TODO

//		String caUrl = CA_SERVER_URL;
//		CAClient caClient = new CAClient(caUrl,null);
//		UserContext adminUserContext = new UserContext();
//		adminUserContext.setName(USER_NAME);
//		adminUserContext.setAffiliation(ORG_NAME);
//		adminUserContext.setMspId(ORG_MSP_NAME);
//		caClient.setAdminUserContext(adminUserContext);
//		adminUserContext = caClient.enrollAdminUser(USER_NAME, USER_SECRET);
//
//		FabricClient fabClient = new FabricClient(adminUserContext);
	}

	/**
	 * 2주차 과제2: 상품 등록 이력 추가
	 * registerItem 함수를 호출하는 메소드
	 * @param iid 상품 id
	 * @param uid 사용자 id
	 * @return boolean
	 */
	@Override
	public boolean registerItem(long iid, long uid) {
		// TODO
		return false;
	}

	/**
	 * 2주차 과제2: 상품 조회(패브릭 기록 쿼리)
	 * 체인코드 query 함수를 호출하는 메소드
	 * @param iid 상품 id
	 * @return
	 */
	@Override
	public FabricRecord query(long iid) {
		//TODO
		return null;
	}

	/**
	 * 4주차 과제1: 상품 상태 변경에 따른 이력 추가
	 */

	/**
	 * deregisterItem 함수를 호출하는 메소드
	 * @param iid 상품 id
	 * @param uid 사용자 id
	 * @return boolean
	 */
	@Override
	public boolean deregisterItem(long iid, long uid) {
		// TODO
		return false;
	}

	/**
	 * startAuction 함수를 호출하는 메소드
	 * @param iid 상품 id
	 * @param uid 사용자 id
	 * @return boolean
	 */
	@Override
	public boolean startAuction(long iid, long uid) {
		// TODO
		return false;
	}

	/**
	 * endAuction 함수를 호출하는 메소드
	 * @param iid 상품 id
	 * @param uid 사용자 id
	 * @return boolean
	 */
	@Override
	public boolean endAuction(long iid, long uid) {
		// TODO
		return false;
	}

	/**
	 * cancelAuction 함수를 호출하는 메소드
	 * @param iid 상품 id
	 * @param uid 사용자 id
	 * @return boolean
	 */
	@Override
	public boolean cancelAuction(long iid, long uid) {
		// TODO
		return false;
	}

	/**
	 * startDelivery 함수를 호출하는 메소드
	 * @param iid 상품 id
	 * @param shippingAdmin 배송관리자 id (임의의)
	 * @return boolean
	 */
	@Override
	public boolean startDelivery(long iid, long shippingAdmin) {
		// TODO
		return false;
	}

	/**
	 * confirmItem 함수를 호출하는 메소드
	 * @param iid 상품 id
	 * @param uid 사용자 id
	 * @return boolean
	 */
	@Override
	public boolean confirmItem(long iid, long uid) {
		// TODO
		return false;
	}

	/**
	 * 체인코드 getItemHistory 함수를 호출하는 메소드
	 * @param iid
	 * @return
	 */
	@Override
	public List<FabricRecord> getItemHistory(long iid) {
		return null;
	}
}
