package com.block.project.springboot.service;

import com.block.project.springboot.domain.fabric.*;
import com.block.project.springboot.web.dto.fabric.FabricDonate;
import com.block.project.springboot.web.dto.fabric.FabricDonationPJT;
import com.block.project.springboot.web.dto.fabric.FabricRefund;
import com.block.project.springboot.web.dto.fabric.FabricUser;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hyperledger.fabric.sdk.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.UTF_8;

@Slf4j
@Service
@NoArgsConstructor
public class FabricCCService {
    private static final Logger logger = LoggerFactory.getLogger(FabricCCService.class);

    /**
     * 패브릭 네트워크를 이용하기 위한 정보
     */
//    @Value("${fabric.ca-server.url}")
    private String CA_SERVER_URL= "http://l02bch4.p.ssafy.io:8054";
    //    @Value("${fabric.ca-server.admin.name}")
    private String CA_SERVER_ADMIN_NAME = "admin";
    //    @Value("${fabric.ca-server.pem.file}")
    private String CA_SERVER_PEM_FILE =  "fabric-ca.pem";
    //    @Value("${fabric.org.name}")
    private String ORG_NAME = "HFTeam2";
    //    @Value("${fabric.org.msp.name}")
    private String ORG_MSP_NAME = "HFTeam2MSP";
    //    @Value("${fabric.org.admin.name}")
    private String ORG_ADMIN_NAME = "Admin@HFTeam2";
    //    @Value("${fabric.peer.name}")
    private String PEER_NAME = "peer0.HFTeam2";
    //    @Value("${fabric.peer.url}")
    private String PEER_URL = "grpc://l02bch4.p.ssafy.io:8051";
    //    @Value("${fabric.peer.pem.file}")
    private String PEER_PEM_FILE = "fabric-peer.pem";;
    //    @Value("${fabric.orderer.name}")
    private String ORDERER_NAME = "orderer0.ordererorg";;
    //    @Value("${fabric.orderer.url}")
    private String ORDERER_URL = "grpc://l02bch4.p.ssafy.io:7050";;
    //    @Value("${fabric.orderer.pem.file}")
    private String ORDERER_PEM_FILE = "fabric-orderer.pem";
    //    @Value("${fabric.org.user.name}")
    private String USER_NAME ="Admin@HFTeam2";
    //    @Value("${fabric.org.user.secret}")
    private String USER_SECRET = "HFTeam2pwd";
    //    @Value("${fabric.channel.name}")
    private String CHANNEL_NAME = "team2channel";
    private final String CHAINCODE_NAME = "donationtest_v_58";

    private final byte[] EXPECTED_EVENT_DATA = "!".getBytes(UTF_8);
    private final String EXPECTED_EVENT_NAME = "event";

    private FabricClient fabClient;
    private ChannelClient channelClient;
    private Channel channel;
    private JsonParser jsonParser = new JsonParser();

//    public FabricCCService() {
//        loadChannel();
//    }

    private boolean requestToLedger(String fcn, String[] args){
        try {

            TransactionProposalRequest request = fabClient.getInstance().newTransactionProposalRequest();
            ChaincodeID ccid = ChaincodeID.newBuilder().setName(CHAINCODE_NAME).build();
            request.setChaincodeID(ccid);
            request.setFcn(fcn);
            String[] arguments = args;
            request.setArgs(arguments);
            request.setProposalWaitTime(1000);

            Map<String, byte[]> tm2 = new HashMap<>();
            tm2.put("HyperLedgerFabric", "TransactionProposalRequest:JavaSDK".getBytes(UTF_8)); // Just some extra junk
            // in transient map
            tm2.put("method", "TransactionProposalRequest".getBytes(UTF_8)); // ditto
            tm2.put("result", ":)".getBytes(UTF_8)); // This should be returned see chaincode why.
            tm2.put(EXPECTED_EVENT_NAME, EXPECTED_EVENT_DATA); // This should trigger an event see chaincode why.
            request.setTransientMap(tm2);

            Iterator<String> mapIter = tm2.keySet().iterator();

            while (mapIter.hasNext()) {

                String key = mapIter.next();
                byte[] value = tm2.get(key);
            }
            Collection<ProposalResponse> responses = channelClient.sendTransactionProposal(request);

            List<ProposalResponse> invalid = responses.stream().filter(r -> r.isInvalid()).collect(Collectors.toList());
            if (!invalid.isEmpty()) {
                invalid.forEach(response -> {
                    logger.info(response.getMessage());
                });
            }
            CompletableFuture<BlockEvent.TransactionEvent> cf = channel.sendTransaction(responses);

            return true;

        } catch (Exception e) {

            return false;

        }
    }

