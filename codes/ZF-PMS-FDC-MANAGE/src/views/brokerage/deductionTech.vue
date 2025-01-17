<template>
    <div class="app-container">
        <y-data-view-page-layout>
            <y-data-view-module>
                <div>
                    <el-divider content-position="center">
                        <y-d-v-over-view style="width: 220px" title="提成奖金池" :typeId="1"
                            :data="[{ text: stat.poolResidueAmount, value: '元' }]"></y-d-v-over-view>
                        <y-d-v-over-view style="width: 220px" title="每月默认预提" :typeId="5"
                            :data="[{ text: stat.defaultBrokerageAmount === null ? 0.00 : stat.defaultBrokerageAmount, value: '元' }]"></y-d-v-over-view>
                    </el-divider>
                </div>
            </y-data-view-module>
        </y-data-view-page-layout>
        <el-tabs v-model="activeName" type="border-card" style="margin-top: 30px;">
            <el-tab-pane label="默认预提抵扣详情" name="first">
                <y-page-list-layout :page-list="pageData" :page-para="listQuery" :get-page-list="getList">
                    <template slot="left">
                        <el-select v-model="listQuery.year" placeholder="结算年份" clearable filterable
                            style="margin-left: 20px;width: 100px;float: left;" class="filter-item">
                            <el-option label="2022" value="2022" />
                            <el-option label="2023" value="2023" />
                            <el-option label="2024" value="2024" />
                            <el-option label="2025" value="2025" />
                            <el-option label="2026" value="2026" />
                            <el-option label="2027" value="2027" />
                            <el-option label="2028" value="2028" />
                            <el-option label="2029" value="2029" />
                            <el-option label="2030" value="2030" />
                        </el-select>
                        <el-select v-model="listQuery.month" placeholder="结算月份" clearable filterable
                            style="margin-left: 20px;width: 100px;float: left;" class="filter-item">
                            <el-option label="1" value="1" />
                            <el-option label="2" value="2" />
                            <el-option label="3" value="3" />
                            <el-option label="4" value="4" />
                            <el-option label="5" value="5" />
                            <el-option label="6" value="6" />
                            <el-option label="7" value="7" />
                            <el-option label="8" value="8" />
                            <el-option label="9" value="9" />
                            <el-option label="10" value="10" />
                            <el-option label="11" value="11" />
                            <el-option label="12" value="12" />
                        </el-select>
                        <el-button class="filter-item" style="margin-left: 10px;float: left;" type="primary"
                            @click="getList" round>搜索
                        </el-button>
                        <el-button class="filter-item" style="float: left;" round type="warning" @click="resetSearch()">重置
                        </el-button>
                    </template>
                    <parentTable v-loading="listLoading" :data="pageData.records" slot="table" style="width: 100%;">
                        <el-table-column type="expand">
                            <template slot-scope="{row}">
                                <parentTable style="font-size: 12px;color: #8c939d" v-loading="listLoading" inline
                                    :data="row.deductionRecords" v-if="row.deductionRecords.length > 0">
                                    <el-table-column align="center" width=285>
                                        <el-tag style="margin-left: 100px;">抵扣项目</el-tag>
                                    </el-table-column>
                                    <el-table-column label="项目名称" align="center">
                                        <template slot-scope="{row}">
                                            <span>{{ row.itemName }}</span>
                                        </template>
                                    </el-table-column>
                                    <el-table-column label="转入年份" align="center">
                                        <template slot-scope="{row}">
                                            <span>{{ row.year }}</span>
                                        </template>
                                    </el-table-column>
                                    <el-table-column label="转入月份" align="center">
                                        <template slot-scope="{row}">
                                            <span>{{ row.month }}</span>
                                        </template>
                                    </el-table-column>
                                    <el-table-column label="转入金额" align="center">
                                        <template slot-scope="{row}">
                                            <span>{{ row.settleAmount }}</span>
                                        </template>
                                    </el-table-column>
                                    <el-table-column label="结算类型" align="center">
                                        <template slot-scope="{row}">
                                            <span>{{ settleCate(row.settleCate) }}</span>
                                        </template>
                                    </el-table-column>
                                    <el-table-column label="转入时间" align="center">
                                        <template slot-scope="{row}">
                                            <span>{{ row.created }}</span>
                                        </template>
                                    </el-table-column>
                                    <el-table-column label="已抵扣金额(元)" align="center">
                                        <template slot-scope="{row}">
                                            <el-tag type="success">{{ row.deductionAmount }}</el-tag>
                                        </template>
                                    </el-table-column>
                                </parentTable>
                                <parentTable style="font-size: 5px;color: #8c939d" v-loading="listLoading" inline
                                    :data="row.deductionRecords" v-else>
                                </parentTable>
                            </template>
                        </el-table-column>
                        <el-table-column label="人员名称" align="center">
                            <template slot-scope="{row}">
                                <span>{{ row.userName }}</span>
                            </template>
                        </el-table-column>
                        <el-table-column label="结算年份" align="center">
                            <template slot-scope="{row}">
                                <span>{{ row.year }}</span>
                            </template>
                        </el-table-column>
                        <el-table-column label="结算月份" align="center">
                            <template slot-scope="{row}">
                                <span>{{ row.month }}</span>
                            </template>
                        </el-table-column>
                        <el-table-column label="结算类型" align="center">
                            <template slot-scope="{row}">
                                <span>{{ settleCate(row.cate) }}</span>
                            </template>
                        </el-table-column>
                        <el-table-column label="已发金额(元)" align="center">
                            <template slot-scope="{row}">
                                <el-tag type="success">{{ row.brokerageAmount }}</el-tag>
                            </template>
                        </el-table-column>
                        <el-table-column label="结算时间" align="center">
                            <template slot-scope="{row}">
                                <span>{{ row.created }}</span>
                            </template>
                        </el-table-column>
                        <el-table-column label="未抵扣金额" align="center">
                            <template slot-scope="{row}">
                                <el-tag type="error" style="color:red">{{ row.notDeductionAmount }}</el-tag>
                            </template>
                        </el-table-column>
                    </parentTable>
                </y-page-list-layout>
            </el-tab-pane>
            <el-tab-pane label="转入奖金池的项目提成抵扣详情">
                <y-page-list-layout :page-list="pageData1" :page-para="listQuery1" :get-page-list="getItemDeductionList">
                    <template slot="left">
                        <el-input v-model="listQuery1.itemName" placeholder="项目名称" clearable
                            style="margin-left: 20px;width: 320px;float: left;">
                        </el-input>
                        <el-button class="filter-item" style="margin-left: 10px;float: left;" type="primary"
                            @click="getItemDeductionList" round>搜索
                        </el-button>
                        <el-button class="filter-item" style="float: left;" round type="warning" @click="resetSearch1()">重置
                        </el-button>
                    </template>
                    <parentTable v-loading="listLoading1" :data="pageData1.records" slot="table" style="width: 100%;">
                        <el-table-column type="expand">
                            <template slot-scope="{row}">
                                <parentTable style="font-size: 12px;color: #8c939d" v-loading="listLoading1" inline
                                    :data="row.defaultDeductions" v-if="row.defaultDeductions.length > 0">
                                    <el-table-column align="center" width="285">
                                        <template slot-scope="{row}">
                                            <el-tag style="margin-left: 100px;">{{row.cate ==='DEFAULT_AHEAD'?'抵扣预提':'一次性结算'}}</el-tag>
                                        </template>
                                    </el-table-column>
                                    <el-table-column label="结算年份" align="center">
                                        <template slot-scope="{row}">
                                            <span>{{ row.year }}</span>
                                        </template>
                                    </el-table-column>
                                    <el-table-column label="结算月份" align="center">
                                        <template slot-scope="{row}">
                                            <span>{{ row.month }}</span>
                                        </template>
                                    </el-table-column>
                                    <el-table-column label="结算金额" align="center">
                                        <template slot-scope="{row}">
                                            <el-tag type="error">{{ row.brokerageAmount }}</el-tag>
                                        </template>
                                    </el-table-column>
                                    <el-table-column label="结算类型" align="center">
                                        <template slot-scope="{row}">
                                            <span>{{ settleCate(row.cate) }}</span>
                                        </template>
                                    </el-table-column>
                                    <el-table-column label="结算时间" align="center">
                                        <template slot-scope="{row}">
                                            <span>{{ row.created }}</span>
                                        </template>
                                    </el-table-column>
                                </parentTable>
                                <parentTable style="font-size: 5px;color: #8c939d" v-loading="listLoading1" inline
                                    :data="row.deductionRecords" v-else>
                                </parentTable>
                            </template>
                        </el-table-column>
                        <el-table-column label="人员名称" align="center">
                            <template slot-scope="{row}">
                                <span>{{ row.userName }}</span>
                            </template>
                        </el-table-column>
                        <el-table-column label="项目名称" align="center">
                            <template slot-scope="{row}">
                                <span>{{ row.itemName }}</span>
                            </template>
                        </el-table-column>
                        <el-table-column label="转入奖金池金额" align="center">
                            <template slot-scope="{row}">
                                <el-tag>{{ row.brokerageAmount }}</el-tag>
                            </template>
                        </el-table-column>
                        <el-table-column label="项目剩余金额" align="center">
                            <template slot-scope="{row}">
                                <el-tag type="success">{{ row.residueAmount }}</el-tag>
                            </template>
                        </el-table-column>
                        <el-table-column label="项目剩余比重" align="center">
                            <template slot-scope="{row}">
                                <el-progress type="circle"
                                    :percentage="Math.floor((row.residueAmount / row.brokerageAmount) * 100) / 100 * 100"
                                    :width=50 :stroke-width=3 :color="colors"></el-progress>
                            </template>
                        </el-table-column>
                        <el-table-column label="转入奖金池时间" align="center">
                            <template slot-scope="{row}">
                                <span>{{ row.created }}</span>
                            </template>
                        </el-table-column>
                        <el-table-column label="备注" align="center">
                            <template slot-scope="{row}">
                                <el-tag type="success">{{ row.embody }}</el-tag>
                            </template>
                        </el-table-column>
                    </parentTable>
                </y-page-list-layout>
            </el-tab-pane>
            <el-tab-pane label="(全额回款）一次性发放项目">
                <y-page-list-layout :page-list="pageData2" :page-para="listQuery2" :get-page-list="getOnceSettle">
                    <template slot="left">
                        <el-input v-model="listQuery2.itemName" placeholder="项目名称" clearable
                            style="margin-left: 20px;width: 320px;float: left;">
                        </el-input>
                        <el-button class="filter-item" style="margin-left: 10px;float: left;" type="primary"
                            @click="getOnceSettle" round>搜索
                        </el-button>
                        <el-button class="filter-item" style="float: left;" round type="warning" @click="resetSearch2()">重置
                        </el-button>
                    </template>
                    <parentTable v-loading="listLoading2" :data="pageData2.records" slot="table" style="width: 100%;">
                        <el-table-column label="人员名称" align="center">
                            <template slot-scope="{row}">
                                <span>{{ row.userName }}</span>
                            </template>
                        </el-table-column>
                        <el-table-column label="项目名称" align="center">
                            <template slot-scope="{row}">
                                <span>{{ row.itemName }}</span>
                            </template>
                        </el-table-column>
                        <el-table-column label="已发放金额" align="center">
                            <template slot-scope="{row}">
                                <el-tag>{{ row.onceAmount }}</el-tag>
                            </template>
                        </el-table-column>
                        <el-table-column label="剩余金额" align="center">
                            <el-tag type="success">0</el-tag>
                        </el-table-column>
                        <el-table-column label="发放时间" align="center">
                            <template slot-scope="{row}">
                                <span>{{ row.created }}</span>
                            </template>
                        </el-table-column>
                        <el-table-column label="备注" align="center">
                            <template slot-scope="{row}">
                               <span>此项目提成金额已从奖金池一次性发放</span>
                            </template>
                        </el-table-column>
                    </parentTable>
                </y-page-list-layout>
            </el-tab-pane>


        </el-tabs>


    </div>
