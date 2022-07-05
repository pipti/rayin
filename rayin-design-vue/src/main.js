/*
 * @Description: 全局样式
 * @Date: 2022-03-09 16:42:16
 * @LastEditTime: 2022-03-29 16:36:04
 */
import Vue from 'vue';
import App from './App.vue';

import '@/assets/style/common_stlye.css';
import '@/assets/icons';

import VueWechatTitle from 'vue-wechat-title';
import VueClipboard from 'vue-clipboard2';
import { codemirror } from 'vue-codemirror';
import 'codemirror/lib/codemirror.css';
import {
  Collapse,
  CollapseItem,
  Select,
  Option,
  Input,
  Button,
  Message,
  MessageBox,
  Tooltip,
  Drawer,
  Container,
  Row,
  Col,
  Image,
  Divider,
  Main,
  Card,
  FormItem,
  Form,
  Dialog,
  Footer,
  Header,
  Link,
  Dropdown,
  Avatar,
  DropdownMenu,
  DropdownItem,
  Aside,
  Menu,
  Submenu,
  Carousel,
  CarouselItem,
  MenuItem,
  RadioGroup,
  RadioButton,
  Popover,
  Loading,
  TableColumn,
  Table,
  Pagination,
  PageHeader,
  Tag,
  Tabs,
  TabPane,
  ButtonGroup,
  InputNumber,
  Upload,
} from 'element-ui';

import 'element-ui/lib/theme-chalk/index.css';
import store from './vuex/store';
import router from './router';
import i18n from './locales/i18n';
import axios from 'axios';
import { isMobile } from '@/utils/utils';
import { getToken, setToken } from '@/utils/auth';


/**
 *  重写ElementUI的Message
 */
const showMessage = Symbol('showMessage');
class DonMessage {
  success(options, single = true) {
    this[showMessage]('success', options, single);
  }
  warning(options, single = true) {
    this[showMessage]('warning', options, single);
  }
  info(options, single = true) {
    this[showMessage]('info', options, single);
  }
  error(options, single = true) {
    this[showMessage]('error', options, single);
  }
  [showMessage](type, options) {
    Message[type](options);
  }
}

Vue.use(axios);
Vue.use(VueWechatTitle);
Vue.use(codemirror);
Vue.use(VueClipboard);
Vue.use(Collapse);
Vue.use(CollapseItem);
Vue.use(Select);
Vue.use(Option);
Vue.use(Input);
Vue.use(Button);
Vue.use(Tooltip);
Vue.use(Drawer);
Vue.use(Container);

Vue.use(Row);
Vue.use(Col);
Vue.use(Image);
Vue.use(Divider);
Vue.use(Main);
Vue.use(Card);
Vue.use(FormItem);
Vue.use(Form);
Vue.use(Dialog);
Vue.use(Footer);

Vue.use(Header);
Vue.use(Link);
Vue.use(Dropdown);
Vue.use(Avatar);
Vue.use(DropdownMenu);
Vue.use(DropdownItem);
Vue.use(Aside);
Vue.use(Menu);
Vue.use(Submenu);
Vue.use(Carousel);
Vue.use(CarouselItem);
Vue.use(MenuItem);
Vue.use(RadioGroup);
Vue.use(RadioButton);
Vue.use(Popover);
Vue.use(Loading);
Vue.use(TableColumn);
Vue.use(Table);
Vue.use(Pagination);
Vue.use(PageHeader);
Vue.use(Tag);
Vue.use(Tabs);
Vue.use(TabPane);
Vue.use(ButtonGroup);
Vue.use(InputNumber);
Vue.use(Upload);

axios.defaults.timeout = 30000;
axios.defaults.withCredentials = true;
Vue.prototype.$alert = MessageBox.alert;
Vue.prototype.$message = new DonMessage();
Vue.prototype.$isMobile = isMobile;

Vue.config.productionTip = false;

document.title = i18n.t('title');

