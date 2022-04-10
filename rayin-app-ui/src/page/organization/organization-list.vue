<template>
  <el-container style="border-bottom:solid 1px #e6e6e6;height:45px">
    <el-header style="padding:0 0;height:45px;border-bottom:solid 1px #e6e6e6;">
      <el-row>
        <el-col style="width: 50px;">
          <el-button type="primary" :icon="collapseIcon" style="border-radius: 0px;padding:6px 10px;font-size: 25px"></el-button>
        </el-col>
        <el-col :span="11">
          <div style="width:100%;text-align: left">
            <img src="/static/images/logo3.png" style="width:200px;">
          </div>
        </el-col>
        <el-col :span="12">

        </el-col>
      </el-row>
    </el-header>
    <el-container>
        <el-main>
          <div style="margin-top: 100px">
          <el-form ref="form" :model="form" :rules="rules" label-width="0px" style="margin: 0 auto;width: 30%;text-align: center">
            <div>您好！开始创建项目</div>
            <div style="width:100%;text-align: center;margin-top:30px">
              <h2>输入项目名称</h2>
            </div>
            <div style="width:100%;text-align: center;margin-top:30px">
              <el-form-item label="" prop="orgName" class="login-input">
                <el-input v-model="form.orgName" placeholder="" style="width:80%;" >
                </el-input>
              </el-form-item>
<!--              <el-form-item label="" prop="number" class="login-input">-->
<!--                <el-input v-model="form.number" style="width:80%">-->
<!--                  <el-button slot="prepend" icon="el-icon-key"></el-button>-->
<!--                </el-input>-->
<!--              </el-form-item>-->
            <el-button type="primary" icon="el-icon-user-solid" @click="createOrg" style="width:80%">开始创建</el-button>
            </div>
          </el-form>
          </div>
        </el-main>
      <el-footer>
      </el-footer>
    </el-container>
  </el-container>
</template>

<script>
/* eslint-disable */
  import Cookies from 'js-cookie'
  import router from '../../router'
  import {setToken, removeToken} from '@/util/auth'
  import axios from 'axios'
  export default {
    name: 'CreateOrganization',
    data() {
      return {
        collapseIcon: 'el-icon-s-fold',
        codeUrl: '/api/users/code/image',
        form: {
          orgName: '',
          number: ''
        },
        // 表单验证，需要在 el-form-item 元素中增加 prop 属性
        rules: {
          orgName: [
            {required: true, message: '项目名为必填项', trigger: 'blur'}
          ]
          // ,
          // number: [
          //   {required: true, message: '密码为必填项', trigger: 'blur'}
          // ]
        },
        dialogVisible: false,
        message: ''
      };
    },methods: {
      createOrg() {
        // this.$router.push({name: "main", params: {}});
        // 数据校验
        this.$refs.form.validate(valid => {
          if (valid) {
            let userId = localStorage.getItem('userId')
            axios.post('/api/users/createOrganization',
              {'organizationName': this.form.orgName, 'number': 50,'userId': userId},
              { headers:
                  {
                    'Content-Type': 'application/json;charset=UTF-8'
                    //将表单数据传递转化为form-data类型
                  },
                withCredentials : true
              })
              .then(function (response) {
                router.push('/guide')
              })
              .catch(function (error) {
                alert(error);
              });
          } else {
            this.dialogVisible = true
            return false
          }
        });
      }
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
<style>
  .login-input .el-form-item__error {
    margin-left: 10% !important;
  }
</style>
