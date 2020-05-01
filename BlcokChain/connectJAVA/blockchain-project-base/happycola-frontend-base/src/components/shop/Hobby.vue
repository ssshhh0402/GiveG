<template>
  <div>
    <h-shop-categories :category="'게임/취미'"></h-shop-categories>
    <div id="artwork-list" class="container">
      <div class="row">
        <div class="col-md-12 text-right">
          <router-link to="/item/create" class="btn btn-outline-secondary"
            >상품 등록하기</router-link
          >
        </div>
      </div>
      <div class="row">
        <div
          class="col-md-3 artwork"
          v-for="item in items"
          v-bind:key="item.id"
        >
          <div class="card">
            <div class="card-body">
              <img src="../../../public/images/millennium-falcon.png" />
              <h4>{{ item["name"] }}</h4>
              <p v-if="item['explanation'].length > 0">
                {{ item["explanation"] }}
              </p>
              <p v-else>-</p>
              <p>{{ item["endTime"] }}</p>
              <router-link
                :to="{ name: 'item.detail', params: { id: item['id'] } }"
                class="btn btn-block btn-secondary"
                >자세히 보기</router-link
              >
            </div>
          </div>
        </div>
      </div>
      <div class="row">
        <div class="col-md-12 text-center">
          <nav class="bottom-pagination">
            <ul class="pagination">
              <li class="page-item disabled">
                <a class="page-link" href="#">이전</a>
              </li>
              <li class="page-item"><a class="page-link" href="#">1</a></li>
              <li class="page-item"><a class="page-link" href="#">2</a></li>
              <li class="page-item"><a class="page-link" href="#">3</a></li>
              <li class="page-item"><a class="page-link" href="#">4</a></li>
              <li class="page-item"><a class="page-link" href="#">5</a></li>
              <li class="page-item"><a class="page-link" href="#">6</a></li>
              <li class="page-item"><a class="page-link" href="#">7</a></li>
              <li class="page-item"><a class="page-link" href="#">8</a></li>
              <li class="page-item"><a class="page-link" href="#">9</a></li>
              <li class="page-item"><a class="page-link" href="#">10</a></li>
              <li class="page-item"><a class="page-link" href="#">다음</a></li>
            </ul>
          </nav>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { findAll } from "../../api/item.js";
import { CATEGORIES, ITEM_STATUS } from "../../config/constants.js";
import HShopCategories from "./HShopCategories.vue";
import { findByItemIdReturnsPromise } from "../../api/auction.js";
import { getLeftTime } from "../../utils/auctionFactory.js";

export default {
  components: {
    HShopCategories
  },
  data() {
    return {
      items: []
    };
  },
  mounted: function() {
    const scope = this;

    findAll(function(response) {
      if (response.data.length > 0) {
        scope.items = response.data.filter(
          e =>
            e.category === CATEGORIES.HOBBY &&
            e.state === ITEM_STATUS.ONSALE.symbol
        );

        for (const item of scope.items) {
          findByItemIdReturnsPromise(item.id).then(response => {
            scope.$set(item, "endTime", getLeftTime(response.data.endTime));
          });
        }
      }
    });
  }
};
</script>

<style></style>
