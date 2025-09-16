CampusHub 校园点评应用 API 文档 v3
版本: 3.0

更新日期: 2025-09-15

变更说明:

项目更名为 CampusHub，接口功能全面扩展。

新增完整的管理端 API，涵盖分类、点评项、评论和用户的 CRUD 操作。

用户对象 User 增加 role 和 status 字段。

所有需要权限的接口都明确标注了 (Admin Only)。

基础 URL
/api

认证
在请求头中携带 Authorization: Bearer <token>。

1. 数据模型
   User (用户)
   {
   "id": 1,
   "name": "张三",
   "avatarUrl": "...",
   "role": "admin", // "user" 或 "admin"
   "status": "active" // "active" 或 "disabled"
   }

Category (分类)
{
"id": 1,
"name": "课程点评"
}

Item (点评项)
{
"id": 101,
"categoryId": 1,
"name": "高级软件工程",
"imageUrl": "...",
"description": "一门深入探讨软件开发生命周期的课程。",
"metadata": { // 存储额外信息
"teacher": "李四教授",
"credits": 3
}
}

Review (评论)
{
"id": 1001,
"itemId": 101,
"score": 9,
"content": "内容很棒，作业有点多。",
"user": { /_ User 对象 _/ },
"createdAt": "...",
"isOwner": true
}

2. 用户端 API
   2.1 认证
   GET /auth/me: 获取当前登录用户信息（包含 role）。

2.2 分类
GET /categories: 获取所有分类列表。

2.3 点评项
GET /items?categoryId={id}: 根据分类 ID 获取点评项列表。

GET /items/{id}: 获取单个点评项的详细信息。

2.4 评论
GET /reviews?itemId={id}: 获取某点评项的所有评论。

POST /reviews: 发表新评论 (需要认证)。

PUT /reviews/{id}: 修改自己的评论 (需要认证)。

DELETE /reviews/{id}: 删除自己的评论 (需要认证)。

3. 管理端 API (Admin Only)
   3.1 仪表盘
   GET /admin/stats: 获取核心统计数据。

成功响应 (200 OK):

{
"code": 200,
"data": {
"userCount": 1500,
"itemCount": 350,
"reviewCount": 8900
}
}

3.2 分类管理
POST /admin/categories: 新增分类。

PUT /admin/categories/{id}: 修改指定分类。

DELETE /admin/categories/{id}: 删除指定分类。

3.3 点评项管理
GET /admin/items: 获取所有点评项（支持分页和按分类筛选）。

POST /admin/items: 新增点评项。

PUT /admin/items/{id}: 修改指定点评项。

DELETE /admin/items/{id}: 删除指定点评项。

3.4 评论管理
GET /admin/reviews: 获取所有评论（支持分页、按用户/点评项筛选）。

DELETE /admin/reviews/{id}: (管理员) 删除任意一条评论。

3.5 用户管理
GET /admin/users: 获取所有用户列表（支持分页和按用户名搜索）。

PUT /admin/users/{id}: 修改用户信息（如 role, status）。

成功响应 (200 OK):

{
"code": 200,
"message": "User updated successfully.",
"data": {
"id": 2,
"name": "李四",
"role": "admin",
"status": "active"
}
}
