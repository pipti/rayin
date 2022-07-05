<template>
  <div class="tinymce-editor">
    <div v-if="editorStatus"
         v-loading="!editorStatus">
      <editor v-model="editorValue"
              :init="init"
              :disabled="disabled"
              @onClick="onClick"
              id="elEditor"
              @objectSelected="objectSelected"
      >
      </editor>
    </div>

  </div>
</template>
<script>
import tinymce from 'tinymce/tinymce';
import Editor from '@tinymce/tinymce-vue';
import axios from 'axios';
// 编辑器插件plugins
// 更多插件参考：https://www.tiny.cloud/docs/plugins/
import 'tinymce/themes/silver';
import 'tinymce/plugins/image';// 插入上传图片插件
import 'tinymce/plugins/media';// 插入视频插件
import 'tinymce/plugins/table';// 插入表格插件
import 'tinymce/plugins/lists';// 列表插件
import 'tinymce/plugins/wordcount';// 字数统计插件
import 'tinymce/plugins/code';// 字数统计插件
import 'tinymce/plugins/fullscreen';// 全屏插件
import 'tinymce/plugins/pagebreak';// 断页插件
import 'tinymce/plugins/fullpage';// 整体页面设置
import 'tinymce/plugins/preview';// html预览插件
import 'tinymce/plugins/visualblocks';// 可见块插件
import 'tinymce/plugins/visualthymeleaf';// thymeleaf可见块插件
import 'tinymce/plugins/template';// 模板插件
import 'tinymce/plugins/autosave';// 可见块插件
import 'tinymce/plugins/lineheight'; // 行间距插件
import 'tinymce/plugins/indent2em'; // 行间距插件
import 'tinymce/plugins/quickbars'; // 行间距插件
import 'tinymce/plugins/hr';
import 'tinymce/plugins/newblock';
import 'tinymce/plugins/pdfmedia';
import 'tinymce/icons/default/icons.min.js';
export default {
  components: {
    Editor,
  },
  props: {
    value: {
      type: String,
      default: '',
    },
    // 基本路径，默认为空根目录，如果你的项目发布后的地址为目录形式，
    // 即abc.com/tinymce，baseUrl需要配置成tinymce，不然发布后资源会找不到
    baseUrl: {
      type: String,
      default: '',
    },
    disabled: {
      type: Boolean,
      default: false,
    },
    plugins: {
      type: [String, Array],
      default: 'lists image  table wordcount  fullscreen pagebreak fullpage preview visualblocks  visualthymeleaf lineheight indent2em  autosave hr nonbreaking newblock pdfmedia',
      // quickbars
    },
    toolbar: {
      type: [String, Array],
      default: 'fullscreen  restoredraft| visualthymeleaf visualblocks newblock | fontselect fontsizeselect lists table image pdfmedia| bold italic forecolor backcolor | '
      + '  lineheight indent2em outdent indent | alignleft aligncenter alignright alignjustify |formatselect| bullist numlist  | pagebreak  nonbreaking| removeformat',
    },
    templates: {
      type: [Object, Array],
      // eslint-disable-next-line
      default: () => [{title: 'Some title 1', description: 'Some desc 1', content: 'My content'},
        { title: 'Some title 2', description: 'Some desc 2', url: 'development.html' }],
    },
    quickbar: {
      type: [String, Array],
      default: 'newblock bold italic | quicklink h2 h3 blockquote quickimage quicktable',
    },

  },
  data() {
    return {
      editorStatus: false,
      init: {
        language_url: './tinymce/langs/zh_CN.js',
        language: 'zh_CN',
        skin_url: './tinymce/skins/ui/oxide',
        content_style: '',
        content_css: './tinymce/editor-content-customize.css',
        font_formats: '',
        fontsize_formats: '2px 3px 4px 5px 6px 7px 8px 9px 10px 11px 12px 13px 14px 15px 16px 17px 18px 19px 20px 22px 24px 36px 48px',
        preview_styles: true,
        autosave_interval: '30s',
        toolbar_drawer: 'sliding',
        autosave_ask_before_unload: true,
        autosave_restore_when_empty: false,
        // autoresize: 'on',
        // content_css: `/static/tinymce/skins/content/default/content.css`,
        // skin_url: `${this.baseUrl}/tinymce/skins/ui/oxide-dark`, // 暗色系
        // content_css: `${this.baseUrl}/tinymce/skins/content/dark/content.css`, // 暗色系
        height: this.$store.state.editorHeight,
        plugins: this.plugins,
        toolbar: this.toolbar,
        quickbars_selection_toolbar: this.quickbar,
        templates: this.templates,
        forced_root_block: false,
        // extended_valid_elements: 'span[th:text]',
        valid_elements: '*[*]',
        draggable_modal: true,
        branding: false,
        menubar: true,
        fullpage_hide_in_source_view: false,
        fullpage_default_encoding: 'UTF-8',
        fullpage_default_font_family: 'FangSong, HanaMinB',
        fullpage_default_font_size: '12px',
        // 此处为图片上传处理函数，这个直接用了base64的图片形式上传图片，
        // 如需ajax上传可参考https://www.tiny.cloud/docs/configure/file-image-upload/#images_upload_handler
        images_upload_handler: (blobInfo, success) => {
          const img = `data:image/jpeg;base64,${blobInfo.base64()}`;
          success(img);
        },
        init_instance_callback: (editor) => {
          editor.on('ObjectSelected', function (e) {
            this.selectedObject = e.target;

            // let text = tinyMCE.activeEditor.getContent( { 'format' : 'text' } );
          });

          //          editor.on('ExecCommand', function (e) {
          //            // console.log('The text color of ')
          //            console.log('preview')
          //            console.log(e)
          //            // this.selectedObject = e.target
          //            // console.log("object" + this.selectedObject)
          //            if (e.command === 'mcePreview') {
          //              console.log('mcePreview')
          //              console.log(editor)
          //              console.log(tinymce)
          //            }
          //          })

          //          editor.execCommand('mceInsertContent', function (e) {
          //            // console.log('The text color of ')
          //            console.log('preview')
          //            console.log(e)
          //            // this.selectedObject = e.target
          //            // console.log("object" + this.selectedObject)
          //            if (e.command === 'mcePreview') {
          //              console.log('mcePreview')
          //              console.log(editor)
          //              console.log(tinymce)
          //            }
          //          })
          //          editor.on('keydown', function (event) {
          //            // console.log(event)
          //            //if (event.isComposing || event.keyCode === 13) {
          //              // event.target.innerHTML =
          //           // }
          //          })

          //
          //          editor.on('focus', function (e) {
          //            // console.log('The text color of ')
          //            console.log('focus')
          //            console.log(editor.selection)
          //          })
          const { initEditorValue } = this;

          editor.on('ExecCommand', (e) => {
            if (e.command === 'mceNewDocument') {
              tinymce.activeEditor.setContent(initEditorValue);
            }
          });

          const tinymceActiv = tinymce.activeEditor;
          const tinymceActivBody = tinymceActiv.getBody();
          tinymceActiv.selection.select(tinymceActivBody);
          const text = tinymceActiv.selection.getContent({ format: 'text' });
          // console.log('editor中纯文本内容' + text.charCodeAt() + '---')
          if (text.charCodeAt() === 10) {
            const url = './init.html';
            axios.get(
                url,
              {},
              {},
            )
              .then((res) => {
              // 初始化编辑器中的HTML
                this.initEditorValue = res.data;
                console.log('编辑器初始化加载成功');
                tinymce.activeEditor.setContent(this.initEditorValue);
                tinymce.activeEditor.execCommand('mceInsertContent', false, '<div>请开始你的表演！</div>');
                this.completeFlag = true;
              })
              .catch((error) => {
                console.log(error);
              });

            //            this.$http.get('/static/tinymce/init.html', '').then((response) => {
            //              // 初始化编辑器中的HTML
            //              this.initEditorValue = response.data
            //              console.log('编辑器初始化加载成功')
            //              tinymce.activeEditor.setContent(this.initEditorValue)
            //              this.completeFlag = true
            //            }).catch(() => {
            //              console.log('编辑器初始化加载失败')
            //            })

            // console.log(initEditorValue)
          }
        },
      },
      editorValue: this.value,
      selectedObject: '',
      initEditorValue: '',
      completeFlag: false,
    };
  },
  mounted() {
    axios.get(
      this.GLOBAL.webappApiConfig.ElementDesign.UserElementFonts.url,
      {},
      {},
    )
      .then((res) => {
        if (res.data.code === 0) {
          let fontsTmp = '';
          console.log(res.data.content);
          if (res.data.content != null) {
            res.data.content.forEach((item) => {
              fontsTmp += `${item}=${item};`;
            });
            this.init.font_formats = fontsTmp + this.init.font_formats;
            console.log(this.init.font_formats);
            this.editorStatus = true;
          }
        }
      })
      .catch((error) => {
        console.log(error);
      });
    tinymce.init({});
  },
  methods: {
    // 添加相关的事件，可用的事件参照文档=> https://github.com/tinymce/tinymce-vue => All available events
    // 需要什么事件可以自己增加
    onClick(e) {
      this.$emit('click', e, tinymce);
      // console.log(tinymce)
      this.$store.state.editorSelectedEditor = tinymce;
      this.$store.state.editorSelectedElement = e;
    },
    objectSelected(e) {
      this.$emit('objectSelected', e, tinymce);
      // console.log(e)
    },
    // 可以添加一些自己的自定义事件，如清空内容
    clear() {
      this.editorValue = '';
    },
  },
  watch: {
    value(newValue) {
      this.editorValue = newValue;
    },
    editorValue(newValue) {
      this.$emit('input', newValue);
    },
    completeFlag(flag) {
      this.$emit('editorLoadComplete', flag);
    },
  },
};
</script>
<style>
  /*.tinymce-editor >> >> >> >> >>.tox-edit-area{*/
    /*background: rgb(204,204,204);*/
    /*border:1px solid black;*/

  /*}*/

  /*.tinymce-editor >> >> >> >> >> >>.tox-edit-area__iframe {*/
    /*background: white;*/
    /*width: 21cm;*/
    /*!*height: 29.7cm;*!*/
    /*!*display: block;*!*/
    /*margin: 0 auto;*/
    /*margin-bottom: 0.5cm;*/
    /*box-shadow: 0 0 0.5cm rgba(0,0,0,0.5);*/
  /*}*/

</style>
