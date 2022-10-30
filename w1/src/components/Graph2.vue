<template>
    <div id="graphScreen">
      <!-- <p>{{data.search}}</p> -->
      <!-- <button @click="RecieveData">我长这么长我就不信我接收</button> -->
      <div id="GS"></div>
    </div>
</template>
  


  <script setup>
  // import Request from '@/utils/request.js'
  import axios from 'axios';
    import { reactive, defineExpose ,watch} from "vue";
    import { onMounted, onUpdated, onRenderTriggered } from 'vue'
    var side = [
            {v1:1,v2:12},
            {v1:1,v2:24},
            {v1:1,v2:2},
            {v1:0,v2:1},
            {v1:0,v2:2},
            {v1:0,v2:3},
            {v1:0,v2:4},
            {v1:0,v2:5},
            {v1:5,v2:0}]
    const ajaxcreated = () =>{
      var tableData = "789";
    async function getData () {
      try {
        tableData = (await axios.get('http://localhost:8200/algo/queryDatasetById?id=383')).data
        console.log("success",tableData.data)
        side = tableData.data
        
      } catch (error) {
        console.error("error",error);
        alert("查询失败，请检查连接")
      }
      finally{
        console.log(side)
      }
    }

    getData()
    
  }
    ajaxcreated();

    
    const data = reactive({
        search:"sno",

    })
    const Receive = (val) => {
        data.search = val;
    };
    defineExpose({ Receive });
    var i =1;
    // const RecieveData = ()=>{
    //   console.log(34562)
    //   var link = 'algo/queryDatasetById?id=383';
    //     Request({
    //       url:link,
    //       method:'get',

    //     }).then(result=>{
    //       console.log(result);
    //       // i = result;
    //     })
    //   }
    
    // const sides = reactive({
    //     side:[{v1:1,v2:2},
    //         {v1:1,v2:2},
    //         {v1:1,v2:2},
    //         {v1:0,v2:1},
    //         {v1:0,v2:2},
    //         {v1:0,v2:3},
    //         {v1:0,v2:4},
    //         {v1:0,v2:5},
    //         {v1:5,v2:0},
    //       ]
    // })

    
    const nodes = reactive({
        node:[]
    })

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
    watch(data, 
    (curr, old) => {
            console.log("changed:000",curr, old,i);
            if(stage!=null){
              clearScreen();
            }
            stage = new jtopo.Stage('GS');
          // var stage = this.Stage;
            layer = new jtopo.Layer('default');
            var src = (data.search)*1;
            var ifexist = 0 
            var sides = side;
            // console.log("typeof1",typeof(src))
            // console.log("typeof2",typeof(sides[0].v1))
            // console.log("typeof3",typeof(src*1))
            console.log("sides:",sides)
            var nodes = []
            console.log(src)
            nodes.push(src)
            for(let i in sides){
                // if (sides[i].v1==src||sides[i].v2==src){
                //     console.log(sides[i].v1,"<=>",sides[i].v2);
                // }
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
                alert("未查询到该点");  
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
                rootNode.selectedStyle = new jtopo.Style({
                    'font': 'bold 20px 仿宋'
                });
                layer.addChild(rootNode);
                for(let i in nodes){
                    if(i==0)continue;
                    // var sjs1=Math.round(Math.random()*1000)
                    // var sjs2=Math.round(Math.random()*1000)
                    // NodeMap.set(nodes[i],new jtopo.CircleNode(nodes[i].toString(), sjs1, sjs2, 36, 36));
                    
                    // var link = new jtopo.Link('Link',rootNode,NodeMap[nodes[i]]);
                    // layer.addChild(link);
                    // NodeMap[nodes[i]].setStyles({
                    //     textPosition: "center",
                    //     textAlign: "center",
                    //     textBaseline: "middle",
                    //     fontColor: 'black',
                    //     fillStyle:"white"
                    // })
                    // NodeMap[nodes[i]].selectedStyle = new jtopo.Style({
                    //     'font': 'bold 20px 仿宋'
                    // });
                    // //NodeMap.set(nodes[i],newNode);
                    // layer.addChild(NodeMap[nodes[i]]);
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
                    newNode.selectedStyle = new jtopo.Style({
                        'font': 'bold 20px 仿宋'
                    });
                    NodeMap.set(nodes[i],newNode);
                    layer.addChild(newNode);
                }
                console.log(NodeMap)
                // for(let i in sides){
                //   if (nodes.indexOf(sides[i].v1)!=-1 &&nodes.indexOf(sides[i].v2)!=-1){
                //     var link = new jtopo.Link('Link',NodeMap[sides[i].v1],NodeMap[sides[i].v2]);
                //     layer.addChild(link);
                // }
                // }

                stage.hideToolbar();
                stage.show();
            }
            

  },
  )


  </script>
  <style lang="less">
  #graphScreen{
    margin: 0;
    // float: left;
    width: 86.5%;
    position: relative;
    left:13.5% ;
  }
  #GS{
    margin: 0;
    position: relative;
    left: 0;
    // display: flex;
    // float: left;
    width: 1600px;
    height: 800px;
    // margin-top: 50px;
    
    canvas{
      position: relative;
      left: 0;
      z-index: -1;
    }
    // background-color: rgb(199, 227, 252);
}
  </style> 



