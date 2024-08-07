package com.kob.back.consumer.utils;

import com.alibaba.fastjson2.JSONObject;
import com.kob.back.consumer.WebSocketServer;
import com.kob.back.service.RecordService;
import lombok.Data;
import lombok.Getter;
import org.apache.coyote.http11.upgrade.UpgradeInfo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

@Getter
public class Game extends Thread {
    private final  Integer rows;
    private final Integer cols;
    private final Integer inner_walls_count;
    private final int[][] g;
    private final static int[] dx = {-1, 0, 1, 0}, dy = {0, 1, 0, -1};
    private final Player playerA, playerB;
    private Integer nextStepA = null;   // A的操作
    private Integer nextStepB = null;   // B的操作
    private ReentrantLock lock = new ReentrantLock();
    private String status = "playing";  // playing -> finished
    private String loser = "";  // all: 平局，A：A输，B：B输

    private static RecordService recordService;
    @Autowired
    public void setRecordService(RecordService recordService) {
        Game.recordService = recordService;
    }

    public Game(Integer rows, Integer cols, Integer inner_walls_count, Integer idA, Integer idB) {
        this.rows = rows;
        this.cols = cols;
        this.inner_walls_count = inner_walls_count;
        this.g = new int[rows][cols];
        playerA = new Player(idA, rows - 2, 1, new ArrayList<>());
        playerB = new Player(idB, 1, cols - 2, new ArrayList<>());
    }

    public void setNextStepA(Integer nextStepA) {
        lock.lock();
        try {
            this.nextStepA = nextStepA;
        } finally {
            lock.unlock();
        }
    }
    public void setNextStepB(Integer nextStepB) {
        lock.lock();
        try {
            this.nextStepB = nextStepB;
        } finally {

            lock.unlock();
        }
    }


//    public int[][] getG() {
//        return g;
//    }
//    public Player getPlayerA() {
//        return playerA;
//    }
//    public Player getPlayerB() {
//        return playerB;
//    }


    private boolean check_connectivity(int sx, int sy, int tx, int ty) {
        if (sx == tx && sy == ty) return true;
        g[sx][sy] = 1;

        for (int i = 0; i < 4; i ++ ) {
            int x = sx + dx[i], y = sy + dy[i];
            if (x >= 0 && x < this.rows && y >= 0 && y < this.cols && g[x][y] == 0) {
                if (check_connectivity(x, y, tx, ty)) {
                    g[sx][sy] = 0;
                    return true;
                }
            }
        }

        g[sx][sy] = 0;
        return false;
    }

    private boolean draw() {  // 画地图
        for (int i = 0; i < this.rows; i ++ ) {
            for (int j = 0; j < this.cols; j ++ ) {
                g[i][j] = 0;
            }
        }

        for (int r = 0; r < this.rows; r ++ ) {
            g[r][0] = g[r][this.cols - 1] = 1;
        }
        for (int c = 0; c < this.cols; c ++ ) {
            g[0][c] = g[this.rows - 1][c] = 1;
        }

        Random random = new Random();
        for (int i = 0; i < this.inner_walls_count / 2; i ++ ) {
            for (int j = 0; j < 1000; j ++ ) {
                int r = random.nextInt(this.rows);
                int c = random.nextInt(this.cols);

                if (g[r][c] == 1 || g[this.rows - 1 - r][this.cols - 1 - c] == 1)
                    continue;
                if (r == this.rows - 2 && c == 1 || r == 1 && c == this.cols - 2)
                    continue;

                g[r][c] = g[this.rows - 1 - r][this.cols - 1 - c] = 1;
                break;
            }
        }

        return check_connectivity(this.rows - 2, 1, 1, this.cols - 2);
    }

    public void createMap() {
        for (int i = 0; i < 1000; i ++ ) {
            if (draw())
                break;
        }
    }

    private void senAllMessage(String message) {
        WebSocketServer.users.get(playerA.getId()).sendMessage(message);
        WebSocketServer.users.get(playerB.getId()).sendMessage(message);
    }

