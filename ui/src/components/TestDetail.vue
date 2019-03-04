<template>
  <div class="test-info">
    <div class="test-desc">
      <label>用例描述:&nbsp;</label>
      <el-input v-model="getSelectedTest().testDesc" ></el-input>
      <el-button style="font-size: 12px;padding-right: 1px;padding-top: 0px;padding-bottom: 0px;padding-left: 0px;margin-top: 3px;height: 25px;" :disabled="isTestSelected()" size="mini">刷新</el-button>
      <el-button style="font-size: 12px;padding-right: 1px;padding-top: 0px;padding-bottom: 0px;padding-left: 0px;margin-top: 3px;height: 25px;" :disabled="isTestSelected()" size="mini" @click="addNextTest">下一个</el-button>
      <el-button style="font-size: 12px;padding-right: 1px;padding-top: 0px;padding-bottom: 0px;padding-left: 0px;margin-top: 3px;height: 25px;" :disabled="isTestSelected()" size="mini" @click="copyNextTest">复制</el-button>
      <el-button style="font-size: 12px;padding-right: 1px;padding-top: 0px;padding-bottom: 0px;padding-left: 0px;margin-top: 3px;height: 25px;" :disabled="isTestSelected()" size="mini" @click="debug">debug</el-button>
    </div>
    <div class="test-detail">
      <el-col :span="19" class="steps-wrapper">
        <steptable :currTest="getSelectedTest()" :testParas="getTestParas()" :editable="true" />
      </el-col>
      <el-col :span="5" class='paralist'>
        <el-row>
          <el-button style="font-size: 12px;padding-right: 1px;padding-top: 0px;padding-bottom: 0px;padding-left: 0px;margin-top: 3px;height: 25px;" :disabled="isTestSelected()" @click="toAddPara">添加</el-button>
          <el-button style="font-size: 12px;padding-right: 1px;padding-top: 0px;padding-bottom: 0px;padding-left: 0px;margin-top: 3px;height: 25px;" :disabled="isTestSelected()" @click="delParaFromTest">删除</el-button>
          <el-button style="font-size: 12px;padding-right: 1px;padding-top: 0px;padding-bottom: 0px;padding-left: 0px;margin-top: 3px;height: 25px;" :disabled="isTestSelected()" @click="setParaFormal">置为入参</el-button>
        </el-row>
        <el-row>
          <el-input v-model="manualFilter"  placeholder="请输入关键词"></el-input>
        </el-row>
        <el-table
          :show-header=false
          :data=testVariables
          highlight-current-row
          :cell-style=paraTableCellStyle
          @cell-dblclick="setEditable"
          @row-click="clickPara"
          @select="setMultiSelect"
          ref="parastable"
          style="height: 85%; overflow-y: auto">
          <el-table-column>
            <el-input :style="paraColor(scope.row)" slot-scope="scope" v-model=scope.row.paraName
            :disabled="!isEditable(scope.row)" />
          </el-table-column>
          <el-table-column min-width="15" type="operation">
            <el-button slot-scope="scope"
              style="font-size: 12px;padding: 3px;height: 18px;"
              size="mini" icon="el-icon-check"
              v-if="isEditable(scope.row)"
              @click="updateParaName(scope.row)"/>
          </el-table-column>
          <el-table-column
            type="selection"
            min-width="15">
          </el-table-column>
        </el-table>
        <el-dialog title="添加参数"
          :visible.sync="addParaVisible"
          :close-on-click-modal=false
          :close-on-press-escape=false
          :show-close=false>
          <div class="test-desc">
            <label>参数名:</label>
            <el-input v-model="newPara.paraName" autocomplete="off"></el-input>
          </div>
          <div slot="footer" class="dialog-footer">
            <el-button style="font-size: 12px;padding-right: 1px;padding-top: 0px;padding-bottom: 0px;padding-left: 0px;margin-top: 3px;height: 25px;" @click="cancelAddPara">取消</el-button>
            <el-button style="font-size: 12px;padding-right: 1px;padding-top: 0px;padding-bottom: 0px;padding-left: 0px;margin-top: 3px;height: 25px;" type="primary" @click="addParaToTest">确定</el-button>
          </div>
        </el-dialog>
      </el-col>
    </div>
  </div>
</template>

<style scoped lang="scss">
// scoped 属性是一个布尔属性。如果使用该属性，则样式仅仅应用到 style 元素的父元素及其子元素
// https://www.jb51.net/article/129228.htm
.test-info {
  height: 100%;
}
.steps-wrapper {
  height: 100%;
}
.test-desc {
  display: flex;
  margin-left: 5px;
  margin-right: 5px;
  justify-content: left;
  align-items: left;
  label {
    font-size: 13px;
    font-weight: bold;
    height: 30px;
    margin-top: 5px;
    text-align: right;
    white-space:nowrap;
  }
}
.test-detail{
  height: 100%;
}
.paralist {
  float: right;
  border-left: 1px solid rgba(236, 234, 234, 0.925);
  height: 100%;
  padding-left: 3px;
}
.el-input {
  height: 30px;
}
</style>

