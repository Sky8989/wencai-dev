#javaweb_sftc

顺丰同城速递 JavaWeb 端代码仓库

---

## 服务器配置信息

### SSH 配置
- IP : 120.76.207.187
- 域名 : sf.dankal.cn
- 端口 : 22
- 用户 : root
- 密码 : Dankal13

### 数据库配置
- 数据库选型 : MySQL 5.7 
- 用户名 : dankal
- 密码 : 0D9F8640-3FB1-4A56-9E26-646F71EE2E45
- 端口 : 3306

### Web 服务器配置：

Tomcat 监听 8080 端口，Nginx 监听 80 端口，Nginx 把 80 端口的请求反向代理转发给 Tomcat，项目只需要用 80 端口访问即可。
    
工作目录 : /data/wwwroot/sf.dankal.cn/
    