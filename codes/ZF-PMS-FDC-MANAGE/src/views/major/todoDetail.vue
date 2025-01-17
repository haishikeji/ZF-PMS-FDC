<template>
  <div class="app-container">
    <!-- <div class="title-container">
      <breadcrumb id="breadcrumb-container" class="breadcrumb-container"/>
    </div> -->
    <el-collapse v-model="activeName" @change="handleChange">
      <el-collapse-item>
        <template slot="title">
          <span style="font-size:20px; color:RGB(42,143,277)">
            <i class="el-icon-star-on"></i>
            <span> {{ currentNode == null ? '???' : currentNode.nodeName ? currentNode.nodeName : '结束' }}</span>
            <span style="margin-left:30px" v-if="reportNo || statementNo">已取号：<span style="font-style:italic;"
                v-if="statementNo">{{ statementNo }}</span>
              <span style="font-style:italic;margin-left:30px" v-if="reportNo">{{ reportNo }}</span></span>
          </span>
          <span style="font-size:15px; margin-left:15px;letter-spacing:2px; color:RGB(42,143,277)">
            点击展开查看实时流程图
          </span>
        </template>
        <WorkflowBoard ref='board' @workflowCommitVerify="workflowCommitVerify" :nodeBusinessInfo="nodeBusinessInfo"
          @getPreviousNode="getPreviousNode" />
      </el-collapse-item>
    </el-collapse>
    <div class="button-area">
      <el-button v-if="nodeBusinessInfo.currentNodePermission.commit" class="y-save" type="success" round
        @click="commit('PASS')" :disabled="!nodeBusinessInfo.doWorkflow">提交</el-button>
      <el-button v-if="nodeBusinessInfo.currentNodePermission.reversible" class="y-save" type="warning" round
        @click="commit('REVERSE')" :disabled="!nodeBusinessInfo.doWorkflow">退回</el-button>
      <el-button v-if="nodeBusinessInfo.currentNodePermission.skippable" class="y-save" type="warning" round
        @click="commit('SKIP')" :disabled="!nodeBusinessInfo.doWorkflow">跳过</el-button>
      <el-button v-if="nodeBusinessInfo.currentNodePermission.terminable" class="y-save" type="danger" round
        @click="commit('TERMINATE')" :disabled="!nodeBusinessInfo.doWorkflow">终止</el-button>
      <el-button v-if="nodeBusinessInfo.currentNodePermission.restartable" class="y-save" type="danger" round
        @click="commit('RESTART')" :disabled="!nodeBusinessInfo.doWorkflow">重置</el-button>
      <el-button class="y-save" round type="info" @click="goBack">返回</el-button>
    </div>
    <el-tabs v-model="activeTagName" @tab-click="handleClick" type="border-card">
      <el-tab-pane name="order" class="pane-class" :lazy=true>
        <span slot="label"><i class="el-icon-document"></i> 市场订单</span>
        <y-detail-page-layout @save="handleCreate" :edit-status="couldEdit">
          <div>
            <el-form ref="majorForm" :model="major" class="form-container">
              <div class="createMajor-main-container">
                <div class="postInfo-container">
                  <div>
                    <el-divider content-position="left">
                      <h3 class="title">
                        <div class="avatar-wrapper icon-title">订单</div>
                        <div class="icon-info">大中型订单</div>
                      </h3>
                    </el-divider>
                  </div>
                  <el-row class="row-style">
                    <el-col :xs="24" :sm="12" :lg="6" :span="6">
                      <el-form-item label="客户经理：" prop="clientManager"
                        :rules="{ required: true, message: '客户经理不能为空', trigger: 'blur' }" label-width="120px"
                        class="postInfo-container-item">
                        <el-input :value="major.clientManager" class="filter-item" readonly disabled />
                      </el-form-item>
                    </el-col>
                    <el-col :xs="24" :sm="12" :lg="6" :span="6">
                      <el-form-item label="订单属性：" prop="financial"
                        :rules="{ required: true, message: '订单属性不能为空', trigger: 'blur' }" label-width="120px"
                        class="postInfo-container-item">
                        <el-select v-model="major.financial" placeholder="请选择" readonly disabled style=" width: 100%">
                          <el-option label="金融" :value="true">金融</el-option>
                          <el-option label="非金融" :value="false">非金融</el-option>
                        </el-select>
                      </el-form-item>
                    </el-col>
                    <el-col :xs="24" :sm="12" :lg="6" :span="6">
                      <el-form-item label="派单方式：" prop="allotType"
                        :rules="{ required: true, message: '派单方式不能为空', trigger: 'blur' }" label-width="120px"
                        class="postInfo-container-item">
                        <el-select v-model="major.allotType" placeholder="请选择" readonly disabled style=" width: 100%">
                          <el-option label="部门轮单" value="轮单"></el-option>
                          <el-option label="部门指派" value="指派"></el-option>
                        </el-select>
                      </el-form-item>
                    </el-col>
                    <el-col :xs="24" :sm="12" :lg="6" :span="6">
                      <el-form-item label="接单部门：" prop="departmentId" label-width="120px" class="postInfo-container-item">
                        <el-select v-model="major.departmentId" placeholder="请选择" readonly disabled style=" width: 100%">
                          <el-option v-for="(d, id) in allotDepartment" :label="d.name" :value="d.id"></el-option>
                        </el-select>
                      </el-form-item>
                    </el-col>
                  </el-row>
                  <el-row class="row-style">
                    <el-col :xs="24" :sm="12" :lg="6" :span="6">
                      <el-form-item label="项目负责人：" prop="principalId" label-width="140px"
                        :class="['postInfo-container-item', currentNode.nodeCode === 'DEPARTMENT_ALLOCATION' ? 'task-class' : '']"
                        :rules="{ required: true, message: '项目负责人不能为空', trigger: 'blur' }">
                        <el-select v-model="major.principalId" placeholder="请选择" style=" width: 100%"
                          :filterable="currentNode.nodeCode === 'DEPARTMENT_ALLOCATION'"
                          :clearable="currentNode.nodeCode === 'DEPARTMENT_ALLOCATION'"
                          :readonly="currentNode.nodeCode != 'DEPARTMENT_ALLOCATION'"
                          :disabled="currentNode.nodeCode != 'DEPARTMENT_ALLOCATION'">
                          <el-option v-for="(p, id) in principals" :label="p.name" :value="p.id"></el-option>
                        </el-select>
                      </el-form-item>
                    </el-col>
                    <el-col :xs="24" :sm="12" :lg="12" :span="6">
                      <el-form-item label="项目成员：" prop="membersId" label-width="120px"
                        :class="['postInfo-container-item', currentNode.nodeCode === 'DEPARTMENT_ALLOCATION' ? 'task-class' : '']"
                        :rules="{ required: true, message: '项目成员不能为空', trigger: 'blur' }">
                        <el-select v-model="major.membersId" placeholder="请选择" multiple
                          :filterable="currentNode.nodeCode === 'DEPARTMENT_ALLOCATION'"
                          :clearable="currentNode.nodeCode === 'DEPARTMENT_ALLOCATION'"
                          :readonly="currentNode.nodeCode != 'DEPARTMENT_ALLOCATION'"
                          :disabled="currentNode.nodeCode != 'DEPARTMENT_ALLOCATION'" style=" width: 100%">
                          <el-option v-for="(u, id) in allUsers" :label="u.name" :value="u.id"></el-option>
                        </el-select>
                      </el-form-item>
                    </el-col>
                  </el-row>
                  <el-row class="row-style">
                    <el-col :xs="12" :sm="12" :lg="18" :span="6">
                      <el-form-item label="订单名称：" prop="name"
                        :rules="{ required: true, message: '订单名称不能为空', trigger: 'blur' }" label-width="120px"
                        class="postInfo-container-item">
                        <el-input v-model="major.name" class="filter-item" readonly disabled />
                      </el-form-item>
                    </el-col>
                  </el-row>
                  <el-row class="row-style">
                    <el-col :xs="24" :sm="12" :lg="6" :span="6">
                      <el-form-item label="对象类型：" prop="businessObjectType"
                        :rules="{ required: true, message: '评估对象类型不能为空', trigger: 'blur' }" label-width="120px"
                        class="postInfo-container-item">
                        <el-select v-model="major.businessObjectType" placeholder="请选择" readonly disabled
                          style=" width: 100%">
                          <el-option label="土地" value="土地"></el-option>
                          <el-option label="房地产" value="房地产"></el-option>
                        </el-select>
                      </el-form-item>
                    </el-col>
                    <el-col :xs="24" :sm="12" :lg="6" :span="6" v-if="major.businessObjectType === '房地产'">
                      <el-form-item label="特殊类型：" prop="specialType" label-width="120px" class="postInfo-container-item">
                        <el-select v-model="major.specialType" placeholder="请选择" readonly disabled style=" width: 100%">
                          <el-option label="在建工程" value="在建工程"></el-option>
                          <el-option label="加油加气站" value="加油加气站"></el-option>
                          <el-option label="电站" value="电站"></el-option>
                          <el-option label="客运站" value="客运站"></el-option>
                        </el-select>
                      </el-form-item>
                    </el-col>
                    <el-col :xs="24" :sm="12" :lg="6" :span="6">
                      <el-form-item label="业务类型：" prop="businessGener" label-width="120px"
                        class="postInfo-container-item">
                        <el-select v-model="major.businessGener" placeholder="请选择" readonly disabled style=" width: 100%">
                          <el-option label="部门轮单" value="轮单"></el-option>
                          <el-option label="部门指派" value="指派"></el-option>
                        </el-select>
                      </el-form-item>
                    </el-col>
                    <el-col :xs="24" :sm="12" :lg="6" :span="6">
                      <el-form-item label="评估目的：" prop="evaluateAim"
                        :rules="{ required: true, message: '评估目的不能为空', trigger: 'blur' }" label-width="120px"
                        class="postInfo-container-item">
                        <el-select v-model="major.evaluateAim" placeholder="请选择" readonly disabled style=" width: 100%">
                          <el-option label="抵押" value="抵押"></el-option>
                          <el-option label="咨询" value="咨询"></el-option>
                          <el-option label="融资" value="融资"></el-option>
                          <el-option label="司法" value="司法"></el-option>
                          <el-option label="可行性研究" value="可行性研究"></el-option>
                          <el-option label="不良资产价值评估" value="不良资产价值评估"></el-option>
                          <el-option label="其他" value="其他"></el-option>
                        </el-select>
                      </el-form-item>
                    </el-col>
                  </el-row>
                  <el-row class="row-style">
                    <el-col :xs="24" :sm="12" :lg="6" :span="6">
                      <el-form-item label="再次委托：" prop="entrustAgain"
                        :rules="{ required: true, message: '再次委托不能为空', trigger: 'blur' }" label-width="120px"
                        class="postInfo-container-item">
                        <el-radio v-model="major.entrustAgain" :label="false" size="medium" readonly disabled>否</el-radio>
                        <el-radio v-model="major.entrustAgain" :label="true" size="medium" readonly disabled>是</el-radio>
                      </el-form-item>
                    </el-col>
                    <el-col :xs="24" :sm="12" :lg="6" :span="6">
                      <el-form-item label="分户搬迁：" prop="multipleRemoval"
                        :rules="{ required: true, message: '分户搬迁不能为空', trigger: 'blur' }" label-width="120px"
                        class="postInfo-container-item">
                        <el-radio v-model="major.multipleRemoval" :label="false" size="medium" readonly
                          disabled>否</el-radio>
                        <el-radio v-model="major.multipleRemoval" :label="true" size="medium" readonly
                          disabled>是</el-radio>
                      </el-form-item>
                    </el-col>
                    <el-col :xs="24" :sm="12" :lg="6" :span="6">
                      <el-form-item label="是否口估：" prop="informalEvaluate"
                        :rules="{ required: true, message: '是否口估不能为空', trigger: 'blur' }" label-width="120px"
                        class="postInfo-container-item">
                        <el-radio v-model="major.informalEvaluate" :label="false" size="medium" readonly
                          disabled>否</el-radio>
                        <el-radio v-model="major.informalEvaluate" :label="true" size="medium" readonly
                          disabled>是</el-radio>
                      </el-form-item>
                    </el-col>
                  </el-row>
                  <el-row class="row-style">
                    <el-col :xs="24" :sm="12" :lg="6" :span="6">
                      <el-form-item label="客户类型：" prop="clienteleType"
                        :rules="{ required: true, message: '客户类型不能为空', trigger: 'blur' }" label-width="120px"
                        class="postInfo-container-item">
                        <el-select v-model="major.clienteleType" placeholder="请选择" readonly disabled style=" width: 100%">
                          <el-option label="企业" value="企业"></el-option>
                          <el-option label="个人" value="个人"></el-option>
                        </el-select>
                      </el-form-item>
                    </el-col>
                    <el-col :xs="24" :sm="12" :lg="6" :span="6">
                      <el-form-item label="客户名称：" prop="clienteleId" label-width="120px" class="postInfo-container-item">
                        <el-input :value="major.cclienteleName" class="filter-item" readonly disabled />
                      </el-form-item>
                    </el-col>
                    <el-col :xs="24" :sm="12" :lg="6" :span="6">
                      <el-form-item label="业务来源：" prop="clienteleId" label-width="120px" class="postInfo-container-item">
                        <el-input :value="major.cclienteleSubName" class="filter-item" readonly disabled />
                      </el-form-item>
                    </el-col>
                  </el-row>
                  <el-row class="row-style">
                    <el-col :xs="24" :sm="12" :lg="6" :span="6">
                      <el-form-item label="客户联系人：" prop="clienteleContactId"
                        :rules="{ required: true, message: '客户联系人不能为空', trigger: 'blur' }" label-width="120px"
                        class="postInfo-container-item">
                        <el-input :value="major.cclienteleContactName" class="filter-item" readonly disabled />
                      </el-form-item>
                    </el-col>
                    <el-col :xs="24" :sm="12" :lg="6" :span="6">
                      <el-form-item label="联系人电话：" prop="clienteleContactId" label-width="120px"
                        class="postInfo-container-item">
                        <el-input :value="major.cmobile" class="filter-item" readonly disabled />
                      </el-form-item>
                    </el-col>
                    <el-col :xs="24" :sm="12" :lg="6" :span="6">
                      <el-form-item label="联系人部门：" prop="clienteleContactId" label-width="120px"
                        class="postInfo-container-item">
                        <el-input :value="major.cdepartment" class="filter-item" readonly disabled />
                      </el-form-item>
                    </el-col>
                    <el-col :xs="24" :sm="12" :lg="6" :span="6">
                      <el-form-item label="联系人职务：" prop="clienteleContactId" label-width="120px"
                        class="postInfo-container-item">
                        <el-input :value="major.cduty" class="filter-item" readonly disabled />
                      </el-form-item>
                    </el-col>
                  </el-row>
                  <el-row class="row-style">
                    <el-col :xs="24" :sm="12" :lg="6" :span="6">
                      <el-form-item label="分配产值：" prop="dispenseBenefit"
                        :rules="{ required: true, message: '分配产值不能为空', trigger: 'blur' }" label-width="120px"
                        class="postInfo-container-item">
                        <el-radio v-model="major.dispenseBenefit" :label="false" size="medium" readonly
                          disabled>否</el-radio>
                        <el-radio v-model="major.dispenseBenefit" :label="true" size="medium" readonly
                          disabled>是</el-radio>
                      </el-form-item>
                    </el-col>
                    <el-col :xs="24" :sm="12" :lg="6" :span="6">
                      <el-form-item label="是否异地：" prop="nonnative"
                        :rules="{ required: true, message: '是否异地不能为空', trigger: 'blur' }" label-width="120px"
                        class="postInfo-container-item">
                        <el-radio v-model="major.nonnative" :label="false" size="medium" readonly disabled>否</el-radio>
                        <el-radio v-model="major.nonnative" :label="true" size="medium" readonly disabled>是</el-radio>
                      </el-form-item>
                    </el-col>
                    <el-col :xs="24" :sm="12" :lg="6" :span="6">
                      <el-form-item label="是否续贷：" prop="refinance"
                        :rules="{ required: true, message: '是否续贷不能为空', trigger: 'blur' }" label-width="120px"
                        class="postInfo-container-item">
                        <el-radio v-model="major.refinance" :label="false" size="medium" readonly disabled>否</el-radio>
                        <el-radio v-model="major.refinance" :label="true" size="medium" readonly disabled>是</el-radio>
                      </el-form-item>
                    </el-col>
                    <el-col :xs="24" :sm="12" :lg="6" :span="6">
                      <el-form-item label="贷款到期：" prop="loanExpire" label-width="120px" class="postInfo-container-item">
                        <el-radio v-model="major.loanExpire" :label="false" size="medium" readonly disabled>否</el-radio>
                        <el-radio v-model="major.loanExpire" :label="true" size="medium" readonly disabled>是</el-radio>
                      </el-form-item>
                    </el-col>
                  </el-row>
                  <el-row class="row-style">
                    <el-col :xs="24" :sm="12" :lg="6" :span="6">
                      <el-form-item label="贷款额度：" prop="loanLimit" label-width="120px" class="postInfo-container-item">
                        <el-input v-model="major.loanLimit" class="filter-item" readonly disabled>
                          <template slot="append">万元</template>
                        </el-input>
                      </el-form-item>
                    </el-col>
                    <el-col :xs="24" :sm="12" :lg="6" :span="6">
                      <el-form-item label="贷款期限：" prop="loanPeriod" label-width="120px" class="postInfo-container-item">
                        <el-input v-model="major.loanPeriod" class="filter-item" readonly disabled>
                          <template slot="append">年</template>
                        </el-input>
                      </el-form-item>
                    </el-col>
                    <el-col :xs="24" :sm="12" :lg="6" :span="6">
                      <el-form-item label="贷款性质：" prop="loanNature" label-width="120px" class="postInfo-container-item">
                        <el-input v-model="major.loanNature" class="filter-item" readonly disabled />
                      </el-form-item>
                    </el-col>
                    <el-col :xs="24" :sm="12" :lg="6" :span="6">
                      <el-form-item label="贷款用途：" prop="loanAim" label-width="120px" class="postInfo-container-item">
                        <el-input v-model="major.loanAim" class="filter-item" readonly disabled />
                      </el-form-item>
                    </el-col>
                  </el-row>
                  <el-row class="row-style">
                    <el-col :xs="24" :sm="12" :lg="12" :span="6">
                      <el-form-item label="委托人：" prop="bailor"
                        :rules="{ required: true, message: '委托人不能为空', trigger: 'blur' }" label-width="120px"
                        class="postInfo-container-item">
                        <el-input v-model="major.bailor" class="filter-item" readonly disabled />
                      </el-form-item>
                    </el-col>
                    <el-col :xs="24" :sm="12" :lg="12" :span="6">
                      <el-form-item label="委托人地址：" prop="bailorAddress" label-width="120px"
                        class="postInfo-container-item">
                        <el-input v-model="major.bailorAddress" class="filter-item" readonly disabled />
                      </el-form-item>
                    </el-col>
                  </el-row>
                  <el-row class="row-style">
                    <el-col :xs="24" :sm="12" :lg="6" :span="6">
                      <el-form-item label="委托联系人：" prop="bailorContactName"
                        :rules="{ required: true, message: '委托联系人不能为空', trigger: 'blur' }" label-width="140px"
                        class="postInfo-container-item">
                        <el-input v-model="major.bailorContactName" class="filter-item" readonly disabled />
                      </el-form-item>
                    </el-col>
                    <el-col :xs="24" :sm="12" :lg="6" :span="6">
                      <el-form-item label="委托电话：" prop="bailorContactTel"
                        :rules="{ required: true, message: '委托联系人电话不能为空', trigger: 'blur' }" label-width="120px"
                        class="postInfo-container-item">
                        <el-input v-model="major.bailorContactTel" class="filter-item" readonly disabled />
                      </el-form-item>
                    </el-col>
                  </el-row>
                  <el-row class="row-style">
                    <el-col :xs="24" :sm="12" :lg="12" :span="6">
                      <el-form-item label="产权人：" prop="owner" label-width="120px" class="postInfo-container-item">
                        <el-input v-model="major.owner" class="filter-item" readonly disabled />
                      </el-form-item>
                    </el-col>
                    <el-col :xs="24" :sm="12" :lg="6" :span="6">
                      <el-form-item label="产权人电话：" prop="ownerTel" label-width="120px" class="postInfo-container-item">
                        <el-input v-model="major.ownerTel" class="filter-item" readonly disabled />
                      </el-form-item>
                    </el-col>
                    <el-col :xs="24" :sm="12" :lg="6" :span="6">
                      <el-form-item label="是否加急：" prop="urgent"
                        :rules="{ required: true, message: '是否加急不能为空', trigger: 'blur' }" label-width="120px"
                        class="postInfo-container-item">
                        <el-radio v-model="major.urgent" :label="false" size="medium" readonly disabled>否</el-radio>
                        <el-radio v-model="major.urgent" :label="true" size="medium" readonly disabled>是</el-radio>
                      </el-form-item>
                    </el-col>
                  </el-row>
                  <el-row class="row-style">
                    <el-col :xs="24" :sm="12" :lg="6" :span="6">
                      <el-form-item label="终端客户类型：" prop="terminalClienteleType" label-width="140px"
                        class="postInfo-container-item">
                        <el-select v-model="major.terminalClienteleType" placeholder="请选择" readonly disabled
                          style=" width: 100%">
                          <el-option label="企业" value="企业"></el-option>
                          <el-option label="个人" value="个人"></el-option>
                        </el-select>
                      </el-form-item>
                    </el-col>
                    <el-col :xs="24" :sm="12" :lg="12" :span="6" v-if="major.terminalClienteleType != ''">
                      <el-form-item label="终端客户名称：" prop="terminalClienteleId" label-width="140px"
                        :rules="{ required: true, message: '终端客户名称不能为空', trigger: 'blur' }"
                        class="postInfo-container-item">
                        <el-input :value="major.tterminalClienteleName" class="filter-item" readonly disabled />
                      </el-form-item>
                    </el-col>
                    <el-col :xs="24" :sm="12" :lg="6" :span="6" v-if="major.terminalClienteleType == '企业'">
                      <el-form-item label="终端联系人：" prop="terminalClienteleContactId" label-width="120px"
                        :rules="{ required: true, message: '终端联系人不能为空', trigger: 'blur' }"
                        class="postInfo-container-item">
                        <el-input :value="major.tterminalClienteleContactName" class="filter-item" readonly disabled />
                      </el-form-item>
                    </el-col>
                    <el-col :xs="24" :sm="12" :lg="6" :span="6">
                      <el-form-item label="终端联系电话：" prop="clienteleType" label-width="130px"
                        class="postInfo-container-item">
                        <el-input :value="major.tmobile" class="filter-item" readonly disabled />
                      </el-form-item>
                    </el-col>
                  </el-row>
                  <el-row class="row-style">
                    <el-col :xs="24" :sm="12" :lg="18" :span="12">
                      <el-form-item label="订单备注：" prop="remark" label-width="120px" class="postInfo-container-item">
                        <el-input v-model="major.remark" :autosize="{ minRows: 1, maxRows: 1 }" class="filter-item"
                          type="textarea" readonly disabled />
                      </el-form-item>
                    </el-col>
                  </el-row>
                </div>
              </div>
            </el-form>
          </div>
        </y-detail-page-layout>
      </el-tab-pane>
      <el-tab-pane name="evaluate" class="pane-class" :lazy=true>
        <span slot="label"><i class="el-icon-office-building"></i> 估价对象</span>
        <div class="createMajor-main-container">
          <div class="postInfo-container">
            <div>
              <el-divider content-position="left">
                <h3 class="title">
                  <div class="avatar-wrapper icon-title">对象</div>
                  <div class="icon-info">{{ major.businessObjectType }}</div>
                </h3>
              </el-divider>
            </div>
          </div>
        </div>
        <div class="evaluate-table" v-if="major.businessObjectType === '土地'">
          <div>
            <el-row>
              <el-col :span="2">
                <div>
                  <el-statistic group-separator="," :precision="4" :value="totalEvaluateValue" title="总价(万)">
                    <template slot="prefix">
                      <i class="el-icon-coin"></i>
                    </template>
                  </el-statistic>
                </div>
              </el-col>
              <el-col :span="2">
                <div>
                  <el-statistic group-separator="," :precision="2" :value="totalAcreage" title="总面积(平)">
                    <template slot="prefix">
                      <i class="el-icon-house"></i>
                    </template>
                  </el-statistic>
                </div>
              </el-col>
              <el-col :span="2">
                <el-button v-if="couldAddTarget" type="danger" round
                  @click="openLandDialog(major.businessObjectType)">添加估价对象</el-button>
              </el-col>
              <el-col :span="2">
                <el-button v-if="couldAddTarget" type="warning" :disabled="!takeNumberItems.length > 0" round
                @click="openMergeNumberDialog()">合并到已有号</el-button>
              </el-col>
              <el-col :span="2" v-if="currentNode.nodeCode === 'WRITE_STATEMENT'">
                <el-tooltip v-if="ifTakeNumber" class="item" effect="dark" content="全选取主号，多次取号(主号-子号)" placement="right">
                  <el-button type="success" :disabled="!takeNumberItems.length > 0" round
                    @click="takeStatementNoOrLettertNo('STATEMENT')">取价值意见书号</el-button>
                </el-tooltip>
                <!-- <el-button v-if="ifCreatePorduction" type="danger"  round @click="createProduction(reportNo)">创建产品</el-button> -->
              </el-col>
              <el-col :span="2" v-if="currentNode.nodeCode === 'WRITE_REPORT'">
                <el-tooltip v-if="ifTakeNumber" class="item" effect="dark" content="全选取主号，多次取号(主号-子号)" placement="right">
                  <el-button type="danger" :disabled="!takeNumberItems.length > 0" round
                    @click="takeNumberDialog = true">取报告号</el-button>
                </el-tooltip>
                <!-- <el-button v-if="ifCreatePorduction" type="danger"  round @click="createProduction(reportNo)">创建产品</el-button> -->
              </el-col>
              <el-col :span="2" v-if="currentNode.nodeCode === 'WRITE_LETTER'">
                <el-tooltip v-if="ifTakeNumber" class="item" effect="dark" content="全选取主号，多次取号(主号-子号)" placement="right">
                  <el-button :disabled="!takeNumberItems.length > 0" round
                    @click="takeStatementNoOrLettertNo('LETTER')">取复评函号</el-button>
                </el-tooltip>
                <!-- <el-button v-if="ifCreatePorduction" type="danger"  round @click="createProduction(reportNo)">创建产品</el-button> -->
              </el-col>
            </el-row>
          </div>
          <el-table ref="multipleTable" :data="evaluateLandData" stripe
            :header-row-style="{ color: '#333333', 'font-size': '14px' }" border takeNumberItems
            style="width: 100%; margin-top:20px;font-size: 14px;" @selection-change="handleSelectionChange">
            <el-table-column v-if="ifTakeNumber" type="selection" width="100" :selectable="selectable" align="center">
            </el-table-column>
            <el-table-column type="index" label="序号" width="50" align="center">
            </el-table-column>
            <el-table-column prop="located" label="坐落" align="center" show-overflow-tooltip>
            </el-table-column>
            <el-table-column prop="landType" label="土地类型" width="80" align="center">
            </el-table-column>
            <el-table-column prop="landUse" label="用途" width="80" align="center">
            </el-table-column>
            <el-table-column prop="rightToUser" label="所有权人" align="center" show-overflow-tooltip>
            </el-table-column>
            <el-table-column prop="landCertificateNo" label="国土证号" align="center" show-overflow-tooltip>
            </el-table-column>
            <el-table-column prop="plotRatio" label="容积率" width="80" align="center">
            </el-table-column>
            <el-table-column prop="useAcreage" label="使用权面积(㎡)" width="80" align="center">
            </el-table-column>
            <el-table-column prop="evaluateValue" label="初步定价(万)" width="80" align="center">
            </el-table-column>
            <el-table-column prop="checked" label="审核定价(万)" width="80" align="center">
              <template slot-scope="scope">
                {{ scope.row.checkValue == null ? '待审核' : scope.row.checkValue }}
              </template>
            </el-table-column>
            <el-table-column prop="statementNo" label="价值意见书号" align="center" width="250">
              <template slot-scope="scope">
                <el-tag :type='scope.row.statementNo == null ? "danger" : "success"'>
                  {{ scope.row.statementNo == null ? '未取号' : scope.row.statementNo }}</el-tag>
              </template>``
            </el-table-column>
            <el-table-column prop="reportNo" label="报告号" align="center" width="250">
              <template slot-scope="scope">
                <el-tag :type='scope.row.reportNo == null ? "danger" : "success"'>
                  {{ scope.row.reportNo == null ? '未取号' : scope.row.reportNo }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="letterNo" label="复评函号" align="center" width=250>
              <template slot-scope="scope">
                <el-tag :type='scope.row.letterNo == null ? "danger" : "success"'>
                  {{ scope.row.letterNo == null ? '未取号' : scope.row.letterNo }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" align="center" width="100" fixed="right">
              <template slot-scope="scope">
                <el-button @click="landTargetEdit(scope.row)" type="text" size="small">
                  {{ hanlderType()==='新增'?'查看':hanlderType()}}
                </el-button>
                <el-button v-if="currentNode.nodeCode === 'SPOT_RECONNAISSANCE'" @click="landTargetRemove(scope.row.id)"
                  type="text" size="small">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
        <div class="evaluate-table" v-if="major.businessObjectType === '房地产'">
          <div>
            <el-row>
              <el-col :span="2">
                <div>
                  <el-statistic group-separator="," :precision="4" :value="totalEvaluateValue" title="估价对象总价(万)">
                    <template slot="prefix">
                      <i class="el-icon-coin"></i>
                    </template>
                  </el-statistic>
                </div>
              </el-col>
              <el-col :span="2" style="margin-left:20px">
                <div>
                  <el-statistic group-separator="," :precision="2" :value="totalAcreage" title="估价对象总面积(㎡)">
                    <template slot="prefix">
                      <i class="el-icon-house"></i>
                    </template>
                  </el-statistic>
                </div>
              </el-col>
              <el-col :span="2">
                <el-button v-if="couldAddTarget" type="danger" round
                  @click="openLandDialog(major.businessObjectType)">添加估价对象</el-button>
              </el-col>
              <el-col :span="2">
                <el-button v-if="couldAddTarget" type="warning" :disabled="!takeNumberItems.length > 0" round
                @click="openMergeNumberDialog()">合并到已有号</el-button>
              </el-col>
              <el-col :span="2" v-if="currentNode.nodeCode === 'WRITE_STATEMENT'">
                <el-tooltip v-if="ifTakeNumber" class="item" effect="dark" content="全选取主号，多次取号(主号-子号)" placement="right">
                  <el-button type="success" :disabled="!takeNumberItems.length > 0" round
                    @click="takeStatementNoOrLettertNo('STATEMENT')">取价值意见书号</el-button>
                </el-tooltip>
                <!-- <el-button v-if="ifCreatePorduction" type="danger"  round @click="createProduction(reportNo)">创建产品</el-button> -->
              </el-col>
              <el-col :span="2" v-if="currentNode.nodeCode === 'WRITE_REPORT'">
                <el-tooltip v-if="ifTakeNumber" class="item" effect="dark" content="全选取主号，多次取号(主号-子号)" placement="right">
                  <el-button type="danger" :disabled="!takeNumberItems.length > 0" round
                    @click="takeNumberDialog = true">取报告号</el-button>
                </el-tooltip>
                <!-- <el-button v-if="ifCreatePorduction" type="danger"  round @click="createProduction(reportNo)">创建产品</el-button> -->
              </el-col>
              <el-col :span="2" v-if="currentNode.nodeCode === 'WRITE_LETTER'">
                <el-tooltip v-if="ifTakeNumber" class="item" effect="dark" content="全选取主号，多次取号(主号-子号)" placement="right">
                  <el-button :disabled="!takeNumberItems.length > 0" round
                    @click="takeStatementNoOrLettertNo('LETTER')">取复评函号</el-button>
                </el-tooltip>
                <!-- <el-button v-if="ifCreatePorduction" type="danger"  round @click="createProduction(reportNo)">创建产品</el-button> -->
              </el-col>
            </el-row>
          </div>
          <el-table ref="multipleTable" :data="evaluateLandData" stripe
            :header-row-style="{ color: '#333333', 'font-size': '14px' }" border takeNumberItems
            style="width: 100%; margin-top:20px;font-size: 14px;" @selection-change="handleSelectionChange">
            <el-table-column v-if="ifTakeNumber" type="selection" width="100" :selectable="selectable" align="center">
            </el-table-column>
            <el-table-column type="index" label="序号" width="50" align="center">
            </el-table-column>
            <el-table-column prop="housePurpose" label="用途" width="80" align="center" show-overflow-tooltip>
              <template slot-scope="scope">
                <span v-for="(u, index) in JSON.parse(scope.row.housePurpose)">
                  {{ u }} {{ index == ((JSON.parse(scope.row.housePurpose)).length - 1) ? '' : ',' }}
                </span>
              </template>
            </el-table-column>
            <el-table-column prop="owner" label="权利人" width="120" show-overflow-tooltip align="center">
            </el-table-column>
            <el-table-column prop="located" label="坐落" align="center" show-overflow-tooltip>
            </el-table-column>
            <el-table-column prop="buildAcreage" label="建筑面积(㎡)" width="80" align="center" show-overflow-tooltip>
            </el-table-column>
            <el-table-column prop="landAcreage" label="土地面积(㎡)" width="80" align="center">
            </el-table-column>
            <el-table-column prop="evaluateValue" label="初步定价(万)" width="80" align="center">
            </el-table-column>
            <el-table-column prop="checked" label="审核定价(万)" width="80" align="center">
              <template slot-scope="scope">
                {{ scope.row.checkValue == null ? '待审核' : scope.row.checkValue }}
              </template>
            </el-table-column>
            <el-table-column prop="statementNo" label="价值意见书号" align="center" width="250">
              <template slot-scope="scope">
                <el-tag :type='scope.row.statementNo == null ? "danger" : "success"'>
                  {{ scope.row.statementNo == null ? '未取号' : scope.row.statementNo }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="reportNo" label="报告号" align="center" width="250">
              <template slot-scope="scope">
                <el-tag :type='scope.row.reportNo == null ? "danger" : "success"'>
                  {{ scope.row.reportNo == null ? '未取号' : scope.row.reportNo }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="letterNo" label="复评函号" align="center" width=250>
              <template slot-scope="scope">
                <el-tag :type='scope.row.letterNo == null ? "danger" : "success"'>
                  {{ scope.row.letterNo == null ? '未取号' : scope.row.letterNo }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="100" align="center">
              <template slot-scope="scope">
                <el-button @click="landTargetEdit(scope.row)" type="text" size="small">
                  {{ hanlderType()==='新增'?'查看':hanlderType() }}
                </el-button>
                <el-button v-if="currentNode.nodeCode === 'SPOT_RECONNAISSANCE'" @click="landTargetRemove(scope.row.id)"
                  type="text" size="small">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
        <div class="createMajor-main-container" style="margin-top:50px"
          v-if="currentNode.nodeCode === 'STATEMENT_FEEDBACK' || currentNode.nodeCode === 'QUOTATION_FEEDBACK'">
          <div class="postInfo-container">
            <div>
              <el-divider content-position="left">
                <h3 class="title">
                  <div class="avatar-wrapper icon-title">反馈</div>
                  <div class="icon-info">客户反馈</div>
                </h3>
              </el-divider>
            </div>
          </div>
          <el-form ref="feedbackFrom" :model="feedback" class="form-container" style="margin-top:50px" :rules="rules">
            <el-row>
              <el-col :xs="24" :sm="12" :lg="6" :span="6">
                <el-form-item label="出具产品类型：" prop="production" label-width="160px" class="postInfo-container-item">
                  <el-checkbox-group v-model="feedback.production" style="width:300px">
                    <el-tooltip :disabled="!feedback.production.includes('STATEMENT')" class="item" effect="dark"
                      content="若意见书已送达，无法取消此类型产品！" placement="top-start">
                      <el-checkbox-button label="STATEMENT" name="production">价值意见书</el-checkbox-button>
                    </el-tooltip>
                    <el-checkbox-button label="REPORT" name="production">报告</el-checkbox-button>
                    <el-checkbox-button label="LETTER" name="production">复评函</el-checkbox-button>
                    <el-tooltip class="item" effect="dark" content="只出具价值意见书，不再出具其他产品。" placement="top-start">
                      <el-checkbox-button v-if="currentNode.nodeCode === 'STATEMENT_FEEDBACK'" label="NONE"
                        name="production">不再出具</el-checkbox-button>
                    </el-tooltip>
                  </el-checkbox-group>
                </el-form-item>
              </el-col>
              <el-col :xs="24" :sm="12" :lg="6" :span="6">
                <el-form-item label="打印份数：" prop="printing" label-width="120px" class="postInfo-container-item"
                  style="margin-left:100px">
                  <el-input-number :precision="0" :min="1" :value-on-clear="1" v-model.number="feedback.printing"
                    type="number" class="filter-item" />
                </el-form-item>
              </el-col>
              <el-col :xs="24" :sm="12" :lg="6" :span="6">
                <el-form-item label="打印备注：" label-width="120px" class="postInfo-container-item">
                  <el-select v-model="feedback.printingRemark">
                    <el-option label="一本不装订" value="一本不装订"></el-option>
                    <el-option label="一本带技术" value="一本带技术"></el-option>
                    <el-option label="一本带技术不装订" value="一本带技术不装订"></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :xs="24" :sm="12" :lg="6" :span="6"
                v-if="currentNode.nodeCode === 'QUOTATION_FEEDBACK' || currentNode.nodeCode === 'STATEMENT_FEEDBACK'">
                <div style="text-align:left;margin-right:30px">
                  <el-button type="success" round @click="saveFeedback()" :disabled="validFeed">保存并提交</el-button>
                </div>
              </el-col>
            </el-row>
          </el-form>
        </div>
      </el-tab-pane>
      <el-tab-pane name="producution" class="pane-class" :lazy=true>
        <span slot="label"><i class="el-icon-document-checked"></i> 产品信息</span>
        <el-table :data="productions" stripe ref="pTable" :header-row-style="{ color: '#333333', 'font-size': '16px' }"
          @selection-change="selectioProductionChange" @row-dblclick="expandRow" border
          style="width: 99%; margin-top:20px; font-size: 16px;">
          <el-table-column type="expand" style="background-color: red">
            <template slot-scope="props">
              <el-form label-position="left" :model="props.row" class="form-container"
                :ref="'productionFrom' + props.row.id" :rules="rules">
                <div class="createMajor-main-container" style="margin-top:40px">
                  <div class="postInfo-container">
                    <div>
                      <el-divider content-position="left">
                        <h3 class="title">
                          <div class="avatar-wrapper icon-title">产品</div>
                          <div class="icon-info">产品信息</div>
                        </h3>
                      </el-divider>
                    </div>
                  </div>
                </div>
                <div>
                  <el-row class="row-style">
                    <el-col :xs="24" :sm="12" :lg="12" :span="6">
                      <el-form-item prop="name" :rules="{ required: true, message: '项目不能为空', trigger: 'blur' }"
                        label-width="120px" class="postInfo-container-item" label="项目名称:">
                        <el-input v-model="props.row.name" class="filter-item" />
                      </el-form-item>
                    </el-col>
                    <el-col :xs="24" :sm="12" :lg="12" :span="6">
                      <el-form-item prop="clientName" :rules="{ required: true, message: '委托方不能为空', trigger: 'blur' }"
                        label-width="120px" class="postInfo-container-item" label="委托方：">
                        <el-input v-model="props.row.clientName" class="filter-item" />
                      </el-form-item>
                    </el-col>
                  </el-row>
                  <el-row class="row-style">
                    <el-col :xs="24" :sm="12" :lg="12" :span="6">
                      <el-form-item prop="clientAddress"
                        :rules="{ required: true, message: '委托方地址不能为空', trigger: 'blur' }" label-width="140px"
                        class="postInfo-container-item" label="委托方地址：">
                        <el-input v-model="props.row.clientAddress" class="filter-item" />
                      </el-form-item>
                    </el-col>
                    <el-col :xs="24" :sm="12" :lg="6" :span="6">
                      <el-form-item prop="clientTel" label-width="140px" class="postInfo-container-item" label="委托方电话：">
                        <el-input v-model="props.row.clientTel" class="filter-item" />
                      </el-form-item>
                    </el-col>
                    <el-col :xs="24" :sm="12" :lg="6" :span="6">
                      <el-form-item prop="validateCode" label-width="140px" class="postInfo-container-item"
                        label="打印二维码：">
                        <a @mouseenter="showQR=true">出入库二维码</a> <a @mouseenter="showQR=false" style="margin-left: 5px">验证二维码</a>
                        <div class="qr-code-div">
                          <img v-show="showQR" class="qr-code" :src="props.row.qrCode" alt="出入库二维码" @click="printQR(props.row.qrCode)"/>
                          <img v-show="!showQR" class="qr-code" :src="props.row.validateCode" alt="验证二维码" @click="printQR(props.row.validateCode)"/>
                        </div>
                      </el-form-item>
                    </el-col>
                  </el-row>
                  <el-row class="row-style">
                    <el-col :xs="24" :sm="12" :lg="6" :span="6">
                      <el-form-item prop="evaluateAmount" label-width="120px" class="postInfo-container-item"
                        label="评估总价：">
                        <el-input v-model="props.row.evaluateAmount" style="width:100%" class="filter-item">
                          <template slot="append">
                            <span>万元</span>
                          </template>
                        </el-input>
                      </el-form-item>
                    </el-col>
                    <el-col :xs="24" :sm="12" :lg="6" :span="6">
                      <el-form-item prop="evaluateAcreage" label-width="120px" class="postInfo-container-item"
                        label="评估面积：">
                        <el-input v-model="props.row.evaluateAcreage" style="width:100%" class="filter-item">
                          <template slot="append">
                            <span>平方</span>
                          </template>
                        </el-input>
                      </el-form-item>
                    </el-col>
                    <el-col :xs="24" :sm="12" :lg="6" :span="6">
                      <el-form-item prop="evaluatePrice" label-width="120px" class="postInfo-container-item"
                        label="评估单价：">
                        <el-input v-model="props.row.evaluatePrice" style="width:100%" readonly disabled
                          class="filter-item">
                          <template slot="append">
                            <span>万元</span>
                          </template>
                        </el-input>
                      </el-form-item>
                    </el-col>
                  </el-row>
                  <el-row class="row-style">
                    <el-col :xs="24" :sm="12" :lg="6" :span="6">
                      <el-form-item prop="owner" :rules="{ required: true, message: '产权人不能为空', trigger: 'blur' }"
                        label-width="120px" class="postInfo-container-item" label="产权人：">
                        <el-input v-model="props.row.owner" class="filter-item" />
                      </el-form-item>
                    </el-col>
                    <el-col :xs="24" :sm="12" :lg="6" :span="6">
                      <el-form-item prop="valueTiming" :rules="{ required: true, message: '价值时点不能为空', trigger: 'blur' }"
                        label-width="120px" class="postInfo-container-item" label="价值时点：">
                        <el-date-picker v-model="props.row.valueTiming" type="date" value-format="yyyy-MM-dd" placeholder="选择日期" style="width:100%">
                        </el-date-picker>
                      </el-form-item>
                    </el-col>
                    <el-col :xs="24" :sm="12" :lg="6" :span="6" v-if="props.row.production != 'REPORT'">
                      <el-form-item label="评估目的：" prop="workPurpose"
                        :rules="{ required: true, message: '估价目的不能为空', trigger: 'blur' }" label-width="120px"
                        class="postInfo-container-item">
                        <el-select v-model="props.row.workPurpose" placeholder="请选择" style=" width: 100%" filterable default-first-option allow-create                        >
                          <el-option label="抵押" value="抵押"></el-option>
                          <el-option label="咨询" value="咨询"></el-option>
                          <el-option label="融资" value="融资"></el-option>
                          <el-option label="司法" value="司法"></el-option>
                          <el-option label="可行性研究" value="可行性研究"></el-option>
                          <el-option label="不良资产价值评估" value="不良资产价值评估"></el-option>
                          <el-option label="租金" value="租金"></el-option>
                          <el-option label="其他" value="其他"></el-option>
                        </el-select>
                      </el-form-item>
                    </el-col>
                    <el-col :xs="24" :sm="12" :lg="6" :span="6" v-if="props.row.production != 'STATEMENT'">
                      <el-form-item prop="signatoryId"
                        :rules="{ required: (props.row.production === 'REPORT'), message: '签字人不能为空', trigger: 'blur' }"
                        label-width="120px" class="postInfo-container-item" label="签字人：">
                        <el-select v-model="props.row.signatoryId" placeholder="产品类型为报告，签字人必填" filterable multiple
                          clearable style=" width: 100%" @change="findWorker(props.row)">
                          <el-option v-for="(u, id) in allUsers" :label="u.name" :value="u.id"></el-option>
                        </el-select>
                      </el-form-item>
                    </el-col>
                    <el-col :xs="24" :sm="12" :lg="6" :span="6" v-else>
                      <el-form-item prop="signatoryId" label-width="120px" class="postInfo-container-item">
                      </el-form-item>
                    </el-col>
                  </el-row>
                  <el-row class="row-style">
                    <el-col :xs="24" :sm="12" :lg="6" :span="6">
                      <el-form-item prop="mainWorker" label-width="120px" class="postInfo-container-item"
                        label="第一报告人：">
                        <el-input :value="props.row.mainWorker" style="width:100%" class="filter-item" readonly disabled>
                          <template slot="append">
                            <span>注册号:{{ props.row.mainWorkerNo }}</span>
                          </template>
                        </el-input>
                      </el-form-item>
                    </el-col>
                    <el-col :xs="24" :sm="12" :lg="6" :span="6">
                      <el-form-item prop="minorWorker" label-width="120px" class="postInfo-container-item"
                        label="第二报告人：">
                        <el-input :value="props.row.minorWorker" style="width:100%" class="filter-item" readonly disabled>
                          <template slot="append">
                            <span>注册号:{{ props.row.minorWorkerNo }}</span>
                          </template>
                        </el-input>
                      </el-form-item>
                    </el-col>
                    <el-col :xs="24" :sm="12" :lg="6" :span="6">
                      <el-form-item prop="smallWorker" label-width="120px" class="postInfo-container-item"
                        label="第三报告人：">
                        <el-input :value="props.row.smallWorker" style="width:100%" class="filter-item" readonly disabled>
                          <template slot="append">
                            <span>注册号:{{ props.row.smallWorkerNo }}</span>
                          </template>
                        </el-input>
                      </el-form-item>
                    </el-col>
                  </el-row>
                  <el-row class="row-style" v-if="props.row.production==='REPORT'">
                    <el-col :xs="24" :sm="12" :lg="6" :span="6">
                      <el-form-item prop="workStartDate" :rules="{ required: true, message: '估价作业开始日期不能为空', trigger: 'blur' }"
                        label-width="180px" class="postInfo-container-item" label="估价作业开始日期：">
                        <el-date-picker v-model="props.row.workStartDate" type="date" value-format="yyyy-MM-dd" placeholder="选择日期" style="width:100%">
                        </el-date-picker>
                      </el-form-item>
                    </el-col>
                    <el-col :xs="24" :sm="12" :lg="6" :span="6">
                      <el-form-item prop="workEndDate" :rules="{ required: true, message: '估价作业结束日期不能为空', trigger: 'blur' }"
                        label-width="180px" class="postInfo-container-item" label="估价作业结束日期：">
                        <el-date-picker v-model="props.row.workEndDate" type="date" value-format="yyyy-MM-dd" placeholder="选择日期" style="width:100%">
                        </el-date-picker>
                      </el-form-item>
                    </el-col>
                    <el-col :xs="24" :sm="12" :lg="6" :span="6">
                      <el-form-item label="评估目的：" prop="workPurpose"
                        :rules="{ required: true, message: '估价目的不能为空', trigger: 'blur' }" label-width="120px"
                        class="postInfo-container-item">
                        <el-select v-model="props.row.workPurpose" placeholder="请选择" style=" width: 100%" filterable default-first-option allow-create                        >
                          <el-option label="抵押" value="抵押"></el-option>
                          <el-option label="咨询" value="咨询"></el-option>
                          <el-option label="融资" value="融资"></el-option>
                          <el-option label="司法" value="司法"></el-option>
                          <el-option label="可行性研究" value="可行性研究"></el-option>
                          <el-option label="不良资产价值评估" value="不良资产价值评估"></el-option>
                          <el-option label="租金" value="租金"></el-option>
                          <el-option label="其他" value="其他"></el-option>
                        </el-select>
                      </el-form-item>
                    </el-col>
                  </el-row>
                  <el-row class="row-style" v-if="props.row.production==='REPORT'">
                    <el-col :xs="24" :sm="12" :lg="6" :span="6" v-if="major.businessObjectType === '房地产'">
                      <el-form-item label="是否上报协会：" prop="isSubmit"
                        :rules="{ required: true, message: '是否上报协会不能为空', trigger: 'blur' }" label-width="140px"
                        :class="['postInfo-container-item','reason']">
                        <el-radio v-model="props.row.isSubmit" :label="true" size="medium" @change="saveProduction(props.row)">是</el-radio>
                        <el-radio v-model="props.row.isSubmit" :label="false" size="medium" @change="saveProduction(props.row)">否</el-radio>
                      </el-form-item>
                    </el-col>
                    <el-col :xs="24" :sm="12" :lg="6" :span="6" v-if="major.businessObjectType === '土地'">
                      <el-form-item label="是否备案：" prop="isRecord" 
                        :rules="{ required: true, message: '是否备案不能为空', trigger: 'blur' }" label-width="120px"
                        :class="['postInfo-container-item','reason']">
                        <el-radio v-model="props.row.isRecord" :label="true" size="medium" @change="saveProduction(props.row)">是</el-radio>
                        <el-radio v-model="props.row.isRecord" :label="false" size="medium" @change="saveProduction(props.row)">否</el-radio>
                      </el-form-item>
                    </el-col>
                    <el-col :xs="24" :sm="12" :lg="18" :span="6" v-if="major.businessObjectType === '土地' && !props.row.isRecord">
                      <el-form-item prop="notRecordReason" label-width="120px" :class="['postInfo-container-item','reason']"
                        :rules="{ required: !props.row.isRecord, message: '不备案原因不能为空', trigger: 'blur' }" label="不备案原因:">
                        <el-input v-model.trim="props.row.notRecordReason" class="filter-item" :readonly="props.row.isRecord" :disabled="props.row.isRecord"/>
                      </el-form-item>
                    </el-col>
                  </el-row>
                 
                  <el-row class="row-style">
                    <el-col :xs="24" :sm="12" :lg="24" :span="6">
                      <el-form-item prop="comment" label-width="120px" class="postInfo-container-item" label="特殊情况说明:">
                        <el-input v-model.trim="props.row.comment" class="filter-item" />
                      </el-form-item>
                    </el-col>

                  </el-row>
                  <el-row class="row-style" v-if="doUpload">
                    <el-col :xs="24" :sm="12" :lg="8" :span="6">
                      <el-form-item label="产品初稿：" label-width="120px" class="postInfo-container-item">
                        <el-upload action="/api/upload" :limit="3" :on-success="changeres" :on-exceed="handleExceed"
                          :on-preview="handleAttachmentPreview" :file-list="props.row.filePathList"
                          :before-remove="beforeRemove">
                          <el-button plain type="info" round style="width: 100%">上传<i
                              class="el-icon-upload el-icon--right"></i></el-button>
                        </el-upload>
                      </el-form-item>
                    </el-col>

                    <el-col :xs="24" :sm="12" :lg="8" :span="6">
                      <el-form-item label="产品审核稿：" label-width="140px" class="postInfo-container-item">
                        <el-upload action="/api/upload" :limit="3" :on-success="changeres1" :on-exceed="handleExceed"
                          :on-preview="handleAttachmentPreview" :file-list="props.row.filePathList1"
                          :before-remove="beforeRemove1">
                          <el-button plain type="info" round style="width: 100%">上传<i
                              class="el-icon-upload el-icon--right"></i></el-button>
                        </el-upload>
                      </el-form-item>
                    </el-col>
                  </el-row>
                </div>
                <div>
                </div>
              </el-form>
              <el-form ref="performanceForm" :model="performance" class="form-container" 
              v-if="currentNode.nodeCode ==='CHECK_STATEMENT'
                ||currentNode.nodeCode ==='CHECK_REPORT'
                ||currentNode.nodeCode ==='CHECK_LETTER'
                ||(currentNode.nodeCode ==='RECHECK_STATEMENT' && props.row.workPurpose!='抵押')
                ||(currentNode.nodeCode ==='RECHECK_REPORT' && props.row.workPurpose!='抵押')
                ||(currentNode.nodeCode ==='RECHECK_LETTER' && props.row.workPurpose!='抵押')
                ||(currentNode.nodeCode ==='THIRD_CHECK_STATEMENT' && props.row.workPurpose!='抵押')
                ||(currentNode.nodeCode ==='THIRD_CHECK_REPORT' && props.row.workPurpose!='抵押')
                ||(currentNode.nodeCode ==='THIRD_CHECK_LETTER' && props.row.workPurpose!='抵押')
                ||(currentNode.nodeCode ==='RECHECK_STATEMENT' && props.row.evaluateAmount>5000 && props.row.workPurpose==='抵押')
                ||(currentNode.nodeCode ==='RECHECK_REPORT' && props.row.evaluateAmount>5000 && props.row.workPurpose==='抵押')
                ||(currentNode.nodeCode ==='RECHECK_LETTER' && props.row.evaluateAmount>5000 && props.row.workPurpose==='抵押')
                ||(currentNode.nodeCode ==='THIRD_CHECK_STATEMENT' && props.row.evaluateAmount>15000 && props.row.workPurpose==='抵押')
                ||(currentNode.nodeCode ==='THIRD_CHECK_REPORT' && props.row.evaluateAmount>15000 && props.row.workPurpose==='抵押')
                ||(currentNode.nodeCode ==='THIRD_CHECK_LETTER' && props.row.evaluateAmount>15000 && props.row.workPurpose==='抵押')"
                >
                <el-card shadow="never" v-for="(p, id) in performance.info" :index="id">
                  <el-button type="primary" style="float: right; margin-right: 140px;" round @click="saveQuality(p)">保存审核</el-button>
                  <div class="createMajor-main-container" style="margin-top:40px">
                  <div class="postInfo-container">
                    <div>
                      <el-divider content-position="left">
                        <h3 class="title">
                          <div class="avatar-wrapper icon-title">审核</div>
                          <div class="icon-info">{{p.checkLoop}}</div>
                        </h3>
                      </el-divider>
                    </div>
                  </div>
                </div>
                  <el-row class="row-style">
                    <el-col :xs="24" :sm="12" :lg="24" :span="6">
                      <el-form-item label-width="120px" class="postInfo-container-item" label="一般错误类型:">
                        <el-checkbox-group v-model="p.normalMistakes">
                          <el-checkbox label="委托书界定事项和报告描述不符" class="mistake-option"></el-checkbox>
                          <el-checkbox label="方位图有误" class="mistake-option"></el-checkbox>
                          <el-checkbox label="实景图有误" class="mistake-option"></el-checkbox>
                          <el-checkbox label="测算参数取值不充分" class="mistake-option"></el-checkbox>
                          <el-checkbox label="估价结果确定方式不恰当" class="mistake-option"></el-checkbox>
                          <el-checkbox label="估价方法表述不完整" class="mistake-option"></el-checkbox>
                          <el-checkbox label="实物、权益、区位描述缺项" class="mistake-option"></el-checkbox>
                          <el-checkbox label="价值定义表述不完整" class="mistake-option"></el-checkbox>
                          <el-checkbox label="非重要文字和数据录入错误" class="mistake-option"></el-checkbox>
                          <span style="margin-left: 30px;" v-for="(name,index) in p.xnormalMistakes">
                            <el-checkbox  :label="name" class="mistake-option"></el-checkbox>
                          </span>
                          <el-button type="text" style="margin-left: 50px" @click="addMistake(p,'normalMistakes')">手动添加</el-button>
                        </el-checkbox-group>
                      </el-form-item>
                    </el-col>
                  </el-row>
                  <el-row class="row-style">
                    <el-col :xs="24" :sm="12" :lg="24" :span="6">
                      <el-form-item label-width="120px" class="postInfo-container-item" label="较大错误类型:">
                        <el-checkbox-group v-model="p.hardMistakes">
                          <el-checkbox label="估价结果价格有误" class="mistake-option"></el-checkbox>
                          <el-checkbox label="大小写不一致" class="mistake-option"></el-checkbox>
                          <el-checkbox label="面积错误" class="mistake-option"></el-checkbox>
                          <el-checkbox label="委托方函重要数据出错" class="mistake-option"></el-checkbox>
                          <el-checkbox label="委托人名称出错" class="mistake-option"></el-checkbox>
                          <el-checkbox label="假设限制条件错漏" class="mistake-option"></el-checkbox>
                          <el-checkbox label="价值定义表述错误" class="mistake-option"></el-checkbox>
                          <el-checkbox label="报告章节缺漏" class="mistake-option"></el-checkbox>
                          <el-checkbox label="报告出具日期错误" class="mistake-option"></el-checkbox>
                          <el-checkbox label="报告有效期错误" class="mistake-option"></el-checkbox>
                          <el-checkbox label="重要文字和数据录入错误" class="mistake-option"></el-checkbox>
                          <span style="margin-left: 30px;" v-for="(name,index) in p.xhardMistakes">
                            <el-checkbox  :label="name" class="mistake-option"></el-checkbox>
                          </span>
                          <el-button type="text" style="margin-left: 50px" @click="addMistake(p,'hardMistakes')">手动添加</el-button>
                        </el-checkbox-group>
                      </el-form-item>
                    </el-col>
                  </el-row>
                  <el-row class="row-style">
                    <el-col :xs="24" :sm="12" :lg="24" :span="6">
                      <el-form-item label-width="120px" class="postInfo-container-item" label="重大错误类型:">
                        <el-checkbox-group v-model="p.fatalMistakes">
                          <el-checkbox label="估价对象错误" class="mistake-option"></el-checkbox>
                          <el-checkbox label="估价目的错误" class="mistake-option"></el-checkbox>
                          <el-checkbox label="价值时点设定出错" class="mistake-option"></el-checkbox>
                          <el-checkbox label="登记状况错误" class="mistake-option"></el-checkbox>
                          <el-checkbox label="权利状况错误" class="mistake-option"></el-checkbox>
                          <el-checkbox label="利用状况错误" class="mistake-option"></el-checkbox>
                          <el-checkbox label="技术路线错误" class="mistake-option"></el-checkbox>
                          <el-checkbox label="关键参数取值错误" class="mistake-option"></el-checkbox>
                          <el-checkbox label="编造虚假案例" class="mistake-option"></el-checkbox>
                          <el-checkbox label="编造虚假照片" class="mistake-option"></el-checkbox>
                          <el-checkbox label="估价依据资料严重不全" class="mistake-option"></el-checkbox>
                          <el-checkbox label="评估结果严重失真超30%以上" class="mistake-option"></el-checkbox>
                          <el-checkbox label="关键文字和数据错误" class="mistake-option"></el-checkbox>
                          <span style="margin-left: 30px;" v-for="(name,index) in p.xfatalMistakes">
                            <el-checkbox  :label="name" class="mistake-option"></el-checkbox>
                          </span>
                          <el-button type="text" style="margin-left: 50px" @click="addMistake(p,'fatalMistakes')">手动添加</el-button>
                        </el-checkbox-group>
                      </el-form-item>
                    </el-col>
                  </el-row>
                </el-card>
              </el-form>
            </template>
          </el-table-column>
          <el-table-column
            v-if="(this.currentNode.nodeCode === 'WRITE_STATEMENT' || this.currentNode.nodeCode === 'WRITE_REPORT' || this.currentNode.nodeCode === 'WRITE_LETTER')"
            type="selection" width="100" align="center">
          </el-table-column>
          <el-table-column label="产品号" prop="reportNo" align="center">
            <template slot-scope="props">
              <span style="color:rgb(131,201,94); font-weight:bold">{{ props.row.reportNo }}</span>
            </template>
          </el-table-column>
          <el-table-column label="项目名称" prop="name" align="center" show-overflow-tooltip>
            <template slot-scope="props">
              {{ props.row.name }}
            </template>
          </el-table-column>
          <el-table-column label="产品类型" prop="production" align="center">
            <template slot-scope="props">
              {{ aliasProductionType(props.row.production) }}
            </template>
          </el-table-column>
          <el-table-column label="操作" align="center">
            <template slot-scope="props">
              <el-button round type="danger" @click="saveProduction(props.row)"
                :disabled="!updateProductionNode.includes(currentNode.nodeCode)">更新产品信息</el-button>
              <el-button type="success" round v-if="doWare" @click="openWareHouseDialog(props.row)">{{
                currentNode.nodeName }}</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>
      <el-tab-pane name="finance" class="pane-class" :lazy=true>
        <span slot="label"><i class="el-icon-coin"></i> 收款开票</span>
        <div class="createMajor-main-container">
          <div class="postInfo-container">
            <div>
              <el-divider content-position="left">
                <h3 class="title">
                  <div class="avatar-wrapper icon-title">收开</div>
                  <div class="icon-info">收款开票</div>
                </h3>
              </el-divider>
            </div>
          </div>
        </div>
        <div style="margin-top: 40px;" v-if="productionFinance.length > 0">
          <el-card v-for="(item, index) in productionFinance" shadow="hover">
            <el-form ref="financeForm" :model="item" class="form-container">
              <el-divider content-position="left">
                <span style="font-size:16px;font-weight:bold;">{{ item.productionNo }} {{ item.productionType }}</span>
              </el-divider>
              <el-row class="row-style">
                <el-col :xs="24" :sm="12" :lg="6" :span="6">
                  <el-form-item label-width="120px" class="postInfo-container-item" prop="prodRealAmount" label="产品实收金额:">
                    <el-input :value="item.prodRealAmount" class="filter-item" type="type" readonly disabled>
                      <template slot="append">元</template>
                    </el-input>
                  </el-form-item>
                </el-col>
                <el-col :xs="24" :sm="12" :lg="6" :span="6">
                  <el-form-item label-width="120px" class="postInfo-container-item" prop="orderRealAmount"
                    label="订单实收金额:">
                    <el-input :value="item.orderRealAmount" class="filter-item" type="type" readonly disabled>
                      <template slot="append">元</template>
                    </el-input>
                  </el-form-item>
                </el-col>
                <el-col :xs="24" :sm="12" :lg="6" :span="6">
                  <el-form-item label-width="120px" class="postInfo-container-item" prop="shouldAmount" label="订单应收金额:">
                    <el-input :value="item.shouldAmount" class="filter-item" type="type" readonly disabled>
                      <template slot="append">元</template>
                    </el-input>
                  </el-form-item>
                </el-col>
                <el-col :xs="24" :sm="12" :lg="6" :span="6">
                  <el-form-item label-width="120px" class="postInfo-container-item" prop="standardAmount" label="产品标准收费:">
                    <el-input :value="item.standardAmount" class="filter-item" type="type" readonly disabled>
                      <template slot="append">元</template>
                    </el-input>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row class="row-style">
                <el-col :xs="24" :sm="12" :lg="12" :span="6">
                  <el-form-item label-width="120px" class="postInfo-container-item" prop="invoiceTitle" label="发票抬头:">
                    <el-input :value="item.invoiceTitle" class="filter-item" type="type" readonly disabled />
                  </el-form-item>
                </el-col>
                <el-col :xs="24" :sm="12" :lg="6" :span="6">
                  <el-form-item label-width="120px" class="postInfo-container-item" prop="invoiceType" label="发票类型:">
                    <el-input :value="item.invoiceType" class="filter-item" type="type" readonly disabled />
                  </el-form-item>
                </el-col>
                <el-col :xs="24" :sm="12" :lg="6" :span="6">
                  <el-form-item label-width="120px" class="postInfo-container-item" prop="taxNo" label="税号:">
                    <el-input :value="item.taxNo" class="filter-item" type="type" readonly disabled />
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row class="row-style">
                <el-col :xs="24" :sm="12" :lg="6" :span="6">
                  <el-form-item label-width="120px" class="postInfo-container-item" prop="invoiceAmount" label="开票金额:">
                    <el-input :value="item.invoiceAmount" class="filter-item" type="type" readonly disabled>
                      <template slot="append">元</template>
                    </el-input>
                  </el-form-item>
                </el-col>
                <el-col :xs="24" :sm="12" :lg="6" :span="6">
                  <el-form-item label-width="120px" class="postInfo-container-item" prop="invoiceDate" label="开票日期:">
                    <el-input :value="item.invoiceDate" class="filter-item" type="type" readonly disabled />
                  </el-form-item>
                </el-col>
                <el-col :xs="24" :sm="12" :lg="6" :span="6">
                  <el-form-item label-width="120px" class="postInfo-container-item" prop="invoiceTaxRate" label="税点:">
                    <el-input :value="item.invoiceTaxRate" class="filter-item" type="type" readonly disabled>
                      <template slot="append">%</template>
                    </el-input>
                  </el-form-item>
                </el-col>
                <el-col :xs="24" :sm="12" :lg="6" :span="6">
                  <el-form-item label-width="120px" class="postInfo-container-item" prop="invoiceAmount" label="税额:">
                    <el-input :value="item.invoiceAmount" class="filter-item" type="type" readonly disabled>
                      <template slot="append">元</template>
                    </el-input>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row class="row-style">
                <el-col :xs="24" :sm="12" :lg="6" :span="6">
                  <el-form-item label-width="120px" class="postInfo-container-item" prop="bankName" label="开户银行:">
                    <el-input :value="item.bankName" class="filter-item" type="type" readonly disabled />
                  </el-form-item>
                </el-col>
                <el-col :xs="24" :sm="12" :lg="6" :span="6">
                  <el-form-item label-width="120px" class="postInfo-container-item" prop="invoiceState" label="开票状态:">
                    <el-input :value="item.invoiceState" class="filter-item" type="type" readonly disabled />
                  </el-form-item>
                </el-col>
              </el-row>
            </el-form>
          </el-card>
        </div>
        <div style="margin-top: 40px;height: 30%;text-align: center" v-else>
          <span style="color: darkgrey;">还未设置订单应收款</span>
        </div>
      </el-tab-pane>
      <el-tab-pane name="workflowLog" class="pane-class" :lazy=true>
        <span slot="label"><i class="el-icon-date"></i> 流程日志</span>
        <div class="createMajor-main-container">
          <div class="postInfo-container">
            <div>
              <el-divider content-position="left">
                <h3 class="title">
                  <div class="avatar-wrapper icon-title">日志</div>
                  <div class="icon-info">流程审批日志</div>
                </h3>
              </el-divider>
            </div>
          </div>
        </div>
        <el-table :data="workflowLogs" stripe :header-row-style="{ color: '#333333' }" border
          style="width: 100%; margin-top:20px">
          <el-table-column prop="nodeName" label="节点名称" align="center">
          </el-table-column>
          <el-table-column prop="state" label="处理结果" align="center">
          </el-table-column>
          <el-table-column prop="handlerName" label="处理人" align="center">
          </el-table-column>
          <el-table-column prop="longTime" label="处理时长" align="center">
          </el-table-column>
          <el-table-column prop="created" label="处理时间" align="center">
          </el-table-column>
          <el-table-column prop="comments" label="处理意见" align="center" show-overflow-tooltip>
          </el-table-column>
        </el-table>
      </el-tab-pane>
    </el-tabs>
    <el-dialog :visible.sync="landDialogVisible" custom-class="doWarehouseClass" @closed="closeLandTarget">
      <div>
        <el-form ref="landFrom" :model="landTarget" style="margin-top:20px" :rules="rules">
          <el-divider content-position="left">【土地】基本信息</el-divider>
          <el-row>
            <el-col :xs="24" :sm="12" :lg="12" :span="6">
              <el-form-item label="土地类型：" prop="landType"
                :rules="{ required: true, message: '土地类型不能为空', trigger: 'blur' }" label-width="140px"
                class="postInfo-container-item">
                <el-radio v-model="landTarget.landType" label="国有" size="mini"
                  :readonly="!couldAddTarget"
                  :disabled="!couldAddTarget">国有</el-radio>
                <el-radio v-model="landTarget.landType" label="集体" size="mini"
                  :readonly="!couldAddTarget"
                  :disabled="!couldAddTarget">集体</el-radio>
              </el-form-item>
            </el-col>
            <el-col :xs="24" :sm="12" :lg="12" :span="6">
              <el-form-item label="使用权面积：" prop="useAcreage" label-width="140px" class="postInfo-container-item">
                <el-input v-model="landTarget.useAcreage" class="filter-item"
                  :readonly="!couldAddTarget"
                  :disabled="!couldAddTarget">
                  <template slot="append">平方</template>
                </el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :xs="24" :sm="12" :lg="12" :span="6">
              <el-form-item label="使用权类型：" prop="rightType"
                :rules="{ required: true, message: '使用权类型不能为空', trigger: 'blur' }" label-width="140px"
                class="postInfo-container-item">
                <el-radio v-model="landTarget.rightType" label="出让" size="mini"
                  :readonly="!couldAddTarget"
                  :disabled="!couldAddTarget">出让</el-radio>
                <el-radio v-model="landTarget.rightType" label="划拨" size="mini"
                  @click.native="cleanResidueYears(landTarget)" :readonly="!couldAddTarget"
                  :disabled="!couldAddTarget">划拨</el-radio>
              </el-form-item>
            </el-col>
            <el-col :xs="24" :sm="12" :lg="12" :span="8">
              <el-form-item label="使用权人：" prop="rightToUser"
                :rules="{ required: true, message: '土地使用权人不能为空', trigger: 'blur' }" label-width="120px"
                class="postInfo-container-item">
                <el-input v-model.trim="landTarget.rightToUser" class="filter-item"
                  :readonly="!couldAddTarget"
                  :disabled="!couldAddTarget" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :xs="24" :sm="12" :lg="12" :span="8">
              <el-form-item label="国土证号：" prop="landCertificateNo" label-width="120px" class="postInfo-container-item">
                <el-input v-model.trim="landTarget.landCertificateNo" class="filter-item"
                  :readonly="!couldAddTarget"
                  :disabled="!couldAddTarget" />
              </el-form-item>
            </el-col>
            <el-col :xs="24" :sm="12" :lg="12" :span="8">
              <el-form-item label="坐落：" prop="located" :rules="{ required: true, message: '坐落不能为空', trigger: 'blur' }"
                label-width="120px" class="postInfo-container-item">
                <el-input v-model.trim="landTarget.located" class="filter-item"
                  :readonly="!couldAddTarget"
                  :disabled="!couldAddTarget" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :xs="24" :sm="12" :lg="12" :span="6">
              <el-form-item label="剩余年限：" prop="residueYears" label-width="120px" class="postInfo-container-item">
                <el-input v-model="landTarget.residueYears" class="filter-item"
                  :readonly="!couldAddTarget || landTarget.rightType === '划拨'"
                  :disabled="!couldAddTarget || landTarget.rightType === '划拨'">
                  <template slot="append">年</template>
                </el-input>
              </el-form-item>
            </el-col>
            <el-col :xs="24" :sm="12" :lg="12" :span="6">
              <el-form-item label="容积率：" prop="plotRatio" label-width="120px" class="postInfo-container-item">
                <el-input v-model="landTarget.plotRatio" class="filter-item"
                  :readonly="!couldAddTarget"
                  :disabled="!couldAddTarget">
                </el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :xs="24" :sm="12" :lg="12" :span="6">
              <el-form-item label="用途(地类)：" prop="landUse" label-width="120px"
                :rules="{ required: true, message: '用途(地类)不能为空', trigger: 'blur' }" class="postInfo-container-item">
                <el-input v-model.trim="landTarget.landUse" class="filter-item"
                  :readonly="!couldAddTarget"
                  :disabled="!couldAddTarget" />
              </el-form-item>
            </el-col>
            <el-col :xs="24" :sm="12" :lg="12" :span="8">
              <el-form-item label="估价期日：" prop="evaluateDate"
                :rules="{ required: true, message: '估价期日不能为空', trigger: 'blur' }" label-width="120px"
                class="postInfo-container-item">
                <el-date-picker v-model="landTarget.evaluateDate" type="date" placeholder="选择日期" style="width:100%"
                  :readonly="!couldAddTarget"
                  :disabled="!couldAddTarget">
                </el-date-picker>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :xs="24" :sm="12" :lg="12" :span="8">
              <el-form-item label="负责人：" prop="principalId"
                :rules="{ required: true, message: '项目负责人不能为空', trigger: 'blur' }" label-width="120px"
                class="postInfo-container-item">
                <el-select :value="landTarget.principalId" readonly disabled style="width:100%">
                  <el-option v-for="(p, id) in principals" :label="p.name" :value="p.id"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :xs="24" :sm="12" :lg="12" :span="8">
              <el-form-item label="实勘人：" prop="sites" :rules="{ required: true, message: '实勘人不能为空', trigger: 'blur' }"
                label-width="120px" class="postInfo-container-item">
                <el-select v-model="landTarget.sites" placeholder="请选择" multiple filterable clearable style="width:100%"
                  @change="sitesChange()" :readonly="!couldAddTarget"
                  :disabled="!couldAddTarget">
                  <el-option v-for="(u, id) in allUsers" :label="u.name" :value="u.id" :key="u.id"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :xs="24" :sm="12" :lg="24" :span="12">
              <el-form-item label="备注：" prop="remark" label-width="120px" class="postInfo-container-item">
                <el-input v-model.trim="landTarget.remark" class="filter-item" type="textarea" style="width:100%;"
                  :readonly="!couldAddTarget"
                  :disabled="!couldAddTarget" />
              </el-form-item>
            </el-col>
          </el-row>
          <div
            v-if="currentNode.nodeCode === 'INITIAL_PRICE' || currentNode.nodeCode === 'REVIEW_QUOTATION' || couldAddTarget">
            <el-divider content-position="left">【土地】定价信息</el-divider>
            <el-row>
              <el-col :xs="24" :sm="12" :lg="24" :span="6">
                <el-form-item label="评估方法：" prop="eMethods"
                   label-width="160px"
                  :class="['postInfo-container-item', 'task-class']">
                  <el-checkbox-group v-model="eMethods">
                    <el-checkbox-button label="市场比较法" key="市场比较法">市场比较法</el-checkbox-button>
                    <el-checkbox-button label="收益还原法" key="收益还原法">收益还原法</el-checkbox-button>
                    <el-checkbox-button label="成本逼近法" key="成本逼近法">成本逼近法</el-checkbox-button>
                    <el-checkbox-button label="剩余法" key="剩余法">剩余法</el-checkbox-button>
                    <el-checkbox-button label="基准地价系数修正法" key="基准地价系数修正法">基准地价系数修正法</el-checkbox-button>
                    <el-checkbox-button label="其他" key="其他">其他</el-checkbox-button>
                  </el-checkbox-group>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :xs="24" :sm="12" :lg="12" :span="6" v-if="eMethods.includes('其他')">
                <el-form-item label="其他评估方法：" label-width="160px" prop="definedEvaluationMethod"
                  :class="['postInfo-container-item', 'task-class']"
                  :rules="{ required: true, message: '其他评估方法不能为空', trigger: 'blur' }">
                  <el-input v-model="landTarget.definedEvaluationMethod">
                  </el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :xs="24" :sm="12" :lg="12" :span="6">
                <el-form-item label="初步定价：" prop="evaluateValue" label-width="160px"
                  :class="['postInfo-container-item', 'task-class']">
                  <el-input v-model="landTarget.evaluateValue" 
                  :disabled="!couldAddTarget"
                  :readonly="!couldAddTarget" class="filter-item">
                    <template slot="append">万</template>
                  </el-input>
                </el-form-item>
              </el-col>
              <el-col :xs="24" :sm="12" :lg="12" :span="6" v-if="currentNode.nodeCode === 'REVIEW_QUOTATION'">
                <el-form-item label="审核定价：" prop="checkValue" label-width="160px"
                  :class="['postInfo-container-item', 'task-class']">
                  <span slot="label">审核定价
                    <el-tooltip class="item" effect="dark" content="默认为‘初步定价’可修改。" placement="top-start">
                      <i class="el-icon-question" /></el-tooltip>
                    ：
                  </span>
                  <el-input v-model="landTarget.checkValue" :placeholder="landTarget.evaluateValue" class="filter-item">
                    <template slot="append">万</template>
                  </el-input>
                </el-form-item>
              </el-col>
            </el-row>
          </div>
          <el-row>
            <el-button v-if="couldAddTarget" type="success"
              @click="saveLandTarget(major.businessObjectType)" style="width:100%;">保存</el-button>
            <el-button v-else type="success" @click="doTask(currentNode.nodeCode)" style="width:100%;">保存</el-button>
          </el-row>
        </el-form>
      </div>
    </el-dialog>
    <el-dialog :visible.sync="houseDialogVisible" custom-class="doWarehouseClass" @closed="closeLandTarget">
      <div>
        <el-form ref="houseFrom" :model="landTarget" style="margin-top:20px" :rules="rules">
          <el-divider content-position="left">【房地产】基本信息</el-divider>
          <el-row>
            <el-col :xs="24" :sm="12" :lg="12" :span="6">
              <el-form-item label="房屋用途：" prop="houseToUse" label-width="120px" :class="['postInfo-container-item']">
                <el-checkbox-group v-model="houseToUse">
                  <el-checkbox-button label="住宅" key="住宅">住宅</el-checkbox-button>
                  <el-checkbox-button label="商业" key="商业">商业</el-checkbox-button>
                  <el-checkbox-button label="办公" key="办公">办公</el-checkbox-button>
                  <el-checkbox-button label="工业" key="工业">工业</el-checkbox-button>
                  <el-checkbox-button label="其他" key="其他">其他</el-checkbox-button>
                </el-checkbox-group>
              </el-form-item>
            </el-col>
            <el-col :xs="24" :sm="12" :lg="12" :span="6">
              <el-form-item label="土地使用权人：" prop="rightToUser" label-width="140px" class="postInfo-container-item">
                <el-input v-model.trim="landTarget.rightToUser" :readonly="!couldAddTarget"
                :disabled="!couldAddTarget" class="filter-item" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :xs="24" :sm="12" :lg="12" :span="6">
              <el-form-item label="权利人：" prop="owner" :rules="{ required: true, message: '权利人不能为空', trigger: 'blur' }"
                label-width="120px" class="postInfo-container-item">
                <el-input v-model.trim="landTarget.owner" class="filter-item" :readonly="!couldAddTarget"
                :disabled="!couldAddTarget"/>
              </el-form-item>
            </el-col>
            <el-col :xs="24" :sm="12" :lg="12" :span="6">
              <el-form-item label="权证号：" prop="ownerNo" label-width="120px" class="postInfo-container-item">
                <el-input v-model.trim="landTarget.ownerNo" class="filter-item" :readonly="!couldAddTarget"
                :disabled="!couldAddTarget"/>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :xs="24" :sm="12" :lg="12" :span="6">
              <el-form-item label="土地证号：" prop="landCertificateNo" label-width="120px" class="postInfo-container-item">
                <el-input v-model.trim="landTarget.landCertificateNo" class="filter-item" :readonly="!couldAddTarget"
                :disabled="!couldAddTarget"/>
              </el-form-item>
            </el-col>
            <el-col :xs="24" :sm="12" :lg="12" :span="6">
              <el-form-item label="坐落：" prop="located" label-width="120px"
                :rules="{ required: true, message: '坐落不能为空', trigger: 'blur' }" class="postInfo-container-item">
                <el-input v-model.trim="landTarget.located" class="filter-item" :readonly="!couldAddTarget"
                :disabled="!couldAddTarget"/>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :xs="24" :sm="12" :lg="12" :span="6">
              <el-form-item label="使用权类型：" :rules="{ required: true, message: '使用权类型不能为空', trigger: 'blur' }"
                prop="useRightType" label-width="140px" class="postInfo-container-item">
                <el-input v-model.trim="landTarget.useRightType" class="filter-item" :readonly="!couldAddTarget"
                :disabled="!couldAddTarget"/>
              </el-form-item>
            </el-col>
            <el-col :xs="24" :sm="12" :lg="12" :span="8">
              <el-form-item label="项目名称：" prop="targetName" label-width="120px" class="postInfo-container-item">
                <el-input v-model.trim="landTarget.targetName" :readonly="!couldAddTarget"
                :disabled="!couldAddTarget"/>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :xs="24" :sm="12" :lg="12" :span="8">
              <el-form-item label="竣工年代：" prop="buildDoneYear" label-width="120px" class="postInfo-container-item">
                <el-input v-model.trim="landTarget.buildDoneYear" class="filter-item" :readonly="!couldAddTarget"
                :disabled="!couldAddTarget"/>
              </el-form-item>
            </el-col>
            <el-col :xs="24" :sm="12" :lg="12" :span="8">
              <el-form-item label="建筑面积：" prop="buildAcreage" label-width="120px" class="postInfo-container-item">
                <el-input v-model="landTarget.buildAcreage" style="width:100%" class="filter-item" :readonly="!couldAddTarget"
                :disabled="!couldAddTarget">
                  <template slot="append">平方</template>
                </el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :xs="24" :sm="12" :lg="12" :span="8">
              <el-form-item label="土地面积：" prop="landAcreage" label-width="120px" class="postInfo-container-item">
                <el-input v-model="landTarget.landAcreage" style="width:100%" class="filter-item" :readonly="!couldAddTarget"
                :disabled="!couldAddTarget">
                  <template slot="append">平方</template>
                </el-input>
              </el-form-item>
            </el-col>
            <el-col :xs="24" :sm="12" :lg="12" :span="6">
              <el-form-item label="规划用途：" :rules="{ required: true, message: '规划用途不能为空', trigger: 'blur' }"
                prop="planPurpose" label-width="120px" class="postInfo-container-item">
                <el-input v-model.trim="landTarget.planPurpose" class="filter-item" :readonly="!couldAddTarget"
                :disabled="!couldAddTarget"/>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :xs="24" :sm="12" :lg="12" :span="6">
              <el-form-item label="实勘地址：" :rules="{ required: true, message: '实勘地址不能为空', trigger: 'blur' }"
                prop="siteLocated" label-width="120px" class="postInfo-container-item">
                <el-input v-model.trim="landTarget.siteLocated" class="filter-item" :readonly="!couldAddTarget"
                :disabled="!couldAddTarget"/>
              </el-form-item>
            </el-col>
            <el-col :xs="24" :sm="12" :lg="12" :span="6">
              <el-form-item label="实际用途：" :rules="{ required: true, message: '实际用途不能为空', trigger: 'blur' }"
                prop="realPurpose" label-width="120px" class="postInfo-container-item">
                <el-input v-model.trim="landTarget.realPurpose" class="filter-item" :readonly="!couldAddTarget"
                :disabled="!couldAddTarget"/>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :xs="24" :sm="12" :lg="8" :span="8">
              <el-form-item label="负责人：" prop="principalId"
                :rules="{ required: true, message: '项目负责人不能为空', trigger: 'blur' }" label-width="120px"
                class="postInfo-container-item">
                <el-select :value="landTarget.principalId" readonly disabled>
                  <el-option v-for="(p, id) in principals" :label="p.name" :value="p.id"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :xs="24" :sm="12" :lg="16" :span="8">
              <el-form-item label="实勘人：" prop="sites" :rules="{ required: true, message: '实勘人不能为空', trigger: 'blur' }"
                label-width="120px" class="postInfo-container-item">
                <el-select v-model="landTarget.sites" placeholder="请选择" multiple filterable clearable style="width:100%"
                  @change="sitesChange()" :readonly="!couldAddTarget"
                  :disabled="!couldAddTarget">
                  <el-option v-for="(u, id) in allUsers" :label="u.name" :value="u.id" :key="u.id"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :xs="24" :sm="12" :lg="24" :span="12">
              <el-form-item label="备注：" prop="remark" label-width="120px" class="postInfo-container-item">
                <el-input v-model.trim="landTarget.remark" class="filter-item" type="textarea" :readonly="!couldAddTarget"
                :disabled="!couldAddTarget"/>
              </el-form-item>
            </el-col>
          </el-row>
          <div
            v-if="currentNode.nodeCode === 'INITIAL_PRICE' || currentNode.nodeCode === 'REVIEW_QUOTATION' || couldAddTarget">
            <el-divider content-position="left">【房地产】定价信息</el-divider>
            <el-row>
              <el-col :xs="24" :sm="12" :lg="24" :span="6">
                <el-form-item label="评估方法：" prop="eMethods"
                   label-width="160px"
                  :class="['postInfo-container-item', 'task-class']">
                  <el-checkbox-group v-model="eMethods">
                    <el-checkbox-button label="比较法" key="比较法">比较法</el-checkbox-button>
                    <el-checkbox-button label="收益法" key="收益法">收益法</el-checkbox-button>
                    <el-checkbox-button label="成本法" key="成本法">成本法</el-checkbox-button>
                    <el-checkbox-button label="假设开发法" key="假设开发法">假设开发法</el-checkbox-button>
                    <el-checkbox-button label="基准地价修正法" key="基准地价修正法">基准地价修正法</el-checkbox-button>
                  </el-checkbox-group>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :xs="24" :sm="12" :lg="12" :span="6">
                <el-form-item label="初步定价：" prop="evaluateValue" label-width="160px"
                  :class="['postInfo-container-item', 'task-class']">
                  <el-input v-model="landTarget.evaluateValue" style="width:100%"
                  :disabled="!couldAddTarget"
                  :readonly="!couldAddTarget" 
                     class="filter-item">
                    <template slot="append">万</template>
                  </el-input>
                </el-form-item>
              </el-col>
              <el-col :xs="24" :sm="12" :lg="12" :span="6" v-if="currentNode.nodeCode === 'REVIEW_QUOTATION'">
                <el-form-item label="审核定价：" prop="checkValue" label-width="180px"
                  :class="['postInfo-container-item', 'task-class']">
                  <span slot="label">审核定价
                    <el-tooltip class="item" effect="dark" content="默认为‘初步定价’若价格无误，可直接点击保存按钮，否则可修改。"
                      placement="top-start">
                      <i class="el-icon-question" /></el-tooltip>
                    ：
                  </span>
                  <el-input v-model="landTarget.checkValue" :placeholder="landTarget.evaluateValue" style="width:100%"
                    class="filter-item">
                    <template slot="append">万</template>
                  </el-input>
                </el-form-item>
              </el-col>
            </el-row>
          </div>
          <el-row>
            <el-button v-if="couldAddTarget" type="success"
              @click="saveLandTarget(major.businessObjectType)" style="width:100%;">保存</el-button>
            <el-button v-else type="success" @click="doTask(currentNode.nodeCode)" style="width:100%;">保存</el-button>
          </el-row>
        </el-form>
      </div>
    </el-dialog>
    <el-dialog :visible.sync="takeNumberDialog" width="20%" title="请选择取号年份">
      <el-card shadow="always">
        <div>
          <el-date-picker v-model="year" type="year" placeholder="请选择取号年份" :picker-options="pickerOptions" format="yyyy年"
            value-format="yyyy">
          </el-date-picker>
          <el-button slot="append" @click="takeNumber">确认取号</el-button>
        </div>
      </el-card>
    </el-dialog>
    <el-dialog :visible.sync="warehouseDialog" width="25%" center top="35vh" custom-class="doWarehouseClass"
      @closed="cleanWareHouseProductionType()">
      <ScanEntry ref="scanEntry" @scanEntryFun="handleScanEntry" label="请扫描二维码或输入产品号" />
      <span slot="footer" class="dialog-footer">
        <el-button @click="warehouseDialog = false">取 消</el-button>
        <el-button type="primary" @click="doWareHouse()">确 定</el-button>
      </span>
    </el-dialog>
    <el-dialog :visible.sync="checkFundDialog" width="25%" center top="35vh" custom-class="doWarehouseClass">
      <el-row>
        <el-col :xs="24" :sm="12" :lg="3" :span="6">
          <i style="font-size:30px;" class="el-icon-warning-outline"></i>
        </el-col>
        <el-col :xs="24" :sm="12" :lg="21" :span="6">
          <span>系统检测到意见书</span><span style="color:rgb(131,201,94);font-style: italic;">【{{ statementNo
          }}】</span><span>当前没有对应的实收款记录，</span>
          <span>后期是否需要收款？
            <el-tooltip class="item" effect="dark" content="收款：继续流程需归档；不收款：终止流程无需归档；" placement="bottom-start">
              <span><i style="font-size:15px" class="el-icon-question"></i></span>
            </el-tooltip>
          </span>
        </el-col>
      </el-row>
      <span slot="footer" class="dialog-footer">
        <el-button @click="doSaveFeedBack(false)">不收款</el-button>
        <el-button type="primary" @click="doSaveFeedBack(true)">收款</el-button>
      </span>
    </el-dialog>
    <el-dialog :visible.sync="chooseSecondCheckerDialog" width="25%" center top="35vh" custom-class="doWarehouseClass"
      @closed="cleanSecondCheck()">
      <div>
        <i class="el-icon-warning" style="color:RGB(230,162,60);font-size:22px;margin-right:10px;"></i>
        <span style="position:absolute;top:58px">请确定复审人：</span>
      </div>
      <div style="margin-top:30px;margin-left:20px">
        <el-radio-group v-model="secondCheckUserId">
          <el-radio v-for="(checker, index) in secondCheckUsers" :label="checker.remark">{{ checker.name }}</el-radio>
        </el-radio-group>
      </div>
      <div style="margin-top: 30px;color: red;font-weight: bold">
          <span v-if="this.productions.length>0 && this.productions[0].production==='REPORT' && !this.productions[0].isRecord && this.major.businessObjectType==='土地'">此报告为土地不备案报告，请知晓！</span>
     </div>
        <span slot="footer" class="dialog-footer">
        <el-button @click="chooseSecondCheckerDialog = false">取消</el-button>
        <el-button type="primary" @click="commit('PASS')" :disabled="!secondCheckUserId">确定</el-button>
      </span>
    </el-dialog>
    <el-dialog :visible.sync="mergeNoDialog" width="25%" center top="35vh" custom-class="doWarehouseClass">
      <div>
        <i class="el-icon-warning" style="color:RGB(230,162,60);font-size:22px;margin-right:10px;"></i>
        <span style="position:absolute;top:58px">请选择已取的号码：</span>
      </div>
        <el-radio-group v-model="toMergeNo" size="medium" style="margin-top: 10px;margin-left: 90px" >
          <el-radio border v-for="(no, index) in mergeNos" :label="no" style="margin-bottom: 5px">{{ no }}</el-radio>
        </el-radio-group>
      <span slot="footer" class="dialog-footer">
        <el-button @click="mergeNoDialog = false">取消</el-button>
        <el-button type="primary" @click="doMergeNo()" :disabled="!toMergeNo">确定</el-button>
      </span>
    </el-dialog>
    <el-dialog :visible.sync="notRecordDialog" width="45%" center top="35vh" custom-class="doWarehouseClass" @closed="remindClean()">
      <el-divider content-position="left">
          <h3 >
            <div style="color: red;"><i class="el-icon-warning"></i> 不备案提醒</div>
          </h3>
      </el-divider>
      <el-form ref="notRecordFrom" v-model="notRecordRemind">
        <el-row>
          <el-col :xs="24" :sm="12" :lg="24" :span="6">
            <el-form-item label="报告号：" prop="reportNo" label-width="140px" :class="['postInfo-container-item','reason']">
              <el-input :value="notRecordRemind.reportNo" :class="['filter-item','resaon_input']" disabled readonly style=" width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :xs="24" :sm="12" :lg="24" :span="6">
            <el-form-item label="是否备案：" prop="isRecord" label-width="140px" :class="['postInfo-container-item','reason']">
              <el-input :value="notRecordRemind.isRecord?'备案':'不备案'" :class="['filter-item','resaon_input']"  readonly style=" width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :xs="24" :sm="12" :lg="24" :span="6">
            <el-form-item label="不备案原因：" prop="notRecordReason" label-width="140px" :class="['postInfo-container-item']">
              <el-input  type="textarea" :value="notRecordRemind.notRecordReason" :class="['filter-item']"  readonly style=" width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="notRecordDialog=false">暂不处理</el-button>
        <el-button type="primary" @click="doPassNotRecord()">已知晓</el-button>
      </span>
    </el-dialog>
    <el-dialog :visible.sync="notSubmitDialog" width="45%" center top="35vh" custom-class="doWarehouseClass" @closed="remindClean()">
      <el-divider content-position="left">
          <h3 >
            <div style="color: red;"><i class="el-icon-warning"></i> 上报协会提醒</div>
          </h3>
      </el-divider>
      <el-form ref="notRecordFrom" v-model="notSubmitRemind">
        <el-row>
          <el-col :xs="24" :sm="12" :lg="24" :span="6">
            <el-form-item  label-width="45px" :class="['postInfo-container-item','reason']">
              <el-input :value="notSubmitRemind.reportNo +'  报告需要上报协会！'" :class="['filter-item','resaon_input']" disabled readonly style=" width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="notSubmitDialog=false">暂不处理</el-button>
        <el-button type="primary" @click="doPassNotSubmit()">已知晓</el-button>
      </span>
    </el-dialog>
  </div>
</template>
<script>
import Breadcrumb from '@/components/Breadcrumb'
import YDetailPageLayout from '@/components/YDetailPageLayout/index_detail'
import WorkflowBoard from '@/components/workflowBoard'
import ScanEntry from '@/components/ScanEntry'
import { isNumber, phoneNumber, postiveInteger, isIdNumber } from '@/utils/validate'


export default {
  name: 'majorTodoDetail',
  components: {
    Breadcrumb,
    YDetailPageLayout,
    WorkflowBoard,
    ScanEntry
  },
  watch: {
    $route(to, from) {
      if (to.path === from.path){
        this.nodeBusinessInfo.doWorkflow = this.$route.query.doWorkflow === 'true';
        this.businessMinId = this.$route.query.reportNo;
        this.businessSubId = this.$route.query.statementNo;
        this.majorId = this.$route.query.id;
        this.reportNo = this.$route.query.reportNo;
        this.statementNo = this.$route.query.statementNo;
        this.currentNode.nodeCode = this.$route.query.currentNodeCode;
        this.nodeBusinessInfo.businessId = this.$route.query.id;
        this.nodeBusinessInfo.currentNodeInstanceId = this.$route.query.currentNodeId;
        this.nodeBusinessInfo.businessMinId = this.$route.query.reportNo;
        this.nodeBusinessInfo.businessSubId = this.$route.query.statementNo;
        this.getCurrentNodeInfo();
        this.getMajorDetail();
        this.getAllotDepartment();
        this.getLandTargetList();
        setTimeout(() => {
            this.getProductions(this.majorId, (this.reportNo == null ? this.statementNo : this.reportNo));
          }, 1000)
        this.getWorkflowLogs();
        this.getPerformance(this.majorId, (this.reportNo == null ? this.statementNo : this.reportNo));
        this.getFinanceInvoiceAmountInfo();
      }
    },
    evaluateLandData(newValue, oldValue) {
      this.totalEvaluateValue = 0;
      this.totalAcreage = 0;
      let totalValues = newValue.map(item => item.evaluateValue);
      let totalAcreages = [];
      if (this.major.businessObjectType === '土地') {
        totalAcreages = this.evaluateLandData.map(item => item.useAcreage);
      } else {
        totalAcreages = this.evaluateLandData.map(item => item.buildAcreage);
      }
      for (let i in totalValues) {
        if (totalValues[i] != null && typeof (totalValues[i]) != 'undefined') {
          this.totalEvaluateValue += totalValues[i];
        }
      }
      for (let j in totalAcreages) {
        if (totalAcreages[j] != null && typeof (totalAcreages[j]) != 'undefined') {
          this.totalAcreage += totalAcreages[j];
        }
      }
    },
    feedback: {
      handler(newValue, oldVal) {
        let ps = newValue.production;
        if (this.currentNode.nodeCode === 'QUOTATION_FEEDBACK' || this.currentNode.nodeCode === 'STATEMENT_FEEDBACK') {
          if (ps.length > 1 && this.currentNode.nodeCode != 'STATEMENT_FEEDBACK' && ps.includes("STATEMENT")) {
            this.$message({
              message: '一次只能选择一种产品类型',
              type: 'error'
            });
            this.validFeed = true;
          }
          else if (this.currentNode.nodeCode === 'STATEMENT_FEEDBACK' && !ps.includes("STATEMENT")) {
            this.feedback.production.push("STATEMENT");
            this.validFeed = true;
          }
          else if (ps.includes("REPORT") && ps.includes("LETTER")) {

            this.validFeed = true;
          } else if ((ps.includes("REPORT") && ps.includes("NONE")) || (ps.includes("LETTER") && ps.includes("NONE"))) {
            this.$message({
              message: '产品类型选择有误',
              type: 'error'
            });
            this.validFeed = true;
          }
          else {
            this.validFeed = false;
          }
        }
      },
      deep: true
    },
    productions: {
      handler(newValue, oldVal) {
        for (let d in newValue) {
          newValue[d].evaluatePrice = ((newValue[d].evaluateAmount) / (newValue[d].evaluateAcreage)).toFixed(4);
        }
      },
      deep: true
    }

  },
  computed: {
    couldEdit() {
      let currentNodeCode = this.$route.query.currentNodeCode;
      if (currentNodeCode === 'DEPARTMENT_ALLOCATION') {
        return true;
      }
      return false;
    },
    ifTakeNumber: function () {
      let tk = this.currentNode.tasks.map(item => item.taskType).includes("WRITE");
      let tn = this.evaluateLandData.map(item => item.reportNo).includes(null)
      return (tk && tn)
    },
    checkLoop: function () {
      const curNode = this.currentNode.nodeCode;
      if (curNode === 'CHECK_STATEMENT' || curNode === 'CHECK_REPORT' || curNode === 'CHECK_LETTER' || curNode === 'LAND_REPORT_NOT_RECORD_CHECK') {
        return "初审";
      }
      if (curNode === 'RECHECK_STATEMENT' || curNode === 'RECHECK_REPORT' || curNode === 'RECHECK_LETTER') {
        return "复审";
      }
      if (curNode === 'THIRD_CHECK_STATEMENT' || curNode === 'THIRD_CHECK_REPORT' || curNode === 'THIRD_CHECK_LETTER') {
        return "三审";
      }
      return null;
    },
    activeTagName: {
      get() {
        const orderTag = ['DEPARTMENT_ALLOCATION', 'CONTACT_DEVELOP_PLAN', 'OPENING_ORDER'];
        const evaluate = ['SPOT_RECONNAISSANCE', 'INITIAL_PRICE', 'REVIEW_QUOTATION', 'FORMAL_QUOTATION', 'QUOTATION_FEEDBACK', 'STATEMENT_FEEDBACK', 'WRITE_STATEMENT', 'WRITE_REPORT', 'WRITE_LETTER'];
        const check = ['CHECK_STATEMENT', 'RECHECK_STATEMENT', 'THIRD_CHECK_STATEMENT', 'CHECK_REPORT', 'RECHECK_REPORT', 'THIRD_CHECK_REPORT', 'CHECK_LETTER', 'RECHECK_LETTER', 'THIRD_CHECK_LETTER'];
        const saveCheck = ['CHECK_ARCHIVING', 'RECHECK_ARCHIVING']
        const nodeCode = this.$route.query.currentNodeCode;
        if (orderTag.includes(nodeCode)) {
          return 'order';
        }
        if (evaluate.includes(nodeCode)) {
          return 'evaluate'
        }
        if (check.includes(nodeCode)) {
          return 'producution'
        }
        if (saveCheck.includes(nodeCode)) {
          return 'finance'
        }
        return 'producution'
      },
      set() { }

    },
    couldAddTarget(){
      const curNode = this.currentNode.nodeCode;
      let couldNodes = ['SPOT_RECONNAISSANCE','WRITE_STATEMENT','WRITE_REPORT','WRITE_LETTER']
      return couldNodes.includes(curNode);
    }

  },
  data() {
    return {
      updateProductionNode: ['WRITE_STATEMENT', 'CHECK_STATEMENT', 'RECHECK_STATEMENT', 'THIRD_CHECK_STATEMENT'
        , 'WRITE_REPORT', 'CHECK_REPORT', 'RECHECK_REPORT', 'THIRD_CHECK_REPORT',
        'WRITE_LETTER', 'CHECK_LETTER', 'RECHECK_LETTER', 'THIRD_CHECK_LETTER'],
      rules: {
        buildAcreage: [{ required: true, message: "建筑面积不能为空", trigger: 'blur' }, { validator: isNumber, trigger: 'blur' }],
        landAcreage: [{ required: false, validator: isNumber, trigger: 'blur' }],
        evaluateValue: [{ required: true, message: "评估价格不能为空", trigger: 'blur' }, { validator: isNumber, trigger: 'blur' }],
        checkValue: [{ required: true, message: "审核价格不能为空", trigger: 'blur' }, { validator: isNumber, trigger: 'blur' }],
        production: [{ required: true, message: "产品类型不能为空", trigger: 'blur' }],
        printing: [{ required: true, message: "打印份数不能为空", trigger: 'blur' }],
        useAcreage: [{ required: true, message: "使用权面积不能为空", trigger: 'blur' }, { validator: isNumber, trigger: 'blur' }],
        residueYears: [{ required: false, validator: isNumber, trigger: 'blur' }],
        plotRatio: [{ required: false, validator: isNumber, trigger: 'blur' }],
        clientTel: [{ required: true, message: "委托方电话不能为空", trigger: 'blur' }, { validator: phoneNumber, trigger: 'blur' }],
        evaluateAmount: [{ required: true, message: "评估总价不能为空", trigger: 'blur' }, { validator: isNumber, trigger: 'blur' }],
        evaluateAcreage: [{ required: true, message: "评估面积不能为空", trigger: 'blur' }, { validator: isNumber, trigger: 'blur' }],
      },
      activeName: 'workflowInfo',
      nodeBusinessInfo: {
        currentNodePermission: {
          commit: true,
          reversible: true,
          restartable: true,
          terminable: true,
          skippable: true,
        },
        mainBusiness: "MAJOR_BUSINESS",
        doWorkflow: false,
        businessSubId: null,
        businessMinId: null,
        businessId: null,
        currentNodeInstanceId: null,
        production: [],
        reportNos: [],
        currentNodeInstanceCode: null,
        ifCheckTask: true,
        ifPay: null
      },
      major: {
        id: null,
        name: null,
        financial: false,
        allotType: "轮单",
        clientManagerId: null,
        principalId: null,
        departmentId: null,
        businessObjectType: "土地",
        specialType: null,
        businessGener: null,
        evaluateAim: "抵押",
        entrustAgain: false,
        multipleRemoval: false,
        informalEvaluate: false,
        clienteleType: "企业",
        clienteleId: null,
        clienteleContactId: null,
        dispenseBenefit: false,
        nonnative: false,
        refinance: false,
        loanLimit: null,
        loanPeriod: null,
        loanNature: null,
        loanAim: null,
        loanExpire: false,
        bailor: null,
        bailorAddress: null,
        bailorContactName: null,
        bailorContactTel: null,
        owner: null,
        ownerTel: null,
        urgent: false,
        terminalClienteleId: null,
        terminalClienteleType: "个人",
        terminalClienteleContactId: null,
        remark: null,
        membersName: null,
        clientManager: null,
        members: [],
        printing: null,
        printingRemark: null,
        cmobile: null,
        cdepartment: null,
        cduty: null,
        cclienteleName: null,
        cclienteleSubName: null,
        cclienteleContactName: null,
        tmobile: null,
        tterminalClienteleContactName: null,
        tterminalClienteleName: null
      },
      currentNode: {
        flowId: null,
        pnodeId: null,
        nodeId: null,
        flowCode: null,
        nodeName: "",
        nodeCode: null,
        state: "",
        instanceId: null,
        sequence: null,
        restartable: null,
        reversible: null,
        skippable: null,
        terminable: null,
        tasks: []
      },
      majorId: null,
      allotDepartment: [],
      principals: [],
      allUsers: [],
      taskRecordDTO: {
        recordId: null,
        taskData: {}
      },
      evaluateLandData: [],
      takeNumberItems: [],
      landDialogVisible: false,
      houseDialogVisible: false,
      landTarget: {
        id: null,
        majorId: null,
        targetType: null,
        landType: null,
        useAcreage: null,
        rightToUser: null,
        landCertificateNo: null,
        located: null,
        residueYears: null,
        plotRatio: null,
        landUse: null,
        principalId: null,
        sites: [],
        evaluateDate: null,
        remark: null,
        evaluateValue: null,
        checkValue: null,
        evaluateMethods: [],
        housePurpose: [],
        owner: null,
        ownerNo: null,
        targetName: null,
        buildDoneYear: null,
        buildAcreage: null,
        landAcreage: null,
        planPurpose: null,
        realPurpose: null,
        siteLocated: null,
        useRightType: null,
        definedEvaluationMethod: null
      },
      houseToUse: [],
      eMethods: [],
      totalEvaluateValue: 0,
      totalAcreage: 0,
      feedback: {
        majorId: null,
        production: [],
        printing: null,
        printingRemark: null
      },
      workflowLogs: [],
      takeNumberDialog: false,
      year: '2024',
      productions: [],
      waitingUpload: [],
      performance: {
        productionNo: null,
        productionType: null,
        principal: null,
        productionName: null,
        info: [],
        isRecord:null
      },
      savePerformanceBtn: null,
      saveOrderBtn: null,
      customerCompany: [],
      subCustomerCompany: [],
      customerContract: [],
      tCustomerCompany: [],
      tSubCustomerCompany: [],
      tCustomerContract: [],
      warehouseDialog: false,
      scanEntryData: null,
      wareHouseProductionType: null,
      doWare: false,
      doUpload: false,
      wareHouseproductionId: null,
      validFeed: false,
      pickerOptions: {
        disabledDate: (time) => {
          var currentDate = new Date();
          return time.getTime() > currentDate || time.getTime() < currentDate.setFullYear(currentDate.getFullYear() - 3)
        },
      },
      checkFundDialog: false,
      pNode: null,
      chooseSecondCheckerDialog: false,
      secondCheckUserId: null,
      secondCheckUsers: [],
      productionFinance: [],
      showQR:true,
      toMergeNo:null,
      mergeNos:[],
      mergeNoDialog:false,
      normalMistakes:[],
      xNormalMistakes:[],
      fatalMistakes:[],
      xFatalMistakes:[],
      xHardMistakes:[],
      hardMistakes:[],
      notRecordDialog:false,
      passNotRecord:false,
      notRecordRemind:{
        reportNo:null,
        isRecord:null,
        notRecordReason:null
      },
      notSubmitDialog:false,
      notSubmitRemind:{
        reportNo:null,
        isSubmit:null,
      },
      passNotSubmit:false
    }
  },
  created() {
    this.nodeBusinessInfo.doWorkflow = this.$route.query.doWorkflow === 'true';
    this.businessMinId = this.$route.query.reportNo;
    this.businessSubId = this.$route.query.statementNo;
    this.majorId = this.$route.query.id;
    this.reportNo = this.$route.query.reportNo;
    this.statementNo = this.$route.query.statementNo;
    this.currentNode.nodeCode = this.$route.query.currentNodeCode;
    this.nodeBusinessInfo.businessId = this.$route.query.id;
    this.nodeBusinessInfo.currentNodeInstanceId = this.$route.query.currentNodeId;
    this.nodeBusinessInfo.businessMinId = this.$route.query.reportNo;
    this.nodeBusinessInfo.businessSubId = this.$route.query.statementNo;
    this.getCurrentNodeInfo();
    this.getMajorDetail();
    if (this.activeTagName === 'order') {
      this.getAllotDepartment();
    }
    if (this.activeTagName === 'evaluate') {
      if (this.major.businessObjectType === '土地') {
        this.getLandTargetList();
      }
      setTimeout(() => {
        this.getProductions(this.majorId, (this.reportNo == null ? this.statementNo : this.reportNo));
      }, 1000)
    }
    if (this.activeTagName === 'workflowLog') {
      this.getWorkflowLogs();
    }
    if (this.activeTagName === 'producution') {
      this.getProductions(this.majorId, (this.reportNo == null ? this.statementNo : this.reportNo));
    }
    if (this.activeTagName === 'quality') {
      if (this.productions.length == 0) {
        this.getProductions(this.majorId, (this.reportNo == null ? this.statementNo : this.reportNo));
      }
      
    }
    if (this.activeTagName === 'finance' && this.productionFinance.length == 0) {
      this.getFinanceInvoiceAmountInfo();
    }
    this.getPerformance(this.majorId, (this.reportNo == null ? this.statementNo : this.reportNo));
  },
  methods: {
    handleClick(tab) {
      if (tab.name === 'order' && this.allotDepartment.length == 0) {
        this.getAllotDepartment();
      }
      if (tab.name === 'evaluate') {
        if (this.evaluateLandData.length == 0) {
          this.getLandTargetList();
        }
      }
      if (tab.name === 'workflowLog') {
        if (this.workflowLogs.length == 0) {
          this.getWorkflowLogs();
        }
      }
      if (tab.name === 'producution') {
        if (this.productions.length == 0) {
          this.getProductions(this.majorId, (this.reportNo == null ? this.statementNo : this.reportNo));
        }
      }
      if (tab.name === 'quality') {
        if (this.productions.length == 0) {
          this.getProductions(this.majorId, (this.reportNo == null ? this.statementNo : this.reportNo));
        }
        this.getPerformance(this.majorId, (this.reportNo == null ? this.statementNo : this.reportNo));
      }
      if (tab.name === 'finance' && this.productionFinance.length == 0) {
        this.getFinanceInvoiceAmountInfo();
      }
    },

    getMajorDetail() {
      if (this.majorId) {
        let majorId = this.majorId;
        let reportNo = this.statementNo;
        this.$api.major.detail(this.majorId).then(res => {
          if (res.code === 200) {
            this.major = res.data;
            this.major.membersId = JSON.parse(this.major.members);
            if (this.major.feedbackInfo) {
              let feedbacks = JSON.parse(this.major.feedbackInfo);
              if (reportNo) {
                this.feedback = feedbacks.filter(function (e, index, array) {
                  return e.reportNo === reportNo;
                })[0]
                if (!this.feedback) {
                  this.feedback = feedbacks.filter(function (e, index, array) {
                    return e.majorId == majorId;
                  })[0];
                }
              } else {
                this.feedback = feedbacks.filter(function (e, index, array) {
                  return e.majorId == majorId;
                })[0];
              }
              this.nodeBusinessInfo.production = this.feedback.production;
              this.nodeBusinessInfo.ifPay = this.feedback.isPay;
            }
            this.getPrincipalByAllotDepartment();
            this.getAllUser();
            this.$refs.board.getInstanceQueue();
          }
        })
      }

    },
    handleChange() {
      let isPay = this.feedback.isPay;
      if (this.currentNode.nodeCode === 'STATEMENT_FEEDBACK' && isPay != undefined && !isPay) {
        this.nodeBusinessInfo.currentNodePermission.terminable = true;
        this.nodeBusinessInfo.currentNodePermission.commit = false;
        this.$forceUpdate();
      }
    },
    getCurrentNodeInfo() {
      if (this.majorId) {
        this.$api.workNodeInstance.currentNode({ "mainBusiness": "MAJOR_BUSINESS", "businessId": this.majorId, "businessSubId": this.businessSubId, "businessMinId": this.businessMinId }).then(res => {
          if (res.code === 200) {
            if (res.data) {
              this.currentNode = res.data;
              this.nodeBusinessInfo.currentInstanceNodeId = res.data.instanceId;
              this.nodeBusinessInfo.currentNodeInstanceCode = res.data.nodeCode;
              this.nodeBusinessInfo.currentNodePermission.restartable = res.data.restartable;
              this.nodeBusinessInfo.currentNodePermission.reversible = res.data.reversible;
              this.nodeBusinessInfo.currentNodePermission.skippable = res.data.skippable;
              this.nodeBusinessInfo.currentNodePermission.terminable = res.data.terminable;
            }
          }
        })
      }
    },
    getAllotDepartment() {
      this.$api.department.allot('MAJOR_BUSINESS').then(res => {
        if (res.code === 200) {
          this.allotDepartment = res.data;
        }
      })
    },
    getPrincipalByAllotDepartment() {
      let departmentId = this.major.departmentId;
      if (departmentId) {
        this.$api.user.usersByDepartmentId(departmentId).then(res => {
          if (res.code === 200) {
            this.principals = res.data;
          }
        })
      }
    },
    getAllUser() {
      this.$api.user.simpleAll().then(res => {
        if (res.code === 200) {
          let principals = this.principals;
          let all = res.data;
          let arr = principals.concat(all);
          let obj = {}
          arr = arr.reduce((pre, next) => {
            obj[next.id] ? null : (obj[next.id] = true && pre.push(next))
            return pre;
          }, [])
          this.allUsers = arr;
        }
      })
    },
    handleCreate() {
      this.$refs.majorForm.validate(valid => {
        if (valid) {
          if (this.major.id) {
            this.taskRecordDTO.recordId = this.currentNode.tasks[0].recordId;
            this.taskRecordDTO.taskData = this.major;
            this.$api.major.allocation(this.taskRecordDTO).then(res => {
              if (res.code === 200 && res.data) {
                this.$notify({
                  title: '成功',
                  message: '保存成功,请提交节点继续流程。',
                  type: 'success',
                  duration: 1000,
                  offset: 100
                });
              }
            })
          }
        }
      })
    },
    handleSelectionChange(val) {
      let pickeds = [];
      for (let i in val) {
        let picked = new Object();
        picked.id = val[i].id;
        picked.amount = val[i].checkValue;
        picked.name = val[i].statementNo;
        if (this.major.businessObjectType === '土地') {
          picked.acreage = val[i].useAcreage;
        } else {
          picked.acreage = val[i].buildAcreage;
        }
        pickeds.push(picked);
      }
      this.takeNumberItems = pickeds;

    },
    getLandTargetList() {
      if (this.majorId) {
        const obj = new Object();
        obj.majorId = this.majorId;
        obj.statementNo = this.statementNo;
        if (this.feedback.production.includes("REPORT")) {
          obj.reportNo = this.reportNo;
        } else {
          obj.letterNo = this.reportNo;
        }
        this.$api.majorTarget.list(obj).then(res => {
          if (res.code === 200) {
            this.evaluateLandData = res.data;
            let totalAcreages = [];
            let totalValues = this.evaluateLandData.map(item => item.evaluateValue);
            if (this.major.businessObjectType === '土地') {
              totalAcreages = this.evaluateLandData.map(item => item.useAcreage);
            } else {
              totalAcreages = this.evaluateLandData.map(item => item.buildAcreage);
            }
            for (let i in totalValues) {
              if (totalValues[i] != null && typeof (totalValues[i]) != 'undefined') {
                this.totalEvaluateValue += totalValues[i];
              }
            }
            for (let j in totalAcreages) {
              if (totalAcreages[j] != null && typeof (totalAcreages[j]) != 'undefined') {
                this.totalAcreage += totalAcreages[j];
              }
            }
          }
        })
      }
    },
    openLandDialog(targetType) {
      if (targetType === '土地') {
        this.landDialogVisible = true;
      } else {
        this.houseDialogVisible = true;
      }
      this.landTarget.majorId = this.major.id;
      this.landTarget.principalId = this.major.principalId;
      this.landTarget.sites = this.major.membersId;
      this.landTarget.owner = this.major.owner;
      this.eMethods = [];
    },
    saveLandTarget(targetType) {
      let isOk = true;
      this.landTarget.eMethods = this.eMethods;
    
      if (targetType === '土地') {
        this.$refs.landFrom.validate(valid => {
          if (!valid) {
            isOk = false;
          }
          this.landTarget.targetType = '土地';
        })
      }
      if (targetType === '房地产') {
        this.$refs.houseFrom.validate(valid => {
          if (!valid) {
            isOk = false;
          }
          this.landTarget.targetType = '房地产';
        })
      }
      if (isOk) {
        if (this.landTarget.majorId) {
          if (!this.landTarget.id) {
            this.landTarget.housePurpose = JSON.stringify(this.houseToUse);
            this.landTarget.evaluateMethods = JSON.stringify(this.eMethods);
            this.taskRecordDTO.recordId = this.currentNode.tasks[0].recordId;
            this.taskRecordDTO.taskData = this.landTarget;
            this.$api.majorTarget.add(this.taskRecordDTO).then(res => {
              if (res.code === 200 && res.data) {
                this.$notify({
                  title: '成功',
                  message: '若还有估价对象请继续添加,没有则请提交节点继续流程。',
                  type: 'success',
                  duration: 2000,
                  offset: 100
                });
                this.getLandTargetList();
                this.landDialogVisible = false;
                this.houseDialogVisible = false;
                this.taskRecordDTO = {};
              }
              else {
                this.$notify({
                  title: '失败',
                  message: '估价对象添加失败。',
                  type: 'error',
                  duration: 1000,
                  offset: 100
                });
              }
            })
          }
          else {
            this.editLandTarget();
          }
        } else {
          this.$notify({
            title: '提示',
            message: '项目id不存在，请联系技术管理员。',
            type: 'info',
            duration: 1000,
            offset: 100
          });
        }
      }
    },
    landTargetEdit(row) {
      if (row.targetType === '土地') {
        this.landDialogVisible = true;
      } else {
        this.houseDialogVisible = true;
      }
      this.landTarget = Object.assign({}, row);
      this.landTarget.sites = JSON.parse(this.landTarget.siteIds);
      this.eMethods = [];
      if (JSON.parse(this.landTarget.evaluateMethods)) {
        this.eMethods = JSON.parse(this.landTarget.evaluateMethods);
      }
      if (this.landTarget.housePurpose) {
        this.houseToUse = JSON.parse(this.landTarget.housePurpose);
      }
    },
    landTargetRemove(id) {
      this.taskRecordDTO.recordId = this.currentNode.tasks[0].recordId;
      this.taskRecordDTO.taskData = id;
      if (id) {
        this.$confirm('确认删除当前估价对象吗', '警告', {
          confirmButtonText: '确认',
          cancelButtonText: '取消',
          type: 'warning',
        }).then(async () =>
          this.$api.majorTarget.delete(this.taskRecordDTO)
            .then(res => {
              if (res.code === 200 && res.data) {
                this.$notify({
                  title: '提示',
                  message: '估价对象删除成功',
                  type: 'success',
                  duration: 1000,
                  offset: 100
                });
                this.getLandTargetList();
              } else {
                this.$notify({
                  title: '失败',
                  message: '估价对象删除失败。',
                  type: 'error',
                  duration: 1000,
                  offset: 100
                });
              }
            })
        )
      }
    },
    doTask(currentNodeCode) {
      if (currentNodeCode === 'INITIAL_PRICE') {
        let values = this.evaluateLandData.map(item => item.evaluateValue)
        if (this.eMethods.length == 0) {
          this.$notify({
            title: '失败',
            message: '评估方法不能为空',
            type: 'info',
            duration: 1000,
            offset: 100
          });
          return;
        }
        if (!this.landTarget.definedEvaluationMethod && this.eMethods.includes('其他')) {
          console.log(this.landTarget.definedEvaluationMethod);
          this.$notify({
            title: '失败',
            message: '请填写其他评估方法',
            type: 'info',
            duration: 1000,
            offset: 100
          });
          return;
        }
        if (this.major.businessObjectType === '房地产' && this.houseToUse.length == 0) {
          this.$notify({
            title: '失败',
            message: '房屋用途不能为空',
            type: 'info',
            duration: 1000,
            offset: 100
          });
          return;
        }
        this.landTarget.evaluateMethods = JSON.stringify(this.eMethods);
        this.landTarget.housePurpose = JSON.stringify(this.houseToUse);
        if (values.includes(null)) {
          this.editLandTarget();
          return;
        } else {
          this.doTaskLandCommon();
          return;
        }
      }
      if (currentNodeCode === 'REVIEW_QUOTATION') {
        let checkeds = this.evaluateLandData.map(item => item.checked);
        if (this.eMethods.length == 0) {
          this.$notify({
            title: '失败',
            message: '评估方法不能为空',
            type: 'info',
            duration: 1000,
            offset: 100
          });
          return;
        }
        if (!this.landTarget.definedEvaluationMethod && this.eMethods.includes('其他')) {
          this.$notify({
            title: '失败',
            message: '请填写其他评估方法',
            type: 'info',
            duration: 1000,
            offset: 100
          });
          return;
        }
        this.landTarget.evaluateMethods = JSON.stringify(this.eMethods);
        if (checkeds.includes(null)) {
          this.editLandTarget();
          return;
        } else {
          this.doTaskLandCommon();
          return;
        }

      }
    },
    editLandTarget() {
      if (this.currentNode.nodeCode === 'REVIEW_QUOTATION' && !this.landTarget.checkValue) {
        this.landTarget.checkValue = this.landTarget.evaluateValue;
      }
      this.landTarget.housePurpose = JSON.stringify(this.houseToUse);
      this.landTarget.evaluateMethods = JSON.stringify(this.eMethods);
      this.taskRecordDTO.recordId = this.currentNode.tasks[0].recordId;
      this.taskRecordDTO.taskData = this.landTarget;
      this.$api.majorTarget.edit(this.taskRecordDTO).then(res => {
        if (res.code === 200 && res.data) {
          this.$notify({
            title: '成功',
            message: '估价对象修改编辑成功。',
            type: 'success',
            duration: 1000,
            offset: 100
          });
          this.getLandTargetList();
          this.landDialogVisible = false;
          this.houseDialogVisible = false;
          this.taskRecordDTO = {};
        }
        else {
          this.$notify({
            title: '失败',
            message: '估价对象编辑失败。',
            type: 'error',
            duration: 1000,
            offset: 100
          });
        }
      })
    },
    hanlderType() {
      if (this.currentNode) {
        if (this.currentNode.nodeCode === 'SPOT_RECONNAISSANCE') {
          return "编辑";
        }
        if (this.currentNode.nodeCode === 'INITIAL_PRICE') {
          return "定价";
        }
        if (this.currentNode.nodeCode === 'REVIEW_QUOTATION') {
          return "审核定价";
        }
        if (this.couldAddTarget){
          return "新增";
        }
        return "查看";

      }
    },
    doTaskLandCommon() {
      this.taskRecordDTO.recordId = this.currentNode.tasks[0].recordId;
      this.taskRecordDTO.taskData = this.landTarget;
      this.$api.majorTarget.doTask(this.taskRecordDTO).then(res => {
        if (res.code === 200 && res.data) {
          this.$notify({
            title: '成功',
            message: '估价对象操作成功，请提交节点继续流程。',
            type: 'success',
            duration: 2000,
            offset: 100
          });
          this.getLandTargetList();
          this.landDialogVisible = false;
          this.houseDialogVisible = false;
          this.taskRecordDTO = {};
        }
        else {
          this.$notify({
            title: '失败',
            message: '估价对象操作失败。',
            type: 'error',
            duration: 1000,
            offset: 100
          });
        }
      })
    },
    saveFeedback() {
      let majorId = this.major.id;
      if (majorId) {
        this.feedback.majorId = majorId;
        this.$refs.feedbackFrom.validate(valid => {
          if (valid) {
            //如果已取号，并且在价值意见书送达反馈节点。 保存 的意见反馈产品信息，需另存
            if (this.currentNode.nodeCode === 'STATEMENT_FEEDBACK') {
              this.feedback.reportNo = this.statementNo;
              if (this.feedback.production.length === 1) {
                this.$message({
                  message: '警告，请更新客户反馈信息（后续需出具的产品类型。若不出具，请选择“不再出具”）',
                  type: 'error'
                });
                return;
              }
              //如果选择不再出具，则检查该产品是否认领实收款
              if (this.feedback.production.includes("NONE")) {
                const pro = new Object();
                pro.businessId = majorId;
                pro.productionNo = this.feedback.reportNo;
                pro.businessType = "MAJOR_BUSINESS";
                pro.productionType = "STATEMENT"
                this.$api.productionFund.checkFund(pro).then(res => {
                  if (res.code === 200 && res.data.realAmount === 0) {
                    this.checkFundDialog = true;
                  } else {
                    this.doSaveFeedBack(true);
                  }
                })
              } else {
                this.doSaveFeedBack();
              }
            } else {
              this.doSaveFeedBack();
            }
          }
        })
      }
    },

    doSaveFeedBack(isPay) {
      this.feedback.isPay = isPay;
      if (isPay === undefined) {
        this.nodeBusinessInfo.currentNodePermission.terminable = false;
        this.nodeBusinessInfo.currentNodePermission.commit = true;
      }
      if (isPay != undefined && !isPay) {
        this.nodeBusinessInfo.currentNodePermission.terminable = true;
        this.nodeBusinessInfo.currentNodePermission.commit = false;
      }
      if (isPay != undefined && isPay) {
        this.nodeBusinessInfo.currentNodePermission.terminable = false;
        this.nodeBusinessInfo.currentNodePermission.commit = true;
      }
      this.taskRecordDTO.recordId = this.currentNode.tasks[0].recordId;
      this.taskRecordDTO.taskData = this.feedback;
      this.$api.major.feedback(this.taskRecordDTO).then(res => {
        if (res.code === 200 && res.data) {
          this.$notify({
            title: '成功',
            message: '保存客户反馈信息成功，请提交节点继续流程',
            type: 'success',
            duration: 1000,
            offset: 100
          });
          this.checkFundDialog = false;
          this.nodeBusinessInfo.production = this.feedback.production;
          this.nodeBusinessInfo.ifPay = this.feedback.isPay;
          this.$refs.board.getInstanceQueue();
          this.taskRecordDTO = {};
          this.$refs.board.commit('PASS');
        } else {
          this.$notify({
            title: '失败',
            message: '保存客户反馈信息失败，请联系系统管理员',
            type: 'error',
            duration: 1000,
            offset: 100
          });
        }
      })
    },
    getWorkflowLogs() {
      let param = {
        businessId: this.majorId,
        businessSubId: this.statementNo,
        businessMinId: this.reportNo,
        businessType: 'MAJOR_BUSINESS'
      }
      this.$api.workflowLog.list(param).then(res => {
        if (res.code === 200) {
          this.workflowLogs = res.data;
        }
      })
    },
    takeNumber() {
      if (!this.year) {
        this.$notify({
          title: '警告',
          message: '取号年份未选择，请选择再取号。',
          type: 'warning',
          duration: 1000,
          offset: 100
        });
        return;
      }
      let takeNumberDTO = {
        businessId: this.majorId,
        businessType: "MAJOR_BUSINESS",
        takeType: this.major.businessObjectType === '土地' ? 'LAND_NUMBER' : 'REALTY_NUMBER',
        targets: this.takeNumberItems,
        year: this.year,
        productionType: "REPORT",
        statementNo:this.statementNo

      }
      this.$api.majorTarget.takeNumber(takeNumberDTO).then(res => {
        if (res.code === 200 && res.data != null) {
          this.$notify({
            title: '成功',
            message: '取号成功。',
            type: 'success',
            duration: 1000,
            offset: 100
          });
          this.getLandTargetList();
          this.createProduction(res.data);
          this.taskRecordDTO = {};
          this.takeNumberDialog = false;
        } else {
          this.$notify({
            title: '失败',
            message: '取号失败。',
            type: 'error',
            duration: 1000,
            offset: 100
          });
        }
      })
    },
    createProduction(productionNo) {
      let productionType = 'STATEMENT';
      if (this.currentNode.nodeCode === 'WRITE_REPORT') {
        productionType = 'REPORT';
      }
      if (this.currentNode.nodeCode === 'WRITE_LETTER') {
        productionType = 'LETTER';
      }

      if (productionNo) {
        let totalAcreage = 0;
        let totalAmount = 0;
        this.takeNumberItems.map(item => item.acreage).map(function (e) {
          totalAcreage += e;
        });
        this.takeNumberItems.map(item => item.amount).map(function (e) {
          totalAmount += e;
        })
        let production = new Object();
        production.majorId = this.majorId;
        production.reportNo = productionNo;
        production.production = productionType;
        production.evaluateAcreage = totalAcreage;
        production.evaluateAmount = totalAmount * 10000;
        production.clientName = this.major.bailor;
        production.clientTel = this.major.bailorContactTel;
        production.clientAddress = this.major.bailorAddress;
        production.owner = this.major.owner;
        production.name = this.major.name;
        production.statementNo = this.takeNumberItems[0].name;
        if (totalAcreage != 0) {
          production.evaluatePrice = (totalAmount * 10000 / totalAcreage).toFixed(4);
        } else {
          production.evaluatePrice = 0;
        }
        this.$api.majorProduction.add(production).then(res => {
          if (res.code === 200 && res.data) {
            this.$notify({
              title: '成功',
              message: '产品创建成功，请完善产品信息。',
              type: 'success',
              duration: 2000,
              offset: 100
            });
            this.takeNumberItems = []
            this.getProductions(this.majorId, null);
            this.nodeBusinessInfo.reportNos.push(productionNo);
          } else {
            this.$notify({
              title: '失败',
              message: '产品创建失败，请联系系统管理员。',
              type: 'error',
              duration: 2000,
              offset: 100
            });
          }
        })
      } else {
        console.log("reportNo is null")
      }
    },
    getProductions(majorId, reportNo) {
      let productionType = 'STATEMENT';
      let currentNodeCode = this.currentNode.nodeCode;
      if (currentNodeCode.includes("REPORT")) {
        productionType = 'REPORT';
      }
      if (currentNodeCode.includes("LETTER")) {
        productionType = 'LETTER';
      }
      if (currentNodeCode === 'CHECK_ARCHIVING' || currentNodeCode === 'RECHECK_ARCHIVING') {
        let productionTypes = this.feedback.production.filter(function (e) {
          return e != 'STATEMENT';
        })
        productionType = productionTypes[0];
      }
      let afterProduction = null;
      if (this.feedback.production.length > 1 && (currentNodeCode === 'WRITE_REPORT' || currentNodeCode === 'WRITE_LETTER')) {
        afterProduction = this.feedback.production[(this.feedback.production.length) - 1]
      }
      if (majorId) {
        let pro = {
          majorId: majorId,
          reportNo: reportNo,
          sequence: this.currentNode.sequence,
          production: productionType,
          afterProduction: afterProduction
        }
        this.$api.majorProduction.list(pro).then(res => {
          if (res.code === 200) {
            this.productions = res.data;
            if (this.productions.length > 0) {
              this.nodeBusinessInfo.doSecondCheck = this.productions[0].secondCheck;
              this.nodeBusinessInfo.doThirdCheck = this.productions[0].thirdCheck;
            }

            //回显上传的文档
            for (let p in this.productions) {
              let d = JSON.parse(this.productions[p].filePath);
              let c = JSON.parse(this.productions[p].checkedFilePath);
              for (let q in d) {
                this.productions[p].filePathList.push({ name: d[q].name, url: d[q].url, index: p });
              }
              for (let cc in c) {
                this.productions[p].filePathList1.push({ name: c[cc].name, url: c[cc].url, index: p });
              }
            }
            //显示出入库操作按钮
            let wareHoseNodes = ['STATEMENT_IN', 'STATEMENT_OUT', 'REPORT_IN', 'REPORT_OUT', 'LETTER_IN', 'LETTER_OUT']
            if (wareHoseNodes.includes(currentNodeCode)) {
              this.doWare = true;
            }
            //显示上传附件按钮
            let writeNodes = ['WRITE_STATEMENT', 'WRITE_REPORT', 'WRITE_LETTER', 'CHECK_STATEMENT', 'CHECK_REPORT', 'CHECK_LETTER'
              , 'RECHECK_STATEMENT', 'THIRD_CHECK_STATEMENT', 'RECHECK_REPORT', 'THIRD_CHECK_REPORT', 'RECHECK_LETTER', 'THIRD_CHECK_LETTER']
            if (writeNodes.includes(currentNodeCode)) {
              this.doUpload = true;
            }
            //默认展开第一行
            setTimeout(() => {
              this.defaultExpandOpen();
            }, 500);
          }
        })
      }
    },
    saveProduction(production) {
      //判断当前节点，确定调用后端接口
      let nodeCode = this.currentNode.nodeCode;
      
      switch (nodeCode) {
        case 'WRITE_STATEMENT':
        case 'WRITE_REPORT':
        case 'WRITE_LETTER':
        case 'CHECK_STATEMENT':
        case 'CHECK_REPORT':
        case 'CHECK_LETTER':
        case 'RECHECK_STATEMENT':
        case 'RECHECK_REPORT':
        case 'RECHECK_LETTER':
        case 'THIRD_CHECK_STATEMENT':
        case 'THIRD_CHECK_REPORT':
        case 'THIRD_CHECK_LETTER':
          this.caseEditProduction(production);
          break;
        case 'CHECK_ARCHIVING':
          this.caseCheckArchiving(production);
          break;
        case 'RECHECK_ARCHIVING':
          this.caseRecheckArchiving(production);
          break;
      }

    },
    aliasProductionType(code) {
      let name = '价值意见书';
      if (code === 'REPORT') {
        name = '报告';
      }
      if (code === 'LETTER') {
        name = '复评函';
      }
      return name;
    },
    changeres(res, file, fileList) {
      let index = fileList[0].index;
      if (typeof (index) === 'undefined') {
        index = (this.productions.length) - 1;
      }
      if (res.code === 200) {
        this.productions[index].filePathList = [];
        for (let i in fileList) {
          if (fileList[i].response) {
            this.productions[index].filePathList.push({ name: fileList[i].name, url: fileList[i].response.data.url });
          } else {
            this.productions[index].filePathList.push({ name: fileList[i].name, url: fileList[i].url });
          }
        }
        this.productions[index].filePath = JSON.stringify(this.productions[index].filePathList);
      } else {
        this.$notify({
          title: '错误',
          message: '产品上传失败',
          type: 'error',
          duration: 2000,
          offset: 100
        });
        fileList.pop();
      }

    },
    changeres1(res, file, fileList) {
      let index = fileList[0].index;
      if (typeof (index) === 'undefined') {
        index = (this.productions.length) - 1;
      }
      if (res.code === 200) {
        this.productions[index].filePathList1 = [];
        for (let i in fileList) {
          if (fileList[i].response) {
            this.productions[index].filePathList1.push({ name: fileList[i].name, url: fileList[i].response.data.url });
          } else {
            this.productions[index].filePathList1.push({ name: fileList[i].name, url: fileList[i].url });
          }
        }
        this.productions[index].checkedFilePath = JSON.stringify(this.productions[index].filePathList1);
      } else {
        this.$notify({
          title: '错误',
          message: '产品上传失败',
          type: 'error',
          duration: 2000,
          offset: 100
        });
        fileList.pop();
      }

    },
    handleExceed(files, fileList) {
      this.$message.warning(`当前限制选择 3个文件，本次选择了 ${files.length} 个文件`);
    },
    handleAttachmentPreview(file) {
      // window.open(file.url)
      this.officeFileView(file.name, file.url);
    },
    beforeRemove(file, fileList) {
      let index = fileList[0].index;
      if (typeof (index) === 'undefined') {
        index = (this.productions.length) - 1;
      }
      let paths = JSON.parse(this.productions[index].filePath);
      let xPaths = paths.filter(item => item.url !== file.url);
      this.productions[index].filePath = JSON.stringify(xPaths);
    },
    beforeRemove1(file, fileList) {
      let index = fileList[0].index;
      if (typeof (index) === 'undefined') {
        index = (this.productions.length) - 1;
      }
      let paths = JSON.parse(this.productions[index].checkedFilePath);
      let xPaths = paths.filter(item => item.url !== file.url);
      this.productions[index].checkedFilePath = JSON.stringify(xPaths);
    },

    caseEditProduction(production) {
      const prodFrom = eval('this.$refs.productionFrom' + production.id);
      if (prodFrom) {
        prodFrom.validate(valid => {
          if (valid) {
            production.evaluateAmount = production.evaluateAmount*10000;
            if (production.id) {
              this.taskRecordDTO.recordId = this.currentNode.tasks[0].recordId;
              this.taskRecordDTO.taskData = production;
              this.$api.majorProduction.edit(this.taskRecordDTO).then(res => {
                if (res.code === 200 && res.data) {
                  this.$notify({
                    title: '成功',
                    message: '产品信息更新成功',
                    type: 'success',
                    duration: 1000,
                    offset: 100
                  });
                  this.taskRecordDTO = {};
                  this.getProductions(this.majorId, (this.reportNo == null ? this.statementNo : this.reportNo));
                  this.getPerformance(this.majorId, (this.reportNo == null ? this.statementNo : this.reportNo));
                } else {
                  this.$notify({
                    title: '失败',
                    message: '产品信息完善失败',
                    type: 'error',
                    duration: 1000,
                    offset: 100
                  });
                }
              })

            }
          }

        })
      }
    },
    caseFirstCheck(production) {
      if (production.id) {
        this.taskRecordDTO.recordId = this.currentNode.tasks[0].recordId;
        let checker = new Object();
        checker.id = production.id;
        checker.result = production.firstCheck;
        this.taskRecordDTO.taskData = checker;
        this.$api.majorProduction.firstCheck(this.taskRecordDTO).then(res => {
          if (res.code === 200 && res.data) {
            this.$notify({
              title: '成功',
              message: '初审状态已更新，请点击提交按钮继续流程。',
              type: 'success',
              duration: 2000,
              offset: 100
            });
          }
        })
      }
    },
    caseSecondCheck(production) {
      if (production.id) {
        this.taskRecordDTO.recordId = this.currentNode.tasks[0].recordId;
        let checker = new Object();
        checker.id = production.id;
        checker.result = production.secondCheck;
        this.taskRecordDTO.taskData = checker;
        this.$api.majorProduction.secondCheck(this.taskRecordDTO).then(res => {
          if (res.code === 200 && res.data) {
            this.$notify({
              title: '成功',
              message: '复审状态已更新，请点击提交按钮继续流程。',
              type: 'success',
              duration: 2000,
              offset: 100
            });
          }
        })
      }
    },
    caseThirdCheck(production) {
      if (production.id) {
        this.taskRecordDTO.recordId = this.currentNode.tasks[0].recordId;
        let checker = new Object();
        checker.id = production.id;
        checker.result = production.thirdCheck;
        this.taskRecordDTO.taskData = checker;
        this.$api.majorProduction.thirdCheck(this.taskRecordDTO).then(res => {
          if (res.code === 200 && res.data) {
            this.$notify({
              title: '成功',
              message: '三审状态已更新，请点击提交按钮继续流程。',
              type: 'success',
              duration: 2000,
              offset: 100
            });
          }
        })
      }
    },
    caseCheckArchiving(production) {
      if (production.id) {
        this.taskRecordDTO.recordId = this.currentNode.tasks[0].recordId;
        let checker = new Object();
        checker.id = production.id;
        checker.check = production.saveFileCheck;
        this.taskRecordDTO.taskData = checker;
        this.$api.majorProduction.saveFileCheck(this.taskRecordDTO).then(res => {
          if (res.code === 200 && res.data) {
            this.$notify({
              title: '成功',
              message: '归档状态已更新，请点击提交按钮继续流程。',
              type: 'success',
              duration: 2000,
              offset: 100
            });
          }
        })
      }
    },
    caseRecheckArchiving(production) {
      if (production.id) {
        this.taskRecordDTO.recordId = this.currentNode.tasks[0].recordId;
        let checker = new Object();
        checker.id = production.id;
        checker.check = production.saveFileSecondCheck;
        this.taskRecordDTO.taskData = checker;
        this.$api.majorProduction.saveFileSecondCheck(this.taskRecordDTO).then(res => {
          if (res.code === 200 && res.data) {
            this.$notify({
              title: '成功',
              message: '复审部归档状态已更新，请点击提交按钮继续流程。',
              type: 'success',
              duration: 2000,
              offset: 100
            });
          }
        })
      }
    },
    selectioProductionChange(val) {
      let selected = null;
      if (val.length > 1) {
        this.$refs.pTable.clearSelection();
        this.$refs.pTable.toggleRowSelection(val[val.length - 1])
        selected = val[val.length - 1];
      } else {
        if (typeof val[0] != 'undefined') {
          selected = val[0];
        }
      }
      if (selected) {
        let po = this.productions.filter(function (i) {
          return i.id != selected.id;
        }).filter(function (e) {
          return e.nextInstanceNodeId == null
        })
        if (po.length != 0) {
          this.nodeBusinessInfo.ifCheckTask = false;

        } else {
          this.nodeBusinessInfo.ifCheckTask = true;
        }

        if (this.currentNode.nodeCode === 'WRITE_STATEMENT') {
          this.nodeBusinessInfo.businessSubId = selected.reportNo;
        }
        if (this.currentNode.nodeCode === 'WRITE_REPORT' || this.currentNode.nodeCode === 'WRITE_LETTER') {
          this.nodeBusinessInfo.businessSubId = this.businessSubId;
          this.nodeBusinessInfo.businessMinId = selected.reportNo;
        }

        this.nodeBusinessInfo.reportNos = [selected.reportNo]

      } else {
        this.nodeBusinessInfo.ifCheckTask = true;
        this.nodeBusinessInfo.businessSubId = null;
        this.nodeBusinessInfo.businessMinId = null;
        this.nodeBusinessInfo.reportNos = [];
      }
    },

    workflowCommitVerify(val, callback) {
      let commit = val.commit;
      let verify = new Object();
      verify.state = true;
      verify.nextHandlerId = this.secondCheckUserId;
      verify.passNotRecord = this.passNotRecord;
      verify.passNotSubmit = this.passNotSubmit;
      let nodeCode = this.currentNode.nodeCode;
      if (commit.state === 'PASS') {
        this.getProductions(this.majorId, (this.reportNo == null ? this.statementNo : this.reportNo));
        switch (nodeCode) {
          case 'WRITE_STATEMENT':
            let notTakeNumberTargets = this.evaluateLandData.filter(function (e) {
              return e.statementNo == null;
            })
            if (notTakeNumberTargets.length > 0) {
              this.$notify({
                title: '提示',
                message: '还有价值意见书号未取的估价对象，请先完成取号操作。',
                type: 'error',
                duration: 3000,
                offset: 100
              });
              verify.state = false;
              callback(verify);
              return;
            }
            if (typeof (commit.businessSubId) == 'undefined' || commit.businessSubId == null || commit.ifCheckTask == null) {
              this.$notify({
                title: '提示',
                message: '请选择一个产品后，再进行流程提交。',
                type: 'error',
                duration: 3000,
                offset: 100
              });
              verify.state = false;
              callback(verify);
              return;
            }
            let selected = this.productions.filter(function (e) {
              return e.reportNo === commit.businessSubId
            })
            if (!selected[0].name || !selected[0].clientName || !selected[0].clientTel || !selected[0].clientAddress || !selected[0].evaluateAmount || !selected[0].evaluateAcreage
              || !selected[0].owner || !selected[0].valueTiming) {
              this.$notify({
                title: '提示',
                message: '请完成所选产品的基本信息后，再进行流程提交。',
                type: 'error',
                duration: 3000,
                offset: 100
              });
              verify.state = false;
              callback(verify);
              return;
            }
            break;
          case 'WRITE_REPORT':
            let notTakeReportNoTargets = this.evaluateLandData.filter(function (e) {
              return e.reportNo == null;
            })
            if (notTakeReportNoTargets.length > 0) {
              this.$notify({
                title: '提示',
                message: '还有报告号未取的估价对象，请先完成取号操作。',
                type: 'error',
                duration: 3000
              });
              verify.state = false;
              callback(verify);
              return;
            }
            if (typeof (commit.businessMinId) == 'undefined' || commit.businessMinId == null || commit.ifCheckTask == null) {
              this.$notify({
                title: '提示',
                message: '请选择一个产品后，再进行流程提交。',
                type: 'error',
                duration: 3000,
                offset: 100
              });
              verify.state = false;
              callback(verify);
              return;
            }
            let selected1 = this.productions.filter(function (e) {
              return e.reportNo === commit.businessMinId
            })
            if (!selected1[0].name || !selected1[0].clientName || !selected1[0].clientTel || !selected1[0].clientAddress || !selected1[0].evaluateAmount || !selected1[0].evaluateAcreage
              || !selected1[0].owner || !selected1[0].valueTiming || !selected1[0].signatory) {
              this.$notify({
                title: '提示',
                message: '请完成所选产品的基本信息后，再进行流程提交。',
                type: 'error',
                duration: 3000,
                offset: 100
              });
              verify.state = false;
              callback(verify);
              return;
            }

            break;
          case 'WRITE_LETTER':
            let notTakeLetterNumberTargets = this.evaluateLandData.filter(function (e) {
              return e.letterNo == null;
            })
            if (notTakeLetterNumberTargets.length > 0) {
              this.$notify({
                title: '提示',
                message: '还有复评函号未取的估价对象，请先完成取号操作。',
                type: 'error',
                duration: 3000,
                offset: 100
              });
              verify.state = false;
              callback(verify);
              return;
            }
            if (typeof (commit.businessMinId) == 'undefined' || commit.businessMinId == null || commit.ifCheckTask == null) {
              this.$notify({
                title: '提示',
                message: '请选择一个产品后，再进行流程提交。',
                type: 'error',
                duration: 3000,
                offset: 100
              });
              verify.state = false;
              callback(verify);
              return;
            }
            let selected2 = this.productions.filter(function (e) {
              return e.reportNo === commit.businessMinId
            })
            if (!selected2[0].name || !selected2[0].clientName || !selected2[0].clientTel || !selected2[0].clientAddress || !selected2[0].evaluateAmount || !selected2[0].evaluateAcreage
              || !selected2[0].owner || !selected2[0].valueTiming) {
              this.$notify({
                title: '提示',
                message: '请完成所选产品的基本信息后，再进行流程提交。',
                type: 'error',
                duration: 3000,
                offset: 100
              });
              verify.state = false;
              callback(verify);
              return;
            }
            break;
          case "CHECK_STATEMENT":
          case "CHECK_REPORT":
          case "CHECK_LETTER":
            if (commit.doSecondCheck != '不审' && !verify.nextHandlerId ) {
              verify.state = false;
              callback(verify);
              this.chooseSecondCheckerDialog = true
              this.$api.dictData.remark("MAJOR_SECOND_CHECK_USERS").then(res => {
                if (res.code === 200) {
                  this.secondCheckUsers = res.data;
                  //默认复审人
                  this.secondCheckUserId = res.data[0].remark;
                }
              })
              return;
            }
            break;
          case "RECHECK_REPORT":
          case "THIRD_CHECK_REPORT":
            if (!this.productions[0].isRecord && !verify.passNotRecord && this.major.businessObjectType==='土地'){
              verify.state = false;
              callback(verify);
              this.notRecordRemind = this.productions[0];
              this.notRecordDialog = true;
              return;
            }
            if (this.productions[0].isSubmit && !verify.passNotSubmit && this.major.businessObjectType==='房地产'){
              verify.state = false;
              callback(verify);
              this.notSubmitRemind = this.productions[0];
              this.notSubmitDialog = true;
              return;
            }
            break;
          case "INITIAL_PRICE":
            let notEvaluateValueTargets = this.evaluateLandData.filter(function (e) {
              return e.evaluateValue == null;
            })
            if (notEvaluateValueTargets.length > 0) {
              this.$notify({
                title: '提示',
                message: '还有有估价对象价格未确定。',
                type: 'error',
                duration: 2000,
                offset: 100
              });
              verify.state = false;
              callback(verify);
              return;
            }
            break;
          case "REVIEW_QUOTATION":
            let notCheckValueTargets = this.evaluateLandData.filter(function (e) {
              return e.checkValue == null;
            })
            if (notCheckValueTargets.length > 0) {
              this.$notify({
                title: '提示',
                message: '还有有估价对象价格未审核。',
                type: 'error',
                duration: 2000,
                offset: 100
              });
              verify.state = false;
              callback(verify);
              return;
            }
            break;
        }
        callback(verify);
      }
      if (commit.state === 'REVERSE') {
        this.$refs.board.getPreviousNode();
        switch (nodeCode) {
          case "WRITE_STATEMENT":
          case "WRITE_REPORT":
          case "WRITE_LETTER":
            if (this.productions.length > 0) {
              if (!this.productions[0].ifReverse && this.pNode.nodeCode === "QUOTATION_FEEDBACK") {
                // 需要验证兄弟流程节点信息
                this.$notify({
                  title: '提示',
                  message: '已有其他子号流程正在进行中，不能退回到上一节点。',
                  type: 'info',
                  duration: 2000,
                  offset: 100
                });
                verify.state = false;
                callback(verify);
                return
              } else {
                verify.state = true;
                verify.cleanReport = true;
                callback(verify);
                return;
              }
            }
        }
        callback(verify);
      }
    },

    createPerformance(info) {
      let allots = this.performance.allots;
      let total = 0;
      for (let i in allots) {
        total += allots[i].ratio;
      }
      if (total != 100) {
        this.$notify({
          title: '提示',
          message: '人员绩效比例之和需等于100%',
          type: 'info',
          duration: 1000,
          offset: 100
        });
        return;
      }
      info = JSON.stringify(allots);
      if (info.id) {
        this.$api.businessProductionPerformance.edit(info).then(res => {
          if (res.code === 200 && res.data) {
            this.$notify({
              title: '成功',
              message: '人员绩效比信息更新成功',
              type: 'success',
              duration: 1000,
              offset: 100
            });
            this.getPerformance(this.majorId, (this.reportNo == null ? this.statementNo : this.reportNo));
          }
        })
      } else {
        info.operation = true;
        info.businessType = 'MAJOR_BUSINESS';
        this.$api.businessProductionPerformance.add(info).then(res => {
          if (res.code === 200 && res.data) {
            this.$notify({
              title: '成功',
              message: '人员绩效比信息保存成功',
              type: 'success',
              duration: 3000,
              offset: 100
            });
            this.getPerformance(this.majorId, (this.reportNo == null ? this.statementNo : this.reportNo));
          }
        })
      }
    },
    getPerformance(majorId, productionNo) {
      if (majorId && productionNo) {
        const obj = new Object();
        obj.businessType = 'MAJOR_BUSINESS';
        obj.majorId = majorId;
        obj.productionNo = productionNo;
        obj.checkLoop = this.checkLoop;
        this.$api.businessProductionPerformance.detail(obj).then(res => {
          if (res.code === 200) {
            this.performance = res.data;
            let prods = res.data.info;
            for(let i in prods){
                if (prods[i].productionType === '报告'){
                    this.nodeBusinessInfo.landIsRecord = prods[i].isRecord;
                    break;
                }
            }
          }
        })
      }
    },
    doWareHouse() {
      let reportNo = this.scanEntryData
      let wareHoaseDto = new Object();
      if (reportNo) {
        wareHoaseDto.reportNo = reportNo;
      } else {
        wareHoaseDto.reportNo = this.$refs.scanEntry.scanEntryData;
      }
      if (this.currentNode.nodeName.includes("入库")) {
        wareHoaseDto.repertoryState = 0;
      } else {
        wareHoaseDto.repertoryState = 1;
      }
      wareHoaseDto.productionType = this.wareHouseProductionType;
      wareHoaseDto.productionId = this.wareHouseproductionId;
      this.taskRecordDTO.recordId = this.currentNode.tasks[0].recordId;
      this.taskRecordDTO.taskData = wareHoaseDto;
      this.$api.majorProduction.warehouse(this.taskRecordDTO).then(res => {
        if (res.code === 200 && res.data) {
          this.$notify({
            title: '成功',
            message: '库存操作成功',
            type: 'success',
            duration: 1000,
            offset: 100
          });
          this.warehouseDialog = false;
          this.getProductions(this.majorId, wareHoaseDto.reportNo);
        }
        else {
          this.$message({
            type: 'info',
            message: '取消操作'
          });
        }
      })
    },
    handleScanEntry(scanData) {
      this.scanEntryData = scanData;
    },
    openWareHouseDialog(row) {
      this.warehouseDialog = true;
      this.wareHouseProductionType = row.production;
      this.wareHouseproductionId = row.id
    },
    cleanWareHouseProductionType() {
      this.wareHouseProductionType = null;
      this.wareHouseproductionId = null;
    },
    sitesChange() {
      this.$forceUpdate()
    },
    expandRow(row) {
      row.expand = !row.expand
      this.$refs.pTable.toggleRowExpansion(row, row.expend);
    },
    getPreviousNode(pNode) {
      this.pNode = pNode;
    },
    //默认展开第一行
    defaultExpandOpen() {
      var els = document.getElementsByClassName('el-table__expand-icon')
      if (els[0]) {
        els[0].click();
      }
    },
    takeStatementNoOrLettertNo(productionType) {
      this.$confirm('请确认所选估价对象是否正确？', '提示', {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        type: 'info',
      }).then(() => {
        let takeNumberDTO = {
          businessId: this.majorId,
          businessType: "MAJOR_BUSINESS",
          takeType: this.major.businessObjectType === '土地' ? 'LAND_NUMBER' : 'REALTY_NUMBER',
          targets: this.takeNumberItems,
          orderId: this.major.orderId,
          productionType: productionType,
          statementNo:this.statementNo
        }
        this.$api.majorTarget.takeNumber(takeNumberDTO).then(res => {
          if (res.code === 200 && res.data != null) {
            this.$notify({
              title: '成功',
              message: '取号成功。',
              type: 'success',
              duration: 1000,
              offset: 100
            });
            this.getLandTargetList();
            this.createProduction(res.data);
            this.taskRecordDTO = {};
          } else {
            this.$notify({
              title: '失败',
              message: '取号失败。',
              type: 'error',
              duration: 1000
            });
          }
        })
      }

      )
    },
    saveQuality(p) {
      this.taskRecordDTO.recordId = this.currentNode.tasks[0].recordId;
      this.taskRecordDTO.taskData = p;
      p.normalMistake = p.normalMistakes.length;
      p.hardMistake = p.hardMistakes.length;
      p.fatalMistake = p.fatalMistakes.length;
      let mistakes = new Object();
      mistakes.normalMistakes = p.normalMistakes;
      mistakes.hardMistakes = p.hardMistakes;
      mistakes.fatalMistakes = p.fatalMistakes;
      mistakes.xnormalMistakes = p.xnormalMistakes;
      mistakes.xhardMistakes = p.xhardMistakes;
      mistakes.xfatalMistakes = p.xfatalMistakes;
      p.reason = JSON.stringify(mistakes);
      p.businessType = 'MAJOR_BUSINESS';
      if (p.id) {
        this.$api.businessProductionPerformance.edit(this.taskRecordDTO).then(res => {
          if (res.code === 200 && res.data) {
            this.$notify({
              title: '成功',
              message: '质检信息更新成功',
              type: 'success',
              duration: 1000,
              offset: 100
            });
            this.getPerformance(this.majorId, (this.reportNo == null ? this.statementNo : this.reportNo));
          }
        })
      } else {
        this.$api.businessProductionPerformance.add(this.taskRecordDTO).then(res => {
          if (res.code === 200 && res.data) {
            this.$notify({
              title: '成功',
              message: '质检信息保存成功',
              type: 'success',
              duration: 1000,
              offset: 100
            });
            this.getPerformance(this.majorId, (this.reportNo == null ? this.statementNo : this.reportNo));
          }
        })
      }
    },
    commit(state) {
      this.$refs.board.commit(state);
    },
    goBack() {
      const back = this.$route.query.back
      if (back) {
        this.$router.push(back)
      }
    },
    cleanResidueYears(target) {
      target.residueYears = null;
    },
    selectable(row, index) {
      let nodeCode = this.currentNode.nodeCode;
      if (nodeCode === 'WRITE_STATEMENT' && row.statementNo != null) {
        return false;
      } else if (nodeCode === 'WRITE_REPORT' && row.reportNo != null) {
        return false;
      } else if (nodeCode === 'WRITE_LETTER' && row.letterNo != null) {
        return false;
      } else {
        return true;
      }
    },
    addErrorType(p, errorType) {
      if (p.reason) {
        p.reason = p.reason + errorType + "; ";
      } else {
        p.reason = errorType + "; ";
      }
    },
    cleanSecondCheck() {
      this.secondCheckUserId = null;
    },
    closeLandTarget() {
      this.houseToUse = [];
      for (let k in this.landTarget) {
        this.landTarget[k] = null;
      }
    },
    getFinanceInvoiceAmountInfo() {
      const obj = new Object();
      obj.majorId = this.majorId;
      obj.productionNos = [];
      if (this.businessSubId) {
        obj.productionNos.push(this.businessSubId);
      }
      if (this.businessMinId) {
        obj.productionNos.push(this.businessMinId);
      }
      this.$api.financeInvoice.getInvoiceAmountInfo(obj).then(res => {
        if (res.code === 200) {
          this.productionFinance = res.data;
        }
      })
    },
    printQR(path) {
      if (path){
          let routeUrl = this.$router.resolve({
            path: `/print/code?path=${path}`
          });
          window.open(routeUrl.href, '_blank');
        }
    },
    // 文档预览
    officeFileView(name, url) {
      if (name || url) {
        const route = this.$router.resolve({
          path: "/office/view/window",
          query: {
            // 文档标题
            title: name,
            // 文档url地址
            url: url,
            mode: 'edit'
          }
        });
        // 在新窗口打开页面
        window.open(route.href, "_blank");
      } else {
        this.$notify({
          title: '失败',
          message: 'url为空，无法打开，如未保存请保存后再试！',
          type: 'error',
          duration: 2000
        });
      }
    },
    showQrCode(url){
      this.qrCodeUrl = url;
    },
    findWorker(row){
      console.log(row.signatoryId);
      if (row.signatoryId.length===0){
          row.mainWorker = null;
          row.mainWorkerNo = null;
          row.minorWorker = null;
          row.minorWorkerNo = null;
          row.smallWorker = null;
          row.smallWorkerNo = null;
      }
      if (row.signatoryId.length === 1){
        let mainWorkerId = row.signatoryId[0];
        this.$api.userArchive.getRegistrationNoByUserId(mainWorkerId).then(res=>{
          if (res.code===200){
              row.mainWorker = res.data.evaluatorName;
              row.mainWorkerNo = res.data.majorEvaluatorRegistrationNo;
          }
        })
        row.minorWorker = null;
        row.minorWorkerNo = null;
        row.smallWorker = null;
        row.smallWorkerNo = null;
      }
      if (row.signatoryId.length === 2){
        let mainWorkerId = row.signatoryId[0];
        this.$api.userArchive.getRegistrationNoByUserId(mainWorkerId).then(res=>{
          if (res.code===200){
              row.mainWorker = res.data.evaluatorName;
              row.mainWorkerNo = res.data.majorEvaluatorRegistrationNo;
          }
        })
        let minorWorkerId = row.signatoryId[1];
        this.$api.userArchive.getRegistrationNoByUserId(minorWorkerId).then(res=>{
          if (res.code===200){
              row.minorWorker = res.data.evaluatorName;
              row.minorWorkerNo = res.data.majorEvaluatorRegistrationNo;
          }
        })
        row.smallWorker = null;
        row.smallWorkerNo = null;
      }
      if (row.signatoryId.length ===3){
        let mainWorkerId = row.signatoryId[0];
        this.$api.userArchive.getRegistrationNoByUserId(mainWorkerId).then(res=>{
          if (res.code===200){
              row.mainWorker = res.data.evaluatorName;
              row.mainWorkerNo = res.data.majorEvaluatorRegistrationNo;
          }
        })
        let minorWorkerId = row.signatoryId[1];
        this.$api.userArchive.getRegistrationNoByUserId(minorWorkerId).then(res=>{
          if (res.code===200){
              row.minorWorker = res.data.evaluatorName;
              row.minorWorkerNo = res.data.majorEvaluatorRegistrationNo;
          }
        })
        let smallWorkerId = row.signatoryId[2];
        this.$api.userArchive.getRegistrationNoByUserId(smallWorkerId).then(res=>{
          if (res.code===200){
              row.smallWorker = res.data.evaluatorName;
              row.smallWorkerNo = res.data.majorEvaluatorRegistrationNo;
          }
        })
      }
      if (row.signatoryId.length > 3){
        this.$message.error("签字人个数建议为2人。")
      }
    },
    openMergeNumberDialog(){
      let numbers = [];
      if (this.currentNode.nodeCode === 'WRITE_STATEMENT'){
        numbers = this.evaluateLandData.reduce((setArray,item)=>{
        if (item.statementNo && !setArray.includes(item.statementNo)){
            setArray.push(item.statementNo)
        }
        return setArray;
      },[]);
      }
      if (this.currentNode.nodeCode === 'WRITE_REPORT'){
        numbers = this.evaluateLandData.reduce((setArray,item)=>{
        if (item.reportNo && !setArray.includes(item.reportNo)){
            setArray.push(item.reportNo)
        }
        return setArray;
      },[]);
      }
      if (this.currentNode.nodeCode === 'WRITE_LETTER'){
        numbers = this.evaluateLandData.reduce((setArray,item)=>{
        if (item.letterNo && !setArray.includes(item.letterNo)){
            setArray.push(item.letterNo)
        }
        return setArray;
      },[]);
      }
      if (numbers.length>0){
        this.mergeNoDialog = true;
        this.mergeNos = numbers;
      }
      else{
        this.$message("还未取任何产品号码。")
      }
    },
    doMergeNo(){
        let param = new Object();
        param.mergeType = this.currentNode.nodeCode === 'WRITE_STATEMENT'?'STATEMENT':
          (this.currentNode.nodeCode === 'WRITE_REPORT'?'REPORT':'LETTER');
        param.toMergeNumber = this.toMergeNo;
        param.targetIds = this.takeNumberItems.map(i=>i.id);
        this.$api.majorTarget.mergeNumber(param).then(res=>{
          if (res.code === 200 && res.data){
            this.$notify({
              title: '成功',
              message: '合并号码成功',
              type: 'success',
              duration: 1000,
              offset: 100
            });
          this.mergeNoDialog = false;
          this.getLandTargetList();
          this.getProductions(this.majorId, (this.reportNo == null ? this.statementNo : this.reportNo));
          }
        })
    },
    addMistake(p,mistakeType){
        let type ;
        if (mistakeType ==='normalMistakes'){
            type = '添加一般错误';
        }
        if (mistakeType ==='hardMistakes'){
            type = '添加较大错误';
        }
        if (mistakeType ==='fatalMistakes'){
            type = '添加重大错误';
        }
        this.$prompt(null, type, {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
        }).then(({ value }) => {
          if (mistakeType ==='normalMistakes'){
            p.xnormalMistakes.push(value)
            p.normalMistakes.push(value);
          }
          if (mistakeType ==='hardMistakes'){
            p.xhardMistakes.push(value);
            p.hardMistakes.push(value);
          }
          if (mistakeType ==='fatalMistakes'){
            p.xfatalMistakes.push(value);
            p.fatalMistakes.push(value);
          }
        })
    },
    doPassNotRecord(){
      this.passNotRecord = true;
      this.commit('PASS');
    },
    doPassNotSubmit(){
      this.passNotSubmit = true;
      this.commit('PASS');
    },
    remindClean(){
      this.passNotRecord = false;
      this.passNotSubmit = false;
    }
  },
}
</script>
<style lang="scss" scoped>
.row-style {
  margin-left: 40px;
  margin-right: 1%;
}


.task-class {
  /deep/ .el-form-item__label {
    color: red;
  }
}

.evaluate-table {
  margin-left: 40px;
  margin-top: 35px;
}

.pane-class {
  width: 100%;
  overflow-y: scroll;
}

/deep/ .cell-class {
  font-size: 13px;
  color: black;
}

.table {
  /deep/ thead {
    .el-table-column--selection {
      .cell {
        display: none;
      }
    }
  }
}

/deep/.doWarehouseClass {
  border-radius: 10px;
}

.button-area {
  position: absolute;
  top: 20px;
  right: 50px;
}

.error-type {
  margin-right: 5px;
}

.error-type:hover {
  cursor: pointer;
}

.qr-code-div {
  background-color: RGB(245, 245, 245);
  width: 150px;
  height: 150px;
  position: absolute;
  display: flex;
  justify-content: center;
  align-items: center;
}

.qr-code {
  width: 150px;
  height: 150px;
  z-index: 999;
  border: 1px #919398 solid;
  border-radius: 5px;
}

.qr-code-div:hover {
  cursor: pointer;
  // background-color:RGB(220,220,220); 
  // width: 180px;
  // height: 180px;

}

a:hover{
  cursor: pointer;
  color: #5a9cf8
}
.reason{
    /deep/ .el-form-item__label {
    color: red;
    font-weight: bold;
    }
   
  }
.resaon_input{
  /deep/ .el-input__inner{
    color: red !important;
    font-weight: bold;
  }
}
.mistake-option{
  transition: transform 0.5s ease;
  color: #acacae;
  
}
.mistake-option:hover{
  transform: scale(1.3); 
  color: black;
  z-index: 999;
}
</style>
