package com.block.project.springboot.service;

import com.block.project.springboot.web.dto.kakao.KakaoUserInfo;
import com.block.project.springboot.web.dto.kakao.PayApproveResponseDto;
import com.block.project.springboot.web.dto.kakao.PayReadyResponseDto;
import com.block.project.springboot.web.dto.kakao.PayRefundResponseDto;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.*;

@RequiredArgsConstructor
@Slf4j
@Service
public class KakaoApiService {

    private static final String HOST = "https://kapi.kakao.com";

    private final String KAKAO_AUTH_URL = "https://kauth.kakao.com/oauth/authorize";
    private final String KAKAO_TOKEN_URL = "https://kauth.kakao.com/oauth/token";
    private final String KAKAO_USER_INFO_URL = "https://kapi.kakao.com/v2/user/me";
    private final String REDIRECT_URL = "http://localhost:8081/login";

//    private final String APPROVAL_URL = "http://localhost:8081/api/v1/donation/success";
//    private final String APPROVAL_URL = "http://i02b202.p.ssafy.io:8081/api/v1/donation/success"; // 폐기
    private final String APPROVAL_URL = "http://i02b202.p.ssafy.io/paymentSuccess";
    private final String CANCEL_URL = "http://localhost:8081/kakaoPayCancel";
    private final String FAIL_URL = "http://localhost:8081/kakaoPaySuccessFail";

    @Value("${spring.kakao.ClientId}")
    private String CLIENT_ID;

    @Value("${spring.kakao.AdminKey}")
    private String ADMIN_KEY;

    @Value("${spring.kakao.payCid}")
    private String CID; //가맹점 코드(테스트용)

//    private PayReadyResponseDto payReadyResponseDto;
//    private PayApproveResponseDto payApproveResponseDto;
//    private Long readyOrderId;
//    private String readyUserId;
//    private int readyAmount;

//    public String getAuthorizationCode(){
//        RestTemplate restTemplate = new RestTemplate();
//
//        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
//        map.add("client_id", CLIENT_ID);
//        map.add("redirect_uri", REDIRECT_URL);
//        map.add("response_type", "code");
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//
//        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(map, headers);
//
//        Map<String, Object> response = restTemplate.postForObject(KAKAO_AUTH_URL, requestEntity, HashMap.class);
//        log.info("code is {}", response);
//
//        return response.get("code").toString();
//    }

    public String getAccessToken(String authorizationCode) {

        RestTemplate restTemplate = new RestTemplate();

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", "authorization_code");
        map.add("client_id", CLIENT_ID);
        map.add("redirect_uri", REDIRECT_URL);
        map.add("code", authorizationCode);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application","x-www-form-urlencoded", Charset.forName("UTF-8")));

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(map, headers);

        Map<String, Object> response = restTemplate.postForObject(KAKAO_TOKEN_URL, requestEntity, HashMap.class);

        //
        Iterator<String> iterator = response.keySet().iterator();

        while (iterator.hasNext()){
            String key = iterator.next();
            String value = response.get(key).toString();

            System.out.println(key + " : " + value);
        }
        //

        log.info("response is {}", response);

        return response.get("access_token").toString();
    }

    public KakaoUserInfo getUserInfo(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();

        log.info("getUserInfo accessToken is {}", accessToken);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(new LinkedMultiValueMap<>(), headers);

        String response = restTemplate.postForObject(KAKAO_USER_INFO_URL, requestEntity, String.class);
        JsonObject rootObject = JsonParser.parseString(response).getAsJsonObject();
        JsonObject properties = rootObject.getAsJsonObject("properties");
        JsonObject accountObject = rootObject.getAsJsonObject("kakao_account");

        log.info("response is {}", response.toString());

        KakaoUserInfo kakaoUserInfo = KakaoUserInfo.builder()
                .id(rootObject.get("id").getAsString())
                .nickname(properties.get("nickname").getAsString())
                .profileImage(properties.get("profile_image").getAsString())
                .email(accountObject.get("email").getAsString())
                .build();

        log.info("kakaoUserInfo is {}", kakaoUserInfo);

        return kakaoUserInfo;
    }

