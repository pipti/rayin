
<template>
  <div >
<!--    <el-row>-->
<!--      <el-page-header @back="goBack" content="添加项目成员">-->
<!--      </el-page-header>-->
<!--    </el-row>-->
    <el-row>
      <el-col :span="4">
        &nbsp;
      </el-col>
      <el-col :span="16" style="text-align: right">
        <el-button type="primary" plain style="font-size: 15px;" @click="memberAddViewerClick()">添加成员</el-button>
      </el-col>
      <el-col :span="4">
        &nbsp;
      </el-col>

    </el-row>
    <el-row>
      <el-col :span="4">
        &nbsp;
      </el-col>
      <el-col :span="16">
      <template>
        <el-table
          :data="memberData"
          style="width: 100%"
          :height="this.$store.state.tableHeight">
          <el-table-column
            label="用户名"
            prop="username"
            width="200"
            >
          </el-table-column>
          <el-table-column
            prop="phone"
            label="手机号"
          >
          </el-table-column>
          <el-table-column
            prop=""
            label="邮箱"
            >
          </el-table-column>
          <el-table-column
            prop=""
            label="角色"
             width="100">
            <template slot-scope="scope">
              <span v-if="scope.row.owner===true">创建者</span>
              <span v-if="scope.row.owner===false">成员</span>
            </template>
          </el-table-column>
          <el-table-column
            fixed="right"
            prop="address"
            label="操作"
            width="100">
            <template slot-scope="scope">
              <el-button type="text" size="small" @click="memeberDel(scope.index, scope.row)" v-if="scope.row.owner===false">删除</el-button>
              <el-button type="text" size="small" @click="memberSetting(scope.index, scope.row)" v-if="scope.row.owner===false">设置</el-button>
              <el-button type="text" size="small" v-if="scope.row.owner===true" disabled>创建者</el-button>
            </template>
          </el-table-column>
        </el-table>
        <div style="width:100%;text-align: right">
          <el-pagination
            @size-change="this.handlePageSizeChange"
            :page-sizes="[5,10, 20, 50, 100]"
            @current-change="handleCurrentChange"
            layout="total,prev, pager, next,sizes"
            :total="memberDataTotal"
            :current-page="memberCurrentPage"
            :page-size="this.$store.state.pageSize">
          </el-pagination>
        </div>
      </template>
      </el-col>
      <el-col :span="4">
        &nbsp;
      </el-col>
    </el-row>

    <!--成员添加设置视图-->
    <el-drawer
      title="添加成员"
      :visible.sync="drawerMemberAddViewer"
      :with-header="true"
      size="30%"
      @opened="drawerMemberAddViewerOpened">
      <el-row>
        <el-col :span="1">
          &nbsp;
        </el-col>
        <el-col :span="22">
          <el-form label-width="100px" :model="MemberAddForm">
            <el-form-item label="用户名称">
              <el-input v-model="MemberAddForm.name" placeholder="请输入要添加的用户名称" style="width:200px" @keyup.enter.native="organizationAddMemberQuery(1)"
                        ref="MemberAddFormName" tabindex="0" ></el-input>
              <el-button type="primary" plain style="font-size: 15px;" @click="organizationAddMemberQuery(1)">查询用户</el-button>
            </el-form-item>
          </el-form>

          <template>
            <el-table
              :data="addMemberData"
              style="width: 100%"
              :height="this.$store.state.tableHeight">
              <el-table-column
                label="用户名"
                prop="username"
                width="200"
              >
              </el-table-column>
              <el-table-column
                prop="phone"
                label="手机号"
              >
              </el-table-column>
              <el-table-column
                prop="mail"
                label="邮箱"
              >
              </el-table-column>

              <el-table-column
                fixed="right"
                prop="address"
                label="操作"
                width="100">
                <template slot-scope="scope">
                  <el-button type="text" size="small" @click="memeberAdd(scope.index, scope.row)">添加</el-button>
                </template>
              </el-table-column>
            </el-table>
            <div style="width:100%;text-align: right">
              <el-pagination
                @size-change="this.handlePageSizeChange"
                :page-sizes="[5,10, 20, 50, 100]"
                @current-change="handleCurrentChange"
                layout="total,prev, pager, next,sizes"
                :total="memberDataTotal"
                :current-page="memberCurrentPage"
                :page-size="this.$store.state.pageSize">
              </el-pagination>
            </div>
          </template>
        </el-col>
        <el-col :span="1">
          &nbsp;
        </el-col>
      </el-row>
    </el-drawer>

    <el-drawer
      title="搜索条件设置"
      :visible.sync="memberIndexViewer"
      :with-header="true"
      size="30%"
      @opened="memberSettingViewerOpened">
      <el-row>
        <el-col :span="16">
          &nbsp;
        </el-col>
        <el-col :span="8">
          <el-button type="primary" @click="userIndexSave" size="small">保存</el-button>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="20" v-for="(userIndex) in userIndexes" :key="userIndex.indexId" :offset="1">
          <!--          <el-card @click.native="selectIndex(orgIndex)">-->
          <span class="info-title">{{userIndex.indexNameChn}}</span>
          <el-input type="text" v-model="userIndex.value" value=""></el-input>
          <!--          </el-card>-->
        </el-col>
      </el-row>
    </el-drawer>
  </div>
