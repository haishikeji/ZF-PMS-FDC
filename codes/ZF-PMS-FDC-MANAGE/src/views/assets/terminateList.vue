<template>
    <div class="app-container">
        <div class="title-container">
            <breadcrumb id="breadcrumb-container" class="breadcrumb-container" />
        </div>
        <y-page-list-layout :page-list="pageData" :page-para="listQuery" :get-page-list="selectTerminatedPage">
            <template slot="left">
                <el-input style="margin-left: 20px;width: 300px;float: left;" class="filter-item"
                    v-model="listQuery.keyWord" placeholder="项目编号/报告号/项目名称/业务来源" clearable>
                </el-input>
                <el-select v-model="listQuery.principalId" filterable placeholder="项目负责人（可搜索）"
                    style=" width: 200px;margin-left: 10px;float: left;">
                    <el-option v-for="(u, id) in allUsers" :label="u.name" :value="u.id"></el-option>
                </el-select>
                <el-select v-model="listQuery.clientManagerId" filterable placeholder="客户经理（可搜索）"
                    style=" width: 200px;margin-left: 10px;float: left;">
                    <el-option v-for="(u, id) in allUsers" :label="u.name" :value="u.id"></el-option>
                </el-select>
                <el-select v-model="listQuery.assetsBusinessGener" filterable placeholder="业务类型"
                    style=" width: 200px;margin-left: 10px;float: left;">
                    <el-option v-for="(s, value) in assetsBusinessGeners" :label="s.label" :value="s.value"></el-option>
                </el-select>
                <el-select v-model="listQuery.departmentId" filterable placeholder="接单部门"
                    style=" width: 200px;margin-left: 10px;float: left;">
                    <el-option v-for="(d, id) in allotDepartment" :label="d.name" :value="d.id"></el-option>
                </el-select>
                <el-button class="filter-item" style="margin-left: 10px;float: left;" type="primary" @click="searchList"
                    round>搜索
                </el-button>
                <el-button class="filter-item" style="float: left;" round type="success" @click="resetParams()">重置
                </el-button>
            </template>
            <parentTable ref="table" v-loading="listLoading" :data="pageData.records" slot="table" style="width: 100%;">
                <el-table-column label="项目编号" align="center" width="150">
                    <template slot-scope="{row}">
                        <span>{{ row.orderId }}</span>
                    </template>
                </el-table-column>
                <el-table-column label="项目名称" align="center" width="300">
                    <template slot-scope="{row}">
                        <span>{{ row.reportName || row.statementName || row.name }}</span>
                    </template>
                </el-table-column>
                <el-table-column label="业务类型" align="center" width="150">
                    <template slot-scope="{row}">
                        <span>{{ row.assetsBusinessGener }}</span>
                    </template>
                </el-table-column>
                <el-table-column label="意见书号" align="center" width="250">
                    <template slot-scope="{row}">
                        <span>{{ row.statementNo == null ? '-' : row.statementNo }}</span>
                    </template>
                </el-table-column>
                <el-table-column label="报告号" align="center" width="250">
                    <template slot-scope="{row}">
                        <span>{{ row.reportNo == null ? '-' : row.reportNo }}</span>
                    </template>
                </el-table-column>
                <el-table-column label="业务类型" align="center" width="150">
                    <template slot-scope="{row}">
                        <span>{{ row.assetsBusinessGener }}</span>
                    </template>
                </el-table-column>
                <el-table-column label="客户名称" align="center" width="150">
                    <template slot-scope="{row}">
                        <span>{{ row.customerName }}</span>
                    </template>
                </el-table-column>
                <el-table-column label="业务来源" align="center" width="150">
                    <template slot-scope="{row}">
                        <span>{{ row.customerSubName }}</span>
                    </template>
                </el-table-column>
                <el-table-column label="委托人" align="center" width="150">
                    <template slot-scope="{row}">
                        <span>{{ row.bailor == null ? '-' : row.bailor }}</span>
                    </template>
                </el-table-column>
                <el-table-column label="委托人联系电话" align="center" width="150">
                    <template slot-scope="{row}">
                        <span>{{ row.bailorContactTel == null ? '-' : row.bailorContactTel }}</span>
                    </template>
                </el-table-column>
                <el-table-column label="项目负责人" align="center" width="150">
                    <template slot-scope="{row}">
                        <span>{{ row.principalName }}</span>
                    </template>
                </el-table-column>
                <el-table-column label="客户经理" align="center" width="150">
                    <template slot-scope="{row}">
                        <span>{{ row.clientManagerName }}</span>
                    </template>
                </el-table-column>
                <el-table-column label="下单时间" align="center" width="200">
                    <template slot-scope="{row}">
                        <span>{{ row.created }}</span>
                    </template>
                </el-table-column>
                <el-table-column label="终止原因" align="center" width="150">
                    <template slot-scope="{row}">
                        <span>{{ row.comment == null ? '-' : row.comment }}</span>
                    </template>
                </el-table-column>
                <el-table-column label="操作" align="center" width="200" fixed="right">
                    <template slot-scope="{row}">
                        <el-button type="text" @click="orderDetail(row)">查看</el-button>
                        <el-button type="text" @click="toAddAssetsOrder(row)">重新下单</el-button>
                    </template>
                </el-table-column>
            </parentTable>
        </y-page-list-layout>
    </div>
