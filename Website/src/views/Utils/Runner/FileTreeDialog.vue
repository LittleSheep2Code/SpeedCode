<template>
  <div>
    <v-dialog v-model="display" @input="update_v_model" :width="width">
      <v-card>
        <v-card-title class="text-h6 secondary lighten-3">
          <v-icon>mdi-folder-open</v-icon> &nbsp; Open file
        </v-card-title>

        <v-card-text>
          <v-card class="mx-auto" max-width="580" style="margin-top: 20px">
            <v-sheet class="pa-4 secondary">
              <v-text-field v-model="toolbar.name" label="Search" flat solo-inverted dark hide-details clearable
                            v-if="!toolbar.new" clear-icon="mdi-close-circle-outline">
                <template v-slot:append-outer>
                  <v-icon v-if="toolbar.select && treeview.selected.length !== 0" @click="remove_file">mdi-trash-can</v-icon>
                </template>
              </v-text-field>

              <v-text-field v-model="toolbar.new_filename" label="Filename" flat dense solo-inverted dark hide-details clearable
                            v-if="toolbar.new" clear-icon="mdi-close-circle-outline">
                <template v-slot:append-outer>
                  <v-icon style="margin-right: 5px" @click="create_file(toolbar.new_filename)">mdi-file-plus</v-icon>
                  <v-icon v-if="toolbar.select && treeview.selected.length !== 0" @click="remove_file">mdi-trash-can</v-icon>
                </template>
              </v-text-field>

              <div style="margin-top: 4px"></div>

              <v-select v-model="toolbar.new_language" label="Language" flat dense return-object single-line hide-details solo-inverted
                        @change="toolbar.new_runtime = translator.go(toolbar.new_language.abbr)"
                        v-if="toolbar.new" :items="assets.languages" item-text="state" item-value="abbr" dark/>

              <div style="margin-top: 4px"></div>

              <v-select v-model="toolbar.new_runtime" label="Runtime" flat dense return-object single-line hide-details solo-inverted
                        v-if="toolbar.new" :items="assets.runtimes" item-text="state" item-value="abbr" dark/>

              <v-checkbox v-model="toolbar.select" dense dark hide-details label="Select"></v-checkbox>
              <v-checkbox v-model="toolbar.new" dense dark hide-details label="Create File"></v-checkbox>
            </v-sheet>

            <v-treeview @update:active="render_file" :active.sync="treeview.selected" :items="treeview.items" activatable
                        item-key="path" item-text="basename" :search="toolbar.name" return-object dense>
              <template v-slot:prepend="{ item, open }">
                    <v-icon v-if="item.type === 'dir'">{{ open ? 'mdi-folder-open-outline' : 'mdi-folder-outline' }}</v-icon>
                    <v-icon v-else>{{ assets.icons[item.extension.toLowerCase()] || assets.icons['other'] }}</v-icon>
                </template>
                <template v-slot:label="{ item }">{{ item.basename }}</template>
            </v-treeview>
          </v-card>
        </v-card-text>
      </v-card>
    </v-dialog>
  </div>
</template>

<script>
import Translator from "@/scripts/CodeRunner/RuntimeCodeTranslator";

export default {
  data: () => ({
    display: false,
    translator: Translator,
    toolbar: {
      name: "",
      select: false,
      new_filename: "",
      new_language: "",
      new_runtime: "",
      new: false
    },

    treeview: {
      items: [],
      selected: []
    },

    assets: {
      icons: {
        zip: "mdi-folder-zip-outline",
        rar: "mdi-folder-zip-outline",
        htm: "mdi-language-html5",
        html: "mdi-language-html5",
        js: "mdi-nodejs",
        json: "mdi-json",
        md: "mdi-markdown",
        pdf: "mdi-file-pdf",
        png: "mdi-file-image",
        jpg: "mdi-file-image",
        jpeg: "mdi-file-image",
        mp4: "mdi-filmstrip",
        mkv: "mdi-filmstrip",
        avi: "mdi-filmstrip",
        wmv: "mdi-filmstrip",
        mov: "mdi-filmstrip",
        txt: "mdi-file-document-outline",
        xls: "mdi-file-excel",
        other: "mdi-file-outline"
      },

      languages: Translator.Languages,
      runtimes: Translator.Runtimes
    }
  }),

  methods: {
    update_v_model($event) {
      this.$emit("input", $event)
    },

    create_file(name) {
      if(this.toolbar.new_language === "" || this.toolbar.new_runtime === "") return

      if(name == null) return
      if(name.replaceAll(" ", "") == "") return
      if(this.files.indexOf(name.toUpperCase()) !== -1) return

      if(this.toolbar.new_language.abbr != null)
        this.toolbar.new_language = this.toolbar.new_language.abbr

      if(this.toolbar.new_runtime.abbr != null)
        this.toolbar.new_runtime = this.toolbar.new_runtime.abbr

      this.$emit("create", {
        name: name.toUpperCase(),
        content: "",
        config: {
          language: this.toolbar.new_language,
          runtime: this.toolbar.new_runtime
        }
      })

      this.update_v_model(false)
    },

    render_file(index) {
      if(this.toolbar.select) return
      if(index.length === 0) return

      let item = index[0]
      if(item.type === "dir") return

      let content
      for(let file in this.files) {
        if(file.name === item.name)
          content = file.content
      }

      this.$emit("render", {
        name: item.name.toUpperCase(),
        content: content
      })

      this.update_v_model(false)
    },

    remove_file() {
      this.$dialog.warning({
        title: "Confirm?",
        text: "You really want delete this file?",
        showClose: false
      }).then(s => {
        if(s) this.$emit("remove", this.treeview.selected[0].name)
      })
    },

    init() {
      this.treeview.items = [];

      // set default files tree items (root item) in nextTick.
      // Otherwise this.open isn't cleared properly (due to syncing perhaps)
      setTimeout(() => {
        let reg = /\.[^\.]+$/

        // Load all files
        this.files.forEach(file => {
          this.treeview.items.push({
            type: "file",
            path: "/" + file.name,
            basename: file.name,
            extension: reg.exec(file.name)[0].substr(1, reg.exec(file.name)[0].length),
            name: file.name
          })
        })
      }, 0);
    },
  },

  props: {
    value: Boolean,
    width: String,
    files: Array
  },

  watch: {
    value() {
       this.display = this.value
    },

    files() {
      this.init()
    }
  },
}
</script>

<style>

</style>
