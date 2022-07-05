<template>
  <el-container>
    <el-row style="padding:10px 5px 5px 5px;height:30px;">
      <el-col :span="12" >
        <el-image :src="require('@/assets/images/logo5.png')" style="width:210px"/>
      </el-col>
      <el-col :span="12" style="text-align: right">
        <!--        <div style="text-align: right;width:100%;margin-top:5px;height: 50px;border: 1px solid black">-->
        <el-button type="primary" plain size="small" @click="toLogin">登录</el-button>
        <!--        </div>-->
      </el-col>
    </el-row>
    <el-divider></el-divider>
    <el-main>
      <el-row>
        <el-col :span="15">
          <el-image :src="require('@/assets/images/login_left.png')" style="width:80%;margin-top:60px"></el-image>
        </el-col>
        <el-col :span="9">
          <el-card style="width:450px;margin-top:110px;height:400px;border-radius: 0px;text-align: center">
            <el-form ref="form" :model="form" :rules="rules" label-width="0px"  >
              <div style="width:100%;text-align: center;margin-top:30px"><h2>注册</h2></div>
              <el-form-item label="" prop="name" class="sign-input">
                <el-input v-model="form.name" placeholder="姓名" style="width:80%;" tabindex="1">
                  <el-button slot="prepend" icon="el-icon-edit-outline"></el-button>
                </el-input>
              </el-form-item>
              <el-form-item label="" prop="username" class="sign-input">
                <el-input v-model="form.username" placeholder="手机或邮箱" style="width:80%;" tabindex="2">
                  <el-button slot="prepend" icon="el-icon-user"></el-button>
                </el-input>
              </el-form-item>
              <el-form-item label="" prop="password" class="sign-input">
                <el-input v-model="form.password" type="password" placeholder="密码" style="width:80%" tabindex="3">
                  <el-button slot="prepend" icon="el-icon-key"></el-button>
                </el-input>
              </el-form-item>
              <!--              <el-form-item label="">-->
              <!--                <el-input v-model="form.code" placeholder="请输入验证码" style="width:47%">-->
              <!--                  <el-button slot="prepend" icon="el-icon-info"></el-button>-->
              <!--                </el-input>-->
              <!--                <el-image src="/api/users/code/image" style="width:32%"/>-->
              <!--              </el-form-item>-->
              <el-button type="primary" icon="el-icon-user-solid" @click="signup" style="width:80%">立即注册</el-button>
            </el-form>
            <!--            <el-dialog-->
            <!--              title="温馨提示"-->
            <!--              :visible.sync="dialogVisible"-->
            <!--              width="30%"-->
            <!--              :before-close="handleClose">-->
            <!--              <span>{{message}}</span>-->
            <!--              <span slot="footer" class="dialog-footer">-->
            <!--                <el-button type="primary" @click="dialogVisible = false">确 定</el-button>-->
            <!--              </span>-->
            <!--            </el-dialog>-->
          </el-card>
        </el-col>
      </el-row>
    </el-main>
    <el-footer>
      <el-divider></el-divider>
    </el-footer>
  </el-container>
</template>

<script>
/* eslint-disable */
import Cookies from 'js-cookie'
import router from '../../router'
import {setToken, removeToken} from '@/utils/auth'
import axios from 'axios'
export default {
  name: 'signup',
  data() {
    return {
      form: {
        username: "",
        password: "",
        name: ""
      },
      // 表单验证，需要在 el-form-item 元素中增加 prop 属性
      rules: {
        name: [
          {required: true, message: '姓名为必填项', trigger: 'blur'}
        ],
        username: [
          {required: true, message: '手机或邮箱为必填项', trigger: 'blur'}
        ],
        password: [
          {required: true, message: '密码为必填项', trigger: 'blur'}
        ]
      },
      dialogVisible: false,
      message: ''
    };
  },methods: {
    signup() {
      // this.$router.push({name: "main", params: {}});
      // 数据校验
      this.$refs.form.validate(valid => {
        if (valid) {
          let param = {'realName': this.form.name, 'username': this.form.username, 'password': this.form.password}
          console.log(param)
          axios.post('/api/users/signup',
            {'realName': this.form.name, 'username': this.form.username, 'password': this.form.password},
            { headers:
                {
                  'Content-Type': 'application/json;charset=UTF-8'
                  //将表单数据传递转化为form-data类型
                }
            })
            .then(function (response) {
              if(response.data.code === 0){
                router.push('/')
              }
            })
            // .catch(function (error) {
            //   alert(error);
            // });
        } else {
          this.dialogVisible = true
          return false
        }
      });
    },
    toLogin() {
      router.push('/')
    }
  }
}
</script>
<style scoped>
header{
  height:30px;
}
.login-page {
  border-radius: 5px;
  margin: auto;
  width: 350px;
  padding: 35px 55px 15px;
  background: #fff;
  border: 1px solid #eaeaea;
  box-shadow: 0 0 25px #cac6c6;
}

.el-input__inner{
  border-radius:0px;
}


</style>
<style>
.sign-input .el-form-item__error {
  margin-left: 10% !important;
}
</style>
