/*
* router 编程式路由 新开标签
* obj 路由参数 和router 参数一致
* */

import Vue from 'vue'
import { targetNewPage } from '@/utils/routerUtil'

Object.defineProperty(Vue.prototype, '$targetNewPage', {
  value: targetNewPage
})
