/****************************************************** 
 *  Copyright 2018 IBM Corporation 
 *  Licensed under the Apache License, Version 2.0 (the "License"); 
 *  you may not use this file except in compliance with the License. 
 *  You may obtain a copy of the License at 
 *  http://www.apache.org/licenses/LICENSE-2.0 
 *  Unless required by applicable law or agreed to in writing, software 
 *  distributed under the License is distributed on an "AS IS" BASIS, 
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 *  See the License for the specific language governing permissions and 
 *  limitations under the License.
 */
package com.dauction.domain;

import org.apache.commons.codec.binary.Hex;
import org.hyperledger.fabric.sdk.*;
import org.hyperledger.fabric.sdk.security.CryptoSuite;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;

/**
 * 
 * @author Balaji Kadambi
 *
 */


public class RegisterEnrollUser {


	private static String CA_SERVER_URL = "http://l02bch4.p.ssafy.io:8054";
	private static String CA_SERVER_ADMIN_NAME = "admin";
	private static String CA_SERVER_PEM_FILE = "fabric-ca.pem";
	private static String ORG_NAME = "HFTeam2";
	private static String ORG_MSP_NAME = "HFTeam2MSP";
	private static String ORG_ADMIN_NAME = "Admin@HFTeam2";
	private static String PEER_NAME = "peer0.HFTeam2";
	private static String PEER_URL = "grpc://l02bch4.p.ssafy.io:8051";
	private static String PEER_PEM_FILE = "fabric-peer.pem";
	private static String ORDERER_NAME = "orderer0.ordererorg";
	private static String ORDERER_URL = "grpc://l02bch4.p.ssafy.io:8050";
	private static String ORDERER_PEM_FILE = "fabric-orderer.pem";
	private static String USER_NAME = "Admin@HFTeam2";
	private static String USER_SECRET = "HFTeam2pwd";
	private static String CHANNEL_NAME = "team2channel";

	private static HFClient hfClient;
	private static Channel channel;

	public static void main(String args[]) {

		try {
			Util.cleanUp();
			String caUrl = CA_SERVER_URL;
			CAClient caClient = new CAClient(caUrl, null);
			// Enroll Admin to Org1MSP
			UserContext adminUserContext = new UserContext();
			adminUserContext.setName(USER_NAME);
			adminUserContext.setAffiliation(ORG_NAME);
			adminUserContext.setMspId(ORG_MSP_NAME);
			caClient.setAdminUserContext(adminUserContext);
			adminUserContext = caClient.enrollAdminUser(USER_NAME, USER_SECRET);

			// Register and Enroll user to Org1MSP
			UserContext userContext = new UserContext();
			String name = "user"+System.currentTimeMillis();
			userContext.setName(name);
			userContext.setAffiliation(ORG_NAME);
			userContext.setMspId(ORG_MSP_NAME);

			String eSecret = caClient.registerUser(name, ORG_NAME);

			userContext = caClient.enrollUser(userContext, eSecret);

			System.out.println(userContext.getAccount());
			System.out.println(userContext.getAffiliation());
			System.out.println(userContext.getEnrollment());
			System.out.println(userContext.getMspId());
			System.out.println(userContext.getName());

			FabricClient fabClient = new FabricClient(adminUserContext);

			ChannelClient channelClient = fabClient.createChannelClient(CHANNEL_NAME);
			Channel channel = channelClient.getChannel();
			Peer peer = fabClient.getInstance().newPeer(PEER_NAME, PEER_URL);
			EventHub eventHub = fabClient.getInstance().newEventHub("eventhub01", "grpc://localhost:7053");
			Orderer orderer = fabClient.getInstance().newOrderer(ORDERER_NAME, ORDERER_URL);
			channel.addPeer(peer);
			channel.addEventHub(eventHub);
			channel.addOrderer(orderer);
			channel.initialize();

			BlockchainInfo channelInfo = channel.queryBlockchainInfo();

			long height = channelInfo.getHeight();
			long MAX_LOG_COUNT = 10;

			System.out.println("Channel height: " + height);

			for (long current = height - 1; current > -1; --current) {
				BlockInfo returnedBlock = channel.queryBlockByNumber(current);
				final long blockNumber = returnedBlock.getBlockNumber();

				System.out.println(String.format("Block #%d - Previous hash id: %s", blockNumber, Hex.encodeHexString(returnedBlock.getPreviousHash())));
				System.out.println(String.format("Block #%d - Data hash: %s", blockNumber, Hex.encodeHexString(returnedBlock.getDataHash())));

				if (height - current > MAX_LOG_COUNT) break;
			}


		} catch (Exception e) {
			e.printStackTrace();
		}




//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}

}