<template>
  <div style="height: 80px">
    <div class="in"><span style="text-align: center; font-size: 24px" ref="reCaptcha.Loading"><b>reCaptcha is loading...</b></span></div>
    <VueRecaptcha class="in" :sitekey="site_key" @verify="completed = true" @expired="completed = false" :loadRecaptchaScript="true"></VueRecaptcha>
  </div>
</template>
<script>
import VueRecaptcha from 'vue-recaptcha';
export default {
  name: "reCAPTCHA",

  components: { VueRecaptcha },

  data: () => {
    return {
      site_key: "6LcIKUsbAAAAAMcbvCf-qmEyw1H7nUK4Qmj8qChZ",
      completed: false
    };
  },

  methods: {
    submit(token) {
      this.axios.get("/reCaptcha/recaptcha/api/siteverify", {

        params: {
          secret: "6LcIKUsbAAAAAEyQ8lTWSAx06XnfJA3xEd0613T-",
          response: token
        }

      }).then(response => {
        this.completed = response.data.success
      });
    },

    verify() {
      return this.completed
    }
  }
};
</script>

<style>
.in {
  position: absolute;
}
</style>
