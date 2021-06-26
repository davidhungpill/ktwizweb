import Vue from "vue";
import VueRouter from "vue-router";
// import Init from '../components/Init'
import Index from "../components/web/Index.vue";
import IndexM from "../components/mobile/Index.vue";

Vue.use(VueRouter);

const routes = [
  {
    path: "/",
    name: "Root",
    components: {
      default: Index,
      mobile: IndexM,
    },
  },
];

const router = new VueRouter({
  mode: "history",
  base: process.env.BASE_URL,
  routes,
});

export default router;
