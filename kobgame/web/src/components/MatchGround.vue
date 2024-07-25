<template>
    <div class="matchGround">
        <div class="row">
            <div class="col-6">
                <div class="user-photo">
                    <img :src="$store.state.user.photo" alt="">
                </div>
                <div class="user-username">
                    {{ $store.state.user.username }}
                </div>
            </div>

            <div class="col-6">

                <div class="user-photo">
                    <img :src="$store.state.pk.opponent_photo" alt="">
                </div>
                <div class="user-username">
                    {{ $store.state.pk.opponent_username }}
                </div>
            </div>

            <div class="col-12" style="padding-top: 15vh; text-align: center;">
                <button @click="clickMatchBtn" type="button" class="btn match-btn btn-lg">{{ matchBtnInfo }}</button>
            </div>
        </div>
    </div>
</template>

<script>
import { ref } from 'vue';
import { useStore } from 'vuex';

export default ({

    setup() {
        const store = useStore();
        let matchBtnInfo = ref("开始匹配");
        const clickMatchBtn = () => {
            if (matchBtnInfo.value === "开始匹配") {
                matchBtnInfo.value = "取消";
                store.state.pk.socket.send(JSON.stringify({
                    event: "stop-matchinmg",
                }))
            } else {
                matchBtnInfo.value = "开始匹配";
                store.state.pk.socket.send(JSON.stringify({
                    event: "start-matchinmg",
                }))
            }
        } 
        
        return {
            matchBtnInfo,
            clickMatchBtn
        }

    },
})
</script>


<style scoped>

div.matchGround {
    width: 60vw;
    height: 70vh;
    margin: 30px auto;
    background-color: rgba(50, 50, 50, 0.5);
}
div.user-photo {
    text-align: center;
    padding-top: 10vh;
}
div.user-photo > img {
    height: 10vh;
    width: 10vw;
    border-radius: 30%;
}
div.user-username {
    text-align: center;
    font-size: 24px;
    font-weight: 600;
    color: white;
}
.match-btn {
    width: 20vw;
    height: 10vh;
    background-color: pink;
}

</style>
