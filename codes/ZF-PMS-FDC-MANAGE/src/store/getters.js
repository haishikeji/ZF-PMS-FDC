const getters = {
  visitedViews: state => state.tagsView.visitedViews,
  cachedViews: state => state.tagsView.cachedViews,
  menus: state => state.user.menus,
  token: state => state.user.token,
  avatar: state => state.user.avatar,
  name: state => state.user.name,
  userInfo: state => state.user.userInfo,
  permission_routes: state => state.permission.routes,
  permission_menus: state => state.permission.menus || [],
  permission_menusLevelList: state => state.permission.menusLevelList || [],
}
export default getters
