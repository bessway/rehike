<template>
  <el-col :span=24 class='steps'>
    <el-row class="para" v-if="editable">
      <el-button :disabled="isTestSelected()" @click="moveUpSteps">上移</el-button>
      <el-button :disabled="isTestSelected()" @click="moveDownSteps">下移</el-button>
      <el-button :disabled="isTestSelected()" @click="debug">复制</el-button>
      <el-button :disabled="isTestSelected()" @click="addStepAfter">添加</el-button>
      <el-button :disabled="isTestSelected()" @click="delSteps">删除</el-button>
      <el-button :disabled="isTestSelected()" @click="refPublicTest">引用</el-button>
      <el-button :disabled="isTestSelected()" @click="saveTestValue">保存用例&参数</el-button>
      <el-input placeholder="已上传的文件地址" readonly></el-input>
      <el-upload
        class="upload"
        action="https://jsonplaceholder.typicode.com/posts/"
        :limit="1">
        <el-button size="small" type="primary">上传</el-button>
      </el-upload>
    </el-row>
    <el-table
      :show-header=false
      :data="currTest.steps"
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
        <stepdetail
          :step="selectedStep"
          :uiobject="uiobject"
          :referTest="refTest"
          :referParas="formalParas"
          :testParas="refTestParas"
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

