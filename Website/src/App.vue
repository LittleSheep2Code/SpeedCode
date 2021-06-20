<template>
  <v-app id="inspire">
    <v-navigation-drawer v-model="isNavigationOpen" app>
      <v-list nav dense>
        <v-list-item-group v-model="navigatorSelected" color="primary">
          <v-list-item v-for="(item, index) in navigatorItems" :key="index" :disabled="navigatorSelected === index" @click="$router.push(item.component)" link>

            <v-list-item-icon><v-icon>{{ item.icon }}</v-icon></v-list-item-icon>

            <v-list-item-content><v-list-item-title>{{ item.title }}</v-list-item-title></v-list-item-content>

          </v-list-item>
        </v-list-item-group>
      </v-list>

      <template v-slot:append>
        <div class="py-2" style="margin-left: 4px">
          <v-tooltip right>
            <template v-slot:activator="{ on, attrs }">
              <v-btn large icon v-bind="attrs" v-on="on" @click="isNavigationOpen = !isNavigationOpen"><v-icon>mdi-close</v-icon></v-btn>
            </template>
            <span>{{ $t("navigator.close-menu") }}</span>
          </v-tooltip>
        </div>
      </template>
    </v-navigation-drawer>

    <v-app-bar app color="grey lighten-3" flat>
      <v-container class="py-0 fill-height">
        <v-app-bar-title>{{ $t("application") }}</v-app-bar-title>

        <v-spacer></v-spacer>

        <v-avatar class="mr-10" color="grey darken-1" size="32"></v-avatar>
      </v-container>
    </v-app-bar>

    <v-main><router-view /></v-main>

    <v-fab-transition>
      <v-btn fab dark bottom left v-show="!isNavigationOpen" @click="isNavigationOpen = !isNavigationOpen" id="nav-open-button">
        <v-icon>mdi-menu</v-icon>
      </v-btn>
    </v-fab-transition>
  </v-app>
</template>

<script>
import i18n from "@/i18n";

export default {

  data: () => ({
    isNavigationOpen: true,

    navigatorSelected: 0,
    navigatorItems: [
      { icon: "mdi-latitude", title: i18n.t("navigator.menu.main"), component: "/" }
    ]
  }),
}
</script>

<style>
#nav-open-button {
  bottom: 0;
  position: absolute;
  margin: 0 0 16px 16px;
}
</style>
