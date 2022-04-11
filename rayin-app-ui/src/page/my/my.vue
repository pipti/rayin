<template>
  <el-container>
    <el-header style="padding:0 0;height:45px;border-bottom:solid 1px #e6e6e6;">
      <el-row>
        <el-col style="width: 50px;">
          <el-button type="primary" :icon="collapseIcon" style="border-radius: 0px;padding:6px 10px;font-size: 25px" @click="collapseStatus"></el-button>
        </el-col>
        <el-col :span="4">
          <div style="width:100%;text-align: left">
            <img src="/static/images/logo5.png" style="width:200px;">
          </div>
        </el-col>

        <el-col :span="8">
          <div style="width: 100%;text-align: left">
            <el-link :underline="false"  style="margin-top: 15px;font-size:22px;margin-right:30px" type="primary" @click="titleRouterClick('orgs')">项目</el-link>

            <!--<el-link :underline="false" style="margin-top: 15px;font-size:22px;margin-right:30px" type="primary" v-if="this.$store.state.organizationName !== null" @click="titleRouterClick('guide')">设计</el-link>-->
            <!--<el-link :underline="false"  style="margin-top: 15px;font-size:22px;margin-right:30px" type="primary" v-if="this.$store.state.organizationName !== null" @click="titleRouterClick('members')">成员</el-link>-->
          </div>
        </el-col>
        <el-col :span="7">
          <div style="width:100%;text-align:right;margin-top:10px;" >
            <!-- 项目列表 -->
            <!--<el-dropdown trigger="click" @command="handleCommand">-->
            <!--<span class="el-dropdown-link">-->
              <div style="border-left:5px solid #409EFF;padding-left: 10px;height:30px;padding-bottom:3px;text-align: left;"
                   v-if="this.$store.state.organizationName !== null">
                <el-link style="font-size:25px;"
                         type="primary" :underline="false" >
                <!--{{this.$store.state.organizationName}}-->
                  <div v-if="this.$store.state.organizationName !== null">{{ this.$store.state.organizationName.substring(0,10) }}
                    <span v-if="this.$store.state.organizationName.length > 10">...</span>
                  </div>
                </el-link>

              </div>
            <!--</span>-->
              <!--<el-dropdown-menu slot="dropdown">-->
                <!--<el-dropdown-item>-->
                  <!--<el-button @click="toCreateOrg()" type="text" size="big">创建项目</el-button>-->
                <!--</el-dropdown-item>-->
                <!--<el-dropdown-item v-for="(item,index) in orgs" :key='index'><span>{{item.organizationName}}</span>-->
                <!--</el-dropdown-item>-->
              <!--</el-dropdown-menu>-->
            <!--</el-dropdown>-->
          </div>
        </el-col>
        <el-col :span="4">
          <div style="width:100%;text-align: right;">
            <el-link  style="margin-bottom: 10px;margin-right: 10px" @click="titleRouterClick('main')">首页</el-link>
            <el-link style="margin-bottom: 10px">帮助</el-link>
            <el-dropdown trigger="click" @command="handleCommand">
            <span class="el-dropdown-link">
              <el-avatar src="/static/images/face1.jpg"></el-avatar>
              <!--<i class="el-icon-arrow-down el-icon&#45;&#45;right"></i>-->
            </span>
              <el-dropdown-menu slot="dropdown">
                <!--<el-dropdown-item>-->
                <!--<el-button @click="toCreateOrg()" type="text" size="big">创建项目</el-button>-->
                <!--<el-button @click="changeOrgClick()" type="text" size="big">切换项目</el-button>-->
                <!--</el-dropdown-item>-->
                <!--<el-dropdown-item v-for="(item,index) in orgs" :key='index'><span>{{item.organizationName}}</span>-->
                <!--</el-dropdown-item>-->
                <!--<el-divider style="margin: 0"></el-divider>-->
                <el-dropdown-item command="toMy" disabled><span>个人设置</span></el-dropdown-item>
                <el-dropdown-item disabled>信息中心</el-dropdown-item>
                <el-dropdown-item command="logout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </div>
        </el-col>
      </el-row>
    </el-header>
    <el-container>
      <el-row style="margin-left: 1%;padding: 5px">
        <!--<el-card style="border-radius: 0px;">-->
          <!--<el-row>-->
            <!--<el-col :span="12">-->
              <!--<el-image src="/static/images/tym.jpg"></el-image>-->
            <!--</el-col>-->
            <!--<el-col :span="12">-->
              <!--<h1><span>唐永茂</span></h1>-->
            <!--</el-col>-->
            <!--<el-col :span="10">-->
  <!--&lt;!&ndash;            <el-popover trigger="hover" placement="top">&ndash;&gt;-->
  <!--&lt;!&ndash;                <span>13674262857</span>&ndash;&gt;-->
  <!--&lt;!&ndash;            </el-popover>&ndash;&gt;-->
                <!--<el-popover trigger="hover" placement="top">-->
                  <!--<p>13674262857</p>-->
                  <!--<div slot="reference" class="name-wrapper">-->
                    <!--<span>13674262857</span>-->
                  <!--</div>-->
                <!--</el-popover>-->
            <!--</el-col>-->
          <!--</el-row>-->
        <!--</el-card>-->
        <!--<el-card style="margin-top:20px;border-radius: 0px;">-->
  <!--&lt;!&ndash;      <el-aside width="auto" style="border-right:solid 1px #e6e6e6">&ndash;&gt;-->
          <!--<el-menu-->
            <!--default-active="1-1"-->
            <!--class="el-menu-vertical-demo "-->
            <!--router-->
            <!--:collapse="isCollapse"-->
          <!--style="border-right: 0px;">-->
  <!--&lt;!&ndash;          <el-submenu index="1">&ndash;&gt;-->
              <!--<el-menu-item index="1-1" route="/my/info">-->
