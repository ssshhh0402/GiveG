<template>
  <div>
    <h-breadcrumb
      title="상품 상세 보기"
      description="등록된 상품의 상세 내역을 볼 수 있습니다."
    ></h-breadcrumb>
    <div class="container">
      <div class="row">
        <div class="col-md-8 mx-auto">
          <div class="card">
            <div class="card-body">
              <img class="center" :src="imgPath" />
              <div class="form-group">
                <label id="name" class="text-secondary">상품 이름</label>
                <p>{{ item.name }}</p>
              </div>
              <div class="form-group">
                <label id="user" class="text-secondary">판매자</label>
                <p>
                  <router-link
                    :to="{ name: 'item.by_user', params: { id: user.id } }"
                    >{{ user.name }}({{ user.email }})
                  </router-link>
                </p>
              </div>
              <div class="form-group">
                <label id="explanation" class="text-secondary">상품 설명</label>
                <p v-if="item.explanation.length > 0">{{ item.explanation }}</p>
                <p v-else>-</p>
              </div>
              <div class="form-group">
                <label id="state" class="text-secondary">상태</label><br />
                <p>{{ item.state.explanation }}</p>
              </div>
              <div class="form-group">
                <label class="text-secondary">최고입찰가</label><br />
                <p>{{ convertWeiToEth(auction.highestBid) }}</p>
              </div>
              <div class="row">
                <div class="col-md-6">
                  <button
                    v-on:click="goBack"
                    class="btn btn-sm btn-outline-secondary"
                  >
                    뒤로가기
                  </button>
                </div>
                <div class="col-md-6 text-right" v-if="userId == item.seller">
                  <router-link
                    :to="{ name: 'item.update', params: { id: item.id } }"
                    class="btn btn-sm btn-info"
                    :class="{ Disabled: !isUpdateAvailable }"
                    :disabled="!isUpdateAvailable"
                    tag="button"
                    >상품 정보 수정</router-link
                  >
                </div>
                <div class="col-md-6 text-right" v-else-if="isUpdateAvailable">
                  <router-link
                    :to="{ name: 'auction.bid', params: { id: item.id } }"
                    class="btn btn-sm btn-info"
                    >입찰하기</router-link
                  >
                  <router-link
                    :to="{ name: 'auction.purchase', params: { id: item.id } }"
                    class="btn btn-sm btn-success"
                    >즉시 구매하기</router-link
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
import * as userService from "../../api/user.js";
import { weiToEth } from "../../utils/ethereumUnitUtils.js";
import { ITEM_STATUS } from "../../config/constants.js";
import { getImgPath } from "../../utils/imageFactory.js";

export default {
  data() {
    return {
      item: {
        id: 0,
        name: "",
        explanation: "",
        isActive: "",
        state: "",
        seller: 0
      },
      user: {
        id: 0,
        name: "",
        email: ""
      },
      auction: {
        highestBid: "" // 최고 입찰가
      },
      userId: this.$store.state.user.id,
      imgPath: "",
      isUpdateAvailable: false
    };
  },
  mounted: function() {
    var scope = this;
    var itemId = this.$route.params.id;

    // 상품 상세 정보 조회
    itemService.findById(itemId, function(response) {
      const data = response.data;
      // set img path
      scope.imgPath = scope.getImgPath(data["category"]);

      scope.item.id = itemId;
      scope.item.name = data["name"];
      scope.item.explanation = data["explanation"];
      scope.item.state = ITEM_STATUS.symbolToStatus(data["state"]);
      scope.item.seller = data["seller"];

      // 입찰 및 즉시 구매 버튼이 컨텍스트에 따라 올바르게 나타남 
      scope.isUpdateAvailable =
        scope.item.state === ITEM_STATUS.ADDED ||
        scope.item.state === ITEM_STATUS.ONSALE;

      userService.findById(scope.item.seller, function(response) {
        const user = response.data;
        scope.user.id = user["id"];
        scope.user.name = user["name"];
        scope.user.email = user["email"];
      });
    });

    // 최고 입찰가
    auctionService.findByItemId(itemId, function(response) {
      scope.auction.highestBid = response.data["highestBid"];
    });
  },
  methods: {
    goBack: function() {
      // 이전 페이지로 이동한다.
      this.$router.go(-1);
    },
    getImgPath(category) {
      return getImgPath(category);
    },
    convertWeiToEth(value) {
      if (value) {
        return weiToEth(value.toString()) + " ETH";
      } else {
        return "-";
      }
    }
  }
};
</script>

<style>
img.center {
  display: block;
  margin: 2rem auto;
}
</style>
