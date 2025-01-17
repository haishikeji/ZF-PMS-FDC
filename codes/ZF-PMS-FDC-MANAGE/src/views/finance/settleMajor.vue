<template>
  <div class="app-container organization-index">
    <div class="title-container">
      <breadcrumb id="breadcrumb-container" class="breadcrumb-container" />
    </div>
    <el-tabs v-model="activeName" @tab-click="handleClick" type="border-card" style="margin-top: 10px;">
      <el-tab-pane label="评估人员" name="evaluate">
        <y-page-list-layout :get-page-list="majorEvaluatorSettleProd" :page-list="evaluatorProd" :page-para="listQuery1">
          <template slot="left">
            <el-input v-model="listQuery1.keyword" placeholder="项目编号、产品号、负责人、客户经理" clearable
              style="width: 300px;float: left;">
            </el-input>
            <el-date-picker clearable v-model="findDate" type="daterange" align="center" unlink-panels range-separator="至"
              start-placeholder="统计日期（开始）" end-placeholder="统计日期（结束）" :picker-options="pickerOptions"
              value-format="yyyy-MM-dd" style="margin-left: 20px;float: left;">
            </el-date-picker>
            <el-date-picker  clearable v-model="paymentDate" type="daterange" align="center" unlink-panels
                        range-separator="至" start-placeholder="到账日期（开始）" end-placeholder="到账日期（结束）" :picker-options="pickerOptions"
                        value-format="yyyy-MM-dd" style="margin-left: 20px;float: left;">
              </el-date-picker>
            <el-button class="filter-item" style="margin-left:20px;float: left;" type="primary" @click="searchList1()"
              round>搜索
            </el-button>
            <el-button class="filter-item" style="float: left;" round type="success" @click="resetSearch1()">重置
            </el-button>
            <el-button class="filter-item" style="float: left;" round type="info" @click="exportEvaluateSettleProd()">导出
            </el-button>
          </template>
          <parentTable :data="evaluatorProd.records" slot="table" style="width: 100%;">
            <el-table-column label="序号" align="center" type="index">
            </el-table-column>
            <el-table-column label="项目编号" width="110" align="center">
              <template slot-scope="{row}">
                <span>{{ row.orderId }}</span>
              </template>
            </el-table-column>
            <el-table-column label="产品号" width="200" align="center">
              <template slot-scope="{row}">
                <span>{{ row.reportNo }}</span>
              </template>
            </el-table-column>
            <el-table-column label="结算日期" width="110" align="center">
              <template slot-scope="{row}">
                <span style="color:red">{{ row.settleDate }}</span>
              </template>
            </el-table-column>
            <el-table-column label="认领日期" width="110" align="center">
              <template slot-scope="{row}">
                <span>{{ row.claimDate }}</span>
              </template>
            </el-table-column>
            <el-table-column label="归档日期" width="110" align="center">
              <template slot-scope="{row}">
                <span>{{ row.saveFileDate }}</span>
              </template>
            </el-table-column>
            <el-table-column label="到账日期" width="110" align="center">
              <template slot-scope="{row}">
                <span>{{ row.payDateTime }}</span>
              </template>
            </el-table-column>
            <el-table-column label="项目名称" width="200" align="center" show-overflow-tooltip>
              <template slot-scope="{row}">
                <span>{{ row.name }}</span>
              </template>
            </el-table-column>
            <el-table-column label="客户名称" align="center">
              <template slot-scope="{row}">
                <span>{{ row.clientName }}</span>
              </template>
            </el-table-column>
            <el-table-column label="业务来源" align="center">
              <template slot-scope="{row}">
                <span>{{ row.clientSubName }}</span>
              </template>
            </el-table-column>
            <el-table-column label="委托方" width="100" align="center">
              <template slot-scope="{row}">
                <span>{{ row.bailor }}</span>
              </template>
            </el-table-column>
            <!-- <el-table-column label="分配产值"   align="center">
                <template slot-scope="{row}">
                  <span>{{ row.clientSubName }}</span>
                </template>
              </el-table-column> -->
            <el-table-column label="市场部" align="center">
              <template slot-scope="{row}">
                <span>{{ row.marketDepartmentName }}</span>
              </template>
            </el-table-column>
            <el-table-column label="评估部" align="center">
              <template slot-scope="{row}">
                <span>{{ row.evaluateDepartmentName }}</span>
              </template>
            </el-table-column>
            <el-table-column label="客户经理" align="center">
              <template slot-scope="{row}">
                <span>{{ row.clientManager }}</span>
              </template>
            </el-table-column>
            <el-table-column label="负责人" align="center">
              <template slot-scope="{row}">
                <span>{{ row.principal }}</span>
              </template>
            </el-table-column>
            <!-- <el-table-column label="参与人"  align="center">
                <template slot-scope="{row}">
                  <span>{{ row.principal }}</span>
                </template>
              </el-table-column> -->
            <el-table-column label="结算金额(元)" align="center">
              <template slot-scope="{row}">
                <span style="color:red;font-weight: bold;">{{ row.claimAmount }}</span>
              </template>
            </el-table-column>
            <el-table-column label="评估价值(万元)" align="center">
              <template slot-scope="{row}">
                <span>{{ row.evaluateAmount / 10000 }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="invoiceUser" label="开票人" align="center" show-overflow-tooltip>
              <template slot-scope="{row}">
                <span v-for="(item, index) in row.invoices">{{ item.invoiceUser }} <br> </span>
              </template>
            </el-table-column>
            <el-table-column prop="invoiceNo" label="发票号" align="center" show-overflow-tooltip>
              <template slot-scope="{row}">
                <span v-for="(item, index) in row.invoices">{{ item.invoiceNo }} <br> </span>
              </template>
            </el-table-column>
            <el-table-column prop="invoiceDate" label="开票日期" align="center" show-overflow-tooltip>
              <template slot-scope="{row}">
                <span v-for="(item, index) in row.invoices">{{ item.invoiceDate }} <br> </span>
              </template>
            </el-table-column>
          </parentTable>
        </y-page-list-layout>
      </el-tab-pane>
      <el-tab-pane label="市场人员" name="market">
        <y-page-list-layout :get-page-list="majorMarketSettleProd" :page-list="marketProd" :page-para="listQuery2">
          <template slot="left">
            <el-input v-model="listQuery2.keyword" placeholder="项目编号、产品号、负责人、客户经理" clearable
              style="width: 300px;float: left;">
            </el-input>
            <el-date-picker clearable v-model="findDate1" type="daterange" align="center" unlink-panels
              range-separator="至" start-placeholder="统计日期（开始）" end-placeholder="统计日期（结束）" :picker-options="pickerOptions"
              value-format="yyyy-MM-dd" style="margin-left:20px;float: left;">
            </el-date-picker>
            <el-date-picker  clearable v-model="paymentDate1" type="daterange" align="center" unlink-panels
                        range-separator="至" start-placeholder="到账日期（开始）" end-placeholder="到账日期（结束）" :picker-options="pickerOptions"
                        value-format="yyyy-MM-dd" style="margin-left: 20px;float: left;">
              </el-date-picker>
            <el-button class="filter-item" style="margin-left:20px;float: left;" type="primary" @click="searchList2()"
              round>搜索
            </el-button>
            <el-button class="filter-item" style="float: left;" round type="success" @click="resetSearch2()">重置
            </el-button>
            <el-button class="filter-item" style="float: left;" round type="info" @click="exportMarketSettleProd()">导出
            </el-button>
          </template>
          <parentTable :data="marketProd.records" slot="table" style="width: 100%;">
            <el-table-column label="序号" align="center" type="index">
            </el-table-column>
            <el-table-column label="项目编号" width="110" align="center">
              <template slot-scope="{row}">
                <span>{{ row.orderId }}</span>
              </template>
            </el-table-column>
            <el-table-column label="产品号" width="200" align="center">
              <template slot-scope="{row}">
                <span>{{ row.reportNo }}</span>
              </template>
            </el-table-column>
            <el-table-column label="结算日期" width="110" align="center">
              <template slot-scope="{row}">
                <span style="color:red">{{ row.settleDate }}</span>
              </template>
            </el-table-column>
            <el-table-column label="认领日期" width="110" align="center">
              <template slot-scope="{row}">
                <span>{{ row.claimDate }}</span>
              </template>
            </el-table-column>
            <el-table-column label="送达日期" width="110" align="center">
              <template slot-scope="{row}">
                <span>{{ row.deliveryDate }}</span>
              </template>
            </el-table-column>
            <el-table-column label="到账日期" width="110" align="center">
              <template slot-scope="{row}">
                <span>{{ row.payDateTime }}</span>
              </template>
            </el-table-column>
            <el-table-column label="项目名称" width="200" align="center" show-overflow-tooltip>
              <template slot-scope="{row}">
                <span>{{ row.name }}</span>
              </template>
            </el-table-column>
            <el-table-column label="客户名称" align="center">
              <template slot-scope="{row}">
                <span>{{ row.clientName }}</span>
              </template>
            </el-table-column>
            <el-table-column label="业务来源" align="center">
              <template slot-scope="{row}">
                <span>{{ row.clientSubName }}</span>
              </template>
            </el-table-column>
            <el-table-column label="委托方" width="100" align="center">
              <template slot-scope="{row}">
                <span>{{ row.bailor }}</span>
              </template>
            </el-table-column>
            <!-- <el-table-column label="分配产值"   align="center">
                <template slot-scope="{row}">
                  <span>{{ row.clientSubName }}</span>
                </template>
              </el-table-column> -->
            <el-table-column label="市场部" align="center">
              <template slot-scope="{row}">
                <span>{{ row.marketDepartmentName }}</span>
              </template>
            </el-table-column>
            <el-table-column label="评估部" align="center">
              <template slot-scope="{row}">
                <span>{{ row.evaluateDepartmentName }}</span>
              </template>
            </el-table-column>
            <el-table-column label="客户经理" align="center">
              <template slot-scope="{row}">
                <span>{{ row.clientManager }}</span>
              </template>
            </el-table-column>
            <el-table-column label="负责人" align="center">
              <template slot-scope="{row}">
                <span>{{ row.principal }}</span>
              </template>
            </el-table-column>
            <!-- <el-table-column label="参与人"  align="center">
                <template slot-scope="{row}">
                  <span>{{ row.principal }}</span>
                </template>
              </el-table-column> -->
            <el-table-column label="结算金额(元)" align="center">
              <template slot-scope="{row}">
                <span style="color:red;font-weight: bold;">{{ row.claimAmount }}</span>
              </template>
            </el-table-column>
            <el-table-column label="评估价值(万元)" align="center">
              <template slot-scope="{row}">
                <span>{{ row.evaluateAmount / 10000 }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="invoiceUser" label="开票人" align="center" show-overflow-tooltip>
              <template slot-scope="{row}">
                <span v-for="(item, index) in row.invoices">{{ item.invoiceUser }} <br> </span>
              </template>
            </el-table-column>
            <el-table-column prop="invoiceNo" label="发票号" align="center" show-overflow-tooltip>
              <template slot-scope="{row}">
                <span v-for="(item, index) in row.invoices">{{ item.invoiceNo }} <br> </span>
              </template>
            </el-table-column>
            <el-table-column prop="invoiceDate" label="开票日期" align="center" show-overflow-tooltip>
              <template slot-scope="{row}">
                <span v-for="(item, index) in row.invoices">{{ item.invoiceDate }} <br> </span>
              </template>
            </el-table-column>
          </parentTable>
        </y-page-list-layout>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>
<script>
import Breadcrumb from '@/components/Breadcrumb'
import YPageListLayout from '@/components/YPageListLayout'

export default {
  name: 'settleMajor',
  components: {
    Breadcrumb,
    YPageListLayout
  },

  data() {
    return {
      activeName: 'evaluate',
      listQuery1: {
        page: 1,
        size: 10,
        descs: 'id',
        startDate: '2024-05-01 00:00:00',
        endDate: '2024-06-01 00:00:00',
        payStartDate:'',
        payEndDate:'',
        keyword:null
      },
      listQuery2: {
        page: 1,
        size: 10,
        descs: 'id',
        startDate: '2024-05-01 00:00:00',
        endDate: '2024-06-01 00:00:00',
        payStartDate:'',
        payEndDate:'',
        keyword:null
      },
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
      findDate: [],
      findDate1: [],
      evaluatorProd: {
        records: []
      },
      marketProd: {
        records: []
      },
      paymentDate:[],
      paymentDate1:[]
    }
  },
  created() {
    this.findDate.push(this.getDefaultStartDate());
    this.findDate.push(this.getDefaultEndDate());
    this.findDate1.push(this.getDefaultStartDate());
    this.findDate1.push(this.getDefaultEndDate());
    this.majorEvaluatorSettleProd();
  },
  methods: {

    getDefaultStartDate() {
      const currentDate = new Date();
      const currentYear = currentDate.getFullYear();
      var currentMonth = String(currentDate.getMonth()).padStart(2, "0");
      return currentYear + '-' + currentMonth + '-' + '23'
    },

    getDefaultEndDate() {
      const currentDate = new Date();
      const currentYear = currentDate.getFullYear();
      var currentMonth = String(currentDate.getMonth() + 1).padStart(2, "0");
      return currentYear + '-' + currentMonth + '-' + '22'
    },

    handleClick(tab, event) {
      if (tab.name === 'market') {
        this.majorMarketSettleProd();
      }
      if (tab.name === 'evaluate') {
        this.majorEvaluatorSettleProd()
      }
    },


    resetSearch1() {
      this.$router.push({ query: {} });
      this.findDate = [this.getDefaultStartDate(), this.getDefaultEndDate()];
      this.paymentDate = [];
      this.listQuery1 = {
        current: 1,
        size: 10,
        descs: 'id',
        startDate: null,
        endDate: null,
        payStartDate:null,
        payEndDate:null
      }
      this.majorEvaluatorSettleProd()
    },

    searchList1() {
      // 重置分页
      this.listQuery1.page = 1
      this.listQuery1.size = 10
      if (this.findDate) {
        this.listQuery1.startDate = this.findDate[0] + ' 00:00:00';
        this.listQuery1.endDate = this.findDate[1] + ' 23:59:59';
      }
      if (this.paymentDate.length>1){
        this.listQuery1.payStartDate = this.paymentDate[0]+' 00:00:00';
        this.listQuery1.payEndDate = this.paymentDate[1]+ ' 23:59:59';
      }
      this.majorEvaluatorSettleProd()
    },

    resetSearch2() {
      this.$router.push({ query: {} });
      this.paymentDate1 = [];
      this.findDate1 = [this.getDefaultStartDate(), this.getDefaultEndDate()];
      this.listQuery2 = {
        current: 1,
        size: 10,
        descs: 'id',
        startDate: null,
        endDate: null,
        payStartDate:null,
        payEndDate:null
      }
      this.majorMarketSettleProd()
    },

    searchList2() {
      // 重置分页
      this.listQuery2.page = 1
      this.listQuery2.size = 10
      if (this.findDate1) {
        this.listQuery2.startDate = this.findDate1[0] + ' 00:00:00';
        this.listQuery2.endDate = this.findDate1[1] + ' 23:59:59';
      }
      if (this.paymentDate1.length>1){
        this.listQuery2.payStartDate = this.paymentDate1[0]+' 00:00:00';
        this.listQuery2.payEndDate = this.paymentDate1[1]+ ' 23:59:59';
      }
      this.majorMarketSettleProd()
    },

    majorEvaluatorSettleProd() {
      if (this.findDate) {
        this.listQuery1.startDate = this.findDate[0] + ' 00:00:00';
        this.listQuery1.endDate = this.findDate[1] + ' 23:59:59';
      }
      if (this.paymentDate.length>1){
          this.listQuery1.payStartDate = this.paymentDate[0]+' 00:00:00';
          this.listQuery1.payEndDate = this.paymentDate[1]+ ' 23:59:59';
        }
      this.$api.financeRealFund.majorEvaluatorSettleProd(this.listQuery1).then(res => {
        if (res.code === 200) {
          this.evaluatorProd = res.data;
        }
      })
    },
    majorMarketSettleProd() {
      if (this.findDate1) {
        this.listQuery2.startDate = this.findDate1[0] + ' 00:00:00';
        this.listQuery2.endDate = this.findDate1[1] + ' 23:59:59';
      }
      if (this.paymentDate1.length>1){
          this.listQuery2.payStartDate = this.paymentDate1[0]+' 00:00:00';
          this.listQuery2.payEndDate = this.paymentDate1[1]+ ' 23:59:59';
        }
      this.$api.financeRealFund.majorMarketSettleProd(this.listQuery2).then(res => {
        if (res.code === 200) {
          this.marketProd = res.data;
        }
      })
    },
    exportMarketSettleProd() {
      this.$utils.exportUtil(
        "/financeRealFund/major/market/settle/export", this.listQuery2,
        "导出"
      );
    },
    exportEvaluateSettleProd() {
      this.$utils.exportUtil(
        "/financeRealFund/major/evaluator/settle/export", this.listQuery1,
        "导出"
      );
    }
  }
}
</script>
<style scoped lang="scss"></style>