</template>

<script>
import YDVOverView from '@/components/YDataViewPageLayout/YDVOverView'
import YDataViewModule from '@/components/YDataViewPageLayout/YDataViewModule'
import YDataViewPageLayout from '@/components/YDataViewPageLayout'
import YPageListLayout from '@/components/YPageListLayout'
export default {
    name: 'deductionTech',
    components: {
        YDVOverView,
        YDataViewModule,
        YDataViewPageLayout,
        YPageListLayout,
    },
    data() {
        return {
            activeName: 'first',
            listQuery: {
                page: 1,
                size: 10,
                descs: 'id',
            },
            listQuery1: {
                page: 1,
                size: 10,
                descs: 'id',
                itemName: null
            },
            listQuery2: {
                page: 1,
                size: 10,
                descs: 'id',
                itemName: null
            },
            pageData: { records: [] },
            pageData1: { records: [] },
            pageData2: { records: [] },
            total: 10,
            listLoading: true,
            listLoading1: true,
            listLoading2: true,
            colors: [
                { color: '#f56c6c', percentage: 20 },
                { color: '#e6a23c', percentage: 40 },
                { color: '#5cb87a', percentage: 60 },
                { color: '#1989fa', percentage: 80 },
                { color: '#008B00', percentage: 100 }
            ],
            stat: {
                poolResidueAmount: null,
                defaultBrokerageAmount: null
            }
        }
    },
    created() {
        this.getStat();
        this.getList();
        this.getItemDeductionList();
        this.getOnceSettle();
    },

    methods: {
        settleCate(val) {
            if (val === 'DEFAULT_AHEAD') {
                return '默认预提';
            }
            if (val === 'ONCE_SETTLE') {
                return '一次性结算';
            }
            if (val === 'POOL_SETTLE') {
                return '转入个人奖金池';
            }
        },
        getList() {
            const that = this;
            this.listLoading = true;
            this.$api.itemBrokerageTechnicistDeduction.list(Object.assign({}, that.listQuery)).then((res) => {
                that.pageData = res.data;
                setTimeout(() => {
                    that.listLoading = false
                }, 200)
            })
                .catch(() => {
                    that.listLoading = false
                })
        },
        getItemDeductionList() {
            const that = this;
            this.listLoading1 = true;
            this.$api.itemBrokerageTechnicist.list(Object.assign({}, that.listQuery1)).then((res) => {
                that.pageData1 = res.data;
                setTimeout(() => {
                    that.listLoading1 = false
                }, 200)
            })
                .catch(() => {
                    that.listLoading1 = false
                })
        },
        resetSearch1() {
            this.listQuery1 = {
                page: 1,
                size: 10,
                descs: 'id',
                itemName: null
            },
                this.getItemDeductionList();
        },
        resetSearch() {
            this.listQuery = {
                page: 1,
                size: 10,
                descs: 'id',
            },
                this.getList();
        },
        getStat() {
            this.$api.itemBrokerageTechnicistDeduction.stat().then(res => {
                this.stat = res.data;
            })
        },
        getOnceSettle(){
            const that = this;
            this.listLoading2 = true;
            this.$api.itemBrokerageTechnicist.onceList(Object.assign({}, that.listQuery2)).then((res) => {
                that.pageData2 = res.data;
                setTimeout(() => {
                    that.listLoading2 = false
                }, 200)
            })
                .catch(() => {
                    that.listLoading2 = false
                })
        }
    }
}
</script>
        