<!--文件上传组件封装 2019.3.05 姚政伟创建
====================调用说明================
<fileUpload
          :limit="10" //最大上传数量 默认1
          :fileList="form.videos" //初始化文件列表 默认为空
          ref="uploadVideosControl" //组件声明别名
          flag="routineWork" //文件标识
          :multiple="true" //是否支持多选文件 默认为false
          :styleType="2" //样式类型(1:图片类型 2.附件类型) 默认1
          v-if="!loading"></fileUpload>
===================回调方法（获取上传文件列表）==========
             var listStr = ref[声明别名].getFileListStr();//多张图片逗号分隔
-->
<template>
  <div>
    <el-upload
      v-loading="uploadConfig.loading"
      element-loading-text="正在上传..."
      element-loading-background="rgba(0, 0, 0, 0.6)"
      :action="uploadConfig.uploadFileApiUrl"
      :show-file-list="true"
      :on-success="handleImageSuccess"
      :before-upload="beforeImageUpload"
      :list-type="styleType == 1 ? 'picture-card':'text'"
      :limit="limit"
      :on-preview="handleImagePreview"
      :on-remove="handleImageUploadRemove"
      :file-list="uploadConfig.initFileList"
      :class="{'yao-upload':true,'uploadDisabled':uploadConfig.succeedFileList.length>=limit}"
      :multiple="multiple"
      :on-exceed="handleExceed"
      :on-error="handleImageError"
      :headers="headers"
      ref="uploadCtl"
      :disabled="isdisabled"
      :data=" {type:this.certType}"
    >
      <i class="el-icon-plus" v-show="styleType == 1"></i>
      <el-button size="small" type="primary" v-show="styleType == 2">点击上传</el-button>
      <el-button size="small" type="primary" v-show="styleType == 3">证件识别</el-button>
    </el-upload>
    <div class="el-upload__tip">{{tipMessage}}</div>
    <el-dialog :visible.sync="uploadConfig.dialogVisible" :append-to-body="true">
      <el-row v-show="styleType == 1" style="text-align: center;"><img style="max-width: 100%;" :src="uploadConfig.previewImageUrl" alt=""></el-row>
      <el-row v-show="styleType == 2">
        <el-col>
          <el-row>
            <el-col :span="6">
              <el-button @click="openLinkUrl(uploadConfig.previewImageUrl)">下载文件</el-button>
            </el-col>
            <el-col :span="18">
              <el-alert
                title="点击下载打开页面，右键保存即可！"
                type="info"
                show-icon
                :closable="false">
              </el-alert>
            </el-col>
          </el-row>
          <el-row>
            <el-col>&nbsp;</el-col>
          </el-row>
          <el-row>
            <el-col :span="6">
              <el-button @click="showPlay = !showPlay" :disabled="!(showVideo || showAudio)">在线播放</el-button>
            </el-col>
            <el-col :span="18">
              <el-alert
                title="该文件支持在线播放！"
                type="success"
                show-icon
                :closable="false"
                v-if="showVideo || showAudio">
              </el-alert>
              <el-alert
                title="该文件不支持在线播放！"
                type="warning"
                show-icon
                :closable="false"
                v-else>
              </el-alert>
            </el-col>
          </el-row>
          <el-row>
            <el-col>&nbsp;</el-col>
          </el-row>
          <el-row>
            <el-col>
              <transition name="el-fade-in">
                <div v-show="showPlay" class="transition-box">
                  <video width="100%" controls v-if="showVideo">
                    <source :src="uploadConfig.previewImageUrl" type="video/mp4">
                    <source :src="uploadConfig.previewImageUrl" type="video/ogg">
                    <source :src="uploadConfig.previewImageUrl" type="video/webm">
                    您的浏览器不支持 视频播放。
                  </video>
                  <audio :src="uploadConfig.previewImageUrl" controls v-if="showAudio">
                    您的浏览器不支持 音频播放。
                  </audio>
                </div>
              </transition>
            </el-col>
          </el-row>
        </el-col>
      </el-row>
      <el-row v-show="styleType == 3">
        <el-col>
          <el-row>
            <el-col :span="6">
              {{uploadConfig.previewImageUrl}}}
              <el-button @click="openLinkUrl(uploadConfig.previewImageUrl)">下载文件1</el-button>
            </el-col>
          </el-row>
        </el-col>
      </el-row>
    </el-dialog>
  </div>
