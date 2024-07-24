<template>
    <NavBar />
    <router-view />
</template>

<script>
// import axios from 'axios';
import NavBar from './components/NavBar.vue';
import "bootstrap/dist/css/bootstrap.min.css";
import "bootstrap/dist/js/bootstrap";
export default {
    components: {
        NavBar,
    },

    setup() {



        // axios({
        //     url: "http://127.0.0.1:9999/user/account/register",
        //     method: "POST",
        //     data: {
        //         username: "xxn",
        //         password: "123456",
        //         confirmPassword: "123456",
        //     }
        // }).then(resp => {
        //     console.log(resp);
        // }).catch(error => {
        //     console.log(error);
        // })

    }
}

</script>


<style>
body {
    background-image: url("./assets/images/background.png");
    background-size: cover;
}
</style>
