<template>
  <div>
    <h-breadcrumb
      title="경매 등록하기"
      description="상품의 경매를 등록합니다."
    ></h-breadcrumb>
    <div class="row">
      <div class="col-md-6 mx-auto">
        <div class="card">
          <div class="card-header">신규 경매 등록하기</div>
          <div class="card-body">
            <div v-if="!registered">
              <div class="form-group">
                <label id="item">상품명</label>
                <input
                  id="privateKey"
                  :value="this.item.name"
                  type="text"
                  class="form-control"
                  disabled
                />
              </div>
              <div class="form-group">
                <label id="privateKey">지갑 개인키</label>
                <input
                  id="privateKey"
                  v-model="auction.privateKey"
                  type="text"
                  class="form-control"
                  placeholder="지갑 개인키를 입력해주세요."
                />
              </div>
              <div class="form-group">
                <label id="minPrice">최저가</label>
                <div class="input-group">
                  <input
                    id="minPrice"
                    v-model="auction.minPrice"
                    type="text"
                    class="form-control"
                    placeholder="최저가를 입력해주세요."
                  />
                  <div class="input-group-append">
                    <div class="input-group-text">ETH</div>
                  </div>
                </div>
              </div>
              <div class="form-group">
                <label id="purchasePrice">즉시 구매가</label>
                <div class="input-group">
                  <input
                    id="purchasePrice"
                    v-model="auction.purchasePrice"
                    type="text"
                    class="form-control"
                    placeholder="즉시 구매가를 입력해주세요."
                  />
                  <div class="input-group-append">
                    <div class="input-group-text">ETH</div>
                  </div>
                </div>
              </div>
              <div class="form-group">
                <label id="startDate">경매 시작일시</label>
                <input
                  id="startDate"
                  type="text"
                  value="경매 등록 시간으로 자동 설정됩니다."
                  class="form-control"
                  disabled
                />
              </div>
              <div class="form-group">
                <label id="untilDate">경매 종료일시</label>
                <input
                  id="untilDate"
                  v-model="auction.untilDate"
                  type="text"
                  class="form-control"
                  placeholder="yyyy-MM-dd HH:mm:ss, 예: 2019-05-03 12:00:00"
                />
              </div>
              <div class="row">
                <div class="col-md-12 text-right">
                  <button
                    class="btn btn-sm btn-primary"
                    v-on:click="register"
                    v-bind:disabled="isCreatingContract"
                  >
                    {{
                      isCreatingContract
                        ? "계약을 생성하는 중입니다."
                        : "경매 등록하기"
                    }}
                  </button>
                </div>
              </div>
            </div>
            <div v-if="registered">
              <div class="alert alert-success" role="alert">
                상품에 대한 경매가 시작되었습니다.
              </div>
              <table class="table table-bordered mt-5">
                <tr>
                  <th>상품 이름</th>
                  <td>{{ item["name"] }}</td>
                </tr>
                <tr>
                  <th>최저가</th>
                  <td>
                    {{ convertWeiToEth(after.result["minPrice"].toString()) }}
                    ETH
                  </td>
                </tr>
                <tr>
                  <th>즉시구매가</th>
                  <td>
                    {{
                      convertWeiToEth(after.result["purchasePrice"].toString())
                    }}
                    ETH
                  </td>
                </tr>
                <tr>
                  <th>시작일시</th>
                  <td>{{ auction.startDate }}</td>
                </tr>
                <tr>
                  <th>종료일시</th>
                  <td>{{ auction.untilDate }}</td>
                </tr>
                <tr>
                  <th>컨트랙트 주소</th>
                  <td>{{ after.result["contractAddress"] }}</td>
                </tr>
              </table>

              <div class="row">
                <div class="col-md-12 text-right">
                  <router-link
                    :to="{ name: 'item.detail', params: { id: item.id } }"
                    class="btn btn-sm btn-dark"
                    >상품 상세 정보</router-link
                  >
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
import { createWeb3, /*createAuction*/ } from "../../utils/auctionFactory.js";
import { weiToEth, ethToWei } from "../../utils/ethereumUnitUtils.js";
import { AUCTION_STATUS } from "../../config/constants.js";

export default {
  data() {
    return {
      // 경매 등록하려는 아이템
      item: {
        id: null,
        name: null
      },
      isCreatingContract: false,
      registered: false,
      userId: this.$store.state.user.id,
      sharedStates: this.$store.state,
      // 경매 등록전 입력값
      auction: {
        privateKey: "",
        minPrice: null,
        purchasePrice: null,
        startDate: null, // 경매 등록 시 설정
        untilDate: null
      },
      // 경매 등록 후 등록 결과 완료 표시 용도
      after: {
        result: {}
      },
      web3: createWeb3()
    };
  },
  methods: {
    register: function() {
      /**
       * TODO: 4주차 과제2 [경매 생성]
       * 경매 종료 날짜 Validitiy를 체크
       * 사용자 이더리움 계정 주소로
       * AuctionFactory를 직접 호출하여
       * 새로운 경매를 블록체인에 생성한다.
       * 생성된 컨트랙트 주소(CA)를 서버에 저장한다.
       */
    },
    /**
     * @param {String} value
     */
    convertWeiToEth(value) {
      return weiToEth(value);
    },
    convertEthToWei(value) {
      return ethToWei(value);
    },
    /**
     * yyyy-MM-ddThh:mm:ss 형태로 날짜를 표기하는 문자열을 현재 지역 시간으로 얻음
     * @param {Date} date
     */
    getDateISOString(date) {
      return new Date(new Date(date).toString().split("GMT")[0] + " UTC")
        .toISOString()
        .split(".")[0];
    }
  },
  mounted: function() {
    var scope = this;
    itemService.findById(this.$route.params.id, function(response) {
      scope.item.id = response.data.id;
      scope.item.name = response.data.name;
    });
  }
};
</script>

<style>
.row {
  padding-top: 1rem;
}
</style>
