<template>
  <v-dialog v-model="display" @input="update_v_model" @input.capture="update_data" :width="width">
    <v-card>
      <v-card-title class="text-h6 secondary lighten-3">
        <v-icon>mdi-cog</v-icon> &nbsp; {{ $t("editors.toolbar.settings") }}
      </v-card-title>

      <v-card-text>
        <v-alert style="margin-top: 30px" text outlined color="deep-orange" icon="mdi-fire">This dialog is not be translate</v-alert>
        <div>
          <div>
            <h3 class="config-title">Editor</h3>

            <div>
              <h4 class="config-subtitle">Language</h4>
              <v-select dense outlined v-model="config.settings.language" :items="config.data.available_language" return-object single-line
                        @change="config.settings.runtime = translator.go(config.settings.language.abbr)"
                        item-text="state" item-value="abbr"></v-select>
            </div>

            <div>
              <h4 class="config-subtitle">Tab size</h4>
              <v-text-field type="number" validate-on-blur :rules="[value => value >= 0 || 'Minimum is 0']" dense outlined v-model="config.settings.tabsize"
                            @change="verify"></v-text-field>
            </div>
          </div>

          <div>
            <h3 class="config-title">Runtime</h3>

            <div>
              <h4 class="config-subtitle">Runtime Environment</h4>
              <v-select dense outlined v-model="config.settings.runtime" :items="config.data.available_runtime" return-object single-line
                        item-text="state" item-value="abbr"></v-select>
            </div>
          </div>

          <div>
            <h3 class="config-title">Automatic save</h3>

            <div>
              <h4 class="config-subtitle">Save delay(ms)</h4>
              <v-text-field type="number" validate-on-blur :rules="[value => value >= 0 || 'Minimum is 0']" dense outlined v-model="config.settings.autosave_delay"
                            @change="verify"></v-text-field>
            </div>
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
    config: {
      settings: {
        language: "javascript",
        runtime: "NODE_JS",
        tabsize: 4,
        autosave_delay: 10000
      },

      data: {
        available_language: [
          { state: 'Javascript', abbr: 'javascript' },
          { state: 'Python', abbr: 'x-python' },
          { state: 'C++', abbr: 'x-csrc' },
          { state: 'C', abbr: 'x-c++rc' },
          { state: 'C#', abbr: 'x-csharp' }
        ],

        available_runtime: [
          { state: 'NodeJS/Javascript', abbr: 'NODE_JS' },
          { state: 'Python 2', abbr: 'PYTHON_2' },
          { state: 'Python 3', abbr: 'PYTHON_3' },
          { state: 'C GCC 7', abbr: 'C_GCC_7' },
          { state: 'C GCC 8', abbr: 'C_GCC_8' },
          { state: 'C GCC 9', abbr: 'C_GCC_9' },
          { state: 'C Clang 7', abbr: 'C_CLANG_7' },
          { state: 'C++ G++ 7', abbr: 'CPP_GPP_7' },
          { state: 'C++ G++ 8', abbr: 'CPP_GPP_8' },
          { state: 'C++ G++ 9', abbr: 'CPP_GPP_9' },
          { state: 'C++ Clang 7', abbr: 'C_CLANG_7' },
          { state: 'C#', abbr: 'C_SHARP' },
        ]
      }
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

    configure_loader() {
      let config = this.$cookies.get("editor-config")

      if(config != null) {
        this.config.settings = config
      }
    },

    verify() {
      if(this.config.settings.tabsize < 0)
        this.config.settings.tabsize = 0

      if(this.config.settings.autosave_delay < 0)
        this.config.settings.autosave_delay = 0
    },

    update_data() {
      if(this.config.settings.runtime.abbr != null)
        this.config.settings.runtime = this.config.settings.runtime.abbr

      if(this.config.settings.language.abbr != null)
        this.config.settings.language = this.config.settings.language.abbr

      this.$cookies.set("editor-config", this.config.settings)
      this.$emit("complete")
    },

    update_v_model($event) {
      this.$emit("input", $event)
    }
  },
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
