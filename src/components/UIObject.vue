<template>
  <div class="objects-editor">
    <div class="object">
      <el-cascader
        :options="getUIObjPages()"
        @active-item-change="handleItemChange"
        :props="searchProps"
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
import {mapGetters} from 'vuex'
import {Message} from 'element-ui'
export default {
  props: ['uiobject', 'editable'],
  data () {
    return {
      localUIobject: this.uiobject,
      searchProps: {
        value: 'label',
        children: 'objects'
      }
    }
  },
  watch: {
    uiobject: function () {
      this.localUIobject = this.uiobject
    }
  },
  methods: {
    ...mapGetters(['getUIObjPages']),
    async searchUIObjectByXpath () {
      // 不能直接修改props
      this.localUIobject = await this.API.getUIObjectByXpath(this.localUIobject.uiObjectPath)
    },
    async createUIObject () {
      if (this.uiobject.uiObjectPage === '' ||
      this.uiobject.uiObjectType === '' ||
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
    handleItemChange (val) {
      if (!this.getUIObjPages().length) {
        this.API.getUIObjectsByPage(val[0])
      }
      setTimeout(_ => {
        console.log('active item:', val[0])
        console.log(this.getUIObjPages()[0])
        if (val[0].indexOf('a') > -1 && !this.getUIObjPages()[0].objects.length) {
          this.getUIObjPages()[0].objects = [{
            label: '南京'
          }]
          console.log(this.getUIObjPages())
        } else if (val.indexOf('浙江') > -1 && !this.options2[1].objects.length) {
          this.options2[1].objects = [{
            label: '杭州'
          }]
        }
      }, 300)
    }
  }
}
</script>
