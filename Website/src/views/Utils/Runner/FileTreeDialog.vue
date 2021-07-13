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
              <v-text-field v-model="search.name" label="Search" flat solo-inverted dark hide-details clearable clear-icon="mdi-close-circle-outline">
                <template v-slot:append-outer>
                  <v-icon @click="create_file(search.name)">mdi-file-plus</v-icon>
                </template>
              </v-text-field>
              <v-checkbox v-model="search.caseSensitive" dark hide-details label="Case sensitive search"></v-checkbox>
            </v-sheet>

            <v-treeview :active.sync="treeview.selected" :items="treeview.items" :filter="filter" activatable></v-treeview>
          </v-card>
        </v-card-text>
      </v-card>
    </v-dialog>
  </div>
</template>

<script>
export default {
  data: () => ({
    display: false,
    search: {
      name: "",
      caseSensitive: false,
    },

    treeview: {
      items: [],
      selected: []
    }
  }),

  computed: {
    filter() {
      return this.search.caseSensitive
        ? (item, search, name) => item[name].indexOf(search) > -1
        : undefined
    }
  },

  methods: {
    update_v_model($event) {
      this.$emit("input", $event)
    },

    create_file(name) {
      if(name == null) return
      if(name.replaceAll(" ", "") == "") return
      if(this.files.indexOf(name.toUpperCase()) !== -1) return

      this.$emit("create", {
        name: name.toUpperCase(),
        content: ""
      })

      this.update_v_model(false)
    }
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

    files(value, old) {
      for(let i = 0; i < value.length; i++){
        this.treeview.items.push({
          id: i,
          name: value[i].name
        })
      }
    },

    "treeview.selected": function(value, old) {

    }
  }
}
</script>

<style>

</style>
