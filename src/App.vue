<template>
  <el-row class="container" id="root">
    <el-col :span="4" class="side">
      <aside :class="{showSidebar:!collapsed}">
        <el-row type="flex" justify="center">
          <el-button type="info" size="small" icon="el-icon-menu"
          @click="debug"></el-button>
          <el-input v-model="filtertext" placeholder="请输入关键词"></el-input>
        </el-row>
        <el-tree
        show-checkbox
        lazy
        :load="loadChildTests"
        :props="testTreeProp"
        :highlight-current=true
        node-key="testId"
        @node-click="clickTest">
        </el-tree>
      </aside>
    </el-col>
    <el-col :span="20" class="main">
      <el-row class="top-nav">
        <el-menu class="nav-menu" mode="horizontal" router>
          <el-menu-item index="/case">用例管理</el-menu-item>
          <el-menu-item index="/execution">用例执行</el-menu-item>
        </el-menu>
      </el-row>
      <el-row class="main-content">
        <router-view/>
      </el-row>
    </el-col>
  </el-row>
</template>

<style scoped lang="scss">
.container {
  position: absolute;
  top: 0px; /*距离容器上边缘0,仅absolute有效*/
  bottom: 0px; /*距离容器下边缘40,仅absolute有效*/
  left: 0px;
  right: 0px;
  overflow: hidden;

  .side {
    height: 100%;
    aside {
      height: 100%;
      .el-tree {
        height: 96%;
        width: 100%;
        background-color: rgb(160, 208, 253);
        border: 1px;
        overflow-x: auto;
        overflow-y: auto;
        .el-tree-node {
          min-width:100%;
          // display: inline-block !important;
        }
      }
    }
    .el-button {
      background-color: #afb2b6da;
      padding-left: 3px;
      padding-right: 3px;
      margin: 0px;
      height: 30px;
    }
  }
  .main {
    height: 100%;
    .main-content {
      height: 100%;
    }
  }
}
.el-menu {
  height: 30px;
  .el-menu-item {
    line-height: 30px;
    height: 30px;
  }
}
</style>

<script>
import { mapMutations, mapGetters } from 'vuex'
export default {
  // name可以随意定，实际不会使用，仅做为一个标示
  name: 'home',
  data () {
    return {
      filtertext: '',
      collapsed: false,
      testTreeProp: {
        label: 'testDesc'
      }
    }
  },
  methods: {
    ...mapMutations(['setSelectedTest', 'setTestParas', 'setActions']),
    ...mapGetters(['getSelectedTest', 'getActions']),

    async loadChildTests (node, resolve) {
      if (node.level === 0) {
        var res = await this.API.getChildTests('000000000000000000000000000000')
        if (res === undefined || res.length === 0) {
          resolve([{testDesc: 'demo', testId: '000000000000000000000000000000'}])
        } else {
          resolve(res)
        }
      } else {
        resolve(await this.API.getChildTests(node.data.testId))
      }
    },
    debug () {
      console.log('debug')
    },
    async loadActions () {
      if (this.getActions().length === 0) {
        this.setActions(await this.API.getActions())
      }
    },
    async loadTestParas (testId) {
      var res = await this.API.getTestParas(testId)
      this.setTestParas(res)
    },
    async clickTest (data, node) {
      this.loadActions()
      this.loadTestParas(data.testId)
      // this.loadTestDetail(data.testId)
      var res = await this.API.getTestDetail(data.testId)
      this.setSelectedTest(res)
    }
  }
}
</script>
