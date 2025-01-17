<template>
  <div class="app-container">
    <div class="title-container">
      <breadcrumb id="breadcrumb-container" class="breadcrumb-container"/>
    </div>
    <y-page-list-layout :page-list="pageData" :page-para="listQuery" :get-page-list="getList">
      <template slot="left">
        <el-input
          v-model="listQuery.itemName"
          placeholder="项目名称"
          clearable
          style="margin-left: 20px;width: 320px;float: left;"
        >
        </el-input>
        <el-input
          v-model="listQuery.userName"
          placeholder="人员名称"
          clearable
          style="margin-left: 20px;width: 320px;float: left;"
        >
        </el-input>
        <el-select
          v-model="listQuery.brokerageRule"
          placeholder="提成规则"
          clearable
          filterable
          style="margin-left: 20px;width: 150px;float: left;"
          class="filter-item"
        >
          <el-option label="市场人员规则" value="LAND_MARKETER_RULE"/>
          <el-option label="营销主管规则" value="LAND_SUPERVISOR_RULE"/>
          <el-option label="营销经理规则" value="LAND_MANAGER_RULE"/>
          <el-option label="参与人员规则" value="LAND_OTHER_RULE"/>
        </el-select>
        <el-select
          v-model="listQuery.brokerageMode"
          placeholder="提成方式"
          clearable
          filterable
          style="margin-left: 20px;width: 150px;float: left;"
          class="filter-item"
        >
          <el-option label="个人提成" value="PERSONAL"/>
          <el-option label="团队提成" value="TEAM_SHARE"/>
        </el-select>
        <el-button
          class="filter-item"
          style="margin-left: 10px;float: left;"
          type="primary"
          @click="getList"
          round
        >搜索
        </el-button>
        <el-button
          class="filter-item"
          style="float: left;"
          round
          type="warning"
          @click="resetSearch()"
        >重置
        </el-button>
      </template>
      <template slot="right">
        <PermissionButton
          menu-code="_views_detail_export"
          class-name="filter-item"
          round
          type="primary"
          name="导出"
          @click="detailExport"
        >
        </PermissionButton>
      </template>
      <parentTable
        v-loading="listLoading"
        :data="pageData.records"
        slot="table"
        style="width: 100%;"
      >
        <el-table-column label="项目名称" align="center">
          <template slot-scope="{row}">
            <span>{{ row.itemName }}</span>
          </template>
        </el-table-column>
        <el-table-column label="阶段名称" align="center">
          <template slot-scope="{row}">
            <span>{{ row.stageName===null?'-': row.stageName}}</span>
          </template>
        </el-table-column>
        <el-table-column label="合同金额(元)" align="center" width="110">
          <template slot-scope="{row}">
            <span>{{ row.amount}}</span>
          </template>
        </el-table-column>
        <el-table-column label="人员名称" align="center" width="150">
          <template slot-scope="{row}">
            <span>{{ row.userName}}</span>
          </template>
        </el-table-column>
        <el-table-column label="人员类型" align="center" width="150">
          <template slot-scope="{row}">
            <span>{{ row.userType}}</span>
          </template>
        </el-table-column>
        <el-table-column label="预提金额(元)" align="center" width="150">
          <template slot-scope="{row}">
            <span>{{ row.aheadAmount}}</span>
          </template>
        </el-table-column>
        <el-table-column label="预计提成金额(元)" align="center" width="160">
          <template slot-scope="{row}">
            <span>{{ row.predictAmount}}</span>
          </template>
        </el-table-column>
        <el-table-column label="实际提成金额(元)" align="center" width="150">
          <template slot-scope="{row}">
            <span>{{row.actualAmount}}</span>
          </template>
        </el-table-column>
        <el-table-column label="已结算金额(元)" align="center" width="120">
          <template slot-scope="{row}">
            <span>{{ row.advanceAmount}}</span>
          </template>
        </el-table-column>
        <el-table-column label="提成规则" align="center" width="150">
          <template slot-scope="{row}">
            <span>{{ brokerageRule(row.brokerageRule)}}</span>
          </template>
        </el-table-column>
        <el-table-column label="提成方式" align="center" width="150">
          <template slot-scope="{row}">
            <span>{{ brokerageMode(row.brokerageMode)}}</span>
          </template>
        </el-table-column>
