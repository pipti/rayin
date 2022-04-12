
<template>
  <div >
    <el-row>
      <el-col style="margin-top: 10px" :xs="5" :sm="3" :md="2" :lg="2" :xl="1">
        <el-tag style="font-size: 20px;">项目</el-tag>
      </el-col>
      <el-col :xs="10" :sm="8" :md="6" :lg="4" :xl="2">
        <div @click="orgSetViewClick()" style="cursor:pointer;text-align: left;width:50px" id="orgAddIcon">
          <el-tooltip class="item" effect="dark" content="请先添加项目" placement="right" :manual="true" :value="orgtTipVisual" transition="el-fade-in-linear"
                      :hide-after="5000" :open-delay="1000">
            <i class="el-icon-circle-plus-outline" ></i>
          </el-tooltip>
        </div>
      </el-col>
    </el-row>
    <el-row style="margin-top:30px">
      <el-col :xs="11" :sm="5" :md="5" :lg="3" :xl="2" v-for="(o) in orgs" :key="o.organizationId" :offset="2" style="margin-top: 10px">
        <el-card :body-style="{ padding: '0px' }" shadow="hover" style="width:230px">
          <div style="width:100%;text-align: center;cursor:pointer;margin-top: 10px" @click="changeOrgClick(o)">
            <!--<i class="el-icon-s-promotion" style="font-size: 180px;color: #F56C6C"></i>-->
            <i :class="[o.icon]" :style="{color:o.iconColor,'font-size':'170px'}"></i>
          </div>
          <div style="padding: 10px;text-align: center">
            <el-row>
              <el-col :span="1">
                &nbsp;
              </el-col>
              <el-col :span="22">
                <!--<span>{{o.organizationName}}</span>-->
                <div v-if="o.organizationName !== null">{{ o.organizationName.substring(0,8) }}
                  <span v-if="o.organizationName.length > 8">...</span>
                </div>
              </el-col>
              <el-col :span="1" >
                <!--<div @click="orgSetViewClick(o)" v-if="o.owner===true" style="float:right"><i class="el-icon-setting" style="font-size: 25px;color: #909399"></i></div>-->
                <!--<div @click="orgSetViewClick(o)" v-if="o.owner===true" style="float:right"><i class="el-icon-setting" style="font-size: 25px;color: #909399"></i></div>-->
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                &nbsp;
              </el-col>

              <el-col :span="12" >
<!--                <div @click="orgMemberSetClick(o)" v-if="o.owner===true" style="float:right;margin-left:5px">-->
<!--                  <i class="el-icon-user" style="font-size: 22px;color: #909399"></i>-->
<!--                </div>-->
                <div @click="orgSetViewClick(o)" v-if="o.owner===true" style="float:right;margin-left:5px"><i class="el-icon-setting" style="font-size: 22px;color: #909399"></i></div>
<!--                <div @click="orgIndexSetClick(o)" v-if="o.owner===true" style="float:right"><i class="el-icon-s-claim" style="font-size: 22px;color: #909399"></i></div>-->
              </el-col>
            </el-row>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!--模板参数设置视图-->
    <el-drawer
      title="项目设置"
      :visible.sync="drawerOrgSetViewer"
      :with-header="true"
      @opened="drawerOrgSetViewerOpened"
      size="500px">
      <el-row>
        <el-col :span="1">
          &nbsp;
        </el-col>
        <el-col :span="22">
          <el-row>
          <el-form label-width="140px" :model="selectOrg">
            <el-form-item label="项目名称">
              <el-input v-model="selectOrg.organizationName" placeholder="请输入项目名称" style="width:285px" tabindex="0" ref="orgName"></el-input>
            </el-form-item>
            <el-form-item label="ACCESS_KEY">
              <el-input v-model="selectOrg.accessKey" placeholder="新建保存后自动生成" style="width:285px"  readonly></el-input>
            </el-form-item>
            <el-form-item label="SECRET_KEY">
              <el-input v-model="selectOrg.secretKey" placeholder="新建保存后自动生成" style="width:285px" readonly></el-input>
            </el-form-item>
