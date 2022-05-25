<template>
  <div>
      <el-row>
        <el-col :span="1">
          &nbsp;
        </el-col>
        <el-col :span="3">
          <el-upload
            action="#"
            :before-upload="beforeUpload"
            multiple
            :auto-upload="true"
            accept=".jpg,.jpeg,.png,.gif,.pdf,.JPG,.JPEG,.GIF,.PDF"
          >
            <el-button type="primary" plain size="medium" style="border-radius: 0px"><i class="el-icon-upload2"></i>文件上传</el-button>
          </el-upload>
        </el-col>
        <el-col :span="8">
<!--            <el-button type="primary" plain size="medium" style="border-radius: 0px"><i class="el-icon-upload2"></i>文件上传</el-button>-->
          <el-button plain size="medium" style="border-radius: 0px" @click="addFolder()"><i class="el-icon-delete"></i>创建文件夹</el-button>
          <el-button plain size="medium" style="border-radius: 0px" @click="batchHandleDelete(multipleSelection)"><i class="el-icon-delete"></i>批量删除</el-button>
          <el-tooltip class="item" effect="dark" placement="right-start" style="border-radius: 0px">
            <div slot="content">用于管理构件设计过程中使用的资源<br/>包括图片、数据等</div>
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
      <el-col :span="24">
        <el-breadcrumb class="breadcrumb-container" separator-class="el-icon-arrow-right">
          <el-breadcrumb-item v-for="path in paths" :key="path.folderId">
            <el-link @click="handelFolderChange(path.folderId)" :disabled="currentFolder.folderId === path.folderId">{{path.dataName}}</el-link>
          </el-breadcrumb-item>
        </el-breadcrumb>
      </el-col>
    </el-row>
<!--    <el-row>-->
<!--      <el-col :span="24">-->
<!--        <i class="el-icon-folder"></i>-->
<!--        <span>..</span>-->
<!--      </el-col>-->
<!--    </el-row>-->
<!--    <el-row>-->
<!--&lt;!&ndash;      <el-col v-for="(folder,index)in folders" :key="index">&ndash;&gt;-->
<!--&lt;!&ndash;      </el-col>&ndash;&gt;-->
<!--      <el-col :span="24" v-for="(folder,index) in folders" :key="index">-->
<!--        <el-col :span="20">-->
<!--          <i class="el-icon-folder"></i>-->
<!--          <el-input v-if ="folder.status === '1'" v-model="folder.name"></el-input>-->
<!--          <span v-else @click="handelFolderChange(folder.id)">{{folder.name}}</span>-->
<!--        </el-col>-->
<!--        <el-col :span="4" >-->
<!--          <el-button v-if="folder.status === '1'" @click="createFolder(folder)" type="text" size="small" >保存</el-button>-->
<!--        </el-col>-->
<!--      </el-col>-->
<!--    </el-row>-->
      <el-row>
        <template>
          <el-table
            ref="multipleTable"
            :data="fileInfos"
            tooltip-effect="dark"
            style="width: 100%"
            @selection-change="handleSelectionChange"
            :height="this.$store.state.tableHeight"
            :show-header="true"
          >
            <el-table-column
              type="selection"
              width="30"
            >
            </el-table-column>
            <el-table-column
              prop="name"
              label="名称"
              min-width="70%"
            >
