/**
 * TODO: 4주차 과제2
 * 스마트 컨트랙트와 상호작용할 함수들을 구현합니다. 
 */
import Web3 from "web3";
import { BLOCKCHAIN_URL, AUCTION_FACTORY_CONTRACT_ADDRESS } from "../config";
import {
    AUCTION_FACTORY_CONTRACT_ABI,
    AUCTION_CONTRACT_ABI
} from "../config/ABIs.js";

// Web3 Object 생성
export function createWeb3() {
    var web3 = new Web3(new Web3.providers.HttpProvider(BLOCKCHAIN_URL));
    return web3;
}

// TODO: AuctionFactory 컨트랙트의 인스턴스
export function createFactoryContract(web3) {
    var auctionContract = new web3.eth.Contract(
        AUCTION_FACTORY_CONTRACT_ABI,
        AUCTION_FACTORY_CONTRACT_ADDRESS
    );
    return auctionContract;
}

// TODO: AuctionFactory 컨트랙트의 인스턴스
export function createAuctionContract(web3, contractAddress) {
    var auctionContract = new web3.eth.Contract(
        AUCTION_CONTRACT_ABI,
        contractAddress
    );
    return auctionContract;
}

// TODO: 경매 생성: AuctionFactory의 createAuction 함수 호출 
export function createAuction(
    options,
    walletAddress,
    privateKey,
    onConfirm,
    onFail,
    onFinally
) {
    // 함수 작성 후 아래 statements는 삭제한다. 
    onFail;
    onFinally;
}

// TODO: 입찰하기: Auction의 bid 함수 호출 
export function auction_bid(options, onConfirm, onFail) {
    // 함수 작성 후 아래 statements는 삭제한다. 
    onFail;
}

// TODO: 경매 종료: Auction의 endAuction 함수 호출 
export function auction_close(options, onConfirm, onFail) {
    // 함수 작성 후 아래 statement는 삭제한다. 
    onFail;

}

// TODO: 경매 취소: Auction의 canceldAuction 함수 호출 
export function auction_cancel(options, onConfirm, onFail, onFinally) {
    // 함수 작성 후 아래 statements는 삭제한다. 
    onFail;
    onFinally;
}

// TODO: 즉시 구매: Auction의 purchase 함수 호출 
export function auction_purchase(options, onConfirm, onFail) {
    // 함수 작성 후 아래 statement는 삭제한다. 
    onFail;
}

// TODO: 구매 확정: Auction의 confirmItem 함수 호출 
export function auction_confirm(options, onConfirm, onFail, onFinally) {
    // 함수 작성 후 아래 statements는 삭제한다. 
    onFail;
    onFinally;
}

// TODO: 환불: Auction의 withraw 함수 호출 
export function bid_withdraw(options, onConfirm, onFail, onFinally) {
    // 함수 작성 후 아래 statements는 삭제한다. 
    onFail;
    onFinally;
}

// EXAMPLE: 환불 받을 이더가 있는지 확인 (필요에 따라 삭제할 수 있음.)
export function getPendingReturnsBy(options, onConfirm) {
    var web3 = new Web3(new Web3.providers.HttpProvider(BLOCKCHAIN_URL));
    var contract = createAuctionContract(web3, options.contractAddress);
    contract.methods
        .getPendingReturnsBy(options.walletAddress)
        .call()
        .then(onConfirm)
        .catch(console.error);
}

// UTILS: 남은 경매 시간
export function getLeftTime(date) {
    var now = new Date();
    var endDate = new Date(date);
    var diff = endDate.getTime() - now.getTime();

    // 만약 종료일자가 지났다면 "경매 마감"을 표시한다.
    if (diff < 0) {
        return "경매 시간 종료";
    } else {
        const d = Math.floor(diff / (1000 * 60 * 60 * 24));
        const h = Math.floor((diff % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
        const m = Math.floor((diff % (1000 * 60 * 60)) / (1000 * 60));
        //const s = Math.floor((diff % (1000 * 60)) / 1000);

        return "남은시간: " + d + "일 " + h + "시간 " + m + "분";
    }
}