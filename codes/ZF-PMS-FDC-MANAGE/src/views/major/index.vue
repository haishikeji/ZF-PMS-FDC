<template>
  <div class="app-container">
    <y-data-view-page-layout title="大中型首页" class="staff-admin-index">
      <y-data-view-module>
        <div style="width: 100%">
          <y-d-v-over-view style="width: 12.5%" title="我的待办" :typeId="2" :ico="require('@/assets/statics/pg8.png')" :data="[{text:majorStaticsInfo.majorToDo,value:'任务'}]" @click.native="todoPage()" ></y-d-v-over-view>
          <y-d-v-over-view style="width: 12.5%" title="我的已办" :typeId="1" :ico="require('@/assets/statics/pg9.png')" :data="[{text:majorStaticsInfo.majorDone,value:'任务'}]" @click.native="toDonePage()"></y-d-v-over-view>
          <y-d-v-over-view style="width: 12.5%" title="进行中项目" :typeId="5" :ico="require('@/assets/statics/pg8.png')" :data="[{text:majorStaticsInfo.pending,value:'单'}]" @click.native="toPendingPage()"></y-d-v-over-view>
          <y-d-v-over-view style="width: 12.5%" title="已出价值意见书" :typeId="3" :ico="require('@/assets/statics/pg9.png')" :data="[{text:majorStaticsInfo.statementCount,value:'件'}]" ></y-d-v-over-view>
          <y-d-v-over-view style="width: 12.5%" title="已出报告" :typeId="1" :ico="require('@/assets/statics/pg9.png')" :data="[{text:majorStaticsInfo.reportCount,value:'件'}]"></y-d-v-over-view>
          <y-d-v-over-view style="width: 12.5%" title="已出复评函" :typeId="4" :ico="require('@/assets/statics/pg9.png')" :data="[{text:majorStaticsInfo.letterCount,value:'件'}]"></y-d-v-over-view>
          <y-d-v-over-view style="width: 12.5%" title="已归档产品" :typeId="5" :ico="require('@/assets/statics/pg9.png')" :data="[{text:majorStaticsInfo.saveFileCount,value:'件'}]" @click.native="toSaveFilePage()" ></y-d-v-over-view>
          <y-d-v-over-view style="width: 12.5%" title="库存产品" :typeId="6" :ico="require('@/assets/statics/pg9.png')" :data="[{text:majorStaticsInfo.inWarehouseCount,value:'件'}]" @click.native="toWarehouseingPage()"></y-d-v-over-view>
        </div>
      </y-data-view-module>
    </y-data-view-page-layout>
    <div id="main" class="calendar-class"></div>
  </div>
</template>
<script>
  import YDVOverView from '@/components/YDataViewPageLayout/YDVOverView'
  import YDataViewModule from '@/components/YDataViewPageLayout/YDataViewModule'
  import YDataViewPageLayout from '@/components/YDataViewPageLayout'

  export default {
    name: 'majorBusinessIndex',
    components: {
      YDVOverView,
      YDataViewModule,
      YDataViewPageLayout
    },
    data() {
      return {
        majorStaticsInfo:{},
        nightingaleEcharts:[]
      }
    },
    created() {
      this.getStaticsInfo();
      //this.instanceNodeCountByCode();
    },
    mounted(){
      
    },
    computed:{
     
    },
    methods: {
      getStaticsInfo(){
        this.$api.major.staticsInfo().then(res=>{
          if (res.code ===200){
              this.majorStaticsInfo = res.data;
          }
        })
      },
      initEcharts(xData){
        var chartDom = document.getElementById('main');
        var myChart = this.$echarts.init(chartDom);
        var option;
        option = {
          title: {
            text: '大中型工作流程节点统计图',
            subtext: '实时数据',
            left: 'center'
          },
          tooltip: {
            trigger: 'item',
            formatter: '{a} <br/>{b} : {c} ({d}%)'
          },
          toolbox: {
            show: true,
            feature: {
              mark: { show: true },
              dataView: { show: true, readOnly: false },
              restore: { show: true },
              saveAsImage: { show: true }
            }
          },
          series: [
            {
            name: 'Area Mode',
            type: 'pie',
            radius: [20, 280],
            // center: ['75%', '50%'],
            roseType: 'area',
              itemStyle: {
                borderRadius: 5
              },
              data: xData
            }
          ]
        }
        option && myChart.setOption(option);
      },
      instanceNodeCountByCode(){
        this.$api.workNodeInstance.instanceNodeCountByCode("MAJOR_BUSINESS").then(res=>{
          if (res.code ===200){
              const d = res.data;
              this.initEcharts(d);

          }
        })
      },
      todoPage(){
        this.$router.push(`/major/todo`)
      },
      toDonePage(){
        this.$router.push(`/major/my/done`)
      },
      toPendingPage(){
        this.$router.push(`/major/list`)
      },
      toSaveFilePage(){
        this.$router.push(`/major/saveFileDone`)
      },
      toWarehouseingPage(){
        this.$router.push(`/major/production/in`)
      }
      
    }
  }
</script>
<style lang="css" scoped>

  .calendar-class{
    height:700px;
  }
  .plan-class{
    /*width: 540px;*/
    /*border: 1px solid darkgray;*/
    /*box-shadow: 10px 10px 5px #888888;;*/
    /*border-radius: 10px;*/
    /*overflow: auto;*/
    width: 540px;
    -moz-box-shadow: 0 2px 10px 1px rgba(0, 0, 0, 0.2);
    -webkit-box-shadow: 0 2px 10px 1px rgba(0, 0, 0, 0.2);
    box-shadow: 0 2px 10px 1px rgba(0, 0, 0, 0.2);
    background:
      -moz-repeating-linear-gradient(
        top,
        #fcf59b,
        #fcf59b 29px,
        #81cbbc 30px
      );

    background:
      -webkit-gradient(
        linear,
        left top, left bottom,
        from(#81cbbc),
        color-stop(2%, #fcf59b)
      );

    background:
      repeating-linear-gradient(
        top,
        #fcf59b,
        #fcf59b 29px,
        #81cbbc 30px
      );

    -webkit-background-size: 100% 30px;
  }
  .plan-class:after {
    width: 180px;
    height: 30px;
    content: " ";
    margin-left: -90px;
    border: 1px solid rgba(200, 200, 200, .8);
    background: rgba(254, 254, 254, .6);

    -moz-box-shadow: 0px 0 3px rgba(0, 0, 0, 0.1);
    -webkit-box-shadow: 0px 0 3px rgba(0, 0, 0, 0.1);
    box-shadow: 0px 0 3px rgba(0, 0, 0, 0.1);

    -moz-transform: rotate(-5deg);
    -webkit-transform: rotate(-5deg);
    -o-transform: rotate(-5deg);
    transform: rotate(-5deg);

    position: absolute;
    left: 50%;
    top: 95px;
  }
  /deep/ .tag-class{
    border-radius: 20px;
  }
  div.cell{
    float: right;
  }
</style>
