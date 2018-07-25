<template>
<el-container style="border: 1px solid #eee">
  <el-aside width=250px style="background-color: rgb(238, 241, 246);color: #333 ">
    <el-input
      placeholder="仅过滤已加载的节点"
      v-model="filterText" height="40px">
    </el-input>
    <el-tree style="background-color: #b3c0d1;height:560px"
      class="filter-tree"
      node-key="refId"
      :load="loadTreeNode"
      lazy
      show-checkbox
      draggable
      :allow-drop="allowDrop"
      :allow-drag="allowDrag"
      :highlight-current=true
      @node-drop="handleDrop"
      @node-click="handleClickNode"
      @node-contextmenu="handleRightClick"
      :filter-node-method="filterNode"
      ref="casetree">
    </el-tree>
    <el-dialog
      title="操作"
      :visible.sync=actionDialogVisible
      width="30%"
      @close-on-press-escape=false
      :before-close="closeActionDialog"
      center>
      <el-tooltip content="复制所有勾选的用例到右键点击的节点" placement="top">
        <el-button @click="handleCopyClick">复制到该节点</el-button>
      </el-tooltip>
    </el-dialog>
  </el-aside>

  <el-container>
    <el-main style="padding-top:5px">
      <el-tabs @tab-click="handleClickTab" style="margin-top:0px;" value="execute">
        <el-tab-pane label="用例执行" name="execute">
          <el-select v-model=selectedOs filterable placeholder="操作系统">
              <el-option
                v-for="item in getOs"
                :key="item.value"
                :label="item.label"
                :value="item.value">
              </el-option>
          </el-select>
          <el-select v-model=selectedBrowser filterable placeholder="浏览器">
              <el-option
                v-for="item in getBrowser"
                :key="item.value"
                :label="item.label"
                :value="item.value">
              </el-option>
          </el-select>
          <el-select v-model=selectedAgent filterable placeholder="任务名">
              <el-option
                v-for="item in getAgents"
                :key="item.value"
                :label="item.label"
                :value="item.value"
                :disabled="item.status">
              </el-option>
          </el-select>
          <el-tooltip content="执行所有勾选的用例" placement="top">
            <el-button type="primary"
              @click="handleRunClick">运行</el-button>
          </el-tooltip>
          <el-button type="primary"
          @click="debug">debug</el-button>
          <el-tooltip content="查看所选任务的执行报告" placement="top">
            <el-button type="primary"
              @click="getJobDetail" style="float:right">查看详情</el-button>
          </el-tooltip>
          <el-tooltip content="刷新所有任务的状态" placement="top">
            <el-button type="primary"
              @click="handleRefreshAgent" style="float:right">刷新</el-button>
          </el-tooltip>
          
          <el-table style="width: 100%;padding-top:10px" stripe height="480"
          :data="executions"
          :default-sort = "{prop: 'createTime', order: 'descending'}"
          highlight-current-row
          ref="executionTable"
          :row-key="getExecRowKeys"
          :row-style = "{'text-align':'center'}"
          @row-click="handleExecRowClick">
            <el-table-column label="任务名" min-width="100" header-align="center" prop="jobName"></el-table-column>
            <el-table-column label="任务编号" min-width="100" header-align="center" prop="buildId"></el-table-column>
            <el-table-column label="创建时间" min-width="100" header-align="center" sortable prop="createTime"></el-table-column>
            <el-table-column label="状态" min-width="100" header-align="center" prop="buildStatus"></el-table-column>
            <el-table-column label="成功用例数量" min-width="100" header-align="center" prop="passed"></el-table-column>
            <el-table-column label="失败用例数量" min-width="100" header-align="center" prop="failed"></el-table-column>
          </el-table>
        </el-tab-pane>
        <el-tab-pane label="用例管理" name="case">
          <div>
            <b><font size="4">正在查看 {{selectedNodeType}} </font></b>
            <el-tooltip content="在选中的节点内添加一个节点" placement="top">
              <el-button size="mini" v-show="isShowAddBtn" icon="el-icon-plus" circle
                @click="handleAddNodeClick"></el-button>
            </el-tooltip>
            <el-tooltip content="删除选中的节点" placement="top">
              <el-button size="mini" v-show="isShowDelBtn" icon="el-icon-minus" circle
                @click="handleDelNodeClick"></el-button>
            </el-tooltip>
            <el-tooltip content="保存选中节点的信息" placement="top">
              <el-button size="mini" v-show="isShowSaveBtn" icon="el-icon-check" circle
                @click="handleSaveClick"></el-button>
            </el-tooltip>
            <el-button size="mini" icon="el-icon-question"
            @click="debug"></el-button>
            <el-tooltip content="向上移动选中的步骤" placement="top">
              <el-button style="float:right;padding:5px" size="mini" icon="el-icon-arrow-up"
                @click="handleStepUpClick"></el-button>
            </el-tooltip>
            <el-tooltip content="向下移动选中的步骤" placement="top">
              <el-button style="float:right;padding:5px" size="mini" icon="el-icon-arrow-down"
                @click="handleStepDownClick"></el-button>
            </el-tooltip>
            <el-tooltip content="放弃当前的改动，重新获取选中节点信息" placement="top">
              <el-button style="float:right;padding:5px" size="mini" icon="el-icon-refresh"
                @click="handleRefreshClick"></el-button>
            </el-tooltip>
            <el-input type="textarea" resize="none" autosize float="left" 
              v-model="caselabel">
            </el-input>
          </div>
          <el-table style="width: 100%;" stripe height="480"
          :data="showingCaseDetail.steps"
          :default-sort = "{prop: 'id', order: 'ascending'}"
          highlight-current-row
          ref="caseTable"
          :row-key="getRowKeys"
          fixed
          @row-click="handleRowClick">
            <el-table-column label="" prop="id" width="30">
            </el-table-column>
			<el-table-column label="描述" min-width="100" header-align="center">
              <template slot-scope="scope">
                <el-input type="textarea" resize="none" autosize min-height="40px" style="padding-top:5px"
                  v-model=scope.row.desc>
                </el-input>
              </template>
            </el-table-column>
            <el-table-column label="操作" min-width="140" header-align="center">
              <template slot-scope="scope">
                <el-select v-model=scope.row.action filterable placeholder="请选择"
                @change="handleActionChange(scope.row)">
                  <el-option
                    v-for="item in actions"
                    :key="item.value"
                    :label="item.name"
                    :value="item.value">
                  </el-option>
                </el-select>
              </template>
            </el-table-column>
            <el-table-column label="页面" min-width="100" header-align="center">
              <template slot-scope="scope">
                <el-select filterable placeholder="输入/选择"
                  v-model=scope.row.page allow-create clearable
                  v-bind:disabled="isDisableSelect(scope.row,scope.column)"
                  @change="handlePageChange(scope.row)">
                  <el-option
                    v-for="item in getPages"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value">
                  </el-option>
                </el-select>
              </template>
            </el-table-column>
            <el-table-column label="对象类型" min-width="100" header-align="center">
              <template slot-scope="scope">
                <el-select filterable placeholder="输入/选择"
                  v-model=scope.row.type allow-create clearable
                  v-bind:disabled="isDisableSelect(scope.row,scope.column)"
                  @change="handleTypeChange(scope.row)">
                  <el-option
                    v-for="item in getTypesOnPage(scope.row.page)"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value">
                  </el-option>
                </el-select>
              </template>
            </el-table-column>
            <el-table-column label="对象名" min-width="100" header-align="center">
              <template slot-scope="scope">
                <el-select filterable placeholder="输入/选择"
                  v-model=scope.row.name allow-create clearable
                  v-bind:disabled="isDisableSelect(scope.row,scope.column)"
                  @change="handleNameChange(scope.row)">
                  <el-option
                    v-for="item in getPathNames(scope.row.page,scope.row.type)"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value">
                  </el-option>
                </el-select>
              </template>
            </el-table-column>
            <el-table-column label="xpath" min-width="140" header-align="center">
              <template slot-scope="scope">
                <el-select filterable placeholder="输入/选择"
                  v-model=scope.row.path allow-create clearable
                  v-bind:disabled="isDisableSelect(scope.row,scope.column)"
                  @change="handlePathChange(scope.row)">
                  <el-option
                    v-for="item in getPaths(scope.row.page,scope.row.type)"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value">
                  </el-option>
                </el-select>
              </template>
            </el-table-column>
            <el-table-column label="返回值" min-width="140" header-align="center">
              <template slot-scope="scope">
                <el-select filterable v-bind:placeholder="getPlaceHolder(scope.row,scope.column)"
                  v-model=scope.row.response allow-create clearable
                  v-bind:disabled="isDisableSelect(scope.row,scope.column)"
                  @change="validateResChange(scope.row)">
                  <el-option
                    v-for="item in globalParas"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value">
                  </el-option>
                </el-select>
              </template>
            </el-table-column>
            <el-table-column label="参数1" min-width="140" header-align="center">
              <template slot-scope="scope">
                <el-select filterable v-bind:placeholder="getPlaceHolder(scope.row,scope.column)"
                  v-model=scope.row.paras[0] allow-create clearable
                  v-bind:disabled="isDisableSelect(scope.row,scope.column)">
                  <el-option
                    v-for="item in globalParas"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value">
                  </el-option>
                </el-select>
              </template>
            </el-table-column>
            <el-table-column label="参数2" min-width="140" header-align="center">
              <template slot-scope="scope">
                <el-select filterable v-bind:placeholder="getPlaceHolder(scope.row,scope.column)"
                  v-model=scope.row.paras[1] allow-create clearable
                  v-bind:disabled="isDisableSelect(scope.row,scope.column)">
                  <el-option
                    v-for="item in globalParas"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value">
                  </el-option>
                </el-select>
              </template>
            </el-table-column>
            <el-table-column label="参数3" min-width="140" header-align="center">
              <template slot-scope="scope">
                <el-select filterable v-bind:placeholder="getPlaceHolder(scope.row,scope.column)"
                  v-model=scope.row.paras[2] allow-create clearable
                  v-bind:disabled="isDisableSelect(scope.row,scope.column)">
                  <el-option
                    v-for="item in globalParas"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value">
                  </el-option>
                </el-select>
              </template>
            </el-table-column>
            <el-table-column label="参数4" min-width="150" header-align="center">
              <template slot-scope="scope">
                <el-select filterable v-bind:placeholder="getPlaceHolder(scope.row,scope.column)"
                  v-model=scope.row.paras[3] allow-create clearable
                  v-bind:disabled="isDisableSelect(scope.row,scope.column)">
                  <el-option
                    v-for="item in globalParas"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value">
                  </el-option>
                </el-select>
              </template>
            </el-table-column>
            <el-table-column label="" min-width="30px" fixed="right">
              <template slot-scope="scope">
                <el-tooltip content="下方添加一行" placement="left">
                  <el-button size="mini" icon="el-icon-circle-plus-outline"
                    @click="handleAddStepClick(scope.row)"></el-button>
                </el-tooltip>
                <el-tooltip content="删除当前行" placement="left">
                  <el-button size="mini" icon="el-icon-remove-outline"
                    @click="handleDelStepClick(scope.row)"></el-button>
                </el-tooltip>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
        <!--el-tab-pane label="参数管理" name="parameter">参数管理</el-tab-pane>
        <el-tab-pane label="对象管理" name="object">对象管理</el-tab-pane>
        <el-tab-pane label="数据管理" name="data">数据管理</el-tab-pane>
        <el-tab-pane-- label="节点管理" name="node">节点管理</el-tab-pane-->

      </el-tabs>
    </el-main>
  </el-container>
