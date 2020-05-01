<template>
  <div>
    <h-breadcrumb
      title="즉시 구매하기"
      description="선택한 상품을 즉시구매가로 바로 구입합니다."
    ></h-breadcrumb>
    <div class="row">
      <div class="col-md-4 mx-auto">
        <div class="card">
          <div class="card-header">즉시 구매하기</div>
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
              <label><b>지갑 개인키</b></label>
              <input
                v-model="input.privateKey"
                type="text"
                class="form-control"
                placeholder="지갑 개인키를 입력해주세요."
              />
            </div>
            <div class="form-group">
              <label><b>즉시 구매가</b></label
              ><br />
              <div class="input-group">
                <input
                  v-model="input.price"
                  type="text"
                  class="form-control"
                  placeholder="즉시 구매가를 입력해주세요."
                />
                <div class="input-group-append">
                  <div class="input-group-text">ETH</div>
                </div>
              </div>
              <br />
              <div class="alert alert-warning" role="alert">
                즉시 구매가는
                {{ convertWeiToEth(auction["purchasePrice"]) }} ETH 입니다.
              </div>
              <div
                v-if="auction.highestBid"
                class="alert alert-danger"
                role="alert"
              >
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
                  v-on:click="purchase"
                  v-bind:disabled="purchasing"
                >
                  {{
                    purchasing ? "즉시 구매를 진행 하는 중입니다." : "구매하기"
                  }}
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
import * as auctionService from "../../api/auction.js";
import * as walletService from "../../api/wallet.js";
import { auction_purchase } from "../../utils/auctionFactory.js";
import { weiToEth, ethToWei } from "../../utils/ethereumUnitUtils.js";
import BN from "bn.js";

export default {
  name: "AuctionPurchase",
  data() {
    return {
      purchasing: false,
      item: {},
      auction: {},
      wallet: {},
      input: {
        privateKey: "",
        price: ""
      },
      userId: this.$store.state.user.id
    };
  },
  methods: {
    purchase() {
      /**
       * TODO: 4주차 과제2 [즉시 구매]
       * 해당 경매 컨트랙트의 purchase 함수를 호출한다.
       * 호출 후 즉시 구매 정보를 서버에 등록한다.
       */
  },
  mounted: function() {
    const scope = this;
    const itemId = this.$route.params.id;

    auctionService.findByItemId(itemId, function(response) {
      scope.auction = response.data;
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
