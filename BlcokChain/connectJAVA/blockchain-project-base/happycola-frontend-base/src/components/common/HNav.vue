<template>
  <nav class="navbar navbar-expand-lg fixed-top navbar-dark bg-primary">
    <div class="container">
      <img id="nav-icon" src="../../../public/images/soda.svg">
      <router-link class="navbar-brand" to="/">HAPPYCOLA</router-link>
      <div
        class="navbar-collapse offcanvas-collapse"
        id="navbarsExampleDefault"
      >
        <ul class="navbar-nav ml-auto">
          <li class="nav-item">
            <router-link class="nav-link" to="/shop">Shop</router-link>
          </li>
          <li class="nav-item">
            <router-link class="nav-link" to="/explorer/auctions"
              >Explorer</router-link
            >
          </li>
          <li class="nav-item" v-if="$store.state.isSigned">
            <router-link
              class="nav-link"
              to="/mypage/wallet_create"
              v-if="!$store.state.user.hasWallet"
              >MyPage</router-link
            >
            <router-link
              class="nav-link"
              to="/mypage/wallet_info"
              v-if="$store.state.user.hasWallet"
              >MyPage</router-link
            >
          </li>
          <li class="nav-item" v-if="!$store.state.isSigned">
            <router-link class="nav-link" to="/login">Sign In</router-link>
          </li>
          <li class="nav-item" v-if="!$store.state.isSigned">
            <router-link class="nav-link" to="/register">Sign Up</router-link>
          </li>
          <li class="nav-item" v-if="$store.state.isSigned">
            <router-link class="nav-link" to="/logout">Sign out</router-link>
          </li>
        </ul>
      </div>
    </div>
  </nav>
</template>

<script>
export default {};
</script>

<style>
#nav-icon {
  height: 40px;
}
</style>
