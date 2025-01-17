<!--富文本编辑器
2019.3.05 姚政伟 创建
-->
<template>
  <div style="line-height:normal">
    <script :id="id" type="text/plain"></script>
  </div>
</template>

<script>
  import '../../../public/ueditor/ueditor.config.js'
  import '../../../public/ueditor/ueditor.all.min.js'
  import '../../../public/ueditor/lang/zh-cn/zh-cn.js'
  import '../../../public/ueditor/ueditor.parse.min.js'

  export default {
    name: 'Ueditor',
    props: {
      content: {
        type: String,
        required: false,
        default: ''
      },
      config: {
        type: Object
      },
      unique: {
        type: Number,
        required: false
      }
    },
    data() {
      return {
        editor: null,
        loading: false,
        defaultConfig: {
          UEDITOR_HOME_URL: process.env.NODE_ENV === 'production' ? '/admin/ueditor/' : '/ueditor/',
          serverUrl: process.env.VUE_APP_BASE_API2 + '/ueditor/exec'
        }
      }
    },
    computed: {
      id() {
        if (typeof this.unique != 'undefined' && this.unique!=null){
          return 'editor' + this.unique
        }else{
          const ram = Math.random();
          return 'editor' + ram
        }
      }
    },

    watch: {
      'content': function (value) {
        const that = this;
        if (value) {
          if (that.loading) {
            that.editor.setContent(value) // 确保UE加载完成后，放入内容。
          } else {
            that.editor.addListener('ready', function () {
              that.loading = true;
              that.editor.setContent(value) // 确保UE加载完成后，放入内容。
            })
          }
        }
      }
    },

    mounted() {
      const _this = this;
      _this.editor = UE.getEditor(this.id, {..._this.defaultConfig, ..._this.config}); // 初始化UE
      _this.editor.addListener('ready', function () {
        _this.loading = true;
        if (_this.content) {
          _this.editor.setContent(_this.content)
        }
      })
    },
    destroyed() {
      this.editor.destroy()
    },
    methods: {
      getUEContent() { // 获取内容方法
        return this.editor.getContent()
      }
    }
  }
</script>
