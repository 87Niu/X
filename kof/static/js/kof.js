import { GameMap } from './gamemap.js';
import { Kyo } from './kyo.js';

export class KOF {

    constructor(id) {
        this.$kof = $('#' + id);

        this.game_map = new GameMap(this);

        this.players = [
            new Kyo(this, {
                id: 0,
                x: 200,
                y: 0,
                width: 50,
                height: 80,
                color: 'orange',
            }),
            new Kyo(this, {
                id: 1,
                x: 700,
                y: 0,
                width: 50,
                height: 80,
                color: 'orange',
            }),
        ]
    }
}