<template>
  <div class="app-container organization-index">
    <div class="title-container">
      <breadcrumb id="breadcrumb-container" class="breadcrumb-container"/>
    </div>
    <div style="padding-top: 30px;">
      <el-tabs v-model="activeName" @tab-click="handleClick">
        <el-tab-pane label="部门日志" name="first">
          <y-page-list-layout :page-para="listQuery1" :get-page-list="getDepartmentList" :page-list="departmentPageData">
            <el-table
              size="medium"
              slot="table"
              row-key="id"
              v-loading="listLoading"
              :data="departmentPageData.records"
              fit
              highlight-current-row
              :header-row-style="{color: '#333333'}"
              style="border-left: 1px solid #EBECED;border-right: 1px solid #EBECED;color: #333333;"
            >
              <el-table-column label="部门名称" min-width="150" align="center">
                <template slot-scope="{row}">
                  <span>{{ row.name }}</span>
                </template>
              </el-table-column>
              <el-table-column label="上级部门" min-width="150" align="center">
                <template slot-scope="{row}">
                  <span>{{ row.parentName || '无' }}</span>
                </template>
              </el-table-column>
              <el-table-column label="在职人数" min-width="150" align="center">
                <template slot-scope="{row}">
                  <span>{{ row.peopleNum || '0' }}</span>
                </template>
              </el-table-column>
              <el-table-column label="联系电话" min-width="150" align="center">
                <template slot-scope="{row}">
                  <span>{{ row.phone }}</span>
                </template>
              </el-table-column>
              <el-table-column label="创建时间" min-width="150" align="center">
                <template slot-scope="{row}">
                  <span>{{ row.created }}</span>
                </template>
              </el-table-column>
              <el-table-column label="操作" align="center" width="250" class-name="small-padding fixed-width">
                <template slot-scope="{row}">
                  <PermissionButton
                    menu-code="_views_logs_collect"
                    class-name="filter-item"
                    name="查看"
                    type="success"
                    :page-jump="true"
                    :page-query="{departmentId: row.id}"
                    round
                    size="mini"
                  />
                </template>
              </el-table-column>
            </el-table>
          </y-page-list-layout>
        </el-tab-pane>
        <el-tab-pane label="项目日志">
          <y-page-list-layout :page-para="listQuery2" :get-page-list="getItemList" :page-list="itemPageData" >
            <template slot="left">
              <el-input
                v-model="listQuery2.name"
                placeholder="项目名称"
                clearable
                style="margin-left: 20px;width: 320px;float: left;"
              >
              </el-input>
              <el-button
                class="filter-item"
                style="margin-left: 10px;float: left;"
                type="primary"
                @click="getItemList"
                round
              >搜索
              </el-button>
              <el-button
                class="filter-item"
                style="float: left;"
                round
                type="warning"
                @click="resetSearch"
              >重置
              </el-button>
            </template>
            <el-table
              size="medium"
              slot="table"
              row-key="id"
              v-loading="listLoading"
              :data="itemPageData.records"
              fit
              highlight-current-row
              :header-row-style="{color: '#333333'}"
              style="border-left: 1px solid #EBECED;border-right: 1px solid #EBECED;color: #333333;"
            >
              <el-table-column label="项目名称" align="center">
                <template slot-scope="{row}">
                  <span>{{ row.name }}</span>
                </template>
              </el-table-column>
              <el-table-column label="事业部流水号" align="center">
                <template slot-scope="{row}">
                  <span>{{ row.businessNo }}</span>
                </template>
              </el-table-column>
              <el-table-column label="项目编号" align="center" width="120">
                <template slot-scope="{row}">
                  <span>{{ row.oaNo }}</span>
                </template>
              </el-table-column>
              <el-table-column label="委托单位" align="center" width="120">
                <template slot-scope="{row}">
                  <span>{{ row.clientUnit}}</span>
                </template>
              </el-table-column>
              <el-table-column label="委托人" align="center" width="120">
                <template slot-scope="{row}">
                  <span>{{ row.clientName}}</span>
                </template>
              </el-table-column>
              <el-table-column label="合同金额" align="center" width="120">
                <template slot-scope="{row}">
                  <span>{{ row.amount}}</span>
                </template>
              </el-table-column>
              <el-table-column label="状态" align="center" width="120">
                <template slot-scope="{row}">
                  <span>{{ row.itemStatus}}</span>
                </template>
              </el-table-column>
              <el-table-column label="操作" align="center" width="100">
                <template slot-scope="{row}">
                  <PermissionButton
                    menu-code="_views_logs_collect"
                    class-name="filter-item"
                    name="查看"
                    type="success"
                    :page-jump="true"
                    :page-query="{itemId: row.id}"
                    round
                    size="mini"
                  />
                </template>
              </el-table-column>
            </el-table>
          </y-page-list-layout>
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>
<script>
  import Breadcrumb from '@/components/Breadcrumb'
  import PermissionButton from '@/components/PermissionButton/PermissionButton'
  import YPageListLayout from '@/components/YPageListLayout'

  export default {
    name: 'rateEntry',
    components: {
      Breadcrumb,
      PermissionButton,
      YPageListLayout
    },
    filters: {
      statusFilter(status) {
        const statusMap = {
          published: 'success',
          draft: 'info',
          deleted: 'danger'
        };
        return statusMap[status]
      }
    },
    data() {
      return {
        activeName: 'first',
        departmentPageData: {},
        itemPageData: {},
        listLoading: false,
        listQuery1: {
          current: 1,
          size: 10,
          descs: 'id',
        },
        listQuery2: {
          current: 1,
          size: 10,
          descs: 'id',
          me: false
        },
      }
    },
    created() {
      this.getDepartmentList();
    },
    methods: {
      handleClick(tab, event) {
        if (tab.label==='项目日志'){
          this.getItemList();
        }else {
          this.getDepartmentList()
        }
      },
      resetTemp() {
        this.temp = {
          code: '',
          name: 1,
          parentId: '',
          phone: ''
        }
      },
      resetSearch() {
        this.listQuery2 = {
          current: 1,
          size: 10,
          descs: 'id',
        }
        this.getItemList()
      },
      getDepartmentList() {
        const that = this;
        this.listLoading = true;
        this.$api.department.permissionPage(that.listQuery1).then(res => {
          that.departmentPageData = res.data;
          setTimeout(() => {
            that.listLoading = false
          }, 200)
        }).catch(() => {
          that.listLoading = false
        });
      },
      getItemList() {
        const that = this;
        this.listLoading = true;
        const key = {};
        if (this.$router.currentRoute.path === '/item/list/me'){
          this.listQuery2.me = true;
        }
        this.$api.item.list(Object.assign({}, that.listQuery2, key)).then((res) => {
          that.itemPageData = res.data;
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
<style lang="scss">
  .organization-index {
    .el-table [class*=el-table__row--level] .el-table__expand-icon {
      color: red;
    }
  }
</style>
<style scoped lang="scss">
</style>
