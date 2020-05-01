<template>
  <div>
    <h-breadcrumb
      title="상품 이력 보기"
      description="등록된 상품과 관련된 이력을 볼 수 있습니다."
    ></h-breadcrumb>
    <div class="container">
      <div class="row">
        <div class="col-md-12 mx-auto">
          <div class="card">
            <div class="card-body">
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
              <div class="row">
                <div class="col-md-12 mb-5">
                  <table class="table table-striped table-bordered">
                    <thead>
                      <tr v-if="history.length == 0">
                        <th>등록된 이력이 없습니다.</th>
                      </tr>
                      <tr v-if="history.length > 0">
                        <th width="30%">상태</th>
                        <th width="30%">날짜</th>
                        <th width="40%">기록자</th>
                      </tr>
                    </thead>
                    <tbody>
                      <tr v-for="item in history" v-bind:key="item.id">
                        <td>{{ item.state }}</td>
                        <td>{{ item.createdAt }}</td>
                        <td>{{ item.recorder | shippingAdmin }}</td>
                      </tr>
                    </tbody>
                  </table>
                </div>
              </div>
              <div class="row">
                <div class="col-md-6">
                  <button
                    v-on:click="$router.go(-1)"
                    class="btn btn-sm btn-outline-secondary"
                  >
                    뒤로가기
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
import * as userService from "../../api/user.js";

export default {
  data() {
    return {
      item: {
        id: 0,
        name: "",
        seller: 0
      },
      user: {
        id: 0,
        name: "",
        email: ""
      },
      userId: this.$store.state.user.id,
      history: []
    };
  },
  filters: {
    shippingAdmin(value) {
      if (value === "100000") {
        return "shipping admin";
      } else {
        return value;
      }
    }
  },
  mounted: function() {
    /**
     * TODO: 4주차 과제4
     * 상품 이력 조회
     */
    const scope = this;
    const itemId = this.$route.params.id;

    // 상품 상세 정보 조회
    itemService.findById(itemId, function(response) {
      const data = response.data;
      scope.item.id = itemId;
      scope.item.name = data["name"];
      scope.item.seller = data["seller"];

      userService.findById(scope.item.seller, function(response) {
        const user = response.data;
        scope.user.id = user["id"];
        scope.user.name = user["name"];
        scope.user.email = user["email"];
      });
    });

    //상품 이력 조회
    itemService.findHistoryById(
      itemId,
      function(response) {
        scope.history = response.data;
      },
      function() {
        alert("이력 조회에 문제가 있습니다.");
      }
    );
  }
};
</script>

<style>
img.center {
  display: block;
  margin: 2rem auto;
}
</style>
