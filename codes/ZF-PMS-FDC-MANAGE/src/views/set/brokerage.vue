<template>
  <div class="app-container organization-index">
    <div class="title-container">
      <breadcrumb id="breadcrumb-container" class="breadcrumb-container"/>
    </div>
    <el-form ref="postForm"
             class="form-container postInfo-container"
             style="position: relative;margin-top: 30px">
      <el-tabs v-model="activeName" @tab-click="handleClick" type="border-card">
        <el-tab-pane label="提成比例" name="first">
          <el-alert
            title="客户经理下单时选择的项目类型和业务来源确定的提成比例，用于计算客户经理的提成金额。"
            type="info"
            show-icon
            :closable="false">
          </el-alert>
          <y-page-list-layout :get-page-list="getMarketerRatioList" :page-list="marketerRatioRecord" :page-para="listQuery1">
            <template slot="left">
              <el-select
                v-model="listQuery1.itemCate"
                placeholder="项目类型"
                clearable
                filterable
                style="margin-left: 20px;width: 200px;float: left;"
                class="filter-item"
                @change="getMarketerRatioList"
              >
                <el-option v-for="item in cateList" :key="item.id" :label="item.name" :value="item.id"/>
              </el-select>
              <el-select
                v-model="listQuery1.itemSource"
                placeholder="业务来源"
                clearable
                filterable
                class="filter-item"
                style="margin-left: 20px;width: 200px;float: left;"
                @change="getMarketerRatioList"
              >
                <el-option v-for="item in businessSourceList" :key="item.id" :label="item.name" :value="item.id"/>
              </el-select>
            </template>
            <el-table
              size="medium"
              slot="table"
              row-key="id"
              v-loading="listLoading"
              :data="marketerRatioRecord.records"
              fit
              highlight-current-row
              :header-row-style="{color: '#333333'}"
              style="border-left: 1px solid #EBECED;border-right: 1px solid #EBECED;color: #333333;"
            >
              <el-table-column label="项目类型" min-width="150" align="center">
                <template slot-scope="{row}">
                  <span>{{ row.itemCateName }}</span>
                </template>
              </el-table-column>
              <el-table-column label="业务来源" min-width="150" align="center">
                <template slot-scope="{row}">
                  <span>{{ row.itemSourceName }}</span>
                </template>
              </el-table-column>
              <el-table-column label="提成比例" min-width="30" align="center">
                <template slot-scope="{row}">
                  <el-input v-model="row.ratio" placeholder="请输入数字">
                    <i slot="suffix" style="font-size:normal;margin-right: 10px;line-height: 30px">%</i>
                  </el-input>
                </template>
              </el-table-column>
              <el-table-column label="操作" align="center" width="250" class-name="small-padding fixed-width">
                <template slot-scope="{row}">
                  <el-button
                    class-name="filter-item"
                    type="primary"
                    @click="updateRatio(row)"
                    round
                  >保存</el-button>
                </template>
              </el-table-column>
            </el-table>
          </y-page-list-layout>
        </el-tab-pane>
        <el-tab-pane label="岗位抽成率" >
          <el-alert
            title="根据不同的管理层岗位确定该岗位人员抽成比例一般用于市场部门，用于计算客户经理的直接或间接领导提成金额;"
            type="info"
            show-icon
            :closable="false">
          </el-alert>
          <y-page-list-layout :get-page-list="getPostRatio" :page-list="postRatioRecord" :page-para="listQuery2">
            <template slot="left">
              <el-select v-model="listQuery2.departmentId" style="width: 200px;margin-left: 20px;" clearable filterable
                         placeholder="所属部门" class="filter-item" @change="getPostRatio"
              >
                <el-option
                  v-for="item in partOptions"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id"
                />
              </el-select>
            </template>
            <el-table
              size="medium"
              slot="table"
              row-key="id"
              v-loading="listLoading"
              :data="postRatioRecord.records"
              fit
              highlight-current-row
              :header-row-style="{color: '#333333'}"
              style="border-left: 1px solid #EBECED;border-right: 1px solid #EBECED;color: #333333;"
            >
              <el-table-column label="部门" min-width="150" align="center">
                <template slot-scope="{row}">
                  <span>{{ row.departmentName }}</span>
                </template>
              </el-table-column>
              <el-table-column label="岗位" min-width="150" align="center">
                <template slot-scope="{row}">
                  <span>{{ row.postName }}</span>
                </template>
              </el-table-column>
              <el-table-column label="提成规则" min-width="200" align="center">
                <template slot-scope="{row}">
                  <el-select v-model="row.brokerageRule" style="width: 200px;margin-left: 20px;" clearable filterable class="filter-item">
                    <el-option key="LAND_MARKETER_RULE" label="土地规划部市场人员规则" value="LAND_MARKETER_RULE" />
                    <el-option key="LAND_SUPERVISOR_RULE" label="土地规划部营销主管规则" value="LAND_SUPERVISOR_RULE" />
                    <el-option key="LAND_MANAGER_RULE" label="土地规划部营销经理规则" value="LAND_MANAGER_RULE" />
                    <el-option key="LAND_OTHER_RULE" label="土地规划部参与人员规则" value="LAND_OTHER_RULE" />
                  </el-select>
                </template>
              </el-table-column>
              <el-table-column label="抽成率" min-width="60" align="center">
                <template slot-scope="{row}">
                  <el-input v-model="row.brokerageRatio" placeholder="请输入数字">
                    <i slot="suffix" style="font-size:normal;margin-right: 10px;line-height: 30px">%</i>
                  </el-input>
                </template>
              </el-table-column>
              <el-table-column label="操作" align="center" width="250" class-name="small-padding fixed-width">
                <template slot-scope="{row}">
                  <el-button
                    class-name="filter-item"
                    type="primary"
                    @click="updatePostRatio(row)"
                    round
                  >保存</el-button>
                </template>
              </el-table-column>
            </el-table>
          </y-page-list-layout>
        </el-tab-pane>
        <el-tab-pane label="预提金额">
          <el-alert
            title="一般用于非市场人员每月固定预提成金额。若员工需每月生成固定预提金额，请在对应员工上配置预提金额。"
            type="info"
            show-icon
            :closable="false">
          </el-alert>
          <y-page-list-layout :get-page-list="getBaseAmount" :page-list="baseAmount" :page-para="listQuery3">
            <template slot="left">
              <el-input
                v-model="listQuery3.description"
                placeholder="请输入内容"
                clearable
                style="margin-left: 20px;width: 320px;float: left;"
                @change="getBaseAmount"
              >
                <el-select slot="prepend" v-model="listQueryKey" style="width: 90px;" placeholder="请选择">
                  <el-option label="全部" value="keyword"/>
                  <el-option label="工号" value="staffNo"/>
                  <el-option label="姓名" value="name"/>
                </el-select>
              </el-input>
            </template>
            <el-table
              size="medium"
              slot="table"
              row-key="id"
              v-loading="listLoading"
              :data="baseAmount.records"
              fit
              highlight-current-row
              :header-row-style="{color: '#333333'}"
              style="border-left: 1px solid #EBECED;border-right: 1px solid #EBECED;color: #333333;"
            >
              <el-table-column label="工号" align="center">
                <template slot-scope="{row}">
                  <span>{{ row.staffNo }}</span>
                </template>
              </el-table-column>
              <el-table-column label="姓名" align="center">
                <template slot-scope="{row}">
                  <span>{{ row.name }}</span>
                </template>
              </el-table-column>
              <el-table-column label="部门-岗位" align="center" width="360">
                <template slot-scope="{row}">
                  <span v-for="(item, index) in row.departmentPostVOList" :key="index">
                    <span v-if="index > 0">、</span>
                    {{ item.departmentName }}-{{ item.postName }}
                  </span>
                </template>
              </el-table-column>
              <el-table-column label="默认提成金额" align="center" width="300">
                <template slot-scope="{row}">
                  <el-input v-model="row.baseAmount" placeholder="请输入数字">
                    <i slot="suffix" style="font-size:normal;margin-right: 10px;line-height: 30px">￥</i>
                  </el-input>
                </template>
              </el-table-column>
              <el-table-column label="操作" align="center" width="100">
                <template slot-scope="{row}">
                  <el-button
                    class-name="filter-item"
                    type="primary"
                    @click="updateBaseAmount(row)"
                    round
                  >保存</el-button>
                </template>
              </el-table-column>
            </el-table>
          </y-page-list-layout>
        </el-tab-pane>
      </el-tabs>
    </el-form>
  </div>
