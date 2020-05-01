<template>
  <div>
    <h-breadcrumb
      title="Block Explorer"
      description="블록체인에서 생성된 블록내역을 보여줍니다."
    ></h-breadcrumb>
    <div class="container">
      <explorer-nav></explorer-nav>
      <div class="row">
        <div class="col-md-12">
          <div id="blocks" class="col-md-8 mx-auto">
            <div class="card shadow-sm">
              <div class="card-header">Blocks</div>
              <div class="card-body">
                <div
                  class="row block-info"
                  v-for="item in blocks"
                  :key="item.id"
                >
                  <div class="col-md-2">BK</div>
                  <div class="col-md-4">
                    <router-link
                      :to="{
                        name: 'explorer.block.detail',
                        params: { blockNumber: item.number }
                      }"
                      class="block-number"
                      >{{ item.number }}</router-link
                    >
                    <p class="block-timestamp">{{ item.timestamp }}</p>
                  </div>
                  <div class="col-md-6 text-right">
                    <p class="block-num-transactions">
                      {{ item.txCount }} Txes
                    </p>
                  </div>
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
  fetchBlocks,
  timeSince,
  REFRESH_TIMES_OF_BLOCKS,
  NUMBER_OF_CONTENTS_TO_SHOW
} from "../../utils/blockchainProvider.js";
import ExplorerNav from "./ExplorerNav.vue";

export default {
  components: {
    ExplorerNav
  },
  data() {
    return {
      lastReadBlock: 0,
      blocks: []
    };
  },
  methods: {
    fetchBlocks: function() {
    /**
     * 4주차 과제4
     * 1. 블록의 목록을 블록체인으로부터 직접 가져옵니다. 
     * 2. 불록의 목록을 서버로부터 요청하여 가져옵니다. 
     */

    // 구현 예: 최근 10개의 블록 정보를 가져와서 계속 업데이트 한다.
  
    }
  },
  mounted: function() {
    this.fetchBlocks();

    this.$nextTick(function() {
      window.setInterval(() => {
        this.fetchBlocks();
      }, REFRESH_TIMES_OF_BLOCKS);
    });
  }
};
</script>

<style></style>
