<template>
  <div>
<!--    <el-row>-->
<!--      <el-col :span="4">-->
<!--        <el-radio-group v-model="viewSwitch" size="small">-->
<!--          <el-radio-button label="search">搜索</el-radio-button>-->
<!--          <el-radio-button label="exactSearch">精确搜索</el-radio-button>-->
<!--        </el-radio-group>-->
<!--      </el-col>-->
<!--    </el-row>-->
    <el-row>
      <el-col :span="24">
        <el-input placeholder="请输入查询内容" v-model="searchKey" class="input-with-select">
          <el-button slot="append" icon="el-icon-search" @click="handleCurrentChange(1)"></el-button>
        </el-input>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="23">
      </el-col>
      <el-col :span="1" style="text-align: center">
        <el-button type="text" @click="orgIndexClick" size="big">筛选</el-button>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="4" v-for="(orgIndex, $index) in orgIndexes" :key="orgIndex.indexId" v-if="orgIndex.status && orgIndex.indexValue !== null">
        <el-card  class="fork" size="small">
          <span>{{orgIndex.indexNameChn}}:{{orgIndex.indexValue}}</span>
          <i @click="selectIndex(orgIndex,$index)"></i>
        </el-card>
      </el-col>
    </el-row>
<!--    <el-row v-if="viewSwitch==='exactSearch'">-->
<!--      <el-col :span="24">-->
<!--        <el-col :span="4" v-for="(orgIndex) in orgIndexes" :key="orgIndex.indexId">-->
<!--          <span class="info-title">{{orgIndex.indexNameChn}}</span>-->
<!--          <el-input type="text" v-model="orgIndex.indexName"></el-input>-->
<!--        </el-col>-->
<!--        &lt;!&ndash;<el-select v-model="bucketValue" placeholder="桶">&ndash;&gt;-->
<!--          &lt;!&ndash;<el-option&ndash;&gt;-->
<!--            &lt;!&ndash;v-for="item in bucketOptions"&ndash;&gt;-->
<!--            &lt;!&ndash;:key="item.value"&ndash;&gt;-->
<!--            &lt;!&ndash;:label="item.label"&ndash;&gt;-->
<!--            &lt;!&ndash;:value="item.value">&ndash;&gt;-->
<!--          &lt;!&ndash;</el-option>&ndash;&gt;-->
<!--        &lt;!&ndash;</el-select>&ndash;&gt;-->
<!--      </el-col>-->
<!--    </el-row>-->
    <el-row>
      <el-col>
        <template>
          <el-table
            :data="pdfData"
            style="width: 100%"
            :height="this.$store.state.tableHeight">
            <el-table-column
              fixed
              prop="x-cms-system-meta-createDate"
              label="上传日期"
              width="200">
            </el-table-column>
            <el-table-column
              prop="id"
              label="文件名"
            >
            </el-table-column>
            <el-table-column
              prop="Size"
              label="文件大小"
              width="200">
            </el-table-column>
            <el-table-column
              prop="operation"
              label="操作"
              width="300">
              <template slot-scope="scope">
                <el-button @click="fileViewClick(scope.row)" type="text" size="small">查看</el-button>
                <el-button @click="handleClick(scope.row)" type="text" size="small" disabled>浏览记录</el-button>
                <el-button @click="downloadPdf(scope.row)" type="text" size="small">下载</el-button>
                <el-button @click="shareUrlClick(scope.row)" type="text" size="small">分享</el-button>
              </template>
            </el-table-column>
          </el-table>
          <div style="width:100%;text-align: right">
            <el-pagination
              @size-change="this.handlePageSizeChange"
              :page-sizes="[5, 10, 20, 50, 100]"
              @current-change="handleCurrentChange"
              layout="total,prev, pager, next,sizes"
              :total="pdfDataTotal"
              :current-page="pdfCurrentPage"
              :page-size="this.$store.state.pageSize"
              :page-count="pdfPageCount">
            </el-pagination>
          </div>
        </template>
      </el-col>
    </el-row>

    <!--模板预览视图-->
    <el-drawer
      title="模板预览"
      :visible.sync="drawerPdfViewer"
      :with-header="true"
      @opened="drawerPdfViewerOpen"
      size="55%">
      <el-row>
        <el-col :span="1">
          &nbsp;
        </el-col>
        <el-col :span="22">
            <iframe id="pdfViewer" src="" width="100%" height="100%" :style="{width: '100%',height:'480%'}" frameborder="0" ></iframe>
        </el-col>
        <el-col :span="1">
          &nbsp;
        </el-col>
      </el-row>
    </el-drawer>

    <el-drawer
      title="筛选"
      :visible.sync="orgIndexViewer"
      :with-header="true"
      @opened="orgIndexOpened"
      size="400px">
      <el-row>
        <el-col :span="14">
          &nbsp;
        </el-col>
        <el-col :span="10">
          <el-button type="primary" @click="confirmParams" class="button-fillet-left" size="small">确定</el-button>
          <el-button type="primary" @click="clearParams" class="button-fillet-right-white" size="small">清除</el-button>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24" v-for="(orgIndex, $index) in orgIndexes" :key="orgIndex.indexId" :offset="1" v-if="orgIndex.value === null">
          <el-card @click.native="selectIndex(orgIndex,$index)" :class="{tick:orgIndex.status}">
              <span class="info-title">{{orgIndex.indexNameChn}}</span>
              <el-input type="text" v-model="orgIndex.indexValue" @click.native.stop="" v-show="orgIndex.status"></el-input>
              <i></i>
          </el-card>
        </el-col>