export default {
  name: 'steptable',
  props: ['currTest', 'testParas', 'editable'],
  beforeCreate: function () {
    // 循环调用组件时，组件比vue实例后创建，官方文档里写组件必须先于实例化引入，所以说组件没有正确的引入。
    // 所以这里异步引入，或者可以在main.js全局引入Vue.component('stepdetail', StepDetail)
    this.$options.components.stepdetail = () => import('./StepDetail.vue')
  },
  data () {
    return {
      onlyExpanded: [],
      selectedStep: {},
      uiobject: {uiObjectId: '', uiObjectPage: '', uiObjectType: '', uiObjectName: '', uiObjectPath: ''},
      refTest: {steps: []},
      formalParas: [],
      refTestParas: [],
      checkedStep: [],
      searchResult: [],
      toRefTest: {},
      searchKey: '',
      searchDlgVisible: false
    }
  },
  watch: {
    currTest: function () {
      this.onlyExpanded = []
      this.selectedStep = {}
    }
  },
  methods: {
    ...mapGetters(['getTestParas', 'getSelectedTest']),
    hideBbutton () {
      if (this.editable) {
        return 'display:none'
      }
    },
    async showStepDetail (currRow, expandedRows) {
      console.log(currRow)
      console.log(this.selectedStep)
      // 重复点击时还原数据
      if (currRow.index === this.selectedStep.index) {
        this.selectedStep = {}
        return
      }
      this.selectedStep = currRow
      await this.loadUIObject(currRow)
      await this.loadRefTest(currRow)
      // 在递归引用里面不能使用router，因为每次都会push到第一层的地址，导致没法显示子组件
      // this.$router.push({path: '/case/stepdetail'})
      // 必须放在最后，因为这个方法实际是在展开后才调用，展开时stepdetail实例还没有创建
      // 下面这句会重置展开状态
      this.onlyExpanded = [currRow.index]
    },
    async loadUIObject (currRow) {
      if (currRow.uiObjectId !== undefined && currRow.uiObjectId !== null) {
        var res = await this.API.getUIObject(this.selectedStep.uiObjectId)
        this.uiobject = res
      } else {
        this.uiobject = {uiObjectId: '', uiObjectPage: '', uiObjectType: '', uiObjectName: '', uiObjectPath: ''}
      }
    },
    async loadRefTest (currRow) {
      // 如果不是refstep, 则refTest={}
      if (currRow.stepType !== 2 || currRow.refTestId === undefined || currRow.refTestId === null) {
        this.refTestParas = this.testParas.slice(0)
        this.refTest = {}
      } else {
        this.formalParas = []
        this.refTest = await this.API.getTestDetail(currRow.refTestId)
        this.refTestParas = await this.API.getTestParasAll(currRow.refTestId, 'default')
        console.log(currRow)
        console.log(this.testParas)
        this.testParas.forEach(item => {
          if (item.stepId === currRow.index && item.refTestId !== undefined && item.refTestId !== null) {
            this.formalParas.push(item)
          }
        })
        console.log(this.formalParas)
      }
    },
    setMultiSelect (selection, row) {
      this.checkedStep = selection
    },
    addStepAfter () {
      if (this.currTest.steps === null) {
        this.currTest.steps = []
      }
      var newStep = {}
      newStep.index = Object.keys(this.currTest.steps).length
      newStep.actionId = ''
      newStep.stepDesc = 'new step' + newStep.index
      newStep.stepType = 0
      newStep.paras = []
      newStep.resParaId = ''
      this.addNewStepAfter(newStep)
    },
    refPublicTest () {
      if (this.currTest.steps === null) {
        this.currTest.steps = []
      }
      this.toRefTest = {}
      this.searchDlgVisible = true
    },
    async addRefTestStep () {
      this.searchDlgVisible = false
      if (Object.keys(this.toRefTest).length !== 0) {
        var newStep = {}
        newStep.index = Object.keys(this.currTest.steps).length
        newStep.stepType = 2
        newStep.refTestId = this.toRefTest.testId
        newStep.stepDesc = this.toRefTest.testDesc
        newStep.paras = []
        newStep.resParaId = ''
        var newFormalParas = await this.API.copyFormalParas(this.toRefTest.testId, this.currTest.testId, newStep.index)
        for (var item in newFormalParas) {
          this.testParas.push(item)
        }
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
        this.currTest.steps.splice(this.checkedStep[0].index + 1, 0, newStep)
        for (var i = this.checkedStep[0].index + 1; i < this.currTest.steps.length; i++) {
          this.currTest.steps[i].index = i
        }
      } else if (this.checkedStep.length === 0) {
        this.currTest.steps.push(newStep)
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
      return Object.keys(this.currTest).length === 0
    },
    delSteps () {
      if (this.currTest.steps === null || this.checkedStep.length === 0) {
        return
      }
      // this.API.delRefFormalParas([])
      this.checkedStep.sort(function (a, b) {
        return a.index - b.index
      })
      for (var i = this.checkedStep.length - 1; i >= 0; i--) {
        this.currTest.steps.splice(this.checkedStep[i].index, 1)
      }
      this.checkedStep = []
      this.$refs.stepstable.clearSelection()
      for (i = 0; i < this.currTest.steps.length; i++) {
        this.currTest.steps[i].index = i
      }
    },
    moveUpSteps () {
      if (this.currTest.steps === null || this.checkedStep.length !== 1) {
        alert('只能选一行')
      } else if (this.checkedStep[0].index !== 0) {
        this.currTest.steps[this.checkedStep[0].index] = this.currTest.steps[this.checkedStep[0].index - 1]
        this.currTest.steps[this.checkedStep[0].index - 1] = this.checkedStep[0]
        this.currTest.steps[this.checkedStep[0].index].index = this.checkedStep[0].index
        this.checkedStep[0].index = this.checkedStep[0].index - 1
        this.currTest.steps = this.currTest.steps.slice(0)
        this.$nextTick(function () {
          this.$refs.stepstable.toggleRowSelection(this.checkedStep[0], true)
        })
      }
    },
    moveDownSteps () {
      if (this.currTest.steps === null || this.checkedStep.length !== 1) {
        alert('只能选一行')
      } else if (this.checkedStep[0].index !== this.currTest.steps.length - 1) {
        this.currTest.steps[this.checkedStep[0].index] = this.currTest.steps[this.checkedStep[0].index + 1]
        this.currTest.steps[this.checkedStep[0].index + 1] = this.checkedStep[0]
        this.currTest.steps[this.checkedStep[0].index].index = this.checkedStep[0].index
        this.checkedStep[0].index = this.checkedStep[0].index + 1
        this.currTest.steps = this.currTest.steps.slice(0)
        // vue是异步渲染，在每个tick开始前会搜集所有的数据变化，放入队列，多次修改仅最后一次生效，然后统一渲染
        // 所以如果要在数据变化后的基础上做修改，需要放在nexttick
        this.$nextTick(function () {
          this.$refs.stepstable.toggleRowSelection(this.checkedStep[0], true)
        })
      }
    },
    // 无论展开哪一级步骤，保存值都只保存当前选中的test
    async saveTestValue () {
      await this.API.updateTest(this.getSelectedTest())
      if (this.getTestParas().length > 0) {
        await this.API.setParasValue(this.getTestParas())
      }
    },
    debug () {
      console.log(this.getSelectedTest())
      console.log(this.currTest)
      console.log(this.refTest)
      console.log(this.refTestParas)
      console.log(this.formalParas)
      console.log(this.testParas)
      console.log(this.getTestParas())
    }
  }
}
</script>
