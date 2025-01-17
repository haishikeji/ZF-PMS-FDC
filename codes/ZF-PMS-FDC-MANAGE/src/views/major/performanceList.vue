<template>
  <div class="app-container">
    <div class="title-container">
      <breadcrumb id="breadcrumb-container" class="breadcrumb-container" />
    </div>
    <y-page-list-layout :page-list="pageData" :page-para="listQuery" :get-page-list="getList">
      <template slot="left">
        <el-input v-model="listQuery.keyword" placeholder="关键字搜索..." clearable
          style="margin-left: 20px;width: 500px;float: left;">
        </el-input>
        <el-select style="margin-left: 20px;width: 170px;float: left;" v-model="listQuery.production" clearable
          placeholder="产品类型" @change="getList()">
          <el-option value="STATEMENT" label="价值意见书">价值意见书</el-option>
          <el-option value="REPORT" label="报告">报告</el-option>
          <el-option value="LETTER" label="复评函">复评函</el-option>
        </el-select>
        <el-select style="margin-left: 20px;width: 170px;float: left;" v-model="listQuery.hasRealAmount" @change="getList()" placeholder="收款状态">
          <el-option label="(实收款)全部">(实收款)全部</el-option>
          <el-option :value="true" label="已收款">已收款</el-option>
          <el-option :value="false" label="未收款">未收款</el-option>
        </el-select>
        <el-select style="margin-left: 20px;width: 170px;float: left;" v-model="listQuery.hasAllot" @change="getList()" placeholder="收款状态">
          <el-option label="(分配)全部">(分配)全部</el-option>
          <el-option :value="true" label="已收款">已分配</el-option>
          <el-option :value="false" label="未收款">未分配</el-option>
        </el-select>
        <el-button class="filter-item" style="margin-left: 10px;float: left;" type="primary" @click="searchList" round>搜索
        </el-button>
        <el-button class="filter-item" style="float: left;" round type="success" @click="resetSearch()">重置
        </el-button>
      </template>
      <parentTable :data="pageData.records" slot="table" style="width: 100%;">
        <el-table-column label="订单编号" align="center">
          <template slot-scope="{row}">
            <span>{{ row.orderId }}</span>
          </template>
        </el-table-column>
        <el-table-column label="项目名称" align="center" show-overflow-tooltip>
          <template slot-scope="{row}">
            <span>{{ row.name }}</span>
          </template>
        </el-table-column>
        <el-table-column label="产品编号" align="center" width='300'>
          <template slot-scope="{row}">
            <span>{{ row.reportNo }}</span>
          </template>
        </el-table-column>
        <el-table-column label="产品类型" align="center">
          <template slot-scope="{row}">
            <span>{{ row.production }}</span>
          </template>
        </el-table-column>
        <el-table-column label="实收款" align="center">
          <template slot-scope="{row}">
            <span>{{ row.claimTimes > 0 ? '已认领' : '未认领' }}</span>
          </template>
        </el-table-column>
        <el-table-column label="绩效分配" align="center">
          <template slot-scope="{row}">
            <span>{{ row.isAllot ? '已分配' : '未分配' }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" width='120'>
          <template slot-scope="{row}">
            <el-button type="text" size="small" @click="doPerformanceAllot(row)">{{ !row.isAllot ? '分配' : '查看'
            }}</el-button>
          </template>
        </el-table-column>
      </parentTable>
    </y-page-list-layout>
    <el-dialog :visible.sync="allotDialog" width="75%" center custom-class="doWarehouseClass">
      <el-form ref="performanceForm" :model="performance" class="form-container">
        <div class="createMajor-main-container">
          <div class="postInfo-container">
            <el-row>
              <el-col :xs="24" :sm="12" :lg="24" :span="6">
                <el-button style="float:right" type="success" @click="saveAllot()">确认分配</el-button>
              </el-col>
            </el-row>
            <div>
              <el-divider content-position="left">
                <h3 class="title">{{ reportNo }} 绩效分配</h3>
              </el-divider>
            </div>
            <el-row class="row-style" v-if="performance.performanceList.length > 0">
              <el-col :xs="24" :sm="12" :lg="6" :span="6" v-for="(a, index) in performance.performanceList">
                <el-card shadow="always" style="margin-top:30px;margin-right:15px;margin-bottom:30px;">
                  <span v-if="a.isPrincipal" style="position:absolute; top:35px;left :87px"><el-tag
                      type="danger">项目负责人</el-tag></span>
                  <div class="performance-avatar-wrapper perfomance-icon-title">{{ a.userName.slice(-2) }}</div>
                  <el-input v-model.number="a.ratio" type="number" style="width:80%;margin-left:5px">
                    <template slot="append">%</template>
                  </el-input>
                </el-card>
              </el-col>
            </el-row>
            <div>
              <el-select style="margin-right: 15px;width: 20%;" v-model="needAddToPerformanceUsers"
                placeholder="请选择要添加的人员" multiple filterable clearable>
                <el-option v-for="(u, id) in allUsers" :label="u.name" :value="u" :key="u.id"></el-option>
              </el-select>
              <el-button type="primary" @click="addToPerformanceUsers()">添加</el-button>
            </div>
          </div>
        </div>
      </el-form>
    </el-dialog>
  </div>
</template>
<script>
import YPageListLayout from '@/components/YPageListLayout'
import Breadcrumb from '@/components/Breadcrumb'


export default {
  name: 'performanceList',
  components: {
    Breadcrumb,
    YPageListLayout
  },

  data() {
    return {
      pageData: { records: [] },
      listQuery: {
        page: 1,
        size: 10,
        descs: 'id',
      },
      allotDialog: false,
      performance: {
        performanceList: [],
        majorProductionId: null
      },
      reportNo: null,
      majorId: null,
      allUsers: [],
      needAddToPerformanceUsers: []

    }
  },
  created() {
    this.reportNo = this.$route.query.reportNo;
    this.performance.majorProductionId = this.$route.query.id;
    this.majorId = this.$route.query.majorId;
    if (this.performance.majorProductionId) {
      this.autoPerformanceAllot();
    }
    this.getList();
  },
  methods: {
    addToPerformanceUsers() {
      this.needAddToPerformanceUsers.forEach((item) => {
        let user = {
          ratio: 0,
          isPrincipal: false,
          userId: item.id,
          userName: item.name,
          id: null,
          created: null,
          modified: null,
          majorId: null,
          majorProductionId: null,
          declareId: null,
          userType: null
        }
        // 判断要添加的人员是否已经存在了,已存在的跳过
        if (!this.performance.performanceList.some(user => user.userId === item.id)) {
          this.performance.performanceList.push(user);
        }
      });
      this.needAddToPerformanceUsers = [];
      console.log(this.performance.performanceList);
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
      this.$api.majorProduction.performanceList(Object.assign({}, this.listQuery)).then(res => {
        if (res.code === 200) {
          this.pageData = res.data;
        }
      })
    },

    autoPerformanceAllot() {
      this.allotDialog = true;
      this.$api.majorPerformanceAllot.list(this.performance.majorProductionId).then(res => {
        if (res.code === 200) {
          const list = res.data;
          for (let i in list) {
            if (list[i].ratio == null) {
              list[i].ratio = 0;
            } else {
              list[i].ratio = (list[i].ratio) * 100;
            }
          }
          this.performance.performanceList = list;
        }
      })
    },
    doPerformanceAllot(row) {
      this.getAllUser();
      this.allotDialog = true;
      this.reportNo = row.reportNo;
      this.performance.majorProductionId = row.id;
      this.majorId = row.majorId;
      this.$api.majorPerformanceAllot.list(row.id).then(res => {
        if (res.code === 200) {
          const list = res.data;
          for (let i in list) {
            if (list[i].ratio == null) {
              list[i].ratio = 0;
            } else {
              list[i].ratio = (list[i].ratio) * 100;
            }
          }
          this.performance.performanceList = list;
          console.log(this.performance.performanceList);
        }
      })
    },
    saveAllot() {
      const id = this.performance.majorProductionId;
      const list = this.performance.performanceList;
      let total = 0;
      list.map(function (item) {
        total += item.ratio;
      })
      if (total != 100) {
        this.$notify({
          title: '提示',
          message: '绩效分配之和需等于100，请检查。',
          type: 'warning',
          duration: 2000
        });
        return;
      }
      for (let i in list) {
        list[i].majorProductionId = id;
        list[i].ratio = (list[i].ratio) / 100;
        list[i].userType = 'EVALUATE';
        list[i].majorId = this.majorId;
      }
      this.$api.majorPerformanceAllot.allot(list).then(res => {
        if (res.code === 200 && res.data) {
          this.$notify({
            title: '成功',
            message: '保存成功',
            type: 'success',
            duration: 2000
          });
          this.allotDialog = false;
          this.getList();
        } else {
          this.$notify({
            title: '失败',
            message: '保存失败',
            type: 'error',
            duration: 2000
          });
        }
      })
    },
    getAllUser() {
      this.$api.user.simpleAll().then(res => {
        if (res.code === 200) {
          this.allUsers = res.data;
        }
      })
    }
  },

}
</script>
<style lang="scss" scoped>
/deep/.doWarehouseClass {
  border-radius: 10px;
}

.performance-avatar-wrapper {
  width: 50px;
  height: 50px;
  position: relative;
  border-radius: 50%;
  background-color: rgba(129, 216, 207, 1);
  text-align: center;
  line-height: 50px;
}

.perfomance-icon-title {
  font-size: 20px;
  color: rgba(255, 255, 255, 1);
  display: inline-block;
  position: relative;
  top: -2px;
}
</style>
