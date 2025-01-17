<template>
  <div>
    <y-detail-page-layout @save="handleCreate" :edit-status="editStatus">
      <el-form ref="immovableCertificateForm" :model="certificate" :rules="rules">
        <div class="createMajor-main-container">
          <div class="postInfo-container">
            <el-row class="row-style">
              <el-col :xs="24" :sm="12" :lg="6" :span="6">
                <el-form-item label="是否提供原件：" prop="haveOrigin" label-width="140px" class="postInfo-container-item">
                  <el-radio-group v-model="certificate.haveOrigin" size="small">
                    <el-radio border :label="false">否</el-radio>
                    <el-radio border :label="true">是</el-radio>
                  </el-radio-group>
                </el-form-item>
              </el-col>

              <el-col :xs="24" :sm="12" :lg="6" :span="6">
                <el-form-item label="权利人：" prop="ownerName" label-width="140px" class="postInfo-container-item"
                  :rules="{ required: true, message: '权利人不能为空', trigger: 'blur' }">
                  <el-input v-model="certificate.ownerName" class="filter-item" />
                </el-form-item>
              </el-col>
              <el-col :xs="24" :sm="12" :lg="6" :span="6">
                <el-form-item label="共有情况	：" prop="ownRight" label-width="140px" class="postInfo-container-item">
                  <el-input v-model="certificate.ownRight" class="filter-item" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row class="row-style">
              <el-col :xs="24" :sm="12" :lg="6" :span="6">
                <el-form-item label="委托人1：" prop="bailor1" label-width="140px" class="postInfo-container-item">
                  <el-input v-model="certificate.bailor1" class="filter-item" />
                </el-form-item>
              </el-col>
              <el-col :xs="24" :sm="12" :lg="6" :span="6">
                <el-form-item label="委托人1电话：" prop="bailor1Tel" label-width="140px" class="postInfo-container-item">
                  <el-input v-model="certificate.bailor1Tel" class="filter-item" />
                </el-form-item>
              </el-col>
              <el-col :xs="24" :sm="12" :lg="12" :span="6">
                <el-form-item label="委托人1身份证号码：" prop="bailor1IdNo" label-width="180px" class="postInfo-container-item">
                  <el-input v-model="certificate.bailor1IdNo" class="filter-item" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row class="row-style">
              <el-col :xs="24" :sm="12" :lg="6" :span="6">
                <el-form-item label="委托人2：" prop="bailor2" label-width="140px" class="postInfo-container-item">
                  <el-input v-model="certificate.bailor2" class="filter-item" />
                </el-form-item>
              </el-col>
              <el-col :xs="24" :sm="12" :lg="6" :span="6">
                <el-form-item label="委托人2电话：" prop="bailor2Tel" label-width="140px" class="postInfo-container-item">
                  <el-input v-model="certificate.bailor2Tel" class="filter-item" />
                </el-form-item>
              </el-col>
              <el-col :xs="24" :sm="12" :lg="12" :span="6">
                <el-form-item label="委托人2身份证号码：" prop="bailor2IdNo" label-width="180px" class="postInfo-container-item">
                  <el-input v-model="certificate.bailor2IdNo" class="filter-item" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row class="row-style">
              <el-col :xs="24" :sm="12" :lg="12" :span="6">
                <el-form-item label="坐落：" prop="location" label-width="140px" class="postInfo-container-item"
                  :rules="{ required: true, message: '坐落不能为空', trigger: 'blur' }">
                  <el-input v-model="certificate.location" class="filter-item" />
                </el-form-item>
              </el-col>
              <el-col :xs="24" :sm="12" :lg="12" :span="6">
                <el-form-item label="实勘地址：" prop="sceneAddress" label-width="140px" class="postInfo-container-item">
                  <el-input v-model="certificate.sceneAddress" class="filter-item" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row class="row-style">
              <el-col :xs="24" :sm="12" :lg="6" :span="6">
                <el-form-item label="地址是否一致：" prop="isSameAddress" label-width="140px" class="postInfo-container-item">
                  <el-radio-group v-model="certificate.isSameAddress" size="small">
                    <el-radio border :label="true">是</el-radio>
                    <el-radio border :label="false">否</el-radio>
                  </el-radio-group>
                </el-form-item>
              </el-col>
              <el-col :xs="24" :sm="12" :lg="6" :span="6">
                <el-form-item label="提供地址证明：" prop="addressProve" label-width="140px" class="postInfo-container-item">
                  <el-radio-group v-model="certificate.addressProve" size="small">
                    <el-radio border :label="true">是</el-radio>
                    <el-radio border :label="false">否</el-radio>
                  </el-radio-group>
                </el-form-item>
              </el-col>
              <el-col :xs="24" :sm="12" :lg="12" :span="6">
                <el-form-item label="《不动产权证书》记载土地使用权面积是否为共用宗土地面积：" prop="isShare" label-width="480px"
                  class="postInfo-container-item">
                  <el-radio-group v-model="certificate.isShare" size="small">
                    <el-radio border :label="true">是</el-radio>
                    <el-radio border :label="false">否</el-radio>
                  </el-radio-group>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row class="row-style">
              <el-col :xs="24" :sm="12" :lg="12" :span="6">
                <el-form-item label="不动产权证书号：" prop="immovableCertificateNo" label-width="160px"
                  class="postInfo-container-item" :rules="{ required: true, message: '不动产权证书号不能为空', trigger: 'blur' }">
                  <el-input v-model="certificate.immovableCertificateNo" class="filter-item" />
                </el-form-item>
              </el-col>
              <el-col :xs="24" :sm="12" :lg="6" :span="6">
                <el-form-item label="权利性质：" prop="rightNature" label-width="140px" class="postInfo-container-item">
                  <el-input v-model="certificate.rightNature" class="filter-item" />
                </el-form-item>
              </el-col>
              <el-col :xs="24" :sm="12" :lg="6" :span="6">
                <el-form-item label="用途：" prop="useTo" label-width="140px" class="postInfo-container-item"
                  :rules="{ required: true, message: '用途不能为空', trigger: 'blur' }">
                  <el-input v-model="certificate.useTo" class="filter-item" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row class="row-style">
              <el-col :xs="24" :sm="12" :lg="12" :span="6">
                <el-form-item label="不动产单元号：" prop="unitNo" label-width="140px" class="postInfo-container-item">
                  <el-input v-model="certificate.unitNo" class="filter-item" />
                </el-form-item>
              </el-col>
              <el-col :xs="24" :sm="12" :lg="12" :span="6">
                <el-form-item label="权利类型：" prop="rightType" label-width="140px" class="postInfo-container-item">
                  <el-input v-model="certificate.rightType" class="filter-item" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row class="row-style">
              <el-col :xs="24" :sm="12" :lg="12" :span="6">
                <el-form-item label="面积：" prop="acreage" label-width="140px" class="postInfo-container-item"
                  :rules="{ required: true, message: '面积不能为空', trigger: 'blur' }">
                  <el-input v-model="certificate.acreage" class="filter-item" />
                </el-form-item>
              </el-col>
              <el-col :xs="24" :sm="12" :lg="12" :span="6">
                <el-form-item label="使用期限：" prop="useLimit" label-width="140px" class="postInfo-container-item">
                  <el-input v-model="certificate.useLimit" class="filter-item" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row class="row-style">
              <el-col :xs="24" :sm="12" :lg="24" :span="6">
                <el-form-item label="权利其他状况：" prop="otherInfo" label-width="140px" class="postInfo-container-item">
                  <el-input type="textarea" v-model="certificate.otherInfo" class="filter-item" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row class="row-style">
              <el-col :xs="24" :sm="12" :lg="24" :span="6">
                <el-form-item label="附记：" prop="attachmentInfo" label-width="140px" class="postInfo-container-item">
                  <el-input type="textarea" v-model="certificate.attachmentInfo" class="filter-item" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row class="row-style">
              <el-col :xs="24" :sm="12" :lg="6" :span="6">
                <el-form-item label="是否设置抵押权：" prop="isPledge" label-width="140px" class="postInfo-container-item">
                  <el-select v-model="certificate.isPledge" placeholder="请选择" style="width:255px">
                    <el-option label="否" value="否">否</el-option>
                    <el-option label="向同一抵押权人申请续期贷款" value="向同一抵押权人申请续期贷款">向同一抵押权人申请续期贷款</el-option>
                    <el-option label="向新抵押权人申请贷款" value="向新抵押权人申请贷款">向新抵押权人申请贷款</el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :xs="24" :sm="12" :lg="6" :span="6"
                v-show="certificate.isPledge === '向同一抵押权人申请续期贷款' || certificate.isPledge === '向新抵押权人申请贷款'">
                <el-form-item label="抵押权人：" prop="pledgePerson" label-width="140px" class="postInfo-container-item">
                  <el-input v-model="certificate.pledgePerson" class="filter-item" />
                </el-form-item>
              </el-col>
              <el-col :xs="24" :sm="12" :lg="6" :span="6" v-show="certificate.isPledge === '向新抵押权人申请贷款'">
                <el-form-item label="新抵押权人：" prop="otherPledgePerson" label-width="140px" class="postInfo-container-item">
                  <el-input v-model="certificate.otherPledgePerson" class="filter-item" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row class="row-style">
              <el-col :xs="24" :sm="12" :lg="8" :span="6">
                <el-form-item label="已抵押担保的债券数额（万元）：" prop="ensureDebiAmount" label-width="280px"
                  class="postInfo-container-item">
                  <el-input v-model="certificate.ensureDebiAmount" class="filter-item" />
                </el-form-item>
              </el-col>
              <el-col :xs="24" :sm="12" :lg="8" :span="6">
                <el-form-item label="拖欠的建设工程价款（万元）	：" prop="defaultDebiAmount" label-width="250px"
                  class="postInfo-container-item">
                  <el-input v-model="certificate.defaultDebiAmount" class="filter-item" />
                </el-form-item>
              </el-col>
              <el-col :xs="24" :sm="12" :lg="8" :span="6">
                <el-form-item label="其他法定优先受偿款（万元）：" prop="otherDebiAmount" label-width="250px"
                  class="postInfo-container-item">
                  <el-input v-model="certificate.otherDebiAmount" class="filter-item" />
                </el-form-item>
              </el-col>
            </el-row>
            <!-- <el-row class="row-style">
              <el-col :xs="24" :sm="12" :lg="6" :span="6">
                <el-form-item label="农商行项目：" prop="isNoneShangCase" label-width="140px" class="postInfo-container-item">
                  <el-radio-group v-model="certificate.isNoneShangCase" size="small">
                    <el-radio border :label="false">否</el-radio>
                    <el-radio border :label="true">是</el-radio>
                  </el-radio-group>
                </el-form-item>
              </el-col>
              <el-col :xs="24" :sm="12" :lg="8" :span="6">
                <el-form-item label="农行西区支行项目：" prop="isNoneWestCase" label-width="160px" class="postInfo-container-item">
                  <el-radio-group v-model="certificate.isNoneWestCase" size="small">
                    <el-radio border :label="false">否</el-radio>
                    <el-radio border :label="true">是</el-radio>
                  </el-radio-group>
                </el-form-item>
              </el-col>
            </el-row> -->
          </div>
        </div>
      </el-form>
    </y-detail-page-layout>
  </div>
