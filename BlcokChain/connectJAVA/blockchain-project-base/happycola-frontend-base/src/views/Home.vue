<template>
  <div>
    <h-nav></h-nav>
    <div id="main-overview" class="container">
      <div class="row">
        <div class="col-md-12">
          <h1>USED ITEM<br />AUCTION</h1>
          <h4>
            안전한 블록체인 기반의 중고 물품 경매를 시작해보세요.
            <img id="haribo-bear" src="../../public/images/haribo-bear.gif" />
          </h4>
          <router-link
            :to="{ name: 'signup' }"
            class="btn btn-lg btn-primary"
            v-if="!$store.state.isSigned"
          >
            회원가입
          </router-link>
          <router-link
            :to="{ name: 'shop' }"
            class="btn btn-lg btn-primary"
            v-if="$store.state.isSigned"
          >
            Shop
          </router-link>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import HNav from "../components/common/HNav";

export default {
  components: {
    HNav
  }
};
</script>

<style>
#haribo-bear {
  height: 200px;
}
</style>
