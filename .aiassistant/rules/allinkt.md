---
apply: 始终
---

# All in Kotlin 项目架构文档

## 项目概述

"All in Kotlin" 是一个基于 Kotlin Multiplatform (KMP) 技术的全栈项目，旨在使用 Kotlin 语言构建跨平台应用程序。该项目实现了代码的最大化复用，支持 Android、iOS、Web 和桌面端等多个平台。

## 技术栈

- **核心语言**: Kotlin Multiplatform
- **前端框架**:
    - 移动端: Jetpack Compose
    - Web端: [kotlin wrappers](https://github.com/JetBrains/kotlin-wrappers),web 主流框架的kotlin官方封装库，包含react、mui、TanStack
- **后端框架**: Spring Boot
- **数据库**: PostgreSQL
- **ORM框架**: Exposed
- **构建工具**: Gradle (Kotlin DSL)
- **客户端依赖注入**: Koin
- **序列化**: kotlinx.serialization
- **代码生成**: Kotlin Symbol Processing (KSP)

## 项目结构

```
.
├── admin/                  # 后台管理系统 (React Web应用)
├── server/                 # 服务端应用 (Spring Boot)
├── composeApp/             # 多平台客户端 (Android/iOS/桌面端)
├── shared/                 # 跨平台共享代码模块
├── client/                 # 客户端专用模块
│   ├── ui/                 # 客户端UI组件模块
│   ├── net/                # 网络请求模块
│   ├── components/         # 通用ui组件
│   └── data/               # 数据管理层
├── ksp/                    # KSP代码生成器
│   ├── annotation/         # ksp注解定义
│   ├── composeApp/         # Compose导航生成
│   ├── server/             # 服务端代码生成
│   └── shared/             # 共享代码生成
├── kt-ai/                  # AI功能模块
└── buildPlugin/            # gradle构建插件
```

## 模块详细介绍

### admin (后台管理系统)
基于 React 的 Web 后台管理系统，大部分库均使用kotlin wrapper包含的kotlin封装库

关键技术点：
- 使用 [Material UI](https://mui.com/) 组件库
- 实现了基于 Token 的认证机制
- 使用了 TanStack
- 为kotlin wrapper未包含的库添加kotlin声明

### server (服务端)
基于 Spring Boot 的后端服务，使用 Exposed ORM 框架操作数据库。

主要特性：
- RESTful API 设计
- 使用 JWT 进行身份验证
- 支持 PostgreSQL 数据库
- 集成 Docker 部署配置

### composeApp (多平台客户端)
使用 Jetpack Compose 构建的多平台应用，支持 Android、iOS 和桌面端。

### shared (共享模块)
包含所有平台间共享的代码，如数据模型、API接口定义、工具类等。

主要组件：
- API 接口定义，API请求地址常量
- 数据模型 (VO)
- 工具类函数
- 常量定义

### client (客户端模块)
专为客户端应用设计的模块集合。

子模块：
- **ui**: 平台无关的 UI 模块
- **net**: 网络请求
- **components**: 通用 Compose 组件
- **data**: 客户端数据管理层

### ksp (代码生成)
使用 Kotlin Symbol Processing 实现代码自动生成。

功能包括：
- Compose 导航代码生成
- 服务端 Entity 到 VO 的转换
- vo生成字段名常量

### kt-ai (AI模块)
Kotlin Multiplatform AI 客户端。

## 命名规范

为了保持代码的一致性和可维护性，项目制定了以下命名规范：

1. 路由相关的 UI 文件以 `Route` 开头
2. 页面 Compose 函数以 `Screen` 结尾
3. React 组件以 `FC` 结尾
4. 通用组件按功能直接命名

## 开发环境配置

### 前提条件
- JDK 21 或更高版本
- Node.js 和 npm (用于 Web 前端)
- Android SDK (用于 Android 客户端)
- Xcode (用于 iOS 客户端，仅限 macOS)

## 代码生成机制

项目利用 KSP (Kotlin Symbol Processing) 实现代码自动生成，包括：

1. **导航路由生成**: 自动为带有 @NavRoute 注解的 Composable 函数生成导航代码
2. **VO 转换**: 在服务端自动将 Entity 转换为 VO 对象
3. **字段名共享**: 在共享模块中生成字段名常量，确保前后端一致性


### 网络请求
客户端和服务端之间通过共享的 API 接口定义进行通信，确保类型安全和一致性。

### 状态管理
使用 Koin 进行依赖注入，结合 ViewModel 进行状态管理。
