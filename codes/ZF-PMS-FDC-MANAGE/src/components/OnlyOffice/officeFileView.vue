<template>
    <div>
        <onlyoffice-editor :loading="loading.editor" :config="editorConfig">
        </onlyoffice-editor>
    </div>
</template>
    
<script>
import OnlyofficeEditor from '@/components/DocumentEditor'

export default {
    components: {
        OnlyofficeEditor
    },
    data() {
        return {
            loading: {
                editor: true,
                save: true,
                forceSave: true
            },
            // 支持的word编辑器的类型
            supportWordFile:['doc', 'docm', 'docx', 'dot', 'dotm', 'dotx', 'epub', 'fb2', 'fodt', 'htm', 'html', 'mht', 'mhtml', 'odt', 'ott', 'rtf', 'stw', 'sxw', 'txt', 'wps', 'wpt', 'xml'],
            // 支持的cell编辑器的类型
            supportCellFile:['csv', 'et', 'ett', 'fods', 'ods', 'ots', 'sxc', 'xls', 'xlsb', 'xlsm', 'xlsx', 'xlt', 'xltm', 'xltx', 'xml'],
            // 支持的slide编辑器的类型
            supportSlideFile:['dps', 'dpt', 'fodp', 'odp', 'otp', 'pot', 'potm', 'potx', 'pps', 'ppsm', 'ppsx', 'ppt', 'pptm', 'pptx', 'sxi'],
            // 支持的pdf编辑器的类型
            supportPdfFile:['djvu', 'docxf', 'oform', 'oxps', 'pdf', 'xps'],
            detail: {},
            // 编辑器配置
            editorConfig: {
                // 编辑器宽度
                width: '100%',
                // 编辑器高度
                height: '100%',
                // 编辑器类型，支持 word（文档）、cell（表格）、slide（PPT）
                documentType: null,
                // 文档配置
                document: {
                    key: null,
                    // 文件类型
                    fileType: null,
                    // 文档url地址
                    url: null,
                    // 文档标题
                    title: null,
                    // 文档权限配置
                    permissions: {
                        edit: true, //文件是否可以编辑，false时文件不可编辑
                    }
                },
                //编辑配置
                editorConfig: {
                    //文档操作模式 view 视图模式不可编辑  edit 编辑模式可编辑文档
                    mode: 'edit',
                         // 回调地址
                    callbackUrl: 'http://mbs.scdayou.com/api/onlyoffice/callback',
                    // 设置语言
                    lang: 'zh-CN',
                }
            }
        }
    },
    created() {
        this.setConfigInfo();
    },
    methods: {
        // 从路由取参设置文件信息
        setConfigInfo() {
            this.editorConfig.document.title = this.$route.query.title;
            this.editorConfig.document.url = process.env.VUE_APP_API_SERVER + this.$route.query.url;
            this.editorConfig.editorConfig.mode = this.$route.query.mode;
            this.editorConfig.editorConfig.callbackUrl = "http://mbs.scdayou.com/api/onlyoffice/callback?originPath=/opt"+this.$route.query.url;
            const lastIndex = this.$route.query.title.lastIndexOf('.');
            this.editorConfig.document.fileType = lastIndex !== -1 ? this.$route.query.title.substring(lastIndex + 1) : null;
            if (this.supportWordFile.includes(this.editorConfig.document.fileType)) {
                this.editorConfig.documentType = "word";
            }
            if (this.supportCellFile.includes(this.editorConfig.document.fileType)) {
                this.editorConfig.documentType = "cell";
            }
            if (this.supportSlideFile.includes(this.editorConfig.document.fileType)) {
                this.editorConfig.documentType = "slide";
            }
            if (this.supportPdfFile.includes(this.editorConfig.document.fileType)) {
                // 巨坑,官方文档写的编辑器类型可以是pdf,但是实测docker最新镜像documentType设置pdf会报错提示不受支持的类型
                // this.editorConfig.documentType = "pdf";
                this.editorConfig.documentType = "word";
            }
            console.log(this.editorConfig.editorConfig.callbackUrl)
        }
    }
}
</script>
<style>
* {
    margin: 0;
    padding: 0;
}

html,
body {
    width: 100%;
    height: 100%;
}

div {
    width: 100%;
    height: 100%;
}
</style>