<template>
    <div class="app-container organization-index">
        <div class="title-container">
            <breadcrumb id="breadcrumb-container" class="breadcrumb-container" />
        </div>
        <div>
            <el-tabs v-model="activeTabName" @tab-click="tabClick" type="border-card">
                <el-tab-pane label="评估部效率值表" name="department">
                    <div style="width: 100%; height: 50px;">
                        <el-date-picker style="margin-right: 20px;float: left;" v-model="selectDate1" type="daterange"
                            :picker-options="pickerOptions" format="yyyy 年 MM 月 dd 日" value-format="yyyy-MM-dd"
                            range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期" align="right">
                        </el-date-picker>
                        <el-select v-model="listQueryByDepartment.departmentId" filterable placeholder="接单部门"
                            style=" width: 200px;margin-left: 10px;float: left;">
                            <el-option v-for="(d, id) in allotDepartment" :label="d.name" :value="d.id"></el-option>
                        </el-select>
                        <el-button class="filter-item" style="margin-left: 10px;float: left;" type="primary"
                            @click="searchList1()" round>搜索
                        </el-button>
                        <el-button class="filter-item" round type="success" @click="resetParams1()">重置
                        </el-button>
                        <el-button class="filter-item" round type="info" @click="exportAssetsEvaluateDepEfficiency()"
                            :disabled="exportStatus">导出
                        </el-button>
                    </div>
                    <el-table :data="depEfficiency" fit stripe highlight-current-row
                        :header-row-style="{ color: '#333333', 'font-size': '14px' }"
                        :header-cell-style="{ 'text-align': 'center' }" :cell-style="{ 'text-align': 'center' }" style=" width: 100%; float: right;
                      border-left: 1px solid #ebeced;
                      border-right: 1px solid #ebeced;
                      color: #333333; font-size: 14px;">
                        <el-table-column label="评估目的" width="150">
                            <template slot-scope="{row}">
                                <span>{{ row.evaluateAim }}</span><br />
                                <span style="color: green;" v-if="row.financial">（金融）</span>
                                <span style="color: red;" v-else>（非金融）</span>
                            </template>
                        </el-table-column>
                        <el-table-column label="未看现场数">
                            <template slot-scope="{row}">
                                <el-link v-if="row.unSiteCount > 0"
                                    @click="setListQueryParms('unSite', row.evaluateAim, row.financial, null, listQueryByDepartment.startTime, listQueryByDepartment.endTime)">
                                    {{ row.unSiteCount }}
                                </el-link>
                                <span v-else>{{ row.unSiteCount }}</span>
                            </template>
                        </el-table-column>
                        <el-table-column label="接单数">
                            <el-table-column label="非口估">
                                <template slot-scope="{row}">
                                    <el-link v-if="row.unInformalEvaluateCount > 0"
                                        @click="setListQueryParms('order', row.evaluateAim, row.financial, false, listQueryByDepartment.startTime, listQueryByDepartment.endTime)">
                                        {{ row.unInformalEvaluateCount }}
                                    </el-link>
                                    <span v-else>{{ row.unInformalEvaluateCount }}</span>
                                </template>
                            </el-table-column>
                            <el-table-column label="口估">
                                <template slot-scope="{row}">
                                    <el-link v-if="row.informalEvaluateCount > 0"
                                        @click="setListQueryParms('order', row.evaluateAim, row.financial, true, listQueryByDepartment.startTime, listQueryByDepartment.endTime)">
                                        {{ row.informalEvaluateCount }}
                                    </el-link>
                                    <span v-else>{{ row.informalEvaluateCount }}</span>
                                </template>
                            </el-table-column>
                            <el-table-column label="同比(%)">
                                <template slot-scope="{row}">
                                    <span>{{ row.YoYOrderVolume }}</span>
                                </template>
                            </el-table-column>
                            <el-table-column label="环比(%)">
                                <template slot-scope="{row}">
                                    <span>{{ row.QoQOrderVolume }}</span>
                                </template>
                            </el-table-column>
                        </el-table-column>
                        <el-table-column label="预评">
                            <el-table-column label="当期">
                                <template slot-scope="{row}">
                                    <el-link v-if="row.currentPeriodStatementVolume > 0"
                                        @click="setListQueryParms('statement', row.evaluateAim, row.financial, null, listQueryByDepartment.startTime, listQueryByDepartment.endTime)">
                                        {{ row.currentPeriodStatementVolume }}
                                    </el-link>
                                    <span v-else>{{ row.currentPeriodStatementVolume }}</span>
                                </template>
                            </el-table-column>
                            <el-table-column label="同比(%)">
                                <template slot-scope="{row}">
                                    <span>{{ row.YoYStatementVolume }}</span>
                                </template>
                            </el-table-column>
                            <el-table-column label="环比(%)">
                                <template slot-scope="{row}">
                                    <span>{{ row.QoQStatementVolume }}</span>
                                </template>
                            </el-table-column>
                        </el-table-column>
                        <el-table-column label="报告">
                            <el-table-column label="当期">
                                <template slot-scope="{row}">
                                    <el-link v-if="row.currentPeriodReportVolume > 0"
                                        @click="setListQueryParms('report', row.evaluateAim, row.financial, null, listQueryByDepartment.startTime, listQueryByDepartment.endTime)">
                                        {{ row.currentPeriodReportVolume }}
                                    </el-link>
                                    <span v-else>{{ row.currentPeriodReportVolume }}</span>
                                </template>
                            </el-table-column>
                            <el-table-column label="同比(%)">
                                <template slot-scope="{row}">
                                    <span>{{ row.YoYReportVolume }}</span>
                                </template>
                            </el-table-column>
                            <el-table-column label="环比(%)">
                                <template slot-scope="{row}">
                                    <span>{{ row.QoQReportVolume }}</span>
                                </template>
                            </el-table-column>
                        </el-table-column>
                        <el-table-column label="应收款">
                            <el-table-column label="当期">
                                <template slot-scope="{row}">
                                    <el-link v-if="row.currentPeriodShouldAmountSum > 0"
                                        @click="setListQueryParms('should', row.evaluateAim, row.financial, null, listQueryByDepartment.startTime, listQueryByDepartment.endTime)">
                                        {{ row.currentPeriodShouldAmountSum }}
                                    </el-link>
                                    <span v-else>{{ row.currentPeriodShouldAmountSum }}</span>
                                </template>
                            </el-table-column>
                            <el-table-column label="同比(%)">
                                <template slot-scope="{row}">
                                    <span>{{ row.YoYShouldAmountSum }}</span>
                                </template>
                            </el-table-column>
                            <el-table-column label="环比(%)">
                                <template slot-scope="{row}">
                                    <span>{{ row.QoQShouldAmountSum }}</span>
                                </template>
                            </el-table-column>
                        </el-table-column>
                        <el-table-column label="实收款">
                            <el-table-column label="当期">
                                <template slot-scope="{row}">
                                    <el-link v-if="row.currentPeriodRealAmountSum > 0"
                                        @click="setListQueryParms('real', row.evaluateAim, row.financial, null, listQueryByDepartment.startTime, listQueryByDepartment.endTime)">
                                        {{ row.currentPeriodRealAmountSum }}
                                    </el-link>
                                    <span v-else>{{ row.currentPeriodRealAmountSum }}</span>
                                </template>
                            </el-table-column>
                            <el-table-column label="同比(%)">
                                <template slot-scope="{row}">
                                    <span>{{ row.YoYRealAmountSum }}</span>
                                </template>
                            </el-table-column>
                            <el-table-column label="环比(%)">
                                <template slot-scope="{row}">
                                    <span>{{ row.QoQRealAmountSum }}</span>
                                </template>
                            </el-table-column>
                        </el-table-column>
                        <el-table-column label="实收款总计">
                            <template slot-scope="{row}">
                                <span>{{ row.totalRealAmountSum }}</span>
                            </template>
                        </el-table-column>
                        <el-table-column label="出单率">
                            <el-table-column label="当期(%)">
                                <template slot-scope="{row}">
                                    <span>{{ row.currentPeriodOrderHasReportVolume }}</span>
                                </template>
                            </el-table-column>
                            <el-table-column label="同比(%)">
                                <template slot-scope="{row}">
                                    <span>{{ row.YoYOrderHasReportVolume }}</span>
                                </template>
                            </el-table-column>
                            <el-table-column label="环比(%)">
                                <template slot-scope="{row}">
                                    <span>{{ row.QoQOrderHasReportVolume }}</span>
                                </template>
                            </el-table-column>
                        </el-table-column>
                        <el-table-column label="累计年口估数">
                            <template slot-scope="{row}">
                                <span>{{ row.yearInformalEvaluateCount }}</span>
                            </template>
                        </el-table-column>
                    </el-table>
                </el-tab-pane>
                <el-tab-pane label="评估人员效率值表" name="evaluator">
                    <div style="width: 100%; height: 50px;">
                        <el-date-picker style="margin-right: 20px;float: left;" v-model="selectDate2" type="daterange"
                            :picker-options="pickerOptions" format="yyyy 年 MM 月 dd 日" value-format="yyyy-MM-dd"
                            range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期" align="right">
                        </el-date-picker>
                        <el-select v-model="listQueryByEvaluator.departmentId" filterable placeholder="接单部门"
                            style=" width: 200px;margin-left: 10px;float: left;">
                            <el-option v-for="(d, id) in allotDepartment" :label="d.name" :value="d.id"></el-option>
                        </el-select>
                        <el-input v-model="listQueryByEvaluator.userName" placeholder="姓名" clearable
                            style="margin-left: 20px;width: 200px;float: left;">
                        </el-input>
                        <el-button class="filter-item" style="margin-left: 10px;float: left;" type="primary"
                            @click="searchList2()" round>搜索
                        </el-button>
                        <el-button class="filter-item" round type="success" @click="resetParams2()">重置
                        </el-button>
                        <el-button class="filter-item" round type="info" @click="exportMajorEvaluatePerEfficiencyVO()"
                            :disabled="exportStatus">导出
                        </el-button>
                    </div>
                    <el-table :data="perEfficiency" fit stripe highlight-current-row
                        :header-row-style="{ color: '#333333', 'font-size': '14px' }"
                        :header-cell-style="{ 'text-align': 'center' }" :cell-style="{ 'text-align': 'center' }" style=" width: 100%; float: right;
                      border-left: 1px solid #ebeced;
                      border-right: 1px solid #ebeced;
                      color: #333333; font-size: 14px;">
                        <el-table-column label="评估人员" width="150">
                            <template slot-scope="{row}">
                                <span>{{ row.userName }}</span>
                            </template>
                        </el-table-column>
                        <el-table-column label="部门名称" width="150">
                            <template slot-scope="{row}">
                                <span>{{ row.departmentName }}</span>
                            </template>
                        </el-table-column>
                        <el-table-column label="接单数">
                            <el-table-column label="非口估">
                                <template slot-scope="{row}">
                                    <el-link v-if="row.unInformalEvaluateCount > 0"
                                        @click="setListQueryParms('order', row.evaluateAim, row.financial, false, listQueryByDepartment.startTime, listQueryByDepartment.endTime, row.userId)">
                                        {{ row.unInformalEvaluateCount }}
                                    </el-link>
                                    <span v-else>{{ row.unInformalEvaluateCount }}</span>
                                </template>
                            </el-table-column>
                            <el-table-column label="口估">
                                <template slot-scope="{row}">
                                    <el-link v-if="row.informalEvaluateCount > 0"
                                        @click="setListQueryParms('order', row.evaluateAim, row.financial, true, listQueryByDepartment.startTime, listQueryByDepartment.endTime, row.userId)">
                                        {{ row.informalEvaluateCount }}
                                    </el-link>
                                    <span v-else>{{ row.informalEvaluateCount }}</span>
                                </template>
                            </el-table-column>
                            <el-table-column label="同比(%)">
                                <template slot-scope="{row}">
                                    <span>{{ row.YoYOrderVolume }}</span>
                                </template>
                            </el-table-column>
                            <el-table-column label="环比(%)">
                                <template slot-scope="{row}">
                                    <span>{{ row.QoQOrderVolume }}</span>
                                </template>
                            </el-table-column>
                        </el-table-column>
                        <el-table-column label="预评(意见书)">
                            <el-table-column label="当期">
                                <template slot-scope="{row}">
                                    <el-link v-if="row.currentPeriodStatementVolume > 0"
                                        @click="setListQueryParms('statement', row.evaluateAim, row.financial, null, listQueryByDepartment.startTime, listQueryByDepartment.endTime, row.userId)">
                                        {{ row.currentPeriodStatementVolume }}
                                    </el-link>
                                    <span v-else>{{ row.currentPeriodStatementVolume }}</span>
                                </template>
                            </el-table-column>
                            <el-table-column label="同比(%)">
                                <template slot-scope="{row}">
                                    <span>{{ row.YoYStatementVolume }}</span>
                                </template>
                            </el-table-column>
                            <el-table-column label="环比(%)">
                                <template slot-scope="{row}">
                                    <span>{{ row.QoQStatementVolume }}</span>
                                </template>
                            </el-table-column>
                        </el-table-column>
                        <el-table-column label="报告(咨询、评估、意见函)">
                            <el-table-column label="当期">
                                <template slot-scope="{row}">
                                    <el-link v-if="row.currentPeriodReportVolume > 0"
                                        @click="setListQueryParms('report', row.evaluateAim, row.financial, null, listQueryByDepartment.startTime, listQueryByDepartment.endTime, row.userId)">
                                        {{ row.currentPeriodReportVolume }}
                                    </el-link>
                                    <span v-else>{{ row.currentPeriodReportVolume }}</span>
                                </template>
                            </el-table-column>
                            <el-table-column label="同比(%)">
                                <template slot-scope="{row}">
                                    <span>{{ row.YoYReportVolume }}</span>
                                </template>
                            </el-table-column>
                            <el-table-column label="环比(%)">
                                <template slot-scope="{row}">
                                    <span>{{ row.QoQReportVolume }}</span>
                                </template>
                            </el-table-column>
                        </el-table-column>
                        <el-table-column label="应收款">
                            <el-table-column label="当期">
                                <template slot-scope="{row}">
                                    <el-link v-if="row.currentPeriodShouldAmountSum > 0"
                                        @click="setListQueryParms('should', row.evaluateAim, row.financial, null, listQueryByDepartment.startTime, listQueryByDepartment.endTime, row.userId)">
                                        {{ row.currentPeriodShouldAmountSum }}
                                    </el-link>
                                    <span v-else>{{ row.currentPeriodShouldAmountSum }}</span>
                                </template>
                            </el-table-column>
                            <el-table-column label="同比(%)">
                                <template slot-scope="{row}">
                                    <span>{{ row.YoYShouldAmountSum }}</span>
                                </template>
                            </el-table-column>
                            <el-table-column label="环比(%)">
                                <template slot-scope="{row}">
                                    <span>{{ row.QoQShouldAmountSum }}</span>
                                </template>
                            </el-table-column>
                        </el-table-column>
                        <el-table-column label="实收款">
                            <el-table-column label="当期">
                                <template slot-scope="{row}">
                                    <el-link v-if="row.currentPeriodRealAmountSum > 0"
                                        @click="setListQueryParms('real', row.evaluateAim, row.financial, null, listQueryByDepartment.startTime, listQueryByDepartment.endTime, row.userId)">
                                        {{ row.currentPeriodRealAmountSum }}
                                    </el-link>
                                    <span v-else>{{ row.currentPeriodRealAmountSum }}</span>
                                </template>
                            </el-table-column>
                            <el-table-column label="同比(%)">
                                <template slot-scope="{row}">
                                    <span>{{ row.YoYRealAmountSum }}</span>
                                </template>
                            </el-table-column>
                            <el-table-column label="环比(%)">
                                <template slot-scope="{row}">
                                    <span>{{ row.QoQRealAmountSum }}</span>
                                </template>
                            </el-table-column>
                        </el-table-column>
                        <el-table-column label="实收款总计">
                            <template slot-scope="{row}">
                                <span>{{ row.totalRealAmountSum }}</span>
                            </template>
                        </el-table-column>
                        <el-table-column label="出单率">
                            <el-table-column label="当期(%)">
                                <template slot-scope="{row}">
                                    <span>{{ row.currentPeriodOrderHasReportVolume }}</span>
                                </template>
                            </el-table-column>
                            <el-table-column label="同比(%)">
                                <template slot-scope="{row}">
                                    <span>{{ row.YoYOrderHasReportVolume }}</span>
                                </template>
                            </el-table-column>
                            <el-table-column label="环比(%)">
                                <template slot-scope="{row}">
                                    <span>{{ row.QoQOrderHasReportVolume }}</span>
                                </template>
                            </el-table-column>
                        </el-table-column>
                        <el-table-column label="意见书平均内控作业时长">
                            <template slot-scope="{row}">
                                <span>{{ row.statementAICPTime }}</span>
                            </template>
                        </el-table-column>
                        <el-table-column label="报告平均内控作业时长">
                            <template slot-scope="{row}">
                                <span>{{ row.reportAICPTime }}</span>
                            </template>
                        </el-table-column>
                        <el-table-column label="报价时长(金融)">
                            <template slot-scope="{row}">
                                <span>{{ row.financialQuoteProcessingTime }}</span>
                            </template>
                        </el-table-column>
                        <el-table-column label="报价时长(非金融)">
                            <template slot-scope="{row}">
                                <span>{{ row.unFinancialQuoteProcessingTime }}</span>
                            </template>
                        </el-table-column>
                    </el-table>
                </el-tab-pane>
            </el-tabs>
        </div>
        <el-dialog title="资产订单列表" :visible.sync="dialogVisible" width="70%" @open="getMajorEfficiencyDetailVO()">
            <y-page-list-layout :get-page-list="getMajorEfficiencyDetailVO" :page-list="pageData" :page-para="listQueryDetail">
                <template slot="left">
                    <el-button class="filter-item" round type="info" @click="exportAssetsEvaEffDetail()">导出
                    </el-button>
                </template>
                <parentTable ref="table" :data="pageData.records" slot="table" style="width: 100%;" :isBoard=800
                    class="tableFull">
                    <el-table-column label="项目编号" align="center">
                        <template slot-scope="{row}">
                            <span>{{ row.orderId }}</span>
                        </template>
                    </el-table-column>
                    <el-table-column label="项目名称" align="center">
                        <template slot-scope="{row}">
                            <span>{{ row.projectName }}</span>
                        </template>
                    </el-table-column>
                    <el-table-column label="客户名称" align="center">
                        <template slot-scope="{row}">
                            <span>{{ row.customerName }}</span>
                        </template>
                    </el-table-column>
                    <el-table-column label="业务来源" align="center">
                        <template slot-scope="{row}">
                            <span>{{ row.customerSubName }}</span>
                        </template>
                    </el-table-column>
                    <el-table-column label="客户联系人名" align="center">
                        <template slot-scope="{row}">
                            <span>{{ row.linkmanName }}</span>
                        </template>
                    </el-table-column>
                    <el-table-column label="委托人" align="center">
                        <template slot-scope="{row}">
                            <span>{{ row.bailor }}</span>
                        </template>
                    </el-table-column>
                    <el-table-column label="报告号" align="center">
                        <template slot-scope="{row}">
                            <span>{{ row.reportNo }}</span>
                        </template>
                    </el-table-column>
                    <el-table-column label="项目负责人" align="center">
                        <template slot-scope="{row}">
                            <span>{{ row.principalName }}</span>
                        </template>
                    </el-table-column>
                    <el-table-column label="项目参与人" align="center">
                        <template slot-scope="{row}">
                            <span>{{ row.members }}</span>
                        </template>
                    </el-table-column>
                    <el-table-column label="客户经理名" align="center">
                        <template slot-scope="{row}">
                            <span>{{ row.clientManagerName }}</span>
                        </template>
                    </el-table-column>
                    <el-table-column label="评估金额(万元)" align="center">
                        <template slot-scope="{row}">
                            <span>{{ row.checkValueSum }}</span>
                        </template>
                    </el-table-column>
                    <el-table-column label="实勘时间" align="center">
                        <template slot-scope="{row}">
                            <span>{{ row.reconnaissanceDate }}</span>
                        </template>
                    </el-table-column>
                    <el-table-column label="出意见书时间" align="center">
                        <template slot-scope="{row}">
                            <span>{{ row.writeStatementDate }}</span>
                        </template>
                    </el-table-column>
                    <el-table-column label="出报告时间" align="center">
                        <template slot-scope="{row}">
                            <span>{{ row.writeReportDate }}</span>
                        </template>
                    </el-table-column>
                    <el-table-column label="订单应收款(元)" align="center">
                        <template slot-scope="{row}">
                            <span>{{ row.shouldAmount }}</span>
                        </template>
                    </el-table-column>
                    <el-table-column label="订单实收款(元)" align="center">
                        <template slot-scope="{row}">
                            <span>{{ row.realAmount }}</span>
                        </template>
                    </el-table-column>
                    <el-table-column label="当前实例节点名称" align="center">
                        <template slot-scope="{row}">
                            <span>{{ row.currentNodeName }}</span>
                        </template>
                    </el-table-column>
                    <el-table-column label="当前节点处理人" align="center">
                        <template slot-scope="{row}">
                            <span>{{ row.handlerName }}</span>
                        </template>
                    </el-table-column>
                </parentTable>
            </y-page-list-layout>
        </el-dialog>
    </div>
