<template>
  <div class="fill-height" style="width: 100%">
    <v-tabs v-model="file_manifest.openindex">
      <div style="height: 48px; width: 50px; line-height: 48px" class="text-center secondary">
        <v-icon color="white" @click="dialogs.filetree = !dialogs.filetree">mdi-plus-box</v-icon>
      </div>

      <v-tooltip v-for="item in file_manifest.files" :key="item.name" bottom>
        <template v-slot:activator="{ on, attrs }">
          <v-tab :v-on="on" :v-bind="attrs">
            {{ item.name }} &nbsp;
            <v-icon small style="cursor: pointer"
                    @click="file_manifest.files = file_manifest.files.filter(i => i.name !== item.name)">mdi-close-circle</v-icon>
          </v-tab>
        </template>
      </v-tooltip>
    </v-tabs>

    <v-row class="fill-height" style="width: 100%" v-if="file_manifest.openfile == null" justify="center" align="center">
       <v-col cols="12" class="text-center">
         <v-icon color="grey" size="64">mdi-file-hidden</v-icon> <br>
         <span class="text-h6">No file was opened</span> <br>
         <div class="grey--text text--darken-3">
           <span>You can press the open file button at the top of the editor to select the file to open!</span>
         </div>
      </v-col>
     </v-row>

    <div class="fill-height" style="width: 100%" v-else>
      <codemirror id="code-editor" @ready="register_editor_autocomplete" @changes="$refs['footer'].update_save_state(false)"
                  ref="editor" :options="editor_configs.configs" v-model="editor_configs.value"></codemirror>
    </div>

    <FileTreeDialog v-model="dialogs.filetree" width="600px" :files="file_manifest.files" @create="create_file"></FileTreeDialog>
    <EditorFooter ref="footer" @reload="configure_loader" @save="save_editor_code"></EditorFooter>
  </div>
</template>

<script>
import EditorFooter from "@/components/LayoutsComponents/Footers/EditorFooter"
import FileTreeDialog from "@/views/Utils/Runner/FileTreeDialog"

import 'codemirror/mode/javascript/javascript.js'
import 'codemirror/mode/clike/clike.js'
import 'codemirror/mode/python/python.js'

import 'codemirror/theme/idea.css'

import 'codemirror/addon/selection/active-line.js'

import 'codemirror/addon/selection/mark-selection.js'
import 'codemirror/addon/search/searchcursor.js'

import 'codemirror/addon/hint/show-hint.js'
import 'codemirror/addon/hint/show-hint.css'
import 'codemirror/addon/hint/javascript-hint.js'
import 'codemirror/addon/selection/active-line.js'

import 'codemirror/addon/scroll/annotatescrollbar.js'
import 'codemirror/addon/search/matchesonscrollbar.js'
import 'codemirror/addon/search/searchcursor.js'
import 'codemirror/addon/search/match-highlighter.js'

import 'codemirror/mode/clike/clike.js'
import 'codemirror/addon/edit/matchbrackets.js'
import 'codemirror/addon/comment/comment.js'
import 'codemirror/addon/dialog/dialog.js'
import 'codemirror/addon/dialog/dialog.css'
import 'codemirror/addon/search/searchcursor.js'
import 'codemirror/addon/search/search.js'
import 'codemirror/keymap/sublime.js'

import 'codemirror/addon/fold/foldgutter.css'
import 'codemirror/addon/fold/brace-fold.js'
import 'codemirror/addon/fold/comment-fold.js'
import 'codemirror/addon/fold/foldcode.js'
import 'codemirror/addon/fold/foldgutter.js'
import 'codemirror/addon/fold/indent-fold.js'
import 'codemirror/addon/fold/markdown-fold.js'
import 'codemirror/addon/fold/xml-fold.js'

export default {
  name: "CodeRunner",
  components: { EditorFooter, FileTreeDialog },

  data: () => ({
    dialogs: {
      filetree: false
    },

    file_manifest: {
      files: [],

      openindex: undefined,
      openfile: null,
    },

    autosave_interval: undefined,
    editor_configs: {
      configs: {
        tabSize: 2,
        styleActiveLine: false,
        lineNumbers: true,
        styleSelectedText: false,
        line: true,
        foldGutter: true,
        gutters: ["CodeMirror-linenumbers", "CodeMirror-foldgutter"],
        highlightSelectionMatches: { showToken: /\w/, annotateScrollbar: true },
        mode: 'text/x-csrc',

        hintOptions:{
          completeSingle: false
        },

        keyMap: "sublime",
        matchBrackets: true,
        showCursorWhenSelecting: true,
        theme: "idea",
        extraKeys: { "Alt": "autocomplete" }
      },

      value: "",
      using_runtime: "C_GCC_9"
    },

    autosave_config: {
      last_save: {
        content: ""
      }
    }
  }),

  methods: {
    register_editor_autocomplete(entity) {
      entity.on('keypress', () => {
        entity.showHint( { completeSingle: false } )
      })
    },

    save_editor_code() {
      if(this.file_manifest.openfile != null && this.autosave_config.last_save.content !== this.editor_configs.value) {

        this.autosave_config.last_save.content = this.editor_configs.value
        this.file_manifest.files[this.file_manifest.openindex].content = this.editor_configs.value
        this.$cookies.set("editor-codes", JSON.stringify(this.file_manifest.files))
        this.$refs["footer"].update_save_state(true)
      }
    },

    configure_loader() {
      let config = this.$cookies.get("editor-config")
      let codes = JSON.parse(this.$cookies.get("editor-codes"))

      // Stop old autosave interval
      clearInterval(this.autosave_interval)

      if(config != null) {
        this.editor_configs.configs.tabSize = config.tabsize
        this.editor_configs.configs.mode = "text/" + config.language
        this.using_runtime = config.runtime

        // Setup new autosave interval
        this.autosave_interval = setInterval(this.save_editor_code, config.autosave_delay)
      }

      else {
        this.$cookies.set("editor-config", {
          language: "javascript",
            runtime: "NODE_JS",
            tabsize: 4,
            autosave_delay: 10000
        })

        // Doesn't config autosave setup
        this.autosave_interval = setInterval(this.save_editor_code, 10000)
      }

      if(codes != null) {
        this.file_manifest.files = codes

        setTimeout(() => {
          this.$refs["footer"].update_save_state(true)
        }, 10)
      }

      this.$refs["editor"].refresh()
    },

    create_file($event) {
      this.file_manifest.files.push($event)
      this.$forceUpdate()
    },
  },

  watch: {
    "file_manifest.openindex": function(value, old) {
      this.save_editor_code()

      // 所有标签已经关闭
      if(value === undefined) {
        this.file_manifest.openfile = null
        this.editor_configs.value = ""
      }

      else {
        this.file_manifest.openfile = this.file_manifest.files[value].name
        this.editor_configs.value = this.file_manifest.files[value].content
      }

      this.$refs["editor"].refresh()
    }
  },

  mounted() {
    this.configure_loader()
  },

  beforeDestroy() {
    this.save_editor_code()
  }
}
</script>

<style scoped>
#code-editor {
  height: 100%;
}
</style>
