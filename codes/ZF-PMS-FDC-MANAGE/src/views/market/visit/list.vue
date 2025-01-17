<template>
  <div class="app-container">
    <div class="title-container">
      <breadcrumb id="breadcrumb-container" class="breadcrumb-container"/>
    </div>

    <y-page-list-layout :page-list="pageData" :page-para="listQuery" :get-page-list="getList">
      <template slot="left">
        <PermissionButton
          menu-code="_views_market_visit_add"
          class-name="filter-item"
          type="primary"
          name
          size="mini"
          :page-jump="true"
          :page-query="{id: null}"
          round
          style="float: left"
        />
        <el-input
          v-model="listQuery.keyword"
          placeholder="名字/等级/单位/科室/职位"
          clearable
          style="margin-left: 20px;width: 320px;float: left;">
        </el-input>
        <el-input
          v-model="listQuery.userName"
          placeholder="客户经理"
          clearable
          style="margin-left: 20px;width: 320px;float: left;">
        </el-input>
        <el-select clearable style="margin-left: 20px;width: 200px;float: left;" v-model="listQuery.visitType" placeholder="拜访类型">
          <el-option key="0" label="初访" value="初访"/>
          <el-option key="1" label="回访" value="回访"/>
        </el-select>
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
      <template slot="right">
        <PermissionButton menu-code="_views_visit_export" class-name="filter-item" round type="primary" name="导出"
          @click="visitExport">
        </PermissionButton>
      </template>
      <parentTable
        v-loading="listLoading"
        :data="pageData.records"
        slot="table"
        style="width: 100%;"
      >
        <el-table-column label="客户名字" align="center" width="120">
          <template slot-scope="{row}">
            <span>{{ row.customerName }}</span>
          </template>
        </el-table-column>
        <el-table-column label="客户等级" align="center" width="120">
          <template slot-scope="{row}">
            <span v-if=" row.customerLevel==='重点客户' " style="color: red">{{row.customerLevel}}</span>
            <span v-if=" row.customerLevel==='普通客户' " style="color: sandybrown">{{row.customerLevel}}</span>
            <span v-if=" row.customerLevel==='一般客户' " style="color: green">{{row.customerLevel}}</span>
          </template>
        </el-table-column>
        <el-table-column label="客户单位" align="center" width="120">
          <template slot-scope="{row}">
            <span>{{ row.customerDepartment }}</span>
          </template>
        </el-table-column>
        <el-table-column label="客户科室" align="center" width="120">
          <template slot-scope="{row}">
            <span>{{ row.customerSection }}</span>
          </template>
        </el-table-column>
        <el-table-column label="客户职位" align="center" width="120">
          <template slot-scope="{row}">
            <span>{{ row.customerPosition }}</span>
          </template>
        </el-table-column>
        <el-table-column label="客户经理" align="center" width="120">
          <template slot-scope="{row}">
            <span>{{ row.userName }}</span>
          </template>
        </el-table-column>
        <el-table-column label="拜访类型" align="center" width="120">
          <template slot-scope="{row}">
            <span>{{ row.visitType }}</span>
          </template>
        </el-table-column>
        <el-table-column label="拜访日期" align="center" width="120">
          <template slot-scope="{row}">
            <span>{{ row.visitTime }}</span>
          </template>
        </el-table-column>
<!--        <el-table-column label="拜访地点" align="center" width="210">-->
<!--          <template slot-scope="{row}">-->
<!--            <span>{{ row.spot }}</span>-->
<!--          </template>-->
<!--        </el-table-column>-->
        <el-table-column label="沟通内容" align="center">
          <template slot-scope="{row}">
            <span>{{ row.content }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" width="210"  fixed="right">
          <template slot-scope="{row}">
            <PermissionButton
              menu-code="_views_market_visit_detail"
              class-name="filter-item"
              name=""
              type="primary"
              :page-jump="true"
              :page-query="{id: row.id,listQuery:listQuery,activeName:'first'}"
              round
              size="mini"
            />
<!--            <el-button-->
<!--              class="filter-item"-->
<!--              style="margin-left: 10px;"-->
<!--              type="success"-->
<!--              round-->
<!--              @click="jumpReply(row.id)"-->
<!--            >-->
<!--              回复-->
<!--            </el-button>-->
            <PermissionButton
              menu-code="_views_market_visit_remove"
              class-name="filter-item"
              name=""
              type="danger"
              round
              size="mini"
              @click="deleteInfo(row.id)"
            />
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
    name: 'ViewsMarketVisitList',
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
        isDisable:false,
        tableKey: 0,
        pageData: { records: [] },
        total: 20,
        listLoading: true,
        listQuery: {
          page: 1,
          size: 10,
          descs: 'id',
          keyword:null
        },
      }
    },
    created() {
      const that = this;
      if (that.$route.query.current && !isNaN(that.$route.query.current)) {
        that.listQuery.current = parseInt(that.$route.query.current);
      }
      if (that.$route.query.keyword) {
        that.listQuery.keyword = that.$route.query.keyword;
      }
      if (that.$route.query.visitType) {
        that.listQuery.visitType = that.$route.query.visitType;
      }
      if (that.$route.query.userName) {
        that.listQuery.userName = that.$route.query.userName;
      }
      that.getList();
    },
    methods: {
      visitExport() {
      this.$utils.exportUtil(
        "/visit/export", this.listQuery,
        "导出"
      );
    },
      jumpReply(id){
        this.$router.push({path:'/market/visit/reply', query:{bizTableId:id}})
      },
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
        const that = this
        this.listLoading = true;
        this.$api.visit
          .list(Object.assign({}, that.listQuery))
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
          that.$api.visit.delete(id).then(data => {
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