<script>
import { mapMutations, mapGetters } from 'vuex'
import steptable from './StepTable.vue'
export default {
  components: {steptable},
  data () {
    return {
      action: {},
      addParaVisible: false,
      newPara: {},
      checkedParas: [],
      manualFilter: '',
      toEditId: undefined
    }
  },
  computed: {
    testVariables: function () {
      return this.getTestParas().filter(this.notFormalFilter())
    },
    isEditable: function () {
      return function (row) {
        return row.paraId === this.toEditId
      }
    },
    paraColor: function () {
      return function (row) {
        if (row.isFormalPara === 1) {
          return 'background-color: blue;padding: 1px;'
        } else {
          return undefined
        }
      }
    }
  },
  watch: {
    testVariables: function () {
      this.toEditId = undefined
    }
  },
  methods: {
    ...mapGetters(['getSelectedTest', 'getTestParas']),
    ...mapMutations(['setIsAddNewTest', 'setActiveEditor', 'addTestParas', 'delTestPara']),

    paraTableCellStyle: function ({row, column, rowIndex, columnIndex}) {
      if (column.type === 'selection' || column.type === 'operation') {
        return {padding: '0px', margin: '0px', width: '14px'}
      } else {
        return {padding: '0px', margin: '0px', width: '100%'}
      }
    },
    notFormalFilter () {
      return (para) => {
        if (this.manualFilter === '' || para.paraName.indexOf(this.manualFilter) !== -1) {
          return (para.refTestId === undefined || para.refTestId === null)
        } else {
          return false
        }
      }
    },
    addNextTest () {
      if (this.getSelectedTest().testId !== undefined) {
        this.setIsAddNewTest(this.getSelectedTest().testId)
      }
    },
    copyNextTest () {
      if (this.getSelectedTest().testId !== undefined) {
        this.setIsAddNewTest('copy' + this.getSelectedTest().testId)
      }
    },
    isTestSelected () {
      return Object.keys(this.getSelectedTest()).length === 0
    },
    toAddPara () {
      this.addParaVisible = true
      this.newPara = {}
    },
    cancelAddPara () {
      this.addParaVisible = false
      this.newPara = {}
    },
    async addParaToTest () {
      this.addParaVisible = false
      this.newPara.testId = this.getSelectedTest().testId
      this.newPara.isFormalPara = 0
      this.newPara.dataVersion = 'default'
      this.newPara.paraName = this.newPara.paraName.replace('{', '')
      this.newPara.paraName = this.newPara.paraName.replace('}', '')
      this.newPara.paraName = '{' + this.newPara.paraName + '}'
      this.newPara = await this.API.createTestPara(this.newPara)
      // this.getTestParas().push(this.newPara)
      if (this.newPara !== undefined) {
        this.addTestParas(this.newPara)
      } else {
        this.newPara = {}
      }
    },
    setMultiSelect (selection, row) {
      this.checkedParas = selection
    },
    async delParaFromTest () {
      if (this.checkedParas.length === 0) {
        return
      }
      var delResult = await this.API.delTestParas(this.checkedParas)
      if (delResult !== undefined) {
        // 从全局变量删除变量，只能编辑原数组，否则子组件需要加watch
        for (var i = 0; i < this.getTestParas().length; i++) {
          var testPara = this.getTestParas()[i]
          this.checkedParas.forEach(delItem => {
            if (testPara.paraId === delItem.paraId) {
              this.delTestPara(i)
            }
          })
        }
        this.checkedParas = []
        this.$refs.parastable.clearSelection()
      }
    },
    setEditable (row, column, cell, event) {
      this.toEditId = row.paraId
    },
    async updateParaName (row) {
      this.toEditId = undefined
      row.paraName = row.paraName.replace('{', '')
      row.paraName = row.paraName.replace('}', '')
      row.paraName = '{' + row.paraName + '}'
      await this.API.updateParaName(row)
    },
    async setParaFormal () {
      if (this.checkedParas.length === 0) {
        return
      }
      await this.API.setFormalParas(this.checkedParas)
      this.checkedParas.forEach(element => {
        element.isFormalPara = 1
      })
    },
    clickPara (row, event, column) {
      // this.setActiveEditor(row)
    },
    debug () {
      console.log(this.getTestParas())
      console.log(this.testVariables)
    }
  }
}
</script>
