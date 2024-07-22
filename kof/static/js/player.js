import { AcGameObject } from "./object.js";

export class Player extends AcGameObject {
    constructor(root, info) {
        super();
        this.root = root;
        this.ctx = this.root.game_map.ctx;

        this.id = info.id;
        this.x = info.x;
        this.y = info.y;
        this.width = info.width;
        this.height = info.height;
        this.color = info.color;

        this.vx = 0;
        this.vy = 0;
        this.grivate = 50;

        this.speedx = 300;
        this.speedy = -1000;

        this.status = 3;

        this.direction = 1;

        this.pressed_keys = this.root.game_map.controller.pressed_keys;
        this.animations = new Map();

        this.frame_current_cnt = 0;


        this.hp = 100;
        this.$hp = this.root.$kof.find(`.kof-head-hp-${this.id}>div`);
        this.$hp_in = this.$hp.find('div');

    }

    start() {}

    update_move() {
        this.vy += this.grivate;
        this.x += (this.vx * this.timedelta) / 1000;
        this.y += (this.vy * this.timedelta) / 1000;

        if (this.y > 270) {
            this.y = 270;
            this.vy = 0;
            if (this.status === 3) this.status = 0;
        }

        if (this.x < 0) {
            this.x = 0;
        } else if (this.x + this.width > this.ctx.canvas.width) {
            this.x = this.ctx.canvas.width - this.width;
        }
    }
    update_direction() {
        if (this.status === 6) return ;
        let players = this.root.players;
        if (players[0] && players[1]) {
            let me = this,
                you = players[1 - this.id];
            if (me.x < you.x) me.direction = 1;
            else me.direction = -1;
        }
    }
    update_control() {
        let a, w, d, space;
        if (this.id === 0) {
            a = this.pressed_keys.has("a");
            w = this.pressed_keys.has("w");
            d = this.pressed_keys.has("d");
            space = this.pressed_keys.has(" ");
        } else {
            a = this.pressed_keys.has("ArrowLeft");
            w = this.pressed_keys.has("ArrowUp");
            d = this.pressed_keys.has("ArrowRight");
            space = this.pressed_keys.has("Enter");
        }

        if (this.status === 0 || this.status === 1) {
            if (space) {
                this.status = 4;
                this.vx = 0;
                this.frame_current_cnt = 0;
            } else if (w) {
                if (a) {
                    this.vx = -this.speedx;
                } else if (d) {
                    this.vx = this.speedx;
                } else {
                    this.vx = 0;
                }
                this.status = 3;
                this.vy = this.speedy;
            } else if (a) {
                this.vx = -this.speedx;
                this.status = 1;
            } else if (d) {
                this.vx = this.speedx;
                this.status = 1;
            } else {
                this.vx = 0;
                this.status = 0;
            }
        }
    }
    update_attack() {
        if (this.status === 4 && this.frame_current_cnt === 18) {
            let players = this.root.players;
            let me = this,
                you = players[1 - this.id];
            let r1;
            if (me.direction > 0) {
                r1 = {
                    x1: me.x + me.width,
                    y1: me.y + 10,
                    x2: me.x + me.width + 40,
                    y2: me.y + 10 + 20,
                };
            } else {
                r1 = {
                    x1: me.x - 40,
                    y1: me.y + 10,
                    x2: me.x,
                    y2: me.y + 10 + 20,
                };
            }

            let r2 = {
                x1: you.x,
                y1: you.y,
                x2: you.x + you.width,
                y2: you.y + you.height,
            };

            if (this.is_collision(r1, r2)) {
                you.is_attack();
            }
        }
    }
    is_collision(r1, r2) {
        if (Math.max(r1.x1, r2.x1) > Math.min(r1.x2, r2.x2)) return false;
        if (Math.max(r1.y1, r2.y1) > Math.min(r1.y2, r2.y2)) return false;
        return true;
    }
    is_attack() {
        if (this.status === 6) return ;
        this.status = 5;
        this.frame_current_cnt = 0;
        this.vx = 0; 

        this.hp = Math.max(this.hp - Math.random() * 20, 0);

        this.$hp_in.animate({
            width: this.$hp.parent().width() * this.hp / 100
        }, 200);
        this.$hp.animate({
            width: this.$hp.parent().width() * this.hp / 100
        }, 500);

        if (this.hp <= 0) {
            this.status = 6;
            this.vx = 0;
            this.frame_current_cnt = 0;
        }
    }
    update() {
        this.update_direction();
        this.update_move();
        this.update_control();
        this.update_attack();

        this.render();
    }

    render() {
        let status = this.status;
        if (this.direction === 1 && this.direction * this.vx < 0) status = 2;
        if (this.direction === -1 && this.direction * this.vx < 0) status = 2;

        let obj = this.animations.get(status);

        if (obj && obj.loaded) {
            if (this.direction > 0) {
                let k =
                    Math.floor(this.frame_current_cnt / obj.frame_rate) %
                    obj.frame_cnt;
                let image = obj.gif.frames[k].image;
                this.ctx.drawImage(
                    image,
                    this.x,
                    this.y + obj.offset_y,
                    image.width * obj.scale,
                    image.height * obj.scale
                );
            } else {
                this.ctx.save();
                this.ctx.scale(-1, 1);
                this.ctx.translate(-this.root.game_map.ctx.canvas.width, 0);

                let k =
                    Math.floor(this.frame_current_cnt / obj.frame_rate) %
                    obj.frame_cnt;
                let image = obj.gif.frames[k].image;
                this.ctx.drawImage(
                    image,
                    this.root.game_map.ctx.canvas.width - this.x - this.width,
                    this.y + obj.offset_y,
                    image.width * obj.scale,
                    image.height * obj.scale
                );
                this.ctx.restore();
            }

            if (status === 4 || status === 5 || status === 6) {
                if (this.frame_current_cnt === (obj.frame_cnt - 1) * obj.frame_rate) {
                    if (status === 6) {
                        this.frame_current_cnt --;
                    } else {
                        this.status = 0;
                    }
                }
            }
        }

        this.frame_current_cnt++;
    }
}
