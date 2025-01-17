<template>
  <div class="app-container">
    <div class="title-container">
      <breadcrumb id="breadcrumb-container" class="breadcrumb-container" />
    </div>
    <y-page-list-layout :page-list="pageData" :page-para="listQuery" :get-page-list="getList" :pageSizes="pageSizes">
      <template slot="left">
        <PermissionButton menu-code="_views_finance_real_fund_create" class-name="filter-item" type="success" icon="el-icon-circle-plus-outline"
          :page-jump="false" round style="float: left" @click="createdRealFundDialog = true" />
        <el-input v-model="listQuery.keyword" placeholder="关键字搜索..." clearable
          style="margin-left: 20px;width: 300px;float: left;">
        </el-input>
        <el-select style="margin-left: 20px;float: left;" v-model="listQuery.notFinished" @change="getList()" >
          <el-option  label="已认领" :value="false">  </el-option>
          <el-option  label="未认领" :value="true"></el-option>
        </el-select>
        <el-date-picker
          style="margin-left: 20px;float: left;"
          v-model="payDate"
          type="daterange"
          align="center"
          unlink-panels
          range-separator="至"
          start-placeholder="到账时日（开始）"
          end-placeholder="到账时日（结束）"
          :picker-options="pickerOptions" value-format="yyyy-MM-dd">
        </el-date-picker>
        <el-button class="filter-item" style="margin-left: 10px;float: left;" type="primary" @click="searchList" v-loading.fullscreen.lock="loading" round>搜索
        </el-button>
        <el-button class="filter-item" style="float: left;" round type="success" @click="resetSearch()">重置
        </el-button>
        <el-button class="filter-item" style="margin-left: 10px;float: left;"  type="info" @click="exportData()" round>导出
        </el-button>
      </template>
      <el-table ref="table" size="medium" slot="table" :data="pageData.records" show-summary  border
        :summary-method="getSummaries" fit stripe highlight-current-row
        :header-row-style="{ color: '#333333' }" style="
          border-left: 1px solid #ebeced;
          border-right: 1px solid #ebeced;
          color: #333333;">
        <el-table-column label="付款方" align="center" width='250' >
          <template slot-scope="{row}">
            <span>{{ row.payer }}</span>
          </template>
        </el-table-column>
        <el-table-column label="付款银行" align="center" width='100'  >
          <template slot-scope="{row}">
            <span>{{ row.payerBank?row.payerBank:'-' }}</span>
          </template>
        </el-table-column>
        <el-table-column label="付款金额(元)" align="center" prop="amount" >
          <template slot-scope="{row}">
            <span>{{ row.amount }}</span>
          </template>
        </el-table-column>
        <el-table-column label="到账时日" align="center">
          <template slot-scope="{row}">
            <span>{{ row.payDatetime }}</span>
          </template>
        </el-table-column>
        <el-table-column label="已认领(元)" align="center" prop="claimAmount" >
          <template slot-scope="{row}">
            <span  style="color:green">{{ row.claimAmount }}</span>
          </template>
        </el-table-column>
        <el-table-column label="未认领(元)" align="center" prop="notClaimAmount" >
          <template slot-scope="{row}">
            <span style="color:red">{{ row.notClaimAmount }}</span>
          </template>
        </el-table-column>
        <el-table-column label="录入人" align="center">
          <template slot-scope="{row}">
            <span>{{ row.creator}}</span>
          </template>
        </el-table-column>
        <el-table-column label="录入时间" align="center" width='170'>
          <template slot-scope="{row}">
            <span>{{ row.created}}</span> 
          </template>
        </el-table-column>
        <el-table-column label="全部认领" align="center" >
          <template slot-scope="{row}">
            <i v-if="row.notClaimAmount<=0" style="font-size:20px; color:green" class="el-icon-success"></i>
            <i v-else style="font-size:20px; color:RGB(255,106,106)" class="el-icon-warning"></i>
          </template>
        </el-table-column>
        <el-table-column label="备注" align="center" width='170'>
          <template slot-scope="{row}">
            <span>{{ row.remark}}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" width="150" >
          <template slot-scope="{row}">
              <PermissionButton menu-code="_views_claim_list" class-name="filter-item" type="text"
              :page-jump="false"  @click="clamiList(row.id,row.notClaimAmount,row.payer,row.amount)" />
              <PermissionButton menu-code="_views_real_fund_edit" class-name="filter-item" type="text"
              :page-jump="false"  @click="detail(row.id)" />
              <PermissionButton menu-code="_views_real_fund_remove" class-name="filter-item" type="text"
              :page-jump="false"  @click="remove(row.id)" />
          </template>
        </el-table-column>
      </el-table>
    </y-page-list-layout>
    <el-dialog :visible.sync="createdRealFundDialog" width="45%" center  custom-class="doWarehouseClass" @closed="cleanFund">
      <el-form ref="realFund" :model="realFund" >
        <el-divider content-position="left">实收款信息</el-divider>
          <el-row>
            <el-col :xs="24" :sm="12" :lg="24" :span="6">
              <el-form-item
                label="付款方名称："
                prop="payer"
                :rules="{required: true, message: '付款方名称不能为空', trigger: 'blur'}"
                label-width="140px"
                class="postInfo-container-item"
              >
                <el-input v-model.trim="realFund.payer" class="filter-item" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :xs="24" :sm="12" :lg="12" :span="6">
              <el-form-item
                label="付款金额(元):"
                prop="amount"
                :rules="{required: true, message: '付款金额不能为空', trigger: 'blur'}"
                label-width="140px"
                class="postInfo-container-item"
              >
              <el-input-number :precision="2" :min="0" :value-on-clear="0" v-model.number="realFund.amount" type="number" 
                style="width:100%" class="filter-item" />
              </el-form-item>
            </el-col>
            <el-col :xs="24" :sm="12" :lg="12" :span="6">
              <el-form-item
                label="到账时间："
                prop="payDatetime"
                :rules="{required: true, message: '到账时间不能为空', trigger: 'blur'}"
                label-width="140px" 
                class="postInfo-container-item"
              >
                <el-date-picker
                  v-model="realFund.payDatetime" type="date" value-format="yyyy-MM-dd"  placeholder="选择日期" style="width:100%">
                </el-date-picker>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :xs="24" :sm="12" :lg="12" :span="6">
              <el-form-item
                label="付款银行："
                prop="payerBank"
                label-width="140px"
                class="postInfo-container-item"
              >
                <el-input v-model.trim="realFund.payerBank" class="filter-item" />
              </el-form-item>
            </el-col>
            <el-col :xs="24" :sm="12" :lg="12" :span="6">
              <el-form-item
                label="付款银行账号："
                prop="payerAccount"
                label-width="140px"
                class="postInfo-container-item"
              >
              <el-input v-model.trim="realFund.payerAccount" class="filter-item" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :xs="24" :sm="12" :lg="24" :span="6">
              <el-form-item
                label="备注："
                prop="remark"
                label-width="140px"
                class="postInfo-container-item"
              >
                <el-input v-model.trim="realFund.remark" class="filter-item" type="textarea" />
              </el-form-item>
            </el-col>
          </el-row>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="createdRealFundDialog = false">取 消</el-button>
        <el-button type="primary" @click="saveRealFund()">确 定</el-button>
      </span>
    </el-dialog>
    <el-dialog :visible.sync="claimListDialog" width="90%" center  custom-class="doWarehouseClass">
      <div style="margin-bottom:20px;font-size:20px;">
        <span>付款方：</span><span style="color:red;font-weight:bold">{{payer}}</span><span style="margin-left:20px">付款金额：</span><span style="color:red;font-weight:bold">{{amount}}￥</span><span style="margin-left:20px">的认领记录:</span>
      </div>
      <parentTable
        :data="claimListData" border stripe :isBoard=300
        style="width: 100%">
        <el-table-column
          align = "center"
          prop="businessType"
          label="业务类型"
          width="180">
        </el-table-column>
        <el-table-column show-overflow-tooltip
        align = "center"
          prop="orderName" 
          label="订单名称"
          width="300">
        </el-table-column>
        <el-table-column show-overflow-tooltip
        align = "center"
          prop="orderId"
          label="订单号"
          width="250">
        </el-table-column>
        <el-table-column show-overflow-tooltip
        align = "center"
          prop="productionNo"
          label="产品号"
          width="250">
        </el-table-column>
        <el-table-column
        align = "center"
          prop="shouldAmount"
          label="应收金额(元)">
          <template slot-scope="{row}">
            <span>{{ row.productionShouldAmount?row.productionShouldAmount:row.shouldAmount}}</span>
          </template>
        </el-table-column>
        <el-table-column
        align = "center"
          prop="claimAmount"
          label="认领金额(元)">
          <template slot-scope="{row}">
            <span style="color: red;">{{ row.claimAmount}}</span>
          </template>
        </el-table-column>
        <el-table-column
        align = "center"
          prop="claimName"
          label="认领人">
        </el-table-column>
        <el-table-column
        align = "center"
          prop="created" width="180"
          label="认领时间">
        </el-table-column>
        <el-table-column
        align = "center"
          label="操作">
          <template slot-scope="scope">
            <el-button @click="removeClaim(scope.row.id)" type="text" size="small">删除</el-button>
          </template>
        </el-table-column>
      </parentTable>
    </el-dialog>
  </div>
