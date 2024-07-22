import { AcGameObject } from './object.js';
import { Controller } from './controller.js';

export class GameMap extends AcGameObject {
    constructor(root) {
        super();
        this.root = root;

        this.$canvas = $(`<canvas width="1000" height="400" tabindex=0></canvas>`);
        this.ctx = this.$canvas[0].getContext('2d');

        this.root.$kof.append(this.$canvas);
        this.$canvas.focus();

        this.controller = new Controller(this.$canvas);


        this.root.$kof.append(`<div class="kof-head">
        <div class="kof-head-hp-0"><div><div></div></div></div>
        <div class="kof-head-timer">60</div>
        <div class="kof-head-hp-1"><div><div></div></div></div>
        </div>`);

        this.$timer = this.root.$kof.find('.kof-head-timer');
        this.time_left = 60000;
    }

    start() {

    }

    update() {
        this.time_left -= this.timedelta;
        if (this.time_left <= 0) {
            this.time_left = 0;
        
        }
        this.$timer.text(parseInt(this.time_left / 1000));
        this.render();
    }
    render() {
       
        this.ctx.clearRect(0, 0, this.ctx.canvas.width, this.ctx.canvas.height);
    }
}