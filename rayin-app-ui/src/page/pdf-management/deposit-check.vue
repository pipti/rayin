<template xmlns:el-col="http://www.w3.org/1999/html">
  <div>
    <el-row>
      <el-col :span="4">
        &nbsp;
      </el-col>
      <el-col :span="16" style="text-align: center">
        <el-upload
          class="upload-file"
          drag
          action="#"
          :before-upload="beforeUpload"
          multiple
          :auto-upload="true"
        >
          <i class="el-icon-upload"></i>
          <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        </el-upload>
      </el-col>
      <el-col :span="4">
        &nbsp;
      </el-col>
    </el-row>
    <el-dialog
      :visible.sync="dialogVisible"
      >
      <div slot="title" class="dialog-title">
        <span class="title-text" style="font-size: 25px">核验结果</span>
        <i class="el-icon-success" style="color: limegreen;font-size: 25px" v-if="result.code === 700"></i>
        <i class="el-icon-error" style="color: red;font-size: 25px" v-else></i>
        <div class="button-right">
          <span class="title-close" @click="cancel"></span>
        </div>
      </div>
      <el-form label-width="140px">
        <span v-if="(result.code === 700 ||result.code === 704) ">
          <el-form-item label="开放联盟链的链ID：">
            <span>{{result.bizid}}</span>
          </el-form-item>
          <el-form-item label="块高：">
            <span>{{result.blockNumber}}</span>
          </el-form-item>
          <el-form-item label="交易HASH：">
            <span>{{result.hash}}</span>
          </el-form-item>
          <el-form-item label="交易日期：">
            <span>{{result.date}}</span>
          </el-form-item>
        </span>
        <el-form-item label="查询信息：">
          <span>{{result.message}}</span>
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
import axios from 'axios'
export default {
  name: 'DepositCheck',
  data () {
    return {
      result: {},
      dialogVisible: false
    }
  },
  methods: {
    beforeUpload (file) {
      let fd = new FormData()
      fd.append('file', file)
      axios.post('/api/pdf-rest/pdf/check', fd)
        .then(res => {
          console.log(res.data.content)
          this.result = res.data.content
          this.dialogVisible = true
        }).catch(function (error) {
          console.log(error)
        })
    },
    cancel () {
      this.dialogVisible = false
    }
  },
  created () {
  },
  mounted () {
  }
}
</script>
<style scoped>
header{
  height:30px;
}
.upload-file >>>.el-upload-dragger {
  background-color: #fff;
  border: 3px dashed #d9d9d9;
  border-radius: 6px;
  -webkit-box-sizing: border-box;
  box-sizing: border-box;
  width: 720px;
  height: 360px;
  text-align: center;
  position: relative;
  overflow: hidden;
}
.el-upload-dragger .el-icon-upload {
  font-size: 100px;
  color: #C0C4CC;
  margin: 100px 0 16px;
  line-height: 50px;
}
.el-upload-dragger .el-upload__text {
  color: #606266;
  font-size: 22px;
  text-align: center;
}
</style>
<style>
  .login-input .el-form-item__error {
    margin-left: 10% !important;
  }
</style>
