---
trigger: always_on
---

# All in Kotlin 项目规则文档

## 项目概述

"All in Kotlin" 是一个基于 Kotlin Multiplatform (KMP) 技术的全栈项目，使用 Kotlin 语言构建跨平台应用程序，支持 Android、iOS、Web (React) 和桌面端 (JVM)。

## 技术栈

### 核心
- **语言**: Kotlin Multiplatform
- **构建工具**: Gradle 9 (Kotlin DSL)
- **JDK**: 21+

### 前端 (admin - 后台管理系统)
- **框架**: React (Kotlin/JS)
- **UI库**: Material UI (MUI) via kotlin-wrappers
- **状态管理**: TanStack Query / React Query
- **路由**: React Router
- **HTTP客户端**: Ktor Client

### 后端 (server)
- **框架**: Spring Boot 4
- **ORM**: Exposed 1.0
- **数据库**: PostgreSQL
- **认证**: JWT
- **API风格**: RESTful

### 移动端/桌面端 (composeApp)
- **UI框架**: Jetpack Compose Multiplatform
- **支持平台**: Android、iOS、JVM Desktop
- **导航**: 自定义导航 (KSP生成)
- **依赖注入**: Koin

### 共享代码 (shared)
- **序列化**: kotlinx.serialization
- **数据验证**: Arrow
- **网络**: Ktor Client (共享API定义)

### 代码生成 (ksp)
- **技术**: Kotlin Symbol Processing
- **功能**: 导航路由生成、VO转换、字段名常量生成

## 项目结构

```
.
├── admin/                  # 后台管理系统 (Kotlin/JS + React)
├── server/                 # 服务端 (Spring Boot 4)
├── composeApp/             # 多平台客户端 (Compose)
├── shared/                 # 跨平台共享代码
├── client/                 # 客户端专用模块
│   ├── ui/                 # UI组件
│   ├── net/                # 网络请求
│   ├── components/         # 通用组件
│   └── data/               # 数据管理
├── ksp/                    # KSP代码生成器
│   ├── annotation/         # 注解定义
│   ├── composeApp/         # Compose导航生成
│   ├── server/             # 服务端代码生成
│   └── shared/             # 共享代码生成
├── kt-ai/                  # AI功能模块
└── buildPlugin/            # Gradle构建插件
```

## 命名规范

### 通用规范
| 场景 | 命名规则 | 示例 |
|------|----------|------|
| 路由页面文件 | `Route` + 名称 | `RouteUser.kt` |
| Compose页面函数 | 名称 + `Screen` | `UserListScreen()` |
| React组件 | 名称 + `FC` | `UserTableFC()` |
| 通用组件 | 功能命名 | `PrimaryButton.kt` |
| API接口 | 动词 + 资源 | `getUserList()` |
| 数据类(VO) | 后缀`VO` | `UserVO` |
| 数据库实体 | 后缀`Entity` | `UserEntity` |
| Protobuf字段 | 可空类型 | `string? name = 1;` |

### 文件组织
- 每个文件只包含一个主要类/函数
- 相关文件放在同一包下
- 测试文件与被测试文件同名 + `Test`后缀

## 各模块开发规范

### admin (Kotlin/JS React)

#### 组件定义
```kotlin
// 使用 FC 后缀命名
val UserTableFC = FC<UserTableProps> { props ->
    // 组件实现
}

// Props 定义
external interface UserTableProps : Props {
    var users: List<UserVO>
    var onDelete: (Long) -> Unit
}
```

#### Hooks使用
- 使用 `useState`、`useEffect` 等 React Hooks
- 数据获取使用 TanStack Query
- 表单处理使用合适的 Hook

#### MUI组件
```kotlin
// 使用 kotlin-wrappers 的 MUI绑定
import mui.material.Box
import mui.material.Button
import mui.material.Typography
import mui.icons.material.Delete
```

### server (Spring Boot 4)

