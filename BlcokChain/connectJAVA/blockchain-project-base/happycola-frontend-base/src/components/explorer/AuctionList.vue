<template>
  <div>
    <h-breadcrumb
      title="Auction Explorer"
      description="블록체인에 기록된 경매내역을 보여줍니다."
    ></h-breadcrumb>
    <div class="container">
      <explorer-nav></explorer-nav>
      <div class="row">
        <div class="col-md-12">
          <table class="table table-bordered table-striped">
            <thead>
              <tr>
                <th>Auction</th>
                <th>Status</th>
                <th>Highest Bid</th>
                <th>Highest Bidder</th>
                <th>Expire Date</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(item, index) in contracts" :key="index">
                <td>
                  <router-link
                    :to="{
                      name: 'explorer.auction.detail',
                      params: { contractAddress: item }
                    }"
                    >{{ item | truncate(15) }}</router-link
                  >
                </td>
                <td>
                  <span
                    class="badge badge-success"
                    v-if="items[index] && !items[index].ended"
                    >Processing</span
                  >
                  <span
                    class="badge badge-danger"
                    v-if="items[index] && items[index].ended"
                    >Ended</span
                  >
                </td>
                <td>{{ items[index] && items[index].highestBid }} ETH</td>
                <td>
                  <span v-if="items[index] && items[index].highestBid != 0">{{
                    items[index] && items[index].highestBidder | truncate(15)
                  }}</span>
                  <span v-if="items[index] && items[index].highestBid == 0"
                    >-</span
                  >
                </td>
                <td>
                  {{ items[index] && items[index].endTime.toLocaleString() }}
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import {
  createWeb3,
  createFactoryContract,
  createAuctionContract
} from "../../utils/auctionFactory.js";
import ExplorerNav from "./ExplorerNav.vue";

export default {
  components: {
    ExplorerNav
  },
  data() {
    return {
      contracts: [],
      items: []
    };
  },
  mounted: async function() {
    /**
     * 4주차 과제4
     * 1. 경매 컨트랙트의 목록을 블록체인으로부터 직접 가져옵니다. 
     * 2. 경매 컨트랙트의 목록을 서버로부터 요청하여 가져옵니다. 
     */
  }
};
</script>

<style></style>
