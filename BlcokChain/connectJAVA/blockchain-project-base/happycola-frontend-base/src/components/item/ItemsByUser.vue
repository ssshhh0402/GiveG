<template>
  <div>
    <h-breadcrumb
      title="사용자 별 상품 조회"
      description="특정 사용자가 등록한 상품의 목록을 보여줍니다."
    ></h-breadcrumb>
    <div class="container">
      <div class="row">
        <div class="col-md-8 mx-auto">
          <div class="card">
            <div class="card-header">
              '{{ user.name }}({{ user.email }})'님의 상품 목록
            </div>
            <div class="card-body">
              <div v-for="item in items" :key="item.id">
                <router-link
                  :to="{ name: 'item.detail', params: { id: item.id } }"
                  class="work-list-item"
                >
                  <h4>{{ item.title }}</h4>
                  <p>{{ item.description }}</p>
                </router-link>
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
      user: {
        id: 0,
        name: "",
        email: ""
      },
      items: [
        {
          id: 0,
          title: "",
          description: ""
        }
      ]
    };
  },
  mounted: function() {
    var scope = this;
    var userId = this.$route.params.id;

    itemService.findItemsByOwner(userId, function(response) {
      var result = response.data;
      scope.items = [];
      for (var i = 0; i < result.length; i++) {
        scope.items.push({
          id: result[i]["id"],
          title: result[i]["name"],
          description:
            result[i]["explanation"] == "" ? "-" : result[i]["explanation"]
        });
      }
    });

    userService.findById(userId, function(response) {
      const data = response.data;
      scope.user.id = data["id"];
      scope.user.name = data["name"];
      scope.user.email = data["email"];
    });
  }
};
</script>

<style></style>
