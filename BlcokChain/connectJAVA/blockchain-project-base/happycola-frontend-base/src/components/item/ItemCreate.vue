<template>
  <div>
    <h-breadcrumb
      title="상품 등록"
      description="새로운 상품을 등록합니다."
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
                  v-model="item.description"
                  placeholder="ex) 1년 사용한 제품입니다. / 오른쪽에 작은 흠집이 있습니다."
                ></textarea>
              </div>
              <button
                type="button"
                class="btn btn-primary"
                v-on:click="save"
                v-bind:disabled="isCreating"
              >
                {{
                  isCreating
                    ? "상품을 등록하는 중입니다."
                    : "상품을 등록합니다."
                }}
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { create } from "../../api/item.js";

export default {
  data() {
    return {
      item: {
        name: "",
        category: "",
        description: ""
      },
      userId: this.$store.state.user.id,
      isCreating: false
    };
  },
  methods: {
    // 상품을 등록한다. 
    save() {
      const scope = this;
      this.isCreating = true;
      const item = {
        name: this.item.name,
        category: this.item.category,
        explanation: this.item.description,
        seller: this.userId
      };

      if (
        item.name.length <= 0 ||
        item.category.length <= 0 ||
        item.userId <= 0
      ) {
        alert("입력폼을 모두 입력해주세요.");
        return;
      }

      create(
        item,
        function(response) {
          alert("상품이 등록되었습니다.");
          scope.$router.push("/auction/register/" + response.data.id);
          scope.isCreating = false;
        },
        function(error) {
          alert("상품 등록에 실패했습니다.");
          console.error(error);
          scope.isCreating = false;
        }
      );
    }
  }
};
</script>

<style></style>
