### docker compose
* docker compose up会重建所有容器
* 在同一个项目里默认共享容器网络

````
# 启动所有服务（如果尚未运行）
docker compose -p aliyun -f docker-compose.yaml -f docker-compose.backend.yaml up -d

# 仅更新 backend.yml 中的服务
# docker compose -p aliyun -f docker-compose.backend.yaml restart

````

