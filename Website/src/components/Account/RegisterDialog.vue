<template>
  <v-dialog v-model="display" @input="update_v_model" :width="width">
    <v-card>
      <v-card-title class="text-h6 secondary lighten-3">
        <v-icon>mdi-account-plus</v-icon> &nbsp; {{ $t("user-management.Register.title") }}
      </v-card-title>

      <v-card-text>
        <v-container>
          <v-stepper alt-labels v-model="step">
            <v-stepper-header>
              <v-stepper-step step="1" :complete="step > 1">{{ $t("user-management.Register.steps[0]") }}</v-stepper-step>

              <v-divider></v-divider>
              <v-stepper-step step="2" :complete="step > 2">{{ $t("user-management.Register.steps[1]") }}</v-stepper-step>

              <v-divider></v-divider>
              <v-stepper-step step="3" :complete="step > 3">{{ $t("user-management.Register.steps[2]") }}</v-stepper-step>
            </v-stepper-header>

            <v-stepper-items>
              <v-stepper-content step="1">
                <v-form lazy-validation ref="from.Register" v-model="form.accept">
                  <v-row>
                    <v-col cols="12" sm="6">
                      <v-text-field :label="$t('user-management.Register.inputs.username')" v-model="form.username" required
                                    :rules="[ v => !!v || $t('user-management.Register.inputs-rule.username') ]"></v-text-field>
                    </v-col>

                    <v-col cols="12" sm="6">
                      <v-text-field :label="$t('user-management.Register.inputs.password')" type="password" v-model="form.password" required
                                    :rules="[ v => !!v || $t('user-management.Register.inputs-rule.password') ]"></v-text-field>
                    </v-col>

                    <v-col cols="12">
                      <v-text-field :label="$t('user-management.Register.inputs.email')" v-model="form.email" required
                                    :rules="[ v => !!v || $t('user-management.Register.inputs-rule.email') ]"></v-text-field>
                    </v-col>

                    <v-col cols="12" class="text-center" style="padding-top: -5px">
                      <ReCAPTCHA ref="Register.reCAPTCHA" class="mx-auto"></ReCAPTCHA>
                    </v-col>
                  </v-row>
                </v-form>

                <v-divider style="margin-top: 16px; margin-bottom: 8px"></v-divider>
                <v-btn color="primary" text @click="commit_basic_data" :loading="form.wait">{{ $t("normal.actions.next") }}</v-btn>
              </v-stepper-content>

              <v-stepper-content step="2">
                <v-row>
                  <v-col cols="12">
                    <v-text-field :label="$t('user-management.Register.inputs.email-code')" v-model="form.email_code"></v-text-field>
                  </v-col>

                  <v-col cols="12">
                    <v-btn color="primary" text @click="commit_email_code" :loading="form.wait">{{ $t("normal.actions.next") }}</v-btn>
                  </v-col>
                </v-row>
              </v-stepper-content>

              <v-stepper-content step="3">
                <v-row>
                  <v-col cols="12" class="text-center"><div v-html="$t('user-manage.dialogs.res.Register.completed')"></div></v-col>

                  <v-col cols="12" class="text-center">
                    <v-btn color="primary" text @click="step = 1" :loading="form.wait">{{ $t("normal.actions.again") }}</v-btn>
                  </v-col>
                </v-row>
              </v-stepper-content>
            </v-stepper-items>
          </v-stepper>
        </v-container>
      </v-card-text>
    </v-card>
  </v-dialog>
</template>

<script>
import ReCAPTCHA from "@/components/reCAPTCHA";
import i18n from "@/i18n";

export default {
  name: "RegisterDialog",
  components: { ReCAPTCHA },

  data: () => ({
    step: 1,
    display: false,
    form: {
      wait: false,
      accept: false,

      username: "",
      password: "",
      email: "",
      email_code: ""
    }
  }),

  props: {
    value: Boolean,
    width: String
  },

  watch: {
    value() {
      this.display = this.value
    }
  },

  methods: {
    commit_basic_data() {
      if(this.$refs["Register.reCAPTCHA"].verify() && this.$refs["from.Register"].validate()) {

        this.form.wait = true

        let data = new FormData()
        data.set("username", this.form.username)
        data.set("password", this.form.password)
        data.set("email", this.form.email)

        this.axios.post("/s-code/account/sign-up", data).then(response => {

          if(response.data["status_code"] === "403") {
            this.$dialog.notify.warning(i18n.t("user-management.Register.messages.repeat-username"), {
              position: "top-right",
              timeout: 3000
            })
          }

          if(response.data["status_code"] === "410") {
            this.$dialog.notify.warning(i18n.t("user-management.Register.messages.repeat-email"), {
              position: "top-right",
              timeout: 3000
            })
          }

          if(response.data["status_code"] === "400") {
            if(response.data["ultra_code"] === "iu") {
              this.$dialog.notify.warning(i18n.t("user-management.Register.messages.wrong-username"), {
                position: "top-right",
                timeout: 3000
              })
            }

            if(response.data["ultra_code"] === "ie") {
              this.$dialog.notify.warning(i18n.t("user-management.Register.messages.wrong-email"), {
                position: "top-right",
                timeout: 3000
              })
            }
          }

          if(response.data["status_code"] === "200") {
            this.step++
          }

          this.form.wait = false
        })
      }
    },

    commit_email_code() {

      if(this.form.email_code == null) return

      this.form.wait = true

      let data = new FormData()
      data.set("username", this.form.username)
      data.set("password", this.form.password)
      data.set("email", this.form.email)
      data.set("email_code", this.form.email_code)

      this.axios.post("/s-code/account/sign-up", data).then(response => {

        if(response.data["status_code"] === "400") {
          this.$dialog.notify.warning(i18n.t("user-management.Register.messages.wrong-email-code"), {
            position: "top-right",
            timeout: 3000
          })
        }

        if(response.data["status_code"] === "200") {
          this.step++
        }

        this.form.wait = false
      }).catch(() => {
        this.form.wait = false
      })
    },

    update_v_model($event) {
      this.$emit("input", $event)
    }
  }
}
</script>

<style scoped>

</style>
