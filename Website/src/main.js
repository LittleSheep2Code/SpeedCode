import Vue from "vue"
import Axios from "axios"
import App from "@/App.vue"
import router from "@/router"
import store from "@/store"
import i18n from "@/i18n"
import vuetify from "@/plugins/vuetify"

Axios.defaults.headers = {
  "Access-Control-Allow-Origin": "*",
  "Access-Control-Allow-Header": "*",
  "Access-Control-Allow-Methods": "OPTIONS, GET, POST, PUT, DELETE, FETCH",
  "Access-Control-Allow-Credentials": true
}

import VueCookies from "vue-cookies"
Vue.use(VueCookies)

import VuetifyDialog from "vuetify-dialog"
import "vuetify-dialog/dist/vuetify-dialog.css"
Vue.use(VuetifyDialog, { context: { vuetify } })

import VueCodemirror from "vue-codemirror";
import "@/styles/codemirror.css"
Vue.use(VueCodemirror)

Axios.interceptors.response.use((response) => {
  return response
}, (error) => {

  // Process auth error
  if(error.response.status === 400) {
    if(error.response.data["status_code"] === "CONREF" || error.response.data["reason_code"] === "UNDFID") {
      Vue.prototype.$dialog.warning({
        title: i18n.t("normal.axios.auth-error-title"),
        text: i18n.t("normal.axios.auth-error-text"),
        showClose: false,
        actions: null
      })

      Vue.prototype.$cookies.remove("access")
    }
  }

  Vue.prototype.$dialog.error({
    title: i18n.t("normal.axios.error-title"),
    text: i18n.t("normal.axios.error-text") + JSON.stringify(error.message),
    showClose: false,
    actions: null
  })

  return Promise.reject(error)
})

Vue.prototype.axios = Axios
Vue.config.productionTip = false

new Vue({
  router,
  store,
  i18n,
  vuetify,
  render: h => h(App)
}).$mount("#app")
