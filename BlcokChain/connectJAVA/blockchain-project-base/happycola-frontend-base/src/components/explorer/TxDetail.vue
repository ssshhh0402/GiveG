<template>
  <div>
    <h-breadcrumb
      title="Transaction Explorer"
      description="블록체인에서 생성된 거래내역을 보여줍니다."
    ></h-breadcrumb>
    <div class="container">
      <explorer-nav></explorer-nav>
      <div class="row">
        <div class="col-md-12">
          <div class="card shadow-sm">
            <div class="card-header">
              <strong>{{ tx.hash }}</strong>
            </div>
            <table class="table">
              <tbody>
                <tr>
                  <th width="200">트랜잭션 해시</th>
                  <td>{{ tx.hash }}</td>
                </tr>
                <tr>
                  <th>블록 넘버</th>
                  <td>{{ tx.block }}</td>
                </tr>
                <tr>
                  <th>날짜</th>
                  <td>{{ tx.timestamp }}</td>
                </tr>
                <tr>
                  <th>송신자 주소</th>
                  <td>
                    <router-link
                      :to="{ name: 'address', params: { address: tx.from } }"
                      >{{ tx.from }}</router-link
                    >
                  </td>
                </tr>
                <tr>
                  <th>수신자 주소</th>
                  <td>
                    <router-link
                      :to="{ name: 'address', params: { address: tx.to } }"
                      >{{ tx.to }}</router-link
                    >
                  </td>
                </tr>
                <tr>
                  <th>전송한 이더</th>
                  <td>{{ tx.value }} Ether</td>
                </tr>
                <tr>
                  <th>Gas</th>
                  <td>{{ tx.gas }}</td>
                </tr>
                <tr>
                  <th>Gas Price</th>
                  <td>{{ tx.gasPrice }}</td>
                </tr>
                <tr>
                  <th>Input Data</th>
                  <td>
                    <textarea class="form-control" v-model="tx.input" readonly>
                    </textarea>
                  </td>
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
      tx: {
        hash: "-",
        timestamp: "-"
      }
    };
  },
  mounted: function() {
    /**
     * 4주차 과제4
     * 1. 특정 블록의 정보을 블록체인으로부터 직접 가져옵니다. 
     * 2. 특정 블록의 정보을 서버로부터 요청하여 가져옵니다. 
     */    
   
  }
};
</script>

<style></style>
