<template>
  <div class="contanier">
      <el-card shadow="always" style="margin-bottom:20px;">
      <div class="node-area" >
        <div class="node-seq">
          <WrokflowNode name="开始" state="START" />
        </div>
        <div class="node-seq"  v-for="(n,index) in queue" :key="index">
          <WrokflowNode :key="index" :name="n.nodeName" :state="n.state"  @openCurrentNodeDialog="openCurrentNodeDialog(n)" />
        </div>
        <div class="node-seq" v-if="!nodeBusinessInfo.production">
          <WrokflowNode name="待定"  state="WAITING" :last=true />
        </div>
        <div class="node-seq" v-else>
          <WrokflowNode name="结束" state="END" :last=true />
        </div>
      </div>
    </el-card>
    <el-dialog custom-class="nodeDialog" :title="dialogTitle" :visible.sync="dialogVisible" width="40%">
        <el-table :data="taskPageData"  border style="width:100%">
          <el-table-column label="任务" align="center"  prop="taskName" />
          <el-table-column label="处理人" align="center"   prop="handler" />
          <el-table-column label="任务状态" align="center"   prop="finished" />
          <el-table-column label="处理时间" align="center"   prop="finishTime" />
      </el-table>
    </el-dialog>
  </div>
</template>

<script>
import WrokflowNode from '@/components/workflowNode'

export default {
  components: {
    WrokflowNode
  },
  name: 'workflowBoard',
  props: {
    nodeBusinessInfo:{
      type: Object
    },
  },
  watch:{
    nodeBusinessInfo:{
      handler(newVal,oldVal){
          if (newVal.ccId){
             this.workNodeCommit.ccId= newVal.ccId;
             this.workNodeCommit.production= newVal.production;
          }
      },
      deep:true,
      immediate:true
    }
  },
  data() {
    return {
      queue:[
      {
          flowId:null,
          pnodeId:null,
          nodeId:null,
          flowCode:null,
          nodeName:"业务下单",
          nodeCode:null,
          state:"PENDING",
          instanceId:null
        }
      ],
      instance:{
        mainBusiness:null,
        businessId:null,
        businessSubId:null,
        businessMinId:null,
        production:[],
        ifPay:null
      },
      dialogVisible:false,
      taskPageData:[],
      dialogTitle:null,
      workNodeCommit:{
        instanceNodeId:null,
        state:null,
        comments:null,
        production:[],
        reportNos:[],
        ifCheckTask:true,
        businessSubId:null,
        doSecondCheck:null,
        doThirdCheck:null,
        doFourthCheck:null
      },
      
    }
  },

  created(){
    //this.getInstanceQueue();
  },
  methods: {
    openCurrentNodeDialog(node){
      this.dialogVisible = true;
      this.dialogTitle = node.nodeName;
      if (node.instanceId){
        this.$api.workNodeTaskRecord.instanceTask(node.instanceId).then(res=>{
          if (res.code === 200 ){
            this.taskPageData = res.data;
          }
        })
      }else {
        this.taskPageData = [];
      }
      
    },
    getInstanceQueue(){
      if (this.nodeBusinessInfo.mainBusiness!=null && this.nodeBusinessInfo.businessId!=null){
        this.instance.mainBusiness = this.nodeBusinessInfo.mainBusiness;
        this.instance.businessId = this.nodeBusinessInfo.businessId;
        this.instance.businessSubId = this.nodeBusinessInfo.businessSubId;
        this.instance.businessMinId = this.nodeBusinessInfo.businessMinId;
        this.instance.production = this.nodeBusinessInfo.production;
        this.instance.ifPay = this.nodeBusinessInfo.ifPay;
        this.$api.workNodeInstance.queue(this.instance).then(res =>{
          if (res.code ===200){
            this.queue = res.data;
          }
        })
      }
    },

    getInstanceArray(mainBusiness,businessId){
      this.instance.mainBusiness = mainBusiness;
      this.instance.businessId = businessId;
      this.$api.workNodeInstance.array(this.instance).then(res =>{
        if (res.code ===200){
          this.queue = res.data;
        }
      })
    },

    commit(state){
      let commit = this.workNodeCommit
      let handldType = "提交";
      let type = "success"
      if (state ==='REVERSE'){
        handldType= "退回"
        type = "warning"
      }
      if (state ==='SKIP'){
        handldType= "跳过"
        type = "warning"
      }
      if (state ==='TERMINATE'){
        handldType= "终止"
        type = "error"
      }
      if (state ==='RESTART'){
        handldType= "重置"
        type = "error"
      }
      //需要把必要的业务信息提交给流程
      commit.state = state;
      commit.instanceNodeId = this.nodeBusinessInfo.currentInstanceNodeId;
      commit.production = this.nodeBusinessInfo.production;
      commit.reportNos = this.nodeBusinessInfo.reportNos;
      commit.ifCheckTask = this.nodeBusinessInfo.ifCheckTask;
      commit.businessSubId = this.nodeBusinessInfo.businessSubId;
      commit.businessMinId = this.nodeBusinessInfo.businessMinId;
      commit.doSecondCheck = this.nodeBusinessInfo.doSecondCheck;
      commit.doThirdCheck = this.nodeBusinessInfo.doThirdCheck;
      commit.doFourthCheck = this.nodeBusinessInfo.doFourthCheck;
      commit.ifFeedback = this.nodeBusinessInfo.ifFeedback;
      commit.ifProductionFund = this.nodeBusinessInfo.ifProductionFund;
      commit.landIsRecord = this.nodeBusinessInfo.landIsRecord;
      commit.ifSkip = this.nodeBusinessInfo.ifSkip;
      commit.ifPay = this.nodeBusinessInfo.ifPay;
      let isOk = true
      this.$emit('workflowCommitVerify',{"commit":commit},val =>{
        if (!val.state){
          isOk = false;
        }
        if (val.cleanReport!=null){
            commit.cleanReportNo = val.cleanReport;
        }
        if (val.nextHandlerId){
          commit.nextHandlerId =  val.nextHandlerId;
        }
      });
      if (isOk){
          this.$prompt('请填写处理意见，若无可直接点击确认按钮。',handldType,{
              confirmButtonText: '确认',
              cancelButtonText: '取消',
              type: type,
          }).then(({value})=>{
            commit.comments = value;
            this.$api.workflow.commit(commit).then(res=>{
            if (res.code === 200 && res.data){
              this.$notify({
                  title: '成功',
                  message: '工作流节点提交成功。',
                  type: 'success',
                  duration: 1000,
                  offset: 100
                });
                this.goBack();
            }else{
              this.$notify({
                  title: '失败',
                  message: '工作流节点提交失败，请联系管理员。',
                  type: 'error',
                  duration: 1000,
                  offset: 100
                });
            }
          })
        })
      }
    },
    goBack(){
      const back = this.$route.query.back;

      if (back) {
        this.$router.push({path:back, query:this.listQuery})
      }
    },
    getPreviousNode(){
      const nodes = this.queue;
      nodes.map((item,index)=>{
        if (item.state==="PENDING"){
          this.$emit("getPreviousNode",this.queue[(index-1)])
        }
      })
    },
  }
}
</script>

<style lang="scss" scoped>
.contanier{
  width:100%;
  height:auto;
}
.node-area{
  width:100%;
}
.node-seq{
  display:flex;
  float:left
}
.button-area{
  text-align: right;
}
/deep/.nodeDialog {
border-radius: 20px;
}

</style>

