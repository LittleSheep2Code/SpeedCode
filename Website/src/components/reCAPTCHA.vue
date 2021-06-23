<template>
  <div style="height: 80px">
    <div class="in"><span style="text-align: center; font-size: 24px" ref="reCaptcha.Loading"><b>reCaptcha is loading...</b></span></div>
    <div class="in" id="grecaptcha" ref="grecaptcha"></div>
  </div>
</template>
<script>
export default {
  name: "reCAPTCHA",

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

    loaded() {
      setTimeout(() => {

        if(this.$store.state.repeat_element_available.reCAPTCHA == null) {
          window.grecaptcha.render("grecaptcha", {
            sitekey: this.site_key,
            callback: this.submit
          })
        }

        else {
          console.log("[INFO]: Found a available reCaptcha dom, use it.")
          this.$refs.grecaptcha.innerHTML = this.$store.state.repeat_element_available.reCAPTCHA
          this.$forceUpdate()
        }

      }, 200);

      setTimeout(() => {

        if(this.$store.state.repeat_element_available.reCAPTCHA == null) {
          this.$store.commit("reCAPTCHA_commit", document.querySelector("#grecaptcha").innerHTML)
        }
      }, 201)
    },

    verify() {
      return this.completed
    }
  },

  mounted() {
    this.loaded()
  }
};
</script>

<style>
.in {
  position: absolute;
}
</style>
