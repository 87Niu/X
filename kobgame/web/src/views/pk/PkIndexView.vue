<template>
    <PlayGround v-if="$store.state.pk.status === 'playing'" />
    <MatchGround v-if="$store.state.pk.status === 'matching'" />
    <ResultBoard v-if="$store.state.pk.loser !== 'none'">
</template>



<script>

import MatchGround from '@/components/MatchGround.vue';
import PlayGround from '@/components/PlayGround.vue';
import ResultBoard from '@/components/ResultBoard.vue';
import ResultBoard from '@/components/ResultBoard.vue';
import { onMounted, onUnmounted } from 'vue';
import { useStore } from 'vuex';

export default {
    components: {
        PlayGround,
        MatchGround,
        ResultBoard
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
                    store.commit("updateGameMap", data.game);
                } else if (data.event === "move") {
                    console.log(data);
                    const game = store.state.pk.gameObject;
                    const [snake0, snake1] = game.snakes;
                    snake0.set_direction(data.a_direction);
                    snake1.set_direction(data.b_direction);
                } else if (data.event === "result") {
                    console.log(data);
                    const game = store.state.pk.gameObject;
                    const [snake0, snake1] = game.snakes;

                    if(data.loser === "all" || data.loser === "A") {
                        snake0.status = "die";
                    }
                    if(data.loser === "all" || data.loser === "B") {
                        snake1.status = "die";
                    }
                    store.commit("updateLoser", data.loser);
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