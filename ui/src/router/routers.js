import Vue from 'vue'
import Router from 'vue-router'
import TestDetail from '@/components/TestDetail'
import Execution from '@/components/Execution'
Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      redirect: '/case'
    },
    {
      path: '/case',
      component: TestDetail
    },
    {
      path: '/execution',
      name: 'executionmanager',
      component: Execution
    }
  ]
})
