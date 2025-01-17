<template>
  <div>
    <y-detail-page-layout @save="handleCreate" :edit-status="editStatus">
      <el-form ref="backgroundInfoForm" :model="backgroundInfo" :rules="rules">
        <div class="createMajor-main-container">
          <div class="postInfo-container">
            <el-divider content-position="left">地理位置</el-divider>
            <el-row class="row-style">
              <el-col :xs="24" :sm="12" :lg="12" :span="6">
                <el-form-item label="坐落：" prop="location" label-width="140px" class="postInfo-container-item" >
                  <el-autocomplete v-model="backgroundInfo.location" :fetch-suggestions="fetchSuggestions"  @select="handleSelect"
                  placeholder="请输入小区名称" style="width:100%" clearable>
                  </el-autocomplete>
                </el-form-item>
              </el-col>
              <el-col :xs="24" :sm="12" :lg="12" :span="6">
                <el-form-item label="区域：" prop="area" label-width="140px" class="postInfo-container-item">
                  <el-input v-model="backgroundInfo.area" class="filter-item" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row class="row-style">
              <el-col :xs="24" :sm="12" :lg="12" :span="6">
                <el-form-item label="四至：" prop="roundTouch" label-width="140px" class="postInfo-container-item">
                  <el-input v-model="backgroundInfo.roundTouch" class="filter-item" />
                </el-form-item>
              </el-col>
              <el-col :xs="24" :sm="12" :lg="12" :span="6">
                <el-form-item label="朝向：" prop="aspect" label-width="140px" class="postInfo-container-item">
                  <el-input v-model="backgroundInfo.aspect" class="filter-item" />
                </el-form-item>
              </el-col>
            </el-row>


            <el-divider content-position="left">交通状况</el-divider>
            <el-row class="row-style">
              <el-col :xs="24" :sm="12" :lg="12" :span="6">
                <el-form-item label="路网道路1：" prop="road1" label-width="140px" class="postInfo-container-item">
                  <el-input v-model="backgroundInfo.road1" class="filter-item" />
                </el-form-item>
              </el-col>
              <el-col :xs="24" :sm="12" :lg="12" :span="6">
                <el-form-item label="路网道路2：" prop="road2" label-width="140px" class="postInfo-container-item">
                  <el-input v-model="backgroundInfo.road2" class="filter-item" />
                </el-form-item>
              </el-col>
            </el-row>
              <el-row class="row-style">
              <el-col :xs="24" :sm="12" :lg="12" :span="6">
                <el-form-item label="路网道路3：" prop="road3" label-width="140px" class="postInfo-container-item">
                  <el-input v-model="backgroundInfo.road3" class="filter-item" />
                </el-form-item>
              </el-col>
              <el-col :xs="24" :sm="12" :lg="12" :span="6">
                <el-form-item label="通达程度：" prop="roadSmooth" label-width="140px" class="postInfo-container-item">
                  <el-input v-model="backgroundInfo.roadSmooth" class="filter-item" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row class="row-style">
              <el-col :xs="24" :sm="12" :lg="12" :span="6">
                <el-form-item label="公交线路：" prop="pubTraffic" label-width="140px" class="postInfo-container-item">
                  <el-input v-model="backgroundInfo.pubTraffic" class="filter-item" />
                </el-form-item>
              </el-col>
              <el-col :xs="24" :sm="12" :lg="12" :span="6">
                <el-form-item label="地铁：" prop="subway" label-width="140px" class="postInfo-container-item">
                  <el-input v-model="backgroundInfo.subway" class="filter-item" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row class="row-style">
              <el-col :xs="24" :sm="12" :lg="12" :span="6">
                <el-form-item label="公交便捷度：" prop="bgTraffic" label-width="140px" class="postInfo-container-item">
                  <el-select v-model="backgroundInfo.bgTraffic" placeholder="请选择" style="width:100%">
                    <el-option label="较好" value="较好"></el-option>
                    <el-option label="一般" value="一般"></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :xs="24" :sm="12" :lg="12" :span="6">
                <el-form-item label="交通限制：" prop="trafficLimit" label-width="140px" class="postInfo-container-item">
                  <el-input v-model="backgroundInfo.trafficLimit" class="filter-item" />
                </el-form-item>
              </el-col>
            </el-row>

            <el-divider content-position="left">公共配套</el-divider>
            <el-row class="row-style">
              <el-col :xs="24" :sm="12" :lg="12" :span="6">
                <el-form-item label="商业网点：" prop="businessPoint" label-width="140px" class="postInfo-container-item">
                  <el-input v-model="backgroundInfo.businessPoint" class="filter-item" />
                </el-form-item>
              </el-col>
              <el-col :xs="24" :sm="12" :lg="12" :span="6">
                <el-form-item label="金融网点：" prop="financePoint" label-width="140px" class="postInfo-container-item">
                  <el-input v-model="backgroundInfo.financePoint" class="filter-item" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row class="row-style">
              <el-col :xs="24" :sm="12" :lg="12" :span="6">
                <el-form-item label="教育设施：" prop="eduEquipment" label-width="140px" class="postInfo-container-item">
                  <el-input v-model="backgroundInfo.eduEquipment" class="filter-item" />
                </el-form-item>
              </el-col>
              <el-col :xs="24" :sm="12" :lg="12" :span="6">
                <el-form-item label="文体设施：" prop="cultureEquipment" label-width="140px" class="postInfo-container-item">
                  <el-input v-model="backgroundInfo.cultureEquipment" class="filter-item" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row class="row-style">
              <el-col :xs="24" :sm="12" :lg="12" :span="6">
                <el-form-item label="医疗卫生：" prop="medicalInfo" label-width="140px" class="postInfo-container-item">
                  <el-input v-model="backgroundInfo.medicalInfo" class="filter-item" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row class="row-style">
              <el-col :xs="24" :sm="12" :lg="12" :span="6">
                <el-form-item label="距区域商服中心距离：" prop="pathToBusinessCenter" label-width="180px"
                  class="postInfo-container-item">
                  <el-select v-model="backgroundInfo.pathToBusinessCenter" placeholder="请选择" style="width:100%">
                    <el-option label="较近" value="较近"></el-option>
                    <el-option label="较远" value="较远"></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :xs="24" :sm="12" :lg="12" :span="6">
                <el-form-item label="城市公共配套设施完善度：" prop="commonEquipment" label-width="210px"
                  class="postInfo-container-item">
                  <el-input v-model="backgroundInfo.commonEquipment" class="filter-item" />
                </el-form-item>
              </el-col>
            </el-row>

            <el-divider content-position="left">周边环境</el-divider>
            <el-row class="row-style">
              <el-col :xs="24" :sm="12" :lg="6" :span="6">
                <el-form-item label="周边小区：" prop="aroundCommunities" label-width="140px" class="postInfo-container-item">
                  <el-input v-model="backgroundInfo.aroundCommunities" class="filter-item" />
                </el-form-item>
              </el-col>
              <el-col :xs="24" :sm="12" :lg="6" :span="6">
                <el-form-item label="市政公园：" prop="cityParks" label-width="140px" class="postInfo-container-item">
                  <el-input v-model="backgroundInfo.cityParks" class="filter-item" />
                </el-form-item>
              </el-col>
              <el-col :xs="24" :sm="12" :lg="6" :span="6">
                <el-form-item label="不利因素：" prop="badFactors" label-width="140px" class="postInfo-container-item">
                  <el-input v-model="backgroundInfo.badFactors" class="filter-item" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row class="row-style">
              <el-col :xs="24" :sm="12" :lg="12" :span="6">
                <el-form-item label="环境质量：" prop="bgEnvir" label-width="140px" class="postInfo-container-item">
                  <el-input v-model="backgroundInfo.bgEnvir" class="filter-item" />
                </el-form-item>
              </el-col>
              <el-col :xs="24" :sm="12" :lg="12" :span="6">
                <el-form-item label="基础设施：" prop="bgBase" label-width="140px" class="postInfo-container-item">
                  <el-input v-model="backgroundInfo.bgBase" class="filter-item" />
                </el-form-item>
              </el-col>
            </el-row>
          </div>
        </div>
      </el-form>
    </y-detail-page-layout>
  </div>
