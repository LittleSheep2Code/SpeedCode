<template>
  <div>
    <v-card>
      <v-card-title class="text-h6 secondary lighten-3">
        <v-icon>mdi-login</v-icon> &nbsp; {{ $t("user-manage.dialogs.title.login") }}
      </v-card-title>

      <v-card-text>
        <v-container>
          <v-form lazy-validation ref="from.Login" v-model="form.accept">
            <v-row>
              <v-col cols="12" sm="6">
                <v-text-field :label="$t('user-manage.dialogs.inputs.login.username')" v-model="form.username" required
                              :rules="[ v => !!v || $t('user-manage.dialogs.inputs.login.username-require') ]"></v-text-field>
              </v-col>

              <v-col cols="12" sm="6">
                <v-text-field :label="$t('user-manage.dialogs.inputs.login.password')" type="password" v-model="form.password" required
                              :rules="[ v => !!v || $t('user-manage.dialogs.inputs.login.password-require') ]"></v-text-field>
              </v-col>

              <v-col cols="12" class="text-center" style="padding-top: -5px">
                <ReCAPTCHA ref="Login.reCAPTCHA" class="mx-auto"></ReCAPTCHA>
              </v-col>
            </v-row>
          </v-form>
        </v-container>
      </v-card-text>

      <v-card-actions>
        <v-btn color="primary" text @click="commit_login" style="margin-left: 10px" :loading="form.wait">{{ $t("user-manage.dialogs.title.login") }}</v-btn>
      </v-card-actions>
    </v-card>
  </div>
</template>

<script>
import ReCAPTCHA from "@/components/reCAPTCHA";
import i18n from "@/i18n"

export default {
  name: "LoginDialog",
  components: { ReCAPTCHA },
  data: () => ({
    "form": {
      "wait": false,
      "accept": false,

      "username": "",
      "password": ""
    }
  }),

  methods: {
    commit_login() {
      if(this.$refs["Login.reCAPTCHA"].verify() && this.$refs["from.Login"].validate()) {

        this.form.wait = true

        let data = new FormData()
        data.set("username", this.form.username)
        data.set("password", this.form.password)

        this.axios.post("/s-code/account/sign-in", data).then(response => {
          if(response.status === 200 && response.data["status_code"] === "PASSED") {
            this.$dialog.notify.info(i18n.t("user-manage.dialogs.res.login.completed"), {
              position: "top-right",
              timeout: 3000
            })

            this.$cookies.set("access", response.data["information"])

            setTimeout(() => {
              this.$router.go(0)
            }, 3000)
          }

          else if(response.data["reason_code"] === "WRODAT") {
            this.$dialog.notify.warning(i18n.t("user-manage.dialogs.res.login.wrong-data"), {
              position: "top-right",
              timeout: 3000
            })
          }

          this.form.wait = false
        })

        this.form.wait = false
      }
    }
  }
}
</script>

<style scoped>

</style>