#### Controller规范
```kotlin
@RestController
@RequestMapping("/api/users")
class UserController(
    private val userService: UserService
) {
    @GetMapping
    fun list(): List<UserVO> = userService.findAll()
    
    @PostMapping
    fun create(@RequestBody dto: UserCreateDTO): UserVO = userService.create(dto)
}
```

#### Service规范
```kotlin
@Service
class UserService(
    private val userRepository: UserRepository
) {
    @Transactional
    fun create(dto: UserCreateDTO): UserVO {
        // 业务逻辑
    }
}
```

#### Exposed ORM
```kotlin
// Entity定义
object Users : LongIdTable("users") {
    val name = varchar("name", 255)
    val email = varchar("email", 255)
    val createdAt = datetime("created_at")
}

// Entity类
class UserEntity(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<UserEntity>(Users)
    
    var name by Users.name
    var email by Users.email
    var createdAt by Users.createdAt
}
```

### composeApp (Compose Multiplatform)

#### Screen定义
```kotlin
@NavRoute("user/list")  // KSP生成导航代码
@Composable
fun UserListScreen(
    viewModel: UserViewModel = koinViewModel()
) {
    val users by viewModel.users.collectAsState()
    
    LazyColumn {
        items(users) { user ->
            UserItem(user)
        }
    }
}
```

#### ViewModel规范
```kotlin
class UserViewModel(
    private val userRepository: UserRepository
) : ViewModel() {
    
    private val _users = MutableStateFlow<List<UserVO>>(emptyList())
    val users: StateFlow<List<UserVO>> = _users.asStateFlow()
    
    fun loadUsers() {
        viewModelScope.launch {
            _users.value = userRepository.findAll()
        }
    }
}
```

#### Koin依赖注入
```kotlin
// Module定义
val appModule = module {
    single { UserRepository(get()) }
    viewModel { UserViewModel(get()) }
}

// 使用
val viewModel: UserViewModel = koinViewModel()
```

### shared (共享模块)

#### API定义
```kotlin
interface UserApi {
    @GET("/api/users")
    suspend fun list(): List<UserVO>
    
    @POST("/api/users")
    suspend fun create(@Body dto: UserCreateDTO): UserVO
}
```

#### VO定义
```kotlin
@Serializable
data class UserVO(
    val id: Long,
    val name: String,
    val email: String
)
```

#### 数据验证 (Arrow)
```kotlin
fun UserCreateDTO.validate(): Validated<NonEmptyList<ValidationError>, UserCreateDTO> {
    return name.validateNotBlank()
        .zip(email.validateEmail()) { _, _ -> this }
}
```

## 代码生成 (KSP)

### 导航路由
为带有 `@NavRoute` 注解的 Composable 生成导航代码：
```kotlin
@NavRoute("user/detail/{userId}")
@Composable
fun UserDetailScreen(userId: Long) { }
```

### VO字段常量
生成字段名字符串常量，确保前后端一致性：
```kotlin
// 生成的代码
object UserVOFields {
    const val ID = "id"
    const val NAME = "name"
    const val EMAIL = "email"
}
```

### Entity转VO
服务端自动生成 Entity 到 VO 的转换代码。

## 构建命令

```bash
# 构建全部
./gradlew build

# 运行服务端
./gradlew :server:bootRun

# 运行后台管理 (开发模式)
./gradlew :admin:jsBrowserDevelopmentRun --continuous

# 运行桌面端
./gradlew :composeApp:run

# 运行Android
./gradlew :composeApp:installDebug

# 代码检查
./gradlew ktlintCheck

# 自动格式化
./gradlew ktlintFormat
```

## 环境要求

- **JDK**: 21 或更高版本
- **Node.js**: 18+ (用于Web前端)
- **Android SDK**: API 34+
- **Xcode**: 15+ (macOS, 用于iOS)
- **PostgreSQL**: 15+

## 重要约束

1. **KSP生成代码**: 不要手动修改生成的代码
2. **API版本**: 共享模块中的API定义是前后端的唯一契约
3. **数据库迁移**: 使用Exposed的迁移工具管理schema变更