</template>

<script>
  import { getToken } from '@/utils/auth'
  export default {
    data () {
      return {
        headers:{},
        video: ".mp4|.ogg|.webm|.AVI|.WMV",
        audio: ".mp3|.ogg|.wav",
        //上传文件配置属性
        uploadConfig: {
          loading: false,
          prefixServerFileUrl: "",//this.$config.prefixServerFileUrl,
          uploadFileApiUrl: process.env.VUE_APP_BASE_API + (this.styleType == 3 ? 'identification': 'upload'),//this.$config.uploadFileApiUrl,
          previewImageUrl: "",
          succeedFileList: [],
          dialogVisible: false,
          initFileList: []
        },
        showPlay: false,
        showVideo: false,
        showAudio: false
      }
    },
    //接收参数
    props: {
      //最大上传数量 默认1
      limit: {
        type: Number,
        required: false,
        default: 1
      },
      isdisabled:{
        type: Boolean,
        required: false,
        default: false
      },
      //初始化文件列表 默认为空
      fileList: {
        type: Array,
        required: false,
        default:function (){
          return []
        }
      },
      //文件标识(本项目不支持)
      flag: {
        type: String,
        // required: true
      },
      //是否支持多选文件 默认为false
      multiple: {
        type: Boolean,
        required: false,
        default: false
      },
      //样式类型(1:图片类型 2.附件类型) 默认1
      styleType: {
        type: Number,
        required: false,
        default: 1
      },
      //样式类型(1:图片类型 2.附件类型) 默认1
      certType: {
        type: String,
        required: false,
        default: ''
      },
      //tip备注提示
      tipMessage: {
        type: String,
        required: false
      },
      //允许上传文件类型后缀白名单（例如：".jpg|.png"，|分隔）
      allowFileTypes: {
        type: String,
        required: false,
        default: ".jpg|.jpeg|.png"
      },
      //缩略图大小（宽高 例如：100x100）(本项目不支持)
      thumbnailSize: {
        type: String,
        required: false,
        default: ""
      }
    },
    created(){
      this.headers = { token: getToken() }
    },
    computed: {
      uploadPath() {
        return this.fileList.map(m=>m.path).join(',');
      }
    },
    watch:{
      uploadPath(newValue, oldValue){
        this.$options.methods.initFileList.bind(this)(this.fileList);
      }
    },
    mounted(){
      this.$options.methods.initFileList.bind(this)(this.fileList);
    },
    methods: {
      handleImageSuccess(res, file) {
        var that = this;
        that.uploadConfig.loading = false;
        if (res && res.code == 200){
          if (file.response.data) {
            that.uploadConfig.succeedFileList.push(file.response.data);

            if(this.styleType == 3) {
              this.$emit('successAction', file.response.data)
            }

          }
        } else{
          that.clearFileUpload()
          that.$message.error(res.msg);
       }
      },
      beforeImageUpload(file) {
        var that = this;
        var checkFile = false;
        const fileNameSuffix = file.name.substring(file.name.lastIndexOf("."));
        const allowFileTypes = that.allowFileTypes.split("|");
        console.log("fileNameSuffix"+fileNameSuffix)
        console.log("allowFileTypes"+allowFileTypes.length)
        const isLt2M = file.size / 1024 / 1024 < 20;
        if (allowFileTypes && allowFileTypes.length > 0) {
          for (var i = 0; i < allowFileTypes.length; i++) {
            if (allowFileTypes[i].toUpperCase() == fileNameSuffix.toUpperCase()) {
              checkFile = true;
              break;
            }
          }
        }
        if (!checkFile) {
          that.$message.error('上传文件格式错误!');
          return false;
        }
        if (that.styleType == 1 && !isLt2M) {
          that.$message.error('上传图片大小不能超过 20MB!');
          return false;
        }
        that.uploadConfig.loading = true;
        return true;
      },
      handleImageUploadRemove(file, fileList){
        var that = this;
        that.uploadConfig.succeedFileList.splice(0, that.uploadConfig.succeedFileList.length);
        if (fileList.length > 0) {
          for (var i = 0; i < fileList.length; i++) {
            var fileItem = fileList[i];
            that.uploadConfig.succeedFileList.push(fileItem.response.data);
          }
        }
      },
      handleImagePreview(file){
        if (file.url.lastIndexOf("_") > -1) {
          this.uploadConfig.previewImageUrl = file.url.substring(0, file.url.lastIndexOf("_"));
        } else {
          this.uploadConfig.previewImageUrl = file.url;
        }

        var fileNameSuffix = file.name.substring(file.name.lastIndexOf("."));
        //判断是否支持视频播放
        var allowVideoFileTypes = this.video.split("|");
        if (allowVideoFileTypes && allowVideoFileTypes.length > 0) {
          for (var i = 0; i < allowVideoFileTypes.length; i++) {
            if (allowVideoFileTypes[i].toUpperCase() == fileNameSuffix.toUpperCase()) {
              this.showVideo = true;
              break;
            }
          }
        }
        //判断是否支持音频播放
        var allowAudioFileTypes = this.audio.split("|");
        if (allowAudioFileTypes && allowAudioFileTypes.length > 0) {
          for (var i = 0; i < allowAudioFileTypes.length; i++) {
            if (allowAudioFileTypes[i].toUpperCase() == fileNameSuffix.toUpperCase()) {
              this.showAudio = true;
              break;
            }
          }
        }

        this.uploadConfig.dialogVisible = true;
      },
      initFileList(fileList){
        var that = this;
        that.uploadConfig.initFileList = [];
        that.uploadConfig.succeedFileList = [];
        if (fileList != null && fileList.length>0) {
          var model = {};
          var file ={};
          for (var i = 0; i < fileList.length; i++) {
            file = fileList[i];
            if (!file || !file.path) {
              continue;
            }
            model = {};
            model.name = file.path.substring(file.path.lastIndexOf("/") + 1);
            if (file.path.indexOf("http") != -1) {
              if (that.thumbnailSize){
                model.url = file.path + "_" + that.thumbnailSize;
              } else{
                model.url = file.path;
              }
            } else {
              if (that.thumbnailSize){
                model.url = that.uploadConfig.prefixServerFileUrl + file.path + "_" + that.thumbnailSize;
              } else{
                model.url = that.uploadConfig.prefixServerFileUrl + file.path;
              }
            }
             model.response = {data: file};
            that.uploadConfig.initFileList.push(model);
            that.uploadConfig.succeedFileList.push(file);
          }
        }
      },
      getFileList(){
        var that = this;
        return that.uploadConfig.succeedFileList;
      },
      handleExceed(files, fileList){
        var that = this;
        that.$message.warning(`当前最多上传 ${that.limit} 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件`);
      },
      openLinkUrl(url){
        location.href = url;
      },
      clearFileUpload(){
        var that = this;
        that.$refs.uploadCtl.clearFiles();
        that.uploadConfig.succeedFileList.splice(0, that.uploadConfig.succeedFileList.length);
      },
      handleImageError(err, file, fileList){
        let that = this;
        that.$message.error('上传文件发生错误!');
        that.uploadConfig.loading = false;
      }
    }
  }
</script>
<style>
  /*文件上传样式重写 2018.3.24 姚政伟 新增*/
  .uploadDisabled .el-upload {
    display: none !important;
  }

  .el-upload-list.is-disabled {
    margin-top: -40px;
  }

  .el-upload-list.is-disabled:before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background-color: #fff;
  }

  .el-upload-list.is-disabled .el-upload-list__item-status-label, .el-upload-list.is-disabled .el-upload-list__item.is-success .el-upload-list__item-status-label {
    display: none;
  }
  .yao-upload .el-progress{
    display: none;
  }
</style>
