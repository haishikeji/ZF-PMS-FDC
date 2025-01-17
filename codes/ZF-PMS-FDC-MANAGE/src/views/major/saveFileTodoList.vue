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
        <el-button class="filter-item" style="margin-left: 10px;float: left;" type="primary" @click="searchList" round>搜索
        </el-button>
        <el-button class="filter-item" style="float: left;" round type="success" @click="resetSearch()">重置
        </el-button>
        <el-tooltip class="item" effect="light" content="请先勾选需归档的报告" placement="top-start">
          <PermissionButton menu-code="_views_major_batch_save_file" class-name="filter-item" type="danger" icon="el-icon-paperclip"
            :page-jump="false" round style="float: left" @click="saveFileDialog = true"  :disabled="(!saveFileProduction.length>0)" />
        </el-tooltip>
      </template>
      <parentTable v-loading="listLoading" :data="pageData.records" slot="table" style="width: 100%;" :selectionChange="selectionProductionChange">
        <el-table-column
          align="center"
          type="selection"
          saveFileProduction
          width="100" border="true">
        </el-table-column>
        <!-- <el-table-column label="订单名称" align="center" width="200" show-overflow-tooltip >
          <template slot-scope="{row}">
            <span>{{ row.orderName }}</span>
          </template>
        </el-table-column> -->
        <el-table-column label="订单号" align="center"  width="200" >
          <template slot-scope="{row}">
            <span>{{ row.orderId }}</span>
          </template>
        </el-table-column>
        <el-table-column label="项目名称" align="center"  width="300" show-overflow-tooltip>
          <template slot-scope="{row}">
            <span>{{ row.name }}</span>
          </template>
        </el-table-column>
        <el-table-column label="产品号" align="center" width="280">
          <template slot-scope="{row}">
            <span>{{ row.reportNo }}</span>
          </template>
        </el-table-column>
        <el-table-column label="产品类型" align="center" width="100">
          <template slot-scope="{row}">
            <span>{{ aliasProductionType(row.production)}}</span>
          </template>
        </el-table-column>
        <el-table-column label="库存状态" align="center">
          <template slot-scope="{row}">
            <span>{{ row.repertoryState===null?'未入库':(row.repertoryState?'已出库':'已入库') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="是否归档" align="center">
          <template slot-scope="{row}">
            <span>{{ row.ifSaveFile?'是':'否' }}</span>
          </template>
        </el-table-column>
        <el-table-column label="实收款(￥)" align="center" width=100>
          <template slot-scope="{row}">
            <span>{{  row.realAmount==null?'0':row.realAmount}}</span>
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
        <el-table-column label="接单部门" align="center" >
          <template slot-scope="{row}">
            <span>{{ row.departmentName }}</span>
          </template>
        </el-table-column>
        <el-table-column label="下单日期" align="center" width=150>
          <template slot-scope="{row}">
            {{ row.created }}
          </template>
        </el-table-column>
      </parentTable>
    </y-page-list-layout>
    <el-dialog :visible.sync="saveFileDialog" width="55%" center  custom-class="doWarehouseClass">
        <el-result icon="warning" subTitle="请确认将以下报告进行归档操作？"></el-result>
        <div style="width:100%;text-align:center;">
            <el-tag v-for="(p,productionId) in saveFileProduction"   style="font-size:18px;margin-right: 10px;" type="success" effect="plain" :key="p.reportNo"> 
              {{p.reportNo}}</el-tag>
        </div>
        <div style="width:100%;text-align:center;margin-top:20px">
          <el-button type="success" @click="batchSaveFile()">确认归档</el-button>
        </div>
    </el-dialog>
  </div>
</template>
<script>
import YPageListLayout from '@/components/YPageListLayout'
import Breadcrumb from '@/components/Breadcrumb'
import PermissionButton from '@/components/PermissionButton/PermissionButton'

export default {
  name: 'MajorList',
  components: {
    Breadcrumb,
    YPageListLayout,
    PermissionButton,
  },
  filters: {
    
  },
  data() {
    return {
      pageData: { records: [] },
      listLoading: false,
      listQuery: {
        page: 1,
        size: 20,
        descs: 'id',
      },
      saveFileProduction:[],
      saveFileDialog:false
    }
  },
  created() {
    this.getList();
  },
  methods: {
  
    resetSearch() {
      this.$router.push({ query: {} });
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
      this.getList()
    },
    getList() {
      this.$api.majorProduction.saveFileTodo(this.listQuery).then(res=>{
        if (res.code ===200){
            this.pageData = res.data;
        }
      })
    },
    
    aliasProductionType(code){
        if (code === 'STATEMENT'){
            return '价值意见书';
        }
        if (code === 'LETTER'){
            return '复评函';
        }
        return '报告';
    },
    selectionProductionChange(val){
      let pickeds = [];
        for (let i in val){
          let picked = new Object();
          picked.taskId = val[i].taskId;
          picked.currentNodeId = val[i].currentNodeId;
          picked.businessId = val[i].businessId;
          picked.reportNo = val[i].reportNo;
          picked.state = "PASS";
          picked.productionId = val[i].productionId;
          picked.orderId = val[i].orderId;
          picked.name = val[i].name;
          picked.taskCreated = val[i].taskCreated;
          picked.businessSubId = val[i].businessSubId;
          picked.businessMinId = val[i].businessMinId;
          pickeds.push(picked);
        }
        this.saveFileProduction = pickeds;

    },
    batchSaveFile(){
      if (this.saveFileProduction.length>0){
          this.$api.majorProduction.batchSaveFile(this.saveFileProduction).then(res=>{
            if (res.code ===200 && res.data){
              this.$notify({
                title: '成功',
                message: '归档成功',
                type: 'success',
                duration: 2000
              });
              this.saveFileDialog = false;
              this.getList();
            }else {
              this.$notify({
                title: '失败',
                message: '归档失败,请联系系统管理员。',
                type: 'error',
                duration: 2000
              });
            }
          })
      }
    }

  },
  
}
</script>
<style lang="scss" scoped>
      /deep/.doWarehouseClass {
      border-radius: 10px;
    }
</style>
