<template>
  <div class="y-select-ico-layout">
    <el-popover
      placement="left"
      width="410"
      trigger="hover"
      class="y-select-ico-box"
      ref="selectMobileIco">
      <div class="y-select-ico">
        <span :class="`mobile-ico ${selected == item?'selected':''}`" v-for="(item,index) in iconNames" :key="index" @click="select(item)"><i :class="`mobile-menu-sprites ${item}`"></i></span>
      </div>
      <div slot="reference">
        <span class="mobile-ico" v-if="value"><i :class="`mobile-menu-sprites ${value}`"  style="margin-top: 5px;"></i></span>
        <el-button type="primary" v-else>图标库</el-button>
      </div>
    </el-popover>
    <i class="el-icon-error" @click="deleteIco" v-if="value"></i>
  </div>
</template>

<script>
  import iconNames from '@/styles/mobileMenu/iconNames'
    export default {
        name: "index",
      props:{
        value:{
          type:String,
          default:''
        }
      },
      data(){
          return{
            iconNames,
            selected:this.value
          }
      },
      methods:{
        select(value){
          this.selected = value
          this.$emit('input',value)
        },
        deleteIco(){
          this.selected = ''
          this.$emit('input')
        }
      }
    }
</script>

<style lang="scss" scoped>
  @import "~@/styles/mobileMenu/menuIcon.css";
  .y-select-ico-layout{
    width: 65px;
    max-height: 60px;
    position: relative;
    .el-icon-error{
      position: absolute;
      right: -15px;
      top:-15px;
      color: #ff4949;
      font-size: 30px;
      background-color: #fff;
      border-radius: 100%;
      cursor: pointer;
    }
  }
  .mobile-ico{
    width: 60px;
    height: 54px;
    display: inline-block;
    text-align: center;
    box-shadow:0px 5px 20px 0px rgba(151,151,151,0.3);
    cursor: pointer;
    margin: 2px;
    background-color: rgba(24,144,255,0.6);
    &.selected{
      background-color: rgba(24,144,255,1);
    }
  }
  .y-select-ico-box{
    .el-popover__reference{
      width: 65px;
      max-height: 60px;
    }
  }
</style>
