import Vue from 'vue';
import VueRouter from 'vue-router';
// 登录
// import Login from '@/page/login/login';
// import PhoneLogin from '@/page/login/phone-login';
// import Signup from '@/page/signup/signup';

import MainPage from '@/page/main';
import Guide from '@/page/guide/guide';
import My from '@/page/my/my';
import MyOrgs from '@/page/my/orgs/my-orgs';
import MyInfo from '@/page/my/info/my-info';

// 模板设计
import ElementDesign from '@/page/element-management/element-design';
import ElementManagement from '@/page/element-management/element-management';
import TemplateManagement from '@/page/element-management/template-management';
import TemplateSet from '@/page/element-management/template-set';
import TemplateTest from '@/page/element-management/template-test';
import TemmplateApiTest from '@/page/element-management/template-api-test';

// 凭证管理
import PDFQuery from '@/page/pdf-management/pdf-query';

// import Home from '@/views/Home.vue';
import VueResource from 'vue-resource';
Vue.use(VueRouter);
Vue.use(VueResource);

const routes = [
  {
    path: '/',
    name: 'Login',
    component: resolve => require(['@/page/login/login'], resolve),
    meta: {
      title: '睿印-登录'
    }
  },
  {
    path: '/phone-login',
    name: 'PhoneLogin',
    component: resolve => require(['@/page/login/phone-login'], resolve),
    meta: {
      title: '睿印-手机登录'
    }
  },
  {
    path: '/signup',
    name: 'Signup',
    component: resolve => require(['@/page/signup/signup'], resolve),
    meta: {
      title: '睿印-注册'
    }
  },
  {
    path: '/my',
    name: 'My',
    component: My,
    children: [
      {
        path: '/my/info',
        name: 'MyInfo',
        component: MyInfo
      }, {
        path: '/my/account',
        name: 'MyAccount',
        component: Guide
      }, {
        path: '/my/security',
        name: 'MySecurity',
        component: Guide
      }, {
        path: '/my/apps',
        name: 'MyApps',
        component: Guide
      }, {
        path: '/my/orgs',
        name: 'MyOrgs',
        component: MyOrgs,
        meta: {
          title: '睿印-我的项目'
        }
      }
    ]
  },
  {
    path: '/main',
    name: 'mainPage',
    component: MainPage,
    children: [
      {
        path: '/main/guide',
        name: 'Guide',
        component: Guide,
        meta: {
          title: '睿印-首页'
        }
      },
      {
        path: '/main/element-design',
        name: 'ElementDesign',
        component: ElementDesign,
        meta: {
          title: '睿印-构件设计'
        }
      },
      {
        path: '/main/template-management',
        name: 'TemplateManagement',
        component: TemplateManagement,
        meta: {
          title: '睿印-模板管理'
        }
      },
      {
        path: '/main/element-management',
        name: 'ElementManagement',
        component: ElementManagement,
        meta: {
          title: '睿印-构件管理'
        }
      },
      {
        path: '/main/template-set',
        name: 'TemplateSet',
        component: TemplateSet
      },
      {
        path: '/main/template-test',
        name: 'TemplateTest',
        component: TemplateTest,
        meta: {
          title: '睿印-模板测试'
        }
      },
      {
        path: '/main/template-api-test',
        name: ' TemmplateApiTest',
        component: TemmplateApiTest,
        meta: {
          title: '睿印-模板接口测试'
        }
      },
      {
        path: '/main/pdf-query',
        name: 'PDFQuery',
        component: PDFQuery
      },
    ]
  }
  // {
  //   path: '/',
  //   name: 'Home',
  //   component: Home,
  // },
  // {
  //   path: '/invite',
  //   name: 'Invite',
  //   // route level code-splitting
  //   // this generates a separate chunk (about.[hash].js) for this route
  //   // which is lazy-loaded when the route is visited.
  //   component: () => import(/* webpackChunkName: "invite" */ '../views/Invite.vue'),
  // },
  // {
  //   path: '/videoCall',
  //   name: 'videoCall',
  //   // route level code-splitting
  //   // this generates a separate chunk (about.[hash].js) for this route
  //   // which is lazy-loaded when the route is visited.
  //   component: () => import(/* webpackChunkName: "invite" */ '../views/videoCall.vue'),
  // },
  // {
  //   path: '/room',
  //   name: 'room',
  //   // route level code-splitting
  //   // this generates a separate chunk (about.[hash].js) for this route
  //   // which is lazy-loaded when the route is visited.
  //   component: () => import(/* webpackChunkName: "invite" */ '../views/room.vue'),
  // },
];

const router = new VueRouter({
  routes,
});


export default router;
