<template>
  <div>
    <el-row>
      <el-page-header @back="goBack" content="模板设置">
      </el-page-header>
    </el-row>
    <el-row :gutter="20" style="margin-top:20px">
      <el-col  :span="10" style="border-right: 1px solid rgb(230, 230, 230)">
        <el-row>
          <el-col :span="12">
            <el-tooltip class="item" effect="dark" placement="right-start" style="border-radius: 0px">
              <div slot="content">
                左侧为构件列表，通过【构件设计】功能添加
              </div>
              <el-button size="small"><i class="el-icon-help"></i>构建列表提示</el-button>
            </el-tooltip>
            </el-col>
          <el-col :span="12">
            <el-input placeholder="请输入搜索内容" v-model="elSearchKey" class="input-with-select" @keyup.enter.native="elSearchClick">
              <el-button slot="append" icon="el-icon-search" @click="elSearchClick"></el-button>
            </el-input>
          </el-col>
        </el-row>
        <el-row>
          <template>
          <el-table
            :data="elData"
            style="width: 100%"
            :height="this.$store.state.tableHeight">
            <el-table-column
              label="构件编号"
              width="120">
              <template slot-scope="scope">
                <el-popover trigger="hover" placement="top">
                  <el-tag size="small">{{ scope.row.elementId }}</el-tag>
                  <div slot="reference" class="name-wrapper">
                    <el-tag size="small">{{ scope.row.elementId.substring(0,10) }}..</el-tag>
                  </div>
                </el-popover>
              </template>
            </el-table-column>
            <el-table-column
              prop="name"
              label="构件名称"
              width="">
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
              prop="elementVersion"
              label="版本号"
              width="80">
            </el-table-column>
            <el-table-column
              fixed="right"
              prop="operation"
              label="操作"
              width="100">
              <template slot-scope="scope">
                <el-button @click="elViewerClick(scope.row)" type="text" size="small">查看</el-button>
                <el-button @click="tplElAddClick(scope.$index, scope.row)" type="text" size="small">添加</el-button>
              </template>
            </el-table-column>
          </el-table>
          <div style="width:100%;text-align: right">
            <el-pagination
              @size-change="this.handlePageSizeChange"
              :page-sizes="[5, 10, 20, 50, 100]"
              @current-change="handleCurrentChange"
              layout="total,prev, pager, next,sizes"
              :total="elDataTotal"
              :current-page="elCurrentPage"
              :page-size="this.$store.state.pageSize">
            </el-pagination>
          </div>
          </template>
        </el-row>
      </el-col>
      <el-col  :span="14">
        <el-row>
          <el-col :span="14">
            <el-tooltip class="item" effect="dark" placement="right-start" style="border-radius: 0px">
              <div slot="content">"序号"代表构件组合顺序，生成的PDF按照序号从前到后进行构件的组合<br>
                "模板设置"进行模板的全局参数设置，包括PDF的属性信息以及全局的页码设置和空白页设置<br>
                单个构件中的设置用来设置单个构件的构件类型，页码设置，空白页等.
              </div>
              <el-button size="small"><i class="el-icon-help"></i>模板列表提示</el-button>
            </el-tooltip>
            <el-button plain size="small" style="border-radius: 0px" @click="tplSetViewerClick"><i class="el-icon-set-up"></i>模板设置</el-button>
            <el-button plain size="small" style="border-radius: 0px" @click="tplSaveClick"><i class="el-icon-check"></i>保存设置</el-button>

          </el-col>

          <el-col :span="10">
            <el-input placeholder="请输入搜索内容" v-model="tplSearchKey" class="input-with-select">
              <el-button slot="append" icon="el-icon-search"></el-button>
            </el-input>
            <!--<el-tag size="small">{{ templateForm.templateId.substring(0,10) }}..</el-tag>-->
          </el-col>
        </el-row>
        <el-row>
          <template>
            <el-table
              :data="elConfig"
              style="width: 100%"
              :height="this.$store.state.tableHeight"
              >
              <el-table-column
                fixed
                type="index"
                label="序号"
                width="50">
              </el-table-column>
              <el-table-column
                prop="elementVersion"
                label="版本号"
                width="100">
              </el-table-column>
              <el-table-column
                label="构件编号"
                width="120">
                <template slot-scope="scope">
                  <el-popover trigger="hover" placement="top">
                    <el-tag size="small">{{ scope.row.elementId }}</el-tag>
                    <div slot="reference" class="name-wrapper">
                      <el-tag size="small">{{ scope.row.elementId.substring(0,10) }}..</el-tag>
                    </div>
                  </el-popover>
                </template>
              </el-table-column>
              <el-table-column
                prop="name"
                label="构件名称"
                width="200">
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
                prop="elementType"
                label="构件类型"
                width="120">
                <template slot-scope="scope">
                  <el-input v-model="scope.row.elementType" placeholder="数字或字母"></el-input>
                </template>
              </el-table-column>
              <el-table-column
                prop="elementAvaliableDataPath"
                label="有效数据路径"
                width="180">
                <template slot-scope="scope">
                  <el-input v-model="scope.row.elementAvaliableDataPath" placeholder="构件有效数据路径"></el-input>
                </template>
              </el-table-column>
              <el-table-column
                prop="pageNumIsDisplay"
                label="页码显示"
                width="100">
                <template slot-scope="scope">
                    <el-switch
                      v-model="scope.row.pageNumIsDisplay"
                      active-text="是"
                      inactive-text="否">
                    </el-switch>
                  </template>
              </el-table-column>
              <el-table-column
                prop="pageNumIsFirstPage"
                label="起始页"
                width="100">
                <template slot-scope="scope">
                    <el-switch
                      v-model="scope.row.pageNumIsFirstPage"
                      active-text="是"
                      inactive-text="否">
                    </el-switch>
                  </template>
              </el-table-column>
              <el-table-column
                prop="pageNumIsCalculate"
                label="累加页"
                width="100">
                <template slot-scope="scope">
                    <el-switch
                      v-model="scope.row.pageNumIsCalculate"
                      active-text="是"
                      inactive-text="否">
                    </el-switch>
                  </template>
              </el-table-column>
              <el-table-column
                prop="pageNumIsCalculate"
                label="追加空白页"
                width="100">
                <template slot-scope="scope">
                  <el-switch
                    v-model="scope.row.addBlankPage"
                    active-text="是"
                    inactive-text="否">
                  </el-switch>
                </template>
              </el-table-column>