</template>
<script>
  import Breadcrumb from '@/components/Breadcrumb'
  import PermissionButton from '@/components/PermissionButton/PermissionButton'
  import YPageListLayout from '@/components/YPageListLayout'

  export default {
    name: 'rateEntry',
    components: {
      Breadcrumb,
      YPageListLayout
    },
    filters: {
      statusFilter(status) {
        const statusMap = {
          published: 'success',
          draft: 'info',
          deleted: 'danger'
        };
        return statusMap[status]
      }
    },
    data() {
      return {
        activeName: 'first',
        postForm:{},
        marketerRatioRecord: {},
        baseAmount:{},
        itemPageData: {},
        postRatioRecord:{ records: [] },
        listLoading: true,
        listQuery1: {
          page: 1,
          size: 10,
          descs: 'id',
        },
        listQuery2: {
          page: 1,
          size: 10,
          descs: 'id',
        },
        listQuery3: {
          page: 1,
          size: 10,
          descs: 'id',
        },
        cateList:[],
        businessSourceList:[],
        partOptions: [],
        listQueryKey: 'keyword',
      }
    },
    created() {
      this.getMarketerRatioList();
      this.getCateList();
      this.getBusinessSourceList();
      this.getPartSelect();
    },
    methods: {
      getPartSelect() {
        this.$api.department.simpleAll().then(res => {
          this.partOptions = res.data
        })
      },
      getBusinessSourceList(){
        this.$api.dictData.simpleType("业务来源").then(res => {
          this.businessSourceList = res.data
        })
      },
      getCateList(){
        this.$api.dictData.simpleType("项目类型").then(res => {
          this.cateList = res.data
        })
      },
      updateBaseAmount(row){
        this.$api.baseAmount.edit(row).then(res => {
          if (res.code === 200) {
            this.$notify({
              title: '成功',
              message: '修改成功',
              type: 'success',
              duration: 2000
            })
            this.getBaseAmount();
          }
        }).catch(() => {
        });
      },
      updatePostRatio(row){
        this.$api.postRatio.edit(row).then(res => {
          if (res.code === 200) {
            this.$notify({
              title: '成功',
              message: '修改成功',
              type: 'success',
              duration: 2000
            })
          }
          this.getPostRatio();
        }).catch(() => {
        });
      },
      getBaseAmount(){
        const that = this;
        this.listLoading = true;
        const key = {};
        key[this.listQueryKey] = this.listQuery3.description;
        this.$api.baseAmount.list(Object.assign({}, that.listQuery3, key)).then(res => {
          that.baseAmount = res.data;
          setTimeout(() => {
            that.listLoading = false
          }, 200)
        }).catch(() => {
          that.listLoading = false
        });
      },
      updateRatio(row){
        this.$api.marketerRatio.edit(row).then(res => {
          if (res.code === 200) {
            this.$notify({
              title: '成功',
              message: '修改成功',
              type: 'success',
              duration: 2000
            });
            this.getMarketerRatioList();
          }
        }).catch(() => {
        });
      },
      handleClick(tab, event) {
        if (tab.label==='预提金额'){
          this.getBaseAmount();
        }else if (tab.label==='配置项') {
          this.getGlobalConfig();
        }else if (tab.label === '岗位抽成率'){
          this.getPostRatio();
        } else {
          this.getMarketerRatioList();
        }
      },
      getPostRatio(){
        const that = this;
        this.listLoading = true;
        this.$api.postRatio.list(that.listQuery2).then(res => {
          that.postRatioRecord = res.data;
          setTimeout(() => {
            that.listLoading = false
          }, 200)
        }).catch(() => {
          that.listLoading = false
        });
      },
      resetTemp() {
        this.temp = {
          code: '',
          name: 1,
          parentId: '',
          phone: ''
        }
      },
      searchList() {
        this.listQuery.current = 1;
      },
      getMarketerRatioList() {
        const that = this;
        this.listLoading = true;
        this.$api.marketerRatio.list(that.listQuery1).then(res => {
          that.marketerRatioRecord = res.data;
          setTimeout(() => {
            that.listLoading = false
          }, 200)
        }).catch(() => {
          that.listLoading = false
        });
      },
    }
  }
</script>
<style lang="scss">
  .organization-index {
    .el-table [class*=el-table__row--level] .el-table__expand-icon {
      color: red;
    }
  }
</style>
<style scoped lang="scss">
</style>
