<template>
    <div class="app-container organization-index">
        <div class="title-container">
            <breadcrumb id="breadcrumb-container" class="breadcrumb-container" />
        </div>
        <div>
            <y-page-list-layout :get-page-list="getAssetsLedger" :page-list="pageData" :page-para="listQuery">
                <template slot="left">
                    <el-date-picker style="margin-right: 20px;float: left;" v-model="selectDate" type="daterange"
                        :picker-options="pickerOptions" format="yyyy 年 MM 月 dd 日" value-format="yyyy-MM-dd"
                        range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期" align="right">
                    </el-date-picker>
                    <el-input v-model="listQuery.keyWord" placeholder="项目编号/项目名称" clearable
                        style="width: 200px;margin-right: 20px;float: left;">
                    </el-input>
                    <el-select v-model="listQuery.principalId" placeholder="项目负责人" clearable
                        style="width: 200px;margin-right: 20px;float: left;">
                        <el-option v-for="(u, id) in allUsers" :label="u.name" :value="u.id"></el-option>
                    </el-select>
                    <el-select v-model="listQuery.clientManagerId" filterable placeholder="客户经理（可搜索）"
                        style=" width: 200px;margin-left: 10px;float: left;">
                        <el-option v-for="(u, id) in allUsers" :label="u.name" :value="u.id"></el-option>
                    </el-select>
                    <el-select v-model="listQuery.assetsBusinessGener" placeholder="资产业务类型" clearable
                        style="width: 200px;margin-right: 20px;float: left;">
                        <el-option v-for="(s, value) in assetsBusinessGeners" :label="s.label" :value="s.value"></el-option>
                    </el-select>
                    <el-select v-model="listQuery.departmentId" filterable placeholder="接单部门"
                        style=" width: 200px;margin-left: 10px;float: left;">
                        <el-option v-for="(d, id) in allotDepartment" :label="d.name" :value="d.id"></el-option>
                    </el-select>
                    <el-button class="filter-item" style="margin-left: 10px;float: left;" type="primary"
                        @click="searchList()" round>搜索
                    </el-button>
                    <el-button class="filter-item" round type="success" @click="resetParams()">重置
                    </el-button>
                    <el-button class="filter-item" round type="info" @click="exportAssetsLedger()"
                        :disabled="exportStatus">导出
                    </el-button>
                </template>
                <parentTable ref="table" v-loading="listLoading" :data="pageData.records" slot="table" style="width: 100%;"
                    class="tableFull">
                    <el-table-column label="项目编号" width="120" align="center">
                        <template slot-scope="{row}">
                            <span>{{ row.orderId }}</span>
                        </template>
                    </el-table-column>
                    <el-table-column label="意见书号" width="220" align="center">
                        <template slot-scope="{row}">
                            <span>{{ row.statementNo }}</span>
                        </template>
                    </el-table-column>
                    <el-table-column label="报告号" width="220" align="center">
                        <template slot-scope="{row}">
                            <span>{{ row.reportNo }}</span>
                        </template>
                    </el-table-column>
                    <el-table-column label="报告类型" width="80" align="center">
                        <template slot-scope="{row}">
                            <span>{{ row.productionType }}</span>
                        </template>
                    </el-table-column>
                    <el-table-column label="评估基准日" width="120" align="center">
                        <template slot-scope="{row}">
                            <span>{{ row.valuationBasisDate }}</span>
                        </template>
                    </el-table-column>
                    <el-table-column label="报告出具日期" width="120" align="center">
                        <template slot-scope="{row}">
                            <span>{{ row.createReportDate }}</span>
                        </template>
                    </el-table-column>
                    <el-table-column label="打印份数" width="120" align="center">
                        <template slot-scope="{row}">
                            <span>{{ row.reportPrintCount }}</span>
                        </template>
                    </el-table-column>
                    <el-table-column label="项目名称" width="300" align="center">
                        <template slot-scope="{row}">
                            <span>{{ row.projectName }}</span>
                        </template>
                    </el-table-column>
                    <el-table-column label="当前节点" width="100" align="center">
                        <template slot-scope="{row}">
                            <span>{{ row.currentNodeName }}</span>
                        </template>
                    </el-table-column>
                    <el-table-column label="是否证券项目" width="80" align="center">
                        <template slot-scope="{row}">
                            <span>
                                <el-tag v-if="row.security" type="success">是</el-tag>
                                <el-tag v-else type="danger">否</el-tag>
                            </span>
                        </template>
                    </el-table-column>
                    <el-table-column label="是否资产重组项目" width="80" align="center">
                        <template slot-scope="{row}">
                            <span>
                                <el-tag v-if="row.significantAssetsReorganization" type="success">是</el-tag>
                                <el-tag v-else type="danger">否</el-tag>
                            </span>
                        </template>
                    </el-table-column>
                    <el-table-column label="是否国资项目" width="80" align="center">
                        <template slot-scope="{row}">
                            <span>
                                <el-tag v-if="row.stateAssets" type="success">是</el-tag>
                                <el-tag v-else type="danger">否</el-tag>
                            </span>
                        </template>
                    </el-table-column>
                    <el-table-column label="是否涉及境外资产" width="80" align="center">
                        <template slot-scope="{row}">
                            <span>
                                <el-tag v-if="row.foreignAssetsInvolved" type="success">是</el-tag>
                                <el-tag v-else type="danger">否</el-tag>
                            </span>
                        </template>
                    </el-table-column>
                    <el-table-column label="是否分配产值" width="80" align="center">
                        <template slot-scope="{row}">
                            <span>
                                <el-tag v-if="row.dispenseBenefit" type="success">是</el-tag>
                                <el-tag v-else type="danger">否</el-tag>
                            </span>
                        </template>
                    </el-table-column>
                    <el-table-column label="客户经理" width="120" align="center">
                        <template slot-scope="{row}">
                            <span>{{ row.clientManagerName }}</span>
                        </template>
                    </el-table-column>
                    <el-table-column label="项目负责人" width="120" align="center">
                        <template slot-scope="{row}">
                            <span>{{ row.principalName }}</span>
                        </template>
                    </el-table-column>
                    <el-table-column label="客户名称" width="120" align="center">
                        <template slot-scope="{row}">
                            <span>{{ row.customerName }}</span>
                        </template>
                    </el-table-column>
                    <el-table-column label="业务来源" width="120" align="center">
                        <template slot-scope="{row}">
                            <span>{{ row.customerSubName }}</span>
                        </template>
                    </el-table-column>
                    <el-table-column label="委托方" width="120" align="center">
                        <template slot-scope="{row}">
                            <span>{{ row.bailor }}</span>
                        </template>
                    </el-table-column>
                    <el-table-column label="委托方电话" width="120" align="center">
                        <template slot-scope="{row}">
                            <span>{{ row.bailorContactTel }}</span>
                        </template>
                    </el-table-column>
                    <el-table-column label="评估对象评估目的" width="120" align="center">
                        <template slot-scope="{row}">
                            <span>{{ row.purposeName }}</span>
                        </template>
                    </el-table-column>
                    <el-table-column label="评估对象价值类型" width="220" align="center">
                        <template slot-scope="{row}">
                            <span>{{ row.valueTypeName }}</span>
                        </template>
                    </el-table-column>
                    <el-table-column label="评估对象" width="220" align="center">
                        <template slot-scope="{row}">
                            <span>{{ row.targetTypeName }}</span>
                        </template>
                    </el-table-column>
                    <el-table-column label="评估方法" width="220" align="center">
                        <template slot-scope="{row}">
                            <span>{{ row.evaluationMethods }}</span>
                        </template>
                    </el-table-column>
                    <el-table-column label="自定义其他评估方法" width="220" align="center">
                        <template slot-scope="{row}">
                            <span>{{ row.definedEvaluationMethod }}</span>
                        </template>
                    </el-table-column>
                    <el-table-column label="最终选择的评估方法" width="220" align="center">
                        <template slot-scope="{row}">
                            <span>{{ row.choiceEvaluationMethods }}</span>
                        </template>
                    </el-table-column>
                    <el-table-column label="最终选择的自定义其他评估方法" width="220" align="center">
                        <template slot-scope="{row}">
                            <span>{{ row.choiceDefinedEvaluationMethod }}</span>
                        </template>
                    </el-table-column>
                    <el-table-column label="评估结论对应的评估值(万)" width="120" align="center">
                        <template slot-scope="{row}">
                            <span>{{ row.estimatedValue }}</span>
                        </template>
                    </el-table-column>
                    <el-table-column label="订单应收款(元)" width="120" align="center">
                        <template slot-scope="{row}">
                            <span>{{ row.shouldAmount }}</span>
                        </template>
                    </el-table-column>
                    <el-table-column label="订单实收款(元)" width="120" align="center">
                        <template slot-scope="{row}">
                            <span>{{ row.realAmount }}</span>
                        </template>
                    </el-table-column>
                    <el-table-column label="签字人" width="120" align="center">
                        <template slot-scope="{row}">
                            <span>{{ row.signatory }}</span>
                        </template>
                    </el-table-column>
                    <el-table-column label="出库时间" width="120" align="center">
                        <template slot-scope="{row}">
                            <span>{{ row.repertoryOutTime }}</span>
                        </template>
                    </el-table-column>
                    <el-table-column label="意见书审核人" width="80" align="center">
                        <template slot-scope="{row}">
                            <span>{{ row.statementCheckerName }}</span>
                        </template>
                    </el-table-column>
                    <el-table-column label="意见书复审人" width="80" align="center">
                        <template slot-scope="{row}">
                            <span>{{ row.statementReCheckerName }}</span>
                        </template>
                    </el-table-column>
                    <el-table-column label="报告审核人" width="80" align="center">
                        <template slot-scope="{row}">
                            <span>{{ row.reportCheckerName }}</span>
                        </template>
                    </el-table-column>
                    <el-table-column label="报告复审人" width="80" align="center">
                        <template slot-scope="{row}">
                            <span>{{ row.reportReCheckerName }}</span>
                        </template>
                    </el-table-column>
                    <el-table-column label="报告四审人" width="80" align="center">
                        <template slot-scope="{row}">
                            <span>{{ row.reportFourthCheckerName }}</span>
                        </template>
                    </el-table-column>
                </parentTable>
            </y-page-list-layout>
        </div>
    </div>
