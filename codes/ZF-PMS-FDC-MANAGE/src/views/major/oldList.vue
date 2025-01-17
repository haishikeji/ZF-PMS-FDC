<template>
  <div class="app-container">
    <div class="title-container">
      <breadcrumb id="breadcrumb-container" class="breadcrumb-container" />
    </div>
    <y-page-list-layout :page-list="pageData" :page-para="hisOrderParam" :get-page-list="getPage">
      <template slot="left">
        <el-input v-model="hisOrderParam.keywords" placeholder="项目编号" clearable
          style="margin-left: 20px;width: 270px;float: left;">
        </el-input>
        <el-select v-model="hisOrderParam.sourceType" placeholder="金融类型" style="margin-left: 20px;width: 200px;float: left;" clearable>
          <el-option key="FINANCE" label="金融" value="FINANCE"></el-option>
          <el-option key="NON_FINANCE" label="非金融" value="NON_FINANCE"></el-option>
        </el-select>
        <!-- <el-select v-model="hisOrderParam.nowNodeName" placeholder="处理节点" style="margin-left: 20px;width: 200px;float: left;" clearable
            filterable>
          <el-option
            v-for="item in majorNodes"
            :key="item.code"
            :label="item.name"
            :value="item.name">
          </el-option>
        </el-select> -->
        <el-button class="filter-item" style="margin-left: 10px;float: left;" type="primary" @click="searchList()" round>搜索
        </el-button>
        <el-button class="filter-item" style="float: left;" round type="success" @click="resetSearch()">重置
        </el-button>
      </template>
      <parentTable ref="table" v-loading="listLoading" :data="pageData.records" slot="table" style="width: 100%;" >
        <el-table-column label="金融类型" align="center" width="130">
          <template slot-scope="{row}">
            <span>{{ row.sourceTypeName}}</span>
          </template>
        </el-table-column>
        <el-table-column label="分单类型" align="center">
          <template slot-scope="{row}">
            <span>{{ row.assign?'指派':'轮单' }}</span>
          </template>
        </el-table-column>
        <el-table-column label="项目编号" align="center" width="150">
          <template slot-scope="{row}">
            <span>{{ row.code }}</span>
          </template>
        </el-table-column>
        <el-table-column label="报告号" align="center" width="280" prop="reportNo">
          <template slot-scope="{row}">
            <span>{{ row.reportNO}}</span>
          </template>
        </el-table-column>
        <el-table-column label="项目名称" align="center" width="300" >
          <template slot-scope="{row}">
            <span>{{ row.name }}</span>
          </template>
        </el-table-column>
        <el-table-column label="评估对象类别" align="center" width="130">
          <template slot-scope="{row}">
            <span>{{ row.businessType }}</span>
          </template>
        </el-table-column>
        <el-table-column label="委托人" align="center" width="130">
          <template slot-scope="{row}">
            <span>{{ row.principal }}</span>
          </template>
        </el-table-column>
        <el-table-column label="委托人联系电话" align="center" width="130">
          <template slot-scope="{row}">
            <span>{{ row.principalMobile}}</span>
          </template>
        </el-table-column>
        <el-table-column label="产权人" align="center" width="130">
          <template slot-scope="{row}">
            <span>{{ row.propertyName}}</span>
          </template>
        </el-table-column>
        <el-table-column label="客户名称" align="center" width="130">
          <template slot-scope="{row}">
            <span>{{ row.customerName }}</span>
          </template>
        </el-table-column>
        <el-table-column label="业务来源" align="center" width="130">
          <template slot-scope="{row}">
            <span>{{ row.customerName2 }}</span>
          </template>
        </el-table-column>
        <el-table-column label="当前节点" align="center" width="130">
          <template slot-scope="{row}">
            <span>{{ row.nowNodeName }}</span>
          </template>
        </el-table-column>
        <el-table-column label="待办人" align="center" width="130">
          <template slot-scope="{row}">
            <span>{{ row.nowNodeEmpName }}</span>
          </template>
        </el-table-column>
        <el-table-column label="项目负责人" align="center">
          <template slot-scope="{row}">
            <span>{{ row.managerName }}</span>
          </template>
        </el-table-column>
        <el-table-column label="客户经理" align="center">
          <template slot-scope="{row}">
            <span>{{ row.masterName }}</span>
          </template>
        </el-table-column>
        <el-table-column label="评估总价（万元）" align="center" width="130">
          <template slot-scope="{row}">
            <span>{{ row.money}}</span>
          </template>
        </el-table-column>
        <el-table-column label="应收款（元）" align="center" >
          <template slot-scope="{row}">
            <span>{{ row.needPay}}</span>
          </template>
        </el-table-column>
        <el-table-column label="实收款（元）" align="center" >
          <template slot-scope="{row}">
            <span>{{ row.realPay}}</span>
          </template>
        </el-table-column>
        <el-table-column label="标准收费（元）" align="center" >
          <template slot-scope="{row}">
            <span>{{ row.standardPrice}}</span>
          </template>
        </el-table-column>
        <el-table-column label="打折比例" align="center">
          <template slot-scope="{row}">
            <span>{{ row.evaluateAmount}}</span>
          </template>
        </el-table-column>
        <el-table-column label="下单时间" align="center" width="100">
          <template slot-scope="{row}">
            {{ $utils.timestampToTime(row.orderTime) }}
          </template>
        </el-table-column>
        
        <el-table-column label="操作" align="center" width="120" fixed="right">
            
          <template slot-scope="{row}">
            <!-- <el-button type="text" @click="fetchData(row.id)">同步</el-button> -->
            <el-button type="text" @click="jumpToOldSystem(row.flowInstanceId)">跳转</el-button>
          </template>
        </el-table-column>
      </parentTable>
    </y-page-list-layout>
  </div>
