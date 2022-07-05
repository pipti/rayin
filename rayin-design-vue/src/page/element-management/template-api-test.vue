
<template>
  <div>
    <!--    <el-row :gutter="5">-->
    <el-row>
      <el-page-header @back="goBack" content="模板测试">
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
        <el-tabs :tab-position="tabPosition" type="border-card"
                 :style="{height:(this.$store.state.windowHeight - 150)+ 'px'}" id="resourceTab">
          <el-tab-pane label="模板">
            <el-divider content-position="left">模板编号</el-divider>
            <el-tag size="small">{{this.tplRow.templateId}}</el-tag>
            <el-divider content-position="left">模板名称</el-divider>
            <span>{{this.tplRow.name}}</span>
            <el-divider content-position="left">版本号</el-divider>
            <span>v{{this.tplRow.templateVersion}}</span>
            <el-divider content-position="left">创建日期</el-divider>
            <span>{{this.tplRow.createTimeStr}}</span>
            <el-divider content-position="left">有效期</el-divider>
            <span>{{this.tplRow.startTimeStr}}</span>
            <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;~<br>
            <span>{{this.tplRow.endTimeStr}}</span>
            <el-divider content-position="left">备注</el-divider>
            <span>{{this.tplRow.memo}}</span>
          </el-tab-pane>
          <el-tab-pane label="数据">
            <el-row>
              <el-col :span="5">
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

                >
                  <el-button icon="el-icon-coin" circle title="加载数据" size="small"></el-button>
                  <!--<el-button size="small" type="primary">点击加载本地数据</el-button>-->
                </el-upload>
              </el-col>
              <el-col :span="19">
                <el-button icon="el-icon-document-checked" circle title="测试"
                           @click="testClick" size="small"></el-button>
                <!--<el-button size="small" type="primary" @click="testClick">测试</el-button>-->
                <el-button icon="el-icon-upload2" circle title="保存数据" @click="dataSaveClick" size="small"></el-button>
                <!--<el-button size="small" type="primary" @click="testClick">保存数据</el-button>-->
              </el-col>
            </el-row>
            <el-row :span="24">
              <div style="font-size: 12px;color:#909399">txt、json文件，且不超过3M</div>
              <!--<el-input v-model="jsonRoot" placeholder="根节点"></el-input>-->
            </el-row>
            <el-row>
              <!--数据-->
              <div :style="{height: this.$store.state.boxHeight,overflow:scroll}" style="width:100%">
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
                >
                </vue-json-pretty>
              </div>
            </el-row>
          </el-tab-pane>
          <el-tab-pane label="构件">

          </el-tab-pane>

        </el-tabs>
      </el-col>
      <el-col :span="17">
        <el-tabs type="border-card" :style="{height:(this.$store.state.windowHeight - 150)+ 'px'}" id="designTab">
          <el-tab-pane label="PDF预览" >
            <iframe id="pdfViewer" src="" width="100%" height="100%"
                    :style="{width: '100%',height:this.$store.state.iframeHeight}" frameborder="0" ></iframe>
          </el-tab-pane>
          <el-tab-pane label="元数据" >
            <!--元数据-->
            <div :style="{height: this.$store.state.boxHeight,overflow:scroll}" style="width:100%">
              <vue-json-pretty
                :data="pdfMetadata"
                :path="path"
                :deep="deep"
                :show-double-quotes="showDoubleQuotes"
                :highlight-mouseover-node="highlightMouseoverNode"
                :highlight-selected-node="highlightSelectedNode"
                :show-length="showLength"
                :show-line="showLine"
                :select-on-click-node="selectOnClickNode"
                :collapsed-on-click-brackets="collapsedOnClickBrackets"

                :path-selectable="((path, data) => typeof data !== 'number')"
                :selectable-type="selectableType"
                :show-select-controller="showSelectController"
                @click="jsonHandleClick(...arguments, 'complexTree')"
              >
              </vue-json-pretty>
            </div>
          </el-tab-pane>
        </el-tabs>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import VueJsonPretty from 'vue-json-pretty';
import axios from 'axios';

