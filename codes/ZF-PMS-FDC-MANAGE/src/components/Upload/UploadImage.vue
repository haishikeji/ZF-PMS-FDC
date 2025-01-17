<!--单图上传  通过v-model绑定-->

<template>
  <div class="upload-container">
    <el-upload
      class="avatar-uploader"
      :class="{'uploadDisabled': value}"
      list-type="picture-card"
      :show-file-list="true"
      :limit="1"
      :file-list="fileList"
      :on-preview="handlePictureCardPreview"
      :before-upload="beforeAvatarUpload"
      :on-remove="rmImage"
      action="/"
    >
      <i class="el-icon-plus avatar-uploader-icon"/>
    </el-upload>
    <el-dialog :visible.sync="dialogVisible">
      <img width="100%" :src="imageUrl" alt="">
    </el-dialog>
  </div>
</template>

<script>

  export default {
    name: 'SingleImageUpload',
    props: {
      value: {
        type: String,
        default: ''
      },
      imageSize: { // 单位Mb
        type: Number,
        default: 10
      }
    },
    data() {
      return {
        tempUrl: '',
        dataObj: { token: '' },
        dialogVisible: false,
      }
    },
    computed: {
      imageUrl() {
        return this.value
      },
      fileList() {
        if (this.value) {
          return [{ url: this.value }]
        } else {
          return []
        }
      }
    },
    methods: {
      rmImage() {
        this.emitInput('')
      },
      emitInput(val) {
        this.$emit('input', val)
      },
      beforeAvatarUpload(file, key) {
        // const isJPG = file.type === 'image/jpeg'
        // const isPNG = file.type === 'image/png'
        const isImg = file.type.indexOf('image') > -1
        const isLt10M = file.size / 1024 / 1024 < this.imageSize

        if (!isImg) {
          this.$message.error('上传图片只能是 图片 格式!')
          return false
        }
        if (!isLt10M) {
          this.$message.error(`上传图片大小不能超过 ${this.imageSize}MB!`)
          return false
        }

        const param = new FormData()
        param.append('file', file, file.name)
        this.$api.common.upload(param).then((res) => {
          this.emitInput(res.data.url)
        })
        return false
      },
      handlePictureCardPreview(file) {
        this.dialogVisible = true
      },
    }
  }
</script>

<style lang="scss" scoped>

  .upload-container {
    width: 146px;
    position: relative;
  }

  /* 超过限制隐藏上传 */
  .uploadDisabled .el-upload {
    display: none !important;
  }
</style>