</template>
<script>
import YPageListLayout from '@/components/YPageListLayout'
import Breadcrumb from '@/components/Breadcrumb'
import excelImport from "@/components/excelImport.vue";


export default {
  name: 'financeRealFund',
  components: {
    Breadcrumb,
    YPageListLayout,
    excelImport
  },

  
  data() {
    return {
      pageData: { records: [] },
      listQuery: {
        page: 1,
        size: 500,
        descs: 'id',
        keyword:null,
        startDate:null,
        endDate:null,
        notFinished:true
      },
      createdRealFundDialog: false,
      claimListDialog:false,
      payDate:'',
      claimListData:null,
      realFund:{
        id:null,
        payer:null,
        payerBank:null,
        payerAccount:null,
        amount:null,
        claimAmount:null,
        notClaimAmount:null,
        payDatetime:null,
        remark:null,
        created:null,
        creator:null
      },
      payer:null,
      amount:null,
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
      loading: false,
      pageSizes:[10, 20, 30, 50,100,200,500,1000]
    }
  },
  created() {
    this.getList();
  },
  methods: {
    resetSearch() {
      this.$router.push({ query: {} });
      this.payDate = '';
      this.listQuery = {
        current: 1,
        size: 500,
        descs: 'id',
        notFinished:true
      }
      this.getList()
    },
    searchList() {
      // 重置分页
     
      this.listQuery.page = 1
      this.listQuery.size = 500
      if (this.payDate){
        this.listQuery.startDate = this.payDate[0];
        this.listQuery.endDate = this.payDate[1];
      }
     
      this.getList()
     
    },
    getList() {
      this.loading = true;
      this.$api.financeRealFund.list(Object.assign({}, this.listQuery)).then(res=>{
        if (res.code ===200){
            this.pageData = res.data;
        }
      })
      setTimeout(() => {
          this.loading = false;
        }, 500);
    },
    saveRealFund(){
      this.$refs.realFund.validate(valid=>{
        if (valid){
          if (this.realFund.id){
            console.log(this.realFund)
              this.$api.financeRealFund.edit(this.realFund).then(res=>{
                if (res.code ===200 && res.data){
                    this.$notify({
                      title: '成功',
                      message: '修改实收款信息成功',
                      type: 'success',
                      duration: 2000
                    });
                    this.getList();
                    this.createdRealFundDialog = false;
                }else {
                  this.$notify({
                      title: '失败',
                      message: '修改实收款信息失败',
                      type: 'error',
                      duration: 2000
                  });
                }
            })
          }else{
            this.$api.financeRealFund.add(this.realFund).then(res=>{
              if (res.code ===200 && res.data){
                  this.$notify({
                    title: '成功',
                    message: '录入实收款信息成功',
                    type: 'success',
                    duration: 2000
                  });
                  this.getList();
                  this.createdRealFundDialog = false;
              }else {
                this.$notify({
                    title: '失败',
                    message: '录入实收款信息失败',
                    type: 'error',
                    duration: 2000
                });
              }
            })
          }
        }
      })
    },
    clamiList(id,notClaimAmount,payer,amount){
      this.claimListDialog = true;
      this.notClaimAmount = notClaimAmount;
      this.payer=payer;
      this.amount = amount;
      this.$api.financeClaim.getList(id).then(res=>{
        if (res.code ===200){
          this.claimListData = res.data;
        }
      })
    },
    detail(id){
      this.createdRealFundDialog = true;
      this.$api.financeRealFund.detail(id).then(res=>{
        if (res.code === 200 ){
          this.realFund = res.data
        }
      })
    },
    remove(id){
      this.$confirm('请确认是否删除此回款记录?','提示',{
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
          center: true
      }).then(()=>{
          this.$api.financeRealFund.delete(id).then(res=>{
            if (res.code === 200 && res.data){
                this.$notify({
                    title: '成功',
                    message: '实收款信息已删除',
                    type: 'success',
                    duration: 2000
                  });
                this.getList();
            }else {
                this.$notify({
                    title: '失败',
                    message: '实收款信息删除失败',
                    type: 'error',
                    duration: 2000
                });
            }
          })
      })
    },

    removeClaim(id){
      this.$confirm('请确认是否删除此条认领记录？','提示',{
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
        center: true       
      }).then(()=>{
        if (id){
          this.$api.financeClaim.delete(id).then(res=>{
            if (res.code ===200 && res.data){
              this.$notify({
                    title: '成功',
                    message: '删除认领记录成功',
                    type: 'success',
                    duration: 2000
                  });
                  this.$api.financeClaim.getList(id).then(res=>{
                    if (res.code ===200){
                      this.claimListData = res.data;
                    }
                  })
                  this.getList();
            }else {
              this.$notify({
                    title: '失败',
                    message: '删除认领记录失败',
                    type: 'error',
                    duration: 2000
              });
            }
          })
        }
      })
      
    },
   
    cleanFund(){
      this.realFund.id=null;
      this.realFund.payer=null;
      this.realFund.payerBank=null;
      this.realFund.payerAccount=null;
      this.realFund.amount=null;
      this.realFund.claimAmount=null;
      this.realFund.payDatetime=null;
      this.realFund.remark=null;
      this.realFund.created=null;
      this.realFund.creator=null;
    },

    exportData(){
      this.$utils.exportUtil(
      "/financeRealFund/export", this.listQuery,
      "导出"
      );
    },
    getSummaries(param){
      const { columns, data } = param;
        const sums = [];
        columns.forEach((column, index) => {
          
          if (index === 0) {
            sums[index] = '合计';
            return;
          }
          const values = data.map(item => Number(item[column.property]));
          if (!values.every(value => isNaN(value))) {
            sums[index] = values.reduce((prev, curr) => {
              const value = Number(curr);
              if (!isNaN(value)) {
                return prev + curr;
              } else {
                return prev;
              }
            }, 0).toFixed(2);
            sums[index] += ' 元';
          } else {
            sums[index] = '-';
          }
        });

        return sums;
    }

  },
  
}
</script>
<style lang="scss" scoped>
    /deep/.doWarehouseClass {
      border-radius: 10px;
    }
  .redPoint{
      color:red;
      position: absolute;
      top:8px;
      left:5px;
    }
</style>