</template>

<script>
import YDetailPageLayout from '@/components/YDetailPageLayout/index_detail'

export default {
  name: "backgroundInfo",
  components: {
    YDetailPageLayout
  },
  props: {
    id: {
      type: Number,
      required: false,
      default: null
    },
    backgroundInfoObject: {
      type: Object,
    },
    editStatus: {
      type: Boolean,
      default: false
    },
  },
  watch: {
    id: function (value) {
      this.backgroundInfo.id = value;
    },
    backgroundInfoObject: function (value) {
      if (value) {
        this.backgroundInfo = value;
      }
    },
  },

  computed: {

  },

  created() {
    this.backgroundInfo.id = this.id;
    if (this.backgroundInfoObject) {
      this.backgroundInfo = this.backgroundInfoObject;
    }
  },
  data() {
    return {
      rules: {
        location: [{ required: true, message: '坐落不能为空', trigger: 'blur' }],
        area: [{ required: true, message: '区域不能为空', trigger: 'blur' }],
        roundTouch: [{ required: true, message: '四至不能为空', trigger: 'blur' }],
        aspect: [{ required: true, message: '朝向不能为空', trigger: 'blur' }],
        road1: [{ required: true, message: '路网道路1不能为空', trigger: 'blur' }],
        road2: [{ required: true, message: '路网道路2不能为空', trigger: 'blur' }],
        road3: [{ required: true, message: '路网道路3不能为空', trigger: 'blur' }],
        roadSmooth: [{ required: true, message: '路网通达程度不能为空', trigger: 'blur' }],
        pubTraffic: [{ required: true, message: '公交线路不能为空', trigger: 'blur' }],
        trafficLimit: [{ required: true, message: '交通限制不能为空', trigger: 'blur' }],
        businessPoint: [{ required: true, message: '商业网点不能为空', trigger: 'blur' }],
        financePoint: [{ required: true, message: '金融网点不能为空', trigger: 'blur' }],
        eduEquipment: [{ required: true, message: '教育设施不能为空', trigger: 'blur' }],
        medicalInfo: [{ required: true, message: '医疗卫生不能为空', trigger: 'blur' }],
        aroundCommunities: [{ required: true, message: '周边小区不能为空', trigger: 'blur' }],
        badFactors: [{ required: true, message: '不利因素不能为空', trigger: 'blur' }],
        bgEnvir: [{ required: true, message: '环境质量不能为空', trigger: 'blur' }],
        bgBase: [{ required: true, message: '基础设施不能为空', trigger: 'blur' }],
      },
      backgroundInfo: {
        id: null,
        location: null,
        area: null,
        roundTouch: null,
        aspect: null,
        road1: null,
        road2: null,
        road3: null,
        roadSmooth: '区域主、次干道及支路分布较密集、道路通达程度较高',
        pubTraffic: null,
        subway: null,
        bgTraffic: '较好',
        trafficLimit: '无限制',
        businessPoint: null,
        financePoint: null,
        eduEquipment: null,
        cultureEquipment: null,
        medicalInfo: null,
        pathToBusinessCenter: '较近',
        commonEquipment: '完善',
        aroundCommunities: null,
        cityParks: null,
        badFactors: '无',
        bgEnvir: '所在区域废气、噪音污染不明显，街道较整洁，绿化率较高，整体环境较好',
        bgBase: '估价对象区域基础设施完善，已实现“六通”（通上下水、电、气、讯、道路），区域基础设施完善。'
      },
    }

  },
  methods: {
    handleCreate() {
      if (this.backgroundInfo.id) {
        this.$refs.backgroundInfoForm.validate(valid => {
          if (valid) {
            this.$api.personalTarget.updateBackgroundInfo(this.backgroundInfo).then(res => {
              if (res.code === 200 && res.data) {
                this.$notify({
                  title: '成功',
                  message: '区位状况保存成功',
                  type: 'success',
                  duration: 2000
                });
                this.callParentEvent(true);
              } else {
                this.$notify({
                  title: '失败',
                  message: '区位状况保存失败',
                  type: 'error',
                  duration: 2000
                });
              }
            })

          }
        })
      }
    },
    // 组件事件
    callParentEvent(data) {
      this.$emit('custom-event', data);
    },
    fetchSuggestions(queryString, callback){
      if (this.backgroundInfo.location){
        const location = this.backgroundInfo.location;
        var list = [{}]
        this.$api.personalTarget.fuzzyLocation(location).then(res=>{

          if (res.code === 200 && res.data.length>0){
            for (let i of res.data) {
              i.value = i.location;// 将想要展示的数据作为value
            }
            list = res.data;
            callback(list)

            }
        })
      }
    },
    handleSelect(item) {
        const id = this.backgroundInfo.id;
        this.backgroundInfo = item;
        this.backgroundInfo.id = id;
      }
  }
}
</script>
<style scoped lang="css">
.row-style {
  margin-left: 40px;
  margin-right: 1%;
}
</style>