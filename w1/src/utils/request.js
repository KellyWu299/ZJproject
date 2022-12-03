import axios from 'axios'
// import router from '@/router/routers'
// import { Notification, MessageBox } from 'element-ui'
// import store from '../store'
import { getToken } from '@/utils/auth'
import Config from '@/settings'

// 创建axios实例



const service = axios.create({
  baseURL:'dataset',//把原来的项目地址，改成api，解决跨域问题
  timeout:3000,
  // baseURL: process.env.NODE_ENV === 'production' ? process.env.VUE_APP_BASE_API : '/', 
  devServer: {
  //是否自动打开浏览器
  open: true,
  //修改默认8080端口号
  port: 8081,
  //跨域,设置后端接口入口地址，/cxk/为代理地址必须有，为后端地址192.168.99.54，7676服务端口号
  proxy: {
    '/algo/':{
      target: 'http://localhost:8200',
      changeOrigin: true
    }
  }
},


  baseURL:'http://localhost:8200',
  timeout: Config.timeout ,// 请求超时时间
  withCredentials: true
})

// request拦截器
service.interceptors.request.use(
  config => {
    if (getToken()) {
      config.headers['Authorization'] = getToken() // 让每个请求携带自定义token 请根据实际情况自行修改
    }
    config.headers['Content-Type'] = 'application/json; charset=utf-8'
    return config
  },
  error => {
    // Do something with request error
    console.log(error) // for debug
    Promise.reject(error)
  }
)

// response 拦截器
service.interceptors.response.use(
  response => {
    const code = response.status
    if (code < 200 || code > 300) {
      // Notification.error({
      //   title: response.message
      // })
      return Promise.reject('error')
    } else {
      return response.data
    }
  },
  error => {
    console.log(error.response);
    let code = 0
    try {
      code = error.response.data.status
    } catch (e) {
      if (error.toString().indexOf('Error: timeout') !== -1) {
        // Notification.error({
        //   title: '网络请求超时',
        //   duration: 5000
        // })
        return Promise.reject(error)
      }
    }
    if(error.response.status==500){
      layer.alert("系统服务忙，请稍后重试！", {icon: 2});
    }
    if(error.response.status==444){
      layer.alert("您输入的【"+error.response.data+"】包含特殊字符请重新输入！", {icon: 5});
    }
    if (code) {
      if (code === 401) {
        // MessageBox.confirm(
        //   '登录状态已过期，您可以继续留在该页面，或者重新登录',
        //   '系统提示',
        //   {
        //     confirmButtonText: '重新登录',
        //     cancelButtonText: '取消',
        //     type: 'warning'
        //   }
        // ).then(() => {
        //   store.dispatch('LogOut').then(() => {
        //     location.reload() // 为了重新实例化vue-router对象 避免bug
        //   })
        // })
      } else if (code === 403) {
        router.push({ path: '/401' })
      } else {
        const errorMsg = error.response.data.message
        if (errorMsg !== undefined) {
          // Notification.error({
          //   title: errorMsg,
          //   duration: 5000
          // })
        }
      }
    } else {
      // Notification.error({
      //   title: '接口请求失败',
      //   duration: 5000
      // })
    }
    return Promise.reject(error)
  }
)
export default service
