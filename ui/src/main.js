// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import AppVue from './App'
import router from './router/routers'
import store from './store/store'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'

Vue.use(ElementUI)
Vue.config.productionTip = false
// require('./mock')
var API = require('./api')
Vue.prototype.API = API

/* eslint-disable no-new */
new Vue({
  // 挂载到入口index.html的id=mountpoint的位置
  el: '#mountpoint',
  /*
  // 将 App 这个组件对象和{el: '#app'}这个对象直接合并, 变成Vue的参数
  // 这种情况下, this.$root 是 App 组件
  ...AppVue,
  */

  // 声明需要使用的组件，是components:{AppVue:AppVue}的缩写
  components: { AppVue },
  // 使用AppVue里的html来替换挂载点的元素div
  template: '<AppVue/>',
  store,
  router
  /*
  // 另外一种写法，createElement是render的核心方法，Vue编译的时候会把模版里面的节点解析成虚拟dom；
  // 虚拟dom不同于真正的dom，它是一个JavaScript对象。当状态发生变化的时候虚拟dom会进行一个diff判断/运算；
  // 然后判断哪些dom是需要被替换的而不是全部重绘，所以性能会比dom操作高很多
  // (x) => x + 6 相当于function(x){return x + 6;};
  // 在箭头函数引用了this、arguments或者参数之外的变量，那它们一定不是箭头函数本身包含的，而是从父级作用域继承的
  render: creatElment => creatElment(AppVue)
  render最优先渲染，其次template，然后才用index.html里的元素
  */
})// .$mount('#mountpoint')
