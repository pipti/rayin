
<template>
  <div>
    <!--    <el-row :gutter="5">-->
<!--    <el-row>-->
<!--      <el-page-header @back="goBack" content="搜索条件配置">-->
<!--      </el-page-header>-->
<!--    </el-row>-->
    <el-row :gutter="5" :style="{marginTop:'20px',height: this.$store.state.boxHeight}">
      <el-col :span="6">
        <el-row>
        <el-tabs :tab-position="tabPosition" type="border-card" :style="{height:(this.$store.state.windowHeight - 160)+ 'px'}" id="resourceTab">
          <el-tab-pane label="数据">
            <el-row :span="24">
              <div style="font-size: 12px;color:#909399">txt、json文件，且不超过3M</div>
<!--              <div style="color:#409EFF">{{jsonInfo.substring(5)}}</div>-->
<!--              <div @click="orgSetIndexClick()" style="cursor:pointer;text-align: left;width:50px" id="orgAddIcon">-->
<!--                <el-tooltip class="item" effect="dark" content="请先添加项目" placement="right" :manual="true" transition="el-fade-in-linear"-->
<!--                            :hide-after="5000" :open-delay="1000">-->
<!--                  <i class="el-icon-circle-plus-outline" ></i>-->
<!--                </el-tooltip>-->
<!--              </div>-->
            </el-row>
            <el-row>
              <el-col :span="20">
                &nbsp;
                <div style="color:#409EFF">{{jsonInfo.substring(5)}}</div>
              </el-col>
              <el-col :span="4">
              </el-col>
            </el-row>
            <el-row>
              <!--数据-->
              <div :style="{height: this.$store.state.boxHeight,overflow:scroll}" style="width:100%;">
                <vue-json-pretty
                  :data="jsonData"
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
                  @click="jsonHandleClick(...arguments, 'complexTree')"
                  style="font-size: 12px;"
                >
                </vue-json-pretty>
              </div>
            </el-row>

<!--        </el-card>-->
          </el-tab-pane>
<!--          <el-tab-pane label="资源">资源</el-tab-pane>-->
        </el-tabs>
        </el-row>
        <el-row>
          &nbsp;
        </el-row>
        <el-row>
          <el-col :span="8">
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
              <el-button icon="el-icon-coin" title="加载数据" type="primary" class="button-fillet-left-long" size="small">加载</el-button>
            </el-upload>
          </el-col>
          <el-col :span="8" align="right">
            <el-button icon="el-icon-connection" title="添加" @click="orgSetIndexClick" type="primary" class="button-fillet-right-long" size="small">添加</el-button>
          </el-col>
        </el-row>
      </el-col>
      <el-col :span="0.5">
        &nbsp;
      </el-col>
      <el-col :span="17">
        <el-table
          :data="orgIndexes"
          :cell-style="cellStyle"
          :header-cell-style="headerCellStyle"
          tooltip-effect="dark"
          style="width: 100%"
          :height="this.$store.state.tableHeight">
          <el-table-column
            label="搜索条件"
            prop="indexNameChn"
            width="100"
          >
            <template slot-scope="scope">
              <el-input v-if="scope.row.status" v-model="scope.row.indexNameChn"></el-input>
              <span v-else>{{scope.row.indexNameChn}}</span>
            </template>
          </el-table-column>
          <el-table-column
            label="数据路径"
            prop="jsonPath"
            width="300"
          >
            <template slot-scope="scope">
              <el-input v-if="scope.row.status" v-model="scope.row.jsonPath" @click.native="focusJsonPath(scope.$index)"></el-input>
              <span v-else>{{scope.row.jsonPath}}</span>
            </template>
          </el-table-column>
          <el-table-column
            label="创建日期"
            prop="createDate"
            width="100"
          >
            <template slot-scope="scope">
              <el-popover trigger="hover" placement="top">
                {{ scope.row.createDate }}
                <div slot="reference" class="name-wrapper">
                  <div v-if="scope.row.createDate !== null">{{ scope.row.createDate.substring(0,10) }}</div>
                </div>
              </el-popover>
            </template>
          </el-table-column>
          <el-table-column
            label="更新日期"
            prop="updateDate"
            width="100"
          >
            <template slot-scope="scope">
              <el-popover trigger="hover" placement="top">
                {{ scope.row.updateDate }}
                <div slot="reference" class="name-wrapper">
                  <div v-if="scope.row.updateDate !== null">{{ scope.row.updateDate.substring(0,10) }}</div>
                </div>
              </el-popover>
            </template>
          </el-table-column>
          <el-table-column
            label="更新用户"
            prop="username"
            width="100"
          >
          </el-table-column>
          <el-table-column
            label="操作"
            prop="jsonPath"
            width="200"
          >
            <template slot-scope="scope">
              <el-button class="button-fillet-left" v-if="scope.row.status" @click="orgSaveIndexClick(scope.row)" type="primary" size="mini">保存</el-button>
              <el-button v-else @click="orgEditIndexClick(scope.$index,scope.row)" type="primary" class="button-fillet-left" size="mini" >编辑</el-button>
              <el-button @click="orgDeleteIndexClick(scope.$index,scope.row)" type="primary" class="button-fillet-right-white" size="mini">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <div style="width:100%;text-align: right">
          <el-pagination
            @size-change="this.handlePageSizeChange"
            :page-sizes="[5,10, 20, 50, 100]"
            @current-change="orgQueryIndex"
            layout="total,prev, pager, next,sizes"
            :total="indexTotal"
            :current-page="indexCurrentPage"
            :page-size="this.$store.state.pageSize">
          </el-pagination>
        </div>
      </el-col>
      <!--      </VueDraggableResizable>-->
      <!--      <VueDraggableResizable-->
      <!--        :w="580"-->
      <!--        :h="500"-->
      <!--        :parent="true"-->
      <!--        :debug="false"-->
      <!--        :min-width="200"-->
      <!--        :min-height="500"-->
      <!--        :snap="true"-->
      <!--        :snapTolerance="50"-->
      <!--        :x="350"-->
      <!--        :is-conflict-check="true"-->
      <!--      >-->
