<template>
  <div class="app-container">
    <div class="title-container">
      <breadcrumb id="breadcrumb-container" class="breadcrumb-container" />
    </div>
    <y-page-list-layout :page-list="pageData" :page-para="listQuery" :get-page-list="getList">
      <template slot="left">
        <PermissionButton menu-code="_views_item_add" class-name="filter-item" type="primary" size="mini"
          :page-jump="true" round style="float: left" />
        <el-select v-model="listQuery.state" placeholder="状态" clearable filterable
          style="margin-left: 20px;width: 100px;float: left;" class="filter-item">
          <el-option label="进行中" value="进行中" />
          <el-option label="已完成" value="已完成" />
          <el-option label="未开始" value="未开始" />
          <el-option label="暂停" value="暂停" />
          <el-option label="终止" value="终止" />
        </el-select>
        <el-select v-model="listQuery.cate" placeholder="项目类型" clearable filterable
          style="margin-left: 20px;width: 100px;float: left;" class="filter-item">
          <el-option v-for="item in cateList" :key="item.id" :label="item.name" :value="item.id" />
        </el-select>
        <el-input v-model="listQuery.name" placeholder="项目名称" clearable
          style="margin-left: 20px;width: 270px;float: left;">
        </el-input>
        <el-input v-model="listQuery.businessNo" placeholder="流水号" clearable
          style="margin-left: 20px;width: 200px;float: left;">
        </el-input>
        <el-input v-model="listQuery.clientManager" placeholder="客户经理" clearable
          style="margin-left: 20px;width: 100px;float: left;">
        </el-input>
        <el-input v-model="listQuery.skiller" placeholder="项目负责人" clearable
          style="margin-left: 20px;width: 150px;float: left;">
        </el-input>
        <el-button class="filter-item" style="margin-left: 10px;float: left;" type="primary" @click="searchList" round>搜索
        </el-button>
        <el-button class="filter-item" style="float: left;" round type="success" @click="resetSearch()">重置
        </el-button>
        <el-button  style="margin-left: 10px; float: left;" round type="info" @click="editBusinessState">项目状态
          <i class="el-icon-edit"></i>
        </el-button>
        <div v-if=manySelect style="float: left;">
          <el-button  style="margin-left: 10px; float: left;" round type="warning" @click="toUpdateState('暂停')">暂停
        </el-button>
        <el-button  style="margin-left: 10px; float: left;" round type="danger" @click="toUpdateState('终止')">终止
        </el-button>
        <el-button  style="margin-left: 10px; float: left;" round  @click="toUpdateState(null)">还原
        </el-button>
        </div>
      </template>
      <template slot="right">
        <PermissionButton menu-code="_views_item_export" class-name="filter-item" round type="primary" name="导出"
          @click="itemExport">
        </PermissionButton>
        <PermissionButton menu-code="_views_item_mine_export" class-name="filter-item" round type="primary" name="导出"
          @click="itemMineExport">
        </PermissionButton>
      </template>
      <parentTable v-loading="listLoading" :data="pageData.records" slot="table" style="width: 100%;"  :selectionChange="handleSelectionChange">
        <el-table-column
          v-if="manySelect"
          align="center"
          type="selection"
          width="100" border="true">
        </el-table-column>
        <el-table-column label="项目名称" align="center" width="200">
          <template slot-scope="{row}">
            <span>{{ row.name }}</span>
          </template>
        </el-table-column>
        <el-table-column label="事业部流水号" align="center" width="135">
          <template slot-scope="{row}">
            <span>{{ row.businessNo }}</span>
          </template>
        </el-table-column>
        <el-table-column label="项目编号" align="center" width="180">
          <template slot-scope="{row}">
            <span>{{ row.belongTo }}{{ row.oaNo }}</span>
          </template>
        </el-table-column>
        <el-table-column label="所属部门" align="center" width="130">
          <template slot-scope="{row}">
            <span>{{ row.departmentName }}</span>
          </template>
        </el-table-column>
        <el-table-column label="项目类型" align="center" width="130">
          <template slot-scope="{row}">
            <span>{{ row.cateName }}</span>
          </template>
        </el-table-column>
        <el-table-column label="业务来源" align="center" width="130">
          <template slot-scope="{row}">
            <span>{{ row.businessSourceName }}</span>
          </template>
        </el-table-column>
        <el-table-column label="委托单位" align="center" width="130">
          <template slot-scope="{row}">
            <span>{{ row.clientUnit }}</span>
          </template>
        </el-table-column>
        <el-table-column label="客户名字" align="center" width="130">
          <template slot-scope="{row}">
            <span>{{ row.clientName }}</span>
          </template>
        </el-table-column>
        <el-table-column label="合同金额" align="center" width="100">
          <template slot-scope="{row}">
            <span>{{ row.amount }}</span>
          </template>
        </el-table-column>
        <el-table-column label="状态" align="center" width="100">
          <template slot-scope="{row}">
            <el-tag>{{ row.itemStatus }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="客户经理" align="center" width="100">
          <template slot-scope="{row}">
            <span>{{ row.clientManager }}</span>
          </template>
        </el-table-column>
        <el-table-column label="项目负责人" align="center" width="100">
          <template slot-scope="{row}">
            <span>{{ row.skiller }}</span>
          </template>
        </el-table-column>
        <el-table-column label="当前阶段" align="center" width="200">
          <template slot-scope="{row}">
            <el-tag>{{ computedStageName(row.stageName) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="电子合同" align="center" width="100">
          <template slot-scope="{row}">
            <el-button v-if="row.contractUrl" type="success" plain round
              @click="downloadContract(row.contractUrl)">下载</el-button>
            <el-button v-else plain round disabled>下载</el-button>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" width="150" fixed="right">
          <template slot-scope="{row}">
          <el-dropdown  type="primary">
              <el-button type="primary" round>
                更多按钮<i class="el-icon-arrow-down el-icon--right"></i>
              </el-button>
            <el-dropdown-menu slot="dropdown" >
              <el-dropdown-item :divided=true >
                <PermissionButton menu-code="_views_item_plan" class-name="filter-item" name="" type="primary"
              :page-jump="true" :page-query="{ id: row.id, itemName: row.name, listQuery: listQuery }" round/>
              </el-dropdown-item>
              <el-dropdown-item :divided=true >
                <PermissionButton menu-code="_views_item_plan_list" class-name="filter-item" name="" type="primary"
              :page-jump="true" :page-query="{ id: row.id, itemName: row.name, listQuery: listQuery }" round />
              </el-dropdown-item>
              <el-dropdown-item :divided=true >
                <PermissionButton menu-code="_view_item_log_write" class-name="filter-item" name="" type="success"
              :page-jump="true"
              :page-query="{ id: row.id, itemName: row.name, stageName: row.stageName, stageId: row.stageId, listQuery: listQuery }" round/>
              </el-dropdown-item>
              <el-dropdown-item :divided=true >
                <PermissionButton menu-code="_views_item_rate" class-name="filter-item" name="日志" type="info"
              :page-jump="true" :page-query="{ id: row.id, listQuery: listQuery }" round  />
              </el-dropdown-item>
              <el-dropdown-item :divided=true >
                <PermissionButton menu-code="_views_item_detail" class-name="filter-item" name="" type="primary"
              :page-jump="true" :page-query="{ id: row.id, listQuery: listQuery }" round  />
              </el-dropdown-item>
              <el-dropdown-item :divided=true >
                <PermissionButton menu-code="_views_item_detail_readonly" class-name="filter-item" name="" type="primary"
              :page-jump="true" :page-query="{ id: row.id, listQuery: listQuery }" round  />
              </el-dropdown-item>
              <el-dropdown-item :divided=true >
                <PermissionButton menu-code="_views_item_remove" class-name="filter-item" name="" type="danger" round @click="deleteInfo(row.id)" />
              </el-dropdown-item>
              <el-dropdown-item :divided=true >
                <PermissionButton menu-code="_views_item_transfer" class-name="filter-item" name="" type="success" round @click="transfer(row)" />
              </el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
          </template>
        </el-table-column>
      </parentTable>
    </y-page-list-layout>
    <el-dialog :visible.sync="dialogFormVisible" :close-on-click-modal="false" custom-class="tansferDialog">
      <div class="detail">
        <el-divider content-position="left">项目信息</el-divider>
        <el-descriptions :column="4" size="lager" border>
          <el-descriptions-item label="项目名称">{{ detail.name }}</el-descriptions-item>
          <el-descriptions-item label="事业部流水号">{{ detail.businessNo }}</el-descriptions-item>
          <el-descriptions-item label="项目编号">{{ detail.belongTo + detail.oaNo }}</el-descriptions-item>
          <el-descriptions-item label="所属部门">{{ detail.departmentName }}</el-descriptions-item>
          <el-descriptions-item label="项目类型">{{ detail.cateName }}</el-descriptions-item>
          <el-descriptions-item label="业务来源">{{ detail.businessSourceName }}</el-descriptions-item>
          <el-descriptions-item label="委托单位">{{ detail.clientUnit }}</el-descriptions-item>
          <el-descriptions-item label="客户名称">{{ detail.clientName }}</el-descriptions-item>
          <el-descriptions-item label="合同金额">{{ detail.amount }}</el-descriptions-item>
          <el-descriptions-item label="客户经理">
            <el-select v-model="detail.userId">
              <el-option v-for="(item, index) in managers" :key="index" :label="item.name" :value="item.id"></el-option>
            </el-select>
            <el-tooltip class="item" effect="dark" content="点击转交" placement="bottom-start">
            <el-button type="success" icon="el-icon-check" circle style="margin-left: 20px;" placeholder="转交"
              @click="saveManager"></el-button>
              </el-tooltip>
          </el-descriptions-item>
          <el-descriptions-item label="状态">{{ detail.itemStatus }}</el-descriptions-item>
          <el-descriptions-item label="当前阶段">{{ computedStageName(detail.stageName) }}</el-descriptions-item>
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
  name: 'ViewsItemList',
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
      listLoading: true,
      listQuery: {
        page: 1,
        size: 10,
        descs: 'id',
        isCurYear: false,
      },
      listQueryKey: 'keyword',
      importLoading: false,
      cateList: [],
      dialogFormVisible: false,
      detail: {},
      activeNames: ['1'],
      historys: [],
      managers: [],
      multipleSelection: [],
      manySelect:false
    }
  },
  created() {
    const that = this;
    if (that.$route.query.state) {
      that.listQuery.state = that.$route.query.state;
    }
    if (that.$route.query.isCurYear) {
      that.listQuery.isCurYear = that.$route.query.isCurYear;
    }
    if (that.$route.query.name) {
      that.listQuery.name = that.$route.query.name;
    }
    if (that.$route.query.current && !isNaN(that.$route.query.current)) {
      that.listQuery.current = parseInt(that.$route.query.current);
    }
    if (that.$route.query.cate) {
      that.listQuery.cate = parseInt(that.$route.query.cate);
    }
    if (that.$route.query.clientManager) {
      that.listQuery.clientManager = that.$route.query.clientManager;
    }
    that.getList();
    that.getCateList();
  },
  methods: {
    itemExport() {
      this.$utils.exportUtil(
        "/item/export", this.listQuery,
        "导出"
      );
    },
    itemMineExport() {
      this.$utils.exportUtil(
        "/item/mine/export", this.listQuery,
        "导出"
      );
    },
    getCateList() {
      this.$api.dictData.simpleType("项目类型").then(res => {
        this.cateList = res.data
      })
    },
    downloadContract(url) {
      var a = document.createElement('a');
      var event = new MouseEvent('click');
      a.download = url;
      a.href = url;//路径前拼上前缀，完整路径
      a.dispatchEvent(event);
    },
    resetSearch() {
      this.$router.push({ query: {} });
      this.listQuery = {
        current: 1,
        size: 10,
        descs: 'id',
        me: false,
      }
      this.getList()
    },
    computedStageName(val) {
      if (val === null || val === undefined || val === '') {
        return "无";
      } else {
        return val;
      }
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
          this.$api.term.delete(row.id).then((res) => {
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
      const that = this;
      this.listLoading = true;
      const key = {};
      if (this.$router.currentRoute.path === '/item/list/mine') {
        this.$api.item.mine(Object.assign({}, that.listQuery, key)).then((res) => {
          that.pageData = res.data;
          setTimeout(() => {
            that.listLoading = false
          }, 200)
        })
          .catch(() => {
            that.listLoading = false
          })
      } else if (this.$router.currentRoute.path === '/item/list/me') {
        this.$api.item.me(Object.assign({}, that.listQuery, key)).then((res) => {
          that.pageData = res.data;
          setTimeout(() => {
            that.listLoading = false
          }, 200)
        })
          .catch(() => {
            that.listLoading = false
          })
      } else {
        this.$api.item.list(Object.assign({}, that.listQuery, key)).then((res) => {
          that.pageData = res.data
          setTimeout(() => {
            that.listLoading = false
          }, 200)
        })
          .catch(() => {
            that.listLoading = false
          })
      }
    },
    deleteInfo(id) {
      const that = this
      that.$confirm('请确认是否删除该数据?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
        center: true
      }).then(() => {
        that.$api.item.delete(id).then(data => {
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
    transfer(row) {
      const originalUserId = row.userId;
      this.dialogFormVisible = true;
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
    saveManager() {
      if (this.detail.userId === this.detail.originalUserId){
        this.$message({
            type: 'error',
            message: '抱歉，不能转交给本人。'
          });
          return ;
      }
      this.$api.item.changeManager(this.detail).then(res => {
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

    getHistroyList(id){
      this.$api.resourceTransfer.history({"resourceType":"ITEM","resourceId":id}).then(res=>{
        if (res.code === 200){
          this.historys = res.data;
        }
      })
    },
    handleSelectionChange(val) {
      this.multipleSelection = [];
       for ( let v in val){
          this.multipleSelection.push(val[v].id)
       }
      },
    editBusinessState(){
        this.manySelect = !this.manySelect
    },
    toUpdateState(state){
      this.$confirm('请确认是否修改这些项目的状态?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
        center: true
      }).then(() => {
      let ids = this.multipleSelection;
      if (ids.length>0){
        this.$api.item.updateState({"ids":ids,"state":state}).then(res=>{
          if (res.code === 200){
            this.$message({
              type: 'success',
              message: '项目状态更新成功'
            });
            this.getList();
          }else {
            this.$message({
              type: 'danger',
              message: '项目状态更新失败'
            });
          }
        })
      }else{
        this.$message({
              type: 'warning',
              message: '请选择需要修改的项目'
          });
      }
    }
    )},
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
