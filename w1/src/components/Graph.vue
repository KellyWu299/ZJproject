<template>
    <div id="graphScreen">
      <li v-for="item in nodes">{{item}}</li>
      <button @click="Re1">接受</button>
      <p>{{newgraph}}</p>
    </div>
</template>
  

  <script>
  import Request from '@/utils/request.js'
  import emitter from '@/utils/bus.js'
  

  import { reactive, defineExpose } from "vue";
 
  const state = reactive({
    data2: 13,
  });
  const showAnotherSon = (val) => {
    state.data2 = val;
  };
  defineExpose({ showAnotherSon });





  export default {
    props: {
      sideV1:{
        default:0,
      },
      sideV2:{
        default:0,
      }
  },


    mounted(){
      this.RecieveData();
    },
    // onMounted(){
    //   emitter.on("share",e=>{
    //     console.log("我拿到了",e);
    //     this.newgraph=e;
    //   })
    // },
    
    
    
    created(){
      // this.$EventBus.$on('share',val=>{
      //   this.nodes = val;
      // })
    },  
    beforeDestroy(){
    //移除监听事件"msg"
    //this.$EventBus.$off("share")
    },
    methods:{
      Re1:function(){
      emitter.on('share', data => {
        console.log(data)
        this.newgraph = data
      })
    },
      RecieveData:function(){
        var link = 'algo/computeBcById?id=358';
        Request({
          url:link,
          method:'get',

        }).then(result=>{
          console.log(result)
        })
      }
    },
    // watch:{
    //   newgraph:
    // },
      data(){
          return{
            newgraph:0,
              zujian:"组件data",
              Stage:null,
              layer:null,
              nodes:[101,101,101]
          }
      }
  }
  </script>
  <style lang="less">
  
  </style> 



