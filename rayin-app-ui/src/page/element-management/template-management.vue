<template>
  <div>
    <el-row>
      <el-col :span="8">
        <el-button type="primary" plain size="medium" style="border-radius: 0px" @click="tplAdd">
          <i class="el-icon-document-add"></i>新增模板</el-button>
<!--        <el-button plain size="medium" style="border-radius: 0px"><i class="el-icon-delete"></i>删除模板</el-button>-->
        <el-tooltip class="item" effect="dark" placement="right-start" style="border-radius: 0px">
          <div slot="content">模板是通过构件进行组合生成，前提是先进行构件的设计，才能配置模板</div>
          <el-button size="medium"><i class="el-icon-help"></i>提示</el-button>
        </el-tooltip>
      </el-col>
      <el-col :span="4">
        <el-radio-group v-model="modelSwitch" size="mini">
          <el-radio-button label="normalData"><i class="el-icon-set-up"></i></el-radio-button>
          <el-radio-button label="recycleData"><i class="el-icon-delete"></i>回收站</el-radio-button>
        </el-radio-group>
      </el-col>
      <el-col :span="12">
        <el-input placeholder="请输入搜索内容" v-model="searchKey" class="input-with-select" @keyup.enter.native="tlSearchClick">
          <el-button slot="append" icon="el-icon-search" @click="tlSearchClick"></el-button>
        </el-input>
      </el-col>
    </el-row>
    <el-row>
      <template>
        <el-table
          :data="tlData"
          style="width: 100%"
          :height="this.$store.state.tableHeight">
          <el-table-column
            label="模板编号"
            width="120"
            fixed>
            <template slot-scope="scope">
              <el-popover trigger="hover" placement="top">
                <el-tag size="small">{{ scope.row.templateId }}</el-tag>
                <div slot="reference" class="name-wrapper">
                  <el-tag size="small">{{ scope.row.templateId.substring(0,10) }}..</el-tag>
                </div>
              </el-popover>
            </template>
          </el-table-column>
          <el-table-column
            prop="templateReleaseStatus"
            label="发布状态"
            width="80">
          </el-table-column>
          <el-table-column
            prop="name"
            label="模板名称"
            width="300">
            <template slot-scope="scope">
              <el-popover trigger="hover" placement="top">
                {{ scope.row.name }}
                <div slot="reference" class="name-wrapper">
                  <div v-if="scope.row.name !== null">{{ scope.row.name.substring(0,10) }}
                    <span v-if="scope.row.name.length > 10">...</span>
                  </div>
                </div>
              </el-popover>
            </template>
          </el-table-column>
          <el-table-column
            prop="templateVersion"
            label="版本号"
            width="80">
          </el-table-column>
          <el-table-column
            prop="createTimeStr"
            label="创建日期"
            width="100">
            <template slot-scope="scope">
              <el-popover trigger="hover" placement="top">
                {{ scope.row.createTimeStr }}
                <div slot="reference" class="name-wrapper">
                  <div v-if="scope.row.createTimeStr !== null">{{ scope.row.createTimeStr.substring(0,10) }}</div>
                </div>
              </el-popover>
            </template>
          </el-table-column>
          <el-table-column
            prop="updateTimeStr"
            label="更新日期"
            width="100">
            <template slot-scope="scope">
              <el-popover trigger="hover" placement="top">
                {{ scope.row.updateTimeStr }}
                <div slot="reference" class="name-wrapper">
                  <div v-if="scope.row.updateTimeStr !== null">{{ scope.row.updateTimeStr.substring(0,10) }}</div>
                </div>
              </el-popover>
            </template>
          </el-table-column>

          <el-table-column
            label="有效期"
            width="200">
            <template slot-scope="scope">
              <el-popover trigger="hover" placement="top">
                {{ scope.row.startTimeStr }}&nbsp;~&nbsp;{{ scope.row.endTimeStr }}
                <div slot="reference" class="name-wrapper">
                  <span v-if="scope.row.startTimeStr !== null">{{ scope.row.startTimeStr.substring(0,10) }}</span>&nbsp;~&nbsp;
                  <span v-if="scope.row.endTimeStr !== null">{{ scope.row.endTimeStr.substring(0,10) }}</span>
                </div>
              </el-popover>
            </template>
          </el-table-column>
          <el-table-column
            label="创建用户"
            width="100">
            <template slot-scope="scope">
              <el-popover trigger="hover" placement="top">
                {{ scope.row.userName }}
                <div slot="reference" class="name-wrapper">
                  <span v-if="scope.row.userName !== null">{{ scope.row.userName.substring(0,10)}}</span>
                </div>
              </el-popover>
            </template>
          </el-table-column>

          <el-table-column
            label="备注">
            <template slot-scope="scope">
              <el-popover trigger="hover" placement="top">
                {{ scope.row.memo }}
                <div slot="reference" class="name-wrapper">
                  <div v-if="scope.row.memo !== null">{{ scope.row.memo.substring(0,10) }}
                    <span v-if="scope.row.memo.length > 10">...</span>
                  </div>
                </div>
              </el-popover>
            </template>
          </el-table-column>
          <el-table-column
            fixed="right"
            prop="address"
            label="操作"
            width="230" v-if="dataModelSwitch==='normalData'">
            <template slot-scope="scope">
              <el-button type="text" size="small" @click="tplEdit(scope.index, scope.row)">编辑</el-button>
              <el-button type="text" size="small" @click="tplTest(scope.row)">模板测试</el-button>
              <el-button type="text" size="small" @click="tplTest(scope.row)" disabled>接口测试</el-button>
              <el-popover
                placement="bottom"
                width="200"
                trigger="hover">
                <div>
                  <el-button type="text" size="small" disabled @click="tplPublishToOtherOrg(scope.index, scope.row)">发布至其他项目</el-button>
                  <el-button type="text" size="small" @click="tplLogicalDel(scope.index, scope.row)">删除</el-button>
                </div>
                <el-button type="text" size="medium" slot="reference" class="el-icon-more"></el-button>
              </el-popover>
            </template>
          </el-table-column>

          <el-table-column
            fixed="right"
            prop="address"
            label="操作"
            width="230" v-if="dataModelSwitch==='recycleData'">
            <template slot-scope="scope">
              <el-button @click="tplResumeClick(scope.$index, scope.row)" type="text" size="small">恢复</el-button>
              <el-button type="text" size="small" @click="tplDel(scope.index, scope.row)">彻底删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <div style="width:100%;text-align: right">
          <el-pagination
            @size-change="this.handlePageSizeChange"
            :page-sizes="[5,10, 20, 50, 100]"
            @current-change="handleCurrentChange"
            layout="total,prev, pager, next,sizes"
            :total="tlDataTotal"
            :current-page="tlCurrentPage"
            :page-size="this.$store.state.pageSize">
          </el-pagination>
        </div>
      </template>

    </el-row>
  </div>
