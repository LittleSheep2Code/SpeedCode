import Vue from 'vue';
import Vuetify from 'vuetify/lib/framework';

Vue.use(Vuetify);

export default new Vuetify({
  theme: {
    themes: {
      "light": {
        primary: "#dea808",
        secondary: "#ff9800",
        accent: "#ff5722",
        error: "#f44336",
        warning: "#ff6f00",
        info: "#8bc34a",
        success: "#4caf50"
      }
    }
  }

});
