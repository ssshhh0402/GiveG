<template>
  <div>
    <h-breadcrumb
      title="마이페이지"
      description="지갑을 생성하거나 상품 상태를 확인할 수 있습니다."
    ></h-breadcrumb>
    <div class="container">
      <my-page-nav></my-page-nav>
      <div id="my-artwork" class="row">
        <h4 class="col-md-12 mt-5">판매 상품</h4>
        <div class="col-md-12 mt-5">
          <h5>경매 전</h5>
          <div class="row">
            <div
              class="col-md-3 artwork"
              v-for="item in addedItems"
              :key="item.id"
            >
              <div class="card">
                <div class="card-body">
                  <img :src="getImgPath(item.category)" />
                  <h4>{{ item.name }}</h4>
                  <span class="badge" :class="getBadgeClass(item.state)">
                    {{ symbolToStatus(item.state).explanation }}
                  </span>
                  <div class="row no-gutters mt-3">
                    <div class="col-md-8">
                      <router-link
                        :to="{
                          name: 'auction.register',
                          params: { id: item.id }
                        }"
                        class="btn btn-block btn-secondary"
                        >경매 등록하기</router-link
                      >
                    </div>
                    <div class="col-md-4">
                      <button
                        class="btn btn-block btn-danger"
                        v-on:click="deleteItem(item.id)"
                      >
                        삭제
                      </button>
                    </div>
                  </div>
                  <!-- <router-link
                    :to="{ name: 'auction.register', params: { id: item.id } }"
                    class="btn btn-block btn-secondary mt-3"
                    >경매 등록하기</router-link
                  > -->
                </div>
              </div>
            </div>
            <div class="col-sm-12 col-md-8 mt-3" v-if="addedItems.length == 0">
              <div class="alert alert-warning">
                경매 전 상품이 없습니다.
              </div>
            </div>
          </div>
        </div>
        <div class="col-md-12 mt-5">
          <h5>경매 중</h5>
          <div class="row">
            <div
              class="col-md-3 artwork"
              v-for="item in onSaleItems"
              :key="item.id"
            >
              <div class="card">
                <div class="card-body">
                  <img :src="getImgPath(item.category)" />
                  <h4>{{ item.name }}</h4>
                  <span class="badge" :class="getBadgeClass(item.state)">
                    {{ symbolToStatus(item.state).explanation }}
                  </span>
                  <router-link
                    :to="{ name: 'auction.detail', params: { id: item.id } }"
                    class="btn btn-block btn-secondary mt-3"
                    >자세히보기</router-link
                  >
                </div>
              </div>
            </div>
            <div class="col-sm-12 col-md-8 mt-3" v-if="onSaleItems.length == 0">
              <div class="alert alert-warning">
                경매 중 상품이 없습니다.
              </div>
            </div>
          </div>
        </div>
        <div class="col-md-12 mt-5">
          <h5>경매 완료</h5>
          <div class="row">
            <div
              class="col-md-3 artwork"
              v-for="item in soldItems"
              :key="item.id"
            >
              <div class="card">
                <div class="card-body">
                  <img :src="getImgPath(item.category)" />
                  <h4>{{ item.name }}</h4>
                  <span class="badge" :class="getBadgeClass(item.state)">
                    {{ symbolToStatus(item.state).explanation }}
                  </span>
                  <div class="row no-gutters mt-3">
                    <div class="col-md-6">
                      <router-link
                        :to="{
                          name: 'auction.detail',
                          params: { id: item.id }
                        }"
                        class="btn btn-block btn-secondary"
                        >자세히보기</router-link
                      >
                    </div>
                    <div class="col-md-6">
                      <router-link
                        :to="{ name: 'item.history', params: { id: item.id } }"
                        class="btn btn-block btn-show-history"
                        >이력보기</router-link
                      >
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <div class="col-sm-12 col-md-8 mt-3" v-if="soldItems.length == 0">
              <div class="alert alert-warning">
                경매 완료 상품이 없습니다.
              </div>
            </div>
          </div>
        </div>
        <div class="col-md-12 mt-5">
          <h5>취소/삭제됨</h5>
          <div class="row">
            <div
              class="col-md-3 artwork"
              v-for="item in canceledItems"
              :key="item.id"
            >
              <div class="card">
                <div class="card-body">
                  <img :src="getImgPath(item.category)" />
                  <h4>{{ item.name }}</h4>
                  <span class="badge" :class="getBadgeClass(item.state)">
                    {{ symbolToStatus(item.state).explanation }}
                  </span>
                  <router-link
                    v-if="item.state === 'X'"
                    :to="{ name: 'auction.detail', params: { id: item.id } }"
                    class="btn btn-block btn-secondary mt-3"
                    >자세히보기</router-link
                  >
                  <router-link
                    v-else
                    :to="{ name: 'item.detail', params: { id: item.id } }"
                    class="btn btn-block btn-secondary mt-3"
                    >자세히보기</router-link
                  >
                </div>
              </div>
            </div>
            <div
              class="col-sm-12 col-md-8 mt-3"
              v-if="canceledItems.length == 0"
            >
              <div class="alert alert-warning">
                판매 취소/삭제된 상품이 없습니다.
              </div>
            </div>
          </div>
        </div>
        <hr />
        <div class="col-md-12 mt-5">
          <h4>경매 참여 상품</h4>
          <div class="row">
            <div
              class="col-md-3 artwork"
              v-for="item in biddingItems"
              :key="item.id"
            >
              <div class="card">
                <div class="card-body">
                  <img :src="getImgPath(item.category)" />
                  <h4>{{ item.name }}</h4>
                  <span class="badge" :class="getBadgeClass(item.state)">
                    {{ symbolToStatus(item.state).explanation }}
                  </span>
                  <div class="row no-gutters mt-3">
                    <div class="col-md-6">
                      <router-link
                        :to="{
                          name: 'auction.detail',
                          params: { id: item.id }
                        }"
                        class="btn btn-block btn-secondary"
                        >자세히보기</router-link
                      >
                    </div>
                    <div class="col-md-6">
                      <router-link
                        :to="{ name: 'item.history', params: { id: item.id } }"
                        class="btn btn-block btn-show-history"
                        >이력보기</router-link
                      >
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <div
              class="col-sm-12 col-md-8 mt-3"
              v-if="biddingItems.length == 0"
            >
              <div class="alert alert-warning">입찰중인 상품이 없습니다.</div>
            </div>
          </div>
        </div>
        <hr />
        <div class="col-md-12 mt-5">
          <h4>구매 상품</h4>
          <div class="row">
            <div
              class="col-md-3 artwork"
              v-for="item in purchasedItems"
              :key="item.id"
            >
              <div class="card">
                <div class="card-body">
                  <img :src="getImgPath(item.category)" />
                  <h4>{{ item.name }}</h4>
                  <span class="badge" :class="getBadgeClass(item.state)">
                    {{ symbolToStatus(item.state).explanation }}
                  </span>
                  <div class="row no-gutters mt-3">
                    <div class="col-md-6">
                      <router-link
                        :to="{
                          name: 'auction.detail',
                          params: { id: item.id }
                        }"
                        class="btn btn-block btn-secondary"
                        >자세히보기</router-link
                      >
                    </div>
                    <div class="col-md-6">
                      <router-link
                        :to="{ name: 'item.history', params: { id: item.id } }"
                        class="btn btn-block btn-show-history"
                        >이력보기</router-link
                      >
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <div
              class="col-sm-12 col-md-8 mt-3"
              v-if="purchasedItems.length == 0"
            >
              <div class="alert alert-warning">구매한 상품이 없습니다.</div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import * as itemService from "../../api/item.js";
