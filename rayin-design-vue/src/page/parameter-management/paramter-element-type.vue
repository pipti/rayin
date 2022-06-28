<template>
  <div>
    <el-row>
      <el-col :span="12">
        <el-button type="primary" plain size="medium" style="border-radius: 0px" @click="paramAdd"><i class="el-icon-document-add"></i>新增参数</el-button>
        <el-tooltip class="item" effect="dark" placement="right-start" style="border-radius: 0px">
          <div slot="content">参数配置</div>
          <el-button size="medium"><i class="el-icon-help"></i>提示</el-button>
        </el-tooltip>
      </el-col>
      <el-col :span="12">
        <el-input placeholder="请输入搜索内容" v-model="searchKey" class="input-with-select" @keyup.enter.native="paramSearchClick">
          <el-button slot="append" icon="el-icon-search" @click="paramSearchClick"></el-button>
        </el-input>
      </el-col>
    </el-row>
    <el-row>
      <template>
        <el-table
          :data="tlData"
          style="width: 100%"
          :height="this.$store.state.tableHeight">
          <el-table-column
            prop="parentParamName"
            label="参数归属名称"
            width="300">
            <template slot-scope="scope">
              <el-popover trigger="hover" placement="top">
                {{ scope.row.name }}
                <div slot="reference" class="name-wrapper">
                  <div v-if="scope.row.paramName !== null">{{ scope.row.name.substring(0,10) }}
                    <span v-if="scope.row.paramName.length > 10">...</span>
                  </div>
                </div>
              </el-popover>
            </template>
          </el-table-column>
          <el-table-column
            prop="paramName"
            label="参数名称"
            width="300">
            <template slot-scope="scope">
              <el-popover trigger="hover" placement="top">
                {{ scope.row.name }}
                <div slot="reference" class="name-wrapper">
                  <div v-if="scope.row.paramName !== null">{{ scope.row.name.substring(0,10) }}
                    <span v-if="scope.row.paramName.length > 10">...</span>
                  </div>
                </div>
              </el-popover>
            </template>
          </el-table-column>
          <el-table-column
            prop="paramKey"
            label="参数Key"
            width="300">
            <template slot-scope="scope">
              <el-popover trigger="hover" placement="top">
                {{ scope.row.name }}
                <div slot="reference" class="name-wrapper">
                  <div v-if="scope.row.paramName !== null">{{ scope.row.name.substring(0,10) }}
                    <span v-if="scope.row.paramName.length > 10">...</span>
                  </div>
                </div>
              </el-popover>
            </template>
          </el-table-column>
          <el-table-column
            prop="paramValue"
            label="参数Value"
            width="300">
            <template slot-scope="scope">
              <el-popover trigger="hover" placement="top">
                {{ scope.row.name }}
                <div slot="reference" class="name-wrapper">
                  <div v-if="scope.row.paramName !== null">{{ scope.row.name.substring(0,10) }}
                    <span v-if="scope.row.paramName.length > 10">...</span>
                  </div>
                </div>
              </el-popover>
            </template>
          </el-table-column>
          <el-table-column
            fixed="right"
            prop="address"
            label="操作"
            width="220">
            <template slot-scope="scope">
              <el-button type="text" size="small" @click="tplEdit">编辑</el-button>
              <el-button type="text" size="small">删除</el-button>
              <el-button type="text" size="small">复制</el-button>
              <el-button type="text" size="small">测试</el-button>
              <el-button type="text" size="small">发布</el-button>
            </template>
          </el-table-column>
        </el-table>
        <div style="width:100%;text-align: right">
          <el-pagination
            @current-change="handleCurrentChange"
            layout="prev, pager, next"
            :total="tlDataTotal"
            :current-page="tlCurrentPage"
            :page-size="pageSize">
          </el-pagination>
        </div>
      </template>

    </el-row>
  </div>
</template>

<script>
import axios from 'axios'
export default {
  data () {
    return {
      activeIndex: '1',
      activeIndex2: '1',
      paramData: [],
      paramDataTotal: 1,
      paramCurrentPage: 1,
      pageSize: this.$store.state.pageSize,
      searchKey: '',
      parentId: '',
      parentParamName: ''
    }
  },
  methods: {
    handleSelect (key, keyPath) {
      console.log(key, keyPath)
    },
    paramAdd () {
      // this.$router.push({name: 'TemplateSet', params: {}})
    },
    paramSave () {

    },
    paramEdit () {
      // this.$router.push({name: 'TemplateSet', params: {}})
    },
    paramSearchClick () {
      this.handleCurrentChange(1)
    },
    handleCurrentChange (val) {
      axios.get(this.GLOBAL.webappApiConfig.BusinessParamManagement.BusinessParamQuery.url + (this.searchKey === '' ? '' : '/' + this.searchKey) + '?pageCurrent=' + val + '&pageSize=' + this.pageSize,
        {},
        {})
        .then(res => {
          this.paramData = res.data.data.records
          this.paramDataTotal = res.data.data.total
          this.paramCurrentPage = res.data.data.current
          console.log(res)
        })
        .catch(function (error) {
          console.log(error)
        })
    }
  }
}
</script>
