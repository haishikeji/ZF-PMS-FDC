<template>
  <div class="app-container">
    <y-data-view-page-layout title="市场管理首页" class="staff-admin-index">
      <y-data-view-module>
        <div style="width: 1660px">
          <y-d-v-over-view style="width: 220px" title="本年签约目标" :typeId="1" :ico="require('@/assets/statics/pg8.png')" :data="[{text:marketStaticsInfo.yearSaleTarget,value:'元'}]" @click.native="openSet('本年签约目标')"></y-d-v-over-view>
          <y-d-v-over-view style="width: 220px" title="本年回款目标" :typeId="2" :ico="require('@/assets/statics/pg8.png')" :data="[{text:marketStaticsInfo.yearPaymentTarget,value:'元'}]" @click.native="openSet('本年回款目标')"></y-d-v-over-view>
          <y-d-v-over-view style="width: 220px" title="潜在商机" :typeId="3" :ico="require('@/assets/statics/pg8.png')" :data="[{text:marketStaticsInfo.businessNum,value:'个'}]" @click.native="toBusinessPage()"></y-d-v-over-view>
          <y-d-v-over-view style="width: 220px" title="预计签约金额" :typeId="6" :ico="require('@/assets/statics/pg9.png')" :data="[{text:marketStaticsInfo.estimateAmount,value:'元'}]" @click.native="toBusinessPage()"></y-d-v-over-view>
          <y-d-v-over-view style="width: 220px" title="本年已签约" :typeId="4" :ico="require('@/assets/statics/pg9.png')" :data="[{text:marketStaticsInfo.yearSaleDone,value:'元'}]" @click.native="toItemPage()"></y-d-v-over-view>
          <y-d-v-over-view style="width: 220px" title="本年已回款" :typeId="5" :ico="require('@/assets/statics/pg9.png')" :data="[{text:marketStaticsInfo.yearPaymentDone,value:'元'}]" @click.native="toPaymentPage('year')"></y-d-v-over-view>
          <y-d-v-over-view style="width: 220px" title="本月已回款" :typeId="6" :ico="require('@/assets/statics/pg9.png')" :data="[{text:marketStaticsInfo.monthPaymentDone,value:'元'}]" @click.native="toPaymentPage('month')"></y-d-v-over-view>
          <el-tooltip class="item" effect="dark" content="当月回款完成率" placement="bottom">
            <el-progress  style=" margin-left:12px ;width: 91%" :text-inside="true" :stroke-width="24" :percentage=percentage status="success" />
          </el-tooltip>
        </div>
      </y-data-view-module>
      <div class="calendar-class">
        <span style="margin-left: 20px">工作台历</span>
        <el-calendar v-model="value">
          <div class="cell"
            slot="dateCell"
            slot-scope="{date, data}" @click="openDrawer(data)" style="width: 100%;height: 100%">
            <span :class="data.isSelected ? 'is-selected' : ''" >
              {{ data.day.split('-').slice(1).join('-') }} {{ data.isSelected ? '✔️' : ''}}
            </span>
            <el-icon v-if="checkStar(data.day)"  class="el-icon-star-on" style="color: #ffdf1a;font-size: 30px "/>
          </div>
        </el-calendar>
      </div>
      <el-drawer
        :title="title"
        :before-close="handleClose"
        :visible.sync="dialog"
        direction="rtl"
        custom-class="demo-drawer"
        ref="drawer"
      >
        <div class="demo-drawer__content">
          <el-form :model="form">
            <el-col :span="10" style="padding-right: 10px">
              <y-data-view-module>
                <el-button type="warning" style="position: absolute;top:20px;right:60px;font-weight: bolder;cursor: pointer" @click="dialogVisible = true" >添加内容</el-button>
                <div class="plan-class">
                  <div style="width: 530px;display: flex " v-for="(item,index) in dynamicTags">
                    <span style="display:inline-block;width:500px;word-break: break-all; white-space:normal;margin: 10px 10px 10px 10px;line-height: 25px;">
                      {{ index+1 }}、{{ item.content }}
                    </span>
                    <el-icon class="el-icon-edit-outline" style="color: red;cursor:pointer; margin-left: 5px" @click.native="editNote(item)"/>
                    <el-icon class="el-icon-circle-close" style="color: red;cursor:pointer; margin-left: 5px" @click.native="deltag(item.id)"/>
                  </div>
                  <span v-if="dynamicTags.length<8 & dynamicTags.length!=0" style="display:inline-block;width:530px;height:400px;word-break: break-all; white-space:normal;margin: 10px 10px 10px 10px;line-height: 25px;" >
                  </span>
                  <div class="plan-class" v-if="dynamicTags.length===0">
                    <span style="display:inline-block;width:530px;height:500px;word-break: break-all; white-space:normal;margin: 10px 10px 10px 10px;line-height: 25px;">
                    </span>
                  </div>
                </div>
              </y-data-view-module>
              <el-dialog
                title="便签内容"
                :visible.sync="dialogVisible"
                width="30%"
                custom-class="tag-class"
              >
                <el-input
                  type="textarea"
                  :rows="2"
                  placeholder="请输入内容"
                  v-model="textareadata"
                />
                <span slot="footer" class="dialog-footer">
                  <el-button @click="dialogVisible = false">取 消</el-button>
                  <el-button type="primary" @click="addtag">确 定</el-button>
                </span>
              </el-dialog>
            </el-col>
          </el-form>
        </div>
      </el-drawer>
      <el-dialog
        :title="setName"
        width="600px"
        :visible.sync="setDialogFormVisible"
        :close-on-click-modal="false"
      >
        <template>
          <el-table
            :data="tableData"
            border
            style="width: 100%">
            <el-table-column
              prop="date"
              label="姓名"
              align="center"
              width="280px">
              <template slot-scope="{row}">
                <span>{{ row.userName }}</span>
              </template>
            </el-table-column>
            <el-table-column v-if="setName==='本年签约目标' "
              prop="name"
              label="签约目标"
              align="center"
              width="278px">
              <template slot-scope="{row}">
                <span>{{ row.saleTarget }}</span>
              </template>
            </el-table-column>
            <el-table-column v-if="setName==='本年回款目标' "
              prop="name"
              label="回款目标"
              align="center"
              width="278px">
              <template slot-scope="{row}">
                <span>{{ row.paymentTarget }}</span>
              </template>
            </el-table-column>
          </el-table>
        </template>
        <br>
        <el-form
          ref="setForm"
          :model="temp"
          label-position="right"
          label-width="110px"
          style="width: 400px; margin-left:50px;"
        >
          <el-form-item label="我的目标：" prop="amount">
            <el-input v-model.number="temp.amount" class="filter-item" type="number"/>
          </el-form-item>
          <el-form-item label="">
            <el-button @click="setDialogFormVisible = false">
              取消
            </el-button>
            <el-button type="primary" @click="saveSet()">
              保存
            </el-button>
          </el-form-item>
        </el-form>
      </el-dialog>
    </y-data-view-page-layout>
  </div>
