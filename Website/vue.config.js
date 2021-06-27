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
        target: 'http://127.0.0.1:20020',
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
