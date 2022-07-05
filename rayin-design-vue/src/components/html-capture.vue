<template>

    <!--<img :src="imgUrl"/>-->
  <el-image
    :src="imgUrl"
    fit="scale-down" :preview-src-list="srcList"></el-image>
    <!--<html-panel :url.sync="htmlUrl" id="htmlSrc" style="display:none"
    @loadCompleted="htmlLoadCompleted"></html-panel>-->

</template>

<script>
import html2canvas from 'html2canvas';

export default {
  name: 'HtmlCapture',
  components: {
  },
  props: {
    htmlUrl: String,
    htmlCode: String,
    width: String,
    height: String,
    src: String,
  },
  data() {
    return {
      imgUrl: '',
      srcList: [

      ],
    };
  },
  methods: {
    // v 将Vue对象传递进方法
    htmlToImg(v) {
      // 通过url获取dom
      // 通过创建临时的iframe动态加载页面
      const iframe = document.createElement('iframe');
      iframe.ref = 'htmlLoadFrame';
      if (this.htmlUrl === '' || this.htmlUrl === null || this.htmlUrl === undefined) {
        iframe.srcdoc = this.htmlCode;
      } else {
        iframe.src = this.htmlUrl;
      }

      // 隐藏
      document.body.appendChild(iframe);
      iframe.style.cssText = 'position: absolute; opacity:0; z-index: -9999';

      // iframe加载完成后触发
      iframe.onload = function () {
        //        console.log(iframe.contentWindow.document.body.clientLeft)
        //        console.log(iframe.contentDocument.documentElement.clientWidth * 2)
        //        console.log(iframe.contentWindow.innerWidth * 2)
        //        console.log(iframe.contentWindow.screenWidth)
        // 生成iframe中的页面截图
        html2canvas(iframe.contentDocument.documentElement, {
          // 设置截图的背景色
          backgroundColor: 'white', // 设置白色背景
          useCORS: true, // 如果截图的内容里有图片,可能会有跨域的情况,加上这个参数,解决文件跨域问题
          allowTaint: true, // 允许跨域（图片跨域相关）
          taintTest: true, // 是否在渲染前测试图片
          scale: 4, // 放大倍数,4倍相对文字比较清晰
          windowWidth: iframe.contentDocument.documentElement.clientWidth * 2,
          width: iframe.contentDocument.documentElement.clientWidth * 2,
        }).then((canvas) => {
          v.imgUrl = canvas.toDataURL('image/png'); // 转换为Base64
          v.srcList.push(v.imgUrl); // 通过参数传递的Vue对象设置变量值，此处无法直接操作Vue this对象
          // 截图完成后销毁
          iframe.remove();
        });
      };
    },
  },
  mounted() {
    this.htmlToImg(this);
  },
};
</script>

<style>

</style>
