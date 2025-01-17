<template>
  <el-menu
    class="sidebar-menu"
    :default-active="activeIndex"
    text-color="#fff"
    active-text-color="#338FFF"
    font-weight="bold"
    background-color="#002140"
  >
    <!--        <el-menu-item v-if="$route.path !== '/home/index'" index="99999" @click="handleSelect('/home/index')">首页</el-menu-item>-->
    <!--      <el-menu-item index="99999" @click="handleSelect('/home/index')">首页</el-menu-item>-->
    <menu-tree
      v-for="item in currentMenus"
      :key="item.id"
      :menu="item"
    ></menu-tree>
  </el-menu>
</template>

<script>
import { mapGetters } from "vuex";
import menuTree from "./menuTree";

export default {
  components: { menuTree },
  data() {
    return {
      activeIndex: "",
      // menuLevel1: null,
      // menuId: null,
      // menus: [],
      currentMenus: [],
      // timer: null,
    };
  },
  computed: {
    ...mapGetters([
      // 'permission_menus',
      "permission_menusLevelList",
    ]),
    // hasData() {
    //   if (this.permission_menus.length && this.permission_menusLevelList.length) {
    //     return true
    //   } else {
    //     return false
    //   }
    // }
  },
  watch: {
    $route: {
      immediate: true, // immediate选项可以开启首次赋值监听
      deep: true,
      handler(newv) {
        this.initData();
        if (this.$route.path === "/home/index") {
          //
        } else {
          // this.menuLevel1 = this.permission_menusLevelList[0].id
          // this.menuId = this.$route.meta.id
          if (this.$route.meta.menuType === "菜单") {
            // 详情菜单选中不变
            this.activeIndex = "id" + this.$route.meta.id;
          }
        }
      },
    },
    permission_menusLevelList: {
      immediate: true, // immediate选项可以开启首次赋值监听
      deep: true,
      handler(newv) {
        if (newv.length) {
          this.currentMenus = newv[0].meta.children;
          this.activeIndex = "id" + this.$route.meta.id;
        }
      },
    },
  },
  created() {
    // this.menuLevel1 = this.permission_menusLevelList[0].id
    this.activeIndex = "id" + this.$route.meta.id;
    // this.currentMenus = []
    // this.getCurrentMenu(this.menus)
  },
  methods: {
    initData() {
      // this.activeIndex = ''
      // this.menuLevel1 = null
      // this.menuId = null
      // this.menus = []
      // this.currentMenus = []
    },
    getCurrentMenu(menus) {
      // console.log(menus)
      // menus = menus || []
      // menus.forEach(item => {
      //   if (parseInt(item.id, 10) === parseInt(this.permission_menusLevelList[0].id, 10)) {
      //     this.currentMenus = item.children
      //   }
      //   // else {
      //   //   if (item.children && item.children.length && item.menuType === '目录') {
      //   //     this.getCurrentMenu(item.children)
      //   //   }
      //   // }
      // })
      this.currentMenus = this.permission_menusLevelList[0].children;
    },
    handleSelect(url) {
      this.$router.push(url);
    },
  },
};
</script>
<style lang="scss">
.sidebar-menu.el-menu {
  border-right: 0;

  .el-menu-item.is-active {
    background-color: #f8fafb;

    &:before {
      content: "";
      display: inline-block;
      width: 2px;
      height: 100%;
      background-color: #338fff;
      position: absolute;
      left: 0;
    }
  }
}
</style>
<style lang="scss" scoped></style>
