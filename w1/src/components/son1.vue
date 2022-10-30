<template>
    <div>
        <p @click="emitSonData">I am OneSon.</p>
    </div>
  </template>
  

  <script setup>
  import { reactive, defineEmits } from "vue";
 
 const state = reactive({
   sonData: 12,
 });
 const emit = defineEmits(["sonDataName"]);
 const emitSonData = () => {
   emit("sonDataName", state.sonData);
 };
 
  </script>
  <style lang="less" scoped>

  </style> 


