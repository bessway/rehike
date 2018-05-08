export default {
  getProjects,
  getNodes,
  getCaseDetail,
  getGlobalParas,
  getObjects,
  getActions,
  moveNode,
  updateCase,
  createSteps,
  updateNodeLabel,
  createNode,
  deleteNode,
  getNodeLabel,
  getExecuteNodes
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

function getObjects() {
  return {
    search: {
      input: {
        keyword: "//div[@id='name']",
        password: "//div[@id='pwd']xxxxxxxxxxxxxxxxxxxxxxxxx"
      },
      button: {
        search: "//div[@id='btn']"
      },
      text: {
        result: "//div[@id='btn']",
        paginition:""
      }
    }
  };
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
function moveNode(movedRefId, targetRefId) {}
function updateCase(refId, casedata) {}
function createSteps(refId, casedata) {}
function updateNodeLabel(refId, newLabel) {}
function createNode(parentRefId) {}
function deleteNode(refId) {}
function getNodeLabel(refId) {}
function getExecuteNodes() {
  return {
    chrome: [
      {
        node: "1",
        version: "1",
        status: true
      }
    ],
    firfox: [
      {
        node: "2",
        version: "2",
        status: false
      }
    ]
  };
}