import MyPageNav from "./MyPageNav.vue";
import { ITEM_STATUS } from "../../config/constants.js";
import { getImgPath } from "../../utils/imageFactory.js";

export default {
  components: {
    MyPageNav
  },
  data() {
    return {
      userId: this.$store.state.user.id,
      myItems: [],
      addedItems: [],
      onSaleItems: [],
      soldItems: [],
      canceledItems: [],
      purchasedItems: [],
      biddingItems: []
    };
  },
  methods: {
    deleteItem(id) {
    /**
     * TODO: 4주차 과제3
     * 마이페이지 회원 상품 목록
     */
    // TODO: 삭제된 상품 목록 가져오기
    },
    getImgPath(category) {
      return getImgPath(category);
    },
    symbolToStatus(state) {
      return ITEM_STATUS.symbolToStatus(state);
    },
    /**
     * 상태에 따라 badge 색상 변경
     */
    getBadgeClass(stateSymbol) {
      switch (stateSymbol) {
        case "A":
          return "badge-light";
        case "S":
          return "badge-success";
        case "D":
          return "badge-info";
        case "C":
          return "badge-primary";
        case "X":
          return "badge-danger";
        case "N":
          return "badge-warning";
      }
    }
  },
  mounted: function() {
    const scope = this;
    /**
     * TODO: 4주차 과제3
     * 마이페이지 회원 상품 목록
     */
    // TODO: 판매 상품 목록 가져오기
    
    // TODO: 입찰 중인 상품 목록 가져오기
    
    // TODO: 구매 상품 목록 가져오기
    
  }
};
</script>

<style>
.badge-primary {
  color: #fff;
  background-color: #007bff;
}
.badge-info {
  color: #fff;
  background-color: #17a2b8;
}
.btn-show-history {
  background-color: #5c130e;
  color: white;
}
hr {
  border: 0;
  clear: both;
  display: block;
  width: 96%;
  background-color: black;
  height: 2px;
}
</style>
