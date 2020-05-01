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
          <div
            class="alert alert-warning"
            v-if="contract && contract.highestBid == 0"
          >
            아직 경매에 참여한 이력이 없습니다.
          </div>
          <table class="table table-bordered table-striped">
            <thead>
              <tr>
                <th colspan="2"># {{ contractAddress }}</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <th width="20%">Contract Address</th>
                <td>{{ contractAddress }}</td>
              </tr>
              <tr>
                <th width="20%">상품</th>
                <td>
                  <router-link
                    :to="{ name: 'item.detail', params: { id: item['id'] } }"
                    >{{ item && item["name"] }}</router-link
                  >
                </td>
              </tr>
              <tr>
                <th>Status</th>
                <td>
                  <span
                    class="badge badge-success"
                    v-if="contract && !contract.ended"
                    >Processing</span
                  >
                  <span
                    class="badge badge-danger"
                    v-if="contract && contract.ended"
                    >Ended</span
                  >
                </td>
              </tr>
              <tr>
                <th>Start Time Time</th>
                <td>{{ contract && contract.startTime.toLocaleString() }}</td>
              </tr>
              <tr>
                <th>Expire Time</th>
                <td>{{ contract && contract.endTime.toLocaleString() }}</td>
              </tr>
              <tr>
                <th>Highest Bid</th>
                <td>{{ contract && contract.highestBid }} ETH</td>
              </tr>
              <tr>
                <th>Highest Bidder</th>
                <td>
                  <span v-if="contract && contract.highestBid == 0">-</span>
                  <span v-if="contract && contract.highestBid != 0">{{
                    contract && contract.highestBidder
                  }}</span>
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
import * as itemService from "../../api/item.js";
import {
  createWeb3,
  createAuctionContract
} from "../../utils/auctionFactory.js";
import ExplorerNav from "./ExplorerNav.vue";

export default {
  components: {
    ExplorerNav
  },
  data() {
    return {
      contractAddress: "",
      contract: null,
      item: {
        id: 0
      }
    };
  },
  mounted: async function() {
    /**
     * 4주차 과제4
     * 1. 특정 경매의 정보을 블록체인으로부터 직접 가져옵니다. 
     * 2. 특정 경매의 목록을 서버로부터 요청하여 가져옵니다. 
     */
    
  }
};
</script>

<style></style>
