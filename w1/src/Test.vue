<script setup>

import TB2 from "@/components/TopBar2.vue"
import G2 from "@/components/Graph2.vue"
import SideBar from "@/components/SideBar.vue"
// import left2bar from "@/components/left2bar.vue"
import { ref } from "vue";
 

// const anotherSon = ref(null);
// const getSonData = (val) => {
//   anotherSon.value.showAnotherSon(val);
// };

const searchToGraph = ref(null);
// const sendSearch = (val) => {
//   searchToGraph.value.Receive(val);
// };

// const searchToGraph = ref(null);
const Sendgid = (val) => {
  searchToGraph.value.ReceiveGraphid(val);
};
</script>
 
<template>
  <!-- <p>I am Father.</p>
  <OneSon @sonDataName="getSonData"></OneSon>
  <AnotherSon ref="anotherSon"></AnotherSon> -->
  <!-- <TB2 @searchNode="sendSearch"></TB2> -->
  <TB2></TB2>
  <SideBar @sendgid="Sendgid"></SideBar>
  <G2 ref="searchToGraph"></G2>
</template>
 
<style lang="less" scoped>
  body{
    padding: 0;
    margin: 0;
  }
</style>