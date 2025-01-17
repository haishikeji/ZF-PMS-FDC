<template>
    <div class="app-container organization-index">
        <div class="title-container">
            <breadcrumb id="breadcrumb-container" class="breadcrumb-container" />
        </div>
        <div>
            <el-tabs v-model="activeTabName" @tab-click="tabClick" type="border-card">
                <el-tab-pane label="评估部绩效扣分" name="department">
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
                        <el-button class="filter-item" round type="info" @click="exportMajorPerformanceDeductionVO()"
                            :disabled="exportStatus">导出
                        </el-button>
                    </div>
                    <el-table :data="departmentPerformanceDeduction" fit stripe highlight-current-row
                        :header-row-style="{ color: '#333333', 'font-size': '14px' }" style=" width: 100%; float: right;
                      border-left: 1px solid #ebeced;
                      border-right: 1px solid #ebeced;
                      color: #333333; font-size: 14px;">
                        <el-table-column label="部门名称" width="150">
                            <template slot-scope="{row}">
                                <span>{{ row.departmentName }}</span>
                            </template>
                        </el-table-column>
                        <el-table-column label="复审扣分">
                            <el-table-column label="一般错误">
                                <template slot-scope="{row}">
                                    <el-link type="danger" v-if="row.reCheckNormalMistakeScore > 0"
                                        @click="setListQueryParms('复审', 'normal', row.departmentId, listQueryByDepartment.startTime, listQueryByDepartment.endTime)">{{
                                            row.reCheckNormalMistakeScore }}</el-link>
                                    <span v-else>{{ row.reCheckNormalMistakeScore }}</span>
                                </template>
                            </el-table-column>
                            <el-table-column label="较大错误">
                                <template slot-scope="{row}">
                                    <el-link type="danger" v-if="row.reCheckHardMistakeScore > 0"
                                        @click="setListQueryParms('复审', 'hard', row.departmentId, listQueryByDepartment.startTime, listQueryByDepartment.endTime)">{{
                                            row.reCheckHardMistakeScore }}</el-link>
                                    <span v-else>{{ row.reCheckHardMistakeScore }}</span>
                                </template>
                            </el-table-column>
                            <el-table-column label="重大错误">
                                <template slot-scope="{row}">
                                    <el-link type="danger" v-if="row.reCheckFatalMistakeScore > 0"
                                        @click="setListQueryParms('复审', 'fatal', row.departmentId, listQueryByDepartment.startTime, listQueryByDepartment.endTime)">{{
                                            row.reCheckFatalMistakeScore }}</el-link>
                                    <span v-else>{{ row.reCheckFatalMistakeScore }}</span>
                                </template>
                            </el-table-column>
                        </el-table-column>
                        <el-table-column label="抽检扣分">
                            <el-table-column label="一般错误">
                            </el-table-column>
                            <el-table-column label="较大错误">
                            </el-table-column>
                            <el-table-column label="重大错误">
                            </el-table-column>
                        </el-table-column>
                        <el-table-column label="外部投诉">
                            <el-table-column label="一般错误">
                            </el-table-column>
                            <el-table-column label="较大错误">
                            </el-table-column>
                            <el-table-column label="重大错误">
                            </el-table-column>
                            <el-table-column label="服务态度恶劣">
                            </el-table-column>
                        </el-table-column>
                        <el-table-column label="内部投诉">
                            <el-table-column label="一般错误">
                            </el-table-column>
                            <el-table-column label="较大错误">
                            </el-table-column>
                            <el-table-column label="重大错误">
                            </el-table-column>
                            <el-table-column label="服务态度恶劣">
                            </el-table-column>
                        </el-table-column>
                        <el-table-column label="疑难项目加减分" width="120">
                        </el-table-column>
                        <el-table-column label="总经理加减分">
                        </el-table-column>
                        <el-table-column label="部门综合评分">
                        </el-table-column>
                        <el-table-column prop="allMistakeScore" label="扣分合计">
                        </el-table-column>
                    </el-table>
                </el-tab-pane>
                <el-tab-pane label="评估人员绩效扣分" name="evaluator">
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
                        <el-button class="filter-item" round type="info"
                            @click="exportMajorPerformanceDeductionByEvaluator()" :disabled="exportStatus">导出
                        </el-button>
                    </div>
                    <el-table :data="evaluatorPerformanceDeduction" fit stripe highlight-current-row
                        :header-row-style="{ color: '#333333', 'font-size': '14px' }" style=" width: 100%; float: right;
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
                        <el-table-column label="复审扣分">
                            <el-table-column label="一般错误">
                                <template slot-scope="{row}">
                                    <el-link type="danger" v-if="row.reCheckNormalMistakeScore > 0"
                                        @click="setListQueryParms('复审', 'normal', row.departmentId, listQueryByEvaluator.startTime, listQueryByEvaluator.endTime, row.userId)">{{
                                            row.reCheckNormalMistakeScore }}</el-link>
                                    <span v-else>{{ row.reCheckNormalMistakeScore }}</span>
                                </template>
                            </el-table-column>
                            <el-table-column label="较大错误">
                                <template slot-scope="{row}">
                                    <el-link type="danger" v-if="row.reCheckHardMistakeScore > 0"
                                        @click="setListQueryParms('复审', 'hard', row.departmentId, listQueryByEvaluator.startTime, listQueryByEvaluator.endTime, row.userId)">{{
                                            row.reCheckHardMistakeScore }}</el-link>
                                    <span v-else>{{ row.reCheckHardMistakeScore }}</span>
                                </template>
                            </el-table-column>
                            <el-table-column label="重大错误">
                                <template slot-scope="{row}">
                                    <el-link type="danger" v-if="row.reCheckFatalMistakeScore > 0"
                                        @click="setListQueryParms('复审', 'fatal', row.departmentId, listQueryByEvaluator.startTime, listQueryByEvaluator.endTime, row.userId)">{{
                                            row.reCheckFatalMistakeScore }}</el-link>
                                    <span v-else>{{ row.reCheckFatalMistakeScore }}</span>
                                </template>
                            </el-table-column>
                        </el-table-column>
                        <el-table-column label="抽检扣分">
                            <el-table-column label="一般错误">
                            </el-table-column>
                            <el-table-column label="较大错误">
                            </el-table-column>
                            <el-table-column label="重大错误">
                            </el-table-column>
                        </el-table-column>
                        <el-table-column label="外部投诉">
                            <el-table-column label="一般错误">
                            </el-table-column>
                            <el-table-column label="较大错误">
                            </el-table-column>
                            <el-table-column label="重大错误">
                            </el-table-column>
                            <el-table-column label="服务态度恶劣">
                            </el-table-column>
                        </el-table-column>
                        <el-table-column label="内部投诉">
                            <el-table-column label="一般错误">
                            </el-table-column>
                            <el-table-column label="较大错误">
                            </el-table-column>
                            <el-table-column label="重大错误">
                            </el-table-column>
                            <el-table-column label="服务态度恶劣">
                            </el-table-column>
                        </el-table-column>
                        <el-table-column prop="allMistakeScore" label="扣分合计">
                        </el-table-column>
                    </el-table>
                </el-tab-pane>
            </el-tabs>
        </div>
        <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="70%"
            @open="getMajorPerformanceDeductionDetailVO()">
            <y-page-list-layout :get-page-list="getMajorPerformanceDeductionDetailVO" :page-list="pageData"
                :page-para="listQueryDetail">
                <template slot="left">
                    <el-button class="filter-item" round type="info" @click="exportPerformanceDeductionDetail()">导出
                    </el-button>
                </template>
                <parentTable ref="table" :data="pageData.records" slot="table" style="width: 100%;" :isBoard=800>
                    <el-table-column label="项目编号" align="center">
                        <template slot-scope="{row}">
                            <span>{{ row.orderId }}</span>
                        </template>
                    </el-table-column>
                    <el-table-column label="是否金融" align="center">
                        <template slot-scope="{row}">
                            <span v-if="row.financial">金融</span>
                            <span v-else>非金融</span>
                        </template>
                    </el-table-column>
                    <el-table-column label="项目名称" align="center">
                        <template slot-scope="{row}">
                            <span>{{ row.projectName }}</span>
                        </template>
                    </el-table-column>
                    <el-table-column label="银行/单位" align="center">
                        <template slot-scope="{row}">
                            <span>{{ row.customerName }}</span>
                        </template>
                    </el-table-column>
                    <el-table-column label="产品类型" align="center">
                        <template slot-scope="{row}">
                            <span>{{ row.productionType }}</span>
                        </template>
                    </el-table-column>
                    <el-table-column label="产品号" align="center">
                        <template slot-scope="{row}">
                            <span>{{ row.productionNo }}</span>
                        </template>
                    </el-table-column>
                    <el-table-column label="项目负责人" align="center">
                        <template slot-scope="{row}">
                            <span>{{ row.principalName }}</span>
                        </template>
                    </el-table-column>
                    <el-table-column label="参与人" align="center">
                        <template slot-scope="{row}">
                            <span>{{ row.members }}</span>
                        </template>
                    </el-table-column>
                    <el-table-column label="评估金额(万元)" align="center">
                        <template slot-scope="{row}">
                            <span>{{ row.evaluateAmount }}</span>
                        </template>
                    </el-table-column>
                    <el-table-column label="评估目的" align="center">
                        <template slot-scope="{row}">
                            <span>{{ row.evaluateAim }}</span>
                        </template>
                    </el-table-column>
                    <el-table-column label="扣分人" align="center">
                        <template slot-scope="{row}">
                            <span>{{ row.checkerName }}</span>
                        </template>
                    </el-table-column>
                    <el-table-column label="错误个数" align="center">
                        <template slot-scope="{row}">
                            <span>{{ row.mistakeCount }}</span>
                        </template>
                    </el-table-column>
                    <el-table-column label="加减分" align="center">
                        <template slot-scope="{row}">
                            <span>{{ row.mistakeScore }}</span>
                        </template>
                    </el-table-column>
                    <el-table-column label="扣分时间" align="center">
                        <template slot-scope="{row}">
                            <span>{{ row.checkTime }}</span>
                        </template>
                    </el-table-column>
                    <el-table-column label="扣分原因" align="center">
                        <template slot-scope="{row}">
                            <span>{{ row.reason }}</span>
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
    name: 'majorPerformanceDeduction',
    components: {
        Breadcrumb,
        YPageListLayout
    },
    data() {
        return {
            activeTabName: 'department',
            dialogVisible: false,
            dialogTitle: null,
            listLoading: false,
            // 时间
            selectDate1: [],
            selectDate2: [],
            listQueryByDepartment: {
                page: 1,
                size: 10,
                current: 1,
                // 开始时间
                startTime: null,
                // 结束时间
                endTime: null,
                // 部门id
                departmentId: null
            },
            listQueryByEvaluator: {
                page: 1,
                size: 10,
                current: 1,
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
                // 审核轮次
                checkLoop: null,
                // 错误类型(normal、hard、fatal)
                mistakeType: null,
                // 开始时间
                startTime: null,
                // 结束时间
                endTime: null,
                // 部门id
                departmentId: null,
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
            departmentPerformanceDeduction: [],
            evaluatorPerformanceDeduction: [],
            pageData: { records: [] },
        }
    },
    created() {
        this.selectDate1.push(this.getDefaultStartDate());
        this.selectDate1.push(this.getDefaultEndDate());
        this.selectDate2.push(this.getDefaultStartDate());
        this.selectDate2.push(this.getDefaultEndDate());
        this.getAllotDepartment();
        this.getMajorPerformanceDeductionVO();
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
                this.getMajorPerformanceDeductionVO();
            }
            if (tab.name === 'evaluator') {
                this.getMajorPerformanceDeductionVOByEvaluator();
            }
        },
        // 条件查询
        searchList1() {
            this.getMajorPerformanceDeductionVO()
        },
        // 重置搜索条件
        resetParams1() {
            this.exportStatus = true;
            this.listQueryByDepartment = {
                // 重置分页
                page: 1,
                size: 10,
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
            this.getMajorPerformanceDeductionVO();
        },
        getMajorPerformanceDeductionVO() {
            if (this.selectDate1) {
                this.listQueryByDepartment.startTime = this.selectDate1[0] + ' 00:00:00';
                this.listQueryByDepartment.endTime = this.selectDate1[1] + ' 23:59:59';
            }
            this.$api.statistical.getMajorPerformanceDeductionVO(this.listQueryByDepartment).then(res => {
                if (res.code === 200) {
                    this.departmentPerformanceDeduction = res.data;
                    if (this.listQueryByDepartment.startTime !== null && this.listQueryByDepartment.endTime !== null && res.data.length > 0) {
                        this.exportStatus = false;
                    } else {
                        this.exportStatus = true;
                    }
                }
            })
        },
        exportMajorPerformanceDeductionVO() {
            this.$utils.exportUtil(
                "major/statisticalStatement/department/getMajorPerformanceDeductionVO/export", this.listQueryByDepartment,
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
        setListQueryParms(checkLoop, mistakeType, departmentId, startTime, endTime, userId) {
            this.dialogVisible = true;
            this.listQueryDetail.page = 1;
            this.listQueryDetail.size = 10;
            this.listQueryDetail.current = 1;
            this.listQueryDetail.checkLoop = checkLoop;
            this.listQueryDetail.mistakeType = mistakeType;
            this.listQueryDetail.departmentId = departmentId;
            this.listQueryDetail.startTime = startTime;
            this.listQueryDetail.endTime = endTime;
            if (userId) {
                this.listQueryDetail.principalId = userId;
            } else {
                this.listQueryDetail.principalId = null;
            }
            if (mistakeType === 'normal') {
                this.dialogTitle = checkLoop + "：一般错误列表";
            } else if (mistakeType === 'hard') {
                this.dialogTitle = checkLoop + "：较大错误列表";
            } else if (mistakeType === 'fatal') {
                this.dialogTitle = checkLoop + "：重大错误列表";
            } else {
                this.dialogTitle = "-";
            }
        },
        // 查询错误详情列表
        getMajorPerformanceDeductionDetailVO() {
            this.$api.statistical.getMajorPerformanceDeductionDetailVO(this.listQueryDetail).then(res => {
                if (res.code === 200) {
                    this.pageData = res.data;
                }
            })
        },
        // 错误详情列表导出
        exportPerformanceDeductionDetail() {
            this.$utils.exportUtil(
                "major/statisticalStatement/getMajorPerformanceDeductionDetailVO/export", this.listQueryDetail,
                "导出"
            );
        },
        // 条件查询
        searchList2() {
            this.getMajorPerformanceDeductionVOByEvaluator()
        },
        // 重置搜索条件
        resetParams2() {
            this.exportStatus = true;
            this.listQueryByEvaluator = {
                // 重置分页
                page: 1,
                size: 10,
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
            this.getMajorPerformanceDeductionVOByEvaluator();
        },
        // 评估人员绩效扣分查询
        getMajorPerformanceDeductionVOByEvaluator() {
            if (this.selectDate2) {
                this.listQueryByEvaluator.startTime = this.selectDate2[0] + ' 00:00:00';
                this.listQueryByEvaluator.endTime = this.selectDate2[1] + ' 23:59:59';
            }
            this.$api.statistical.getMajorPerformanceDeductionVOByEvaluator(this.listQueryByEvaluator).then(res => {
                if (res.code === 200) {
                    this.evaluatorPerformanceDeduction = res.data;
                    if (this.listQueryByEvaluator.startTime !== null && this.listQueryByEvaluator.endTime !== null && res.data.length > 0) {
                        this.exportStatus = false;
                    } else {
                        this.exportStatus = true;
                    }
                }
            })
        },
        exportMajorPerformanceDeductionByEvaluator() {
            this.$utils.exportUtil(
                "major/statisticalStatement/evaluator/getMajorPerformanceDeductionVO/export", this.listQueryByEvaluator,
                "导出"
            );
        }
    }
}
</script>