<template>
    <div>
        <el-dialog  :visible.sync="xVisible" width="50%" center top="25vh" custom-class="doWarehouseClass" @closed="parentFalse()" >
            <el-card shadow="never" class="card">
                <div v-for="(d,id) in departments">
                    <el-divider content-position="left">{{d.name}}</el-divider>
                    <el-radio-group v-model="toUserId" size="medium">
                        <div v-for="(u,id) in d.users" class="xRadio">
                            <el-radio  border  :label="u.id" :disabled="userInfo.id===u.id" >{{ u.name }} </el-radio>
                        </div>
                    </el-radio-group>
                </div>
            </el-card>
            <div style="display: flex;">
                <el-button style="margin-left: 43%; margin-top: 10px;" type="info" @click="xVisible=false">取消</el-button>
                <el-button style="margin-top: 10px; float: right;" type="success" @click="transfer()">确认</el-button>
            </div>
        </el-dialog>
    </div>
</template>
  
  <script>
  import { mapGetters } from 'vuex'
  export default {
        name: 'TransferDialog',

        computed: {
            ...mapGetters([
            'userInfo'
            ]),

        },

        watch:{
            transferDialogVisible:function(newv){
                this.xVisible = newv;
                if (newv){
                    this.getDepartmentUsers();
                }
            },
        },
        data() {
            return {
                xVisible:false,
                toUserId:null,
                departments:[],
                toUser:null,
            };
        },
        props: {
            transferDialogVisible:{
                type: Boolean,
                require:false,
                default: false,
            },
            recordId:{
                type: Number,
                require: true
            }
        },

        mounted() {
           
        },

        methods: {
            parentFalse(){
               this.$emit('parentFalse')
            },
            transfer(){
                if (this.recordId && this.toUserId){
                    const transfer = new Object();
                    transfer.recordId = this.recordId;
                    transfer.toId = this.toUserId;
                    this.$api.taskRecordTransfer.add(transfer).then(res=>{
                        if (res.code === 200 && res.data){
                            this.$message.success("任务转交成功!");
                            this.xVisible = false;
                            this.$emit('getPage')
                        }
                    })
                    
                }
            },
            getDepartmentUsers(){
                this.$api.user.departUsers().then(res=>{
                    if (res.code===200){
                        this.departments = res.data;
                    }
                })
            }
        },
  
}
  
</script>

<style lang="scss" scoped>
    /deep/.doWarehouseClass {
        border-radius: 10px;
      }
      /deep/.el-radio{
        width: 90px;
      }

      .card{
        width: 100%;

      }
      .xRadio{
        margin-right: 5px;
        margin-bottom: 10px;
        float: left;
      }
</style>
