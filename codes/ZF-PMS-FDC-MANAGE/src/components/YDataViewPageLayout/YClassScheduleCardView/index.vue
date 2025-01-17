<template>
  <div class="y-class-schedule-card-view">
    <div class="box box-y">
      <template v-if="data.y && data.y.length > 0">
        <div class="item item-y"><span class="title"></span></div>
        <div :class="`item item-y ${item.ifBreaktime?'empty':''}`" v-for="(item,index) in data.y" :key="index">
          <template v-if="item.ifBreaktime">
            <span class="title">{{item.period || ''}}</span>
            <span class="sub-title">{{`${(item.startTime && item.startTime.split(':').slice(0,2).join(':')) || ''} - ${(item.endTime && item.endTime.split(':').slice(0,2).join(':')) || ''}`}}</span>
          </template>
          <template v-else>
            <span class="title">{{`${item.period}${item.segment}`}}</span>
            <span class="sub-title">{{`${(item.startTime && item.startTime.split(':').slice(0,2).join(':')) || ''} - ${(item.endTime && item.endTime.split(':').slice(0,2).join(':')) || ''}`}}</span>
          </template>
        </div>
      </template>
    </div>
    <div class="box box-x" style="cursor : pointer;">
      <template v-if="data.x && data.x.length > 0">
        <div  class="group" v-for="(itemX,index) in data.x" :key="index">
          <div class="item item-x">
            <span class="title">{{itemX.name}}</span>
          </div>
          <template v-if="data.y && data.y.length > 0">
            <div @click="gotoUrl"  :class="`item item-x course ${(course(itemX.id,itemY.id).title || course(itemX.id,itemY.id).subTitle)?'has':''} ${itemY.ifBreaktime?'empty':''}`" v-for="(itemY,index) in data.y" :key="index">
              <template v-if="itemY.ifBreaktime">
                <span class="title">{{itemY.segment || ''}}</span>
              </template>
              <template v-else>
                <span class="title">{{course(itemX.id,itemY.id).title}}</span>
                <span class="sub-title">{{course(itemX.id,itemY.id).subTitle}}</span>
              </template>
            </div>
          </template>
        </div>
      </template>
    </div>
    <div class="box box-end">
      <template v-if="data.y && data.y.length > 0">
        <div class="item item-y" @click="gotoUrl"><span class="title"></span></div>
        <div @click="gotoUrl" :class="`item item-y ${item.ifBreaktime?'empty':''}`" v-for="(item,index) in data.y" :key="index"></div>
      </template>
    </div>
  </div>
</template>

<script>
  export default {
    name: "YClassScheduleCardView",
    props:{
      data:{
        type:Object
      },
      url:{
        type:String,
        default:''
      },
      //课程内容字段映射key
      //{title:'T',subTitle:'T1'}
      modelKey:{
        type:Object,
        default: function () {
          return {}
        }
      },
    },
    computed:{
      course(){
        return function (xid,yid) {
          let item ={
            title:'',
            subTitle:''
          }
          if (this.data.metaDataList && this.data.metaDataList.length > 0){
            let metaData = this.data.metaDataList.find(m => m.xid == xid && m.yid == yid)
            if (metaData) {
              item.title = this.modelKey.title && metaData[this.modelKey.title]
              item.subTitle = this.modelKey.subTitle && metaData[this.modelKey.subTitle]
            }
          }
          return item
        }
      }
    },
    methods:{
      gotoUrl(){
        let that = this
        if(that.url){
          that.$router.push({
            path: that.url,
          })
        }
      },
    }
  }
</script>
