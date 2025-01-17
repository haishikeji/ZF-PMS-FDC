<template>
  <div class="app-container">
    <y-data-view-page-layout title="日志首页" class="staff-admin-index">
      <y-data-view-module>
        <y-d-v-over-view title="当天日志数" :typeId="1" :ico="require('@/assets/statics/pg9.png')" :data="[{text:logStaticsInfo.currentDayNum,value:'份'}]"></y-d-v-over-view>
        <y-d-v-over-view title="日志总数" :typeId="2" :ico="require('@/assets/statics/pg9.png')" :data="[{text:logStaticsInfo.totalNum,value:'份'}]" ></y-d-v-over-view>
        <y-d-v-over-view title="最新提交人" :typeId="3" :ico="require('@/assets/statics/pg7.png')" :data="[{text:logStaticsInfo.userName}]" ></y-d-v-over-view>
        <y-d-v-over-view title="最新提交日期" :typeId="4" :ico="require('@/assets/statics/pg10.png')" :data="[{text:logStaticsInfo.logDate}]" ></y-d-v-over-view>
      </y-data-view-module>
    </y-data-view-page-layout>
    <div id="main" ></div>
  </div>
</template>
<script>
  import YDVOverView from '@/components/YDataViewPageLayout/YDVOverView'
  import YDataViewModule from '@/components/YDataViewPageLayout/YDataViewModule'
  import YDataViewPageLayout from '@/components/YDataViewPageLayout'
  //require('echarts/theme/macarons') // echarts theme

  export default {
    name: 'logIndex',
    components: {
      YDVOverView,
      YDataViewModule,
      YDataViewPageLayout
    },
    data() {
      return {
        logStaticsInfo:{},
        legend:{
          data:[]
        },
        xAxis:{},
        series:[]
      }
    },
    created() {
      this.getCharsData();
    },
    mounted() {
      this.$nextTick(() => {
        this.getLogStat();
      })
    },
    methods: {
      initChart() {
        var chartDom = document.getElementById('main');
        var myChart = this.$echarts.init(chartDom);
        var option;
        option = {
          title: {
            text: '项目日志每日提交情况'
          },
          tooltip: {
            trigger: 'axis'
          },
          legend: {
            data: this.legend.data
          },
          grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
          },
          toolbox: {
            feature: {
              saveAsImage: {}
            }
          },
          xAxis: this.xAxis,
          yAxis: {
            type: 'value'
          },
          series: this.series
        };
        option && myChart.setOption(option);
      },
      getCharsData() {
        this.$api.itemLog.getCharsData().then(res => {
          if (res.code === 200){
            const data = res.data;
            this.legend.data = data.itemNames;
            this.xAxis = data.xasis;
            this.series = data.items;
            this.initChart();
          }
        });
      },
      getLogStat(){
        this.$api.itemLog.getLogStat().then(res => {
          if (res.code === 200){
            this.logStaticsInfo = res.data;
          }
        });
      },
    }
    }
</script>
<style lang="css" scoped>

  #main{
    width: auto;
    height: 500px;
    margin-top: 100px;
  }
</style>
