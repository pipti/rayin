
<template>
  <div>
<!--    <el-row :gutter="5">-->
    <el-row>
      <el-page-header @back="goBack" content="构件设计">
      </el-page-header>
    </el-row>
      <el-row :gutter="5" :style="{marginTop:'20px',height: this.$store.state.boxHeight}">
<!--      <VueDraggableResizable-->
<!--        :w="340"-->
<!--        :h="500"-->
<!--        :parent="true"-->
<!--        :debug="false"-->
<!--        :min-width="200"-->
<!--        :min-height="500"-->
<!--        :snap="true"-->
<!--        :snapTolerance="50"-->
<!--      >-->
<!--        :isConflictCheck="true" :is-conflict-check="true"-->
        <el-col :span="7">
        <el-tabs :tab-position="tabPosition" type="card" :style="{height:(this.$store.state.windowHeight - 150)+ 'px'}" id="resourceTab" v-model="activeName">
          <el-tab-pane label="设置">
            <div :style="{height: (this.$store.state.windowHeight - 150)+ 'px'}">
              <div style="width:100%;text-align: right">
                <el-button-group>
                  <el-button type="primary" icon="el-icon-setting" @click="pageAttrsSet" size="mini">设置</el-button>
                  <el-tooltip class="item" effect="dark" placement="right-start" style="border-radius: 0px">
                    <div slot="content">保存当前版本，保存修改并不会影响模板中配置此构件的内容，如果需要同步模板中的构件内容，需要在构件列表中选择"关联模板"，并选择相应的模板进行同步。</div>
                    <el-button type="primary" icon="el-icon-check" size="mini" @click="elementSaveClick">保存</el-button>
                  </el-tooltip>
                  <el-button type="primary" @click="elModifyHistoryViewerClick(1)" size="mini" v-if="elementForm.updateTimeStr !== ''" icon="el-icon-notebook-1">修改历史</el-button>

                  <el-tooltip class="item" effect="dark" placement="right-start" style="border-radius: 0px">
                    <div slot="content">版本号自动增加0.01</div>
                    <el-button type="primary"  size="mini" @click="elementNewVersionSaveClick" v-if="elementForm.elementId !== ''">
                    +v
                    </el-button>
                    </el-tooltip>
                </el-button-group>
              </div>
              <div style="margin-top: 10px" v-if="elementForm.elementId !== ''">
                构件编号 v{{ elementForm.elementVersion }}
                <el-tag size="mini">{{ elementForm.elementId }}</el-tag>
              </div>
            <el-form :label-position="labelPosition" label-width="80px" :model="elementForm">
              <el-form-item label="名称">
                <el-input v-model="elementForm.name" placeholder="请输入构件名称"></el-input>
              </el-form-item>
              <el-form-item label="备注" >
                <el-input
                  type="textarea"
                  :rows="2"
                  placeholder="请输入备注"
                  v-model="elementForm.memo">
                </el-input>
              </el-form-item>
              <el-form-item label="构件大小">
                <el-select v-model="pageSize" placeholder="构件大小">
                  <el-option
                    v-for="item in optionsPageSize"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value">
                  </el-option>
                </el-select><br>
                宽（cm）：<el-input-number size="mini" v-model="pageWidth" :precision="2" :step="0.1"  :disabled="pageSizeSetAva"></el-input-number><br>
                高（cm）：<el-input-number size="mini" v-model="pageHeight" :precision="2" :step="0.1"  :disabled="pageSizeSetAva"></el-input-number>
              </el-form-item>

              <el-form-item label="方向">
                <el-select v-model="pageDirection" placeholder="方向" :disabled="pageDirectionAva">
                  <el-option
                    v-for="item in optionsPageDirection"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value">
                  </el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="页边距">
                上（cm）： <el-input-number size="mini" v-model="marginTop" :precision="2" :step="0.1" :max="10"></el-input-number><br>
                下（cm）： <el-input-number size="mini" v-model="marginBottom" :precision="2" :step="0.1" :max="10"></el-input-number><br>
                左（cm）： <el-input-number size="mini" v-model="marginLeft" :precision="2" :step="0.1" :max="10"></el-input-number><br>
                右（cm）： <el-input-number size="mini" v-model="marginRight" :precision="2" :step="0.1" :max="10"></el-input-number>
              </el-form-item>
            </el-form>
            </div>
          </el-tab-pane>
          <el-tab-pane label="数据">
            <el-row>
              <el-col :span="12">
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
                <el-button icon="el-icon-coin" title="加载数据" size="mini" type="primary">加载</el-button>
                <el-button icon="el-icon-download" size="mini" type="primary" @click="dataDownloadClick" title="下载数据"></el-button>
              </el-upload>
              </el-col>
              <el-col :span="12">
                <el-button icon="el-icon-connection" title="绑定数据" size="mini" @click="dataBindClick" type="primary">绑定</el-button>
                <!--<el-button size="small" type="primary" @click="dataBindClick">绑定数据</el-button>-->
              </el-col>
              </el-row>
            <el-row :span="24">
              <div style="font-size: 12px;color:#909399">txt、json文件，且不超过3M</div>
              <div style="color:#409EFF">{{jsonInfo.substring(5)}}</div>
            </el-row>
            <el-row>
              <!--数据-->
              <div :style="{height: this.$store.state.boxHeight}" style="width:100%">
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

          </el-tab-pane>
          <!--<el-tab-pane label="收藏">-->
            <!--&lt;!&ndash;构件&ndash;&gt;-->
            <!--<div :style="{height: this.$store.state.boxHeight,overflow_y:scroll}">-->
              <!--&lt;!&ndash;eslint-disable-next-line&ndash;&gt;-->
              <!--<div v-for="el in elFavourites" >-->
                <!--<el-card class="box-card" :style="{height:'300px'}" shadow="hover">-->
                  <!--<html-capture :htmlUrl="el.url" style="width:100%"></html-capture>-->
                <!--</el-card>-->
                <!--<div style="margin-top: 0px;text-align: right">-->
                  <!--<div class="bottom clearfix">-->
                    <!--<el-button type="text" class="button" size="small" @click="elLoad(el)">载入构件</el-button>-->
                  <!--</div>-->
                <!--</div>-->
              <!--</div>-->
            <!--</div>-->
          <!--</el-tab-pane>-->
          <el-tab-pane label="收藏">
            <!--构件-->
            <div :style="{height: this.$store.state.boxHeight}">
              <!--eslint-disable-next-line-->
              <div v-for="(el,index) in elFavourites" ref="elCaptures">
                <el-card class="box-card"  shadow="hover">
                  <html-capture :htmlCode="el.content" :htmlUrl="el.url" style="width:100%"></html-capture>
                </el-card>
                <div style="margin-top: 0px;text-align: right">
                  <div class="bottom clearfix">
                    <el-button type="text" class="button" size="small" @click="elFavoritesDel(index,el)">删除</el-button>
                    <el-button type="text" class="button" size="small" @click="elLoad(el)">载入构件</el-button>
                  </div>
                </div>
              </div>
            </div>
          </el-tab-pane>

          <el-tab-pane label="样例">
            <!--构件-->
            <div :style="{height: this.$store.state.boxHeight}">
              <!--eslint-disable-next-line-->
              <div v-for="(el,index) in elSysFavourites" ref="elCaptures">
                <el-card class="box-card"  shadow="hover">
                  <html-capture :htmlCode="el.content" :htmlUrl="el.url" style="width:100%"></html-capture>
                </el-card>
                <div style="margin-top: 0px;text-align: right">
                  <div class="bottom clearfix">
                    <el-button type="text" class="button" size="small" @click="elFavoritesDel(index,el)">删除</el-button>
                    <el-button type="text" class="button" size="small" @click="elLoad(el)">载入构件</el-button>
                  </div>
                </div>
              </div>
            </div>
          </el-tab-pane>
        </el-tabs>
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
        <el-col :span="1"></el-col>
        <el-col :span="16">
          <el-tabs type="card" @tab-click="tabHandleClick" :style="{height:(this.$store.state.windowHeight - 150)+ 'px'}" id="designTab">
            <el-tab-pane label="设计" style="padding:0px">

                <tinymce-editor ref="editor"
                                v-model="elHtmlCode"
                                :disabled="disabled"
                                @objectSelected="objectSelected"
                                :init="editorInit"
                                @editorLoadComplete="editContentInit" style="overflow:visible"
                                :style="{height:(this.$store.state.windowHeight - 210)+ 'px',width:this.pageWidth + 'cm',border:'1px solid #E4E7ED',margin:'auto'}">
                </tinymce-editor>
                <!--<button @click="clear">清空内容</button>-->
                <!--{{elHtmlCode}}-->
                <!--<span  @dblclick="dataDblClick">双击添加标签th</span>-->
                <!--<button @click="addTh">添加标签</button>-->
                <!--<button @click="delTh">删除标签</button>-->
                <br>
                <div id="capture"></div>

            </el-tab-pane>
            <el-tab-pane label="专家模式" >
              <!--<textarea style="width:100%" :style="{height:(this.$store.state.windowHeight - 150)+ 'px'}">{{elHtmlCode}}</textarea>-->
              <div :style="{height:(this.$store.state.windowHeight - 190)+ 'px'}" style="overflow: scroll">
                <codemirror  ref="elDesginCodeEditor" v-model="elHtmlCode"  :options="cmOptions" @change="codeEditorChanged"
                             ></codemirror>
              </div>
            </el-tab-pane>
            <el-tab-pane label="PDF预览" >
              <iframe id="pdfViewer" src="" width="100%" height="100%" :style="{width: '100%',height:this.$store.state.iframeHeight}" frameborder="0" ></iframe>
            </el-tab-pane>
            <el-tab-pane label="帮助" >
              <div :style="{height:(this.$store.state.windowHeight - 190)+ 'px'}" style="overflow: scroll">
                <markdown-view style="width:100%"></markdown-view>
              </div>
            </el-tab-pane>
          </el-tabs>
        </el-col>
