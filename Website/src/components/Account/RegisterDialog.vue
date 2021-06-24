<template>
  <div>
    <v-card>
      <v-card-title class="text-h6 secondary lighten-3">
        <v-icon>mdi-account-plus</v-icon> &nbsp; {{ $t("user-manage.dialogs.title.register") }}
      </v-card-title>

      <v-card-text>
        <v-container>
          <v-stepper alt-labels v-model="step">
            <v-stepper-header>
              <v-stepper-step step="1" :complete="step > 1">{{ $t("user-manage.dialogs.steps.register[0]") }}</v-stepper-step>

              <v-divider></v-divider>
              <v-stepper-step step="2" :complete="step > 2">{{ $t("user-manage.dialogs.steps.register[1]") }}</v-stepper-step>

              <v-divider></v-divider>
              <v-stepper-step step="3" :complete="step > 3">{{ $t("user-manage.dialogs.steps.register[2]") }}</v-stepper-step>
            </v-stepper-header>

            <v-stepper-items>
              <v-stepper-content step="1">
                <v-form lazy-validation ref="from.Register" v-model="form.accept">
                  <v-row>
                    <v-col cols="12" sm="6">
                      <v-text-field :label="$t('user-manage.dialogs.inputs.register.username')" v-model="form.username" required
                                    :rules="[ v => !!v || $t('user-manage.dialogs.inputs.register.username-require') ]"></v-text-field>
                    </v-col>

                    <v-col cols="12" sm="6">
                      <v-text-field :label="$t('user-manage.dialogs.inputs.register.password')" type="password" v-model="form.password" required
                                    :rules="[ v => !!v || $t('user-manage.dialogs.inputs.register.password-require') ]"></v-text-field>
                    </v-col>

                    <v-col cols="12">
                      <v-text-field :label="$t('user-manage.dialogs.inputs.register.email')" v-model="form.email" required
                                    :rules="[ v => !!v || $t('user-manage.dialogs.inputs.register.email-require') ]"></v-text-field>
                    </v-col>

                    <v-col cols="12" class="text-center" style="padding-top: -5px">
                      <ReCAPTCHA ref="Register.reCAPTCHA" class="mx-auto"></ReCAPTCHA>
                    </v-col>
                  </v-row>
                </v-form>

                <v-divider style="margin-top: 16px; margin-bottom: 8px"></v-divider>
                <v-btn color="primary" text @click="commit_basic_data" :loading="form.wait">{{ $t("actions.next") }}</v-btn>
              </v-stepper-content>

              <v-stepper-content step="2">
                <v-row>
                  <v-col cols="12">
                    <v-text-field :label="$t('user-manage.dialogs.inputs.register.email-code')" v-model="form.email_code"
                                  :loading="form.wait"></v-text-field>
                  </v-col>

                  <v-col cols="12">
                    <v-btn color="primary" text @click="commit_email_code" :loading="form.wait">{{ $t("actions.next") }}</v-btn>
                  </v-col>
                </v-row>
              </v-stepper-content>

              <v-stepper-content step="3">
                <v-row>
                  <v-col cols="12" class="text-center"><div v-html="$t('user-manage.dialogs.res.register.completed')"></div></v-col>

                  <v-col cols="12" class="text-center">
                    <v-btn color="primary" text @click="step = 1" :loading="form.wait">{{ $t("actions.again") }}</v-btn>
                  </v-col>
                </v-row>
              </v-stepper-content>
            </v-stepper-items>
          </v-stepper>
        </v-container>
      </v-card-text>
    </v-card>
  </div>
</template>

<script>
import ReCAPTCHA from "@/components/reCAPTCHA";
import i18n from "@/i18n";

export default {
  name: "RegisterDialog",
  components: { ReCAPTCHA },

  data: () => ({
    "step": 1,
    "form": {
      "wait": false,
      "accept": false,

      "username": "",
      "password": "",
      "email": "",
      "email_code": ""
    }
  }),

  methods: {
    commit_basic_data() {
      if(this.$refs["Register.reCAPTCHA"].verify() && this.$refs["from.Register"].validate()) {

        this.form.wait = true

        let data = new FormData()
        data.set("username", this.form.username)
        data.set("password", this.form.password)
        data.set("email", this.form.email)

        this.axios.post("/s-code/account/sign-up", data).then(response => {

          if(response.data["reason_code"] != null && response.data["reason_code"].startsWith("REPEAT")) {

            if(response.data["reason_code"].endsWith("NAME")) {
              this.$dialog.notify.warning(i18n.t("user-manage.dialogs.res.register.repeat-username"), {
                position: "top-right",
                timeout: 3000
              })
            }

            if(response.data["reason_code"].endsWith("EMAIL")) {
              this.$dialog.notify.warning(i18n.t("user-manage.dialogs.res.register.repeat-email"), {
                position: "top-right",
                timeout: 3000
              })
            }
          }

          if(response.data["reason_code"] != null && response.data["reason_code"].startsWith("WRODAT")) {

            if(response.data["reason_code"].endsWith("NAME")) {
              this.$dialog.notify.warning(i18n.t("user-manage.dialogs.res.register.wrong-username"), {
                position: "top-right",
                timeout: 3000
              })
            }

            if(response.data["reason_code"].endsWith("EMAIL")) {
              this.$dialog.notify.warning(i18n.t("user-manage.dialogs.res.register.wrong-email"), {
                position: "top-right",
                timeout: 3000
              })
            }
          }

          if(response.data["status_code"] === "PASSED") {
            this.step++
          }

          this.form.wait = false
        })
      }
    },

    commit_email_code() {

      this.form.wait = true

      let data = new FormData()
      data.set("username", this.form.username)
      data.set("password", this.form.password)
      data.set("email", this.form.email)
      data.set("email_code", this.form.email_code)

      this.axios.post("/s-code/account/sign-up", data).then(response => {

        if(response.data["status_code"] === "WRODAT") {
          this.$dialog.notify.warning(i18n.t("user-manage.dialogs.res.register.wrong-email-code"), {
            position: "top-right",
            timeout: 3000
          })
        }

        if(response.data["status_code"] === "PASSED") {
          this.step++
        }

        this.form.wait = false
      }).catch(() => {
        this.form.wait = false
      })
    }
  }
}
</script>

<style scoped>

</style>