    public PayReadyResponseDto readyPayment(Long donationId, String userId, String postsTitle, int amount){
        PayReadyResponseDto payReadyResponseDto;
        RestTemplate restTemplate = new RestTemplate();

//        readyOrderId = donationId;
//        readyUserId = userId;
//        readyAmount = amount;
        // 서버로 요청할 Header
        HttpHeaders headers = new HttpHeaders();

        headers.set("Authorization", "KakaoAK " + ADMIN_KEY);
        headers.setContentType(new MediaType("application","x-www-form-urlencoded", Charset.forName("UTF-8")));

        int tax = (int)((double)amount * 0.11);
        // 서버로 요청할 Body
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add("cid", "TC0ONETIME");
        params.add("partner_order_id", donationId + "");
        params.add("partner_user_id", userId);
        params.add("item_name", postsTitle);
        params.add("quantity", "1");
        params.add("total_amount", amount + "");
        params.add("tax_free_amount", tax + "");
        params.add("approval_url", APPROVAL_URL);
        params.add("cancel_url", CANCEL_URL);
        params.add("fail_url", FAIL_URL);

        HttpEntity<MultiValueMap<String, String>> body = new HttpEntity<MultiValueMap<String, String>>(params, headers);

        try {
            payReadyResponseDto = restTemplate.postForObject(new URI(HOST + "/v1/payment/ready"), body, PayReadyResponseDto.class);

            log.info("tid : {}", payReadyResponseDto.getTid());
            log.info("redirect Url : {}", payReadyResponseDto.getNext_redirect_pc_url());
            log.info("create At : {}", payReadyResponseDto.getCreate_at());

            return payReadyResponseDto;

        } catch (RestClientException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

    public PayApproveResponseDto kakaoPayInfo(String pg_token, String tid, Long donationId, String userId) {
        PayApproveResponseDto payApproveResponseDto;
        log.info("kakaoPayInfo............................................");

        RestTemplate restTemplate = new RestTemplate();

        // 서버로 요청할 Header
        HttpHeaders headers = new HttpHeaders();
//        headers.add("Authorization", "KakaoAK " + ADMIN_KEY);
//        headers.add("Accept", MediaType.APPLICATION_JSON_UTF8_VALUE);
//        headers.add("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE + ";charset=UTF-8");
        headers.set("Authorization", "KakaoAK " + ADMIN_KEY);
        headers.setContentType(new MediaType("application","x-www-form-urlencoded", Charset.forName("UTF-8")));

        log.info("tid : {}", tid);
        log.info("orderId : {}",donationId);
        log.info("userId : {}",userId);
        // 서버로 요청할 Body
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add("cid", "TC0ONETIME");
        params.add("tid", tid);
        params.add("partner_order_id", donationId + ""); // 받은 값 == donationId
        params.add("partner_user_id", userId);   // 받은 값
        params.add("pg_token", pg_token);
//        params.add("total_amount", "1000");     // 받은 값 넣어줘야됨

        HttpEntity<MultiValueMap<String, String>> body = new HttpEntity<MultiValueMap<String, String>>(params, headers);

        try {
            payApproveResponseDto = restTemplate.postForObject(new URI(HOST + "/v1/payment/approve"), body, PayApproveResponseDto.class);
            log.info("" + payApproveResponseDto);

            return payApproveResponseDto;

        } catch (RestClientException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

    public PayRefundResponseDto kakaoPayRefund(String tid, int cancelAmount){
        PayRefundResponseDto payRefundResponseDto;
        log.info("kakaoPayRefund............................................");

        RestTemplate restTemplate = new RestTemplate();

        // 서버로 요청할 Header
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + ADMIN_KEY);
        headers.setContentType(new MediaType("application","x-www-form-urlencoded", Charset.forName("UTF-8")));

        log.info("tid : {}", tid);
        log.info("cancelAmount : {}",cancelAmount);
        // 서버로 요청할 Body
        int refundTax = (int)((double)cancelAmount * 0.11);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add("cid", "TC0ONETIME");
        params.add("tid", tid);
        params.add("cancel_amount", cancelAmount + "");
        params.add("cancel_tax_free_amount", refundTax + "");

        HttpEntity<MultiValueMap<String, String>> body = new HttpEntity<MultiValueMap<String, String>>(params, headers);

        try {
            payRefundResponseDto = restTemplate.postForObject(new URI(HOST + "/v1/payment/cancel"), body, PayRefundResponseDto.class);
            log.info("" + payRefundResponseDto);

            return payRefundResponseDto;

        } catch (RestClientException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }
}