<!--              <el-table-column-->
<!--                prop="uncommonCharsAnalysis"-->
<!--                label="生僻字解析"-->
<!--                width="100">-->
<!--                <template slot-scope="scope">-->
<!--                  <el-switch-->
<!--                    v-model="scope.row.uncommonCharsAnalysis"-->
<!--                    active-text="是"-->
<!--                    inactive-text="否">-->
<!--                  </el-switch>-->
<!--                </template>-->
<!--              </el-table-column>-->
              <el-table-column
                prop="uncommonCharsAnalysis"
                label="页码属性"
                width="100">
                <template slot-scope="scope">
                  <el-button icon="el-icon-tickets" size="mini" @click="elPageNumParamSetViewerClick(scope.index, scope.row)"></el-button>
                </template>
              </el-table-column>
              <el-table-column
                fixed="right"
                prop="address"
                label="操作"
                width="210">
                <template slot-scope="scope">
                  <el-button @click="elViewerClick(scope.row)" type="text" size="small">查看</el-button>
                  <el-button @click="tplElDelClick(scope.$index, scope.row)" type="text" size="small">删除</el-button>
                  <!--<el-button @click="elSetViewerClick(scope.row)" type="text" size="small" >设置</el-button>-->
                  <el-button @click="tplElUpClick(scope.$index, scope.row)" type="text" size="small">向上</el-button>
                  <el-button @click="tplElDownClick(scope.$index, scope.row)" type="text" size="small">向下</el-button>
                </template>
              </el-table-column>
            </el-table>
            <!--<div style="width:100%;text-align: right">-->
              <!--<el-pagination-->
                <!--layout="prev, pager, next"-->
                <!--:total="elConfigTotal"-->
                <!--:page-size="this.$store.state.pageSize"-->
                <!--:current-page="elConfigCurrentPage">-->
              <!--</el-pagination>-->
            <!--</div>-->
          </template>

        </el-row>
      </el-col>
    </el-row>

    <!--模板预览视图-->
    <el-drawer
      title="模板预览"
      :visible.sync="drawerElViewer"
      :with-header="true"
      size="50%">
      <el-row>
        <el-col :span="1">
          &nbsp;
        </el-col>
        <el-col :span="22">
          <el-card :style="{marginTop:'20px',overflow: 'auto',height:this.$store.state.elViewerBoxHeight}">
            <html-panel :htmlCode="elCode"></html-panel>
          </el-card>
        </el-col>
        <el-col :span="1">
          &nbsp;
        </el-col>
      </el-row>
    </el-drawer>

    <!--构件参数设置视图-->
    <el-drawer
      title="构件参数设置"
      :visible.sync="drawerElSetViewer"
      :with-header="true"
      size="50%">
      <el-row>
        <el-col :span="1">
          &nbsp;
        </el-col>
        <el-col :span="22">

        </el-col>
        <el-col :span="1">
          &nbsp;
        </el-col>
      </el-row>
    </el-drawer>

    <!--模板参数设置视图-->
    <el-drawer
      title="模板参数设置"
      :visible.sync="drawerTplSetViewer"
      :with-header="true"
      size="50%">
      <el-row>
        <el-col :span="1">
          &nbsp;
        </el-col>
        <el-col :span="22">
          <el-form label-width="100px" :model="templateForm">
            <el-form-item label="模板名称">
              <el-input v-model="templateForm.name" placeholder="请输入构件名称"></el-input>
            </el-form-item>
            <el-form-item label="标题">
              <el-input v-model="templateForm.title" placeholder="请输入构件标题"></el-input>
            </el-form-item>
            <el-form-item label="作者">
              <el-input v-model="templateForm.author" placeholder="请输入构件作者"></el-input>
            </el-form-item>
            <el-form-item label="主题">
              <el-input v-model="templateForm.subject" placeholder="请输入构件主题"></el-input>
            </el-form-item>
            <el-form-item label="关键字">
              <el-input v-model="templateForm.keywords" placeholder="请输入构件关键字"></el-input>
            </el-form-item>
            <el-form-item label="创建者">
              <el-input v-model="templateForm.creator" placeholder="请输入创建者"></el-input>
            </el-form-item>
            <el-form-item label="生产者">
              <el-input v-model="templateForm.producer" placeholder="请输入生产者"></el-input>
            </el-form-item>
            <el-form-item label="密码">
              <el-input v-model="templateForm.password" placeholder="请输入密码"></el-input>
            </el-form-item>
            <el-form-item label="备注" >
              <el-input
                type="textarea"
                :rows="2"
                placeholder="请输入备注"
                v-model="templateForm.memo">
              </el-input>
            </el-form-item>

            <el-form-item label="生效开始时间" >
              <el-date-picker
                v-model="templateForm.startTime"
                type="datetime"
                placeholder="选择开始日期时间">
              </el-date-picker>
            </el-form-item>
            <el-form-item label="生效结束时间" >
              <el-date-picker
                v-model="templateForm.endTime"
                type="datetime"
                placeholder="选择结束日期时间">
              </el-date-picker>
            </el-form-item>
            <el-form-item label="是否可编辑" >
              <el-switch
                v-model="templateForm.editorable"
                active-text="是"
                inactive-text="否">
              </el-switch>
            </el-form-item>
          </el-form>
        </el-col>
        <el-col :span="1">
          &nbsp;
        </el-col>
      </el-row>
    </el-drawer>

    <!--构件页码属性设置-->
    <el-drawer
      title="页码属性设置"
      :visible.sync="drawerElPageNumParamSetViewer"
      :with-header="true"
      :before-close="drawerElPageNumParamSetViewerHandleClose()"
      size="40%">
      <el-row>
        <el-col :span="1">
          &nbsp;
        </el-col>
        <el-col :span="22">
          <div style="100%;text-align: right"><el-button type="primary" icon="el-icon-plus" size="mini" @click="elPageNumParamAddClick()"></el-button></div>
          <template>
            <el-table
              :data="pageNumDisplayPoss"
              style="width: 100%"
              :height="this.$store.state.tableHeight"
            >
              <el-table-column
                fixed
                type="index"
                label="序号"
                width="50">
              </el-table-column>

              <el-table-column
                prop="x"
                label="x坐标"
                width="150">
                <template slot-scope="scope">
                  <el-input-number size="mini" v-model="scope.row.x"  :precision="1" :step="10" controls-position="right"></el-input-number>
                  </template>
              </el-table-column>
              <el-table-column
                prop="y"
                label="y坐标"
                width="150">
                <template slot-scope="scope">
                <el-input-number size="mini" v-model="scope.row.y" :precision="1" :step="10" controls-position="right"></el-input-number>
                </template>
              </el-table-column>
              <el-table-column
                prop="mark"
                label="关键字"
                width="150">
                <template slot-scope="scope">
                  <el-input size="mini" v-model="scope.row.mark" controls-position="right"></el-input>
                </template>
              </el-table-column>
              <el-table-column
                fixed="right"
                prop="address"
                label="操作">
                <template slot-scope="scope">
                  <el-button @click="elPageNumParamDelClick(scope.$index, scope.row)" type="text" size="small">删除</el-button>
                </template>
              </el-table-column>
            </el-table>
          </template>
        </el-col>
        <el-col :span="1">
          &nbsp;
        </el-col>
      </el-row>
    </el-drawer>
  </div>
