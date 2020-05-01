<template>
  <div class="breadcrumb">
    <div class="container">
      <h4>{{ title }}</h4>
      <p>{{ description }}</p>
    </div>
  </div>
</template>

<script>
export default {
  props: ["title", "description"]
};
</script>