</template>

<script>
import Breadcrumb from '@/components/Breadcrumb'
import YPageListLayout from '@/components/YPageListLayout'

export default {
    name: 'majorEvaluateEfficiency',
    components: {
        Breadcrumb,
        YPageListLayout
    },
    data() {
        return {
            activeTabName: 'department',
            dialogVisible: false,
            listLoading: false,
            // 时间
            selectDate1: [],
            selectDate2: [],
            listQueryByDepartment: {
                // 开始时间
                startTime: null,
                // 结束时间
                endTime: null,
                // 部门id
                departmentId: null
            },
            listQueryByEvaluator: {
                // 开始时间
                startTime: null,
                // 结束时间
                endTime: null,
                // 部门id
                departmentId: null,
                // 评估人员姓名
                userName: null
            },
            listQueryDetail: {
                page: 1,
                size: 10,
                current: 1,
                // 效率类型(接单:order、预评:statement、报告:report、应收款:should、实收款:real)
                efficiencyType: null,
                // 开始时间
                startTime: null,
                // 结束时间
                endTime: null,
                evaluateAim: null,
                financial: null,
                informalEvaluate: null,
                principalId: null
            },
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
            exportStatus: true,
            depEfficiency: [],
            perEfficiency: [],
            pageData: { records: [] },
        }
    },
    created() {
        this.selectDate1.push(this.getDefaultStartDate());
        this.selectDate1.push(this.getDefaultEndDate());
        this.selectDate2.push(this.getDefaultStartDate());
        this.selectDate2.push(this.getDefaultEndDate());
        this.getAllotDepartment();
        this.getMajorEvaluateDepEfficiencyVO();
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
        tabClick(tab, event) {
            if (tab.name === 'department') {
                this.getMajorEvaluateDepEfficiencyVO();
            }
            if (tab.name === 'evaluator') {
                this.getMajorEvaluatePerEfficiencyVO();
            }
        },
        // 条件查询
        searchList1() {
            this.getMajorEvaluateDepEfficiencyVO()
        },
        // 重置搜索条件
        resetParams1() {
            this.exportStatus = true;
            this.listQueryByDepartment = {
                // 开始时间
                startTime: null,
                // 结束时间
                endTime: null,
                // 部门id
                departmentId: null
            };
            this.selectDate1 = [];
            this.selectDate1.push(this.getDefaultStartDate());
            this.selectDate1.push(this.getDefaultEndDate());
            this.getMajorEvaluateDepEfficiencyVO();
        },
        getMajorEvaluateDepEfficiencyVO() {
            if (this.selectDate1) {
                this.listQueryByDepartment.startTime = this.selectDate1[0] + ' 00:00:00';
                this.listQueryByDepartment.endTime = this.selectDate1[1] + ' 23:59:59';
            }
            this.$api.statistical.getMajorEvaluateDepEfficiencyVO(this.listQueryByDepartment).then(res => {
                if (res.code === 200) {
                    this.depEfficiency = res.data;
                    if (this.listQueryByDepartment.startTime !== null && this.listQueryByDepartment.endTime !== null && res.data.length > 0) {
                        this.exportStatus = false;
                    } else {
                        this.exportStatus = true;
                    }
                }
            })
        },
        exportAssetsEvaluateDepEfficiency() {
            this.$utils.exportUtil(
                "major/statisticalStatement/evaluate/dep/getEfficiencyVO/export", this.listQueryByDepartment,
                "导出"
            );
        },
        // 获取部门下拉列表
        getAllotDepartment() {
            this.$api.department.allot('MAJOR_BUSINESS').then(res => {
                if (res.code === 200) {
                    this.allotDepartment = res.data;
                }
            })
        },
        // 条件查询
        searchList2() {
            this.getMajorEvaluatePerEfficiencyVO()
        },
        // 重置搜索条件
        resetParams2() {
            this.exportStatus = true;
            this.listQueryByEvaluator = {
                // 开始时间
                startTime: null,
                // 结束时间
                endTime: null,
                // 部门id
                departmentId: null
            };
            this.selectDate2 = [];
            this.selectDate2.push(this.getDefaultStartDate());
            this.selectDate2.push(this.getDefaultEndDate());
            this.getMajorEvaluatePerEfficiencyVO();
        },
        // 评估人员绩效扣分查询
        getMajorEvaluatePerEfficiencyVO() {
            if (this.selectDate2) {
                this.listQueryByEvaluator.startTime = this.selectDate2[0] + ' 00:00:00';
                this.listQueryByEvaluator.endTime = this.selectDate2[1] + ' 23:59:59';
            }
            this.$api.statistical.getMajorEvaluatePerEfficiencyVO(this.listQueryByEvaluator).then(res => {
                if (res.code === 200) {
                    this.perEfficiency = res.data;
                    if (this.listQueryByEvaluator.startTime !== null && this.listQueryByEvaluator.endTime !== null && res.data.length > 0) {
                        this.exportStatus = false;
                    } else {
                        this.exportStatus = true;
                    }
                }
            })
        },
        exportMajorEvaluatePerEfficiencyVO() {
            this.$utils.exportUtil(
                "major/statisticalStatement/evaluate/per/getEfficiencyVO/export", this.listQueryByEvaluator,
                "导出"
            );
        },
        setListQueryParms(efficiencyType, evaluateAim, financial, informalEvaluate, startTime, endTime, userId) {
            this.dialogVisible = true;
            this.listQueryDetail.page = 1;
            this.listQueryDetail.size = 10;
            this.listQueryDetail.current = 1;
            this.listQueryDetail.efficiencyType = efficiencyType;
            this.listQueryDetail.evaluateAim = evaluateAim;
            this.listQueryDetail.financial = financial;
            this.listQueryDetail.startTime = startTime;
            this.listQueryDetail.endTime = endTime;
            if (userId) {
                this.listQueryDetail.principalId = userId;
            } else {
                this.listQueryDetail.principalId = null;
            }
        },
        // 效率详情列表查询
        getMajorEfficiencyDetailVO() {
            this.$api.statistical.getMajorEfficiencyDetailVO(this.listQueryDetail).then(res => {
                if (res.code === 200) {
                    this.pageData = res.data;
                }
            })
        },
        // 效率详情列表查询导出
        exportAssetsEvaEffDetail() {
            this.$utils.exportUtil(
                "assets/statisticalStatement/evaluate/getMajorEfficiencyDetailVO/export", this.listQueryDetail,
                "导出"
            );
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