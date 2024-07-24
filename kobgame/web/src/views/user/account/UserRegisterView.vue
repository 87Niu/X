<template>

<BaseContent>
        <div class="row justify-content-md-center">
            <div class="col-3">
                <form @submit.prevent="register">
                    <div class="mb-3">
                        <label for="username" class="form-label">用户名</label>
                        <input v-model="username" type="text" class="form-control" id="username" placeholder="请输入用户名">
                    </div>
                    <div class="mb-3">
                        <label for="password" class="form-label">密码</label>
                        <input v-model="password" type="password" class="form-control" id="password"
                            placeholder="请输入密码">
                    </div>
                    <div class="mb-3">
                        <label for="confirmPassword" class="form-label">重复密码</label>
                        <input v-model="confirmPassword" type="password" class="form-control" id="confirmPassword"
                            placeholder="请再次输入密码">
                    </div>
                    <div class="error-message">
                        {{ error_message }}
                    </div>
                    <button type="submit" class="btn btn-primary">提交</button>
                </form>
            </div>
        </div>
    </BaseContent>
</template>



<script>
import BaseContent from '@/components/BaseContent.vue';
import { ref } from 'vue';
import axios from 'axios';
import router from '@/router';
export default {

    components: {
        BaseContent
    },

    setup() {
        let username = ref('');
        let password = ref('');
        let confirmPassword = ref('');

        let error_message = ref('');

        const register = () => {
            axios({
                url: "http://127.0.0.1:9999/user/account/register",
                method: "POST",
                headers: {
                    'Content-Type': 'application/json'
                },
                data: JSON.stringify({
                    username: username.value,
                    password: password.value,
                    confirmPassword: confirmPassword.value,
                })
            }).then((result) => {
                if (result.data.error_message === "success") {
                    router.push({name: 'user_account_login'});
                } else {
                    error_message.value = result.data.error_message;
                }
            })
        }


        return {
            username, 
            password,
            confirmPassword,
            error_message,
            register,
        }

    }

}
</script>



<style scoped>

button {
    width: 100%;
}
div.error-message {
    color: red;
    justify-content: center;
}
</style>