<template>
<el-container style="border: 1px solid #eee">
  <el-aside width="200px" style="background-color: rgb(238, 241, 246);color: #333 ">
    <el-input
      placeholder="仅过滤已加载的节点"
      v-model="filterText" height="40px">
    </el-input>
    <el-tree style="background-color: #b3c0d1;"
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
            <el-button style="float:right;padding:5px" size="mini" icon="el-icon-arrow-up"
            @click="handleStepUpClick"></el-button>
            <el-button style="float:right;padding:5px" size="mini" icon="el-icon-arrow-down"
            @click="handleStepDownClick"></el-button>
            <el-button style="float:right;padding:5px" size="mini" icon="el-icon-refresh"
            @click="handleRefreshClick"></el-button>
            <el-input type="textarea" resize="none" autosize float="left" 
              v-model="selectedLabel">
            </el-input>
          </div>
          <el-table style="width: 100%;" stripe height="480"
          :data="showingCaseDetail.steps"
          :default-sort = "{prop: 'id', order: 'ascending'}"
          highlight-current-row
          ref="caseTable"
          :row-key="getRowKeys"
          @row-click="handleRowClick">
            <el-table-column label="" prop='id' sortable width="0">
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
            <el-table-column label="页面" min-width="60" header-align="center">
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
            <el-table-column label="对象类型" min-width="60" header-align="center">
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
            <el-table-column label="对象名" min-width="80" header-align="center">
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
            <el-table-column label="参数1" min-width="100" header-align="center">
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
            <el-table-column label="参数2" min-width="100" header-align="center">
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
            <el-table-column label="参数3" min-width="100" header-align="center">
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
            <el-table-column label="参数4" min-width="100" header-align="center">
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
            <el-table-column label="返回值" min-width="100" header-align="center">
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
            <el-table-column label="" min-width="30px">
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
          <el-select v-model=selectedBrowser filterable placeholder="请选择">
              <el-option
                v-for="item in getBrowser"
                :key="item.value"
                :label="item.label"
                :value="item.value">
              </el-option>
          </el-select>
          <el-select v-model=selectedAgent filterable placeholder="请选择">
              <el-option
                v-for="item in getAgents"
                :key="item.value"
                :label="item.label"
                :value="item.value"
                :disabled="item.disabled">
              </el-option>
          </el-select>
          <el-button type="primary"
          @click="handleRunClick">运行</el-button>
          <el-button type="primary"
          @click="handleLocalRunClick">本地运行</el-button>
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
const defaultTableData = '{"steps": [{"id": 0}]}';
const emptyTableData = '{"steps": []}';
const emptyNode = '{"label":"","refId":""}';
var paraCount=4;
export default {
  data() {
    return {
      filterText: "",
      props: {
        label: "label"
      },
      selectedNode: null,
      selectedRowId: null,
      actions: null,
      objects: null,
      types: null,
      names: null,
      xpaths: null,
      globalParas: null,
      showingCaseDetail: JSON.parse(emptyTableData),
      nodes: null,
      selectedAgent: null,
      selectedBrowser: null
    };
  },
  //如果函数的依赖有变化，则按逻辑同步更新computed属相
  //即selectedNode有变化，则执行函数更新isShowAddBtn
  computed: {
    isShowAddBtn: function() {
      if (this.selectedNode === null) {
        return 0;
      } else {
        return this.selectedNode.level !== 4 && this.selectedNode.level !== 0;
      }
    },
    isShowDelBtn: function() {
      if (this.selectedNode === null) {
        return 0;
      } else {
        return this.selectedNode.level !== 0;
      }
    },
    isShowSaveBtn: function() {
      if (this.selectedNode === null) {
        return 0;
      } else {
        return this.selectedNode.level > 0;
      }
    },
    selectedNodeType:function(){
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
    selectedLabel: function() {
      if (this.selectedNode === null) {
        return "无";
      } else {
        return this.selectedNode.label;

      }
    },
    getPages: function() {
      var pages = new Array();
      for (var item in this.objects) {
        pages.push({ label: item, value: item });
      }
      return pages;
    },
    getBrowser: function() {
      var browsers = new Array();
      for (var item in this.nodes) {
        browsers.push({ label: item, value: item });
      }
      return browsers;
    },
    getAgents() {
      if (this.selectedBrowser == null) {
        return [];
      }
      var agents = new Array();
      for (var i = 0; i < this.nodes[this.selectedBrowser].length; i++) {
        console.log(this.nodes[this.selectedBrowser][i]);
        agents.push({
          label:
            this.nodes[this.selectedBrowser][i].node +
            "_" +
            this.nodes[this.selectedBrowser][i].version,
          value:
            this.nodes[this.selectedBrowser][i].node +
            "_" +
            this.nodes[this.selectedBrowser][i].version,
          disabled: this.nodes[this.selectedBrowser][i].status
        });
      }
      return agents;
    }
  },
  //watch是指watch的属性例如filterText发生变化，则执行函数
  //这里之所以能触发是因为在input中，v-model会监控value属性，所以只要有输入就会触发
  watch: {
    filterText(val) {
      this.$refs.casetree.filter(val);
    }
  },

  methods: {
    getRowKeys(row) {
      return row.id;
    },

    getTypesOnPage: function(page) {
      if (page == null || page == "") {
        return [];
      }
      var types = new Array();
      var objs = this.objects[page];
      for (var item in objs) {
        types.push({ label: item, value: item });
      }
      return types;
    },
    getPathNames: function(page, type) {
      if (page == null || page == "" || type == null || type == "") {
        return [];
      }
      var names = new Array();
      var objs = this.objects[page][type];
      for (var item in objs) {
        names.push({ label: item, value: item });
      }
      return names;
    },
    getPaths: function(page, type) {
      if (page == null || page == "" || type == null || type == "") {
        return [];
      }
      var paths = new Array();
      var objs = this.objects[page][type];
      for (var item in objs) {
        paths.push({ label: objs[item], value: objs[item] });
      }
      return paths;
    },
    isDisableSelect(row, col) {
      if (this.actions === null || this.actions.length === 0) {
        return false;
      }
      for (var i = 0; i < this.actions.length; i++) {
        var result = false;
        if (this.actions[i].value === row.action) {
          if(col.label.indexOf("参数")!=-1){
            var colIndex=col.label.substring(2,3);
            if(colIndex>this.actions[i].paras.length){
              result= true;
            }else{
              result= false;
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
          }else if(col.label==="返回值"){
            result=!this.actions[i].response;
          }
          return result;
        }
      }
    },
    getPlaceHolder(row, col) {
      if (this.actions === null || this.actions.length === 0) {
        return "";
      }
      for (var i = 0; i < this.actions.length; i++) {
        if (this.actions[i].value === row.action) {
          if (col.label === "参数1") {
            return this.actions[i].p1desc === undefined
              ? ""
              : this.actions[i].p1desc;
          } else if (col.label === "参数2") {
            return this.actions[i].p2desc === undefined
              ? ""
              : this.actions[i].p2desc;
          } else if (col.label === "参数3") {
            return this.actions[i].p3desc === undefined
              ? ""
              : this.actions[i].p3desc;
          } else if (col.label === "参数4") {
            return this.actions[i].p4desc === undefined
              ? ""
              : this.actions[i].p4desc;
          }
        }
      }
      return "";
    },
    handleClickTab(tab) {
      if (tab.name === "case") {
        this.loadActions();
        this.loadObjects();
        this.loadGlobalParas();
        this.selectedRow = null;
      } else if (tab.name === "execute") {
        this.loadAgents();
      }
    },
    handleClickNode(data, node) {
      this.selectedNode = node;
      this.loadCaseDetail();
      this.selectedRow = null;
    },
    filterNode(value, data) {
      if (!value) return true;
      return data.label.indexOf(value) !== -1;
    },
    loadActions() {
      if (this.actions !== null) {
        return;
      }
      HomeData.getActions(this.bindActionData);
    },
    bindActionData(response){
      this.actions=response;
      for(var i=0;i<this.actions.length;i++){
        this.actions[i].value=this.actions[i].name;
      }
    },
    loadObjects() {
      if (this.objects !== null) {
        return;
      }
      setTimeout(() => {
        this.objects = HomeData.getObjects();
      }, 500);
    },
    loadGlobalParas() {
      if (this.globalParas !== null) {
        return;
      }
      HomeData.getGlobalParas(this.bindGlobalParas);
    },
    bindGlobalParas(response){
      if(this.globalParas===null){
        this.globalParas=new Array();
      }
      for(var item in response){
        let tmp={};
        tmp.label=item;
        tmp.value=item;

        this.globalParas.push(tmp);
      }
    },
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
      HomeData.getCaseDetail(this.selectedNode.data.refId,this.bindCaseDetail);
    },
    bindCaseDetail(response){
      this.showingCaseDetail=response;
      if (JSON.stringify(this.showingCaseDetail) === "{}") {
        this.showingCaseDetail = JSON.parse(defaultTableData);
      }
      for(var i=0;i<this.showingCaseDetail.steps.length;i++){
        this.showingCaseDetail.steps[i].paras=this.appendBlank(this.showingCaseDetail.steps[i].paras);
      }
      console.log(this.showingCaseDetail);
      this.selectedNode.data.caseDetail = this.showingCaseDetail;
    },
    appendBlank(originArray){
      var empty=new Array(paraCount);
      for(var item in empty){
            item="";
      }
      if(originArray==null||originArray==undefined){
        originArray=new Array();
      }
      return originArray.concat(empty).slice(0,4);
    },
    loadTreeNode(node, resolve) {
      var data;
      if (node.level === 0) {
        HomeData.getProjects(resolve);
      } else {
        HomeData.getNodes(node.data.refId,resolve);
      }
    },
    allowDrag(draggingNode) {
      return draggingNode.level > 1;
    },
    allowDrop(draggingNode, dropNode) {
      if (draggingNode.level-dropNode.level!==1) {
        return false;
      } else {
        return true;
      }
    },
    handleDrop(draggingNode, dropNode, dropType, ev) {
      //call server
    },
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
    handleRefreshClick() {
      if (this.$refs.casetree.getCurrentNode().caseDetail == undefined) {
        return;
      }
      this.$refs.casetree.getCurrentNode().caseDetail = null;
      this.loadCaseDetail(this.$refs.casetree.currentNode.node);
    },
    handleSaveClick() {
      console.log(JSON.stringify(this.showingCaseDetail));
      console.log(this.selectedRowId);
      console.log(this.$refs.caseTable);
    },
    handleAddNodeClick() {
      var tmp = JSON.parse(emptyNode);
      if (this.$refs.casetree.currentNode.node.level === 4) {
        tmp.casedetail = JSON.parse(defaultTableData);
      }
      this.$refs.casetree.append(tmp, this.$refs.casetree.currentNode.node);
    },
    handleDelNodeClick() {
      this.$refs.casetree.remove(this.$refs.casetree.currentNode.node);
    },
    handleRowClick(value, row) {
      this.selectedRowId = row.id;
    },
    handleActionChange(row) {
      row.page = "";
      row.type = "";
      row.name = "";
      row.path = "";
      row.p1 = "";
      row.p2 = "";
      row.p3 = "";
      row.p4 = "";
      row.res = "";
    },
    handlePageChange(row) {
      row.type = "";
      row.name = "";
      row.path = "";
    },
    handleTypeChange(row) {
      row.name = "";
      row.path = "";
    },
    handleNameChange(row) {
      var result = this.objects[row.page][row.type][row.name];
      if (result != undefined && result != null && result != "") {
        row.path = result;
      } else {
        row.path = "";
      }
    },
    handlePathChange(row) {
      console.log(this.objects);
      var result = "";
      var items = this.objects[row.page][row.type];
      for (var item in items) {
        if (items[item] == row.path) {
          result = item;
          break;
        }
      }
      row.name = result;
    },
    loadAgents() {
      this.nodes = HomeData.getExecuteNodes();
    },
    handleRunClick(){
      console.log(this.$refs.casetree.getCheckedNodes());
      console.log(this.$refs.casetree.getCheckedNodes()[0].level);
    },
    handleLocalRunClick(){
      this.startLocalAgent();
      this.handleRunClick()
    },
    startLocalAgent(){

    }
  }
};
</script>