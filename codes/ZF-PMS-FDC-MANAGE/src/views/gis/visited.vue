<template>
    <div class="app-container">
      <div class="title-container">
        <breadcrumb id="breadcrumb-container" class="breadcrumb-container"/>
      </div>
  
      <y-page-list-layout :page-list="pageData" :page-para="listQuery" :get-page-list="getList">
        <template slot="left">
          <el-input
            v-model="listQuery.remoteAddr"
            placeholder="远程地址"
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
          <el-table-column label="访问IP地址" align="center" >
            <template slot-scope="{row}">
              <span>{{ row.remoteIp }}</span>
            </template>
          </el-table-column>
          <el-table-column label="远程主机" align="center" >
            <template slot-scope="{row}">
              <span>{{ row.remoteHost }}</span>
            </template>
          </el-table-column>
          <el-table-column label="远程地址" align="center" >
            <template slot-scope="{row}">
              <span>{{ row.remoteAddr }}</span>
            </template>
          </el-table-column>
          <el-table-column label="访问时间" align="center">
            <template slot-scope="{row}">
              <span>{{ row.created}}</span>
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
      name: 'ViewsVisitedList',
      components: {
        Breadcrumb,
        YPageListLayout,
      },
      data() {
        return {
          pageData: { records: [] },
          total: 10,
          listLoading: true,
          listQuery: {
            page: 1,
            size: 50,
            descs: 'id',
          },
        }
      },
      created() {
        const that = this;
        if (that.$route.query.current && !isNaN(that.$route.query.current)) {
          that.listQuery.current = parseInt(that.$route.query.current);
        }
        that.getList()
      },
      methods: {
        resetSearch() {
          this.$router.push({ query: {} });
          this.listQuery = {
            current: 1,
            size: 50,
            descs: 'id',
          }
          this.getList()
        },
        searchList() {
          // 重置分页
          this.listQuery.page = 1
          this.listQuery.size = 50
          this.getList()
        },
        getList() {
          const that = this
          this.listLoading = true
          // console.log(that.listQuery)
          const key = {}
          key[this.listQueryKey] = this.listQuery.description
          this.$api.gisVisited
            .list(Object.assign({}, that.listQuery, key))
            .then((res) => {
              that.pageData = res.data
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
  