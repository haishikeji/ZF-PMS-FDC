<template>
  <div class="app-container">
    <div class="title-container">
      <breadcrumb id="breadcrumb-container" class="breadcrumb-container"/>
    </div>
    <y-detail-page-layout @save="handleCreate" :edit-status="false" v-loading="vLoading" element-loading-text="处理中。。。">
      <div style="padding-top: 30px;">
        <el-tabs v-model="activeName">
          <el-tab-pane label="日志信息" name="first">
            <el-form ref="postForm" :model="postForm" class="form-container" style="padding-left: 500px">
              <div>
                <div class="postInfo-container">
                  <el-row>
                    <el-col :xs="24" :sm="12" :lg="10" :span="6">
                      <el-form-item
                        label="项目名称："
                        prop="itemName"
                        label-width="180px"
                        class="postInfo-container-item"
                      >
                        <el-input :value="postForm.itemName" class="filter-item" readonly disabled/>
                      </el-form-item>
                    </el-col>
                  </el-row>
                </div>
              </div>
              <div>
                <div class="postInfo-container">
                  <el-row>
                    <el-col :xs="24" :sm="12" :lg="10" :span="6">
                      <el-form-item
                        label="当前阶段："
                        prop="stageName"
                        label-width="180px"
                        class="postInfo-container-item"
                      >
                        <el-input :value="stageName" class="filter-item" readonly disabled/>
                      </el-form-item>
                    </el-col>
                  </el-row>
                </div>
              </div>
              <div>
                <div class="postInfo-container">
                  <el-row>
                    <el-col :xs="24" :sm="12" :lg="10" :span="6">
                      <el-form-item
                        label="日期："
                        prop="logDate"
                        :rules="{required: true, message: '请选择日志日期', trigger: 'blur'}"
                        label-width="180px"
                        class="postInfo-container-item"
                      >
                        <el-date-picker
                          v-model="postForm.logDate"
                          type="date"
                          value-format="yyyy-MM-dd"
                          style="width: 100%"
                          placeholder="选择日期"
                        />
                      </el-form-item>
                    </el-col>
                  </el-row>
                </div>
              </div>
              <div>
                <div class="postInfo-container">
                  <el-row>
                    <el-col :xs="24" :sm="12" :lg="10" :span="6">
                      <el-form-item
                        label="每日任务情况："
                        prop="taskSituation"
                        :rules="{required: true, message: '请填写每日任务情况', trigger: 'blur'}"
                        label-width="180px"
                        class="postInfo-container-item"
                      >
                        <el-input type="textarea" v-model="postForm.taskSituation" class="filter-item" placeholder="200字符"/>
                      </el-form-item>
                    </el-col>
                  </el-row>
                </div>
              </div>
            </el-form>
          </el-tab-pane>
        </el-tabs>
      </div>
    </y-detail-page-layout>
  </div>
</template>
<script>
  import Breadcrumb from '@/components/Breadcrumb'
  import YDetailPageLayout from '@/components/YDetailPageLayout'

  export default {
    name: 'itemDetail',
    components: {
      Breadcrumb,
      YDetailPageLayout
    },
    data() {
      return {
        type: 'logDetail',
        postForm: {},
        id: this.$route.query.id,
        stageName : this.$route.query.stageName,
        activeName: 'first',
        vLoading: false,
        listQuery:{},
        filterMethod(query, item) {
          return item.label.indexOf(query) > -1;
        },
      }
    },
    created() {
      this.getDetail();
    },
    mounted() {
      this.postForm.logDate = this.timeDefault;
    },
    computed: {
      timeDefault() {
        const date = new Date();
        const s1 = date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + (date.getDate());
        return s1;
      }
    },
    methods: {
      // getDetail() {
      //   if (this.itemId) {
      //     this.$api.itemLog.detail(this.itemId).then(res => {
      //       this.postForm = res.data;
      //     });
      //   }
      // },
      handleCreate() {
        if (this.postForm.taskSituation && this.postForm.taskSituation.length>200){
          this.$notify({
            title: '错误',
            message: '每日任务情况不超过200字符',
            type: 'error',
            duration: 2000
          });
          return;
        }
        this.vLoading = true;
        this.$refs.postForm.validate(valid => {
          if (valid) {
            if (this.id) {
              this.$api.itemLog.edit(Object.assign({}, this.postForm, {})).then(res => {
                if (res.code === 200) {
                  this.$notify({
                    title: '成功',
                    message: '保存成功',
                    type: 'success',
                    duration: 2000
                  });
                  const back = this.$route.query.back;
                  if (back) {
                    this.$router.push(back)
                  }
                  this.initData();
                  this.vLoading = false
                }
              }).catch(() => {
                this.vLoading = false
              })
            }
          }
        })
      },

      getDetail() {
        if (this.id) {
          this.$api.itemLog.detail(this.id).then(res => {
            this.postForm = res.data;
          });
        }
      },
    }
  }
</script>
<style lang="scss" scoped>

</style>
