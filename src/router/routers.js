import Vue from 'vue'
import Router from 'vue-router'
import TestDetail from '@/components/TestDetail'
import Execution from '@/components/Execution'
import UIstep from '@/components/UIStep.vue'
import APIstep from '@/components/APIStep.vue'
import RefStep from '@/components/RefStep.vue'
Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      component: TestDetail
    },
    {
      path: '/case',
      component: TestDetail,
      children: [
        {
          path: '/case/uistep',
          component: UIstep
        },
        {
          path: '/case/apistep',
          component: APIstep
        },
        {
          path: '/case/refstep',
          component: RefStep
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
