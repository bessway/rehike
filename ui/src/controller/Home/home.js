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

function getProjects() {
  return [
    {
      refId: 1,
      label: "project1"
    },
    {
      refId: 2,
      label: "project2"
    }
  ];
}

function getNodes(level, refId) {
  var data;
  if (level === 1) {
    //获取指定projectid的模块
    if (refId === 1) {
      data = [
        {
          refId: 100,
          label: "mudule100"
        },
        {
          refId: 101,
          label: "mudule101"
        }
      ];
    } else {
      data = [
        {
          refId: 200,
          label: "mudule200"
        },
        {
          refId: 201,
          label: "mudule201"
        }
      ];
    }
  } else if (level === 2) {
    //获取指定moduleid的suite
    if (refId === 100) {
      data = [
        {
          refId: 100000,
          label: "suite100000"
        },
        {
          refId: 100001,
          label: "suite100001"
        }
      ];
    } else if (refId === 101) {
      data = [
        {
          refId: 101000,
          label: "suite101000"
        },
        {
          refId: 101001,
          label: "suite101001"
        }
      ];
    } else if (refId === 200) {
      data = [
        {
          refId: 200000,
          label: "suite200000"
        },
        {
          refId: 200001,
          label: "suite200001"
        }
      ];
    } else if (refId === 201) {
      data = [
        {
          refId: 201000,
          label: "suite201000"
        },
        {
          refId: 201001,
          label: "suite201001"
        }
      ];
    } else {
      data = [];
    }
  } else {
    //获取指定suiteid的case
    if (refId === 100000) {
      data = [
        {
          refId: 1000000000,
          label: "case1000000000"
        },
        {
          refId: 1000000001,
          label: "case1000000001"
        }
      ];
    } else if (refId === 100001) {
      data = [
        {
          refId: 1000010000,
          label: "case1000010000"
        },
        {
          refId: 1000010001,
          label: "case1000010001"
        }
      ];
    } else if (refId === 200000) {
      data = [
        {
          refId: 2000000000,
          label: "case2000000000"
        },
        {
          refId: 2000000001,
          label: "case2000000001"
        }
      ];
    } else if (refId === 300000) {
      data = [
        {
          refId: 3000000000,
          label: "case3000000000"
        },
        {
          refId: 3000000001,
          label: "case3000000001"
        }
      ];
    } else {
      data = [];
    }
  }
  return data;
}

function getCaseDetail(refId) {
  if (refId === 1000000000) {
    return {
      steps: [
        {
          id: 1,
          action: "open browser",
          page: "",
          type: "",
          name: "",
          path: "",
          p1: "http://www.baidu.com",
          p2: "",
          p3: "",
          p4: "",
          res: ""
        },
        {
          id: 0,
          action: "assert invisible",
          page: "login",
          type: "input",
          name: "password",
          path: "//div[@id='pwd']xxxxxxxxxxxxxxxxxxxxxxxxx",
          p1: "",
          p2: "",
          p3: "",
          p4: "",
          res: ""
        },
        {
          id: 2,
          action: "assert visible",
          page: "login",
          type: "input",
          name: "name",
          path: "//div[@id='name']xxxxxxxxxxxxxxxxxxxxxxxxx",
          p1: "",
          p2: "",
          p3: "",
          p4: "",
          res: ""
        }
      ]
    };
  } else if (refId === 1000000001) {
    return {
      steps: [
        {
          id: 1,
          action: "click",
          page: "login",
          type: "button",
          name: "submit",
          path: "//div[@id='submit']",
          p1: "",
          p2: "",
          p3: "",
          p4: "",
          res: ""
        },
        {
          id: 0,
          action: "input",
          page: "login",
          type: "input",
          name: "password",
          path: "//div[@id='pwd']xxxxxxxxxxxxxxxxxxxxxxxxx",
          p1: "",
          p2: "",
          p3: "",
          p4: "",
          res: ""
        }
      ]
    };
  } else {
    return {};
  }
}

function getGlobalParas() {
  return [
    {
      label: "{name}",
      value: "{name}"
    },
    {
      label: "{password}",
      value: "{password}"
    },
    {
      label: "{{url}}",
      value: "{{url}}"
    }
  ];
}

function getObjects() {
  return {
    login: {
      input: {
        name: "//div[@id='name']",
        password: "//div[@id='pwd']xxxxxxxxxxxxxxxxxxxxxxxxx"
      },
      button: {
        submit: "//div[@id='btn']"
      }
    }
  };
}

function getActions() {
  return [
    {
      label: "assert equal",
      value: "assert equal",
      object: true,
      p1: true,
      p1desc: "要比较的属性",
      p2: true,
      p2desc: "预期值",
      p3: false,
      p4: false,
      res: true
    },
    {
      label: "assert invisible",
      value: "assert invisible",
      object: true,
      p1: false,
      p2: false,
      p3: false,
      p4: false,
      res: true
    },
    {
      label: "assert not equal",
      value: "assert not equal",
      object: true,
      p1: true,
      p1desc: "要比较的属性",
      p2: true,
      p2desc: "预期值",
      p3: false,
      p4: false,
      res: true
    },
    {
      label: "assert visible",
      value: "assert visible",
      object: true,
      p1: false,
      p2: false,
      p3: false,
      p4: false,
      res: true
    },
    {
      label: "call api",
      value: "call api",
      object: false,
      p1: true,
      p1desc: "url",
      p2: true,
      p2desc: "type",
      p3: true,
      p3desc: "header(json, must have content-type)",
      p4: true,
      p4desc: "body",
      res: true
    },
    {
      label: "click",
      value: "click",
      object: true,
      p1: false,
      p2: false,
      p3: false,
      p4: false,
      res: false
    },
    {
      label: "get attribute",
      value: "get attribute",
      object: true,
      p1: true,
      p1desc: "要获取的属性",
      p2: false,
      p3: false,
      p4: false,
      res: true
    },
    {
      label: "get value",
      value: "get value",
      object: true,
      p1: true,
      p2: false,
      p3: false,
      p4: false,
      res: false
    },
    {
      label: "input",
      value: "input",
      object: true,
      p1: true,
      p1desc: "输入值",
      p2: false,
      p3: false,
      p4: false,
      res: false
    },
    {
      label: "navigate to",
      value: "navigate to",
      object: false,
      p1: true,
      p1desc: "url",
      p2: false,
      p3: false,
      p4: false,
      res: false
    },
    {
      label: "open browser",
      value: "open browser",
      object: false,
      p1: true,
      p1desc: "url",
      p2: false,
      p3: false,
      p4: false,
      res: false
    },
    {
      label: "select",
      value: "select",
      object: true,
      p1: true,
      p1desc: "要选的值",
      p2: false,
      p3: false,
      p4: false,
      res: false
    },
    {
      label: "switch to",
      value: "switch to",
      object: true,
      p1: false,
      p2: false,
      p3: false,
      p4: false,
      res: false
    },
    {
      label: "wait present",
      value: "wait present",
      object: true,
      p1: false,
      p2: false,
      p3: false,
      p4: false,
      res: false
    },
    {
      label: "wait disappear",
      value: "wait disappear",
      object: true,
      p1: false,
      p2: false,
      p3: false,
      p4: false,
      res: false
    }
  ];
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
