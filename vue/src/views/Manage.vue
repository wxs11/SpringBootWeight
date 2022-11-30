<template>
  <div>
    <!-- <img alt="logo" src="../assets/logo.png"> -->
    <el-container style="height: 100vh">
      <el-aside
          :width="sidewith + 'px'"
          style="
          background-color: rgb(238, 241, 246);
          overflow-x: hidden;
          padding: 0%;"
      >
        <AsideVue :isCollapse="isCollapse"/>
      </el-aside>

      <el-container>
        <el-header
            style=" border-bottom: 1px solid #ccc; ">
            <!-- <meta http-equiv="Content-Security-Policy" content="upgrade-insecure-requests" /> -->
          <HeaderVue :collapse="collapse" :collapseBtnClass="collapseBtnClass"/>
        </el-header>

        <el-main>
          <!--     当前页面的子路由显示 -->
          <router-view/>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<style>

</style>

<script>
// @ is an alias to /src
import AsideVue from '../components/Aside.vue'
import HeaderVue from '../components/Header.vue'


export default {
  name: "Manage",
  components: {AsideVue, HeaderVue},
  data() {
    return {
      collapseBtnClass: "el-icon-s-fold",
      isCollapse: false,
      sidewith: 200,
    };
  },
  methods: {
    collapse() {
      //点击收缩侧边栏触发
      this.isCollapse = !this.isCollapse;
      if (this.isCollapse) {
        //收缩
        this.collapseBtnClass = "el-icon-s-unfold";
        this.sidewith = 64;
      } else {
        // 展开
        this.collapseBtnClass = "el-icon-s-fold";
        this.sidewith = 200;
      }
    },
  },
};
</script>
