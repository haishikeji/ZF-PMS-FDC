<template>
    <div class="app-container organization-index">
        <div class="title-container">
            <breadcrumb id="breadcrumb-container" class="breadcrumb-container" />
        </div>
        <div>
            <div style="width: 100%; height: 50px;">
                <el-date-picker style="margin-right: 20px;float: left;" v-model="selectDate" type="daterange"
                    :picker-options="pickerOptions" format="yyyy 年 MM 月 dd 日" value-format="yyyy-MM-dd" range-separator="至"
                    start-placeholder="开始日期" end-placeholder="结束日期" align="right">
                </el-date-picker>
                <el-button class="filter-item" style="margin-left: 10px;float: left;" type="primary" @click="searchList()"
                    round>搜索
                </el-button>
                <el-button class="filter-item" round type="success" @click="resetParams()">重置
                </el-button>
            </div>
            <el-table :data="depPerformanceOverview" fit stripe highlight-current-row
                :header-row-style="{ color: '#333333', 'font-size': '14px' }" style=" width: 100%; float: right;
                      border-left: 1px solid #ebeced;
                      border-right: 1px solid #ebeced;
                      color: #333333; font-size: 14px;">
                <el-table-column label="评估部门">
                    <template slot-scope="{row}">
                        <span>{{ row.departmentName }}</span>
                    </template>
                </el-table-column>
                <el-table-column label="业务总量">
                    <el-table-column label="产值">
                        <template slot-scope="{row}">
                            <span>{{ row.outputValue }}</span>
                        </template>
                    </el-table-column>
                    <el-table-column label="产值计分">
                        <template slot-scope="{row}">
                            <span :style="{ color: row.outputValueScore > 0 ? 'red' : 'black' }">{{ row.outputValueScore
                            }}</span>
                        </template>
                    </el-table-column>
                    <el-table-column label="接件数">
                        <template slot-scope="{row}">
                            <span>{{ row.orderVolume }}</span>
                        </template>
                    </el-table-column>
                    <el-table-column label="接件计分">
                        <template slot-scope="{row}">
                            <span :style="{ color: row.orderVolumeScore > 0 ? 'red' : 'black' }">{{ row.orderVolumeScore
                            }}</span>
                        </template>
                    </el-table-column>
                </el-table-column>
                <el-table-column label="业务效率">
                    <el-table-column label="报价效率（总时长/总件数，单位：天/件）">
                        <el-table-column label="金融">
                            <template slot-scope="{row}">
                                <span>{{ row.financialQuoteProcessingTime }}</span>
                            </template>
                        </el-table-column>
                        <el-table-column label="金融效率计分">
                            <template slot-scope="{row}">
                                <span :style="{ color: row.financialQuoteProcessingTimeScore > 0 ? 'red' : 'black' }">{{
                                    row.financialQuoteProcessingTimeScore }}</span>
                            </template>
                        </el-table-column>
                        <el-table-column label="非金融">
                            <template slot-scope="{row}">
                                <span>{{ row.unFinancialQuoteProcessingTime }}</span>
                            </template>
                        </el-table-column>
                        <el-table-column label="非金融效率计分">
                            <template slot-scope="{row}">
                                <span :style="{ color: row.unFinancialQuoteProcessingTimeScore > 0 ? 'red' : 'black' }">{{
                                    row.unFinancialQuoteProcessingTimeScore }}</span>
                            </template>
                        </el-table-column>
                    </el-table-column>
                </el-table-column>
                <el-table-column label="质量">
                    <el-table-column label="复审">
                        <el-table-column label="复审错误分类">
                            <template slot-scope="{row}">
                                <span :style="{ color: row.reCheckFatalMistakeCount > 0 ? 'red' : 'black' }">重大错误 x {{
                                    row.reCheckFatalMistakeCount }}</span><br />
                                <span :style="{ color: row.reCheckHardMistakeCount > 0 ? 'red' : 'black' }">较大错误 x {{
                                    row.reCheckHardMistakeCount }}</span><br />
                                <span :style="{ color: row.reCheckNormalMistakeCount > 0 ? 'red' : 'black' }">一般错误 x {{
                                    row.reCheckNormalMistakeCount }}</span><br />
                            </template>
                        </el-table-column>
                        <el-table-column label="复审（扣分）">
                            <template slot-scope="{row}">
                                <span>{{ row.reCheckTotalScore }}</span>
                            </template>
                        </el-table-column>
                    </el-table-column>
                    <el-table-column label="抽检">
                        <el-table-column label="抽检错误分类">
                            <span>重大错误 x 0</span><br />
                            <span>较大错误 x 0</span><br />
                            <span>一般错误 x 0</span><br />
                        </el-table-column>
                        <el-table-column label="抽检加减分">
                            <span>0</span>
                        </el-table-column>
                    </el-table-column>
                    <el-table-column label="投诉">
                        <el-table-column label="投诉分类" width="160">
                            <span>外部投诉 x 0</span><br />
                            <span>内部重大错误 x 0</span><br />
                            <span>内部较大错误 x 0</span><br />
                            <span>内部一般错误 x 0</span><br />
                            <span>内部服务态度恶劣 x 0</span><br />
                        </el-table-column>
                        <el-table-column label="投诉（扣分）">
                        </el-table-column>
                    </el-table-column>
                </el-table-column>
                <el-table-column label="复杂疑难项目加分">
                    <el-table-column label="复审">
                        <el-table-column label="涉及项目数量">
                        </el-table-column>
                        <el-table-column label="总计分">
                        </el-table-column>
                    </el-table-column>
                    <el-table-column label="全部门综合评分" width="120">
                    </el-table-column>
                    <el-table-column label="总经理加减分">
                    </el-table-column>
                </el-table-column>
                <el-table-column label="时点累计分值">
                    <template slot-scope="{row}">
                        <span style="color: red;font-weight: bold;">{{ row.timePointTotalScore }}</span>
                    </template>
                </el-table-column>
            </el-table>
        </div>
    </div>
