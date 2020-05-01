import { createInstance } from "./index.js";

const instance = createInstance();

// 전체 경매 내역 조회
export function findAll(success, fail) {
  instance
    .get("/api/auctions")
    .then(success)
    .catch(fail);
}

export function findAllByUser(userId, success, fail) {
  instance
    .get("/api/auctions/owner/" + userId)
    .then(success)
    .catch(fail);
}

export function register(body, success, fail) {
  instance
    .post("/api/auctions", JSON.stringify(body))
    .then(success)
    .catch(fail);
}

export function findById(id, success, fail) {
  instance
    .get("/api/auctions/" + id)
    .then(success)
    .catch(fail);
}

export function findByItemId(id, success, fail) {
  instance
    .get("/api/auctions/item/" + id)
    .then(success)
    .catch(fail);
}

// 경매 내역 저장
export function saveBid(bidder, auctionId, bidPrice, success, fail) {
  const data = {
    participant: bidder,
    auctionId: auctionId,
    amount: bidPrice,
    createdAt: new Date()
  };

  instance
    .post("/api/auctions/bid", JSON.stringify(data))
    .then(success)
    .catch(fail);
}
// 경매 취소
export function cancel(auctionId, creatorId, success, fail) {
  instance
    .delete("/api/auctions/" + auctionId + "/by/" + creatorId)
    .then(success)
    .catch(fail);
}
// 경매 종료
export function close(auctionId, bidderId, success, fail) {
  instance
    .put("/api/auctions/" + auctionId + "/by/" + bidderId)
    .then(success)
    .catch(fail);
}
// 상품 즉시 구매
export function purchase(buyer, auctionId, price, success, fail) {
  const data = {
    participant: buyer,
    auctionId: auctionId,
    amount: price
  };

  instance
    .put("/api/auctions/purchase", JSON.stringify(data))
    .then(success)
    .catch(fail);
}

export function findByItemIdReturnsPromise(id) {
  return instance.get("/api/auctions/item/" + id);
}
