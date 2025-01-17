<template>
    <div class="app-container">
        <div class="title-container">
            <breadcrumb id="breadcrumb-container" class="breadcrumb-container" />
        </div>

        <y-page-list-layout :page-list="pageData" :page-para="listQuery" :get-page-list="getList">
            <template slot="left">
                <el-button class-name="filter-item" type="danger" size="mini" round style="float: left"
                    @click="createReport">创建报告</el-button>
                <el-input v-model="listQuery.title" placeholder="报告号" clearable
                    style="margin-left: 20px; width: 220px;float: left;">
                </el-input>
                <el-input v-model="listQuery.creator" placeholder="创建人" clearable
                    style="margin-left: 20px; width: 220px;float: left;">
                </el-input>
                <el-input v-model="listQuery.checker" placeholder="审核人" clearable
                    style="margin-left: 20px; width: 220px;float: left;">
                </el-input>
                <el-select v-model="listQuery.status" filterable style="margin-left: 20px; width: 120px;float: left;" 
                placeholder="请选择" clearable>
                    <el-option
                        v-for="(item,index) in statuses"
                        :key="index"
                        :label="item"
                        :value="item"
                    />
                </el-select>
                <el-button class="filter-item" style="margin-left: 10px;float: left;" type="primary" @click="searchList"
                    round>搜索
                </el-button>
                <el-button class="filter-item" style="float: left;" round type="warning" @click="resetSearch()">重置
                </el-button>
            </template>
            <parentTable v-loading="listLoading" :data="pageData.records" slot="table" style="width: 100%;">
                <el-table-column label="报告号" align="center">
                    <template slot-scope="{row}">
                        <span>{{ row.title }}</span>
                    </template>
                </el-table-column>
                <el-table-column label="报告类型" align="center">
                    <template slot-scope="{row}">
                        <span>{{ row.reportType==='LAND_RISK_FTL'?'社会风险稳定性评价报告':'' }}</span>
                    </template>
                </el-table-column>
                <el-table-column label="报告状态" align="center">
                    <template slot-scope="{row}">
                        <span>{{row.status }}</span>
                    </template>
                </el-table-column>
                <el-table-column label="创建人" align="center" width="180">
                    <template slot-scope="{row}">
                        <span>{{ row.creator }}</span>
                    </template>
                </el-table-column>
                <el-table-column label="审核人" align="center" width="180">
                    <template slot-scope="{row}">
                        <span>{{ row.checker!=null?row.checker:'-' }}</span>
                    </template>
                </el-table-column>
                <el-table-column label="报告文档" align="center">
                    <template slot-scope="{row}">
                        <el-button v-if="row.url" type="success" plain round
                            @click="downloadContract(row.url,row.title)">下载</el-button>
                        <el-button v-else plain round disabled>下载</el-button>
                    </template>
                </el-table-column>
                <el-table-column label="创建时间" align="center">
                    <template slot-scope="{row}">
                        <span>{{ row.created }}</span>
                    </template>
                </el-table-column>
                <el-table-column label="操作" align="center" width="250">
                    <template slot-scope="{row}">
                        <PermissionButton menu-code="_views_report_main_edit" class-name="filter-item" name=""
                            type="primary" round size="mini" @click="edit(row.id)" :page-jump="true" :page-query="{ id: row.id,title:row.title }"  />
                        <!-- <PermissionButton menu-code="_views_report_editor" class-name="filter-item" name=""
                        type="warning" round size="mini" @click="documentEditor(row)"  /> -->
                        <PermissionButton menu-code="_views_report_main_remove" class-name="filter-item" name=""
                            type="danger" round size="mini" @click="deleteInfo(row.id)" />
                    </template>
                </el-table-column>
            </parentTable>
        </y-page-list-layout>
        <!-- <el-dialog :visible.sync="dialogVisible" center @closed="closeDialog" width="25%" >
            <div style="height: auto;">
                <el-select style="width: 400px;" v-model="reportType" placeholder="请选择你要创建的报告类型">
                <el-option v-for="(r, id) in reportTypes" :key="r.id" :label="r.headline" :value="r.id" />
            </el-select>
            </div>
            <span slot="footer" class="dialog-footer">
                <el-button @click="dialogVisible = false">取 消</el-button>
                <el-button type="primary" @click="createReport" :disabled="reportType==null">创 建</el-button>
            </span>
        </el-dialog> -->
    </div>
