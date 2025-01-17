<template>
  <div class="app-container">
    <div class="title-container">
      <breadcrumb id="breadcrumb-container" class="breadcrumb-container" />
    </div>
    <el-collapse v-model="activeNames" style="margin-top: 30px;">
      <el-collapse-item name="board">
        <template slot="title">
          <span style="font-size:15px; margin-left:15px;letter-spacing:2px; color:RGB(168,168,168)">
            点击展开查看实时流程图
          </span>
        </template>
        <WorkflowBoard ref='board' :nodeBusinessInfo="nodeBusinessInfo" />
      </el-collapse-item>
    </el-collapse>
    <div class="button-area">
      <el-button v-if="nodeBusinessInfo.currentNodePermission.commit && doWorkflow" class="y-save" type="success" round
        @click="commit('PASS')">通过</el-button>
      <el-button v-if="nodeBusinessInfo.currentNodePermission.terminable && doWorkflow" class="y-save" type="danger" round
        @click="commit('TERMINATE')">拒绝</el-button>
      <el-button class="y-save" round type="info" @click="goBack">返回</el-button>
    </div>
    <el-card>
      <el-divider content-position="left">
        <span style="color:red;font-weight: bold;">
          【{{ this.nodeBusinessInfo.mainBusiness==='COMMISSION_DECLARE_MAJOR_EVALUATE'?'评估人员':'市场人员' }}】
        </span>大中型业务提成申报详情</el-divider>
      <el-form :model="declareForm" ref="declareForm">
        <el-row>
          <el-col :xs="24" :sm="12" :lg="24" :span="6">
            <el-form-item label="项目名称：" prop="name" label-width="140px" class="postInfo-container-item">
              <el-input :value="declareForm.name" :step="1" :max="100" style="width: 100%;" readonly disabled></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :xs="24" :sm="12" :lg="12" :span="6">
            <el-form-item label="订单号：" prop="orderId" label-width="140px" class="postInfo-container-item">
              <el-input :value="declareForm.orderId" :step="1" :max="100" style="width: 100%;" readonly
                disabled></el-input>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :lg="12" :span="6">
            <el-form-item label="报告号：" prop="reportNo" label-width="140px" class="postInfo-container-item">
              <el-input :value="declareForm.reportNo" :step="1" :max="100" style="width: 100%;" readonly
                disabled></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :xs="24" :sm="12" :lg="12" :span="6">
            <el-form-item label="业务分类：" prop="businessCate" label-width="140px" class="postInfo-container-item">
              <el-input :value="declareForm.businessCate" :step="1" :max="100" style="width: 100%;" readonly
                disabled></el-input>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :lg="12" :span="6">
            <el-form-item label="产品类型：" prop="production" label-width="140px" class="postInfo-container-item">
              <el-input :value="prod" :step="1" :max="100" style="width: 100%;" readonly
                disabled></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :xs="24" :sm="12" :lg="12" :span="6">
            <el-form-item label="申报人：" prop="declareUser" label-width="140px" class="postInfo-container-item">
              <el-input :value="declareForm.declareUser" style="width: 100%;" readonly disabled>
              </el-input>
            </el-form-item>
            </el-col>
            <el-col :xs="24" :sm="12" :lg="12" :span="6">
            <el-form-item label="申报日期：" prop="created" label-width="140px" class="postInfo-container-item">
              <el-input :value="declareForm.created" style="width: 100%;" readonly
                disabled></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :xs="24" :sm="12" :lg="12" :span="6">
            <el-form-item label="审核状态：" prop="declareResult" label-width="140px" class="postInfo-container-item">
              <el-input :value="declareForm.declareResult"  :class="['filter-item','resaon_input']" readonly disabled>
              </el-input>
            </el-form-item>
            </el-col>
            <el-col :xs="24" :sm="12" :lg="12" :span="6">
            <el-form-item label="审核备注：" prop="remarks" label-width="140px" class="postInfo-container-item">
              <el-input :value="declareForm.remarks+''"  style="width: 100%; color: red;" readonly
                disabled></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-table :data="declareForm.userShareRates" border style="width: 100%; margin-top: 10px; margin-bottom: 10px;">
          <el-table-column prop="userId" :label="this.nodeBusinessInfo.mainBusiness==='COMMISSION_DECLARE_MAJOR_EVALUATE'?'评估人员':'市场人员'" align="center">
            <template slot-scope="{row}">
              <span>{{ row.name }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="rate" label="提成比例" align="center">
            <template slot-scope="{row}">
              <span  style="color: red;">{{ row.rate*100 }}%</span>
            </template>
          </el-table-column>
        </el-table>
      </el-form>
    </el-card>
  </div>
</template>
<script>
import Breadcrumb from '@/components/Breadcrumb'
import WorkflowBoard from '@/components/workflowBoard'


export default {
  name: 'declareDetail',
  components: {
    Breadcrumb,
    WorkflowBoard
  },
  filters: {

  },
  computed: {
    prod(){
      if (this.declareForm.production==='REPORT'){
         return "报告";
      }
      if (this.declareForm.production==='LETTER'){
         return "复评函";
      }
      if (this.declareForm.production==='STATEMENT'){
         return "价值意见书";
      }
    }
  },
  data() {
    return {
      activeNames: ['board'],
      nodeBusinessInfo: {
        currentNodePermission: {
          commit: true,
          reversible: true,
          restartable: true,
          terminable: true,
          skippable: true,
        },
        mainBusiness: null,
        doWorkflow: null,
        businessSubId: null,
        businessId: null,
        currentInstanceNodeId: null,
        production: [],
        ifSkip:false,
        currentNodeCode:null
      },
      doWorkflow: false,
      declareForm: {
        orderId: null,
        name: null,
        reportNo: null,
        businessId: null,
        productionId: null,
        businessType: null,
        commissionRateId: null,
        userShareRates: [
          {
            userId: this.$store.getters.userInfo.id,
            rate: 100
          }
        ],
        production:null,
        remarks:[]

      },
      users: [],
      cates: []
    }
  },
  created() {
    this.nodeBusinessInfo.businessId = this.$route.query.businessId;
    this.nodeBusinessInfo.mainBusiness = this.$route.query.businessType;
    this.declareForm.businessType = this.$route.query.businessType;
    this.doWorkflow = this.$route.query.doWorkflow === 'true';
    this.getCurrentNodeInfo();
    this.businessId = this.$route.query.businessId;
    this.getDeclareDetail();
  },
  methods: {

    getCurrentNodeInfo() {
      if (this.nodeBusinessInfo.businessId) {
        this.$api.workNodeInstance.currentNode({
          "mainBusiness": this.nodeBusinessInfo.mainBusiness,
          "businessId": this.nodeBusinessInfo.businessId
        }).then(res => {
          if (res.code === 200 && res.data!=null) {
              this.nodeBusinessInfo.currentInstanceNodeId = res.data.instanceId;
              this.nodeBusinessInfo.currentNodeCode = res.data.nodeCode;
          }
          this.$refs.board.getInstanceArray(this.nodeBusinessInfo.mainBusiness,this.nodeBusinessInfo.businessId);
        })
      }
    },
    goBack() {
      const back = this.$route.query.back
      if (back) {
        this.$router.push(back)
      }
    },
    getSimpleAllUser() {
      this.$api.user.simpleAll().then(res => {
        if (res.code === 200) {
          this.users = res.data;
        }
      })
    },
    getMajorMarketCate() {
      this.$api.businessCommissionRate.cateList("BUSINESS_MAJOR", "MARKETER").then(res => {
        if (res.code === 200) {
          this.cates = res.data;
        }
      })
    },
    getDeclareDetail() {
      this.$api.commissonDeclare.detail(this.businessId).then(res => {
        if (res.code === 200) {
          this.declareForm = res.data;
        }
      })
    },
    //提交节点
    commit(state){
        if (this.declareForm.businessCate==='一般业务'  
              && this.nodeBusinessInfo.currentNodeCode === 'DEPARTMENT_LEADER_CHECK'
                && this.nodeBusinessInfo.mainBusiness === 'COMMISSION_DECLARE_MAJOR_EVALUATE'){
               
          this.nodeBusinessInfo.ifSkip = true;
          
        }
       this.$refs.board.commit(state);
    },

  },

}
</script>
<style lang="scss" scoped>
.button-area {
  position: absolute;
  top: 70px;
  right: 50px;
}
.resaon_input{
    /deep/ .el-input__inner{
      color: red !important;
      font-weight: bold;
    }
  }
</style>
