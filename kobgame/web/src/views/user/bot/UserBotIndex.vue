<template>

    <div class="container">
        <div class="row">
            <div class="col-3">
                <div class="card" style="margin-top: 40px;">
                    <div class="card-body">
                        <img :src="$store.state.user.photo" alt="" style="width: 100%;">
                    </div>
                </div>
            </div>
            <div class="col-9">
                <div class="card" style="margin-top: 40px;">
                    <div class="card-header">
                        <span style="font-size: 130%;">我的Bot</span>
                        <button type="button" class="btn btn-primary float-end" data-bs-toggle="modal"
                            data-bs-target="#addBotBtn">
                            创建Bot
                        </button>

                        <!-- Modal -->
                        <div class="modal fade" id="addBotBtn" tabindex="-1">
                            <div class="modal-dialog modal-xl">
                                <div class="modal-content">
                                    <div class="modal-header">
                                        <h1 class="modal-title fs-5" id="exampleModalLabel">创建Bot</h1>
                                        <button type="button" class="btn-close" data-bs-dismiss="modal"
                                            aria-label="Close"></button>
                                    </div>
                                    <div class="modal-body">
                                        <div class="mb-3">
                                            <label for="add-bot-title" class="form-label">名称</label>
                                            <input v-model="botadd.title" type="text" class="form-control"
                                                id="add-bot-title" placeholder="请输入Bot名称">
                                        </div>
                                        <div class="mb-3">
                                            <label for="add-bot-description" class="form-label">简介</label>
                                            <textarea v-model="botadd.description" class="form-control"
                                                id="add-bot-description" rows="3" placeholder="请输入Bot简介"></textarea>
                                        </div>
                                        <div class="mb-3">
                                            <label for="add-bot-content" class="form-label">代码</label>
                                            <VAceEditor v-model:value="botadd.content"  lang="c_cpp"
                                                theme="textmate" style="height: 300px" :options="{
                                                    enableBasicAutocompletion: true, //启用基本自动完成
                                                    enableSnippets: true, // 启用代码段
                                                    enableLiveAutocompletion: true, // 启用实时自动完成
                                                    fontSize: 13, //设置字号
                                                    tabSize: 4, // 标签大小
                                                    showPrintMargin: false, //去除编辑器里的竖线
                                                    highlightActiveLine: true,
                                                }" />



                                        </div>
                                    </div>
                                    <div class="modal-footer">
                                        <div class="error-message">{{ botadd.error_message }}</div>
                                        <button type="button" class="btn btn-secondary"
                                            data-bs-dismiss="modal">取消</button>
                                        <button @click="add_bot" type="button" class="btn btn-primary" data-bs-dismiss="modal" >创建</button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="card-body">
                        <table class="table table-striped table-hover">
                            <thead>
                                <tr>
                                    <th>名称</th>
                                    <th>创建时间</th>
                                    <th>操作</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr v-for="bot in bots" :key="bot.id">
                                    <td>{{ bot.title }}</td>
                                    <td>{{ bot.createTime }}</td>
                                    <td>
                                        <button type="button" class="btn btn-secondary" style="margin-right: 20px"
                                            data-bs-toggle="modal"
                                            :data-bs-target="'#update-bot-modal-' + bot.id">修改</button>
                                        <button @click="remove_bot(bot)" type="button"
                                            class="btn btn-danger">删除</button>
                                    </td>
                                    <!-- Modal -->
                                    <div class="modal fade" :id="'update-bot-modal-' + bot.id" tabindex="-1">
                                        <div class="modal-dialog modal-xl">
                                            <div class="modal-content">
                                                <div class="modal-header">
                                                    <h1 class="modal-title fs-5">创建Bot</h1>
                                                    <button type="button" class="btn-close" data-bs-dismiss="modal"
                                                        aria-label="Close"></button>
                                                </div>
                                                <div class="modal-body">
                                                    <div class="mb-3">
                                                        <label for="add-bot-title" class="form-label">名称</label>
                                                        <input v-model="botadd.title" type="text" class="form-control"
                                                            id="add-bot-title" placeholder="请输入Bot名称">
                                                    </div>
                                                    <div class="mb-3">
                                                        <label for="add-bot-description" class="form-label">简介</label>
                                                        <textarea v-model="botadd.description" class="form-control"
                                                            id="add-bot-description" rows="3"
                                                            placeholder="请输入Bot简介"></textarea>
                                                    </div>
                                                    <div class="mb-3">
                                                        <label for="add-bot-content" class="form-label">代码</label>
                                                        <VAceEditor v-model:value="bot.content"
                                                            lang="c_cpp" theme="textmate" style="height: 300px"
                                                            :options="{
                                                                enableBasicAutocompletion: true, //启用基本自动完成
                                                                enableSnippets: true, // 启用代码段
                                                                enableLiveAutocompletion: true, // 启用实时自动完成
                                                                fontSize: 13, //设置字号
                                                                tabSize: 4, // 标签大小
                                                                showPrintMargin: false, //去除编辑器里的竖线
                                                                highlightActiveLine: true,
                                                            }" />



                                                    </div>
                                                </div>
                                                <div class="modal-footer">
                                                    <div class="error-message">{{ botadd.error_message }}</div>
                                                    <button type="button" class="btn btn-secondary"
                                                        data-bs-dismiss="modal">取消</button>
                                                    <button @click="update_bot(bot)" type="button"
                                                        class="btn btn-primary">保存修改</button>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>