</template>
<script>
  import YDVOverView from '@/components/YDataViewPageLayout/YDVOverView'
  import YDataViewModule from '@/components/YDataViewPageLayout/YDataViewModule'
  import YDataViewPageLayout from '@/components/YDataViewPageLayout'

  export default {
    name: 'logIndex',
    components: {
      YDVOverView,
      YDataViewModule,
      YDataViewPageLayout
    },
    data() {
      return {
        value: new Date(),
        marketStaticsInfo:{},
        table: false,
        dialog: false,
        loading: false,
        form: {
          name: '',
          region: '',
          date1: '',
          date2: '',
          delivery: false,
          type: [],
          resource: '',
          desc: ''
        },
        formLabelWidth: '80px',
        timer: null,
        currentDate:'',
        editId:'',
        textareadata:'',
        dialogVisible:false,
        dynamicTags: [],
        calandarList:[],
        setDialogFormVisible:false,
        temp:{
          saleTarget:null,
          paymentTarget:null,
          amount:null
        },
        setName:'',
        tableData:[],
        noteId:null,
      }
    },
    created() {
      this.getCanlendarList();
      this.getMarketStat();
      
    },
    computed:{
      title(){
        return this.currentDate +" 工作计划";
      },
      percentage(){
          const date = new Date();
        const month = date.getMonth() + 1;
        const target = this.marketStaticsInfo.yearPaymentTarget;
        const paymentDone = this.marketStaticsInfo.yearPaymentDone;
        const currentTarget = target/10 * month===0?1:target/10 * month;
        const percent = Math.round((paymentDone/currentTarget) * 100)
        if (!isNaN(percent)){
          return Math.round((paymentDone/currentTarget) * 100);
        }
      }
    },
    methods: {
      editNote(item){
        this.dialogVisible = true;
        this.textareadata = item.content;
        this.noteId = item.id;
      },
      toItemPage(){
        this.$router.push(`/item/list/mine?isCurYear=true`);
      },
      toBusinessPage() {
        this.$router.push(`/market/business`)
      },
      toPaymentPage(val) {
        const date = new Date();
        if (val === 'year'){
          this.$router.push(`/market/payment/list?isCurYear=true`)
        }
        if (val === 'month'){
          this.$router.push(`/market/payment/list?isCurMonth=true`)
        }
      },
      saveSet(){
        if (isNaN(this.temp.amount) || this.temp.amount===null){
          this.$notify({
            title: '失败',
            message: '请输入数字',
            type: 'error',
            duration: 2000
          });
          return
        }
        if (this.setName === '本年签约目标'){
          this.temp.saleTarget = this.temp.amount;
        }
        if (this.setName === '本年回款目标'){
          this.temp.paymentTarget = this.temp.amount;
        }
        let data = {
          saleTarget:this.temp.saleTarget,
          paymentTarget:this.temp.paymentTarget
        };
        this.$api.userTarget.add(data).then(data => {
            if (data.code === 200) {
              this.setDialogFormVisible = false;
              this.$notify({
                title: '成功',
                message: '修改成功',
                type: 'success',
                duration: 2000
              });
              this.getMarketStat();
          }else{
            this.$message.success(data.msg)
          }
        })
      },
      openSet(key){
        this.$api.userTarget.list().then(data=>{
          if (data.code ===200){
            this.tableData = data.data
          }else{
            this.$message.success(data.msg)
          }
        });
        this.temp.amount = null;
        this.setName = key;
        this.setDialogFormVisible = true;
      },
      getCanlendarList(){

        let userId = this.$store.getters.userInfo.id;

        this.$api.note.countByDate(userId).then(data => {
          if (data.code ===200){
            this.calandarList = data.data
          }else{
            this.$message.success(data.msg)
          }

        })
      },
      checkStar(day){
        this.flag = false;
        for(let item of this.calandarList){
          if (item.noteDate === day){
            this.flag = true;
          }
        }
        return this.flag;
      },
      addtag(){
        let data = {
          noteDate:this.currentDate,
          content:this.textareadata,
          userId: this.$store.getters.userInfo.id,
          id:this.noteId
        };
        if(data.id){
          this.$api.note.edit(data).then(data => {
            if (data.code ===200){
              this.$message.success('修改成功');
              this.dialogVisible=false
              this.textareadata = '';
              this.tagdata();
              this.editId = '';
              this.getCanlendarList();
            }else{
              this.$message.success(data.msg)
            }
            this.noteId = null;
          })
        }else{
          this.$api.note.add(data).then(data => {
            if (data.code ===200){
              this.$message.success('保存成功');
              this.dialogVisible=false;
              this.textareadata = '';
              this.tagdata();
              this.editId = '';
              this.getCanlendarList();
            }else{
              this.$message.success(data.msg)
            }

          })
        }
      },
      tagdata(){
        let data = {
          noteDate:this.currentDate,
          userId: this.$store.getters.userInfo.id
        }
        this.$api.note.listByDate(data).then(data => {
          if (data.code ===200){
            this.dynamicTags = data.data
          }else{
            this.$message.success(data.msg)
          }

        })
      },
      deltag(val){
        this.$api.note.delete(val).then(data => {
          if (data.code ===200){
            this.$message.success('删除成功')
            this.tagdata()
            this.getCanlendarList();
          }else{
            this.$message.success(data.msg)
          }

        })
      },
      handleClose(done) {
        if (this.loading) {
          return;
        }
          done();
          // 动画关闭需要一定的时间
          setTimeout(() => {
            this.loading = false;
          }, 400);
      },
      cancelForm() {
        this.loading = false;
        this.dialog = false;
        clearTimeout(this.timer);
      },
      openDrawer(date){
        this.dialog = true
        this.currentDate = date.day;
        this.tagdata();
      },
      getMarketStat(){
        this.$api.marketStat.getMarketStat().then(res => {
          if (res.code === 200){
            this.marketStaticsInfo = res.data;
          }
        });
      },
    }
    }
</script>
<style lang="css" scoped>

  .calendar-class{
    margin-left: 20px;
    height: 500px;
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
  /deep/.tag-class{
    border-radius: 20px;
  }
  div.cell{
    float: right;
  }
</style>
