<template>
  <div>
    <y-detail-page-layout @save="handleCreate" :edit-status="editStatus">
      <el-form ref="landCertificateForm" :model="certificate" :rules="rules">
        <div class="createMajor-main-container">
          <div class="postInfo-container">
            <el-row class="row-style">
              <el-col :xs="24" :sm="12" :lg="8" :span="6">
                <el-form-item label="国有土地使用证类型：" prop="certificateType" label-width="180px"
                  class="postInfo-container-item" :rules="{ required: true, message: '使用证类型不能为空', trigger: 'blur' }">
                  <el-radio-group  v-model="certificate.certificateType" size="small">
                    <el-radio border label="分户">分户</el-radio>
                    <el-radio border label="大证">大证</el-radio>
                  </el-radio-group>
                </el-form-item>
              </el-col>
              <el-col :xs="24" :sm="12" :lg="8" :span="6">
                <el-form-item label="国有土地使用证编号：" prop="certificateNo" label-width="180px" class="postInfo-container-item">
                  <el-input v-model="certificate.certificateNo" class="filter-item" />
                </el-form-item>
              </el-col>
              <el-col :xs="24" :sm="12" :lg="8" :span="6">
                <el-form-item label="土地使用权人：" prop="userRightName" label-width="140px" class="postInfo-container-item">
                  <el-input v-model="certificate.userRightName" class="filter-item" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row class="row-style">
              <el-col :xs="24" :sm="12" :lg="24" :span="6">
                <el-form-item label="坐落：" prop="landLocation" label-width="180px" class="postInfo-container-item">
                  <el-input v-model="certificate.landLocation" class="filter-item" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row class="row-style">
              <el-col :xs="24" :sm="12" :lg="6" :span="6">
                <el-form-item label="地号：" prop="landNo" label-width="180px" class="postInfo-container-item">
                  <el-input v-model="certificate.landNo" class="filter-item" />
                </el-form-item>
              </el-col>
              <el-col :xs="24" :sm="12" :lg="6" :span="6">
                <el-form-item label="图号：" prop="imageNo" label-width="140px" class="postInfo-container-item">
                  <el-input v-model="certificate.imageNo" class="filter-item" />
                </el-form-item>
              </el-col>
              <el-col :xs="24" :sm="12" :lg="6" :span="6">
                <el-form-item label="地类(用途):" prop="landUseTo" label-width="140px" class="postInfo-container-item">
                  <el-input v-model="certificate.landUseTo" class="filter-item" />
                </el-form-item>
              </el-col>
              <el-col :xs="24" :sm="12" :lg="6" :span="6">
                <el-form-item label="取得价格：" prop="cost" label-width="120px" class="postInfo-container-item">
                  <el-input v-model="certificate.cost" class="filter-item" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row class="row-style">
              <el-col :xs="24" :sm="12" :lg="6" :span="6">
                <el-form-item label="使用权类型：" prop="userRightType" label-width="180px" class="postInfo-container-item">
                  <el-radio-group v-model="certificate.userRightType" size="small">
                    <el-radio border label="SELL">出让</el-radio>
                    <el-radio border label="ALLOT">划拨</el-radio>
                  </el-radio-group>
                </el-form-item>
              </el-col>
              <el-col :xs="24" :sm="12" :lg="6" :span="6">
                <el-form-item label="终止日期：" prop="limitDateD" label-width="140px" type="date" value-format="yyyy-MM-dd"
                  class="postInfo-container-item" :rules="{ required: true, message: '终止日期不能为空', trigger: 'blur' }">
                  <el-date-picker v-model="certificate.limitDateD" type="date" style="width: 100%;" placeholder="选择日期">
                  </el-date-picker>
                </el-form-item>
              </el-col>
              <el-col :xs="24" :sm="12" :lg="6" :span="6">
                <el-form-item label="使用权面积(㎡)：" prop="landAllAcreage" label-width="180px" class="postInfo-container-item">
                  <el-input v-model="certificate.landAllAcreage" class="filter-item" />
                </el-form-item>
              </el-col>
              <el-col :xs="24" :sm="12" :lg="6" :span="6">
                <el-form-item label="分摊面积(㎡)：" prop="landOutAcreage" label-width="140px" class="postInfo-container-item">
                  <el-input v-model="certificate.landOutAcreage" class="filter-item" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row class="row-style">
              <el-col :xs="24" :sm="12" :lg="6" :span="6">
                <el-form-item label="独用面积(㎡)：" prop="landInAcreage" label-width="180px" class="postInfo-container-item">
                  <el-input v-model="certificate.landInAcreage" class="filter-item" />
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
import { isNumber } from '@/utils/validate'
import moment from 'moment';

export default {
  name: "landCertificate",
  components: {
    YDetailPageLayout
  },
  props: {
    id: {
      type: Number,
      required: false,
      default: null
    },
    landCertificateObject: {
      type: Object,
    },
    linkAttributes: {
      type: Object,
      default: null
    },
    editStatus: {
      type: Boolean,
      default: false
    },
  },
  watch: {
    id: function (value) {
      this.certificate.id = value;
    },
    landCertificateObject: function (value) {
      if (value) {
        this.certificate = value;
      }
    },
  },
  created() {
    this.certificate.id = this.id;
    if (this.landCertificateObject) {
      this.certificate = this.landCertificateObject;
    }
  },
  computed: {

  },
  data() {
    return {
      rules: {
        cost: [{ validator: isNumber, trigger: 'blur' }],
        landAllAcreage: [{ validator: isNumber, trigger: 'blur' }],
        landOutAcreage: [{ validator: isNumber, trigger: 'blur' }],
        landInAcreage: [{ validator: isNumber, trigger: 'blur' }]
      },
      certificate: {
        id: null,
        certificateType: null,
        certificateNo: null,
        userRightName: null,
        landLocation: null,
        landNo: null,
        imageNo: null,
        landUseTo: "",
        cost: null,
        userRightType: null,
        landLimitDate: null,
        limitDateD: null,
        landAllAcreage: null,
        landOutAcreage: null,
        landInAcreage: null,
      }
    }

  },
  methods: {
    handleCreate() {
      if (this.certificate.id) {
        this.$refs.landCertificateForm.validate(valid => {
          if (valid) {
            this.certificate.landLimitDate = moment(this.certificate.limitDateD).format('YYYY年MM月DD日');
            this.$api.personalTarget.updateLandCer(this.certificate).then(res => {
              if (res.code === 200 && res.data) {
                this.$notify({
                  title: '成功',
                  message: '国土证信息保存成功',
                  type: 'success',
                  duration: 2000
                });
                this.callParentEvent(true);
              } else {
                this.$notify({
                  title: '失败',
                  message: '国土产权证信息保存失败',
                  type: 'error',
                  duration: 2000
                });
              }
            })

          }
        })
      }
    },
    refreshLinkAttributes(){
      if (this.landCertificateObject == null) {
          this.certificate.landLocation = this.linkAttributes.location;
        }
    },
    // 组件事件
    callParentEvent(data) {
      this.$emit('custom-event', data);
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