<template>
  <div class="app-container">
    <div class="title-container">
      <breadcrumb id="breadcrumb-container" class="breadcrumb-container" />
    </div>
    <y-page-list-layout :page-list="pageData" :page-para="listQuery" :get-page-list="getList">
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
        <el-button class="filter-item" style="margin-left:20px;float: left;" type="primary" @click="searchList" round>搜索
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
        <el-table-column label="项目名称" width="160" align="center" show-overflow-tooltip>
          <template slot-scope="{row}">
            <span>{{ row.name ? row.name : row.orderName }}</span>
          </template>
        </el-table-column>
        <el-table-column label="报告号" width="200" align="center">
          <template slot-scope="{row}">
            <span>{{ row.reportNo ? row.reportNo : '--' }}</span>
          </template>
        </el-table-column>
        <el-table-column label="对象类别" align="center">
          <template slot-scope="{row}">
            <span>{{ row.businessObjectType }}</span>
          </template>
        </el-table-column>
        <el-table-column label="委托人" align="center">
          <template slot-scope="{row}">
            <span>{{ row.bailor }}</span>
          </template>
        </el-table-column>
        <el-table-column label="联系人" align="center">
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
        <el-table-column label="下单时间" width="110" align="center">
          <template slot-scope="{row}">
            <span>{{ row.orderCreated }}</span>
          </template>
        </el-table-column>
        <el-table-column label="提成类型" width="150" align="center" show-overflow-tooltip>
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
            <el-tag  v-if="row.declareResult == null" type="info" effect="plain">待申报</el-tag>
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
              <el-button v-if="row.declareResult == '审核中' || row.declareResult == '审核通过' || row.declareResult == '审核拒绝'" type="text" size="small"
                @click="checkDetail(row.cid)">查看</el-button>
            </div>
          </template>
        </el-table-column>
      </parentTable>
    </y-page-list-layout>
    <el-dialog :visible.sync="declareDialog" width="45%" center custom-class="doWarehouseClass">
      <el-divider content-position="left">
        <span style="color:red;font-weight: bold;">
          【市场人员】
        </span>大中型业务提成申报</el-divider>
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
        <el-table :data="declareForm.userShareRates" border style="width: 100%; margin-top: 10px; margin-bottom: 10px;">
          <el-table-column prop="userId" label="客户经理" align="center">
            <template slot-scope="{row}">
              <el-select filterable clearable v-model="row.userId" placeholder="选择人员">
                <el-option v-for="(u, id) in users" :value="u.id" :label="u.name">{{ u.name }}</el-option>
              </el-select>
            </template>
          </el-table-column>
          <el-table-column prop="rate" label="提成比例%" align="center">
            <template slot-scope="{row}">
              <el-input-number v-model="row.rate" :step="1" :max="100" style="width: 100%;"></el-input-number>
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
  name: 'declareMajorMarket',
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
        businessType: 'COMMISSION_DECLARE_MAJOR_MARKET',
        commissionRateId: null,
        userShareRates: [
          {
            userId: this.$store.getters.userInfo.id,
            rate: 100
          }
        ],
      },
    }
  },
  created() {
    this.getList();
    this.getMajorMarketCate();
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
      this.getList()
    },

    searchList() {
      this.listQuery.current = 1;
      this.getList();
    },

    getList() {
      this.$api.commissonDeclare.majorMarketDeclarePage(this.listQuery).then(res => {
        if (res.code === 200) {
          this.pageData = res.data;
        }
      })
    },
    getMajorMarketCate() {
      this.$api.businessCommissionRate.cateList("MAJOR_BUSINESS", "MARKETER").then(res => {
        if (res.code === 200) {
          this.cates = res.data;
        }
      })
    },
    openDeclareDialog(row) {
      this.getSimpleAllUser();
      this.declareDialog = true;
      this.declareForm.orderId = row.orderId;
      this.declareForm.name = row.name==null?row.orderName:row.name;
      this.declareForm.reportNo = row.reportNo;
      this.declareForm.businessId = row.businessId;
      this.declareForm.productionId = row.productionId;
    },
    declareMarket() {
      this.$refs.declareForm.validate(valid => {
        if (valid) {
          let total = 0;
          for (let i in this.declareForm.userShareRates) {
            if (!this.declareForm.userShareRates[i].userId) {
              this.$message.error("客户经理未指定");
              return;
            }
            if (!this.declareForm.userShareRates[i].rate) {
              this.$message.error("设置比例不能为空");
              return;
            }
          }
          for (let i in this.declareForm.userShareRates) {
            this.declareForm.userShareRates[i].rate = (this.declareForm.userShareRates[i].rate) / 100;
            total += this.declareForm.userShareRates[i].rate;
          }
          if (total != 1) {
            for (let i in this.declareForm.userShareRates) {
              this.declareForm.userShareRates[i].rate = (this.declareForm.userShareRates[i].rate) * 100;
            }
            this.$message.error("提成比例之和必须为100%");
            return;
          }
          this.$api.commissonDeclare.declare(this.declareForm).then(res => {
            if (res.code === 200) {
              this.$notify({
                title: '成功',
                message: '提成申报已提交',
                type: 'success',
                duration: 2000
              });
              this.getList();
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
      if (this.declareForm.userShareRates.length > 4) {
        this.$message.error("人数超限")
        return;
      }
      let user = new Object;
      user.index
      this.declareForm.userShareRates.push(user);
    },
    removeUser(index) {
      this.declareForm.userShareRates.splice(index, 1)
    },
    checkDetail(cid) {
      this.$router.push(`/income/major/market/detail?businessId=${cid}&businessType=COMMISSION_DECLARE_MAJOR_MARKET&doWorkflow=${false}&back=${'/income/major/declare/market'}`)
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
