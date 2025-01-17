<template>
  <div class="app-container">
    <div class="title-container">
      <breadcrumb id="breadcrumb-container" class="breadcrumb-container" />
    </div>
    <y-page-list-layout :page-list="pageData" :page-para="listQuery" :get-page-list="assetsMarketDeclarePage">
      <template slot="left">
        <el-input v-model="listQuery.orderId" placeholder="订单号" clearable style="width: 200px;float: left;">
        </el-input>
        <el-input v-model="listQuery.name" placeholder="项目名称" clearable
          style="margin-left: 20px;width: 200px;float: left;">
        </el-input>
        <el-input v-model="listQuery.reportNo" placeholder="报告号" clearable
          style="margin-left: 20px;width: 200px;float: left;">
        </el-input>
        <el-select clearable v-model="listQuery.commissionRateId" placeholder="提成类型"
          style="margin-left: 20px;width: 200px;float: left;">
          <el-option v-for="(c, id) in cates" :value="c.id" :label="c.name">{{ c.name }}</el-option>
        </el-select>
        <el-select clearable v-model="listQuery.declareType" placeholder="申报类型"
          style="margin-left: 20px;width: 200px;float: left;">
          <el-option :value="true" label="产品">产品</el-option>
          <el-option :value="false" label="订单">订单</el-option>
        </el-select>
        <el-select clearable v-model="listQuery.declareResult" placeholder="审核状态"
          style="margin-left: 20px;width: 200px;float: left;">
          <el-option value="待申报" label="待申报"></el-option>
          <el-option value="审核中" label="审核中"></el-option>
          <el-option value="审核通过" label="审核通过"></el-option>
          <el-option value="审核拒绝" label="审核拒绝"></el-option>
        </el-select>
        <el-button class="filter-item" style="margin-left:20px;float: left;" type="primary" @click="searchList()" round>搜索
        </el-button>
        <el-button class="filter-item" style="float: left;" round type="success" @click="resetSearch()">重置
        </el-button>
      </template>
      <parentTable :data="pageData.records" slot="table" style="width: 100%;">
        <el-table-column label="订单号" width="150" align="center">
          <template slot-scope="{row}">
            <span>{{ row.orderId }}</span>
          </template>
        </el-table-column>
        <el-table-column label="项目名称" width="150" align="center">
          <template slot-scope="{row}">
            <span>{{ row.name ? row.name : row.orderName }}</span>
          </template>
        </el-table-column>
        <el-table-column label="报告号" width="200" align="center">
          <template slot-scope="{row}">
            <span>{{ row.reportNo ? row.reportNo : '--' }}</span>
          </template>
        </el-table-column>
        <el-table-column label="评估对象类别" align="center">
          <template slot-scope="{row}">
            <span>{{ row.businessObjectType }}</span>
          </template>
        </el-table-column>
        <el-table-column label="委托人" align="center">
          <template slot-scope="{row}">
            <span>{{ row.bailor }}</span>
          </template>
        </el-table-column>
        <el-table-column label="委托联系人" align="center">
          <template slot-scope="{row}">
            <span>{{ row.bailorContactName }}</span>
          </template>
        </el-table-column>
        <el-table-column label="委托人电话" width="150" align="center">
          <template slot-scope="{row}">
            <span>{{ row.bailorContactTel }}</span>
          </template>
        </el-table-column>
        <el-table-column label="客户名称" align="center">
          <template slot-scope="{row}">
            <span>{{ row.clienteleName }}</span>
          </template>
        </el-table-column>
        <el-table-column label="业务来源" align="center">
          <template slot-scope="{row}">
            <span>{{ row.clienteleSubName }}</span>
          </template>
        </el-table-column>
        <el-table-column label="负责人" align="center">
          <template slot-scope="{row}">
            <span>{{ row.principal }}</span>
          </template>
        </el-table-column>
        <el-table-column label="下单时间" align="center">
          <template slot-scope="{row}">
            <span>{{ row.orderCreated }}</span>
          </template>
        </el-table-column>
        <el-table-column label="提成类型" width="150" align="center">
          <template slot-scope="{row}">
            <span style="color:red">{{ row.commissionType ? row.commissionType : '--' }}</span>
          </template>
        </el-table-column>
        <el-table-column label="申报类型" align="center">
          <template slot-scope="{row}">
            <span style="font-weight: bold;">{{ row.reportNo ? '产品' : '订单' }}</span>
          </template>
        </el-table-column>
        <el-table-column label="审核状态" align="center">
          <template slot-scope="{row}">
            <el-tag v-if="row.declareResult == null" type="info" effect="plain">待申报</el-tag>
            <el-tag v-else-if="row.declareResult == '审核中'" type="warning" effect="plain">审核中</el-tag>
            <el-tag v-else-if="row.declareResult == '审核通过'" type="success" effect="plain">通过</el-tag>
            <el-tag v-else-if="row.declareResult == '审核拒绝'" type="danger" effect="plain">拒绝</el-tag>
          </template>
        </el-table-column>
        <el-table-column fixed="right" label="操作" align="center">
          <template slot-scope="{row}">
            <div>
              <el-button v-if="row.declareResult == null || row.declareResult == '审核拒绝'" type="text" size="small"
                @click="openDeclareDialog(row)">申报</el-button>
              <el-button v-if="row.declareResult == '审核中' || row.declareResult == '审核通过' || row.declareResult == '审核拒绝'"
                type="text" size="small" @click="checkDetail(row.id)">查看</el-button>
            </div>
          </template>
        </el-table-column>
      </parentTable>
    </y-page-list-layout>
    <el-dialog :visible.sync="declareDialog" width="45%" center custom-class="doWarehouseClass">
      <el-divider content-position="left">
        <span style="color:red;font-weight: bold;">
          【市场人员】
        </span>资产业务提成申报</el-divider>
      <el-form :model="declareForm" ref="declareForm" :rules="rules">
        <el-form-item label="订单号：" prop="orderId" label-width="140px" class="postInfo-container-item">
          <el-input v-model="declareForm.orderId" :step="1" :max="100" style="width: 100%;" readonly disabled></el-input>
        </el-form-item>
        <el-form-item label="项目名称：" prop="name" label-width="140px" class="postInfo-container-item">
          <el-input v-model="declareForm.name" :step="1" :max="100" style="width: 100%;" readonly disabled></el-input>
        </el-form-item>
        <el-form-item label="报告号：" prop="reportNo" label-width="140px" class="postInfo-container-item">
          <el-input v-model="declareForm.reportNo" :step="1" :max="100" style="width: 100%;" readonly disabled></el-input>
        </el-form-item>
        <el-form-item label="业务分类：" prop="commissionRateId" label-width="140px" class="postInfo-container-item">
          <el-select clearable v-model="declareForm.commissionRateId" placeholder="请选择业务分类" style="width: 100%;">
            <el-option v-for="(c, id) in cates" :value="c.id" :label="c.name">{{ c.name }}</el-option>
          </el-select>
        </el-form-item>
        <el-button type="success" round @click="addUser()">添加客户经理</el-button>
        <el-table :data="businessPerformanceDistributionForm.distributionList" border
          style="width: 100%; margin-top: 10px; margin-bottom: 10px;">
          <el-table-column prop="userId" label="客户经理" align="center">
            <template slot-scope="{row}">
              <el-select filterable clearable v-model="row.memberId" placeholder="选择人员">
                <el-option v-for="(u, id) in users" :value="u.id" :label="u.name">{{ u.name }}</el-option>
              </el-select>
            </template>
          </el-table-column>
          <el-table-column prop="rate" label="提成比例%" align="center">
            <template slot-scope="{row}">
              <el-input-number v-model="row.performanceDistribution" :step="1" :max="100"
                style="width: 100%;"></el-input-number>
            </template>
          </el-table-column>
          <el-table-column label="操作" align="center">
            <template slot-scope="row">
              <el-button type="text" @click="removeUser(row.$index)" :disabled="row.$index === 0">删除</el-button>
            </template>
          </el-table-column>
        </el-table>

        <el-form-item>
          <el-button type="primary" @click="declareMarket()" style="margin-left: 40%;">提交</el-button>
          <el-button @click="declareDialog = false">取消</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>