</template>
<script>
import YPageListLayout from '@/components/YPageListLayout'
import Breadcrumb from '@/components/Breadcrumb'
import PermissionButton from '@/components/PermissionButton/PermissionButton'

export default {
  name: 'majorOldList',
  components: {
    Breadcrumb,
    YPageListLayout,
    PermissionButton,
  },

  data() {
    return {
      pageData:{
        records:[]
      },
      listLoading: false,
      hisOrderParam: {
        start: 0,
        orderBy: '-createdDatetime',
        limit: 10,
        current:1,
        size:10,
        deleted: false,
        sourceType:null,
        keywords:null,
        nowNodeName:null,
      },
      majorNodes:[]
    }
  },
  created() {
    this.getPage();
    //this.getNodeEnum();
  },
  methods: {
    getPage() {
      this.$api.historyOrder.major(this.hisOrderParam).then(res=>{
        if (res.code===200){
          this.pageData = res.data;
        }
      }).catch(()=>{
        document.cookie = "JSESSIONID=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;"; 
        console.log("JSESSIONID remove")
      })
    },

    getNodeEnum() {
      this.$api.workNode.nodesByBusiness("MAJOR_BUSINESS").then(res => {
                if (res.code === 200) {
                    this.majorNodes = res.data;
                }
            })
    },

    searchList(){
      // 重置分页
      this.hisOrderParam.current = 1;
      this.hisOrderParam.size = 10;
      this.getPage();
    },

    resetSearch() {
       // 重置分页
       this.hisOrderParam.current = 1;
       this.hisOrderParam.size = 10;
       this.hisOrderParam.sourceType = null;
       this.hisOrderParam.keywords = null;
       this.hisOrderParam.nowNodeName = null;
       this.getPage();
    },

    jumpToOldSystem(instanceId){
        this.getEverCookie();
        window.open("http://noa.scdayou.com/workflow/runtime/flowTask/detail-instance?flowInstanceId="+instanceId,'_blank')
    },

    getEverCookie(){
      this.$api.historyOrder.cookie().then(res=>{
        if (res.code === 200){
            let cookieMap = res.data.split("=");
            document.cookie = cookieMap[0]+"="+cookieMap[1]+"; domain=scdayou.com; path=/;"; 
        }
      })
    },
    fetchData(id){
      if (id){
        this.$api.historyOrder.fetchAboutMajorById(id).then(res=>{
          if (res.code ===200 && res.data){
             this.$message.success("同步成功，请在正在进行列表中查看。")
          }
        })
      }
    }
  },
  
}
</script>
<style lang="scss" scoped>
 
</style>
