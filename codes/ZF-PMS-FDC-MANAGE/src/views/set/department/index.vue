<template>
  <div class="app-container organization-index">
    <div class="title-container">
      <breadcrumb id="breadcrumb-container" class="breadcrumb-container"/>
    </div>
    <div class="statisticsInfo">
      <div class="menu-2-box">
        <div
          class="menu-2-item hvr-underline-from-center"
        ><img src="../../../assets/sta1.png" height="50" width="50">
          <div class="text">
            <div class="analysis-text"><span class="tag">{{ statisticsInfo.orgNum }}</span></div>
            <div class="analysis-text-small">当前机构总数</div>
          </div>
        </div>
        <div
          class="menu-2-item hvr-underline-from-center"
        ><img src="../../../assets/sta2.png" height="50" width="50">
          <div class="text">
            <div class="analysis-text"><span class="tag">{{ statisticsInfo.postNum }}</span></div>
            <div class="analysis-text-small">当前岗位总数</div>
          </div>
        </div>
        <div
          class="menu-2-item hvr-underline-from-center"
        ><img src="../../../assets/sta3.png" height="50" width="50">
          <div class="text">
            <div class="analysis-text"><span class="tag">{{ statisticsInfo.staffNum }}</span></div>
            <div class="analysis-text-small">当前人员总数</div>
          </div>
        </div>
      </div>
    </div>
    <y-page-list-layout :page-list="pageData" :page-para="listQuery" :get-page-list="getList">
      <template slot="left">
        <PermissionButton
          menu-code="_view_department_add"
          class="filter-item"
          icon="el-icon-plus"
          type="primary"
          name=""
          round
          @click="handleAdd"
        />
      </template>
      <parentTable
       v-loading="listLoading"
        :data="pageData.records"
        slot="table"
        style="width: 100%;"
      >
        <el-table-column label="部门名称" min-width="150" align="center">
          <template slot-scope="{row}">
                       <span>{{ row.name }}</span>
          </template>
        </el-table-column>
        <el-table-column label="上级部门" min-width="150" align="center">
          <template slot-scope="{row}">
                <span>{{ row.parentName || '无' }}</span>
          </template>
        </el-table-column>
        <el-table-column label="在职人数" min-width="150" align="center">
          <template slot-scope="{row}">
              <span>{{ row.peopleNum || '0' }}</span>
          </template>
        </el-table-column>
        <el-table-column label="部门领导" min-width="150" align="center">
          <template slot-scope="{row}">
              <span>{{ row.leaderName}}</span>
          </template>
        </el-table-column>
        <el-table-column label="轮单部门" min-width="150" align="center">
          <template slot-scope="{row}">
              <span>{{ row.ifAllot?'是':'否' }}</span>
          </template>
        </el-table-column>
        <el-table-column label="联系电话" min-width="150" align="center">
          <template slot-scope="{row}">
            <span>{{ row.phone }}</span>
          </template>
        </el-table-column>
        <el-table-column label="创建时间" min-width="150" align="center">
          <template slot-scope="{row}">
            <span>{{ row.created }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" width="250" class-name="small-padding fixed-width">
          <template slot-scope="{row}">
            <PermissionButton
              menu-code="_view_department_edit"
              type="primary"
              size="mini"
              name=""
              round
              @click="handleUpdate(row)"
            />
            <PermissionButton
              menu-code="_view_department_remove"
              type="danger"
              size="mini"
              name=""
              round
              @click="(data) =>{handleDelete(row, data)}"
            />
            <el-button type="info" round @click="goPostList(row)">
              岗位
            </el-button>
          </template>
        </el-table-column>
      </parentTable>
    </y-page-list-layout>
    <el-dialog
      width="600px"
      :title="textMap[dialogStatus]"
      :visible.sync="dialogFormVisible"
      :close-on-click-modal="false"
    >
      <el-form
        ref="dataForm"
        v-loading="dialogLoading"
        :rules="rules"
        :model="temp"
        label-position="right"
        label-width="110px"
        style="width: 400px; margin-left:50px;"
      >
        <el-form-item label="部门名称：" prop="name">
          <el-input v-model="temp.name" class="filter-item"/>
        </el-form-item>

        <el-form-item label="上级部门：" filterable prop="parentId">
          <el-select v-model="temp.parentId" filterable class="filter-item" style="float: left;width: 100%;" placeholder="请选择" clearable>
            <el-option
              v-for="item in partOptions"
              :disabled="disabledIds.includes(item.id)"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="联系电话：">
          <el-input v-model="temp.phone" class="filter-item"/>
        </el-form-item>
        <el-form-item label="部门领导：">
          <el-select v-model="temp.leaderId" 
            placeholder="请选择" 
              filterable
              clearable style="width:100%">
              <el-option v-for="(u,id) in allUsers" :label="u.name" :value="u.id"></el-option> 
            </el-select>
        </el-form-item>
        <el-form-item label="轮单部门：">
          <el-select v-model="temp.ifAllot" style="width:100%">
              <el-option label="是" :value="true">是</el-option>
              <el-option label="否" :value="false">否</el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="">
          <el-button @click="dialogFormVisible = false">
            取消
          </el-button>
          <el-button type="primary" @click="dialogStatus==='create'?createData():updateData()">
            保存
          </el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>
<script>
  import Breadcrumb from '@/components/Breadcrumb'
  import PermissionButton from '@/components/PermissionButton/PermissionButton'
  import YPageListLayout from '@/components/YPageListLayout'

  export default {
    name: 'department',
    components: {
      Breadcrumb,
      PermissionButton,
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
        tableData: 0,
        tableKey: 0,
        pageData: {},
        listLoading: false,
        dialogLoading: false,
        listQuery: {
          current: 1,
          size: 10,
          descs: 'id'
        },
        dialogFormVisible: false,
        dialogStatus: '',
        temp: {
          code: '',
          name: 1,
          parentId: '',
          phone: ''
        },
        statusOptions: ['published', 'draft', 'deleted'],
        textMap: {
          update: '编辑',
          create: '新增'
        },
        rules: {
          name: [{
            required: true,
            message: '请填写名称',
            trigger: 'blur'
          }],
        },
        partOptions: [],
        statisticsInfo: {},
        disabledIds: [], // 编辑子集id
        allUsers:[]
      }
    },
    created() {
      this.getPartSelect();
      this.getList()
    },
    methods: {
      getstatisticsInfo() {
        this.$api.department.sta().then(res => {
          this.statisticsInfo = res.data
        })
      },
      resetTemp() {
        this.temp = {
          code: '',
          name: 1,
          parentId: '',
          phone: ''
        }
      },
      getPartSelect() {
        this.$api.department.simpleAll().then(res => {
          this.partOptions = res.data
        })
      },
      // handleCreate() {
      //   this.resetTemp()
      //   this.dialogStatus = 'create'
      //   this.dialogFormVisible = true
      //   this.$nextTick(() => {
      //     this.$refs['dataForm'].clearValidate()
      //   })
      // },
      createData() {
        if (this.temp.phone && this.temp.phone.length > 30) {
          this.$message.error('联系电话不能超过30个字符！');
          return
        }
        this.$refs['dataForm'].validate((valid) => {
          if (valid) {
            this.dialogLoading = true;
            this.$api.department.add(this.temp).then(res => {
              if (res.code === 200) {
                this.dialogLoading = false;
                this.dialogFormVisible = false;
                this.$notify({
                  title: '成功',
                  message: '创建成功',
                  type: 'success',
                  duration: 2000
                });
                this.getList()
              }
            }).catch(() => {
              this.dialogLoading = false
            })
          }
        })
      },
      updateData() {
        if (this.temp.phone && this.temp.phone.length > 30) {
          this.$message.error('联系电话不能超过30个字符！');
          return
        }
        this.$refs['dataForm'].validate((valid) => {
          if (valid) {
            this.$api.department.edit(this.temp).then(res => {
              if (res.code === 200) {
                this.dialogFormVisible = false;
                this.$notify({
                  title: '成功',
                  message: '编辑成功',
                  type: 'success',
                  duration: 2000
                });
                this.getList()
              }
            })
          }
        })
      },
      handleAdd() {
        this.disabledIds = [];
        this.getAllUser();
        this.getPartSelect();
        this.temp = Object.assign({}); // copy obj
        this.temp.timestamp = new Date(this.temp.timestamp);
        this.dialogStatus = 'create';
        this.dialogFormVisible = true;
        this.$nextTick(() => {
          this.$refs['dataForm'].clearValidate()
        })
      },
      handleUpdate(row) {
        this.getAllUser();
        this.disabledIds = this.getDisabledIds(row);
        this.getPartSelect();
        this.temp = Object.assign({}, row); // copy obj
        this.dialogStatus = 'update';
        this.dialogFormVisible = true;
        this.$nextTick(() => {
          this.$refs['dataForm'].clearValidate()
        })
      },
      getDisabledIds(temp) {
        let ids = [];
        function tree(item) {
          if(item.id) {
            ids.push(item.id)
          }
          if(item.children && item.children.length) {
            item.children.forEach(c =>{
              tree(c)
            })
          }
        }
        tree(temp);
        return ids
      },
      searchList() {
        this.listQuery.current = 1;
        this.getList()
      },
      getList() {
        const that = this;
        this.listLoading = true;
        this.$api.department.page(that.listQuery).then(res => {
          that.pageData = res.data;
          setTimeout(() => {
            that.listLoading = false
          }, 200)
        }).catch(() => {
          that.listLoading = false
        });
        // this.$api.department.tree().then(res => {
        //   that.pageData = res.data;
        //   setTimeout(() => {
        //     that.listLoading = false
        //   }, 200)
        // }).catch(() => {
        //   that.listLoading = false
        // });
        this.getstatisticsInfo()
      },
      handleDelete(row, data) {
        // console.log(data)
        const that = this;
        that.$confirm('确认删除当前记录吗?', '警告', {
          confirmButtonText: '确认',
          cancelButtonText: '取消',
          type: 'warning'
        })
          .then(async () => {
            this.$api.department.delete(row.id).then(res => {
              if (res.code === 200) {
                this.$message({
                  type: 'success',
                  message: '删除成功'
                });
                this.getList()
              }
            })
          })
          .catch(err => { console.error(err) })
      },
      goPostList(row) {
        this.$router.push(`/setting/post?departmentName=${row.name}`)
      },
      getAllUser() {
        this.$api.user.simpleAll().then(res => {
          if (res.code === 200) {
              this.allUsers = res.data;
          } 
        })
      },
    }
  }
</script>
<style lang="scss">
  .organization-index {
    .el-table [class*=el-table__row--level] .el-table__expand-icon {
    /*  position: relative;*/
    /*  left: 0px;*/
      color: red;
    /*  font-weight: bold;*/
    /*  transform: scale(1.6);*/
    }
  }
</style>
<style scoped lang="scss">
</style>
