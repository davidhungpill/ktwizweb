<template>
  <div class="home">
    <img alt="Vue logo" src="../assets/logo.png" />
    <PredictionEvent msg="Welcome to Your Vue.js + TypeScript App" />
  </div>
</template>

<script lang="ts">
import { Options, Vue } from "vue-class-component";
import PredictionEvent from "@/components/PredictionEvent.vue"; // @ is an alias to /src

@Options({
  components: {
    PredictionEvent,
  },
})
export default class Home extends Vue {}
</script>
<style></style>