<!--      </VueDraggableResizable>-->
    </el-row>
<!--    <div>-->
<!--      <VueDraggableResizable :w="100" :h="100" v-on:dragging="onDrag" v-on:resizing="onResize" :parent="true">-->
<!--        <p><br>-->
<!--          X: {{ x }} / Y: {{ y }} - Width: {{ width }} / Height: {{ height }}</p>-->
<!--      </VueDraggableResizable>-->
<!--    </div>-->
    <el-dialog title="循环绑定控件选择" :visible.sync="dialogDataBindSelectVisible" :before-close="dataBindDiagleHandClose">
      <el-form :model="eachDataBindSelectForm">
        <el-radio v-model="eachDataBindSelectForm.ctrlRadio" label="tr" >行元素</el-radio>
        <el-radio v-model="eachDataBindSelectForm.ctrlRadio" label="table" >整个表格</el-radio>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <!--<el-button @click="dialogDataBindSelectVisible = false">取 消</el-button>-->
        <el-button type="primary" @click="dialogDataBindSelectVisible = false">确 定</el-button>
      </div>
    </el-dialog>

    <!--构件同步日志-->
    <el-drawer
      title="构件修改历史"
      :visible.sync="drawerElModifyHistoryViewer"
      :with-header="true"
      size="40%">
      <el-row>
        <el-col :span="1">
          &nbsp;
        </el-col>
        <el-col :span="22">
          <template>
            <el-table
              :data="elModifyHistoryData"
              style="width: 100%"
              :height="this.$store.state.tableHeight">
              <el-table-column
                prop="updateTimeStr"
                label="修改日期"
                width="150">
              </el-table-column>
              <el-table-column
                prop="name"
                label="构件名称"
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
                prop="elementVersion"
                label="构件版本号"
                width="100">
              </el-table-column>
              <el-table-column
                prop="updateUserName"
                label="修改用户名"
                width="100">
              </el-table-column>
              <el-table-column
                fixed="right"
                label="操作"
                width="100">
                <template slot-scope="scope">
                  <el-button @click="elViewerClick(scope.row)" type="text" size="small" >查看</el-button>
                  <el-button @click="elResumeClick(scope.$index, scope.row)" type="text" size="small">载入</el-button>
                </template>
              </el-table-column>
            </el-table>
            <div style="width:100%;text-align: right">
              <el-pagination
                @size-change="this.handlePageSizeChange"
                :page-sizes="[5, 10, 20, 50, 100]"
                @current-change="elModifyHistoryHandleCurrentChange"
                layout="total,prev, pager, next,sizes"
                :total="elModifyHistoryTotal"
                :current-page="elModifyHistoryCurrentPage"
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

    <!--模板预览视图-->
    <el-drawer
      title="模板预览"
      :visible.sync="drawerElViewer"
      :with-header="true"
      direction="ltr"
      size="50%">
      <el-row>
        <el-col :span="1">
          &nbsp;
        </el-col>
        <el-col :span="22">
          <el-card :style="{marginTop:'20px',overflow: 'auto',height:this.$store.state.elViewerBoxHeight}" >
            <html-panel :htmlCode.sync="elCode" ></html-panel>
          </el-card>
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
import TinymceEditor from '@/components/tinymce-editor'
import HtmlCapture from '@/components/html-capture'
import MarkdownView from '@/components/markdown-view'
import { codemirror } from 'vue-codemirror'
import HtmlPanel from '@/components/html-panel'
import router from "../../router";

