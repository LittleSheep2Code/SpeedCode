<template>
  <v-app>
    <v-navigation-drawer v-model="isNavigationOpen" app class="secondary lighten-2" fixed elevate-on-scroll>
      <v-list nav dense>
        <v-list-item v-for="(item, index) in navigatorItems" :key="index" :disabled="$route.path === item.component" @click="$router.push(item.component)" link>

          <v-list-item-icon><v-icon>{{ item.icon }}</v-icon></v-list-item-icon>

          <v-list-item-content><v-list-item-title>{{ item.title }}</v-list-item-title></v-list-item-content>

        </v-list-item>
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

    <v-app-bar app color="secondary lighten-3" flat>
      <v-app-bar-nav-icon @click="isNavigationOpen = !isNavigationOpen"></v-app-bar-nav-icon>
      <v-app-bar-title>{{ $t("application") }}</v-app-bar-title>

      <v-spacer></v-spacer>

      <!--  Doesn't login avatar    -->
      <v-menu offset-y bottom v-if="selectAccounts.access == null" open-on-hover>
        <template v-slot:activator="{ on, attrs }">
          <v-avatar class="mr-10" color="blue-grey darken-1" size="32px" v-bind="attrs" v-on="on" tile><v-icon color="white">mdi-account</v-icon></v-avatar>
        </template>

        <v-list class="text-right">
          <v-list-item @click="dialogs.Login = true"><v-list-item-subtitle>Login &nbsp; <v-icon>mdi-login-variant</v-icon></v-list-item-subtitle></v-list-item>
          <v-list-item @click="dialogs.Register = true"><v-list-item-subtitle>Register &nbsp; <v-icon>mdi-account-plus</v-icon></v-list-item-subtitle></v-list-item>
        </v-list>
      </v-menu>

      <!--  Completed login avatar    -->
      <v-menu offset-y bottom v-else open-on-hover>
        <template v-slot:activator="{ on, attrs }">
          <v-avatar class="mr-10" color="orange darken-1" size="32px" v-bind="attrs" v-on="on" tile>{{ selectAccounts.username.substr(0, 1) }}</v-avatar>
        </template>

        <v-list class="text-right">
          <v-list-item @click="exit_account"><v-list-item-subtitle>Logout &nbsp; <v-icon>mdi-logout-variant</v-icon></v-list-item-subtitle></v-list-item>
        </v-list>
      </v-menu>

    </v-app-bar>

    <v-main><v-container fill-height><slot/></v-container></v-main>

    <v-dialog v-model="dialogs.Login" width="500px">
      <LoginDialog></LoginDialog>
    </v-dialog>

    <v-dialog v-model="dialogs.Register" width="500px">
      <RegisterDialog></RegisterDialog>
    </v-dialog>
  </v-app>
</template>

<script>
import i18n from "@/i18n";
import LoginDialog from "@/components/Account/LoginDialog";
import RegisterDialog from "@/components/Account/RegisterDialog";

export default {
  name: "BasicLayout",
  components: { RegisterDialog, LoginDialog },
  mounted() {
    this.selectAccounts.access = this.$cookies.get("access")

    if(this.selectAccounts.access != null) {

      this.axios.get("/s-code/account/detail", { headers: { "access_token": this.selectAccounts.access } }).then(res => {

        this.selectAccounts.username = res.data["information"]["username"]
      })
    }
  },

  methods: {
    exit_account() {
      this.$dialog.warning({
        title: this.$t("actions.confirm"),
        text: this.$t("user-manage.dialogs.message.logout"),
        icon: "mdi-logout",
        showClose: false,

      }).then(s => {
        if(s) {
          this.$dialog.notify.info(i18n.t("user-manage.dialogs.res.logout.completed"), {
            position: "top-right",
            timeout: 3000
          })

          this.$cookies.remove("access")

          setTimeout(() => {
            this.$router.go(0)
          }, 3000)
        }
      })
    }
  },

  data: () => ({
    dialogs: {
      Login: false,
      Register: false
    },

    selectAccounts: {
      access: undefined,
      username: undefined
    },

    isNavigationOpen: false,

    navigatorItems: [
      { icon: "mdi-latitude", title: i18n.t("navigator.menu.main"), component: "/" },
      { icon: "mdi-content-save-edit", title: i18n.t("navigator.menu.code-runner"), component: "/utils/editor" }
    ]
  }),
}
</script>

<style>

</style>
