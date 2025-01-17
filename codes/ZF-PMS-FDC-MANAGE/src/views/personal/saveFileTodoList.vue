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
        <el-button class="filter-item" style="margin-left: 10px;float: left;" type="primary" @click="searchList" round>搜索
        </el-button>
        <el-button class="filter-item" style="float: left;" round type="success" @click="resetSearch()">重置
        </el-button>
        <el-tooltip class="item" effect="light" content="请先勾选需归档的项目" placement="top-start">
          <PermissionButton menu-code="_views_personal_batch_save_file" class-name="filter-item" type="danger" icon="el-icon-paperclip"
            :page-jump="false" round style="float: left" @click="saveFileDialog = true"  :disabled="(!saveFileOrder.length>0)" />
        </el-tooltip>
      </template>
      <parentTable ref="table" v-loading="listLoading" :data="pageData.records" slot="table" style="width: 100%;" :selectionChange="selectionOrderChange" >
        <el-table-column
          align="center"
          type="selection"
          saveFileOrder
          width="100" border="true">
        </el-table-column>
        <el-table-column label="坐落" align="center" width="300">
          <template slot-scope="{row}">
            <span>{{ row.location }}</span>
          </template>
        </el-table-column>
        <el-table-column label="项目编号" align="center" width="140" >
          <template slot-scope="{row}">
            <span>{{ row.orderId }}</span>
          </template>
        </el-table-column>
        <el-table-column label="面积" align="center" >
          <template slot-scope="{row}">
            <span>{{ row.acreage }}</span>
          </template>
        </el-table-column>
        <el-table-column label="单价" align="center" >
          <template slot-scope="{row}">
            <span>{{ row.price }}</span>
          </template>
        </el-table-column>
        <el-table-column label="总价" align="center" >
          <template slot-scope="{row}">
            <span>{{ row.amount }}</span>
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
        <el-table-column label="客户经理" align="center">
          <template slot-scope="{row}">
            <span>{{ row.clientManager }}</span>
          </template>
        </el-table-column>
        <el-table-column label="实勘人" align="center">
          <template slot-scope="{row}">
            <span>{{ row.outwardStaffName }}</span>
          </template>
        </el-table-column>
        <el-table-column label="内业人员" align="center">
          <template slot-scope="{row}">
            <span>{{ row.inwardStaffName }}</span>
          </template>
        </el-table-column>
        <el-table-column label="定价人员" align="center">
          <template slot-scope="{row}">
            <span>{{ row.pricingStaffName }}</span>
          </template>
        </el-table-column>
        <el-table-column label="实收款(元)" align="center">
          <template slot-scope="{row}">
            <span style="color:red;font-weight:bold">{{ row.realAmount }}</span>
          </template>
        </el-table-column>
        <el-table-column label="下单时间" align="center" width="100" prop="created" sortable>
          <template slot-scope="{row}">
            {{ row.created }}
          </template>
        </el-table-column>
      </parentTable>
    </y-page-list-layout>
    <el-dialog :visible.sync="saveFileDialog" width="55%" center  custom-class="doWarehouseClass">
        <el-result icon="warning" subTitle="请确认将以下项目进行归档操作？"></el-result>
        <div style="width:100%;text-align:center;">
            <el-tag v-for="(p,id) in saveFileOrder"   style="font-size:18px;margin-right: 10px;" type="success" effect="plain" :key="p.id"> 
              {{p.orderId}}</el-tag>
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
  name: 'PersonalTodoSaveFile',
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
        size: 10,
        descs: 'id',
      },
      saveFileOrder:[],
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
        size: 10,
        descs: 'id',
      }
      this.getList()
    },
   
    searchList() {
      // 重置分页
      this.listQuery.page = 1
      this.listQuery.size = 10
      this.getList()
    },
    getList() {
      this.$api.personal.todoSaveFile(this.listQuery).then(res=>{
        if (res.code ===200){
            this.pageData = res.data;
        }
      })
    },
    
    selectionOrderChange(val){
      let pickeds = [];
        for (let i in val){
          let picked = new Object();
          picked.taskId = val[i].taskId;
          picked.currentNodeId = val[i].currentNodeId;
          picked.businessId = val[i].id;
          picked.state = "PASS";
          picked.orderId = val[i].orderId;
          picked.taskCreated = val[i].taskCreated;
          pickeds.push(picked);
        }
        this.saveFileOrder = pickeds;

    },
    batchSaveFile(){
      if (this.saveFileOrder.length>0){
          this.$api.personal.batchSaveFile(this.saveFileOrder).then(res=>{
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
