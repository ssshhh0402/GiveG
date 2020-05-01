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
          <div class="card shadow-sm">
            <div class="card-header">
              블록 <strong># {{ block.number }}</strong>
            </div>
            <table class="table">
              <tbody>
                <tr>
                  <th width="300">블록 height</th>
                  <td>{{ block.number }}</td>
                </tr>
                <tr>
                  <th>블록 해시</th>
                  <td>{{ block.hash }}</td>
                </tr>
                <tr>
                  <th>블록 생성 시간</th>
                  <td>{{ block.timestamp }}</td>
                </tr>
                <tr>
                  <th>Miner</th>
                  <td>
                    <a href="#">{{ block.miner }}</a>
                  </td>
                </tr>
                <tr>
                  <th>Nonce</th>
                  <td>{{ block.nonce }}</td>
                </tr>
                <tr>
                  <th>문제 난이도 (Difficulty)</th>
                  <td>{{ block.difficulty }}</td>
                </tr>
                <tr>
                  <th>블록 크기</th>
                  <td>{{ block.size }} bytes</td>
                </tr>
                <tr>
                  <th>gasLimit</th>
                  <td>{{ block.gasLimit }}</td>
                </tr>
                <tr>
                  <th>gasUsed</th>
                  <td>{{ block.gasUsed }}</td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import ExplorerNav from "./ExplorerNav.vue";
import { createWeb3 } from "../../utils/auctionFactory.js";

export default {
  components: {
    ExplorerNav
  },
  data() {
    return {
      isValid: true,
      block: {
        number: 0
      }
    };
  },
  mounted: function() {
    /**
     * 4주차 과제4
     * 1. 특정 블록의 정보을 블록체인으로부터 직접 가져옵니다. 
     * 2. 특정 블록의 목록을 서버로부터 요청하여 가져옵니다. 
     */
  }
};
</script>

<style></style>
