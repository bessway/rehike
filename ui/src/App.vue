<template>
  <el-row class="container" id="root">
    <el-col :span="4" class="side">
      <aside :class="{showSidebar:!collapsed}">
        <el-row type="flex" justify="center">
          <el-button type="info" size="small" icon="el-icon-menu"
            @click="debug">
          </el-button>
          <el-input v-model="filtertext" placeholder="请输入关键词"></el-input>
        </el-row>
        <el-row>
          <el-button class="operator" type="info" size="mini">删除</el-button>
          <el-button class="operator" type="info" size="mini" @click="addChildNode">添加</el-button>
        </el-row>
        <el-tree
          show-checkbox
          lazy
          :load="loadChildTests"
          :props="testTreeProp"
          :highlight-current=true
          node-key="testId"
          @check-change="setCheckedNodes"
          @node-click="clickTest"
          ref="testTree">
        </el-tree>
      </aside>
    </el-col>
    <el-col :span="20" class="main">
      <el-row class="top-nav">
        <el-menu class="nav-menu" mode="horizontal" router :default-active="defaultMenu">
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
  overflow-y: hidden;

  .side {
    height: 100%;
    aside {
      height: 100%;
      .el-tree {
        height: 90%;
        width: 100%;
        background-color: rgb(166, 208, 247);
        border: 1px;
        overflow: auto;
      }
    }
    .el-button {
      background-color: #afb2b6da;
      padding-left: 3px;
      padding-right: 3px;
      margin: 0px;
      height: 25px;
      width: 25px;
    }
    .operator {
      width: 30px;
      background-color: rgb(166, 208, 247);
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
      defaultMenu: this.getActiveMenu(),
      filtertext: '',
      collapsed: false,
      testTreeProp: {
        label: this.testLabel
      }
    }
  },
  computed: {
    isAddTestHere: function () {
      return this.getIsAddNewTest()
    }
  },
  watch: {
    isAddTestHere: function (selectedTestId) {
      this.goNewNode(selectedTestId)
    }
  },
  methods: {
    ...mapMutations(['setSelectedTest', 'setTestParas', 'setActions', 'setUIObjectPages', 'setCheckedTests']),
    ...mapGetters(['getSelectedTest', 'getActions', 'getUIObjPages', 'getIsAddNewTest']),

    testLabel: function (data, node) {
      return data.index + '-' + data.testDesc
    },
    getActiveMenu: function () {
      if (this.$route.path.indexOf('/case') >= 0) {
        return '/case'
      } else {
        return '/execution'
      }
    },
    async loadChildTests (node, resolve) {
      if (node.level === 0) {
        resolve(await this.API.getChildTests('000000000000000000000000000000'))
      } else {
        resolve(await this.API.getChildTests(node.data.testId))
      }
    },
    async loadActions () {
      if (this.getActions().length === 0) {
        this.setActions(await this.API.getActions())
      }
    },
    async loadTestParas (testId) {
      var res = await this.API.getTestParasAll(testId, 'default')
      this.setTestParas(res)
    },
    async clickTest (data, node) {
      var res = await this.API.getTestDetail(data.testId)
      this.setSelectedTest(res)
      this.loadActions()
      this.loadTestParas(data.testId)
      this.$router.push('/')
      this.loadUIObjectPages()
      // 这里是把node.data和selectedTest关联，
      // 从而使node.label(node.data)和testDesc(selectedTest)是同一个数据源
      node.data = this.getSelectedTest()
    },
    async loadUIObjectPages () {
      if (this.getUIObjPages().length === 0) {
        var pages = await this.API.getUIPages()
        this.setUIObjectPages(pages)
      }
    },
    async goNewNode (selectedTestId) {
      if (this.$refs.testTree.getCurrentNode() === null) {
        return
      }
      var parentId = this.$refs.testTree.getCurrentNode().parentId
      var newTest = {}
      if (selectedTestId.substring(0, 4) === 'copy') {
        newTest = await this.API.copyTest([{'testId': selectedTestId}])
      } else {
        newTest = await this.API.createTest({'parentId': parentId})
      }
      if (parentId === '000000000000000000000000000000') {
        this.$router.go(0)
      } else {
        this.$refs.testTree.insertAfter(newTest, this.$refs.testTree.getCurrentNode())
        this.$refs.testTree.setCurrentKey(newTest.testId)
        this.setSelectedTest(newTest)
      }
    },
    async addChildNode () {
      var parentId = '000000000000000000000000000000'
      if (this.$refs.testTree.getCurrentNode() !== null) {
        parentId = this.$refs.testTree.getCurrentNode().testId
      }
      var newTest = await this.API.createTest({'parentId': parentId})
      if (parentId === '000000000000000000000000000000') {
        this.$router.go(0)
      } else {
        this.$refs.testTree.append(newTest, this.$refs.testTree.getCurrentNode())
      }
    },
    setCheckedNodes () {
      this.setCheckedTests(this.$refs.testTree.getCheckedNodes())
    },
    debug () {
      console.log(this.getSelectedTest())
    }
  }
}
</script>
