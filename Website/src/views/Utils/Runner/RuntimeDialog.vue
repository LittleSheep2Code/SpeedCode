<template>
  <div>
    <v-dialog v-model="display" @input="update_v_model" :width="width">
      <v-card>
        <v-card-title class="text-h6 secondary lighten-3">
          <v-icon>mdi-application-cog</v-icon> &nbsp; Execute
        </v-card-title>

        <v-card-text>
          <v-alert style="margin-top: 30px" text outlined color="deep-orange" icon="mdi-fire">This dialog is not be translate</v-alert>
          <div>
            <div>
              <h4 class="config-subtitle">Stdin</h4>
              <v-textarea auto-grow outlined dense clearable v-model="runtime_config.stdin"></v-textarea>
            </div>
          </div>

          <div class="text-left">
            <v-btn color="grey" @click="update_v_model(false)" text>Cancel</v-btn>
            <v-btn color="primary" @click="execute_script" :loading="wait" text>Run</v-btn>
          </div>
        </v-card-text>
      </v-card>
    </v-dialog>

    <v-dialog v-model="result" width="600px">
      <v-card>
        <v-card-title class="text-h6 success lighten-1">
          <v-icon>mdi-information</v-icon> &nbsp; Result
        </v-card-title>

        <v-card-text>
          <div style="margin-top: 25px">
            <div v-show="result_content['stdout']">
              <h4 class="result-title">Stdout</h4>
              <v-textarea dense :value="result_content['stdout']" auto-grow outlined readonly></v-textarea>
            </div>
            <div v-show="result_content['stderr']">
              <h4 class="result-title">Stderr</h4>
              <v-textarea dense :value="result_content['stderr']" auto-grow outlined readonly></v-textarea>
            </div>
            <div v-show="result_content['compile_output']">
              <h4 class="result-title">Compile Error</h4>
              <v-textarea dense :value="result_content['compile_output']" auto-grow outlined readonly></v-textarea>
            </div>
            <div>
              <h4 class="result-title">Memory Usage</h4>
              <v-text-field dense :value="result_content['memory']" outlined readonly></v-text-field>
            </div>
            <div>
              <h4 class="result-title">Time Usage</h4>
              <v-text-field dense :value="result_content['time']" outlined readonly></v-text-field>
            </div>
          </div>
        </v-card-text>
      </v-card>
    </v-dialog>
  </div>
</template>

<script>
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
        this.$dialog.notify.info("Running...", {
          position: "top-left",
          timeout: 3000
        })

        this.$emit("save-require")
        this.execute_script()
      }

      if(event.key === "F6" && this.result_content != null) {
        this.result = true
      }
    })
  },

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
    execute_script() {

      let data = new FormData()

      if(this.$cookies.get("editor-code") == null) {
        this.$dialog.notify.warning("Please enter code first!", {
          position: "top-left",
          timeout: 3000
        })

        return
      }

      if(this.$cookies.get("editor-config")["runtime"] == null)
        data.set("language", "C_GCC_9")

      data.set("source", this.$cookies.get("editor-code"))
      data.set("language", this.$cookies.get("editor-config")["runtime"])

      if(this.stdin != null || this.stdin !== "") {
        data.set("stdin", this.stdin)
      }

      this.wait = true
      this.axios.post("/s-code/program-runner", data).then(response => {
        this.update_v_model(false)

        if(response.data["status_code"] !== "PASSED") {
          this.$dialog.error({
            title: "Failed to running code",
            text: `<code>${JSON.stringify(response.data)}</code>`,
            showClose: false,
            actions: null
          })
        }

        else {
          this.result_content = response.data['information']
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
</style>