</template>



<script>
import { reactive, ref } from 'vue';
import axios from 'axios';
import { useStore } from 'vuex';
import { VAceEditor } from 'vue3-ace-editor';
import ace from 'ace-builds';
import { Modal } from 'bootstrap/dist/js/bootstrap'
import 'ace-builds/src-noconflict/mode-c_cpp';
import 'ace-builds/src-noconflict/mode-json';
import 'ace-builds/src-noconflict/theme-chrome';
import 'ace-builds/src-noconflict/ext-language_tools';

export default {

    components: {
        VAceEditor
    },

    setup() {
        ace.config.set(
            "basePath",
            "https://cdn.jsdelivr.net/npm/ace-builds@" +
            require("ace-builds").version +
            "/src-noconflict/")


        const store = useStore();

        let bots = ref([]);

        const botadd = reactive({
            title: "",
            description: "",
            content: "",
            error_message: "",
        });

        const refresh_bots = () => {
            axios({
                url: "http://127.0.0.1:9999/user/bot/getlist",
                method: "GET",
                headers: {
                    Authorization: "Bearer " + store.state.user.token,
                    'Content-Type': 'application/json',
                },
            }).then(resp => {
                bots.value = resp.data;
            })
        }
        refresh_bots();

        const add_bot = () => {
            botadd.error_message = "";
            axios({
                url: "http://127.0.0.1:9999/user/bot/add",
                method: "POST",
                headers: {
                    Authorization: "Bearer " + store.state.user.token,
                    'Content-Type': 'application/json',
                },
                data: JSON.stringify({
                    title: botadd.title,
                    description: botadd.description,
                    content: botadd.content,
                })
            }).then(resp => {
                if (resp.error_message === "success") {
                    botadd.title = "";
                    botadd.content = "";
                    botadd.description = "";
                    refresh_bots();
                } else {
                    refresh_bots();
                    botadd.error_message = resp.data.error_message;
                }
            });
        }

        const update_bot = (bot) => {
            botadd.error_message = "";
            axios({
                url: "http://127.0.0.1:9999/user/bot/update",
                method: "POST",
                headers: {
                    Authorization: "Bearer " + store.state.user.token,
                    'Content-Type': 'application/json',
                },
                data: {
                    id: bot.id,
                    title: botadd.title,
                    description: botadd.description,
                    content: botadd.content,
                }
            }).then(resp => {
                if (resp.data.error_message === "success") {
                    refresh_bots();
                    Modal.getInstance('#update-bot-modal-' + bot.id).hide();
                } else {
                    botadd.error_message = resp.data.error_message;
                }
            })

        }

        const remove_bot = (bot) => {
            axios({
                url: "http://127.0.0.1:9999/user/bot/remove",
                method: "DELETE",
                headers: {
                    Authorization: "Bearer " + store.state.user.token,
                    'Content-Type': 'application/json',
                },
                data: {
                    id: bot.id,
                }
            }).then(resp => {
                if (resp.data.error_message === "success") {
                    refresh_bots();
                }

            })
        }

        return {
            bots,
            botadd,
            add_bot,
            remove_bot,
            update_bot,
        }

    }

}
</script>



<style scoped>
div.error-message {
    color: red;
}
</style>