<!--
 * @Description: App.vue
 * @Date: 2022-03-21 12:16:32
 * @LastEditTime: 2022-03-21 12:21:49
-->
<template>
  <div id="app">
    <router-view :style="{height:(this.$store.state.windowHeight - 20) + 'px'}" v-wechat-title='$route.meta.title'/>
  </div>
</template>

<script>
export default {
  name: 'App',
  data() {
    return {
      activeIndex: '1',
      activeIndex2: '1',
      isCollapse: true
    };
  },
    mounted() {
  },
};
</script>

<style scoped>
#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  /*text-align: center;*/
  /*color: #2c3e50;*/
  /*margin-top: 60px;*/
}
.el-menu-vertical-demo:not(.el-menu--collapse) {
  min-height: 550px;
}

</style>
<style>
::-webkit-scrollbar {/*滚动条整体样式*/
  width: 6px;     /*高宽分别对应横竖滚动条的尺寸*/
  height: 1px;
}
::-webkit-scrollbar-track {/*滚动条里面轨道*/
  /*-webkit-box-shadow: inset 0 0 5px rgba(0,0,0,0.2);*/
  /*border-radius: 10px;*/
  background: #ffffff;
}
::-webkit-scrollbar-thumb {/*滚动条里面小方块*/
  border-radius: 5px;
  -webkit-box-shadow: inset 0 0 5px rgba(0,0,0,0.2);
  background: #C0C4CC;
}
</style>
