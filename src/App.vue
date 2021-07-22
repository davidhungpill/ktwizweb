<template>
  <div id="app">
    <Header />
    <PredictionEvent msg="Welcome to Your Vue.js App" />
    <Footer />
  </div>
  {{ phase }}
</template>

<script>
import Header from "./components/common/header"; //import 헤더 추가
import PredictionEvent from "./components/PredictionEvent.vue";
import Footer from "./components/common/footer"; //import 풋터 추가

export default {
  name: "App",
  components: {
    Header,
    PredictionEvent,
    Footer,
  },

  data: () => ({
    isMobile: false,
  }),

  created() {
    console.log(process.env);
  },

  beforeUnmount() {
    if (typeof window !== "undefined") {
      window.removeEventListener("resize", this.onResize, { passive: true });
    }
  },

  mounted() {
    this.phase = process.env.VUE_APP_PHASE;
    this.onResize();
    window.addEventListener("resize", this.onResize, { passive: true });
  },

  methods: {
    onResize() {
      this.isMobile = window.innerWidth < 600;
    },
  },
};
</script>