<!--      <el-col :span="17">-->
<!--        <el-tabs type="border-card" @tab-click="tabHandleClick" :style="{height:(this.$store.state.windowHeight - 150)+ 'px'}" id="designTab">-->
<!--          <el-tab-pane label="设计" style="padding:0px">-->
<!--            <div>-->
<!--              <tinymce-editor ref="editor"-->
<!--                              v-model="elHtmlCode"-->
<!--                              :disabled="disabled"-->
<!--                              @objectSelected="objectSelected"-->
<!--                              :init="editorInit"-->
<!--                              @editorLoadComplete="editContentInit">-->
<!--              </tinymce-editor>-->
<!--              &lt;!&ndash;<button @click="clear">清空内容</button>&ndash;&gt;-->
<!--              &lt;!&ndash;{{elHtmlCode}}&ndash;&gt;-->
<!--              &lt;!&ndash;<span  @dblclick="dataDblClick">双击添加标签th</span>&ndash;&gt;-->
<!--              &lt;!&ndash;<button @click="addTh">添加标签</button>&ndash;&gt;-->
<!--              &lt;!&ndash;<button @click="delTh">删除标签</button>&ndash;&gt;-->
<!--              <br>-->
<!--              <div id="capture"></div>-->
<!--            </div>-->
<!--          </el-tab-pane>-->
<!--          <el-tab-pane label="专家模式" :style="{height:(this.$store.state.windowHeight - 150)+ 'px'}">-->
<!--            &lt;!&ndash;<textarea style="width:100%" :style="{height:(this.$store.state.windowHeight - 150)+ 'px'}">{{elHtmlCode}}</textarea>&ndash;&gt;-->
<!--            <div :style="{height:(this.$store.state.windowHeight - 190)+ 'px'}" style="overflow:scroll;flex-grow: 1;">-->
<!--              <codemirror  ref="elDesginCodeEditor" v-model="elHtmlCode"  :options="cmOptions" @change="codeEditorChanged"></codemirror>-->
<!--            </div>-->
<!--          </el-tab-pane>-->
<!--          <el-tab-pane label="PDF预览" >-->
<!--            <iframe id="pdfViewer" src="" width="100%" height="100%" :style="{width: '100%',height:this.$store.state.iframeHeight}" frameborder="0" ></iframe>-->
<!--          </el-tab-pane>-->
<!--          <el-tab-pane label="帮助" >-->
<!--            <div :style="{height:(this.$store.state.windowHeight - 190)+ 'px'}" style="overflow:scroll;flex-grow: 1;" >-->
<!--              <markdown-view></markdown-view>-->
<!--            </div>-->
<!--          </el-tab-pane>-->
<!--        </el-tabs>-->
<!--      </el-col>-->
      <!--      </VueDraggableResizable>-->
    </el-row>
    <!--    <div>-->
    <!--      <VueDraggableResizable :w="100" :h="100" v-on:dragging="onDrag" v-on:resizing="onResize" :parent="true">-->
    <!--        <p><br>-->
    <!--          X: {{ x }} / Y: {{ y }} - Width: {{ width }} / Height: {{ height }}</p>-->
    <!--      </VueDraggableResizable>-->
    <!--    </div>-->
