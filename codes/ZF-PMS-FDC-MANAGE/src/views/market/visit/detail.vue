<template>
  <div class="app-container">
    <div class="title-container">
      <breadcrumb id="breadcrumb-container" class="breadcrumb-container"/>
    </div>
    <div style="padding-top: 30px;">
      <el-tabs v-model="activeName" type="border-card">
        <el-tab-pane label="拜访详情" name="first">
          <y-detail-page-layout @save="handleCreate" :edit-status="true" v-loading="vLoading" element-loading-text="处理中。。。" :list-query="listQuery">
            <el-form ref="postForm" :model="postForm" class="form-container" style="padding-left: 200px;">
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
                          style="width:360px"
                          @change="getCustomerDetail"

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
                  <el-row>
                    <el-col :xs="24" :sm="12" :lg="10" :span="6">
                      <el-form-item
                        label="客户等级："
                        prop="level"
                        label-width="180px"
                        class="postInfo-container-item"
                      >
                        <el-input style="width:360px" :value="postForm.customerLevel" readonly disabled/>
                      </el-form-item>
                    </el-col>
                  </el-row>
                  <el-row>
                    <el-col :xs="24" :sm="12" :lg="10" :span="6">
                      <el-form-item
                        label="客户部门："
                        prop="department"
                        label-width="180px"
                        class="postInfo-container-item"
                      >
                        <el-input style="width:360px" :value="postForm.customerDepartment" readonly disabled/>
                      </el-form-item>
                    </el-col>
                  </el-row>
                  <el-row>
                    <el-col :xs="24" :sm="12" :lg="10" :span="6">
                      <el-form-item
                        label="客户科室："
                        prop="section"
                        label-width="180px"
                        class="postInfo-container-item"
                      >
                        <el-input style="width:360px" :value="postForm.customerSection" readonly disabled/>
                      </el-form-item>
                    </el-col>
                  </el-row>
                  <el-row>
                    <el-col :xs="24" :sm="12" :lg="10" :span="6">
                      <el-form-item
                        label="客户职位："
                        prop="position"
                        label-width="180px"
                        class="postInfo-container-item"
                      >
                        <el-input style="width:360px" :value="postForm.customerPosition" readonly disabled/>
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
                          label="拜访类型："
                          prop="visitType"
                          label-width="180px"
                          class="postInfo-container-item"
                          :rules="{required: true, message: '选择拜访类型', trigger: 'blur'}"
                        >
                          <el-select clearable style="width:360px" v-model="postForm.visitType" placeholder="拜访类型">
                            <el-option key="0" label="初访" value="初访"/>
                            <el-option key="1" label="回访" value="回访"/>
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
                          label="拜访日期："
                          prop="visitTime"
                          :rules="{required: true, message: '请选择日期', trigger: 'blur'}"
                          label-width="180px"
                          class="postInfo-container-item"
                        >
                          <el-date-picker
                            v-model="postForm.visitTime"
                            type="date"
                            value-format="yyyy-MM-dd"
                            style="width:360px"
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
                          label="拜访地点："
                          prop="spot"
                          label-width="180px"
                          class="postInfo-container-item"
                        >
                          <el-input v-model="postForm.spot"  style="width:360px" class="filter-item"/>
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
                          label="沟通内容："
                          prop="content"
                          label-width="180px"
                          class="postInfo-container-item"
                        >
                          <el-input type="textarea" style="width:360px" v-model="postForm.content" class="filter-item" placeholder="200字符" :autosize="{ minRows: 8, maxRows: 8}"/>
                        </el-form-item>
                      </el-col>
                    </el-row>
                  </div>
                </div>
              </div>
            </el-form>
          </y-detail-page-layout>
        </el-tab-pane>
        <el-tab-pane :lazy="paneLazy" name="replyList">
          <span slot="label">
            回复记录<el-badge :is-dot="isDot" class="item"></el-badge>
          </span>
          <y-page-list-layout :page-list="pageData" :page-para="listQuery" :get-page-list="getList">
            <template slot="left">
              <el-button
                class="filter-item"
                style="margin-left: 10px;"
                type="success"
                round
                :disabled="disable"
                @click="dialogVisible = true"
              >
                回复
              </el-button>
            </template>
            <parentTable
              v-loading="listLoading"
              :data="pageData.records"
              slot="table"
              style="width: 100%;"
            >
              <el-table-column label="内容" align="center" width="500">
                <template slot-scope="{row}">
                  <span>{{ row.content }}</span>
                </template>
              </el-table-column>
              <el-table-column label="回复人" align="center">
                <template slot-scope="{row}">
                  <span>{{ row.replier }}</span>
                </template>
              </el-table-column>
              <el-table-column label="接收人" align="center">
                <template slot-scope="{row}">
                  <span>{{ row.receiver }}</span>
                </template>
              </el-table-column>
              <el-table-column label="状态" align="center">
                <template slot-scope="{row}">
                  <el-tag type="error">{{row.state?'已读':'未读'}}</el-tag>
                </template>
              </el-table-column>
              <el-table-column label="回复时间" align="center">
                <template slot-scope="{row}">
                  <span>{{ row.created}}</span>
                </template>
              </el-table-column>
            </parentTable>
          </y-page-list-layout>
        </el-tab-pane>
      </el-tabs>
      <el-dialog
        title="回复内容"
        :visible.sync="dialogVisible"
        width="30%"
        custom-class="tag-class"
      >
        <el-input
          type="textarea"
          :rows="8"
          placeholder="请输入内容"
          v-model="replyForm.content"
        />
        <span slot="footer" class="dialog-footer">
          <el-button @click="dialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="replay">确 定</el-button>
        </span>
      </el-dialog>
    </div>
  </div>