<!--        <el-col :span="1">-->
<!--          &nbsp;-->
<!--        </el-col>-->
<!--        <el-col :span="22">-->
<!--          <el-table-->
<!--            :data="orgData"-->
<!--            style="width: 100%"-->
<!--            :height="this.$store.state.tableHeight">-->
<!--            <el-table-column-->
<!--              label="名称"-->
<!--              prop="dataName"-->
<!--              width="100"-->
<!--            >-->
<!--            </el-table-column>-->
<!--            <el-table-column-->
<!--              label="数据"-->
<!--              prop="data"-->
<!--              width="200"-->
<!--            >-->
<!--              <template slot-scope="scope">-->
<!--                <el-popover trigger="hover" placement="top-start" width="300">-->
<!--                  <span>{{ scope.row.data }}</span>-->
<!--                  <div slot="reference" class="name-wrapper">-->
<!--                    <div v-if="scope.row.data !== null">{{ scope.row.data.substring(0,10) + ' ....' }}</div>-->
<!--                  </div>-->
<!--                </el-popover>-->
<!--              </template>-->
<!--            </el-table-column>-->
<!--            &lt;!&ndash;            <el-table-column&ndash;&gt;-->
<!--            &lt;!&ndash;              label="创建时间"&ndash;&gt;-->
<!--            &lt;!&ndash;              prop="createDate"&ndash;&gt;-->
<!--            &lt;!&ndash;              width="150"&ndash;&gt;-->
<!--            &lt;!&ndash;            >&ndash;&gt;-->
<!--            &lt;!&ndash;            </el-table-column>&ndash;&gt;-->
<!--            &lt;!&ndash;            <el-table-column&ndash;&gt;-->
<!--            &lt;!&ndash;              label="更新时间"&ndash;&gt;-->
<!--            &lt;!&ndash;              prop="updateDate"&ndash;&gt;-->
<!--            &lt;!&ndash;              width="150"&ndash;&gt;-->
<!--            &lt;!&ndash;            >&ndash;&gt;-->
<!--            &lt;!&ndash;            </el-table-column>&ndash;&gt;-->
<!--            <el-table-column-->
<!--              label="所属用户"-->
<!--              prop="username"-->
<!--              width="100"-->
<!--            >-->
<!--            </el-table-column>-->
<!--            <el-table-column-->
<!--              label="操作"-->
<!--              prop=""-->
<!--              width="200"-->
<!--            >-->
<!--              <template slot-scope="scope">-->
<!--                <el-button @click="orgLoadIndexClick(scope.row)" type="warning" size="mini">载入</el-button>-->
<!--              </template>-->
<!--            </el-table-column>-->
<!--          </el-table>-->
<!--        </el-col>-->
<!--        <el-col :span="1">-->
<!--          &nbsp;-->
<!--        </el-col>-->
      </el-row>
    </el-drawer>
    <el-dialog title="下载地址" :visible.sync="dialogVisible">
      <el-row>
        {{pdfUrl}}
      </el-row>
      <el-row>
        <el-col :span="18">
          &nbsp;
        </el-col>
        <el-col :span="6">
          <el-button type="primary" @click="copy(pdfUrl)" class="button-fillet-left">复制</el-button>
          <el-button type="primary" @click="dialogClose" class="button-fillet-right">关闭</el-button>
        </el-col>
      </el-row>
    </el-dialog>
  </div>