    private Collection<ProposalResponse> queryToLedger(String[] args){

        try{
            String[] args1 = args;
            Collection<ProposalResponse> responses1Query = channelClient.queryByChainCode(CHAINCODE_NAME, "query", args1);

            return responses1Query;
        }catch(Exception e){
            return null;
        }
    }

    private void loadChannel() {
        // TODO

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
            String name = "user" + System.currentTimeMillis();
            userContext.setName(name);
            userContext.setAffiliation(ORG_NAME);
            userContext.setMspId(ORG_MSP_NAME);

            String eSecret = caClient.registerUser(name, ORG_NAME);

            userContext = caClient.enrollUser(userContext, eSecret);
            fabClient = new FabricClient(adminUserContext);

            channelClient = fabClient.createChannelClient(CHANNEL_NAME);
            channel = channelClient.getChannel();
            Peer peer = fabClient.getInstance().newPeer(PEER_NAME, PEER_URL);
            EventHub eventHub = fabClient.getInstance().newEventHub("eventhub01", "grpc://localhost:7053");
            Orderer orderer = fabClient.getInstance().newOrderer(ORDERER_NAME, ORDERER_URL);
            channel.addPeer(peer);
            channel.addEventHub(eventHub);
            channel.addOrderer(orderer);
            channel.initialize();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean registerUser(String userId) {
        if(fabClient == null){
            loadChannel();
        }

        FabricUser fabricUser = queryUserInfo(userId);

        if(fabricUser!=null){ // 중복 확인
            logger.error("Duplicate ERROR {}", userId);
            return false;
        }

        String fcn = "registerUser";
        String[] args = {userId, "1", "user"};;
        return requestToLedger(fcn, args);
    }

    public FabricUser queryUserInfo(String userId) {
        if(fabClient == null) {
            loadChannel();
        }
        try{
            String[] args = { "searchUser",userId };
            Collection<ProposalResponse> responses1Query = queryToLedger(args);

            for (ProposalResponse pres : responses1Query) {
                String stringResponse = new String(pres.getChaincodeActionResponsePayload());
                JsonElement jsonElement = jsonParser.parse(stringResponse);
                String userID = jsonElement.getAsJsonObject().get("userID").toString();
                String userPW = jsonElement.getAsJsonObject().get("userPW").toString();
                String refundAmount = jsonElement.getAsJsonObject().get("refundAmount").toString();
                String refundDonations = jsonElement.getAsJsonObject().get("refundDonations").toString();
                String auth = jsonElement.getAsJsonObject().get("auth").toString();
                String totalDonationAmount = jsonElement.getAsJsonObject().get("totalDonationAmount").toString();
                String organizedDonationProject = jsonElement.getAsJsonObject().get("organizedDonationProject").toString();
                String createdAt = jsonElement.getAsJsonObject().get("createdAt").toString();

                FabricUser fabricUser = new FabricUser(userID,userPW,auth,refundAmount,refundDonations,organizedDonationProject,totalDonationAmount,organizedDonationProject,createdAt);
                logger.info(fabricUser.toString());
                return fabricUser;
            }
        }catch (Exception e){
            logger.error(e.toString());
        }
        return null;
    }


    public boolean registerDonationPJT(String donationPJTId, String organizer, String targetAmount, String fundraisingEndDate, String[] projectGoals, String[] goalAmounts) {
        if(fabClient == null) {
            loadChannel();
        }
//      [projecttID, organizer, targetAmount, fundraisingEndDate, projectGoals, goalAmounts]

        FabricDonationPJT fabricDonationPJT = queryDonationPJTInfo(donationPJTId);

        if(fabricDonationPJT!=null){ // 중복 확인
            logger.error("Duplicate ERROR {}", donationPJTId);
            return false;
        }

        String fcn = "registerDonationProject";
        String[] args = { donationPJTId, organizer, targetAmount, fundraisingEndDate, String.join(", ", projectGoals), String.join(", ", goalAmounts)};
        return requestToLedger(fcn, args);

    }

    public FabricDonationPJT queryDonationPJTInfo(String donationPJTId) {
        if(fabClient == null) {
            loadChannel();
        }
        try{
            String[] args = { "searchDonationProject",donationPJTId };
            Collection<ProposalResponse> responses1Query = queryToLedger(args);

            for (ProposalResponse pres : responses1Query) {
                String stringResponse = new String(pres.getChaincodeActionResponsePayload());
                JsonElement jsonElement = jsonParser.parse(stringResponse);
                String projectID = jsonElement.getAsJsonObject().get("projecttID").toString();
                String organizer = jsonElement.getAsJsonObject().get("organizer").toString();
                String createdAt = jsonElement.getAsJsonObject().get("createdAt").toString();
                String targetAmount = jsonElement.getAsJsonObject().get("targetAmount").toString().replaceAll("\"","").replaceAll("'","");
                String currentAmount = jsonElement.getAsJsonObject().get("currentAmount").toString().replaceAll("\"","").replaceAll("'","");;
                String participants = jsonElement.getAsJsonObject().get("participants").toString();
                String projectGoalMap = jsonElement.getAsJsonObject().get("projectGoalMap").toString();
                String fundraisingEndDate = jsonElement.getAsJsonObject().get("fundraisingEndDate").toString();
                String state = jsonElement.getAsJsonObject().get("state").toString();
                FabricDonationPJT fabricDonationPJT = new FabricDonationPJT(projectID, organizer,
                        createdAt, targetAmount, currentAmount, participants, projectGoalMap, fundraisingEndDate, state);
                logger.info(fabricDonationPJT.toString());
                return fabricDonationPJT;
            }
        }catch (Exception e){
            logger.error(e.toString());
        }
        return null;
    }

    public boolean startDonationPJT(String donationPJTId, String organizer) {
        if(fabClient == null) {
            loadChannel();
        }
        //있는지 확인
        //state 확인
        //모금액 확인
        FabricDonationPJT fabricDonationPJT = queryDonationPJTInfo(donationPJTId);

        if(fabricDonationPJT==null){ // 있니?
            //log Not Found ERROR
            logger.error("Not Found ERROR ERROR {}", donationPJTId);
            return false;
        }
        if(!fabricDonationPJT.getState().contains("ProjectRegistered")){ //상태가 ProjectRegistered 일경우만 실행가능
            //log State Error
            logger.error("State Error {} is not ProjectRegistered", donationPJTId);
            return false;
        }
        if(Integer.parseInt(fabricDonationPJT.getCurrentAmount())<Integer.parseInt(fabricDonationPJT.getTargetAmount())){
            //목표모금액 이상 모았니?
            //log do not fill targetAmount
            logger.error("Amount Error do not fill targetAmount {}", donationPJTId);
            return false;
        }

        String fcn = "startDonationProject";
        String[] args = { donationPJTId, organizer};
        return requestToLedger(fcn, args);

    }

    //!!!!!!!!!!!!!!!!!!!!!goalid 처리부분이 좀 이상함
    //************************************
    //목표 완료시에
    public boolean compeleteGoalsDonationProject(String donationPJTID, String organizer, String goalID, String contents ){
        if(fabClient == null) {
            loadChannel();
        }
        /**
         * organizer인지 확인
         * goalID 있는지 확인
         */

        FabricDonationPJT fabricDonationPJT = queryDonationPJTInfo(donationPJTID);
        if(fabricDonationPJT==null){ // 있니?
            //log Not Found ERROR
            logger.error("Not Found ERROR ERROR {}", donationPJTID);
            return false;
        }
        if(!fabricDonationPJT.getState().contains("ProjectStarted")){ //상태가 ProjectStarted 일경우만 실행가능
            //log State Error
            logger.error("State Error {} is not ProjectStarted", donationPJTID);
            return false;
        }
        if(!fabricDonationPJT.getOrganizer().contains(organizer)){
            logger.error("Organizer Error {} is not Organizer ", donationPJTID);
        }

        TreeMap<String, String> goalsMap = getGoals(fabricDonationPJT.getProjectGoalMap());
        boolean isExit = false;
        Iterator<String> iter = goalsMap.keySet().iterator();
        while(iter.hasNext()) {
            String key = iter.next();
            if(key.equals(goalID)){
                isExit = true;
                break;
            }
        }
        if(!isExit){
            logger.error("Goals Error {} do not have ", donationPJTID);
        }

        String fcn = "compeleteGoalsDonationProject";
        String[] args = {donationPJTID, organizer, goalID, contents};
        return requestToLedger(fcn, args);
    }


    public boolean endDonationPJT(String donationPJTId, String organizer) {
        if(fabClient == null) {
            loadChannel();
        }
        //있니?
        //state확인
        //gooals확인
        //getGoals 사용
        FabricDonationPJT fabricDonationPJT = queryDonationPJTInfo(donationPJTId);

        if(fabricDonationPJT==null){ // 있니?
            //log Not Found ERROR
            logger.error("Not Found ERROR {}", donationPJTId);
            return false;
        }

        if(!fabricDonationPJT.getState().contains("ProjectStarted")){ //상태가 ProjectStarted 일경우만 실행가능
            //log State Error
            logger.error("State Error {} is not ProjectStarted", donationPJTId);
            return false;
        }

        TreeMap<String, String> goalsMap = getGoals(fabricDonationPJT.getProjectGoalMap());
        int trueCount = 0;
        Iterator<String> iter = goalsMap.keySet().iterator();
        while(iter.hasNext()) {
            String key = iter.next();
            if(goalsMap.get(key).contains("true")){
                trueCount++;
            }
        }

        if(goalsMap.size()!=trueCount){
            //log do not finish Gaols
            logger.error("Goals Error do not finish Gaols {} ", donationPJTId);
            return false;
        }

        String fcn = "endDonationProject";
        String[] args = { donationPJTId, organizer };
        return requestToLedger(fcn, args);

    }

    public boolean stopDonationPJT(String donationPJTId) {
        if(fabClient == null) {
            loadChannel();
        }
        //있니?
        //state확인
        FabricDonationPJT fabricDonationPJT = queryDonationPJTInfo(donationPJTId);
        if(fabricDonationPJT==null){ // 있니?
            //log Not Found ERROR
            return false;
        }
        if(!fabricDonationPJT.getState().contains("ProjectStarted")){ //상태가 ProjectStarted 일경우만 실행가능
            //log State Error
            return false;
        }

        String fcn = "doNotCompeleteGoal";
        String[] args = { donationPJTId };
        return requestToLedger(fcn, args);

    }

    public boolean donateToPJT(String userId, String donationPJTId, String amount) {
        if(fabClient == null) {
            loadChannel();
        }

        FabricDonationPJT fabricDonationPJT = queryDonationPJTInfo(donationPJTId);
        FabricUser fabricUser = queryUserInfo(userId);

        if(fabricUser==null){ // 있니?
            logger.error("Not Found ERROR ERROR {}", userId);
            return false;
        }
        if(fabricDonationPJT==null){ // 있니?
            //log Not Found ERROR
            logger.error(" Not Found ERROR ERROR {}", donationPJTId);
            return false;
        }

        if(!fabricDonationPJT.getState().contains("ProjectRegistered")){ //상태가 ProjectRegistered 일경우만 실행가능
            //log State Error
            logger.error("State Error {} is not ProjectRegistered", donationPJTId);
            return false;
        }

        String fcn = "donateToProject";
        String[] args = { userId, donationPJTId, amount };
        return requestToLedger(fcn, args);

    }

    public FabricDonate querydonationInfo(String userId, String donationPJTId) {

        try{

            String[] args = { "searchDonation", userId , donationPJTId };
            Collection<ProposalResponse> responses1Query = queryToLedger(args);
            for (ProposalResponse pres : responses1Query) {
                String stringResponse = new String(pres.getChaincodeActionResponsePayload());
                JsonElement jsonElement = jsonParser.parse(stringResponse);


                String amount = jsonElement.getAsJsonObject().get("amount").toString();
                String state = jsonElement.getAsJsonObject().get("state").toString();
                String createdAt = jsonElement.getAsJsonObject().get("createdAt").toString();
                String userID = userId;
                String projectID = donationPJTId;

                FabricDonate fabricDonate = new FabricDonate(userID, projectID, amount, state, createdAt);
                logger.info(fabricDonate.toString());
                return fabricDonate;
            }

        }catch(Exception e){
            logger.error(e.toString());
        }
        return null;
    }

    public boolean refundToonationPJT(String userId, String donationPJTId) {
        if(fabClient == null) {
            loadChannel();
        }
        //PJT 있니?
        //기부 한적 있니?
        FabricDonationPJT fabricDonationPJT = queryDonationPJTInfo(donationPJTId);
        FabricDonate fabricDonate = querydonationInfo(userId,donationPJTId);

        if(fabricDonationPJT==null){ // 있니?
            //log Not Found ERROR
            logger.error("Not Found ERROR ERROR {}", donationPJTId);
            return false;
        }
        if(!fabricDonationPJT.getState().contains("ProjectRegistered")){ //상태가 ProjectRegistered 일경우만 실행가능
            //log State Error
            logger.error("State Error {} is not ProjectRegistered", donationPJTId);
            return false;
        }
        if(fabricDonate!=null){ // 중복 확인
            //log Not Found ERROR
            logger.error("Not Found ERROR {} {} ", userId, donationPJTId);
            return false;
        }

        String fcn = "requestRefund";
        String[] args = { userId, donationPJTId };
        return requestToLedger(fcn, args);
    }

    public boolean queryRefund(String userId, String donationPJTId) {

        try{

            String[] args = { "searchRefundation", userId ,donationPJTId };

            Collection<ProposalResponse> responses1Query = queryToLedger(args);

            for (ProposalResponse pres : responses1Query) {
                String stringResponse = new String(pres.getChaincodeActionResponsePayload());
                JsonElement jsonElement = jsonParser.parse(stringResponse);
                String userID = userId;
                String projectID = donationPJTId;
                String amount = jsonElement.getAsJsonObject().get("amount").toString();
                String state = jsonElement.getAsJsonObject().get("state").toString();
                String createtAt = jsonElement.getAsJsonObject().get("createdAt").toString();

                FabricRefund fabricRefund = new FabricRefund(userID, projectID, state, createtAt, amount);

                logger.info(fabricRefund.toString());
            }
            return true;

        }catch(Exception e){
            logger.error(e.toString());
        }
        return false;
    }

    private TreeMap<String, String> getGoals(String projectGoalMap){
        ArrayList<String> projectGoalArray = new ArrayList<>();
        Stack<Integer> q = new Stack<>();
        TreeMap<String, String> goalsMap = new TreeMap<>();
        for(int i = 0; i < projectGoalMap.length()-1; i++){
            if(projectGoalMap.charAt(i)=='[') {
                q.add(i);
            }else if(projectGoalMap.charAt(i)==']'){
                int start = q.pop();
                int end = i;
                projectGoalArray.add(projectGoalMap.substring(start, end+1));
                int startName = projectGoalMap.substring(start, end+1).indexOf("[\\\"");
                int endName = projectGoalMap.substring(start, end+1).indexOf("\\\",{");
                int endBracket = projectGoalMap.substring(start, end+1).indexOf("}]");
                String goalName = projectGoalMap.substring(start, end+1).substring(startName+3,endName);
                String value = projectGoalMap.substring(start, end+1).substring(endName+3, endBracket+2);
                goalsMap.put(goalName,value);
            }
        }
        projectGoalArray.remove(projectGoalArray.size()-1);
        logger.info(projectGoalArray.toString());
        return goalsMap;
    }

//    public static void main(String[] args){
//        FabricCCService fbcc = new FabricCCService();
//        String[] goals = {"1","2","3"};
//        String[] amounts = {"30","30","40"};
//        fbcc.registerDonationPJT("qwerPJT2", "qwer","120000", "2020-05-20", goals, amounts);
//        fbcc.queryUserInfo("qwer2");
//        fbcc.querydonationInfo("qwer2", "qwerPJT2");
//        fbcc.queryDonationPJTInfo("qwerPJT2");
//        fbcc.queryRefund("qwer2","qwerPJT");
//        fbcc.donateToPJT("qwer2", "qwerPJT2", "120000");
//        fbcc.refundToDonationPJT("qwer2", "qwerPJT2");
//        fbcc.queryRefund("qwer2", "qwerPJT2");
//        fbcc.startDonationPJT("qwerPJT2","qwer");
//        fbcc.compeleteGoalsDonationProject("qwerPJT2","qwer"," 2","123213312");
//        fbcc.endDonationPJT("qwerPJT2","qwer");
//    }
}

