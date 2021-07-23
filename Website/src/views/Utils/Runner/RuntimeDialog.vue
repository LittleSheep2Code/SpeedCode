<template>
  <div>
    <v-dialog v-model="display" @input="update_v_model" :width="width">
      <v-card>
        <v-card-title class="text-h6 secondary lighten-3">
          <v-icon>mdi-application-cog</v-icon> &nbsp; {{ $t("editor.Runtime.settings") }}
        </v-card-title>

        <v-card-text>
          <div style="margin-top: 30px">
            <div>
              <h4 class="config-subtitle">{{ $t("editor.Runtime.Settings.stdin") }}</h4>
              <v-textarea class="pre-textarea" auto-grow outlined dense clearable v-model="runtime_config.stdin"></v-textarea>
            </div>
          </div>

          <div class="text-left">
            <v-btn color="grey" @click="update_v_model(false)" text>{{ $t("normal.actions.cancel") }}</v-btn>
            <v-btn color="primary" @click="execute_script" :loading="wait" text>{{ $t("normal.actions.run") }}</v-btn>
          </div>
        </v-card-text>
      </v-card>
    </v-dialog>

    <v-dialog v-model="result" width="600px">
      <v-card>
        <v-card-title class="text-h6 success lighten-1">
          <v-icon>mdi-information</v-icon> &nbsp; {{ $t("editor.Runtime.result") }}
        </v-card-title>

        <v-card-text>
          <div style="margin-top: 25px">
            <div v-show="result_content['stdout']">
              <h4 class="result-title">{{ $t("editor.Runtime.Result.stdout") }}</h4>
              <v-textarea dense class="pre-textarea" :value="result_content['stdout']" auto-grow outlined readonly></v-textarea>
            </div>
            <div v-show="result_content['stderr']">
              <h4 class="result-title">{{ $t("editor.Runtime.Result.stderr") }}</h4>
              <v-textarea dense class="pre-textarea" value="result_content['stderr']" auto-grow outlined readonly></v-textarea>
            </div>
            <div v-show="result_content['compile_output']">
              <h4 class="result-title">{{ $t("editor.Runtime.Result.compile-error") }}</h4>
              <v-textarea dense class="pre-textarea" :value="result_content['compile_output']" auto-grow outlined readonly></v-textarea>
            </div>
            <div>
              <h4 class="result-title">{{ $t("editor.Runtime.Result.memory") }}</h4>
              <v-text-field dense class="pre-textarea" :value="result_content['memory']" outlined readonly></v-text-field>
            </div>
            <div>
              <h4 class="result-title">{{ $t("editor.Runtime.Result.time") }}</h4>
              <v-text-field dense class="pre-textarea" :value="result_content['time']" outlined readonly></v-text-field>
            </div>
          </div>
        </v-card-text>
      </v-card>
    </v-dialog>
  </div>
</template>

<script>
import i18n from "@/i18n";

export default {
  name: "RuntimeDialog",
  data: () => ({
    wait: false,
    display: false,
    result: false,
    result_content: false,
    runtime_config: {
      stdin: ""
    }
  }),

  created() {
    window.addEventListener('keydown', (event) => {
      if(event.key === "F9" && !this.wait) {
        this.wait = true
        this.$dialog.notify.info(i18n.t("editor.Runtime.running"), {
          position: "top-right",
          timeout: 3000
        })

        this.execute_script()
      }

      if(event.key === "F6" && this.result_content != null && this.config != null) {
        this.result = true
      }
    })
  },

  props: {
    value: Boolean,
    width: String,
    config: Object,
    content: String
  },

  watch: {
    value() {
      this.display = this.value
    }
  },

  methods: {
    execute_script() {

      // Create the from data
      let data = new FormData()

      // Code is null warning
      if(this.content == null) {
        this.$dialog.notify.warning(i18n.t("editor.Runtime.null-code"), {
          position: "top-right",
          timeout: 3000
        })

        return
      }

      // Default Runtime
      if(this.config["runtime"] == null)
        data.set("language", "CPP_GPP_9")

      // Set data to from data object
      data.set("source", this.content)
      data.set("language", this.config["runtime"])
      data.set("stdin", this.runtime_config.stdin)

      this.wait = true
      this.axios.post("/s-code/program-runner", data).then(response => {
        this.update_v_model(false)

        if(response.data["status_code"] !== "200") {
          this.$dialog.error({
            title: i18n.t("normal.status.failed"),
            text: `<code>${JSON.stringify(response.data)}</code>`,
            showClose: false,
            actions: null
          })
        }

        else {
          this.result_content = response.data['return']
          this.result = true
        }

        this.wait = false
      }).catch(() => {
        this.wait = false
        this.update_v_model(false)
      })
    },

    update_v_model($event) {
      this.$emit("input", $event)
    }
  }
}
</script>

<style scoped>
.config-title {
  font-size: 22px;
  margin: 8px 0;
}

.config-subtitle {
  font-size: 15px;
  font-weight: normal;
  margin-bottom: 2px;
}

.result-title {
  font-size: 15px;
  font-weight: bold;
  margin-bottom: 2px;
}

.pre-textarea {
  font-family: "CodeNewRoram", "Monospace", "Roboto", "Helvetica";
}
</style>
