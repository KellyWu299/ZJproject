<template>
    <div id="sidebar">
        <!-- <div class="showsidebar" @click="changeShowBar">
            <li class="yesshow" v-if="ifshow"> 收起 </li>
            <li class="noshow" v-if="!ifshow"> 显示 </li>
        </div> -->
        <dir class="left1bar">
            <ul  class="bars">
              
              <li class="Bar" v-for="(item,index) in bars" :key="index" @click="getLists(index)">
                  <!-- <div class="vif"  v-if="index == 3"  @click="changereadme" ></div> -->
                  <span class="vif" >{{item.title}}</span>
              </li>
          </ul>

          <div id="readme" class="tanchuang" v-if="data.showreadme">用户手册
            <button @click="item4">关闭</button>
          </div>
          <div id="tools" class="tanchuang" v-if="data.showtools">设置
            <button @click="item5">关闭</button>
          </div>
        </dir>

        <div class="left2bar">
          
          <div class="lifather" v-if="data.ifhistory">
            <p>Search History</p> 
            <li class="datasetli"  v-for="(item,index) in hhistory" :key="index">
              <li>datasetid : {{item.datasetid}}</li>
              <li>pointid : {{item.pointid}}</li>
            </li>
          </div>
          <div class="lifather" v-if="data.nebulaid">
            <p>Nubula Dataset</p>
            <li class="datasetli" @click="changeheight" v-for="(item,index) in dataset.nebulaDataset" :key="index">
              <li>id : {{item.id}}</li>
              <li>name : {{item.name}}</li>
              <li>description : {{item.description}}</li>
              <li>vertexProperty : {{item.vertexProperty}}</li>
              <li>edgeProperty : {{item.edgeProperty}}</li>
              <li>taskName : {{item.taskName}}</li>
              <li>janusIdFileName : {{item.janusIdFileName}}</li>
            </li>
          </div>
          <div class="lifather" v-if="data.janusid">
            <p>Janus Dataset</p>
            <li class="datasetli" @click="changeheight" v-for="(item,index) in dataset.janasDataset" :key="index">
              <li>id : {{item.id}}</li>
              <li>name : {{item.name}}</li>
              <li>description : {{item.description}}</li>
              <li>vertexProperty : {{item.vertexProperty}}</li>
              <li>edgeProperty : {{item.edgeProperty}}</li>
              <li>taskName : {{item.taskName}}</li>
              <li>janusIdFileName : {{item.janusIdFileName}}</li>
            </li>
          </div>
      </div>

      <div class="searchg1">
            <input ref='datasetID' v-model="searchdata.datasetid" type="text" placeholder=" 请输入数据集id">

            <input ref='pointID' v-model="searchdata.pointid" type="text" placeholder=" 请输入点id">
            <button class=" change3 searchbtn" @click="doSearch"><span>GO</span></button>
            
        </div>
    </div>
  </template>
  

  <script setup>
  //import { monitorEventLoopDelay } from "perf_hooks";

  import { reactive, defineEmits ,ref,watch} from "vue";
  import { showFooter } from "../settings";
  import axios from 'axios';



    var ifshow = true;
    const data = reactive({
      showreadme : false,
      showtools :false,
      color:"red",
      graphid:0  ,
      ifhistory:false,
      nebulaid:true,
      janusid:false,
      ifheight : false
    })

    const dataset = reactive({
      janasDataset:[],
      nebulaDataset:[]
    })

    
    var  hhistory = [
    {
      datasetid:1,
      pointid:2
    },
    {
      datasetid:11,
      pointid:2
    },
    ]
    
    const searchdata = reactive({
      datasetid:"",
      pointid:""
    })
    

    const ajaxcreated = () =>{
        var tableData = "";
        var centerData = "";
        var datasetinfo = "";
        
        var port = "8199";
        var ip0 = "localhost";
        var ip1 = "192.168.70.184";
      async function getData () {
        var selectDataset = "http://"+ip0+":"+port+"/dataset/selectDataset";
        try {
          
          datasetinfo = (await axios.get(selectDataset)).data.data
          console.log("datasetinfo",datasetinfo)
          for(let i in datasetinfo)
          if(datasetinfo[i]["source"] == 2){
            dataset.janasDataset.push(datasetinfo[i]);
          }
          else{
            dataset.nebulaDataset.push(datasetinfo[i]);
          }
        } catch (error) {
          // console.error("error",error);
          // alert("查询失败，请检查连接")
        }
        finally{
          console.log("nebula",dataset.nebulaDataset);
          console.log("janus",dataset.janasDataset);
        }
      }
      getData()
    }
  ajaxcreated();

    
    /*
    <!-- const changegid = (val)=>{
      data.graphid=val;
        console.log("changeid:",data.graphid);
        emit("sendgid", data.graphid);  
        console.log("changeid2:",data.graphid);
    }

    const emit = defineEmits(["searchid"]); -->
    */
    const emit = defineEmits(["sendgid"]);
    const doSearch = ()=>{
        console.log(searchdata)
        emit("sendgid", searchdata);  
        
        var ifexist=0;
        /*
        <!-- for(let i in hhistory)
        {
          if(hhistory[i]==searchdata)ifexist=1;
        } -->
        */
        if(true){
          hhistory.push({
            datasetid:searchdata.datasetid,
            pointid:searchdata.pointid
          })
        }
        
        
    }

    const bars = [
        {title:'图计算'},
        {title:'Nebula'},
        {title:'Janus'},
        {title:'历史记录'},
        {title:'用户手册'},
        {title:'设置'},
        
      ]
     
    const getLists = (index)=>{
        switch (index+1) {
          case 1:item1()
          break
          case 2:item2()
          break
          case 3: item3()
          break
          case 4:item6()
          break
          case 5:item4()
          break
          case 6:item5()
          break
        }}

    const changeheight=(e)=>{
      let ce = e.currentTarget
      if(ce.style.height == 'auto'){
        ce.style.height = '33px'
      }
      else{
        ce.style.height = 'auto'
      };   
    }
    

    const item1=()=>{
      console.log("I am item1's method")
    }
    const item2 =()=>{
      console.log("I am item2's method")
      //changegid(0);
      
      data.nebulaid=true;
      data.janusid=false;
      data.ifhistory=false;
      
    }
    const item3 = ()=>{
      console.log("I am item3's method")
      //changegid(1);
      
      data.nebulaid=false;
      data.janusid=true;
      data.ifhistory=false;
    }
    const item4 = ()=>{
      console.log("I am item4's method")
      data.showreadme=!data.showreadme;
      console.log(data.showreadme)
    }
    const item5 = ()=>{
      console.log("I am item5's method")
      data.showtools=!data.showtools;
    }
    const item6 = ()=>{
      console.log("I am item6's method")
      
      data.nebulaid=false;
      data.janusid=false;
      data.ifhistory=true;
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
    .left1bar{
      
        background-color: rgb(255, 222, 222);
        margin: 0;
        position: fixed;
        left: 0;
        height: 100%;
        width: 6%;
        float: left;
        // display: flex;
        //z-index: 10;
        overflow: hidden;
        .Bar{
        position: relative;
        //left: -10%;
        //margin: 1px;
        text-align: center;
        height: 70px;
        width: 110%;
        line-height: 70px;
        border-bottom: solid 1px rgb(175, 175, 175);
        //padding-left: 10%;
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
        background-color: rgb(255, 238, 238);
        li{
            position: relative;
            top: 43%;
            left: 20%;
        }
    }


    .Side{
        overflow:scroll;
    }

    .tanchuang{
      position: fixed;
        top: 150px;
        left: 50%;
        margin-left: -300px;
        height: 500px;
        width: 600px;
        background-color: rgb(127, 187, 255);
    }

    }

    
    .left2bar{
    background-color: rgb(255, 238, 238);
    margin: 0;
    position: fixed;
    left: 6%;
    height: 100%;
    width: 17%;
    float: left;
    overflow: scroll;


    z-index: 11;

    div{
      width: 100%;
      height: 100%;
      //background-color: antiquewhite;

      .datasetli{
        height: 33px;
        overflow: hidden;
        padding: 5px;
        cursor: pointer;
      }

      .datasetli:hover{
        height: auto;
        
      }

      .datasetli:checked{
        height: 150px;
      }
      .datasetli:nth-child(odd){
        background-color: rgb(255, 177, 203);
      }

      .datasetli:nth-child(even){
        background-color: rgb(177, 191, 255);
      }
    }

    p{
      font-size: x-large;
      margin:10px
      
    }
  }



  .searchg1{
            
            float: left;
            width: 800px;
            height: 100%;
            left: 23%;
            position: fixed;
            margin-left: 100px;
        input{
          //border-radius: 20px;
          height: 30px;
          width: 200px;
          font-size: large;
          padding-left: 10px;
          float: left;
        }
        .searchbtn{
          height: 40px;
          line-height: 40px;
          color: rgb(255, 255, 255);
          font-size: larger;
          cursor: pointer;
          position: relative;
  float: left;
  left: 30px;
        }
        input::-webkit-input-placeholder {    /* Chrome/Opera/Safari */
            color: rgb(140, 140, 140);
            }      
        }
  
  </style> 



