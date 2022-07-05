<template>
  <el-container>
    <el-header style="padding:5px 5px 5px 5px;height:50px;border-bottom:solid 1px #e6e6e6;">
      <el-row>
        <el-col style="width: 50px;">
          <el-button type="primary" :icon="collapseIcon" style="border-radius: 0px;padding:6px 10px;font-size: 25px"
                     @click="collapseStatus"></el-button>
        </el-col>
        <el-col :span="4">
          <div style="width:100%;text-align: left">
            <img src="@/assets/images/logo5.png" style="width:200px;">
          </div>
        </el-col>

        <el-col :span="8">
            <div style="width: 100%;text-align: left">
              <el-link :underline="false"  style="margin-top: 15px;font-size:22px;margin-right:30px" type="primary"
                       @click="titleRouterClick('orgs')">
                {{$i18n.t('project')}}</el-link>
              <!--<el-link :underline="false"  style="margin-top: 15px;font-size:22px;margin-right:30px" type="primary"
              @click="titleRouterClick('guide')">设计</el-link>-->
              <!--<el-link :underline="false"  style="margin-top: 15px;font-size:22px;margin-right:30px" type="primary"
              @click="titleRouterClick('members')">成员</el-link>-->
            </div>
        </el-col>
        <el-col :span="7">
          <div style="width:100%;text-align:right;margin-top:10px;" >
            <!-- 项目列表 -->
            <el-dropdown trigger="click" @command="handleCommand">
            <span class="el-dropdown-link">
             <div style="border-left:5px solid #409EFF;padding-left: 10px;height:30px;padding-bottom:3px">
               <el-link style="font-size:25px;" type="primary" :underline="false">
                 {{this.$store.state.organizationName}}</el-link>
             </div>
            </span>
              <el-dropdown-menu slot="dropdown">
                <!--<el-dropdown-item>-->
                  <!--<el-button @click="toCreateOrg()" type="text" size="big">创建项目</el-button>-->
                <!--</el-dropdown-item>-->
                <!--<el-dropdown-item>-->
                  <!--<el-button @click="toCreateOrg()" type="text" size="big">我自己</el-button>-->
                <!--</el-dropdown-item>-->
                <!--<el-divider style="margin: 0"></el-divider>-->
                <el-dropdown-item v-for="(item,index) in orgs" :key='index'><span>{{item.organizationName}}</span>
                </el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </div>
        </el-col>
        <el-col :span="4">
          <div style="width:100%;text-align: right;">
            <el-link  style="margin-bottom: 10px;margin-right: 10px" @click="titleRouterClick('main')">
              {{$i18n.t('mainPage')}}</el-link>
            <el-link style="margin-bottom: 10px" href="http://rayin.ink/" target="_blank">
              {{$i18n.t('help')}}</el-link>
            <el-dropdown trigger="click" @command="handleCommand">
            <span class="el-dropdown-link">
              <el-avatar :src="require('@/assets/images/face1.jpg')"></el-avatar>
              <!--<i class="el-icon-arrow-down el-icon&#45;&#45;right"></i>-->
            </span>
              <el-dropdown-menu slot="dropdown">
                <!--<el-dropdown-item>-->
                  <!--<el-button @click="toCreateOrg()" type="text" size="big">创建项目</el-button>-->
                  <!--<el-button @click="changeOrgClick()" type="text" size="big">切换项目</el-button>-->
                <!--</el-dropdown-item>-->
                <!--<el-dropdown-item v-for="(item,index) in orgs" :key='index'>
                <span>{{item.organizationName}}</span>-->
                <!--</el-dropdown-item>-->
                <!--<el-divider style="margin: 0"></el-divider>-->
<!--                <el-dropdown-item command="toMy"><span>个人设置</span></el-dropdown-item>-->
<!--                <el-dropdown-item disabled>信息中心</el-dropdown-item>-->
                <el-dropdown-item command="logout">{{$i18n.t('logout')}}</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </div>
        </el-col>
      </el-row>
    </el-header>
    <el-container>
      <el-aside width="auto" style="border-right:solid 1px #e6e6e6;" >
        <el-menu
          default-active="this.$route.path"
          class="el-menu-vertical-demo "
          @open="handleOpen"
          @close="handleClose" router
          :collapse="isCollapse"
        style="border-right:solid 0px #e6e6e6;" >
          <el-submenu index="2">
            <template slot="title">
              <i class="el-icon-collection"></i>
              <span>{{$i18n.t('menu.design')}}</span>
            </template>
            <el-menu-item v-for="menu in menus" :key="menu.index" :index="menu.index" :route="menu.route"
                           :disabled="menu.disable" >
              <i :class="menu.icon" v-if="menu.show"></i>
              <span v-if="menu.show">{{menu.name}}</span>
            </el-menu-item>
            <!--<el-menu-item index="2-4" route="/main/template-management">-->
              <!--<i class="el-icon-document-copy"></i>-->
              <!--<span>模板接口测试</span>-->
            <!--</el-menu-item>-->
          </el-submenu>
          <el-submenu index="3">
            <template slot="title">
              <i class="el-icon-files"></i>
              <span>{{$i18n.t('menu.proofManage')}}</span>
            </template>
            <el-menu-item index="3-1" route="/main/pdf-query">
              <i class="el-icon-search"></i>
              <span slot="title">{{$i18n.t('menu.fileQueryDownload')}}</span>
            </el-menu-item>
          </el-submenu>
        </el-menu>
      </el-aside>
      <el-main v-loading="this.$store.state.loading">
          <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
import Vue from 'vue';
import global_ from '@/components/global';
import router from '../router';
import axios from 'axios';
Vue.prototype.GLOBAL = global_;

