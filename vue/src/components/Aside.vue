<template>
  <el-menu
    :collapse="isCollapse"
    :collapse-transition="false"
    :default-openeds="['1']"
    :unique-opened="false"
    background-color="rgb(48,65,86)"
    router
    style="min-height: 100vh; overflow-x: hidden"
    text-color="#fff"
  >
    <div
      class="block"
      style="height: 60px; line-height: 60px; text-align: center"
    >
      <span v-show="!isCollapse" style="color: white">料号称重信息管理</span>
    </div>

    <el-submenu index="1">
      <template slot="title">
        <i class="el-icon-menu"></i>
        <span slot="title">称重信息</span>
      </template>
      <el-menu-item index="home">主页</el-menu-item>
      <el-menu-item index="testHome">测试页面</el-menu-item>
      <el-menu-item index="maintain">料号资料维护</el-menu-item>
      <el-menu-item index="history">称重历史记录</el-menu-item>
    </el-submenu>
    <el-submenu index="2">
      <template slot="title">
        <i class="el-icon-setting"></i>
        <span slot="title">设置</span>
      </template>
      <el-menu-item index="SerialPort">串口设置</el-menu-item>
    </el-submenu>
  </el-menu>
</template>

<script>
export default {
  name: "Aside",
  props: {
    isCollapse: Boolean,
  },
};
</script>

<style>
</style>