</template>

<script>
import axios from 'axios'
export default {
  data () {
    return {
      modelSwitch: 'normalData',
      dataModelSwitch: 'normalData',
      delFlag: false,
      activeIndex: '1',
      activeIndex2: '1',
      tlData: [],
      tlDataTotal: 1,
      tlCurrentPage: 1,
      searchKey: '',
      loading: false
    }
  },
  methods: {
    handleSelect (key, keyPath) {
      // console.log(key, keyPath)
    },
    tplAdd (key, keyPath) {
      this.$router.push({name: 'TemplateSet', params: {}})
    },
    tplEdit (index, row) {
      this.$router.push({name: 'TemplateSet', params: {tplRow: row}})
    },
    tplTest (row) {
      this.$router.push({name: 'TemplateTest', params: {tplRow: row, searchKey: this.searchKey}})
    },
    tplLogicalDel (index, row) {
      console.log(row)
      // 删除构件
      this.$confirm('是否确定删除该构件至回收站, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        // 删除模板
        axios.post(this.GLOBAL.webappApiConfig.TemplateManagement.UserTemplateLogicalDel.url,
          row,
          {})
          .then(res => {
            this.tlData.splice(index, 1)
          }).catch(function (error) {
            console.log(error)
          })
      })
    },
    tplDel (index, row) {
      // 删除模板
      this.$confirm('是否确定彻底删除该模板，有关该模板的所有版本都将被删除, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        // 删除模板
        axios.post(this.GLOBAL.webappApiConfig.TemplateManagement.UserTemplateDel.url,
          row,
          {})
          .then(res => {
            this.tlData.splice(index, 1)
          }).catch(function (error) {
            console.log(error)
          })
      })
    },
    tplResumeClick (index, row) {
    // 删除构件
      axios.post(this.GLOBAL.webappApiConfig.TemplateManagement.UserTemplateResume.url,
        row,
        {})
        .then(res => {
          this.tlData.splice(index, 1)
        })
        .catch(function (error) {
          console.log(error)
        })
    },
    tlSearchClick () {
      this.handleCurrentChange(1)
    },
    handleCurrentChange (val) {
      this.loading = true
      axios.get(this.GLOBAL.webappApiConfig.TemplateManagement.UserTemplateQuery.url + (this.searchKey === '' ? '' : '/' + this.searchKey) + '?pageCurrent=' + val + '&pageSize=' + this.$store.state.pageSize + '&delFlag=' + this.delFlag,
        {},
        {})
        .then(res => {
          this.tlData = res.data.content.records
          this.tlDataTotal = res.data.content.total
          this.tlCurrentPage = res.data.content.current
          this.dataModelSwitch = this.modelSwitch
          this.loading = false
          // console.log(res)
        })
        .catch(function (error) {
          console.log(error)
        })
    }
  },
  mounted () {
    this.tlSearchClick()
  },
  watch: {
    modelSwitch (val) {
      if (val === 'normalData') {
        this.delFlag = false
      } else {
        this.delFlag = true
      }
      this.handleCurrentChange(1)
    }
  }
}
</script>
