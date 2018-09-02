<template>
  <div class="objects-editor">
    <div class="object">
      <el-cascader
        :options="options2"
        @active-item-change="handleItemChange"
        :props="props"
      ></el-cascader>
      <el-input placeholder="xpath">
        <el-button slot="append" icon="el-icon-search"></el-button>
      </el-input>
    </div>
    <div class="object">
      <el-input placeholder="页面"></el-input>
      <el-input placeholder="类型"></el-input>
      <el-input placeholder="名称"></el-input><br/>
      <el-button size="mini">添加</el-button><br/>
    </div>
  </div>
</template>
<script>
export default {
  data () {
    return {
      options2: [{
        label: '江苏',
        cities: []
      }, {
        label: '浙江',
        cities: []
      }],
      props: {
        value: 'label',
        children: 'cities'
      }
    }
  },

  methods: {
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
