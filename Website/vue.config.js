const HtmlWebpackPlugin = require("html-webpack-plugin");
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
    plugins: [
      new HtmlWebpackPlugin({
        filename: 'public/index.html',
        template: 'public/index.html',
        favicon: 'public/favicon.png',
        inject: true
      })
    ]
  },

  pwa: {
    iconPaths: {
      favicon32: 'public/favicon.ico',
      favicon16: 'public/favicon.ico',
      appleTouchIcon: 'public/favicon.ico',
      maskIcon: 'public/favicon.ico',
      msTileImage: 'public/favicon.ico'
    }
  },

  transpileDependencies: [
    'vuetify'
  ]
}
