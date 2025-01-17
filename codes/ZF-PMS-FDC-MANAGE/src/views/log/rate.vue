<template>
  <div class="app-container">
    <div class="title-container">
      <breadcrumb id="breadcrumb-container" class="breadcrumb-container"/>
    </div>
    <div class="condition">
      <el-date-picker
        v-model="startDate"
        type="date"
        value-format="yyyy-MM-dd"
        style="width: 200px;margin-top: 10px;margin-left: 17px"
        placeholder="开始日期"
      />
      <el-date-picker
        v-model="endDate"
        type="date"
        value-format="yyyy-MM-dd"
        style="width: 200px ;margin-top: 10px;margin-left: 17px"
        placeholder="结束日期"
      />
      <el-button
        class="filter-item"
        style="margin-left: 10px;"
        type="primary"
        @click="getLogs"
        round
      >搜索
      </el-button>
    </div>
    <y-page-list-layout>
      <el-table :data="tableData" slot="table" style="width: 100%;" border="border" height="900px" :row-style="{height:'100px'}">
        <el-table-column v-if="tableData.length>0" label="日期" width="150px" align="center" fixed>
          <template slot-scope="{row}">
            <span>{{row.logDate}}</span>
          </template>
        </el-table-column>
        <el-table-column :label="name" v-for="(name,index) in userNames" :key="index" width="520px">
          <template slot-scope="{row}">
            <div v-for="(l,index) in row.list" :key="index">
              <div class="content" v-if = "g.userName===name" v-for="g in l.logs" :key="g.id">
                <el-tag effect="dark">{{g.itemName}}</el-tag>
                <el-tag type="success" effect="dark">{{g.stageName===null?'无':g.stageName}}</el-tag>
                <div class="info">
                  <div id="info-item" type="info" effect="dark">
                    {{g.taskSituation}}
                  </div>
                </div>
              </div>
            </div>
          </template>
        </el-table-column>
      </el-table>
    </y-page-list-layout>
    <el-dialog
      width="600px"
      title="日志详情"
      :visible.sync="dialogFormVisible"
      :close-on-click-modal="false"
      custom-class="myDialog"
    >
      <el-form ref="postForm" :model="postForm" class="form-container">
        <div>
          <div class="postInfo-container">
            <el-row>
              <el-col :xs="24" :sm="12" :lg="18" :span="6">
                <el-form-item
                  label="项目名称："
                  prop="itemName"
                  label-width="180px"
                  class="postInfo-container-item"
                >
                  <el-input :value="postForm.itemName" class="filter-item" readonly/>
                </el-form-item>
              </el-col>
            </el-row>
          </div>
        </div>
        <div>
          <div class="postInfo-container">
            <el-row>
              <el-col :xs="24" :sm="12" :lg="18" :span="6">
                <el-form-item
                  label="阶段名称："
                  prop="stageName"
                  label-width="180px"
                  class="postInfo-container-item"
                >
                  <el-input :value="postForm.stageName" class="filter-item" readonly disabled/>
                </el-form-item>
              </el-col>
            </el-row>
          </div>
        </div>
        <div>
          <div class="postInfo-container">
            <el-row>
              <el-col :xs="24" :sm="12" :lg="18" :span="6">
                <el-form-item
                  label="日期："
                  prop="logDate"
                  label-width="180px"
                  class="postInfo-container-item"
                >
                  <el-date-picker
                    :value="postForm.logDate"
                    type="date"
                    value-format="yyyy-MM-dd"
                    style="width: 100%"
                    placeholder="选择日期"
                    readonly
                  />
                </el-form-item>
              </el-col>
            </el-row>
          </div>
        </div>
        <div>
          <div class="postInfo-container">
            <el-row>
              <el-col :xs="24" :sm="12" :lg="18" :span="6">
                <el-form-item
                  label="每日任务情况："
                  prop="taskSituation"
                  :rules="{required: true, message: '请填写每日任务情况', trigger: 'blur'}"
                  label-width="180px"
                  class="postInfo-container-item"
                >
                  <el-input type="textarea" :value="postForm.taskSituation" class="filter-item" readonly/>
                </el-form-item>
              </el-col>
            </el-row>
          </div>
        </div>
      </el-form>
    </el-dialog>
  </div>
</template>
<script>
  import Breadcrumb from '@/components/Breadcrumb'
  import YDetailPageLayout from '@/components/YDetailPageLayout'

  export default {
    name: 'itemRate',
    components: {
      Breadcrumb,
      YDetailPageLayout
    },
    data() {
      return {
        type: 'detail',
        br: '<br/>',
        postForm: {
          itemId: this.$route.query.id,
          userId: this.$store.getters.userInfo.id,
          logDate:null
        },
        itemId: this.$route.query.id,
        itemName: this.$route.query.itemName,
        activeName: 'first',
        vLoading: false,
        listQuery:{},
        scope: true,
        tableData: [],
        dates:[],
        dialogFormVisible: false,
        userNames:[],
        startDate:null,
        endDate:null
      }
    },
    created() {
      this.endDate = this.timeDefault;
      this.startDate = this.getago(this.endDate, -10);
      this.getLogs();
    },
    mounted() {
      // const d = this.tableData;
      // console.log(d);
      // d.forEach(function (e) {
      //   console.log(e.logDate);
      // })
    },
    computed:{
      timeDefault() {
        const date = new Date();
        const s1 = date.getFullYear() + "-" + (this.getZero(date.getMonth() + 1)) + "-" + this.getZero((date.getDate()));
        return s1;
      },
    },
    methods:{
      getago(startDate, valueTime){
        var date = new Date(startDate);
        var newDate = new Date(date.getFullYear(), date.getMonth(), date.getDate()+ +valueTime);
        var Y = newDate.getFullYear();
        var M = newDate.getMonth()+1;
        M = M<10?'0'+M:M;
        var D = newDate.getDate();
        D = D<10?'0'+D:D;
        return `${Y}-${M}-${D}`;
      },
      getZero(num) {
        // 单数前面加0
        if (parseInt(num) < 10) {
          num = '0' + num;
        }
        return num;
      },
      openDetail(id){
        const that = this;
        this.$api.itemLog.detail(id).then(res=>{
          this.postForm = res.data;
          setTimeout(() => {
            that.dialogFormVisible = true;
          }, 200)
        }).catch(
          that.dialogFormVisible = false
        )
      },
      getLogs(){
        this.$api.itemLog.collect({scope:this.scope, itemId:this.itemId, startDate:this.startDate, endDate:this.endDate}).then(res=>{
          this.tableData = res.data;
          const us = this.userNames;
          const d = this.tableData;
          d.forEach(function (e) {
            us.push(e.userName);
          });
          this.userNames = Array.from(new Set(us));
          this.tableData = this.groupByDate(d, 'logDate');
        });
      },
      groupByDate(list, key){
        var keysArr = list.map(item=>item[key])
        var keys = [...new Set(keysArr)]
        var newList = keys.map(item=> {
          return {
            [key]: item,
            list: list.filter(i=>i[key] === item)
          }
        })
        return newList;
      }
    }
  }
</script>
<style lang="scss" scoped>
  div.content{
    margin-bottom: 5px;
    width: 520px;
  }
  .info {
    width: 500px;
    height: auto;
  }
  #info-item{
    text-overflow: ellipsis !important;
    white-space: pre-wrap !important;
  }
  /deep/.myDialog{
    border-radius: 20px;
  }
</style>
