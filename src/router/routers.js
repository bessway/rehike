import Vue from 'vue'
import Router from 'vue-router'
import CaseDetail from '@/components/CaseDetail'
import Execution from '@/components/Execution'
import UIstep from '@/components/UIStep.vue'
import APIstep from '@/components/APIStep.vue'
Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      component: CaseDetail
    },
    {
      path: '/case',
      component: CaseDetail,
      children: [
        {
          path: '/uistep',
          component: UIstep
        },
        {
          path: '/apistep',
          component: APIstep
        }
      ]
    },
    {
      path: '/execution',
      name: 'executionmanager',
      component: Execution
    }
  ]
})
