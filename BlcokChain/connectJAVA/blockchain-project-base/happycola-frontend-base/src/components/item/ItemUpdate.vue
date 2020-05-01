<template>
  <div>
    <h-breadcrumb
      title="상품 정보 수정"
      description="상품 정보를 수정합니다."
    ></h-breadcrumb>
    <div class="container">
      <div class="row">
        <div class="col-md-8 mx-auto">
          <div class="card">
            <div class="card-body">
              <div class="form-group">
                <label id="name">상품 이름</label>
                <input
                  type="text"
                  class="form-control"
                  id="name"
                  v-model="item.name"
                />
              </div>
              <div class="form-group">
                <label id="name">카테고리</label>
                <select
                  class="form-control"
                  id="category"
                  v-model="item.category"
                >
                  <option value="D">디지털/가전</option>
                  <option value="C">유아물품</option>
                  <option value="H">게임/취미</option>
                </select>
              </div>
              <div class="form-group">
                <label id="description">상품 설명</label>
                <textarea
                  class="form-control"
                  id="description"
                  v-model="item.explanation"
                ></textarea>
              </div>
              <button type="button" class="btn btn-primary" v-on:click="update">
                저장
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { update, findById } from "../../api/item.js";

export default {
  name: "ItemUpdate",
  data() {
    return {
      item: {
        id: null,
        name: "",
        category: "",
        explanation: "",
        state: "",
        seller: ""
      },
      userId: this.$store.state.user.id
    };
  },
  mounted: function() {
    var scope = this;
    var itemId = this.$route.params.id;

    findById(itemId, function(response) {
      const data = response.data;
      scope.item.id = itemId;
      scope.item.name = data["name"];
      scope.item.category = data["category"];
      scope.item.explanation = data["explanation"];
      scope.item.state = data["state"];
      scope.item.seller = scope.userId;
    });
  },
  methods: {
    update: function() {
      const scope = this;
      const itemId = this.$route.params.id;

      if (this.item.name.length > 0 && this.item.category.length > 0) {
        update(
          this.item,
          function() {
            alert("상품 정보가 수정되었습니다.");
            scope.$router.push("/item/detail/" + itemId);
          },
          function() {
            alert("상품 정보 수정에 실패했습니다.");
          }
        );
      } else {
        alert("필수 입력 정보를 입력해주세요.");
      }
    }
  }
};
</script>

<style></style>
