<template>
    <PlayGround v-if="$store.state.pk.status === 'playing'" />
    <MatchGround v-if="$store.state.pk.status === 'matching'" />
</template>



<script>

import MatchGround from '@/components/MatchGround.vue';
import PlayGround from '@/components/PlayGround.vue';
import { onMounted, onUnmounted } from 'vue';
import { useStore } from 'vuex';

export default {
    components: {
        PlayGround,
        MatchGround,
    },

    setup() {
        const store = useStore();

        const socketUrl = `ws://127.0.0.1:9999/websocket/${store.state.user.token}/`;

        let socket = null;
        onMounted(() => {
            store.commit("updateOpponent", {
                username: "我的对手",
                photo: "https://img2.baidu.com/it/u=1476729792,3813943312&fm=253&fmt=auto&app=120&f=JPEG?w=500&h=667"
            })
            socket = new WebSocket(socketUrl);
            
            socket.onopen = () => {    
                console.log("connected");
                store.commit("updateSocket", socket);
            }

            socket.onmessage = msg => {
                const data = JSON.parse(msg.data);
                console.log(data);
                if (data.event === "start-matching") {
                    store.commit("updateOpponent", {
                        username: data.opponent_username,
                        photo: data.opponent_photo,
                    });
                    setTimeout(() => {
                        store.commit("updateStatus", "playing");
                    }, 2000);
                    store.commit("updateGameMap", data.gamemap);
                }
            }

            socket.onclose = () => {
                console.log("disconnected");
                store.commit("updateStatus", "matching");
            }
        });
        onUnmounted(() => {
            socket.close();
        })
    }

}
</script>



<style scoped></style>