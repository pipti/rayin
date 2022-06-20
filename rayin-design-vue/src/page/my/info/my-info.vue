
<template>
  <div>
    <el-card style="border-radius: 0px;">
      <div style="width:100%;">
        <h2>基本信息</h2>
      </div>
      <el-divider></el-divider>
      <el-form ref="userForm" :model="userForm" :rules="rules" label-width="0px" style="">

        <div style="width:100%;">
          <span class="info-title">手机</span>
          <el-form-item label="" prop="phone" class="login-input">
            <el-input v-model="userForm.phone" placeholder="" style="width:80%;" >
            </el-input>
          </el-form-item>
          <span class="info-title">姓名</span>
          <el-form-item label="" prop="realName" class="login-input">
            <el-input v-model="userForm.realName" placeholder="" style="width:80%;" >
            </el-input>
          </el-form-item>
          <span class="info-title">性别</span>
          <el-form-item label="" prop="sex" class="login-input">
            <el-radio-group v-model="userForm.sex">
                <el-radio :label="0">男</el-radio>
                <el-radio :label="1">女</el-radio>
            </el-radio-group>
          </el-form-item>
<!--          <span class="info-title">出生年月</span>-->
<!--          <el-form-item label="" prop="sex" class="login-input">-->
<!--            <el-select-dropdown>-->
<!--            </el-select-dropdown>-->
<!--            <el-select>eere</el-select>-->
<!--            <el-input v-model="form.birthday" style="width:80%">-->
<!--            </el-input>-->
<!--          </el-form-item>-->
          <span class="info-title">邮箱</span>
          <el-form-item label="" prop="mail" class="login-input">
            <el-input v-model="userForm.mail" style="width:80%">
            </el-input>
          </el-form-item>
          <span class="info-title">微信</span>
          <el-form-item label="" prop="weChat" class="login-input">
            <el-input v-model="userForm.weChat" style="width:80%">
            </el-input>
          </el-form-item>
          <span class="info-title">职业</span>
          <el-form-item label="" prop="profession" class="login-input">
            <el-input v-model="userForm.profession" style="width:80%">
            </el-input>
          </el-form-item>
          <span class="info-title">住址</span>
          <el-form-item label="" prop="address" class="login-input">
            <el-input v-model="userForm.address" style="width:80%">
            </el-input>
          </el-form-item>
          <span class="info-title">公司</span>
          <el-form-item label="" prop="company" class="login-input">
            <el-input v-model="userForm.company" style="width:80%">
            </el-input>
          </el-form-item>
          <span class="info-title">职务</span>
          <el-form-item label="" prop="job" class="login-input">
            <el-input v-model="userForm.job" style="width:80%">
            </el-input>
          </el-form-item>
          <el-button type="primary" icon="el-icon-user-solid" @click="saveInfo" style="width:80%">保存</el-button>
        </div>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'MyInfo',
  components: {
  },
  data () {
    return {
      userForm: {
        phone: '',
        realName: '',
        number: '',
        sex: '',
        birthday: '',
        mail: '',
        weChat: '',
        profession: '',
        address: '',
        company: '',
        job: ''
      }
    }
  },
  methods: {
    saveInfo () {
      let userId = localStorage.getItem('userId')
      axios.post('/api/users/saveInfo',
        {'userId': userId, 'phone': this.userForm.phone, 'realName': this.userForm.realName, 'sex': this.userForm.sex, 'birthday': this.userForm.birthday, 'mail': this.userForm.mail, 'weChat': this.userForm.weChat, 'profession': this.userForm.profession, 'address': this.userForm.address, 'company': this.userForm.company, 'job': this.userForm.job})
        .then(function (response) {
        })
        .catch(function (error) {
          alert(error)
        })
    }
  },
  mounted () {
    let userId = localStorage.getItem('userId')
    axios.post('/api/users/userDetails',
      {'userId': userId})
      .then((response) => {
        console.log(response.data)
        this.userForm = response.data
        if (response.data.sex != null && response.data.sex !== '') {
          this.userForm.sex = Number.parseInt(response.data.sex)
        }
      })
      .catch(function (error) {
        alert(error)
      })
  },
  created () {
  }
}
</script>
<style scoped>
  .info-title {
    font-weight: bolder;
    color: #8a8a8a;
  }
  .el-input {
    margin-top: 10px;
  }
</style>
