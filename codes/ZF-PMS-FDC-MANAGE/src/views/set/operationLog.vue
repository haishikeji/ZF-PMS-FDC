<template>
  <div class="app-container">
    <div class="title-container">
      <breadcrumb id="breadcrumb-container" class="breadcrumb-container"/>
    </div>

    <y-page-list-layout :page-list="pageData" :page-para="listQuery" :get-page-list="getList">
      <template slot="left">
        <el-select
          v-model="listQuery.operationType"
          placeholder="业务类型"
          clearable
          filterable
          style="margin-left: 20px;width: 100px;float: left;"
          class="filter-item"
        >
          <el-option label="登录" value="登录"/>
          <el-option label="新增" value="新增"/>
          <el-option label="编辑" value="编辑"/>
          <el-option label="删除" value="删除"/>
          <el-option label="导出" value="导出"/>
          <el-option label="导入" value="导入"/>
          <el-option label="保存" value="保存"/>
          <el-option label="设置" value="设置"/>
          <el-option label="重置" value="重置"/>
          <el-option label="生成" value="生成"/>
          <el-option label="操作" value="操作"/>
          <el-option label="审核" value="审核"/>
        </el-select>
        <el-input
          v-model="listQuery.userName"
          placeholder="员工姓名"
          clearable
          style="margin-left: 20px;width: 320px;float: left;"
        >
        </el-input>
        <el-button
          class="filter-item"
          style="margin-left: 10px;float: left;"
          type="primary"
          @click="searchList"
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
      <parentTable
        v-loading="listLoading"
        :data="pageData.records"
        slot="table"
        style="width: 100%;"
      >
        <el-table-column label="员工姓名" align="center" width="130" >
          <template slot-scope="{row}">
            <span>{{ row.userName }}</span>
          </template>
        </el-table-column>
        <el-table-column label="功能名称" align="center" width="130">
          <template slot-scope="{row}">
            <span>{{ row.title }}</span>
          </template>
        </el-table-column>
        <el-table-column label="业务类型" align="center" width="130">
          <template slot-scope="{row}">
            <span>{{ row.operationType }}</span>
          </template>
        </el-table-column>
        <el-table-column label="请求URL" align="center" width="200">
          <template slot-scope="{row}">
            <span>{{ row.operationUrl}}</span>
          </template>
        </el-table-column>
        <el-table-column label="请求参数" align="center">
          <template slot-scope="{row}">
            <el-tag>{{ row.operationParam}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="客户端IP" align="center" width="130">
          <template slot-scope="{row}">
            <span>{{ row.ip}}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作时间" align="center" width="180">
          <template slot-scope="{row}">
            <span>{{ row.operationTime }}</span>
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
    name: 'operationLogList',
    components: {
      Breadcrumb,
      YPageListLayout,
    },

    data() {
      return {
        tableKey: 0,
        pageData: { records: [] },
        total: 20,
        listLoading: true,
        listQuery: {
          page: 1,
          size: 10,
          descs: 'id',
        },
        listQueryKey: 'keyword',
      }
    },
    created() {
      const that = this;
      that.getList()
    },
    methods: {
      resetSearch() {
        this.listQuery = {
          current: 1,
          size: 10,
          descs: 'id',
          me: false
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
        const that = this
        this.listLoading = true
        const key = {}
        this.$api.operationLog.list(Object.assign({}, that.listQuery, key)).then((res) => {
            that.pageData = res.data;
            setTimeout(() => {
              that.listLoading = false
            }, 200)
          })
          .catch(() => {
            that.listLoading = false
          })
      },
    },
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
