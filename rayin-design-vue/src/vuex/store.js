import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)
// 动态数据共享
const store = new Vuex.Store({
  // 定义状态
  state: {
    selectedJsonDataKey: '',
    selectedJsonDataValue: '',
    editorSelectedElement: '',
    editorSelectedEditor: '',
    boxHeight: window.innerHeight - 250 + 'px',
    tableHeight: window.innerHeight - 200 + 'px',
    iframeHeight: window.innerHeight - 230 + 'px',
    windowHeight: window.innerHeight,
    windowWidth: window.innerWidth,
    editorHeight: window.innerHeight - 200 + 'px',
    elViewerBoxHeight: window.innerHeight - 170 + 'px',
    pageSize: (localStorage.getItem('ecs-page-size') === null ? 10 : parseInt(localStorage.getItem('ecs-page-size'))),
    loading: false,
    organizationName: (localStorage.getItem(localStorage.getItem('userId') + '-organizationName')),
    organizationId: (localStorage.getItem(localStorage.getItem('userId') + '-organizationId'))
  }

})

// const getters = new Vuex.Store({
//   handleSizeChange (val) {
//     this.$store.state.pageSize = val
//     localStorage.setItem('ecs-page-size', val)
//     console.log('全局每页显示条数更改=>' + val)
//   }
// })

export default store
