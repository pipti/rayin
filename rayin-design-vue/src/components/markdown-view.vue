<template>
  <div class="main" ref="markdownView">
    <VueMarkdown :source="htmlMD"></VueMarkdown>
  </div>
</template>

<script>
import axios from 'axios';
import VueMarkdown from 'vue-markdown';
// import Highlight from 'vue-markdown-highlight'
// import 'highlight.js/styles/default.css'
import hljs from 'highlight.js';
import 'highlight.js/styles/atom-one-dark.css';

const highlightCode = () => {
  const preEl = document.querySelectorAll('pre');
  //  console.log('获取')
  //  console.log(preEl)
  preEl.forEach((el) => {
    hljs.initHighlightingOnLoad(el);
  });
};

export default {
  name: 'MarkdownView',
  components: {
    VueMarkdown,
  },
  data() {
    return {
      htmlMD: '',
    };
  },
  methods: {
  },
  created() {

  },
  mounted() {
    const url = './help/README.md';
    axios.get(url).then((response) => {
      this.htmlMD = response.data;
      highlightCode();
    });
  },
  update() {
    highlightCode();
  },
};
</script>
<style>
  .main {
    padding: 10px 30px;
  }
  blockquote {
    border-left: #eee solid 5px;
    padding-left: 15px;
    color: #8e8e8e;
    margin-inline-start: 0px;
    margin-inline-end: 0px;
  }
  ul li {
    line-height: 25px;
  }
  pre code {
    background: #F6F6F6;
  }
  p code {
    color: #D34B62;
    background: #F6F6F6;
    margin: 0 2px;
  }

  @keyframes squeezeBody {
    from {
      width: 100%;
    }
    to {
      width: calc(100% - 300px);
    }
  }

  @-webkit-keyframes squeezeBody {
    from {
      width: 100%;
    }
    to {
      width: calc(100% - 300px);
    }
  }

  @keyframes stretchBody {
    from {
      width: calc(100% - 300px);
    }
    to {
      width: 100%;
    }
  }

  @-webkit-keyframes stretchBody {
    from {
      width: calc(100% - 300px);
    }
    to {
      width: 100%;
    }
  }

  .squeezed-body {
    animation: squeezeBody 0.5s ease;
    -webkit-animation: squeezeBody 0.5s ease;
    width: calc(100% - 300px);
  }

  .full-body {
    animation: stretchBody 0.5s ease;
    -webkit-animation: stretchBody 0.5s ease;
    width: 100%;
  }

  h1,
  h2,
  h3,
  h4,
  h5,
  h6 {
    font-weight: bold;
  }

  h1 {
    font-size: 2em;
    margin-block-start: 0.67em;
    margin-block-end: 0.67em;
    margin-inline-start: 0px;
    margin-inline-end: 0px;
  }

  h2 {
    font-size: 1.5em;
    margin-block-start: 0.83em;
    margin-block-end: 0.83em;
    margin-inline-start: 0px;
    margin-inline-end: 0px;
  }

  h3 {
    font-size: 1.17em;
    margin-block-start: 1em;
    margin-block-end: 1em;
    margin-inline-start: 0px;
    margin-inline-end: 0px;
  }

  h4 {
    margin-block-start: 1.33em;
    margin-block-end: 1.33em;
    margin-inline-start: 0px;
    margin-inline-end: 0px;
  }

  .serif {
    font-family: 'Old Standard TT', serif;
  }

  .top-bar {
    height: 45px;
    min-height: 45px;
    position: absolute;
    top: 0;
    right: 0;
    left: 0;
  }

  .bars-lnk {
    color: #fff;
  }

  .bars-lnk i {
    display: inline-block;
    margin-left: 10px;
    margin-top: 7px;
  }

  .bars-lnk img {
    display: inline-block;
    margin-left: 10px;
    margin-top: -15px;
    margin-right: 15px;
    height: 35px;
  }

  .lateral-menu {
    background-color: #333;
    color: rgb(144, 144, 144);
    width: 300px;
    font-family: 'Open Sans', 'Myriad Pro', 'Lucida Grande', 'Lucida Sans Unicode',
    'Lucida Sans', Geneva, Verdana, sans-serif;
  }

  .lateral-menu label {
    color: rgb(144, 144, 144);
  }

  .lateral-menu-content {
    padding-left: 10px;
    height: 100%;
    font-size: 12px;
    font-style: normal;
    font-variant: normal;
    font-weight: bold;
    line-height: 16px;
  }

  .lateral-menu-content .title {
    padding-top: 15px;
    font-size: 2em;
    height: 45px;
  }

  .lateral-menu-content-inner {
    overflow-y: auto;
    height: 100%;
    padding-top: 10px;
    padding-bottom: 50px;
    padding-right: 10px;
    font-size: 0.9em;
  }

  .container {
    display: flex;
    flex-direction: row;
    flex-wrap: nowrap;
    justify-content: center;
    align-items: stretch;
    width: 100%;
    height: 100%;
    padding-top: 65px;
  }

  .container>* {
    display: block;
    width: 50%;
    margin-left: 10px;
    margin-right: 10px;
    max-height: 100%;
  }

  .container textarea {
    resize: none;
    font-family: Consolas, "Liberation Mono", Courier, monospace;
    height: 97%;
    max-height: 97%;
    width: 45%;
  }

  #preview {
    height: 97%;
    max-height: 97%;
    border: 1px solid #eee;
    overflow-y: scroll;
    width: 55%;
    padding: 10px;
  }

  /*pre {*/
    /*white-space: pre-wrap;*/
    /*!* css-3 *!*/
    /*white-space: -moz-pre-wrap;*/
    /*!* Mozilla, since 1999 *!*/
    /*white-space: -pre-wrap;*/
    /*!* Opera 4-6 *!*/
    /*white-space: -o-pre-wrap;*/
    /*!* Opera 7 *!*/
    /*word-wrap: break-word;*/
    /*!* Internet Explorer 5.5+ *!*/
    /*!*background-color: #f8f8f8;*!*/
    /*border: 1px solid #dfdfdf;*/
    /*margin: 1em 0px;*/
  /*}*/

  /*pre {*/
    /*padding: 16px;*/
    /*overflow: auto;*/
    /*font-size: 85%;*/
    /*line-height: 1.45;*/
    /*!*background-color: #000000;*!*/
    /*border-radius: 0px;*/
  /*}*/

  .modal-wrapper {
    position: absolute;
    width: 100%;
    height: 100%;
    top: 0;
    left: 0;
    z-index: 999;
    background-color: rgba(51, 51, 51, 0.5);
  }

  .modal-inner {
    margin-top: 200px;
    margin-left: auto;
    margin-right: auto;
    width: 600px;
    height: 225px;
    background-color: #fff;
    opacity: 1;
    z-index: 1000;
  }

  .modal-close-btn {
    float: right;
    display: inline-block;
    margin-right: 5px;
    color: #ff4336;
  }

  .modal-close-btn:hover {
    float: right;
    display: inline-block;
    margin-right: 5px;
    color: #8d0002;
  }

  .modal-topbar {
    clear: both;
    height: 25px;
  }

  .modal-inner .link-area {
    margin: 10px;
    height: 170px;
  }

  .modal-inner textarea {
    width: 100%;
    height: 100%;
    margin: 0;
    padding: 0;
  }

  .version {
    color: white;
    font-size: 0.8em !important;
  }
</style>
