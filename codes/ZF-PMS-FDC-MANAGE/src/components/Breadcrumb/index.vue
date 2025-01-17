<template>
  <el-breadcrumb class="app-breadcrumb" separator=">">
    <transition-group name="breadcrumb">
      <el-breadcrumb-item v-for="(item,index) in permission_menusLevelList" :key="index+'id'">
        <!--        <span v-if="item.redirect==='noRedirect'||index==levelList.length-1"-->
        <!--              class="no-redirect">{{ item.meta.title }}</span>-->
        <!--        <a v-else @click.prevent="handleLink(item)">{{ item.meta.title }}</a>-->
        <span
          v-if="index === permission_menusLevelList.length - 1"
          class="no-redirect"
          style="color:#338FFF"
        >{{ item.meta.title }}</span>
        <span v-else class="no-redirect">{{ item.meta.title }}</span>
      </el-breadcrumb-item>
    </transition-group>
  </el-breadcrumb>
</template>

<script>
  import pathToRegexp from 'path-to-regexp'
  import { mapGetters } from 'vuex'

  export default {
    data() {
      return {
      }
    },
    computed: {
      ...mapGetters(['permission_menusLevelList'])
    },
    methods: {
      isDashboard(route) {
        const name = route && route.name
        if (!name) {
          return false
        }
        return name.trim().toLocaleLowerCase() === 'home'.toLocaleLowerCase()
      },
      pathCompile(path) {
        // To solve this problem https://github.com/PanJiaChen/vue-element-admin/issues/561
        const { params } = this.$route
        var toPath = pathToRegexp.compile(path)
        return toPath(params)
      },
      handleLink(item) {
        const { redirect, path } = item
        if (redirect) {
          this.$router.push(redirect)
          return
        }
        this.$router.push(this.pathCompile(path))
      }
    }
  }
</script>
<style lang="scss" scoped>
  .app-breadcrumb.el-breadcrumb {
    display: inline-block;
    font-size: 15px;
    /*line-height: 50px;*/
    margin-left: 8px;

    .no-redirect {
      color: #97a8be;
      cursor: text;
    }
    .app-breadcrumb {
      color: #778CA2;
    }
  }
</style>