<!--              <template slot-scope="scope" v-if="scope.row.status">-->
<!--                -->
<!--              </template>-->
              <template slot-scope="scope" >
                <span v-if="scope.row.status === '1'">
                  <el-col :span="1">
                    <i class="el-icon-folder" style="font-size: 25px;color: #ffc400"></i>
                  </el-col>
                  <el-col :span="10">
                    <el-input v-model="scope.row.dataName"></el-input>
                  </el-col>
                  <el-col :span="1">
                    &nbsp;
                  </el-col>
                  <el-col :span="3">
                    <el-button @click="createFolder(scope.row)" type="text" size="small" >保存</el-button>
                    <el-button @click="cancelFolder(scope.row)" type="text" size="small" >取消</el-button>
                  </el-col>
                </span>
                <span v-else>
                  <el-link @click="handelFolderChange(scope.row.folderId)"  v-if="scope.row.dataType === 'folder'"><i class="el-icon-folder" style="font-size: 25px;color: #ffc400"></i>&nbsp;{{scope.row.dataName}}</el-link>
                  <span v-else>{{scope.row.dataName}}</span>
                </span>
              </template>
            </el-table-column>
            <el-table-column
              prop="date"
              label="日期"
              min-width="10%"
              >
            </el-table-column>
            <el-table-column
              prop="address"
              label="操作"
              min-width="15%"
            >
              <template slot-scope="scope">
                <span v-if="scope.row.dataType === 'file'">
                  <el-button @click="viewClick(scope.row)" type="text" size="small" >查看</el-button>
                  <el-button @click="downloadClick(scope.row)" type="text" size="small">下载</el-button>
                </span>
                <el-button type="text" size="small" @click="deleteClick(scope.row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
          <div style="width:100%;text-align: right">
            <el-pagination
              layout="prev, pager, next"
              :total="50">
            </el-pagination>
          </div>

        </template>
      </el-row>

    <!--查看详情右侧扩展页-->
    <el-drawer
      title="文件详情"
      :visible.sync="drawer"
      :with-header="true"
      @opened="drawerPdfViewerOpen"
      size="55%">
      <el-row>
        <el-col :span="1">
          &nbsp;
        </el-col>
        <el-col :span="22">
          <iframe id="pdfViewer" src="" width="100%" height="100%" :style="{width: '100%',height:'480px'}" frameborder="0" v-if="showView"></iframe>
          <el-card shadow="hover" style="margin-top:20px" v-else>
            <el-image :src="fileSrc">
              <div slot="placeholder" class="image-slot">
                加载中<span class="dot">...</span>
              </div>
            </el-image>
          </el-card>
        </el-col>
        <el-col :span="1">
          &nbsp;
        </el-col>
      </el-row>
<!--      <el-row>-->
<!--        <el-col :span="1">-->
<!--          &nbsp;-->
<!--        </el-col>-->
<!--        <el-col :span="22" >-->
<!--&lt;!&ndash;          <template>&ndash;&gt;-->
<!--&lt;!&ndash;            <el-table&ndash;&gt;-->
<!--&lt;!&ndash;              :data="fileInfo"&ndash;&gt;-->
<!--&lt;!&ndash;              border&ndash;&gt;-->
<!--&lt;!&ndash;              :show-header="false"&ndash;&gt;-->
<!--&lt;!&ndash;              style="width: 100%;margin-top: 30px">&ndash;&gt;-->
<!--&lt;!&ndash;              <el-table-column&ndash;&gt;-->
<!--&lt;!&ndash;                prop="attr"&ndash;&gt;-->
<!--&lt;!&ndash;                label="属性"&ndash;&gt;-->
<!--&lt;!&ndash;                width="100">&ndash;&gt;-->
<!--&lt;!&ndash;              </el-table-column>&ndash;&gt;-->
<!--&lt;!&ndash;              <el-table-column&ndash;&gt;-->
<!--&lt;!&ndash;                prop="value"&ndash;&gt;-->
<!--&lt;!&ndash;                label="值">&ndash;&gt;-->
<!--&lt;!&ndash;              </el-table-column>&ndash;&gt;-->
<!--&lt;!&ndash;            </el-table>&ndash;&gt;-->
<!--&lt;!&ndash;          </template>&ndash;&gt;-->
<!--        </el-col>-->
<!--        <el-col :span="1">-->
<!--          &nbsp;-->
<!--        </el-col>-->
<!--      </el-row>-->
    </el-drawer>
  </div>
</template>

