/*
* 页面 按钮 混入
* 使用，在需要得配置按钮权限得页面 引入该文件
* 如：views/set/post/index 使用
* 组件内获取 this.permissionButtons  按钮数组
* */
import { mapGetters } from 'vuex'

function getCurrentPageButtons(menus, currentPageMenuId) {
  const btns = []

  function tree(menus, currentPageMenuId) {
    menus.forEach(item => {
      if (item.id === currentPageMenuId) {
        if (item.children) {
          item.children.forEach(btn => {
            if (btn.menuType === '按钮') {
              btns.push(btn)
            }
          })
        }
      } else if (item.children) {
        tree(item.children, currentPageMenuId)
      }
    })
  }

  tree(menus, currentPageMenuId)
  return btns
}

const mixin = {
  data() {
    return {
      permissionButtons: [],
      currentPageMenuId: ''
    }
  },
  watch: {
    permission_menus: {
      immediate: true, // immediate选项可以开启首次赋值监听
      deep: true,
      handler(newv) {
        if (newv && newv.length) {
          this.permissionButtons = getCurrentPageButtons(newv, parseInt(this.$route.meta.id, 10))
          //console.log(this.permissionButtons, '当前页面按钮信息')
        }
      }
    }
  },
  computed: {
    ...mapGetters([
      'permission_menus'
    ])
  },
  created() {
    this.currentPageMenuId = this.$route.query.menuId
  }
}

export default mixin
