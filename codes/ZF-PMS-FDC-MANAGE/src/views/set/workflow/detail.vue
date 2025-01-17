<template>
  <div class="app-container">
    <div class="title-container">
      <breadcrumb id="breadcrumb-container" class="breadcrumb-container"/>
    </div>
    <y-detail-page-layout @save="saveFlow" :edit-status="true" v-loading="vLoading" element-loading-text="处理中。。。" >
      <div style="padding-top: 30px;">
        <el-form ref="workflow" :model="workflow" class="form-container">
          <div class="createPost-main-container">
            <div class="postInfo-container">
              <div style="padding-bottom:20px">
                <el-divider content-position="left">
                <h3 class="title">
                  <div class="avatar-wrapper icon-title">flow</div>
                  <div class="icon-info">工作流</div>
                </h3>
                </el-divider>
                <div style="padding-left:80px;padding-top:20px">
                  <el-row>
                    <el-col :xs="24" :sm="12" :lg="6" :span="6">
                    
                      <el-form-item
                        label="名称："
                        prop="name"
                        :rules="{required: true, message: '工作流名称不能为空', trigger: 'blur'}"
                        label-width="120px"
                        class="postInfo-container-item"
                      >
                      <el-select
                        v-model="workflow.name"
                        placeholder="若未找到工作流名称，请联系管理员。"
                        clearable
                        filterable
                        class="filter-item"
                        style="width: 100%"
                        @change="selectFlow"
                      >
                        <el-option v-for="(item, index) in flows" :key="index" :label="item.name" :value="item.name"/>
                      </el-select>
                      </el-form-item>
                    </el-col>
                    <el-col :xs="24" :sm="12" :lg="6" :span="6">
                      <el-form-item
                        label="编码："
                        prop="code"
                        :rules="{required: true, message: '工作流编码不能为空', trigger: 'blur'}"
                        label-width="120px"
                        class="postInfo-container-item"
                      >
                      <el-input :value="workflow.code" class="filter-item" disabled readonly/>
                      </el-form-item>
                    </el-col>
                    <el-col :xs="24" :sm="12" :lg="6" :span="6">
                      <el-form-item
                        label="状态："
                        prop="state"
                        label-width="120px"
                        class="postInfo-container-item"
                      >
                      <el-switch
                        style="display: block;margin-top:4px"
                        v-model="workflow.state"
                        active-color="#13ce66"
                        inactive-color="#ff4949"
                        active-text="启用"
                        inactive-text="禁用"
                        active-value="false" inactive-value="true"	
                        :width=50>
                      </el-switch>
                      </el-form-item>
                    </el-col>
                  </el-row>
                </div>
              </div>
              <div style="padding-bottom:20px">
                <el-divider content-position="left">
                    <h3 class="title">
                      <div class="avatar-wrapper icon-title" style="background:rgba(255,175,41,1)">node</div>
                      <div class="icon-info">
                        流程节点
                      </div>
                    </h3>
                </el-divider>
                <div>
                  <el-button :type="editing==false?'info':'success'" round  style="float:right;margin-right:20px" @click="toEditNoe">编辑节点</el-button>
                </div>
                <div class="node-area" >
                  <div class="node-seq">
                    <WrokflowNode name="开始" state="START" />
                    <div class="add-icom" v-if="editing">
                         <img src="../../../assets/images/add.png" alt="" class="add-node-icom" @click="openDialog(null)"/>
                      </div>
                  </div>
                  <div class="node-seq"  v-for="(n,index) in nodes" :key="index">
                    <WrokflowNode :key="index" :name="n.name" :state="editing==true?'EDITING':''"  @openCurrentNodeDialog="openCurrentNodeDialog(n)" />
                    <div class="add-icom" v-if="editing">
                        <img src="../../../assets/images/add.png" alt="" class="add-node-icom" @click="openDialog(n)"/>
                    </div>
                  </div>
                  <div class="node-seq">
                    <WrokflowNode name="结束" state="END" :last=true />
                  </div>
                </div>
              </div>
            </div>
          </div>
        </el-form>
      </div>
    </y-detail-page-layout>
    <el-dialog width="700px" :visible.sync="dialogFormVisible" 
      custom-class="editNodeDialog" :before-close="cleanDialog">
      <el-form ref="nodeForm" :model="editNode" label-position="right" label-width="110px"
        style="width: 520px; margin-left:50px;">
        <el-form-item label="流程节点：" prop="name" :rules="{ required: true, message: '请填写节点名称', trigger: 'blur' }">
          <el-select
            v-model="editNode.code"
            placeholder="请选择"
            clearable
            filterable
            class="filter-item"
            style="width: 100%"
          >
            <el-option v-for="(item, index) in allNodes" :key="index" :label="item.name" :value="item.code"/>
          </el-select>
        </el-form-item>

        <el-form-item label="节点类型：" prop="type" :rules="{ required: true, message: '请选择节点类型', trigger: 'blur' }">
          <el-select
              v-model="editNode.type"
              placeholder="请选择"
              clearable
              filterable
              class="filter-item"
              style="width: 100%"
            >
              <el-option key="1" label="任务节点" value="TASK"/>
              <el-option key="2" label="状态节点" value="STATE"/>
            </el-select>
        </el-form-item>
        <el-form-item label="校验业务数据：" prop="verify" >
          <el-select
              v-model="editNode.verify"
              placeholder="请选择"
              clearable
              filterable
              class="filter-item"
              style="width: 100%"
            >
              <el-option key="1" label="评估对象价值校验(大中型)" value="CHECK_PRODUCTION_CHECK"/>
              <el-option key="2" label="出具产品类型校验(大中型)" value="CHECK_PRODUCTION_TYPE"/>
              <el-option key="3" label="产品已收款校验(大中型)" value="CHECK_PRODUCTION_FUND"/>
              <el-option key="4" label="报价反馈节点校验(个贷)" value="CHECK_FEEDBACK"/>
              <el-option key="5" label="出具产品类型校验(个贷)" value="CHECK_PERSONAL_PRODUCTION_TYPE"/>
              <el-option key="6" label="客户类型校验(个贷)" value="CHECK_PERSONAL_CLIENT_TYPE"/>
              <el-option key="7" label="出具产品类型校验(资产)" value="CHECK_ASSET_PRODUCTION_TYPE"/>
              <el-option key="8" label="报告四审校验(资产)" value="CHECK_ASSET_FOURTH_CHECK_REPORT"/>
              <el-option key="9" label="产品认领实收款校验(资产)" value="CHECK_ASSET_ORDER_FUND"/>
              <el-option key="10" label="土地报告备案校验(大中型)" value="CHECK_LAND_REPORT_IS_RECORD"/>
            </el-select>
        </el-form-item>
        <el-form-item label="节点任务:" prop="tasks">
        <el-checkbox-group v-model="editNode.taskItems" :disabled = "editNode.type === null || editNode.type ==='STATE'" >
          <el-checkbox v-for="(t,index) in taksEnums" :key="index" :label="t.description"	  name="tasks" @change="addToTasks(t)">{{t.description}}
          </el-checkbox>
        </el-checkbox-group>
      </el-form-item>
      <el-form-item :label="t.description+':'" :prop="t.description" v-for="(t,index) in editNode.tasks" :key="index">
          <el-select
              v-model="editNode.tasks[index].businessHandle"
              placeholder="业务操作"
              class="filter-item"
              style="width: 27%"
            >
            <el-option  label="需要业务操作" :value=true />
            <el-option  label="不需要业务操作" :value=false />
          </el-select>
          <el-select
              v-model="editNode.tasks[index].name"
              placeholder="处理人类型"
              clearable
              filterable
              class="filter-item"
              style="width: 27% ;margin-left:10px"
              @change="getOptions(editNode.tasks[index].name,editNode.tasks[index])"
            >
            <el-option v-for="(permission, index) in permissionEnum" :key="index" :label="permission.name" :value="permission.code"/>
          </el-select>
          <el-select
              v-if="editNode.tasks[index].name === 'EMPLOYEE'"
              v-model="editNode.tasks[index].powerId"
              placeholder="请选择员工"
              clearable
              filterable
              class="filter-item"
              style="width: 41%;margin-left:10px"
            >
            <el-option v-for="(user, index) in allUser" :key="index" :label="user.name" :value="user.id"/>
          </el-select>
          <el-select
              v-if="editNode.tasks[index].name === 'POST'"
              v-model="editNode.tasks[index].powerId"
              placeholder="请选择岗位"
              clearable
              filterable
              class="filter-item"
              style="width: 41%;margin-left:10px"
            >
            <el-option v-for="(post, index) in allPost" :key="index" :label="post.name" :value="post.id"/>
          </el-select>
      </el-form-item>
     
      <el-form-item label="限时完成:" prop="deadline">
        <el-input
          v-model.number="editNode.deadlineNumber"
          placeholder="请输入内容"
        >
          <el-select  slot="append" v-model="editNode.deadlineType" style="width: 130px;">
            <el-option label="小时" value="HOUR" ></el-option>
            <el-option label="分钟" value="MINUTE"></el-option>
          </el-select>
        </el-input>
      </el-form-item>
        <el-form-item label="可退回：" >
          <el-switch v-model="editNode.reversible" />
          <div style="float:right;margin-top:-10px;margin-left:3px">
            <el-alert
              title="当前节点的审批人可以将流程退回到上一个节点。"
              type="info"
              :closable="false"
              show-icon>
            </el-alert>
          </div>
        </el-form-item>
        <el-form-item label="可重置：" >
          <el-switch v-model="editNode.restartable" />
          <div style="float:right;margin-top:-10px;margin-left:3px">
            <el-alert
              title="当前节点的审批人可以将流程退回到第一个节点。"
              type="info"
              :closable="false"
              show-icon>
            </el-alert>
          </div>
        </el-form-item>
        <el-form-item label="可跳过：" >
          <el-switch v-model="editNode.skippable" />
          <div style="float:right;margin-top:-10px;margin-right:12px">
            <el-alert
              title="当前节点允许审批人不处理，进入下一个节点。"
              type="info"
              :closable="false"
              show-icon>
            </el-alert>
          </div>
        </el-form-item>
        <el-form-item label="可终止：" >
          <el-switch v-model="editNode.terminable" />
          <div style="float:right;margin-top:-10px;margin-right:78px">
            <el-alert
              title="当前节点的审批人可以将流程终止。"
              type="info"
              :closable="false"
              show-icon>
            </el-alert>
          </div>
        </el-form-item>
        <el-form-item label="任务描述:">
          <el-input
            v-model="editNode.info" type="textarea"
            placeholder="请输入内容"
          />
        </el-form-item>
      </el-form>
      <div slot="footer"  style="text-align:center;">
        <el-button v-if="editNode.id" type="danger" @click="delNode" style="width:520px;margin-bottom:10px" :disabled="!editing">删除</el-button>
        <el-button type="success" @click="saveNode" style="width:520px" :disabled="!editing">保存</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
  import Breadcrumb from '@/components/Breadcrumb';
  import WrokflowNode from '@/components/workflowNode'

  export default {
    name: 'workflowDetail',
    components: {
      Breadcrumb,
      WrokflowNode
    },

    data() {
      return {
        dialogFormVisible:false,
        vLoading: false,
        editing:false,
        postForm:{},
        nodes:[
        {
            flowId:null,
            priviousNodeId:null,
            code:null,
            type:null,
            name:"",
            tasks:[],
            deadlineNumber:null,
            deadlineType:null,
            deadline:null,
            taskItems:[]
          }
        ],
        allNodes:[],
        editNode:{
          flowId:null,
          priviousNodeId:null,
          code:null,
          type:null,
          tasks:[],
          deadlineNumber:null,
          deadlineType:"MINUTE",
          deadline:null,
          taskItems:[],
          info:null,
          name: null

        },
        taksEnums:[
          {
            description:null,
            taskType:null,
            businessHandle:null
          }
        ],
        workflow:{
          id:null,
          code:null,
          state:null
        },
        permissionEnum:[
          {
            name:null,
            code:null
          }
        ],
        allUser:[],
        allPost:[],
        flows:[]
      
      }
    },
    created() {
      this.workflow = this.$route.query;
      this.$route.query.back = "/setting/workflow/list";
      this.getNodeList();
      this.getFlowsEnum();
    },
    methods: {
      openDialog(node){
        this.getNodeEnum();
        this.getAllTaskEnum();
        this.getAllPermissionEnum();
        if (node!=null && node.id!=null){
          this.editNode.priviousNodeId = node.id
        }
        this.dialogFormVisible = true;
      },
      openCurrentNodeDialog(node){
        this.getNodeEnum();
        this.getAllTaskEnum();
        this.getAllPermissionEnum();
        this.getOptions("POST",null);
        this.getOptions("EMPLOYEE",null)
        this.editNode = node;
        this.dialogFormVisible = true;
      },
      getFlowsEnum(){
        this.$api.workflow.enum().then(res=>{
          if (res.code === 200){
              this.flows = res.data;
          }
        })
      },
      getNodeEnum(){
        this.$api.workNode.enum().then(res=>{
          if (res.code === 200){
              this.allNodes = res.data;
          }
        })
      },
      getAllTaskEnum(){
        this.$api.workNodeTask.enum().then(res=>{
          if (res.code === 200){
              this.taksEnums = res.data;
          }
        })
      },
      getAllPermissionEnum(){
        this.$api.workNodeTask.permissionEnum().then(res=>{
          if (res.code === 200){
              this.permissionEnum = res.data;
          }
        })
      },
      toEditNoe(){
        if (!this.workflow.id){
          this.$notify.error({
          title: '错误',
          message: '请先保存工作流名称和编码。'
        });
        return ;
        }
        this.editing = !this.editing;
      },
      saveFlow(){
        let workflow = this.workflow;
        this.$refs.workflow.validate(valid => {
            if (valid){
              if (workflow.id!=null){
          this.$api.workflow.edit(workflow).then(res=>{
            if (res.code === 200 && res.data ){
                this.$notify.success({
                  title: '成功',
                  message: '工作流修改成功。'
                });
            }else{
              this.$notify.error({
                title: '失败',
                message: '工作流修改失败，请稍后再试。'
              });
            }
          })
        }else{
            this.$api.workflow.add(workflow).then(res=>{
            if (res.code === 200 && res.data !=null ){
                this.workflow.id = res.data;
                this.$notify.success({
                  title: '成功',
                  message: '工作流保存成功，请继续添加节点。'
                });
            }else{
              this.$notify.error({
                title: '失败',
                message: '工作流保存失败，请稍后再试。'
            });
          }
          })
        }
            }
        })
       
      },
      saveNode(){
         if ( this.workflow.id && this.workflow.code){
          this.editNode.flowId = this.workflow.id;
          const node = this.allNodes.find(node => node.code === this.editNode.code);
          this.editNode.name = node.name;
          this.editNode.businessType =  this.workflow.code;
            if (this.editNode.deadlineNumber && this.editNode.deadlineType==="HOUR"){
                this.editNode.deadline = this.editNode.deadlineNumber * 60 *60 *1000;
            }
            if (this.editNode.deadlineNumber && this.editNode.deadlineType==="MINUTE"){
                this.editNode.deadline = this.editNode.deadlineNumber *60 *1000;
            }
          if (this.editNode.id){
            this.$api.workNode.edit(this.editNode).then(res=>{
              if (res.code === 200 && res.data){
                this.$notify.success({
                  title: '成功',
                  message: '节点修改成功。'
                });
                this.getNodeList();
                this.dialogFormVisible = false;
              }
           })
          }else{
            this.$api.workNode.add(this.editNode).then(res=>{
              if (res.code === 200 && res.data){
                this.$notify.success({
                  title: '成功',
                  message: '节点保存成功。'
                });
                this.getNodeList();
                this.dialogFormVisible = false;
              }
           })
          }
           
         }
      },
      getOptions(val,val1){
          if (val === 'EMPLOYEE'){
              this.$api.user.simpleAll().then(res=>{
                if (res.code === 200){
                  this.allUser = res.data;
                  return;
                }
              })
          }
          if (val === "POST"){
              this.$api.post.xSimpleAll().then(res=>{
                if (res.code === 200){
                  this.allPost = res.data;
                  return;
                }
              })
          }
          if (val1){
            if (val1.powerId){
                val1.powerId = null;
            }
            let permission = JSON.parse(val1.handlerPermission);
            if(permission.powerId){
              permission.powerId = null;
              val1.handlerPermission = JSON.stringify(permission);
            };
          }
      },
      cleanDialog(done){
        this.editNode = {   
          flowId:null,
          priviousNodeId:null,
          code:null,
          type:null,
          tasks:[],
          deadlineNumber:null,
          deadlineType:"MINUTE",
          deadline:null,
          taskItems:[]}
          done();
      },

      addToTasks(t){
        let taskItems = this.editNode.tasks.map(item=>item.taskType)
        if (!taskItems.includes(t.taskType)){
          this.editNode.tasks.push(t)
        }else{
          let index = 0;
          let flag = false;
          for ( let i in this.editNode.tasks){
              if (this.editNode.tasks[i].taskType === t.taskType){
                  index = i;
                  flag = true
                  break;
              }else {
                  flag = false
              }
          }
          if (flag) {
            this.editNode.tasks.splice(index, 1);
          }
        }
          
      },
      getNodeList(){
        const flowId = this.workflow.id;
        this.$api.workNode.flow(flowId).then(res=>{
          if (res.code === 200){
            const nodes =  res.data;
            for (let node in nodes){
                let tasks = nodes[node].tasks;
                let taskItems = tasks.map(item=>item.description);
                nodes[node].taskItems = taskItems;
            }
            this.nodes = nodes;
          }
        })
      },
      delNode(){
        if (this.editNode.id){
            this.$api.workNode.delete(this.editNode.id).then(res=>{
              if (res.code === 200 && res.data){
                this.$notify.success({
                  title: '成功',
                  message: '节点删除成功。'
                });
                this.getNodeList();
                this.dialogFormVisible = false;
              }
           })
          }
      },
      selectFlow(){
        if (this.workflow.name){
          for (let i in this.flows){
              if (this.flows[i].name === this.workflow.name){
                this.workflow.code = this.flows[i].code
                break;
              }
          }
        }
      }
    },
  }
</script>
<style lang="scss" scoped>
.node-area{
  padding-left:130px;
  padding-top:40px;
  width:100%;
  height:850px;
}
.add-icom :hover{
  cursor:pointer;

}
.add-node-icom{
  width:50px;
  height:50px;
  margin-top:65px;
  margin-left:10px;
}

/deep/.editNodeDialog {
  border-radius: 20px;
}
.node-seq{
  display:flex;
  float:left
}
</style>
