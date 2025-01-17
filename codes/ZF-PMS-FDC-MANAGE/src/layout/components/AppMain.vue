<template>
  <section class="app-main" v-if="show">
    <transition name="fade-transform" mode="out-in">
      <keep-alive :include="cachedViews">
        <router-view :key="key" />
      </keep-alive>
    </transition>
  </section>
  <section class="app-main" v-else>
    <el-empty :image="require('../../assets/images/no-auth.png')" :image-size=700 description="Oops! 需要输入查询密码...">
      <!-- <i class="el-icon-edit" :style="iconstyle"  @click="openDig" @mouseover="hoverIcon"></i> -->
      <img src="../../assets/gif/input_psw.gif" :style="iconstyle" alt="" @click="openDig" @mouseover="hoverIcon">
    </el-empty>
  </section>
</template>

<script>
  import eventBus from '../../utils/eventBus.js'
export default {
  name: 'AppMain',
  props:{
    show:{
      type: Boolean,
      require: true,
      default:true
    }
  },

  watch:{
    show:{
      handler(newValue,oldValue){
        const path = this.$route.path;
        if (newValue && path==='/brokerage/item/settle'){
          this.getMessageId();
        }
      },
      deep: true,
      immediate: true
    },
    $route: {
      deep: true,
      immediate: true,
      handler() {
        if (!this.$route.path.startsWith("/brokerage")){
          this.getMessageId();
        }else{
          this.messageId = null;
        }
      },
    },
  },
  data(){
    return {
      iconstyle:"width: 30px;",
      messageId:null,
    }
  },
  computed: {
    cachedViews() {
      return this.$store.state.tagsView.cachedViews
    },
    key() {
      return this.$route.path
    }
  },

  mounted(){
      eventBus.$on("messageId", res=>{
      this.messageId = res;
      })
  },

  methods:{
    openDig(){
      eventBus.$emit("openDig",true)
    },
    hoverIcon(){
      this.iconstyle = "width:30px ;cursor:pointer"
    },

    getMessageId(){
      this.$emit('getMessageId', this.messageId)
    }

  }
}
</script>

<style lang="scss" scoped>
.app-main {
  /* 60= navbar  60  */
  min-height: calc(100vh - 80px);
  width: 100%;
  position: relative;
  //overflow: hidden;
}

.fixed-header+.app-main {
  padding-top: 60px;
}

.hasTagsView {
  .app-main {
    /* 84 = navbar + tags-view = 50 + 34 */
    min-height: calc(100vh - 84px);
  }

  .fixed-header+.app-main {
    padding-top: 84px;
  }
}
</style>

<style lang="scss">
// fix css style bug in open el-dialog
.el-popup-parent--hidden {
  .fixed-header {
    padding-right: 15px;
  }
}
</style>
