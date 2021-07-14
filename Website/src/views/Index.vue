<template>
  <v-container fill-height>
    <v-row justify="center" align="center">
      <v-col cols="12" class="text-center" style="padding-top: 40px">
        <span style="font-size: 32px"><b>{{ $t("application") }}</b></span> <br>
        <img alt="speedcode icon" src="../assets/Images/SpeedCode-Pixel-1000p-Icon.png" width="200" height="200"> <br>
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

        <v-col cols="12" class="text-center">
          <div style="margin-top: 160px">
            <span class="text-h6">{{ $t("index.description.list[1]") }}</span> <br>
            <div style="font-size: 14px" class="grey--text text--darken-3">
              <span v-html="$t('index.description.list[2]')"></span>
              <span v-html="$t('index.description.list[3]')"></span>
            </div>
          </div>
        </v-col>

        <v-col cols="12" class="text-center">
          <div style="margin-top: 100px">
            <span class="text-h6" @click="get_sponsor" style="cursor: pointer">{{ $t("index.description.list[4]") }}</span> <br>
            <div style="margin-top: 8px; font-size: 14px">
              <div class="yellow--text text--darken-3">
                <b><span>Edisson</span></b>
              </div>
            </div>
          </div>
        </v-col>
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