</template>

<script>
import axios from 'axios'
export default {
  name: 'PDFQuery',
  data () {
    return {
      activeIndex: '1',
      activeIndex2: '1',
      pdfData: [],
      bucketValue: '',
      searchKey: '',
      pdfDataTotal: 0,
      pdfCurrentPage: 1,
      pdfPageCount: 1,
      drawerPdfViewer: false,
      orgIndexes: [],
      orgIndexViewer: false,
      searchParams: {},
      isSelected: false,
      selectIndexes: [],
      pdfUrl: '',
      dialogVisible: false
    }
  },
  methods: {
    handleClick (key, keyPath) {
      console.log(key, keyPath)
    },
    handleCurrentChange (val) {
      let queryParams = {}
      this.orgIndexes.forEach(function (item, index) {
        if (item.indexValue !== null && item.status) {
          queryParams[item.indexName] = item.indexValue
        }
      })
      // let paramsss = JSON.parse(this.searchParams)
      if (this.searchKey !== null) {
        queryParams.dimParam = this.searchKey
      }
      axios.post(this.GLOBAL.webappApiConfig.OrganizationPDFManagement.OrganizationPDFQuery.url + '?pageCurrent=' + val + '&pageSize=' + this.$store.state.pageSize,
        {queryParams},
        {})
        .then(res => {
          this.pdfData = res.data.content.data
          this.pdfCurrentPage = res.data.content.currentPage
          this.pdfDataTotal = res.data.content.pdfDataTotal
          this.pdfPageCount = res.data.content.pdfPageCount
          //          this.pdfDataTotal = res.data.content.total
          //          this.elCurrentPage = res.data.content.current
          // console.log(res)
          // 数据渲染完成后再渲染组件样式，否则样式会错乱
          //          this.dataModelSwitch = this.modelSwitch
        })
        .catch(function (error) {
          console.log(error)
        })
      // this.pdfData = [{Key: 'fdafdafda', Size: '321321M'}]
    },
    fileViewClick (row) {
      console.log(row.id)
      axios.post(this.GLOBAL.webappApiConfig.OrganizationPDFManagement.OrganizationPDFView.url,
        {id: row.id, bucket: row.bucket})
        .then(res => {
          sessionStorage.setItem('pdfData', res.data.content.pdfFile)
          this.drawerPdfViewer = true
        })
        .catch(function (error) {
          console.log(error)
        })
    },
    drawerPdfViewerOpen () {
      let pdfViewerFrame = document.getElementById('pdfViewer')
      pdfViewerFrame.focus()
      pdfViewerFrame.src = '/static/pdfjs/web/pdfviewer.html'
      // pdfViewerFrame.src = this.pdfContent
    },
    orgQueryIndex (val) {
      // axios.get(this.GLOBAL.webappApiConfig.OrganizationIndex.OrganizationIndexQuery.url + '?pageCurrent=' + val + '&pageSize=' + this.$store.state.pageSize)
      //   .then(res => {
      //     this.orgIndexes = res.data.content.records
      //     // this.indexTotal = res.data.content.total
      //     // this.indexCurrentPage = res.data.content.current
      //   })
      //   // eslint-disable-next-line handle-callback-err
      //   .catch(function (error) {
      //   })
      axios.get(this.GLOBAL.webappApiConfig.UserIndex.UserIndexQuery.url + '/' + localStorage.getItem('userId') + '?pageCurrent=' + val + '&pageSize=' + this.$store.state.pageSize)
        .then(res => {
          this.orgIndexes = res.data.content.records
          this.orgIndexes.unshift({indexName: 'index_no', indexNameChn: '业务流水号', indexValue: null, value: null})
        })
        // eslint-disable-next-line handle-callback-err
        .catch(function (error) {
        })
    },
    orgIndexClick () {
      this.orgIndexViewer = true
    },
    orgIndexOpened () {
      this.orgQueryIndex(this.pdfCurrentPage)
    },
    selectIndex (orgIndex, $index) {
      if (orgIndex.status === true) {
        orgIndex.status = false
      } else {
        orgIndex.status = true
      }
      this.$forceUpdate()
      // if (this.selectIndexes[$index] === orgIndex.indexId) {
      //   this.selectIndexes[$index] = false
      // } else {
      //   this.selectIndexes[$index] = orgIndex.indexId
      // }
    },
    shareUrlClick (row) {
      axios.get('/api/organization/pdf/url' + '/' + 'b-eprint-test' + '/' + row.id)
        .then(res => {
          // console.log(res.data.content.pdfFile)
          // this.$confirm(res.data.content, '下载地址', {
          //   cancelButtonText: '关闭',
          //   type: 'confirm'
          // })
          this.pdfUrl = res.data.content
          this.dialogVisible = true
        })
        .catch(function (error) {
          console.log(error)
        })
    },
    copy (data) {
      let url = data
      let oInput = document.createElement('input')
      oInput.value = url
      document.body.appendChild(oInput)
      oInput.select()// 选择对象;
      console.log(oInput.value)
      document.execCommand('Copy') // 执行浏览器复制命令
      oInput.remove()
      this.$message.success('复制成功')
    },
    dialogClose () {
      this.dialogVisible = false
    },
    downloadPdf (row) {
      axios.get('/api/organization/pdf/url' + '/' + 'b-eprint-test' + '/' + row.id)
        .then(res => {
          // console.log(res.data.content.pdfFile)
          // this.$confirm(res.data.content, '下载地址', {
          //   cancelButtonText: '关闭',
          //   type: 'confirm'
          // })
          window.location.href = res.data.content
        })
        .catch(function (error) {
          console.log(error)
        })
    },
    clearParams () {
      this.orgIndexes.forEach(function (item, index) {
        item.indexValue = ''
        item.status = false
      })
      this.$forceUpdate()
    },
    confirmParams () {
      this.handleCurrentChange(1)
      this.orgIndexViewer = false
    }
  },
  mounted () {
    // this.handleCurrentChange(1)
    this.orgQueryIndex(1)
  }
}
</script>
<style scoped>
  .el-select .el-input {
    width: 200px;
  }
  .input-with-select .el-input-group__prepend {
    background-color: #fff;
  }
  .el-row {
    margin-bottom: 5px;
    display: flex;
    flex-wrap: wrap
  }
  .select-index {
    background-color: #bfc1c0;
  }
  .el-card {
    position:relative;
  }
  .tick {
    border:1px;
    border-style:solid;
    border-color:#66b1ff;
  }
  .fork {
    border:1px;
    border-style:solid;
    border-color: #ff1500;
  }

  .fork >>>.el-card__body{
    padding: 11px;
    height: 50%;
  }
  .tick i {

    display: block;

    position: absolute;

    border-bottom: 25px solid #66b1ff;

    border-left: 2rem solid transparent;

    width: 0px;

    height: 0px;

    bottom: 0rem;

    right: 0;

  }

  .tick i:after {

    position: absolute;

    content: '\2714';

    color: #fff;

    left: -1rem;

    top: 0rem;

    font-size: 1.2rem;

  }

  .fork i {

    display: block;

    position: absolute;

    border-bottom: 25px solid #ff1500;

    border-left: 2rem solid transparent;

    width: 0px;

    height: 0px;

    bottom: 0rem;

    right: 0;

  }

  .fork i:after {

    position: absolute;

    content: '×';

    color: #fff;

    left: -1rem;

    top: 0.3rem;

    font-size: 1.3rem;
    /*transform:rotate(-5deg)*/
  }
</style>
