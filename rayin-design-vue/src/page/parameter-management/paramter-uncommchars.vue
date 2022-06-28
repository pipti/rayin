<template>
  <div>
    <h1>生僻字参数配置</h1>
    <span>设置生僻字对应的字体名称</span>
  </div>
</template>

<script>
export default {
  name: 'uncommcharsparamset',
  data () {
    return {
      activeIndex: '1',
      activeIndex2: '1'
    }
  },
  methods: {
    handleSelect (key, keyPath) {
      console.log(key, keyPath)
    }
  }
}
</script>
