import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'

window.__VUE_PROD_DEVTOOLS__ = false;
window.__VUE_PROD_HYDRATION_MISMATCH_DETAILS__ = false;

createApp(App).use(store).use(router).mount('#app')
