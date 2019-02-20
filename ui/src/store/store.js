import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)
export default new Vuex.Store({
  state: {
    selectedTest: {},
    checkedTests: [],
    testParas: [],
    actions: [],
    uiobjectPages: [],
    activeEditor: {},
    isAddNewTest: '',
    agents: []
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
    },
    getUIObjPages: state => {
      return state.uiobjectPages
    },
    getActiveEditor: state => {
      return state.activeEditor
    },
    getIsAddNewTest: state => {
      return state.isAddNewTest
    },
    getAgents: state => {
      return state.agents
    },
    getCheckedTests: state => {
      return state.checkedTests
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
    },
    setUIObjectPages (state, pages) {
      state.uiobjectPages = pages
    },
    setActiveEditor (state, editor) {
      state.activeEditor = editor
    },
    setIsAddNewTest (state, testId) {
      state.isAddNewTest = testId
    },
    setAgents (state, agents) {
      state.agents = agents
    },
    setCheckedTests (state, checkedTests) {
      state.checkedTests = checkedTests
    }
  }
})