export default {
  name: 'main',
  data() {
    return {
      collapseBtnClick: true,
      isCollapse: true,
      collapseIcon: 'el-icon-s-fold',
      screenHeight: '',
      orgs: [],
      menus: [],
    };
  },
  methods: {
    // eslint-disable-next-line no-unused-vars
    handleSelect(key, keyPath) {
      // console.log(key, keyPath)
    },
    // eslint-disable-next-line no-unused-vars
    handleClose(key, keyPath) {
      // console.log(key, keyPath)
    },
    // eslint-disable-next-line no-unused-vars
    handleOpen(key, keyPath) {
      // console.log(key, keyPath)
    },
    handleCommand(command) {
      if (command === 'logout') {
        if (command === 'logout') {
          axios.post('/api/users/logout')
            .then(() => {
              // eslint-disable-next-line handle-callback-err
            })
            .catch(() => {
            });
          this.$router.push({ name: 'Login', params: {} });
        } else {
          this.choose = command;
        }
        // this.$router.push({name: 'Login', params: {}})
      }
      if (command === 'toMy') {
        this.$router.push('/my/info');
      }
    },
    collapseStatus() {
      this.collapseBtnClick = this.isCollapse;
      this.isCollapse = !this.isCollapse;
      if (this.isCollapse === false) {
        this.collapseIcon = 'el-icon-s-unfold';
      } else {
        this.collapseIcon = 'el-icon-s-fold';
      }
    },
    // 设置全局表格自适应高度
    getHeight(val) {
      this.$store.state.tableHeight = `${val - 200}px`;
      this.$store.state.boxHeight = `${val - 200}px`;
      this.$store.state.iframeHeight = `${val - 230}px`;
      this.$store.state.windowHeight = val;
      this.$store.state.boxHeight = `${val - 200}px`;
    },
    toMy() {

    },
    toCreateOrg() {
      router.push('/createOrg');
    },
    changeOrgClick() {
      router.push('/organization-list');
    },
    orgQuery(val) {
      if (localStorage.getItem('orgs') === undefined || localStorage.getItem('orgs') === '') {
        axios.get(
          `${this.GLOBAL.webappApiConfig.OrganizationManager.OrganizationQuery.url}?pageCurrent=${val}&pageSize=${this.$store.state.pageSize}`,
          {},
          {},
        )
          .then((response) => {
            this.orgs = response.data.content.records;
            console.log(response);
            localStorage.setItem('orgs', JSON.stringify(this.orgs));
            // this.orgs = data
          });
      } else {
        this.orgs = JSON.parse(localStorage.getItem('orgs'));
      }
    },
    titleRouterClick(name) {
      switch (name) {
        case 'orgs':
          router.push({ name: 'MyOrgs' });
          break;
        case 'guide':
          router.push({ name: 'Guide' });
          break;
        case 'members':
          router.push({ name: 'OrgMember' });
          break;
        case 'main':
          router.push({ name: 'Guide' });
          break;
        case 'check':
          router.push({ name: 'DepositCheck' });
          break;
      }
    },
  },
  // created () {
  //   window.addEventListener('resize', this.getHeight)
  //   this.getHeight()
  //   console.log('main created')
  // },
  mounted() {
    if (this.isCollapse === false) {
      this.collapseIcon = 'el-icon-s-unfold';
    } else {
      this.collapseIcon = 'el-icon-s-fold';
    }

    window.onresize = () => (() => {
      this.screenHeight = window.innerHeight;
    })();
  },
  watch: {
    screenHeight(val) {
      this.getHeight(val);
      // console.log('screenHeightWatch=>tableHeight=>' + this.$store.state.tableHeight)
      // console.log('screenHeightWatch=>boxHeight=>' + this.$store.state.boxHeight)
    },
  },
  created() {
    if (localStorage.getItem(`${localStorage.getItem('userId')}-organizationName`) === null) {
      this.$router.push({ name: 'MyOrgs', params: {} });
      // return
    }
    this.menus.push({ name: this.$i18n.t('menu.guide'), index: '2-1', route: '/main/guide', icon: 'el-icon-help', show: true, disable: false });
    this.menus.push({ name: this.$i18n.t('menu.elementDesign'), index: '2-2', route: '/main/element-management', icon: 'el-icon-document', show: true, disable: false });
    this.menus.push({ name: this.$i18n.t('menu.templateSetting'), index: '2-3', route: '/main/template-management', icon: 'el-icon-document-copy', show: true, disable: false });
    // eslint-disable-next-line no-empty
    if (localStorage.getItem(`${localStorage.getItem('userId')}-organizationOwner`) === 'true') {
    } else {
      this.menus.push({ name: '成员配置', index: '2-4', route: '/my/org-member', icon: 'el-icon-user', show: false, disable: false });
      this.menus.push({ name: '搜索配置', index: '2-5', route: '/my/org-index', icon: 'el-icon-search', show: false, disable: false });
    }
    //    let username = localStorage.getItem('username')
    //    axios.post('/api/users/userInfo',
    //      username,
    //      {})
    //      .then((response) => {
    //        let data = response.data.content.orgs
    //        console.log('/api/users/userInfo=>')
    //        console.log(data)
    //        localStorage.setItem('orgs', JSON.stringify(data))
    //        this.orgs = data
    //      })

    // this.orgQuery(1)
  },
};
</script>
<style scoped>
  .el-menu-vertical-demo:not(.el-menu--collapse) {
    width: 250px;
    /*min-height: 500px;*/
  }
  .el-divider--horizontal {
   margin: 0;
  }
</style>
