<template>
  <div>
    <v-footer padless fixed inset app class="justify-center pl-0" style="z-index: 100">
      <v-card flat tile width="100%" class="secondary lighten-3">
        <v-card-title>
          <v-tooltip right>
            <template v-slot:activator="{ on, attrs }">
              <v-btn icon v-bind="attrs" v-on="on" @click="dialogs.settings = true"><v-icon>mdi-cog</v-icon></v-btn>
            </template>
            <span>{{ $t("editors.toolbar.settings") }}</span>
          </v-tooltip>

          <v-spacer></v-spacer>

          <v-tooltip left>
            <template v-slot:activator="{ on, attrs }">
              <v-btn v-bind="attrs" v-on="on" @click="$emit('execute')" @keydown.120="$emit('execute')"
                     text>{{ u_runtime }} &nbsp; <v-icon color="green">mdi-play</v-icon></v-btn>
            </template>
            <span>F9</span>
          </v-tooltip>
        </v-card-title>

        <v-card-text class="py-2 accent elevation-2">
          <v-row>
            <v-col col="6" class="text-left">
              <div style="font-size: 16px">
                <span v-if="!save_state">Unsaved</span>
                <span v-else><b>Saved</b></span>
              </div>
            </v-col>
          </v-row>
        </v-card-text>
      </v-card>
    </v-footer>

    <!--  Dialogs   -->
    <SettingsDialog v-model="dialogs.settings" width="600px" @complete="setting_change"></SettingsDialog>
  </div>
</template>

<script>
import SettingsDialog from "@/views/Utils/Runner/SettingsDialog";

export default {
  name: "EditorFooter",
  components: { SettingsDialog },
  data: () => ({
    u_runtime: undefined,
    save_state: true,

    dialogs: {
      settings: false
    }
  }),

  methods: {
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
