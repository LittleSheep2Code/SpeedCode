<template>
  <v-footer padless inset app class="justify-center pl-0">
    <v-card flat tile width="100%" class="secondary lighten-3">
      <v-card-text>
        <v-btn text><v-icon>mdi-cog</v-icon> &nbsp; {{ $t("editors.toolbar.settings") }}</v-btn>
      </v-card-text>
    </v-card>
  </v-footer>
</template>

<script>
export default {
  name: "EditorFooter"
}
</script>

<style scoped>

</style>
