<template>
  <div class="row sub-nav mb-3">
    <div class="col-md-6">
      <nav class="nav nav-pills nav-fill">
        <router-link
          class="nav-item nav-link"
          v-bind:class="{ active: path.startsWith('/explorer/auction') }"
          :to="{ name: 'explorer.auction' }"
          >Auctions</router-link
        >
        <router-link
          class="nav-item nav-link"
          v-bind:class="{ active: path.startsWith('/explorer/block') }"
          :to="{ name: 'explorer.block' }"
          >Blocks</router-link
        >
        <router-link
          class="nav-item nav-link"
          v-bind:class="{ active: path.startsWith('/explorer/tx') }"
          :to="{ name: 'explorer.tx' }"
          >Transactions</router-link
        >
      </nav>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      path: ""
    };
  },
  mounted() {
    this.path = this.$route.path;
  }
};
</script>

<style></style>
