<template>
  <v-container fill-height>
    <v-row justify="center" align="center">
      <v-col cols="3" class="text-right">
        <img alt="speedcode icon" src="../assets/Images/SpeedCode-v2-Icon.svg">
      </v-col>

      <v-col cols="5" class="text-left" style="font-size: 22px; font-family: 'Helvetica Neue', sans-serif">
        <span style="font-size: 32px"><b>{{ $t("application") }}</b></span> <br>
        <span>{{ $t("application-des.ls[0]") }}</span> <br>

        <v-btn text color="primary" elevation="2" style="margin-top: 6px">{{ $t("application-des.start") }} &nbsp; <v-icon>mdi-arrow-right</v-icon></v-btn>
      </v-col>
    </v-row>

    <Footers></Footers>
  </v-container>
</template>

<script>
import Footers from "@/components/Footers";

export default {
  name: "Index",
  components: { Footers }
}
</script>

<style scoped>

</style>
