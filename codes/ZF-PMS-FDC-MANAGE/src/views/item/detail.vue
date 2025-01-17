<template>
  <div class="app-container">
    <div class="title-container">
      <breadcrumb id="breadcrumb-container" class="breadcrumb-container"/>
    </div>
    <y-detail-page-layout @save="handleCreate" :edit-status="true" v-loading="vLoading" element-loading-text="处理中。。。" :list-query="listQuery">
      <div style="padding-top: 30px;">
        <el-tabs v-model="activeName">
          <el-tab-pane label="项目信息" name="first">
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
                        label="项目名称："
                        prop="name"
                        :rules="{required: true, message: '项目名称不能为空', trigger: 'blur'}"
                        label-width="120px"
                        class="postInfo-container-item"
                      >
                        <el-input v-model="postForm.name" class="filter-item"/>
                      </el-form-item>
                    </el-col>
                    <el-col :xs="24" :sm="12" :lg="6" :span="6">
                      <el-form-item
                        label="归属单位："
                        prop="belongTo"
                        :rules="{required: true, message: '归属单位不能为空', trigger: 'blur'}"
                        label-width="120px"
                        class="postInfo-container-item"
                      >
                        <el-select
                          v-model="postForm.belongTo"
                          placeholder="请选择"
                          clearable
                          filterable
                          class="filter-item"
                          style=" width: 100%"
                          @change="changePrefix"
                        >
                          <el-option key="1" label="大友" value="DY"/>
                          <el-option key="2" label="泰济诚" value="TJC"/>
                        </el-select>
                      </el-form-item>
                    </el-col>
                    <el-col :xs="24" :sm="12" :lg="6" :span="6">
                      <el-form-item
                        label="项目编号："
                        prop="oaNo"
                        label-width="120px"
                        class="postInfo-container-item"
                      >
                        <el-input v-model="postForm.oaNo" class="filter-item" :placeholder="tip" :disabled="oaNoDisable" :readonly="oaNoReadonly">
                          <template slot="prepend">{{postForm.belongTo}}</template>
                        </el-input>
                      </el-form-item>
                    </el-col>
                    <el-col :xs="24" :sm="12" :lg="6" :span="6">
                      <el-form-item
                        label="事业部流水号："
                        prop="businessNo"
                        label-width="150px"
                        class="postInfo-container-item"
                      >
                        <el-input :value="postForm.businessNo" class="filter-item" placeholder="系统自动生成" :disabled="true" readonly/>
                      </el-form-item>
                    </el-col>
                  </el-row>
                  <el-row>
                    <el-col :xs="24" :sm="12" :lg="6" :span="6">
                      <el-form-item
                        label="项目类型："
                        prop="cate"
                        :rules="{required: true, message: '项目类型不能为空', trigger: 'blur'}"
                        label-width="120px"
                        class="postInfo-container-item"
                      >
                        <el-select
                          v-model="postForm.cate"
                          placeholder="请选择"
                          clearable
                          filterable
                          class="filter-item"
                          style=" width: 100%"
                        >
                          <el-option v-for="item in cateList" :key="item.id" :label="item.name" :value="item.id"/>
                        </el-select>
                      </el-form-item>
                    </el-col>
                    <el-col :xs="24" :sm="12" :lg="6" :span="6">
                      <el-form-item
                        label="客户名字："
                        prop="customerId"
                        label-width="120px"
                        class="postInfo-container-item"
                      >
                        <el-select v-model="customerInfo" filterable class="filter-item" style="float: left;width: 100%;" placeholder="请选择" @change="getCustomerDetail">
                          <el-option
                            v-for="item in customerOptions"
                            :key="item.id"
                            :label="item.name"
                            :value="[item.id,item.name]"
                          />
                        </el-select>
                        <el-select v-show="false" v-model="postForm.clientName" />
                      </el-form-item>
                    </el-col>
                    <el-col :xs="24" :sm="12" :lg="6" :span="6">
                      <el-form-item
                        label="委托单位："
                        prop="clientUnit"
                        label-width="120px"
                        class="postInfo-container-item"
                      >
                        <el-input :value="postForm.clientUnit" class="filter-item" readonly disabled/>
                      </el-form-item>
                    </el-col>
                    <el-col :xs="24" :sm="12" :lg="6" :span="6">
                      <el-form-item
                        label="联系方式："
                        prop="mobile"
                        label-width="120px"
                        class="postInfo-container-item"
                      >
                        <el-input :value="postForm.mobile" class="filter-item" readonly disabled />
                      </el-form-item>
                    </el-col>
                  </el-row>
                  <el-row>
                    <el-col :xs="24" :sm="12" :lg="6" :span="6">
                      <el-form-item
                        label="客户经理："
                        prop="clientManager"
                        label-width="120px"
                        class="postInfo-container-item"
                      >
                        <el-input :value="postForm.clientManager" class="filter-item" readonly disabled/>
                      </el-form-item>
                    </el-col>
                    <el-col :xs="24" :sm="12" :lg="6" :span="6">
                      <el-form-item
                        label="业务来源："
                        prop="businessSource"
                        :rules="{required: true, message: '业务来源不能为空', trigger: 'blur'}"
                        label-width="120px"
                        class="postInfo-container-item"
                      >
                        <el-select
                          v-model="postForm.businessSource"
                          placeholder="请选择"
                          clearable
                          filterable
                          class="filter-item"
                          style=" width: 100%"
                        >
                          <el-option v-for="item in businessSourceList" :key="item.id" :label="item.name" :value="item.id"/>
                        </el-select>
                      </el-form-item>
                    </el-col>
                    <el-col :xs="24" :sm="12" :lg="6" :span="6">
                      <el-form-item
                        label="项目负责人："
                        prop="skiller"
                        :rules="{required: true, message: '项目负责人不能为空', trigger: 'blur'}"
                        label-width="120px"
                        class="postInfo-container-item"
                      >
                        <el-input v-model="postForm.skiller" class="filter-item"/>
                      </el-form-item>
                    </el-col>
                    <el-col :xs="24" :sm="12" :lg="6" :span="6">
                      <el-form-item
                        label="签订日期："
                        prop="signDate"
                        label-width="120px"
                        class="postInfo-container-item"
                      >
                        <el-date-picker
                          v-model="postForm.signDate"
                          type="date"
                          value-format="yyyy-MM-dd"
                          style="width: 100%"
                          placeholder="选择日期"
                        />
                      </el-form-item>
                    </el-col>
                  </el-row>
                  <el-row>
                    <el-col :xs="24" :sm="12" :lg="6" :span="6">
                      <el-form-item
                        label="付款类型："
                        prop="paymentMethod"
                        :rules="{required: true, message: '付款方式不能为空', trigger: 'blur'}"
                        label-width="120px"
                        class="postInfo-container-item"
                      >
                        <el-select
                          v-model="postForm.paymentMethod"
                          placeholder="请选择"
                          clearable
                          filterable
                          class="filter-item"
                          style=" width: 100%"
                        >
                          <el-option key="1" label="一次性付款" value="一次性付款"/>
                          <el-option key="2" label="分期付款" value="分期付款"/>
                        </el-select>
                      </el-form-item>
                    </el-col>
                    <el-col :xs="24" :sm="12" :lg="6" :span="6">
                      <el-form-item
                        label="合同金额："
                        prop="amount"
                        label-width="120px"
                        class="postInfo-container-item"
                      >
                        <el-input v-model.number="postForm.amount" class="filter-item" type="number">
                          <i slot="suffix" style="font-size:normal;margin-right: 10px;line-height: 30px">元</i>
                        </el-input>
                      </el-form-item>
                    </el-col>
                    <el-col :xs="24" :sm="12" :lg="6" :span="6">
                      <el-form-item
                        label="所属部门："
                        prop="departmentId"
                        :rules="{required: true, message: '所属部门不能为空', trigger: 'blur'}"
                        label-width="120px"
                        class="postInfo-container-item"
                      >
                        <el-select
                          v-model="postForm.departmentId"
                          placeholder="请选择"
                          clearable
                          filterable
                          class="filter-item"
                          style=" width: 100%"
                        >
                          <el-option
                            v-for="item in departmentsOptions"
                            :key="item.id"
                            :label="item.name"
                            :value="item.id"
                          />
                        </el-select>
                      </el-form-item>
                    </el-col>
                    <el-col :xs="24" :sm="12" :lg="6" :span="6">
                      <el-form-item   label-width="120px" v-model="postForm.contractUrl" style=" width: 100%">
                        <el-upload
                          action="/api/upload"
                          :limit="1" :on-success="changeres" :on-exceed="handleExceed" :on-preview="handleAttachmentPreview"
                          :file-list="fileList" :before-remove="beforeRemove">
                          <el-button type="primary">上传电子合同<i class="el-icon-upload el-icon--right"></i></el-button>
                        </el-upload>
                      </el-form-item>
                    </el-col>
                  </el-row>
                  <div style="margin-top:20px;width:100%;height:1px;background:rgba(242,242,242,1);"/>
                  <div style="margin-bottom: 30px">
                    <h3 class="title">
                      <div class="avatar-wrapper icon-title" style="background:rgba(255,175,41,1)">人</div>
                      <div class="icon-info">
                        人员配置
                        <span style="color: orangered">*</span>
                      </div>
                    </h3>
                  </div>
                </div>
                <el-transfer
                  filterable
                  :filter-method="filterMethod"
                  filter-placeholder="请输入姓名"
                  v-model="userIds"
                  :titles="['所有员工', '所选员工']"
                  :data="users"
                >
                </el-transfer>
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
      YDetailPageLayout,
    },
    data() {
      return {
        type: 'detail',
        postForm: {
          departmentId:'',
          clientUnit:'',
          mobile:'',
          userId: this.$store.getters.userInfo.id,
          clientManager:this.$store.getters.userInfo.name,
          oaNo:''
        },
        dataId: this.$route.query.id,
        activeName: 'first',
        vLoading: false,
        users:[],
        listQuery:null,
        userIds: [],
        filterMethod(query, item) {
          return item.label.indexOf(query) > -1;
        },
        departmentsOptions:[],
        contractName:'',
        fileList:[],
        customerOptions:[],
        customerInfo:[],
        tip:null,
        oaNoDisable:false,
        oaNoReadonly:false,
        cateList:[],
        businessSourceList:[]
      }
    },
    created() {
      this.listQuery = this.$route.query.listQuery;
      this.getAllUser();
      this.getDepartment();
      this.getDetail();
      this.getCustomerSelect();
      this.getCateList();
      this.getBusinessSourceList();
    },
    methods: {

      getCateList(){
        this.$api.dictData.simpleType("项目类型").then(res => {
          this.cateList = res.data
        })
      },
      getBusinessSourceList(){
        this.$api.dictData.simpleType("业务来源").then(res => {
          this.businessSourceList = res.data
        })
      },
      getCustomerDetail(){
        this.postForm.clientName = this.customerInfo[1];
        this.postForm.customerId = this.customerInfo[0];
        this.$api.customer.detail(this.postForm.customerId).then(res =>{
          this.postForm.clientUnit = res.data.department;
          this.postForm.mobile = res.data.mobile;
        })
      },
      getCustomerSelect() {
        this.$api.customer.simpleAll().then(res => {
          this.customerOptions = res.data
        })
      }, handleAttachmentPreview(file){
        window.open(file.url)
      },
      handleExceed(files, fileList) {
        this.$message.warning(`当前限制选择 1个文件，本次选择了 ${files.length} 个文件`);
      },
      // 上传
      changeres(res, file) {
        if (res.code === 200){
          this.postForm.contractUrl = res.data.url;
          const arr = res.data.url.split("-");
          this.contractName = arr[1];
          const date = new Date();
          this.postForm.uploadDate = date.getFullYear() + "-" + (this.getZero(date.getMonth() + 1)) + "-" + this.getZero((date.getDate()));
        }else {
          this.$notify({
            title: '错误',
            message: '合同上传失败',
            type: 'error',
            duration: 2000
          });
          return;
        }
      },
      changePrefix(e){
        this.postForm.belongTo = e;
        this.postForm.oaNo = '';
        if (e === 'TJC'){
          this.tip = '事业部流水号';
          this.oaNoDisable = true;
          this.oaNoReadonly = true;
        }
        if (e === 'DY'){
          this.tip = null;
          this.oaNoDisable = false;
          this.oaNoReadonly = false;
        }
        this.postForm.oaNo = null;
      },
      getZero(num) {
        // 单数前面加0
        if (parseInt(num) < 10) {
          num = '0' + num;
        }
        return num;
      },
      getAllUser() {
        const that = this;
        that.$api.user.simpleAll().then(data => {
          if (data.code === 200) {
            this.users = data.data.map((item) => {
              return {
                key: item.id,
                label: item.name,
              };
            });
          } else {
            this.$message({
              type: 'error',
              message: data.msg
            })
          }
        })
      },
      getDepartment(){
        this.$api.department.simpleAll().then(res => {
          this.departmentsOptions = res.data
        })
      },
      getDetail() {
        if (this.dataId) {
          this.$api.item.detail(this.dataId).then(res => {
            this.postForm = res.data;
            this.customerInfo = res.data.clientName;
            if (res.data.contractUrl){
              const arr = res.data.contractUrl.split("-");
              this.contractName = arr[1];
              this.fileList.push({name: this.contractName, url:this.postForm.contractUrl});
            }
            this.userIds = res.data.userIds.map(item => {
              return item
            })
          });
        }
      },
      handleCreate() {
        if (!this.postForm.customerId){
            this.$notify({
              title: '错误',
              message: '客户名字不能为空',
              type: 'error',
              duration: 2000
            });
            return;
        }
        if (this.userIds.length==0){
          this.$notify({
            title: '错误',
            message: '人员配置不能为空',
            type: 'error',
            duration: 2000
          });
          return;
        }
        if (!this.postForm.contractUrl){
          this.postForm.uploadDate = null;
        }
        this.$refs.postForm.validate(valid => {
          if (valid) {
            if (this.dataId) {
              this.$api.item.edit(Object.assign({}, this.postForm, {
                userIds: this.userIds.map(item => { return item }),
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
              this.$api.item.add(Object.assign({}, this.postForm, {
                userIds: this.userIds.map(item => { return item }),
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
      beforeRemove(file, fileList) {
      this.$confirm(`确定移除 ${ file.name }？`, '警告', {
          confirmButtonText: '确认',
          cancelButtonText: '取消',
          type: 'warning',
        })
          .then(async () => {
            this.postForm.contractUrl = '';
          })
      }
    }
  }
</script>
<style lang="scss" scoped>

</style>
