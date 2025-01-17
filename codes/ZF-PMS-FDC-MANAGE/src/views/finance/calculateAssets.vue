<template>
    <div class="app-container organization-index">
        <div class="title-container">
            <breadcrumb id="breadcrumb-container" class="breadcrumb-container" />
        </div>
        <el-tabs v-model="activeName" @tab-click="handleClick" type="border-card" style="margin-top: 10px;">
            <el-tab-pane label="评估人员" name="evaluate">
                <div style="width: 100%; height: 50px;">
                    <el-date-picker v-model="findDate" type="daterange" align="center" unlink-panels range-separator="至"
                        start-placeholder="统计日期（开始）" end-placeholder="统计日期（结束）" :picker-options="pickerOptions"
                        value-format="yyyy-MM-dd" style="float: left; width: 300px;">
                    </el-date-picker>
                    <el-input v-model="listQuery1.userName" placeholder="姓名" clearable
                        style="margin-left: 20px;width: 200px;float: left;">
                    </el-input>
                    <el-button class="filter-item" style="margin-left:20px;float: left;" type="primary"
                        @click="searchList1()" round>搜索
                    </el-button>
                    <el-button class="filter-item" style="float: left;" round type="success" @click="resetSearch1()">重置
                    </el-button>
                    <el-button class="filter-item" style="float: left;" round type="info"
                        @click="evaluatorTotalAmountExport()">导出合计
                    </el-button>
                    <el-button class="filter-item" style="float: left;" round type="info"
                        @click="evaluatorCurUserDetailExport()">导出当前员工提成详情
                    </el-button>
                    <el-button class="filter-item" style="float: left;" round type="info"
                        @click="evaluatorAllUserDetailExport()">导出所有员工提成详情
                    </el-button>
                </div>
                <div style="width: 100%;">
                    <el-table :data="evaluatorAmount" fit stripe highlight-current-row @row-click="evaluatorAmountDetail"
                        :header-row-style="{ color: '#333333', 'font-size': '14px' }" style=" width: 18.1%; float: left;
                    border-left: 1px solid #ebeced;
                    border-right: 1px solid #ebeced;
                    color: #333333; font-size: 14px;
                  ">
                        <el-table-column label="姓名" align="center">
                            <template slot-scope="{row}">
                                <span>{{ row.userName }}</span>
                            </template>
                        </el-table-column>
                        <el-table-column label="预计提成金额(元)" align="center">
                            <template slot-scope="{row}">
                                <span>{{ row.allCommissionTotalAmount }}</span>
                            </template>
                        </el-table-column>
                        <el-table-column label="归档提成金额(元)" align="center">
                            <template slot-scope="{row}">
                                <span>{{ row.commissionTotalAmount }}</span>
                            </template>
                        </el-table-column>
                    </el-table>
                    <el-table :data="evaluatorProd" fit stripe highlight-current-row
                        :header-row-style="{ color: '#333333', 'font-size': '14px' }" style=" width: 80.5%; float: right;
                      border-left: 1px solid #ebeced;
                      border-right: 1px solid #ebeced;
                      color: #333333; font-size: 14px;
                    ">
                        <el-table-column label="姓名" align="center">
                            <template slot-scope="{row}">
                                <span>{{ row.userName }}</span>
                            </template>
                        </el-table-column>
                        <el-table-column label="产品号" width="250" align="center">
                            <template slot-scope="{row}">
                                <el-tag type="success">{{ row.reportNo }}</el-tag>
                            </template>
                        </el-table-column>
                        <el-table-column label="项目名称" width="200" align="center" show-overflow-tooltip>
                            <template slot-scope="{row}">
                                <span>{{ row.name }}</span>
                            </template>
                        </el-table-column>
                        <el-table-column label="项目分类" width="100" align="center" show-overflow-tooltip>
                            <template slot-scope="{row}">
                                <span>{{ row.businessCate }}</span>
                            </template>
                        </el-table-column>
                        <el-table-column label="提成比例" align="center">
                            <template slot-scope="{row}">
                                <span>{{ row.ratio }}%</span>
                            </template>
                        </el-table-column>
                        <el-table-column label="总提成额(￥)" align="center">
                            <template slot-scope="{row}">
                                <span>{{ row.totalAmount }}</span>
                            </template>
                        </el-table-column>
                        <el-table-column label="分配比例" align="center">
                            <template slot-scope="{row}">
                                <span>{{ row.allotRatio }}%</span>
                            </template>
                        </el-table-column>
                        <el-table-column label="提成下限(元)" align="center">
                            <template slot-scope="{row}">
                                <span>{{ row.lowLimitAmount }}</span>
                            </template>
                        </el-table-column>
                        <el-table-column label="提成额(￥)" align="center">
                            <template slot-scope="{row}">
                                <span style="color:red">{{ row.commissionAmount }}</span>
                            </template>
                        </el-table-column>
                        <el-table-column label="出报告日期" align="center">
                            <template slot-scope="{row}">
                                <span>{{ row.created }}</span>
                            </template>
                        </el-table-column>
                        <el-table-column label="实收款(￥)" align="center">
                            <template slot-scope="{row}">
                                <span>{{ row.realAmount }}</span>
                            </template>
                        </el-table-column>
                        <el-table-column label="提成日期" width="100" align="center">
                            <template slot-scope="{row}">
                                <span>{{ row.settleDate }}</span>
                            </template>
                        </el-table-column>
                        <el-table-column label="实收款认领时间" width="130" align="center">
                            <template slot-scope="{row}">
                                <span>{{ row.realFundDateTime }}</span>
                            </template>
                        </el-table-column>
                    </el-table>
                </div>
            </el-tab-pane>
            <el-tab-pane label="市场人员" name="market">
                <div style="width: 100%; height: 50px;">
                    <el-date-picker v-model="findDate1" type="daterange" align="center" unlink-panels range-separator="至"
                        start-placeholder="统计日期（开始）" end-placeholder="统计日期（结束）" :picker-options="pickerOptions"
                        value-format="yyyy-MM-dd" style="float: left; width: 300px;">
                    </el-date-picker>
                    <el-input v-model="listQuery2.userName" placeholder="姓名" clearable
                        style="margin-left: 20px;width: 200px;float: left;">
                    </el-input>
                    <el-button class="filter-item" style="margin-left:20px;float: left;" type="primary"
                        @click="searchList2()" round>搜索
                    </el-button>
                    <el-button class="filter-item" style="float: left;" round type="success" @click="resetSearch2()">重置
                    </el-button>
                    <el-button class="filter-item" style="float: left;" round type="info"
                        @click="marketTotalAmountExport()">导出合计
                    </el-button>
                    <el-button class="filter-item" style="float: left;" round type="info"
                        @click="marketCurUserDetailExport()">导出当前员工提成详情
                    </el-button>
                    <el-button class="filter-item" style="float: left;" round type="info"
                        @click="marketAllUserDetailExport()">导出所有员工提成详情
                    </el-button>
                </div>
                <div style="width: 100%;">
                    <el-table :data="marketAmount" fit stripe highlight-current-row @row-click="marketAmountDetail"
                        :header-row-style="{ color: '#333333', 'font-size': '14px' }" style=" width: 18.1%; float: left;
                    border-left: 1px solid #ebeced;
                    border-right: 1px solid #ebeced;
                    color: #333333; font-size: 14px;
                  ">
                        <el-table-column label="姓名" align="center">
                            <template slot-scope="{row}">
                                <span>{{ row.userName }}</span>
                            </template>
                        </el-table-column>
                        <el-table-column label="提成金额(元)" align="center">
                            <template slot-scope="{row}">
                                <span>{{ row.commissionTotalAmount }}</span>
                            </template>
                        </el-table-column>
                    </el-table>
                    <el-table :data="marketProd" fit stripe highlight-current-row
                        :header-row-style="{ color: '#333333', 'font-size': '14px' }" style=" width: 80.5%; float: right;
                      border-left: 1px solid #ebeced;
                      border-right: 1px solid #ebeced;
                      color: #333333; font-size: 14px;
                    ">
                        <el-table-column label="姓名" align="center">
                            <template slot-scope="{row}">
                                <span>{{ row.userName }}</span>
                            </template>
                        </el-table-column>
                        <el-table-column label="产品号" width="250" align="center">
                            <template slot-scope="{row}">
                                <el-tag type="success">{{ row.reportNo }}</el-tag>
                            </template>
                        </el-table-column>
                        <el-table-column label="项目名称" width="200" align="center" show-overflow-tooltip>
                            <template slot-scope="{row}">
                                <span>{{ row.name }}</span>
                            </template>
                        </el-table-column>
                        <el-table-column label="项目分类" width="100" align="center" show-overflow-tooltip>
                            <template slot-scope="{row}">
                                <span>{{ row.businessCate }}</span>
                            </template>
                        </el-table-column>
                        <el-table-column label="提成比例" align="center">
                            <template slot-scope="{row}">
                                <span>{{ row.ratio }}%</span>
                            </template>
                        </el-table-column>
                        <el-table-column label="总提成额(￥)" align="center">
                            <template slot-scope="{row}">
                                <span>{{ row.totalAmount }}</span>
                            </template>
                        </el-table-column>
                        <el-table-column label="分配比例" align="center">
                            <template slot-scope="{row}">
                                <span>{{ row.allotRatio }}%</span>
                            </template>
                        </el-table-column>
                        <el-table-column label="提成额(￥)" align="center">
                            <template slot-scope="{row}">
                                <span style="color:red">{{ row.commissionAmount }}</span>
                            </template>
                        </el-table-column>
                        <el-table-column label="出报告日期" align="center">
                            <template slot-scope="{row}">
                                <span>{{ row.created }}</span>
                            </template>
                        </el-table-column>
                        <el-table-column label="实收款(￥)" align="center">
                            <template slot-scope="{row}">
                                <span>{{ row.realAmount }}</span>
                            </template>
                        </el-table-column>
                        <el-table-column label="提成日期" width="100" align="center">
                            <template slot-scope="{row}">
                                <span>{{ row.settleDate }}</span>
                            </template>
                        </el-table-column>
                    </el-table>
                </div>
            </el-tab-pane>
        </el-tabs>
    </div>
