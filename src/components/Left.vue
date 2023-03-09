<template>
    <div class="left">
      <h1>{{ msg }}</h1>
      <h2>你在干什么</h2>
      <hr>
      <MyCount></MyCount>
      
      <input type="text" v-if="ifvisisble" @blur="showButton" ref="input1"/>
      <button v-else @click="showInput">展示输入框</button>

    </div>
  </template>
  
  <script>
  export default {
    name: 'Left',
    props: {
      
    },
    data(){
        return{
            msg:"你是什么",
            ifvisisble:false,
        }
        
    },
    methods:{
      showInput(){
        this.ifvisisble =true
        this.$nextTick(()=>{
          this.$refs.input1.focus()
        })
        
      },
      showButton(){
        this.ifvisisble=false
      }
    }
  }
  </script>
  
  <!-- Add "scoped" attribute to limit CSS to this component only -->
  <style scoped lang="less">
  
  .left{
    background-color: #42b983;
  }
  h3 {
    margin: 40px 0 0;
  }
  ul {
    list-style-type: none;
    padding: 0;
  }
  li {
    display: inline-block;
    margin: 0 10px;
  }
  a {
    color: #42b983;
  }
  </style>
  