</template>

<script>
import HtmlPanel from '@/components/html-panel'
import axios from 'axios'
export default {
  name: 'TemplateSet',
  components: {
    HtmlPanel
  },
  data () {
    return {
      templateForm: {
        name: '',
        version: '',
        memo: '',
        elConfig: '',
        testData: '',
        templateId: '',
        newFlag: ''
      },
      elData: [],
      elConfig: [],
      pageNumDisplayPoss: [],
      elPageNumSetSel: [],
      elSearchKey: '',
      tplSearchKey: '',
      elCode: '',
      drawerElViewer: false,
      drawerElSetViewer: false,
      drawerTplSetViewer: false,
      drawerElPageNumParamSetViewer: false,
      elViewerBoxHeight: '',
      elConfigTotal: 1,
      elConfigCurrentPage: 1,
      elDataTotal: 1,
      elCurrentPage: 1,
      pageSize: this.$store.state.pageSize,
      elementTypeOptions: [{
        value: '1',
        label: '封皮'
      }, {
        value: '2',
        label: '客户指南'
      }, {
        value: '3',
        label: '保单页'
      }, {
        value: '4',
        label: '现金价值'
      }, {
        value: '5',
        label: '条款'
      }, {
        value: '6',
        label: '送达书'
      }]
    }
  },
  methods: {
    elPageNumParamAddClick () {
      this.pageNumDisplayPoss.push({x: 0, y: 0})
    },
    elPageNumParamDelClick (index, row) {
      this.pageNumDisplayPoss.splice(index, 1)
    },
    drawerElPageNumParamSetViewerHandleClose () {
      //      this.pageNumDisplayPoss.forEach((item) => {
      //        console.log(item.x)
      //          if(item.x === '' || item.y === ''){
      //            this.$message({
      //              showClose: true,
      //              message: '坐标数值不能空，如果无需设置请删除',
      //              type: 'error'
      //            })
      //            return
      //          }
      //        }
      //      )
      this.elPageNumSetSel.pageNumDisplayPoss = this.pageNumDisplayPoss
    },
    goBack () {
      this.$router.push({name: 'TemplateManagement', params: {}})
    },
    tplElAddClick (index, row) {
      let temp = this.elData[index]
      // temp.seq = index
      //      temp.content = ''
      //      temp.testData = ''

      this.elConfig.push(temp)
      let seq = 1
      this.elConfig.forEach((item) => {
        item.seq = seq++
      })
    },
    tplElDelClick (index, row) {
      this.elConfig.splice(index, 1)

      let seq = 1
      this.elConfig.forEach((item) => {
        item.seq = seq++
      })
    },
    tplElDownClick (index, row) {
      if (index !== this.elConfig.length - 1) {
        let tmp = this.elConfig[index]
        this.elConfig.splice(index, 1, this.elConfig[index + 1])
        this.elConfig.splice(index + 1, 1, tmp)
      } else {
        this.$message({
          showClose: true,
          message: '已经是最后一行,无法下移',
          type: 'warning'
        })
      }
      let seq = 1
      this.elConfig.forEach((item) => {
        item.seq = seq++
      })
    },
    tplElUpClick (index, row) {
      if (index !== 0) {
        let tmp = this.elConfig[index - 1]
        this.elConfig.splice(index - 1, 1, this.elConfig[index])
        this.elConfig.splice(index, 1, tmp)
      } else {
        this.$message({
          showClose: true,
          message: '已经是第一行,无法上移',
          type: 'warning'
        })
      }
      let seq = 1
      this.elConfig.forEach((item) => {
        item.seq = seq++
      })
    },
    elViewerClick (row) {
      // console.log(row.content)
      this.elCode = row.content
      // this.htmlLoadCompleted = true
      this.drawerElViewer = true
    },
    elSetViewerClick (row) {
      this.drawerElSetViewer = true
    },
    tplSetViewerClick () {
      this.drawerTplSetViewer = true
    },
    elPageNumParamSetViewerClick (index, row) {
      if (row.pageNumDisplayPoss === undefined) {
        this.pageNumDisplayPoss = []
      } else {
        this.pageNumDisplayPoss = row.pageNumDisplayPoss
      }
      this.elPageNumSetSel = row
      this.drawerElPageNumParamSetViewer = true
    },
    elSearchClick () {
      this.handleCurrentChange(1)
    },
    handleCurrentChange (val) {
      axios.get(this.GLOBAL.webappApiConfig.ELementManagement.UserElementQuery.url + (this.elSearchKey === '' ? '' : '/' + this.elSearchKey) + '?pageCurrent=' + val + '&pageSize=' + this.$store.state.pageSize,
        {},
        {})
        .then(res => {
          this.elData = res.data.content.records
          this.elDataTotal = res.data.content.total
          this.elCurrentPage = res.data.content.current
        })
        .catch(function (error) {
          console.log(error)
          // alert(error)
        })
    },
    tplSaveClick () {
      if (this.templateForm.name === null || this.templateForm.name === '') {
        this.$message({
          showClose: true,
          message: '请添加模板名称',
          type: 'error'
        })
        this.tplSetViewerClick()
      } else if (this.elConfig.length === 0) {
        this.$message({
          showClose: true,
          message: '请添加构件',
          type: 'error'
        })
      } else {
        this.templateForm.elConfig = JSON.stringify(this.elConfig)
        // this.elementForm.testData = JSON.stringify(this.this.elConfig)
        console.log(this.elConfig)
        axios.post(this.GLOBAL.webappApiConfig.TemplateManagement.UserTemplateSave.url,
          this.templateForm,
          {})
          .then(res => {
            if (res.status === 200) {
              this.$router.push({name: 'TemplateManagement', params: {}})
            }
          })
          .catch(function (error) {
            console.log(error)
          })
      }
    }
  },
  // data中的变量之间赋值
  mounted () {
    this.elSearchClick()

    if (this.$route.params.tplRow !== undefined) {
      this.templateForm = this.$route.params.tplRow
      this.elConfig = JSON.parse(this.$route.params.tplRow.elConfig)

      // this.elConfigTotal = this.elConfig.length
    }
  },
  // 检测数据变化 watch 方法为变量名
  watch: {
    // elConfig (val) {
    // this.elConfigTotal = this.elConfig.length
    // this.elDataTotal = this.elData.length
    // }

  }
}
</script>
<style scoped>
  .el-form >>> .el-input__inner{
    border-radius: 0px;
  }
  .el-form >>> .el-textarea__inner{
    border-radius: 0px;
  }
</style>
