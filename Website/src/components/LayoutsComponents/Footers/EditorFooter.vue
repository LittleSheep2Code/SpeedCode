<template>
  <div>
    <v-footer padless fixed inset app class="justify-center pl-0" style="z-index: 100">
      <v-card flat tile width="100%" class="secondary lighten-3">
        <v-card-title>
          <v-tooltip right>
            <template v-slot:activator="{ on, attrs }">
              <v-btn icon v-bind="attrs" v-on="on" @click="dialogs.settings = true"><v-icon>mdi-cog</v-icon></v-btn>
            </template>
            <span>{{ $t("editor.Toolbar.settings") }}</span>
          </v-tooltip>

          <v-tooltip right>
            <template v-slot:activator="{ on, attrs }">
              <v-btn icon v-bind="attrs" v-on="on" @click="clear_code"><v-icon>mdi-backspace</v-icon></v-btn>
            </template>
            <span>{{ $t("editor.Toolbar.clear") }}</span>
          </v-tooltip>

          <v-spacer></v-spacer>

          <v-tooltip left>
            <template v-slot:activator="{ on, attrs }">
              <v-btn v-bind="attrs" v-on="on" @click="dialogs.application = true"
                     text>{{ u_runtime }} &nbsp; <v-icon color="green">mdi-play</v-icon></v-btn>
            </template>
            <span>{{ $t("editor.Toolbar.runtime") }}</span>
          </v-tooltip>
        </v-card-title>

        <v-card-text class="py-2 secondary elevation-2">
          <v-row>
            <v-col col="6" class="text-left">
              <div style="font-size: 16px">
                <v-tooltip left>
                  <template v-slot:activator="{ on, attrs }">
                    <div v-bind="attrs" v-on="on">
                      <span v-if="!save_state">{{ $t("editor.Toolbar.status.unsaved") }}</span>
                      <span v-else><b>{{ $t("editor.Toolbar.status.saved") }}</b></span>
                    </div>
                  </template>
                  <span>{{ $t("editor.Toolbar.save") }}</span>
                </v-tooltip>
              </div>
            </v-col>
          </v-row>
        </v-card-text>
      </v-card>
    </v-footer>

    <!--  Dialogs   -->
    <SettingsDialog v-model="dialogs.settings" width="600px" @complete="setting_change"></SettingsDialog>
    <RuntimeDialog v-model="dialogs.application" width="600px" @save="$emit('save')" ref="runtime"></RuntimeDialog>
  </div>
</template>

<script>
import SettingsDialog from "@/views/Utils/Runner/SettingsDialog";
import RuntimeDialog from "@/views/Utils/Runner/RuntimeDialog";
import i18n from "@/i18n"

export default {
  name: "EditorFooter",
  components: { RuntimeDialog, SettingsDialog },
  data: () => ({
    u_runtime: "CPP GPP 9",
    save_state: true,

    dialogs: {
      settings: false,
      application: false
    }
  }),

  created() {
    window.addEventListener('keydown', (event) => {
      if(event.ctrlKey && event.key === "s") {
        this.$emit("save")
      }
    });
  },

  methods: {
    clear_code() {
      this.$dialog.warning({
        title: i18n.t("editor.Toolbar.Clear.title"),
        text: i18n.t("editor.Toolbar.Clear.text"),
        showClose: false
      }).then(r => {
        if(r) {
          this.$cookies.remove("editor-code")
          this.$router.go(0)
        }
      })
    },

    setting_change($event) {
      this.configure_loader()
      this.$emit("reload", $event)
    },

    configure_loader() {
      let config = this.$cookies.get("editor-config")

      if(config != null) {
        this.u_runtime = config.runtime.replaceAll("_", " ")
      }
    },

    update_save_state(state) {
      this.save_state = state
    }
  },

  mounted() {
    this.configure_loader()
  }
}
</script>

<style scoped>

</style>