</template>
<script>
import Breadcrumb from '@/components/Breadcrumb'
import YPageListLayout from '@/components/YPageListLayout'

export default {
    name: 'calculateAssets',
    components: {
        Breadcrumb,
        YPageListLayout
    },

    data() {
        return {
            activeName: 'evaluate',
            listQuery1: {
                page: 1,
                size: 10,
                descs: 'id',
                startDate: '2024-05-01 00:00:00',
                endDate: '2024-06-01 00:00:00',
                userName: null
            },
            listQuery2: {
                page: 1,
                size: 10,
                descs: 'id',
                startDate: '2024-05-01 00:00:00',
                endDate: '2024-06-01 00:00:00',
                userName: null
            },
            pickerOptions: {
                shortcuts: [{
                    text: '最近一周',
                    onClick(picker) {
                        const end = new Date();
                        const start = new Date();
                        start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
                        picker.$emit('pick', [start, end]);
                    }
                }, {
                    text: '最近一个月',
                    onClick(picker) {
                        const end = new Date();
                        const start = new Date();
                        start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
                        picker.$emit('pick', [start, end]);
                    }
                }, {
                    text: '最近三个月',
                    onClick(picker) {
                        const end = new Date();
                        const start = new Date();
                        start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
                        picker.$emit('pick', [start, end]);
                    }
                }]
            },
            findDate: [],
            findDate1: [],
            evaluatorAmount: [],
            evaluatorProd: [],
            marketAmount: [],
            marketProd: [],
        }
    },
    created() {
        this.findDate.push(this.getDefaultStartDate());
        this.findDate.push(this.getDefaultEndDate());
        this.findDate1.push(this.getDefaultStartDate());
        this.findDate1.push(this.getDefaultEndDate());
        this.assetsEvaluatorAmount();
    },
    methods: {
        getDefaultStartDate() {
            const currentDate = new Date();
            const currentYear = currentDate.getFullYear();
            var currentMonth = String(currentDate.getMonth()).padStart(2, "0");
            return currentYear + '-' + currentMonth + '-' + '23'
        },
        getDefaultEndDate() {
            const currentDate = new Date();
            const currentYear = currentDate.getFullYear();
            var currentMonth = String(currentDate.getMonth() + 1).padStart(2, "0");
            return currentYear + '-' + currentMonth + '-' + '22'
        },
        handleClick(tab, event) {
            if (tab.name === 'market') {
                this.assetsMarketAmount();
            }
            if (tab.name === 'evaluate') {
                this.assetsEvaluatorAmount()
            }
        },
        resetSearch1() {
            this.$router.push({ query: {} });
            this.listQuery1 = {
                current: 1,
                size: 10,
                descs: 'id',
                userName: null,
            }
            this.searchList1()
        },
        searchList1() {
            // 重置分页
            this.listQuery1.page = 1
            this.listQuery1.size = 10
            if (this.findDate) {
                this.listQuery1.startDate = this.findDate[0] + ' 00:00:00';
                this.listQuery1.endDate = this.findDate[1] + ' 23:59:59';
            }
            this.assetsEvaluatorAmount()
        },
        resetSearch2() {
            this.$router.push({ query: {} });
            this.listQuery2 = {
                current: 1,
                size: 10,
                descs: 'id',
                userName: null,
            }
            this.searchList2()
        },
        searchList2() {
            // 重置分页
            this.listQuery2.page = 1
            this.listQuery2.size = 10
            if (this.findDate1) {
                this.listQuery2.startDate = this.findDate1[0] + ' 00:00:00';
                this.listQuery2.endDate = this.findDate1[1] + ' 23:59:59';
            }
            this.assetsMarketAmount()
        },
        assetsEvaluatorAmount() {
            if (this.findDate) {
                this.listQuery1.startDate = this.findDate[0] + ' 00:00:00';
                this.listQuery1.endDate = this.findDate[1] + ' 23:59:59';
            }
            this.$api.financeRealFund.assetsEvaluatorCommission(this.listQuery1).then(res => {
                if (res.code === 200) {
                    this.evaluatorAmount = res.data;
                    if (this.evaluatorAmount.length > 0) {
                        this.listQuery1.userId = this.evaluatorAmount[0].id;
                        this.$api.financeRealFund.assetsEvaluatorCommissionDetail(this.listQuery1).then(res => {
                            if (res.code === 200) {
                                this.evaluatorProd = res.data;
                            }
                        })
                    } else {
                        this.evaluatorProd = []
                    }
                }
            })
        },
        evaluatorAmountDetail(row, column, event) {
            this.listQuery1.userId = row.id;
            this.$api.financeRealFund.assetsEvaluatorCommissionDetail(this.listQuery1).then(res => {
                if (res.code === 200) {
                    this.evaluatorProd = res.data;
                }
            })
        },
        assetsMarketAmount() {
            if (this.findDate1) {
                this.listQuery2.startDate = this.findDate1[0] + ' 00:00:00';
                this.listQuery2.endDate = this.findDate1[1] + ' 23:59:59';
            }
            this.$api.financeRealFund.assetsMarketCommission(this.listQuery2).then(res => {
                if (res.code === 200) {
                    this.marketAmount = res.data;
                    if (this.marketAmount.length > 0) {
                        this.listQuery2.userId = this.marketAmount[0].id;
                        this.$api.financeRealFund.assetsMarketCommissionDetail(this.listQuery2).then(res => {
                            if (res.code === 200) {
                                this.marketProd = res.data;
                            }
                        })
                    } else {
                        this.marketProd = []
                    }
                }
            })
        },
        marketAmountDetail(row, column, event) {
            this.listQuery2.userId = row.id;
            this.$api.financeRealFund.assetsMarketCommissionDetail(this.listQuery2).then(res => {
                if (res.code === 200) {
                    this.marketProd = res.data;
                }
            })
        },
        evaluatorTotalAmountExport() {
            this.$utils.exportUtil(
                "/financeRealFund/assets/evaluator/commission/total/export", this.listQuery1,
                "导出"
            );
        },
        evaluatorCurUserDetailExport() {
            this.$utils.exportUtil(
                "/financeRealFund/assets/evaluator/commission/current/export", this.listQuery1,
                "导出"
            );
        },
        evaluatorAllUserDetailExport() {
            this.$utils.exportUtil(
                "/financeRealFund/assets/evaluator/commission/all/export", this.listQuery1,
                "导出"
            );
        },
        marketTotalAmountExport() {
            this.$utils.exportUtil(
                "/financeRealFund/assets/market/commission/total/export", this.listQuery2,
                "导出"
            );
        },
        marketCurUserDetailExport() {
            this.$utils.exportUtil(
                "/financeRealFund/assets/market/commission/current/export", this.listQuery2,
                "导出"
            );
        },
        marketAllUserDetailExport() {
            this.$utils.exportUtil(
                "/financeRealFund/assets/market/commission/all/export", this.listQuery2,
                "导出"
            );
        }
    }
}
</script>
<style scoped lang="scss"></style>
  