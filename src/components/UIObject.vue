<template>
  <div class="objects-editor">
    <div class="object">
      <el-cascader
        :options="uiobjects"
        @active-item-change="handleItemChange"
        @focus="getUIObjectPages"
        :props="props"
        :disabled="!editable">
      </el-cascader>
      <el-input placeholder="xpath" v-model="localUIobject.uiObjectPath" :disabled="!editable">
        <el-button
          slot="append"
          icon="el-icon-search"
          @click="searchUIObjectByXpath"
          :disabled="!editable">
        </el-button>
      </el-input>
    </div>
    <div class="object">
      <el-input placeholder="页面" v-model="localUIobject.uiObjectPage" :disabled="!editable"></el-input>
      <el-input placeholder="类型" v-model="localUIobject.uiObjectType" :disabled="!editable"></el-input>
      <el-input placeholder="名称" v-model="localUIobject.uiObjectName" :disabled="!editable"></el-input><br/>
      <el-button size="mini" @click="createUIObject" :disabled="!editable">添加</el-button><br/>
      <el-button size="mini" @click="createUIObject" :disabled="!editable">修改</el-button><br/>
    </div>
  </div>
</template>

<style lang="scss">
.object {
  display: flex;
  justify-content: left;
  align-items: left;
  .el-button {
    margin: 0px;
    padding: 3px;
  }
}
.el-cascader {
  height: 28px;
  width: 260px;
  margin-top: 0px;
  line-height: 28px;
  border: 0px;
}
.el-input {
  font-size: 12px;
  .el-input__inner {
    height: 28px;
    margin-top: 0px;
    padding: 0px;
    font-size: 12px;
  }
  .el-input__suffix {
    padding-right: 0px;
    height: 28px;
    line-height: 28px;
    .el-input__icon {
      line-height: 28px;
    }
  }
  .el-input-group__append {
    height: 28px;
    padding: 0px;
    font-size: 12px;
    line-height: 28px;
  }
}
.el-cascader-menus {
  font-size: 12px;
  width: 100px;
  margin: 0px;
  .el-cascader-menu__item {
    height: 25px;
  }
}
.el-popper[x-placement^=bottom] {
  margin: 3px;
}
.el-select-dropdown__empty {
  height: 25px;
  font-size: 12px;
  padding: 0px;
  width: 100px;
}
</style>

<script>
import {Message} from 'element-ui'
export default {
  props: ['uiobject', 'editable'],
  data () {
    return {
      uiobjects: [],
      localUIobject: this.uiobject,
      props: {
        value: 'label',
        children: 'cities'
      }
    }
  },
  methods: {
    async searchUIObjectByXpath () {
      this.localUIobject = await this.API.getUIObjectByXpath(this.localUIobject.uiObjectPath)
    },
    async createUIObject () {
      if (this.uiobject.uiObjectPage === '' ||
      this.uiobjectuiObjectType === '' ||
      this.uiobject.uiObjectName === '' ||
      this.uiobject.uiObjectPath === '') {
        Message.error({message: 'page type name xpath不能为空!'})
      } else {
        var strUIObj = JSON.stringify(this.uiobject)
        var newUIObj = JSON.parse(strUIObj)
        delete newUIObj.uiObjectId
        this.uiobject = await this.API.createUIObject(newUIObj)
      }
    },
    async getUIObjectPages () {
      console.log('afsag')
      this.uiobjects = await this.API.getUIPages()
    },
    handleItemChange (val) {
      console.log('active item:', val)
      setTimeout(_ => {
        if (val.indexOf('江苏') > -1 && !this.options2[0].cities.length) {
          this.options2[0].cities = [{
            label: '南京'
          }]
        } else if (val.indexOf('浙江') > -1 && !this.options2[1].cities.length) {
          this.options2[1].cities = [{
            label: '杭州'
          }]
        }
      }, 300)
    }
  }
}
</script>
