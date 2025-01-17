<template>
  <el-submenu v-if="menu.menuType === '目录'" :index="'id'+menu.id">
    <template slot="title">
      <i
        :class="iconName"
        style="transform: scale(0.8);margin-right: 14px;"
      />
      <span slot="title">{{ menu.name }}</span>
    </template>
    <MenuTree v-for="item in menu.children" :key="item.id" :menu="item" />
  </el-submenu>
  <el-menu-item v-else-if="menu.pcUrl || menu.outerUrl" :index="'id'+menu.id" @click="handleRoute(menu)">
    <i
      :class="iconName"
      style="transform: scale(0.8);margin-right: 10px;"
    />
    <span slot="title">{{ menu.name }}</span>
  </el-menu-item>
</template>

<script>
  import { mapGetters } from 'vuex'

  export default {
    name: 'MenuTree',
    props: {
      menu: {
        type: Object,
        required: true
      },
    },
    data() {
      return {
        menuId: this.$route.meta.id || '',
        iconName: ''
      }
    },
    computed: {
      ...mapGetters([
        'permission_menusLevelList',
      ]),
    },
    watch: {
      '$route': {
        deep: true,
        immediate: true,
        handler() {
          // eslint-disable-next-line eqeqeq
          this.iconName = 'menu-sprites ' + this.menu.pcIcon + (this.$route.meta.id == this.menu.id ? '' : '1')
        }
      }
    },
    created() {
      // eslint-disable-next-line eqeqeq
      this.iconName = 'menu-sprites ' + this.menu.pcIcon + (this.menuId == this.menu.id ? '' : '1')
    },
    methods: {
      handleRoute(menu) {
        // 通过菜单URL跳转至指定路由
        if (menu.external) {
          if (menu.ifNewTag === false) { // 当前页面
            this.$router.push({
              path: menu.pcUrl,
              query: {
                link: menu.outerUrl?encodeURIComponent(menu.outerUrl):''
              }
            })
          } else { // true 或未设置（兼容以前未设置的情况） 新开页面
            window.open(menu.outerUrl)
          }
        } else {
          if (menu.ifNewTag) { // 必须设为新开才新开，其他都是在当前页面。和上面相反
            this.$targetNewPage({
              path: menu.pcUrl,
              query: {}
            })
          } else {
            this.$router.push({
              path: menu.pcUrl,
              query: {}
            })
          }
        }
      }
    }
  }
</script>