export default {
  components: {
    VueJsonPretty,
  },
  data() {
    return {
      disabled: false,
      labelPosition: 'top',
      tabPosition: 'left',
      fileList: [],
      tplRow: {},
      // JSON 数据
      jsonData: {
      },
      selectableType: 'single',
      showSelectController: false,
      showLength: false,
      showLine: true,
      showDoubleQuotes: true,
      highlightMouseoverNode: true,
      highlightSelectedNode: true,
      selectOnClickNode: true,
      collapsedOnClickBrackets: true,
      path: 'root',
      deep: 4,
      jsonPath: '',
      jsonValue: '',
      jsonPrettyValue: '',
      boxHeight: this.$store.state.boxHeight,
      searchKey: '',
      scroll: 'scroll',
      jsonRoot: '',
      pdfMetadata: {},
    };
  },
  methods: {
    elLoad(el) {
      //      v.style = 'border:1px solid black'
      console.log(`elLoad${el.url}`);

      if (el.url && el.url.length > 0) {
        // 加载中
        this.loading = true;
        const param = {
          accept: 'text/html, text/plain',
        };
        this.$http.get(el.url, param).then((response) => {
          this.loading = false;
          // 处理HTML显示
          this.elHtmlCode = response.data;
          this.loadCompletedFlag = true;
          console.log('模板加载成功');
        })
          .catch(() => {
            this.loading = false;
            this.elHtmlCode = '加载中';
          });
      }

      if (el.data && el.data.length > 0) {
        // 加载中
        this.loading = true;
        const param = {
          accept: 'text/html, text/plain',
        };
        this.$http.get(el.data, param).then((response) => {
          this.loading = false;
          // 处理HTML显示
          this.jsonData = response.data;
          // console.log(response.data)
          console.log('数据加载成功');
        })
          .catch(() => {

          });
      } else {
        console.log('无测试数据！');
      }
    },
    handleRemove() {
      // console.log(file, fileList)
    },
    handleExceed(files, fileList) {
      this.$message.warning(`当前限制选择 3 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件`);
      console.log(`当前限制选择 3 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件`);
    },
    beforeRemove(file) {
      return this.$confirm(`确定移除 ${file.name}？`);
    },
    handleOnChange(file) {
      this.$refs['json-load'].clearFiles();
      const reader = new FileReader();
      reader.readAsText(file.raw, 'UTF-8');

      reader.onload = (e) => {
        this.jsonData = JSON.parse(e.target.result);
      };
    },
    goBack() {
      this.$router.push({ name: 'TemplateManagement', params: { searchKey: this.searchKey } });
    },
    testClick() {
      const pdfViewerFrame = document.getElementById('pdfViewer');
      this.tplRow.testData = JSON.stringify(this.jsonData);
      axios.post(
        this.GLOBAL.webappApiConfig.TemplateManagement.UserTemplateTest.url,
        this.tplRow,
        {},
      )
        .then((res) => {
          if (res.status === 200) {
            // console.log(res)
            sessionStorage.setItem('pdfData', res.data.content.pdfFile);
            pdfViewerFrame.removeAttribute('srcdoc');
            pdfViewerFrame.src = './pdfjs/web/pdfviewer.html';
            this.pdfMetadata = res.data.content.pdfMetadata;
          }
        })
        .catch((error) => {
          console.log(error);
        });
    },
    dataSaveClick() {
      this.tplRow.testData = JSON.stringify(this.jsonData);
      axios.post(
        this.GLOBAL.webappApiConfig.TemplateManagement.UserTemplateTestDataSave.url,
        this.tplRow,
        {},
      )
        .then(() => {

        })
        .catch((error) => {
          console.log(error);
        });
    },
  },
  mounted() {
    this.tplRow = this.$route.params.tplRow;
    if (this.$route.params.tplRow.testData !== '' && this.$route.params.tplRow.testData !== null) {
      this.jsonData = JSON.parse(this.$route.params.tplRow.testData);
    }

    this.searchKey = this.$route.params.searchKey;
    const pdfViewerFrame = document.getElementById('pdfViewer');
    pdfViewerFrame.srcdoc = '<span style=\'font-size:14px;color: #606266;\'>选择左侧[数据]标签，加载本地数据，并点击测试</span>';
  },
  created() {

  },
  watch: {
  },
};
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

</style>
