import { constantRoutes } from '@/router'
import urlMap from '../../router/urlMap'
import Layout from '@/layout'

/**
 * Filter asynchronous routing tables by recursion
 * @param routes asyncRoutes
 * @param roles
 */
function toHump(name) {
  return name.replace(/\_(\w)/g, function (all, letter) {
    return letter.toUpperCase()
  })
}

export function filterAsyncRoutes(menusData) {
  // console.info('原始数据', menusData)
  const res = [
    {
      path: '/auth',
      component: Layout,
      meta: {},
      children: []
    }
  ]

  // 排除不需要默认布局Layout路由菜单编码列表
  const menuCodeList = ['_views_workflow_Design']
  let pageMenuCode = null // 记录按钮所在的菜单编码，

  function tree(menusData) {
    menusData.forEach(item => {
      const tmp = { ...item }
      if (item.menuType === '菜单') {
        pageMenuCode = tmp.menuCode
      }
      if (tmp.children && tmp.children.length) {
        // console.log('递归')
        tree(tmp.children)
      }
      // if (!item.external && item.pcUrl && (item.menuType === '菜单' || (item.menuType === '按钮'))) {
      if (
        item.pcUrl &&
        (item.menuType === '菜单' || item.menuType === '按钮')
      ) {
        if (menuCodeList.indexOf(tmp.menuCode) > -1) {
          res.push({
            path: tmp.pcUrl,
            name: toHump(tmp.menuCode),
            component: urlMap[tmp.menuCode],
            meta: {
              pageMenuCode: pageMenuCode,
              title: tmp.name,
              icon: tmp.pcIcon,
              noCache: tmp.cachedViews,
              ...tmp
            }
          })
        } else {
          res[0].children.push({
            path: tmp.pcUrl,
            name: toHump(tmp.menuCode),
            component: urlMap[tmp.menuCode],
            meta: {
              pageMenuCode: pageMenuCode,
              title: tmp.name,
              icon: tmp.icon,
              noCache: tmp.cachedViews,
              ...tmp
            }
          })
        }
      }
    })
  }

  tree(menusData)
  return res
}

const state = {
  routes: [],
  addRoutes: [],
  menus: [],
  menusLevelList: []
}

const mutations = {
  SET_ROUTES: (state, routes) => {
    state.addRoutes = routes
    state.routes = constantRoutes.concat(routes)
  },
  SET_MENUS: (state, data) => {
    state.menus = data
  },
  SET_LELLIST: (state, data) => {
    state.menusLevelList = data
  }
}

const actions = {
  generateRoutes({ commit }, menusData) {
    return new Promise(resolve => {
      const accessedRoutes = filterAsyncRoutes(menusData)
      commit('SET_ROUTES', accessedRoutes)
      commit('SET_MENUS', menusData)
      resolve(accessedRoutes)
    })
  },
  menusLevelList({ commit }, list) {
    // console.info(list, 'action')
    return new Promise(resolve => {
      commit('SET_LELLIST', list)
      resolve(resolve)
    })
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
