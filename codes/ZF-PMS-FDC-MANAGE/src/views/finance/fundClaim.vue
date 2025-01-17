<template>
  <div class="app-container">
    <div class="title-container">
      <breadcrumb id="breadcrumb-container" class="breadcrumb-container" />
    </div>
    <y-page-list-layout :page-list="pageData" :page-para="listQuery" :get-page-list="getList">
      <template slot="left">
        <PermissionButton menu-code="_views_finance_real_fund_create" class-name="filter-item" type="success"
          icon="el-icon-circle-plus-outline" :page-jump="false" round style="float: left"
          @click="createdRealFundDialog = true" />
        <el-input v-model="listQuery.keyword" placeholder="关键字搜索..." clearable
          style="margin-left: 20px;width: 500px;float: left;">
        </el-input>
        <el-date-picker style="margin-left: 20px;float: left;" v-model="payDate" type="daterange" align="center"
          unlink-panels range-separator="至" start-placeholder="到账时日（开始）" end-placeholder="到账时日（结束）"
          :picker-options="pickerOptions" value-format="yyyy-MM-dd">
        </el-date-picker>
        <el-date-picker style="margin-left: 20px;float: left;" v-model="orderDate" type="daterange" align="center"
          unlink-panels range-separator="至" start-placeholder="录入时间（开始）" end-placeholder="录入时间（结束）"
          :picker-options="pickerOptions" value-format="yyyy-MM-dd">
        </el-date-picker>
        <el-button class="filter-item" style="margin-left: 10px;float: left;" type="primary" @click="searchList" round>搜索
        </el-button>
        <el-button class="filter-item" style="float: left;" round type="success" @click="resetSearch()">重置
        </el-button>
      </template>
      <parentTable :data="pageData.records" slot="table" style="width: 100%;">
        <el-table-column label="付款方" align="center" width='200'>
          <template slot-scope="{row}">
            <span>{{ row.payer }}</span>
          </template>
        </el-table-column>
        <el-table-column label="付款银行" align="center" width='130'>
          <template slot-scope="{row}">
            <span>{{ row.payerBank }}</span>
          </template>
        </el-table-column>
        <el-table-column label="付款金额(元)" align="center">
          <template slot-scope="{row}">
            <span>{{ row.amount }}</span>
          </template>
        </el-table-column>
        <el-table-column label="到账时日" align="center">
          <template slot-scope="{row}">
            <span>{{ row.payDatetime }}</span>
          </template>
        </el-table-column>
        <el-table-column label="已认领(元)" align="center">
          <template slot-scope="{row}">
            <span style="color:green">{{ row.claimAmount }}</span>
          </template>
        </el-table-column>
        <el-table-column label="未认领(元)" align="center">
          <template slot-scope="{row}">
            <span style="color:red">{{ row.notClaimAmount }}</span>
          </template>
        </el-table-column>
        <el-table-column label="录入人" align="center">
          <template slot-scope="{row}">
            <span>{{ row.creator }}</span>
          </template>
        </el-table-column>
        <el-table-column label="录入时间" align="center" width='160'>
          <template slot-scope="{row}">
            <span>{{ row.created }}</span>
          </template>
        </el-table-column>
        <el-table-column label="全部认领" align="center">
          <template slot-scope="{row}">
            <i v-if="row.notClaimAmount <= 0" style="font-size:20px; color:green" class="el-icon-success"></i>
            <i v-else style="font-size:20px; color:RGB(255,106,106)" class="el-icon-warning"></i>
          </template>
        </el-table-column>
        <el-table-column label="备注" align="center" width='170'>
          <template slot-scope="{row}">
            <span>{{ row.remark }}</span>
          </template>
        </el-table-column>
        <el-table-column label="认领" align="center" width="200" fixed="right">
          <template slot-scope="{row}">
            <el-tooltip effect="light" :disabled="!row.recommends.includes('MAJOR_BUSINESS')" class="item"  content="推荐" placement="top-start">
              <el-button type="text" @click="claimMajor(row)" :style="row.recommends.includes('MAJOR_BUSINESS')?'color:red':''">大中型</el-button>
            </el-tooltip>
            <el-tooltip effect="light" :disabled="!row.recommends.includes('PERSONAL_BUSINESS')" class="item"  content="推荐" placement="top-start">
              <el-button type="text" @click="claimPersonal(row)" :style="row.recommends.includes('PERSONAL_BUSINESS')?'color:red':''">个贷</el-button>
            </el-tooltip>
            <el-tooltip effect="light" :disabled="!row.recommends.includes('ASSET_BUSINESS')" class="item"  content="推荐" placement="top-start">
              <el-button type="text" @click="claimAssets(row)" :style="row.recommends.includes('ASSET_BUSINESS')?'color:red':''">资产</el-button>
            </el-tooltip>
          </template>
        </el-table-column>
      </parentTable>
    </y-page-list-layout>

    <el-dialog :visible.sync="allotAmountDialog" width="45%" center custom-class="doWarehouseClass"
      @closed="cleanClaimDTO">
      <el-form v-model="claimOrders" ref="claimOrdersForm">
        <el-row>
          <el-col :xs="24" :sm="12" :lg="21" :span="6">
            <div style="font-size:20px;">
              <span>此笔实收款剩余：</span><span style="color:red;font-weight:bold">{{ notClaimAmount }}￥</span>
            </div>
          </el-col>
          <el-col :xs="24" :sm="12" :lg="3" :span="6">
            <el-button style="float:right" type="success" @click="doClaimMajorToOrder()">确认认领</el-button>
          </el-col>
        </el-row>
        <el-card style="margin-top:20px" v-for="(p, id) in claimOrders" shadow="hover">
          <div slot="header" class="clearfix">
            <el-row>
              <el-col :xs="24" :sm="12" :lg="24" :span="6">
                <el-form-item label="订单名称" prop="orderName" label-width="140px" class="postInfo-container-item">
                  <el-input :value="p.orderName" type="text" class="filter-item" disabled readonly />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :xs="24" :sm="12" :lg="12" :span="6">
                <span class="redPoint">*</span>
                <el-form-item label="本次认领金额：" prop="thisTimeAmount" label-width="140px" class="postInfo-container-item">
                  <el-input-number :precision="2" :min="0" :value-on-clear="0" v-model.number="p.thisTimeAmount"
                    style="width:100%" class="filter-item" />
                </el-form-item>
              </el-col>
              <el-col :xs="24" :sm="12" :lg="12" :span="6">
                <el-form-item label="订单号" prop="orderId" label-width="140px" class="postInfo-container-item">
                  <el-input :value="p.orderId" type="text" class="filter-item" disabled readonly />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :xs="24" :sm="12" :lg="12" :span="6">
                <el-form-item label="已认领金额：" prop="realAmount" label-width="140px" class="postInfo-container-item">
                  <el-input :value="p.realAmount" type="text" class="filter-item" disabled readonly>
                    <template slot="append">元</template>
                  </el-input>
                </el-form-item>
              </el-col>
              <el-col :xs="24" :sm="12" :lg="12" :span="6">
                <el-form-item label="订单应收金额：" prop="shouldAmount" label-width="140px" class="postInfo-container-item">
                  <el-input :value="p.shouldAmount" type="text" class="filter-item" disabled readonly>
                    <template slot="append">元</template>
                  </el-input>
                </el-form-item>
              </el-col>
            </el-row>
          </div>
        </el-card>
      </el-form>
    </el-dialog>
    <el-dialog :visible.sync="allotProdDialog" width="45%" center custom-class="doWarehouseClass" @closed="cleanClaimDTO">
      <el-form v-model="claimOrders" ref="claimOrdersForm">
        <el-row>
          <el-col :xs="24" :sm="12" :lg="21" :span="6">
            <div style="font-size:20px;">
              <span>此笔实收款剩余：</span><span style="color:red;font-weight:bold">{{ notClaimAmount }}￥</span>
            </div>
          </el-col>
          <el-col :xs="24" :sm="12" :lg="3" :span="6">
            <el-button style="float:right" type="success" @click="doClaimMajor()">确认认领</el-button>
          </el-col>
        </el-row>
        <el-card style="margin-top:20px" v-for="(p, id) in claimOrders" shadow="hover">
          <div slot="header" class="clearfix">
            <el-row>
              <el-col :xs="24" :sm="12" :lg="24" :span="6">
                <el-form-item label="项目名称" prop="name" label-width="140px" class="postInfo-container-item">
                  <el-input :value="p.name" type="text" class="filter-item" disabled readonly />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :xs="24" :sm="12" :lg="12" :span="6">
                <span class="redPoint">*</span>
                <el-form-item label="本次认领金额：" prop="thisTimeAmount" label-width="140px" class="postInfo-container-item">
                  <el-input-number :precision="2" :min="0" :value-on-clear="0" v-model.number="p.thisTimeAmount"
                    style="width:100%" class="filter-item" />
                </el-form-item>
              </el-col>
              <el-col :xs="24" :sm="12" :lg="12" :span="6">
                <el-form-item label="报告号" prop="reportNo" label-width="140px" class="postInfo-container-item">
                  <el-input :value="p.reportNo" type="text" class="filter-item" disabled readonly />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :xs="24" :sm="12" :lg="12" :span="6">
                <el-form-item label="已认领金额：" prop="realAmount" label-width="140px" class="postInfo-container-item">
                  <el-input :value="p.realAmount" type="text" class="filter-item" disabled readonly>
                    <template slot="append">元</template>
                  </el-input>
                </el-form-item>
              </el-col>
              <el-col :xs="24" :sm="12" :lg="12" :span="6">
                <el-form-item label="应收金额：" prop="shouldAmount" label-width="140px" class="postInfo-container-item">
                  <el-input :value="p.shouldAmount" type="text" class="filter-item" disabled readonly>
                    <template slot="append">元</template>
                  </el-input>
                </el-form-item>
              </el-col>
            </el-row>
          </div>
        </el-card>
      </el-form>
    </el-dialog>
    <el-dialog :visible.sync="myMajorOrderDialog" width="95%" center custom-class="doWarehouseClass" @closed="getList">
      <y-page-list-layout :page-list="myMajorPageData" :page-para="majorListQuery" :get-page-list="getMyMajor">
        <template slot="left">
          <!-- <el-switch style="width: 270px;float: left;margin-top: 5px;" v-model="toProd" active-color="#13ce66"
            inactive-color="#ff4949" active-text="认领到产品" inactive-text="认领到订单" disabled>
          </el-switch> -->
          <el-input v-model="majorListQuery.name" placeholder="订单/项目名称" clearable style="width: 200px;float: left;">
          </el-input>
          <el-input v-model="majorListQuery.orderId" placeholder="订单号" clearable
            style="margin-left: 20px;width: 200px;float: left;">
          </el-input>
          <el-input v-model="majorListQuery.reportNo" placeholder="产品编号" clearable
            style="margin-left: 20px;width: 200px;float: left;">
          </el-input>
          <el-select v-model="majorListQuery.eDeclareResult" placeholder="评估人员申报" clearable
            style="margin-left: 20px;width: 150px;float: left;">
            <el-option value="审核通过">审核通过</el-option>
            <el-option value="审核中">审核中</el-option>
            <el-option value="未申报">未申报</el-option>
            <el-option value="审核拒绝">审核拒绝</el-option>
          </el-select>
          <el-select v-model="majorListQuery.mDeclareResult" placeholder="市场人员申报" clearable
            style="margin-left: 20px;width: 150px;float: left;">
            <el-option value="审核通过">审核通过</el-option>
            <el-option value="审核中">审核中</el-option>
            <el-option value="未申报">未申报</el-option>
            <el-option value="审核拒绝">审核拒绝</el-option>
          </el-select>
          <el-select v-model="majorListQuery.hasRealAmount" placeholder="收款状态" clearable
            style="margin-left: 20px;width: 150px;float: left;">
            <el-option label="未收款" value="false"></el-option>
            <el-option label="已收款" value="true"></el-option>
          </el-select>
          <el-button class="filter-item" style="margin-left: 10px;float: left;" type="primary"
            @click="getMajorOrderOrProd()" round>搜索
          </el-button>
          <el-button class="filter-item" style="float: left;" round type="success" @click="resetMajorOrderOrProd()">重置
          </el-button>
          <el-button style="float: left;" round type="danger" @click="openClaimDialog()">选择认领</el-button>
        </template>
        <parentTable v-show="!toProd" ref="order" :clickRowToPick="false" :data="myMajorPageData.records" slot="table"
          style="width: 100%; height: 50%;" :selectionChange="handleSelectionChange">
          <el-table-column align="center" type="selection" claimOrders width="100" border="true"
            :selectable="checkSelectable">
          </el-table-column>
          <el-table-column label="订单名称" align="center" show-overflow-tooltip>
            <template slot-scope="{row}">
              <span>{{ row.orderName }}</span>
            </template>
          </el-table-column>
          <el-table-column label="订单号" align="center" show-overflow-tooltip width='200'>
            <template slot-scope="{row}">
              <span>{{ row.orderId }}</span>
            </template>
          </el-table-column>
          <el-table-column label="委托人" align="center" show-overflow-tooltip width='100'>
            <template slot-scope="{row}">
              <span>{{ row.bailor }}</span>
            </template>
          </el-table-column>
          <el-table-column label="产权人" align="center" show-overflow-tooltip width='100'>
            <template slot-scope="{row}">
              <span>{{ row.owner }}</span>
            </template>
          </el-table-column>
          <el-table-column label="客户名称" align="center" width='120' show-overflow-tooltip>
            <template slot-scope="{row}">
              <span>{{ row.clienteleName }}</span>
            </template>
          </el-table-column>
          <el-table-column label="业务来源" align="center" width='120' show-overflow-tooltip>
            <template slot-scope="{row}">
              <span>{{ row.clienteleSubName }}</span>
            </template>
          </el-table-column>
          <el-table-column label="客户联系人" align="center" width='120' show-overflow-tooltip>
            <template slot-scope="{row}">
              <span>{{ row.clienteleContactName }}</span>
            </template>
          </el-table-column>
          <el-table-column label="应收款(￥)" align="center" width='120'>
            <template slot-scope="{row}">
              <span>{{ row.shouldAmount }}</span>
            </template>
          </el-table-column>
          <el-table-column label="实收款(￥)" align="center" width='120'>
            <template slot-scope="{row}">
              <span style="color:red;font-weight:bold">{{ row.realAmount==null? 0 : row.realAmount }}</span>
            </template>
          </el-table-column>
          <el-table-column align="center" width='140' label="市场人员申报提成">
            <template slot-scope="{row}">
              <el-tag :type="row.mdeclareResult ? (row.mdeclareResult === '审核通过' ? 'success' : 'info') : 'danger'">{{
                row.mdeclareResult ? row.mdeclareResult : '未申报' }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column align="center" width='140' label="评估人员申报提成">
            <template slot-scope="{row}">
              <el-tag :type="row.edeclareResult ? (row.edeclareResult === '审核通过' ? 'success' : 'info') : 'danger'">{{
                row.edeclareResult ? row.edeclareResult : '未申报' }}</el-tag>
            </template>
          </el-table-column>
        </parentTable>
        <el-table v-show="toProd" ref="prod" :data="myMajorPageData.records" slot="table"
          :header-row-style="{ color: '#333333' }" style="
            border-left: 1px solid #ebeced;
            border-right: 1px solid #ebeced;
            color: #333333; font-size: 14px
          " @selection-change="handleSelectionChange"  :rowClassName="tableRowClassName">
          <el-table-column align="center" type="selection" claimOrders border="true" :selectable="checkSelectable">
          </el-table-column>
          <el-table-column align="center"  width='100'>
            <template slot-scope="{row}">
              <span style="color:red">
                {{ row.invoiceAmount === notClaimAmount && row.realAmount === null ? '推荐匹配' : '' }}
              </span>
            </template>
          </el-table-column>
          <el-table-column label="金融类型" align="center" width='100'>
            <template slot-scope="{row}">
              <span>{{ row.financial == true ? '金融' : '非金融' }}</span>
            </template>
          </el-table-column>
          <el-table-column label="项目名称" align="center" show-overflow-tooltip>
            <template slot-scope="{row}">
              <span>{{ row.name }}</span>
            </template>
          </el-table-column>
          <el-table-column label="订单编号" align="center" width='160'>
            <template slot-scope="{row}">
              <span>{{ row.orderId }}</span>
            </template>
          </el-table-column>
          <el-table-column label="产品类型" align="center" width='80'>
            <template slot-scope="{row}">
              <span>{{ row.production }}</span>
            </template>
          </el-table-column>
          <el-table-column label="产品编号" align="center" width='200'>
            <template slot-scope="{row}">
              <span>{{ row.reportNo }}</span>
            </template>
          </el-table-column>
          <el-table-column label="客户名称" align="center" width='100'>
            <template slot-scope="{row}">
              <span>{{ row.clienteleName }}</span>
            </template>
          </el-table-column>
          <el-table-column label="收费标准(￥)" align="center" width='100'>
            <template slot-scope="{row}">
              <span>{{ row.standardAmount }}</span>
            </template>
          </el-table-column>
          <el-table-column label="应收款(￥)" align="center" width='100'>
            <template slot-scope="{row}">
              <span>{{ row.shouldAmount?row.shouldAmount:row.orderShouldAmount }}</span>
            </template>
          </el-table-column>
          <el-table-column label="实收款(￥)" align="center" width='100'>
            <template slot-scope="{row}">
              <span style="color:red;font-weight:bold">{{ row.realAmount==null? 0 : row.realAmount }}</span>
            </template>
          </el-table-column>
          <el-table-column label="开票金额(￥)" align="center" width='80'>
            <template slot-scope="{row}">
              <span >{{ row.invoiceAmount?row.invoiceAmount:'--'}}</span>
            </template>
          </el-table-column>
          <el-table-column label="市场人员" align="center" width='100'>
            <template slot-scope="{row}">
              <span>{{ row.clientManager }}</span>
            </template>
          </el-table-column>
          <el-table-column align="center" width='140' label="市场人员申报提成">
            <template slot-scope="{row}">
              <el-tag :type="row.mdeclareResult ? (row.mdeclareResult === '审核通过' ? 'success' : 'info') : 'danger'">{{
                row.mdeclareResult ? row.mdeclareResult : '未申报' }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="评估人员" align="center" width='100'>
            <template slot-scope="{row}">
              <span>{{ row.principal }}</span>
            </template>
          </el-table-column>
          <el-table-column align="center" width='140' label="评估人员申报提成">
            <template slot-scope="{row}">
              <el-tag :type="row.edeclareResult ? (row.edeclareResult === '审核通过' ? 'success' : 'info') : 'danger'">{{
                row.edeclareResult ? row.edeclareResult : '未申报' }}</el-tag>
            </template>
          </el-table-column>
        </el-table>
      </y-page-list-layout>
    </el-dialog>
    <el-dialog :visible.sync="myPersonalOrderDialog" width="98%" center custom-class="doWarehouseClass" @closed="getList">
      <y-page-list-layout :page-list="myPersonalPageData" :page-para="personalListQuery" :get-page-list="getMyPersonalOrder">
        <template slot="left">
          <el-input v-model="personalListQuery.keyword" placeholder="坐落" clearable
            style="margin-left: 10px;width: 200px;float: left;">
          </el-input>
          <el-input v-model="personalListQuery.orderId" placeholder="订单号" clearable
            style="margin-left: 10px;width: 150px;float: left;">
          </el-input>
          <el-input v-model="personalListQuery.bailor" placeholder="委托人" clearable
            style="margin-left: 10px;width: 150px;float: left;">
          </el-input>
          <el-input v-model="personalListQuery.clientName" placeholder="客户名称" clearable
            style="margin-left: 10px;width: 150px;float: left;">
          </el-input>
          <el-input v-model="personalListQuery.clientSubName" placeholder="业务来源" clearable
            style="margin-left: 10px;width: 150px;float: left;">
          </el-input>
          <el-input v-model="personalListQuery.shouldAmount" placeholder="应收款" clearable
            style="margin-left: 10px;width: 150px;float: left;">
          </el-input>
          <el-input v-model="personalListQuery.invoiceAmountCondition" placeholder="开票金额" clearable
            style="margin-left: 10px;width: 150px;float: left;">
          </el-input>
          <el-select v-model="personalListQuery.hasRealAmount" placeholder="收款状态" clearable
            style="margin-left: 20px;width: 150px;float: left;">
            <el-option label="未收款" value="false"></el-option>
            <el-option label="已收款" value="true"></el-option>
          </el-select>
          <el-button class="filter-item" style="margin-left: 10px;float: left;" type="primary" @click="getMyPersonalOrder"
            round>搜索
          </el-button>
          <el-button class="filter-item" style="float: left;" round type="success" @click="getMyPersonalOrderResetSearch()">重置
          </el-button>
          <el-button class="filter-item" style="float: left;" round type="info" @click="downloadTemp()">下载模板
          </el-button>
          <ExcelImport style="float: left; margin-left: 10px; " ref="uploadControl" buttonType="danger" :limit="1"
            :flag="'financeClaim/personal/temp/upload/' + financeFundId" :style-type="2" title="模版认领"
            @fath="getMyPersonalOrder" />
          <el-button style="margin-left: 10px ;float: left;" round type="danger"
            @click="openPersonalOrderDialog()">选择认领</el-button>
        </template>
        <el-table ref="table" :data="myPersonalPageData.records"  slot="table"
            :header-row-style="{ color: '#333333' }" style="
            border-left: 1px solid #ebeced; 
            border-right: 1px solid #ebeced; font-size: 14px;
            color: #333333;" @selection-change="handleSelectionChange" :row-class-name="tableRowClassName">
          <el-table-column align="center" type="selection" claimOrders width="100" border="true">
          </el-table-column>
          <el-table-column align="center"  width='100'>
            <template slot-scope="{row}">
              <span style="color:red">
                {{ row.invoiceAmount === notClaimAmount && row.realAmount === null ? '推荐匹配' : '' }}
              </span>
            </template>
          </el-table-column>
          <el-table-column label="坐落" align="center" show-overflow-tooltip>
            <template slot-scope="{row}">
              <span>{{ row.orderName }}</span>
            </template>
          </el-table-column>
          <el-table-column label="项目编号" align="center">
            <template slot-scope="{row}">
              <span>{{ row.orderId }}</span>
            </template>
          </el-table-column>
          <el-table-column label="委托人" align="center" width='100' show-overflow-tooltip>
            <template slot-scope="{row}">
              <span>{{ row.bailorA }} {{ row.bailorB }}</span>
            </template>
          </el-table-column>
          <el-table-column label="客户名称" align="center" width='200'>
            <template slot-scope="{row}">
              <span>{{ row.clientName }}</span>
            </template>
          </el-table-column>
          <el-table-column label="业务来源" align="center">
            <template slot-scope="{row}">
              <span>{{ row.clientSubName }}</span>
            </template>
          </el-table-column>
          <el-table-column label="中介" align="center" show-overflow-tooltip>
            <template slot-scope="{row}">
              <span>{{ row.agent == null ? '-' : row.agent }}</span>
            </template>
          </el-table-column>
          <el-table-column label="联系人" align="center">
            <template slot-scope="{row}">
              <span>{{ row.contactName }}</span>
            </template>
          </el-table-column>
          <el-table-column label="应收款(￥)" align="center" width='100'>
            <template slot-scope="{row}">
              <span>{{ row.shouldAmount?row.shouldAmount:'--' }}</span>
            </template>
          </el-table-column>
          <el-table-column label="实收款(元)" align="center">
            <template slot-scope="{row}">
              <span style="color:red;font-weight: bold;">{{ row.realAmount ? row.realAmount : 0 }}</span>
            </template>
          </el-table-column>
          <el-table-column label="开票金额(￥)" align="center" width='100'>
            <template slot-scope="{row}">
              <span>{{ row.invoiceAmount?row.invoiceAmount:'--' }}</span>
            </template>
          </el-table-column>
          <el-table-column align="center" show-overflow-tooltip width='120' label="备注">
            <template slot-scope="{row}">
              <span>{{ row.remark }}</span>
            </template>
          </el-table-column>
        </el-table>
      </y-page-list-layout>
    </el-dialog>
    <el-dialog :visible.sync="myAssetsOrderDialog" width="95%" center custom-class="doWarehouseClass" @closed="getList">
      <y-page-list-layout :page-list="myAssetsPageData" :page-para="assetsListQuery"
        :get-page-list="getMyAssetsOrderOrProd">
        <template slot="left">
          <!-- <el-switch style="width: 270px;float: left;margin-top: 5px;" v-model="toAssetsProd" active-color="#13ce66"
            inactive-color="#ff4949" active-text="认领到产品" inactive-text="认领到订单" disabled>
          </el-switch> -->
          <el-input v-model="assetsListQuery.keyWord" placeholder="订单/项目名称" clearable style="width: 270px;float: left;">
          </el-input>
          <el-input v-model="assetsListQuery.orderId" placeholder="订单号" clearable
            style="margin-left: 20px;width: 200px;float: left;">
          </el-input>
          <el-input v-show="toAssetsProd" v-model="assetsListQuery.reportNo" placeholder="报告号" clearable
            style="margin-left: 20px;width: 200px;float: left;">
          </el-input>
          <el-select v-model="assetsListQuery.eDeclareResult" placeholder="评估人员申报" clearable
            style="margin-left: 20px;width: 150px;float: left;">
            <el-option value="审核通过">审核通过</el-option>
            <el-option value="审核中">审核中</el-option>
            <el-option value="未申报">未申报</el-option>
            <el-option value="审核拒绝">审核拒绝</el-option>
          </el-select>
          <el-select v-model="assetsListQuery.mDeclareResult" placeholder="市场人员申报" clearable
            style="margin-left: 20px;width: 150px;float: left;">
            <el-option value="审核通过">审核通过</el-option>
            <el-option value="审核中">审核中</el-option>
            <el-option value="未申报">未申报</el-option>
            <el-option value="审核拒绝">审核拒绝</el-option>
          </el-select>
          <el-select v-model="assetsListQuery.hasRealAmount" placeholder="收款状态" clearable
            style="margin-left: 20px;width: 150px;float: left;">
            <el-option label="未收款" value="false"></el-option>
            <el-option label="已收款" value="true"></el-option>
          </el-select>
          <el-button v-show="toAssetsProd" class="filter-item" style="margin-left: 10px;float: left;" type="primary"
            @click="getMyAssetsOrderProd()" round>搜索
          </el-button>
          <el-button v-show="!toAssetsProd" class="filter-item" style="margin-left: 10px;float: left;" type="primary"
            @click="getMyAssetsOrder()" round>搜索
          </el-button>
          <el-button v-show="toAssetsProd" class="filter-item" style="float: left;" round type="success"
            @click="getMyAssetsProdResetSearch()">重置
          </el-button>
          <el-button v-show="!toAssetsProd" class="filter-item" style="float: left;" round type="success"
            @click="getMyAssetsOrderResetSearch()">重置
          </el-button>
          <el-button style="float: left;" round type="danger" @click="openAssetsClaimDialog()">选择认领</el-button>
        </template>
        <parentTable v-show="toAssetsProd" ref="prod" :clickRowToPick="false" :data="myAssetsPageData.records"
          slot="table" style="width: 100%;" :selectionChange="assetsHandleSelectionChange"  :isBoard=500>
          <el-table-column align="center" type="selection" claimOrders width="100" border="true"
            :selectable="checkSelectable">
          </el-table-column>
          <el-table-column align="center"  width='100'>
            <template slot-scope="{row}">
              <span style="color:red">
                {{ row.invoiceAmount === notClaimAmount && row.orderRealAmount === null ? '推荐匹配' : '' }}
              </span>
            </template>
          </el-table-column>
          <el-table-column label="订单名称" align="center" show-overflow-tooltip>
            <template slot-scope="{row}">
              <span>{{ row.name }}</span>
            </template>
          </el-table-column>
          <el-table-column label="订单号" align="center" width='160'>
            <template slot-scope="{row}">
              <span>{{ row.orderId }}</span>
            </template>
          </el-table-column>
          <el-table-column label="报告号" align="center" width='220'>
            <template slot-scope="{row}">
              <span>{{ row.productionNo }}</span>
            </template>
          </el-table-column>
          <el-table-column label="报告类型" align="center" width="80">
            <template slot-scope="{row}">
              <span v-if="row.productionType === 'STATEMENT'">价值意见书</span>
              <span v-if="row.productionType == 'LETTER'">意见函</span>
              <span v-if="row.productionType == 'CONSULT'">咨询报告</span>
              <span v-if="row.productionType == 'REPORT'">评估报告</span>
            </template>
          </el-table-column>
          <el-table-column label="客户名称" align="center" width='100'>
            <template slot-scope="{row}">
              <span>{{ row.clienteleName }}</span>
            </template>
          </el-table-column>
          <!-- <el-table-column label="订单应收款(元)" align="center" width='120'>
            <template slot-scope="{row}">
              <span>{{ row.orderShouldAmount }}</span>
            </template>
          </el-table-column>
          <el-table-column label="订单实收款(元)" align="center" width='120'>
            <template slot-scope="{row}">
              <span>{{ row.orderRealAmount }}</span>
            </template>
          </el-table-column> -->
          <el-table-column label="应收款(元)" align="center" width='100'>
            <template slot-scope="{row}">
              <span>{{ row.productionShouldAmount }}</span>
            </template>
          </el-table-column>
          <el-table-column label="实收款(元)" align="center" width='100'>
            <template slot-scope="{row}">
              <span style="color:red;font-weight: bold;">{{ row.productionRealAmount }}</span>
            </template>
          </el-table-column>
          <el-table-column label="标准收费(元)" align="center" width='120'>
            <template slot-scope="{row}">
              <span>{{ row.standardAmount }}</span>
            </template>
          </el-table-column>
          <el-table-column label="开票金额(元)" align="center" width='120'>
            <template slot-scope="{row}">
              <span>{{ row.invoiceAmount?row.invoiceAmount:'--' }}</span>
            </template>
          </el-table-column>
          <el-table-column label="市场人员" align="center" width='100'>
            <template slot-scope="{row}">
              <span>{{ row.marketUser }}</span>
            </template>
          </el-table-column>
          <el-table-column align="center" width='140' label="市场人员申报提成">
            <template slot-scope="{row}">
              <el-tag :type="row.mdeclareResult ? (row.mdeclareResult === '审核通过' ? 'success' : 'info') : 'danger'">{{
                row.mdeclareResult ? row.mdeclareResult : '未申报' }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="评估人员" align="center" width='100'>
            <template slot-scope="{row}">
              <span>{{ row.evaluateUser }}</span>
            </template>
          </el-table-column>
          <el-table-column align="center" width='140' label="评估人员申报提成">
            <template slot-scope="{row}">
              <el-tag :type="row.edeclareResult ? (row.edeclareResult === '审核通过' ? 'success' : 'info') : 'danger'">{{
                row.edeclareResult ? row.edeclareResult : '未申报' }}</el-tag>
            </template>
          </el-table-column>
        </parentTable>
        <parentTable v-show="!toAssetsProd" ref="order" :clickRowToPick="false" :data="myAssetsPageData.records"
          slot="table" style="width: 100%;" :selectionChange="assetsHandleSelectionChange">
          <el-table-column align="center" type="selection" claimOrders width="100" border="true"
            :selectable="checkSelectable">
          </el-table-column>
          <el-table-column label="订单名称" align="center" show-overflow-tooltip>
            <template slot-scope="{row}">
              <span>{{ row.name }}</span>
            </template>
          </el-table-column>
          <el-table-column label="订单号" align="center" width='200'>
            <template slot-scope="{row}">
              <span>{{ row.orderId }}</span>
            </template>
          </el-table-column>
          <el-table-column label="客户名称" align="center" width='200'>
            <template slot-scope="{row}">
              <span>{{ row.clienteleName }}</span>
            </template>
          </el-table-column>
          <el-table-column label="订单应收款(元)" align="center" width='200'>
            <template slot-scope="{row}">
              <span>{{ row.orderShouldAmount }}</span>
            </template>
          </el-table-column>
          <el-table-column label="订单实收款(元)" align="center" width='200'>
            <template slot-scope="{row}">
              <span>{{ row.orderRealAmount }}</span>
            </template>
          </el-table-column>
          <el-table-column label="市场人员" align="center" width='200'>
            <template slot-scope="{row}">
              <span>{{ row.marketUser }}</span>
            </template>
          </el-table-column>
          <el-table-column align="center" width='215' label="市场人员申报提成">
            <template slot-scope="{row}">
              <el-tag :type="row.mdeclareResult ? (row.mdeclareResult === '审核通过' ? 'success' : 'info') : 'danger'">{{
                row.mdeclareResult ? row.mdeclareResult : '未申报' }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="评估人员" align="center" width='200'>
            <template slot-scope="{row}">
              <span>{{ row.evaluateUser }}</span>
            </template>
          </el-table-column>
          <el-table-column align="center" width='215' label="评估人员申报提成">
            <template slot-scope="{row}">
              <el-tag :type="row.edeclareResult ? (row.edeclareResult === '审核通过' ? 'success' : 'info') : 'danger'">{{
                row.edeclareResult ? row.edeclareResult : '未申报' }}</el-tag>
            </template>
          </el-table-column>
        </parentTable>
      </y-page-list-layout>
    </el-dialog>
    <el-dialog :visible.sync="allotAssetsAmountDialog" width="45%" center custom-class="doWarehouseClass"
      @closed="cleanClaimDTO">
      <el-form v-model="claimOrders" ref="claimOrdersForm">
        <el-row>
          <el-col :xs="24" :sm="12" :lg="21" :span="6">
            <div style="font-size:20px;">
              <span>此笔实收款剩余：</span><span style="color:red;font-weight:bold">{{ notClaimAmount }}￥</span>
            </div>
          </el-col>
          <el-col :xs="24" :sm="12" :lg="3" :span="6">
            <el-button style="float:right" type="success" @click="assetsDoAllotRealAmountToOrder()">确认认领</el-button>
          </el-col>
        </el-row>
        <el-card style="margin-top:20px" v-for="(p, id) in claimOrders" shadow="hover">
          <div slot="header" class="clearfix">
            <el-row>
              <el-col :xs="24" :sm="12" :lg="24" :span="6">
                <el-form-item label="订单名称" prop="orderName" label-width="140px" class="postInfo-container-item">
                  <el-input :value="p.name" type="text" class="filter-item" disabled readonly />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :xs="24" :sm="12" :lg="12" :span="6">
                <span class="redPoint">*</span>
                <el-form-item label="本次认领金额：" prop="thisTimeAmount" label-width="140px" class="postInfo-container-item">
                  <el-input-number :precision="2" :min="0" :value-on-clear="0" v-model.number="p.thisTimeAmount"
                    style="width:100%" class="filter-item" />
                </el-form-item>
              </el-col>
              <el-col :xs="24" :sm="12" :lg="12" :span="6">
                <el-form-item label="订单号" prop="orderId" label-width="140px" class="postInfo-container-item">
                  <el-input :value="p.orderId" type="text" class="filter-item" disabled readonly />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :xs="24" :sm="12" :lg="12" :span="6">
                <el-form-item label="已认领金额：" prop="realAmount" label-width="140px" class="postInfo-container-item">
                  <el-input :value="p.realAmount" type="text" class="filter-item" disabled readonly>
                    <template slot="append">元</template>
                  </el-input>
                </el-form-item>
              </el-col>
              <el-col :xs="24" :sm="12" :lg="12" :span="6">
                <el-form-item label="应收金额：" prop="shouldAmount" label-width="140px" class="postInfo-container-item">
                  <el-input :value="p.shouldAmount" type="text" class="filter-item" disabled readonly>
                    <template slot="append">元</template>
                  </el-input>
                </el-form-item>
              </el-col>
            </el-row>
          </div>
        </el-card>
      </el-form>
    </el-dialog>
    <el-dialog :visible.sync="allotAssetsProdDialog" width="45%" center custom-class="doWarehouseClass"
      @closed="cleanClaimDTO">
      <el-form v-model="claimOrders" ref="claimOrdersForm">
        <el-row>
          <el-col :xs="24" :sm="12" :lg="21" :span="6">
            <div style="font-size:20px;">
              <span>此笔实收款剩余：</span><span style="color:red;font-weight:bold">{{ notClaimAmount }}￥</span>
            </div>
          </el-col>
          <el-col :xs="24" :sm="12" :lg="3" :span="6">
            <el-button style="float:right" type="success" @click="assetsDoAllotRealAmountToProd()">确认认领</el-button>
          </el-col>
        </el-row>
        <el-card style="margin-top:20px" v-for="(p, id) in claimOrders" shadow="hover">
          <div slot="header" class="clearfix">
            <el-row>
              <el-col :xs="24" :sm="12" :lg="24" :span="6">
                <el-form-item label="订单名称：" prop="name" label-width="140px" class="postInfo-container-item">
                  <el-input :value="p.name" type="text" class="filter-item" disabled readonly />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :xs="24" :sm="12" :lg="12" :span="6">
                <span class="redPoint">*</span>
                <el-form-item label="本次认领金额：" prop="thisTimeAmount" label-width="140px" class="postInfo-container-item">
                  <el-input-number :precision="2" :min="0" :value-on-clear="0" v-model.number="p.thisTimeAmount"
                    style="width:100%" class="filter-item" />
                </el-form-item>
              </el-col>
              <el-col :xs="24" :sm="12" :lg="12" :span="6">
                <el-form-item label="报告号：" prop="reportNo" label-width="140px" class="postInfo-container-item">
                  <el-input :value="p.reportNo" type="text" class="filter-item" disabled readonly />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :xs="24" :sm="12" :lg="12" :span="6">
                <el-form-item label="已认领金额：" prop="realAmount" label-width="140px" class="postInfo-container-item">
                  <el-input :value="p.realAmount" type="text" class="filter-item" disabled readonly>
                    <template slot="append">元</template>
                  </el-input>
                </el-form-item>
              </el-col>
              <el-col :xs="24" :sm="12" :lg="12" :span="6">
                <el-form-item label="产品应收金额：" prop="shouldAmount" label-width="140px" class="postInfo-container-item">
                  <el-input :value="p.shouldAmount" type="text" class="filter-item" disabled readonly>
                    <template slot="append">元</template>
                  </el-input>
                </el-form-item>
              </el-col>
            </el-row>
          </div>
        </el-card>
      </el-form>
    </el-dialog>
    <el-dialog :visible.sync="allotPersonalAmountDialog" width="45%" center custom-class="doWarehouseClass"
      @closed="cleanClaimDTO">
      <el-form v-model="claimOrders" ref="claimOrdersForm">
        <el-row>
          <el-col :xs="24" :sm="12" :lg="21" :span="6">
            <div style="font-size:20px;">
              <span>此笔实收款剩余：</span><span style="color:red;font-weight:bold">{{ notClaimAmount }}￥</span>
            </div>
          </el-col>
          <el-col :xs="24" :sm="12" :lg="3" :span="6">
            <el-button style="float:right" type="success" @click="doClaimPersonal()">确认认领</el-button>
          </el-col>
        </el-row>
        <el-card style="margin-top:20px" v-for="(p, id) in claimOrders" shadow="hover">
          <div slot="header" class="clearfix">
            <el-row>
              <el-col :xs="24" :sm="12" :lg="24" :span="6">
                <el-form-item label="订单名称" prop="orderName" label-width="140px" class="postInfo-container-item">
                  <el-input :value="p.orderName" type="text" class="filter-item" disabled readonly />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :xs="24" :sm="12" :lg="12" :span="6">
                <span class="redPoint">*</span>
                <el-form-item label="本次认领金额：" prop="thisTimeAmount" label-width="140px" class="postInfo-container-item">
                  <el-input-number :precision="2" :min="0" :value-on-clear="0" v-model.number="p.thisTimeAmount"
                    style="width:100%" class="filter-item" />
                </el-form-item>
              </el-col>
              <el-col :xs="24" :sm="12" :lg="12" :span="6">
                <el-form-item label="订单号" prop="orderId" label-width="140px" class="postInfo-container-item">
                  <el-input :value="p.orderId" type="text" class="filter-item" disabled readonly />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :xs="24" :sm="12" :lg="12" :span="6">
                <el-form-item label="已认领金额：" prop="realAmount" label-width="140px" class="postInfo-container-item">
                  <el-input :value="p.realAmount" type="text" class="filter-item" disabled readonly>
                    <template slot="append">元</template>
                  </el-input>
                </el-form-item>
              </el-col>
              <el-col :xs="24" :sm="12" :lg="12" :span="6">
                <el-form-item label="应收金额：" prop="shouldAmount" label-width="140px" class="postInfo-container-item">
                  <el-input :value="p.shouldAmount" type="text" class="filter-item" disabled readonly>
                    <template slot="append">元</template>
                  </el-input>
                </el-form-item>
              </el-col>
            </el-row>
          </div>
        </el-card>
      </el-form>
    </el-dialog>
  </div>
</template>
<script>
import YPageListLayout from '@/components/YPageListLayout'
import Breadcrumb from '@/components/Breadcrumb'
import ExcelImport from "@/components/excelImport.vue";


export default {
  name: 'financeRealFundClaim',
  components: {
    Breadcrumb,
    YPageListLayout,
    ExcelImport
  },

  watch: {
    toProd: {
      handler(value) {
        if (value) {
          this.getMyMajorProd();
        } else {
          this.getMyMajorOrder();
        }
      },

      immediate: true
    },
    toAssetsProd: {
      handler(value) {
        if (value) {
          this.getMyAssetsOrderProd();
        } else {
          this.getMyAssetsOrder();
        }
      },

      immediate: true
    }
  },

  computed: {

  },


  data() {
    return {
      pageData: { records: [] },
      listQuery: {
        page: 1,
        size: 10,
        descs: 'id',
        keyword: null,
        startDate: null,
        endDate: null,
        notFinished:true
      },
      myPersonalOrderDialog: false,
      myAssetsOrderDialog: false,
      allotAmountDialog: false,
      allotProdDialog: false,
      allotAssetsAmountDialog: false,
      allotAssetsProdDialog: false,
      realFund: {
        id: null,
        payer: null,
        payerBank: null,
        payerAccount: null,
        amount: null,
        claimAmount: null,
        notClaimAmount: null,
        payDatetime: null,
        remark: null,
        created: null,
        creator: null
      },
      toProd: true,
      toAssetsProd: true,
      myMajorOrderDialog: false,
      majorListQuery: {
        page: 1,
        size: 10,
        descs: 'id',
        hasRealAmount: "false"
      },
      myMajorPageData: { records: [] },
      notClaimAmount: 0,
      notInfo: "此笔实收款剩余：",
      claimOrders: [],
      financeFundId: null,
      payer: null,
      amount: null,
      productionLabel: "产品",
      orderDate: '',
      payDate: '',
      myPersonalPageData: { records: [] },
      pickerOptions: {
        shortcuts: [{
          text: '最近一周',
          onClick(picker) {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
            picker.$emit('pick', [start, end]);
          }
        }, {
          text: '最近一个月',
          onClick(picker) {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
            picker.$emit('pick', [start, end]);
          }
        }, {
          text: '最近三个月',
          onClick(picker) {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
            picker.$emit('pick', [start, end]);
          }
        }]
      },
      myAssetsPageData: { records: [] },
      assetsListQuery: {
        page: 1,
        size: 10,
        descs: 'id',
        hasRealAmount: "false"
      },
      personalListQuery: {
        page: 1,
        size: 10,
        descs: 'id',
        hasRealAmount: "false"
      },
      allotPersonalAmountDialog:false,
    }
  },
  created() {
    this.getList();
  },
  methods: {

    resetSearch() {
      this.$router.push({ query: {} });
      this.orderDate = '';
      this.payDate = '';
      this.listQuery = {
        current: 1,
        size: 10,
        descs: 'id',
      }
      this.getList()
    },

    getMyMajor(){
      if (this.toProd){
          this.getMyMajorProd();
      }else{
        this.getMyMajorOrder();
      }
    },

    getMyMajorOrderResetSearch() {
      this.$router.push({ query: {} });
      this.majorListQuery = {
        current: 1,
        size: 10,
        descs: 'id',
        hasRealAmount: "false"
      }
      this.getMyMajorOrder()
    },
    getMyMajorProdResetSearch() {
      this.$router.push({ query: {} });
      this.majorListQuery = {
        current: 1,
        size: 10,
        descs: 'id',
        hasRealAmount: "false"
      }
      this.getMyMajorProd()
    },
    searchList() {
      // 重置分页
      this.listQuery.page = 1
      this.listQuery.size = 10
      if (this.payDate) {
        this.listQuery.startDate = this.payDate[0];
        this.listQuery.endDate = this.payDate[1];
      }
      if (this.orderDate) {
        this.listQuery.xStartDate = this.orderDate[0] + ' 00:00:00';
        this.listQuery.xEndDate = this.orderDate[1] + ' 23:59:59';
      }
      this.getList()
    },
    getList() {
      this.listQuery.notFinished = true;
      this.claimOrders = [];
      this.$api.financeRealFund.claimList(Object.assign({}, this.listQuery)).then(res => {
        if (res.code === 200) {
          this.pageData = res.data;
        }
      })
      console.log(this.claimOrders)
    },

    getMyMajorOrder() {
      this.$api.majorProduction.waitingClaim(Object.assign({}, this.majorListQuery)).then(res => {
        if (res.code === 200) {
          this.myMajorPageData = res.data;
        }
      })
    },
    getMyMajorProd() {
      this.$api.majorProduction.waitingClaimProd(Object.assign({'invoiceAmount':this.notClaimAmount}, this.majorListQuery)).then(res => {
        if (res.code === 200) {
          this.myMajorPageData = res.data;
        }
      })
    },
    cleanClaimDTO() {
      this.notInfo = "此笔实收款剩余：";
    },
    handleSelectionChange(val) {
      let pickeds = [];
      for (let i in val) {
        let picked = new Object();
        picked.id = val[i].id;
        picked.orderName = val[i].orderName;
        picked.name = val[i].name;
        picked.reportNo = val[i].reportNo;
        picked.orderId = val[i].orderId;
        picked.shouldAmount = val[i].shouldAmount;
        picked.orderFundId = val[i].orderFundId;
        picked.realAmount = val[i].realAmount;
        picked.financeFundId = this.financeFundId;
        picked.productionFundId = val[i].productionFundId;
        pickeds.push(picked);
      }
      this.claimOrders = pickeds;
    },
    assetsHandleSelectionChange(val) {
      let pickeds = [];
      for (let i in val) {
        let picked = new Object();
        picked.id = val[i].id;
        picked.orderName = val[i].name;
        picked.name = val[i].name;
        picked.reportNo = val[i].productionNo;
        picked.orderId = val[i].orderId;
        if (this.toAssetsProd) {
          picked.shouldAmount = val[i].productionShouldAmount;
          picked.realAmount = val[i].productionRealAmount;
        } else {
          picked.shouldAmount = val[i].orderShouldAmount;
          picked.realAmount = val[i].orderRealAmount;
        }
        picked.orderFundId = val[i].orderFundId;
        picked.financeFundId = this.financeFundId;
        picked.productionFundId = val[i].productionFundId;
        pickeds.push(picked);
      }
      this.claimOrders = pickeds;
    },
    openClaimDialog() {
      if (this.claimOrders.length === 0) {
        this.$notify({
          title: '提示',
          message: '请先选择订单，然后认领。',
          type: 'info',
          duration: 2000
        });
        return;
      }
      if (this.toProd) {
        this.allotProdDialog = true;
      } else {
        this.allotAmountDialog = true;
      }

      this.notInfo = this.notInfo + this.notClaimAmount + '￥'
    },
    openAssetsClaimDialog() {
      if (this.claimOrders.length === 0) {
        this.$notify({
          title: '提示',
          message: '请先选择订单，然后认领。',
          type: 'info',
          duration: 2000
        });
        return;
      }
      if (this.toAssetsProd) {
        this.allotAssetsProdDialog = true;
      } else {
        this.allotAssetsAmountDialog = true;
      }

      this.notInfo = this.notInfo + this.notClaimAmount + '￥'
    },
    getMyPersonalOrder() {
      this.$api.personal.waitingClamOrder(Object.assign({'invoiceAmount':this.notClaimAmount}, this.personalListQuery)).then(res => {
        if (res.code === 200) {
          this.myPersonalPageData = res.data;
        }
      })
    },
    getMyPersonalOrderResetSearch() {
      this.$router.push({ query: {} });
      this.personalListQuery = {
        current: 1,
        size: 10,
        descs: 'id',
        hasRealAmount: "false"
      }
      this.getMyPersonalOrder()
    },
    downloadTemp() {
      window.location.href = "/api/financeClaim/personal/claim/template"
    },
    doClaimMajor() {
      const orders = this.claimOrders;
      for (let i in orders) {
        if (!orders[i].thisTimeAmount) {
          this.$message.error("报告号:[" + orders[i].reportNo + "],本次认领金额不能为空。");
          return;
        }
      }
      this.$api.businessProduction.allotRealAmount(this.claimOrders).then(res => {
        if (res.code === 200 && res.data) {
          this.$notify({
            title: '成功',
            message: '实收款认领成功',
            type: 'success',
            duration: 2000
          });
          this.getMajorOrderOrProd();
          this.allotAmountDialog = false;
          this.allotProdDialog = false;
          this.myMajorOrderDialog = false;
        } else {
          this.$notify({
            title: '失败',
            message: '实收款认领失败',
            type: 'error',
            duration: 2000
          });
        }
      })
    },
    doClaimMajorToOrder() {
      const orders = this.claimOrders;
      for (let i in orders) {
        if (!orders[i].thisTimeAmount) {
          this.$message.error("订单号:[" + orders[i].orderId + "],本次认领金额不能为空。");
          return;
        }
      }
      this.$api.businessProduction.allotRealAmountToOrder(this.claimOrders).then(res => {
        if (res.code === 200 && res.data) {
          this.$notify({
            title: '成功',
            message: '实收款认领成功',
            type: 'success',
            duration: 2000
          });
          
          this.getMajorOrderOrProd();
          this.allotAmountDialog = false;
          this.allotProdDialog = false;
          this.myMajorOrderDialog = false;
          
        } else {
          this.$notify({
            title: '失败',
            message: '实收款认领失败',
            type: 'error',
            duration: 2000
          });
        }
      })
    },
    doClaimPersonal() {
      const orders = this.claimOrders;
      for (let i in orders) {
        if (!orders[i].thisTimeAmount) {
          this.$message.error("订单号:[" + orders[i].orderId + "],本次认领金额不能为空。");
          return;
        }
      }
      this.$api.businessProduction.personalAllotRealAmount(this.claimOrders).then(res => {
        if (res.code === 200 && res.data) {
          this.$notify({
            title: '成功',
            message: '实收款认领成功',
            type: 'success',
            duration: 2000
          });
          this.getMyPersonalOrder();
          this.allotPersonalAmountDialog = false;
        } else {
          this.$notify({
            title: '失败',
            message: '实收款认领失败',
            type: 'error',
            duration: 2000
          });
        }
      })
    },
    checkSelectable(row) {
      if (row.mdeclareResult === '审核通过' && row.edeclareResult === '审核通过') {
        return true;
      } else {
        return false;
      }
    },
    claimMajor(row) {
      this.myMajorOrderDialog = true;
      this.notClaimAmount = row.notClaimAmount;
      this.financeFundId = row.id;
      this.getMyMajorProd();
    },
    claimPersonal(row) {
      this.myPersonalOrderDialog = true;
      this.financeFundId = row.id;
      this.notClaimAmount = row.notClaimAmount;
      this.getMyPersonalOrder();
    },
    getMajorOrderOrProd() {
      if (this.toProd) {
        this.getMyMajorProd();
      } else {
        this.getMyMajorOrder();
      }
    },
    resetMajorOrderOrProd() {
      if (this.toProd) {
        this.getMyMajorProdResetSearch();
      } else {
        this.getMyMajorOrderResetSearch();
      }
    },
    claimAssets(row) {
      this.notClaimAmount = row.notClaimAmount;
      this.myAssetsOrderDialog = true;
      this.financeFundId = row.id;
      this.getMyAssetsOrderProd();
      
    },
    resetAssetsListQuery() {
      this.assetsListQuery = {
        page: 1,
        size: 10,
        descs: 'id',
        hasRealAmount: "false"
      }
    },
    getMyAssetsOrderProd() {
      this.$api.assetsProduction.getMyAssetsOrderProd(Object.assign({'invoiceAmount':this.notClaimAmount}, this.assetsListQuery)).then(res => {
        if (res.code == 200) {
          this.myAssetsPageData = res.data;
        }
      })
    },
    getMyAssetsOrder() {
      this.$api.assetsProduction.getMyAssetsOrder(this.assetsListQuery).then(res => {
        if (res.code == 200) {
          this.myAssetsPageData = res.data;
        }
      })
    },
    getMyAssetsProdResetSearch() {
      this.$router.push({ query: {} });
      this.assetsListQuery = {
        current: 1,
        size: 10,
        descs: 'id',
        hasRealAmount: "false"
      }
      this.getMyAssetsOrderProd()
    },
    getMyAssetsOrderResetSearch() {
      this.$router.push({ query: {} });
      this.assetsListQuery = {
        current: 1,
        size: 10,
        descs: 'id',
      }
      this.getMyAssetsOrder()
    },
    getMyAssetsOrderOrProd() {
      if (this.toAssetsProd) {
        this.getMyAssetsOrderProd();
      } else {
        this.getMyAssetsOrder();
      }
    },
    // 资产认领实收款到 订单
    assetsDoAllotRealAmountToOrder() {
      const orders = this.claimOrders;
      for (let i in orders) {
        if (!orders[i].thisTimeAmount) {
          this.$message.error("订单号:[" + orders[i].orderId + "],本次认领金额不能为空。");
          return;
        }
      }
      this.$api.businessProduction.assetsDoAllotRealAmountToOrder(this.claimOrders).then(res => {
        if (res.code === 200 && res.data) {
          this.$notify({
            title: '成功',
            message: '实收款认领成功',
            type: 'success',
            duration: 2000
          });
          this.getMyAssetsOrder();
          this.allotAssetsAmountDialog = false;
          this.allotAssetsProdDialog = false;
        } else {
          this.$notify({
            title: '失败',
            message: '实收款认领失败',
            type: 'error',
            duration: 2000
          });
        }
      })
    },
    // 资产认领实收款到 产品
    assetsDoAllotRealAmountToProd() {
      const orders = this.claimOrders;
      for (let i in orders) {
        if (!orders[i].thisTimeAmount) {
          this.$message.error("报告号:[" + orders[i].reportNo + "],本次认领金额不能为空。");
          return;
        }
      }
      this.$api.businessProduction.assetsDoAllotRealAmountToProd(this.claimOrders).then(res => {
        if (res.code === 200 && res.data) {
          this.$notify({
            title: '成功',
            message: '实收款认领成功',
            type: 'success',
            duration: 2000
          });
          this.getMyAssetsOrderProd();
          this.allotAssetsAmountDialog = false;
          this.allotAssetsProdDialog = false;
        } else {
          this.$notify({
            title: '失败',
            message: '实收款认领失败',
            type: 'error',
            duration: 2000
          });
        }
      })
    },
    openPersonalOrderDialog(){
      if (this.claimOrders.length === 0) {
        this.$notify({
          title: '提示',
          message: '请先选择订单，然后认领。',
          type: 'info',
          duration: 2000
        });
        return;
      }

      this.notInfo = this.notInfo + this.notClaimAmount + '￥'
      this.allotPersonalAmountDialog = true;
    },
    tableRowClassName({row, rowIndex}) {
        if (row.invoiceAmount === this.notClaimAmount) {
          return 'success-row';
        }
        return '';
      }
  },

}
</script>
<style lang="scss" scoped>
/deep/.doWarehouseClass {
  border-radius: 10px;
}

.redPoint {
  color: red;
  position: absolute;
  top: 8px;
  left: 5px;
}
</style>

<style>
  .el-table .success-row {
    background: #f0f9eb;
  }
</style>
