<template>
  <div class="app-container">
    <div class="title-container">
      <breadcrumb id="breadcrumb-container" class="breadcrumb-container" />
    </div>
    <y-page-list-layout :page-list="pageData" :page-para="listQuery" :get-page-list="getList">
      <template slot="left">
        <MoreSearchBar business="MAJOR_BUSINESS" @fliterSearch="fliterSearch" @resetParams="resetParams"
          :listQuery="listQuery" :nodeCode="false" :financial="false" :delivery="true" :hasRealAmount="true"
          :hasShouldAmount="true" :clientManager="false" :businessObjectType="false" :department="false" :orderId="true"
          :productionNo="true" :productionType="true">
          <template v-slot:otherButton>
            <PermissionButton menu-code="_views_major_order" class-name="filter-item" type="danger"
              icon="el-icon-circle-plus-outline" :page-jump="true" round
              :page-query="{ 'couldEdit': true, 'couldBack': true }" />
            <el-button class="filter-item" round type="success" @click="exportMyOrder()"
              :disabled="!listQuery.startDate && !listQuery.endDate">导出
            </el-button>
          </template>
        </MoreSearchBar>
      </template>
      <parentTable :data="pageData.records" slot="table" style="width: 100%;">
        <el-table-column label="订单名称" align="center" show-overflow-tooltip width='300'>
          <template slot-scope="{row}">
            <span>{{ row.orderName }}</span>
          </template>
        </el-table-column>
        <el-table-column label="订单号" align="center" width='150'>
          <template slot-scope="{row}">
            <span>{{ row.orderId }}</span>
          </template>
        </el-table-column>
        <el-table-column label="产品号" align="center" width='200'>
          <template slot-scope="{row}">
            <span>{{ row.productionNo ? row.productionNo : '--' }}</span>
          </template>
        </el-table-column>
        <el-table-column label="产品类型" align="center" width='120'>
          <template slot-scope="{row}">
            <span>{{ row.production ? (row.production === 'REPORT' ? '报告' : (row.production === 'STATEMENT' ? '意见书' :
              '复评函'
            )) : '订单' }}</span>
          </template>
        </el-table-column>
        <el-table-column label="当前节点" align="center" width='200'>
          <template slot-scope="{row}">
            <span>{{ row.currentNodeName == null ? (row.ifSaveFile === true ? '已归档' : '--') : row.currentNodeName
            }}</span>
          </template>
        </el-table-column>
        <el-table-column label="负责人" align="center" width='120'>
          <template slot-scope="{row}">
            <span>{{ row.principal }}</span>
          </template>
        </el-table-column>
        <el-table-column label="客户经理" align="center" width='120'>
          <template slot-scope="{row}">
            <span>{{ row.clientManager }}</span>
          </template>
        </el-table-column>
        <el-table-column label="实收款(元)" align="center" width='120'>
          <template slot-scope="{row}">
            <span style="color:red;font-weight:bold">{{ row.realAmount ? row.realAmount : '--' }}</span>
          </template>
        </el-table-column>
        <el-table-column label="应收款(元)" align="center" width='120'>
          <template slot-scope="{row}">
            <span>{{ row.shouldAmount ? row.shouldAmount : (row.orderShouldAmount ? row.orderShouldAmount : '--')
            }}</span>
          </template>
        </el-table-column>
        <el-table-column label="开票金额(元)" align="center" width='120'>
          <template slot-scope="{row}">
            <span style="color:red;font-weight:bold">{{ row.invoiceAmount ? row.invoiceAmount : '--' }}</span>
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
        <el-table-column label="委托人" align="center" show-overflow-tooltip>
          <template slot-scope="{row}">
            <span>{{ row.bailor }}</span>
          </template>
        </el-table-column>
        <el-table-column label="产权人" align="center" show-overflow-tooltip>
          <template slot-scope="{row}">
            <span>{{ row.owner }}</span>
          </template>
        </el-table-column>
        <el-table-column label="送达状态" align="center" width='120'>
          <template slot-scope="{row}">
            <span>{{ row.delivery ? '已送达' : '--' }}</span>
          </template>
        </el-table-column>
        <el-table-column label="下单时间" align="center" width='200'>
          <template slot-scope="{row}">
            <span>{{ row.created }}</span>
          </template>
        </el-table-column>
        <el-table-column fixed="right" label="操作" width="250" align="center">
          <template slot-scope="{row}">
            <PermissionButton menu-code="_views_myOrder_detail" class-name="filter-item" type="text" :page-jump="true"
              size="mini"
              :page-query="{ 'couldEdit': true, 'id': row.businessId, 'back': '/major/my/order', 'recall': true, 'couldBack': true, 'orderId': row.orderId }" />
            <el-button :disabled="!row.id" type="text" size="small" @click="openShouldAmountDialog(row)">应收款</el-button>
            <!-- <el-button type="text" size="small" @click="openAllotProductionDialog(row)">实收款分配</el-button> -->
            <el-button :disabled="!row.id" type="text" size="small"
              @click="openFinanceInvoiceDialog(row.orderFundId, row.realAmount, row.clienteleName, row.clienteleSubName, row.productionFundId)">申请开票</el-button>
            <el-button type="text" size="small" @click="productionList(row.businessId)">产品包</el-button>
          </template>
        </el-table-column>
      </parentTable>
    </y-page-list-layout>
    <el-dialog :visible.sync="shouldAmountDialog" width="35%" center top="35vh" custom-class="doWarehouseClass"
      @closed="cleanData">
      <el-form ref="fund" :model="fund" style="margin-left:40px">
        <el-row>
          <el-col :xs="24" :sm="12" :lg="24" :span="12">
            <el-form-item label="应收款金额(元)：" prop="shouldAmount" label-width="160px" class="postInfo-container-item"
              :rules="{ required: true, message: '应收款金额不能为空', trigger: 'blur' }">
              <el-input-number :precision="2" :min="0" :value-on-clear="0" v-model.number="fund.shouldAmount"
                type="number" style="width:300px" class="filter-item" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :xs="24" :sm="12" :lg="24" :span="12">
            <el-form-item label="备注：" prop="remark" label-width="160px" class="postInfo-container-item">
              <el-input style="width:300px" type="textarea" v-model="fund.remark"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="shouldAmountDialog = false">取 消</el-button>
        <el-button type="primary" @click="saveShouldAmount()">确 定</el-button>
      </span>
    </el-dialog>
    <el-dialog :visible.sync="makeInvoiceListDialog" width="90%" center custom-class="doWarehouseClass">
      <el-button type="danger" round @click="openNewInvoiceDialog">新增开票</el-button>
      <div style="margin-top:30px;">
        <el-table :data="invoiceData" stripe :header-row-style="{ color: '#333333', 'font-size': '14px' }" border
          style="width: 100%; margin-top:20px;font-size: 14px;">
          <el-table-column align="center" prop="title" label="发票抬头" width="180">
          </el-table-column>
          <el-table-column align="center" prop="taxNo" label="税号" width="180">
          </el-table-column>
          <el-table-column align="center" prop="type" label="发票类型">
          </el-table-column>
          <el-table-column align="center" prop="state" label="开票状态">
            <template slot-scope="{row}">
              <span :style="stateStyle(row.state)">{{ row.state }}</span>
            </template>
          </el-table-column>
          <el-table-column align="center" prop="planAmount" label="计划开票金额">
          </el-table-column>
          <el-table-column align="center" prop="planMakeDate" label="计划开票日期">
          </el-table-column>
          <el-table-column align="center" prop="realAmount" label="实际开票金额">
          </el-table-column>
          <el-table-column align="center" prop="realMakeDate" label="实际开票日期">
          </el-table-column>
          <el-table-column align="center" prop="reason" label="驳回/作废原因">
          </el-table-column>
          <el-table-column align="center" prop="remark" label="备注">
          </el-table-column>
          <el-table-column fixed="right" label="操作" width="200" align="center">
            <template slot-scope="{row}">
              <el-button :disabled="row.state != '审核中'" @click="invoiceDetail(row.id)" type="text"
                size="small">修改</el-button>
              <el-button :disabled="row.state != '已开票'" @click="cancellation(row.id)" type="text"
                size="small">申请作废</el-button>
              <el-button @click="copyInvoice(row)" type="text" size="small">复制</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </el-dialog>
    <el-dialog :visible.sync="newInvoiceDialog" width="55%" top="10vh" center custom-class="doWarehouseClass"
      @closed="cleanFinanceInvoiceForm">
      <el-form ref="invoice" :model="invoice">
        <el-divider content-position="left">开票信息</el-divider>
        <el-row>
          <el-col :xs="24" :sm="12" :lg="12" :span="6">
            <el-form-item label="发票抬头：" :rules="{ required: true, message: '发票抬头不能为空', trigger: 'blur' }" prop="title"
              label-width="140px" class="postInfo-container-item">
              <el-input v-model.trim="invoice.title" class="filter-item" />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :lg="12" :span="6">
            <el-form-item label="发票类型：" :rules="{ required: true, message: '发票类型不能为空', trigger: 'blur' }" prop="type"
              label-width="140px" class="postInfo-container-item">
              <el-select v-model="invoice.type" style="width:100%">
                <el-option label="普票" value="普票"></el-option>
                <el-option label="专票" value="专票"></el-option>
              </el-select>
            </el-form-item>
          </el-col>

        </el-row>
        <el-row>
          <el-col :xs="24" :sm="12" :lg="12" :span="6">
            <el-form-item label="税号：" :rules="{ required: true, message: '税号不能为空', trigger: 'blur' }" prop="taxNo"
              label-width="140px" class="postInfo-container-item">
              <el-input v-model.trim="invoice.taxNo" class="filter-item" />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :lg="12" :span="6">
            <el-form-item label="计划开票日期：" prop="planMakeDate"
              :rules="{ required: true, message: '计划开票日期不能为空', trigger: 'blur' }" label-width="140px"
              class="postInfo-container-item">
              <el-date-picker v-model="invoice.planMakeDate" type="date" placeholder="选择日期" value-format="yyyy-MM-dd"
                style="width:100%">
              </el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :xs="24" :sm="12" :lg="12" :span="6">
            <el-form-item label="待开金额(元):" prop="planAmount"
              :rules="{ required: true, message: '待开金额不能为空', trigger: 'blur' }" label-width="140px"
              class="postInfo-container-item">
              <el-input-number :precision="2" :min="0" :value-on-clear="0" v-model.number="invoice.planAmount"
                type="number" style="width:100%" class="filter-item" />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :lg="12" :span="6">
            <el-form-item label="是否收款:" label-width="140px" class="postInfo-container-item">
              <el-radio-group :value="invoice.realPayAmount ? '已收款' : '未收款'">
                <el-radio label="已收款"></el-radio>
                <el-radio label="未收款"></el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :xs="24" :sm="12" :lg="12" :span="6">
            <el-form-item label="开票项目：" prop="makeItem" label-width="140px" class="postInfo-container-item">
              <el-input v-model.trim="invoice.makeItem" class="filter-item" />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :lg="12" :span="6">
            <el-form-item label="项目数量:" prop="itemQuantity" label-width="140px" class="postInfo-container-item">
              <el-input v-model.trim="invoice.itemQuantity" class="filter-item" />
            </el-form-item>
          </el-col>

        </el-row>
        <el-row>
          <el-col :xs="24" :sm="12" :lg="12" :span="6">
            <el-form-item label="计量单位：" prop="itemUnit" label-width="140px" class="postInfo-container-item">
              <el-input v-model.trim="invoice.itemUnit" class="filter-item" placeholder="个/套/箱/次/斤/公斤/..." />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :xs="24" :sm="12" :lg="24" :span="6">
            <el-form-item label="备注：" prop="remark" label-width="140px" class="postInfo-container-item">
              <el-input v-model.trim="invoice.remark" type="textarea" :autosize="{ minRows: 5, maxRows: 8 }"
                class="filter-item" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="newInvoiceDialog = false">取 消</el-button>
        <el-button type="primary" @click="saveInvoice()">确 定</el-button>
      </span>
    </el-dialog>
    <el-dialog :visible.sync="outWarehouseListDialog" width="90%" center custom-class="doWarehouseClass">
      <el-button type="danger" round @click="openOutWarehouseDialog()">新增出库申请</el-button>
      <div style="margin-top:30px;">
        <el-table :data="outApllyList" stripe :header-row-style="{ color: '#333333', 'font-size': '14px' }" border
          style="width: 100%; margin-top:20px;font-size: 14px;">
          <el-table-column align="center" prop="realAmount" label="申请时实收款金额(元)" width="180">
            <template slot-scope="{row}">
              <span style="color:red;font-weight:bold">{{ row.realAmount == null ? '0' : row.realAmount }}</span>
            </template>
          </el-table-column>
          <el-table-column align="center" prop="applyName" label="申请人">
            <template slot-scope="{row}">
              <span>{{ row.applyName }}</span>
            </template>
          </el-table-column>
          <el-table-column align="center" prop="remark" label="申请原因" show-overflow-tooltip>
            <template slot-scope="{row}">
              <span>{{ row.remark == null ? '-' : row.remark }}</span>
            </template>
          </el-table-column>
          <el-table-column align="center" prop="created" label="申请日期" width="200">
            <template slot-scope="{row}">
              <span>{{ row.created }}</span>
            </template>
          </el-table-column>
          <el-table-column align="center" prop="departmentCheckState" label="部门审核状态">
            <template slot-scope="{row}">
              <span>{{ row.departmentCheckState == null ? '-' : row.departmentCheckState }}</span>
            </template>
          </el-table-column>
          <el-table-column align="center" prop="departmentChecker" label="部门审核人">
            <template slot-scope="{row}">
              <span>{{ row.departmentChecker == null ? '-' : row.departmentChecker }}</span>
            </template>
          </el-table-column>
          <el-table-column align="center" prop="departmentCheckTime" label="部门审核时间" width="200">
            <template slot-scope="{row}">
              <span>{{ row.departmentCheckTime == null ? '-' : row.departmentCheckTime }}</span>
            </template>
          </el-table-column>
          <el-table-column align="center" prop="departmentReply" label="部门审核回复" show-overflow-tooltip>
            <template slot-scope="{row}">
              <span>{{ row.departmentReply == null ? '-' : row.departmentReply }}</span>
            </template>
          </el-table-column>
          <el-table-column align="center" prop="financeCheckState" label="财务审核状态">
            <template slot-scope="{row}">
              <span>{{ row.financeCheckState == null ? '-' : row.financeCheckState }}</span>
            </template>
          </el-table-column>
          <el-table-column align="center" prop="financeChecker" label="财务审核人">
            <template slot-scope="{row}">
              <span>{{ row.financeChecker == null ? '-' : row.financeChecker }}</span>
            </template>
          </el-table-column>
          <el-table-column align="center" prop="financeCheckTime" label="财务审核时间" width="200">
            <template slot-scope="{row}">
              <span>{{ row.financeCheckTime == null ? '-' : row.financeCheckTime }}</span>
            </template>
          </el-table-column>
          <el-table-column align="center" prop="financeReply" label="财务审核回复" show-overflow-tooltip>
            <template slot-scope="{row}">
              <span>{{ row.financeReply == null ? '-' : row.financeReply }}</span>
            </template>
          </el-table-column>
          <!-- <el-table-column fixed="right" label="操作" width="100" align = "center">
          <template slot-scope="{row}">
            <el-button @click="outApplyCancel(row.id,row.productionId,row.financeCheckState)" type="text" size="small">撤销</el-button>
          </template>
        </el-table-column> -->
        </el-table>
      </div>
    </el-dialog>
    <el-dialog :visible.sync="outWarehouseDialog" width="35%" center top="35vh" custom-class="doWarehouseClass">
      <el-form ref="outWarehouse" :model="outWarehouse">
        <el-row>
          <el-col :xs="24" :sm="12" :lg="24" :span="12">
            <el-form-item prop="remark" class="postInfo-container-item">
              <el-input placeholder="请输入出库原因" type="textarea" v-model="outWarehouse.remark"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="outWarehouseDialog = false">取 消</el-button>
        <el-button type="primary" @click="doApplyOut()">确 定</el-button>
      </span>
    </el-dialog>
    <el-dialog :visible.sync="productionListDialog" width="55%" center custom-class="doWarehouseClass">
      <el-collapse v-model="activeName" accordion v-if="bag.productions.length > 0">
        <el-collapse-item v-for="(r, index) in bag.productions" :name="r.productionNo">
          <template slot="title">
            <i class="el-icon-document"></i>
            <span style="font-weight:bold;margin-left:10px;margin-right:10px">{{ aliasProductionType(r.production)
            }}</span>
            {{ r.productionNo }}
          </template>
          <el-card style="margin-top:20px" shadow="hover">
            <div slot="header" class="clearfix">
              <!-- <span style="font-size:16px;font-weight:bold;">{{aliasProductionType(r.production)}}</span> -->
              <span style="margin-left:30px;font-size:16px;font-weight:bold; color:red">实收款金额：{{ r.realAmount != null ?
                r.realAmount : 0 }}￥</span>
              <span style="margin-left:30px;font-size:16px;">应收款金额：{{ r.shouldAmount != null ?
                r.shouldAmount : 0 }}￥</span>
              <el-button style="margin-left:30px;" type="text" size="small"
                @click="openShouldAmountDialog(r)">填写应收款</el-button>
              <!-- <el-button
                v-if="(r.productionNo === reportNo && r.production != 'STATEMENT' && r.repertoryState != null && !r.repertoryState)"
                style="float: right; padding: 3px 0;margin-left:10px" type="text"
                @click="openApplyOutWarehouseList(r)">出库申请</el-button> -->
              <el-button
                v-if="(r.productionNo === reportNo && r.production != 'STATEMENT' && r.repertoryState != null && !r.repertoryState)"
                style="float: right; padding: 3px 0;margin-left:10px" type="text" @click="doApplyOut(r)">出库申请</el-button>
              <el-button v-if="r.productionNo === statementNo && !r.delivery && r.production === 'STATEMENT'"
                style="float: right; padding: 3px 0;" type="text" @click="confirmDelivery(r.id)">确认送达</el-button>
              <el-button v-if="!r.delivery && r.production != 'STATEMENT' && r.repertoryState"
                style="float: right; padding: 3px 0;" type="text" @click="noTaskConfirmDelivery(r.id)">确认送达</el-button>
            </div>
            <el-form :model="r">
              <el-row>
                <el-col :xs="24" :sm="12" :lg="24" :span="12">
                  <el-form-item label="项目名称：" prop="name" label-width="120px" class="postInfo-container-item">
                    <el-input :value="r.name" type="text" class="filter-item" disabled readonly />
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :xs="24" :sm="12" :lg="12" :span="12">
                  <el-form-item label="委托方：" prop="clientName" label-width="120px" class="postInfo-container-item">
                    <el-input :value="r.clientName" type="text" class="filter-item" disabled readonly />
                  </el-form-item>
                </el-col>
                <el-col :xs="24" :sm="12" :lg="12" :span="12">
                  <el-form-item label="产权人：" prop="owner" label-width="120px" class="postInfo-container-item">
                    <el-input :value="r.owner" type="text" class="filter-item" disabled readonly />
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :xs="24" :sm="12" :lg="8" :span="12">
                  <el-form-item label="评估总价：" prop="evaluateAmount" label-width="120px" class="postInfo-container-item">
                    <el-input :value="r.evaluateAmount / 10000" type="text" class="filter-item" disabled readonly>
                      <template slot="append">万元</template>
                    </el-input>
                  </el-form-item>
                </el-col>
                <el-col :xs="24" :sm="12" :lg="8" :span="12">
                  <el-form-item label="评估面积：" prop="evaluateAcreage" label-width="120px" class="postInfo-container-item">
                    <el-input :value="r.evaluateAcreage" type="text" class="filter-item" disabled readonly>
                      <template slot="append">平方</template>
                    </el-input>
                  </el-form-item>
                </el-col>
                <el-col :xs="24" :sm="12" :lg="8" :span="12">
                  <el-form-item label="评估单价：" prop="evaluatePrice" label-width="120px" class="postInfo-container-item">
                    <el-input :value="r.evaluatePrice / 10000" type="text" class="filter-item" disabled readonly>
                      <template slot="append">万元</template>
                    </el-input>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :xs="24" :sm="12" :lg="8" :span="12">
                  <el-form-item label="库存状态：" prop="repertoryState" label-width="120px" class="postInfo-container-item">
                    <el-input :value="r.repertoryState == null ? '未入库' : (r.repertoryState ? '已出库' : '已入库')" type="text"
                      class="filter-item" disabled readonly />
                  </el-form-item>
                </el-col>
                <el-col :xs="24" :sm="12" :lg="8" :span="12">
                  <el-form-item label="入库时间：" prop="repertoryInTime" label-width="120px" class="postInfo-container-item">
                    <el-input :value="r.repertoryInTime" type="text" class="filter-item" disabled readonly />
                  </el-form-item>
                </el-col>
                <el-col :xs="24" :sm="12" :lg="8" :span="12">
                  <el-form-item label="出库时间：" prop="repertoryOutTime" label-width="120px" class="postInfo-container-item">
                    <el-input :value="r.repertoryOutTime" type="text" class="filter-item" disabled readonly />
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :xs="24" :sm="12" :lg="8" :span="12">
                  <el-form-item label="送达状态：" prop="delivery" label-width="120px" class="postInfo-container-item">
                    <el-input :value="r.delivery ? '已送达' : '未送达'" type="text" class="filter-item" disabled readonly />
                  </el-form-item>
                </el-col>
                <el-col :xs="24" :sm="12" :lg="8" :span="12">
                  <el-form-item label="归档状态：" prop="ifSaveFile" label-width="120px" class="postInfo-container-item">
                    <el-input :value="r.ifSaveFile ? '已归档' : '未归档'" type="text" class="filter-item" disabled readonly />
                  </el-form-item>
                </el-col>
                <el-col :xs="24" :sm="12" :lg="8" :span="12">
                  <el-form-item label="归档时间：" prop="saveFileDate" label-width="120px" class="postInfo-container-item">
                    <el-input :value="r.saveFileDate" type="text" class="filter-item" disabled readonly />
                  </el-form-item>
                </el-col>
              </el-row>
            </el-form>
          </el-card>
        </el-collapse-item>
      </el-collapse>
      <el-empty v-else description="暂无产品"></el-empty>
    </el-dialog>
    <el-dialog :visible.sync="allotAmountDialog" width="65%" center custom-class="doWarehouseClass">
      <el-form v-model="orderProduction" ref="orderProduction">
        <el-row>
          <el-col :xs="24" :sm="12" :lg="24" :span="6">
            <el-button style="float:right" type="success" @click="allotProduction()">确认分配</el-button>
          </el-col>
        </el-row>
        <el-card style="margin-top:20px" shadow="hover">
          <div slot="header" class="clearfix">
            <el-row>
              <el-col :xs="24" :sm="12" :lg="12" :span="6">
                <el-form-item label="订单名称：" prop="orderName" label-width="140px" class="postInfo-container-item">
                  <el-input :value="orderProduction.orderName" type="text" class="filter-item" disabled readonly />
                </el-form-item>
              </el-col>
              <el-col :xs="24" :sm="12" :lg="12" :span="6">
                <el-form-item label="订单号：" prop="orderId" label-width="140px" class="postInfo-container-item">
                  <el-input :value="orderProduction.orderId" type="text" class="filter-item" disabled readonly />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :xs="24" :sm="12" :lg="12" :span="6">
                <el-form-item label="订单已认领金额：" prop="realAmount" label-width="160px"
                  :class="['postInfo-container-item', 'real-amount']">
                  <el-input :value="orderProduction.realAmount" type="text" class="filter-item" disabled readonly>
                    <template slot="append">元</template>
                  </el-input>
                </el-form-item>
              </el-col>
              <el-col :xs="24" :sm="12" :lg="12" :span="6">
                <el-form-item label="未分配金额：" prop="notAllotAmount" label-width="160px"
                  :class="['postInfo-container-item', 'real-amount']">
                  <el-input :value="notAllotAmount" type="text" class="filter-item" disabled readonly>
                    <template slot="append">元</template>
                  </el-input>
                </el-form-item>
              </el-col>
            </el-row>
          </div>
          <el-row>
            <el-col :xs="24" :sm="12" :lg="12" :span="6">
              <el-form-item label="产品列表：" prop="orderName" label-width="140px" class="postInfo-container-item" />
            </el-col>
          </el-row>
          <div v-if="orderProduction.reports != null && orderProduction.reports.length > 0">
            <el-row v-for="(r, index) in orderProduction.reports">
              <div>
                <el-col :xs="24" :sm="12" :lg="5" :span="6">
                  <el-form-item :label="productionLabel + (index + 1) + ':'" prop="productionType" label-width="90px"
                    class="postInfo-container-item">
                    <el-input :value="r.productionType" type="text" class="filter-item" disabled readonly />
                  </el-form-item>
                </el-col>
                <el-col :xs="24" :sm="12" :lg="7" :span="6">
                  <el-form-item label="报告号：" prop="businessSubId" label-width="90px" class="postInfo-container-item">
                    <el-input :value="r.businessSubId" type="text" class="filter-item" disabled readonly />
                  </el-form-item>
                </el-col>
                <el-col :xs="24" :sm="12" :lg="6" :span="6">
                  <el-form-item label="已分配金额：" prop="claimedAmount" label-width="120px" class="postInfo-container-item">
                    <el-input-number :precision="2" :min="0" :value-on-clear="0" v-model.number="r.claimedAmount"
                      type="number" style="width:100%" class="filter-item" readonly disabled />
                  </el-form-item>
                </el-col>
                <el-col :xs="24" :sm="12" :lg="6" :span="6">
                  <el-form-item label="本次分配：" prop="thisTimeAmount" label-width="100px" class="postInfo-container-item">
                    <el-input-number :precision="2" :min="0" :value-on-clear="0" v-model.number="r.thisTimeAmount"
                      type="number" style="width:100%" class="filter-item" />
                  </el-form-item>
                </el-col>
              </div>
            </el-row>
          </div>
          <span v-else style="margin-left:140px;color:RGB(153,153,153)">暂无产品</span>
        </el-card>
      </el-form>
    </el-dialog>
  </div>
