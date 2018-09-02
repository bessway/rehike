<template>
  <div class='test-info'>
    <div class='test-desc'>
      <label>用例描述:</label>
      <el-input v-model=caseDesc ></el-input>
      <el-button size="mini">保存</el-button>
      <el-button size="mini">刷新</el-button>
      <el-button size="mini">新建</el-button>
      <el-button size="mini">复制</el-button>
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
        <el-tree :data=steps show-checkbox
        @node-click='nodeClick'>
        </el-tree>
      </el-col>

      <el-col :span="13" class='step-editor'>
        <el-row class='test-desc'>
          <label>步骤描述:</label>
          <el-input/>
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
        <el-table :show-header=false :data=variables  highlight-current-row
        :cell-style=stepTableCellStyle>
          <el-table-column prop="name"/>
        </el-table>
      </el-col>
    </div>
  </div>
</template>
<script>

export default {
  data () {
    return {
      caseDesc: '',
      steps: [{label: 'this is the first step example'}, {label: 'second'}],
      variables: [{name: '%%a%%'}, {name: '%%b%%'}],
      actions: [{name: '1'}, {name: '2'}],
      action: '',
      count: 0
    }
  },
  computed: {
  },
  methods: {
    stepTableCellStyle: function ({row, column, rowIndex, columnIndex}) {
      if (column.type === 'selection') {
        return {padding: '0px', margin: '0px', width: '14px'}
      } else {
        return {padding: '0px', margin: '0px', width: '100%'}
      }
    },
    nodeClick: function () {
      if (this.count % 2 === 0) {
        this.$router.push({path: '/apistep'})
      } else {
        this.$router.push({path: '/uistep'})
      }
      this.count = this.count + 1
    }
  }
}
</script>

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
      overflow-x: auto;
      overflow-y: auto;
      .el-tree-node{
        min-width:100%;
        display: inline-block !important;
        .el-checkbox{
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
label {
  font-size: 13px;
  font-weight: bold;
  width: 80px;
  height: 30px;
  margin-top: 5px;
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
  padding-right: 3px;
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
