<template>
  <div>
    <h1>规则模板设置</h1>
    <span>通过数据规则对应相应的模板配置</span>
  </div>
</template>

<script>
export default {
  name: 'ParameterTemplateRule',
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
