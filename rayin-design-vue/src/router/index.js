import Vue from 'vue'
import Router from 'vue-router'
import Main from '@/page/main'

import Guide from '@/page/guide/guide'
// 模板设计
import ElementDesign from '@/page/element-management/element-design'
import ElementManagement from '@/page/element-management/element-management'
import TemplateManagement from '@/page/element-management/template-management'
import TemplateSet from '@/page/element-management/template-set'
import TemplateTest from '@/page/element-management/template-test'
import TemmplateApiTest from '@/page/element-management/template-api-test'

// 凭证管理
import PDFQuery from '@/page/pdf-management/pdf-query'
import PDFCheck from '@/page/pdf-management/pdf-check'

// 参数管理
import ParamterElementType from '@/page/parameter-management/paramter-element-type'
import ParamterQualityCheck from '@/page/parameter-management/paramter-quality-check'
import ParamterQuery from '@/page/parameter-management/paramter-query'
import ParamterUncommchars from '@/page/parameter-management/paramter-uncommchars'
import ParamterTemplateRule from '@/page/parameter-management/paramter-template-rule'
import ParamterInternation from '@/page/parameter-management/paramter-internation'

// 登录
import Login from '@/page/login/login'
import PhoneLogin from '@/page/login/phone-login'
import Signup from '@/page/signup/signup'
//
import My from '@/page/my/my'
import MyOrgs from '@/page/my/orgs/my-orgs'
import MyInfo from '@/page/my/info/my-info'

import OrgMember from '@/page/my/member/org-member'
import OrgIndex from '@/page/my/orgs/org-index'
//
import DepositCheck from '@/page/pdf-management/deposit-check.vue'

import VueResource from 'vue-resource'

Vue.use(Router)
Vue.use(VueResource)

export default new Router({
  routes: [
    {
      path: '/main',
      name: 'main',
      component: Main,
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
        {
          path: '/main/pdf-check',
          name: 'PDFCheck',
          component: PDFCheck
        },
        {
          path: '/main/paramter-quality-check',
          name: 'ParamterQualityCheck',
          component: ParamterQualityCheck
        },
        {
          path: '/main/paramter-element-type',
          name: 'ParamterElementType',
          component: ParamterElementType
        },
        {
          path: '/main/paramter-query',
          name: 'ParamterQuery',
          component: ParamterQuery
        },
        {
          path: '/main/paramter-uncommchars',
          name: 'ParamterUncommchars',
          component: ParamterUncommchars
        },
        {
          path: '/main/paramter-template-rule',
          name: 'ParamterTemplateRule',
          component: ParamterTemplateRule
        },
        {
          path: '/main/paramter-internation',
          name: 'ParamterInternation',
          component: ParamterInternation
        },
        {
          path: '/my/org-member',
          name: 'OrgMember',
          component: OrgMember,
          meta: {
            title: '睿印-项目成员'
          }
        },
        {
          path: '/my/org-index',
          name: 'OrgIndex',
          component: OrgIndex,
          meta: {
            title: '睿印-项目索引'
          }
        },
        {
          path: '/pdf/deposit-check',
          name: 'DepositCheck',
          component: DepositCheck,
          meta: {
            title: '睿印-项目管理'
          }
        }
      ]
    },
    {
      path: '/',
      name: 'Login',
      component: Login,
      meta: {
        title: '睿印-登录'
      }
    },
    {
      path: '/phone-login',
      name: 'PhoneLogin',
      component: PhoneLogin,
      meta: {
        title: '睿印-手机登录'
      }
    },
    {
      path: '/signup',
      name: 'Signup',
      component: Signup,
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
    }
  ]
})