</template>

<script>
import Breadcrumb from '@/components/Breadcrumb'
import YPageListLayout from '@/components/YPageListLayout'

export default {
    name: 'assetsLedgerList',
    components: {
        Breadcrumb,
        YPageListLayout
    },
    data() {
        return {
            evaluationMethods: null,
            pageData: { records: [] },
            listLoading: false,
            // 时间
            selectDate: '',
            listQuery: {
                page: 1,
                size: 10,
                current: 1,
                // 开始时间
                startTime: null,
                // 结束时间
                endTime: null,
                // 关键字信息
                keyWord: null,
                // 客户经理id
                clientManagerId: null,
                // 项目负责人id
                pricipalId: null,
                // departmentId
                departmentId: null,
                // 资产业务类型
                assetsBusinessGener: null
            },
            // 用户下拉列表
            allUsers: [],
            // 业务类型下拉列表
            assetsBusinessGeners: [
                {
                    value: '企业价值',
                    label: '企业价值'
                }, {
                    value: '单项资产',
                    label: '单项资产'
                }, {
                    value: '资产组合',
                    label: '资产组合'
                }, {
                    value: '无形资产',
                    label: '无形资产'
                }, {
                    value: '森林资源资产',
                    label: '森林资源资产'
                }, {
                    value: '珠宝首饰艺术品',
                    label: '珠宝首饰艺术品'
                }, {
                    value: '其他资产评估',
                    label: '其他资产评估'
                }
            ],
            // 接单部门
            allotDepartment: [],
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
            exportStatus: true
        }
    },
    created() {
        this.getAllUser();
        this.getAssetsLedger();
    },
    methods: {
        // 获取所有用户下拉列表
        getAllUser() {
            this.$api.user.simpleAll().then(res => {
                if (res.code === 200) {
                    this.allUsers = res.data;
                }
            })
        },
        // 条件查询
        searchList() {
            // 重置分页
            this.listQuery.page = 1
            this.listQuery.size = 10
            this.getAssetsLedger()
        },
        // 重置搜索条件
        resetParams() {
            this.exportStatus = true;
            this.selectDate = '';
            this.$router.push({ query: {} });
            this.listQuery = {
                current: 1,
                size: 10,
            }
            this.getAssetsLedger();
        },
        getAssetsLedger() {
            if (this.selectDate) {
                this.listQuery.startTime = this.selectDate[0] + ' 00:00:00';
                this.listQuery.endTime = this.selectDate[1] + ' 23:59:59';
            }
            this.$api.statistical.getAssetsLedger(this.listQuery).then(res => {
                if (res.code === 200) {
                    this.pageData = res.data;
                    if (this.listQuery.startTime !== null && this.listQuery.endTime !== null && res.data.records.length > 0) {
                        this.exportStatus = false;
                    } else {
                        this.exportStatus = true;
                    }
                }
            })
        },
        exportAssetsLedger() {
            this.$utils.exportUtil(
                "assets/statisticalStatement/getAssetsLedgerVO/export", this.listQuery,
                "导出"
            );
        },
        // 获取部门下拉列表
        getAllotDepartment() {
            this.$api.department.allot('ASSETS_BUSINESS').then(res => {
                if (res.code === 200) {
                    this.allotDepartment = res.data;
                }
            })
        }
    }
}
</script>
<style scoped lang="scss">
/* el-table 列数据为空自动显示 - */
.tableFull :empty::before {
    content: '-';
    color: gray;
}
</style>