<!--
数据模块布局组件
 2020.10.26 姚政伟 创建
-->
<template>
  <div class="y-dataview-module">
    <div class="title" v-if="title">
      <h5>{{title}}<span class="more" v-if="more" @click="gotoDetail">更多</span></h5>
    </div>
    <div class="y-dm-content">
      <slot></slot>
    </div>
  </div>
</template>

<script>
  export default {
    name: "YDataViewModule",
    props:{
      title:{
        type:String
      },
      more:{
        type:String
      }
    },
    methods: {
      gotoDetail(){
        let that = this
        that.$router.push({
          path: that.more
        })
      }
    }
  }
</script>

<style scoped>

</style>
