`nginx.conf` 和 `default.conf` 在 Nginx 配置中扮演不同的角色。理解它们的区别有助于更好地管理和配置 Nginx 服务器。

### `nginx.conf`

- **位置**: 通常位于 `/etc/nginx/nginx.conf`。
- **作用**: 这是 Nginx 的主配置文件，包含全局设置和基本的 HTTP 服务器配置。
- **内容**:
    - **全局指令**: 如 `user`, `worker_processes`, `error_log`, `pid` 等。
    - **事件模块**: 如 `events` 块，定义如何处理连接请求。
    - **HTTP 模块**: 如 `http` 块，包含 MIME 类型、日志格式、默认类型等全局设置。
    - **包含其他配置文件**: 使用 `include` 指令包含其他配置文件，如 `/etc/nginx/conf.d/*.conf` 或 `/etc/nginx/sites-enabled/*`。

### `default.conf`

- **位置**: 通常位于 `/etc/nginx/conf.d/default.conf` 或 `/etc/nginx/sites-available/default` 并通过符号链接指向 `/etc/nginx/sites-enabled/default`。
- **作用**: 这是一个示例或默认的虚拟主机配置文件，用于定义一个基本的 HTTP 服务器。
- **内容**:
    - **监听端口**: 如 `listen 80;`。
    - **服务器名称**: 如 `server_name _;`（通配符匹配所有域名）。
    - **根目录**: 如 `root /usr/share/nginx/html;`。
    - **索引文件**: 如 `index index.html index.htm;`。
    - **位置块**: 如 `location / { ... }`，定义如何处理不同路径的请求。

### 示例

#### `nginx.conf`

```nginx
user nginx;
worker_processes auto;

error_log /var/log/nginx/error.log;
pid /run/nginx.pid;

events {
    worker_connections 1024;
}

http {
    include /etc/nginx/mime.types;
    default_type application/octet-stream;

    log_format main '$remote_addr - $remote_user [$time_local] "$request" '
                    '$status $body_bytes_sent "$http_referer" '
                    '"$http_user_agent" "$http_x_forwarded_for"';
    access_log /var/log/nginx/access.log main;

    sendfile on;
    keepalive_timeout 65;
    gzip on;

    include /etc/nginx/conf.d/*.conf;
}
```

#### `default.conf`

```nginx
server {
    listen 80;
    server_name _;

    root /usr/share/nginx/html;
    index index.html index.htm;

    location / {
        try_files $uri $uri/ =404;
    }

    error_page 404 /404.html;
    location = /404.html {
        internal;
    }
}
```

### 关键区别

1. **层次结构**:
    - `nginx.conf` 是顶层配置文件，包含全局设置和对其他配置文件的引用。
    - `default.conf` 是一个具体的虚拟主机配置文件，定义了一个基本的 HTTP 服务器。

2. **用途**:
    - `nginx.conf` 用于定义全局参数和加载其他配置文件。
    - `default.conf` 用于定义特定站点的配置，包括监听端口、根目录、索引文件等。

3. **修改建议**:
    - 修改全局设置时，编辑 `nginx.conf`。
    - 修改特定站点的配置时，编辑相应的虚拟主机配置文件（如 `default.conf` 或自定义的站点配置文件）。
