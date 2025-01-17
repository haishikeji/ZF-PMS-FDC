<template>
    <div>
        <el-card class="box-card" shadow="never">
            <div>
                <el-card v-for="(t, index) in tricks" :key="index" class="chlid-box-card" shadow="hover">
                    <div slot="header" class="cardHeader">
                        段落模板：<el-tag effect="plain">{{ t.name }}</el-tag>
                    </div>
                    <p class="trick" v-if="removeHtmlTag(t.section) != null">{{ removeHtmlTag(t.section) }}
                        <el-button plain @click="reference(t.section)">引用</el-button><el-button plain
                            @click="prview(t.section)" type="primary">预览</el-button>
                    </p>
                </el-card>
            </div>
        </el-card>
        <el-dialog :visible.sync="dialogTableVisible" width="60%" center>
            <div class="dialog-bg">
                <div class="like-word" v-html="htmlStr">
                </div>
            </div>
        </el-dialog>
    </div>
</template>

<script>
export default {
    props: {
        tricks: {
            type: Array,
            required: false,
            default: []
        },
    },

    computed: {

    },
    data() {
        return {
            dialogTableVisible: false,
            htmlStr: null,
        }

    },
    methods: {
        prview(htmlStr) {
            this.dialogTableVisible = true
            this.htmlStr = htmlStr;
        },
        removeHtmlTag(htmlStr) {
            if (htmlStr) {
                let content = htmlStr.replace(/<[^>]+>/g, "");
                return content.length > 80 ? (content.slice(0, 80) + '...') : (content);
            }
            else {
                return null;
            }

        },
        reference(section) {
            this.$emit('reference',section)
        }
    }
}
</script>
<style scoped>
.box-card {
    width: 750px;
}
.cardHeader {
    height: 20px;
    color: gray;
}

.chlid-box-card {
    width: 710px;
    height: 200px;
    margin-bottom: 30px;
}

.chlid-box-card:hover {
    color: black;
    cursor: pointer;
    font-weight: bold;
}

p {
    text-indent: 2em;
    color: gray;
    letter-spacing: 2px;
    line-height: 1.5
}

.dialog-bg {
    width: 100%;
    height: 700px;
    position: relative;
    overflow: scroll;

}

.like-word {
    width: 90%;
    height: auto;
    position: absolute;
    top: 20px;
    left: 0;
    right: 0;
    margin: auto;
    -moz-box-shadow: 3px 3px 4px rgb(255, 228, 227);
    -webkit-box-shadow: 3px 3px 4px rgb(255, 228, 227);
    box-shadow: 0 0 20px rgb(175, 175, 175);
    padding: 20px;
}</style>