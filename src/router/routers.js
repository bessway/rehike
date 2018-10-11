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
          path: '/uistep',
          component: UIstep
        },
        {
          path: '/apistep',
          component: APIstep
        },
        {
          path: '/refstep',
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
