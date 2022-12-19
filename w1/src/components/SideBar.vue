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
            <!-- <button @click="item4">关闭</button> -->
            <p>1、点击侧边栏，选择数据集种类，可以获得相关数据集展示</p>
            <p>2、触碰相应数据集可以获得详情，点击相关数据集，进入查询模式</p>
            <p>3、进入查询模式后，在输入框输入相应点id，点击go查询，点击draw渲染网络图</p>
            <p>4、点击数据集上方nebula\janus graph可进入数据集操作界面</p>
            <p>5、点击删除可删除数据集，点击右上角加号可添加数据集</p>
            <p>6、点击history获取历史查询数据</p>
            <p>7、选择数据集后，请务必点击Go载入数据集后再进行相关查询，包括最短路径查询时</p>
          </div>
          <div id="tools" class="tanchuang" v-if="data.showtools">设置
          </div>
          <div id="tools" class="tanchuang" v-if="data.showtools">设置
            <p>本功能仍在开发中</p>
            <!-- <button @click="item5">关闭</button> -->
          </div>
        </dir>

        <div class="left2bar" v-if="data.ifshowmore">
          
          <div class="lifather" v-if="data.ifhistory">
            <p>Search History</p> 
            <li class="datasetli"  v-for="(item,index) in hhistory" :key="index">
              <li>datasetid : {{item.datasetid}}</li>
              <li>pointid : {{item.pointid}}</li>
            </li>
          </div>
          <div class="lifather" v-if="data.nebulaid">
            <p @click="showmore">Nubula Dataset</p>
            <li class="datasetli"  v-for="(item,index) in dataset.nebulaDataset" :key="index" @click="changeDatasetId(item.id)">
              <li >id : {{item.id}}</li>
              <li>name : {{item.name}}</li>
              <li>description : {{item.description}}</li>
              <li>vertexProperty : {{item.vertexProperty}}</li>
              <li>edgeProperty : {{item.edgeProperty}}</li>
              <li>taskName : {{item.taskName}}</li>
              <li>janusIdFileName : {{item.janusIdFileName}}</li>
            </li>
          </div>
          <div class="lifather" v-if="data.janusid">
            <p @click="showmore">Janus Dataset</p>
            <li class="datasetli" @click="changeDatasetId(item.id)" v-for="(item,index) in dataset.janasDataset" :key="index" >
              <li>id : {{item.id}}</li>
              <li>name : {{item.name}}</li>
              <li>description : {{item.description}}</li>
              <li>vertexProperty : {{item.vertexProperty}}</li>
              <li>edgeProperty : {{item.edgeProperty}}</li>
              <li>taskName : {{(item.taskName)}}</li>
              <li>janusIdFileName : {{item.janusIdFileName}}</li>
            </li>
          </div>
      </div>

      <div class="left3bar" v-if="data.dontshowmore"> 

          <div class="lifather" v-if="data.nebulaid">
            <p class="gun" @click="showmore">Nubula Dataset</p>
            <ul class="lihead">
              <li class="lid">id</li>
              <li class="lname">name</li>
              <li class="ldes">description</li>
              <li class="lver">vertexProperty</li>
              <li class="ledg">edgeProperty</li>
              <!-- <li class="ltas">taskName</li> -->
              <li class="lfm">janusIdFileName</li>
              <li class="addDataset" @click="changeAdd(true)">+</li>
            </ul>
            <li class="datasetli"  v-for="(item,index) in dataset.nebulaDataset" :key="index" @click="changeDatasetId(item.id)">
              <li class="lid">{{(item.id||null)}}</li>
              <li class="lname">{{(item.name||null)}}</li>
              <li class="ldes">{{(item.description||null)}}</li>
              <li class="lver">{{(item.vertexProperty||null)}}</li>
              <li class="ledg">{{(item.edgeProperty||null)}}</li>
              <!-- <li class="ltas">{{(item.taskName||null)}}</li> -->
              <li class="lfm">{{(item.janusIdFileName||null)}}</li>
              <li class="delete" @click="ajaxdelete(item.id)">删除</li>
            </li>
          </div>
          <div class="lifather" v-if="data.janusid">
            <p class="gun" @click="showmore">Janus Dataset</p>
            <ul class="lihead">
              <li class="lid">id</li>
              <li class="lname">name</li>
              <li class="ldes">description</li>
              <li class="lver">vertexProperty</li>
              <li class="ledg">edgeProperty</li>
              <!-- <li class="ltas">taskName</li> -->
              <li class="lfm">janusIdFileName</li>
              <li class="addDataset" @click="changeAdd(true)">+</li>
            </ul>
            <li class="datasetli" @click="changeheight" v-for="(item,index) in dataset.janasDataset" :key="index">
              <li class="lid">{{item.id}}</li>
              <li class="lname">{{item.name}}</li>
              <li class="ldes">{{item.description}}</li>
              <li class="lver">{{item.vertexProperty}}</li>
              <li class="ledg">{{item.edgeProperty}}</li>
              <!-- <li class="ltas">{{item.taskName}}</li> -->
              <li class="lfm">{{item.janusIdFileName}}</li>
              <li class="delete" @click="ajaxdelete(item.id)">删除</li>
            </li>
          </div>
      </div>
         
      <div class="searchg1">
            <!-- <input ref='datasetID' v-model="searchdata.datasetid" type="text" placeholder=" 请输入数据集id"> -->

            
            <input class='pointID' v-model="searchdata.pointid" type="text" placeholder=" 请输入点id">
            <button class=" change3 searchbtn" @click="doSearchAndShowmore"><span>GO</span></button>
            
        </div>

        <div class="addBar" v-if="data.ifadd">
          <button class="closebtn" @click="changeAdd(false)">X</button>
          <!-- <form id="ufd" action="" > -->
            <input class="upfile" ref="file1" type="file" name="111"   placeholder="点击上传文件1"/>
            <input class="req" type="text" placeholder="req参数" v-model="UPFILE.req">
            <button class="change3 searchbtn submitbtn" @click="upFile">提交</button>
          <!-- </form> -->
          
        </div>
    </div>
  </template>
  

  <script setup>
  //import { monitorEventLoopDelay } from "perf_hooks";

  import { reactive, defineEmits ,ref,watch} from "vue";
  import { showFooter } from "../settings";
  import axios from 'axios';
