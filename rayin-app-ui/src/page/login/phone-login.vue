<template>
  <el-container>
    <el-header style="padding:0 0;height:45px;border-bottom:solid 1px #e6e6e6;">
    <el-row style="height: 20px">
      <el-col :span="12" >
        <div>
          <el-image src="/static/images/logo3.png" style="width:210px"/>
        </div>
      </el-col>
      <el-col :span="12" >
      <div style="width:100%;text-align: right;">
        <el-link href="/" style="margin-top: 10px;margin-right: 10px;font-size: 15px;" route="/main/paramter-uncommchars">首页</el-link>
        </div>
        </el-col>
    </el-row>
    </el-header>
    <el-main>
      <el-row>
        <el-col :span="15">
          <el-image src="/static/images/login_left.png" style="width:80%;margin-top:60px"></el-image>
        </el-col>
        <el-col :span="9">
          <el-card style="width:450px;margin-top:110px;height:400px;border-radius: 0px;text-align: left">
            <el-form ref="form" :model="form"  :rules="rules" label-width="0px"  >
              <div style="width:100%;text-align: center;margin-top:30px"><h2>手机登陆</h2></div>
              <el-form-item label="" class="sign-input" prop="username">
                <el-input v-model="form.username" placeholder="请输入手机号码" style="width:80%;" tabindex="1"></el-input>
              </el-form-item>
              <el-form-item label="" prop="verifyCode">
                <el-input v-model="form.verifyCode" placeholder="请输入验证码" style="width:40%"></el-input>
                <span v-show="sendAuthCode"  @click="getAuthCode" style="width:250px">获取验证码</span>
                <span v-show="!sendAuthCode" style="width:200px"> {{auth_time}} 秒之后重新发送验证码</span>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" icon="el-icon-user-solid" @click="login" style="width:80%" :loading="logining">登录</el-button>
                <hr>
                <p>还没有账号？<span class="to" @click="tosignup">免费注册</span></p>
              </el-form-item>
            </el-form>
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
  import {setToken, removeToken} from '@/util/auth'
  import axios from 'axios'
  export default {
    name: 'login',
    data() {
      return {
        form: {
          username: "",
          verifyCode:""//绑定输入验证码框
        },
        logining: false,
        sendAuthCode:true,/*布尔值，通过v-show控制显示‘获取按钮’还是‘倒计时’ */
        auth_time: 0, /*倒计时 计数器*/
       

        // 表单验证，需要在 el-form-item 元素中增加 prop 属性
        rules: {
          username: [
            {required: true, message: '手机号为必填项', trigger: 'blur'}
          ],
          verifyCode: [
            {required: true, message: '验证码为必填项', trigger: 'blur'}
          ]
        },
        
        dialogVisible: false,
        message: ''
      }
    },
    methods: {
      // 验证
      getAuthCode () {
        // 1.首先判断是否未输入手机号码
        if(this.form.username != '' ){ 
          // 2.使用正则判断手机输入的验证码是否符合规范
          if(/^1[3456789]\d{9}$/.test(this.form.username)){ 
            // 3.设置倒计时间为60s
            this.auth_time = 60;
            // 4.隐藏获取验证码按钮,展示倒计时模块
            this.sendAuthCode = false;
            // 5.调用后端获取验证码接口的函数
            this.getPhoneCode() 
            var auth_timetimer = setInterval(()=>{
              // 6.设置每秒钟递减
              this.auth_time--;
              // 7.递减至0时,显示获取验证码按钮,隐藏倒计时模块,清除定时器,并将其置为null
              if(this.auth_time<=0){
                this.sendAuthCode = true;
                clearInterval(auth_timetimer);
              }
            }, 1000);
          } else {
              this.$message({
                type: 'error',
                message: '手机号格式不正确!'
              })
            }
        } else {
          this.$message({
            type: 'error',
            message: '请先填写手机号码!'
          })
        }
      },
      getPhoneCode(){
        let param = {'username': this.form.username}
        axios.post('/api/users/sendSms', param).then(function (response) {
          alert(response.data.message)
        })
        .catch(function (error) {
          alert(error)
        })
      },
      login() {
        // this.$router.push({name: "main", params: {}});
        // 数据校验
        this.$refs.form.validate(valid => {
          if (valid) {
            let param = {'username': this.form.username, 'verifyCode': this.form.verifyCode}
            let username = this.form.username
            removeToken()
            axios.post('/api/users/login',
              { data: param },
              {})
              .then(function (response) {
                let token = response.headers['authorization']
                setToken(token)
                localStorage.setItem('username',username)
                localStorage.setItem('userId',response.data)
                if(localStorage.getItem(localStorage.getItem('userId') + '-organizationName') === null){
//                  router.push('/main/guide')
                  router.push({name: 'MyOrgs', params: {}})
                }else {
//                  router.push('/main/guide')
                  router.push({name: 'Guide', params: {}})
                }
              })
              .catch(function (error) { });
          } else {
            this.dialogVisible = true
            return false
          }
        });
      },
      tosignup () {
        //未注册过跳转到注册界面
         router.push('/signup')
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
  .to {
    color: #FA5555;
    cursor: pointer;
  }
  
</style>
