<template>
  <div id="app">

    <!-- <router-view/> -->
    <!--缓存想要缓存的页面，实现后退不刷新-->
    <!--加上v-if的判断，可以自定义想要缓存的组件，自定义在router里面-->
    <keep-alive>
      <router-view v-if="$route.meta.keepAlive" class="router-view">
      </router-view>
 </keep-alive>  
 <router-view v-if="!$route.meta.keepAlive" class="router-view" ></router-view>
  </div>
</template>
<style>

</style>
