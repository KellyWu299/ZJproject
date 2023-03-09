<template>
  <div id="graphScreen">
    <button class="change3 searchbtn DRAW" @click="draw">DRAW</button>



    <!-- <p v-if="data.graphid">JanusGraph数据集id:</p>
    <p v-else>NebulaGraph数据集id:</p>
    <input v-model="data.tableid" type="text" placeholder=" 请输入想要查询的数据集id"> -->

    <!-- <p ></p> -->
    <!-- <input v-model="data.pointid" type="text" placeholder=" 查询中心性"> -->
    <!-- <p v-if="data.pointid">{{CenterNodes.center[data.pointid]}}</p> -->

    <div class="showcenter">

      <div class="time">
        <h3 @click="checkindex" class="pointer">最短路径查询</h3>
        <input type="text" v-model="Index.v"> 
        
        <input type="text" v-model="Index.e">
        <br>
        <br>
        <li>优化前 : {{CenterNodes.badtime}}ms</li>
        <li>优化后 : {{CenterNodes.goodtime}}ms</li>
      </div>
      <h3>顶点中心性查询</h3>
      <div class="datasetli" v-for="(item) in CenterNodes.center" :key="item.index">
        <li>顶点id:{{item.node}}</li>
        <li>中心性:{{(item.centers||-1)}}</li>
      </div>
    </div>
    <!-- <input type="text" name="" id=""> -->
    <!-- <p>{{data.search}}</p> -->
    <!-- <button @click="RecieveData">我长这么长我就不信我接收</button> -->
    <div id="GS"></div>
  </div>
</template>



