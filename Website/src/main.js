import Vue from 'vue'
import Axios from 'axios'
import App from './App.vue'
import router from './router'
import store from './store'
import i18n from './i18n'
import vuetify from './plugins/vuetify'

Axios.defaults.headers = {
  "Access-Control-Allow-Origin": "*",
  "Access-Control-Allow-Header": "*",
  "Access-Control-Allow-Methods": "OPTIONS, GET, POST, PUT, DELETE, FETCH",
  "Access-Control-Allow-Credentials": true
}

import VueCookies from 'vue-cookies'
Vue.use(VueCookies)

import VuetifyDialog from 'vuetify-dialog'
import 'vuetify-dialog/dist/vuetify-dialog.css'
Vue.use(VuetifyDialog, { context: { vuetify } })

Vue.prototype.axios = Axios
Vue.config.productionTip = false

new Vue({
  router,
  store,
  i18n,
  vuetify,
  render: h => h(App)
}).$mount('#app')
