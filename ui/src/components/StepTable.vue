<template>
  <el-col :span=24 class="steps">
    <el-row class="test-detail-header" v-if="editable">
      <el-button style="font-size: 12px;padding-right: 1px;padding-top: 0px;padding-bottom: 0px;padding-left: 0px;margin-top: 3px;height: 25px;" :disabled="isTestSelected()" @click="moveUpSteps">上移</el-button>
      <el-button style="font-size: 12px;padding-right: 1px;padding-top: 0px;padding-bottom: 0px;padding-left: 0px;margin-top: 3px;height: 25px;" :disabled="isTestSelected()" @click="moveDownSteps">下移</el-button>
      <el-button style="font-size: 12px;padding-right: 1px;padding-top: 0px;padding-bottom: 0px;padding-left: 0px;margin-top: 3px;height: 25px;" :disabled="isTestSelected()" @click="debug">复制</el-button>
      <el-button style="font-size: 12px;padding-right: 1px;padding-top: 0px;padding-bottom: 0px;padding-left: 0px;margin-top: 3px;height: 25px;" :disabled="isTestSelected()" @click="addStepAfter">添加</el-button>
      <el-button style="font-size: 12px;padding-right: 1px;padding-top: 0px;padding-bottom: 0px;padding-left: 0px;margin-top: 3px;height: 25px;" :disabled="isTestSelected()" @click="delSteps">删除</el-button>
      <el-button style="font-size: 12px;padding-right: 1px;padding-top: 0px;padding-bottom: 0px;padding-left: 0px;margin-top: 3px;height: 25px;" :disabled="isTestSelected()" @click="refPublicTest">引用</el-button>
      <el-button style="font-size: 12px;padding-right: 1px;padding-top: 0px;padding-bottom: 0px;padding-left: 0px;margin-top: 3px;height: 25px;" :disabled="isTestSelected()" @click="saveTestValue">保存用例&参数</el-button>
      <el-input placeholder="已上传的文件地址" readonly></el-input>
      <el-upload
        class="upload"
        action="https://jsonplaceholder.typicode.com/posts/"
        :limit="1">
        <el-button style="font-size: 12px;padding-right: 1px;padding-top: 0px;padding-bottom: 0px;padding-left: 0px;margin-top: 3px;height: 25px;" size="small" type="primary">上传</el-button>
      </el-upload>
    </el-row>
    <el-table
      :show-header=false
      :data="currTest.steps"
      style="height: 85%; overflow-y: auto;"
      @expand-change="showStepDetail"
      row-key="uniqueIdInTest"
      :expand-row-keys="onlyExpanded"
      @select="setMultiSelect"
      ref="stepstable">
      <el-table-column
        type="index"
        width="40px">
      </el-table-column>
      <el-table-column
        type="expand"
        width="15px">
        <!--stepdetail slot-scope="scope"
          v-if="isShowDetail(scope.row.uniqueIdInTest)"
          :step="selectedStep"
          :readOnlyParas="testParas"
          :editable="editable">
        </stepdetail-->
        <stepdetail
          :step="selectedStep"
          :readOnlyParas="testParas"
          :editable="editable">
        </stepdetail>
        <!--button slot-scope="scope" v-if="isShowDetail(scope.row.uniqueIdInTest)">test</button-->
      </el-table-column>
      <el-table-column
        prop="stepDesc"
        show-overflow-tooltip
        min-width="500">
      </el-table-column>
      <el-table-column
        type="selection"
        :selectable="() => {return editable}"
        min-width="15">
      </el-table-column>
    </el-table>
    <el-dialog title="搜索公共用例"
      :visible.sync="searchDlgVisible"
      :close-on-click-modal=false
      :close-on-press-escape=false
      :show-close=false>
      <div class='test-desc'>
        <el-input v-model="searchKey" autocomplete="off"></el-input>
        <el-button style="font-size: 12px;padding-right: 1px;padding-top: 0px;padding-bottom: 0px;padding-left: 0px;margin-top: 3px;height: 25px;" type="text" @click="searchTestByDesc">搜索</el-button>
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
        <el-button style="font-size: 12px;padding-right: 1px;padding-top: 0px;padding-bottom: 0px;padding-left: 0px;margin-top: 3px;height: 25px;" @click="cancelRefTest">取 消</el-button>
        <el-button style="font-size: 12px;padding-right: 1px;padding-top: 0px;padding-bottom: 0px;padding-left: 0px;margin-top: 3px;height: 25px;" type="primary" @click="addRefTestStep">确 定</el-button>
      </div>
    </el-dialog>
  </el-col>
</template>

<style scoped lang="scss">
.steps {
  float: left;
  border-right: 1px solid rgba(236, 234, 234, 0.925);
  height: 100%;
  padding-left: 3px;
  .test-detail-header {
    display: flex;
    justify-content: left;
    align-items: left;
  }
}
</style>

