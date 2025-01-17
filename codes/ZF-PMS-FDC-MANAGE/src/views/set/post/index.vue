<template>
  <div class="app-container">
    <div class="title-container">
      <breadcrumb id="breadcrumb-container" class="breadcrumb-container"/>
    </div>
    <y-page-list-layout :page-list="pageData" :page-para="listQuery" :get-page-list="getList">
      <template slot="left">
        <PermissionButton
          menu-code="_view_post_add"
          class="filter-item"
          icon="el-icon-plus"
          type="primary"
          name=""
          round
          @click="handleAdd"
        />
        <el-select v-model="listQuery.departmentName" style="width: 200px;margin-left: 20px;" clearable filterable
                   placeholder="所属部门" class="filter-item"
        >
          <el-option
            v-for="item in partOptions"
            :key="item.id"
            :label="item.name"
            :value="item.name"
          />
        </el-select>
        <el-input
          v-model="listQuery.name"
          placeholder="岗位名称关键字"
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
      <parentTable v-loading="listLoading" :data="pageData.records" slot="table">
        <el-table-column label="岗位名称" min-width="150" align="center">
          <template slot-scope="{row}">
            <span>{{ row.name }}</span>
          </template>
        </el-table-column>
        <el-table-column label="所属部门" min-width="150" align="center">
          <template slot-scope="{row}">
            <el-tag>{{ row.departmentName || '无' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="领导部门" min-width="150" align="center">
          <template slot-scope="{row}">
            <el-tag>{{ row.leaderDepartmentName || '无' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="领导岗位" min-width="150" align="center">
          <template slot-scope="{row}">
            <el-tag>{{ row.leaderPost || '无' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="主要职责" min-width="150" align="center">
          <template slot-scope="{row}">
            <span>{{ row.duty }}</span>
          </template>
        </el-table-column>
        <el-table-column label="创建时间" min-width="150" align="center">
          <template slot-scope="{row}">
            <span>{{ row.created }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" width="280" class-name="small-padding fixed-width">
          <template slot-scope="{row,$index}">
            <PermissionButton
              menu-code="_views_set_post_edit"
              type="primary"
              size="mini"
              name=""
              round
              @click="handleUpdate(row)"
            />
            <PermissionButton
              menu-code="_views_set_post_remove"
              type="danger"
              size="mini"
              name=""
              round
              @click="(data) =>{handleDelete(row, data)}"
            />
            <PermissionButton
              menu-code="_views_set_post_auth"
              type="warning"
              size="mini"
              name=""
              :page-jump="true"
              :page-query="{id: row.id,name:row.name}"
              round
            />
          </template>
        </el-table-column>
        <!--      </el-table>-->
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
        <el-form-item label="岗位名称：" prop="name">
          <el-input v-model="temp.name" class="filter-item"/>
        </el-form-item>

        <el-form-item label="所属部门：" filterable prop="departmentId">
          <el-select v-model="temp.departmentId" filterable class="filter-item" style="float: left;width: 100%;" placeholder="请选择">
            <el-option
              v-for="item in partOptions"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="领导部门：" filterable prop="leaderDepartmentId">
          <el-select v-model="temp.leaderDepartmentId" filterable class="filter-item" style="float: left;width: 100%;" placeholder="请选择" @change="getLeaderPost">
            <el-option
              v-for="item in partOptions"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="领导岗位：" filterable prop="parentId">
          <el-select v-model="temp.parentId" filterable class="filter-item" style="float: left;width: 100%;" placeholder="请选择">
            <el-option
              v-for="item in postOptions"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="主要职责：">
          <el-input v-model="temp.duty" class="filter-item" type="textarea" placeholder="200字符"/>
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
      <!--      <div slot="footer" class="dialog-footer" style="text-align: center">-->

      <!--      </div>-->
    </el-dialog>
  </div>
</template>
<script>
  import Breadcrumb from '@/components/Breadcrumb'
  import PermissionButton from '@/components/PermissionButton/PermissionButton'
  import YPageListLayout from '@/components/YPageListLayout'

  export default {
    name: 'ViewsSetPost',
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
        tableKey: 0,
        pageData: { records: [] },
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
          departmentId: '',
          leaderDepartmentName:'',
          leaderPost:'',
          leaderDepartmentId:null,
          parentId:null,
          duty: ''
        },
        statusOptions: ['published', 'draft', 'deleted'],
        textMap: {
          update: '编辑岗位',
          create: '新增岗位'
        },
        rules: {
          name: [{
            required: true,
            message: '请填写岗位名称',
            trigger: 'blur'
          }],
          depatmentId: [{
            required: true,
            message: '请选择所属部门',
            trigger: 'change'
          }]
        },
        partOptions: [],
        leaderDepartmentId:null,
        postOptions: [],
      }
    },
    created() {
      if (this.$route.query.departmentName) {
        this.listQuery.departmentName = this.$route.query.departmentName
      }
      this.getPartSelect();
      this.getList()
    },
    methods: {
      getLeaderPost(){
        this.$api.post.xList(this.temp).then(res => {
          this.postOptions = res.data;
        })
      },
      resetSearch() {
        this.listQuery = {
          current: 1,
          size: 10,
          descs: 'id'
        }
      },
      resetTemp() {
        this.temp = {
          code: '',
          name: 1,
          orgId: '',
          duty: ''
        }
      },
      getPartSelect() {
        this.$api.department.simpleAll().then(res => {
          this.partOptions = res.data
        })
      },
      createData() {
        if (this.temp.duty && this.temp.duty.length>200){
          this.$notify({
            title: '错误',
            message: '岗位职责不超过200字符',
            type: 'error',
            duration: 2000
          });
          return;
        }
        this.$refs['dataForm'].validate((valid) => {
          if (valid) {
            this.dialogLoading = true;
            this.$api.post.add(this.temp).then(res => {
              if (res.code === 200) {
                this.dialogLoading = false;
                this.dialogFormVisible = false;
                this.$notify({
                  title: '成功',
                  message: '岗位创建成功',
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
        if (this.temp.duty && this.temp.duty.length>200){
          this.$notify({
            title: '错误',
            message: '岗位职责不超过200字符',
            type: 'error',
            duration: 2000
          });
          return;
        }
        this.$refs['dataForm'].validate((valid) => {
          if (valid) {
            this.$api.post.edit(this.temp).then(res => {
              if (res.code === 200) {
                this.dialogFormVisible = false;
                this.$notify({
                  title: '成功',
                  message: '岗位编辑成功',
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
        this.temp = Object.assign({}); // copy obj
        this.temp.timestamp = new Date(this.temp.timestamp);
        this.dialogStatus = 'create';
        this.dialogFormVisible = true;
        this.$nextTick(() => {
          this.$refs['dataForm'].clearValidate()
        })
      },
      handleUpdate(row) {
        this.temp = Object.assign({}, row); // copy obj
        this.dialogStatus = 'update';
        this.dialogFormVisible = true;
        this.getLeaderPost();
        this.$nextTick(() => {
          this.$refs['dataForm'].clearValidate()
        })
      },
      searchList() {
        this.listQuery.current = 1;
        this.getList()
      },
      getList() {
        const that = this;
        this.listLoading = true;
        // console.log(that.listQuery)
        this.$api.post.list(that.listQuery).then(res => {
          that.pageData = res.data;
          setTimeout(() => {
            that.listLoading = false
          }, 200)
        }).catch(() => {
          that.listLoading = false
        })
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
            this.$api.post.delete(row.id).then(res => {
              if (res.code === 200) {
                // this.$message({
                //   type: 'success',
                //   message: '删除成功'
                // })
                this.$notify({
                  title: '成功',
                  message: '删除成功',
                  type: 'success',
                  duration: 2000
                });
                this.getList()
              }
            })
          })
          .catch(err => { console.error(err) })
      },
      copyPost(row) {
        this.$api.post.copyPost(row.id).then(res => {
          if (res.code === 200) {
            this.getList();
            this.$notify({
              title: '成功',
              message: '复制成功',
              type: 'success',
              duration: 2000
            })
          }
        })
      }
    }
  }
</script>
<style scoped lang="scss">
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
      box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04);

      .text {
        margin-left: 16px;
      }
    }
  }
</style>
