<template>
  <dev>
    <el-container>
      <el-header style="text-align: right;">
        <el-menu :default-active="activeIndex" class="el-menu-demo" mode="horizontal" @select="handleSelect">
          <el-menu-item index="1">首页</el-menu-item>

          <el-submenu index="2">
            <template slot="title">环境选择</template>
            <el-menu-item index="2-1">开发</el-menu-item>
            <el-menu-item index="2-2">测试</el-menu-item>
            <el-menu-item index="2-3">生产</el-menu-item>
          </el-submenu>

          <el-menu-item index="3">消息中心</el-menu-item>

          <el-submenu index="4">
            <template slot="title">我的工作台</template>
            <el-menu-item index="4-1">选项1</el-menu-item>
            <el-menu-item index="4-2">选项2</el-menu-item>
            <el-menu-item index="4-3">选项3</el-menu-item>
            <el-submenu index="4-4">
              <template slot="title">我得收藏</template>
              <el-menu-item index="4-4-1">选项1</el-menu-item>
              <el-menu-item index="4-4-2">选项2</el-menu-item>
              <el-menu-item index="4-4-3">选项3</el-menu-item>
            </el-submenu>
          </el-submenu>
        </el-menu>
        <!--        <el-dropdown>-->
        <!--          <i class="el-icon-setting" style="margin-right: 15px"></i>-->
        <!--          <el-dropdown-menu slot="dropdown">-->
        <!--            <el-dropdown-item>查看</el-dropdown-item>-->
        <!--            <el-dropdown-item>新增</el-dropdown-item>-->
        <!--            <el-dropdown-item>删除</el-dropdown-item>-->
        <!--          </el-dropdown-menu>-->
        <!--        </el-dropdown>-->
        <!--        <span>王小虎</span>-->
      </el-header>
      <el-container>
        <el-aside >
          <el-menu
            default-active="this.$route.path"
            class="el-menu-vertical-demo "
            @open="handleOpen"
            @close="handleClose" router>
            <el-submenu index="1">
              <template slot="title">
                <i class="el-icon-collection"></i>
                <span>模板管理</span>
              </template>
              <el-menu-item index="1-1" route="/main/guide">
<!--              <el-menu-item index="1-1" @click="open('guide')">-->
                <i class="el-icon-help"></i>
                <span>向导</span>
              </el-menu-item>

              <el-menu-item index="1-3" route="/main/ElementDesign">
                <i class="el-icon-document"></i>
                <span>构件设计</span>
              </el-menu-item>
              <el-menu-item index="1-4" route="/main/templateset">
                <i class="el-icon-document-copy"></i>
                <span>模板配置</span>
              </el-menu-item>
            </el-submenu>
            <el-submenu index="2">
              <template slot="title">
                <i class="el-icon-files"></i>
                <span>凭证管理</span>
              </template>
              <el-menu-item index="2-1" route="/main/proofquery">
                <i class="el-icon-search"></i>
                <span slot="title">文件查询下载</span>
              </el-menu-item>
              <el-menu-item index="2-2" route="/main/proofcheck">
                <i class="el-icon-document-checked"></i>
                <span slot="title">文件质检</span>
              </el-menu-item>
            </el-submenu>
            <el-submenu index="3">
              <template slot="title">
                <i class="el-icon-setting"></i>
                <span>参数管理</span>
              </template>
              <el-menu-item index="3-1" route="/main/elementparamset">
                <i class="el-icon-collection"></i>
                <span slot="title">构件类型参数设置</span>
              </el-menu-item>
              <el-menu-item index="3-2" route="/main/qualitycheckparamset">
                <i class="el-icon-finished"></i>
                <span slot="title">质检规则配置</span>
              </el-menu-item>
              <el-menu-item index="3-3" route="/main/uncommcharsparamset">
                <i class="el-icon-cpu"></i>
                <span slot="title">生僻字参数配置</span>
              </el-menu-item>
              <el-menu-item index="3-4" route="/main/queryparamset">
                <i class="el-icon-thumb"></i>
                <span slot="title">凭证查询参数配置</span>
              </el-menu-item>
              <el-menu-item index="3-5" route="/main/ruletemplateset">
                <i class="el-icon-notebook-2"></i>
                <span slot="title">模板规则映射参数配置</span>
              </el-menu-item>
              <el-menu-item index="3-6" route="/main/internationparamset">
                <i class="el-icon-chat-line-square"></i>
                <span slot="title">国际化参数配置</span>
              </el-menu-item>
            </el-submenu>
          </el-menu>
        </el-aside>
        <el-main>
          <keep-alive>
            <router-view name="inside"
                         v-if="$route.meta.keepAlive" />
          </keep-alive>
          <router-view
                       v-if="!$route.meta.keepAlive" />
        </el-main>
      </el-container>
    </el-container>
  </dev>
</template>

<script>
export default {
  name: 'sidebarItem',
  methods: {
    open(item) {
      this.$router.push(item);
    }
  }
};
</script>

<style scoped>

</style>
