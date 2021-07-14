<template>
  <v-dialog v-model="display" @input="update_v_model" @input.capture="update_data" :width="width">
    <v-card>
      <v-card-title class="text-h6 secondary lighten-3">
        <v-icon>mdi-cog</v-icon> &nbsp; {{ $t("editor.Toolbar.settings") }}
      </v-card-title>

      <v-card-text>
        <div style="margin-top: 30px">
          <h3 class="config-title">{{ $t("editor.Settings.editor") }}</h3>

         <div>
            <h4 class="config-subtitle">{{ $t("editor.Settings.Editor.language") }}</h4>
            <v-select dense outlined v-model="bconfig.language" :items="gconfig.data.available_language" return-object single-line
                      @change="config.settings.runtime = translator.go(config.settings.language.abbr)"
                      item-text="state" item-value="abbr" :disabled="this.bconfig.language == null"></v-select>
          </div>

          <div>
            <h4 class="config-subtitle">{{ $t("editor.Settings.Editor.theme") }}</h4>
            <v-select dense outlined v-model="gconfig.settings.theme" :items="gconfig.data.available_theme" return-object single-line
                      item-text="state" item-value="abbr"></v-select>
          </div>

          <div>
            <h4 class="config-subtitle">{{ $t("editor.Settings.Editor.tabsize") }}</h4>
            <v-text-field type="number" validate-on-blur :rules="[value => value >= 0 || 'Minimum is 0']" dense outlined v-model="gconfig.settings.tabsize"
                          @change="verify"></v-text-field>
          </div>
        </div>

        <div>
          <h3 class="config-title">{{ $t("editor.Settings.runtime") }}</h3>

          <div>
            <h4 class="config-subtitle">{{ $t("editor.Settings.Runtime.environment") }}</h4>
            <v-select dense outlined v-model="bconfig.runtime" :items="gconfig.data.available_runtime" return-object single-line
                      item-text="state" item-value="abbr" :disabled="this.bconfig.runtime == null"></v-select>
          </div>
        </div>

        <div>
          <h3 class="config-title">{{ $t("editor.Settings.autosave") }}</h3>

          <div>
            <h4 class="config-subtitle">{{ $t("editor.Settings.Autosave.delay") }}</h4>
            <v-text-field type="number" validate-on-blur :rules="[value => value >= 0 || 'Minimum is 0']" dense outlined v-model="gconfig.settings.autosave_delay"
                          @change="verify"></v-text-field>
          </div>
        </div>

        <div>
          <h3 class="config-subtitle">{{ $t("editor.Settings.reset") }}</h3>

          <div>
            <v-btn text block color="error" @click="reset_configure">{{ $t("editor.Settings.Reset.info") }} &nbsp; <v-icon>mdi-reload</v-icon></v-btn>
          </div>
        </div>
      </v-card-text>
    </v-card>
  </v-dialog>
</template>

<script>
import Translator from "@/scripts/CodeRunner/RuntimeCodeTranslator";

export default {
  name: "SettingsDialog",
  mounted() {
    this.configure_loader()
  },

  data: () => ({
    translator: Translator,
    display: false,
    gconfig: {
      settings: {
        theme: "idea",
        tabsize: 2,
        autosave_delay: 10000
      },

      data: {
        available_theme: [
          { state: 'Light Idea(Default)', abbr: 'idea' }
        ],

        available_language: Translator.Languages,
        available_runtime: Translator.Runtimes
      }
    },

    bconfig: {
      language: null,
      runtime: null
    }
  }),

  props: {
    value: Boolean,
    width: String,
    config: Object
  },

  watch: {
    value() {
      this.display = this.value
    },

    config() {
      this.bconfig = this.config
    }
  },

  methods: {
    configure_loader() {
      let config = this.$cookies.get("editor-config")

      if(config != null) {
        this.gconfig.settings = config
      }
    },

    verify() {
      if(this.config.settings.tabsize < 0)
        this.config.settings.tabsize = 0

      if(this.config.settings.autosave_delay < 0)
        this.config.settings.autosave_delay = 0
    },

    update_data() {
      if(this.bconfig.runtime.abbr != null)
        this.bconfig.runtime = this.config.runtime.abbr

      if(this.config.language.abbr != null)
        this.config.language = this.config.language.abbr

      if(this.config.settings.theme.abbr != null)
        this.config.settings.theme = this.config.settings.theme.abbr

      this.$cookies.set("editor-config", this.config.settings)
      this.$emit("complete", this.bconfig)
    },

    update_v_model($event) {
      this.$emit("input", $event)
    },

    reset_configure() {
      this.$dialog.warning({
        text: "You really wanna reset all config of editor?",
        title: "Confirm",
        showClose: false
      }).then(status => {
        if(status) {
          console.log("[SETTINGS] ALL CONFIGURE IS RESET!")
          this.update_v_model(false)
          this.config.settings = {
            language: "javascript",
            runtime: "NODE_JS",
            theme: "idea",
            tabsize: 2,
            autosave_delay: 10000
          }

          this.$cookies.remove("editor-config")
          this.$emit("complete")
        }
      })
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
</style>