</template>
<script>
  import Breadcrumb from '@/components/Breadcrumb'
  import YDetailPageLayout from '@/components/YDetailPageLayout'

  export default {
    name: 'visitDetail',
    components: {
      Breadcrumb,
      YDetailPageLayout
    },
    data() {
      return {
        disable:null,
        paneLazy:true,
        isDot:null,
        replyForm: {
          id:null,
          content:null,
          bizType:'MARKET_VISIT_REPLY',
          bizTableId:this.$route.query.id,
          parentId:null,
          replierId:null,
          receiverId:null
        },
        dialogVisible: false,
        activeName: 'first',
        tableKey: 0,
        pageData: {},
        total: 20,
        listLoading: true,
        listQuery: {
          page: 1,
          size: 10,
          descs: 'id',
        },
        listQueryKey: 'keyword',
        postForm: {
          customerId:null,
          customerDepartment:null,
          customerSection:null,
          customerPosition:null,
          customerLevel:null,
          customerManagerId:null,
          userId:null
        },
        visitId : this.$route.query.id,
        vLoading: false,
        customers:[],
        lastReplier:null,
        lastReceiver:null,
      }
    },
    created() {
      this.listQuery = this.$route.query.listQuery;
      this.activeName = this.$route.query.activeName;
      this.getCustomer();
      this.getDetail();
      this.getNotRead();
    },
    watch:{
      activeName: {
        handler(newValue){
          if (newValue === 'replyList'){
            this.disable = this.$route.query.id===null?true:false;
            this.getList();
            this.isDot=false;
          }
        }
      }
    },
    methods: {
      getNotRead(){
        this.replyForm.receiverId = this.$store.getters.userInfo.id;
        this.replyForm.bizType = 'MARKET_VISIT_REPLY';
        this.replyForm.bizTableId = this.$route.query.id;
        this.$api.businessReply.notRead(this.replyForm).then(res => {
          if (res.code === 200){
            this.isDot = res.data !== 0;
          }
        });
      },
      replay(){
        if (this.lastReplier!=null && this.lastReceiver!=null){
          this.replyForm.receiverId = this.lastReplier;
          this.replyForm.replierId = this.$store.getters.userInfo.id;
        }
        if (this.lastReplier === this.$store.getters.userInfo.id){
          this.$notify({
            title: '错误',
            message: '不能回复给自己',
            type: 'error',
            duration: 2000
          });
          this.dialogVisible = false;
          return;
        }
        if (this.postForm.userId ===this.$store.getters.userInfo.id && this.lastReceiver===null){
          this.$notify({
            title: '错误',
            message: '不能回复给自己',
            type: 'error',
            duration: 2000
          });
          this.dialogVisible = false;
          return;
        }
        this.replyForm.replierId = this.$store.getters.userInfo.id;
        if (this.lastReceiver===null){
          this.replyForm.receiverId = this.postForm.userId;
        }
        this.$api.businessReply.add(Object.assign({}, this.replyForm, {
        })).then(res => {
          if (res.code === 200) {
            this.$notify({
              title: '成功',
              message: '回复成功',
              type: 'success',
              duration: 2000
            });
            this.dialogVisible = false;
            this.getList();
            this.vLoading = false
          }
        }).catch(() => {
          this.vLoading = false
        })
      },
      getList() {
        const that = this;
        this.listLoading = true;
        this.$api.businessReply.list(Object.assign({bizType:'MARKET_VISIT_REPLY', bizTableId:this.visitId}, that.listQuery)).then((res) => {
          that.pageData = res.data;
          this.lastReplier = res.data.records[0].replierId;
          this.lastReceiver = res.data.records[0].receiverId;
          setTimeout(() => {
            that.listLoading = false
          }, 200);
        })
          .catch(() => {
            that.listLoading = false
          })
      },
      getCustomerDetail(){
        this.$api.customer.detail(this.postForm.customerId).then(res =>{
          this.postForm.customerDepartment = res.data.department;
          this.postForm.customerPosition = res.data.position;
          this.postForm.customerSection = res.data.section;
          this.postForm.customerLevel = res.data.level;
        })
      },
      getDetail() {
        if (this.visitId) {
          this.$api.visit.detail(this.visitId).then(res => {
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
            if (this.visitId) {
              this.$api.visit.edit(Object.assign({}, this.postForm, {
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
                    this.$router.push(back);
                  }
                  this.initData();
                  this.vLoading = false
                }
              }).catch(() => {
                this.vLoading = false
              })
            } else {
              this.$api.visit.add(Object.assign({}, this.postForm, {
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
