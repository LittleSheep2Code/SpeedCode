<template>
  <v-container fill-height>
    <v-row justify="center" align="center">
      <v-col cols="8">
        <v-card>
          <v-card-title class="text-h5 grey lighten-3">
            <img src="../../assets/Images/SpeedCode-v2-Icon.svg" width="24" height="30" alt="icon" /> &nbsp;
            <span>{{ $t("application") }}</span> <br>
          </v-card-title>

          <v-card-text>
            <div style="margin-top: 30px">
              <div>
                <div class="h3" style="margin-bottom: 8px; width: 100%">
                  <span class="text-left">SpeedCode v2.0.3</span>
                </div>
                <v-divider></v-divider>

                <div style="padding-left: 5px; font-size: 16px; margin-top: 8px">
                  <span>+ &nbsp; Add settings reset</span> <br>
                  <span>= &nbsp; Fixed cannot using stdin bugs</span> <br>
                </div>
              </div>

              <div class="next">
                <div class="h3" style="margin-bottom: 8px; width: 100%">
                  <span class="text-left">SpeedCode v2.0.2</span>
                </div>
                <v-divider></v-divider>

                <div style="padding-left: 5px; font-size: 16px; margin-top: 8px">
                  <span>+ &nbsp; New Homepage</span> <br>
                  <span>= &nbsp; Fixed Login and Account operating Bug</span> <br>
                  <span>= &nbsp; Fixed reCaptcha Bugs</span> <br>
                </div>
              </div>

              <div class="next">
                <div class="h3" style="margin-bottom: 8px; width: 100%">
                  <span class="text-left">SpeedCode v2.0.1</span>
                  <b class="text-right float-right">Major Update!</b>
                </div>
                <v-divider></v-divider>

                <div style="padding-left: 5px; font-size: 16px; margin-top: 8px">
                  <span>+ &nbsp; Rewrite all code</span> <br>
                  <span>+ &nbsp; Move Account setting from settings to BasicLayout</span> <br>
                  <span>+ &nbsp; Now using CodeMirror Editor</span> <br>
                  <span>+ &nbsp; Better experience on iPad and small screen device</span> <br>
                  <span>+ &nbsp; Using grid layout</span> <br>
                  <span>+ &nbsp; Add i18n and Language Switcher</span> <br>
                  <span>+ &nbsp; Better data updater, without reload now</span> <br>
                  <span>+ &nbsp; Open Source on Gitee and GitHub</span> <br>
                  <span>+ &nbsp; New more Logo and Icons</span> <br>
                  <span>+ &nbsp; Using Vuetify(Vue2 + Webpack) + Flask</span> <br>
                  <span>= &nbsp; Remove Bugs</span> <br>
                  <span>= &nbsp; <b>More 5+</b></span>
                </div>
              </div>
            </div>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>

    <Footers></Footers>
  </v-container>
</template>

<script>
import Footers from "@/components/LayoutsComponents/Footers/Footers";
export default {
  name: "UpdateLogs",
  components: { Footers }
}
</script>

<style scoped>
.h3 {
  display: block;
  font-size: 1.17em;
  margin-block-start: 1em;
  margin-block-end: 1em;
  margin-inline-start: 0px;
  margin-inline-end: 0px;
}

.next {
  margin-top: 10px;
}
</style>
