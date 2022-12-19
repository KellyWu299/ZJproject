<template>
  <TB2></TB2>
  <SideBar @sendgid="Sendgid"></SideBar>
  <G2 ref="searchToGraph"></G2>
</template>

<script setup>

import TB2 from "@/components/TopBar2.vue"
import G2 from "@/components/Graph2.vue"
import SideBar from "@/components/SideBar.vue"
import { ref } from "vue";
 

const searchToGraph = ref(null);

const Sendgid = (val) => {
  searchToGraph.value.ReceiveGraphid(val);
};
</script>

<style lang="less">
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}


li{
    list-style: none;
}
*{
    padding: 0;
    margin: 0;
}
:root,body{
    height: 100%;
}

.btn{
	background-color:rgba(0,180,100,1);
	border:rgba(0,180,100,1) 1px solid;
	outline:none;  /*去除点击时的蓝色边框*/
	cursor:pointer;
	padding:14px 10px;
	color:rgba(255,255,255,1);
	text-align: center;
	font-size: 16px;
	margin:10px;
}
.disable{
	background-color:rgba(0,180,100,0.5);
	cursor:not-allowed;
}
.shadow{
	box-shadow:0 5px 13px 0 rgba(0,0,0,0.3);
}
.cir1{
	border-radius:10px;
}
.cir2{
	padding:13px 25px;
	border-radius:30px;
	box-shadow:0 3px 10px 0 rgba(0,0,0,0.3);
	background: -webkit-linear-gradient(bottom,  rgba(0,180,100,0.2),  rgba(0,180,100,1), rgba(0,200,100,1) ); /* Safari 5.1 - 6.0 */
    background: -o-linear-gradient(bottom,  rgba(0,180,100,0.2),  rgba(0,180,100,1), rgba(0,200,100,1) ); /* Opera 11.1 - 12.0 */
    background: -moz-linear-gradient(bottom,  rgba(0,180,100,0.2),  rgba(0,180,100,1), rgba(0,200,100,1) ); /* Firefox 3.6 - 15 */
    background: linear-gradient(to bottom,  rgba(0,180,100,0.2),  rgba(0,180,100,1), rgba(0,200,100,1) ); /* 标准的语法 */
}
 
 
/*动态效果*/
.change{
	 transition: all 0.5s;
    -webkit-transition: all 0.5s; /* Safari */
}
.change1:hover{
	background-color:rgba(0,180,100,0.1);
	color:rgb(0,0,0);	
}
.change2:hover{
	box-shadow:0 5px 13px 0 rgba(0,0,0,0.3);
}
.change3{
	background-color:rgba(255,100,0,1);
	border:rgba(255,120,0,1) 1px solid;
	border-radius:30px;
	width:110px;
}
.change3 span{
  transition: 0.6s;
}
.change3 span:after {
  content: ' »';
  opacity: 0;
  top: 0;
  right: -15px;
  transition: 0.6s;
}
.change3:hover span{
	padding-right:20px;
	transition: 0.6s;
}
.change3:hover span:after {
  opacity: 1;
  right: 0;
}
.change4{
	width:100px;
	border-radius:30px;
	background-image: -webkit-linear-gradient(10deg,#3CFA14,#F0FA00,#98FB98,#3CFA14);
	background-size: 200% 100%;
	animation:move 3s infinite linear;
	-webkit-animation:move 3s infinite linear; /* Safari 与 Chrome */
}
@keyframes move{
	0% {
    background-position: 0 0;
	}
    100% {
    background-position: -200% 0;
	}
}

.v-move {
    transition: 2s;
}

.pointer{
	cursor: pointer;
}
</style>
