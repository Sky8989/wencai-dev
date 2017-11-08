# javaweb_sftc

顺丰同城速递 JavaWeb 端代码仓库

---

## 服务器配置信息

### 开发环境

#### SSH 配置

- IP : `120.76.207.187`
- 域名 : `sftc.dankal.cn`
- 端口 : `22`
- 用户 : `root`
- 密码 : `Dankal13`

#### 数据库配置

- 数据库选型 : MySQL 5.7 
- 用户名 : `dankal`
- 密码 : `0D9F8640-3FB1-4A56-9E26-646F71EE2E45`
- 端口 : `3306`

#### Web服务器配置：

Tomcat 监听 8080 端口，Nginx 监听 80 端口，Nginx 把 80 端口的请求反向代理转发给 Tomcat，项目只需要用 80 端口访问即可。
    
工作目录 : /data/wwwroot/sf.dankal.cn/
    
---
    
### 生产环境

#### SSH 跳板机

- IP : `202.104.112.165`
- 端口 : `2223`
- 用户 : `deploy`
- 密码 : 不允许密码登录

```vim
$ ssh -A -p 2223 deploy@202.104.112.165
```

#### SSH 小程序专用机

- 主机 : `wx01`
- 域名 : `api-wxc.sf-rush.com` （配备SSL证书，指向`wx01`的80端口）
- 已配置 sudo 权限，只能用 `sudo yum install` 安装依赖。

```vim
$ ssh -A wx01
```

#### SSH 说明

- 必须通过SSH登录跳板机，再从跳板机登录专用机。
- 如果从跳板机登录专用机提示因为公钥拒绝，则在本地开发机需要执行以下命令，将 key 添加到 ssh-agent 中：

```vim
$ ssh-add ~/.ssh/id_rsa
```

> 由 Bingo 负责与顺丰的海龙对接，暂时只添加了 Bingo 的公钥，只有 Bingo 可以登录跳板机进而登录专用主机。

## 项目部署

### 开发环境

开发环境已经搭建 Jenkins，持续构建，无须手动发版本，只要把代码 Push 到 dev 分支即可。

### 生产环境

生产环境需要 SSH 连上顺丰小程序专用机 wx01，然后进入 `/home/deploy/javaweb_sftc/` 目录。

现在定时器已经默认开启，无需手动调用接口开启。

## 项目相关

### 订单状态

| state  | 状态 |
| --- | --- |
| WAIT_FILL | 待好友填写 |
| ALREADY_FILL | 好友已填写 |
| INIT | 下单 |
| PAYING | 支付中 |
| WAIT_HAND_OVER	 | 待揽件 |
| DELIVERING | 派送中 |
| FINISHED | 已完成 |
| ABNORMAL | 不正常的 |
| CANCELED | 取消单 |
| WAIT_REFUND | 等待退款 |
| REFUNDING | 退款中 |
| REFUNDED | 已退款 |
| OVERTIME | 填写超时、付款超时 |

#### 订单状态周期

##### 同城寄付

下单 -> `INIT`
调起支付 -> `PAYING` (payed: false) -> 等待支付完成
完成支付 -> `PAYING` (payed: true) -> 呼叫小哥
派到小哥 -> `WAIT_HAND_OVER` 待揽件
小哥揽件 -> `DELIVERING` 派送中
送达 -> `FINISHED` 已完成

##### 同城到付

下单 -> `INIT` -> 呼叫小哥
派到小哥 -> `WAIT_HAND_OVER` 待揽件
小哥揽件 -> `DELIVERING` 派送中
送达 -> 小哥提供二维码，用户扫码支付 (`PAYING`) 
支付完成 -> `FINISHED` 已完成

#### 我的订单列表，关键词搜索订单状态

| keyword | state |
| --- | ---- |
| 已 | FINISHED |
| 已完成 | FINISHED |
| 完成 | FINISHED |
| 待填写 | WAIT_FILL |
| 已填写 | ALREADY_FILL |
| 下单 | INIT |
| 待支付 | INIT + PAYING |
| 支付 | PAYING |
| 支付中 | PAYING |
| 待揽件 | WAIT_HAND_OVER |
| 揽件 | WAIT_HAND_OVER |
| 退款 | WAIT_REFUND + REFUNDING + REFUNDED |
| 已退款 | REFUNDED |
| 退款中 | REFUNDING |
| 待退款 | WAIT_REFUND |
| 派送 | DELIVERING |
| 派送中 | DELIVERING |
| 派件 | DELIVERING |
| 派件中 | DELIVERING |
| 取消 | CANCELED |
| 已取消 | CANCELED |
| 取消订单 | CANCELED |
| 订单取消 | CANCELED |


	
	
	
	
	



