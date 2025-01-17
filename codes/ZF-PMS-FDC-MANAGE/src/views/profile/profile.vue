<template>
  <div class="app-container">
    <y-detail-page-layout  :edit-status="true" v-loading="vLoading" element-loading-text="处理中。。。">
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
                    <el-button type="primary" style="margin-left: 300px" @click="openDialog">修改密码</el-button>
                    <el-button type="primary" @click="openDialogX">修改提成查询密码</el-button>
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
                      <el-form-item label="入职日期：" label-width="120px" class="postInfo-container-item">
                        <el-date-picker
                          v-model="postForm.hireDate"
                          type="date"
                          value-format="yyyy-MM-dd"
                          style="width: 100%"
                          placeholder="选择日期"
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
    <el-dialog class="dialog" title="修改密码" :visible.sync="dialogFormVisible" :close-on-click-modal="false" width="600px" custom-class="customClass">
      <el-form :model="dialogForm" :rules="rules" ref="dialogForm" label-position="right"
               label-width="110px"
               style="width: 400px; margin-left:50px;">
        <el-form-item label="原密码" prop="originalPassword">
          <el-input v-model="dialogForm.originalPassword" type="password" autocomplete="off"/>
        </el-form-item>
        <el-form-item label="新密码" prop="currentPassword">
          <el-input v-model="dialogForm.currentPassword" type="password" autocomplete="off"/>
        </el-form-item>
        <el-form-item label="确认密码" prop="repeatPassword">
          <el-input v-model="dialogForm.repeatPassword" type="password" autocomplete="off"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="savePass('dialogForm')">修 改</el-button>
      </div>
    </el-dialog>
    <el-dialog class="dialog" title="修改提成查询密码" :visible.sync="dialogFormVisibleX" :close-on-click-modal="false" width="600px" custom-class="customClass">
      <el-form :model="dialogFormX" :rules="rules" ref="dialogFormX" label-position="right"
               label-width="110px"
               style="width: 400px; margin-left:50px;">
        <el-form-item label="原查询密码" prop="originalPassword">
          <el-input v-model="dialogFormX.originalPassword" type="password" autocomplete="off"/>
        </el-form-item>
        <el-form-item label="新查询密码" prop="currentPassword">
          <el-input v-model="dialogFormX.currentPassword" type="password" autocomplete="off"/>
        </el-form-item>
        <el-form-item label="确认密码" prop="repeatPassword">
          <el-input v-model="dialogFormX.repeatPassword" type="password" autocomplete="off"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisibleX = false">取 消</el-button>
        <el-button type="primary" @click="saveSettlePass('dialogFormX')">修 改</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
  import YDetailPageLayout from '@/components/YDetailPageLayout'

  export default {
    name: 'StaffDetail',
    components: {
      YDetailPageLayout
    },
    data() {
      return {
        dialogFormVisible:false,
        dialogFormVisibleX:false,
        type: 'detail',
        postForm: {
          userPosts: [],
          resignState:false,
          userType: '正式员工',
        },
        userId: this.$store.getters.userInfo.id,
        activeName: 'first',
        vLoading: false,
        AllEnum:[],
        listQuery:{},
        postOptions:[],
        userPosts:[],
        dialogForm: {
          originalPassword: '',
          currentPassword: '',
          repeatPassword: '',
        },
        dialogFormX:{
          originalPassword: '',
          currentPassword: '',
          repeatPassword: '',
        },
        rules: {
          originalPassword: [{
            required: true,
            message: '请输入原密码',
            trigger: 'blur'
          }],
          currentPassword: [{
            required: true,
            message: '请输入新密码',
            trigger: 'change'
          }],
          repeatPassword: [{
            required: true,
            message: '请确认新密码',
            trigger: 'change'
          }]
        },
      }
    },
    created() {
      this.getAllEnum();
      this.getOptions();
      this.getDetail();
    },
    methods: {
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
        if (this.userId) {
          this.$api.user.detail(this.userId).then(res => {
            this.postForm = res.data;
            this.userPosts = res.data.userPosts.map(item => {
              return item.id
            })
          });
        }
      },
      openDialog(){
        this.dialogFormVisible = true;
      },
      openDialogX(){
        this.dialogFormVisibleX = true;
      },
      savePass(formName) {
        this.$refs[formName].validate((valid) => {
          if (valid) {
            if (this.dialogForm.currentPassword.length!=8){
              this.$notify({
                title: '错误',
                message: '新密码必须是8位字符',
                type: 'error',
                duration: 3000
              });
              return
            }
            if (this.dialogForm.currentPassword != this.dialogForm.repeatPassword){
              this.$notify({
                title: '错误',
                message: '两次输入的密码不一致，请检查。',
                type: 'error',
                duration: 3000
              });
              return
            }
            this.$api.user.modifyPass(this.dialogForm).then(res => {
              if (res.code === 200) {
                this.$notify({
                  title: '成功',
                  message: '修改成功',
                  type: 'success',
                  duration: 2000
                })
                this.dialogFormVisible = false
              }
            })
          } else {
            console.log('error submit!!')
            return false
          }
        })
      },
      saveSettlePass(formName) {
        this.$refs[formName].validate((valid) => {
          if (valid) {
            if (this.dialogFormX.currentPassword.length!=8){
              this.$notify({
                title: '错误',
                message: '新密码必须是8位字符',
                type: 'error',
                duration: 3000
              });
              return
            }
            if (this.dialogFormX.currentPassword != this.dialogFormX.repeatPassword){
              this.$notify({
                title: '错误',
                message: '两次输入的密码不一致，请检查。',
                type: 'error',
                duration: 3000
              });
              return
            }
            this.$api.user.modifySettlePass(this.dialogFormX).then(res => {
              if (res.code === 200) {
                this.$notify({
                  title: '成功',
                  message: '修改提成查询密码成功',
                  type: 'success',
                  duration: 2000
                })
                this.dialogFormVisibleX = false
              }
            })
          } else {
            console.log('error submit!!')
            return false
          }
        })
      },
    }
  }
</script>
<style lang="scss" scoped>
  /deep/.customClass{
    border-radius: 20px;
  }
</style>
