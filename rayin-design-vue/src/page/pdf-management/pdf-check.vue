<template>
  <div>
    <h1>人工质检</h1>
    <span>通过质检规则配置被过滤出来的凭证进行查询和质检</span>
  </div>
</template>

<script>
export default {
  name: 'ProofCheck',
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
