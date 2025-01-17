<template>
  <div class="app-container">
    <div class="title-container">
      <breadcrumb id="breadcrumb-container" class="breadcrumb-container"/>
    </div>
    <y-page-list-layout :page-list="pageData" :page-para="listQuery" :get-page-list="getList">
      <template slot="left">
        <el-button
          class-name="filter-item"
          type="primary"
          size="mini"
          round
          style="float: left"
          @click="openCreateTeam"
        >创建团队</el-button>
        <el-input
          v-model="listQuery.name"
          placeholder="团队名称"
          prefix-icon="el-icon-search"
          style="margin-left: 20px;width: 200px;"
          clearable
          class="filter-item"
        />
        <el-input
          v-model="listQuery.cultivateName"
          placeholder="上级领导"
          prefix-icon="el-icon-search"
          style="margin-left: 20px;width: 200px;"
          clearable
          class="filter-item"
        />
        <el-button style="margin-left: 20px;" class="filter-item" type="primary" round @click="searchList">
          搜索
        </el-button>
        <el-button class="filter-item" round type="warning" @click="resetSearch()">
          重置
        </el-button>
      </template>
      <parentTable
        v-loading="listLoading"
        :data="pageData.records"
        slot="table"
        style="width: 100%;"
      >
        <el-table-column type="expand">
          <template slot-scope="{row}">
            <parentTable style="font-size: 12px;color: #8c939d" v-loading="listLoading" inline :data="row.members">
              <el-table-column label="组员名称" align="center">
                <template slot-scope="{row}">
                  <span>{{ row.memberName }}</span>
                </template>
              </el-table-column>
              <el-table-column label="组员岗位" align="center">
                <template slot-scope="{row}">
                  <span>{{ row.memberPost}}</span>
                </template>
              </el-table-column>
              <el-table-column label="加入时间" align="center">
                <template slot-scope="{row}">
                  <span>{{ row.created }}</span>
                </template>
              </el-table-column>
              <el-table-column label="操作" align="center">
                <template slot-scope="{row}">
                  <el-button
                    class-name="filter-item"
                    name=""
                    type="danger"
                    round
                    size="mini"
                    @click="removeMember(row.id)"
                  >移除组员</el-button>
                </template>
              </el-table-column>
            </parentTable>
          </template>
        </el-table-column>
        <el-table-column label="团队名称" align="center" width="220">
          <template slot-scope="{row}">
            <span>{{ row.name }}</span>
          </template>
        </el-table-column>
        <el-table-column label="营销主管" align="center" width="220">
          <template slot-scope="{row}">
            <span>{{ row.supervisorName }}</span>
          </template>
        </el-table-column>
        <el-table-column label="上级领导" align="center" width="220">
          <template slot-scope="{row}">
            <span>{{ row.cultivateName }}</span>
          </template>
        </el-table-column>
        <el-table-column label="领导岗位" align="center" width="220">
          <template slot-scope="{row}">
            <span>{{ row.cultivatePost }}</span>
          </template>
        </el-table-column>
        <el-table-column label="创建时间" align="center" width="220">
          <template slot-scope="{row}">
            <span>{{ row.created }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center">
          <template slot-scope="{row}">
            <el-button
              class-name="filter-item"
              name=""
              type="primary"
              round
              size="mini"
              @click="openAddMembers(row.id)"
            >添加组员</el-button>
            <el-button
              class-name="filter-item"
              name=""
              type="danger"
              round
              size="mini"
              @click="deleteInfo(row.id)"
            >解散团队</el-button>
          </template>
        </el-table-column>
      </parentTable>
    </y-page-list-layout>
    <el-dialog
      width="600px"
      title="新增团队"
      :visible.sync="dialogFormVisible"
      :close-on-click-modal="false"
    >
      <el-form
        ref="dataForm"
        v-loading="dialogLoading"
        :rules="rules"
        :model="team"
        label-position="right"
        label-width="110px"
        style="width: 400px; margin-left:50px;"
      >
        <el-form-item label="团队名称：" prop="name">
          <el-input v-model="team.name" class="filter-item"/>
        </el-form-item>

        <el-form-item label="营销主管：" filterable prop="supervisorId">
          <el-select v-model="team.supervisorId" filterable class="filter-item" style="float: left;width: 100%;" placeholder="请选择">
            <el-option
              v-for="s in supervisors"
              :key="s.id"
              :label="s.name"
              :value="s.id"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="上级经理：" filterable prop="cultivateId">
          <el-select v-model="team.cultivateId" filterable class="filter-item" style="float: left;width: 100%;" placeholder="请选择">
            <el-option
              v-for="c in cultivater"
              :key="c.id"
              :label="c.name"
              :value="c.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="">
          <el-button @click="dialogFormVisible = false">
            取消
          </el-button>
          <el-button type="primary" @click="createData()">
            保存
          </el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
    <el-dialog
      width="800px"
      title="添加组员"
      :visible.sync="dialogFormVisible1"
      :close-on-click-modal="false"
    >
      <y-page-list-layout :get-page-list="getAddMember">
        <parentTable
          ref="multipleTable"
          v-loading="listLoading1"
          :data="toAddMembers"
          slot="table"
          style="width: 100%;"
          tooltip-effect="dark"
          :selection-change="selectionChange"
        >
          <el-table-column
            type="selection"
            width="55">
          </el-table-column>
          <el-table-column label="工号" align="center" width="150">
            <template slot-scope="{row}">
              <span>{{ row.staffNo }}</span>
            </template>
          </el-table-column>
          <el-table-column label="姓名" align="center" width="150">
            <template slot-scope="{row}">
              <span>{{ row.name }}</span>
            </template>
          </el-table-column>
          <el-table-column label="性别" align="center" width="150">
            <template slot-scope="{row}">
              <span>{{ row.sex }}</span>
            </template>
          </el-table-column>
          <el-table-column label="所属部门-岗位" align="center">
            <template slot-scope="{row}">
              <span v-for="(item, index) in row.departmentPostVOList" :key="index">
                <span v-if="index > 0">、</span>
                {{ item.departmentName }}-{{ item.postName }}
              </span>
            </template>
          </el-table-column>
        </parentTable>
      </y-page-list-layout>
      <div slot="footer" class="dialog-footer" style="text-align: center">
        <el-button @click="dialogFormVisible1 = false">
          取消
        </el-button>
        <el-button type="primary" @click="addMembers">
          保存
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
  import YPageListLayout from '@/components/YPageListLayout'
  import Breadcrumb from '@/components/Breadcrumb'
  import PermissionButton from '@/components/PermissionButton/PermissionButton'

  export default {
    name: 'ViewsMarketTeamList',
    components: {
      Breadcrumb,
      YPageListLayout,
      PermissionButton,
    },
    filters: {
      statusFilter(status) {
        const statusMap = {
          published: 'success',
          draft: 'info',
          deleted: 'danger',
        }
        return statusMap[status]
      },
    },
    data() {
      return {
        dialogFormVisible1:false,
        toAddMembers:[],
        listLoading1:false,
        supervisors:[],
        cultivater:[],
        dialogFormVisible: false,
        activeName: 'first',
        tableKey: 0,
        pageData: { records: [] },
        total: 20,
        listLoading: true,
        dialogLoading: false,
        team:{},
        listQuery: {
          page: 1,
          size: 10,
          descs: 'id',
          departmentName:'市场部'
        },
        rules: {
          name: [{
            required: true,
            message: '请填写团队名称',
            trigger: 'blur'
          }],
          supervisorId: [{
            required: true,
            message: '请选择营销主管',
            trigger: 'change'
          }],
          cultivateId: [{
            required: true,
            message: '请选择上级经理',
            trigger: 'change'
          }]
        },
        multipleSelection:[],
        addTeam:{
          id:null,
          memberIds:[]
        }
      }
    },
    created() {
      const that = this;
      if (that.$route.query.current && !isNaN(that.$route.query.current)) {
        that.listQuery.current = parseInt(that.$route.query.current);
      }
      that.getList();
    },
    methods: {
      openAddMembers(teamId) {
        this.addTeam = {};
        this.addTeam.id = teamId;
        this.dialogFormVisible1 = true;
        this.getAddMember();
      },
      selectionChange(val){
        this.multipleSelection = val;
        console.log('arr', val)
      },
      addMembers(){
        const members = this.multipleSelection;
        const ids = [];
        if (members.length>0){
          members.forEach(item=>{
            ids.push(item.id);
          });
          this.addTeam.memberIds = ids;
        }else {
          this.$notify({
            title: '错误',
            message: '团队成员不能为空',
            type: 'error',
            duration: 2000
          });
          return;
        }
        this.$api.teamMember.add(Object.assign({}, this.addTeam,
        )).then(res => {
          if (res.code === 200) {
            this.$notify({
              title: '成功',
              message: '保存成功',
              type: 'success',
              duration: 2000
            });
            this.getList();
            this.vLoading = false;
            this.dialogFormVisible1 = false;
          }
        }).catch(() => {
          this.vLoading = false;
          this.dialogFormVisible1 = false;
        })

      },
      getAddMember() {
        const that = this;
        this.listLoading = true;
        this.$api.user
          .postUserVOList("客户经理")
          .then((res) => {
            that.toAddMembers = res.data;
            setTimeout(() => {
              that.listLoading = false;
            }, 200)
          })
          .catch(() => {
            that.listLoading = false;
          })
      },
      createData() {
        this.$api.team.add(Object.assign({}, this.team,
        )).then(res => {
          if (res.code === 200) {
            this.$notify({
              title: '成功',
              message: '保存成功',
              type: 'success',
              duration: 2000
            });
            this.getList();
            this.vLoading = false
            this.dialogFormVisible = false;
          }
        }).catch(() => {
          this.vLoading = false;
          this.dialogFormVisible = false;
        })
      },
      openCreateTeam(){
        this.dialogFormVisible = true;
        this.$api.user
          .postUser("营销主管")
          .then((res) => {
            this.supervisors = res.data;
            setTimeout(() => {
            }, 200)
          });

        this.$api.user
          .postUser("市场部经理")
          .then((res) => {
            this.cultivater = res.data;
            setTimeout(() => {
            }, 200)
          })
      },
      resetSearch() {
        this.$router.push({ query: {} });
        this.listQuery = {
          current: 1,
          size: 10,
          descs: 'id',
          departmentName:'市场部'
        };
        this.getList()
      },
      searchList() {
        // 重置分页
        this.listQuery.page = 1;
        this.listQuery.size = 10;
        this.getList()
      },
      getList() {
        const that = this
        this.listLoading = true
        // console.log(that.listQuery)
        const key = {}
        key[this.listQueryKey] = this.listQuery.description;
        this.$api.team
          .list(Object.assign({}, that.listQuery, key))
          .then((res) => {
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
        const that = this;
        that.$confirm('请确认是否解散改团队?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
          center: true
        }).then(() => {
          that.$api.team.delete(id).then(data => {
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
      removeMember(id) {
        const that = this;
        that.$confirm('请确认是否移除该组员?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
          center: true
        }).then(() => {
          that.$api.teamMember.delete(id).then(data => {
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
      }
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
