module.exports = {
  pluginOptions: {
    i18n: {
      locale: 'en-US',
      fallbackLocale: 'en-US',
      localeDir: 'i18n',
      enableInSFC: false
    }
  },

  devServer: {
    proxy: {
      '/reCaptcha': {
        target: 'https://recaptcha.net',
        changeOrigin: true,
        pathRewrite: {
          '^/reCaptcha': ''
        }
      },

      '/s-code': {
        target: 'https://apis.speed-code.online',
        changeOrigin: true,
        pathRewrite: {
          '^/s-code': ''
        }
      }
    }
  },

  configureWebpack: {

  },

  transpileDependencies: [
    'vuetify'
  ]
}
