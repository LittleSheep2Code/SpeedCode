<template>
    <v-dialog v-model="display" @input="update_v_model" :width="width">
      <v-card>
        <v-card-title class="text-h6 secondary lighten-3">
          <v-icon>mdi-account-reactivate</v-icon> &nbsp; {{ $t("user-management.Activate.title") }}
        </v-card-title>

        <v-card-text>
          <v-container>
            <v-row>
              <v-col cols="12">
                <v-text-field :label="$t('user-management.Activate.inputs.activate')" v-model="form.activate">
                </v-text-field>
              </v-col>
            </v-row>
          </v-container>
        </v-card-text>

        <v-card-actions>
          <v-btn color="primary" text @click="commit_activate" style="margin-left: 10px" :loading="form.wait">{{ $t("user-management.Activate.title") }}</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
</template>

<script>
import i18n from "@/i18n";

export default {
  name: "ActivateDialog",
  data: () => ({
    display: false,
    form: {
      wait: false,

      activate: ""
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
    commit_activate() {

      if(this.form.activate == null) return

      this.form.wait = true

      let access = this.$cookies.get("access")

      let data = new FormData()
      data.set("source", this.form.activate)

      this.axios.post("/s-code/account/activate", data, { headers: { "access_token": access } }).then(response => {

        if(response.data["status_code"] === "400") {
          this.$dialog.notify.warning(i18n.t("user-management.Activate.messages.wrong-activate"), {
            position: "top-right",
            timeout: 3000
          })
        }

        if(response.data["status_code"] === "200") {
          this.$dialog.notify.info(i18n.t("user-management.Activate.messages.completed"), {
            position: "top-right",
            timeout: 3000
          })
        }

        this.form.wait = false
        this.$emit("input", false)
        this.$emit("complete")

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
