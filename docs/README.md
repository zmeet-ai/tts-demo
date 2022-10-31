## 语音合成WebSocket接口

### 创建连接

ws api： wss://ai.abcpen.com/v2/tts/streaming

### 开始请求

- client 向 server 端发送开始信号，获取本次连接的标识

| 字段   | 必选 | 类型   | 说明     |
| ------ | ---- | ------ | -------- |
| task   | 是   | string | tts      |
| signal | 是   | string | 请求类型 |

- 请求示例

```
{
    "task": "tts",
    "signal": "start"
}
```

- server 信息 server 端返回新连接的情况

| 字段    | 必选 | 类型   | 说明           |
| ------- | ---- | ------ | -------------- |
| status  | 是   | int    | TTS服务端状态  |
| signal  | 是   | string | 状态描述       |
| session | 是   | string | 本次连接的标识 |

```
{
    "status": 0, 
    "signal": "server ready"，
    "session": "f0bfbabd-3e04-41db-9ece-38ed8b7c5b49"
}
```

###  数据传送

client和server建立连接之后，client端向server端发送请求。

- client 信息 发送待合成文本的base64格式数据到服务端

| 字段   | 必选 | 类型   | 说明                        |
| ------ | ---- | ------ | --------------------------- |
| text   | 是   | string | 待合成文本内容              |
| spk_id | 否   | int    | 发音人id，未使用到，默认：0 |

- 请求示例

```json
{
    "text": "无论是互联网巨头，还是刚起步的创业公司，都在竞相努力成为元宇宙这条充满无限可能性赛道的领先者",
    "spk_id": "yuanxi"
}
```

- server 信息 处理client端发送的请求，每产生一段音频，返回一段音频，直至返回结束。

| 字段   | 必选 | 类型   | 说明                                                         |
| ------ | ---- | ------ | ------------------------------------------------------------ |
| status | 是   | int    | 返回数据包是否为最后一个包， 1表示非最后一个包，2表示最后一个包 |
| audio  | 是   | string | 返回音频的base64                                             |

- 返回示例

```
{
    "status": 1,
    "audio": <base64>
}
```

### 结束请求

收到最后一个数据包后，需要发送给服务端一个结束的命令，通知服务端销毁该链接的相关资源。

- client 信息

| 字段    | 必选 | 类型   | 说明               |
| ------- | ---- | ------ | ------------------ |
| task    | 是   | string | 语音任务           |
| signal  | 是   | string | 请求类型           |
| session | 是   | string | 需要结束的本次连接 |

请求示例：

```
{
    "task": "tts",
    "signal": "end",
    "session": "f0bfbabd-3e04-41db-9ece-38ed8b7c5b49"
}
```

- server 信息

server 端信息接收到结束信息之后，返回响应并结束连接。

| 字段    | 必选 | 类型   | 说明           |
| ------- | ---- | ------ | -------------- |
| status  | 是   | int    | TTS服务端状态  |
| signal  | 是   | string | 状态描述       |
| session | 是   | string | 断开连接的标识 |

返回示例：

```
{
    "status": 0, 
    "signal": "connection will be closed",
    "session": "f0bfbabd-3e04-41db-9ece-38ed8b7c5b49"
}
```