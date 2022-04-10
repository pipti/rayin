<template>
  <div>
    <el-row>
      <el-col :span="12">
        <el-button type="primary" plain size="medium" style="border-radius: 0px" @click="loadData"><i class="el-icon-upload2"></i>数据载入</el-button>
        <el-button plain size="medium" style="border-radius: 0px" @click="batchDelete(multipleSelection)"><i class="el-icon-delete"></i>批量删除</el-button>
        <el-tooltip class="item" effect="dark" placement="right-start" style="border-radius: 0px">
          <div slot="content">用于管理构件设计过程中使用的数据</div>
          <el-button size="medium"><i class="el-icon-help"></i>提示</el-button>
        </el-tooltip>
      </el-col>
      <el-col :span="12">
        <el-input placeholder="请输入搜索内容" v-model="searchKey" class="input-with-select">
          <el-button slot="append" icon="el-icon-search"></el-button>
        </el-input>
      </el-col>
    </el-row>
    <el-row>
      <el-col>
        <template>
          <el-table
            :data="orgData"
            style="width: 100%"
            :height="this.$store.state.tableHeight"
            @selection-change="handleSelectionChange"
          >
            <el-table-column
              type="selection"
              width="55"
              prop="dataId"
              :selectable='checkbox'
              disabled="true"
            >
            </el-table-column>
            <el-table-column
              label="名称"
              prop="dataName"
              width="100"
            >
            </el-table-column>
            <el-table-column
              label="数据"
              prop="data"
              width="800"
            >

              <template slot-scope="scope">
                <div v-if="scope.row.data !== null">{{ scope.row.data.substring(0,100) + ' ....' }}</div>
              </template>
            </el-table-column>
            <el-table-column
              prop="createDate"
              label="上传日期"
              width="200">
            </el-table-column>
            <el-table-column
              label="所属用户"
              prop="username"
              width="100"
            >
            </el-table-column>
            <el-table-column
              prop="operation"
              label="操作"
              width="300">
              <template slot-scope="scope">
                <el-button @click="dataViewClick(scope.row)" type="text" size="small">查看</el-button>
                <el-button @click="deleteClick(scope.row)" type="text" size="small" v-if="currentUserId === scope.row.userId">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
          <div style="width:100%;text-align: right">
            <el-pagination
              @size-change="this.handlePageSizeChange"
              :page-sizes="[5, 10, 20, 50, 100]"
              @current-change="handleCurrentChange"
              layout="total,prev, pager, next,sizes"
              :total="dataTotal"
              :current-page="dataCurrentPage"
              :page-size="this.$store.state.pageSize"
              :page-count="dataPageCount">
            </el-pagination>
          </div>
        </template>
      </el-col>
    </el-row>
    <el-drawer
      title="数据"
      :visible.sync="dataShowDrawer"
      :with-header="true"
      size="700px"
    >
      <el-row>
        <el-col :span="1">
          &nbsp;
        </el-col>
        <el-col :span="22" >
          <el-row>
            <!--数据-->
            <div :style="{height: this.$store.state.boxHeight,overflow:scroll}" style="width:100%;">
              <vue-json-pretty
                :data="showData"
                :path="path"
                :deep="deep"
                :show-double-quotes="showDoubleQuotes"
                :highlight-mouseover-node="highlightMouseoverNode"
                :highlight-selected-node="highlightSelectedNode"
                :show-length="showLength"
                :show-line="showLine"
                :select-on-click-node="selectOnClickNode"
                :collapsed-on-click-brackets="collapsedOnClickBrackets"
                v-model="jsonPrettyValue"
                :path-selectable="((path, data) => typeof data !== 'number')"
                :selectable-type="selectableType"
                :show-select-controller="showSelectController"
                style="font-size: 12px"
              >
              </vue-json-pretty>
            </div>
          </el-row>
        </el-col>
        <el-col :span="1">
          &nbsp;
        </el-col>
      </el-row>
    </el-drawer>

    <el-drawer
      title="载入数据"
      :visible.sync="dataSaveDrawer"
      :with-header="true"
      size="700px"
    >
      <el-row>
        <el-col :span="1">
          &nbsp;
        </el-col>
        <el-col :span="22">
          <span class="info-title">名称</span>
          <el-input
            type="text"
            v-model.lazy.trim="saveOrgData.dataName"
            @change="check"
            :class="{input_error:saveOrgData.dataName === ''}"
          >
          </el-input>
        </el-col>
        <el-col :span="1">
          &nbsp;
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="1">
          &nbsp;
        </el-col>
        <el-col :span="22" >
          <el-row>
            <span class="info-title">数据</span>
            <!--数据-->
            <div :style="{height: this.$store.state.boxHeight,overflow:scroll}" style="width:100%;">
              <vue-json-pretty
                :data="showSaveData"
                :path="path"
                :deep="deep"
                :show-double-quotes="showDoubleQuotes"
                :highlight-mouseover-node="highlightMouseoverNode"
                :highlight-selected-node="highlightSelectedNode"
                :show-length="showLength"
                :show-line="showLine"
                :select-on-click-node="selectOnClickNode"
                :collapsed-on-click-brackets="collapsedOnClickBrackets"
                v-model="jsonPrettyValue"
                :path-selectable="((path, data) => typeof data !== 'number')"
                :selectable-type="selectableType"
                :show-select-controller="showSelectController"
                style="font-size: 12px"
              >
              </vue-json-pretty>
            </div>
          </el-row>
          <el-row>
            <el-col :span="18">
              &nbsp;
            </el-col>
            <el-col :span="3">
              <el-upload ref="json-load"
                         class="upload-demo"
                         action="#"
                         :on-change="handleOnChange"
                         :limit="1"
                         :on-exceed="handleExceed"
                         :file-list="fileList"
                         list-type="text"
                         :auto-upload="false"
                         accept=".txt,.json"
                         :before-upload="beforeAvatarUpload"
              >
                <!--<el-button size="small" type="primary">数据加载</el-button>-->
                <el-button icon="el-icon-coin" title="加载数据" type="primary" class="button-fillet-left" size="small">加载</el-button>
              </el-upload>
            </el-col>
            <el-col :span="3">
              <el-button icon="el-icon-connection" title="保存" @click="saveDataClick" type="primary" class="button-fillet-right" size="small">保存</el-button>
            </el-col>
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
/* eslint-disable */
  import Cookies from 'js-cookie'
  import router from '../../router'
  import {setToken, removeToken} from '@/util/auth'
  import VueJsonPretty from 'vue-json-pretty'
  import axios from 'axios'
  export default {
    name: 'DataManagement',
    components: {
      VueJsonPretty
    },
    data() {
      return {
        orgData:[],
        dataTotal: 0,
        dataCurrentPage: 1,
        dataPageCount: 0,
        dataShowDrawer: false,
        dataSaveDrawer: false,
        showData: {},
        showSaveData:{},
        saveOrgData: {
          data: '',
          dataName: ' '
        },
        path: 'root',
        deep: 10,
        showDoubleQuotes: true,
        highlightMouseoverNode: true,
        highlightSelectedNode: true,
        selectOnClickNode: true,
        showLength: false,
        showLine: true,
        collapsedOnClickBrackets: true,
        jsonPrettyValue: '',
        boxHeight: this.$store.state.boxHeight,
        dialogDataBindSelectVisible: false,
        selectableType: 'single',
        showSelectController: false,
        scroll:'scroll',
        searchKey: '',
        multipleSelection: [],
        fileList: [],
        currentUserId: localStorage.getItem('userId'),
        isnull: true
      };
    },
    methods: {
      handleCurrentChange(val) {
        axios.get(this.GLOBAL.webappApiConfig.OrganizationResource.OrganizationDataQuery.url + '?pageCurrent=' + this.dataCurrentPage + '&pageSize=' + this.$store.state.pageSize,)
          .then(res => {
            this.orgData = res.data.content.records
            this.dataTotal = res.data.content.total
            this.dataCurrentPage = res.data.content.current
          })
          .catch(function (error) {
          })
      },
      dataViewClick(row) {
        this.showData = JSON.parse(row.data)
        this.dataShowDrawer = true
      },
      deleteClick(row) {
        let delParam = []
        delParam[0] = row
        this.batchDelete(delParam)
      },
      loadData() {
        this.dataSaveDrawer = true
        this.saveOrgData.dataName = ' '
      },
      saveDataClick() {
        if (this.saveOrgData.dataName.trim() === '') {
          this.saveOrgData.dataName = ''
          return
        }
        this.saveOrgData.data = JSON.stringify(this.showSaveData)
        axios.post(this.GLOBAL.webappApiConfig.OrganizationResource.OrganizationDataSave.url,
        this.saveOrgData)
          .then(res => {
            this.handleCurrentChange(1)
            this.dataSaveDrawer = false
            this.showSaveData = {}
            this.saveOrgData.dataName = ''
          })
          .catch(function (error) {
          })
      },
      handleOnChange(file, fileList){
        this.$refs['json-load'].clearFiles();
        let reader = new FileReader()
        reader.readAsText(file.raw,'UTF-8');

        reader.onload=(e)=>{
          this.showSaveData = JSON.parse(e.target.result)
        }

      },
      handleExceed(files, fileList) {
        this.$message.warning(`当前限制选择 3 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件`);
        console.log(`当前限制选择 3 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件`);
      },
      beforeAvatarUpload(){
        const isJSON = file.type === '.txt,.json';
        const isLt3M = file.size / 1024 / 1024 < 3;

        if (!isJSON) {
          this.$message.error('加载格式为文本或json格式!');
        }

        if (!isLt3M) {
          this.$message.error('上传头像图片大小不能超过 3MB!');
        }
      },
      checkbox(row,index) {
        if (row.userId === this.currentUserId) {
          return 1;
        } else {
          return 0;
        }
      },
      handleSelectionChange (val) {
        this.multipleSelection = val
      },
      batchDelete(value) {
        if (value.length == 0) {
          this.$message.error("请选择要删除的数据")
          return
        }
        this.$confirm("删除所选数据将无法恢复, 是否继续?", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(() => {
          axios.post(this.GLOBAL.webappApiConfig.OrganizationResource.OrganizationDataDel.url,value)
            .then(res => {
              this.handleCurrentChange(1)
            })
            .catch(function (error) {
            })
        }).catch(() => {});

      },
      check () {
        // this.$forceUpdate()
      }
    },
    mounted() {
      this.handleCurrentChange(1)
    },
    created() {
    }
  }
</script>
<style scoped>
header{
  height:30px;
}
  .el-input__inner{
    border-radius:0px;
  }

  .el-form-item__error {
    margin-left: 22% !important;
  }
</style>