<!--    <el-dialog title="循环绑定控件选择" :visible.sync="dialogDataBindSelectVisible" :before-close="dataBindDiagleHandClose">-->
<!--      <el-form :model="eachDataBindSelectForm">-->
<!--        <el-radio v-model="eachDataBindSelectForm.ctrlRadio" label="tr" >行元素</el-radio>-->
<!--        <el-radio v-model="eachDataBindSelectForm.ctrlRadio" label="table" >整个表格</el-radio>-->
<!--      </el-form>-->
<!--      <div slot="footer" class="dialog-footer">-->
<!--        &lt;!&ndash;<el-button @click="dialogDataBindSelectVisible = false">取 消</el-button>&ndash;&gt;-->
<!--        <el-button type="primary" @click="dialogDataBindSelectVisible = false">确 定</el-button>-->
<!--      </div>-->
<!--    </el-dialog>-->
    <el-drawer
      title="JSON资源"
      :visible.sync="orgDataViewer"
      :with-header="true"
      @opened="jsonSourceViewerOpened"
      size="800px">
      <el-row>
        <el-col :span="1">
          &nbsp;
        </el-col>
        <el-col :span="22">
          <el-table
            :data="orgData"
            style="width: 100%"
            :height="this.$store.state.tableHeight">
            <el-table-column
              label="名称"
              prop="dataName"
              width="100"
            >
            </el-table-column>
            <el-table-column
              label="数据"
              prop="data"
              width="200"
            >
              <template slot-scope="scope">
                <el-popover trigger="hover" placement="top-start" width="300">
                  <span>{{ scope.row.data }}</span>
                  <div slot="reference" class="name-wrapper">
                    <div v-if="scope.row.data !== null">{{ scope.row.data.substring(0,10) + ' ....' }}</div>
                  </div>
                </el-popover>
              </template>
            </el-table-column>
<!--            <el-table-column-->
<!--              label="创建时间"-->
<!--              prop="createDate"-->
<!--              width="150"-->
<!--            >-->
<!--            </el-table-column>-->
<!--            <el-table-column-->
<!--              label="更新时间"-->
<!--              prop="updateDate"-->
<!--              width="150"-->
<!--            >-->
<!--            </el-table-column>-->
            <el-table-column
              label="所属用户"
              prop="username"
              width="100"
            >
            </el-table-column>
            <el-table-column
              label="操作"
              prop=""
              width="200"
            >
              <template slot-scope="scope">
                <el-button @click="orgLoadIndexClick(scope.row)" type="warning" size="mini">载入</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
        <el-col :span="1">
          &nbsp;
        </el-col>
      </el-row>
    </el-drawer>
  </div>
</template>

<script>
import VueJsonPretty from 'vue-json-pretty'
// eslint-disable-next-line no-unused-vars
import axios from 'axios'

