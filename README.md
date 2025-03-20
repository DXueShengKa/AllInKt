# all in kotlin

- [:admin](admin) 后台管理
- [:server](server) 服务器
- [:composeApp](composeApp) 多平台客户端（安卓、ios、桌面端）
- [:shared](shared) 全平台代码共享模块  
- [:client:ui](client/ui) 客户端纯ui模块，不含业务逻辑
- [:client:net](client/net) 客户端和后台管理的网络请求配置
- [:client:components](client/components) 一些compose组件
- [:client:data](client/data) 客户端的数据模块
- [:allKsp](allKsp) ksp生成代码，包含了生成compose导航，服务器的entity转vo，shared的生成字段名等
- [issApp](iosApp) ios的启动工程，xcode选择这个目录打开
- [:ksp-annotation](ksp-annotation) 给ksp使用的额外注解
- [:kt-ai](kt-ai) kotlin多平台ai客户端

#### 命名规则
- 带有路由的ui代码文件，使用Route开头，路由值都使用Route开头方便ide统一提示
- Compose中页面函数使用Screen结尾，表示这个compose组件基本渲染了当前整个屏幕
- react对外暴露的使用FC结尾,表示是个react组件
- 不包含业务通用组件直接按照组件功能命名

#### 开发提示

##### npm淘宝镜像源
> npm config set registry https://registry.npmmirror.com/

运行开发版本js需要在gradle里加上 --continuous
