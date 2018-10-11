<template>
  <div class='test-info'>
    <div class='test-desc'>
      <label>用例描述:</label>
      <el-input v-model=testDesc ></el-input>
      <el-button size="mini">保存</el-button>
      <el-button size="mini">刷新</el-button>
      <el-button size="mini">新建</el-button>
      <el-button size="mini">复制</el-button>
      <el-button size="mini">预览</el-button>
    </div>
    <div class="test-detail">
      <el-col :span="6" class='steps'>
        <el-row>
          <el-button>上移</el-button>
          <el-button>下移</el-button>
          <el-button>复制</el-button>
          <el-button>添加</el-button>
          <el-button>删除</el-button>
          <el-button>引用</el-button>
        </el-row>
        <el-tree
        :data="steps"
        lazy
        :load="loadRefSteps"
        show-checkbox
        :highlight-current=true
        @node-click='clickStep'
        :props="stepTreeProp">
        </el-tree>
      </el-col>

      <el-col :span="13" class='step-editor'>
        <el-row class='test-desc'>
          <label>步骤描述:</label>
          <el-input v-model=stepDesc ></el-input>
          <el-select placeholder="操作" v-model="action">
            <el-option
              v-for="item in actions"
              :key="item.name"
              :value="item.name">
            </el-option>
          </el-select>
        </el-row>
        <router-view/>
      </el-col>

      <el-col :span="5" class='paralist'>
        <el-row>
          <el-button>添加</el-button>
          <el-button>删除</el-button>
        </el-row>
        <el-table
        :show-header=false
        :data=testVariables
        highlight-current-row
        :cell-style=stepTableCellStyle
        height="500">
          <el-table-column prop="paraName"/>
        </el-table>
      </el-col>
    </div>
  </div>
</template>

<style lang="scss">
// scoped 属性是一个布尔属性。如果使用该属性，则样式仅仅应用到 style 元素的父元素及其子元素
// https://www.jb51.net/article/129228.htm
.test-info {
  height: 100%;
}
.test-desc {
  display: flex;
  margin-left: 5px;
  justify-content: left;
  align-items: left;
  .el-input {
    .el-input__inner {
      margin-top: 3px;
      height: 25px;
    }
  }
  label {
    font-size: 13px;
    font-weight: bold;
    width: 80px;
    height: 30px;
    margin-top: 5px;
  }
}
.test-detail{
  height: 100%;
}
.steps {
  float: left;
  border-right: 1px solid rgba(236, 234, 234, 0.925);
  height: 100%;
  padding-left: 3px;
  .el-tree {
    height: 86%;
    width: 100%;
    border: 1px;
    background-color: rgb(186, 218, 247);
    overflow-x: auto;
    overflow-y: auto;
    .el-tree-node {
      min-width:100%;
      // display: inline-block !important;
      .el-checkbox {
        width: 14px;
      }
    }
  }
}
.step_editor {
  float: right;
}
.paralist {
  float: right;
  border-left: 1px solid rgba(236, 234, 234, 0.925);
  height: 100%;
  padding-left: 3px;
}
.el-input {
  height: 30px;
  .el-input__inner {
    height: 30px;
    padding: 0px;
  }
}
.el-button {
  font-size: 12px;
  padding-right: 1px;
  padding-top: 0px;
  padding-bottom: 0px;
  padding-left: 0px;
  margin-top: 3px;
  height: 25px;
}
.el-select {
  margin-top: 0px;
  .el-input--suffix {
    padding-right: 0px;
  }
  .el-input__inner {
    height: 25px;
    margin-top: 0px;
    margin-bottom: 3px;
    padding: 0px;
  }
}
.el-select-dropdown {
  font-size: 12px;
  width: 100px;
  margin: 0px;
  .el-select-dropdown__item {
    height: 25px;
  }
}
.el-popper[x-placement^=bottom] {
  margin: 3px;
}
.el-select-dropdown__empty {
  height: 25px;
  font-size: 12px;
  padding: 0px;
  width: 100px;
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
}
</style>

<script>
import { mapMutations, mapGetters } from 'vuex'

export default {
  data () {
    return {
      action: '',
      stepTreeProp: {
        label: this.getStepLabel
      }
    }
  },
  computed: {
    steps: function () {
      return this.getSelectedTest().steps
    },
    testDesc: function () {
      return this.getSelectedTest().testDesc
    },
    stepDesc: function () {
      return this.getSelectedStep().stepDesc
    },
    testVariables: function () {
      return this.getTestParas()
    },
    actions: function () {
      return this.getActions()
    }
  },
  methods: {
    ...mapMutations(['setSelectedStep', 'setUIObject']),
    ...mapGetters(['getSelectedTest', 'getSelectedStep', 'getTestParas', 'getActions']),

    stepTableCellStyle: function ({row, column, rowIndex, columnIndex}) {
      if (column.type === 'selection') {
        return {padding: '0px', margin: '0px', width: '14px'}
      } else {
        return {padding: '0px', margin: '0px', width: '100%'}
      }
    },
    getStepLabel (data, node) {
      return data.id + '-' + data.stepDesc
    },
    async loadUIObject (uiObjectId) {
      this.setUIObject(await this.API.getUIObject(uiObjectId))
    },
    async loadRefSteps (node, resolve) {
      if (node.level > 0 && node.data.stepType === 3) {
        var refTestDetail = await this.API.getTestDetail(node.data.refTestId)
        resolve(refTestDetail.steps)
      } else {
        resolve([])
      }
    },
    async clickStep (data, node, nodeComp) {
      for (var i = 0; i < this.getActions().length; i++) {
        if (data.actionId === this.getActions()[i].actionId) {
          this.action = this.getActions()[i].actionName
          break
        }
      }
      if (data.stepType === 1) {
        this.$router.push({path: '/apistep'})
      } else if (data.stepType === 2) {
        this.uiObject = this.loadUIObject(data.uiObjectId)
        this.$router.push({path: '/uistep'})
      } else if (data.stepType === 3) {
        this.$router.push({path: '/refstep'})
      }
      this.setSelectedStep(data)
    }
  }
}
</script>
