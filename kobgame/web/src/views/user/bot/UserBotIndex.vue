<template>

    <BaseContent>mybot</BaseContent>

</template>



<script>
import BaseContent from '@/components/BaseContent.vue';
import axios from 'axios';
import { useStore } from 'vuex';

export default {
    components: {
        BaseContent
    },

    setup() {
        const store = useStore();
        // axios({
        //     url: "http://127.0.0.1:9999/user/bot/add",
        //     method: "POST",
        //     headers: {
        //         Authorization: "Bearer " + store.state.user.token,
        //         'Content-Type': 'application/json',
        //     },
        //     data: JSON.stringify({
        //         title: "标题1",
        //         description: "描述",
        //         content: "内容",
        //     })
        // }).then(resp => {
        //     console.log(resp.data);
        // }).catch(error => {
        //     console.log(error.data);
        // })
        // axios({
        //     url: "http://127.0.0.1:9999/user/bot/remove",
        //     method: "DELETE",
        //     headers: {
        //         Authorization: "Bearer " + store.state.user.token,
        //         'Content-Type': 'application/json',
        //     },
        //     data: {
        //         id: 3,
        //     }
        // }).then(resp => {
        //     console.log(resp.data);
        // }).catch(error => {
        //     console.log(error.data);
        // })
        axios({
            url: "http://127.0.0.1:9999/user/bot/getlist",
            method: "GET",
            headers: {
                Authorization: "Bearer " + store.state.user.token,
                'Content-Type': 'application/json',
            },
        }).then(resp => {
            console.log(resp.data);
        }).catch(error => {
            console.log(error.data);
        })
        // axios({
        //     url: "http://127.0.0.1:9999/user/bot/update",
        //     method: "POST",
        //     headers: {
        //         Authorization: "Bearer " + store.state.user.token,
        //         'Content-Type': 'application/json',
        //     },
        //     data: {
        //         id: 1,
        //         title: "修改的标题",
        //     }
        // }).then(resp => {
        //     console.log(resp.data);
        // }).catch(error => {
        //     console.log(error.data);
        // })
    }

}
</script>



<style scoped></style>