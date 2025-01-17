<template>
  <div class="app-container">
    <el-collapse v-model="activeName" @change="handleChange"  v-if="nodeBusinessInfo.currentNodeInstanceId">
      <el-collapse-item>
        <template slot="title" >
          <span style="font-size:20px; color:RGB(168,168,168)">
            <i class="el-icon-star-on"></i>
           <span> {{currentNode==null ?'???':currentNode.nodeName?currentNode.nodeName:'结束'}}</span>
           <span style="margin-left:30px" v-if="reportNo || statementNo">已取号：<span style="font-style:italic;" v-if="statementNo">{{statementNo}}</span>
           <span style="font-style:italic;margin-left:30px" v-if="reportNo">{{reportNo}}</span></span>
          </span>
          <span style="font-size:15px; margin-left:15px;letter-spacing:2px; color:RGB(168,168,168)">
            点击展开查看实时流程图
          </span>
        </template>
        <WorkflowBoard ref='board' :nodeBusinessInfo="nodeBusinessInfo" />
      </el-collapse-item>
    </el-collapse>
    <div class="button-area">
      <el-button class="y-save" round type="info" @click="goBack">返回</el-button>
    </div>
     <el-tabs v-model="activeTagName" @tab-click="handleClick" type="border-card">
      <el-tab-pane name="order" class="pane-class" :lazy=true>
        <span slot="label"><i class="el-icon-document"></i> 市场订单</span>
        <y-detail-page-layout ref="myPersonalOrder" @save="handleCreate"  :editStatus="couldEdit" :couldBack="couldBack">
          <div>
            <el-form ref="majorForm" :model="major" class="form-container" :rules="rules">
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
                      <el-form-item
                        label="客户经理："
                        prop="clientManager"
                        :rules="{required: true, message: '客户经理不能为空', trigger: 'blur'}"
                        label-width="120px"
                        class="postInfo-container-item"
                      >
                        <el-input :value="major.clientManager" class="filter-item" readonly disabled  />
                      </el-form-item>
                    </el-col>
                    <el-col :xs="24" :sm="12" :lg="6" :span="6">
                      <el-form-item
                        label="订单属性："
                        prop="financial"
                        :rules="{required: true, message: '订单属性不能为空', trigger: 'blur'}"
                        label-width="120px"
                        class="postInfo-container-item"
                      >
                      <el-select v-model="major.financial" placeholder="请选择" style="width: 100%">
                              <el-option label="金融" :value="true">金融</el-option> 
                              <el-option label="非金融" :value="false">非金融</el-option>
                            </el-select>
                      </el-form-item>
                    </el-col>
                    <el-col :xs="24" :sm="12" :lg="6" :span="6">
                      <el-form-item
                        label="派单方式："
                        prop="allotType"
                        :rules="{required: true, message: '派单方式不能为空', trigger: 'blur'}"
                        label-width="120px"
                        class="postInfo-container-item"
                      >
                      <el-select v-model="major.allotType" placeholder="请选择" style="width: 100%">
                              <el-option label="部门轮单" value="轮单"></el-option> 
                              <el-option label="部门指派" value="指派"></el-option>
                            </el-select>
                      </el-form-item>
                    </el-col>
                    <el-col :xs="24" :sm="12" :lg="6" :span="6" v-if="major.allotType==='指派' || nodeBusinessInfo.currentNodeInstanceId ">
                      <el-form-item
                        label="接单部门："
                        prop="departmentId"
                        label-width="120px"
                        class="postInfo-container-item"
                      >
                      <el-select v-model="major.departmentId" style="width: 100%">
                              <el-option v-for="(d,id) in allotDepartment" :label="d.name" :value="d.id"></el-option> 
                        </el-select>
                      </el-form-item>
                    </el-col>
                  </el-row>
                  <el-row class="row-style" v-if="saveOrderBtn==null">
                    <el-col :xs="24" :sm="12" :lg="6" :span="6">
                      <el-form-item
                        label="项目负责人："
                        prop="principalId"
                        label-width="120px"
                        :class="['postInfo-container-item']"
                      >
                        <el-select v-model="major.principalId" 
                          placeholder="待定"  style="width: 100%"
                          readonly
                          disabled>
                              <el-option v-for="(p,id) in principals" :label="p.name" :value="p.id"></el-option> 
                        </el-select>
                      </el-form-item>
                    </el-col>
                    <el-col :xs="24" :sm="12" :lg="12" :span="6">
                      <el-form-item
                        label="项目成员："
                        prop="membersId"
                        label-width="120px"
                        :class="['postInfo-container-item']"
                      >
                        <el-select v-model="major.membersId" 
                        placeholder="待定" 
                          multiple
                          readonly
                          disabled
                          style="width: 100%">
                              <el-option v-for="(u,id) in allUsers" :label="u.name" :value="u.id"></el-option> 
                        </el-select>
                      </el-form-item>
                    </el-col>
                  </el-row>
                  <el-row class="row-style">
                    <el-col :xs="12" :sm="12" :lg="18" :span="6">
                      <el-form-item
                        label="订单名称："
                        prop="name"
                        :rules="{required: true, message: '订单名称不能为空', trigger: 'blur'}"
                        label-width="120px"
                        class="postInfo-container-item"
                      >
                        <el-input v-model="major.name" clearable class="filter-item" style=" width: 100%" />
                      </el-form-item>
                    </el-col>
                  </el-row>
                  <el-row class="row-style">
                    <el-col :xs="24" :sm="12" :lg="6" :span="6">
                      <el-form-item
                        label="对象类型："
                        prop="businessObjectType"
                        :rules="{required: true, message: '评估对象类型不能为空', trigger: 'blur'}"
                        label-width="120px"
                        class="postInfo-container-item"
                      >
                      <el-select v-model="major.businessObjectType" placeholder="请选择" @change="changeType" style="width: 100%">
                        <el-option label="土地" value="土地"></el-option>
                        <el-option label="房地产" value="房地产"></el-option> 
                      </el-select>
                      </el-form-item>
                    </el-col>
                    <el-col :xs="24" :sm="12" :lg="6" :span="6" v-if="major.businessObjectType==='房地产'">
                      <el-form-item
                        label="特殊类型："
                        prop="specialType"
                        label-width="120px"
                        class="postInfo-container-item"
                      >
                      <el-select v-model="major.specialType" placeholder="请选择" style="width: 100%">
                              <el-option label="在建工程" value="在建工程"></el-option> 
                              <el-option label="加油加气站" value="加油加气站"></el-option>
                              <el-option label="电站" value="电站"></el-option> 
                              <el-option label="客运站" value="客运站"></el-option>
                            </el-select>
                      </el-form-item>
                    </el-col>
                    <el-col :xs="24" :sm="12" :lg="6" :span="6">
                      <el-form-item
                        label="业务类型："
                        prop="businessGener"
                        label-width="120px"
                        class="postInfo-container-item"
                      >
                          <el-select v-model="major.businessGener" placeholder="请选择" clearable style="width: 100%" >
                              <el-option v-for="(type,index) in types" :label="type" :value="type"></el-option> 
                            </el-select>
                      </el-form-item>
                    </el-col>
                    <el-col :xs="24" :sm="12" :lg="6" :span="6">
                      <el-form-item
                        label="评估目的："
                        prop="evaluateAim"
                        :rules="{required: true, message: '评估目的不能为空', trigger: 'blur'}"
                        label-width="120px"
                        class="postInfo-container-item"
                      >
                      <el-select v-model="major.evaluateAim" placeholder="请选择" style="width: 100%">
                              <el-option label="抵押" value="抵押"></el-option> 
                              <el-option label="咨询" value="咨询"></el-option>
                              <el-option label="融资" value="融资"></el-option> 
                              <el-option label="可行性研究" value="可行性研究"></el-option>
                              <el-option label="不良资产价值评估" value="不良资产价值评估"></el-option> 
                              <el-option label="其他" value="其他"></el-option>
                            </el-select>
                      </el-form-item>
                    </el-col>
                  </el-row>
                  <el-row class="row-style">
                    <el-col :xs="24" :sm="12" :lg="6" :span="6">
                      <el-form-item
                        label="再次委托："
                        prop="entrustAgain"
                        :rules="{required: true, message: '再次委托不能为空', trigger: 'blur'}"
                        label-width="120px"
                        class="postInfo-container-item"
                      >
                      <el-radio  v-model="major.entrustAgain" :label="false"  size="medium" >否</el-radio>
                      <el-radio  v-model="major.entrustAgain" :label="true"  size="medium" >是</el-radio>
                      </el-form-item>
                    </el-col>
                      <el-col :xs="24" :sm="12" :lg="6" :span="6">
                      <el-form-item
                        label="分户搬迁："
                        prop="multipleRemoval"
                        :rules="{required: true, message: '分户搬迁不能为空', trigger: 'blur'}"
                        label-width="120px"
                        class="postInfo-container-item"
                      >
                      <el-radio  v-model="major.multipleRemoval" :label="false"  size="medium" >否</el-radio>
                      <el-radio  v-model="major.multipleRemoval" :label="true"  size="medium" >是</el-radio>
                      </el-form-item>
                    </el-col>
                    <el-col :xs="24" :sm="12" :lg="6" :span="6">
                      <el-form-item
                        label="是否口估："
                        prop="informalEvaluate"
                        :rules="{required: true, message: '是否口估不能为空', trigger: 'blur'}"
                        label-width="120px"
                        class="postInfo-container-item"
                      >
                      <el-radio  v-model="major.informalEvaluate" :label="false"  size="medium" >否</el-radio>
                      <el-radio  v-model="major.informalEvaluate" :label="true"  size="medium" >是</el-radio>
                      </el-form-item>
                    </el-col>
                  </el-row>
                  <el-row class="row-style">
                    <el-col :xs="24" :sm="12" :lg="6" :span="6">
                      <el-form-item
                        label="客户类型："
                        prop="clienteleType"
                        :rules="{required: true, message: '客户类型不能为空', trigger: 'blur'}"
                        label-width="120px"
                        class="postInfo-container-item"
                      >
                      <el-select v-model="major.clienteleType" placeholder="请选择" @change="changeCustomerType(0)" style="width: 100%">
                              <el-option label="企业" value="企业"></el-option> 
                              <el-option label="个人" value="个人"></el-option>
                            </el-select>
                      </el-form-item>
                    </el-col>
                    <el-col :xs="24" :sm="12" :lg="6" :span="6" v-if="major.clienteleType==='企业'">
                      <el-form-item
                        label="客户名称："
                        prop="clienteleId"
                        label-width="120px"
                        class="postInfo-container-item"
                      >
                        <el-select v-model="major.clienteleId" placeholder="请选择(可搜索)" @change="findSubClientele(0)" clearable filterable style="width: 100%" v-if="couldEdit">
                          <el-option v-for="(c,id) in customerCompany" :label="c.name" :value="c.id"></el-option> 
                        </el-select>
                        <el-input :value="major.cclienteleName" class="filter-item"   v-else/>
                      </el-form-item>
                    </el-col>
                    <el-col :xs="24" :sm="12" :lg="6" :span="6" v-if="major.clienteleType==='企业'">
                      <el-form-item
                        label="业务来源："
                        prop="clienteleSubId"
                        label-width="120px"
                        class="postInfo-container-item"
                      >
                        <el-select v-model="major.clienteleSubId" placeholder="请选择(可搜索)"  @change="getCustomerContract" clearable filterable style="width: 100%" v-if="couldEdit">
                          <el-option v-for="(s,id) in subCustomerCompany" :label="s.name" :value="s.id"></el-option> 
                        </el-select>
                        <el-input :value="major.cclienteleSubName" class="filter-item"   v-else />
                      </el-form-item>
                    </el-col>
                    <el-col :xs="24" :sm="12" :lg="6" :span="6">
                      <el-form-item
                        label="客户联系人："
                        prop="clienteleContactId"
                        :rules="{required: true, message: '客户联系人不能为空', trigger: 'blur'}"
                        label-width="140px"
                        class="postInfo-container-item"
                      >   
                      <div  v-if="couldEdit">
                        <el-select v-model="major.clienteleContactId" placeholder="请选择(可搜索)"  clearable filterable @change="getContactInfo" style="width:80%">
                            <el-option v-for="(c,id) in customerContract" :label="c.name" :value="c.id"></el-option> 
                        </el-select>
                        <el-tooltip class="item" effect="dark" content="没有联系人？点击新增" placement="top-end">
                          <span style="margin-left:5%;color:RGB(64,158,255);cursor:pointer" @click="createLinkman(false)">新增</span>
                        </el-tooltip>
                      </div>
                      <el-input :value="major.cclienteleContactName" class="filter-item" v-else/>
                      </el-form-item>
                    </el-col>
                  </el-row>
                  <el-row class="row-style">
                    <el-col :xs="24" :sm="12" :lg="6" :span="6">
                      <el-form-item
                        label="联系人电话："
                        prop="clienteleContactId"
                        label-width="120px"
                        class="postInfo-container-item"
                      >
                      <el-input :value="major.cmobile" class="filter-item"   readonly disabled/>
                      </el-form-item>
                    </el-col>
                    <el-col :xs="24" :sm="12" :lg="6" :span="6">
                      <el-form-item
                        label="联系人部门："
                        prop="cdepartment"
                        label-width="120px"
                        class="postInfo-container-item"
                      >
                      <el-input :value="major.cdepartment" class="filter-item" readonly disabled/>
                      </el-form-item>
                    </el-col>
                    <el-col :xs="24" :sm="12" :lg="6" :span="6">
                      <el-form-item
                        label="联系人职务："
                        prop="cduty"
                        label-width="120px"
                        class="postInfo-container-item"
                      >
                      <el-input :value="major.cduty" class="filter-item"   readonly disabled/>
                      </el-form-item>
                    </el-col>
                  </el-row>
                  <el-row class="row-style">
                    <el-col :xs="24" :sm="12" :lg="6" :span="6">
                      <el-form-item
                        label="分配产值："
                        prop="dispenseBenefit"
                        :rules="{required: true, message: '分配产值不能为空', trigger: 'blur'}"
                        label-width="120px"
                        class="postInfo-container-item"
                      >
                      <el-radio  v-model="major.dispenseBenefit" :label="false"  size="medium" >否</el-radio>
                      <el-radio  v-model="major.dispenseBenefit" :label="true"  size="medium">是</el-radio>
                      </el-form-item>
                    </el-col>
                      <el-col :xs="24" :sm="12" :lg="6" :span="6">
                      <el-form-item
                        label="是否异地："
                        prop="nonnative"
                        :rules="{required: true, message: '是否异地不能为空', trigger: 'blur'}"
                        label-width="120px"
                        class="postInfo-container-item"
                      >
                      <el-radio  v-model="major.nonnative" :label="false"  size="medium" >否</el-radio>
                      <el-radio  v-model="major.nonnative" :label="true"  size="medium" >是</el-radio>
                      </el-form-item>
                    </el-col>
                    <el-col :xs="24" :sm="12" :lg="6" :span="6">
                      <el-form-item
                        label="是否续贷："
                        prop="refinance"
                        :rules="{required: true, message: '是否续贷不能为空', trigger: 'blur'}"
                        label-width="120px"
                        class="postInfo-container-item"
                      >
                      <el-radio  v-model="major.refinance" :label="false"  size="medium" >否</el-radio>
                      <el-radio  v-model="major.refinance" :label="true"  size="medium">是</el-radio>
                      </el-form-item>
                    </el-col>
                    <el-col :xs="24" :sm="12" :lg="6" :span="6">
                      <el-form-item
                        label="贷款到期："
                        prop="loanExpire"
                        label-width="120px"
                        class="postInfo-container-item"
                      >
                      <el-radio  v-model="major.loanExpire" :label="false"  size="medium" >否</el-radio>
                      <el-radio  v-model="major.loanExpire" :label="true"  size="medium">是</el-radio>
                      </el-form-item>
                    </el-col>
                  </el-row>
                  <el-row class="row-style">
                    <el-col :xs="24" :sm="12" :lg="6" :span="6">
                      <el-form-item
                        label="贷款额度："
                        prop="loanLimit"
                        label-width="120px"
                        class="postInfo-container-item"
                      >
                      <el-input v-model="major.loanLimit" clearable class="filter-item" >
                        <template slot="append">万</template>
                      </el-input>
                      </el-form-item>
                    </el-col>
                    <el-col :xs="24" :sm="12" :lg="6" :span="6">
                      <el-form-item
                        label="贷款期限："
                        prop="loanPeriod"
                        label-width="120px"
                        class="postInfo-container-item"
                      >
                      <el-input v-model="major.loanPeriod" clearable class="filter-item"  >
                        <template slot="append">年</template>
                      </el-input>
                      </el-form-item>
                    </el-col>
                    <el-col :xs="24" :sm="12" :lg="6" :span="6">
                      <el-form-item
                        label="贷款性质："
                        prop="loanNature"
                        label-width="120px"
                        class="postInfo-container-item"
                      >
                      <el-input v-model="major.loanNature" clearable class="filter-item"   />
                      </el-form-item>
                    </el-col>
                    <el-col :xs="24" :sm="12" :lg="6" :span="6">
                      <el-form-item
                        label="贷款用途："
                        prop="loanAim"
                        label-width="120px"
                        class="postInfo-container-item"
                      >
                      <el-input v-model="major.loanAim" clearable class="filter-item"   />
                      </el-form-item>
                    </el-col>
                  </el-row>
                  <el-row class="row-style">
                    <el-col :xs="24" :sm="12" :lg="12" :span="6">
                      <el-form-item
                        label="委托人："
                        prop="bailor"
                        :rules="{required: true, message: '委托人不能为空', trigger: 'blur'}"
                        label-width="120px"
                        class="postInfo-container-item"
                      >
                      <el-input v-model="major.bailor" clearable class="filter-item"   />
                      </el-form-item>
                    </el-col>
                    <el-col :xs="24" :sm="12" :lg="12" :span="6">
                      <el-form-item
                        label="委托人地址："
                        prop="bailorAddress"
                        label-width="120px"
                        class="postInfo-container-item"
                      >
                      <el-input v-model="major.bailorAddress" clearable class="filter-item"   />
                      </el-form-item>
                    </el-col>
                  </el-row>
                  <el-row class="row-style">
                    <el-col :xs="24" :sm="12" :lg="6" :span="6">
                      <el-form-item
                        label="委托联系人："
                        prop="bailorContactName"
                        :rules="{required: true, message: '委托联系人不能为空', trigger: 'blur'}"
                        label-width="140px"
                        class="postInfo-container-item"
                      >
                      <el-input v-model="major.bailorContactName" clearable class="filter-item" />
                      </el-form-item>
                    </el-col>
                    <el-col :xs="24" :sm="12" :lg="6" :span="6">
                      <el-form-item
                        label="委托电话："
                        prop="bailorContactTel"
                        label-width="120px"
                        class="postInfo-container-item" 
                      >
                      <el-input v-model="major.bailorContactTel" clearable class="filter-item"/>
                      </el-form-item>
                    </el-col>
                  </el-row>
                  <el-row class="row-style">
                    <el-col :xs="24" :sm="12" :lg="12" :span="6">
                      <el-form-item
                        label="产权人："
                        prop="owner"
                        label-width="120px"
                        class="postInfo-container-item"
                      >
                      <el-input v-model="major.owner" clearable class="filter-item"/>
                      </el-form-item>
                    </el-col>
                    <el-col :xs="24" :sm="12" :lg="6" :span="6">
                      <el-form-item
                        label="产权人电话："
                        prop="ownerTel"
                        label-width="120px"
                        class="postInfo-container-item"
                      >
                      <el-input v-model="major.ownerTel" clearable class="filter-item"/>
                      </el-form-item>
                    </el-col>
                    <el-col :xs="24" :sm="12" :lg="6" :span="6">
                      <el-form-item
                        label="是否加急："
                        prop="urgent"
                        :rules="{required: true, message: '是否加急不能为空', trigger: 'blur'}"
                        label-width="120px"
                        class="postInfo-container-item"
                      >
                      <el-radio  v-model="major.urgent" :label="false"  size="medium" >否</el-radio>
                      <el-radio  v-model="major.urgent" :label="true"  size="medium" >是</el-radio>
                      </el-form-item>
                    </el-col>
                  </el-row>
                  <el-row class="row-style">
                    <el-col :xs="24" :sm="12" :lg="6" :span="6">
                      <el-form-item
                        label="终端客户类型："
                        prop="terminalClienteleType"
                        label-width="140px"
                        class="postInfo-container-item"
                      >
                      <el-select v-model="major.terminalClienteleType" placeholder="请选择" clearable @change="changeCustomerType(1)" style="width:100%">
                              <el-option label="企业" value="企业"></el-option> 
                              <el-option label="个人" value="个人"></el-option>
                            </el-select>
                      </el-form-item>
                    </el-col>
                  
                    <el-col :xs="24" :sm="12" :lg="12" :span="6" v-if="major.terminalClienteleType!=''">
                      <el-form-item
                        label="终端客户名称："
                        prop="terminalClienteleId"
                        label-width="140px"
                        :rules="{required: true, message: '终端客户名称不能为空', trigger: 'blur'}"
                        class="postInfo-container-item"
                      > 
                      <div v-if="couldEdit">
                          <el-select v-model="major.terminalClienteleId" placeholder="请选择(可搜索)" @change="findSubClientele(1)" clearable filterable style="width:90%">
                            <el-option v-for="(c,id) in tCustomerCompany" :label="c.name" :value="c.id"></el-option> 
                          </el-select>
                          <el-tooltip class="item" effect="dark" content="没有终端客户？点击新增" placement="top-end">
                            <span style="margin-left:5%;color:RGB(64,158,255);cursor:pointer" @click="createTerminalClient()">新增</span>
                          </el-tooltip>
                      </div>
                      <el-input :value="major.tterminalClienteleName" readonly disabled class="filter-item" v-else/>
                      </el-form-item>
                    </el-col>
                    <el-col :xs="24" :sm="12" :lg="6" :span="6" v-if="major.terminalClienteleType=='企业'">
                      <el-form-item
                        label="终端联系人："
                        prop="terminalClienteleContactId"
                        label-width="140px"
                        :rules="{required: true, message: '终端联系人不能为空', trigger: 'blur'}"
                        class="postInfo-container-item"
                      >
                        <div v-if="couldEdit">
                          <el-select v-model="major.terminalClienteleContactId" placeholder="请选择(可搜索)"   clearable filterable @change="getTcontactInfo" style="width:80%">
                            <el-option v-for="(c,id) in tCustomerContract" :label="c.name" :value="c.id"></el-option> 
                          </el-select>
                          <el-tooltip class="item" effect="dark" content="没有联系人？点击新增" placement="top-end">
                            <span style="margin-left:5%;color:RGB(64,158,255);cursor:pointer" @click="createTlinkman()">新增</span>
                          </el-tooltip>
                        </div>
                        <el-input :value="major.tterminalClienteleContactName" readonly disabled class="filter-item"  v-else/>
                      </el-form-item>
                    </el-col>
                    <el-col :xs="24" :sm="12" :lg="6" :span="6" v-if="major.terminalClienteleType!=''">
                      <el-form-item
                        label="终端联系电话："
                        prop="tmobile"
                        label-width="140px"
                        class="postInfo-container-item"
                      >
                      <el-input :value="major.tmobile" class="filter-item"   readonly disabled/>
                      </el-form-item>
                    </el-col>
                  </el-row>
                  <el-row class="row-style">
                    <el-col :xs="24" :sm="12" :lg="18" :span="12">
                      <el-form-item
                        label="订单备注："
                        prop="remark"
                        label-width="120px"
                        class="postInfo-container-item"
                      >
                      <el-input v-model="major.remark" :autosize="{ minRows: 1, maxRows: 1}" clearable class="filter-item" type="textarea"  />
                      </el-form-item>
                    </el-col>
                  </el-row>
                </div>
              </div>
            </el-form>
          </div>
        </y-detail-page-layout>
      </el-tab-pane>
      <el-tab-pane name="evaluate" class="pane-class" :lazy=true v-if ="!couldEdit">
        <span slot="label"><i class="el-icon-office-building"></i> 估价对象</span>
        <div class="createMajor-main-container">
            <div class="postInfo-container">
              <div>
                <el-divider content-position="left">
                  <h3 class="title">
                  <div class="avatar-wrapper icon-title">对象</div>
                  <div class="icon-info">{{major.businessObjectType}}</div>
                  </h3>
                </el-divider>
              </div>
            </div>
        </div>
        <div class="evaluate-table" v-if="major.businessObjectType==='土地'">
          <div >
            <el-row >
              <el-col :span="2">
                <div>
                  <el-statistic
                    group-separator=","
                    :precision="4"
                    :value="totalEvaluateValue"
                    title="估价对象总价(万)"
                  >
                  <template slot="prefix">
                    <i class="el-icon-coin"></i>
                  </template>
                </el-statistic>
                </div>
              </el-col>
              <el-col :span="2" style="margin-left:20px">
                <div>
                  <el-statistic
                    group-separator=","
                    :precision="2"
                    :value="totalAcreage"
                    title="估价对象总面积(㎡)"
                  >
                  <template slot="prefix">
                    <i class="el-icon-house"></i>
                  </template>
                </el-statistic>
                </div>
              </el-col>
            </el-row>
          </div>
          <el-table
            ref="multipleTable"
            :data="evaluateLandData"
            stripe
            :header-row-style="{ color: '#333333','font-size': '14px' }"
            border
            takeNumberItems
            style="width: 100%; margin-top:20px;font-size: 14px;">
            <el-table-column
              type="index"
              label="序号"
              width="50"
              align="center">
            </el-table-column>
            <el-table-column
              prop="located"
              label="坐落"
              align="center"
              show-overflow-tooltip>
            </el-table-column>
            <el-table-column
              prop="landType"
              label="土地类型"
              width="80"
              align="center">
            </el-table-column>
            <el-table-column
              prop="landUse"
              label="用途"
              width="80"
              align="center">
            </el-table-column>
            <el-table-column
              prop="rightToUser"
              label="所有权人"
              align="center"
              show-overflow-tooltip>
            </el-table-column>
            <el-table-column
              prop="landCertificateNo"
              label="国土证号"
              align="center"
              show-overflow-tooltip>
            </el-table-column>
            <el-table-column
              prop="plotRatio"
              label="容积率(%)"
              width="80"
              align="center">
            </el-table-column>
            <el-table-column
              prop="useAcreage"
              label="使用权面积(㎡)"
              width="80"
              align="center">
            </el-table-column>
            <el-table-column
              prop="evaluateValue"
              label="初步定价(万)"
              width="80"
              align="center">
            </el-table-column>
            <el-table-column
                prop="checked"
                label="审核定价(万)"
                width="80"
                align="center">
                <template slot-scope="scope">
                  {{scope.row.checkValue==null?'待审核':scope.row.checkValue}}
              </template>
              </el-table-column>
              <el-table-column
                prop="statementNo"
                label="价值意见书号"
                align="center"
                width=250
                show-overflow-tooltip>
                <template slot-scope="scope">
                 <el-tag type='success'> 
                  {{scope.row.statementNo==null?'未取号':scope.row.statementNo}}</el-tag>
              </template>
              </el-table-column>
              <el-table-column
                prop="reportNo"
                label="报告号"
                align="center"
                width=250
                show-overflow-tooltip>
                <template slot-scope="scope">
                 <el-tag type='danger'> 
                  {{scope.row.reportNo==null?'未取号':scope.row.reportNo}}</el-tag>
              </template>
              </el-table-column>
              <el-table-column
                prop="letterNo"
                label="复评函号"
                align="center"
                width=250
                show-overflow-tooltip>
                <template slot-scope="scope">
                 <el-tag> 
                  {{scope.row.letterNo==null?'未取号':scope.row.letterNo}}</el-tag>
              </template>
              </el-table-column>
            <el-table-column
              label="操作"
              width="100"
              align="center">
              <template slot-scope="scope">
                <el-button @click="landTargetEdit(scope.row)" type="text" size="small">
                  查看
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
        <div class="evaluate-table" v-if="major.businessObjectType==='房地产'">
          <div >
            <el-row>
              <el-col :span="2">
                <div>
                  <el-statistic
                    group-separator=","
                    :precision="4"
                    :value="totalEvaluateValue"
                    title="估价对象总价(万)"
                  >
                  <template slot="prefix">
                    <i class="el-icon-coin"></i>
                  </template>
                </el-statistic>
                </div>
              </el-col>
              <el-col :span="2" style="margin-left:20px">
                <div>
                  <el-statistic
                    group-separator=","
                    :precision="2"
                    :value="totalAcreage"
                    title="估价对象总面积(㎡)"
                  >
                  <template slot="prefix">
                    <i class="el-icon-house"></i>
                  </template>
                </el-statistic>
                </div>
              </el-col>
            </el-row>
          </div>
            <el-table
              ref="multipleTable"
              :data="evaluateLandData"
              stripe
              :header-row-style="{ color: '#333333','font-size': '14px' }"
              border
              takeNumberItems
              style="width: 100%; margin-top:20px;font-size: 14px;">
              <el-table-column
                type="index"
                label="序号"
                width="50"
                align="center">
              </el-table-column>
              <el-table-column
                prop="housePurpose"
                label="用途"
                width="80"
                align="center"
                show-overflow-tooltip>
                <template slot-scope="scope">
                    <span v-for="(u,index) in JSON.parse(scope.row.housePurpose)">
                      {{u}} {{index==((JSON.parse(scope.row.housePurpose)).length-1)?'':','}}   
                    </span>
                </template>
              </el-table-column>
              <el-table-column
                prop="owner"
                label="权利人"
                width="120"
                show-overflow-tooltip
                align="center">
              </el-table-column>
              <el-table-column
                prop="located"
                label="坐落"
                align="center"
                show-overflow-tooltip>
              </el-table-column>
              <el-table-column
                prop="buildAcreage"
                label="建筑面积(㎡)"
                width="80"
                align="center"
                show-overflow-tooltip>
              </el-table-column>
              <el-table-column
                prop="landAcreage"
                label="土地面积(㎡)"
                width="80"
                align="center">
              </el-table-column>
              <el-table-column
                prop="evaluateValue"
                label="初步定价(万)"
                width="80"
                align="center">
              </el-table-column>
              <el-table-column
                prop="checked"
                label="审核定价(万)"
                width="80"
                align="center">
                <template slot-scope="scope">
                  {{scope.row.checkValue==null?'待审核':scope.row.checkValue}}
              </template>
              </el-table-column>
              <el-table-column
                prop="statementNo"
                label="价值意见书号"
                align="center"
                width=250
                show-overflow-tooltip>
                <template slot-scope="scope">
                 <el-tag type='success'> 
                  {{scope.row.statementNo==null?'未取号':scope.row.statementNo}}</el-tag>
              </template>
              </el-table-column>
              <el-table-column
                prop="reportNo"
                label="报告号"
                align="center"
                width=250
                show-overflow-tooltip>
                <template slot-scope="scope">
                 <el-tag type='danger'> 
                  {{scope.row.reportNo==null?'未取号':scope.row.reportNo}}</el-tag>
              </template>
              </el-table-column>
              <el-table-column
                prop="letterNo"
                label="复评函号"
                align="center"
                width=250
                show-overflow-tooltip>
                <template slot-scope="scope">
                 <el-tag > 
                  {{scope.row.letterNo==null?'未取号':scope.row.letterNo}}</el-tag>
              </template>
              </el-table-column>
              <el-table-column
                label="操作"
                width="100"
                align="center">
                <template slot-scope="scope">
                  <el-button @click="landTargetEdit(scope.row)" type="text" size="small">
                    查看
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
        </div>
        <div class="createMajor-main-container" style="margin-top:50px">
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
              <el-form ref="feedbackFrom" :model="feedback" class="form-container"  style="margin-top:50px">
                  <el-row >
                    <el-col :xs="24" :sm="12" :lg="8" :span="6">
                      <el-form-item
                        label="出具产品类型："
                        prop="production"
                        :rules="{required: true, message: '出具产品类型不能为空', trigger: 'blur'}"
                        label-width="160px"
                        class="postInfo-container-item"
                      >
                        <el-checkbox-group v-model="feedback.production">
                          <el-checkbox-button label="STATEMENT" name="production">价值意见书</el-checkbox-button>
                          <el-checkbox-button label="REPORT" name="production">报告</el-checkbox-button>
                          <el-checkbox-button label="LETTER" name="production">复评函</el-checkbox-button>
                        </el-checkbox-group>
                      </el-form-item>
                    </el-col>
                    <el-col :xs="24" :sm="12" :lg="6" :span="6">
                      <el-form-item
                        label="打印份数："
                        prop="printing"
                        :rules="{required: true, message: '打印份数不能为空', trigger: 'blur'}"
                        label-width="120px"
                        class="postInfo-container-item"
                      >
                      <el-input-number disabled :precision="0" :min="1" :value-on-clear="1" v-model.number="feedback.printing" type="number" 
                        style="width:206px"
                      class="filter-item" />
                      </el-form-item>
                    </el-col>
                    <el-col :xs="24" :sm="12" :lg="6" :span="6">
                      <el-form-item
                        label="打印备注："
                        prop="printingRemark"
                        label-width="120px"
                        class="postInfo-container-item"
                        
                      >
                        <el-select v-model="feedback.printingRemark" disabled readonly>
                          <el-option label="一本不装订" value="一本不装订"></el-option> 
                          <el-option label="一本带技术" value="一本带技术"></el-option> 
                          <el-option label="一本带技术不装订" value="一本带技术不装订"></el-option> 
                        </el-select>
                      </el-form-item>
                    </el-col>
                  </el-row>
              </el-form>
        </div>
      </el-tab-pane>
      <el-tab-pane name="producution" class="pane-class" :lazy=true v-if ="!couldEdit">
        <span slot="label"><i class="el-icon-document-checked"></i> 产品信息</span>
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
        <el-table
          :data="productions"
          stripe
          ref = "pTable"
          :header-row-style="{ color: '#333333', 'font-size': '16px' }"
          border
          style="width: 99%; margin-top:20px;font-size: 16px;">
          <el-table-column type="expand">
            <template slot-scope="props">
              <el-form label-position="left" :model="props.row" class="form-container" ref="productionFrom">
                <div>
                  <el-row class="row-style">
                    <el-col :xs="24" :sm="12" :lg="12" :span="6">
                      <el-form-item  
                          prop="name"
                          :rules="{required: true, message: '项目不能为空', trigger: 'blur'}"
                          label-width="120px"
                          class="postInfo-container-item" label="项目名称:">
                          <el-input v-model="props.row.name" class="filter-item"  disabled readonly/>
                        </el-form-item>
                      </el-col>
                      <el-col :xs="24" :sm="12" :lg="12" :span="6">
                        <el-form-item  prop="clientName"
                          :rules="{required: true, message: '委托方不能为空', trigger: 'blur'}"
                          label-width="120px"
                          class="postInfo-container-item" label="委托方：">
                          <el-input v-model="props.row.clientName" class="filter-item"  disabled readonly />
                        </el-form-item>
                      </el-col>
                    </el-row><el-row class="row-style">
                      <el-col :xs="24" :sm="12" :lg="12" :span="6">
                        <el-form-item  prop="clientAddress"
                          :rules="{required: true, message: '委托方地址不能为空', trigger: 'blur'}"
                          label-width="140px"
                          class="postInfo-container-item" label="委托方地址：">
                          <el-input v-model="props.row.clientAddress" class="filter-item"   disabled readonly/>
                        </el-form-item>
                      </el-col>
                      <el-col :xs="24" :sm="12" :lg="6" :span="6">
                        <el-form-item  prop="clientTel"
                          :rules="{required: true, message: '委托方电话不能为空', trigger: 'blur'}"
                          label-width="140px"
                          class="postInfo-container-item" label="委托方电话：">
                          <el-input v-model="props.row.clientTel" class="filter-item"   disabled readonly/>
                        </el-form-item>
                      </el-col>
                      <el-col :xs="24" :sm="12" :lg="6" :span="6">
                      <el-form-item prop="validateCode" label-width="140px" class="postInfo-container-item" label="打印二维码：">
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
                        <el-form-item  prop="evaluateAmount"
                          :rules="{required: true, message: '评估总价不能为空', trigger: 'blur'}"
                          label-width="120px"
                          class="postInfo-container-item" label="评估总价：">
                            <el-input :precision="2" :min="0" v-model.number="props.row.evaluateAmount" type="number"   disabled readonly
                                style="width:100%"
                              class="filter-item">
                              <template slot="append">
                                <span>万元</span>
                              </template>
                            </el-input>
                        </el-form-item>
                      </el-col>
                      <el-col :xs="24" :sm="12" :lg="6" :span="6">
                        <el-form-item  prop="evaluateAcreage"
                          :rules="{required: true, message: '评估面积不能为空', trigger: 'blur'}"
                          label-width="120px"
                          class="postInfo-container-item" label="评估面积：">
                              <el-input :precision="2" :min="0"  v-model.number="props.row.evaluateAcreage" type="number"   disabled readonly
                              style="width:100%"
                              class="filter-item" >
                              <template slot="append">
                                <span>平方</span>
                              </template>
                              </el-input>
                        </el-form-item>
                      </el-col>
                      <el-col :xs="24" :sm="12" :lg="6" :span="6">
                          <el-form-item  prop="evaluatePrice"
                              :rules="{required: true, message: '评估单价不能为空', trigger: 'blur'}"
                              label-width="120px"
                              class="postInfo-container-item" label="评估单价：">
                              <el-input :precision="2" :min="0"  v-model.number="props.row.evaluatePrice" type="number"  disabled readonly
                                style="width:100%"
                                class="filter-item" >
                                <template slot="append">
                                  <span>万元</span>
                                </template>
                              </el-input>
                          </el-form-item>
                      </el-col>
                  </el-row>
                  <el-row class="row-style">
                    <el-col :xs="24" :sm="12" :lg="6" :span="6">
                          <el-form-item  prop="owner"
                          :rules="{required: true, message: '产权人不能为空', trigger: 'blur'}"
                          label-width="120px"
                          class="postInfo-container-item" label="产权人：">
                          <el-input v-model="props.row.owner" class="filter-item"   disabled readonly/>
                        </el-form-item>
                      </el-col>
                    <el-col :xs="24" :sm="12" :lg="6" :span="6">
                        <el-form-item  prop="valueTiming"
                        :rules="{required: true, message: '价值时点不能为空', trigger: 'blur'}"
                        label-width="120px"
                        class="postInfo-container-item" label="价值时点：">
                          <el-date-picker
                          v-model="props.row.valueTiming" type="date" placeholder="选择日期" style="width:100%"  disabled readonly>
                          </el-date-picker>
                        </el-form-item>
                    </el-col>
                    <el-col :xs="24" :sm="12" :lg="6" :span="6" v-if="props.row.production==='REPORT'">
                        <el-form-item  prop="signatory"
                        label-width="120px"
                        class="postInfo-container-item" label="签字人：" >
                          <el-select v-model="props.row.signatoryId" 
                          placeholder="产品类型为报告，签字人必填" 
                            multiple
                            clearable  disabled readonly
                            style=" width: 100%" >
                                <el-option v-for="(u,id) in allUsers" :label="u.name" :value="u.id"></el-option> 
                          </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :xs="24" :sm="12" :lg="6" :span="6" v-else>
                        <el-form-item  prop="signatory"
                        label-width="120px"
                        class="postInfo-container-item">
                        </el-form-item>
                    </el-col>
                  
                  </el-row>
                  <el-row class="row-style" v-if="props.row.production==='REPORT'">
                    <el-col :xs="24" :sm="12" :lg="6" :span="6">
                      <el-form-item prop="workStartDate" :rules="{ required: true, message: '估价作业开始日期不能为空', trigger: 'blur' }"
                        label-width="180px" class="postInfo-container-item" label="估价作业开始日期：">
                        <el-date-picker v-model="props.row.workStartDate" type="date" value-format="yyyy-MM-dd" placeholder="选择日期" style="width:100%"  disabled readonly>
                        </el-date-picker>
                      </el-form-item>
                    </el-col>
                    <el-col :xs="24" :sm="12" :lg="6" :span="6">
                      <el-form-item prop="workEndDate" :rules="{ required: true, message: '估价作业结束日期不能为空', trigger: 'blur' }"
                        label-width="180px" class="postInfo-container-item" label="估价作业结束日期：">
                        <el-date-picker v-model="props.row.workEndDate" type="date" value-format="yyyy-MM-dd" placeholder="选择日期" style="width:100%"  disabled readonly>
                        </el-date-picker>
                      </el-form-item>
                    </el-col>
                    <el-col :xs="24" :sm="12" :lg="6" :span="6">
                      <el-form-item label="评估目的：" prop="workPurpose"
                        :rules="{ required: true, message: '估价目的不能为空', trigger: 'blur' }" label-width="120px"
                        class="postInfo-container-item">
                        <el-select v-model="props.row.workPurpose" placeholder="请选择" style=" width: 100%" filterable default-first-option allow-create  disabled readonly                    >
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
                  <el-row class="row-style" v-if="props.row.production==='REPORT'">
                    <el-col :xs="24" :sm="12" :lg="6" :span="6" v-if="major.businessObjectType === '房地产'">
                      <el-form-item label="是否上报协会：" prop="isSubmit"
                        :rules="{ required: true, message: '是否上报协会不能为空', trigger: 'blur' }" label-width="140px"
                        class="postInfo-container-item">
                        <el-radio v-model="props.row.isSubmit" :label="true" size="medium"  disabled readonly>是</el-radio>
                        <el-radio v-model="props.row.isSubmit" :label="false" size="medium"  disabled readonly>否</el-radio>
                      </el-form-item>
                    </el-col>
                    <el-col :xs="24" :sm="12" :lg="6" :span="6" v-if="major.businessObjectType === '土地'">
                      <el-form-item label="是否备案：" prop="isRecord"
                        :rules="{ required: true, message: '是否备案不能为空', trigger: 'blur' }" label-width="120px"
                        class="postInfo-container-item">
                        <el-radio v-model="props.row.isRecord" :label="true" size="medium"  disabled readonly>是</el-radio>
                        <el-radio v-model="props.row.isRecord" :label="false" size="medium"  disabled readonly>否</el-radio>
                      </el-form-item>
                    </el-col>
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
                  <el-row class="row-style">
                    <el-col :xs="24" :sm="12" :lg="18" :span="6">
                        <el-form-item  prop="comment"
                          label-width="120px"
                          class="postInfo-container-item" label="特殊情况说明:">
                          <el-input v-model.trim="props.row.comment" class="filter-item"  disabled readonly/>
                        </el-form-item>
                      </el-col>
                   
                  </el-row>
                  <el-row class="row-style">
                    <el-col :xs="24" :sm="12" :lg="8" :span="6">
                      <el-form-item label="产品初稿：" label-width="120px" class="postInfo-container-item">
                        <span v-for="file in props.row.filePathList" :key="file.name">
                            <el-link @click="officeFileView(file.name, file.url)">{{ file.name }}</el-link>
                          </span>
                      </el-form-item>
                    </el-col>

                    <el-col :xs="24" :sm="12" :lg="8" :span="6">
                      <el-form-item label="产品审核稿：" label-width="140px" class="postInfo-container-item">
                        <span  v-for="file in props.row.filePathList1" :key="file.name">
                            <el-link @click="officeFileView(file.name, file.url)">{{ file.name }}</el-link>
                          </span>
                      </el-form-item>
                    </el-col>
                  </el-row>
                </div>
              </el-form>
            </template>
          </el-table-column>
          <el-table-column
            label="产品号"
            prop="reportNo"
            align="center">
            <template slot-scope="props">
              <span style="color:rgb(131,201,94); font-weight:bold">{{ props.row.reportNo }}</span>
            </template>
          </el-table-column>
          <el-table-column
            label="项目名称"
            prop="name"
            align="center" show-overflow-tooltip>
            <template slot-scope="props">
                <span style="font-size:16px">{{props.row.name}}</span>
            </template>
          </el-table-column>
          <el-table-column
            label="产品类型"
            prop="production"
            align="center">
            <template slot-scope="props">
               <span style="font-size:16px">{{aliasProductionType(props.row.production)}}</span>
            </template>
          </el-table-column>
          <el-table-column
            label="产品状态"
            prop="name"
            align="center">
            <template slot-scope="props">
                <i v-if="props.row.name" style="font-size:20px; color:green" class="el-icon-success"></i>
                <i v-else style="font-size:20px; color:red" class="el-icon-error"></i>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>
      <el-tab-pane name="quality" class="pane-class" :lazy=true v-if ="!couldEdit">
        <span slot="label"><i class="el-icon-pie-chart"></i> 产品质检</span>
          <el-form ref="performanceForm" :model="performance" class="form-container">
            <div class="createMajor-main-container">
                <div class="postInfo-container">
                  <div>
                    <el-divider content-position="left">
                      <h3 class="title">
                      <div class="avatar-wrapper icon-title">质检</div>
                      <div class="icon-info">产品质检</div>
                      </h3>
                    </el-divider>
                  </div>
                <div style="margin-top:50px"> 
                  <el-row class="row-style" style="margin-top:30px;" >
                    <el-col :xs="24" :sm="12" :lg="7" :span="6">
                      <el-form-item  prop="productionNo"
                        label-width="120px"
                        class="postInfo-container-item" label="产品号:">
                        <el-input style="width:100%;" disabled readonly :value="performance.productionNo" class="filter-item" />
                      </el-form-item>
                    </el-col>
                    <el-col :xs="24" :sm="12" :lg="4" :span="6">
                      <el-form-item  prop="productionType"
                        label-width="120px"
                        class="postInfo-container-item" label="产品类型：">
                        <el-input style="width:100%;" disabled readonly :value="performance.productionType" class="filter-item" />
                      </el-form-item>
                    </el-col>
                    <el-col :xs="24" :sm="12" :lg="5" :span="6">
                      <el-form-item  prop="productionName"
                        label-width="120px"
                        class="postInfo-container-item" label="项目名称:">
                        <el-input  style="width:100%;" disabled readonly :value="performance.productionName" class="filter-item" />
                      </el-form-item>
                    </el-col>
                    <el-col :xs="24" :sm="12" :lg="4" :span="6">
                      <el-form-item  prop="productionName"
                        label-width="120px"
                        class="postInfo-container-item" label="负责人:">
                        <el-input  style="width:100%;" disabled readonly :value="performance.principal" class="filter-item" />
                      </el-form-item>
                    </el-col>
                  </el-row>
                  <el-card shadow="hover" style="width:80%;margin-left:3%" v-for="(p,id) in performance.info" :index="id">
                    <el-divider content-position="left">
                      <h3 class="title">
                        {{p.checkLoop}}
                      </h3>
                    </el-divider>
                    <el-row class="row-style" >
                      <el-col :xs="24" :sm="12" :lg="8" :span="6">
                        <el-form-item  prop="normalMistake"
                          label-width="120px"
                          class="postInfo-container-item" label="一般错误个数:">
                          <el-input-number  :min="0" style="width:100%;" type="number" disabled readonly :value="p.normalMistake" class="filter-item" />
                        </el-form-item>
                      </el-col>
                      <el-col :xs="24" :sm="12" :lg="8" :span="6">
                        <el-form-item  prop="hardMistake"
                          label-width="120px"
                          class="postInfo-container-item" label="较大错误个数:">
                          <el-input-number :min="0" style="width:100%;" type="number" disabled readonly :value="p.hardMistake" class="filter-item" />
                        </el-form-item>
                      </el-col>
                      <el-col :xs="24" :sm="12" :lg="8" :span="6">
                        <el-form-item  prop="fatalMistake"
                          label-width="120px"
                          class="postInfo-container-item" label="重大错误个数:">
                          <el-input-number :min="0"  style="width:100%;" type="number" disabled readonly :value="p.fatalMistake" class="filter-item" />
                        </el-form-item>
                      </el-col>
                    </el-row>
                    <el-row class="row-style">
                      <el-col :xs="24" :sm="12" :lg="24" :span="6">
                        <el-form-item  prop="reason"
                          label-width="120px"
                          class="postInfo-container-item" label="扣分原因:">
                          <el-input disabled readonly :value="p.reason" class="filter-item" type="textarea" />
                        </el-form-item>
                      </el-col>
                    </el-row>
                  </el-card>
                </div>
              </div>
            </div>
          </el-form>
      </el-tab-pane>
      <el-tab-pane name="finance" class="pane-class" :lazy=true v-if ="!couldEdit">
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
          <el-card v-for="(item,index) in productionFinance" shadow="hover">
          <el-form ref="financeForm" :model="item" class="form-container">
            <el-divider content-position="left" >
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
              <el-form-item label-width="120px" class="postInfo-container-item" prop="orderRealAmount" label="订单实收金额:">
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
                    <el-input :value="item.invoiceAmount" class="filter-item" type="type" readonly disabled >
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
      <el-tab-pane name="workflowLog" class="pane-class" :lazy=true v-if="!couldEdit">
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
        <el-table
            :data="workflowLogs"
            stripe
            :header-row-style="{ color: '#333333' }"
            border
            style="width: 100%; margin-top:20px">
            <el-table-column
              prop="nodeName"
              label="节点名称"
              align="center">
            </el-table-column>
            <el-table-column
              prop="state"
              label="处理结果"
              align="center">
            </el-table-column>
            <el-table-column
              prop="handlerName"
              label="处理人"
              align="center">
            </el-table-column>
            <el-table-column
              prop="longTime"
              label="处理时长"
              align="center">
            </el-table-column>
            <el-table-column
              prop="created"
              label="处理时间"
              align="center">
            </el-table-column>
            <el-table-column
              prop="comments"
              label="处理意见"
              align="center"
              show-overflow-tooltip>
            </el-table-column>
          </el-table>
      </el-tab-pane>
    </el-tabs>
    <el-dialog  :visible.sync="landDialogVisible"  custom-class="createLinkman">
      <div>
        <el-form ref="landFrom" :model="landTarget" style="margin-top:20px">
          <el-divider content-position="left">【土地】基本信息</el-divider>
          <el-row>
            <el-col :xs="24" :sm="12" :lg="12" :span="6">
              <el-form-item
                label="土地类型："
                prop="landType"
                label-width="120px"
                class="postInfo-container-item"
              >
                <el-radio  v-model="landTarget.landType" label="国有"  size="mini" readonly disabled>国有</el-radio>
                <el-radio  v-model="landTarget.landType" label="集体"  size="mini" readonly disabled>集体</el-radio>
              </el-form-item>
            </el-col>
            <el-col :xs="24" :sm="12" :lg="12" :span="6">
              <el-form-item
                label="使用权面积："
                prop="useAcreage"
                label-width="120px"
                class="postInfo-container-item"
              >
              <el-input v-model="landTarget.useAcreage" class="filter-item" readonly disabled>
                <template slot="append">平方</template>
              </el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :xs="24" :sm="12" :lg="12" :span="6">
              <el-form-item
                label="使用权类型："
                prop="rights"
                label-width="120px"
                class="postInfo-container-item"
              >
                <!-- <el-checkbox-group v-model="landTarget.rights" >
                  <el-checkbox-button label="出让" name="rights"></el-checkbox-button>
                  <el-checkbox-button label="划拨" name="rights"></el-checkbox-button>
                </el-checkbox-group> -->
                <el-radio v-model="landTarget.rightType" label="出让" size="mini">出让</el-radio>
                <el-radio v-model="landTarget.rightType" label="划拨" size="mini">划拨</el-radio>
              </el-form-item>
            </el-col>
            <el-col :xs="24" :sm="12" :lg="12" :span="8">
              <el-form-item
                label="使用权人："
                prop="rightToUser"
                label-width="120px"
                class="postInfo-container-item"
              >
                <el-input v-model.trim="landTarget.rightToUser" class="filter-item" readonly disabled/>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :xs="24" :sm="12" :lg="12" :span="8">
                <el-form-item
                  label="国土证号："
                  prop="landCertificateNo"
                  label-width="120px"
                  class="postInfo-container-item"
                >
                  <el-input v-model.trim="landTarget.landCertificateNo" class="filter-item" readonly disabled/>
                </el-form-item>
            </el-col>
            <el-col :xs="24" :sm="12" :lg="12" :span="8">
              <el-form-item
                label="坐落："
                prop="located"
                label-width="120px"
                class="postInfo-container-item"
              >
                <el-input v-model.trim="landTarget.located" class="filter-item" readonly disabled />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :xs="24" :sm="12" :lg="12" :span="6">
              <el-form-item
                label="剩余年限："
                prop="residueYears"
                label-width="120px"
                class="postInfo-container-item"
              >
              <el-input v-model="landTarget.residueYears" class="filter-item" readonly disabled>
                <template slot="append">年</template>
              </el-input>
              </el-form-item>
            </el-col>
            <el-col :xs="24" :sm="12" :lg="12" :span="6">
              <el-form-item
                label="容积率："
                prop="plotRatio"
                label-width="120px"
                class="postInfo-container-item"
              >
              <el-input v-model="landTarget.plotRatio" class="filter-item" readonly disabled>
                  <template slot="append">%</template>
              </el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :xs="24" :sm="12" :lg="12" :span="6">
              <el-form-item
                label="用途(地类)："
                prop="landUse"
                label-width="120px"
                class="postInfo-container-item"
              >
              <el-input v-model.trim="landTarget.landUse" class="filter-item" readonly disabled />
              </el-form-item>
            </el-col>
            <el-col :xs="24" :sm="12" :lg="12" :span="8">
              <el-form-item
                label="估价期日："
                prop="evaluateDate"
                label-width="120px"
                class="postInfo-container-item"
              >
                <el-date-picker
                  v-model="landTarget.evaluateDate" type="date" placeholder="选择日期" style="width:100%" readonly disabled>
                </el-date-picker>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :xs="24" :sm="12" :lg="12" :span="8">
              <el-form-item
                label="负责人："
                prop="principalId"
                label-width="120px"
                class="postInfo-container-item"
              >
                <el-select :value="landTarget.principalId" readonly disabled style="width:100%">
                  <el-option v-for="(p,id) in principals" :label="p.name" :value="p.id"></el-option> 
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :xs="24" :sm="12" :lg="12" :span="8">
                <el-form-item
                  label="实勘人："
                  prop="sites"
                  label-width="120px"
                  class="postInfo-container-item"
                >
                  <el-select v-model="landTarget.sites" multiple readonly disabled placeholder="请选择" style="width:100%">
                    <el-option v-for="(u,id) in allUsers" :label="u.name" :value="u.id"></el-option> 
                  </el-select>
                </el-form-item>
              </el-col>
          </el-row>
          <el-row>
            <el-col :xs="24" :sm="12" :lg="24" :span="12">
              <el-form-item
                label="备注："
                prop="remark"
                label-width="120px"
                class="postInfo-container-item"
              >
              <el-input v-model.trim="landTarget.remark" class="filter-item" type="textarea" style="width:100%;" readonly disabled />
              </el-form-item>
            </el-col>
          </el-row>
          <div>
            <el-divider content-position="left">【土地】定价信息</el-divider>
            <el-row>
              <el-col :xs="24" :sm="12" :lg="24" :span="6">
                <el-form-item
                  label="评估方法："
                  prop="eMethods"
                  label-width="160px"
                  :class="['postInfo-container-item','task-class']"
                >
                <el-checkbox-group v-model="eMethods">
                    <el-checkbox-button label="市场比较法" name="eMethods"></el-checkbox-button>
                    <el-checkbox-button label="收益还原法" name="eMethods"></el-checkbox-button>
                    <el-checkbox-button label="成本逼近法" name="eMethods"></el-checkbox-button>
                    <el-checkbox-button label="剩余法" name="eMethods"></el-checkbox-button>
                    <el-checkbox-button label="基准地价系数修正法" name="eMethods"></el-checkbox-button>
                    <el-checkbox-button label="其他" name="eMethods"></el-checkbox-button>
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
                <el-form-item
                  label="初步定价："
                  prop="evaluateValue"
                  label-width="160px"
                  :class="['postInfo-container-item','task-class']"
                >
                <el-input  v-model="landTarget.evaluateValue" class="filter-item" readonly disabled>
                  <template slot="append">万</template>
                </el-input>
                </el-form-item>
              </el-col>
              <el-col :xs="24" :sm="12" :lg="12" :span="6">
                <el-form-item
                  label="审核定价："
                  prop="checkValue"
                  label-width="160px"
                  :class="['postInfo-container-item','task-class']"
                >
                <span slot="label">审核定价
                        <el-tooltip
                          class="item"
                          effect="dark"
                          content="默认为‘初步定价’可修改。"
                          placement="top-start"
                        >
                          <i class="el-icon-question"/></el-tooltip>
                        ：
                      </span>
                <el-input v-model="landTarget.checkValue"  :placeholder="landTarget.evaluateValue" class="filter-item" readonly disabled>
                  <template slot="append">万</template>
                </el-input>
                </el-form-item>
              </el-col>
            </el-row>
          </div>
        </el-form>
      </div>
    </el-dialog>
    <el-dialog  :visible.sync="houseDialogVisible"  custom-class="createLinkman">
      <div>
        <el-divider content-position="left">【房地产】基本信息</el-divider>
        <el-form ref="houseFrom" :model="landTarget" style="margin-top:20px">
          <el-row>
            <el-col :xs="24" :sm="12" :lg="12" :span="6">
              <el-form-item
                label="房屋用途："
                prop="houseUseTO"
                label-width="120px"
                :class="['postInfo-container-item']"
              >
              <el-checkbox-group v-model="houseUseTO">
                  <el-checkbox-button label="住宅" name="houseUseTO"></el-checkbox-button>
                  <el-checkbox-button label="商业" name="houseUseTO"></el-checkbox-button>
                  <el-checkbox-button label="办公" name="houseUseTO"></el-checkbox-button>
                  <el-checkbox-button label="工业" name="houseUseTO"></el-checkbox-button>
                  <el-checkbox-button label="其他" name="houseUseTO"></el-checkbox-button>
              </el-checkbox-group>
              </el-form-item>
            </el-col>
            <el-col :xs="24" :sm="12" :lg="12" :span="6">
              <el-form-item
                label="土地使用权人："
                prop="rightToUser"
                label-width="140px"
                class="postInfo-container-item"
              >
                <el-input v-model.trim="landTarget.rightToUser" class="filter-item" readonly disabled />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :xs="24" :sm="12" :lg="12" :span="6">
              <el-form-item
                label="权利人："
                prop="owner"
                label-width="120px"
                class="postInfo-container-item"
              >
                <el-input v-model.trim="landTarget.owner" class="filter-item" readonly disabled />
              </el-form-item>
            </el-col>
            <el-col :xs="24" :sm="12" :lg="12" :span="6">
                <el-form-item
                  label="权证号："
                  prop="ownerNo"
                  label-width="120px"
                  class="postInfo-container-item"
                >
                  <el-input v-model.trim="landTarget.ownerNo" class="filter-item" readonly disabled />
                </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :xs="24" :sm="12" :lg="12" :span="6">
              <el-form-item
                label="土地证号："
                prop="landCertificateNo"
                label-width="120px"
                class="postInfo-container-item"
              >
              <el-input v-model.trim="landTarget.landCertificateNo" class="filter-item" readonly disabled />
              </el-form-item>
            </el-col>
            <el-col :xs="24" :sm="12" :lg="12" :span="6">
              <el-form-item
                label="坐落："
                prop="located"
                label-width="120px"
                class="postInfo-container-item"
              >
              <el-input v-model.trim="landTarget.located" class="filter-item" readonly disabled/>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :xs="24" :sm="12" :lg="12" :span="6">
              <el-form-item
                label="使用权类型："
                prop="useRightType"
                label-width="120px"
                class="postInfo-container-item"
              >
              <el-input v-model.trim="landTarget.useRightType" class="filter-item" readonly disabled />
              </el-form-item>
            </el-col>
            <el-col :xs="24" :sm="12" :lg="12" :span="8">
              <el-form-item
                label="项目名称："
                prop="targetName"
                label-width="120px"
                class="postInfo-container-item"
              >
              <el-input v-model.trim="landTarget.targetName" readonly disabled/>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :xs="24" :sm="12" :lg="12" :span="8">
              <el-form-item
                label="竣工年代："
                prop="buildDoneYear"
                label-width="120px"
                class="postInfo-container-item"
              >
              <el-input v-model.trim="landTarget.buildDoneYear" class="filter-item" readonly disabled/>
              </el-form-item>
            </el-col>
            <el-col :xs="24" :sm="12" :lg="12" :span="8">
                <el-form-item
                  label="建筑面积："
                  prop="buildAcreage"
                  label-width="120px"
                  class="postInfo-container-item"
                >
                <el-input v-model="landTarget.buildAcreage" readonly disabled
                  style="width:100%" class="filter-item" >
                  <template slot="append">平方</template>
                </el-input>
                </el-form-item>
              </el-col>
          </el-row>
          <el-row>
            <el-col :xs="24" :sm="12" :lg="12" :span="8">
              <el-form-item
                label="土地面积："
                prop="landAcreage"
                label-width="120px"
                class="postInfo-container-item"
              >
              <el-input v-model="landTarget.landAcreage" readonly disabled
                style="width:100%" class="filter-item" >
                <template slot="append">平方</template>
              </el-input>
              </el-form-item>
            </el-col>
            <el-col :xs="24" :sm="12" :lg="12" :span="6">
              <el-form-item
                label="规划用途："
                prop="planPurpose"
                label-width="120px"
                class="postInfo-container-item"
              >
              <el-input v-model.trim="landTarget.planPurpose" class="filter-item" readonly disabled/>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :xs="24" :sm="12" :lg="12" :span="6">
              <el-form-item
                label="实勘地址："
                prop="siteLocated"
                label-width="120px"
                class="postInfo-container-item"
              >
              <el-input v-model.trim="landTarget.siteLocated" class="filter-item" readonly disabled/>
              </el-form-item>
            </el-col>
            <el-col :xs="24" :sm="12" :lg="12" :span="6">
                <el-form-item
                  label="实际用途："
                  prop="realPurpose"
                  label-width="120px"
                  class="postInfo-container-item"
                >
                <el-input v-model.trim="landTarget.realPurpose" class="filter-item" readonly disabled/>
                </el-form-item>
              </el-col>
          </el-row>
          <el-row>
            <el-col :xs="24" :sm="12" :lg="12" :span="8">
              <el-form-item
                label="负责人："
                prop="principalId"
                label-width="120px"
                class="postInfo-container-item"
              >
                <el-select :value="landTarget.principalId" readonly disabled style="width:100%">
                  <el-option v-for="(p,id) in principals" :label="p.name" :value="p.id"></el-option> 
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :xs="24" :sm="12" :lg="12" :span="8">
                <el-form-item
                  label="实勘人："
                  prop="sites"
                  label-width="120px"
                  class="postInfo-container-item"
                 
                >
                  <el-select v-model="landTarget.sites" multiple readonly disabled  placeholder="请选择" style="width:100%">
                    <el-option v-for="(u,id) in allUsers" :label="u.name" :value="u.id"></el-option> 
                  </el-select>
                </el-form-item>
              </el-col>
          </el-row>
          <el-row>
            <el-col :xs="24" :sm="12" :lg="24" :span="12">
              <el-form-item
                label="备注："
                prop="remark"
                label-width="120px"
                class="postInfo-container-item"
              >
              <el-input v-model.trim="landTarget.remark" class="filter-item" type="textarea" readonly disabled/>
              </el-form-item>
            </el-col>
          </el-row>
          <div>
            <el-divider content-position="left">【房地产】定价信息</el-divider>
            <el-row>
              <el-col :xs="24" :sm="12" :lg="24" :span="6">
                <el-form-item
                  label="评估方法："
                  prop="eMethods"
                  label-width="160px"
                  :class="['postInfo-container-item','task-class']"
                >
                <el-checkbox-group v-model="eMethods">
                    <el-checkbox-button label="比较法" name="eMethods"></el-checkbox-button>
                    <el-checkbox-button label="还原法" name="eMethods"></el-checkbox-button>
                    <el-checkbox-button label="成本法" name="eMethods"></el-checkbox-button>
                    <el-checkbox-button label="假设开发法" name="eMethods"></el-checkbox-button>
                    <el-checkbox-button label="基准地价修正法" name="eMethods"></el-checkbox-button>
                </el-checkbox-group>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :xs="24" :sm="12" :lg="12" :span="6">
                <el-form-item
                  label="初步定价："
                  prop="evaluateValue"
                  label-width="160px"
                  :class="['postInfo-container-item','task-class']"
                >
                <el-input  v-model="landTarget.evaluateValue"  readonly disabled
                  style="width:100%" 
                class="filter-item">
                <template slot="append">万</template>
              </el-input>
                </el-form-item>
              </el-col>
              <el-col :xs="24" :sm="12" :lg="12" :span="6">
                <el-form-item
                  label="审核定价："
                  prop="checkValue"
                  label-width="180px"
                  :class="['postInfo-container-item','task-class']"
                >
                <span slot="label">审核定价
                        <el-tooltip
                          class="item"
                          effect="dark"
                          content="默认为‘初步定价’若价格无误，可直接点击保存按钮，否则可修改。"
                          placement="top-start"
                        >
                          <i class="el-icon-question"/></el-tooltip>
                        ：
                      </span>
                <el-input v-model="landTarget.checkValue" :placeholder="landTarget.evaluateValue" readonly disabled
                  style="width:100%"
                class="filter-item">
                <template slot="append">万</template>
              </el-input>
                </el-form-item>
              </el-col>
            </el-row>
          </div>
        </el-form>
      </div>
    </el-dialog>
    <el-dialog :visible.sync="createLinkmanVisible" width="70%" custom-class="createLinkman">
      <el-divider content-position="left">新增客户联系人</el-divider>
      <el-form ref="linkmanFrom" :model="linkmanFrom" :rules="rules">
        <el-row>
          <el-col :xs="24" :sm="12" :lg="6" :span="6">
            <el-form-item
              label="客户类型："
              prop="clienteleType"
              :rules="{required: true, message: '客户类型不能为空', trigger: 'blur'}"
              label-width="120px"
              class="postInfo-container-item"
            >
            <el-select v-model="linkmanFrom.clienteleType" disabled readonly>
                    <el-option label="企业" value="企业"></el-option> 
                    <el-option label="个人" value="个人"></el-option>
                  </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :lg="6" :span="6" v-if="major.clienteleType==='企业'">
            <el-form-item
              label="客户名称："
              prop="clienteleId"
              :rules="{required: true, message: '客户名称不能为空', trigger: 'blur'}"
              label-width="120px"
              class="postInfo-container-item"
            >
              <el-select v-model="linkmanFrom.clienteleId"  disabled readonly>
                <el-option v-for="(c,id) in customerCompany" :label="c.name" :value="c.id"></el-option> 
              </el-select>
             
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :lg="6" :span="6" v-if="major.clienteleType==='企业'">
            <el-form-item
              label="业务来源："
              prop="clienteleSubId"
              :rules="{required: true, message: '业务来源不能为空', trigger: 'blur'}"
              label-width="120px"
              class="postInfo-container-item"
            >
              <el-select v-model="linkmanFrom.clienteleSubId"  disabled readonly>
                <el-option v-for="(s,id) in subCustomerCompany" :label="s.name" :value="s.id"></el-option> 
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :xs="24" :sm="12" :lg="6" :span="6">
            <el-form-item
              label="联系人姓名："
              prop="name"
              :rules="{required: true, message: '联系人姓名不能为空', trigger: 'blur'}"
              label-width="120px"
              class="postInfo-container-item"
            >
              <el-input v-model.trim="linkmanFrom.name" class="filter-item" />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :lg="6" :span="6">
            <el-form-item
              label="联系电话："
              prop="mobile"
              label-width="120px"
              class="postInfo-container-item"
            >
              <el-input v-model.trim="linkmanFrom.mobile" class="filter-item" />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :lg="6" :span="6">
            <el-form-item
              label="性别："
              prop="sex"
              label-width="120px"
              class="postInfo-container-item"
            >
              <el-select v-model=linkmanFrom.sex>
                <el-option label="男" value="男">男</el-option>
                <el-option label="女" value="女">女</el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :lg="6" :span="6">
            <el-form-item
              label="身份证号："
              prop="idNo"
              label-width="120px"
              class="postInfo-container-item"
            >
              <el-input v-model.trim="linkmanFrom.idNo" class="filter-item" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :xs="24" :sm="12" :lg="6" :span="6">
            <el-form-item
              label="微信号："
              prop="wechatNo"
              label-width="120px"
              class="postInfo-container-item"
            >
              <el-input v-model.trim="linkmanFrom.wechatNo" class="filter-item" />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :lg="6" :span="6">
            <el-form-item
              label="QQ号："
              prop="qqNo"
              label-width="120px"
              class="postInfo-container-item"
            >
              <el-input v-model.trim="linkmanFrom.qqNo" class="filter-item" />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :lg="6" :span="6">
            <el-form-item
              label="毕业院校："
              prop="school"
              label-width="120px"
              class="postInfo-container-item"
            >
              <el-input v-model.trim="linkmanFrom.school" class="filter-item" />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :lg="6" :span="6">
            <el-form-item
              label="毕业专业："
              prop="industry"
              label-width="120px"
              class="postInfo-container-item"
            >
              <el-input v-model.trim="linkmanFrom.industry" class="filter-item" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :xs="24" :sm="12" :lg="6" :span="6">
            <el-form-item
              label="所在部门："
              prop="department"
              label-width="120px"
              class="postInfo-container-item"
            >
              <el-input v-model.trim="linkmanFrom.department" class="filter-item" />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :lg="6" :span="6">
            <el-form-item
              label="职位："
              prop="duty"
              label-width="120px"
              class="postInfo-container-item"
            >
              <el-input v-model.trim="linkmanFrom.duty" class="filter-item" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :xs="24" :sm="12" :lg="24" :span="6">
            <el-form-item
                label="备注："
                prop="description"
                label-width="120px"
                class="postInfo-container-item"
              >
                <el-input v-model.trim="linkmanFrom.description" class="filter-item" type="textarea" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-button type="primary" @click="saveLinkman()" style="position:absolute;top:45px;right:20px">保存</el-button>
      </el-form>
    </el-dialog>
    <el-dialog :visible.sync="createTlinkmanVisible" width="70%" custom-class="createLinkman">
      <el-divider content-position="left">{{major.terminalClienteleType==='企业'?'新增终端客户联系人':'新增终端客户'}}</el-divider>
      <el-form ref="tLinkmanFrom" :model="tLinkmanFrom" :rules="rules" >
        <el-row v-if="major.terminalClienteleType==='企业'">
          <el-col :xs="24" :sm="12" :lg="12" :span="6">
            <el-form-item
              label="所属企业："
              prop="ccId"
              label-width="120px"
              class="postInfo-container-item"
            >
            <el-select v-model="major.terminalClienteleId" disabled readonly style="width:100%">
              <el-option v-for="(c,id) in tCustomerCompany" :label="c.name" :value="c.id"></el-option> 
            </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :xs="24" :sm="12" :lg="6" :span="6">
            <el-form-item
              label="联系人姓名："
              prop="name"
              :rules="{required: true, message: '联系人姓名不能为空', trigger: 'blur'}"
              label-width="140px"
              class="postInfo-container-item"
            >
              <el-input v-model.trim="tLinkmanFrom.name" class="filter-item" />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :lg="6" :span="6">
            <el-form-item
              label="联系电话："
              prop="mobile"
              label-width="120px"
              class="postInfo-container-item"
            >
              <el-input v-model.trim="tLinkmanFrom.mobile" class="filter-item" />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :lg="6" :span="6">
            <el-form-item
              label="性别："
              prop="sex"
              label-width="120px"
              class="postInfo-container-item"
            >
              <el-select v-model=tLinkmanFrom.sex>
                <el-option label="男" value="男">男</el-option>
                <el-option label="女" value="女">女</el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :lg="6" :span="6">
            <el-form-item
              label="身份证号："
              prop="idNo"
              label-width="120px"
              class="postInfo-container-item"
            >
              <el-input v-model.trim="tLinkmanFrom.idNo" class="filter-item" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :xs="24" :sm="12" :lg="6" :span="6">
            <el-form-item
              label="微信号："
              prop="wechatNo"
              label-width="120px"
              class="postInfo-container-item"
            >
              <el-input v-model.trim="tLinkmanFrom.wechatNo" class="filter-item" />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :lg="6" :span="6">
            <el-form-item
              label="QQ号："
              prop="qqNo"
              label-width="120px"
              class="postInfo-container-item"
            >
              <el-input v-model.trim="tLinkmanFrom.qqNo" class="filter-item" />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :lg="6" :span="6">
            <el-form-item
              label="毕业院校："
              prop="school"
              label-width="120px"
              class="postInfo-container-item"
            >
              <el-input v-model.trim="tLinkmanFrom.school" class="filter-item" />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :lg="6" :span="6">
            <el-form-item
              label="毕业专业："
              prop="industry"
              label-width="120px"
              class="postInfo-container-item"
            >
              <el-input v-model.trim="tLinkmanFrom.industry" class="filter-item" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :xs="24" :sm="12" :lg="6" :span="6">
            <el-form-item
              label="所在部门："
              prop="department"
              label-width="120px"
              class="postInfo-container-item"
            >
              <el-input v-model.trim="tLinkmanFrom.department" class="filter-item" />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :lg="6" :span="6">
            <el-form-item
              label="职位："
              prop="duty"
              label-width="120px"
              class="postInfo-container-item"
            >
              <el-input v-model.trim="tLinkmanFrom.duty" class="filter-item" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :xs="24" :sm="12" :lg="24" :span="6">
            <el-form-item
                label="备注："
                prop="description"
                label-width="120px"
                class="postInfo-container-item"
              >
                <el-input v-model.trim="tLinkmanFrom.description" class="filter-item" type="textarea" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-button type="primary" @click="saveTlinkman()" style="position:absolute;top:45px;right:20px">保存</el-button>
      </el-form>
    </el-dialog>
    <el-dialog :visible.sync="createTerminalClientVisible" width="70%" custom-class="createLinkman">
      <el-divider content-position="left">新增终端客户</el-divider>
      <el-form ref="terminalClientFrom" :model="terminalClientFrom" :rules="rules" >
        <el-row>
          <el-col :xs="24" :sm="12" :lg="8" :span="6">
            <el-form-item
              label="客户名称："
              prop="name"
              :rules="{required: true, message: '联系人姓名不能为空', trigger: 'blur'}"
              label-width="120px"
              class="postInfo-container-item"
            >
              <el-input v-model.trim="terminalClientFrom.name" class="filter-item" />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :lg="8" :span="6">
            <el-form-item
              label="企业类型："
              prop="companyType"
              :rules="{required: true, message: '联系电话不能为空', trigger: 'blur'}"
              label-width="120px"
              class="postInfo-container-item"
            >
              <el-input v-model.trim="terminalClientFrom.companyType" class="filter-item" />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :lg="8" :span="6">
            <el-form-item
              label="联系电话："
              prop="phone"
              :rules="{required: true, message: '联系电话不能为空', trigger: 'blur'}"
              label-width="120px"
              class="postInfo-container-item"
            >
               <el-input v-model.trim="terminalClientFrom.phone" class="filter-item" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :xs="24" :sm="12" :lg="24" :span="6">
            <el-form-item
              label="客户地址："
              prop="address"
              label-width="120px"
              class="postInfo-container-item"
            >
              <el-input v-model.trim="terminalClientFrom.address" class="filter-item" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-button type="primary" @click="saveClient()" style="position:absolute;top:45px;right:20px">保存</el-button>
      </el-form>
    </el-dialog>
  </div>
