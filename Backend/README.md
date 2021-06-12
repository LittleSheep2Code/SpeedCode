# SpeedCode-Server

## 描述

`SpeedCode-Server` 是 `SpeedCode-Website` 和 `SpeedCode-Client` 的前置服务器项目


## Getting Start

听到这里你是不是激动的搓搓手了呢？

如果有，那么我们开始吧！

0. 检查配置环境
   - 环境要求：
     - JDK 8+
     - 1MB+ Network
     - Docker
     - [Judge0](https://github.com/judge0/judge0)


1. 下载项目 Jar
   
2. 安装 Judge0
```shell
# 请确保安装了 docker 与 docker-compose

# 下载 Judge0
wget https://github.com/judge0/judge0/releases/download/v1.13.0/judge0-v1.13.0.zip
unzip judge0-v1.13.0.zip

# 安装/启动 Judge0
cd judge0-v1.13.0
docker-compose up -d db redis
sleep 10s
docker-compose up -d
sleep 5s

# 如果 pull judge0-server 时发生错误，可以使用一下语句
# 之后再执行 安装/启动 Judge0 命令
# docker pull judge0/judge0
```

3. 运行服务器 Jar

```shell
# git clone https://gitee.com/LittleSheeper/SpeedCode-Backend.git
# 克隆完成后

# 切换目录
cd SpeedCode-Backend

# 运行(使用 Tab 自动补全)
java -jar <文件名>
```

   

4. 更换客户端配置
   - 详见子项目文档
   - 重新打包并运行

Getting Start 完成 ✅


## 标准

在介绍各种功能之前，我有必要说一说代码提交标准。

如果您想给该项目提交新功能的话，请务必耐心读完本章！

这是一个 EmailSender 类

```java
package SpeedCodeBKD.Utils.Processor;

// ...

public class EmailSender {
    
    @Autowried
    private static int ValueForExample = 200;

    public void send_email(Boolean is_rendered) {
        String contentForSend;
        
        // ...
    }
}
```

你发现了吗?
 1. 参数与函数需采用下划线的方式命名
 2. 类名需要以大驼峰的方式命名
 3. 常量需要以大驼峰大方式命名
 4. 变量需要小驼峰的方式命名

## 文档

### 权限划分

| 等级       | 名称                                       |  描述 |
| ----------| ---- | ------------------------------------------ |
|-256       | 任何人 | 任何人的权限，表示此 Api 可可以被任何人访问，包括封禁的人 |

#### 用户授权类

Prefix: `/user-tools/authorization/`

##### 创建用户

###### 描述

Full Url: `/user-tools/authorization/register`

Url: `/register`

###### 参数

需要 Query 参数

| 参数名     | 描述                                       | 需求 |
| ---------- | ------------------------------------------ | ---- |
| username   | 需注册用户的用户名                         | 是   |
| password   | 需注册用户的密码                           | 是   |
| email      | 需注册用户的邮箱                           | 是   |
| acceptCode | 邮箱验证是发送的验证码，需要完成注册需填写 | 否   |

**提示：**`acceptCode` 虽然是选填参数，但要完成注册需要填写

###### 请求例

```http
# 请求邮箱验证码
POST localhost:20020/user-tools/authorization/register/?username=xxx&email=xxx@xxx.com&password=xxx
```

```http
# 完成注册
POST localhost:20020/user-tools/authorization/register/?username=xxx&email=xxx@xxx.com&password=xxx&acceptCode=000000
```

###### 补充

此方法在第一次请求的时候会创建一个缓冲用户，若不完成邮箱验证，会在 2 小时后自动销毁

第二次方法会生成一个未激活用户，若在 7 天内[不激活]()，会自动销毁



## Api 制定标准与概念

#### 状态码

| 代码 | 代号              | 描述                                 |
| ---- | ----------------- | ------------------------------------ |
| 200  | Completed         | 当操作**完成且成功**时返回           |
| 201  | Refuse processing | 当请求**接受但无需或无法处理**时返回 |
| 400  | Bad request       | 错误：**请求为错误**的一方           |
| 500  | Server Error      | 错误：**服务器为错误**的一方         |

#### Status

| 代码      | 代号         | 描述            |
| --------- | ------------ | --------------- |
| Completed | 200          | Status 界的 200 |
| Warning   | Bad request  | Status 界的 400 |
| Error     | Server Error | Status 界的 500 |

#### 请求需要拥有的

1. `result` 或 `warnReason` 或 `errReason` (`String` )：处理后的结果或错误原因
2. `operating` (`String`)：操作的名字
3. `status`(`String`)：状态(见 [状态](#Status))
4. `status_code`(`Int`)：状态码(见 [状态码](#状态码))

#### 请求结果事例

通常我们会使用 `JSONObject`  或  `dict`  来处理 **请求返回**

```json
{
    "result": {
        "date": "2021年 2月13日 星期六 13时54分14秒 CST",
        "ver": "v1.0",
        "documentation": ""
    },
    "operating": "getUserAgreement",
    "status": "Completed",
    "statusCode": 200
}
```

