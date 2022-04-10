<template>
  <div>
    <h1>质检规则配置</h1>
    <span>配置质检的检索数据条件，通过数据路径和符合条件进行配置</span>
  </div>
</template>

<script>
export default {
  name: 'paramter-qualitycheck',
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
