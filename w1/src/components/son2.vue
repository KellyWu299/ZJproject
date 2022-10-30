<script setup>
import { reactive, defineExpose } from "vue";
 
const state = reactive({
  display: 166,
});
const showAnotherSon = (val) => {
  state.display = val;
};
const hidden= () => {
  state.display = 167;
};
defineExpose({ showAnotherSon });
</script>
 
<template>
  <p @click="hidden()">I am AnotherSon.{{state.display}}</p>
</template>
 
<style lang="less" scoped></style>