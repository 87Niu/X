import { GIF } from "./gif.js";
import { Player } from "./player.js";

export class Kyo extends Player {
    constructor(root, info) {
        super(root, info);

        this.init_animation();
    }

    init_animation() {
        let outer = this;
        let offsets = [0, -10, -10, -50, 0, 0, 0];
        for (let i = 0; i < 7; i ++) {
            let gif = GIF();
            gif.load(`/static/images/player/kyo/${i}.gif`);
            this.animations.set(i, {
                gif: gif,
                frame_cnt: 0,
                frame_rate: 5,
                loaded: false,
                scale: 0.8,
                offset_y: offsets[i],
            });

            gif.onload = function() {
                let obj = outer.animations.get(i);
                obj.loaded = true;
                obj.frame_cnt = gif.frames.length;
            }

        }

    }
}