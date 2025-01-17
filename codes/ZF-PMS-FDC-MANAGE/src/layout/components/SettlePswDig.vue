<template>
    <div>
        <el-dialog :visible.sync="isShowProp" width="30%" custom-class="customClass"
            :before-close="cleanPassword2">
            <el-card shadow="always">
                <div>
                    <el-input v-model="password2" type="password">
                        <template slot="prepend">请输入查询密码</template>
                        <el-button slot="append" @mouseover.native="hoverBtn" @mouseleave.native="loseBtn" :style=hoverStyle
                            @click="validPassword2">确认</el-button>
                    </el-input>
                </div>
            </el-card>
        </el-dialog>
    </div>
</template>

<script>
import eventBus from '../../utils/eventBus.js'
export default {
    name: "settlePswDig",
    props: {
        isShow: {
            type: Boolean,
            require: true,
            default: false
        },

    },
    data() {
        return {
            password2: null,
            hoverStyle: null,
            isShowProp:false,
            validCache:false
        }
    },
    watch:{
        isShow:{
            handler(newValue,oldValue){
                this.isShowProp = newValue
            },
            deep: true,
            immediate:true
        }
    },
    mounted(){
        eventBus.$on("openDig", res=>{
            if (!this.isShowProp){
                this.isShowProp = res
            }
        })
    },
    methods: {
        validPassword2() {
            const password2 = this.password2;
            this.$api.user.validPassword2(password2).then(res => {
                this.password2 = null;
                if (res.data === true) {
                    this.$parent.validSettlePsw()
                }
            })
        },
        cleanPassword2(done) {
            this.password2 = null;
            done();

        },
        hoverBtn() {
            this.hoverStyle = "background-color:rgb(64,158,255) ;color:white ;font-weight:bold";
        },
        loseBtn() {
            this.hoverStyle = null;
        },
    }
}
</script>

<style lang="scss" scoped>
/deep/.customClass{
    border-radius: 10px;
  }
</style>