</template>

<script>
import Breadcrumb from '@/components/Breadcrumb'
import YPageListLayout from '@/components/YPageListLayout'

export default {
    name: 'majorDepartmentPerformanceOverview',
    components: {
        Breadcrumb,
        YPageListLayout
    },
    data() {
        return {
            listLoading: false,
            // 时间
            selectDate: [],
            listQuery: {
                // 开始时间
                startTime: null,
                // 结束时间
                endTime: null
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
            depPerformanceOverview: []
        }
    },
    created() {
        this.selectDate.push(this.getDefaultStartDate());
        this.selectDate.push(this.getDefaultEndDate());
        this.getMajorDepPerformanceOverviewVO();
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
        // 条件查询
        searchList() {
            this.getMajorDepPerformanceOverviewVO()
        },
        // 重置搜索条件
        resetParams() {
            this.exportStatus = true;
            this.listQuery = {
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
            this.selectDate = [];
            this.selectDate.push(this.getDefaultStartDate());
            this.selectDate.push(this.getDefaultEndDate());
            this.getMajorDepPerformanceOverviewVO();
        },
        getMajorDepPerformanceOverviewVO() {
            if (this.selectDate) {
                this.listQuery.startTime = this.selectDate[0] + ' 00:00:00';
                this.listQuery.endTime = this.selectDate[1] + ' 23:59:59';
            }
            this.$api.statistical.getMajorDepPerformanceOverviewVO(this.listQuery).then(res => {
                if (res.code === 200) {
                    this.depPerformanceOverview = res.data;
                    if (this.listQuery.startTime !== null && this.listQuery.endTime !== null && res.data.length > 0) {
                        this.exportStatus = false;
                    } else {
                        this.exportStatus = true;
                    }
                }
            })
        }
    }
}
</script>