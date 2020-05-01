<template>
  <div>
    <h-breadcrumb
      title="Transaction Explorer"
      description="블록체인에서 생성된 거래내역을 보여줍니다."
    ></h-breadcrumb>
    <div class="container">
      <explorer-nav></explorer-nav>
      <div class="row" v-if="transactions.length == 0">
        <div class="col-md-8 mx-auto">
          <div class="alert alert-warning">
            No transaction recorded at. #{{ block && block.number }} blocks
          </div>
        </div>
      </div>
      <div class="row">
        <div id="transactions" class="col-md-8 mx-auto">
          <div class="card shadow-sm">
            <div class="card-header">Transactions</div>
            <div class="card-body">
              <div
                class="row tx-info"
                v-for="item in transactions"
                :key="item.id"
              >
                <div class="col-md-2">Tx</div>
                <div class="col-md-4">
                  <router-link
                    :to="{
                      name: 'explorer.tx.detail',
                      params: { hash: item.hash }
                    }"
                    class="tx-number"
                    >{{ item.hash | truncate(10) }}</router-link
                  >
                  <p class="tx-timestamp">{{ item.timeSince }}</p>
                </div>
                <div class="col-md-6">
                  <p>
                    <label class="text-secondary">From</label>
                    {{ item.from | truncate(10) }}
                  </p>
                  <p>
                    <label class="text-secondary">To</label>
                    {{ item.to | truncate(10) }}
                  </p>
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
import {
  fetchLatestBlock,
  timeSince,
  REFRESH_TIMES_OF_TRANSACTIONS,
  NUMBER_OF_CONTENTS_TO_SHOW
} from "../../utils/blockchainProvider.js";
import ExplorerNav from "./ExplorerNav.vue";
import { createWeb3 } from "../../utils/auctionFactory.js";

export default {
  components: {
    ExplorerNav
  },
  data() {
    return {
      transactions: [],
      block: {},
      blockHeight: null
    };
  },
  methods: {
    fetchTxes: function() {
      /**
       * 4주차 과제4
       * 1. 트랜잭션의 목록을 블록체인으로부터 직접 가져옵니다. 
       * 2. 트랜잭션의 목록을 서버로부터 요청하여 가져옵니다. 
       */
    }
  },
  mounted: function() {
    this.fetchTxes();
    this.$nextTick(function() {
      window.setInterval(() => {
        this.fetchTxes();
      }, REFRESH_TIMES_OF_TRANSACTIONS);
    });
  }
};
</script>

<style></style>