</template>

<script>
import axios from 'axios'

export default {

  components: {
  },
  data () {
    return {
      memberData: [
      ],
      memberDataTotal: 0,
      memberCurrentPage: 0,
      addMemberData: [],
      addMemberDataTotal: 0,
      addMemberCurrentPage: 0,
      drawerMemberAddViewer: false,
      MemberAddForm: {},
      memberIndexViewer: false,
      userIndexes: [],
      currentSettingUserId: ''
    }
  },
  methods: {
    memberAddViewerClick () {
      this.drawerMemberAddViewer = true
      //      this.$refs.MemberAddFormName.focus()
    },
    drawerMemberAddViewerOpened () {
      this.$refs.MemberAddFormName.focus()
    },
    handleCurrentChange (val) {
      axios.get(this.GLOBAL.webappApiConfig.OrganizationManager.OrganizationMemberQuery.url + '?pageCurrent=' + val + '&pageSize=' + this.$store.state.pageSize,
        {},
        {})
        .then(res => {
          this.memberData = res.data.content.records
          this.memberDataTotal = res.data.content.total
          this.memberCurrentPage = res.data.content.current
          console.log(this.memberData)
        })
        .catch(function (error) {
          console.log(error)
        })
    },
    organizationAddMemberQuery (val) {
      if (this.MemberAddForm.name === null || this.MemberAddForm.name === '' || this.MemberAddForm.name === undefined) {
        this.$message({
          showClose: true,
          message: '请输入用户名',
          type: 'error'
        })
        return
      }

      axios.get(this.GLOBAL.webappApiConfig.OrganizationManager.OrganizationAddMemberQuery.url + '/' + this.MemberAddForm.name + '?pageCurrent=' + val + '&pageSize=' + this.$store.state.pageSize,
        {},
        {})
        .then(res => {
          if (res.data.content.records.length === 0) {
            this.$message({
              showClose: true,
              message: '未查询到该用户',
              type: 'warning'
            })
          }
          this.addMemberData = res.data.content.records
          this.addMemberDataTotal = res.data.content.total
          this.addMemberCurrentPage = res.data.content.current
        })
        .catch(function (error) {
          console.log(error)
        })
    },
    memeberDel (index, row) {
      axios.post(this.GLOBAL.webappApiConfig.OrganizationManager.OrganizationMemberDel.url,
        row,
        {})
        .then(res => {
          this.memberData.splice(index, 1)
        })
        .catch(function (error) {
          console.log(error)
        })
    },
    memeberAdd (index, row) {
      axios.post(this.GLOBAL.webappApiConfig.OrganizationManager.OrganizationMemberAdd.url,
        row,
        {})
        .then(res => {
          this.handleCurrentChange(1)
        })
        .catch(function (error) {
          console.log(error)
        })
    },
    goBack () {
      this.$router.push({name: 'Guide', params: {}})
    },
    memberSetting (index, row) {
      this.currentSettingUserId = row.userId
      this.userIndexQuery(row.userId, 1)
      this.memberIndexViewer = true
    },
    memberSettingViewerOpened () {
      // this.userIndexQuery(1)
    },
    userIndexQuery (userId, currentPage) {
      axios.get(this.GLOBAL.webappApiConfig.UserIndex.UserIndexQuery.url + '/' + userId + '?pageCurrent=' + currentPage + '&pageSize=' + this.$store.state.pageSize)
        .then(res => {
          this.userIndexes = res.data.content.records
        })
        // eslint-disable-next-line handle-callback-err
        .catch(function (error) {
        })
    },
    userIndexSave () {
      // return
      axios.post(this.GLOBAL.webappApiConfig.UserIndex.UserIndexSave.url + '/' + this.currentSettingUserId,
        this.userIndexes)
        .then(res => {
          // this.userIndexes = res.data.content.records
        })
        // eslint-disable-next-line handle-callback-err
        .catch(function (error) {
        })
    }
  },
  mounted () {
  },
  created () {
    this.handleCurrentChange(1)
  }
}
</script>

<style scoped>
  .el-tabs .el-tabs--left .el-tabs--border-card{
    height:400px;
    /*overflow:scroll;*/
}

  .helloword {
    overflow: hidden;
  }
  .text-event {
    float: left;
    height: 500px;
    width: 500px;
    border: 1px solid red;
    position: relative;
    background: linear-gradient(-90deg, rgba(0, 0, 0, 0.1) 1px, transparent 1px) 0% 0% / 10px 10px, linear-gradient(rgba(0, 0, 0, 0.1) 1px, transparent 1px) 0% 0% / 10px 10px;

  }
  .p-event {
    float: left;
    height: 300px;
    width: 300px;
    border: 1px solid blue;
    position: relative;
    margin-left: 20px;
  }

  .dragging1 {
    border: 1px solid #000;
    color: #000;
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

  /*#resourceTab >>>> .el-tab-pane{*/
  /*  overflow:scroll;*/
  /*  flex-grow: 1;*/
  /*  overflow-y: scroll;*/
  /*}*/
  #dataCol >.el-tabs--left{
    overflow:visible!important;
  }
</style>