</template>
<script>
  import Breadcrumb from '@/components/Breadcrumb'
  import YDetailPageLayout from '@/components/YDetailPageLayout/index_detail'
  import WorkflowBoard from '@/components/workflowBoard'
  import {isNumber,phoneNumber,postiveInteger,isIdNumber} from '@/utils/validate'

  export default {
    name: 'majorDetail',
    components: {
      Breadcrumb,
      YDetailPageLayout,
      WorkflowBoard
    },
    watch:{
      $route(to, from) {
        if (to.path === from.path){
          this.couldEdit = this.$route.query.couldEdit ==='true';
          this.showBtn = this.$route.query.showBtn ==='true';
          this.couldBack = this.$route.query.couldBack ==='true';
          this.reportNo = this.$route.query.reportNo;
          this.statementNo = this.$route.query.statementNo;
          this.saveOrderBtn = this.$route.query.saveOrderBtn;
          this.majorId = this.$route.query.id;
          this.currentNode.nodeName = this.$route.query.currentNodeName;
          this.nodeBusinessInfo.doWorkflow = this.$route.query.couldEdit ==='true';
          this.nodeBusinessInfo.currentNodeInstanceId= this.$route.query.currentNodeId;
          this.nodeBusinessInfo.businessId= this.$route.query.id;
          this.nodeBusinessInfo.businessMinId=this.$route.query.reportNo;
          this.nodeBusinessInfo.businessSubId=this.$route.query.statementNo;
          this.getMajorDetail();
          this.getCustomerCompany(0);
          this.getCustomerCompany(1);
          this.changeType();
          if (typeof(this.nodeBusinessInfo.currentNodeInstanceId)!='undefined' &&this.nodeBusinessInfo.currentNodeInstanceId!=null){
            this.getCurrentNodeInfo();
          }
          this.getAllotDepartment();
          this.getLandTargetList();
          this.getWorkflowLogs();
          this.getProductions(this.majorId,this.statementNo,this.reportNo);
          this.getPerformance(this.majorId,(this.reportNo==null?this.statementNo:this.reportNo));
          this.getFinanceInvoiceAmountInfo();
        
        }
      },
      productions: {
        handler(newValue, oldVal) {
            for (let p in oldVal){
              if (oldVal[p].evaluateAmount==null){
                oldVal[p].evaluateAmount = this.totalEvaluateValue;
              }
              if (oldVal[p].evaluateAcreage==null){
                oldVal[p].evaluateAcreage = this.totalAcreage;
              }
            }
            for (let d in newValue){
              newValue[d].evaluatePrice = ((newValue[d].evaluateAmount)/(newValue[d].evaluateAcreage)).toFixed(4);
            }
        },
        deep: true
      },
      nodeBusinessInfo: {
        handler(newValue, oldValue) {
          const board = this.$refs.board;
          if (board){
            board.getInstanceQueue();
          }
        },
        deep: true,
      }
    },

    data() {
      return {
        rules:{
          loanLimit:[{required: false,validator:isNumber,trigger:'blur'}],
          loanPeriod:[{required: false,validator:postiveInteger,trigger:'blur'}],
          bailorContactTel:[{required: true, message: '委托联系人电话不能为空', trigger: 'blur'},{validator:phoneNumber,trigger:'blur'}],
          ownerTel:[{required: false,validator:phoneNumber,trigger:'blur'}],
          phone:[{required: true, message: '联系电话不能为空', trigger: 'blur'},{validator:phoneNumber,trigger:'blur'}],
          // tmobile:[{required: true, message: '终端联系电话不能为空', trigger: 'blur'},{validator:phoneNumber,trigger:'blur'}],
          idNo:[{required: false,validator:isIdNumber,trigger:'blur'}],
          mobile:[{required: true, message: '联系电话不能为空', trigger: 'blur'},{validator:phoneNumber,trigger:'blur'}],
        },
        activeName:'workflowInfo',
        activeTagName: 'order',
        nodeBusinessInfo:{
            currentNodePermission:{
              commit:true,
              reversible:true,
              restartable:true,
              terminable:true,
              skippable:true,
          },
          mainBusiness:"MAJOR_BUSINESS",
          doWorkflow:false,
          businessSubId:null,
          businessId:null,
          businessMinId:null,
          currentNodeInstanceId:null,
          production:[],
          reportNos:[],
          currentNodeInstanceCode:null,
          ifCheckTask:true
        },
        major: {
          id:null,
          name:null,
          financial:false,
          allotType:"轮单",
          clientManagerId:this.$store.getters.userInfo.id,
          principalId:null,
          departmentId:null,
          businessObjectType:"土地",
          specialType:null,
          businessGener:null,
          evaluateAim:"抵押",
          entrustAgain:false,
          multipleRemoval:false,
          informalEvaluate:false,
          clienteleType:"企业",
          clienteleId:null,
          clienteleContactId:null,
          dispenseBenefit:false,
          nonnative:false,
          refinance:false,
          loanLimit:null,
          loanPeriod:null,
          loanNature:null,
          loanAim:null,
          loanExpire:false,
          bailor:null,
          bailorAddress:null,
          bailorContactName:null,
          bailorContactTel:null,
          owner:null,
          ownerTel:null,
          urgent:false,
          terminalClienteleId:null,
          terminalClienteleType:'',
          terminalClienteleContactId:null,
          remark:null,
          membersName:null,
          clientManager:this.$store.getters.userInfo.name,
          members:null,
          productionInfo:null,
          printing:null,
          printingRemark:null,
          cmobile:null,
          cdepartment:null,
          cduty:null,
          cclienteleName:null,
          cclienteleSubName:null,
          cclienteleContactName:null,
          tmobile:null,
          tterminalClienteleContactName:null,
          tterminalClienteleName:null,
          clienteleSubId:null,
        },
        currentNode:{
            flowId:null,
            pnodeId:null,
            nodeId:null,
            flowCode:null,
            nodeName:"",
            nodeCode:null,
            state:"",
            instanceId:null,
            sequence:null,
            tasks:[]
        },
        majorId:null,
        allotDepartment:[],
        principals:[],
        allUsers:[],
        evaluateLandData: [],
        landDialogVisible:false,
        houseDialogVisible:false,
        landTarget:{
          id:null,
          majorId:null,
          targetType:null,
          landType:null,
          useAcreage:null,
          rights:[],
          rightToUser:null,
          landCertificateNo:null,
          located:null,
          residueYears:null,
          plotRatio:null,
          landUse:null,
          principalId:null,
          sites:[],
          evaluateDate:null,
          remark:null,
          evaluateValue:null,
          evaluateMethods:[],
          housePurpose:[],
          owner:null,
          ownerNo:null,
          targetName:null,
          buildDoneYear:null,
          buildAcreage:null,
          landAcreage:null,
          planPurpose:null,
          realPurpose:null,
          siteLocated:null,
          useRightType:null
        },
        eMethods:[],
        houseUseTO:[],
        totalEvaluateValue:0,
        totalAcreage:0,
        feedback:{
          majorId:null,
          production:[],
          printing:null,
          printingRemark:null
        },
        workflowLogs:[],
        productions:[],
        waitingUpload:[],
        performance:{
          productionNo:null,
          productionType:null,
          principal:null,
          productionName:null,
          info:[]
          // id:null,
          // allots:[],
          // fatalMistake:null,
          // hardMistake:null,
          // normalMistake:null,
          // reason:null,
          // performanceAllot:null,
          // checkLoop:null,
        
        },
        saveOrderBtn:null,
        customerCompany:[],
        subCustomerCompany:[],
        customerContract:[],
        tCustomerCompany:[],
        tSubCustomerCompany:[],
        tCustomerContract:[],
       
        types:[],
        landType:[ "划拨土地价格评估",
          "土地利用总体规划、规划调整、项目规划",
          "城乡建设用地增减挂钩项目",
          "建设用地报件",
          "房屋征收、搬迁社会稳定风险评估",
          "社会稳定风险评价",
          "自然资源资产负债表项目",
          "航空摄影测量",
          "集体建设用地使用权价格评估",
          "土地复垦方案",
          "开发区土地集约利用评价",
          "城市土地集约利用评价",
          "土地基准地价评估",
          "土地标定地价评估",
          "土地地价动态监测",
          "建设项目节地评价",
          "集体承包土地的经营权流转租赁评估",
          "集体经营性建设用地出让地价、租赁地价评估",
          "集体经营性建设用地转让、出资、抵押评估",
          "农用地质量分等评估",
          "农用地定级和基准地价评估",
          "集体建设用地定级和基准地价评估",
          "工业用地集约利用评价",
          "征收农用地区片综合地价评估",
          "城市低效用地专项规划项目"],
        houseType :[ "司法鉴定中不动产损害赔偿评估",
          "税收部门征税房地产批量评估",
          "房地产投资项目可行性研究",
          "金融机构已设营业网点市场前景调查分析报告",
          "企业国有资产产权无偿划转操作方案咨询",
          "房地产区域市场研究",
          "商业银行押品快速变现价值的量化分析评估",
          "城市旧城改造、棚户区改造实施规划编制",
          "城市旧城改造、棚户区改造项目可行性研究",
          "城市更新项目规划编制",
          "城市旧城改造、棚户区改造征收、搬迁方案咨询",
          "城市更新项目可行性研究",
          "以财务报表为目的投资性物业价值评估（投资性房地产市场价值除外）",
          "住房租赁证券化评估",
          "房屋征收、搬迁社会稳定风险评估",
          "政府为出让土地成片大规模土地分等定级研究",
          "“城中村”房屋搬迁补偿评估",
          "经营性物业租金体系研究",
          "存量房地产再开发利用投资价值评估、咨询",
          "公共性、公益性房地产评估",
        ],
        createLinkmanVisible:false,
        linkmanFrom:{
          clienteleType:null,
          clienteleSubId:null,
          clienteleId:null,
          ccId:null,
          name:null,
          mobile:null,
          phone:null,
          sex:null,
          idNo:null,
          wechatNo:null,
          qqNo:null,
          school:null,
          industry:null,
          department:null,
          duty:null,
          terminal:false,
          description:null
        },
        createTerminalClientVisible:false,
        terminalClientFrom:{
          name:null,
          companyType:null,
          address:null,
          phone:null,
          terminal:true
        },
        createTlinkmanVisible:false,
        tLinkmanFrom:{
          ccId:null,
          name:null,
          mobile:null,
          phone:null,
          sex:null,
          idNo:null,
          wechatNo:null,
          qqNo:null,
          school:null,
          industry:null,
          department:null,
          duty:null,
          terminal:false,
          description:null
        },
        productionFinance: [],
        showQR:true
      }
    },
    created() {
        this.couldEdit = this.$route.query.couldEdit ==='true';
        this.showBtn = this.$route.query.showBtn ==='true';
        this.couldBack = this.$route.query.couldBack ==='true';
        this.reportNo = this.$route.query.reportNo;
        this.statementNo = this.$route.query.statementNo;
        this.saveOrderBtn = this.$route.query.saveOrderBtn;
        this.majorId = this.$route.query.id;
        this.currentNode.nodeName = this.$route.query.currentNodeName;
        this.nodeBusinessInfo.doWorkflow = this.$route.query.couldEdit ==='true';
        this.nodeBusinessInfo.currentNodeInstanceId= this.$route.query.currentNodeId;
        this.nodeBusinessInfo.businessId= this.$route.query.id;
        this.nodeBusinessInfo.businessMinId=this.$route.query.reportNo;
        this.nodeBusinessInfo.businessSubId=this.$route.query.statementNo;
        this.getMajorDetail();
        this.getCustomerCompany(0);
        this.getCustomerCompany(1);
        this.changeType();
        if (typeof(this.nodeBusinessInfo.currentNodeInstanceId)!='undefined' &&this.nodeBusinessInfo.currentNodeInstanceId!=null){
          this.getCurrentNodeInfo();
        }
        if (this.activeTagName === 'order'){
          this.getAllotDepartment();
          this.getLandTargetList();
        }
        if (this.activeTagName === 'evaluate'){
          if (this.major.businessObjectType==='土地'){
              this.getLandTargetList();
          }
        }
        if (this.activeTagName === 'workflowLog'){
          this.getWorkflowLogs();
        }
        if (this.activeTagName === 'producution'){
          this.getProductions(this.majorId,this.statementNo,this.reportNo);
        }
        if (this.activeTagName === 'quality'){
          this.getPerformance(this.majorId,(this.reportNo==null?this.statementNo:this.reportNo));
        }
        if (this.activeTagName === 'finance' && this.productionFinance.length == 0){
          this.getFinanceInvoiceAmountInfo();
        }

    },
    methods: {
      handleChange(){

      },
      handleClick(tab) {
        if (tab.name === 'order' && this.allotDepartment.length==0){
          this.getAllotDepartment();
        }
        if (tab.name === 'evaluate'){
          if ( this.evaluateLandData.length==0){
            this.getLandTargetList();
          }
        }
        if (tab.name === 'workflowLog'){
          if ( this.workflowLogs.length==0){
            this.getWorkflowLogs();
          }
        }
        if (tab.name === 'producution'){
          this.getLandTargetList();
          this.getProductions(this.majorId,this.statementNo,this.reportNo);
        }
        // if (tab.name === 'performance'){
        //   if ( this.performance.allots.length==0){
        //     this.getPerformance();
        //   }
        // }
        if (tab.name === 'quality'){
            this.getPerformance(this.majorId,(this.reportNo==null?this.statementNo:this.reportNo));
        }
        if (tab.name === 'finance' && this.productionFinance.length == 0){
          this.getFinanceInvoiceAmountInfo();
      }
      },
      handleCreate(){
        this.$refs.majorForm.validate(valid => {
          if (valid) {
            if (this.major.id) {
              this.$api.major.edit(this.major).then(res => {
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
                }
              })
            } else {
              if (this.major.terminalClienteleType==='个人'){
                  this.major.terminalClienteleContactId = this.major.terminalClienteleId;
                  this.major.terminalClienteleId = null;
              }
              this.$api.major.add(this.major).then(res => {
                if (res.code === 200) {
                  if (res.data && res.data.instanceNodeId && res.data.state){
                    this.$api.workflow.commit(res.data).then(result =>{
                        if (result.code === 200 && result.data){
                          this.$notify({
                            title: '成功',
                            message: '下单并提交流程成功',
                            type: 'success',
                            duration: 2000
                          });
                        }
                    })
                  }else{
                    this.$notify({
                            title: '成功',
                            message: '下单成功,需要手动提交流程。',
                            type: 'warning',
                            duration: 2000
                          });
                  }
                  const back = this.$route.query.back;
                  if (back) {
                    this.$router.push(back)
                  }
                }else{
                  this.$notify({
                            title: '失败',
                            message: '下单失败，请稍后再试。',
                            type: 'error',
                            duration: 2000
                          });
                }
              })
            }
          }else{
            console.log("error")
          }
        })
      },
      getMajorDetail(){
        if (this.majorId){
          let majorId= this.majorId;
          let reportNo = this.statementNo;
          this.$api.major.detail(this.majorId).then(res=>{
            if (res.code === 200){
                this.major = res.data;
                this.major.membersId = JSON.parse(this.major.members);
                if (this.major.feedbackInfo){
                  let feedbacks = JSON.parse(this.major.feedbackInfo);
                  if (reportNo){
                    this.feedback  = feedbacks.filter(function(e,index,array){
                        return e.reportNo === reportNo;
                    })[0]
                    if (!this.feedback){
                      this.feedback = feedbacks.filter(function(e,index,array){
                        return e.majorId == majorId;
                    })[0];
                    }

                  }else {
                    this.feedback = feedbacks.filter(function(e,index,array){
                        return e.majorId == majorId;
                    })[0];
                  }
                  this.nodeBusinessInfo.production = this.feedback.production;
                }
                this.getPrincipalByAllotDepartment();
                this.getAllUser();
                // if (typeof(this.nodeBusinessInfo.currentNodeInstanceId)!='undefined'){
                //   this.$refs.board.getInstanceQueue();
                // }
            }
          })
        }
        
      },
      getAllotDepartment(){
        this.$api.department.allot('MAJOR_BUSINESS').then(res=>{
          if (res.code === 200){
              this.allotDepartment = res.data;
          }
        })
      },
      getPrincipalByAllotDepartment(){
        let departmentId = this.major.departmentId;
        if (departmentId){
          this.$api.user.usersByDepartmentId(departmentId).then(res=>{
            if (res.code === 200){
                this.principals = res.data;
            }
          })
        }
      },
      getAllUser() {
        this.$api.user.simpleAll().then(res => {
          if (res.code === 200) {
              this.allUsers = res.data;
          } 
        })
      },
      getCurrentNodeInfo(){
        if (this.majorId){
          this.$api.workNodeInstance.currentNode({"mainBusiness":"MAJOR_BUSINESS","businessId":this.majorId,"businessSubId":this.statementNo,"businessMinId":this.reportNo}).then(res=>{
            if (res.code === 200){
                if (res.data){
                  this.currentNode = res.data;
                  this.nodeBusinessInfo.currentInstanceNodeId = res.data.instanceId;
                  this.nodeBusinessInfo.currentNodeInstanceCode = res.data.nodeCode;
                }
                if (this.currentNode.nodeCode ==='CHECK_STATEMENT' ||
                    this.currentNode.nodeCode ==='CHECK_REPORT' ||
                    this.currentNode.nodeCode ==='CHECK_LETTER' ){
                    this.getPerformance();
                }
  
                if (this.currentNode.nodeCode ==='WRITE_STATEMENT' ||
                    this.currentNode.nodeCode ==='WRITE_REPORT' ||
                    this.currentNode.nodeCode ==='WRITE_LETTER' ){
                      if (this.major.businessObjectType==='土地'){
                          this.getLandTargetList();
                      }
                }
            }
          })
        }
      },
      getLandTargetList(){
         if (this.majorId){
            const obj = new Object();
            obj.majorId = this.majorId;
            if (this.statementNo){
              obj.statementNo =this.statementNo;
            }
            if (this.reportNo ){
              console.log(this.feedback.production)
              if (this.feedback.production.includes("REPORT")){
              obj.reportNo = this.reportNo;
              }else{
                obj.letterNo = this.reportNo;
              }
            }
            
            this.$api.majorTarget.list(obj).then(res=>{
              if (res.code === 200){
                  this.evaluateLandData = res.data;
                  if (this.evaluateLandData.length===1){
                    this.statementNo = this.evaluateLandData[0].statementNo
                  }
                  let totalAcreages = [];
                  let totalValues = this.evaluateLandData.map(item=>item.evaluateValue);
                  if (this.major.businessObjectType === '土地'){
                    totalAcreages = this.evaluateLandData.map(item=>item.useAcreage);
                  }else{
                    totalAcreages = this.evaluateLandData.map(item=>item.buildAcreage);
                  }
                  for (let i in totalValues){
                    if (totalValues[i]!=null && typeof(totalValues[i])!='undefined'){
                      this.totalEvaluateValue += totalValues[i];
                    }
                  }
                  for (let j in totalAcreages){
                    if (totalAcreages[j]!=null && typeof(totalAcreages[j])!='undefined'){
                      this.totalAcreage += totalAcreages[j];
                    }
                  }
              }
            })
         }
      },
      getWorkflowLogs(){
        console.log(this.statementNo);
        let param = {
          businessId:this.majorId,
          businessSubId: this.statementNo,
          businessMinId:this.reportNo,
          businessType:'MAJOR_BUSINESS'
        }
        this.$api.workflowLog.list(param).then(res=>{
          if (res.code === 200){
              this.workflowLogs = res.data;
          }
        })
      },
      getProductions(majorId,statementNo,reportNo){
      
          if (majorId){
            let pro = {
              majorId:majorId,
              reportNo:reportNo,
              statementNo:statementNo,
              sequence:this.currentNode.sequence,
            }
            this.$api.majorProduction.pendingList(pro).then(res=>{
              if (res.code === 200 ){
                 this.productions = res.data;
                 this.nodeBusinessInfo.doSecondCheck =  this.productions[0].secondCheck;
                 this.nodeBusinessInfo.doThirdCheck =  this.productions[0].thirdCheck;
                   //回显上传的文档
                   for (let p in this.productions){
                      let d = JSON.parse(this.productions[p].filePath);
                      let c = JSON.parse(this.productions[p].checkedFilePath);
                      for (let q in d){
                        this.productions[p].filePathList.push({name: d[q].name, url:d[q].url,index:p});
                      }
                      for (let cc in c){
                        this.productions[p].filePathList1.push({name: c[cc].name, url:c[cc].url,index:p});
                      }
                  }
              }
            })
          }
      },
      getPerformance(majorId,productionNo){
        if (majorId && productionNo){
            const obj = new Object();
            obj.businessType = 'MAJOR_BUSINESS';
            obj.majorId = majorId;
            obj.productionNo = productionNo;
            obj.checkLoop = this.checkLoop;
            this.$api.businessProductionPerformance.detail(obj).then(res=>{
              if (res.code === 200){
                  this.performance = res.data;
              }
            })
        }
      },
      landTargetEdit(row){
          if (row.targetType ==='土地'){
            this.landDialogVisible = true;
          }else{
            this.houseDialogVisible = true;
          }
          
          this.landTarget = row;
          // this.landTarget.rights = JSON.parse(this.landTarget.rightType);
          // this.landTarget.sites = JSON.parse(this.landTarget.siteIds);
          this.landTarget.rights = this.landTarget.rightType;
          this.landTarget.sites = this.landTarget.siteIds;
          this.eMethods = [];
          this.houseUseTO = [];
          if (JSON.parse(this.landTarget.evaluateMethods)){
            this.eMethods = JSON.parse(this.landTarget.evaluateMethods);
          }
          if (JSON.parse(this.landTarget.housePurpose)){
            this.houseUseTO = JSON.parse(this.landTarget.housePurpose);
          }
      },
      aliasProductionType(code){
        let name = '价值意见书';
        if (code ==='REPORT'){
            name = '报告';
        }
        if (code ==='LETTER'){
          name = '复评函';
        }
          return name;
      },
      getCustomerCompany(val){
        if (val ===0){
          let simpleAll = new Object();
          simpleAll.terminal=val;
          this.$api.customerCompany.simpleAll(simpleAll).then(res=>{
              if (res.code === 200){
                  this.customerCompany = res.data;
              }
          })
        }else{
          let simpleAll = new Object();
          simpleAll.terminal=val;
          this.$api.customerCompany.simpleAll(simpleAll).then(res=>{
              if (res.code === 200){
                  this.tCustomerCompany = res.data;
              }
          })
        }
      },
      findSubClientele(val){
        if (val===0){
          this.major.cmobile = null;
          this.major.cDepartment = null;
          this.major.cDuty = null;
          if (this.major.clienteleId){
            let simpleAll = new Object();
              simpleAll.terminal=val;
              simpleAll.parentId= this.major.clienteleId;
              this.$api.customerCompany.simpleAll(simpleAll).then(res=>{
              if (res.code === 200){
                  this.subCustomerCompany = res.data;
                  if (this.major.clienteleSubId){
                    this.major.clienteleSubId = null
                  }
                  if (this.major.clienteleContactId){
                    this.major.clienteleContactId = null
                    this.customerContract =[];
                  }
              }
            })
          }
        }else {
          if (this.major.terminalClienteleType==='企业' && this.major.terminalClienteleId){
            let simpleAll = new Object();
              simpleAll.terminal=val;
              simpleAll.ccId = this.major.terminalClienteleId;
              this.$api.customerLinkman.simpleAll(simpleAll).then(res=>{
              if (res.code === 200){
                this.tCustomerContract =res.data;
              }
              this.major.terminalClienteleContactId = null;
              this.major.tmobile = null;
            })
          }else{
            if (this.major.terminalClienteleType==='个人' && this.major.terminalClienteleId){
                this.$api.customerLinkman.detail(this.major.terminalClienteleId).then(res=>{
                    if (res.code === 200){
                      this.major.tmobile = res.data.mobile;
                    }
                })
            }
          }
        }
      },
      getCustomerContract(){
        this.major.cmobile = null;
          this.major.cDepartment = null;
          this.major.cDuty = null;
        if (this.major.clienteleSubId){
            let simpleAll = new Object();
            simpleAll.ccId = this.major.clienteleSubId
            this.$api.customerLinkman.simpleAll(simpleAll).then(res=>{
            if (res.code === 200){
                this.customerContract = res.data;
                if (this.major.clienteleContactId){
                  this.major.clienteleContactId = null
                }
            }
          })
        }
      },
      changeCustomerType(val){
        if (val ===0){
            this.major.cmobile = null;
            this.major.cDepartment = null;
            this.major.cDuty = null;
          if (this.major.clienteleId){
            this.major.clienteleId = null;
            this.customerCompany=[]
          }
          if (this.major.clienteleSubId){
            this.major.clienteleSubId = null;
            this.subCustomerCompany=[]
          }
          if (this.major.clienteleContactId){
            this.major.clienteleContactId = null;
            this.customerContract =[]
          }
          if (this.major.clienteleType==='个人'){
            let simpleAll = new Object();
            simpleAll.ccId = 1;
            simpleAll.terminal = 0;
            this.$api.customerLinkman.simpleAll(simpleAll).then(res=>{
              if (res.code === 200){
                  this.customerContract = res.data;
                  if (this.major.clienteleContactId){
                    this.major.clienteleContactId = null
                  }
              }
            })
          }
          if (this.major.clienteleType==='企业'){
              this.getCustomerCompany(0)
              this.major.clienteleContactId = null
              this.customerContract =[];
          }
        }else {
          if (this.major.terminalClienteleType==='个人'){
            let simpleAll = new Object();
            simpleAll.ccId = 2;
            simpleAll.terminal = 1;
            this.$api.customerLinkman.simpleAll(simpleAll).then(res=>{
              if (res.code === 200){
                  this.tCustomerCompany = res.data;
                  if (this.major.terminalClienteleId){
                    this.major.terminalClienteleContactId = null
                    this.major.terminalClienteleId = null
                    this.tCustomerContract =[];
                    this.major.tmobile = null;
                  }
              }
            })
          }
          if (this.major.terminalClienteleType==='企业'){
              this.major.terminalClienteleContactId = null
              this.major.terminalClienteleId = null;
              this.major.tmobile = null;
              this.tCustomerContract =[];
              this.getCustomerCompany(1)
             
          }
          if (this.major.terminalClienteleType===''){
            this.major.terminalClienteleContactId = null;
            this.major.terminalClienteleId = null;
            this.major.tmobile = null;
            this.tCustomerCompany= [];
            this.tCustomerContract =[];
          }
        }
      },
      getContactInfo(){
        if (this.major.clienteleContactId){
            this.$api.customerLinkman.detail(this.major.clienteleContactId).then(res=>{
              if (res.code === 200){
                this.major.cmobile = res.data.mobile;
                this.major.cDepartment = res.data.department
                this.major.cDuty = res.data.duty
              }
                
            })
        }
      },
      getTcontactInfo(){
        if (this.major.terminalClienteleContactId){
            this.$api.customerLinkman.detail(this.major.terminalClienteleContactId).then(res=>{
                if (res.code === 200){
                  this.major.tmobile = res.data.mobile;
                }
            })
        }
      },
      changeType(){
        if (this.major.businessGener){
            this.major.businessGener = null;
        }
        if (this.major.businessObjectType==='土地'){
            this.types = this.landType;
        }
        if (this.major.businessObjectType==='房地产'){
            this.types = this.houseType;
        }
      },
      createLinkman(val){
          this.createLinkmanVisible = true;
          this.linkmanFrom.terminal = val;
          this.linkmanFrom.clienteleType = this.major.clienteleType;
          this.linkmanFrom.clienteleId = this.major.clienteleId;
          this.linkmanFrom.clienteleSubId = this.major.clienteleSubId;
          if (this.major.clienteleType==='企业'){
            this.linkmanFrom.ccId = this.major.clienteleSubId
          }else{
            this.major.clienteleSubId = 1;
            this.linkmanFrom.ccId = this.major.clienteleSubId;
          }
          
      },
      createTlinkman(){
          this.createTlinkmanVisible = true;
          this.tLinkmanFrom.terminal = 1;
          if (this.major.terminalClienteleType==='企业'){
                 this.tLinkmanFrom.ccId = this.major.terminalClienteleId;
          }else{
            this.tLinkmanFrom.ccId = 1;
          }
      },
      saveLinkman(){
          this.$refs.linkmanFrom.validate(valid =>{
            if (valid){
                this.$api.customerLinkman.add(this.linkmanFrom).then(res=>{
                  if (res.code ===200){
                      this.$notify({
                        title: '成功',
                        message: '新增客户联系人成功',
                        type: 'success',
                        duration: 2000
                      });
                  }
                  this.getCustomerContract();
                  setTimeout(()=>{
                    this.major.clienteleContactId = this.customerContract[0].id;
                    this.getContactInfo();
                  },2000)
                  this.createLinkmanVisible=false;
                })
            }
          })
      },
      saveTlinkman(){
        this.$refs.tLinkmanFrom.validate(valid =>{
            if (valid){
                if (!this.tLinkmanFrom.ccId){
                  this.tLinkmanFrom.ccId = 2;
                  this.tLinkmanFrom.terminal = 1;
                }
                this.$api.customerLinkman.add(this.tLinkmanFrom).then(res=>{
                  if (res.code ===200){
                      this.$notify({
                        title: '成功',
                        message: '新增终端联系人成功',
                        type: 'success',
                        duration: 2000
                      });
                  }
                  if (this.major.terminalClienteleType==='企业'){
                    this.findSubClientele(1);
                    setTimeout(()=>{
                      this.major.terminalClienteleContactId = this.tCustomerContract[0].id;
                      this.getTcontactInfo();
                    },2000)
                  }
                  if (this.major.terminalClienteleType==='个人'){
                    let simpleAll = new Object();
                    simpleAll.ccId = 2;
                    simpleAll.terminal = 1;
                    this.$api.customerLinkman.simpleAll(simpleAll).then(res=>{
                      if (res.code === 200){
                          this.tCustomerCompany = res.data;
                      }
                    })
                    setTimeout(()=>{
                      this.major.terminalClienteleId = this.tCustomerCompany[0].id;
                      this.major.terminalClienteleContactId = this.tCustomerCompany[0].id;
                      this.getTcontactInfo();
                    },2000)
                  }
                  
                  this.createTlinkmanVisible=false;
                })
            }
          })
      },
      createTerminalClient(){
        if (this.major.terminalClienteleType==='企业'){
          this.createTerminalClientVisible = true;
        }else{
          this.createTlinkmanVisible = true;
        }
        
      },
      saveClient(){
          this.$refs.terminalClientFrom.validate(valid =>{
            if (valid){
                this.$api.customerCompany.add(this.terminalClientFrom).then(res=>{
                  if (res.code ===200){
                      this.$notify({
                        title: '成功',
                        message: '新增客户成功',
                        type: 'success',
                        duration: 2000
                      });
                  }
                  this.getCustomerCompany(1);
                  this.major.terminalClienteleContactId = null;
                  this.major.tmobile = null;
                  this.tCustomerContract =[];
                  setTimeout(()=>{
                    this.major.terminalClienteleId  = this.tCustomerCompany[0].id;
                    this.findSubClientele(1);
                  },2000)
                  this.createTerminalClientVisible=false;
                })
            }
          })
      },
      getFinanceInvoiceAmountInfo(){
        const obj = new Object();
        obj.majorId= this.majorId;
        obj.productionNos = [];
        if (this.statementNo){
          obj.productionNos.push(this.statementNo);
        }
        if (this.reportNo){
          obj.productionNos.push(this.reportNo);
        }
        this.$api.financeInvoice.getInvoiceAmountInfo(obj).then(res => {
          if (res.code === 200) {
            this.productionFinance = res.data;
          }
        })
      },
      printQR(path){
        if (path){
          let routeUrl = this.$router.resolve({
            path: `/print/code?path=${path}`
          });
          window.open(routeUrl.href, '_blank');
        }
      },
    
      handleAttachmentPreview(file) {
      // window.open(file.url)
        this.officeFileView(file.name, file.url);
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
              mode: 'view'
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
      goBack() {
        const back = this.$route.query.back
        if (back) {
          this.$router.push(back)
        }
      },
    },

  }
