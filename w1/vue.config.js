const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  lintOnSave:false, 
  transpileDependencies: true,
  devServer:{
    proxy:{
      '/dataset':{//表示拦截以/api开头的请求路径
        target:'http://localhost:8199/dataset/selectDataset',
        changOrigin: true,//是否开启跨域
        pathRewrite:{
          '^/dataset':'' //重写api，把api变成空字符，因为我们真正请求的路径是没有api的
        }
      }
    }
  }

})
