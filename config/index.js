'use strict'
// Template version: 1.3.1
// see http://vuejs-templates.github.io/webpack for documentation.

const path = require('path')

module.exports = {
  dev: {

    // Paths
    assetsSubDirectory: 'static',
    assetsPublicPath: '/',
    // 开发过程中代理请求。开发时我们会在本地启用一个nodejs服务器，然后代理你的所有
    // 请求，把请求转发至远程开发服务器，解决跨域问题。
    // 下边是一个简单的设置
    proxyTable: {},

    // Various Dev Server settings
    host: '127.0.0.1', // can be overwritten by process.env.HOST
    port: 8010, // can be overwritten by process.env.PORT, if port is in use, a free one will be determined
    autoOpenBrowser: false,
    errorOverlay: true,
    notifyOnErrors: true,
    poll: false, // https://webpack.js.org/configuration/dev-server/#devserver-watchoptions-

    // Use Eslint Loader?
    // If true, your code will be linted during bundling and
    // linting errors and warnings will be shown in the console.
    useEslint: true,
    // If true, eslint errors and warnings will also be shown in the error overlay
    // in the browser.
    showEslintErrorsInOverlay: false,

    /**
     * Source Maps
     */

    // https://webpack.js.org/configuration/devtool/#development
    devtool: 'cheap-module-eval-source-map',

    // If you have problems debugging vue-files in devtools,
    // set this to false - it *may* help
    // https://vue-loader.vuejs.org/en/options.html#cachebusting
    cacheBusting: true,

    cssSourceMap: true
  },

  build: {
    // Template for index.html
    // 入口文件是打包后的dist文件夹中的index.html文件，必须是绝对路径
    index: path.resolve(__dirname, '../dist/index.html'),

    // Paths
    // 指向包含打包后所有静态资源的目录地址。必须是绝对路径
    assetsRoot: path.resolve(__dirname, '../dist'),
    // 这里是所有的静态资产，其中static的是被直接复制过来的，而assets先处理再拿过来
    assetsSubDirectory: 'static',
    assetsPublicPath: '/',

    /**
     * Source Maps
     */

    productionSourceMap: true,
    // https://webpack.js.org/configuration/devtool/#production
    devtool: '#source-map',

    // Gzip off by default as many popular static hosts such as
    // Surge or Netlify already gzip all static assets for you.
    // Before setting to `true`, make sure to:
    // npm install --save-dev compression-webpack-plugin
    productionGzip: false,
    productionGzipExtensions: ['js', 'css'],

    // Run the build command with an extra argument to
    // View the bundle analyzer report after build finishes:
    // `npm run build --report`
    // Set to `true` or `false` to always turn it on or off
    bundleAnalyzerReport: process.env.npm_config_report
  }
}
