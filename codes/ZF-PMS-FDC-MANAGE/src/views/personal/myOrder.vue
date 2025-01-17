<template>
  <div class="app-container">
    <div class="title-container">
      <breadcrumb id="breadcrumb-container" class="breadcrumb-container" />
    </div>
    <y-page-list-layout :page-list="pageData" :page-para="listQuery" :get-page-list="getPage">
      <template slot="left">
        <el-col :xs="24" :sm="12" :lg="2" :span="6">
          <PermissionButton menu-code="_views_personal_order" class-name="filter-item" type="danger" icon="el-icon-circle-plus-outline"
          :page-jump="true" round style="position: absolute;right: 30px;z-index:9" :page-query="{'couldEdit':true,'saveOrderBtn':'保存订单','showBtn':true ,'couldBack':true}" />
        </el-col>
          <MoreSearchBar business="PERSONAL_BUSINESS" @fliterSearch="fliterSearch" @resetParams="resetParams" :listQuery="listQuery" 
              :nodeCode="false" :clientManager="false" :outwardStaffName="false" :inwardStaffName="false" :pricingStaffName="false" :handlerName="false">
            <template v-slot:otherButton>
              <el-button class="filter-item" round type="success" @click="exportMyOrder()" :disabled="!listQuery.startDate && !listQuery.endDate">导出
              </el-button>
            </template>
        </MoreSearchBar>
      </template>
      <parentTable ref="table" v-loading="listLoading" :data="pageData.records" slot="table" style="width: 100%;" >
        <el-table-column label="坐落" align="center" width="300" show-overflow-tooltip>
          <template slot-scope="{row}">
            <span>{{ row.orderName }}</span>
          </template>
        </el-table-column>
        <el-table-column label="项目编号" align="center" width="130" >
          <template slot-scope="{row}">
            <span>{{ row.orderId }}</span>
          </template>
        </el-table-column>
        <el-table-column label="面积" align="center" width="130">
          <template slot-scope="{row}">
            <span>{{ row.acreage?row.acreage:'-' }}</span>
          </template>
        </el-table-column>
        <el-table-column label="单价" align="center" width="130">
          <template slot-scope="{row}">
            <span>{{ row.price?row.price:'-' }}</span>
          </template>
        </el-table-column>
        <el-table-column label="总价" align="center" width="130">
          <template slot-scope="{row}">
            <span>{{ row.amount?row.amount:'-' }}</span>
          </template>
        </el-table-column>
        <el-table-column label="客户名称" align="center" width="130">
          <template slot-scope="{row}">
            <span>{{ row.clientName==null?'-': row.clientName }}</span>
          </template>
        </el-table-column>
        <el-table-column label="业务来源" align="center" width="130">
          <template slot-scope="{row}">
            <span>{{ row.clientSubName }}</span>
          </template>
        </el-table-column>
        <el-table-column label="中介" align="center" width="130">
          <template slot-scope="{row}">
            <span>{{ row.agent==null?'-': row.agent }}</span>
          </template>
        </el-table-column>
        <el-table-column label="联系人" align="center" width="130">
          <template slot-scope="{row}">
            <span>{{ row.contactName}}</span>
          </template>
        </el-table-column>
        <el-table-column label="联系人电话" align="center" width="130">
          <template slot-scope="{row}">
            <span>{{ row.contactTel }}</span>
          </template>
        </el-table-column>
        <el-table-column label="委托人" align="center">
          <template slot-scope="{row}">
            <span>{{ row.bailorA}} {{ row.bailorB}}</span>
          </template>
        </el-table-column>
        <el-table-column label="实收款(元)" align="center" width='120'>
          <template slot-scope="{row}">
            <span style="color:red;font-weight:bold">{{ row.realAmount }}</span>
          </template>
        </el-table-column>
        <el-table-column label="应收款(元)" align="center" width='120'>
          <template slot-scope="{row}">
            <span>{{ row.shouldAmount }}</span>
          </template>
        </el-table-column>
        <el-table-column label="开票金额(元)" align="center" width='120'>
          <template slot-scope="{row}">
            <span>{{ row.invoiceAmount }}</span>
          </template>
        </el-table-column>
        <el-table-column label="下单时间" align="center" width="110" prop="created">
          <template slot-scope="{row}">
            {{ row.created }}
          </template>
        </el-table-column>
        <el-table-column fixed="right" label="操作" width="200" align="center">
          <template slot-scope="{row}">
            <el-button type="text" size="small" @click="orderDetail(row)">详情</el-button>
            <el-button type="text" size="small" @click="openShouldAmountDialog(row)">应收款</el-button>
            <el-button type="text" size="small" @click="openFinanceInvoiceDialog(row.orderFundId,row.realAmount,row.clientName,row.clientSubName)">申请开票</el-button>
            <el-button type="text" size="small" @click="productionList(row.id)">产品包</el-button>
          </template>
        </el-table-column>
      </parentTable>
    </y-page-list-layout>
    <el-dialog :visible.sync="productionListDialog" width="60%" center  custom-class="doWarehouseClass" @closed="cleanProductionListDialog()">
      <el-collapse v-model="activeName" accordion v-if="bag.productions.length>0">
        <el-collapse-item v-for="(r,index) in bag.productions" :name="index">
          <template slot="title">
            <i class="el-icon-document"></i>
            <span style="font-weight:bold;margin-left:10px;margin-right:10px">{{ aliasProductionType(r.production) }}</span>
            {{r.productionNo}}
          </template>
          <el-card style="margin-top:20px" shadow="hover">
            <div slot="header" class="clearfix"  >
              <el-button v-if="(r.production!='STATEMENT' && r.repertoryState !=null && !r.repertoryState)"
                style="float: right; padding: 3px 0;margin-left:10px" type="text" @click="openApplyOutWarehouseList(bag.businessId)">出库申请记录</el-button>
              <el-button v-if="(taskId && r.production!='STATEMENT' && r.repertoryState !=null && !r.repertoryState)"
                style="float: right; padding: 3px 0;margin-left:10px" type="text" @click="openOutApply(bag.businessId)">出库申请</el-button>
            </div>
            <el-form :model="r">
              <el-row>
                <el-col :xs="24" :sm="12" :lg="24" :span="12">
                  <el-form-item label="项目名称：" prop="name"  label-width="140px" class="postInfo-container-item">
                    <el-input :value="r.name" type="text" class="filter-item" disabled readonly/>
                  </el-form-item >
                </el-col>
              </el-row>
              <el-row>
                <el-col :xs="24" :sm="12" :lg="8" :span="12">
                  <el-form-item label="出具报告日期：" prop="createDate"  label-width="140px" class="postInfo-container-item">
                    <el-input :value="r.createDate" type="text" class="filter-item" disabled readonly />
                  </el-form-item >
                </el-col>
                <el-col :xs="24" :sm="12" :lg="8" :span="12">
                  <el-form-item label="实勘日期：" prop="sceneDate"  label-width="140px" class="postInfo-container-item">
                    <el-input :value="r.sceneDate" type="text" class="filter-item" disabled readonly />
                  </el-form-item >
                </el-col>
                <el-col :xs="24" :sm="12" :lg="8" :span="12">
                  <el-form-item label="报告使用期限：" prop="repertoryOutTime"  label-width="140px" class="postInfo-container-item">
                    <el-input :value="r.limitDate" type="text" class="filter-item" disabled readonly />
                  </el-form-item >
                </el-col>
              </el-row>
              <el-row>
                <el-col :xs="24" :sm="12" :lg="8" :span="12">
                  <el-form-item label="评估总价：" prop="evaluateAmount"  label-width="140px" class="postInfo-container-item">
                    <el-input :value="r.evaluateAmount/10000" type="text" class="filter-item" disabled readonly>
                      <template slot="append">万元</template>
                    </el-input>
                  </el-form-item >
                </el-col>
                <el-col :xs="24" :sm="12" :lg="8" :span="12">
                  <el-form-item label="评估面积：" prop="evaluateAcreage"  label-width="140px" class="postInfo-container-item">
                    <el-input :value="r.evaluateAcreage" type="text" class="filter-item" disabled readonly>
                      <template slot="append">平方</template>
                    </el-input>
                  </el-form-item >
                </el-col>
                <el-col :xs="24" :sm="12" :lg="8" :span="12">
                  <el-form-item label="评估单价：" prop="evaluatePrice"  label-width="140px" class="postInfo-container-item">
                    <el-input :value="r.evaluatePrice/10000" type="text" class="filter-item" disabled readonly>
                      <template slot="append">万元</template>
                    </el-input>
                  </el-form-item >
                </el-col>
              </el-row>
              <el-row>
                <el-col :xs="24" :sm="12" :lg="8" :span="12">
                  <el-form-item label="库存状态：" prop="repertoryState"  label-width="140px" class="postInfo-container-item">
                    <el-input :value="r.repertoryState==null?'未入库':(r.repertoryState?'已出库':'已入库')" type="text" class="filter-item" disabled readonly />
                  </el-form-item >
                </el-col>
                <el-col :xs="24" :sm="12" :lg="8" :span="12">
                  <el-form-item label="入库时间：" prop="repertoryInTime"  label-width="140px" class="postInfo-container-item">
                    <el-input :value="r.repertoryInTime" type="text" class="filter-item" disabled readonly />
                  </el-form-item >
                </el-col>
                <el-col :xs="24" :sm="12" :lg="8" :span="12">
                  <el-form-item label="出库时间：" prop="repertoryOutTime"  label-width="140px" class="postInfo-container-item">
                    <el-input :value="r.repertoryOutTime" type="text" class="filter-item" disabled readonly />
                  </el-form-item >
                </el-col>
              </el-row>
              <el-row>
                <el-col :xs="24" :sm="12" :lg="8" :span="12">
                  <el-form-item label="归档状态：" prop="ifSaveFile"  label-width="140px" class="postInfo-container-item">
                    <el-input :value="r.ifSaveFile?'已归档':'未归档'" type="text" class="filter-item" disabled readonly />
                  </el-form-item >
                </el-col>
                <el-col :xs="24" :sm="12" :lg="8" :span="12">
                  <el-form-item label="归档时间：" prop="saveFileDate"  label-width="140px" class="postInfo-container-item">
                    <el-input :value="r.saveFileDate" type="text" class="filter-item" disabled readonly />
                  </el-form-item >
                </el-col>
              </el-row>
            </el-form>
          </el-card>
        </el-collapse-item>
      </el-collapse>
      <el-empty v-else description="暂无产品"></el-empty>
    </el-dialog>
    <el-dialog :visible.sync="outWarehouseListDialog" width="70%" center  custom-class="doWarehouseClass" >
      <div style="margin-top:30px;">
        <el-table
          :data="outApllyList"
          stripe
          style="width: 100%">
          <el-table-column
          align = "center"
            prop="realAmount"
            label="申请时实收款金额(元)"
            width="180">
            <template slot-scope="{row}">
              <span style="color:red;font-weight: bold;">{{ row.realAmount==null?'未收款': row.realAmount}}</span>
            </template>
          </el-table-column>
          <el-table-column
          align = "center"
            prop="applyName"
            label="申请人">
            <template slot-scope="{row}">
              <span>{{ row.applyName }}</span>
            </template>
          </el-table-column>
          <el-table-column
          align = "center"
            prop="remark"
            label="申请原因" show-overflow-tooltip>
            <template slot-scope="{row}">
              <span>{{ row.remark==null?'-':row.remark }}</span>
            </template>
          </el-table-column>
          <el-table-column
          align = "center"
            prop="created"
            label="申请日期" width="150">
            <template slot-scope="{row}">
              <span>{{ row.created }}</span>
            </template>
          </el-table-column>
          <el-table-column
          align = "center"
            prop="departmentCheckState"
            label="部门审核状态">
            <template slot-scope="{row}">
              <span>{{ row.departmentCheckState==null?'-':row.departmentCheckState }}</span>
            </template>
          </el-table-column>
          <el-table-column
          align = "center"
            prop="departmentChecker"
            label="部门审核人">
            <template slot-scope="{row}">
              <span>{{ row.departmentChecker==null?'-':row.departmentChecker }}</span>
            </template>
          </el-table-column>
          <el-table-column
          align = "center"
            prop="departmentCheckTime"
            label="部门审核时间" width="150">
            <template slot-scope="{row}">
              <span>{{ row.departmentCheckTime==null?'-': row.departmentCheckTime}}</span>
            </template>
          </el-table-column>
          <el-table-column
          align = "center"
            prop="departmentReply"
            label="部门审核回复" show-overflow-tooltip>
            <template slot-scope="{row}">
              <span>{{ row.departmentReply==null?'-':row.departmentReply }}</span>
            </template>
          </el-table-column>
          <!-- <el-table-column
          align = "center"
            prop="financeCheckState"
            label="财务审核状态">
            <template slot-scope="{row}">
              <span>{{ row.financeCheckState==null?'-':row.financeCheckState }}</span>
            </template>
          </el-table-column>
          <el-table-column
          align = "center"
            prop="financeChecker"
            label="财务审核人">
            <template slot-scope="{row}">
              <span>{{ row.financeChecker==null?'-':row.financeChecker }}</span>
            </template>
          </el-table-column>
          <el-table-column
          align = "center"
            prop="financeCheckTime"
            label="财务审核时间" width="150">
            <template slot-scope="{row}">
              <span>{{ row.financeCheckTime==null?'-':row.financeCheckTime }}</span>
            </template>
          </el-table-column>
          <el-table-column
          align = "center"
            prop="financeReply"
            label="财务审核回复" show-overflow-tooltip>
            <template slot-scope="{row}">
              <span>{{ row.financeReply==null?'-':row.financeReply }}</span>
            </template>
          </el-table-column> -->
          <!-- <el-table-column fixed="right" label="操作" width="100" align = "center">
          <template slot-scope="{row}">
            <el-button @click="outApplyCancel(row.id,row.productionId,row.financeCheckState)" type="text" size="small">撤销</el-button>
          </template>
        </el-table-column> -->
        </el-table>
      </div>
    </el-dialog>
    <el-dialog :visible.sync="outWarehouseDialog" width="35%" center top="35vh" custom-class="doWarehouseClass" >
      <el-form ref="outWarehouse" :model="outWarehouse" style="margin-left:40px" :rules="rules">
          <el-row>
            <el-col :xs="24" :sm="12" :lg="24" :span="12">
              <el-form-item label="应收款金额：" prop="shouldAmount"  label-width="160px" class="postInfo-container-item">
                    <el-input style="width:300px" v-model="outWarehouse.shouldAmount" type="text" class="filter-item" />
                  </el-form-item >
            </el-col>
          </el-row>
          <el-row>
            <el-col :xs="24" :sm="12" :lg="24" :span="12">
                <el-form-item label="出库原因：" prop="remark"  label-width="160px" class="postInfo-container-item">
                    <el-input style="width:300px" type="textarea" v-model="outWarehouse.remark"></el-input>
                </el-form-item>
            </el-col>
          </el-row>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="outWarehouseDialog = false">取 消</el-button>
        <el-button type="primary" @click="doApplyOut()">确 定</el-button>
      </span>
    </el-dialog>
    <el-dialog :visible.sync="shouldAmountDialog" width="35%" center top="35vh" custom-class="doWarehouseClass"
      @closed="cleanData">
      <el-form ref="fund" :model="fund" style="margin-left:40px">
        <el-row>
          <el-col :xs="24" :sm="12" :lg="24" :span="12">
            <el-form-item label="应收款金额(元)：" prop="shouldAmount" label-width="160px" class="postInfo-container-item"
              :rules="{ required: true, message: '应收款金额不能为空', trigger: 'blur' }">
              <el-input-number :precision="2" :min="null" :value-on-clear="0" v-model.number="fund.shouldAmount"
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
      <el-button type="danger" round @click="openNewInvoiceDialog">申请开票</el-button>
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
              <el-button :disabled="row.state != '审核中'" @click="invoiceDetail(row.id)" type="text" size="small">修改</el-button>
              <el-button :disabled="row.state != '已开票'" @click="cancellation(row.id)" type="text" size="small">申请作废</el-button>
              <el-button  @click="copyInvoice(row)" type="text" size="small">复制</el-button>
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
              <el-date-picker v-model="invoice.planMakeDate" type="date" placeholder="选择日期" value-format="yyyy-MM-dd" style="width:100%">
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
            <el-form-item label="是否收款:" label-width="140px"
              class="postInfo-container-item">
              <el-radio-group :value="invoice.realPayAmount?'已收款':'未收款'">
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
              <el-input v-model.trim="invoice.remark" type="textarea" :autosize="{ minRows: 5, maxRows: 8}"  class="filter-item" />
            </el-form-item>
          </el-col>
        </el-row>
        <!-- <el-divider content-position="left">银行信息</el-divider>
        <el-row>
          <el-col :xs="24" :sm="12" :lg="12" :span="6">
            <el-form-item label="开户行：" prop="bankName" label-width="140px" class="postInfo-container-item">
              <el-input v-model.trim="invoice.bankName" class="filter-item" />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :lg="6" :span="6">
            <el-form-item label="银行账号：" prop="bankAccount" label-width="140px" class="postInfo-container-item">
              <el-input v-model.trim="invoice.bankAccount" class="filter-item" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :xs="24" :sm="12" :lg="12" :span="6">
            <el-form-item label="地址：" prop="bankAddress" label-width="140px" class="postInfo-container-item">
              <el-input v-model.trim="invoice.bankAddress" class="filter-item" />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :lg="6" :span="6">
            <el-form-item label="电话：" prop="bankTel" label-width="140px" class="postInfo-container-item">
              <el-input v-model.trim="invoice.bankTel" class="filter-item" />
            </el-form-item>
          </el-col>
        </el-row> -->
        <!-- <el-divider content-position="left">发票信息</el-divider>
        <el-row>
          <el-col :xs="24" :sm="12" :lg="12" :span="6">
            <el-form-item label="发票号码：" prop="ticketNo" label-width="140px" class="postInfo-container-item">
              <el-input :value="invoice.ticketNo" class="filter-item"  readonly disabled/>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :lg="12" :span="6">
            <el-form-item label="实开金额(元)：" prop="realAmount" label-width="140px" class="postInfo-container-item">
              <el-input :value="invoice.realAmount" class="filter-item" readonly disabled/>
            </el-form-item>
          </el-col>
         
        </el-row>
        <el-row>
          <el-col :xs="24" :sm="12" :lg="12" :span="6">
            <el-form-item label="申请日期：" prop="created" label-width="140px" class="postInfo-container-item">
              <el-input :value="invoice.created" class="filter-item" readonly disabled/>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :lg="12" :span="6">
            <el-form-item label="实开日期：" prop="realMakeDate" label-width="140px" class="postInfo-container-item">
              <el-input :value="invoice.realMakeDate" class="filter-item" readonly disabled/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :xs="24" :sm="12" :lg="12" :span="6">
            <el-form-item label="订单类型：" prop="businessType" label-width="140px" class="postInfo-container-item">
              <el-input :value="invoice.businessType" class="filter-item" readonly disabled/>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :lg="12" :span="6">
            <el-form-item label="开票人：" prop="applyName" label-width="140px" class="postInfo-container-item">
              <el-input :value="invoice.applyName" class="filter-item" readonly disabled/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :xs="24" :sm="12" :lg="12" :span="6">
            <el-form-item label="订单编号：" prop="orderId" label-width="140px" class="postInfo-container-item">
              <el-input :value="invoice.orderId" class="filter-item" readonly disabled/>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :lg="12" :span="6">
            <el-form-item label="产品号：" prop="productionNo" label-width="140px" class="postInfo-container-item">
              <el-input :value="invoice.productionNo" class="filter-item" readonly disabled/>
            </el-form-item>
          </el-col>
        </el-row> -->
       
        <!-- <el-row>
          <el-col :xs="24" :sm="12" :lg="6" :span="6">
            <el-form-item label="税点：" prop="taxRate" label-width="140px" class="postInfo-container-item">
              <el-input-number :precision="2" :min="0" :value-on-clear="0" v-model.number="invoice.taxRate" type="number"
                style="width:206px" class="filter-item" />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :lg="6" :span="6">
            <el-form-item label="税额:" prop="taxAmount" label-width="140px" class="postInfo-container-item">
              <el-input-number :precision="2" :min="0" :value-on-clear="0" v-model.number="invoice.taxAmount"
                type="number" style="width:206px" class="filter-item" />
            </el-form-item>
          </el-col>
        </el-row> -->
       
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="newInvoiceDialog = false">取 消</el-button>
        <el-button type="primary" @click="saveInvoice()">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>