<!--        <el-table-column label="操作" align="center" width="100"  fixed="right">-->
<!--          <template slot-scope="{row}">-->
<!--            <el-button-->
<!--              class-name="filter-item"-->
<!--              type="primary"-->
<!--              :page-jump="true"-->
<!--              :page-query="{id: row.id}"-->
<!--              round-->
<!--            >提成明细</el-button>-->
<!--          </template>-->
<!--        </el-table-column>-->
      </parentTable>
    </y-page-list-layout>
  </div>
</template>
<script>
  import YPageListLayout from '@/components/YPageListLayout'
  import Breadcrumb from '@/components/Breadcrumb'
  import PermissionButton from '@/components/PermissionButton/PermissionButton'
  import { fileDown } from '../../utils/file'

  export default {
    name: 'ViewsBrokerageDetail',
    components: {
      Breadcrumb,
      YPageListLayout,
    },
    filters: {
      statusFilter(status) {
        const statusMap = {
          published: 'success',
          draft: 'info',
          deleted: 'danger',
        }
        return statusMap[status]
      },
    },
    data() {
      return {
        isDisable:false,
        tableKey: 0,
        pageData: { records: [] },
        total: 20,
        listLoading: true,
        listQuery: {
          page: 1,
          size: 10,
          descs: 'id',
          generalId:null
        },
        listQueryKey: 'keyword',
      }
    },
    created() {
      if (this.$route.query.generalId) {
        this.listQuery.generalId = this.$route.query.generalId;
      }
      this.getList()
    },
    methods: {
      detailExport(){
        this.$utils.exportUtil(
          "/itemBrokerageDetail/export", this.listQuery,
          "导出"
        );
      },
      resetSearch() {
        this.listQuery = {
          current: 1,
          size: 10,
          descs: 'id',
        };
        this.getList()
      },
      brokerageRule(val){
        if (val === 'LAND_OTHER_RULE'){
          return '参与人员规则';
        }
        if (val === 'LAND_MARKETER_RULE'){
          return '市场人员规则';
        }
        if (val === 'LAND_SUPERVISOR_RULE'){
          return '营销主管规则';
        }
        if (val === 'LAND_MANAGER_RULE'){
          return '营销经理规则';
        }
      },
      brokerageMode(val){
        if (val === 'PERSONAL'){
          return '个人提成';
        }
        if (val === 'TEAM_SHARE'){
          return '团队提成';
        }
      },
      getList() {
        const that = this;
        this.listLoading = true;
        this.$api.itemBrokerageDetail.list(Object.assign({}, that.listQuery)).then((res) => {
          that.pageData = res.data;
          setTimeout(() => {
            that.listLoading = false
          }, 200)
        })
          .catch(() => {
            that.listLoading = false
          })
      },
    }
  }
</script>
<style lang="scss" scoped>
  .right {
    flex: 1;

    .title {
      font-size: 16px;
      font-weight: 500;
      color: rgba(51, 51, 51, 1);
      line-height: 35px;
      margin-bottom: 8px;
    }

    .menu-2-box {
      display: flex;
      flex-wrap: wrap;
      width: 100%;
    }

    .menu-2-item {
      display: flex;
      align-items: center;
      color: #656565;
      font-size: 12px;
      width: 230px;
      height: 101px;
      background: rgb(255, 185, 129);
      border-radius: 3px;
      padding-left: 20px;
      margin-right: 10px;
      margin-bottom: 10px;
      cursor: pointer;
      box-shadow: 0 2px 4px rgba(0, 0, 0, 0.12), 0 0 6px rgba(0, 0, 0, 0.04);

      .text {
        margin-left: 16px;
      }
    }
  }
</style>