</script>
<style lang="scss" scoped>
  .row-style{
    margin-left:40px;
    margin-right:1%;
  }


  .task-class{
    /deep/ .el-form-item__label {
      color: red;
    }
  }
  .evaluate-table{
    margin-left:40px;
    margin-top:35px;
  }
  .pane-class{
    width:100%;
    overflow-y: scroll;
  }
  /deep/ .cell-class{
    font-size:13px;
    color:black;
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
  .performance-avatar-wrapper{
    width: 50px;
    height: 50px;
    position: relative;
    border-radius: 50%;
    background-color: rgba(129,216,207,1);
    text-align: center;
    line-height: 50px;
  }
  .perfomance-icon-title{
    font-size: 20px;
    color: rgba(255, 255, 255, 1);
    display: inline-block;
    position: relative;
    top: -2px;
  }
  /deep/.createLinkman {
      border-radius: 10px;
    }
.qr-code-div{
      background-color: RGB(245, 245, 245);
  width: 150px;
  height: 150px;
  position: absolute;
  display: flex;
  justify-content: center;
  align-items: center;
}
.qr-code{
  width: 150px;
  height: 150px;
  z-index: 999;
  border: 1px #919398 solid;
  border-radius: 5px;
}
.qr-code-div:hover {
  cursor: pointer;
  // background-color:RGB(220,220,220); 
  // width: 205px; 
  // height: 205px; 

}
a:hover{
  cursor: pointer;
  color: #5a9cf8
}

.button-area {
  position: absolute;
  top: 18px;
  right: 50px;
}
</style>
