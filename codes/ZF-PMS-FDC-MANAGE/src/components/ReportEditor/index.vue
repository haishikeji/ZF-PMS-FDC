
<template>
  <div class="report">
    <div v-if="processLoading" class="loader"></div>

    <!-- 一级标题 -->
    <el-tabs v-loading="listLoading" tab-position="top" style="height: auto; width: 99%;" type="border-card" @tab-click="pickHeadline1" >
      <el-tab-pane label="名词配置">
        <el-divider content-position="right" style="margin-top:100px;"><el-button type="success" @click="saveNouns" :disabled="ableAdd">保存</el-button>
          <el-button type="danger" @click="generateReport" :disabled="ableAdd">生成报告</el-button>
            </el-divider>
        <div class="nouns-area">
          <el-collapse v-model="activeNames">
            <el-collapse-item  name="common">
              <template slot="title">
                <span style="margin-left: 40%; font-weight: bold;  color:gray ;letter-spacing: 2px;">通用名词</span>
                <i class="el-icon-s-grid" style=" color:gray ;"></i>
              </template>
              <div>
                <el-row :gutter="20" v-for="(n3, index) in commonNouns" :key="index" style="margin-bottom: 10px;">
                  <el-col :span="8" v-for="(n, index) in n3" :key="index">
                <el-card shadow="hover" :body-style="{ padding: '5px' }" class="cardHover">
                  <div style="display: flex; float: right; margin-bottom: 5px;" >
                    <div>
                      <el-tag type="info">中文含义</el-tag>
                      <input class="nounsInput" v-model="n.name"  disabled />
 
                    </div>
                    <div>
                      <el-tag type="info">占位符</el-tag>
                      <input class="nounsInput" v-model="n.placeholder" disabled/>
                    </div>
                    <div>
                      <el-tag type="info" :style="setValueStyle(n.value)">替换值</el-tag>
                      <input class="nounsInput" v-model="n.value" @blur="check($event)"/>
                    </div>
                  </div>
                </el-card>
              </el-col>
            </el-row>
              </div>
            </el-collapse-item>
            <el-collapse-item  name="custom">
              <el-divider content-position="right"><el-button type="success" @click="addCustomNoun" :disabled="ableAdd">添加</el-button>
            </el-divider>
              <template slot="title">
                <span style="margin-left: 40%; font-weight: bold; color:gray ; letter-spacing: 2px;">自定义名词</span>
                <i class="el-icon-s-grid" style="  color:gray ;"></i>
              </template>
              <div>
                <el-row :gutter="20" v-for="(n3, index) in customNouns" :key="index" style="margin-bottom: 10px;">
              <el-col :span="8" v-for="(n, index) in n3" :key="index">
                <el-card shadow="hover" :body-style="{ padding: '5px' }"  class="cardHover">
                  <div style="display: flex; float: right; margin-bottom: 5px;">
                    <div>
                      <el-tag type="info" :style="setValueStyle(n.name)">中文含义</el-tag>
                      <input class="nounsInput" v-model="n.name" @blur="check($event)"  />
                    </div>
                    <div>
                      <el-tag type="info" :style="setValueStyle(n.placeholder)">占位符</el-tag>
                      <input class="nounsInput" v-model="n.placeholder" @blur="check($event)" />
                    </div>
                    <div>
                      <i class="el-icon-error nouns-del" @click="deleteCard(n)"></i>
                      <el-tag type="info" :style="setValueStyle(n.value)">替换值</el-tag>
                      <input class="nounsInput" v-model="n.value" @blur="check($event)"/>
                    </div>
                  </div>
                </el-card>
              </el-col>
            </el-row>
              </div>
            </el-collapse-item>
          </el-collapse>
        </div>
      </el-tab-pane>
      <el-tab-pane v-for="(t, index) in headlineTree" :key="index" :label="t.headline" :name="t.id + ''" :lazy=true>
        <!-- 二级标题 -->
        <el-tabs v-loading="listLoading" v-model="activeName" v-if="t.children!=null && t.children.length > 0" tab-position="top"
          style="height: auto;" @tab-click="pickHeadline2">
          <el-tab-pane v-for="(tc, index) in t.children" :key="index" :label="tc.headline" :name="tc.id + ''" :lazy=true>
            <span slot="label"><i class="el-icon-success" :style=setIstyle(tc.section)></i> {{ tc.headline }}</span>
            <span slot="label" v-if="tc.action!=null">
              <img src="../../assets/icon/jisuanqi.png" class="jisuan-icon"  alt="" @click="openTableDialog(tc.headline)"></span>
            <!-- 三级标题 -->
            <el-tabs v-loading="listLoading" v-model="activeName1" v-if="tc.children != null && tc.children.length > 0"
              tab-position="top" style="height: auto;" @tab-click="pickHeadline3">
              <el-tab-pane v-for="(tcc, index) in tc.children" :key="index" :label="tcc.headline" :name="tcc.id + ''" :lazy=true>
                <span slot="label"><i class="el-icon-success" :style=setIstyle(tcc.section)></i> {{ tcc.headline }}</span>
                <span slot="label" v-if="tcc.action!=null">
                  <el-tooltip class="item" effect="dark" content="点击输入计算数据" placement="top-start">
                    <img src="../../assets/icon/jisuanqi.png" class="jisuan-icon" alt="" @click="openTableDialog(tcc.headline)">
                  </el-tooltip>
                </span>
                <div>
                  <el-row :gutter="850">
                    <el-col :span="6">
                      <div class="editor-area">
                        <YUeditor :content="tcc.section" :unique=tcc.sectionId  ref="euditor" :config={offsetWidth:1,offsetHeight:1} />
                        <div style="text-align: center;">
                          <el-button type="success" style="margin-top: 10px;"
                            @click="saveSection(tcc.sectionId)">保存至文档</el-button>
                        </div>
                      </div>
                    </el-col>
                    <el-col :span="6">
                      <div v-if="tricks.length > 0" class="section-area">
                        <ReportContentCarousel :tricks="tricks" @reference="getSection" />
                      </div>
                      <div v-else class="null-area">
                        <el-empty :image="require('../../assets/images/null.png')" :image-size=500
                          description="还没有段落模板，快去创建吧。">
                        </el-empty>
                      </div>
                    </el-col>
                  </el-row>
                </div>
              </el-tab-pane>
            </el-tabs>
            <div v-else>
              <el-row :gutter="850">
                <el-col :span="6">
                  <div class="editor-area">
                    <YUeditor :content="tc.section" :unique=tc.sectionId ref="euditor" :config={offsetWidth:1,offsetHeight:1} />
                    <div style="text-align: center;">
                      <el-button type="success" style="margin-top: 10px;"
                        @click="saveSection(tc.sectionId)">保存至文档</el-button>
                    </div>
                  </div>
                </el-col>
                <el-col :span="6">
                  <div v-if="tricks.length > 0" class="section-area">
                    <ReportContentCarousel :tricks="tricks" @reference="getSection" />
                  </div>
                  <div v-else class="null-area">
                    <el-empty :image="require('../../assets/images/null.png')" :image-size=500
                      description="还没有段落模板，快去创建吧。">
                    </el-empty>
                  </div>
                </el-col>
              </el-row>
            </div>
          </el-tab-pane>
        </el-tabs>
        <div v-else>
          <div>
              <el-row :gutter="850">
                <el-col :span="6">
                  <div class="editor-area">
                    <YUeditor :content="t.section" :unique=t.sectionId ref="euditor" :config={offsetWidth:1,offsetHeight:1} />
                    <div style="text-align: center;">
                      <el-button type="success" style="margin-top: 10px;"
                        @click="saveSection(t.sectionId)">保存至文档</el-button>
                    </div>
                  </div>
                </el-col>
                <el-col :span="6">
                  <div v-if="tricks.length > 0" class="section-area">
                    <ReportContentCarousel :tricks="tricks" @reference="getSection" />
                  </div>
                  <div v-else class="null-area">
                    <el-empty :image="require('../../assets/images/null.png')" :image-size=500
                      description="还没有段落模板，快去创建吧。">
                    </el-empty>
                  </div>
                </el-col>
              </el-row>
            </div>
        </div>
      </el-tab-pane>
    </el-tabs>
    <div class="generator">
      <el-tag style="margin-top: 3px;">{{ title }}</el-tag>
    </div>
    <el-dialog :visible.sync="dialogVisible" width="80%"  center :title="dialogName" @closed="resetTableData">
        <el-form label-width="550px" >
            <el-card v-for="(t,index ) in inputData.inputTableData" :key=index>
              <div slot="header" class="clearfix">
                <el-tag style="font-size:larger;">{{ t.tableName }}</el-tag>
                <el-button style="font-size:larger; float: right; padding: 3px 0" type="text" icon="el-icon-circle-plus" @click=addTableData(t.tableData,t.tableHead)>添加</el-button>
                <el-button style="font-size:larger; float: right; padding: 3px 0" type="text" icon="el-icon-remove" @click=cleanTableData(index)>清除</el-button>
              </div>
                <el-table :data="t.tableData"  border style="width: 100%" >
                  <el-table-column
                    :label="t.label"
                    :prop="t.prop"
                    v-for="(t, index) in t.tableHead"
                    :key="index"
                  >
                  <template slot-scope="scope">
                    <el-input type="text" v-model.trim="scope.row[t.prop] " />
                  </template>
                  </el-table-column>
              </el-table>
            </el-card>
            <el-card v-if="inputData.params">
              <div slot="header" class="clearfix">
                <el-tag style="font-size:larger;">固定参数</el-tag>
              </div>
              <div style="width: 50%;">
                <el-form-item v-for="(i,index ) in inputData.params" :key=index :label=i.label :prop="i.name">
                  <el-input  v-model.trim="i.value"/>
                </el-form-item>
              </div>
            </el-card>
        </el-form>
        <span slot="footer" class="dialog-footer">
            <el-button @click="dialogVisible = false">取 消</el-button>
            <el-button type="primary" @click=saveTableData>保 存</el-button>
        </span>
    </el-dialog>
  </div>

