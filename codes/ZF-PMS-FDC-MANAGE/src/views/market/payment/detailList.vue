<template>
  <div class="app-container">
    <div class="title-container">
      <breadcrumb id="breadcrumb-container" class="breadcrumb-container"/>
    </div>

    <y-page-list-layout :page-list="pageData" :page-para="listQuery" :get-page-list="getList">
      <template slot="left">
        <el-input
          v-model="listQuery.name"
          placeholder="回款名称"
          clearable
          style="margin-left: 20px;width: 320px;float: left;">
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
        <el-table-column label="项目名称" align="center" width="310">
          <template slot-scope="{row}">
            <span>{{ row.itemName }}</span>
          </template>
        </el-table-column>
        <el-table-column label="回款名称" align="center" width="310">
          <template slot-scope="{row}">
            <span>{{ row.name }}</span>
          </template>
        </el-table-column>
        <el-table-column label="回款金额" align="center" width="310">
          <template slot-scope="{row}">
            <span>{{ row.amount }}</span>
          </template>
        </el-table-column>
        <el-table-column label="回款日期" align="center" width="310">
          <template slot-scope="{row}">
            <span>{{ row.paymentDate }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" >
          <template slot-scope="{row}">
            <el-button
              class="filter-item download-button"
              style="margin-left: 10px;"
              icon="el-icon-delete-solid"
              type="danger"
              @click="deleteInfo(row.id)"
            >
              删除
            </el-button>
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
    name: 'ViewsMarketPaymentDetailList',
    components: {
      Breadcrumb,
      YPageListLayout,
      PermissionButton,
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
        tableKey: 0,
        pageData: { records: [] },
        total: 20,
        listLoading: true,
        listQuery: {
          page: 1,
          size: 10,
          descs: 'id',
          itemId : this.$route.query.id
        },
        listQueryKey: 'keyword'
      }
    },
    created() {
      if (this.$route.query.year) {
        this.listQuery.year = this.$route.query.year;
      }
      if (this.$route.query.month) {
        this.listQuery.month = this.$route.query.month;
      }
      if (this.$route.query.isCurYear) {
        this.listQuery.isCurYear = this.$route.query.isCurYear;
      }
      if (this.$route.query.isCurMonth) {
        this.listQuery.isCurMonth = this.$route.query.isCurMonth;
      }
      this.getList();
    },
    methods: {
      resetSearch() {
        this.listQuery = {
          current: 1,
          size: 10,
          descs: 'id',
          itemId: this.$route.query.id
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
        const that = this;
        this.listLoading = true;
        // console.log(that.listQuery)
        const key = {}
        key[this.listQueryKey] = this.listQuery.description;
        this.$api.payment
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
      deleteInfo(id) {
        const that = this
        that.$confirm('请确认是否删除该数据?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
          center: true
        }).then(() => {
          that.$api.payment.delete(id).then(data => {
            that.loading = false
            if (data.code === 200) {
              that.getList()
            } else {
              this.$message({
                type: 'error',
                message: data.msg
              })
            }
          })
        }).catch(() => {
        })
      }
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
