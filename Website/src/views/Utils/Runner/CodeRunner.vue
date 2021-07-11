<template>
  <div class="fill-height" style="width: 100%">
    <div class="fill-height" style="width: 100%">
      <codemirror id="code-editor" @ready="register_editor_autocomplete" @changes="$refs['footer'].update_save_state(false)"
                  ref="editor" :options="editor_configs.configs" v-model="editor_configs.value" ></codemirror>
    </div>

    <EditorFooter ref="footer" @reload="configure_loader" @save="save_editor_code"></EditorFooter>
  </div>
</template>

<script>
import i18n from "@/i18n"
import EditorFooter from "@/components/LayoutsComponents/Footers/EditorFooter"

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
  components: { EditorFooter },

  data: () => ({
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
      if(this.autosave_config.last_save.content !== this.editor_configs.value) {

        console.log("[INFO]: AUTOSAVE!")

        this.autosave_config.last_save.content = this.editor_configs.value
        this.$cookies.set("editor-code", this.editor_configs.value)
        this.$refs["footer"].update_save_state(true)
      }
    },

    configure_loader() {
      let config = this.$cookies.get("editor-config")
      let code = this.$cookies.get("editor-code")

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

      if(code != null) {
        this.editor_configs.value = code
        this.autosave_config.last_save.content = code

        setTimeout(() => {
          this.$refs["footer"].update_save_state(true)
        }, 10)
      }

      this.$refs["editor"].refresh()
    }
  },

  mounted() {
    this.configure_loader()
  },

  beforeDestroy() {

    // Save the code
    this.save_editor_code()
  }
}
</script>

<style scoped>
#code-editor {
  height: 100%;
}
</style>
