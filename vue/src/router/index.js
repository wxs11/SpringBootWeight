import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

// 获取原型对象上的push函数
const originalPush = VueRouter.prototype.push
// 修改原型对象中的push方法
VueRouter.prototype.push = function push(location) {
    return originalPush.call(this, location).catch(err => err)
}


const routes = [
    {
        path: '/',
        name: 'manage',
        component: () => import('../views/Manage.vue'),
        meta: { title: '框架主体' },
        redirect: "/home",
        children: [
            {
                path: 'home',
                name: 'home',
                component: () => import('../views/Home.vue'),
                meta: {
                    title: '主页', breadNumber: 1,
                    keepAlive: true
                },
            },
            {
                path: 'testHome',
                name: 'testHome',
                component: () => import('../views/TestHome.vue'),
                meta: {
                    title: '测试页面', breadNumber: 1,
                    keepAlive: true
                },
            },
            {
                path: 'history',
                name: 'history',
                component: () => import('../views/History.vue'),
                meta: {
                    title: '历史称重记录',
                    breadNumber: 2,
                },
            },
            {
                path: 'maintain',
                name: 'maintain',
                component: () => import('../views/Maintain.vue'),
                meta: {
                    title: '料号资料维护',
                    breadNumber: 3,
                },
            },


        ],
    },
    {
        path: '/',
        name: 'set',
        component: () => import('../views/Manage.vue'),
        meta: { title: '框架主体' },
        redirect: "/serialport",
        children: [
            {
                path: 'serialport',
                name: 'serialport',
                component: () => import('../views/SerialPort.vue'),
                meta: {
                    title: '串口',
                    breadNumber: 4,
                    keepAlive: true
                },

            }
        ]
    }


]

const router = new VueRouter({
    routes,
    mode: 'history'
})

export default router