<script>
import axios from 'axios'
export default {
  name: 'ResourceManager',
  data () {
    return {

      tableData: [{
        date: '2020-01-16',
        filesize: '123k',
        filename: 'logo.jpg'
      }, {
        date: '2020-01-16',
        filesize: '123k',
        filename: 'D3F.pdf'
      }],
      fileInfos: [],
      searchKey: '',
      drawer: false,
      showView: '',
      fileSrc: '',
      multipleSelection: [],
      paths: [],
      currentFolder: {
        dataName: '/',
        folderId: 'root'
      },
      folders: []
    }
  },
  methods: {
    viewClick (row) {
      this.drawer = true
      if (row.fileType === 'pdf') {
        this.showView = true
      } else {
        this.showView = false
      }
      axios.get(this.GLOBAL.webappApiConfig.OrganizationResource.organizationFileView.url + '?objectId=' + row.objectId + '&fileType=' + row.fileType)
        .then(res => {
          if (this.showView) {
            sessionStorage.setItem('pdfData', res.data.content.file)
            let pdfViewerFrame = document.getElementById('pdfViewer')
            pdfViewerFrame.focus()
            pdfViewerFrame.src = '/static/pdfjs/web/pdfviewer.html'
          } else {
            this.fileSrc = res.data.content.file
          }
        })
        .catch(function (error) {
          console.log(error)
        })
    },
    handleSelectionChange (val) {
      this.multipleSelection = val
      val.forEach((val, index) => {
        this.fileInfos.forEach((v, i) => {
          // id 是每一行的数据id
          if (val.objectId === v.objectId) {
            this.multipleSelection.index = i
          }
        })
      })
    },
    handleDelete (index, row) {
      this.tableData.splice(index, 1)
      // console.log(index, row)
      this.$message({
        showClose: true,
        message: '删除成功',
        type: 'success'
      })
    },
    batchHandleDelete (msrow) {
      msrow.forEach(index => {
        this.tableData.splice(index, 1)
      })
      this.$message({
        showClose: true,
        message: '批量删除成功',
        type: 'success'
      })
    },
    handelFolderChange (val) {
      this.fileInfos = []
      axios.get(this.GLOBAL.webappApiConfig.OrganizationResource.organizationFileQuery.url + '?folderId=' + val)
        .then(res => {
          if (res.data.content != null && res.data.content.length > 0) {
            this.fileInfos = res.data.content
            this.currentFolder = this.fileInfos.shift()
            console.log(this.fileInfos)
          }
          axios.get(this.GLOBAL.webappApiConfig.OrganizationResource.organizationFileAllPath.url + '?folderId=' + val)
            .then(res => {
              this.paths = res.data.content
            })
            .catch(function (error) {
              console.log(error)
            })
        })
        .catch(function (error) {
          console.log(error)
        })
    },
    addFolder () {
      this.fileInfos.unshift({dataName: '', fatherFolderId: '', status: '1'})
    },
    createFolder (value) {
      axios.post(this.GLOBAL.webappApiConfig.OrganizationResource.organizationCreateFolder.url,
        {dataName: value.dataName, fatherFolderId: this.currentFolder.folderId})
        .then(res => {
          // this.handelFolderChange(this.currentFolder.folderId)
          value.folderId = res.data.content
          value.status = '0'
          value.dataType = 'folder'
        })
        .catch(function (error) {
          console.log(error)
        })
      // this.$forceUpdate()
    },
    cancelFolder (index) {
      this.fileInfos.splice(index, 1)
    },
    beforeUpload (file) {
      let fd = new FormData()
      fd.append('file', file)
      fd.append('folderId', this.currentFolder.folderId)
      axios.post(this.GLOBAL.webappApiConfig.OrganizationResource.organizationFileUpload.url, fd)
        .then(res => {
          this.handelFolderChange(this.currentFolder.folderId)
        }).catch(function (error) {
          console.log(error)
        })
    },
    batchDelete (files) {
      files.forEach(file => {
        this.fileInfos.splice(file.index, 1)
      })
      axios.post(this.GLOBAL.webappApiConfig.OrganizationResource.organizationFileDel.url, files)
        .then(res => {
          // this.handelFolderChange(this.currentFolder.folderId)
        }).catch(function (error) {
          console.log(error)
        })
    },
    deleteClick (row) {
      this.fileInfos.forEach((v, i) => {
        // id 是每一行的数据id
        if (row.objectId === v.objectId) {
          row.index = i
        }
      })
      let delParam = []
      delParam[0] = row
      this.batchDelete(delParam)
    },
    downloadClick (row) {
      axios.get(this.GLOBAL.webappApiConfig.OrganizationResource.organizationFileUrl.url + '/' + row.objectId)
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
    drawerPdfViewerOpen () {
      // pdfViewerFrame.src = this.pdfContent
    }
  },
  mounted () {
    this.handelFolderChange(this.currentFolder.folderId)
  },
  created () {
  }
}
</script>
<style scoped>

</style>
