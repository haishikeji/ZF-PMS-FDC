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
                <!-- <el-select clearable v-model="listQuery.declareType" placeholder="申报类型"
                    style="margin-left: 20px;width: 200px;float: left;">
                    <el-option :value="true" label="产品">产品</el-option>
                    <el-option :value="false" label="订单">订单</el-option>
                </el-select> -->
                <el-button class="filter-item" style="margin-left:20px;float: left;" type="primary" @click="searchList"
                    round>搜索
                </el-button>
                <el-button class="filter-item" style="float: left;" round type="success" @click="resetSearch()">重置
                </el-button>
            </template>
            <parentTable :data="pageData.records" slot="table" style="width: 100%;">
                <el-table-column label="订单号" width="150" align="center">
                    <template slot-scope="{row}">
                        <span>{{ row.assetsSimple.orderId }}</span>
                    </template>
                </el-table-column>
                <el-table-column label="项目名称" width="150" align="center">
                    <template slot-scope="{row}">
                        <span>{{ row.assetsSimple.name ? row.assetsSimple.name : row.assetsSimple.orderName }}</span>
                    </template>
                </el-table-column>
                <el-table-column label="报告号" width="200" align="center">
                    <template slot-scope="{row}">
                        <span>{{ row.assetsSimple.reportNo ? row.assetsSimple.reportNo : '--' }}</span>
                    </template>
                </el-table-column>
                <el-table-column label="申报人" align="center">
                    <template slot-scope="{row}">
                        <span>{{ row.declareUser }}</span>
                    </template>
                </el-table-column>
                <el-table-column label="审核人" align="center">
                    <template slot-scope="{row}">
                        <span>{{ row.handler }}</span>
                    </template>
                </el-table-column>
                <el-table-column label="人员类型" align="center">
                    <template slot-scope="{row}">
                        <span>{{ row.userType }}</span>
                    </template>
                </el-table-column>
                <el-table-column label="提成类型" width="150" align="center">
                    <template slot-scope="{row}">
                        <span>{{ row.businessCate }}</span>
                    </template>
                </el-table-column>
                <el-table-column label="提成比例" width="150" align="center">
                    <template slot-scope="{row}">
                        <span>{{ row.ratio }}%</span>
                    </template>
                </el-table-column>
                <el-table-column label="申报类型" align="center">
                    <template slot-scope="{row}">
                        <span style="font-weight: bold;">{{ row.prodId ? '产品' : '订单' }}</span>
                    </template>
                </el-table-column>
                <el-table-column label="申报时间" align="center" width="160">
                    <template slot-scope="{row}">
                        <span>{{ row.created }}</span>
                    </template>
                </el-table-column>
                <el-table-column label="当前节点" align="center">
                    <template slot-scope="{row}">
                        <span>{{ row.nodeName }}</span>
                    </template>
                </el-table-column>
                <el-table-column fixed="right" label="操作" align="center">
                    <template slot-scope="{row}">
                        <div>
                            <el-button type="text" size="small" @click="checkDetail(row)">审核</el-button>
                        </div>
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
    name: 'declareAssetsCheckList',
    components: {
        Breadcrumb,
        YPageListLayout,
    },
    filters: {

    },
    data() {
        return {
            pageData: { records: [] },
            listQuery: {
                page: 1,
                size: 20,
                descs: 'id',
            },
            cates: [],
        }
    },
    created() {
        this.getAssetsEvaluateCate();
        this.getList();
    },
    methods: {

        resetSearch() {
            this.$router.push({ query: {} });
            this.orderDate = '';
            this.listQuery = {
                current: 1,
                size: 20,
                descs: 'id',
            }
            this.getList()
        },

        searchList() {
            this.listQuery.current = 1;
            this.getList();
        },

        getList() {
            this.$api.commissonDeclare.assetsCheckPage(this.listQuery).then(res => {
                if (res.code === 200) {
                    this.pageData = res.data;
                }
            })
        },
        getAssetsEvaluateCate() {
            this.$api.businessCommissionRate.cateList("ASSET_BUSINESS", "EVALUATOR").then(res => {
                if (res.code === 200) {
                    this.cates = res.data;
                }
            })
        },
        checkDetail(row) {
            let backPath = '/administration/assets/declare/check/list';
            this.$router.push(`/administration/assets/declare/check?businessId=${row.businessId}&businessType=${row.businessType}&doWorkflow=${true}&back=${backPath}&couldEdit=${true}`)
        }
    }

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
  