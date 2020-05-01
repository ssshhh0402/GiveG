<template>
  <div>
    <h-breadcrumb
      title="경매 상품 상세 정보"
      description="선택하신 경매 상품의 상세 정보를 보여줍니다."
    ></h-breadcrumb>
    <div class="container">
      <div class="row">
        <div class="col-md-12">
          <div class="card">
            <div class="card-body">
              <table class="table table-bordered">
                <tr>
                  <th width="20%">생성자</th>
                  <td>
                    <router-link
                      :to="{
                        name: 'item.by_user',
                        params: { id: creator['id'] }
                      }"
                      >{{ creator["name"] }}({{
                        creator["email"]
                      }})</router-link
                    >
                  </td>
                </tr>
                <tr>
                  <th>상품명</th>
                  <td>{{ item["name"] }}</td>
                </tr>
                <tr>
                  <th>상품 설명</th>
                  <td>{{ item["explanation"] }}</td>
                </tr>
                <tr>
                  <th>경매 시작일</th>
                  <td>{{ auction["startTime"] }}</td>
                </tr>
                <tr>
                  <th>경매 종료일</th>
                  <td>{{ auction["endTime"] }}</td>
                </tr>
                <tr>
                  <th>최저가</th>
                  <td>
                    <strong
                      >{{ convertWeiToEth(auction["minPrice"]) }} ETH</strong
                    >
                  </td>
                </tr>
                <tr>
                  <th>즉시 구매가</th>
                  <td>
                    <strong
                      >{{
                        convertWeiToEth(auction["purchasePrice"])
                      }}
                      ETH</strong
                    >
                  </td>
                </tr>
                <tr>
                  <th>컨트랙트 주소</th>
                  <td>
                    <a href="#">{{ auction["contractAddress"] }}</a>
                  </td>
                </tr>
                <tr>
                  <th>상태</th>
                  <td>{{ stateSymbolToExplanation(auction["state"]) }}</td>
                </tr>
              </table>

              <table class="table table-bordered mt-3">
                <tr>
                  <th width="20%">최고 입찰자</th>
                  <td v-if="!auction['highestBidder']">-</td>
                  <td v-else>
                    <router-link
                      :to="{
                        name: 'item.by_user',
                        params: { id: auction['highestBidder'] }
                      }"
                      >{{ highestBidder["name"] }}({{
                        highestBidder["email"]
                      }})</router-link
                    >
                  </td>
                </tr>
                <tr>
                  <th>최고 입찰액</th>
                  <td v-if="!auction['highestBidder']">-</td>
                  <td v-else>
                    {{ convertWeiToEth(auction["highestBid"]) }} ETH
                  </td>
                </tr>
              </table>

              <div
                class="alert alert-danger mt-3"
                role="alert"
                v-if="new Date(auction['endTime']) < new Date()"
              >
                경매가 종료되었습니다.
              </div>
              <div class="row mt-5">
                <div class="col-md-6">
                  <a
                    class="btn btn-sm btn-outline-secondary"
                    v-on:click="$router.go(-1)"
                    >이전으로 돌아가기</a
                  >
                </div>
                <div
                  class="col-md-6 text-right"
                  v-if="userId === item['seller'] && !auction['ended']"
                >
                  <button
                    type="button"
                    class="btn btn-sm btn-primary"
                    v-on:click="endAuction"
                    v-bind:disabled="isCanceling || isClosing"
                  >
                    {{ isClosing ? "종료 중" : "판매종료" }}
                  </button>

                  <button
                    type="button"
                    class="btn btn-sm btn-danger"
                    v-if="new Date(auction['endTime']) > new Date()"
                    v-on:click="cancelAuction"
                    v-bind:disabled="isCanceling || isClosing"
                  >
                    {{ isCanceling ? "취소하는 중" : "판매취소" }}
                  </button>
                </div>
                <div
                  class="col-md-6 text-right"
                  v-else-if="
                    userId === item['buyer'] &&
                      item['state'] === ITEM_STATUS_DELIVER.symbol
                  "
                >
                  <button
                    type="button"
                    class="btn btn-sm btn-primary"
                    v-on:click="confirm"
                    v-bind:disabled="isConfirming"
                  >
                    {{ isConfirming ? "구매 확정 중" : "구매 확정" }}
                  </button>
                </div>
                <div
                  class="col-md-6 text-right"
                  v-else-if="
                    isBidder && item['state'] === ITEM_STATUS_ONSALE.symbol
                  "
                >
                  <button
                    type="button"
                    class="btn btn-sm btn-primary"
                    v-on:click="withdrawBid"
                    v-bind:disabled="isWithdrawing"
                  >
                    {{ isWithdrawing ? "환불 중" : "환불 요청" }}
                  </button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import * as itemService from "../../api/item.js";