</template>

<script>

import YDetailPageLayout from '@/components/YDetailPageLayout/index_detail'
import { isNumber, phoneNumber, isIdNumber } from '@/utils/validate'

export default {
  name: "immovableCertificate",
  components: {
    YDetailPageLayout
  },
  props: {
    id: {
      type: Number,
      required: false,
      default: null
    },
    immovableCertificateObject: {
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
    immovableCertificateObject: function (value) {
      if (value) {
        this.certificate = value;
      }
    },

  },
  computed: {

  },
  created() {
    this.certificate.id = this.id;
    if (this.immovableCertificateObject) {
      this.certificate = this.immovableCertificateObject;
    }
  },
  data() {
    return {
      rules: {
        bailor1Tel: [{ validator: phoneNumber, trigger: 'blur' }],
        bailor2Tel: [{ validator: phoneNumber, trigger: 'blur' }],
        bailor1IdNo: [{ validator: isIdNumber, trigger: 'blur' }],
        bailor2IdNo: [{ validator: isIdNumber, trigger: 'blur' }],
        acreage: [{ required: true, message: '建筑面积不能为空', trigger: 'blur' }, { validator: isNumber, trigger: 'blur' }],
        ensureDebiAmount: [{ validator: isNumber, trigger: 'blur' }],
        defaultDebiAmount: [{ validator: isNumber, trigger: 'blur' }],
        otherDebiAmount: [{ validator: isNumber, trigger: 'blur' }]
      },
      certificate: {
        id: null,
        haveOrigin: false,
        immovableCertificateNo: null,
        ownerName: null,
        ownRight: null,
        bailor1: null,
        bailor1Tel: null,
        bailor1IdNo: null,
        bailor2: null,
        bailor2Tel: null,
        bailor2IdNo: null,
        location: null,
        sceneAddress: null,
        isSameAddress: true,
        addressProve: false,
        isShare: false,
        unitNo: null,
        rightType: null,
        rightNature: null,
        useTo: "",
        acreage: null,
        useLimit: null,
        otherInfo: null,
        attachmentInfo: null,
        isPledge: "否",
        pledgePerson: null,
        otherPledgePerson: null,
        ensureDebiAmount: 0,
        defaultDebiAmount: 0,
        otherDebiAmount: 0,
        isNoneShangCase: false,
        isNoneWestCase: false
      }
    }

  },
  methods: {
    handleCreate() {
      if (this.certificate.id) {
        this.$refs.immovableCertificateForm.validate(valid => {
          if (valid) {
            this.$api.personalTarget.updateImmovableCer(this.certificate).then(res => {
              if (res.code === 200 && res.data) {
                this.$notify({
                  title: '成功',
                  type: 'success',
                  message: '不动产权证信息保存成功',
                  duration: 2000
                });
                this.callParentEvent(true);
              } else {
                this.$notify({
                  title: '失败',
                  message: '不动产权证信息保存失败',
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
      if (this.immovableCertificateObject == null) {
          this.certificate.location = this.linkAttributes.location;
          this.certificate.sceneAddress = this.linkAttributes.location;
          this.certificate.bailor1 = this.linkAttributes.bailor1;
          this.certificate.bailor1Tel = this.linkAttributes.bailor1Tel;
          this.certificate.bailor2 = this.linkAttributes.bailor2;
          this.certificate.bailor2Tel = this.linkAttributes.bailor2Tel;
          this.certificate.ownerName = this.linkAttributes.ownerName;
          
        }
    },
    // 组件事件
    callParentEvent(data) {
      this.$emit('custom-event', data);
    }

  },
  
}
</script>
<style scoped lang="css">
.row-style {
  margin-left: 40px;
  margin-right: 1%;
}
</style>