<!--            <el-form-item label="上链存证" v-if="selectOrg.owner===true">-->
<!--              <el-tooltip class="item" effect="dark" placement="top-start" style="border-radius: 0px;font-size: 20px">-->
<!--                <div slot="content">当前项目区块链上链开关，开启后该项目生成的凭证可以在开放联盟链上链存证</div>-->
<!--                <el-checkbox v-model="selectOrg.deposit" :checked="selectOrg.deposit === true"></el-checkbox>-->
<!--              </el-tooltip>-->
<!--            </el-form-item>-->
<!--            <el-form-item label="第三方存储URL">-->
<!--              <el-input v-model="selectOrg.thirdStorageUrl" placeholder="" style="width:285px" tabindex="1"></el-input>-->
<!--            </el-form-item>-->
<!--            <el-form-item label="第三方存储AK">-->
<!--              <el-input v-model="selectOrg.thirdStorageAccessKey" placeholder="" style="width:285px" tabindex="2"></el-input>-->
<!--            </el-form-item>-->
<!--            <el-form-item label="第三方存储SK">-->
<!--              <el-input v-model="selectOrg.thirdStorageSecretKey" placeholder="" style="width:285px" tabindex="3"></el-input>-->
<!--            </el-form-item>-->
<!--            <el-form-item label="第三方存储PDF桶名">-->
<!--              <el-input v-model="selectOrg.thirdStorageBucket" placeholder="" style="width:285px" tabindex="4"></el-input>-->
<!--            </el-form-item>-->
<!--            <el-form-item label="第三方存储资源桶名">-->
<!--              <el-input v-model="selectOrg.thirdStorageResourceBucket" placeholder="" style="width:285px" tabindex="4"></el-input>-->
<!--            </el-form-item>-->
<!--            <el-form-item label="存储类型">-->
<!--              <template>-->
<!--                <el-select placeholder="请选择存储类型" v-model="selectOrg.ossType" style="width:285px">-->
<!--                  <el-option-->
<!--                    v-for="item in ossTypeOptions"-->
<!--                    :key="item.value"-->
<!--                    :label="item.label"-->
<!--                    :value="item.value" >-->
<!--                  </el-option>-->
<!--                </el-select>-->
<!--              </template>-->
<!--            </el-form-item>-->
            <el-form-item label="ICON">
              <el-row>
                <el-col :span="10">
                  <el-card style="width:100px;height:100px"><i :class="[selectOrg.icon]" :style="{color:selectOrg.iconColor,'font-size':'60px'}"></i></el-card>
                </el-col>
                <el-col :span="14">
                  <el-color-picker
                    v-model="selectOrg.iconColor"
                    show-alpha
                    :predefine="predefineColors">
                  </el-color-picker>
                </el-col>
              </el-row>
            </el-form-item>
          </el-form>
          </el-row>
          <el-row style="margin-top: 30px" class="icon-hover">
            <el-col :span="7">&nbsp;</el-col>
              <el-col :span="2"><i class="el-icon-ship" @click="orgIconClick($event)"></i></el-col>
              <el-col :span="2"><i class="el-icon-truck" @click="orgIconClick($event)"></i></el-col>
              <el-col :span="2"><i class="el-icon-basketball" @click="orgIconClick($event)"></i></el-col>
              <el-col :span="2"><i class="el-icon-football" @click="orgIconClick($event)"></i></el-col>
              <el-col :span="2"><i class="el-icon-soccer" @click="orgIconClick($event)"></i></el-col>
            <el-col :span="7">&nbsp;</el-col>
          </el-row>
          <el-row class="icon-hover">
            <el-col :span="7">&nbsp;</el-col>
              <el-col :span="2"><i class="el-icon-baseball" @click="orgIconClick($event)"></i></el-col>
              <el-col :span="2"><i class="el-icon-wind-power" @click="orgIconClick($event)"></i></el-col>
              <el-col :span="2" ><i class="el-icon-food" @click="orgIconClick($event)"></i></el-col>
              <el-col :span="2"><i class="el-icon-wind-power" @click="orgIconClick($event)"></i></el-col>
              <el-col :span="2"><i class="el-icon-grape" @click="orgIconClick($event)"></i></el-col>
            <el-col :span="7">&nbsp;</el-col>
          </el-row>
          <el-row class="icon-hover">
            <el-col :span="7">&nbsp;</el-col>
            <el-col :span="2"><i class="el-icon-watermelon" @click="orgIconClick($event)"></i></el-col>
            <el-col :span="2"><i class="el-icon-cherry" @click="orgIconClick($event)"></i></el-col>
            <el-col :span="2"><i class="el-icon-apple" @click="orgIconClick($event)"></i></el-col>
            <el-col :span="2"><i class="el-icon-pear" @click="orgIconClick($event)"></i></el-col>
            <el-col :span="2"><i class="el-icon-orange" @click="orgIconClick($event)"></i></el-col>
            <el-col :span="7">&nbsp;</el-col>
          </el-row>
          <el-row style="font-size: 20px" class="icon-hover">
            <el-col :span="7">&nbsp;</el-col>
           <el-col :span="2"><i class="el-icon-coffee" @click="orgIconClick($event)"></i></el-col>
            <el-col :span="2"><i class="el-icon-ice-tea" @click="orgIconClick($event)"></i></el-col>
            <el-col :span="2"><i class="el-icon-ice-drink" @click="orgIconClick($event)"></i></el-col>
            <el-col :span="2"><i class="el-icon-milk-tea" @click="orgIconClick($event)"></i></el-col>
            <el-col :span="2"><i class="el-icon-potato-strips" @click="orgIconClick($event)"></i></el-col>
            <el-col :span="7">&nbsp;</el-col>
          </el-row>
          <el-row style="margin-top: 30px">
            <el-col :span="24" style="text-align: center">
              <el-button type="primary" icon="el-icon-setting" @click="orgSetSaveClick" size="small" style="width:150px">保存</el-button>
              <el-button type="primary" icon="el-icon-coin" @click="storageTestClick" size="small" style="width:100px">存储测试</el-button>
            </el-col>
          </el-row>
          <el-row :span="24" style="text-align: center;margin-top: 20px">
            <el-button type="danger" icon="el-icon-setting" @click="orgDelClick" size="small" style="width:155px" :disabled="!orgDelSwitch">项目删除</el-button>
            <el-switch
              v-model="orgDelSwitch"
              active-text="删除确认"
              inactive-text=""
              active-color="#F56C6C">
            </el-switch>
          </el-row>
        </el-col>
        <el-col :span="1">
          &nbsp;
        </el-col>
      </el-row>
    </el-drawer>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'MyOrgs',

  components: {
  },
  data () {
    return {
      orgtTipVisual: false,
      orgDelSwitch: false,
      drawerOrgSetViewer: false,
      selectOrg: {icon: '', iconColor: '', organizationName: '', ossType: ''},
      orgs: [
      ],
      color: '#409EFF',
      predefineColors: [
        '#ff4500',
        '#ff8c00',
        '#ffd700',
        '#90ee90',
        '#00ced1',
        '#1e90ff',
        '#c71585',
        'rgba(255, 69, 0, 0.68)',
        'rgb(255, 120, 0)',
        'hsv(51, 100, 98)',
        'hsva(120, 40, 94, 0.5)',
        'hsl(181, 100%, 37%)',
        'hsla(209, 100%, 56%, 0.73)'
      ],
      ossTypeOptions: [{
        value: 'minio',
        label: 'minio'
      }, {
        value: 'aliyun-oss',
        label: 'aliyun-oss'
      },
      {
        value: 'tencent-cos',
        label: 'tencent-cos'
      },
      {
        value: 'huawei-oss',
        label: 'huawei-oss'
      }]
    }
  },
  methods: {
    changeOrgClick (org) {
      console.log('changeOrgClick' + org.organizationId)
      this.$store.state.organizationName = org.organizationName
      this.$store.state.organizationId = org.organizationId
      localStorage.setItem(localStorage.getItem('userId') + '-organizationName', org.organizationName)
      localStorage.setItem(localStorage.getItem('userId') + '-organizationId', org.organizationId)
      localStorage.setItem(localStorage.getItem('userId') + '-organizationOwner', org.owner)
      this.$router.push({name: 'Guide', params: {}})
    },
    orgSetViewClick (o) {
      this.orgDelSwitch = false
      if (o === undefined) {
        this.selectOrg = {icon: '', iconColor: '', organizationName: ''}
      } else {
        this.selectOrg = o
      }
      console.log('项目设置')
      this.drawerOrgSetViewer = true
    },
    drawerOrgSetViewerOpened () {
      this.$refs.orgName.focus()
    },
    orgIconClick (event) {
      this.selectOrg.icon = event.currentTarget.getAttribute('class')
      this.selectOrg.iconColor = '#409EFF'
      console.log(this.selectOrg.icon)
    },
    orgMemberSetClick (o) {
      localStorage.setItem(localStorage.getItem('userId') + '-organizationName', o.organizationName)
      localStorage.setItem(localStorage.getItem('userId') + '-organizationId', o.organizationId)
      this.$router.push({name: 'OrgMember', params: {org: o}})
    },
    orgIndexSetClick (o) {
      localStorage.setItem(localStorage.getItem('userId') + '-organizationName', o.organizationName)
      localStorage.setItem(localStorage.getItem('userId') + '-organizationId', o.organizationId)
      this.$router.push({name: 'OrgIndex', params: {org: o}})
    },
    orgSetSaveClick () {
      if (this.selectOrg.organizationName === undefined || this.selectOrg.organizationName === '') {
        this.$message({
          showClose: true,
          message: '请输入项目名称',
          type: 'error'
        })
        return
      }
      if (this.selectOrg.icon === undefined || this.selectOrg.icon === '') {
        this.$message({
          showClose: true,
          message: '请选择一个图标',
          type: 'error'
        })
        return
      }
      // 机构保存
      axios.post(this.GLOBAL.webappApiConfig.OrganizationManager.OrganizationSave.url,
        this.selectOrg,
        {})
        .then(res => {
          this.orgQuery(1)
          this.drawerOrgSetViewer = false
        })
        .catch(function (error) {
          console.log(error)
        })
    },
    orgDelClick () {
      // 删除项目
      this.$confirm('是否确定删除该项目，有关该项目的所有数据将被删除，无法恢复, 是否确认?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        // 删除模板
        axios.post(this.GLOBAL.webappApiConfig.OrganizationManager.OrganizationDel.url,
          this.selectOrg,
          {})
          .then(res => {

          }).catch(function (error) {
            console.log(error)
          })
      })
    },
    orgQuery (val) {
      axios.get(this.GLOBAL.webappApiConfig.OrganizationManager.OrganizationQuery.url + '?pageCurrent=' + val + '&pageSize=' + this.$store.state.pageSize,
        {},
        {})
        .then((response) => {
          this.orgs = response.data.content.records
          console.log(this.orgs)
          if (this.orgs.length === 0) {
            this.orgtTipVisual = true
          } else {
            this.orgtTipVisual = false
            localStorage.setItem('orgs', JSON.stringify(this.orgs))
          }
          console.log(this.orgtTipVisual)
          // this.orgs = data
        })
    },
    storageTestClick () {
      if (this.selectOrg.thirdStorageAccessKey === null || this.selectOrg.thirdStorageAccessKey === undefined) {
        this.$message({
          showClose: true,
          message: '请添加第三方存储access_key',
          type: 'error'
        })
        return
      }

      if (this.selectOrg.thirdStorageSecretKey === '' || this.selectOrg.thirdStorageSecretKey === undefined) {
        this.$message({
          showClose: true,
          message: '请添加第三方存储secret_key',
          type: 'error'
        })
        return
      }

      if (this.selectOrg.thirdStorageUrl === '' || this.selectOrg.thirdStorageUrl === undefined) {
        this.$message({
          showClose: true,
          message: '请添加第三方存储服务url',
          type: 'error'
        })
        return
      }

      if (this.selectOrg.thirdStorageBucket === null || this.selectOrg.thirdStorageBucket === undefined) {
        this.$message({
          showClose: true,
          message: '请添加第三方存储服务PDFbucket',
          type: 'error'
        })
        return
      }

      if (this.selectOrg.thirdStorageResourceBucket === null || this.selectOrg.thirdStorageResourceBucket === undefined) {
        this.$message({
          showClose: true,
          message: '请添加第三方存储服务资源bucket',
          type: 'error'
        })
        return
      }
      console.log(this.selectOrg.ossType)
      if (this.selectOrg.ossType === null || this.selectOrg.ossType === undefined) {
        this.$message({
          showClose: true,
          message: '请选择存储类型',
          type: 'error'
        })
        return
      }

      axios.post(this.GLOBAL.webappApiConfig.OrganizationManager.OrganizationThirdStorageTest.url,
        this.selectOrg,
        {})
        .then(res => {
          console.log(res)
        })
        .catch(function (error) {
          console.log(error)
        })
    }
  },
  mounted () {
    this.orgQuery(1)
  },
  created () {
    //    let userId = localStorage.getItem('userId')
    //    axios.post('/api/users/getMyOrganization',
    //      userId)
    //      .then((response) => {
    //        let data = response.data.content
    //        console.log(data)
    //        this.orgs = data
    //        console.log(this.orgs)
    //      })

    //    let username = localStorage.getItem('username')
  },
  watch: {
  }
}
</script>

<style scoped>
  .el-tabs .el-tabs--left .el-tabs--border-card{
    height:400px;
    /*overflow:scroll;*/
}
  #designTab >>> .el-tabs__content{
    padding:0px;
  }

  #resourceTab >>> .el-tabs__content{
    /*overflow:scroll;*/
    flex-grow: 1;
    /*overflow-y: scroll;*/
    overflow:visible!important;
  }

  #dataCol >.el-tabs--left{
    overflow:visible!important;
  }
  .icon-hover i:hover{
    color:#409EFF;
    text-shadow:1px 1px 2px #909399;
  }
  .icon-hover i:active{
    color:#409EFF;
    text-shadow:0px 1px 0px #909399;
  }
  .icon-hover i{
    color:#909399;
    font-size:25px;
  }
  .el-form >>> input {
    border-top:0px;
    border-left:0px;
    border-right:0px;
    border-radius: 0px;
  }
  #orgAddIcon i{
    color:#EBEEF5;
    font-size:50px;
  }
  #orgAddIcon i:hover{
    color:#409EFF;
    text-shadow:1px 1px 2px #909399;
  }
</style>
