<template>
<el-container style="border: 1px solid #eee">
  <el-aside width=250px style="background-color: rgb(238, 241, 246);color: #333 ">
    <el-input
      placeholder="仅过滤已加载的节点"
      v-model="filterText" height="40px">
    </el-input>
    <el-tree style="background-color: #b3c0d1;height:560px"
      class="filter-tree"
      node-key="id"
      :load="loadTreeNode"
      lazy
      show-checkbox
      draggable
      :allow-drop="allowDrop"
      :allow-drag="allowDrag"
      :highlight-current=true
      @node-drop="handleDrop"
      @node-click="handleClickNode"
      :filter-node-method="filterNode"
      ref="casetree">
    </el-tree>
  </el-aside>
  
  <el-container>
    <el-main style="padding-top:5px">
      <el-tabs @tab-click="handleClickTab" style="margin-top:0px;">
        <el-tab-pane label="用例管理" name="case">
          <div>
            <b><font size="4">正在查看 {{selectedNodeType}} </font></b>
            <el-button size="mini" v-show="isShowAddBtn" icon="el-icon-plus" circle
            @click="handleAddNodeClick"></el-button>
            <el-button size="mini" v-show="isShowDelBtn" icon="el-icon-minus" circle
            @click="handleDelNodeClick"></el-button>
            <el-button size="mini" v-show="isShowSaveBtn" icon="el-icon-check" circle
            @click="handleSaveClick"></el-button>
            <el-button size="mini"
            @click="debug">copy</el-button>
            <el-button size="mini" icon="el-icon-question"
            @click="debug"></el-button>
            <el-button style="float:right;padding:5px" size="mini" icon="el-icon-arrow-up"
            @click="handleStepUpClick"></el-button>
            <el-button style="float:right;padding:5px" size="mini" icon="el-icon-arrow-down"
            @click="handleStepDownClick"></el-button>
            <el-button style="float:right;padding:5px" size="mini" icon="el-icon-refresh"
            @click="handleRefreshClick"></el-button>
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
          :header-cell-style = "getHeaderCellStyle"
          @row-click="handleRowClick">
            <el-table-column label="" prop='id' width="0">
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
            <el-table-column label="页面" min-width="80" header-align="center">
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
            <el-table-column label="参数4" min-width="140" header-align="center">
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
            <el-table-column label="返回值" min-width="160" header-align="center">
              <template slot-scope="scope">
                <el-select filterable v-bind:placeholder="getPlaceHolder(scope.row,scope.column)"
                  v-model=scope.row.response allow-create clearable
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
                  <el-button size="mini" icon="el-icon-circle-plus-outline"
                  @click="handleAddStepClick(scope.row)"></el-button>
                  <el-button size="mini" icon="el-icon-remove-outline"
                  @click="handleDelStepClick(scope.row)"></el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
        <!--el-tab-pane label="参数管理" name="parameter">参数管理</el-tab-pane>
        <el-tab-pane label="对象管理" name="object">对象管理</el-tab-pane>
        <el-tab-pane label="数据管理" name="data">数据管理</el-tab-pane>
        <el-tab-pane-- label="节点管理" name="node">节点管理</el-tab-pane-->
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
          <el-button type="primary"
          @click="handleRunClick">运行</el-button>
          <el-button type="primary"
          @click="handleLocalRunClick">本地运行</el-button>
          <el-button type="primary"
          @click="getJobDetail" style="float:right">查看详情</el-button>
          <el-button type="primary"
          @click="handleRefreshAgent" style="float:right">刷新</el-button>
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
  '{"steps": [{"id": 0,"action":"","name":"","page":"","paras":["","","",""],"path":"","response":"","type":""}]}';
