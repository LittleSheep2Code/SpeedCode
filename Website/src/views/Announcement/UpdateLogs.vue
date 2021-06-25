<template>
  <v-container fill-height>
    <v-row justify="center" align="center">
      <v-col cols="8">
        <h2><b>{{ $t("updatelogs.notfound") }}</b></h2>
      </v-col>
    </v-row>

    <Footers></Footers>
  </v-container>
</template>

<script>
import Footers from "@/components/LayoutsComponents/Footers/Footers";
export default {
  name: "UpdateLogs",
  components: { Footers },
  data: () => {

  }
}
</script>

<style scoped>

</style>
