<template>
  <div class="app-container">
    <div class="title-container">
      <breadcrumb id="breadcrumb-container" class="breadcrumb-container"/>
    </div>
    <y-detail-page-layout @save="handleCreate" :edit-status="true" v-loading="vLoading" element-loading-text="处理中。。。" :list-query="listQuery">
      <div style="padding-top: 30px;">
        <el-tabs v-model="activeName">
          <el-tab-pane label="商机详情" name="first">
            <el-form ref="postForm" :model="postForm" class="form-container" style="padding-left: 500px">
              <div>
                <div class="postInfo-container">
                  <el-row>
                    <el-col :xs="24" :sm="12" :lg="10" :span="6">
                      <el-form-item
                        label="商机名称："
                        prop="opportunityName"
                        label-width="180px"
                        class="postInfo-container-item"
                        :rules="{required: true, message: '请填写商机名称', trigger: 'blur'}"
                      >
                        <el-input v-model="postForm.opportunityName" class="filter-item"/>
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
                        label="客户名字："
                        prop="customerId"
                        label-width="180px"
                        class="postInfo-container-item"
                        :rules="{required: true, message: '请填选择客户名字', trigger: 'blur'}"
                      >
                        <el-select
                          v-model="postForm.customerId"
                          placeholder=""
                          clearable
                          filterable
                          class="filter-item"
                          style=" width: 100%"

                        >
                          <el-option
                            v-for="item in customers"
                            :key="item.id"
                            :label="item.name"
                            :value="item.id"
                          />
                        </el-select>
                      </el-form-item>
                    </el-col>
                  </el-row>
                </div>
              </div>
              <div>
                <div>
                  <div class="postInfo-container">
                    <el-row>
                      <el-col :xs="24" :sm="12" :lg="10" :span="6">
                        <el-form-item
                          label="预计金额："
                          prop="estimateAmount"
                          label-width="180px"
                          class="postInfo-container-item"
                        >
                          <el-input v-model="postForm.estimateAmount" class="filter-item"/>
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
                          label="预计开展日期："
                          prop="estimateDate"
                          :rules="{required: true, message: '请选择日期', trigger: 'blur'}"
                          label-width="180px"
                          class="postInfo-container-item"
                        >
                          <el-date-picker
                            v-model="postForm.estimateDate"
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
                        <el-form-item label="状态：" label-width="180px" class="postInfo-container-item">
                          <el-select
                            v-model="postForm.state"
                            placeholder=""
                            filterable
                            class="filter-item"
                            style="width: 100%"
                          >
                            <el-option key="0" label="跟进中" value="跟进中"/>
                            <el-option key="1" label="签约" value="签约"/>
                            <el-option key="2" label="终止" value="终止"/>
                          </el-select>
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
                          label="终止原因："
                          prop="offReason"
                          label-width="180px"
                          class="postInfo-container-item"
                        >
                          <el-input v-model="postForm.offReason" class="filter-item"/>
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
                          label="备注："
                          prop="remark"
                          label-width="180px"
                          class="postInfo-container-item"
                        >
                          <el-input type="textarea" v-model="postForm.remark" class="filter-item" placeholder="200字符"/>
                        </el-form-item>
                      </el-col>
                    </el-row>
                  </div>
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
    name: 'businessDetail',
    components: {
      Breadcrumb,
      YDetailPageLayout
    },
    data() {
      return {
        type: 'detail',
        postForm: {
          state:'跟进中'
        },
        businessId : this.$route.query.id,
        activeName: 'first',
        vLoading: false,
        listQuery:null,
        customers:[]
      }
    },
    created() {
      this.listQuery = this.$route.query.listQuery;
      this.getCustomer();
      this.getDetail();
    },
    methods: {
      getDetail() {
        if (this.businessId) {
          this.$api.business.detail(this.businessId).then(res => {
            this.postForm = res.data;
          });
        }
      },
      getCustomer(){
        const that = this;
        that.$api.customer.simpleAll().then(data => {
          if (data.code === 200) {
            that.customers = data.data
          } else {
            this.$message({
              type: 'error',
              message: data.msg
            })
          }
        })
      },
      handleCreate() {
        this.$refs.postForm.validate(valid => {
          if (valid) {
            if (this.businessId) {
              this.$api.business.edit(Object.assign({}, this.postForm, {
              })).then(res => {
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
            } else {
              this.$api.business.add(Object.assign({}, this.postForm, {
              })).then(res => {
                if (res.code === 200) {
                  this.$notify({
                    title: '成功',
                    message: '新增成功',
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
    }
  }
</script>
<style lang="scss" scoped>

</style>
