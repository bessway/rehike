export default {
  getProjects,
  getNodes,
  getCaseDetail,
  getGlobalParas,
  getObjects,
  getActions,
  moveNode,
  copyNodes,
  updateCase,
  updateSteps,
  createNode,
  deleteNode,
  getAgents,
  getExecutions,
  startJob,
  getJobDetail
};
import axios from "axios";
//var host = "http://192.168.2.103:8081";
var host = "http://118.178.133.96:8080/hike";
function getProjects(resolve,popup) {
  axios({
    method: "get",
    url: host + "/1/test/projects"
  })
    .then(response => {
      let _data = response.data;
      resolve(_data);
    })
    .catch(function(err) {
      popup("获取数据失败");
      console.log(err);
    });
}

function getNodes(refId,resolve,popup) {
  axios({
    method: "get",
    url: host + "/1/test/nodes/"+refId
  })
    .then(response => {
      let _data = response.data;
      resolve(_data);
    })
    .catch(function(err) {
      console.log(err);
      popup("获取节点失败");
    });
}

function getCaseDetail(refId,bindData,popup) {
  axios({
    method: "get",
    url: host + "/1/test/detail/"+refId
  })
    .then(response => {
      let _data = response.data;
      bindData(_data);
    })
    .catch(function(err) {
      console.log(err);
      popup("获取用例信息失败");
    });
}

function getGlobalParas(bindData,popup) {
  axios({
    method: "get",
    url: host + "/1/data/global"
  })
    .then(response => {
      let _data = response.data;
      bindData(_data);
    })
    .catch(function(err) {
      console.log(err);
      popup("获取全局变量失败");
    });
}

function getObjects(bindData,popup) {
  axios({
    method: "get",
    url: host + "/1/object/all"
  })
    .then(response => {
      let _data = response.data;
      bindData(_data);
    })
    .catch(function(err) {
      console.log(err);
      popup("获取对象信息失败");
    });
}

function getActions(bindData,popup) {
  axios({
    method: "get",
    url: host + "/1/action/all"
  })
    .then(response => {
      let _data = response.data;
      bindData(_data);
    })
    .catch(function(err) {
      console.log(err);
      popup("获取关键词信息失败");
    });
}

function createNode(parentRefId,bindData,popup) {
  axios({
    method: "post",
    url: host + "/1/test/node",
    data:{'parentId':parentRefId},
    headers:{'Content-Type':'application/json;charset=UTF-8'},
  })
    .then(response => {
      let _data = response.data;
      bindData(_data);
    })
    .catch(function(err) {
      console.log(err);
      popup("添加节点失败");
    });
}
function moveNode(movedRefId, targetRefId, bindData,popup) {
  axios({
    method: "put",
    url: host + "/1/test/node/hierachy/"+movedRefId,
    data:{'refId':targetRefId},
    headers:{'Content-Type':'application/json;charset=UTF-8'},
  })
    .then(response => {
      let _data = response.data;
      bindData(_data);
    })
    .catch(function(err) {
      console.log(err);
      popup("移动节点失败");
    });
}
//复制子节点
function copyNodes(targetRefId, copiedRefIds, bindData,popup) {
  axios({
    method: "post",
    url: host + "/1/test/node/hierachy/"+targetRefId,
    data:copiedRefIds,
    headers:{'Content-Type':'application/json;charset=UTF-8'},
  })
    .then(response => {
      let _data = response.data;
      bindData(_data);
    })
    .catch(function(err) {
      console.log(err);
      popup("复制节点失败");
    });
}

function updateCase(refId, caseName,popup) {
  axios({
    method: "put",
    url: host + "/1/test/node/"+refId,
    data:{'label':caseName},
    headers:{'Content-Type':'application/json;charset=UTF-8'},
  })
    .then(response => {
      let _data = response.data;
    })
    .catch(function(err) {
      console.log(err);
      popup("保存用例失败");
    });
}
function updateSteps(refId, casedata,popup) {
  axios({
    method: "put",
    url: host + "/1/test/node/step/"+refId,
    data:casedata,
    headers:{'Content-Type':'application/json;charset=UTF-8'},
  })
    .then(response => {
      let _data = response.data;
    })
    .catch(function(err) {
      console.log(err);
      popup("保存用例信息失败");
    });
}
function deleteNode(refId,bindData,popup) {
  axios({
    method: "delete",
    url: host + "/1/test/node/"+refId
  })
    .then(response => {
      let _data = response.data;
      bindData(_data);
    })
    .catch(function(err) {
      console.log(err);
      popup("删除节点失败");
    });
}
function startJob(build,bindData,popup){
  axios({
    method: "post",
    url: host + "/1/jenkins/job",
    data: build
  })
    .then(response => {
      let _data = response.data;
      bindData(_data);
    })
    .catch(function(err) {
      console.log(err);
      popup("启动任务失败");
    });
}
function getExecutions(bindData,popup){
  axios({
    method: "get",
    url: host + "/1/jenkins/jobs"
  })
    .then(response => {
      let _data = response.data;
      bindData(_data);
    })
    .catch(function(err) {
      console.log(err);
      popup("获取任务信息失败");
    });
}
function getAgents(bindData,popup) {
  axios({
    method: "get",
    url: host + "/1/jenkins/agents"
  })
    .then(response => {
      let _data = response.data;
      bindData(_data);
    })
    .catch(function(err) {
      console.log(err);
      popup("获取节点机器失败");
    });
}
function getJobDetail(job,build,bindData,popup) {
  axios({
    method: "get",
    url: host + "/1/jenkins/jobdetail/"+job+"/build/"+build
  })
    .then(response => {
      let _data = response.data;
      bindData(_data);
    })
    .catch(function(err) {
      console.log(err);
      popup("获取报告失败");
    });
}