<!--&lt;!&ndash;                <i class="el-icon-help"></i>&ndash;&gt;-->
                <!--<span class="my-menu-font">个人信息</span>-->
              <!--</el-menu-item>-->
              <!--<el-menu-item index="1-2" route="/my/account">-->
<!--&lt;!&ndash;                <i class="el-icon-picture-outline"></i>&ndash;&gt;-->
                <!--<span>账号密码</span>-->
              <!--</el-menu-item>-->
              <!--<el-menu-item index="1-3" route="/my/security">-->
<!--&lt;!&ndash;                <i class="el-icon-document"></i>&ndash;&gt;-->
                <!--<span>安全设置</span>-->
              <!--</el-menu-item>-->
              <!--<el-menu-item index="1-4" route="/my/apps">-->
<!--&lt;!&ndash;                <i class="el-icon-document-copy"></i>&ndash;&gt;-->
                <!--<span>应用管理</span>-->
              <!--</el-menu-item>-->
              <!--<el-menu-item index="1-5" route="/my/orgs">-->
<!--&lt;!&ndash;                <i class="el-icon-document-copy"></i>&ndash;&gt;-->
                <!--<span>已加入组织</span>-->
              <!--</el-menu-item>-->
  <!--&lt;!&ndash;          </el-submenu>&ndash;&gt;-->
          <!--</el-menu>-->
        <!--</el-card>-->
      </el-row>
<!--      </el-aside>-->
      <el-main  style="margin-right: 3%;" v-loading="this.$store.state.loading">
        <!--<el-card style="border-radius: 0px;">-->
          <router-view/>
        <!--</el-card>-->
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
import Vue from 'vue'
import global_ from '@/components/global'
import router from '../../router'
Vue.prototype.GLOBAL = global_
export default {
  name: 'My',
  data () {
    return {
      collapseBtnClick: false,
      isCollapse: false,
      collapseIcon: 'el-icon-s-fold',
      screenHeight: '',
      orgs: [],
      choose: ''
    }
  },
  methods: {
    handleSelect (key, keyPath) {
      console.log(key, keyPath)
    },
    handleCommand (command) {
      if (command === 'logout') {
        this.$router.push({name: 'Login', params: {}})
      } else {
        this.choose = command
      }
    },
    collapseStatus () {
      this.collapseBtnClick = this.isCollapse
      this.isCollapse = !this.isCollapse
      if (this.isCollapse === false) {
        this.collapseIcon = 'el-icon-s-unfold'
      } else {
        this.collapseIcon = 'el-icon-s-fold'
      }
    },
    // 设置全局表格自适应高度
    getHeight (val) {
      this.GLOBAL.tableHeight = val - 200 + 'px'
      this.GLOBAL.boxHeight = val - 200 + 'px'
      this.GLOBAL.iframeHeight = val - 230 + 'px'
      this.GLOBAL.windowHeight = val
    },
    toCreateOrg () {
      router.push('/createOrg')
    },
    titleRouterClick (name) {
      switch (name) {
        case 'orgs':
          router.push({name: 'MyOrgs'})
          break
        case 'guide':
          router.push({name: 'Guide'})
          break
        case 'members':
          router.push({name: 'OrgMember'})
          break
        case 'main':
          router.push({name: 'Guide'})
          break
        case 'check':
          router.push({name: 'DepositCheck'})
          break
      }
    }
  },
  // created () {
  //   window.addEventListener('resize', this.getHeight)
  //   this.getHeight()
  //   console.log('main created')
  // },
  mounted () {
    //    if (this.isCollapse === false) {
    //      this.collapseIcon = 'el-icon-s-unfold'
    //    } else {
    //      this.collapseIcon = 'el-icon-s-fold'
    //    }

    window.onresize = () => {
      return (() => {
        this.screenHeight = window.innerHeight
      })()
    }
    //    let data = localStorage.getItem('orgs')
    //    this.orgs = JSON.parse(data)
    //    console.log(this.orgs.organizationName)
    //    console.log(this.orgs)
  },
  watch: {
    screenHeight (val) {
      this.getHeight(val)
      console.log('screenHeightWatch=>tableHeight=>' + this.GLOBAL.tableHeight)
    }
  }
}
</script>
<style scoped>
  .el-menu-vertical-demo:not(.el-menu--collapse) {
    width: 250px;
    /*min-height: 500px;*/
  }
  .el-menu-item.is-active {
    font-weight: bolder;
    color: #1a1a1a !important;
  }
  .el-menu-item{
    font-size: 22px;
    color: #8a8a8a !important;
  }
  .el-divider--horizontal {
    margin: 0;
  }
</style>
