<template>
    <div id="topbar">
        <div class="logo">
            <h2>湖南大学 x 之江实验室</h2>
            <h2>查询图关系使用手册</h2>
        </div>
        <div class="g1">
            <input  type="text" v-model="search" placeholder=" 请输入想要查询的点">
            <button class="btn change3 searchbtn" @click="doSearch"><span>GO</span></button>
            <button class="btn change3 clearbtn" @click="clearScreen"><span>clear</span></button>
            
        </div>
        <button @click="send">send</button>
    </div>
  </template>
  

  <script>
  import emitter from '@/utils/bus.js'
  import { reactive, defineEmits } from "vue";
  
  export default {
    
    props: {
      
  },
      data(){
          return{
            nodes:[100,100,100],
            search:"",
            sides:[
            {v1:1,v2:2},
            {v1:1,v2:2},
            {v1:1,v2:2},
            {v1:0,v2:1},
            {v1:0,v2:2},
            {v1:0,v2:3},
            {v1:0,v2:4},
            {v1:0,v2:5},
            {v1:0,v2:6},
            {v1:0,v2:7},
            {v1:0,v2:8},
            {v1:0,v2:13},
            {v1:0,v2:14},
            {v1:0,v2:16},
            {v1:0,v2:23},
            {v1:0,v2:28},
            {v1:0,v2:31},
            {v1:0,v2:44},
            {v1:0,v2:66},
            {v1:0,v2:87},
            {v1:0,v2:88},
            {v1:0,v2:126},
            {v1:0,v2:160},
            {v1:0,v2:183},
            {v1:0,v2:195},
            {v1:0,v2:203},
            {v1:0,v2:210},
            {v1:0,v2:215},
            {v1:0,v2:234},
            {v1:0,v2:249},
            {v1:0,v2:257},
            {v1:0,v2:280},
            {v1:0,v2:283},
            {v1:0,v2:341},
            {v1:0,v2:374},
            {v1:0,v2:442},
            {v1:0,v2:482},
            {v1:0,v2:508},
            {v1:0,v2:547},
            {v1:0,v2:571},
            {v1:0,v2:589},
            {v1:0,v2:610},
            {v1:0,v2:621},
            {v1:0,v2:633},
            {v1:0,v2:659},
            {v1:0,v2:669},
            {v1:0,v2:749},
            {v1:0,v2:862},
            {v1:0,v2:897},
            {v1:0,v2:909},
            {v1:0,v2:973},
            {v1:0,v2:975},
            {v1:1,v2:0},
            {v1:1,v2:4},
            {v1:1,v2:9},
            {v1:1,v2:10},
            {v1:1,v2:16},
            {v1:1,v2:17},
            {v1:1,v2:20},
            {v1:1,v2:21},
            {v1:1,v2:25},
            {v1:1,v2:26},
            {v1:1,v2:27},
            {v1:1,v2:29},
            {v1:1,v2:34},
            {v1:1,v2:35},
            {v1:1,v2:37},
            {v1:1,v2:42},
            {v1:1,v2:48},
            {v1:1,v2:58},
            {v1:1,v2:63},
            {v1:1,v2:67},
            {v1:1,v2:68},
            {v1:1,v2:73},
            {v1:1,v2:75},
            {v1:1,v2:83},
            {v1:1,v2:105},
            {v1:1,v2:106},
            {v1:1,v2:131},
            {v1:1,v2:134},
            {v1:1,v2:138},
            {v1:1,v2:141},
            {v1:1,v2:165},
            {v1:1,v2:173},
            {v1:1,v2:182},
            {v1:1,v2:186},
            {v1:1,v2:189},
            {v1:1,v2:225},
            {v1:1,v2:264},
            {v1:1,v2:300}
            ],
            sendTest:1011
          }
      },
      methods:{
        unique:function(arr){
            console.log("unique")
            return Array.from(new Set(arr))
        },

        send:function(){
            emitter.emit("share",this.sendTest)
        },
         
        doSearch:function(e){
            var ifexist = 0 
            console.log(this.search)
            var src = this.search;
            let sides = this.sides;
            this.nodes = []
            this.nodes.push(src-0)
            for(let i in sides){
                if (sides[i].v1==src){
                    this.nodes.push(sides[i].v2);
                    ifexist=1;
                }
                if (sides[i].v2==src){
                    this.nodes.push(sides[i].v1);
                    ifexist=1;
                }
            }
            
            if(!ifexist){
                alert("未查询到该点");  
            }
            else{
                this.nodes = this.unique(this.nodes);
                console.log("nodes",this.nodes)
                var nodes = this.nodes;
                
                stage.addChild(layer);
                
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
                    layer.addChild(newNode);
                }
                stage.hideToolbar();
                stage.show();
            }
            
        },
        clearScreen:function(e){
            layer.removeAllChild();
            layer.update();
        }
    }
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
                left: 62%;
            }

            .clearbtn{
                position: absolute;
                top: 20%;
                left: 70%;
            }
                
        }

        
    }
  </style> 



