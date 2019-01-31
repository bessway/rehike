<template>
  <div class='test-info'>
    <div class='test-desc'>
      <label>用例描述:</label>
      <el-input v-model="getSelectedTest().testDesc" ></el-input>
      <el-button size="mini" @click="debug">debug</el-button>
      <el-button size="mini">刷新</el-button>
      <el-button size="mini">新建</el-button>
      <el-button size="mini">复制</el-button>
      <el-button size="mini">预览</el-button>
    </div>
    <div class="test-detail">
      <el-col :span="19" class='steps-wrapper'>
        <steptable :selectedTest="getSelectedTest()" :editable="true"/>
      </el-col>
      <el-col :span="5" class='paralist'>
        <el-row>
          <el-button>添加</el-button>
          <el-button>删除</el-button>
          <el-button>设置为入参</el-button>
        </el-row>
        <el-table
          :show-header=false
          :data=testVariables
          highlight-current-row
          :cell-style=paraTableCellStyle
          style="height: 85%; overflow-y: auto">
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
.steps-wrapper {
  height: 100%
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
.paralist {
  float: right;
  border-left: 1px solid rgba(236, 234, 234, 0.925);
  height: 100%;
  padding-left: 3px;
  .el-button {
    margin-right: 3px;
  }
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
.el-button+.el-button {
  margin-left: 3px;
}
.el-table {
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
// 因为App组件限制了scoped，只能在这里改tree的样式
.el-tree-node__children {
  overflow: visible !important;
}
</style>

<script>
import { mapGetters } from 'vuex'
import steptable from './StepTable.vue'
export default {
  components: {steptable},
  data () {
    return {
      action: {}
    }
  },
  computed: {
    testVariables: function () {
      return this.getTestParas()
    }
  },
  methods: {
    ...mapGetters(['getSelectedTest', 'getTestParas']),

    paraTableCellStyle: function ({row, column, rowIndex, columnIndex}) {
      if (column.type === 'selection') {
        return {padding: '0px', margin: '0px', width: '14px'}
      } else {
        return {padding: '0px', margin: '0px', width: '100%'}
      }
    },
    debug () {
    }
  }
}
</script>