</template>
  
<script>
import YUeditor from "@/components/YUeditor"
import ReportContentCarousel from "@/components/ReportContentCarousel"
export default {
  components: {
    YUeditor,
    ReportContentCarousel
  },

  props: {
    headlineTree: {
      type: Array,
      require: true,
    }

  },
  watch: {

  },
  computed: {

  },
  data() {
    return {
      dialogVisible:false,
      processLoading: false,
      listLoading: true,
      activeName: null,
      activeName1: null,
      tricks: [],
      pickedNode1Childrens: [],
      pickedNode2Childrens: [],
      activeNode: {
        section: null,
      },
      filled: false,
      reportId: this.$route.query.id,
      title:this.$route.query.title,
      nouns:{},
      commonNouns1: [],
      commonNouns: [],
      customNouns: [],
      customNouns1: [],
      newNoun:{
        "name": null, "placeholder": null, "value": null
      },
      reportMain:{
        id:null,
        reportNouns:{
        }
      },
      activeNames:["common","custom"],
      ableAdd:false,
      dialogName:null,
      inputData:{
        action:null,
        sectionId:null,
        params:[],
        inputTableData:[
          {
            tableName:null,
            tableNo:null,
            tableHead:[],
            tableData:[]
         
          }
        ],
        jsonString:null,
      },

      // calculateData:{
      //   action:null,
      //   calculateDataDTO:{
      //     sectionId:null,
      //     tableData:[],
      //     params:[]
      //   },
      //   jsonString:null
      // }
    }
  },

  mounted(){

  },
  created() {
    this.getNouns();
  },
  methods: {
    getReportSectionList(headlineId) {
      const that = this;
      that.listLoading = true
      that.$api.reportSectionTemplate.getReportSectionList(headlineId).then(res => {
        if (res.code === 200) {
          that.tricks = res.data;
          that.listLoading = false;
        }
      }).catch(() => {
        that.listLoading = false;
      })
    },
    pickHeadline1(tab, event) {
      let tree = this.headlineTree;
      let name = tab.name;
      let pickedNode = tree.find(t => t.id.toString() === name)
      if (typeof pickedNode != 'undefined') {
        if (pickedNode.children != null && pickedNode.children.length > 0) {
          this.pickedNode1Childrens = pickedNode.children,
            this.activeName = pickedNode.children[0].id.toString();
          if (pickedNode.children[0].children != null && pickedNode.children[0].children.length > 0) {
            this.activeName1 = pickedNode.children[0].children[0].id.toString();
            this.getReportSectionList(pickedNode.children[0].children[0].id);
            this.activeNode = pickedNode.children[0].children[0];
            this.pickedNode2Childrens = pickedNode.children[0].children;
          } else {
            this.getReportSectionList(pickedNode.children[0].id);
            this.activeNode = pickedNode.children[0];
            this.pickedNode2Childrens = [];
          }
        } else {
          this.getReportSectionList(name);
          this.activeNode = pickedNode;
          this.pickedNode1Childrens = [];
        }
      }
    },
    pickHeadline2(tab, event) {
      let name = tab.name;
      if (this.pickedNode1Childrens.length > 0) {
        let pickedNode2 = this.pickedNode1Childrens.find(p => p.id.toString() === name);
        if (typeof pickedNode2 != 'undefined') {
          if (pickedNode2.children != null && pickedNode2.children.length > 0) {
            this.pickedNode2Childrens = pickedNode2.children;
            this.activeName1 = pickedNode2.children[0].id.toString();
            this.getReportSectionList(pickedNode2.children[0].id);
            this.activeNode = pickedNode2.children[0];
            this.pickedNode2Childrens = pickedNode2.children;
          } else {
            this.getReportSectionList(name);
            this.activeNode = pickedNode2;
            this.pickedNode2Childrens = [];
          }
        }
      }

    },
    pickHeadline3(tab, event) {
      let name = tab.name;
      this.getReportSectionList(name);
      if (this.pickedNode2Childrens.length > 0) {
        let pickedNode = this.pickedNode2Childrens.find(t => t.id.toString() === name);
        this.activeNode = pickedNode;
      }
    },
    //从子组件获取引用文本
    getSection(section) {
      this.listLoading = true
      let all = [];
      let commons = this.reportMain.reportNouns.common;
      let customs = this.reportMain.reportNouns.custom;
      if (typeof customs  != 'undefined' && customs!=null){
        commons = commons.concat(customs);
      }
      for (let t in commons){
          var reg = new RegExp(commons[t].placeholder,"g")
          if (section.includes(commons[t].placeholder)){
            section = section.replace(reg,commons[t].value)
          }
      
      }
      this.activeNode.section = section;
      this.listLoading = false
      
    },
    saveSection(sectionId) {
      this.listLoading = true;
      this.activeNode.children = [];
      let content = this.$refs.euditor.find(e => e.unique === sectionId).getUEContent();
      if (content==="" || content===null || typeof content ==='undefined'){
        this.$notify({
              title: '错误',
              message: '文档段落还没有内容，请添加内容后保存。',
              type: 'error',
              duration: 2000
            })
            this.listLoading = false;
            return ;
      }
      this.activeNode.section = content;
      if (this.activeNode.sectionId != null && this.activeNode.section != null) {
        this.$api.reportSection.edit(this.activeNode).then(res => {
          if (res.code === 200) {
            this.activeNode.section = res.data;
            this.$notify({
              title: '成功',
              message: '文档段落保存成功',
              type: 'success',
              duration: 2000
            })
            this.listLoading = false;
          } else {
            this.$notify({
              title: '错误',
              message: '文档段落引用失败，请联系管理员',
              type: 'error',
              duration: 2000
            })
            this.listLoading = false;
          }
        }).catch(() => {
          this.listLoading = false;
        })
      }
      this.listLoading = false;
    },
    setIstyle(section) {
      if (section != null) {
        return "color:green";
      }
    },
    setValueStyle(value) {
      if (typeof value ==='undefined' || value===null || value==="") {
        return "color:red";
      }
    },
    generateReport() {
      let _this = this;

      let reportId = _this.reportId;
      if (reportId) {
        _this.processLoading = true;
        setTimeout(() => {
          _this.$api.reportMain.generateReport(reportId).then(res => {
            if (res.code === 200) {
              _this.processLoading = false;
              _this.$notify({
                title: '成功',
                message: '报告已生成,请返回上级页面预览或下载。',
                type: 'success',
                duration: 2000
              })
            }
          }).catch(() => {
            _this.processLoading = false;
          })
        }, 3500)
      }
    },
    addCustomNoun() {
      //将二维数组转一维
      if (this.customNouns.length>0){
        this.nouns.custom = this.customNouns.reduce(function(a,b){
          return a.concat(b);
        })
      }
      this.nouns.custom.push(this.newNoun);
      let arr = JSON.parse(JSON.stringify(this.reportMain.reportNouns.custom))
      this.customNouns = this.arrChange(JSON.parse(JSON.stringify(arr)))
    },
    arrChange(arr) {
      let xArry = [];
      while (arr.length > 0) {
        xArry.push(arr.splice(0, 3));
      }
      return xArry;
    },
    saveNouns(){
      this.listLoading = true;
      this.nouns.common = this.commonNouns.reduce(function(a,b){
        return a.concat(b);
      })
      if (this.customNouns.length>0){
        this.nouns.custom = this.customNouns.reduce(function(a,b){
        return a.concat(b);
      })
      }
      this.reportMain.reportNouns = this.nouns;
      //需不需要判空逻辑
      this.$api.reportMain.edit(this.reportMain).then(res =>{
        if (res.code === 200) {
            this.$notify({
              title: '成功',
              message: '名称配置已更新',
              type: 'success',
              duration: 2000
            })
            this.listLoading = false;
          } else {
            this.$notify({
              title: '错误',
              message: '操作失败，请联系管理员',
              type: 'error',
              duration: 2000
            })
            this.listLoading = false;
          }
      }).catch(() => {
          this.listLoading = false;
        })
    },
    check(event){
      event.stopPropagation();
      //检查是否为空值
      let prevElement = event.currentTarget.previousElementSibling
      if (prevElement.innerHTML==='中文含义' || prevElement.innerHTML==='替换值' ){
        if (event.target.value ===null || event.target.value ===""){
        prevElement.style.color = "red";
        }else{
          prevElement.style.color = "rgb(144, 147, 153)";
        }
      }
     
      //检查是否有重复的placeholder

      if(prevElement.innerHTML==='占位符' || prevElement.innerHTML==='占位符有重复'){
        let common = this.commonNouns.reduce(function(a,b){
            return a.concat(b);
          })
          let custom = [];
          if (this.customNouns.length>0){
            custom = this.customNouns.reduce(function(a,b){
            return a.concat(b);
          })}
          let all = common.concat(custom)
          let newArray =  Array.from(all, ({placeholder}) => placeholder).filter(item => item !== null);
          if (this.isRepeat(newArray) === event.target.value){
            this.ableAdd = true;
            prevElement.innerHTML = "占位符有重复"
            prevElement.style.color = "red";
          }else{
            this.ableAdd = false;
            prevElement.innerHTML = "占位符"
            prevElement.style.color = "rgb(144, 147, 153)";
          }
          if (event.target.value ===null || event.target.value ==="" ||this.isRepeat(newArray)=== event.target.value){
            prevElement.style.color = "red";
          }else{
            prevElement.style.color = "rgb(144, 147, 153)";
          }
      }
      
    },
    getNouns(){
      this.listLoading = true;
      this.$api.reportMain.detail(this.reportId).then(res =>{
      if (res.code === 200){
          this.reportMain = res.data;
          this.nouns = this.reportMain.reportNouns;
          if (this.nouns.common!=null && this.nouns.common.length>0){
            this.commonNouns = this.arrChange(JSON.parse(JSON.stringify(this.nouns.common)));
          }
          
          if (typeof this.nouns.custom !='undefined' && this.nouns.custom !=null){
            this.customNouns = this.arrChange(JSON.parse(JSON.stringify(this.nouns.custom)))
          }else{
            this.nouns.custom = [];
          }
          
      }
      this.listLoading = false;
    }).catch(()=>{
      this.listLoading = false;
    })
    },
    deleteCard(card){
      this.nouns.custom = this.customNouns.reduce(function(a,b){
        return a.concat(b);
      })
      for(let n in this.nouns.custom){
          if (this.nouns.custom[n].placeholder === card.placeholder){
            this.nouns.custom.splice(this.nouns.custom.indexOf(this.nouns.custom[n]),1)
            this.reportMain.reportNouns.custom = this.nouns.custom;
            let arr = JSON.parse(JSON.stringify(this.reportMain.reportNouns.custom))
            this.customNouns = this.arrChange(JSON.parse(JSON.stringify(arr)))
            break;
          }
      }
    },
    isRepeat(arr){
      var hash = {};
      for(var i in arr) {
        if(hash[arr[i]])
          return arr[i];
          hash[arr[i]] = true;
        }
      return false;
    },
    openTableDialog(headlineName){
        this.dialogVisible =true;
        this.dialogName = headlineName;
        let sectionId = this.activeNode.sectionId;
        this.$api.reportSection.structure(sectionId).then(res=>{
          if (res.code === 200){
              this.inputData = res.data;
        }
      })
    },
    addTableData(tableData,tableHead){
      let item = new Object();
      for (let index in tableHead){
        Object.defineProperty(item,tableHead[index].prop,{value:null,enumerable:true,writable:true,configurable:true});
      }
      tableData.push(item)
    },
    cleanTableData(index){
      this.inputData.inputTableData[index].tableData = [];
    },
    saveTableData(){
      this.listLoading = true;
      this.inputData.action = this.activeNode.action;
      this.inputData.sectionId = this.activeNode.sectionId
      this.inputData.jsonString = JSON.stringify(this.inputData);
      console.log(this.activeNode)
      this.$api.reportSection.calculate(this.inputData).then(res =>{
        if (res.code === 200 && res.data!=null){
          this.activeNode.section = res.data;
          this.$notify({
              title: '成功',
              message: '计算结果已生成',
              type: 'success',
              duration: 2000
            })
            this.resetTableData();
            this.listLoading = false;
            this.dialogVisible = false;
        }else {
            this.$notify({
              title: '错误',
              message: '操作失败，请检查计算参数',
              type: 'error',
              duration: 2000
            })
            this.listLoading = false;
            this.dialogVisible = false;
          }
      }).catch(() => {
          this.listLoading = false;
        })
    },
    resetTableData(){
      this.inputData = {};
    },

  }
}
</script>
  
