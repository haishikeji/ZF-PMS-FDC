<!--
模块首页数据视图布局组件
 2020.10.26 姚政伟 创建
-->
<template>
    <div class="y-dataview-pagelayout">
      <h3 v-if="title">{{title}}</h3>
      <div class="y-dp-content">
        <slot></slot>
      </div>
    </div>
</template>

<script>
    export default {
      name: "YDataViewPageLayout",
      props:{
        title:{
          type:String
        }
      }
    }
</script>

<style lang="scss">
  @import "./assets/css/main.scss";
</style>