    private boolean nextStep() {
        // 每秒五步操作，因此第一步操作是在200ms后判断是否接收到输入。并给地图初始化时间
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        /*
         * 个人理解：此循环循环了5000ms，也就是5s，前端是一秒移动5步，
         * 后端接收玩家键盘输入是5s内玩家的一个输入，若在一方先输入，
         * 一方还未输入，输入的一方多此操作，以最后一次为准。
         */
        // 因为会读玩家的nextStep操作，因此加锁
        for(int i = 0; i < 50; i++) {
            try {
                Thread.sleep(100);
                lock.lock();
                try {
                    if(nextStepA != null && nextStepB != null) {
                        playerA.getSteps().add(nextStepA);
                        playerB.getSteps().add(nextStepB);
                        return true;
                    }
                } finally {
                    lock.unlock();
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return false;
    }
    private String getMapString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < rows; i ++) {
            for (int j = 0; j < cols; j ++) {
                sb.append(g[i][j]);
            }
        }
        return sb.toString();
    }

    private void saveToDataBase() {
        Record record = new Record(null, playerA.getId(), playerA.getSx(), playerA.getSy(),
                playerB.getId(), playerB.getSx(), playerB.getSy(), playerA.getStepsString(), playerB.getStepsString(),
                getMapString(), loser, new Date(), new Date());
        recordService.save(record);
    }

    private void sendResult() {     // 向两名玩家发送游戏结果
        JSONObject resp = new JSONObject();
        resp.put("event", "result");
        resp.put("loser", loser);
        senAllMessage(resp.toJSONString());
    }

    private boolean check_valid(List<Cell> cellsA, List<Cell> cellsB) {
        int n = cellsA.size();
        Cell cell = cellsA.get(n - 1);
        // 如果是墙，则非法
        if(g[cell.x][cell.y] == 1) return false;

        for(int i = 0; i < n - 1; i++) {
            // 和蛇身是否重合
            if(cellsA.get(i).x == cell.x && cellsA.get(i).y == cell.y) {
                return false;
            }
        }

        // 遍历B除最后一个Cell
        for(int i = 0; i < n - 1; i++) {
            // 和B蛇身是否重合
            if(cellsB.get(i).x == cell.x && cellsB.get(i).y == cell.y) {
                return false;
            }
        }
        return true;
    }

    private void judge() {      // 判断两名玩家操作是否合法
        List<Cell> cellsA = playerA.getCells();
        List<Cell> cellsB = playerB.getCells();

        boolean validA = check_valid(cellsA, cellsB);
        boolean valibB = check_valid(cellsB, cellsA);

        if (!validA || !valibB) {
            status = "finished";
            if (!validA && !valibB) {
                loser = "all";
            } else if (!validA) {
                loser = "A";
            } else {
                loser = "B";
            }
        }
    }



    private void sendMove() {   // 向两名玩家传递移动信息
        // 因为需要读玩家的下一步操作，所以需要加锁
        lock.lock();
        try {
            JSONObject resp = new JSONObject();
            resp.put("event", "move");
            resp.put("a_direction", nextStepA);
            resp.put("b_direction", nextStepB);
            saveToDataBase();
            senAllMessage(resp.toJSONString());
            nextStepA = nextStepB = null;
        } finally {
            lock.unlock();
        }
    }


    @Override
    public void run() {
        for(int i = 0; i < 1000; i++) {
            // 是否获取到两条蛇的下一步操作
            if(nextStep()) {
                judge();
                if(status.equals("playing")) {
                    sendMove();
                } else {
                    sendResult();
                    break;
                }
            } else {
                status = "finished";
                // 因为读了nextStep操作，因此也要加锁
                lock.lock();
                // try finally是为了出异常也会抛锁
                try {
                    if(nextStepA == null && nextStepB == null) {
                        loser = "all";
                    } else if(nextStepA == null) {
                        loser = "A";
                    } else {
                        loser = "B";
                    }
                } finally {
                    lock.unlock();
                }
                // 游戏结束
                sendResult();
                break;
            }
        }
    }
}