// import VueDragResize from 'vue-drag-resize'
// import VueDraggableResizable from 'vue-draggable-resizable-gorkys'
// import 'vue-draggable-resizable-gorkys/dist/VueDraggableResizable.css'
// import router from "../../router";
/* eslint-disable */

  // Import TinyMCE
  //import tinymce from 'tinymce/tinymce';

  // A theme is also required
  //import 'tinymce/themes/silver/theme';
  //import Editor from '@tinymce/tinymce-vue'
  // Any plugins you want to use has to be imported
  //import 'tinymce/plugins/paste';
  //import 'tinymce/plugins/link';
  // import TinymceEditor from '@/components/tinymce-editor'
  // import HtmlCapture from '@/components/html-capture'
  // import MarkdownView from '@/components/markdown-view'
  import { codemirror } from 'vue-codemirror'
import store from "../../../vuex/store";

  // require("codemirror/mode/htmlmixed/htmlmixed.js")
  // require('codemirror/addon/fold/foldcode.js')
  // require('codemirror/addon/fold/foldgutter.js')
  // require('codemirror/addon/fold/brace-fold.js')
  // require('codemirror/addon/fold/xml-fold.js')
  // require('codemirror/addon/fold/indent-fold.js')
  // require('codemirror/addon/fold/markdown-fold.js')
  // require('codemirror/addon/fold/comment-fold.js')
  // require('codemirror/addon/fold/comment-fold.js')

  export default {
    name: 'ElementDesign',
    components: {
      VueJsonPretty,
    },
    data () {
      return {
        orgIndexes:[],
        indexCurrentPage: 1,
        dataCurrentPage: 1,
        dataTotal: 0,
        indexTotal: 0,
        currentJsonPath: -1,
        orgDataViewer: false,
        orgData:[],
        cellStyle: {
          'text-align': 'center'
        },
        headerCellStyle: {
          'text-align': 'center',
          'background-color': '#F3F6F6',
          'color': '#BCC3C3',
          'font-weight': 'normal'
        },
        editorInit:{
          content_style: ''
        },
        elFavourites: [
        ],
        elSysFavourites:[
          {
            url: '/static/testresource/elements/电子保单.html',
            data:'/static/testresource/elements/电子保单.json'
          },
          {
            url: '/static/testresource/elements/009_familyproof.html',
            data: '/static/testresource/elements/009_familyproof.json'
          }
          ,
          {
            url: '/static/testresource/elements/封面.html',
            data:'/static/testresource/elements/电子保单.json'
          },
          {
            url: '/static/testresource/elements/八版保单页.html',
            data:'/static/testresource/data/八版保单页.json'
          },
          {
            url: '/static/testresource/elements/带水印.html'
          }
        ],
        elHtmlCode: '',
        disabled: false,
        labelPosition: 'top',
        fileList: [],
        tabPosition: 'left',
        // JSON 数据
        jsonData: {
        },
        marginTop: 1.00,
        marginBottom: 1.00,
        marginLeft: 1.00,
        marginRight: 1.00,
        scroll:'scroll',
        jsonInfo: '',
        selectableType: 'single',
        showSelectController: false,
        showLength: true,
        showLine: true,
        showDoubleQuotes: true,
        highlightMouseoverNode: true,
        highlightSelectedNode: true,
        selectOnClickNode: true,
        collapsedOnClickBrackets: true,
        path: 'root',
        deep: 10,
        jsonPath: '',
        jsonValue: '',
        jsonPrettyValue: '',
        boxHeight: this.$store.state.boxHeight,
        dialogDataBindSelectVisible:false,

        eachDataBindSelectForm:{
          ctrlRadio: 'tr'
        }
      }
    },
    methods: {
      elLoad(el){

//      v.style = 'border:1px solid black'
//      console.log('elLoad' + el.url)
//      console.log(el)

        if (el.url && el.url.length > 0) {
          // 加载中
          this.loading = true
          let param = {
            accept: 'text/html, text/plain'
          }
          this.$http.get(el.url, param).then((response) => {
            this.loading = false
            // 处理HTML显示
            this.elHtmlCode = response.data
            this.loadCompletedFlag = true
            console.log('模板加载成功')
          }).catch(() => {
            this.loading = false
            this.elHtmlCode = '加载中'
          })


          if (el.data && el.data.length > 0) {
            // 加载中
            this.loading = true
            let param = {
              accept: 'text/html, text/plain'
            }
            this.$http.get(el.data, param).then((response) => {
              this.loading = false
              // 处理HTML显示
              this.jsonData = response.data
              // console.log(response.data)
              console.log('数据加载成功')
            }).catch(() => {

            })
          }else{
            console.log('无测试数据！')
          }
        }else{
          this.jsonData = el.testData
          this.elHtmlCode = el.content
        }


      },
      delTh (){
//      tinymce.activeEditor.execCommand('Delete')
//      let editorSelectElement = this.$store.state.editorSelectedElement.toElement
//      console.log(editorSelectElement)
//      this.editorSelectElement.removeAttribute('data-th-text')
//      this.editorSelectElement.removeAttribute('data-th-each')
      },
      objectSelected (e){
        //console.log(e)
      },
      // 点击json节点显示路径数据
      jsonHandleClick (path, data){
        this.jsonInfo = path
        this.jsonPath = path
        this.jsonValue = data
      },
      // 数据绑定按钮
      dataBindClick (){
          let jsonPathNoRoot = this.jsonPath.substring(5)
          let regx = /\[(\d)\]/
          let p = this.jsonPath.search(regx)
          let arrayPrefix = this.jsonPath.substring(5,this.jsonPath.indexOf('['))
          let arrayName = this.jsonPath.substring(5,this.jsonPath.indexOf(']') + 1)
          console.log(this.currentJsonPath)
          p > 0 ? this.orgIndexes[this.currentJsonPath].jsonPath = arrayPrefix : this.orgIndexes[this.currentJsonPath].jsonPath = jsonPathNoRoot
      },
      saveConfirmOpen() {
        const h = this.$createElement;
        this.$msgbox({
          title: '消息',
          message: h('p', null, [
            h('span', null, '内容可以是 '),
            h('i', { style: 'color: teal' }, 'VNode')
          ]),
          showCancelButton: true,
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          beforeClose: (action, instance, done) => {
            if (action === 'confirm') {
              instance.confirmButtonLoading = true;
              instance.confirmButtonText = '执行中...';
              setTimeout(() => {
                done();
                setTimeout(() => {
                  instance.confirmButtonLoading = false;
                }, 300);
              }, 3000);
            } else {
              done();
            }
          }
        }).then(action => {
          this.$message({
            type: 'info',
            message: 'action: ' + action
          });
        });
      },
      // 清空内容
      clear () {
        this.$refs.editor.clear()
      },
      handleExceed(files, fileList) {
        this.$message.warning(`当前限制选择 3 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件`);
        console.log(`当前限制选择 3 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件`);
      },
      // beforeRemove(file, fileList) {
      //   return this.$confirm(`确定移除 ${ file.name }？`);
      // },
      handleOnChange(file, fileList){
        this.$refs['json-load'].clearFiles();
        let reader = new FileReader()
        reader.readAsText(file.raw,'UTF-8');

        reader.onload=(e)=>{
          this.jsonData = JSON.parse(e.target.result)
        }

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
      goBack () {
        this.$router.push({name: 'Guide', params: {}})
      },
      orgSetIndexClick() {
        if (this.jsonInfo === ''){
          this.$message({
            showClose: true,
            message: '请选择要绑定的数据项',
            type: 'error'
          })
          return
        }
        this.orgIndexes.push({ indexNameChn: '', jsonPath: '', createDate: null, updateDate: null,status:true});
        this.currentJsonPath = this.orgIndexes.length - 1
        if (this.currentJsonPath == -1) {
          this.$message({
            showClose: true,
            message: '请选择一个绑定对象',
            type: 'error'
          })
          return;
        }
        this.dataBindClick()
      },
      orgEditIndexClick(index,el){
        el.status = true;
        this.focusJsonPath(index)
      },
      orgSaveIndexClick(el){
        axios.post(this.GLOBAL.webappApiConfig.OrganizationIndex.OrganizationIndexSave.url,
          el)
          .then(res => {
            let hasEditIndex = false;
            this.orgIndexes.forEach(function (item,index) {
                if (item.status) {
                  hasEditIndex = true;
                  return
                }
            })
            if (!hasEditIndex) {
              this.orgQueryIndex(this.indexCurrentPage)
            }
            this.currentJsonPath = -1
          })
          .catch(function (error) {
            console.log(error)
          })
        el.status = false;
      },
      orgDeleteIndexClick(index,el){
        if (!el.status) {
          this.$confirm("删除此条件将无法对此条件精确搜索, 是否继续?", "提示", {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning"
          })
            .then(() => {
              axios.post(this.GLOBAL.webappApiConfig.OrganizationIndex.OrganizationIndexDel.url,
                el)
                .then(res => {
                  this.orgQueryIndex(this.indexCurrentPage)
                })
                .catch(function (error) {
                  console.log(error)
                })
              this.orgIndexes.splice(index, 1);
              // this.$message({
              //   type: "success",
              //   message: "删除成功!"
              // });
            })
            .catch(() => {});
        } else {
          this.orgIndexes.splice(index, 1);
        }

      },
      orgQueryIndex(val) {
        axios.get(this.GLOBAL.webappApiConfig.OrganizationIndex.OrganizationIndexQuery.url + '?pageCurrent=' + val + '&pageSize=' + this.$store.state.pageSize)
          .then(res => {
            res.data.content.records.forEach(function (item, index) {
              item.status = false;
            });
            this.orgIndexes = res.data.content.records
            this.indexTotal = res.data.content.total
            this.indexCurrentPage = res.data.content.current
          })
          .catch(function (error) {
          })
      },
      focusJsonPath(index) {
        console.log(index)
        this.currentJsonPath = index
      },
      orgDataClick(){
        this.orgDataViewer = true;
      },
      jsonSourceViewerOpened() {
        axios.get(this.GLOBAL.webappApiConfig.OrganizationResource.OrganizationDataQuery.url + '?pageCurrent=' + this.dataCurrentPage + '&pageSize=' + this.$store.state.pageSize,)
          .then(res => {
            this.orgData = res.data.content.records
            this.dataTotal = res.data.content.total
            this.dataCurrentPage = res.data.content.current
          })
          .catch(function (error) {
          })
      },
      orgLoadIndexClick(el) {
        this.jsonData = JSON.parse(el.data)
        this.orgDataViewer = false;
      }
    },
    mounted() {
      this.orgQueryIndex(this.indexCurrentPage)
    },created(){
      // if (this.$route.params != undefined) {
      //   this.$store.state.organizationId = this.$route.params.org.organizationId
      //   this.$store.state.organizationName = this.$route.params.org.organizationName
      // }
      // console.log(this.$store.state.organizationId)
      //
      // this.orgQueryIndex(this.indexCurrentPage)
    },
    watch: {
      dialogDataBindSelectVisible(val){
        if(val === false){
          console.log("diagle close")
          this.dataBindDiagleHandClose()
        }
      }
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

  .el-tabs .el-tabs--left .el-tabs--border-card{
    height:400px;
    overflow:scroll;
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
    overflow:scroll;
    flex-grow: 1;
    overflow-y: scroll;
  }

  #resourceTab >>>> .el-tab-pane{
    overflow:scroll;
    flex-grow: 1;
    overflow-y: scroll;
  }

  /* 代码编辑器高度 */
  .el-tab-pane >>> .CodeMirror{
    min-height: 1000px;
  }
  #designTab >>> .tox-tinymce{
    border-left:0px;
    border-right:0px;
    border-top:0px;
  }
  .tinymce-editor{
    border:0px;
  }
  #designTab >>> .tox-edit-area{
    /*background-color: #0f74a8;*/
    /*align-content:center;*/
  }
  #designTab >>> #elEditor_ifr{
    /* 可模拟控制宽度 */
    width: 21cm;
    /*min-height: 29.2cm;*/
    /*display: block;*/
    margin:0.5cm 5% 0.1cm 5%;
    box-shadow: 0 0 0.5cm rgba(0,0,0,0.5);
    /* 可模拟控制边距 */
    padding:1cm;
  }

  .el-button--warning:hover{
    background-color: #EF9E6F;
    border-color: #EF9E6F;
  }
  .el-button--warning:focus{
    background-color: #EF9E6F;
    border-color: #EF9E6F;
  }
</style>
