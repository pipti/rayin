<template>
  <div>
    <h1>国际化参数配置</h1>
    <span>主要针对凭证的名称进行国际化转换设置，需要设置报文的检索路径</span>
  </div>
</template>

<script>
export default {
  name: 'paramter-internation',
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
