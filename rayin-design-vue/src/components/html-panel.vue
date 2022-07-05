<template>
  <iframe :srcdoc="htmlCode" :src="htmlUrl" style="width:100%;frameborder:0px;border:0px;scroll:auto"
          :style="{height:this.$store.state.elViewerBoxHeight}" ref="htmlView"></iframe>
</template>

<script>
export default {
  name: 'HtmlPanel',
  // 使用时请使用 :url.sync=""传值
  props: {
    htmlUrl: {
      required: false,
    },
    htmlCode: {
      required: false,
    },
  },
  data() {
    return {
      loading: false,
      html: '',
      loadCompletedFlag: false,
    };
  },
  mounted() {
    // this.load(this.url,this.code)
  },
  methods: {
    loadUrl(url) {
      if (url && url.length > 0) {
        // 加载中
        this.loading = true;
        const param = {
          accept: 'text/html, text/plain',
        };
        this.$http.get(url, param).then((response) => {
          this.loading = false;
          // 处理HTML显示
          this.htmlUrl = response.data;
          this.loadCompletedFlag = true;
          console.log('模板加载成功');
        })
          .catch(() => {
            this.loading = false;
            this.html = '加载失败';
          });
      }
    },
    loadCode(code) {
      this.html = code;
    },
  },
  watch: {
    htmlUrl(value) {
      this.$ref.htmlView.removeAttribute('srcdoc');
      this.loadUrl(value);
    },
    htmlCode() {
      // console.log('code' + value)
      // this.loadCode(value)
      this.loadCompletedFlag = true;
    },
    loadCompletedFlag(val) {
      if (val === true) {
        this.$emit('loadCompleted', this.loadCompletedFlag);
      }
    },
  },
};
</script>

<style scoped>

</style>
