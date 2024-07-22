#### <div style="color: purple; text-align: center; font-size: 40px;">游戏 RPC服务</div>

<div style="color: blue; font-size: 20px; text-indent: 40px; font-family: 'JetBrains Mono NL', monospace;">
主要是通过 thrift 跨平台语言, 使用单个主机模拟两个服务器 (存储服务器, 匹配服务器) 和多个终端。通过游戏玩家的分数进行匹配，当玩家分数差距过大时，需要进行时间上的等待，在等待一定时间后还没有匹配成功，则将这两个玩家进行匹配游戏。
</div>

---

<div style="border: 10px solid lightgreen; line-height: 40px; padding: 10px;">
<ol>
    <li>多线程游戏玩家匹配</li>
    <li>分数和等待时间优化匹配</li>
    <li>模拟一个客户端, 两个服务器端进行通信</li>
    <li>在存储服务器上进行结果的存储</li>
    <li>根据每日时间打印匹配记录</li>
    <li>匹配记录包含玩家匹配成功时的时分秒以及玩家的 id, name, score.</li>
    <li>匹配记录包含在存储记录服务器中</li>
</ol>
</div>

---

## 结果

<div style="border: 10px solid lightgreen; padding: 10px;">
    <img src="data/images/截屏2024-05-14 17.25.53.png" style="border: 3px solid lightgreen;">
    <hr style="border: 1px solid black;">
    <img src="data/images/截屏2024-05-14 17.27.10.png" style="border: 3px solid lightgreen;">
</div>
