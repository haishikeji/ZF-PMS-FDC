<template>
  <div class="app-container">
    <div class="title-container">
      <breadcrumb id="breadcrumb-container" class="breadcrumb-container" />
    </div>

    <y-page-list-layout :page-list="pageData" :page-para="listQuery" :get-page-list="getList">
      <template slot="left">
        <PermissionButton menu-code="_views_market_business_add" class-name="filter-item" type="primary" name size="mini"
          :page-jump="true" round style="float: left" />
        <el-input v-model="listQuery.keyword" placeholder="客户名字/客户经理" clearable
          style="margin-left: 20px;width: 320px;float: left;">
        </el-input>
        <el-input v-model="listQuery.opportunityName" placeholder="商机名称" clearable
          style="margin-left: 20px;width: 320px;float: left;">
        </el-input>
        <el-select clearable style="margin-left: 20px;width: 200px;float: left;" v-model="listQuery.state"
          placeholder="状态">
          <el-option key="0" label="跟进中" value="跟进中" />
          <el-option key="1" label="签约" value="签约" />
          <el-option key="2" label="终止" value="终止" />
        </el-select>
        <el-button class="filter-item" style="margin-left: 10px;float: left;" type="primary" @click="searchList" round>搜索
        </el-button>
        <el-button class="filter-item" style="float: left;" round type="warning" @click="resetSearch()">重置
        </el-button>
      </template>
      <parentTable v-loading="listLoading" :data="pageData.records" slot="table" style="width: 100%;">
        <el-table-column label="客户名字" align="center" width="210">
          <template slot-scope="{row}">
            <span>{{ row.customerName }}</span>
          </template>
        </el-table-column>
        <el-table-column label="商机名称" align="center" width="210">
          <template slot-scope="{row}">
            <span>{{ row.opportunityName }}</span>
          </template>
        </el-table-column>
        <el-table-column label="预计金额" align="center" width="210">
          <template slot-scope="{row}">
            <span>{{ row.estimateAmount }}</span>
          </template>
        </el-table-column>
        <el-table-column label="预计开展日期" align="center" width="210">
          <template slot-scope="{row}">
            <span>{{ row.estimateDate }}</span>
          </template>
        </el-table-column>
        <el-table-column label="状态" align="center" width="120">
          <template slot-scope="{row}">
            <el-tag>{{ row.state }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="终止原因" align="center" width="210">
          <template slot-scope="{row}">
            <span>{{ row.offReason }}</span>
          </template>
        </el-table-column>
        <el-table-column label="客户经理" align="center" width="210">
          <template slot-scope="{row}">
            <span>{{ row.userName }}</span>
          </template>
        </el-table-column>
        <el-table-column label="备注" align="center" width="210">
          <template slot-scope="{row}">
            <span>{{ row.remark }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" width="300" fixed="right">
          <template slot-scope="{row}">
            <PermissionButton menu-code="_views_market_business_detail" class-name="filter-item" name="" type="primary"
              :page-jump="true" :page-query="{ id: row.id, listQuery: listQuery }" round size="mini" />
            <PermissionButton menu-code="_views_market_business_state" class-name="filter-item" name="" type="danger"
              round size="mini" @click="handleState(row)" />
            <PermissionButton menu-code="_views_market_business_transfer" class-name="filter-item" name="" type="success"
              round size="mini" @click="openTransferDialog(row)" />
          </template>
        </el-table-column>
      </parentTable>
    </y-page-list-layout>
    <el-dialog width="600px" title="状态流转" :visible.sync="dialogFormVisible" :close-on-click-modal="false">
      <el-form ref="dataForm" v-loading="dialogLoading" :model="temp" label-position="right" label-width="110px"
        style="width: 400px; margin-left:50px;">
        <el-form-item label="商机名称：" prop="name">
          <el-input v-model="temp.opportunityName" class="filter-item" readonly disabled />
        </el-form-item>

        <el-form-item label="流转状态：" filterable prop="departmentId">
          <el-select v-model="temp.state" filterable class="filter-item" style="float: left;width: 100%;"
            placeholder="请选择">
            <el-option key="0" label="跟进中" value="跟进中" />
            <el-option key="1" label="签约" value="签约" />
            <el-option key="2" label="终止" value="终止" />
          </el-select>
        </el-form-item>

        <el-form-item label="终止原因：">
          <el-input v-model="temp.offReason" class="filter-item" type="text" />
        </el-form-item>
        <el-form-item label="">
          <el-button @click="dialogFormVisible = false">
            取消
          </el-button>
          <el-button type="primary" @click="updateState()">
            保存
          </el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
    <el-dialog :visible.sync="dialogFormVisibleX" :close-on-click-modal="false" custom-class="tansferDialog">
      <div class="detail">
        <el-divider content-position="left">项目信息</el-divider>
        <el-descriptions :column="4" size="lager" border>
          <el-descriptions-item label="客户名字">{{ detail.customerName }}</el-descriptions-item>
          <el-descriptions-item label="商机名称">{{ detail.opportunityName }}</el-descriptions-item>
          <el-descriptions-item label="预计金额">{{ detail.estimateAmount }}</el-descriptions-item>
          <el-descriptions-item label="预计开展日期">{{ detail.estimateDate }}</el-descriptions-item>
          <el-descriptions-item label="状态">{{ detail.state }}</el-descriptions-item>
          <el-descriptions-item label="客户经理">
            <el-select v-model="detail.userId">
              <el-option v-for="(item, index) in managers" :key="index" :label="item.name" :value="item.id"></el-option>
            </el-select>
            <el-tooltip class="item" effect="dark" content="点击转交" placement="bottom-start">
              <el-button type="success" icon="el-icon-check" circle style="margin-left: 20px;" placeholder="转交"
                @click="saveManager"></el-button>
            </el-tooltip>
          </el-descriptions-item>
          <el-descriptions-item label="终止原因">{{ detail.offReason }}</el-descriptions-item>
        </el-descriptions>
      </div>
      <div class="histroy">
        <el-divider content-position="left">转交记录</el-divider>
        <el-collapse v-model="activeNames">
          <el-collapse-item name="1">
            <el-timeline>
              <el-timeline-item v-for="item in historys" :key="item.id" :timestamp="item.created" placement="top">
                <el-card>
                  <el-descriptions :column="3" border>
                    <el-descriptions-item label="前客户经理"><el-tag type="info">{{ item.originalUserName
                    }}</el-tag></el-descriptions-item>
                    <el-descriptions-item label="后客户经理"><el-tag type="success">{{ item.currentUserName
                    }}</el-tag></el-descriptions-item>
                    <el-descriptions-item label="操作人"><el-tag>{{ item.operatorName }}</el-tag></el-descriptions-item>
                  </el-descriptions>
                </el-card>
              </el-timeline-item>
            </el-timeline>
          </el-collapse-item>
        </el-collapse>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import YPageListLayout from '@/components/YPageListLayout'
import Breadcrumb from '@/components/Breadcrumb'
import PermissionButton from '@/components/PermissionButton/PermissionButton'

export default {
  name: 'ViewsMarketBusinessList',
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
      isDisable: false,
      tableKey: 0,
      pageData: { records: [] },
      total: 20,
      dialogLoading: false,
      listLoading: true,
      listQuery: {
        page: 1,
        size: 10,
        descs: 'id',
        keyword: null
      },
      listQueryKey: 'keyword',
      importLoading: false,
      AllEnum: [],
      dialogFormVisible: false,
      temp: {},
      dialogFormVisibleX: false,
      detail: {},
      activeNames: ['1'],
      historys: [],
      managers: [],
    }
  },
  created() {
    const that = this;
    if (that.$route.query.current && !isNaN(that.$route.query.current)) {
      that.listQuery.current = parseInt(that.$route.query.current);
    }
    if (that.$route.query.customerName) {
      that.listQuery.customerName = that.$route.query.customerName;
    }
    if (that.$route.query.opportunityName) {
      that.listQuery.opportunityName = that.$route.query.opportunityName;
    }
    if (that.$route.query.state) {
      that.listQuery.state = that.$route.query.state;
    }
    that.getAllEnum();
    that.getList();
  },
  methods: {
    handleState(row) {
      this.temp = row;
      // that.temp.id = row.businessId;
      // that.temp.opportunityName = row.businessName;
      // that.temp.state = row.state;
      // that.temp.offReason = row.offReason;
      this.dialogFormVisible = true;
    },
    updateState() {
      if (this.temp.id) {
        this.$api.business.edit(Object.assign({}, this.temp, {
        })).then(res => {
          if (res.code === 200) {
            this.$notify({
              title: '成功',
              message: '状态流转成功',
              type: 'success',
              duration: 2000
            });
            this.dialogFormVisible = false;
            if (this.temp.state === "签约") {
              this.temp.id = null;
              this.$router.push({ path: "/item/transfer", query: this.temp })
            }

          }
        }).catch(() => {
          this.dialogFormVisible = false;
        })
      }
    },
    getAllEnum() {
      const that = this;
      that.$api.globalConfig.getAllEnum().then(data => {
        if (data.code === 200) {
          that.AllEnum = data.data
        } else {
          this.$message({
            type: 'error',
            message: data.msg
          })
        }
      })
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
    removeHandle(row) {
      // console.log(data)
      const that = this
      that
        .$confirm('确认删除当前记录吗?', '警告', {
          confirmButtonText: '确认',
          cancelButtonText: '取消',
          type: 'warning',
        })
        .then(async () => {
          this.$api.business.delete(row.id).then((res) => {
            if (res.code === 200) {
              this.$message({
                type: 'success',
                message: '删除成功',
              })
              this.getList()
            }
          })
        })
        .catch((err) => {
          console.error(err)
        })
    },
    searchList() {
      // 重置分页
      this.listQuery.page = 1
      this.listQuery.size = 10
      this.getList()
    },
    getList() {
      const that = this
      this.listLoading = true
      // console.log(that.listQuery)
      this.$api.business
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
        that.$api.business.delete(id).then(data => {
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
    },
    openTransferDialog(row) {
      const originalUserId = row.userId;
      this.dialogFormVisibleX = true;
      this.detail = row;
      this.detail.originalUserId = originalUserId;
      this.getHistroyList(row.id);
      this.getManagers();
    },
    getManagers() {
      this.$api.user.usersByDepartment("市场部").then(res => {
        this.managers = res.data
      })
    },
    getHistroyList(id) {
      this.$api.resourceTransfer.history({ "resourceType": "BUSINESS_OPPORTUNITY", "resourceId": id }).then(res => {
        if (res.code === 200) {
          this.historys = res.data;
        }
      })
    },
    saveManager() {
      if (this.detail.userId === this.detail.originalUserId) {
        this.$message({
          type: 'error',
          message: '抱歉，不能转交给本人。'
        });
        return;
      }
      this.$api.business.changeManager(this.detail).then(res => {
        if (res.code === 200 && res.data === true) {
          this.$message({
            type: 'success',
            message: '转交成功'
          });
          this.getHistroyList(this.detail.id);
        }
      }
      );
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

/deep/ .tansferDialog {
  width: 60%;
  border-radius: 10px;
}

.el-dropdown-link {
  cursor: pointer;
  color: red;
}

.el-icon-arrow-down {
  font-size: 12px;
}
</style>
