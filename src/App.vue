<template>
  <div id="app">
    <Header />
    <HelloWorld msg="Welcome to Your Vue.js App" />
    <Footer />
  </div>
  {{ phase }}
</template>

<script>
import Header from "./components/common/header"; //import 헤더 추가
import HelloWorld from "./components/HelloWorld.vue";
import Footer from "./components/common/footer"; //import 풋터 추가

export default {
  name: "App",
  components: {
    Header,
    HelloWorld,
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
