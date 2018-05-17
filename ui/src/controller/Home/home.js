export default {
  getProjects,
  getNodes,
  getCaseDetail,
  getGlobalParas,
  getObjects,
  getActions,
  moveNode,
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
var host = "http://127.0.0.1:8081";
function getProjects(resolve) {
  axios({
    method: "get",
    url: host + "/1/test/projects"
  })
    .then(response => {
      let _data = response.data;
      resolve(_data);
    })
    .catch(function(err) {
      console.log(err);
    });
}

function getNodes(refId,resolve) {
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
    });
}

function getCaseDetail(refId,bindData) {
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
    });
}

function getGlobalParas(bindData) {
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
    });
}

function getObjects(bindData) {
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
    });
}

function getActions(bindData) {
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
    });
}

function createNode(parentRefId,bindData) {
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
    });
}
function moveNode(movedRefId, targetRefId, bindData) {
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
    });
}
function updateCase(refId, caseName) {
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
    });
}
function updateSteps(refId, casedata) {
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
    });
}
function deleteNode(refId,bindData) {
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
    });
}
function startJob(build,bindData){
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
    });
}
function getExecutions(bindData){
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
    });
}
function getAgents(bindData) {
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
    });
}
function getJobDetail(job,build,bindData) {
  axios({
    method: "get",
    url: host + "/1/jenkins/job/"+job+"/build/"+build
  })
    .then(response => {
      let _data = response.data;
      bindData(_data);
    })
    .catch(function(err) {
      console.log(err);
    });
}
