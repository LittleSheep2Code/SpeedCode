<template>
  <v-container fill-height>
    <v-row justify="center" align="center">
      <v-col cols="12" class="text-center">
        <span style="font-size: 32px"><b>{{ $t("application") }}</b></span> <br> <br>
        <img alt="speedcode icon" src="../assets/Images/SpeedCode-v2-Icon.svg" width="120" height="150"> <br> <br>
        <span>{{ $t("index.description.list[0]") }}</span> <br>

        <div style="margin-top: 20px">
          <v-btn text color="primary" style="margin-right: 12px" elevation="2" @click="$router.push('/utils/editor')">
            {{ $t("index.description.start") }} &nbsp; <v-icon>mdi-arrow-right</v-icon>
          </v-btn>

          <v-btn text color="primary" style="margin-right: 12px" elevation="2" @click="get_sponsor">
            {{ $t("index.description.sponsor") }} &nbsp; <v-icon>mdi-launch</v-icon>
          </v-btn>

          <v-btn text color="primary" style="margin-right: 12px" elevation="2" @click="$router.push('/update-logs')">
            {{ $t("index.description.new") }} &nbsp; <v-icon>mdi-arrow-right</v-icon>
          </v-btn>
        </div>
      </v-col>
    </v-row>

    <Footers></Footers>
  </v-container>
</template>

<script>
import Footers from "@/components/LayoutsComponents/Footers/Footers";

export default {
  name: "Index",
  components: { Footers },
  methods: {

    get_sponsor() {
      window.location.replace('https://afdian.net/@LittleSheep')
    }
  }
}
</script>

<style scoped>

</style>
