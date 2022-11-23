## 语音合成WebSocket接口

## 接口要求

集成实时语音转写API时，需按照以下要求。

| 内容     | 说明                                                         |
| :------- | ------------------------------------------------------------ |
| 请求协议 | http[s] (为提高安全性，强烈推荐https)                        |
| 请求地址 | http[s]: //tts.yitutech.com//v2/tts/streaming?{请求参数} *注：服务器IP不固定，为保证您的接口稳定，请勿通过指定IP的方式调用接口，使用域名方式调用* |
| 接口鉴权 | 签名机制，详见 [signa生成](#signa生成)                       |
| 响应格式 | 统一采用JSON格式                                             |
| 开发语言 | 任意，只要可以向笔声云服务发起http[s]请求的均可              |



## 接口调用流程

*注：* 若需配置IP白名单，请发送邮件到support@abcpen.com

实时语音转写接口调用包括两个阶段：握手阶段和实时通信阶段。

### 握手阶段

接口地址

```text
    ws://tts.yitutech.com//v2/tts/streaming?{请求参数}
    或
    wss://tts.yitutech.com//v2/tts/streaming?{请求参数}
```

参数格式

```text
    key1=value1&key2=value2…（key和value都需要进行urlencode）
```

参数说明

情感参数，支持高兴，热情，愤怒，悲哀等情感的调整最近发布，请耐心等待。

| 参数             | 类型   | 必须 | 说明                                                         | 示例                                                         |
| :--------------- | :----- | :--- | :----------------------------------------------------------- | :----------------------------------------------------------- |
| appid            | string | 是   | 笔声开放平台应用ID                                           | 595f23df                                                     |
| ts               | string | 是   | 当前时间戳，从1970年1月1日0点0分0秒开始到现在的秒数          | 1512041814                                                   |
| signa            | string | 是   | 加密数字签名（基于HMACSHA1算法）                             | IrrzsJeOFk1NGfJHW6SkHUoN9CU=                                 |
| audio_samplerate | string | 否   | 音频采样率，有"8000", "16000", "44100", "48000"这四种选择，模式是“16000” | “160000”                                                     |
| audio_encode     | string | 否   | 音频编码格式，有"pcm", "aac", "mpeg2", "opus", "flac"这五种选择，请选择其中一种编码格式， 默认“mpeg2” | “mpeg2”, 常规的mp3编码；详细的音频编码格式，参考上述[支持音频详情](#支持音频详情) |
| model            | string | 否   | 支持如下模特语音选择 <br/>yunxiao<br/>yunni<br/>yunbei<br/>yunyang<br/>yunxia<br/>yunxi<br/>yunjian<br/>yunyi | 默认是"yunxia"                                               |
| locale           | string | 否   | 国家语言，默认是“CN”, 支持多国语言合成                       |                                                              |




TTS模型列表

| 模特名称 | 模特特点                                                  | 男声/女声                                          |
| -------- | --------------------------------------------------------- | -------------------------------------------------- |
| 云霄     | [试听声音](https://zos.abcpen.com/zos/models/yunxiao.mp3) | 女声，成熟女声，声音柔和纯美                       |
| 云逸     | [试听声音](https://zos.abcpen.com/zos/models/yunyi.mp3)   | 女生（少女），少女声音，柔和纯美                   |
| 云剑     | [试听声音](https://zos.abcpen.com/zos/models/yunjian.mp3) | 男声，央视播音，富有热情                           |
| 云曦     | [试听声音](https://zos.abcpen.com/zos/models/yunxi.mp3)   | 男声，抖音配音常见风格，声音较快，充满时尚         |
| 云夏     | [试听声音](https://zos.abcpen.com/zos/models/yunxia.mp3)  | 男声（男童），抖音配音常见风格，充满激情，热情四射 |
| 云阳     | [试听声音](https://zos.abcpen.com/zos/models/yunyang.mp3) | 男声，和“云剑”相比，更正式，更有新闻联播的风格     |
| 云北     | [试听声音](https://zos.abcpen.com/zos/models/yunbei.mp3)  | 女生，东北风格语音，有调侃说单口相声的风格         |
| 云妮     | [试听声音](https://zos.abcpen.com/zos/models/yunni.mp3)   | 女生，东北风格语音，比“云北”更具备调侃的风格       |
| 云中     | [试听声音](https://zos.abcpen.com/zos/models/yunni.mp3)   | 待完善                                             |
| 云百     | [试听声音](https://zos.abcpen.com/zos/models/yunni.mp3)   | 待完善                                             |





#### signa生成

1.获取baseString，baseString由appid和当前时间戳ts拼接而成，假如appid为595f23df，ts为1512041814，则baseString为

> 595f23df1512041814

2.对baseString进行MD5，假如baseString为上一步生成的595f23df1512041814，MD5之后则为

> 0829d4012497c14a30e7e72aeebe565e

3.以apiKey为key对MD5之后的baseString进行HmacSHA1加密，然后再对加密后的字符串进行base64编码。
假如apiKey为d9f4aa7ea6d94faca62cd88a28fd5234，MD5之后的baseString为上一步生成的0829d4012497c14a30e7e72aeebe565e，
则加密之后再进行base64编码得到的signa为

> IrrzsJeOFk1NGfJHW6SkHUoN9CU=

备注：

- apiKey：接口密钥，在应用中添加实时语音转写服务时自动生成，调用方注意保管；
- signa的生成公式：HmacSHA1(MD5(appid + ts), api_key)，具体的生成方法参考本git实例代码；

####请求示例

```text
	wss://tts.yitutech.com//v2/tts/streaming?appid=595f23df&ts=1512041814&signa=IrrzsJeOFk1NGfJHW6SkHUoN9CU=&pd=edu
```

### 创建连接

ws api： wss://tts.yitutech.com/v2/tts/streaming


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