import { upload } from "@/utils/upload";
import { type } from "os";



    var ifshow = true;
    const data = reactive({
      showreadme : false,
      showtools :false,
      color:"red",
      graphid:0  ,
      ifhistory:false,
      nebulaid:true,
      janusid:false,
      ifheight : false,
      ifshowmore:true,
      dontshowmore:false,
      ifadd:false
    })
 
    const dataset = reactive({
      janasDataset:[{
            "id": 422,
            "name": "thousand.txt",
            "source": 2,
            "description": "thousandTest",
            "vertexProperty": "vv1",
            "edgeProperty": "ee1",
            "taskName": null,
            "janusIdFileName": "IdFile11"
        },
        {
            "id": 423,
            "name": "tenThousand.txt",
            "source": 2,
            "description": "tenthousandTest",
            "vertexProperty": "vv2",
            "edgeProperty": "ee2",
            "taskName": null,
            "janusIdFileName": "IdFile22"
        },
        {
            "id": 424,
            "name": "oneHundredThousand.txt",
            "source": 2,
            "description": "onehundredthousandTest",
            "vertexProperty": "vv3",
            "edgeProperty": "ee3",
            "taskName": null,
            "janusIdFileName": "IdFile33"
        },
        {
            "id": 428,
            "name": "thousand .csv",
            "source": 1,
            "description": "thousandTest",
            "vertexProperty": "null",
            "edgeProperty": "null",
            "taskName": null,
            "janusIdFileName": "null"
        },
        {
            "id": 429,
            "name": "tenThousand .csv",
            "source": 1,
            "description": "tenthousandTest",
            "vertexProperty": "null",
            "edgeProperty": "null",
            "taskName": null,
            "janusIdFileName": "null"
        },
        {
            "id": 430,
            "name": "oneHundredThousand.csv",
            "source": 1,
            "description": "onehundredthousandTest",
            "vertexProperty": "null",
            "edgeProperty": "null",
            "taskName": null,
            "janusIdFileName": "null"
        },
        {
            "id": 431,
            "name": "thousand .csv",
            "source": 1,
            "description": "thousand",
            "vertexProperty": "null",
            "edgeProperty": "null",
            "taskName": null,
            "janusIdFileName": "null"
        },
        {
            "id": 432,
            "name": "thousand .csv",
            "source": 1,
            "description": "thousand",
            "vertexProperty": "null",
            "edgeProperty": "null",
            "taskName": null,
            "janusIdFileName": "null"
        },
        {
            "id": 433,
            "name": "1000.csv",
            "source": 1,
            "description": "thousand",
            "vertexProperty": "222",
            "edgeProperty": "222",
            "taskName": null,
            "janusIdFileName": "null"
        },
        {
            "id": 434,
            "name": "1000.csv",
            "source": 1,
            "description": "thousand",
            "vertexProperty": "222",
            "edgeProperty": "222",
            "taskName": null,
            "janusIdFileName": "null"
        },
        {
            "id": 435,
            "name": "thousand .csv",
            "source": 1,
            "description": "thousand",
            "vertexProperty": "null",
            "edgeProperty": "null",
            "taskName": null,
            "janusIdFileName": "null"
        },
        {
            "id": 436,
            "name": "thousand .csv",
            "source": 1,
            "description": "thousand",
            "vertexProperty": "null",
            "edgeProperty": "null",
            "taskName": null,
            "janusIdFileName": "null"
        }],
      nebulaDataset:[{
            "id": 422,
            "name": "thousand.txt",
            "source": 2,
            "description": "thousandTest",
            "vertexProperty": "vv1",
            "edgeProperty": "ee1",
            "taskName": null,
            "janusIdFileName": "IdFile11"
        },
        {
            "id": 423,
            "name": "tenThousand.txt",
            "source": 2,
            "description": "tenthousandTest",
            "vertexProperty": "vv2",
            "edgeProperty": "ee2",
            "taskName": null,
            "janusIdFileName": "IdFile22"
        },
        {
            "id": 424,
            "name": "oneHundredThousand.txt",
            "source": 2,
            "description": "onehundredthousandTest",
            "vertexProperty": "vv3",
            "edgeProperty": "ee3",
            "taskName": null,
            "janusIdFileName": "IdFile33"
        },
        {
            "id": 428,
            "name": "thousand .csv",
            "source": 1,
            "description": "thousandTest",
            "vertexProperty": "null",
            "edgeProperty": "null",
            "taskName": null,
            "janusIdFileName": "null"
        },
        {
            "id": 429,
            "name": "tenThousand .csv",
            "source": 1,
            "description": "tenthousandTest",
            "vertexProperty": "null",
            "edgeProperty": "null",
            "taskName": null,
            "janusIdFileName": "null"
        },
        {
            "id": 430,
            "name": "oneHundredThousand.csv",
            "source": 1,
            "description": "onehundredthousandTest",
            "vertexProperty": "null",
            "edgeProperty": "null",
            "taskName": null,
            "janusIdFileName": "null"
        },
        {
            "id": 431,
            "name": "thousand .csv",
            "source": 1,
            "description": "thousand",
            "vertexProperty": "null",
            "edgeProperty": "null",
            "taskName": null,
            "janusIdFileName": "null"
        },
        {
            "id": 432,
            "name": "thousand .csv",
            "source": 1,
            "description": "thousand",
            "vertexProperty": "null",
            "edgeProperty": "null",
            "taskName": null,
            "janusIdFileName": "null"
        },
        {
            "id": 433,
            "name": "1000.csv",
            "source": 1,
            "description": "thousand",
            "vertexProperty": "222",
            "edgeProperty": "222",
            "taskName": null,
            "janusIdFileName": "null"
        },
        {
            "id": 434,
            "name": "1000.csv",
            "source": 1,
            "description": "thousand",
            "vertexProperty": "222",
            "edgeProperty": "222",
            "taskName": null,
            "janusIdFileName": "null"
        },
        {
            "id": 435,
            "name": "thousand .csv",
            "source": 1,
            "description": "thousand",
            "vertexProperty": "null",
            "edgeProperty": "null",
            "taskName": null,
            "janusIdFileName": "null"
        },
        {
            "id": 436,
            "name": "thousand .csv",
            "source": 1,
            "description": "thousand",
            "vertexProperty": "null",
            "edgeProperty": "null",
            "taskName": null,
            "janusIdFileName": "null"
        }]
    })

    
    var  hhistory = [
    ]
    
    const searchdata = reactive({
      datasetid:"",
      pointid:""
    })
    
    const UPFILE = reactive({
      req:""
    })

    var port = "8199";
    var ip1 = "localhost";
    var ip0 = "192.168.70.184";
    const  file1 = ref(null);
    const  file2 = ref(null);
    const ajaxcreated = () =>{
        var tableData = "";
        var centerData = "";
        var datasetinfo = "";    
      async function getData () {
        var selectDataset = "http://"+ip0+":"+port+"/dataset/selectDataset";
        try {
          
          datasetinfo = (await axios.get(selectDataset)).data.data
          console.log("datasetinfo",datasetinfo)
          dataset.janasDataset=[];
          dataset.nebulaDataset=[];
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



  


    const ajaxdelete =(did)=>{
      var deleteinfo="";
      //var id = did;
      async function deleteDataset(did){
        var deletedata = "http://"+ip0+":"+port+"/dataset/delete/"+did;
        try {
          deleteinfo = (await axios.post(deletedata)).data
          console.log("deleteinfo",deleteinfo);
          if(deleteinfo["code"]==200){
            alert("删除成功，请刷新查看");
          }else{
            alert("删除失败，请重试")
          }
        } catch (error) {
        }
        finally{
          console.log("nebula",dataset.nebulaDataset.length);
          console.log("janus",dataset.janasDataset.length);
        }
      }
      deleteDataset(did);
    }

  const upl1 = ()=>{
    $("#ufd").ajaxSubmit({
        type: 'post',
        xhrFields: {
          withCredentials: true
        },
        url: 'xxxxxxxxxx',
        success: function (result) {}
 })
  }
    
    
    const upFile=()=> {
      console.log(file1.value)
      var file = file1.value.files[0];
      //var file22 = file2.value.files[0];
      console.log("file",file)
      const req= {
        "source":1,"description":"thousand","vertexProperty":"null","edgeProperty":"null","taskName":"addthousandDataset--nebula","janusIdFileName":"null"
      }
      //var newreq =JSON.stringify(req);
      var newreq = UPFILE.req;
      const blob = new Blob([newreq], {
          type: 'application/json',
        });
      var blobs=new Array();
      blobs.push(blob);
      //blobs.push(blob);
      var formdata = new FormData();
      // 这里只是基本设置，对应接口需求设置响应的类型属性值
          formdata.append('file', file);
          //formdata.append('file', file22);
          //formdata.append('req','{"source":1,"description":"thousand","vertexProperty":"null","edgeProperty":"null","taskName":"addthousandDataset--nebula","janusIdFileName":"null"}')

          formdata.append("req",blob);
          console.log("formdata",formdata)
          const uploadDataset=()=>{
              try {
                
                var uploadinfo = axios.post('http://'+ip0+':'+port+'/dataset/addDataset', formdata
                ,{headers: {'Content-Type': 'multipart/form-data'}}
                );
                
                console.log("uploadinfo",uploadinfo);
                
                  alert("添加成功，请刷新查看");
                
                  
                
              } catch (error) {
                alert("添加失败，请重试")
                console.log("error",error);
              }
              finally{
                console.log("nebula",dataset.nebulaDataset.length);
                console.log("janus",dataset.janasDataset.length);
              }
                }
          uploadDataset();
      

    }


    const uploadFile=(file, url) =>{
      // 处理文件转换成formData格式
      var formdata = new FormData();
      // 这里只是基本设置，对应接口需求设置响应的类型属性值
          formdata.append('file', file);
          formdata.append('Status', 0);
          formdata.append('req','[{"source":1,"description":"thousand","vertexProperty":"null","edgeProperty":"null","taskName":"addthousandDataset--nebula","janusIdFileName":"null"}]')
      // 接口调用
      let xml = new XMLHttpRequest();
      xml.open('POST', url,true) // 第三个值指定接口是否异步
      // 设置请求头信息
      xml.setRequestHeader('token', token);
      // 监控上传进度
      //xml.upload.onprogress  = onprogressEvent()
      // 接口调用成功回调
      xml.onload = onloadEvent()
      // 接口调用失败处理
      xml.onerror = onerrorEvent()
	}
	const onprogressEvent=(e)=> {
		if (e.lengthComputable) {
			// 可以获取到实时的接口进度
        	this.realTimePercent = +parseInt((e.loaded / e.total) * 100);
      }
	}
	const onloadEvent=(e)=> {
		// 获取到接口调用成功后的返回数据
		const res = JSON.parse(e.currentTarget.response);
	}
	const onerrorEvent=(e)=> {
		// 接口调用失败后的处理
		
	}


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
    
    const changeDatasetId=(id)=>{
      searchdata.datasetid=id;
      console.log(searchdata.datasetid);
    }

    const changeAdd=(int)=>{
      data.ifadd = int;
      console.log("ifadd",data.ifadd);
    }

    

    const changeAll=(id)=>{
      changeheight();
      changeDatasetId(id);
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
    
    const showmore = ()=>{
      data.ifshowmore = !data.ifshowmore;
      data.dontshowmore = !data.dontshowmore;
      console.log(data.ifshowmore)
    }
  
    const doSearchAndShowmore = ()=>{
      doSearch();
      if(data.dontshowmore==true)
      {
        showmore();
      }
      
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
        background-color: rgb(246,222,212);
      }

      .datasetli:nth-child(even){
        background-color: rgb(226,175,144);
      }
    }

    p{
      font-size: x-large;
      margin:10px;
      cursor: pointer;
    }
  }

    .left3bar{
      position: fixed;
      width: 78%;
      left: 7%;
      top:15%;
      height: 80%;
      float: left;
      overflow:hidden;
      z-index: 11;
      //background-color: rgb(255, 194, 194);
      border: 2px solid gray;

      div{
      width: 100%;
      height: 100%;
      //background-color: antiquewhite;

      .lihead{
        height: 50px;
        width: 100%;
        line-height: 50px;
        overflow: hidden;
        padding: 5px;
        //cursor: pointer;
        float: left;
        li{
          float: left;
          overflow: hidden;
          //border:1px solid black ;
        }
        .addDataset{
        position: absolute;
        right:10px;
        font-size: xx-large;
        cursor: pointer;
      }
      }
      .datasetli{
        
        height: 33px;
        line-height: 33px;
        overflow: hidden;
        padding: 5px;
        cursor: pointer;
        li{
          float: left;
          overflow: hidden;
        }
        .delete{
        position: relative;
        right:15px;
        float: right;
      }
      }

      .lid{
        width: 10%;
      }
      .lname{
        width:10%;
      }
      .ldes{
        width: 25%;
      }
      .lver{
        width: 12%;
      }
      .ledg{
        width: 12%;
      }
      .ltas{
        width: 15%;
      }
      .lfm{
        width: 13%;
      }
      
      
      .datasetli:nth-child(odd){
        background-color: rgb(246,222,212);
      }

      .datasetli:nth-child(even){
        background-color: rgb(226,175,144);
      }
    }

      .gun{
        position: fixed;
        top:9%;
        left: 10%;
        font-size: larger;
        cursor: pointer;
      }
    }

  .searchg1{
            
            float: left;
            width: 800px;
            height: 80%;
            left:18%;
            position: fixed;
            margin-left: 100px;
        input{
          //border-radius: 20px;
          position: relative;
          height: 30px;
          width: 200px;
          font-size: large;
          padding-left: 10px;
          float: left;
          //border: 0;
          top: 1%;
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
  
  .addBar{
    position: fixed;
    width: 600px;
    height: 400px;
    top: 25%;
    left: 50%;
    margin-left: -300px;
    background-color: aliceblue;
    z-index: 20;
    input{
      //display: block;
      width:200px;
      position: absolute;
      left: 50%;
      margin-left: -100px;
    }

    .upfile{
      top: 10%;
    }
    .req{
      top:20%;
      width: 40%;
      height: 10%;
    }

    .submitbtn{
      width:100px;
      position: absolute;
      left: 50%;
      margin-left: -50px;
      top:70%
    }
    
  }
  .closebtn{
    position: absolute;
    right: 0;
    border: 0ch;
    font-size: xx-large;
    background: transparent;
    cursor: pointer;


    

  }

  </style> 



