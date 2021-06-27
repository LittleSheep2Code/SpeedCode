<template>
  <v-app>
    <BasicLayout>
      <router-view/>
    </BasicLayout>
  </v-app>
</template>

<script>
import BasicLayout from "@/layouts/BasicLayout";
export default {
  components: { BasicLayout }
}
</script>