// HTTPrequest拦截
axios.interceptors.request.use((config) => {
  // config.headers['Authorization'] = `Basic ${Base64.encode(`${website.clientId}:${website.clientSecret}`)}`;
  // config.headers['Access-Control-Allow-Origin'] = '*' // 解决cors头问题
  // config.headers['Access-Control-Allow-Headers'] = 'X-Requested-With,Content-Type' // 解决cors头问题
  // config.headers['Access-Control-Allow-Methods'] = 'PUT,POST,GET,DELETE,OPTIONS' // 解决cors头问题
  config.headers['Content-Type'] = 'application/json;charset=UTF-8';
  config.headers['Content-Encoding'] = 'gzip';
  config.headers.organizationId = store.state.organizationId;
  // console.log(config.headers)

  if (getToken() != null && config.url !== '/api/users/login') {
    config.headers.authorization = getToken();
  }
  if (store.state.loading === false) {
    store.state.loading = true;
  }

  return config;
}, (error) => {
  console.log(`request error =>${error}`);
  return Promise.reject(error);
});

// http response 封装后台响应拦截器
axios.interceptors.response.use(
  (response) => {
    console.log(response);
    if (response.status === 200) {
      if (response.data.message && response.data.code === 0) {
        Message({
          showClose: true,
          message: response.data.message,
          type: 'success',
        });

        return;
      }

      if (response.data.message && (response.data.code === -1 || response.data.code === 99999)) {
        Message({
          showClose: true,
          message: response.data.message,
          type: 'error',
        });
        return;
      }

      // console.log('interceptors =>')
      // console.log(response)

      if (response.headers.authorization !== null && response.headers.authorization !== undefined
        && response.headers.authorization !== '') {
        setToken(response.headers.authorization);
      }
      // console.log(response.headers['authorization'])
      store.state.loading = false;

      return Promise.resolve(response);
    }
    // else {
    //   return Promise.reject(response)
    // }
  },
  (error) => {
    if (error.response.status) {
      console.log('error =>');
      console.log(error.response);
      switch (error.response.status) {
        // 400: 错误请求
        case 400:
          Message({
            showClose: true,
            message: '错误请求',
            type: 'error',
          });

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

          break;
        // 401: 未登录
        // 未登录则跳转登录页面，并携带当前页面的路径
        // 在登录成功后返回当前页面，这一步需要在登录页操作。
        case 401:
          Message({
            showClose: true,
            message: (error.response.data.message !== null ? error.response.data.message : '验证失败，请重新登录'),
            type: 'error',
          });
          router.push({ name: 'Login', params: {} });
          // router.go(0)
          break;
        // 403 token过期
        // 登录过期对用户进行提示
        // 清除本地token和清空vuex中token对象
        // 跳转登录页面
        case 403:
          Message({
            showClose: true,
            message: '登录过期，请重新登录',
            type: 'error',
          });
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
          router.push({ name: 'Login', params: {} });
          // router.go(0)
          break;
        // 404请求不存在
        case 404:
          Message({
            showClose: true,
            message: '网络请求不存在',
            type: 'error',
          });
          break;
        // 其他错误，直接抛出错误提示
        case 500:
          Message({
            showClose: true,
            message: error.response.data.message,
            type: 'error',
          });
          break;
        // 504请求不存在
        case 504:
          Message({
            showClose: true,
            message: '服务不可用，请联系系统管理员！',
            type: 'error',
          });
          break;
        default:
          Message({
            showClose: true,
            message: error.response.data.message,
            type: 'error',
          });
          console.log(error);
      }
    }
    store.state.loading = false;
    return Promise.reject(error);
  },
);
Vue.prototype.handlePageSizeChange = function (val) {
  store.state.pageSize = val;
  localStorage.setItem('ecs-page-size', val);
  console.log(`全局每页显示条数更改=>${val}`);
  Message({
    showClose: true,
    message: `变更每页显示条数为 ${val}，请重新查询数据`,
    type: 'success',
  });
};

new Vue({
  el: '#app',
  router,
  i18n,
  axios,
  store,
  render: h => h(App),
}).$mount('#app');

