<template>
  <div id="app">
    <!-- <router-view/> -->
    <!--缓存想要缓存的页面，实现后退不刷新-->
    <!--加上v-if的判断，可以自定义想要缓存的组件，自定义在router里面-->
    <!-- 设置key为$route.fullPath之后，由于两个路由的fullPath不同，组件会被强制不复用 -->
    <keep-alive>
      <router-view v-if="$route.meta.keepAlive" :key="$route.fullPath">
      </router-view>
    </keep-alive>
    <router-view
      v-if="!$route.meta.keepAlive" :key="$route.fullPath"
    ></router-view>
  </div>
</template>
<style>
</style>
