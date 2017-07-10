#javaweb_sftc

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

