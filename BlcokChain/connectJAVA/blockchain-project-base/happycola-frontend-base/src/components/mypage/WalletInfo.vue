<template>
  <div>
    <h-breadcrumb
      title="마이페이지"
      description="지갑을 생성하거나 상품 상태를 확인할 수 있습니다."
    ></h-breadcrumb>
    <div class="container">
      <my-page-nav></my-page-nav>
      <div id="mywallet-info" class="row">
        <div class="col-md-12 mt-5">
          <div class="card">
            <table class="table table-bordered">
              <tr>
                <th>회원 정보</th>
                <td class="text-right">{{ user.name }}({{ user.email }})</td>
              </tr>
              <tr>
                <th>총보유 ETH</th>
                <td class="text-right">{{ wallet["balance"] }} ETH</td>
                <td colspan="2">
                  <button
                    type="button"
                    class="btn btn-secondary"
                    v-on:click="charge()"
                    v-bind:disabled="isCharging"
                  >
                    {{ isCharging ? "충전중" : "ETH 충전하기" }}
                  </button>
                </td>
              </tr>
              <tr>
                <th>내 지갑주소</th>
                <td class="text-right">{{ wallet["address"] }}</td>
                <th>충전 회수</th>
                <td class="text-right">{{ wallet["receivingCount"] }}회</td>
              </tr>
            </table>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import * as walletService from "../../api/wallet.js";
import { findById } from "../../api/user.js";
import { createWeb3 } from "../../utils/auctionFactory.js";
import MyPageNav from "./MyPageNav.vue";

export default {
  components: {
    MyPageNav
  },
  data() {
    return {
      wallet: {
        id: 0,
        ownerId: null,
        address: "",
        balance: 0,
        receivingCount: 0
      },
      user: {
        name: "",
        email: ""
      },
      isCharging: false, // 현재 코인을 충전하고 있는 중인지 확인하는 변수
      userId: this.$store.state.user.id
    };
  },
  methods: {
    // ETH 충전하기
    charge() {
      /**
       * TODO: 2주차 과제1 [코인 충전]
       */
    },
    fetchWalletInfo() {
      /**
       * TODO: 2주차 과제1 [지갑 열람]
       * 사용자 지갑을 조회하여 잔액을 화면에 보여준다.
       */
    },
    // 회원 정보를 가져온다.
    fetchUserInfo() {
      const scope = this;
      findById(this.userId, function(response) {
        const data = response.data;
        scope.user.name = data["name"];
        scope.user.email = data["email"];
      });
    }
  },
  mounted() {
    this.fetchWalletInfo();
    this.fetchUserInfo();
  }
};
</script>

<style></style>