</template>

<script>
import YPageListLayout from '@/components/YPageListLayout'
import Breadcrumb from '@/components/Breadcrumb'
import PermissionButton from '@/components/PermissionButton/PermissionButton'

export default {
    name: 'AssetsTerminateList',
    components: {
        Breadcrumb,
        YPageListLayout,
        PermissionButton,
    },
    data() {
        return {
            pageData: { records: [] },
            listLoading: false,
            listQuery: {
                page: 1,
                size: 10,
                current: 1,
                // 节点code
                nodeCode: null,
                // 客户经理id
                clientManagerId: null,
                // 项目负责人id
                principalId: null,
                // 业务类型
                assetsBusinessGener: null,
                // 关键字
                keyWord: null,
                departmentId: null
            },
            // 接单部门
            allotDepartment: [],
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
            ]
        }
    },
    created() {
        // 获取用户下拉列表
        this.getAllUser();
        // 获取分单部门
        this.getAllotDepartment();
        // 获取资产业务订单
        this.selectTerminatedPage();
    },
    methods: {
        // 无条件分页查询
        selectTerminatedPage() {
            this.$api.assets.selectTerminatedPage(this.listQuery).then(res => {
                if (res.code === 200) {
                    this.pageData = res.data;
                }
            })
        },
        // 条件查询
        searchList() {
            // 重置分页
            this.listQuery.page = 1
            this.listQuery.size = 10
            this.selectTerminatedPage()
        },
        // 重置搜索条件
        resetParams() {
            this.$router.push({ query: {} });
            this.listQuery = {
                current: 1,
                size: 10,
            }
            this.selectTerminatedPage();
        },
        // 获取所有用户下拉列表
        getAllUser() {
            this.$api.user.simpleAll().then(res => {
                if (res.code === 200) {
                    this.allUsers = res.data;
                }
            })
        },
        getNodes() {
            this.$api.workNode.enum().then(res => {
                if (res.code === 200) {
                    this.nodes = res.data
                }
            })
        },
        orderDetail(row) {
            const newRow = {
                assetsId: row.assetsId,
                statementNo: row.statementNo,
                reportNo: row.reportNo,
            }
            this.$router.push({
                path: "/assets/orderDetail",
                query: {
                    row: JSON.stringify(newRow),
                    back: '/assets/allList',
                    couldEdit: false,
                    couldBack: false,
                    disabledStatus: true
                }
            })
        },
        toAddAssetsOrder(row) {
            this.$router.push({
                path: "/assets/addOrder",
                query: {
                    row: JSON.stringify(row),
                    back: '/assets/terminateList',
                    couldEdit: true,
                    couldBack: true,
                    disabledStatus: false,
                    reorder: true
                }
            })
        },
        // 获取部门下拉列表
        getAllotDepartment() {
            this.$api.department.allot('ASSET_BUSINESS').then(res => {
                if (res.code === 200) {
                    this.allotDepartment = res.data;
                }
            })
        }
    }
}
</script>

<style lang="scss" scoped></style>