<template>
    <div id="topbar">
        <div class="logo">
            <h2>湖南大学 x 之江实验室</h2>
            <h2>查询图关系使用手册</h2>
        </div>
        <!-- <div class="g1">
            <input ref='info' v-model="data.search" type="text" placeholder=" 请输入想要查询的点">
            <button class="btn change3 searchbtn" @click="doSearch"><span>GO</span></button>
             <button class="btn change3 clearbtn" @click="clearScreen"><span>clear</span></button> 
            
        </div> -->
    </div>
  </template>
  

  <script setup>
  import { reactive, defineEmits ,ref} from "vue";

  const info = ref(null)
  const data = reactive({
    nodes:[100,100,100],
    search:"",
    sendTest:1011
 });
    const emit = defineEmits(["searchNode"]);
    const doSearch = ()=>{
        console.log(data.search)
        emit("searchNode", data.search);  
    }

    const clearScreen = ()=>{
        console.log("clear");
    }

  </script>
  <style lang="less" scoped>
  .toolbar canvas{
    position: relative;
    text-emphasis-position:center;
    text-align: center;
}

    #topbar{
        width: 100%;
        background-color: rgb(178, 27, 47);
        position: fixed;
        top: 0;
        margin: 0;
        z-index: 1000;
        height: 50px;

        .logo{
            float: left;
            height: 100%;
            position: relative;
            // left: 3%;
            h2,h3{
                float: left;
                height: 100%;
                line-height: 50px;
                color: rgb(251, 249, 249);
                margin-left: 40px;
            }
            
        }

        .g1{
            
            float: left;
            width: 400px;
            height: 100%;
            margin-left: 100px;
        input{
            padding-left: 17px;
            font-size: medium;
            height: 60%;
            width: 15%;
            // position: relative;
            // top:20%;
            border: 0ch;
            outline: none; 
            border-radius: 20px;
            position: absolute;
            top: 20%;
            left: 50%;
        }
        input::-webkit-input-placeholder {    /* Chrome/Opera/Safari */
            color: rgb(140, 140, 140);
            }
        
            .btn{
                height: 61%;
                padding: 5px;
                width: 90px;
                margin: 0;
                // margin-top: 10px;
            }
            .searchbtn{
                position: absolute;
                top: 20%;
                left: 63%;
            }

            .clearbtn{
                position: absolute;
                top: 20%;
                left: 70%;
            }
                
        }

        
    }
  </style> 



