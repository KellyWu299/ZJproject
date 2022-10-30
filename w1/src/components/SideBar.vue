<template>
    <div id="sidebar">
        <!-- <div class="showsidebar" @click="changeShowBar">
            <li class="yesshow" v-if="ifshow"> 收起 </li>
            <li class="noshow" v-if="!ifshow"> 显示 </li>
        </div> -->
        <ul  class="bars">
            
            <li class="Bar" v-for="(item,index) in bars" :key="index" @click="getLists(index)">
                <!-- <div class="vif"  v-if="index == 3"  @click="changereadme" ></div> -->
                <span class="vif" >{{item.title}}</span>
            </li>
        </ul>

        <div id="readme" v-if="data.showreadme">asdugasdi</div>
    </div>
  </template>
  

  <script setup>
  import { reactive, defineExpose ,watch} from "vue";
      

    var ifshow = true;
    const data = reactive({
      showreadme : false

    })
    const bars = [
        {title:'图计算',method:'item1'},
        {title:'Nebula Graph',method:'item2'},
        {title:'Janus Graph',method:'item3'},
        {title:'用户手册',method:'item4'},
        {title:'设置',method:'item5'},
      ]
     
    const getLists = (index)=>{
        switch (index+1) {
          case 1:item1()
          break
          case 2:item2()
          break
          case 3: item3()
          break
          case 4:item4()
          break
          case 5:item5()
        }}
    const item1=()=>{
      console.log("I am item1's method")
    }
    const item2 =()=>{
      console.log("I am item2's method")
    }
    const item3 = ()=>{
      console.log("I am item3's method")
    }
    const item4 = ()=>{
      console.log("I am item4's method")
      data.showreadme=!data.showreadme;
      console.log(data.showreadme)
    }
    const item5 = ()=>{
      console.log("I am item5's method")
    }


    const changeShowBar = (e)=>{
            let Sidebar = e.currentTarget.parentElement;
            if(ifshow==true){
                Sidebar.style.left= "-15.2%"
            }else
            {
                Sidebar.style.left= "0"
            }
            ifshow = !ifshow;
        }
    
  
  </script>
  <style lang="less" scoped>
  
    *{
        margin: 0;
        padding: 0;
        user-select: none;
    }
    #sidebar{
        background-color: rgb(249, 226, 226);
        margin: 0;
        position: fixed;
        left: 0;
        height: 100%;
        width: 13.5%;
        float: left;
        // display: flex;
        z-index: 10;
        overflow: hidden;

    }

    .Bar{
        position: relative;
        left: -10%;
        text-align: center;
        height: 70px;
        width: 110%;
        line-height: 70px;
        border-bottom: solid 1px black;
        padding-left: 10%;
        font-size: large;
        pointer-events:all;
        span{
            height: 100%;
            width: 100%;
        }
    }

    .Bar:hover{
        background-color: rgb(146, 127, 255);
        cursor:pointer;

    }
    .showsidebar{
        position: absolute;
        right: 0;
        height: 100%;
        width: 10%;
        // border: 1px solid rgb(255, 217, 217);
        line-height: 100%;
        z-index: 100;
        background-color: rgb(255, 217, 217);
        li{
            position: relative;
            top: 43%;
            left: 20%;
        }
    }


    .Side{
        overflow:scroll;
    }

    #readme{
      position: fixed;
        top: 200px;
        left: 50%;
        margin-left: -200px;
        height: 400px;
        width: 400px;
        background-color: aquamarine;
    }
  </style> 



