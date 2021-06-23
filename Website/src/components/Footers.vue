<template>
  <v-footer padless inset app class="justify-center pl-0">
    <v-card flat tile width="100%" class="secondary lighten-3 text-center">
      <v-card-text>
        <v-btn class="mx-4" icon @click="$router.push('/update-logs')"><v-icon size="24px">mdi-update</v-icon></v-btn>
      </v-card-text>

      <v-divider></v-divider>

      <v-card-text>{{ new Date().getFullYear() }} &nbsp; © &nbsp; <strong>SmartSheep Studio</strong></v-card-text>
    </v-card>
  </v-footer>
</template>

<script>
export default {
  name: "Footers"
}
</script>

<style scoped>

</style>
