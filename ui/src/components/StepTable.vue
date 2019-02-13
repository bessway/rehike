<template>
  <el-col :span=24 class='steps'>
    <el-row v-if="editable">
      <el-button :disabled="isTestSelected()" @click="moveUpSteps">上移</el-button>
      <el-button :disabled="isTestSelected()" @click="moveDownSteps">下移</el-button>
      <el-button :disabled="isTestSelected()" @click="debug">复制</el-button>
      <el-button :disabled="isTestSelected()" @click="addStepAfter">添加</el-button>
      <el-button :disabled="isTestSelected()" @click="delSteps">删除</el-button>
      <el-button :disabled="isTestSelected()" @click="refPublicTest">引用</el-button>
      <el-button :disabled="isTestSelected()" @click="saveTestValue">保存用例&参数</el-button>
    </el-row>
    <el-table
      :show-header=false
      :data="selectedTest.steps"
      style="height: 85%; overflow-y: auto"
      @expand-change="showStepDetail"
      row-key="index"
      :expand-row-keys="onlyExpanded"
      @select="setMultiSelect"
      ref="stepstable">
      <el-table-column
        type="index"
        width="25">
      </el-table-column>
      <el-table-column
        type="expand"
        width="15">
        <!--stepdetail
          :action="action"
          :paras="paras"
          :uiobject="uiobject"
          :refTest="refTest"
          :step="selectedStep"
          :editable="editable">
        </stepdetail-->
        <stepdetail
          :step="selectedStep"
          :uiobject="uiobject"
          :refTest="refTest"
          :refParas="refParas"
          :editable="editable">
        </stepdetail>
      </el-table-column>
      <el-table-column
        prop="stepDesc"
        show-overflow-tooltip>
      </el-table-column>
      <el-table-column
        type="selection"
        :selectable="() => {return editable}"
        width="15">
      </el-table-column>
    </el-table>
    <el-dialog title="搜索公共用例"
      :visible.sync="searchDlgVisible"
      :close-on-click-modal=false
      :close-on-press-escape=false
      :show-close=false>
      <div class='test-desc'>
        <el-input v-model="searchKey" autocomplete="off"></el-input>
        <el-button type="text" @click="searchTestByDesc">搜索</el-button>
      </div>
      <el-table
      :show-header=false
      :data="searchResult"
      highlight-current-row
      @row-click="setToRefTest"
      style="height: 100%; overflow-y: auto">
        <el-table-column width="600" prop="testDesc"></el-table-column>
      </el-table>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancelRefTest">取 消</el-button>
        <el-button type="primary" @click="addRefTestStep">确 定</el-button>
      </div>
    </el-dialog>
  </el-col>
</template>

<style lang="scss">
.steps {
  float: left;
  border-right: 1px solid rgba(236, 234, 234, 0.925);
  height: 100%;
  padding-left: 3px;
}
.el-table {
  margin-right: 14px;
  .el-checkbox {
    width: 14px;
  }
  .cell {
    padding: 0px;
  }
  .el-table-column--selection {
    width: 16px;
    .cell {
      width: 16px;
    }
  }
  .el-table__expanded-cell[class*=cell] {
    padding-left: 20px;
    padding-right: 16px;
    padding-top: 0px;
    padding-bottom: 10px;
  }
}
</style>

<script>
import { mapGetters } from 'vuex'
// import { Message } from 'element-ui'

