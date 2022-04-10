<template>
  <div>
    <h1>凭证查询参数配置</h1>
    <span>通过配置数据路径进行定制凭证查询管理中的查询条件，以及展示字段</span>

  </div>
</template>

<script>
export default {
  name: 'queryparamset',
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
