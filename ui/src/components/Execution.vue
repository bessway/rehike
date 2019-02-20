<template>
  <div>
    <div class="env">
      <el-select v-model=selectedOs filterable placeholder="操作系统">
        <el-option
          v-for="item in os"
          :key="item"
          :value="item">
        </el-option>
      </el-select>
      <el-select v-model=sldBrowser filterable placeholder="浏览器">
        <el-option
          v-for="item in browsers"
          :key="item"
          :value="item">
        </el-option>
      </el-select>
      <el-select v-model=selectedAgent filterable placeholder="任务名">
        <el-option
          v-for="item in agents"
          :key="item.jobName"
          :value="item"
          :label="item.jobName"
          :disabled="item.status === 1? false : true">
        </el-option>
      </el-select>
      <el-select v-model=selectedEnv filterable placeholder="环境">
        <el-option
          v-for="item in envs"
          :key="item"
          :value="item">
        </el-option>
      </el-select>
      <el-select v-model=selectedData filterable placeholder="数据">
        <el-option
          v-for="item in dataVersions"
          :key="item"
          :value="item">
        </el-option>
      </el-select>
      <el-button size="mini" @click="startJob">执行</el-button>
      <el-button size="mini" @click="debug">刷新</el-button>
    </div>
    <el-table stripe height="480"
      :data="tasks"
      :default-sort = "{prop: 'createTime', order: 'descending'}"
      ref="execs"
      :cell-style = "{'text-align':'center'}">
      <el-table-column label="任务名" min-width="80" header-align="center" prop="jenkinsJobName"></el-table-column>
      <el-table-column label="任务编号" min-width="80" header-align="center" prop="jenkinsBuildId"></el-table-column>
      <el-table-column label="创建时间" min-width="100" header-align="center" prop="createTime"></el-table-column>
      <el-table-column label="状态" min-width="80" header-align="center" prop="taskStatus"></el-table-column>
      <el-table-column label="成功" min-width="40" header-align="center" prop="passedCnt"></el-table-column>
      <el-table-column label="失败" min-width="40" header-align="center" prop="failedCnt"></el-table-column>
      <el-table-column label="操作" min-width="40" header-align="center">
        <template slot-scope="scope">
          <el-button size="mini" @click="viewDetail(scope.row)" v-if=isAableToViewReport(scope.row)>详情</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>
<style lang="scss">
.env {
  display: flex;
  justify-content: left;
  align-items: left;
  margin-top: 5px;
}
.el-input {
  font-size: 12px;
  .el-input__inner {
    height: 28px;
    margin-top: 0px;
    padding: 0px;
    font-size: 12px;
  }
}
</style>
<script>
import {mapGetters, mapMutations} from 'vuex'

export default {
  data () {
    return {
      selectedOs: '',
      sldBrowser: '',
      selectedAgent: {},
      selectedEnv: 'uat',
      envs: ['qa', 'uat', 'prd'],
      tasks: [],
      selectedData: 'default',
      dataVersions: ['default'],
      newTask: null
    }
  },
  mounted: function () {
    this.loadAgents()
    this.loadTasks()
  },
  watch: {
    newTask: function () {
      if (this.newTask !== null) {
        this.tasks.push(this.newTask)
      }
    }
  },
  computed: {
    os () {
      if (this.getAgents() === undefined || this.getAgents() === null) {
        return []
      }
      var temp = {}
      this.getAgents().forEach(item => {
        temp[item.osType + ' ' + item.osVersion] = ''
      })
      return Object.keys(temp)
    },
    browsers () {
      if (this.selectedOs === '') {
        return []
      }
      var temp = {}
      this.getAgents().forEach(item => {
        var os = item.osType + ' ' + item.osVersion
        if (os === this.selectedOs) {
          temp[item.browserType + ' ' + item.browserVersion] = ''
        }
      })
      return Object.keys(temp)
    },
    agents () {
      var agent = []
      this.getAgents().forEach(item => {
        var browser = item.browserType + ' ' + item.browserVersion
        var os = item.osType + ' ' + item.osVersion
        if ((os === this.selectedOs || this.selectedOs === '') &&
         (browser === this.sldBrowser || this.sldBrowser === '')) {
          agent.push(item)
        }
      })
      if (agent.length === 1) {
        this.setSelectedAgent(agent[0])
      }
      return agent
    }
  },
  methods: {
    ...mapGetters(['getAgents', 'getCheckedTests']),
    ...mapMutations(['setAgents']),

    async startJob () {
      var task = {}
      task['jenkinsJobName'] = this.selectedAgent.jobName
      task['logLevel'] = 'info'
      task['env'] = this.selectedEnv
      task['dataVersion'] = this.selectedData
      task['browserType'] = this.sldBrowser
      task['tests'] = {}
      this.getCheckedTests().forEach(test => {
        task['tests'][test.testId] = ''
      })
      this.newTask = await this.API.startJob(task)
    },
    setSelectedAgent (agent) {
      if (agent.status === 1) {
        this.selectedAgent = agent
      }
    },
    async loadAgents () {
      this.setAgents(await this.API.getAgents())
    },
    async loadTasks () {
      // 默认只取最近一周的
      this.tasks = await this.API.getTasks()
    },
    viewDetail (row) {
      window.open(row.reportUrl)
    },
    isAableToViewReport (row) {
      if (row.taskStatus === 'SUCCESS' || row.taskStatus === 'FAILED' || row.taskStatus === 'EXCEPTION') {
        return true
      } else {
        return false
      }
    },
    debug () {
      console.log(this.getCheckedTests())
    }
  }
}
</script>