</el-container>
</template>

<style>
@import "../../css/Home/home.css";
</style>

<script>
import HomeData from "../../controller/Home/home.js";
const defaultTableData =
  '{"steps": [{"id": 0,"action":"","name":"","page":"","paras":["","","",""],"path":"","response":"","type":"","desc":""}]}';
const emptyTableData = '{"steps": []}';
const emptyNode = '{"label":"","refId":""}';
var paraCount = 4;
var haveCachedDataChange=false;
export default {
  data() {
    return {
      filterText: "",
      props: {
        label: "label"
      },
      selectedNode: null,
      caselabel: null,
      selectedRow: null,
      actions: null,
      strctureObjects: null,
      globalParas: null,
      showingCaseDetail: JSON.parse(emptyTableData),
      agents: null,
      selectedAgent: null,
      selectedBrowser: null,
      selectedOs: null,
      executions: null,
      selectedExecRowData: null,
      targetNode: null
    };
  },
  mounted:function(){
    this.loadAgents();
    this.loadExecutions();
  },
  //如果函数的依赖有变化，则按逻辑同步更新computed属性
  //即selectedNode有变化，则执行函数更新isShowAddBtn
  computed: {
    //根据选择的node显示按钮
    isShowAddBtn: function() {
      if (this.selectedNode === null) {
        return 0;
      } else {
        return this.selectedNode.level !== 4 && this.selectedNode.level !== 0;
      }
    },
    //根据选择的node显示按钮
    isShowDelBtn: function() {
      if (this.selectedNode === null) {
        return 0;
      } else {
        return this.selectedNode.level !== 0;
      }
    },
    //根据选择的node显示按钮
    isShowSaveBtn: function() {
      if (this.selectedNode === null) {
        return 0;
      } else {
        return this.selectedNode.level > 0;
      }
    },
    //根据选择的node显示tag
    selectedNodeType: function() {
      if (this.selectedNode === null) {
        return "无";
      }
      if (this.selectedNode.level === 1) {
        return "项目：";
      } else if (this.selectedNode.level === 2) {
        return "模块：";
      } else if (this.selectedNode.level === 3) {
        return "用例集：";
      } else if (this.selectedNode.level === 4) {
        return "用例：";
      } else {
        return "无";
      }
    },
    //根据选择的node显示lebel
    selectedLabel: function() {
      if (this.selectedNode === null) {
        return "无";
      } else {
        return this.selectedNode.label;
      }
    },
    //获取所有对象的page list
    getPages: function() {
      if (this.strctureObjects == null || this.strctureObjects.length == 0) {
        return [];
      }
      var pages = new Array();
      for (var item in this.strctureObjects) {
        pages.push({ label: item, value: item });
      }
      return pages;
    },
    getBrowser: function() {
      if (this.agents == null) {
        return [];
      }
      var browsers = {};
      for (var i = 0; i < this.agents.length; i++) {
        browsers[
          this.agents[i].browserType + "_" + this.agents[i].browserVersion
        ] =
          "";
      }
      var result = [];
      for (var item in browsers) {
        result.push({ label: item, value: item });
      }
      return result;
    },
    getOs: function() {
      if (this.agents == null) {
        return [];
      }
      var os = {};
      for (var i = 0; i < this.agents.length; i++) {
        os[this.agents[i].osType + "_" + this.agents[i].osVersion] = "";
      }
      var result = [];
      for (var item in os) {
        result.push({ label: item, value: item });
      }
      return result;
    },
    getAgents() {
      if (this.agents == null) {
        return [];
      }
      if (this.selectedBrowser == null || this.selectedOs == null) {
        return [];
      }
      var agents = [];
      for (var i = 0; i < this.agents.length; i++) {
        if (
          this.agents[i].browserType + "_" + this.agents[i].browserVersion ===
            this.selectedBrowser &&
          this.agents[i].osType + "_" + this.agents[i].osVersion ===
            this.selectedOs
        ) {
          agents.push({
            label: this.agents[i].jobName,
            value: this.agents[i].jobName,
            status: !this.agents[i].status
          });
        }
      }
      return agents;
    },
    actionDialogVisible() {
      if (this.targetNode != null) {
        return true;
      } else {
        return false;
      }
    }
  },
  //watch是指watch的属性例如filterText发生变化，则执行函数
  //这里之所以能触发是因为在input中，v-model会监控value属性，所以只要有输入就会触发
  watch: {
    filterText(val) {
      this.$refs.casetree.filter(val);
    },
    caselabel(val) {
      if (this.$refs.casetree.currentNode != null) {
        this.$refs.casetree.currentNode.node.data.label = val;
      }
    }
  },

  methods: {
    //设置table的key，用于排序
    getRowKeys(row) {
      return row.id;
    },
    //设置table的key，用于排序
    getExecRowKeys(row) {
      return row.createTime;
    },
    //获取所选page中所有对象的类型
    getTypesOnPage: function(page) {
      if (
        this.strctureObjects == null ||
        this.strctureObjects.length == 0 ||
        page == null ||
        page == ""
      ) {
        return [];
      }
      if (this.strctureObjects[page] == undefined) {
        return [];
      }
      var types = new Array();
      for (var item in this.strctureObjects[page]) {
        types.push({ label: item, value: item });
      }
      return types;
    },
    //获取所选page中某个类型的所有对象的名称
    getPathNames: function(page, type) {
      if (
        page == null ||
        page == "" ||
        type == null ||
        type == "" ||
        this.strctureObjects == null ||
        this.strctureObjects.length == 0
      ) {
        return [];
      }
      if (
        this.strctureObjects[page] == undefined ||
        this.strctureObjects[page][type] == undefined
      ) {
        return [];
      }
      var names = new Array();
      for (var item in this.strctureObjects[page][type]) {
        names.push({ label: item, value: item });
      }
      return names;
    },
    //获取所选page中某个类型的所有对象的xpath
    getPaths: function(page, type) {
      if (
        page == null ||
        page == "" ||
        type == null ||
        type == "" ||
        this.strctureObjects == null ||
        this.strctureObjects.length == 0
      ) {
        return [];
      }
      if (
        this.strctureObjects[page] == undefined ||
        this.strctureObjects[page][type] == undefined
      ) {
        return [];
      }
      var paths = new Array();
      for (var item in this.strctureObjects[page][type]) {
        paths.push({
          label: this.strctureObjects[page][type][item],
          value: this.strctureObjects[page][type][item]
        });
      }
      return paths;
    },
    //根据选择的action判断是否禁用参数字段
    isDisableSelect(row, col) {
      if (this.actions === null || this.actions.length === 0) {
        return false;
      }
      for (var i = 0; i < this.actions.length; i++) {
        var result = false;
        if (this.actions[i].value === row.action) {
          if (col.label.indexOf("参数") != -1) {
            var colIndex = col.label.substring(2, 3);
            if (colIndex > this.actions[i].paras.length) {
              result = true;
              row.paras[colIndex - 1] = null;
            } else {
              result = false;
            }
          } else if (
            col.label === "页面" ||
            col.label === "对象" ||
            col.label === "对象类型" ||
            col.label === "对象名" ||
            col.label === "xpath"
          ) {
            result = !this.actions[i].target;
            if (result) {
              row.page = "";
              row.type = "";
              row.name = "";
              row.path = "";
            }
          } else if (col.label === "返回值") {
            result = !this.actions[i].response;
          }
          return result;
        }
      }
    },
    //根据选择的action显示每个参数的提示
    getPlaceHolder(row, col) {
      if (this.actions === null || this.actions.length === 0) {
        return "";
      }
      if (col.label == "返回值") {
        return "变量名";
      }
      for (var i = 0; i < this.actions.length; i++) {
        if (this.actions[i].value === row.action) {
          if (col.label.indexOf("参数") != -1) {
            var colIndex = col.label.substring(2, 3);
            if (colIndex > this.actions[i].paras.length) {
              return "";
            } else {
              return this.actions[i].paras[colIndex - 1].pDesc;
            }
          }
        }
      }
      return "";
    },
    //选择tab后加载action list，object list，global parameter list etc
    handleClickTab(tab) {
      if (tab.name === "case") {
        this.loadActions();
        this.loadObjects();
        this.loadGlobalParas();
      } else if (tab.name === "execute") {
        this.loadAgents();
        this.loadExecutions();
      }
      this.selectedRow = null;
      this.$refs.caseTable.setCurrentRow();
      this.selectedExecRowData=null;
      this.$refs.executionTable.setCurrentRow();
    },
    //选择node后记载详细信息
    handleClickNode(data, node) {
      if(haveCachedDataChange){
        alert("有未保存的修改，请先保存或者丢弃");
        this.$refs.casetree.setCurrentNode(this.selectedNode.data);
        
        return;
      }
      this.selectedNode = node;
      this.loadCaseDetail();
      this.selectedRow = null;
      this.caselabel = node.label;
      if(haveCachedDataChange){
        this.strctureObjects=null;
        this.loadObjects();
      }
      this.globalParas=null;
      this.loadGlobalParas();
      haveCachedDataChange=false;
    },
    handleRightClick(ev, data, node, component) {
      this.targetNode = node;
    },
    handleCopyClick() {
      if (this.targetNode == null||this.targetNode.level!=3) {
        alert("需要右键点击目标节点，仅支持复制用例到上一层");
        this.targetNode = null;
      }
      var copiedCases=this.getCases(this.$refs.casetree.getCheckedKeys());
      if(this.targetNode != null&&copiedCases.length==0){
        alert("需要勾选要复制的用例，仅支持复制用例");
        this.targetNode = null;
      }
      if(this.targetNode != null){
        HomeData.copyNodes(
          this.targetNode.data.refId,
          copiedCases,
          function(response) {
            for(var i=0;i<response.length;i++){
              this.$refs.casetree.append(
                response[i],
                this.targetNode
              );
            }
            this.targetNode = null;
          }.bind(this),function(msg){
            this.popup(msg);
            this.targetNode = null;
          }.bind(this)
        );
      }
    },
    //传入勾选的节点，找出case
    getCases(nodes){
      var result=new Array();
      for(var i=0;i<nodes.length;i++){
        var node=this.$refs.casetree.getNode(nodes[i]);
        if(node.level==4){
          result.push(node.data);
        }
      }
      return result;
    },
/*    getTopNode(node){
      //如果没有父节点，则返回本节点
      if(node.parent==undefined||node.parent==null){
        return node;
      }else{
        //如果有父节点但是没有选中，返回本节点
        if(!node.parent.checked){
          return node;
        }else{
          //如果已选中，继续向上查
          return getTopNode(node.parent);
        }
      }
    },*/
    closeActionDialog() {
      this.targetNode = null;
    },
    //tree关键字搜索
    filterNode(value, data) {
      if (!value) return true;
      return data.label.indexOf(value) !== -1;
    },
    //获取action list
    loadActions() {
      if (this.actions !== null) {
        return;
      }
      HomeData.getActions(
        function(response) {
          for (var i = 0; i < response.length; i++) {
            response[i].value = response[i].name;
            response[i].label = response[i].name;
          }
          this.actions = response;
        }.bind(this),
        this.popup.bind(this)
      );
    },
    //获取object list
    loadObjects() {
      if (this.strctureObjects !== null) {
        return;
      }
      HomeData.getObjects(
        function(response) {
          var result = {};
          for (var obj in response) {
            var target = obj.split(".");
            this.addToStrctureObject(result,target[0],target[1],target[2],response[obj]);
          }
          this.strctureObjects = result;
        }.bind(this),
        this.popup.bind(this)
      );
    },
    addToStrctureObject(obj,page,type,name,path){
      var tmp=obj;
      if (tmp[page] == undefined) {
        tmp[page] = {};
      }
      tmp = tmp[page];
      if (tmp[type] == undefined) {
        tmp[type] = {};
      }
      tmp = tmp[type];

      tmp[name] = path;
    },
    //获取global parameter list
    loadGlobalParas() {
      if (this.globalParas !== null) {
        return;
      }
      HomeData.getGlobalParas(
        function(response) {
          if (this.globalParas === null) {
            this.globalParas = new Array();
          }
          for (var i = 0; i < response.length; i++) {
            let tmp = {};
            tmp.label = response[i];
            tmp.value = response[i];

            this.globalParas.push(tmp);
          }
        }.bind(this),
        this.popup.bind(this)
      );
    },
    //获取case detail
    loadCaseDetail() {
      if (this.selectedNode.level !== 4) {
        this.showingCaseDetail = JSON.parse(emptyTableData);
        return;
      }
      //已经加载过的不再重新加载
/*      if (
        this.selectedNode.data.caseDetail != undefined &&
        this.selectedNode.data.caseDetail != null &&
        JSON.stringify(this.selectedNode.data.caseDetail) !== defaultTableData
      ) {
        this.showingCaseDetail = this.selectedNode.data.caseDetail;
        return;
      }*/
      HomeData.getCaseDetail(
        this.selectedNode.data.refId,
        function(response) {
          this.showingCaseDetail = response;
          if (JSON.stringify(this.showingCaseDetail) === "{}") {
            this.showingCaseDetail = JSON.parse(defaultTableData);
          }
          for (var i = 0; i < this.showingCaseDetail.steps.length; i++) {
            this.showingCaseDetail.steps[i].paras = this.appendBlank(
              this.showingCaseDetail.steps[i].paras
            );
          }
          this.selectedNode.data.caseDetail = this.showingCaseDetail;
        }.bind(this),
        this.popup.bind(this)
      );
    },
    //casedetail禁用的参数字段置空
    appendBlank(originArray) {
      var empty = new Array(paraCount);
      for (var item in empty) {
        item = "";
      }
      if (originArray == null || originArray == undefined) {
        originArray = new Array();
      }
      return originArray.concat(empty).slice(0, 4);
    },
    //获取下一级node
    loadTreeNode(node, resolve) {
      var data;
      if (node.level === 0) {
        HomeData.getProjects(resolve, this.popup.bind(this));
      } else {
        HomeData.getNodes(node.data.refId, resolve, this.popup.bind(this));
      }
    },
    loadAgents() {
      HomeData.getAgents(
        function(response) {
          this.agents = response;
        }.bind(this),
        this.popup.bind(this)
      );
    },
    loadExecutions() {
      HomeData.getExecutions(
        function(response) {
          this.executions = response;
        }.bind(this),
        this.popup.bind(this)
      );
    },
    //判断node是否可以拖动
    allowDrag(draggingNode) {
      return draggingNode.level > 1;
    },
    //判断node是否可以放到目标node下
    allowDrop(draggingNode, dropNode) {
      if (draggingNode.level - dropNode.level !== 1) {
        return false;
      } else {
        return true;
      }
    },
    //移动node
    handleDrop(draggingNode, dropNode, dropType, ev) {
      HomeData.moveNode(
        draggingNode.data.refId,
        dropNode.data.refId,
        function(response) {
          draggingNode.data.parentId = response.parentId;
        },
        this.popup.bind(this)
      );
    },

    //添加step
    handleAddStepClick(row) {
      for (var i = 0; i < this.showingCaseDetail.steps.length; i++) {
        if (this.showingCaseDetail.steps[i].id > row.id) {
          this.showingCaseDetail.steps[i].id =
            this.showingCaseDetail.steps[i].id + 1;
        }
      }
      var tmpcase = JSON.parse(defaultTableData);

      tmpcase.steps[0].id = row.id + 1;
      this.$set(
        this.showingCaseDetail.steps,
        this.showingCaseDetail.steps.length,
        tmpcase.steps[0]
      );
    },
    //删除step
    handleDelStepClick(row) {
      var index = null;
      for (var i = 0; i < this.showingCaseDetail.steps.length; i++) {
        if (this.showingCaseDetail.steps[i].id === row.id) {
          index = i;
        }
        if (this.showingCaseDetail.steps[i].id > row.id) {
          this.showingCaseDetail.steps[i].id =
            this.showingCaseDetail.steps[i].id - 1;
        }
      }
      this.showingCaseDetail.steps.splice(index, 1);
    },
    handleRowClick(row, event,column) {
      this.selectedRow = row;
    },
    //向上移动step   BUG
    handleStepUpClick() {
      if(this.selectedRow==null){
        alert("需要先选择一行");
        return;
      }
      if(this.selectedRow.id==0){
        return;
      }
      var selectedIndex=this.selectedRow.id;
      var nextStep=this.showingCaseDetail.steps[selectedIndex-1];
      
      this.showingCaseDetail.steps[selectedIndex-1]=this.selectedRow;
      this.showingCaseDetail.steps[selectedIndex]=nextStep;
      this.showingCaseDetail.steps[selectedIndex].id=selectedIndex;
      this.showingCaseDetail.steps[selectedIndex-1].id=selectedIndex-1;
      //必须新建数组，否则vue不会重新渲染
      this.showingCaseDetail.steps = this.showingCaseDetail.steps.slice(0);
      //这可能是elementui的bug，如果oldCurrentRow和newCurrentRow是一个，会跳过渲染
      //所以这里先把currentRow设置到别的行，以便nexttick中的setCurrentRow能够生效
      this.$refs.caseTable.setCurrentRow(this.showingCaseDetail.steps[selectedIndex]);
      //vue是异步渲染，在每个tick开始前会搜集所有的数据变化，放入队列，多次修改仅最后一次生效，然后统一渲染
      //所以如果要在数据变化后的基础上做修改，需要放在nexttick
      this.$nextTick(function() {
        this.$refs.caseTable.setCurrentRow(this.showingCaseDetail.steps[selectedIndex-1]);
      });
    },
    //向下移动step  BUG
    handleStepDownClick() {
      if(this.selectedRow==null){
        alert("需要先选择一行");
        return;
      }
      if(this.selectedRow.id==this.showingCaseDetail.steps.length-1){
        return;
      }
      var selectedIndex=this.selectedRow.id;
      var nextStep=this.showingCaseDetail.steps[selectedIndex+1];
      
      this.showingCaseDetail.steps[selectedIndex+1]=this.selectedRow;
      this.showingCaseDetail.steps[selectedIndex]=nextStep;
      this.showingCaseDetail.steps[selectedIndex].id=selectedIndex;
      this.showingCaseDetail.steps[selectedIndex+1].id=selectedIndex+1;

      this.showingCaseDetail.steps = this.showingCaseDetail.steps.slice(0);
      this.$refs.caseTable.setCurrentRow(this.showingCaseDetail.steps[selectedIndex]);
      this.$nextTick(function() {
        this.$refs.caseTable.setCurrentRow(this.showingCaseDetail.steps[selectedIndex+1]);
      });
    },
    //强制重新获取casedetail
    handleRefreshClick() {
      if (this.$refs.casetree.getCurrentNode().caseDetail == undefined) {
        return;
      }
      this.$refs.casetree.getCurrentNode().caseDetail = null;
      this.loadCaseDetail(this.$refs.casetree.currentNode.node);
      if(haveCachedDataChange){
        this.strctureObjects=null;
        this.loadObjects();
      }
      this.globalParas=null;
      this.loadGlobalParas();
      haveCachedDataChange=false;
    },
    //保存steps
    handleSaveClick() {
      HomeData.updateCase(
        this.selectedNode.data.refId,
        this.selectedNode.label,
        this.popup.bind(this)
      );
      //有空步骤，不应该保存
      for (var i = 0; i < this.showingCaseDetail.steps.length; i++) {
        if (this.showingCaseDetail.steps[i].action == "") {
          alert("have empty step");
          return;
        }
        if (
          this.showingCaseDetail.steps[i].page != "" &&
          (this.showingCaseDetail.steps[i].type == "" ||
            this.showingCaseDetail.steps[i].name == "" ||
            this.showingCaseDetail.steps[i].path == "")
        ) {
          alert("object is not complete");
          return;
        }
      }
      if (
        JSON.stringify(this.selectedNode.data.caseDetail) !==
        this.emptyTableData
      ) {
        HomeData.updateSteps(
          this.selectedNode.data.refId,
          this.showingCaseDetail.steps,
          this.popup.bind(this)
        );
      }
      haveCachedDataChange=false;
    },
    //添加一个新的node
    handleAddNodeClick() {
      HomeData.createNode(
        this.$refs.casetree.currentNode.node.data.refId,
        function(response) {
          //return the new node info,add empty detail
          this.$refs.casetree.append(
            response,
            this.$refs.casetree.currentNode.node
          );
        }.bind(this),
        this.popup.bind(this)
      );
    },
    //TO-DO
    handleDelNodeClick() {
      this.$confirm("此操作将永久删除该节点以及对应的数据, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          HomeData.deleteNode(
            this.$refs.casetree.currentNode.node.data.refId,
            function(response) {
              this.$refs.casetree.remove(this.$refs.casetree.currentNode.node);
            }.bind(this),
            this.popup.bind(this)
          );
        })
        .catch(() => {});
    },

    handleExecRowClick(row, event, column) {
      this.selectedExecRowData = row;
    },
    //重新选操作后清空后面所有的字段
    handleActionChange(row) {
      row.page = "";
      row.type = "";
      row.name = "";
      row.path = "";
      for (var i = 0; i < paraCount; i++) {
        row.paras[i] = "";
      }
      row.response = "";
    },
    //重新选page后清空类型，对象名，xpath
    handlePageChange(row) {
      row.type = "";
      row.name = "";
      row.path = "";
    },
    //重新选type后情空对象名和xpath
    handleTypeChange(row) {
      if(row.page==null||row.page==undefined||row.page==""){
        alert("需要先选择页面");
      }
      row.name = "";
      row.path = "";
    },
    //xpath跟着对象名变化
    handleNameChange(row) {
      if(row.page==null||row.page==undefined||row.page==""){
        alert("需要先选择页面");
      }
      if(row.type==null||row.type==undefined||row.type==""){
        alert("需要先选择类型");
      }
      //如果缓存中找不到这个对象，说明是要新建对象
      if (
        this.strctureObjects[row.page] == undefined ||
        this.strctureObjects[row.page][row.type] == undefined ||
        this.strctureObjects[row.page][row.type][row.name]==undefined
      ) {
        //如果xpath已经有值，则说明是先输入的path，保留这个值
        if(row.path!=""){
          //如果当前page+type下path已经存在，不允许重复添加
          var objs=this.strctureObjects[row.page][row.type];
          for(var key in objs){
            if(objs[key]==row.path){
              alert(row.path+"已经存在:"+row.page+"."+row.type+"."+key);
              row.name="";
              return;
            }
          }
          this.addToStrctureObject(this.strctureObjects,row.page,row.type,row.name,row.path);
          haveCachedDataChange=true;
        }
        return;
      }else{
        row.path=this.strctureObjects[row.page][row.type][row.name];
      }
    },
    //对象名跟着xpath变化
    handlePathChange(row) {
      var isFound = false;
      for (var page in this.strctureObjects) {
        for (var type in this.strctureObjects[page]) {
          for (var name in this.strctureObjects[page][type]) {
            if (this.strctureObjects[page][type][name] == row.path) {
              row.page = page;
              row.type = type;
              row.name = name;
              isFound = true;
              break;
            }
          }
          if(isFound){break;}
        }
        if(isFound){break;}
      }
      //没找到则说明是新抓取的xpath，如果之前已经选取了name，则认为是要更新已有的对象
      if (!isFound && row.name != "") {
        alert("当前xpath的修改会影响所有使用该对象的用例，请谨慎确认后保存！");
        this.addToStrctureObject(this.strctureObjects,row.page,row.type,row.name,row.path);
        haveCachedDataChange=true;
        for(var i=0;i<this.showingCaseDetail.steps.length;i++){
          var step=this.showingCaseDetail.steps[i];
          if(step.page==row.page&&step.type==row.type&&step.name==row.name&&step.path!=row.path){
            step.path=row.path;
          }
        }
      }
    },
    validateResChange(row){
      var l=row.response.length;
      if(row.response.substring(0,2)!="%%" && row.response.substring(0,2)!="@@" && row.response.substring(l,l-2)!="%%"&& row.response.substring(l,l-2)!="@@"){
        alert("返回值变量名必须是%%name%%或者@@name@@");
        row.response="";
      }
      var isFound=false;
      if(row.response.substring(0,2)=="%%"){
        for(var i=0;i<this.globalParas.length;i++){
          if(this.globalParas[i].value==row.response){
            isFound=true;
            break;
          }
        }
        if(!isFound){
          let tmp = {};
          tmp.label = row.response;
          tmp.value = row.response;
          this.globalParas.push(tmp);
        }
      }
    },
    //获取最新的任务执行状态以及可用的agent机器
    handleRefreshAgent() {
      this.loadAgents();
      this.loadExecutions();
    },
    handleRunClick() {
      if (
        this.selectedAgent == null ||
        this.$refs.casetree.getCheckedNodes().length == 0
      ) {
        alert("need to select agent and cases");
        return;
      }
      var build = {};
      build["jobName"] = this.selectedAgent;
      build["paras"] = {};
      build["paras"]["dataVersion"] = "default";
      build["paras"]["logLevel"] = "debug";
      build["cases"] = {};
      for (var i = 0; i < this.$refs.casetree.getCheckedNodes().length; i++) {
        build["cases"][this.$refs.casetree.getCheckedNodes()[i].refId] = 0;
      }
      HomeData.startJob(
        build,
        function(response) {
          if (this.executions == null) {
            this.executions = [];
          }
          this.executions.push(response);
        }.bind(this),
        this.popup.bind(this)
      );
    },
    getJobDetail() {
      if (this.selectedExecRowData == null) {
        alert("please select one job");
        return;
      }
      HomeData.getJobDetail(
        this.selectedExecRowData.jobName,
        this.selectedExecRowData.buildId,
        function(response) {
          console.log(response.url);
          window.open(response.url);
        }.bind(this),
        this.popup.bind(this)
      );
    },
    handleLocalRunClick() {
      this.startLocalAgent();
      this.reRunJob();
    },
    popup(msg) {
      this.$message({
        type: "info",
        message: msg
      });
    },
    //TO-DO
    reRunJob() {},
    //TO-DO
    startLocalAgent() {
      console.log(this.agents);
      console.log(this.executions);
    },
    debug() {
      //console.log(this.$refs.casetree.currentNode.node.store);
      //this.$refs.casetree.currentNode.node.store.load(this.$refs.casetree.currentNode.node,resolve);
      console.log(document.querySelector("main"));
      console.log(this.showingCaseDetail);
      console.log(this.globalParas);
      console.log(this.strctureObjects);
      console.log(this.$refs.casetree.getCurrentNode());
      console.log(this.selectedNode);
    }
  }
};
</script>