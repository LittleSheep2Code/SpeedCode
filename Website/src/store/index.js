import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    repeat_element_available: {
      reCAPTCHA: undefined
    }
  },

  mutations: {
    reCAPTCHA_commit(state, value) {
      state.repeat_element_available.reCAPTCHA = value
    }
  },

  actions: {
  },
  modules: {
  }
})