export default {
  name: 'steptable',
  props: ['selectedTest', 'editable'],
  beforeCreate: function () {
    // 循环调用组件时，组件比vue实例后创建，官方文档里写组件必须先于实例化引入，所以说组件没有正确的引入。
    // 所以这里异步引入，或者可以在main.js全局引入Vue.component('stepdetail', StepDetail)
    this.$options.components.stepdetail = () => import('./StepDetail.vue')
  },
  data () {
    return {
      onlyExpanded: [],
      // action: {actionId: '', actionName: '', hasUIObject: 1, actionType: 1},
      // paras: {p1: {paraName: '', paraId: ''}, p2: {paraName: '', paraId: ''}, p3: {paraName: '', paraId: ''}, p4: {paraName: '', paraId: ''}, p5: {paraName: '', paraId: ''}, response: {paraName: '', paraId: ''}},
      selectedStep: {},
      uiobject: {uiObjectId: '', uiObjectPage: '', uiObjectType: '', uiObjectName: '', uiObjectPath: ''},
      refTest: {},
      refParas: [],
      checkedStep: [],
      searchResult: [],
      toRefTest: {},
      searchKey: '',
      searchDlgVisible: false
    }
  },
  methods: {
    ...mapGetters(['getTestParas']),
    hideBbutton () {
      if (this.editable) {
        return 'display:none'
      }
    },
    // findAcion (currRow) {
    //   if (currRow.actionId === undefined || currRow.actionId === '') {
    //     this.action = {actionId: '', actionName: '', hasUIObject: 1, actionType: 1}
    //     return
    //   }
    //   for (var i = 0; i < this.getActions().length; i++) {
    //     // if (currRow.actionId === this.getActions()[i].actionId) {
    //     if (currRow.actionId.indexOf(this.getActions()[i].actionId.substring(0, 1)) >= 0) {
    //       console.log(this.getActions()[i])
    //       this.action = this.getActions()[i]
    //       return
    //     }
    //   }
    //   Message.error({message: '找不到' + currRow.actionId + '对应的操作!'})
    //   this.action = {actionId: '', actionName: '', hasUIObject: 1, actionType: 1}
    // },
    // findParas (currRow) {
    //   var paras = {p1: {}, p2: {}, p3: {}, p4: {}, p5: {}, response: {}}
    //   var temp = {}
    //   if (currRow.paras.length > 0) {
    //     var ids = currRow.paras.slice(0)
    //     ids.push(currRow.resParaId)
    //     ids.forEach((paraId, index) => {
    //       for (var i = 0; i < this.getTestParas().length; i++) {
    //         if (paraId === this.getTestParas()[i].paraId) {
    //         // if (paraId.indexOf(this.getTestParas()[i].paraId.substring(0, 1)) >= 0) {
    //           temp = this.getTestParas()[i]
    //           break
    //         }
    //       }
    //       if (temp.length === 0) {
    //         temp = {paraName: '', paraId: ''}
    //         Message.error({message: '找不到' + paraId + '对应的参数!'})
    //       }
    //       if (index === ids.length - 1) {
    //         paras['response'] = temp
    //       } else {
    //         paras['p' + (index + 1)] = temp
    //       }
    //     })
    //   }
    //   console.log(paras)
    //   this.paras = paras
    // },
    async showStepDetail (currRow, expandedRows) {
      console.log(currRow.index + ' ' + this.selectedStep.index)
      // 重复点击时还原数据
      if (currRow.index === this.selectedStep.index) {
        this.selectedStep = {}
        return
      }
      this.selectedStep = currRow
      console.log(this.selectedStep)
      // this.findAcion(currRow)
      // this.findParas(currRow)
      await this.loadUIObject(currRow)
      await this.loadRefTest(currRow)
      // 在递归引用里面不能使用router，因为每次都会push到第一层的地址，导致没法显示子组件
      // this.$router.push({path: '/case/stepdetail'})
      // 必须放在最后，因为这个方法实际是在展开后才调用，展开时stepdetail实例还没有创建
      // 下面这句会重置展开状态
      this.onlyExpanded = [currRow.index]
    },
    async loadUIObject (currRow) {
      if (currRow.uiObjectId !== undefined && currRow.uiObjectId !== '') {
        var res = await this.API.getUIObject(this.selectedStep.uiObjectId)
        this.uiobject = res
      } else {
        this.uiobject = {uiObjectId: '', uiObjectPage: '', uiObjectType: '', uiObjectName: '', uiObjectPath: ''}
      }
    },
    async loadRefTest (currRow) {
      if (currRow.stepType !== 2 || currRow.refTestId === undefined || currRow.refTestId === '') {
        this.refTest = {}
      } else {
        var refTestDetail = await this.API.getTestDetail(currRow.refTestId)
        this.refTest = refTestDetail
        var refTestPara = await this.API.getRefStepParas(this.selectedTest.testId, currRow.index)
        this.refParas = refTestPara
      }
      console.log(this.refTest)
    },
    setMultiSelect (selection, row) {
      this.checkedStep = selection
    },
    addStepAfter () {
      if (this.selectedTest.steps === null) {
        this.selectedTest.steps = []
      }
      var newStep = {}
      newStep.index = Object.keys(this.selectedTest.steps).length
      newStep.actionId = ''
      newStep.stepDesc = 'new step' + newStep.index
      newStep.stepType = 0
      newStep.paras = []
      newStep.resParaId = ''
      this.addNewStepAfter(newStep)
    },
    refPublicTest () {
      if (this.selectedTest.steps === null) {
        this.selectedTest.steps = []
      }
      this.toRefTest = {}
      this.searchDlgVisible = true
    },
    async addRefTestStep () {
      this.searchDlgVisible = false
      if (Object.keys(this.toRefTest).length !== 0) {
        var newStep = {}
        newStep.index = Object.keys(this.selectedTest.steps).length
        newStep.stepType = 2
        newStep.refTestId = this.toRefTest.testId
        newStep.stepDesc = this.toRefTest.testDesc
        newStep.paras = []
        newStep.resParaId = ''
        await this.API.copyFormalParas(this.toRefTest.testId, this.selectedTest.testId, newStep.index)
        this.addNewStepAfter(newStep)
      }
    },
    cancelRefTest () {
      this.searchResult = []
      this.searchKey = ''
      this.toRefTest = {}
      this.searchDlgVisible = false
    },
    addNewStepAfter (newStep) {
      if (this.checkedStep.length === 1) {
        this.selectedTest.steps.splice(this.checkedStep[0].index + 1, 0, newStep)
        for (var i = this.checkedStep[0].index + 1; i < this.selectedTest.steps.length; i++) {
          this.selectedTest.steps[i].index = i
        }
      } else if (this.checkedStep.length === 0) {
        this.selectedTest.steps.push(newStep)
      } else {
        alert('只能选一行')
      }
      this.searchResult = []
      this.searchKey = ''
    },
    async searchTestByDesc () {
      if (this.searchKey === '') {
        return
      }
      this.searchResult = await this.API.searchTest(this.searchKey)
    },
    setToRefTest (row, event, column) {
      this.toRefTest = row
    },
    isTestSelected () {
      return Object.keys(this.selectedTest).length === 0
    },
    delSteps () {
      if (this.selectedTest.steps === null || this.checkedStep.length === 0) {
        return
      }
      // this.API.delRefFormalParas([])
      this.checkedStep.sort(function (a, b) {
        return a.index - b.index
      })
      for (var i = this.checkedStep.length - 1; i >= 0; i--) {
        this.selectedTest.steps.splice(this.checkedStep[i].index, 1)
      }
      this.checkedStep = []
      this.$refs.stepstable.clearSelection()
      for (i = 0; i < this.selectedTest.steps.length; i++) {
        this.selectedTest.steps[i].index = i
      }
    },
    moveUpSteps () {
      if (this.selectedTest.steps === null || this.checkedStep.length !== 1) {
        alert('只能选一行')
      } else if (this.checkedStep[0].index !== 0) {
        this.selectedTest.steps[this.checkedStep[0].index] = this.selectedTest.steps[this.checkedStep[0].index - 1]
        this.selectedTest.steps[this.checkedStep[0].index - 1] = this.checkedStep[0]
        this.selectedTest.steps[this.checkedStep[0].index].index = this.checkedStep[0].index
        this.checkedStep[0].index = this.checkedStep[0].index - 1
        this.selectedTest.steps = this.selectedTest.steps.slice(0)
        this.$nextTick(function () {
          this.$refs.stepstable.toggleRowSelection(this.checkedStep[0], true)
        })
      }
    },
    moveDownSteps () {
      if (this.selectedTest.steps === null || this.checkedStep.length !== 1) {
        alert('只能选一行')
      } else if (this.checkedStep[0].index !== this.selectedTest.steps.length - 1) {
        this.selectedTest.steps[this.checkedStep[0].index] = this.selectedTest.steps[this.checkedStep[0].index + 1]
        this.selectedTest.steps[this.checkedStep[0].index + 1] = this.checkedStep[0]
        this.selectedTest.steps[this.checkedStep[0].index].index = this.checkedStep[0].index
        this.checkedStep[0].index = this.checkedStep[0].index + 1
        this.selectedTest.steps = this.selectedTest.steps.slice(0)
        // vue是异步渲染，在每个tick开始前会搜集所有的数据变化，放入队列，多次修改仅最后一次生效，然后统一渲染
        // 所以如果要在数据变化后的基础上做修改，需要放在nexttick
        this.$nextTick(function () {
          this.$refs.stepstable.toggleRowSelection(this.checkedStep[0], true)
        })
      }
    },
    async saveTestValue () {
      await this.API.updateTest(this.selectedTest)
      if (this.getTestParas().length > 0) {
        await this.API.setParasValue(this.getTestParas())
      }
    },
    debug () {
      console.log(this.selectedTest)
    }
  }
}
</script>