<script>
import { mapGetters } from 'vuex'
// import stepdetail from './StepDetail.vue'
export default {
  props: ['currTest', 'testParas', 'editable'],
  name: 'steptable',
  // components: {stepdetail},
  beforeCreate: function () {
    // 循环调用组件时，组件在vue实例后创建，官方文档里写组件必须先于实例化引入，所以说组件没有正确的引入。
    // 所以这里异步引入，或者可以在main.js全局引入Vue.component('stepdetail', StepDetail)
    this.$options.components.stepdetail = () => import('./StepDetail.vue')
  },
  data () {
    return {
      onlyExpanded: [],
      selectedStep: {},
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
      this.$refs.stepstable.clearSelection()
      this.checkedStep = []
    }
  },
  methods: {
    ...mapGetters(['getTestParas', 'getSelectedTest']),
    showStepDetail (currRow, expandedRows) {
      // 重复点击时还原数据
      if (currRow.uniqueIdInTest === this.selectedStep.uniqueIdInTest) {
        this.onlyExpanded = []
        this.selectedStep = {}
        return
      }
      this.selectedStep = currRow
      // 在递归引用里面不能使用router，因为每次都会push到第一层的地址，导致没法显示子组件
      // this.$router.push({path: '/case/stepdetail'})
      // 必须放在最后，因为这个方法实际是在展开后才调用，展开时stepdetail实例还没有创建
      // 下面这句会重置展开状态
      this.onlyExpanded = [currRow.uniqueIdInTest]
    },
    isShowDetail (stepId) {
      if (stepId === this.onlyExpanded[0]) {
        return true
      }
      return false
    },
    setMultiSelect (selection, row) {
      this.checkedStep = selection
    },
    addStepAfter () {
      if (this.currTest.steps === null) {
        this.currTest.steps = []
      }
      var newStep = {}
      newStep.uniqueIdInTest = (new Date()).getTime()
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
        newStep.uniqueIdInTest = (new Date()).getTime()
        newStep.index = Object.keys(this.currTest.steps).length
        newStep.stepType = 2
        newStep.refTestId = this.toRefTest.testId
        newStep.stepDesc = this.toRefTest.testDesc
        newStep.paras = []
        newStep.resParaId = ''
        var newFormalParas = await this.API.copyFormalParas(this.toRefTest.testId, this.currTest.testId, newStep.uniqueIdInTest)
        newFormalParas.forEach(item => {
          this.testParas.push(item)
        })
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
        // 更新步骤的index
        for (var i = this.checkedStep[0].index + 1; i < this.currTest.steps.length; i++) {
          // if (this.currTest.steps[i].stepType === 2) {
          //   this.testParas.forEach(item => {
          //     if (item.refTestId !== undefined && item.refTestId !== null
          //       && item.stepId === this.currTest.steps[i].index) {
          //       item.stepId = i
          //     }
          //   })
          // }
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
    async delSteps () {
      if (this.currTest.steps === null || this.checkedStep.length === 0) {
        return
      }
      var stepIds = []
      this.checkedStep.forEach(item => {
        stepIds.push(item.uniqueIdInTest)
      })
      // 如果是引用步骤，需要删除对应的变量
      await this.API.delStepsFormalParas(this.currTest.testId, stepIds)
      // 排序勾选的步骤，从后往前删除
      this.checkedStep.sort(function (a, b) {
        return a.index - b.index
      })
      for (var i = this.checkedStep.length - 1; i >= 0; i--) {
        this.currTest.steps.splice(this.checkedStep[i].index, 1)
      }
      this.checkedStep = []
      this.$refs.stepstable.clearSelection()
      // 更新剩余步骤的index
      for (i = 0; i < this.currTest.steps.length; i++) {
        // if (this.currTest.steps[i].stepType === 2) {
        //   this.testParas.forEach(item => {
        //     if (item.refTestId !== undefined && item.refTestId !== null &&
        //       item.stepId === this.currTest.steps[i].index) {
        //       item.stepId = i
        //     }
        //   })
        // }
        this.currTest.steps[i].index = i
      }
    },
    // syncRefParaStepId (oldStepId, upOrDown) {
    //   this.testParas.forEach(item => {
    //     if (item.refTestId !== undefined && item.refTestId !== null && item.stepId === oldStepId) {
    //       item.stepId = item.stepId + upOrDown
    //     }
    //   })
    // },
    // 引用步骤的index变了，需要把对应的变量的stepId也更新掉
    moveUpSteps () {
      if (this.currTest.steps === null || this.checkedStep.length !== 1) {
        alert('只能选一行')
      } else if (this.checkedStep[0].index !== 0) {
        // 更新勾选行的参数stepId
        // if (this.checkedStep[0].stepType === 2) {
        //   this.syncRefParaStepId(this.checkedStep[0].index, -1)
        // }
        // 更新上面一行的参数stepId
        // if (this.currTest.steps[this.checkedStep[0].index - 1].stepType === 2) {
        //   this.syncRefParaStepId(this.checkedStep[0].index - 1, 1)
        // }
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
        // if (this.checkedStep[0].stepType === 2) {
        //   this.syncRefParaStepId(this.checkedStep[0].index, 1)
        // }
        // if (this.currTest.steps[this.checkedStep[0].index + 1].stepType === 2) {
        //   this.syncRefParaStepId(this.checkedStep[0].index - 1, -1)
        // }
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
      console.log(this.testParas)
      console.log(this.getTestParas())
      console.log(this.checkedStep[0])
    }
  }
}
</script>
