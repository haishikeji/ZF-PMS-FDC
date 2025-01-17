import Vue from 'vue'

import api from '@/api'
Object.defineProperty(Vue.prototype, '$api', {
  value: api
})
