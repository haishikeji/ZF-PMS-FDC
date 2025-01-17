<template>
  <div class="app-container">
    <div class="title-container">
      <breadcrumb id="breadcrumb-container" class="breadcrumb-container" />
    </div>
    <y-page-list-layout :page-list="pageData" :page-para="listQuery" :get-page-list="getList">
      <template slot="left">
        <el-input v-model="listQuery.keyword" placeholder="坐落/项目编号/报告号" clearable
          style="margin-left: 20px;width: 270px;float: left;">
        </el-input>
        <el-select v-model="listQuery.nodeName" placeholder="处理节点" style="margin-left: 20px;width: 200px;float: left;" clearable
            filterable>
          <el-option
            v-for="item in persoanlNodes"
            :key="item.code"
            :label="item.name"
            :value="item.name">
          </el-option>
        </el-select>
        <el-select v-model="listQuery.state" placeholder="节点状态" style="margin-left: 20px;width: 200px;float: left;" clearable
            filterable>
          <el-option key="PASS" label="提交" value="PASS"></el-option>
          <el-option key="TERMINATE" label="终止" value="TERMINATE"></el-option>
          <el-option key="REVERSE" label="退回" value="REVERSE"></el-option>
          <el-option key="SKIP" label="跳过" value="SKIP"></el-option>
          <el-option key="RESTART" label="重置" value="RESTART"></el-option>
        </el-select>
        <el-date-picker
          style="margin-left: 20px;float: left;"
          v-model="finishTimeBucket"
          type="daterange"
          align="center"
          unlink-panels
          range-separator="至"
          start-placeholder="处理日期（开始）"
          end-placeholder="处理日期（结束）"
          :picker-options="pickerOptions" value-format="yyyy-MM-dd">
        </el-date-picker>
        <el-button class="filter-item" style="margin-left: 10px;float: left;" type="primary" @click="searchList" round>搜索
        </el-button>
        <el-button class="filter-item" style="float: left;" round type="success" @click="resetSearch()">重置
        </el-button>
      </template>
      <parentTable  :data="pageData.records" slot="table" style="width: 100%;">
        <!-- <el-table-column label="业务类型" align="center">
          <template slot-scope="{row}">
            <span>个贷业务</span>
          </template>
        </el-table-column> -->
        <el-table-column label="坐落" align="center" width="300">
          <template slot-scope="{row}">
            <span>{{ row.location }}</span>
          </template>
        </el-table-column>
        <el-table-column label="项目编号" align="center"  width='150'>
          <template slot-scope="{row}">
            <span>{{ row.orderId }}</span>
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
        <el-table-column label="委托人" align="center">
          <template slot-scope="{row}">
            <span>{{ row.bailorA}} {{ row.bailorB}}</span>
          </template>
        </el-table-column>
        <el-table-column label="处理人" align="center">
          <template slot-scope="{row}">
            <span>{{ row.handler }}</span>
          </template>
        </el-table-column>
        <el-table-column label="处理节点" align="center"  width='130'>
          <template slot-scope="{row}">
            <span>{{ row.nodeName }}</span>
          </template>
        </el-table-column>
        <el-table-column label="处理时间" align="center" width='150'>
          <template slot-scope="{row}">
            <span>{{ row.finishTime }}</span>
          </template>
        </el-table-column>
        <el-table-column label="处理时长" align="center">
          <template slot-scope="{row}">
            <span>{{ row.handingTime }}</span>
          </template>
        </el-table-column>
        <el-table-column label="节点状态" align="center">
          <template slot-scope="{row}">
            <span>{{ row.state }}</span>
          </template>
        </el-table-column>
        <el-table-column label="处理意见" align="center"  width='130' show-overflow-tooltip>
          <template slot-scope="{row}">
            <span>{{ row.comments==null?'-':row.comments}}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center"  width='100'>
          <template slot-scope="{row}">
            <el-button v-if="row.instanceState==='CLOSED'" type="text" size="small" @click="activate(row)">激活</el-button>
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
  name: 'persoanlDoneList',
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
        size: 10,
        descs: 'id',
        startDate:null,
        endDate:null,
        keyword:null
      },
      finishTimeBucket:'',
      persoanlNodes:[],
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
    this.getPersonalNodes();
  },
  methods: {
  
    resetSearch() {
      this.$router.push({ query: {} });
      this.finishTimeBucket = '';
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
      if (this.finishTimeBucket){
        this.listQuery.startDate = this.finishTimeBucket[0]+' 00:00:00';
        this.listQuery.endDate = this.finishTimeBucket[1]+ ' 23:59:59';
      }
      this.getList()
    },
    getList() {
      this.$api.workNodeTaskRecord.persoanlTaskDoneList(Object.assign({}, this.listQuery)).then(res=>{
        if (res.code ===200){
            this.pageData = res.data;
        }
      })
    },
  
    getPersonalNodes(){
        this.$api.workNode.nodesByBusiness('PERSONAL_BUSINESS').then(res=>{
          if (res.code === 200){
              this.persoanlNodes = res.data
          }
        })
    },
    activate(row){
      this.$confirm('确认激活:['+row.majorName+']订单?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
        center: true
      }).then(()=>{
          this.$api.workNodeInstance.activate(row.instanceId).then(res=>{
            if (res.code === 200 && res.data){
              this.$notify({
                title: '成功',
                message: '订单激活成功，可在我的待办中查看。',
                type: 'success',
                duration: 2000
              });
              this.getList();
            }else {
              this.$notify({
                title: '失败',
                message: '订单激活失败，请联系系统管理员。',
                type: 'error',
                duration: 2000
              });
            }
          })
      })
    }
  },
  
}
</script>
<style lang="scss" scoped>

</style>
