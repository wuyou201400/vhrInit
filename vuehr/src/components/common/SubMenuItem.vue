<template>
  <div>
  <div v-if="item.children">
    <template v-if="item.children.length == 0">
      <el-menu-item :index="item.path" v-if="item.enabled">
        <i :class="item.iconCls" style="color: #20a0ff;width: 14px;"></i>
        {{item.name}}
      </el-menu-item>
    </template>
    <el-submenu :index="item.id+''" v-else>
      <template slot="title" >
        <i :class="item.iconCls" style="color: #20a0ff;width: 14px;"></i>
        {{item.name}}
      </template>
      <SubMenuItem v-for="(submenu,index) in item.children" v-if="submenu.enabled" :item="submenu" :key="index" />
    </el-submenu>
  </div>
  <template v-else>
    <el-menu-item :index="item.path" v-if="item.enabled">
      <i :class="item.iconCls" style="color: #20a0ff;width: 14px;"></i>
      {{item.name}}
    </el-menu-item>
  </template>
  </div>
</template>

<script>
  export default {
    name: "SubMenuItem",
    props: {
      item: {
        type: Object,
        required: true
      }
    }
  }
</script>

<style scoped>

</style>
