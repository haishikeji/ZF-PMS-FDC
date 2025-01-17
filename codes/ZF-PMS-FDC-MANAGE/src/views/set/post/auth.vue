<template>
  <div class="app-container">
    <div class="title-container">
      <breadcrumb id="breadcrumb-container" class="breadcrumb-container"/>
    </div>
    <!--    <y-detail-page-layout :save="handleCreate" :edit-status="true">-->
    <!--      -->
    <!--    </y-detail-page-layout>-->
    <div class="filter-main-div" style="margin-top: -20px;">
      <div class="set-menu">
        <div class="set-menu-box">
          <div class="left">
            <div style="margin-bottom: 10px;text-align: right;margin-top: 30px;">
              <!--          <span style="color: #666666;font-size: 10px;padding-left: 10px;">(点击设置权限)</span>-->
            </div>

            <!--default-expand-all-->
            <div style="height: calc(100vh - 200px); overflow-y: auto;">
              <el-tree
                v-loading="treeLoading"
                ref="tree"
                default-expand-all
                :expand-on-click-node="false"
                :data="treeData"
                node-key="id"
                @node-click="nodeClick"
              >
                <span slot-scope="{ node, data }" class="custom-tree-node">
                  <el-checkbox
                    v-model="data.checked"
                    style="margin-right: 3px;"
                    @change="(sta) => {checkboxChange(data, node, sta)}"
                  />
                  <el-tooltip class="item" effect="dark" :content="data.menuType" placement="top-start">
                    <i v-if="data.menuType==='目录'" class="el-icon-folder-opened"/>
                    <i v-if="data.menuType==='菜单'" class="el-icon-document"/>
                    <i v-if="data.menuType==='按钮'" class="el-icon-thumb"/>
                  </el-tooltip>
                  <!--            <span class="tips">{{ node.level }}</span>-->
                  <!--            <span>{{ data }}</span>-->
                  <span style="margin-left: 5px;">{{ data.name }}</span>
                </span>
              </el-tree>
            </div>
          </div>
          <div class="right">
            <div class="btn-set">
              <el-button
                class="filter-item"
                style="position: absolute;right: 10px; z-index: 99;"
                type="primary"
                round
                @click="handleCreate"
              >
                保存
              </el-button>
              <el-tabs type="card">
                <el-tab-pane :label=" '【'+postName + '】权限设置'">
                </el-tab-pane>
              </el-tabs>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import Breadcrumb from '@/components/Breadcrumb'
  // import YDetailPageLayout from '@/components/YDetailPageLayout'

  export default {
    name: 'Auth',
    components: {
      Breadcrumb,
      // YDetailPageLayout,
    },
    data() {
      return {
        temp: {
          btn: [],
          level: '',
          dataPrivilege: null,
        },
        postName:this.$route.query.name,
        treeData: [],
        treeListData: [],
        defaultProps: {
          children: 'children',
          label: 'label'
        },
        menuItem: {},
        checkedIds: [],
        treeLoading: false,
      }
    },
    mounted() {
      this.getDetail()
    },
    methods: {
      initData() {
        this.temp = {
          btn: [],
          level: '',
          dataPrivilege: null,
        }
      },

      getDetail() {
        this.treeLoading = true
        this.$api.postAuth.tree({ postId: this.$route.query.id }).then(res => {
          this.treeData = res.data.filter((item) => { return item.id && item.name })
          const list = []
          const checkeds = []

          function treeMap(data) {
            data.forEach(item => {
              list.push(item)
              if (item.checked) {
                checkeds.push(item.id)
              }
              if (item.children && item.children.length > 0) {
                treeMap(item.children)
              }
            })
          }

          treeMap(this.treeData)
          this.treeListData = list
          this.checkedIds = checkeds
          this.$refs.tree.setCheckedKeys(this.checkedIds)

          this.treeLoading = false
        }).catch(()=>{
          this.treeLoading = false
        })
      },
      setParentCheck(data) { // 获取父节点id数组/修改父节点checked 为true
        const parentChecks = []

        function tree(data, list) {
          if (data.parentId) {
            list.forEach(item => {
              if (item.id === data.parentId) {
                parentChecks.push(item.id)
                item.checked = true
                if (item.parentId) {
                  tree(item, list)
                }
              }
            })
          }
        }

        tree(data, this.treeListData)
        return parentChecks
      },
      setChildrenCheck(data, checkedStatus = true) { // 取消子节点选中效果
        const that = this
        const childrenIds = []

        function tree(list) {
          list.forEach(item => {
            // console.log('子菜单', item.name)
            item.checked = checkedStatus
            childrenIds.push(item.id)
            if (item.children) {
              tree(item.children)
            }
          })
        }

        data.children && tree(data.children)
        // console.log(data.children, '子树', childrenIds)
        return childrenIds
      },
      handleCheckChange(data, checked, indeterminate) {
        // console.log(data, checked, indeterminate, 'check node')
        // if (checked === true) {
        //   const pChecks = this.setParentCheck(data)
        //   this.checkedIds = this.checkedIds.concat(pChecks)
        //   this.checkedIds = Array.from(new Set(this.checkedIds)) // 去重
        //   this.$refs.tree.setCheckedKeys(this.checkedIds)
        //   console.log(this.setChildrenCheck(data, true), 'id数据')
        // } else {
        //   const childrenIds = this.setChildrenCheck(data)
        //   this.checkedIds = Array.from(new Set(this.checkedIds)) // 去重
        //   // childrenIds.forEach(cId => {
        //   //   const index = this.checkedIds.indexOf(cId)
        //   //   if (index > -1) {
        //   //     this.checkedIds.splice(index, 1)
        //   //   }
        //   // })
        //   // console.log(childrenIds, this.checkedIds, 'id数据')
        //   // this.$refs.tree.setCheckedKeys(this.checkedIds)
        // }
      },
      nodeClick(data, node, it) {
        this.menuItem = data
      },
      checkboxChange(data, node, sta) {
        // console.log(this.$refs.tree.getCheckedKeys(), '当前被选中key')
        // this.menuItem = data
        if (sta) { // 设为选中，父子都选中
          // 父节点
          const pChecks = this.setParentCheck(data)

          // 字节点
          const childrenIds = this.setChildrenCheck(data, true)
        } else {
          // 字节点
          const childrenIds = this.setChildrenCheck(data, false)
        }
      },
      handleCreate() {
        // console.log(this.$refs.tree.getCheckedNodes())
        // console.log(this.$refs.tree.getCheckedKeys())
        // console.log(JSON.stringify({
        //   postId: this.$route.query.id,
        //   permissionTree: JSON.stringify(this.treeData)
        // }))
        this.$api.postAuth.edit({
          postId: this.$route.query.id,
          permissionTree: JSON.stringify(this.treeData)
        }).then(res => {
          if (res.code === 200) {
            this.$notify({
              title: '成功',
              message: '保存成功',
              type: 'success',
              duration: 2000
            })
            this.getDetail()
          }
        })
      }
    }
  }
</script>

<style scoped lang="scss">
  .btn-box {
    width: 1300px;
    margin: auto;
    margin-top: 30px;
  }

  .set-menu-box {
    display: flex;
    margin: 30px auto;
    /*border: 1px solid #f0f0f0;*/

    .left {
      width: 360px;
      padding: 10px;
      border-right: 1px solid #f0f0f0;
    }

    .right {
      flex: 1;
      min-height: 500px;

      .btn-set {
        padding: 10px 10px 30px 10px;
        position: relative;

        .title-line {
          border-bottom: 2px solid #2a8fe3;
          margin-bottom: 20px;
          margin-top: 10px;
        }

        .title {
          background-color: #2a8fe3;
          color: #ffffff;
          padding: 4px 5px;
        }
      }
    }
  }

  .custom-tree-node {
    display: flex;
    align-items: center;

    .tips {
      display: inline-block;
      width: 18px;
      height: 18px;
      text-align: center;
      background-color: #f67220;
      color: #ffffff;
      border-radius: 50%;
    }

    .btns {
      margin-left: 10px;
    }
  }
</style>
<style lang="scss" scoped>
  .set-menu {
    .el-tree-node__content {
      margin-bottom: 5px;
    }
  }
</style>
