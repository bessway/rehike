import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)
export default new Vuex.Store({
  state: {
    selectedTest: {},
    checkedTests: [],
    testParas: [],
    actions: []
  },
  getters: {
    getSelectedTest: state => {
      return state.selectedTest
    },
    getTestParas: state => {
      return state.testParas
    },
    getActions: state => {
      return state.actions
    }
  },
  mutations: {
    setSelectedTest (state, test) {
      state.selectedTest = test
      state.testParas = []
    },
    setTestParas (state, paras) {
      state.testParas = paras
    },
    setActions (state, actions) {
      state.actions = actions
    }
  }
})