</template>
<script>
import YPageListLayout from '@/components/YPageListLayout'
import Breadcrumb from '@/components/Breadcrumb'
import MoreSearchBar from '@/components/MoreSearchBar'


export default {
  name: 'myOrder',
  components: {
    Breadcrumb,
    YPageListLayout,
    MoreSearchBar
  },
  filters: {

  },

  data() {
    return {
      pageData: { records: [] },
      listQuery: {
        page: 1,
        size: 20,
        descs: 'id',
        keyword: null,
        startDate: null,
        endDate: null
      },
      shouldAmountDialog: false,
      makeInvoiceListDialog: false,
      newInvoiceDialog: false,
      outWarehouseDialog: false,
      outWarehouseListDialog: false,
      productionListDialog: false,
      allotAmountDialog: false,
      fund: {
        id: null,
        businessType: null,
        orderName: null,
        orderId: null,
        shouldAmount: null,
        remark: null,
        businessType: null,
        businessId: null,
        businessSubId: null,
        orderFundId: null,
        productionFundId: null,
        evaluateAmount: null,
        orderShouldAmount: null,
      },
      invoiceData: [],
      invoice: {
        id: null,
        orderFundId: null,
        productionFundId: null,
        title: null,
        type: null,
        taxNo: null,
        planAmount: null,
        planMakeDate: null,
        bankName: null,
        bankAccount: null,
        bankAddress: null,
        bankTel: null,
        makeItem: null,
        itemQuantity: null,
        itemUnit: null,
        taxRate: null,
        taxAmount: null,
        remark: null,
        realPayAmount: null,
        realAmount: null,
        realMakeDate: null
      },
      outWarehouse: {
        id: null,
        productionFundId: null,
        remark: null,
        realAmount: null,
        standardAmount: null
      },
      outApllyList: [],
      bag: {
        businessId: null,
        businessType: null,
        reportNo: null,
        productions: []
      },
      activeName: null,
      todoBusinessId: null,
      taskId: null,
      orderProduction: {
        reports: [],
        orderName: null,
        orderId: null,
        realAmount: null,
        id: null
      },
      productionLabel: "产品",
      notAllotAmount: null,
      orderDate: '',
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
    }
  },
  created() {
    this.getList();
    this.todoBusinessId = this.$route.query.todoBusinessId;
    this.taskId = this.$route.query.tId;
    this.currentNodeId = this.$route.query.cId;
    this.statementNo = this.$route.query.sNo;
    this.reportNo = typeof (this.$route.query.rNo) == undefined ? null : this.$route.query.rNo;
    if (this.todoBusinessId) {
      this.productionListDialog = true;
      this.productionList(this.todoBusinessId, this.reportNo == null ? this.statementNo : this.reportNo)
      this.activeName = this.reportNo == null ? this.statementNo : this.reportNo
    }
  },
  methods: {


    resetSearch() {
      this.$router.push({ query: {} });
      this.orderDate = '';
      this.listQuery = {
        current: 1,
        size: 20,
        descs: 'id',
      }
      this.getList()
    },

    searchList() {
      // 重置分页
      this.listQuery.page = 1;
      this.listQuery.size = 20;
      if (this.orderDate) {
        this.listQuery.startDate = this.orderDate[0] + ' 00:00:00';
        this.listQuery.endDate = this.orderDate[1] + ' 23:59:59';
      }
      this.getList()
    },
    getList() {
      this.$api.majorProduction.myOrder(Object.assign({}, this.listQuery)).then(res => {
        if (res.code === 200) {
          this.pageData = res.data;
        }
      })
    },
    aliasProductionType(code) {
      if (code === 'STATEMENT') {
        return '价值意见书';
      }
      if (code === 'LETTER') {
        return '复评函';
      }
      return '报告';
    },
    openShouldAmountDialog(row) {
      this.shouldAmountDialog = true;
      this.fund.id = row.id;
      this.fund.businessType = 'MAJOR_BUSINESS';
      this.fund.businessId = row.businessId;
      this.fund.orderFundId = row.orderFundId;
      this.fund.shouldAmount = row.shouldAmount;
      this.fund.remark = row.remark;
      this.fund.orderId = row.orderId;
      this.fund.orderName = row.orderName;
      this.fund.productionFundId = row.productionFundId;
      this.fund.productionNo = row.productionNo;
      this.fund.productionType = row.production;
      this.fund.evaluateAmount = row.evaluateAmount;
    },
    saveShouldAmount() {
      this.$refs.fund.validate(valid => {
        if (valid) {
          //设置订单应收款
          if (!this.fund.id) {
            this.fund.id = this.fund.orderFundId;
            if (this.fund.id) {
              this.$api.orderFund.edit(this.fund).then(res => {
                if (res.code === 200 && res.data) {
                  this.$notify({
                    title: '成功',
                    message: '订单应收款已完善',
                    type: 'success',
                    duration: 1000
                  });
                  this.getList();
                  this.shouldAmountDialog = false;
                } else {
                  this.$notify({
                    title: '失败',
                    message: '订单应收款保存错误',
                    type: 'error',
                    duration: 1000
                  });
                }
              })
            } else {
              this.$api.orderFund.add(this.fund).then(res => {
                if (res.code === 200 && res.data) {
                  this.$notify({
                    title: '成功',
                    message: '订单应收款已完善',
                    type: 'success',
                    duration: 1000
                  });
                  this.getList();
                  this.shouldAmountDialog = false;
                } else {
                  this.$notify({
                    title: '失败',
                    message: '订单应收款保存错误',
                    type: 'error',
                    duration: 1000
                  });
                }
              })
            }
          } else {
            //设置产品应收款
            this.fund.productionShouldAmount = this.fund.shouldAmount;
            this.fund.id = this.fund.productionFundId;
            if (this.fund.id) {
              this.$api.productionFund.edit(this.fund).then(res => {
                if (res.code === 200 && res.data) {
                  this.$notify({
                    title: '成功',
                    message: '订单应收款已完善',
                    type: 'success',
                    duration: 1000
                  });
                  this.getList();
                  this.shouldAmountDialog = false;
                  this.shouldAmountDialog = false;
                  this.productionList(this.todoBusinessId, this.reportNo == null ? this.statementNo : this.reportNo)
                } else {
                  this.$notify({
                    title: '失败',
                    message: '订单应收款保存错误',
                    type: 'error',
                    duration: 1000
                  });
                }
              })
            } else {
              this.$api.productionFund.add(this.fund).then(res => {
                if (res.code === 200 && res.data) {
                  this.$notify({
                    title: '成功',
                    message: '订单应收款已完善',
                    type: 'success',
                    duration: 1000
                  });
                  this.getList();
                  this.shouldAmountDialog = false;
                  this.productionList(this.todoBusinessId, this.reportNo == null ? this.statementNo : this.reportNo)
                } else {
                  this.$notify({
                    title: '失败',
                    message: '订单应收款保存错误',
                    type: 'error',
                    duration: 1000
                  });
                }
              })
            }
          }
        }
      })
    },

    cleanData() {
      this.fund.shouldAmount = null;
      this.fund.id = null;
      this.fund.businessType = null;
      this.fund.remark = null;
      this.fund.businessType = null;
      this.fund.businessId = null;
      this.fund.businessSubId = null;
      this.fund.productionFundId = null;
      this.fund.remark = null;
      this.fund.evaluateAmount = null
    },
    getFinanceInvoiceList() {
      this.$api.financeInvoice.getList(this.invoice.orderFundId, this.invoice.productionFundId).then(res => {
        if (res.code === 200) {
          this.invoiceData = res.data
        }
      })
    },
    openFinanceInvoiceDialog(orderFundId, realAmount, clientName, clientSubName, productionFundId) {
      if (!orderFundId) {
        this.$notify({
          title: '提示',
          message: '请先完善应收款，再操作。',
          type: 'info',
          duration: 2000
        });
        return;
      }
      this.makeInvoiceListDialog = true;
      if (orderFundId) {
        this.invoice.orderFundId = orderFundId;
        this.invoice.realPayAmount = realAmount;
        this.invoice.productionFundId = productionFundId;
        this.invoice.title = clientName + clientSubName;
        this.getFinanceInvoiceList();
      }
    },
    openNewInvoiceDialog() {
      if (!this.invoice.orderFundId) {
        this.$notify({
          title: '提示',
          message: '申请开票前，请先完善应收款金额。',
          type: 'info',
          duration: 2000
        });
        return;
      } else {
        this.newInvoiceDialog = true;

      }
    },
    saveInvoice() {
      this.$refs.invoice.validate(valid => {
        if (valid) {
          if (this.invoice.id) {
            this.$api.financeInvoice.edit(this.invoice).then(res => {
              if (res.code === 200 && res.data) {
                this.$notify({
                  title: '成功',
                  message: '开票申请修改成功',
                  type: 'success',
                  duration: 2000
                });
                this.getFinanceInvoiceList();
                this.newInvoiceDialog = false;
              } else {
                this.$notify({
                  title: '失败',
                  message: '开票申请修改失败',
                  type: 'error',
                  duration: 2000
                });
              }
            })
          } else {
            this.$api.financeInvoice.add(this.invoice).then(res => {
              if (res.code === 200 && res.data) {
                this.$notify({
                  title: '成功',
                  message: '开票申请提交成功',
                  type: 'success',
                  duration: 2000
                });
                this.getFinanceInvoiceList();
                this.newInvoiceDialog = false;
              } else {
                this.$notify({
                  title: '失败',
                  message: '开票申请提交失败',
                  type: 'error',
                  duration: 2000
                });
              }
            })
          }
        }
      })
    },
    cleanFinanceInvoiceForm() {
      // this.$refs.invoice.resetFields();
      this.invoice = {
        id: null,
        orderFundId: this.invoice.orderFundId,
        productionFundId: this.invoice.productionFundId,
        title: null,
        type: null,
        taxNo: null,
        planAmount: null,
        planMakeDate: null,
        bankName: null,
        bankAccount: null,
        bankAddress: null,
        bankTel: null,
        makeItem: null,
        itemQuantity: null,
        itemUnit: null,
        taxRate: null,
        taxAmount: null,
        remark: null
      }
    },
    invoiceDetail(id) {
      this.$api.financeInvoice.detail(id).then(res => {
        if (res.code === 200) {
          this.invoice = res.data;
        }
      })
      this.newInvoiceDialog = true;
    },
    cancellation(id) {
      const h = this.$createElement
      this.$prompt(
        h('p', null, [h('span', { style: 'color: red' }, '请输入作废原因：')]), '开票作废', {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        type: 'error',
        inputValidator: (value) => {
          if (!value) {
            return '作废原因不能为空'
          }
        },
      }).then(({ value }) => {
        this.invoice.reason = value;
        this.invoice.id = id;
        this.$api.financeInvoice.cancellation(this.invoice).then(res => {
          if (res.code === 200 && res.data) {
            this.$notify({
              title: '成功',
              message: '作废申请已提交',
              type: 'success',
              duration: 2000
            });
            this.getFinanceInvoiceList();
          } else {
            this.$notify({
              title: '失败',
              message: '作废提交失败',
              type: 'error',
              duration: 2000
            });
          }
        })

      })
    },
    openApplyOutWarehouseList(row) {
      console.log(row)
      if (row.shouldAmount) {
        this.getListByBizTypeProductionId('MAJOR_BUSINESS', row.id);
        this.outWarehouseListDialog = true;
        this.outWarehouse.productionFundId = row.productionFundId;
        this.outWarehouse.realAmount = row.realAmount
        this.outWarehouse.ifProductionFund = row.realAmount != null
        this.outWarehouse.id = row.id;
        this.outWarehouse.businessType = 'MAJOR_BUSINESS';
        this.outWarehouse.standardAmount = row.standardAmount;
      } else {
        this.$notify({
          title: '失败',
          message: '请先填写应收款',
          type: 'error',
          duration: 2000
        });
      }

    },
    openOutWarehouseDialog() {
      if (this.outApllyList.length > 0) {
        const applys = this.outApllyList;
        for (let i in applys) {
          if (applys[i].departmentCheckState == null && applys[i].financeCheckState == null) {
            this.$notify({
              title: '提示',
              message: '存在待审核的申请，请勿重复申请。',
              type: 'info',
              duration: 2000
            });
            return;
          }
        }
        this.outWarehouseDialog = true;
      } else {
        this.outWarehouseDialog = true
      }

    },
    getApplyOutList(productionFundId) {
      this.$api.productionOutWarehouse.getList(productionFundId).then(res => {
        if (res.code === 200) {
          this.outApllyList = res.data;
        }
      })
    },
    getListByBizTypeProductionId(businessType, productionId) {
      this.$api.productionOutWarehouse.getListByBizTypeProductionId(businessType, productionId).then(res => {
        if (res.code === 200) {
          this.outApllyList = res.data;
        }
      })
    },
    doApplyOut(row) {
      if (row.shouldAmount) {
        this.getListByBizTypeProductionId('MAJOR_BUSINESS', row.id);
        if (this.outApllyList.length > 0) {
          const applys = this.outApllyList;
          for (let i in applys) {
            if (applys[i].departmentCheckState == null && applys[i].financeCheckState == null) {
              this.$notify({
                title: '提示',
                message: '存在待审核的申请，请勿重复申请。',
                type: 'info',
                duration: 2000
              });
              return;
            }
          }
        } else {

          this.outWarehouse.productionFundId = row.productionFundId;
          this.outWarehouse.realAmount = row.realAmount
          this.outWarehouse.ifProductionFund = row.realAmount != null
          this.outWarehouse.id = row.id;
          this.outWarehouse.businessType = 'MAJOR_BUSINESS';
          this.outWarehouse.standardAmount = row.standardAmount;

          let taskRecordDTO = new Object();
          taskRecordDTO.recordId = this.taskId;
          taskRecordDTO.taskData = this.outWarehouse;
          this.$api.productionOutWarehouse.add(taskRecordDTO).then(res => {
            if (res.code === 200 && res.data) {
              this.$notify({
                title: '成功',
                message: '出库申请已提交',
                type: 'success',
                duration: 2000
              });
              this.commitNode();
              this.outWarehouseDialog = false;
              this.outWarehouseListDialog = false;
              this.productionListDialog = false;
            } else {
              this.$notify({
                title: '失败',
                message: '出库申请提交失败',
                type: 'error',
                duration: 2000
              });
            }
          })
        }
      } else {
        this.$notify({
          title: '失败',
          message: '请先填写应收款',
          type: 'error',
          duration: 2000
        });
      }
    },
    outApplyCancel(id, productionId, state) {
      this.$confirm('请确认是否撤销此出库申请?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
        center: true
      }).then(() => {
        if (state === '审核通过') {
          this.$notify({
            title: '提示',
            message: '出库申请已通过，无法撤销。',
            type: 'info',
            duration: 2000
          });
          return;
        }
        if (id) {
          this.$api.productionOutWarehouse.cancel(id).then(res => {
            if (res.code === 200 && res.data) {
              this.$notify({
                title: '成功',
                message: '申请已撤销',
                type: 'success',
                duration: 2000
              });
              this.getListByBizTypeProductionId('MAJOR_BUSINESS', productionId);
            }
          })
        }
      })
    },
    productionList(id) {
      this.productionListDialog = true;
      this.bag.businessId = id;
      if (id) {
        this.$api.businessProduction.bag(id).then(res => {
          if (res.code === 200) {
            this.bag = res.data;
          }
        })
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
    noTaskConfirmDelivery(id) {
      this.$confirm('确认执行产品送达操作？', '提示', {
        confirmButtonText: '确认送达',
        cancelButtonText: '取消',
        type: 'warning',
        center: true
      }).then(() => {
        this.$api.majorProduction.noTaskConfirmDelivery(id).then(res => {
          if (res.code === 200 && res.data) {
            this.$notify({
              title: '成功',
              message: '产品送达状态修改成功',
              type: 'success',
              duration: 2000
            });
            this.productionListDialog = false;
          } else {
            this.$notify({
              title: '失败',
              message: '产品送达状态修改失败',
              type: 'error',
              duration: 2000
            });
          }
        })
      })
    },
    confirmDelivery(id) {
      this.$confirm('确认执行产品送达操作？', '提示', {
        confirmButtonText: '确认送达',
        cancelButtonText: '取消',
        type: 'warning',
        center: true
      }).then(() => {
        let taskRecordDTO = new Object();
        taskRecordDTO.recordId = this.taskId;
        taskRecordDTO.taskData = id;
        this.$api.majorProduction.confirmDelivery(taskRecordDTO).then(res => {
          if (res.code === 200 && res.data) {
            this.$notify({
              title: '成功',
              message: '产品送达状态修改成功',
              type: 'success',
              duration: 2000
            });
            this.productionListDialog = false;
            this.commitNode();

          } else {
            this.$notify({
              title: '失败',
              message: '产品送达状态修改失败',
              type: 'error',
              duration: 2000
            });
          }
        })
      })
    },
    commitNode() {
      let commit = new Object;
      commit.instanceNodeId = this.currentNodeId;
      commit.state = 'PASS';
      commit.comments = this.outWarehouse.remark;
      commit.businessMinId = this.reportNo;
      commit.businessSubId = this.statementNo;
      commit.ifProductionFund = this.outWarehouse.ifProductionFund;
      this.$api.workflow.commit(commit).then(res => {
        if (res.code === 200 && res.data) {
          this.$notify({
            title: '成功',
            message: '工作流节点提交成功。',
            type: 'success',
            duration: 2000
          });
        } else {
          this.$notify({
            title: '失败',
            message: '工作流节点提交失败，请联系管理员。',
            type: 'error',
            duration: 2000
          });
        }
      })
    },

    openAllotProductionDialog(row) {
      if (!row.orderFundId) {
        this.$notify({
          title: '提示',
          message: '该订单还没有认领实收款，请先认领实收款后再分配给产品。',
          type: 'info',
          duration: 3000
        });
        return;
      }
      this.getProductionList(row.orderFundId);
      this.orderProduction.orderId = row.orderId;
      this.orderProduction.orderName = row.orderName;
      this.orderProduction.realAmount = row.realAmount;
      this.orderProduction.orderFundId = row.orderFundId;

    },
    getProductionList(orderFundId) {
      if (orderFundId) {
        this.$api.businessProduction.getProductionByOrderFundId(orderFundId).then(res => {
          if (res.code === 200) {
            this.orderProduction.reports = res.data;
            let allotAmount = res.data.map(item => item.claimedAmount).reduce(function (prev, cur) {
              return prev + cur;
            }, 0);
            this.notAllotAmount = this.orderProduction.realAmount - allotAmount;
            this.allotAmountDialog = true;
          }
        })
      }
    },
    allotProduction() {
      this.$api.businessProduction.allotMajorProduction(this.orderProduction).then(res => {
        if (res.code === 200 && res.data) {
          this.$notify({
            title: '成功',
            message: '实收款分配成功',
            type: 'success',
            duration: 2000
          });
          this.allotAmountDialog = false;
        } else {
          this.$notify({
            title: '失败',
            message: '实收款分配失败',
            type: 'error',
            duration: 2000
          });
        }
      })
    },
    fliterSearch(params) {
      this.$api.majorProduction.myOrder(Object.assign({}, params)).then(res => {
        if (res.code === 200) {
          this.pageData = res.data;
        }
      })
    },

    searchList() {
      // 重置分页
      this.listQuery.page = 1
      this.listQuery.size = 20
      this.getList()
    },

    resetParams() {
      this.$router.push({ query: {} });
      this.listQuery = {
        current: 1,
        size: 20,
        descs: 'created',
      }
      this.getList();
    },
    stateStyle(state) {
      if (state === '已开票') {
        return 'color:green'
      } else {
        return 'color:red'
      }
    },
    copyInvoice(row) {
      this.invoice = row;
      this.invoice.id = null;
      this.invoice.state = null;
      this.invoice.reason = null;
      this.invoice.created = null;
      this.invoice.modified = null;
      this.newInvoiceDialog = true;
    },
    // 大中型我的订单导出
    exportMyOrder() {
      this.$utils.exportUtil(
        "/majorProduction/exportMyOrder", this.listQuery,
        "导出"
      );
    }
  }

}
</script>
<style lang="scss" scoped>
/deep/.doWarehouseClass {
  border-radius: 10px;
}

.real-amount {
  /deep/ .el-form-item__label {
    color: red;
    font-weight: bold
  }
}
</style>