<script>
import YPageListLayout from '@/components/YPageListLayout'
import Breadcrumb from '@/components/Breadcrumb'


export default {
  name: 'declareAssetMarket',
  components: {
    Breadcrumb,
    YPageListLayout,
  },
  filters: {

  },
  data() {
    return {
      rules: {
        commissionRateId: [{
          required: true,
          message: '业务分类不能为空',
          trigger: 'blur'
        }],
      },
      pageData: { records: [] },
      listQuery: {
        page: 1,
        size: 10,
        descs: 'id',
      },
      cates: [],
      users: [],
      declareDialog: false,
      declareForm: {
        orderId: null,
        name: null,
        reportNo: null,
        businessId: null,
        productionId: null,
        businessType: 'COMMISSION_DECLARE_ASSET_MARKET',
        commissionRateId: null,
        userShareRates: [
          {
            memberId: this.$store.getters.userInfo.id,
            performanceDistribution: 100
          }
        ],
      },
      // 资产绩效比例表单
      businessPerformanceDistributionForm: {
        businessId: null,
        businessType: null,
        reportNo: null,
        userType: null,
        distributionList: [
          {
            memberId: this.$store.getters.userInfo.id,
            performanceDistribution: 100,
            memberType: "客户经理"
          }
        ]
      }
    }
  },
  created() {
    this.assetsMarketDeclarePage();
    this.getAssetMarketCate();
  },
  methods: {

    resetSearch() {
      this.$router.push({ query: {} });
      this.orderDate = '';
      this.listQuery = {
        current: 1,
        size: 10,
        descs: 'id',
      }
      this.assetsMarketDeclarePage()
    },

    searchList() {
      this.listQuery.current = 1;
      this.assetsMarketDeclarePage();
    },

    assetsMarketDeclarePage() {
      this.$api.commissonDeclare.assetsMarketDeclarePage(this.listQuery).then(res => {
        if (res.code === 200) {
          this.pageData = res.data;
        }
      })
    },
    getAssetMarketCate() {
      this.$api.businessCommissionRate.cateList("ASSET_BUSINESS", "MANAGER").then(res => {
        if (res.code === 200) {
          this.cates = res.data;
        }
      })
    },
    openDeclareDialog(row) {
      if (row.delivery) {
        this.getSimpleAllUser();
        this.declareDialog = true;
        this.declareForm.orderId = row.orderId;
        this.declareForm.name = row.orderName;
        this.declareForm.reportNo = row.reportNo;
        this.declareForm.businessId = row.businessId;
        this.declareForm.productionId = row.productionId;
        this.businessPerformanceDistributionForm.businessId = row.businessId;
        this.businessPerformanceDistributionForm.businessType = 'ASSET_BUSINESS';
        this.businessPerformanceDistributionForm.reportNo = row.reportNo;
        // 人员类型（市场人员/评估人员）
        this.businessPerformanceDistributionForm.userType = '市场人员';
      } else {
        this.$notify({
          title: '失败',
          message: '产品需先标记送达',
          type: 'error',
          duration: 2000
        });
      }

    },
    declareMarket() {
      this.$refs.declareForm.validate(valid => {
        if (valid) {
          for (let i in this.businessPerformanceDistributionForm.distributionList) {
            if (!this.businessPerformanceDistributionForm.distributionList[i].memberId) {
              this.$message.error("客户经理未指定");
              return;
            }
            if (!this.businessPerformanceDistributionForm.distributionList[i].performanceDistribution) {
              this.$message.error("设置比例不能为空");
              return;
            }
          }
          const sum = this.businessPerformanceDistributionForm.distributionList.reduce((accumulator, obj) => {
            return accumulator + obj.performanceDistribution;
          }, 0);
          if (sum === 100) {
            this.declareForm.distributionDto = this.businessPerformanceDistributionForm;
            // console.log(this.declareForm);
            this.$api.commissonDeclare.assetsDeclare(this.declareForm).then(res => {
              if (res.code === 200) {
                this.$notify({
                  title: '成功',
                  message: '提成申报已提交',
                  type: 'success',
                  duration: 2000
                });
                this.assetsMarketDeclarePage();
                this.declareDialog = false;
              } else {
                this.$notify({
                  title: '失败',
                  message: '提成申报提交失败，请联系管理员',
                  type: 'error',
                  duration: 2000
                });
              }
            })
          } else {
            this.$notify({
              title: '警告',
              message: '所有人员分配的绩效之和必须等于100%！',
              type: 'error',
              duration: 3000
            });
          }
        }
      })
    },

    getSimpleAllUser() {
      this.$api.user.simpleAll().then(res => {
        if (res.code === 200) {
          this.users = res.data;
        }
      })
    },
    addUser() {
      if (this.businessPerformanceDistributionForm.distributionList.length > 4) {
        this.$message.error("人数超限")
        return;
      }
      let user = new Object;
      user.memberType = "客户经理"
      user.index
      this.businessPerformanceDistributionForm.distributionList.push(user);
    },
    removeUser(index) {
      this.businessPerformanceDistributionForm.distributionList.splice(index, 1)
    },
    checkDetail(id) {
      this.$router.push(`/income/assets/declare/detail?businessId=${id}&businessType=COMMISSION_DECLARE_ASSET_MARKET&doWorkflow=${false}&back=${'/income/assets/declare/market'}`)
    }

  },

}
</script>
<style lang="scss" scoped>
/deep/.doWarehouseClass {
  border-radius: 10px;
}

.real-amount {
  /deep/ .el-form-item__label {
    color: red;
    font-weight: bold
  }
}
</style>
