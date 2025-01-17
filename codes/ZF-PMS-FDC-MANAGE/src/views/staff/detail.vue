<template>
  <div class="app-container">
    <div class="title-container">
      <breadcrumb id="breadcrumb-container" class="breadcrumb-container"/>
    </div>
    <y-detail-page-layout @save="handleCreate" :edit-status="true" v-loading="vLoading" element-loading-text="处理中。。。" :list-query="listQuery">
      <div style="padding-top: 30px;">
        <el-tabs v-model="activeName">
          <el-tab-pane label="员工信息" name="first">
            <el-form ref="postForm" :model="postForm" class="form-container">
              <div class="createPost-main-container">
                <div class="postInfo-container">
                  <div style="margin-bottom: 30px">
                    <h3 class="title">
                      <div class="avatar-wrapper icon-title">基</div>
                      <div class="icon-info">基本信息</div>
                    </h3>
                  </div>
                  <el-row>
                    <el-col :xs="24" :sm="12" :lg="6" :span="6">
                      <el-form-item
                        label="员工姓名："
                        prop="name"
                        :rules="{required: true, message: '请输入员工姓名', trigger: 'blur'}"
                        label-width="120px"
                        class="postInfo-container-item"
                      >
                        <el-input v-model="postForm.name" class="filter-item"/>
                      </el-form-item>
                    </el-col>
                    <el-col :xs="24" :sm="12" :lg="6" :span="6">
                      <el-form-item
                        label="性别："
                        prop="sex"
                        :rules="{required: true, message: '请选择性别', trigger: 'blur'}"
                        label-width="120px"
                        class="postInfo-container-item"
                      >
                        <el-select
                          v-model="postForm.sex"
                          placeholder=""
                          clearable
                          filterable
                          class="filter-item"
                          style="width: 100%"
                        >
                          <el-option key="1" label="男" value="男"/>
                          <el-option key="2" label="女" value="女"/>
                        </el-select>
                      </el-form-item>
                    </el-col>
                    <el-col :xs="24" :sm="12" :lg="6" :span="6">
                      <el-form-item
                        label="身份证号："
                        prop="idNo"
                        :rules="[{required: true, message: '请选择身份证号', trigger: 'blur'},
            {
              pattern:  /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/,
              message: '证件号码格式有误！',
              trigger: 'blur'}]"
                        label-width="120px"
                        class="postInfo-container-item"
                      >
                        <el-input v-model="postForm.idNo" class="filter-item"/>
                      </el-form-item>
                    </el-col>
                    <el-col :xs="24" :sm="12" :lg="6" :span="6">
                      <el-form-item label="出生日期：" label-width="120px" class="postInfo-container-item">
                        <el-date-picker
                          v-model="postForm.birthday"
                          type="date"
                          value-format="yyyy-MM-dd"
                          style="width: 100%"
                          placeholder="选择日期"
                        />
                      </el-form-item>
                    </el-col>
                    <el-col :xs="24" :sm="12" :lg="6" :span="6">
                      <el-form-item label="民族：" label-width="120px" class="postInfo-container-item">
                        <el-select
                          v-model="postForm.nation"
                          placeholder=""
                          clearable
                          filterable
                          class="filter-item"
                          style="width: 100%"
                        >
                          <el-option v-for="(item, index) in AllEnum['民族']" :key="index" :label="item" :value="item"/>
                        </el-select>
                      </el-form-item>
                    </el-col>
                    <el-col :xs="24" :sm="12" :lg="6" :span="6">
                      <el-form-item label="联系电话："
                                    prop="mobile"
                                    :rules="[{required: true, message: '请输入联系电话', trigger: 'blur'},
            {
              pattern:  /^1\d{10}$/,
              message: '联系电话格式有误！',
              trigger: 'blur'}]"
                                    label-width="120px" class="postInfo-container-item">
                        <el-input v-model="postForm.mobile" class="filter-item"/>
                      </el-form-item>
                    </el-col>
                    <el-col :xs="24" :sm="12" :lg="6" :span="6">
                      <el-form-item label="政治面貌：" label-width="120px" class="postInfo-container-item">
                        <el-select
                          v-model="postForm.politics"
                          placeholder=""
                          clearable
                          filterable
                          class="filter-item"
                          style="width: 100%"
                        >
                          <el-option v-for="(item, index) in AllEnum['政治面貌']" :key="index" :label="item" :value="item"/>
                        </el-select>
                      </el-form-item>
                    </el-col>
                    <el-col :xs="24" :sm="12" :lg="6" :span="6">
                      <el-form-item
                        label="籍贯："
                        prop="nativePlace"
                        label-width="120px"
                        class="postInfo-container-item"
                      >
                        <el-input v-model="postForm.nativePlace" class="filter-item"/>
                      </el-form-item>
                    </el-col>
                    <el-col :xs="24" :sm="12" :lg="6" :span="6">
                      <el-form-item
                        label="家庭住址："
                        prop="homeAddress"
                        label-width="120px"
                        class="postInfo-container-item"
                      >
                        <el-input v-model="postForm.homeAddress" class="filter-item" style="width: 480px"/>
                      </el-form-item>
                    </el-col>
                    <el-button type="danger" style="margin-left: 300px" @click="initPassword">重置密码</el-button>
                    <el-button type="danger" @click="initSettlePassword">重置提成查询密码</el-button>
                  </el-row>
                  <div style="margin-top:20px;width:100%;height:1px;background:rgba(242,242,242,1);"/>
                  <div style="margin-bottom: 30px">
                    <h3 class="title">
                      <div class="avatar-wrapper icon-title" style="background:rgba(255,175,41,1)">工</div>
                      <div class="icon-info">工作信息</div>
                    </h3>
                  </div>
                  <el-row>
                    <el-col :xs="24" :sm="12" :lg="6" :span="6">
                      <el-form-item
                        label="员工类型："
                        prop="userType"
                        :rules="{required: true, message: '请选择员工类型', trigger: 'blur'}"
                        label-width="120px"
                        class="postInfo-container-item"
                      >
                        <el-select
                          v-model="postForm.userType"
                          clearable
                          filterable
                          placeholder=""
                          style="width: 100%"
                        >
                          <el-option v-for="(item, index) in AllEnum['员工类型']" :key="index" :label="item" :value="item"/>
                        </el-select>
                      </el-form-item>
                    </el-col>
                    <el-col :xs="24" :sm="12" :lg="6" :span="6">
                      <el-form-item
                        label="员工工号："
                        prop="staffNo"
                        label-width="120px"
                        class="postInfo-container-item"
                      >
                        <el-input :value="postForm.staffNo" class="filter-item" placeholder="系统自动生成" :disabled="true" readonly/>
                      </el-form-item>
                    </el-col>
                    <el-col :xs="24" :sm="24" :lg="12" :span="12">
                      <el-form-item
                        label="岗-部门名："
                        prop="userPosts"
                        :rules="{required: true, message: '请选择岗-部门', trigger: 'blur'}"
                        label-width="120px"
                        class="postInfo-container-item"
                      >
                        <el-select
                          v-model="userPosts"
                          placeholder=""
                          clearable
                          multiple
                          filterable
                          class="filter-item"
                          style=" width: 100%"

                        >
                          <el-option
                            v-for="item in postOptions"
                            :key="item.id"
                            :label="item.name"
                            :value="item.id"
                          />
                        </el-select>
                      </el-form-item>
                    </el-col>
                  </el-row>
                  <el-row>
                    <el-col :xs="24" :sm="12" :lg="6" :span="6">
                      <el-form-item
                        label="在职状态："
                        prop="resignState"
                        :rules="{required: true, message: '请选择在职状态', trigger: 'blur'}"
                        label-width="120px"
                        class="postInfo-container-item"
                      >
                        <el-select
                          v-model="postForm.resignState"
                          placeholder=""
                          clearable
                          filterable
                          class="filter-item"
                          style="width: 100%"
                        >
                          <el-option key="0" label="离职" :value="true" />
                          <el-option key="1" label="在职" :value="false"/>
                        </el-select>
                      </el-form-item>
                    </el-col>
                    <el-col :xs="24" :sm="12" :lg="6" :span="6">
                      <el-form-item label="入职日期：" :rules="{required: true, message: '请选择入职日期', trigger: 'blur'}" label-width="120px" class="postInfo-container-item">
                        <el-date-picker
                          v-model="postForm.hireDate"
                          type="date"
                          value-format="yyyy-MM-dd"
                          style="width: 100%"
                          placeholder="选择日期"
                          :disabled="ifAble"
                        />
                      </el-form-item>
                    </el-col>
                    <el-col :xs="24" :sm="12" :lg="6" :span="6">
                      <el-form-item label="离职日期：" label-width="120px" class="postInfo-container-item">
                        <el-date-picker
                          v-model="postForm.resignDate"
                          type="date"
                          value-format="yyyy-MM-dd"
                          style="width: 100%"
                          placeholder="选择日期"
                        />
                      </el-form-item>
                    </el-col>
                  </el-row>
                  <div style="margin-top:20px;width:100%;height:1px;background:rgba(242,242,242,1);"/>
                  <div style="margin-bottom: 30px">
                    <h3 class="title">
                      <div class="avatar-wrapper icon-title" style="background:#9E9CF4">信</div>
                      <div class="icon-info">信息备注</div>
                    </h3>
                  </div>
                  <el-row>
                    <el-col :span="24">
                      <el-form-item label="备注：" label-width="120px" class="postInfo-container-item">
                        <el-input v-model="postForm.remark" type="textarea" class="filter-item" placeholder="200字符"/>
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
    name: 'StaffDetail',
    components: {
      Breadcrumb,
      YDetailPageLayout
    },
    data() {
      return {
        type: 'detail',
        postForm: {
          userPosts: [],
          resignState:false,
          userType: '正式员工',
        },
        dataId: this.$route.query.id,
        activeName: 'first',
        vLoading: false,
        AllEnum:[],
        listQuery:null,
        postOptions:[],
        userPosts:[],
        disable: false,
      }
    },
    created() {
      this.listQuery = this.$route.query.listQuery;
      this.getAllEnum();
      this.getOptions();
      this.getDetail();
    },
    computed:{
      ifAble(){
        if (this.dataId){
          return true;
        }else {
          return false;
        }
      }
    },
    methods: {
      initSettlePassword(){
        this.$confirm('确认重置提成查询密码, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$api.user.resetSettlePwd(this.postForm.id).then(res => {
            if (res.code === 200) {
              this.$notify({
                title: '成功',
                message: '重置提成查询密码成功。',
                type: 'success',
                duration: 2000
              })
            }
          })
        }).catch(() => {
        })
      },
      initPassword() {
        this.$confirm('确认初始密码, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$api.user.resetPwd(this.postForm.id).then(res => {
            if (res.code === 200) {
              this.$notify({
                title: '成功',
                message: '密码初始成功',
                type: 'success',
                duration: 2000
              })
            }
          })
        }).catch(() => {
        })
      },
      getOptions(){
        this.$api.post.xSimpleAll().then(res => {
          this.postOptions = res.data
        })
      },
      getAllEnum() {
        const that = this;
        that.$api.globalConfig.getAllEnum().then(data => {
          if (data.code === 200) {
            that.AllEnum = data.data
          } else {
            this.$message({
              type: 'error',
              message: data.msg
            })
          }
        })
      },
      getDetail() {
        if (this.dataId) {
          this.$api.user.detail(this.dataId).then(res => {
            this.postForm = res.data;
            this.userPosts = res.data.userPosts.map(item => {
              return item.id
            })
          });
        }
      },
      handleCreate() {
        if (!this.postForm.userType) {
          this.$notify({
            title: '错误',
            message: '请选择员工类型',
            type: 'error',
            duration: 2000
          });
          return
        }
        if (!this.postForm.name) {
          this.$notify({
            title: '错误',
            message: '请输入姓名',
            type: 'error',
            duration: 2000
          });
          return
        }
        if (!this.postForm.sex) {
          this.$notify({
            title: '错误',
            message: '请选择性别',
            type: 'error',
            duration: 2000
          });
          return
        }
        if (!this.postForm.idNo) {
          this.$notify({
            title: '错误',
            message: '请输入身份证号',
            type: 'error',
            duration: 2000
          });
          return
        }
        if (this.postForm.remark && this.postForm.remark.length>200) {
          this.$notify({
            title: '错误',
            message: '备注信息长度应不超过200字符',
            type: 'error',
            duration: 2000
          });
          return
        }
        this.postForm.userPosts = this.userPosts;
        if (!this.postForm.userPosts || !this.postForm.userPosts.length) {
          this.$notify({
            title: '错误',
            message: '请选择岗-部门',
            type: 'error',
            duration: 2000
          });
          return
        }

        if (!this.postForm.hireDate) {
          this.$notify({
            title: '错误',
            message: '请选择入职日期',
            type: 'error',
            duration: 2000
          });
          return
        }
        this.vLoading = true;

        this.$refs.postForm.validate(valid => {
          if (valid) {
            if (this.dataId) {
              this.$api.user.edit(Object.assign({}, this.postForm, {
                userPosts: this.userPosts.map(item => { return { id: item } }),
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
              this.$api.user.add(Object.assign({}, this.postForm, {
                userPosts: this.userPosts.map(item => { return { id: item } }),
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