<script setup>
// <script src="http://www.jtopo.com/download/jtopo-1.0.1_trial-min.umd.js">
// import Request from '@/utils/request.js'
import axios from 'axios';
  import { reactive, defineExpose ,watch} from "vue";
  import { onMounted, onUpdated, onRenderTriggered } from 'vue'
  var Nebulaside = [
          {v1:1,v2:12},
          {v1:1,v2:24},
          {v1:1,v2:2},
          {v1:0,v2:1},
          {v1:0,v2:2},
          {v1:0,v2:3},
          {v1:0,v2:4},
          {v1:0,v2:5},
          {v1:5,v2:0}]
  var Janusside = [
          {v1:1,v2:12},
          {v1:1,v2:24},
          {v1:1,v2:2},
          {v1:0,v2:1},
          {v1:0,v2:2},
          {v1:0,v2:3},
          {v1:0,v2:4},
          {v1:0,v2:5},
          {v1:5,v2:0}]
  var side = []
  
  var centerCount = []
  var centerMap = new Map()
  const data = reactive({
      search:"sno",
      graphid :0,
      tableid :"",
      pointid:0
  })
  const ifchanged = reactive({
      ccount :0
  })
  // const Receive = (val) => {
  //   console.log("serach:",data.search)
  //     data.search = val;
  // };
  // defineExpose({ Receive });
  const ReceiveGraphid = (val) => {
      data.tableid = val.datasetid;
      data.search = val.pointid;
      console.log("daaaaaaaaaaaaaaaatagid",data)
      ifchanged.ccount++;
  };
  defineExpose({ ReceiveGraphid });
  var i =1;
  var port = "8199";
    var ip1 = "localhost";
    var ip0 = "192.168.70.184";
  const CenterNodes = reactive({
      node:[],
      center:[],
      janasDataset:[],
      nebulaDataset:[],
      badtime:0,
      goodtime:0,
  })

  const Index = reactive({
      v:0,
      e:0
  })


  const checkindex = () =>{
        var indexinfo = "";    
      async function getData1 () {
        var indexweb0 = "http://192.168.70.184:8199/algo/getSingleIndex?v1=271&v2=456&id=503"
        var indexweb2 = "http://192.168.70.184:8199/dataset/selectDataset"
        var indexweb ='http://'+ip0+':'+port+'/algo/getSingleIndex?v1='+Index.v+'&v2='+Index.e+'&id='+data.tableid;
        try {
          console.log("tableid:",data.tableid)
          indexinfo = (await axios.get(indexweb)).data.data["6.0"]
          console.log("indexinfo",indexinfo)
          CenterNodes.goodtime = indexinfo[0]
          CenterNodes.badtime = indexinfo[1]
          
        } catch (error) {
          
        }
        finally{
          
        }
      }
      getData1()
    }
    // checkindex();


  //   watch(Index,
  // (curr, old) => {
  //   checkindex();
  // }
  // )


  const unique = (arr)=>{
    
    console.log("unique")
    return Array.from(new Set(arr))
};
  
  var stage = null;
  var layer = null;
  const clearScreen = ()=>{
    layer.removeAllChild();
      layer.update();
  }
  var NodeMap=new Map()
  // watch(data.tableid,
  // (curr, old) => {
  //   getData()
  // },)
  const ajaxcreated = () =>{
    CenterNodes.center=[]
    var tableData = "";
    var centerData = "";
    var datasetinfo = "";
    
    var port = "8199";
    var ip1 = "localhost";
    var ip0 = "192.168.70.184";
  async function getData () {
    var selectDataset = "http://"+ip0+":"+port+"/dataset/selectDataset";
    var JanusWeb = 'http://'+ip0+':'+port+'/algo/queryDatasetById?id=';
    var NebulaWeb = 'http://'+ip0+':'+port+'/algo/queryDatasetById?id=';
    var web = 'http://'+ip0+':'+port+'/algo/queryDatasetById?id=';
    var centerweb = 'http://'+ip0+':'+port+'/algo/computeEgoById?id='
    try {
      
      // datasetinfo = (await axios.get("http://"+ip0+":"+port+"/dataset/selectDataset")).data
      // console.log("datasetinfo",datasetinfo)
      // for(let i in datasetinfo)
      // if(datasetinfo[i]["sourse"] == 2){
      //   web=JanusWeb;
      // }
      // else{
      //   web=NebulaWeb
      // }
      tableData = (await axios.get(web+data.tableid)).data
      // tableData = (await axios.get('http://localhost:8200/algo/queryDatasetById?id=383')).data
      console.log("success",tableData.data)
      side = tableData.data
      
      centerData = (await axios.get(centerweb+data.tableid)).data
      console.log("success22",centerData.data)
      centerCount = centerData.data
      // for (var i=0;i<10;i++){
      //   console.log(centerCount[i])
      // }
      
      
      
    } catch (error) {
      // console.error("error",error);
      // alert("查询失败，请检查连接")
    }
    finally{
      console.log(side)
    }
  }
  getData()
  
}
  ajaxcreated();
  
  watch(ifchanged,
  (curr, old) => {
    ajaxcreated();
  }
  )

  const draw = () => {
          // getData();
          CenterNodes.center=[];
          CenterNodes.node=[];
          // ajaxcreated  ();
          // console.log("changed:000",curr, old,i);
          if(stage!=null){
            clearScreen();
          }
          stage = new jtopo.Stage('GS');
        // var stage = this.Stage;
          layer = new jtopo.Layer('default');
          var src = (data.search)*1;
          var ifexist = 0 
          var sides = side;
          console.log("sides:",sides)
          var nodes = []
          console.log(src)
          nodes.push(src)
          for(let i in sides){
              if (sides[i].vertex1==src){
                  nodes.push(sides[i].vertex2);
                  ifexist=1;
              }
              if (sides[i].vertex2==src){
                  nodes.push(sides[i].vertex1);
                  ifexist=1;
              }
          }
          console.log("nodes::",nodes)
          if(!ifexist){
              // alert("未查询到该点");  
          }
          else{
              nodes = unique(nodes);
              console.log(nodes)
              var nodes = nodes;
              
              stage.addChild(layer);
              
              // for(let i in nodes){
              //     console.log(nodes[i])
              // }
              var rootNode = new jtopo.CircleNode(nodes[0].toString(), 300, 400, 36, 36);
              rootNode.setStyles({
                  textPosition: "center",
                  textAlign: "center",
                  textBaseline: "middle",
                  fontColor: 'black',
                  fillStyle:"orange"
              })
              rootNode.addEventListener("click",function(){
                    console.log(nodes[0])
                    console.log(centerCount[nodes[0]])
                    console.log(centerCount)
                    CenterNodes.center.push({"node":nodes[0],"centers":centerCount[nodes[0]]})
                  })
              rootNode.selectedStyle = new jtopo.Style({
                  'font': 'bold 20px 仿宋'
              });
              layer.addChild(rootNode);
              for(let i in nodes){
                  if(i==0)continue;
                  var sjs1=Math.round(Math.random()*1000)
                  var sjs2=Math.round(Math.random()*1000)
                  var newNode = new jtopo.CircleNode(nodes[i].toString(), sjs1, sjs2, 36, 36);
                  var link = new jtopo.Link('Link',rootNode,newNode);
                  layer.addChild(link);
                  newNode.setStyles({
                      textPosition: "center",
                      textAlign: "center",
                      textBaseline: "middle",
                      fontColor: 'black',
                      fillStyle:"white"
                  })
                  newNode.addEventListener("click",function(){
                    console.log(nodes[i])
                    console.log(centerCount[nodes[i]])
                    console.log(centerCount)
                    CenterNodes.center.push({"node":nodes[i],"centers":centerCount[nodes[i]]})
                  })
                  newNode.selectedStyle = new jtopo.Style({
                      'font': 'bold 20px 仿宋'
                  });
                  NodeMap.set(nodes[i],newNode);
                  layer.addChild(newNode);
              }
              console.log(NodeMap)
         
              stage.hideToolbar();
              stage.show();
          }
          
}
// ,{ deep: true }
// )
</script>
<style lang="less" >
.pointer{
	cursor: pointer;
}
#graphScreen{
  // background-color: rgb(255, 210, 210);
  margin: 0;
  // float: left;
  width: 74.5%;
  height: 1200px;
  position: relative;
  left:23% ;
  top: 80px;
  p{
    float: left;
    margin-left: 250px;
    font-size: 20px;
    z-index: 10;
  }
  input{
    float: left;
    margin-left: 15px;
    
          padding-left: 2px;
          font-size: medium;
          height: 20px;
          // position: relative;
          // top:20%;
          outline: none; 
          border-radius: 20px;
         
    z-index: 10;
  }
  .showcenter{
    // margin-right:50px ;
    float: right;
    position: fixed;
    right: 0;
    top: 49px;
    // height: 200px;
    width: 200px;
    height: 120%;
    border: 1px solid rgb(201, 147, 147);
    // height: 150px;
    background-color:rgb(255, 238, 238);
    overflow:-moz-hidden-unscrollable;
    h3{
      margin: 10px;
    }  

    .datasetli:nth-child(odd){
        background-color: rgb(246,222,212);
      }

    .datasetli:nth-child(even){
        background-color: rgb(226,175,144);
      }
    .time{
      input{
        width: 30%;
      }

    }
  }
}
#GS{
  margin: 0;
  position: relative;
  // left: 0;
   top: 20px;
  // // display: flex;
  // // float: left;
  width: 80%;
  height: 90%;
  // // margin-top: 50px;
  
  canvas{
    height: 100%;
    width: 100%;
    position: relative;
    left: 0;
    z-index: -1;
  }
  // background-color: rgb(199, 227, 252);
}
  
.searchbtn{
          height: 40px;
          line-height: 40px;
          color: rgb(255, 255, 255);
          font-size: larger;
          cursor: pointer;
        }
.DRAW{
  position: relative;
  float: left;
  left: 400px;
  top:-80px
}
</style> 