</template>
<script>
import YPageListLayout from '@/components/YPageListLayout'
import Breadcrumb from '@/components/Breadcrumb'
import PermissionButton from '@/components/PermissionButton/PermissionButton'

export default {
    name: 'risk',
    components: {
        Breadcrumb,
        YPageListLayout,
        PermissionButton,
    },
    data() {
        return {
            dialogVisible: false,
            pageData: { records: [] },
            total: 20,
            listLoading: true,
            listQuery: {
                page: 1,
                size: 10,
                descs: 'id',
                reportType:'LAND_RISK_FTL'
            },
            reportTypes: [],
            reportType: null,
            statuses:["编辑中","审核中","审核通过","审核拒绝","已完成","已归档",],
            newReport:{
                "reportType": 'LAND_RISK_FTL',
                "prefix":"RISK"
            }
        }
    },
    created() {
        this.getList();
        this.getReportTypes();
    },
    methods: {
        resetSearch() {
            this.$router.push({ query: {} });
            this.listQuery = {
                current: 1,
                size: 10,
                descs: 'id',
                reportType:'LAND_RISK_FTL'
            }
            this.getList()
        },
        searchList() {
            // 重置分页
            this.listQuery.page = 1
            this.listQuery.size = 20
            this.getList()
        },
        getList() {
            const that = this
            this.listLoading = true
            this.$api.reportMain.list(Object.assign({}, that.listQuery)).then((res) => {
                that.pageData = res.data;
                setTimeout(() => {
                    that.listLoading = false
                }, 200)
            })
                .catch(() => {
                    that.listLoading = false
                })
        },
        deleteInfo(id) {
            const that = this
            that.$confirm('请确认是否删除该数据?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning',
                center: true
            }).then(() => {
                that.$api.reportMain.delete(id).then(data => {
                    that.loading = false
                    if (data.code === 200) {
                        that.getList()
                    } else {
                        this.$message({
                            type: 'error',
                            message: data.msg
                        })
                    }
                })
            }).catch(() => {
            })
        },
        createReport() {
            let that = this;
            that.$confirm('请确认报告类型是否正确?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning',
                center: true
            }).then(() => {
                that.$api.reportMain.add(this.newReport).then(data => {
                    that.loading = false
                    if (data.code === 200) {
                        that.getList();
                        that.dialogVisible = false;
                        that.$notify({
                            title: '成功',
                            message: '创建报告成功',
                            type: 'success',
                            duration: 2000
                        })
                    } else {
                        that.$message({
                            type: 'error',
                            message: data.msg
                        })
                    }
                })
            }).catch(() => {
            })
        },
        getReportTypes() {
            this.$api.reportHeadline.getReportType().then(res => {
                if (res.code === 200) {
                    this.reportTypes = res.data;
                }
            })
        },
        closeDialog() {
            this.reportType = null;
        },
        downloadContract(url,title) {
            var a = document.createElement('a');
            var event = new MouseEvent('click');
            a.download = title+".docx";
            console.log(a.download)
            a.href = url;//路径前拼上前缀，完整路径
            a.dispatchEvent(event);
        },
        documentEditor(row) {
            let routeUrl = this.$router.resolve({
                path: "/document/window",
                query: {key:row.reportKey,url:row.url,title:row.title}
        });
        window.open(routeUrl.href, '_blank');
        },
    },
}
</script>
<style lang="scss" scoped>
.right {
    flex: 1;

    .title {
        font-size: 16px;
        font-weight: 500;
        color: rgba(51, 51, 51, 1);
        line-height: 35px;
        margin-bottom: 8px;
    }

    .menu-2-box {
        display: flex;
        flex-wrap: wrap;
        width: 100%;
    }

    .menu-2-item {
        display: flex;
        align-items: center;
        color: #656565;
        font-size: 12px;
        width: 230px;
        height: 101px;
        background: rgb(255, 185, 129);
        border-radius: 3px;
        padding-left: 20px;
        margin-right: 10px;
        margin-bottom: 10px;
        cursor: pointer;
        box-shadow: 0 2px 4px rgba(0, 0, 0, 0.12), 0 0 6px rgba(0, 0, 0, 0.04);

        .text {
            margin-left: 16px;
        }
    }
}
</style>
  