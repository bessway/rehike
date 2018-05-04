import Vue from 'vue'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import home from './components/Home/home.vue'

Vue.use(ElementUI)

new Vue({
  el: '#app',//这里是在把home注册为一个组件
  render: h => h(home)
})