require("codemirror/mode/htmlmixed/htmlmixed.js")
require('codemirror/addon/fold/foldcode.js')
require('codemirror/addon/fold/foldgutter.js')
require('codemirror/addon/fold/brace-fold.js')
require('codemirror/addon/fold/xml-fold.js')
require('codemirror/addon/fold/indent-fold.js')
require('codemirror/addon/fold/markdown-fold.js')
require('codemirror/addon/fold/comment-fold.js')
require('codemirror/addon/fold/comment-fold.js')

export default {
  name: 'ElementDesign',
  components: {
    VueJsonPretty,
    // VueDraggableResizable,
    TinymceEditor,
    HtmlCapture,
    codemirror,
    MarkdownView,
    HtmlPanel
  },
  data () {
    return {
      editorInit:{
        content_style: ''
      },
      cmOptions: {
          tabSize: 4,
          mode: 'htmlmixed',
          thmeme: 'darcula',
          lineNumbers: true,
          lineWrapping: true,
          extraKeys: {"Ctrl": "autocomplete"},
          lineWiseCopyCut: true,
          showCursorWhenSelecting: true,
          matchBrackets: true,
          line:true,
          autofocus: true,
          height: '500px',
          autoRefresh: true,
          favoritesHtmlViewWidth:this.$store.state.windowWidth/11
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
      elementForm:{
        name: '',
        elementVersion: '',
        memo:'',
        content:'',
        testData:'',
        elementId:'',
        newFlag: ''
      },
      fileList: [],
      tabPosition: 'left',
      // JSON 数据
      jsonData: {
      },
      // 构件参数设置
      optionsPageSize: [{
        value: 'A3',
        label: 'A3'
      }, {
        value: 'A4',
        label: 'A4'
      }, {
        value: 'A5',
        label: 'A5'
      }, {
        value: 'B4',
        label: 'B4'
      }, {
        value: 'B5',
        label: 'B5'
      },
      {
        value: 'NORMAL',
        label: '自定义大小'
      }],
      pageSize: 'A4',
        optionsPageDirection: [{
        value: '',
        label: '纵向'
      }, {
        value: 'landscape',
        label: '横向'
      }],
      pageDirection: '',
      marginTop: 1.00,
      marginBottom: 1.00,
      marginLeft: 1.00,
      marginRight: 1.00,
      pageWidth: 21.00,
      pageHeight:29.70,
      pageSizeSetAva: true,
      pageDirectionAva: false,
      width: 0,
      height: 0,
      x: 0,
      y: 0,
      textarea: '',
      editorSelectElement: '',
      captureImgUrl: '',
      htmlUrl: '/static/testresource/elements/009_familyproof.html',
      elTplBorder: '',
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

      elFavoritesSearchKey: '',
      elFavouritesDataTotal: 1,
      elFavouritesCurrentPage: 1,
      dialogDataBindSelectVisible:false,

      drawerElModifyHistoryViewer:false,
      elModifyHistoryData:[],
      elModifyHistoryTotal: 1,
      elModifyHistoryCurrentPage: 1,

      drawerElViewer: false,
      elCode: '',
      eachDataBindSelectForm:{
        ctrlRadio: 'tr'
      },
      fileInfos: [],
      currentFolder: {
        dataName: '/',
        folderId: 'root'
      },
      paths: [],
      activeName: '0'
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
      if (this.jsonInfo === ''){
        this.$message({
          showClose: true,
          message: '请选择要绑定的数据项',
          type: 'error'
        })
        return
      }
      let tinymce = this.$store.state.editorSelectedEditor
      //console.log(tinymce)
      if (tinymce === undefined || tinymce === '') {
        this.$message({
          showClose: true,
          message: '请在编辑器中选择一个绑定对象',
          type: 'error'
        })
      } else {
        let jsonPathNoRoot = this.jsonPath.substring(5)
        let regx = /\[(\d)\]/
        let p = this.jsonPath.search(regx)
        let arrayPrefix = this.jsonPath.substring(5,this.jsonPath.indexOf('['))
        let arrayName = this.jsonPath.substring(5,this.jsonPath.indexOf(']') + 1)
        let arrayEl = this.jsonPath.substring(this.jsonPath.indexOf(']') + 1)
        let thExp = arrayPrefix + '=${' + arrayName + '}'

        let selectedElement = tinymce.activeEditor.selection.getNode()
        let parentEl = ['tr','table','div']
        let eachItemPrefix = ''


        parentEl.forEach((item,index,array)=>{
          let findEachElement =  tinymce.activeEditor.selection.getNode();
          if(findEachElement.getAttribute('data-th-each') !== null){
            //选择节点为循环根节点
            eachItemPrefix = findEachElement.getAttribute('data-th-each').substring(0,findEachElement.getAttribute('data-th-each').indexOf(':'))
          }else{
            //选择节点为其他节点
            let count = 0
            do{
              findEachElement = findEachElement.parentNode
              if(findEachElement.nodeName === 'HTML'){
                break
              }
              if(findEachElement !== null && findEachElement.getAttribute('data-th-each') !== null){

                eachItemPrefix = findEachElement.getAttribute('data-th-each').substring(0,findEachElement.getAttribute('data-th-each').indexOf(':'))
                console.log("找到父类存在data-th-each：" + eachItemPrefix)
                break
              }else{
                console.log(findEachElement)
                console.log("查找父节点data-th-earch")
                continue
              }
            }while(findEachElement !== undefined && findEachElement !== null && count++ < 10)
            console.log("寻找父类是否有data-th-each")

          }

        })
        console.log(jsonPathNoRoot + "数组正则匹配：" + p)
        if(p > 0 && eachItemPrefix === ''){
          this.$message({
            showClose: true,
            message: '该变量为数组元素，您尚未绑定数组变量，该元素将固定显示选择的数组元素位置',
            type: 'warning'
          })
        }

        if(p > 0 && !(this.jsonValue instanceof Array)){
          if(eachItemPrefix !== ''){
            console.log("数组绑定")
            tinymce.activeEditor.execCommand('mceInsertContent', false, '<span data-th-text="${' + eachItemPrefix  + arrayEl + '}"> ' + this.jsonValue + '</span><span>&nbsp;</span>')
            return
          }else {
            console.log("数组元素原路径显示")
            //tinymce.activeEditor.execCommand('mceInsertContent', false, '<div data-th-with="' + thExp + '"><span data-th-text="${' + arrayPrefix + arrayEl + '}"> ' + this.jsonValue + '</span></div>')
            tinymce.activeEditor.execCommand('mceInsertContent', false, '<span data-th-text="${' + jsonPathNoRoot + '}"> ' + this.jsonValue + '</span><span>&nbsp;</span>')
            return
          }
        }

        if(p <= 0 && !(this.jsonValue instanceof Array)){
          tinymce.activeEditor.execCommand('mceInsertContent', false, '<span data-th-text="${' + jsonPathNoRoot + '}"> ' + this.jsonValue + '</span><span>&nbsp;</span>')
          return
        }

        if(this.jsonValue instanceof Array) {
          if(selectedElement.nodeName === 'TR' || selectedElement.nodeName === 'DIV' || selectedElement.nodeName === 'TD'){
            //console.log(selectedElement.nodeName)

            if(selectedElement.nodeName === 'TD'){
              this.dialogDataBindSelectVisible = true

            }else {
              selectedElement.setAttribute('data-th-each', jsonPathNoRoot.replace(/\./g,'_') + ':${' + jsonPathNoRoot + '}')
            }
            //console.log(this.$store.state.editorSelectedElement.toElement)
          }else {
            tinymce.activeEditor.execCommand('mceInsertContent', false, '<div data-th-each="' + jsonPathNoRoot.replace(/\./g,'_') + ':${' + jsonPathNoRoot + '}"></div>')

            //selectedElement.setAttribute('data-th-each', jsonPathNoRoot.replace(/\./g,'_') + ':${' + jsonPathNoRoot + '}')
//            this.$message({
//              showClose: true,
//              message: '您选择的是数组对象，请将该数据绑定至表格或者块中，然后对元素进行绑定',
//              type: 'error'
//            })
          }
          return
        }
//        else {
//          tinymce.activeEditor.execCommand('mceInsertContent', false, '<span data-th-text="${' + this.jsonPath.substring(5) + '}"> ' + this.jsonValue + '</span>')
//        }
      }
    },
    // 页面属性设置
    pageAttrsSet(){
      let styles = ''
      if(this.pageSize !== 'NORMAL'){
        styles = '@page{size: ' + this.pageSize + ' ' + this.pageDirection + '  ; margin-top: '+ this.marginTop +'cm;margin-bottom: ' + this.marginBottom + 'cm;margin-left:'+ this.marginLeft + 'cm;margin-right:'+ this.marginLeft +'cm}'
      }else{
        styles = '@page{size: ' + this.pageWidth + 'cm ' + this.pageHeight + 'cm ; margin-top : '+ this.marginTop +'cm;margin-bottom: ' + this.marginBottom + 'cm;margin-left:'+ this.marginLeft + 'cm;margin-right:'+ this.marginLeft +'cm}'
      }

      //let dom = tinymce.activeEditor.dom
      //tinymce.editors[0].editorManager.get('elEditor').focus()

      var editorHtmlParser = new DOMParser();
      var htmlDoc = editorHtmlParser.parseFromString(this.elHtmlCode, 'text/html');

      if(htmlDoc.getElementById('eprint_page_style') != null) {
        htmlDoc.getElementById('eprint_page_style').remove()
      }

      var hstyle = document.createElement("style");
      hstyle.id = 'eprint_page_style'
      hstyle.innerText = styles
      htmlDoc.getElementsByTagName('head')[0].appendChild(hstyle)

      //console.log(htmlDoc.getElementsByTagName('html')[0].innerHTML)

      this.elHtmlCode = '<!DOCTYPE html>' + htmlDoc.getElementsByTagName('html')[0].innerHTML

      this.$message({
        showClose: true,
        message: '设置成功',
        type: 'success'
      })
    },
    // 保存构件
    elementSaveClick () {
      //console.log('this.elementForm.name => ' + this.elementForm.name)
      if(this.elementForm.name === null || this.elementForm.name === ''){
        this.$message({
          showClose: true,
          message: '请添加构件名称',
          type: 'error'
        })
      }else{
        this.elementForm.content = this.elHtmlCode
        this.elementForm.testData = JSON.stringify(this.jsonData)
        axios.post(this.GLOBAL.webappApiConfig.ElementDesign.UserElementSave.url,
          this.elementForm,
          {})
          .then(res => {
           if(res.status === 200){
             this.$router.push({name: 'ElementManagement', params: {}})
           }
          })
          .catch(function (error) {
            console.log(error)
          })
      }
    },
    // 新增相应的版本构件
    elementNewVersionSaveClick () {
      if(this.elementForm.elementId === null){
        this.$message({
          showClose: true,
          message: '新增版本异常，缺失构件信息，请返回重新选择构件',
          type: 'error'
        })
      }

      if(this.elementForm.name === null || this.elementForm.name === ''){
        this.$message({
          showClose: true,
          message: '请添加构件名称',
          type: 'error'
        })
      }else{
        this.elementForm.newFlag = '1'
        this.elementForm.content = this.elHtmlCode
        this.elementForm.testData = JSON.stringify(this.jsonData)
        axios.post(this.GLOBAL.webappApiConfig.ElementDesign.UserElementSave.url,
          this.elementForm,
          {})
          .then(res => {
            if(res.status === 200){
              this.$router.push({name: 'ElementManagement', params: {}})
            }
          })
          .catch(function (error) {

          });
      }
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
    // tab标签选择
    tabHandleClick (tab, event) {
/* eslint-disable */
        //console.log(tab, event)
        if (tab.label === 'PDF预览') {
            if(this.elHtmlCode === null || this.elHtmlCode === ''){
              this.$message({
                showClose: true,
                message: '请先在编辑区编辑内容后再预览PDF',
                type: 'error'
              })
              return
            }
            //let ckeditorFrame = document.getElementById('ckeditor')
            let pdfViewerFrame = document.getElementById('pdfViewer')
            // console.log(this.elHtmlCode)
            // console.log(this.elTestData)
            // let param = {'data':{},'thtml':ckeditorFrame.contentWindow.myEditor.getData()};

            axios.post(this.GLOBAL.webappApiConfig.ElementDesign.UserElementPdfView.url,
                {data:this.jsonData , thtml: this.elHtmlCode},
                {})
                .then(res => {
                    sessionStorage.setItem('pdfData', res.data.filedata)
                    pdfViewerFrame.src = '/static/pdfjs/web/pdfviewer.html'
                  //console.log(res.status)
                })
                .catch(function (error) {
                     console.log(error);
                });
        }else if(tab.label === '专家模式'){
          let selectedElement = tinymce.activeEditor.selection.getNode()
          setTimeout(() => {
            this.$refs.elDesginCodeEditor.codemirror.refresh()

          },50)
        }else if(tab.label === '帮助'){

        }
    },
    // },
     handleRemove(file, fileList) {
         //console.log(file, fileList);
     },
     handleExceed(files, fileList) {
         this.$message.warning(`当前限制选择 3 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件`);
         console.log(`当前限制选择 3 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件`);
     },
     beforeRemove(file, fileList) {
         return this.$confirm(`确定移除 ${ file.name }？`);
     },
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
    // onResize: function(x, y, width, height) {
    //   this.x = x;
    //   this.y = y;
    //   this.width = width;
    //   this.height = height;
    // },
    // onDrag: function(x, y) {
    //   console.log('...')
    //   this.x = x;
    //   this.y = y;
    // },
      goBack () {
          this.$router.push({name: 'ElementManagement', params: {}})
      },
      // 构件收藏查询
      userElFavoritesQuery(val){
        axios.get(this.GLOBAL.webappApiConfig.ElementDesign.UserElementFavoritesQuery.url + (this.elFavoritesSearchKey === '' ? '' : '/' + this.elFavoritesSearchKey) + '?pageCurrent=' + val + '&pageSize=' + this.$store.state.pageSize,
          {},
          {})
          .then(res => {
            if (res.data.content.records.length !== 0) {
              this.elFavourites = res.data.content.records
              this.elFavouritesDataTotal = res.data.content.total
              this.elFavouritesCurrentPage = res.data.content.current

            } else {

            }
            // console.log(res)
          })
          .catch(function (error) {
            console.log(error)
            // alert(error)
          })
      },
    elFavoritesDel(index, el){
      // 删除构件
      axios.post(this.GLOBAL.webappApiConfig.ElementDesign.UserElementFavoritesDel.url,
        el,
        {})
        .then(res => {
          this.elFavourites.splice(index, 1)
          this.$refs.elCaptures[index].remove()
        })
        .catch(function (error) {
          //console.log(error)
        })
    },
    editContentInit(flag){
      if(flag === true){
        if(this.$route.params.element !== undefined){
          this.elementForm.name = this.$route.params.element.name
          this.elementForm.memo = this.$route.params.element.memo
          this.elementForm.content = this.$route.params.element.content
          this.elementForm.testData = this.$route.params.element.testData
          this.elementForm.elementId = this.$route.params.element.elementId
          this.elementForm.elementVersion = this.$route.params.element.elementVersion

          this.jsonData = JSON.parse(this.elementForm.testData)
          this.elHtmlCode = this.elementForm.content
        }
      }

//      this.$refs.elDesginCodeEditor.codemirror.focus()
//      this.$refs.elDesginCodeEditor.codemirror.setCursor(2,1)
//      this.$nextTick(()=> {
//        this.$refs.elDesginCodeEditor.codemirror.refresh();
//      })

//      setTimeout(() => {
//
//        this.$refs.elDesginCodeEditor.codemirror.refresh()
//
//      },50)
    },
    codeEditorChanged(){

    },
    dataBindDiagleHandClose(){
      console.log("ddd"+this.eachDataBindSelectForm.ctrlRadio);
      let jsonPathNoRoot = this.jsonPath.substring(5)
      let selectedElement = tinymce.activeEditor.selection.getNode()
      if(this.eachDataBindSelectForm.ctrlRadio === ''){
        return
      }
      if(this.eachDataBindSelectForm.ctrlRadio === 'tr'){
        selectedElement.parentNode.setAttribute('data-th-each', jsonPathNoRoot.replace(/\./g,'_') + ':${' + jsonPathNoRoot +'}')
      }else{
        //let count = 0
        do{
          selectedElement = selectedElement.parentNode
          if(selectedElement.nodeName !== 'TABLE'){
            continue
          }else{
            selectedElement.setAttribute('data-th-each', jsonPathNoRoot.replace(/\./g,'_') + ':${' + jsonPathNoRoot +'}')
            break
          }
        }while(selectedElement !== undefined && selectedElement !== null)

      }
      console.log(this.eachDataBindSelectForm.ctrlRadio);
    },
    dataDownloadClick () {
      if (this.jsonData != null) {
        const data = JSON.stringify(this.jsonData)
        const blob = new Blob([data], {type: ''})
        if(this.elementForm.name === null)
          FileSaver.saveAs(blob, 'el_data.json')
        else
          FileSaver.saveAs(blob, this.elementForm.name + '_data.json')
      }
    },
    elModifyHistoryViewerClick (val) {
      //this.currentSelectRow = row
      this.elElModifyHistoryQuery(this.elementForm,val)
      this.drawerElModifyHistoryViewer = true
    },
    elElModifyHistoryQuery (row, val) {
      axios.get(this.GLOBAL.webappApiConfig.ELementManagement.UserElementElModifyHistoryQuery.url + '/' + row.elementId + '?pageCurrent=' + val + '&pageSize=' + this.$store.state.pageSize,
        {},
        {})
        .then(res => {
          this.elModifyHistoryData = res.data.content.records
          this.elModifyHistoryTotal = res.data.content.total
          this.elModifyHistoryCurrentPage = res.data.content.current
        })
        .catch(function (error) {
          console.log(error)
          // alert(error)
        })
    },
    elModifyHistoryHandleCurrentChange(val){
      this.elElModifyHistoryQuery(this.elementForm, val)
    },
    elResumeClick(index,row){
      // 删除构件
      this.$confirm('是否确认载入历史记录并覆盖当前编辑内容?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.elementForm.name = row.name
        this.elementForm.memo = row.memo
        this.elementForm.content = row.content
        this.elementForm.testData = row.testData

        this.jsonData = JSON.parse(this.elementForm.testData)
        this.elHtmlCode = this.elementForm.content

      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消载入历史记录'
        })
      })

    },
    elViewerClick (row) {
      // console.log(row.content)
      console.log(row.content)
      this.elCode = row.content
      this.drawerElViewer = true
    },
    //资源搜索
    handelFolderChange (val) {
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
    }
  },
    mounted() {

    },created(){
      this.userElFavoritesQuery(1)

    },watch: {
      pageSize(val){
        //console.log(val)
        switch (val) {
          case 'A4':
            this.pageWidth = 21.0
            this.pageHeight = 29.7
            this.pageSizeSetAva = true
            this.pageDirectionAva = false
            break;
          case 'A3':
            this.pageWidth = 29.7
            this.pageHeight = 42.0
            this.pageSizeSetAva = true
            this.pageDirectionAva = false
            break;
          case 'A5':
            this.pageWidth = 14.8
            this.pageHeight = 21.0
            this.pageSizeSetAva = true
            this.pageDirectionAva = false
            break;
          case 'B5':
            this.pageWidth = 17.6
            this.pageHeight = 25.0
            this.pageSizeSetAva = true
            this.pageDirectionAva = false
            break;
          case 'B4':
            this.pageWidth = 25.0
            this.pageHeight = 35.3
            this.pageSizeSetAva = true
            this.pageDirectionAva = false
            break;
          case 'NORMAL':
            this.pageSizeSetAva = false
            this.pageDirectionAva = true
            break;
        }
      },
      dialogDataBindSelectVisible(val){
          if(val === false){
            console.log("diagle close")
            this.dataBindDiagleHandClose()
          }
      },
      'activeName':function(val) {
        switch (val) {
          case 'resource':
            this.handelFolderChange(this.currentFolder.folderId)
            break
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
    /*padding:20px;*/
  }

  #resourceTab >>> .el-tabs__content{
    overflow:scroll;
    flex-grow: 1;
    /*overflow-y: scroll;*/
    padding:20px;
  }

  #resourceTab >>>> .el-tab-pane{
    overflow:scroll;
    flex-grow: 1;
    /*overflow-y: scroll;*/
  }

  /* 代码编辑器高度 */
  .el-tab-pane >>> .CodeMirror{
    /*border: 1px solid #eee;*/
    height:auto;
  }

  /*.el-tab-pane >>> .CodeMirror-scroll{*/
    /*overflow:scroll;*/
    /*!*height: auto;*!*/
    /*!*overflow-y: scroll;*!*/
    /*!*overflow-x: auto;*!*/
    /*!*flex-grow: 1;*!*/
  /*}*/


  #designTab >>> .tox-tinymce{
    border-left:0px;
    border-right:0px;
    border-top:0px;
    border-:0px;
  }
  .tinymce-editor{
    border:0px;
  }
  /**
  设置编辑器外侧
   */
  #designTab >>> .tox-edit-area{
    background-color: #E4E7ED;
    //padding-left:0.5cm
    /*align-content:center;*/


  }

  /*#designTab >>> .tox-sidebar-wrap{*/
    /*width: 21cm;*/
    /*box-shadow: 0 0 0.5cm rgba(0,0,0,0.5);*/
    /*!*padding:1cm;*!*/
  /*}*/


  #designTab >>> #elEditor_ifr{
    /* 可模拟控制宽度 */
    /*width: 21cm;*/
    /*min-height: 29.2cm;*/
    /*display: block;*/
    /*margin:0.5cm 5% 0.1cm 10%;*/
    /*box-shadow: 0 0 0.5cm rgba(0,0,0,0.5);*/
    /* 可模拟控制边距 */
    /*padding:1cm;*/
  }

  #designEditorDiv{
    width: 21cm;
    margin:0.5cm 5% 0.1cm 10%;
    box-shadow: 0 0 0.5cm rgba(0,0,0,0.5);
    padding:20px 1cm 20px 1cm;
  }

</style>