<script>
import YPageListLayout from '@/components/YPageListLayout'
import Breadcrumb from '@/components/Breadcrumb'
import PermissionButton from '@/components/PermissionButton/PermissionButton'
import {isNumber} from '@/utils/validate'
import MoreSearchBar from '@/components/MoreSearchBar'

export default {
  name: 'PersonalMyOrder',
  components: {
    Breadcrumb,
    YPageListLayout,
    PermissionButton,
    MoreSearchBar
  },

  data() {
    return {
      rules:{
            shouldAmount:[{required:true,message:"应收款金额必填",trigger:'blur'},{validator:isNumber,trigger:'blur'}],
          },
      pageData: { records: [] },
      listLoading: false,
      listQuery: {
        page: 1,
        size: 20,
        current: 1,
        descs: null,
        ascs:null,
        keyword:null,
        startDate:null,
        endDate:null
      },
      productionListDialog:false,
      bag:{
        businessId:null,
        businessType:null,
        productions:[]
      },
      activeName: '0',
      outWarehouseListDialog:false,
      outWarehouseDialog:false,
      outWarehouse:{
        id:null,
        orderFundId:null,
        remark:null,
        realAmount:null
      },
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
        productionFundId: null,
        evaluateAmount: null
      },
      outApllyList:[],
      shouldAmountDialog:false,
      makeInvoiceListDialog:false,
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
        realPayAmount:null,
        realAmount:null,
        realMakeDate:null
      },
      newInvoiceDialog:false,
      invoiceData: [],
    }
  },
  created() {
    this.getPage();
    this.todoBusinessId = this.$route.query.todoBusinessId;
    this.taskId = this.$route.query.tId;
    this.currentNodeId = this.$route.query.cId;
    this.orderId = this.$route.query.orderId;
    if(this.todoBusinessId){
      this.productionListDialog = true;
      this.productionList(this.todoBusinessId)
    }
  },
  methods: {
    getPage() {
      this.$api.personal.myOrder(this.listQuery).then(res=>{
        if (res.code ===200){
            this.pageData = res.data;
        }
      })
    },

    fliterSearch(params){
      this.$api.personal.myOrder(params).then(res=>{
        if (res.code ===200){
            this.pageData = res.data;
        }
      })
    },

    searchList() {
      // 重置分页
      this.listQuery.page = 1
      this.listQuery.size = 20
      this.getPage()
    },

    resetParams(){
      this.$router.push({ query: {} });
      this.listQuery = {
          current: 1,
          size: 20,
          descs: 'created',
      }
      this.getPage();
    },

    orderDetail(row){
      this.$router.push(`/personal/order?id=${row.id}&couldEdit=true&recall=true&couldBack=true&back=${'/personal/my/order'}`)
    },
    productionList(id){
      this.productionListDialog = true;
      if (id){
          this.$api.businessProduction.personalBag(id).then(res=>{
            if (res.code === 200 && res.data!=null){
                let prods = res.data.productions
                if (prods){
                  let prodType = prods.map(item=>item.production)
                  if (prodType.includes("TECHNIC") && prodType.includes("FINAL")){
                    prods = prods.filter(item=>item.production!='TECHNIC')
                    for (let i in prods){
                       if (prods[i].production==='FINAL'){
                        prods[i].production = 'TECHNIC+FINAL'
                       }
                    }
                    this.bag = res.data;
                    this.bag.productions = prods;
                  }
                }
                this.bag = res.data;

            }
          })
      }
    },
    aliasProductionType(code){
        if (code === 'STATEMENT'){
            return '房地产意见书';
        }
        if (code === 'LETTER'){
            return '复评函';
        }
        if (code === 'TECHNIC'){
            return '报告(技术报告)';
        }
        if (code === 'FINAL'){
            return '报告(结果报告)';
        }
        return '报告(技术报告、结果报告)'
    },
    openApplyOutWarehouseList(businessId){
      this.getListByBizTypeProductionId(businessId);
      this.outWarehouseListDialog = true;
    
    },
    getListByBizTypeProductionId(businessId){
      this.$api.productionOutWarehouse.getPersonalByOrderId(businessId).then(res=>{
          if (res.code === 200){
              this.outApllyList = res.data;
          }
        })
    },
    doApplyOut(){
      this.$refs.outWarehouse.validate(valid=>{
        if (valid){
          let taskRecordDTO = new Object();
          taskRecordDTO.recordId = this.taskId;
          taskRecordDTO.taskData = this.outWarehouse;
          this.$api.productionOutWarehouse.add(taskRecordDTO).then(res=>{
            if (res.code === 200 && res.data){
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
            }else {
              this.$notify({
                  title: '失败',
                  message: '出库申请提交失败',
                  type: 'error',
                  duration: 2000
                });
            }
          })
        }
      })
    },
    commitNode(){
      let commit = new Object;
      commit.instanceNodeId = this.currentNodeId;
      commit.state = 'PASS';
      commit.comments = this.outWarehouse.remark;
      commit.businessSubId = this.orderId;
      this.$api.workflow.commit(commit).then(res=>{
        if (res.code === 200 && res.data){
          this.$notify({
              title: '成功',
              message: '工作流节点提交成功。',
              type: 'success',
              duration: 2000
            });
        }else{
          this.$notify({
              title: '失败',
              message: '工作流节点提交失败，请联系管理员。',
              type: 'error',
              duration: 2000
            });
        }
      })
    },
    cleanProductionListDialog(){
      this.bag.productions = [];
      this.bag.businessId=null;
      this.bag.property = null;
    },
    openOutApply(businessId){
      const applys = this.outApllyList;
      if (applys.length===0){
        this.getListByBizTypeProductionId(businessId);
      }
      for (let i in applys){
        if (applys[i].departmentCheckState==null){
          this.$notify({
              title: '提示',
              message: '已有申请待审核，请勿重复申请。',
              type: 'info',
              duration: 2000
            });
            return;
        }
      }
      this.outWarehouse.businessType = 'PERSONAL_BUSINESS';
      this.outWarehouse.businessId = businessId;
      this.outWarehouseDialog = true;
    },
    openFinanceInvoiceDialog(orderFundId,realAmount,clientName,clientSubName) {
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
        this.invoice.title = clientName+clientSubName;
        this.getFinanceInvoiceList();
      }
    },
    openShouldAmountDialog(row) {
      this.shouldAmountDialog = true;
      this.fund.id = row.orderFundId;
      this.fund.businessType = 'PERSONAL_BUSINESS';
      this.fund.businessId = row.id;
      this.fund.shouldAmount = row.shouldAmount;
      this.fund.remark = row.remark;
      this.fund.orderId = row.orderId;
      this.fund.orderName = row.orderName;
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
    saveShouldAmount() {
      this.$refs.fund.validate(valid => {
        if (valid) {
          if (this.fund.id) {
            this.$api.orderFund.edit(this.fund).then(res => {
              if (res.code === 200 && res.data) {
                this.$notify({
                  title: '成功',
                  message: '订单应收款已完善',
                  type: 'success',
                  duration: 1000
                });
                this.getPage();
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
                this.getPage();
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
        }
      })
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
    getFinanceInvoiceList() {
      this.$api.financeInvoice.getList(this.invoice.orderFundId,this.invoice.productionFundId).then(res => {
        if (res.code === 200) {
          this.invoiceData = res.data
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
    stateStyle(state){
        if (state==='已开票'){
          return 'color:green'
        }else{
          return 'color:red'
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
            this.$prompt('作废原因:','已开发票作废',{
              confirmButtonText: '确认',
              cancelButtonText: '取消',
              type: 'warning',
          }).then(({value}) => {
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
    copyInvoice(row){
        this.invoice = row;
        this.invoice.id = null;
        this.invoice.state = null;
        this.invoice.reason = null;
        this.invoice.created = null;
        this.invoice.modified = null;
        this.newInvoiceDialog = true;
    },
    // 个贷我的订单导出
    exportMyOrder() {
        this.$utils.exportUtil(
            "/personal/exportMyOrder", this.listQuery,
            "导出"
        );
    }
  },
  
}
</script>
<style lang="scss" scoped>
     /deep/.doWarehouseClass {
     border-radius: 10px;
    }
</style>
