// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import '@/css/common_stlye.css'
import axios from 'axios'
import {getToken, setToken} from '@/util/auth'
import VueWechatTitle from 'vue-wechat-title'

import Vuex from 'vuex'
import store from './vuex/store'
import { codemirror } from 'vue-codemirror'
import 'codemirror/lib/codemirror.css'
import 'vue-awesome/icons'
import Icon from 'vue-awesome/components/Icon'

Vue.use(ElementUI)
Vue.use(axios)
Vue.use(Vuex)
Vue.use(codemirror)
Vue.use(VueWechatTitle)
Vue.component('icon', Icon)

axios.defaults.timeout = 10000
// 返回其他状态吗
// axios.defaults.validateStatus = function (status) {
//   return status >= 200 && status <= 500 // 默认的
// }
// 跨域请求，允许保存cookie
axios.defaults.withCredentials = true
// HTTPrequest拦截
axios.interceptors.request.use(config => {
  // config.headers['Authorization'] = `Basic ${Base64.encode(`${website.clientId}:${website.clientSecret}`)}`;
  // config.headers['Access-Control-Allow-Origin'] = '*' // 解决cors头问题
  // config.headers['Access-Control-Allow-Headers'] = 'X-Requested-With,Content-Type' // 解决cors头问题
  // config.headers['Access-Control-Allow-Methods'] = 'PUT,POST,GET,DELETE,OPTIONS' // 解决cors头问题
  config.headers['Content-Type'] = `application/json;charset=UTF-8`
  config.headers['Content-Encoding'] = 'gzip'
  config.headers['organizationId'] = store.state.organizationId
  // console.log(config.headers)

  console.log(config)
  if (getToken() != null && config.url !== '/api/users/login') {
    config.headers['authorization'] = getToken()
  }
  if (store.state.loading === false) {
    store.state.loading = true
  }

  return config
}, error => {
  console.log('request error =>' + error)
  return Promise.reject(error)
})

// http response 封装后台响应拦截器
axios.interceptors.response.use(
  response => {
    console.log(response)
    if (response.status === 200) {
      if (response.data.message && response.data.code === 0) {
        ElementUI.Message({
          showClose: true,
          message: response.data.message,
          type: 'success'
        })
      }

      if (response.data.message && response.data.code === -1) {
        ElementUI.Message({
          showClose: true,
          message: response.data.message,
          type: 'error'
        })
      }

      // console.log('interceptors =>')
      // console.log(response)

      if (response.headers['authorization'] !== null && response.headers['authorization'] !== undefined &&
      response.headers['authorization'] !== '') {
        setToken(response.headers['authorization'])
      }
      // console.log(response.headers['authorization'])
      store.state.loading = false

      return Promise.resolve(response)
    }
    // else {
    //   return Promise.reject(response)
    // }
  },
  error => {
    if (error.response.status) {
      console.log('error =>')
      console.log(error.response)
      switch (error.response.status) {
        // 400: 错误请求
        case 400:
          ElementUI.Message({
            showClose: true,
            message: '错误请求',
            type: 'error'
          })

          // 清除token
          // localStorage.removeItem('Authorization')
          // removeToken()
          // 跳转登录页面，并将要浏览的页面fullPath传过去，登录成功后跳转需要访问的页面
          // setTimeout(() => {
          //   router.replace({
          //     path: '/',
          //     query: {
          //       redirect: router.currentRoute.fullPath
          //     }
          //   })
          // }, 1000)

          break
        // 401: 未登录
        // 未登录则跳转登录页面，并携带当前页面的路径
        // 在登录成功后返回当前页面，这一步需要在登录页操作。
        case 401:
          ElementUI.Message({
            showClose: true,
            message: (error.response.data.message !== null?error.response.data.message:'验证失败，请重新登录'),
            type: 'error'
          })
          router.push({name: 'Login', params: {}})
          //router.go(0)
          break
        // 403 token过期
        // 登录过期对用户进行提示
        // 清除本地token和清空vuex中token对象
        // 跳转登录页面
        case 403:
          ElementUI.Message({
            showClose: true,
            message: '登录过期，请重新登录',
            type: 'error'
          })
          // 清除token
          // removeToken()
          // 跳转登录页面，并将要浏览的页面fullPath传过去，登录成功后跳转需要访问的页面
          // setTimeout(() => {
          //   router.replace({
          //     path: '/',
          //     query: {
          //       redirect: router.currentRoute.fullPath
          //     }
          //   })
          // }, 1000)
          router.push({name: 'Login', params: {}})
          // router.go(0)
          break
        // 404请求不存在
        case 404:
          ElementUI.Message({
            showClose: true,
            message: '网络请求不存在',
            type: 'error'
          })
          break
        // 其他错误，直接抛出错误提示
        case 500:
          ElementUI.Message({
            showClose: true,
            message: error.response.data.message,
            type: 'error'
          })
          break
        default:
          ElementUI.Message({
            showClose: true,
            message: error.response.data.message,
            type: 'error'
          })
          console.log(error)
      }
    }
    store.state.loading = false
    return Promise.reject(error)
  }
)
Vue.prototype.handlePageSizeChange = function (val) {
  store.state.pageSize = val
  localStorage.setItem('ecs-page-size', val)
  console.log('全局每页显示条数更改=>' + val)
  ElementUI.Message({
    showClose: true,
    message: '变更每页显示条数为 ' + val + '，请重新查询数据',
    type: 'success'
  })
}
// Vue.http.interceptors.push((request, next) => {
//   request.credentials = true
//   next()
// })
/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  axios,
  store,
  components: { App },
  template: '<App/>'
})
