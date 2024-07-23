import { createRouter, createWebHistory } from 'vue-router'
import PkIndexView from '@/views/pk/PkIndexView.vue'
import NotFound from '@/views/error/NotFound.vue'
import RanklistIndexView from '@/views/ranklist/RanklistIndexView.vue'
import RecordIndexView from '@/views/record/RecordIndexView.vue'
import UserBotIndex from '@/views/user/bot/UserBotIndex.vue' 
const routes = [
    {
        path: "/",
        name: "home",
        redirect: "/pk/",
    },
    {
        path: "/pk/",
        name: "pk_index",
        component: PkIndexView,
    },
    {
        path: "/record/",
        name: "record_index",
        component: RecordIndexView,
    },
    {
        path: "/ranklist/",
        name: "ranklist_index",
        component: RanklistIndexView,
    },
    {
        path: "/user/bot/",
        name: "user_bot_index",
        component: UserBotIndex,
    },
    {
        path: "/404/",
        name: "404",
        component: NotFound,
    },
    {
        path: "/:catchAll(.*)",
        redirect: "/404/"
    },
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router