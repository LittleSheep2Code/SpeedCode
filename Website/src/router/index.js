import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'Index',
    component: () => import("@/views/Index")
  },

  {
    path: '/update-logs',
    name: 'UpdateLogs',
    component: () => import("@/views/Announcement/UpdateLogs")
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
