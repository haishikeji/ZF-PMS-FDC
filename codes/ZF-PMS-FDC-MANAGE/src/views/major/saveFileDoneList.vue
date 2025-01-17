<template>
  <div class="app-container">
    <div class="title-container">
      <breadcrumb id="breadcrumb-container" class="breadcrumb-container" />
    </div>
    <y-page-list-layout :page-list="pageData" :page-para="listQuery" :get-page-list="getList">
      <template slot="left">
        <el-input v-model="listQuery.keyword" placeholder="关键字搜索..." clearable
          style="margin-left: 20px;width: 500px;float: left;">
        </el-input>
        <el-select style="margin-left: 20px;width: 170px;float: left;" v-model="listQuery.production" clearable placeholder="产品类型">
          <el-option value="STATEMENT" label="价值意见书">价值意见书</el-option>
          <el-option value="REPORT" label="报告">报告</el-option>
          <el-option value="LETTER" label="复评函">复评函</el-option>
        </el-select>
        <el-date-picker
          style="margin-left: 20px;float: left;"
          v-model="saveFileDate"
          type="daterange"
          align="center"
          unlink-panels
          range-separator="至"
          start-placeholder="归档日期（开始）"
          end-placeholder="归档日期（结束）"
          :picker-options="pickerOptions" value-format="yyyy-MM-dd">
        </el-date-picker>
        <el-button class="filter-item" style="margin-left: 10px;float: left;" type="primary" @click="searchList" round>搜索
        </el-button>
        <el-button class="filter-item" style="float: left;" round type="success" @click="resetSearch()">重置
        </el-button>
      </template>
      <parentTable  :data="pageData.records" slot="table" style="width: 100%;">
        <el-table-column label="订单名称" align="center" width="300" show-overflow-tooltip >
          <template slot-scope="{row}">
            <span>{{ row.orderName }}</span>
          </template>
        </el-table-column>
        <el-table-column label="订单号" align="center" width="200" show-overflow-tooltip>
          <template slot-scope="{row}">
            <span>{{ row.orderId }}</span>
          </template>
        </el-table-column>
        <el-table-column label="报告号" align="center" width="200">
          <template slot-scope="{row}">
            <span>{{ row.reportNo==null?'-':row.reportNo}}</span>
          </template>
        </el-table-column>
        <el-table-column label="项目名称" align="center" width="300" show-overflow-tooltip>
          <template slot-scope="{row}">
            <span>{{ row.name}}</span>
          </template>
        </el-table-column>
        <el-table-column label="产品类型" align="center" width="130">
          <template slot-scope="{row}">
            <span>{{ aliasProductionType(row.production)}}</span>
          </template>
        </el-table-column>
        <el-table-column label="库存状态" align="center" width="130">
          <template slot-scope="{row}">
            <span>{{ row.repertoryState==null?'未入库':(row.repertoryState?'已出库':'未出库')}}</span>
          </template>
        </el-table-column>
        <el-table-column label="实收款(￥)" align="center" width=100>
          <template slot-scope="{row}">
            <span>{{  row.realAmount==null?'0':row.realAmount}}</span>
          </template>
        </el-table-column>
        <el-table-column label="客户经理" align="center" >
          <template slot-scope="{row}">
            <span>{{ row.clientManager }}</span>
          </template>
        </el-table-column>
        <el-table-column label="负责人" align="center" >
          <template slot-scope="{row}">
            <span>{{ row.principal }}</span>
          </template>
        </el-table-column>
        <el-table-column label="归档日期" align="center" width="160">
          <template slot-scope="{row}">
            <span>{{ row.saveFileDate }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" width="130" fixed="right">
          <template slot-scope="{row}">
            <el-button type="text" @click="toDetail(row)">产品详情</el-button>
          </template>
        </el-table-column>
      </parentTable>
    </y-page-list-layout>
  </div>
</template>
<script>
import YPageListLayout from '@/components/YPageListLayout'
import Breadcrumb from '@/components/Breadcrumb'

export default {
  name: 'saveFileDoneList',
  components: {
    Breadcrumb,
    YPageListLayout,
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
        keyword:null
      },
      saveFileDate:'',
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
    this.listQuery.keyword = this.$route.query.keyword;
    this.getList();
  },
  methods: {
  
    resetSearch() {
      this.$router.push({ query: {} });
      this.saveFileDate='';
      this.listQuery = {
        current: 1,
        size: 20,
        descs: 'id',
      }
      this.getList()
    },
   
    searchList() {
      // 重置分页
      this.listQuery.page = 1
      this.listQuery.size = 20
      if (this.saveFileDate){
        this.listQuery.startDate = this.saveFileDate[0]+' 00:00:00';
        this.listQuery.endDate = this.saveFileDate[1]+ ' 23:59:59';
      }
      this.getList()
    },
    getList() {
      this.$api.majorProduction.saveDone(this.listQuery).then(res=>{
        if (res.code ===200){
            this.pageData = res.data;
        }
      })
    },
   
    toDetail(row){
      this.$router.push(`/major/detail?id=${row.majorId}&couldEdit=${false}&reportNo=${row.reportNo}&back=${'/major/saveFileDone'}`)
    },
    aliasProductionType(code){
        if (code === 'STATEMENT'){
            return '价值意见书';
        }
        if (code === 'LETTER'){
            return '复评函';
        }
        return '报告';
    }
  },
  
}
</script>
<style lang="scss" scoped>

</style>
