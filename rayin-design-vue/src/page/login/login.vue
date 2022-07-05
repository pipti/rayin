<template>
  <el-container>
    <el-row style="padding:10px 5px 5px 5px;height:30px;">
      <el-col :span="12" >
          <el-image :src="require('@/assets/images/logo5.png')" style="width:210px"/>
      </el-col>
      <el-col :span="11" style="text-align: right">
        <el-button type="primary" plain size="small" @click="toSignup">{{$i18n.t('login.signUp')}}</el-button>
      </el-col>
      <el-col :span="1" style="text-align: right">
          <span class="language" @click="toggleLanguage">
            <svg-icon icon-name="language" className="language-svg"></svg-icon>
          </span>
          <span class="github" @click="goToGithub">
            <svg-icon icon-name="github" className="github-svg"></svg-icon>
          </span>
      </el-col>
    </el-row>
    <el-row><el-divider></el-divider></el-row>

    <el-main style="overflow:visible">
      <el-row>
        <el-col :span="15">
          <el-image :src="require('@/assets/images/login_left.png')" style="width:80%;margin-top:60px"></el-image>
        </el-col>
        <el-col :span="9">
          <el-card style="width:450px;margin-top:110px;height:400px;border-radius: 0px;text-align: center">
            <el-form ref="form" :model="form" :rules="rules" label-width="0px"  >
              <div style="width:100%;text-align: center;margin-top:30px"><h2>{{$i18n.t('login.signIn')}}</h2></div>
              <el-form-item label="" prop="username" class="login-input">
                <el-input v-model="form.username" :placeholder="$i18n.t('login.placeholder.inputUserName')"
                          style="width:80%;" tabindex="1" ref="username">
                  <el-button slot="prepend" icon="el-icon-user"></el-button>
                </el-input>
              </el-form-item>
              <el-form-item label="" prop="password" class="login-input">
                <el-input v-model="form.password" type="password" :placeholder="$i18n.t('login.placeholder.inputPass')"
                          style="width:80%" tabindex="2">
                  <el-button slot="prepend" icon="el-icon-key"></el-button>
                </el-input>
              </el-form-item>
              <el-form-item label="" class="login-input" prop="code">
                <el-input v-model="form.code" :placeholder="$i18n.t('login.placeholder.inputCode')"
                          style="width:47%" @keyup.enter.native="login"
                          tabindex="3">
                  <el-button slot="prepend" icon="el-icon-info"></el-button>
                </el-input>
                <el-image :src="captchaDataLoginPass" style="width:130px" ondragstart="return false;" class="imgCode"
                          @click="refurbishCode('loginPass')" draggable="false"/>
              </el-form-item>
              <el-button type="primary" icon="el-icon-user-solid" @click="login" style="width:80%">
                {{$i18n.t('login.signIn')}}</el-button>
            </el-form>
            <el-dialog
              title="温馨提示"
              :visible.sync="dialogVisible"
              width="30%"
              :before-close="handleClose">
              <span>{{message}}</span>
              <span slot="footer" class="dialog-footer">
                <el-button type="primary" @click="dialogVisible = false">{{$i18n.t('confirm')}}</el-button>
              </span>
            </el-dialog>
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
    name: 'login',
    data() {
      return {
        codeUrl: '/api/users/code/image',
        form: {
          username: "",
          password: "",
          code:""
        },
        // 表单验证，需要在 el-form-item 元素中增加 prop 属性
        rules: {
          username: [
            {required: true, message: this.$i18n.t('login.check.inputUserName'), trigger: 'blur'}
          ],
          password: [
            {required: true, message: this.$i18n.t('login.check.inputPass'), trigger: 'blur'}
          ],
          code: [
            {required: true, message: this.$i18n.t('login.check.inputCode'), trigger: 'blur'}
          ]
        },
        dialogVisible: false,
        message: '',
        captchaDataLoginPass:''
      };
    },methods: {
      login() {
        // this.$router.push({name: "main", params: {}});
        // 数据校验
        this.$refs.form.validate(valid => {
          if (valid) {
            let param = {'username': this.form.username, 'password': this.form.password, 'code': this.form.code}
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
      toSignup() {
        router.push('/signup')
      },
      toPhoneLogin(){
        router.push('/phone-login')
      },
      refurbishCode(refreshType) {
        //let num = Math.ceil(Math.random() * 10);
        this.codeUrl = "/api/users/code/image?" + "&refreshType=" + refreshType;
        this.captcha(refreshType);
      },
    captcha(refreshType) {
      axios
        .get(this.codeUrl,  {})
        .then(res =>{
          if(refreshType === 'loginSMS'){
            this.captchaDataLoginSMS = res.data.content;
          }else if(refreshType === 'loginPass'){
            this.captchaDataLoginPass = res.data.content;
          }

          //console.log(this.captchaDataLoginSMS)
        })
    },
      handleClose(){

      },
      toggleLanguage() {
        switch (this.$i18n.locale) {
          case 'en':
            this.$i18n.locale = 'zh';
            localStorage.setItem('rayin-quick-vue2-language', 'zh');
            break;
          case 'zh':
            this.$i18n.locale = 'en';
            localStorage.setItem('rayin-quick-vue2-language', 'en');
            break;
        }
      },
      goToGithub() {
        window.open('https://github.com/pipti/rayin', '_blank');
      },
    },mounted(){
      this.refurbishCode('loginPass');
      this.$refs.username.focus()
    },computed: {
      isZhLang() {
        return this.$i18n.locale === 'zh';
      },
      isEnLang() {
        return this.$i18n.locale === 'en';
      },
    },
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

  .el-form-item__error {
    margin-left: 22% !important;
  }

</style>
<style>
  .login-input .el-form-item__error {
    margin-left: 10% !important;
  }
</style>
<style lang="scss" scoped>

  .github {
    margin-left: 10px;
    cursor: pointer;
    .github-svg {
      width: 25px;
      height: 25px;
    }
  }

  .language {
    margin-left: 10px;
    cursor: pointer;
    .language-svg {
      width: 25px;
      height: 25px;
    }
  }
</style>
