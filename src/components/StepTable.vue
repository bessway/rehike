<template>
  <el-col :span=24 class='steps'>
    <el-row v-if="editable">
      <el-button>上移</el-button>
      <el-button>下移</el-button>
      <el-button>复制</el-button>
      <el-button>添加</el-button>
      <el-button>删除</el-button>
      <el-button>引用</el-button>
      <el-button @click="debug">保存</el-button>
    </el-row>
    <el-table
      :show-header=false
      :data="selectedTest.steps"
      style="height: 85%; overflow-y: auto"
      @expand-change="showStepDetail"
      row-key="id"
      :expand-row-keys="onlyExpanded">
      <el-table-column
        type="index"
        width="25">
      </el-table-column>
      <el-table-column
        type="expand"
        width="15">
        <stepdetail
          :action="action"
          :paras="paras"
          :uiobject="uiobject"
          :refTest="refTest"
          :step="selectedStep"
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
    padding-right: 0px;
    padding-top: 0px;
    padding-bottom: 10px;
  }
}
</style>

<script>
import { mapGetters } from 'vuex'
import { Message } from 'element-ui'

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
      action: {actionId: '', actionName: '', hasUIObject: 1, actionType: 1},
      paras: {p1: {paraName: '', paraId: ''}, p2: {paraName: '', paraId: ''}, p3: {paraName: '', paraId: ''}, p4: {paraName: '', paraId: ''}, p5: {paraName: '', paraId: ''}, response: {paraName: '', paraId: ''}},
      selectedStep: {},
      uiobject: {uiObjectId: '', uiObjectPage: '', uiObjectType: '', uiObjectName: '', uiObjectPath: ''},
      refTest: {}
    }
  },
  methods: {
    ...mapGetters(['getActions', 'getTestParas']),
    hideBbutton () {
      if (this.editable) {
        return 'display:none'
      }
    },
    findAcion (currRow) {
      for (var i = 0; i < this.getActions().length; i++) {
        // if (currRow.actionId === this.getActions()[i].actionId) {
        if (currRow.actionId.indexOf(this.getActions()[i].actionId.substring(0, 1)) >= 0) {
          console.log(this.getActions()[i])
          this.action = this.getActions()[i]
          return
        }
      }
      Message.error({message: '找不到' + currRow.actionId + '对应的操作!'})
      this.action = {actionId: '', actionName: '', hasUIObject: 1, actionType: 1}
    },
    findParas (currRow) {
      var paras = {p1: {}, p2: {}, p3: {}, p4: {}, p5: {}, response: {}}
      var temp = {}
      var ids = currRow.stepParaIds.slice(0)
      ids.push(currRow.resParaId)
      ids.forEach((paraId, index) => {
        for (var i = 0; i < this.getTestParas().length; i++) {
          // if (paraId === this.getTestParas()[i].paraId) {
          if (paraId.indexOf(this.getTestParas()[i].paraId.substring(0, 1)) >= 0) {
            temp = this.getTestParas()[i]
            break
          }
        }
        if (temp.length === 0) {
          temp = {paraName: '', paraId: ''}
          Message.error({message: '找不到' + paraId + '对应的参数!'})
        }
        if (index === ids.length - 1) {
          paras['response'] = temp
        } else {
          paras['p' + (index + 1)] = temp
        }
      })
      console.log(paras)
      this.paras = paras
    },
    async showStepDetail (currRow, expandedRows) {
      console.log(currRow.id + ' ' + this.selectedStep.id)
      if (currRow.id === this.selectedStep.id) {
        this.selectedStep = {}
        return
      }
      this.selectedStep = currRow
      console.log(this.selectedStep)
      this.findAcion(currRow)
      this.findParas(currRow)
      await this.loadUIObject()
      await this.loadRefTest()
      // 在递归引用里面不能使用router，因为每次都会push到第一层的地址，导致没法显示子组件
      // this.$router.push({path: '/case/stepdetail'})
      // 必须放在最后，因为这个方法实际是在展开后才调用，展开时stepdetail实例还没有创建
      // 下面这句会重置展开状态
      this.onlyExpanded = [currRow.id]
    },
    async loadUIObject () {
      if (this.action.hasUIObject === 1) {
        var res = await this.API.getUIObject(this.selectedStep.uiObjectId)
        console.log(res)
        this.uiobject = res
      } else {
        this.uiobject = {uiObjectId: '', uiObjectPage: '', uiObjectType: '', uiObjectName: '', uiObjectPath: ''}
      }
    },
    async loadRefTest () {
      var refTestDetail = await this.API.getTestDetail(this.selectedStep.refTestId)
      this.refTest = refTestDetail
      console.log(this.refTest)
    },
    debug () {
      console.log('click')
    }
  }
}
</script>