<style lang="css" scoped>
.report {
  width: 100%;
}

.section-area {
  height: 785px;
  width: 800px;
  overflow-y: scroll;
  border: 1px rgb(212, 212, 212) solid;
  border-radius: 4px;
}

.editor-area {
  height: 830px;
  width: 830px;
  overflow-y: scroll;
}

.null-area {
  height: 785px;
  width: 800px;
}

.generator {
  display: flex;
  position: absolute;
  top: -30px;
  right: 10px;
}

.loader {
  width: 100%;
  height: 10px;
  border-radius: 2px;
  background-color: rgba(252, 251, 251, 0.2);
  /* position: absolute; */

}

.loader::before {
  content: "";
  position: absolute;
  background-color: rgb(9, 188, 9);
  width: 0%;
  height: 10px;
  border-radius: 2px;
  animation: load 3.5s ease-in-out infinite;
  box-shadow: rgb(9, 188, 9) 0px 2px 29px 0px;
}


@keyframes load {
  50% {
    width: 100%;
  }

  100% {
    right: 0;
    left: unset;
  }
}

.nouns-area {
  width: 100%;

}
.nounsInfo{
  font-size: small;
  color: red;
  text-align: center;
}
.nounsInput{
  height: 32px; 
  width: 175px;
  border: rgb(220, 223, 230) 1px solid;
  border-radius: 3px;
  font-size: small;
  color: gray;
  padding-left: 5px;
}

.cardHover:hover{
	z-index: 2;
	box-shadow: 0 30px 30px rgba(0,0,0,.1);
  background-color: rgb(244,244,245)
 }
 .nouns-del {
  z-index: 2;
  float: right; 
  color: gray;
 }

 .nouns-del:hover{
  cursor: pointer;
 }

/*选中样式*/
::v-deep .el-tabs__item.is-active {
  font-weight: bold;
  /* background-color: #e1251b; */
}
.jisuan-icon{
  width: 25px;
  height: 25px;
  margin-bottom: -8px;
}

</style>