const emptyTableData = '{"steps": []}';
const emptyNode = '{"label":"","refId":""}';
var paraCount = 4;
export default {
  data() {
    return {
      filterText: "",
      props: {
        label: "label"
      },
      selectedNode: null,
      caselabel: null,
      selectedRowId: null,
      actions: null,
      strctureObjects: null,
      types: null,
      names: null,
      xpaths: null,
      globalParas: null,
      showingCaseDetail: JSON.parse(emptyTableData),
      agents: null,
      selectedAgent: null,
      selectedBrowser: null,
      selectedOs: null,
      executions: null,
      selectedExecRowData: null
    };
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
    getHeaderCellStyle(style) {
      var result;
      if (style.columnIndex > 0 && style.columnIndex < 11) {
        result = { "padding-left": style.columnIndex * 10 + "px" };
      }
      return result;
    },
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
      if(this.strctureObjects[page]==undefined){
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
      if(this.strctureObjects[page]==undefined ||this.strctureObjects[page][type]==undefined){
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
      if(this.strctureObjects[page]==undefined ||this.strctureObjects[page][type]==undefined){
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
      if(col.label=="返回值"){
        return "参数名";
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
        this.selectedRow = null;
      } else if (tab.name === "execute") {
        this.loadAgents();
        this.loadExecutions();
      }
    },
    //选择node后记载详细信息
    handleClickNode(data, node) {
      this.selectedNode = node;
      this.loadCaseDetail();
      this.selectedRow = null;
      this.caselabel = node.label;
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
        }.bind(this)
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
            var tmp = result;
            var target = obj.split(".");
            if (tmp[target[0]] == undefined) {
              tmp[target[0]] = {};
            }
            tmp = tmp[target[0]];
            if (tmp[target[1]] == undefined) {
              tmp[target[1]] = {};
            }
            tmp = tmp[target[1]];
            tmp[target[2]] = response[obj];
          }
          this.strctureObjects = result;
        }.bind(this)
      );
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
        }.bind(this)
      );
    },
    //获取case detail
    loadCaseDetail() {
      if (this.selectedNode.level !== 4) {
        this.showingCaseDetail = JSON.parse(emptyTableData);
        return;
      }
      //已经加载过的不再重新加载
      if (
        this.selectedNode.data.caseDetail != undefined &&
        this.selectedNode.data.caseDetail != null &&
        JSON.stringify(this.selectedNode.data.caseDetail) !== defaultTableData
      ) {
        this.showingCaseDetail = this.selectedNode.data.caseDetail;
        return;
      }
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
        }.bind(this)
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
        HomeData.getProjects(resolve);
      } else {
        HomeData.getNodes(node.data.refId, resolve);
      }
    },
    loadAgents() {
      HomeData.getAgents(
        function(response) {
          this.agents = response;
        }.bind(this)
      );
    },
    loadExecutions() {
      HomeData.getExecutions(
        function(response) {
          this.executions = response;
        }.bind(this)
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
      HomeData.moveNode(draggingNode.data.refId, dropNode.data.refId, function(
        response
      ) {
        draggingNode.data.parentId = response.parentId;
      });
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
    //向上移动step   BUG
    handleStepUpClick() {
      var currstep = null;
      var currIndex = null;
      var nextstep = null;
      var nextIndex = null;
      if (this.selectedRowId != null && this.selectedRowId > 0) {
        for (var i = 0; i < this.showingCaseDetail.steps.length; i++) {
          if (this.showingCaseDetail.steps[i].id === this.selectedRowId) {
            currstep = this.showingCaseDetail.steps[i];
            currIndex = i;
          } else if (
            this.showingCaseDetail.steps[i].id ===
            this.selectedRowId - 1
          ) {
            nextstep = this.showingCaseDetail.steps[i];
            nextIndex = i;
          }
        }
        nextstep.id = this.selectedRowId;
        currstep.id = this.selectedRowId - 1;
        this.selectedRowId = this.selectedRowId - 1;

        this.showingCaseDetail.steps = this.showingCaseDetail.steps.slice(0);
        this.$nextTick(function() {
          console.log(this.$refs.caseTable.data[currIndex]);
          this.$refs.caseTable.setCurrentRow(
            this.$refs.caseTable.data[currIndex]
          );
        });
        /*
        this.$nextTick(function() {
          //this.$set(this.showingCaseDetail.steps, currIndex, currstep);
          this.showingCaseDetail.steps = this.showingCaseDetail.steps.slice(0);
          this.$nextTick(function() {
            this.$refs.caseTable.setCurrentRow(
              this.$refs.caseTable.data[currIndex]
            );
          });
        });*/
      }
    },
    //向下移动step  BUG
    handleStepDownClick() {
      var currstep = null;
      var currIndex = null;
      var nextstep = null;
      var nextIndex = null;
      if (
        this.selectedRowId != null &&
        this.selectedRowId < this.showingCaseDetail.steps.length - 1
      ) {
        for (var i = 0; i < this.showingCaseDetail.steps.length; i++) {
          if (this.showingCaseDetail.steps[i].id === this.selectedRowId) {
            currstep = this.showingCaseDetail.steps[i];
            currIndex = i;
          } else if (
            this.showingCaseDetail.steps[i].id ===
            this.selectedRowId + 1
          ) {
            nextstep = this.showingCaseDetail.steps[i];
            nextIndex = i;
          }
        }
        nextstep.id = this.selectedRowId;
        currstep.id = this.selectedRowId + 1;
        this.selectedRowId = this.selectedRowId + 1;
        //this.$set(this.showingCaseDetail.steps, currIndex, currstep);
        this.$nextTick(function() {
          //this.$set(this.showingCaseDetail.steps, currIndex, currstep);
          this.showingCaseDetail.steps = this.showingCaseDetail.steps.slice(0);
          this.$nextTick(function() {
            this.$refs.caseTable.setCurrentRow(
              this.$refs.caseTable.data[currIndex]
            );
          });
        });
      }
    },
    //强制重新获取casedetail
    handleRefreshClick() {
      if (this.$refs.casetree.getCurrentNode().caseDetail == undefined) {
        return;
      }
      this.$refs.casetree.getCurrentNode().caseDetail = null;
      this.loadCaseDetail(this.$refs.casetree.currentNode.node);
    },
    //保存steps
    handleSaveClick() {
      //有空步骤，不应该保存
      for (var i = 0; i < this.showingCaseDetail.steps.length; i++) {
        if (this.showingCaseDetail.steps[i].action == "") {
          alert("have empty step");
          return;
        }
      }
      HomeData.updateCase(
        this.selectedNode.data.refId,
        this.selectedNode.label
      );

      HomeData.updateSteps(
        this.selectedNode.data.refId,
        this.showingCaseDetail.steps
      );
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
        }.bind(this)
      );
    },
    //TO-DO
    handleDelNodeClick() {
      HomeData.deleteNode(
        this.$refs.casetree.currentNode.node.data.refId,
        function(response) {
          this.$refs.casetree.remove(this.$refs.casetree.currentNode.node);
        }.bind(this)
      );
    },
    handleRowClick(value, row) {
      this.selectedRowId = row.id;
    },
    handleExecRowClick(value, row) {
      this.selectedExecRowData = value;
    },
    //重新选操作后清空后面所有的字段
    handleActionChange(row) {
      row.page = "";
      row.type = "";
      row.name = "";
      row.path = "";
      for (var i = 0; i < this.paraCount; i++) {
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
      row.name = "";
      row.path = "";
    },
    //xpath跟着对象名变化
    handleNameChange(row) {
      if(this.strctureObjects[row.page]==undefined ||this.strctureObjects[row.page][row.type]==undefined){
        row.path=="";
        return;
      }
      var result = this.strctureObjects[row.page][row.type][row.name];

      if (result != undefined && result != null) {
        row.path = result;
      } else {
        //如果没有对应的记录，置空
        row.path = "";
      }
    },
    //对象名跟着xpath变化
    handlePathChange(row) {
      if(this.strctureObjects[row.page]==undefined ||this.strctureObjects[row.page][row.type]==undefined){
        row.name=="";
        return;
      }
      var names = this.strctureObjects[row.page][row.type];
      for (var item in names) {
        if (names[item] == row.path) {
          row.name = item;
          break;
        } else {
          var result = this.strctureObjects[row.page][row.type][row.name];
          //如果没有对应的记录，说明是新建的
          if (result != undefined && result != null) {
            row.name = "";
          }
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
        }.bind(this)
      );
    },
    getJobDetail() {
      if(this.selectedExecRowData==null){
        alert("please select one job");
        return;
      }
      HomeData.getJobDetail(this.selectedExecRowData.jobName,this.selectedExecRowData.buildId,function(response) {
        console.log(response.url);
          window.open(response.url);
        }.bind(this)
      );
    },
    handleLocalRunClick() {
      this.startLocalAgent();
      this.reRunJob();
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
    }
  }
};
</script>