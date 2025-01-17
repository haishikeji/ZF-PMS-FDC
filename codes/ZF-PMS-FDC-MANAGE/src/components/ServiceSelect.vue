<template>
  <el-select
    v-bind="$attrs"
    v-on="$listeners"
    filterable
    :value="textValue"
    :loading="loading"
    :multiple="multiple"
    @change="handleChange"
    @visible-change="handleVisibleChange"
    @remove-tag="removeTag"
    @clear="afterClear"
  >
    <template v-if="pureList">
      <el-option
        v-for="(obj, index) in dataList"
        :key="index"
        :label="obj"
        :value="obj"
        @click.native="afterSelect(obj)"
      />
    </template>
    <template v-else>
      <el-option
        v-for="(obj, index) in dataList"
        :key="index"
        :label="obj[name]"
        :value="obj[field]"
        @click.native="afterSelect(obj)"
      />
    </template>
  </el-select>
</template>

<script>
  import ElementEmitter from 'element-ui/src/mixins/emitter'

  export default {
    name: 'ServiceSelect',
    provide: { elFormItem: false }, // 拦截elFormItem往下的注入，避免input等文本框错误的渲染
    mixins: [ElementEmitter],
    props: {
      // props
      /**
       * 组件值
       */
      value: {
        type: [String, Number, Array],
        default: ''
      },
      /**
       * 数据接口,必穿
       */
      dataService: {
        type: Function,
        required: true
      },
      /**
       * 默认查询
       */
      defaultQuery: {
        type: [Object, Number, String, Array],
        default: null
      },
      /**
       * label对应的属性key
       */
      name: {
        type: String,
        default: 'label'
      },
      /**
       * value对应的属性key（不传field时取name）
       */
      field: {
        type: String,
        default: 'value'
      },
      /**
       * true 每次打开下拉面板都请求数据  false 组件渲染时请求一次数据
       */
      immediate: {
        type: Boolean,
        default: true
      },
      /**
       * data数据格式 true 值数组  false 对象数组
       */
      pureList: {
        type: Boolean,
        default: false
      },
      /**
       * 多选
       */
      multiple: {
        type: Boolean,
        default: false
      },
      /**
       * 数据处理
       */
      dataFormat: {
        type: Function
      }
    },
    data () {
      return {
        loading: false,
        dataList: [],
        selection: []
      }
    },
    computed: {
      textValue () {
        return this.value || ''
      }
    },
    watch: {
      // 监听值变化触发表单校验
      value: {
        deep: true,
        immediate: true,
        handler (newVal) {
          this.dispatch('ElFormItem', 'el.form.change', [newVal])
        }
      }
    },
    created () {
      if (!this.immediate) {
        this.getData();
      }
    },
    methods: {
      handleChange (value) {
        this.$emit('input', value)
      },
      // 下拉框显示/隐藏
      handleVisibleChange (showFlag) {
        if (showFlag && this.immediate) {
          this.getData()
        }
      },
      // 获取数据
      getData () {
        this.loading = true;
        this.dataService(this.defaultQuery)
          .then(res => {
            if (res.code === 200) {
              let data = res.data;
              if (this.dataFormat) {
                data = this.dataFormat(data);
              }
              if (this.pureList) {
                this.dataList = data
              } else {
                // eslint-disable-next-line no-prototype-builtins
                const index = data.findIndex(obj => !obj.hasOwnProperty(this.field));
                if (index === -1) {
                  this.dataList = data
                } else {
                  this.$message({ type: 'warning', message: `数据中存在不含有${this.field}属性的对象！` })
                }
              }
              this.loading = false
            }
          })
      },
      afterSelect(option) {
        if (!this.multiple) {
          this.$emit('after-select', option);
        } else {
          this.$emit('after-select', option, this.value.includes(option[this.field]));
        }
      },
      afterClear() {
        this.$emit('after-select', null);
      },
      removeTag(val) {
        const option = this.dataList.find(item => val === item[this.field]);
        this.$emit('after-select', option, false);
      }
    }
  }
</script>
