<template>
  <div>
    <h-breadcrumb
      title="경매 입찰하기"
      description="선택한 경매에 입찰을 합니다."
    ></h-breadcrumb>
    <div class="row">
      <div class="col-md-4 mx-auto">
        <div class="card">
          <div class="card-header">경매 입찰하기</div>
          <div class="card-body">
            <div class="form-group">
              <label><b>상품</b></label
              ><br />
              {{ item["name"] }}
            </div>
            <div class="form-group">
              <label><b>내 지갑 잔액</b></label
              ><br />
              {{ wallet["balance"] }} ETH
            </div>
            <div class="form-group">
              <label id="privateKey"><b>지갑 개인키</b></label>
              <input
                id="privateKey"
                v-model="input.privateKey"
                type="text"
                class="form-control"
                placeholder="지갑 개인키를 입력해주세요."
              />
            </div>
            <div class="form-group">
              <label id="price"><b>입찰금액</b></label>
              <div class="input-group">
                <input
                  id="price"
                  v-model="input.price"
                  type="text"
                  class="form-control"
                  placeholder="입찰 금액을 입력해주세요."
                />
                <div class="input-group-append">
                  <div class="input-group-text">ETH</div>
                </div>
              </div>
              <br />
              <div
                v-if="!auction.highestBid"
                class="alert alert-warning"
                role="alert"
              >
                최소 입찰 금액은
                {{ convertWeiToEth(auction["minPrice"]) }} ETH 입니다.
              </div>
              <div v-else class="alert alert-warning" role="alert">
                현재 최고 입찰가는
                {{ convertWeiToEth(auction["highestBid"]) }} ETH 입니다.
              </div>
            </div>
            <div class="row">
              <div class="col-md-6">
                <button
                  class="btn btn-sm btn-outline-secondary"
                  v-on:click="$router.go(-1)"
                >
                  이전으로 돌아가기
                </button>
              </div>
              <div class="col-md-6 text-right">
                <button
                  class="btn btn-sm btn-primary"
                  v-on:click="bid"
                  v-bind:disabled="bidding"
                >
                  {{ bidding ? "입찰을 진행 하는 중입니다." : "입찰하기" }}
                </button>
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
// import * as auctionService from "../../api/auction.js";
import * as walletService from "../../api/wallet.js";
// import { auction_bid } from "../../utils/auctionFactory.js";
import { weiToEth, ethToWei } from "../../utils/ethereumUnitUtils.js";
import BN from "bn.js";

export default {
  data() {
    return {
      bidding: false,
      item: {},
      auction: {},
      input: {
        privateKey: "",
        price: 0
      },
      userId: this.$store.state.user.id,
      wallet: {}
    };
  },
  methods: {
    bid() {
      /**
       * TODO: 4주차 과제2 [입찰]
       * 해당 경매 컨트랙트의 bid 함수를 호출한다.
       * bid 함수는 payable 함수임을 유의한다. 
       * 입찰 후 입찰 정보를 서버에 저장한다.
       */
    },
    convertWeiToEth(value) {
      if (value) {
        return weiToEth(value.toString());
      }

      return value;
    }
  },
  mounted() {
    const scope = this;
    const itemId = this.$route.params.id;

    auctionService.findByItemId(itemId, function(response) {
      scope.auction = response.data; //auction.minPrice 더 이상 Number 형이 아니다.
      itemService.findById(itemId, function(response) {
        scope.item = response.data;
      });
    });

    // 내 지갑 정보 조회
    walletService.findById(scope.userId, function(response) {
      const wallet = response.data;
      wallet["balance"] = Number(wallet["balance"]) / 10 ** 18;
      scope.wallet = wallet;
    });
  }
};
</script>

<style></style>