import * as auctionService from "../../api/auction.js";
import * as walletService from "../../api/wallet.js";
import * as userService from "../../api/user.js";
import { AUCTION_STATUS, ITEM_STATUS } from "../../config/constants.js";
import { weiToEth } from "../../utils/ethereumUnitUtils.js";
import {
  auction_close,
  auction_cancel,
  auction_confirm,
  bid_withdraw,
  getPendingReturnsBy
} from "../../utils/auctionFactory.js";

export default {
  name: "AuctionDetail",
  data() {
    return {
      itemId: this.$route.params.id,
      userId: this.$store.state.user.id,
      item: {},
      creator: {},
      auction: {},
      highestBidder: {
        name: "",
        email: ""
      },
      isCanceling: false,
      isClosing: false,
      isConfirming: false,
      isWithdrawing: false,
      isBidder: false,
      isReturnsPending: false,
      ITEM_STATUS_DELIVER: ITEM_STATUS.DELIVER,
      ITEM_STATUS_ONSALE: ITEM_STATUS.ONSALE
    };
  },
  methods: {
    endAuction: function() {
      /**
       * TODO: 4주차 과제2 [경매 종료]
       * 해당 경매 컨트랙트의 endAuction 함수를 호출한다.
       * 이 함수 호출은 경매 등록자에 의해서만 유효하다. 
       * 경매 종료 후 해당 정보를 서버에 등록한다. 
       */
    },
    cancelAuction() {
      /**
       * TODO: 4주차 과제2 [경매 취소]
       * 해당 경매 컨트랙트의 cancelAuction 함수를 호출한다.
       * 이 함수 호출은 경매 등록자에 의해서만 유효하다. 
       * 경매 취소 후 해당 정보를 서버에 등록한다. 
       */
    },
    confirm() {
     /**
     * TODO: 4주차 과제3 [구매 확정]
     * 낙찰되거나 즉시 구매 한 상품이 배송중일 때 
     * 스마트 컨트랙트를 호출하여 구매를 확정한다.
     * 서버에 구매 확정을 요청한다. 
     */     
    },
    withdrawBid() {
    /**
     * TODO: 4주차 과제3 [환불]
     * 입찰한 경매에 대해 컨트랙트를 호출하여 환불을 요청힌다.
     */ 
    },
    stateSymbolToExplanation(symbol) {
      if (symbol) {
        return AUCTION_STATUS.symbolToStatus(symbol).explanation;
      } else {
        return "-";
      }
    },
    convertWeiToEth(value) {
      if (value) {
        return weiToEth(value.toString());
      } else {
        return "-";
      }
    }
  },
  mounted() {
    const scope = this;

    // 경매 정보 조회
    auctionService.findByItemId(this.itemId, function(response) {
      const auction = response.data;

      // 상품 정보 조회
      itemService.findById(auction["itemId"], function(response) {
        scope.item = response.data;

        // 생성자 정보 조회
        userService.findById(scope.item["seller"], function(response) {
          scope.creator = response.data;
        });
      });

      // 최고 입찰가 정보 가져오기
      if (auction["highestBidder"]) {
        userService.findById(auction["highestBidder"], function(response) {
          scope.highestBidder["name"] = response.data["name"];
          scope.highestBidder["email"] = response.data["email"];
        });
      }

      // 입찰자인지 확인
      itemService.findByBidder(scope.userId, function(response) {
        if (
          response.status === 200 &&
          response.data.filter(item => item.id === scope.itemId).length === 1
        ) {
          scope.isBidder = true;
        }
      });

      scope.auction = auction;
    });
  }
};
</script>

<style></style>
