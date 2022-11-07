# 语音合成API文档
* [Https语音合成接口](https://github.com/zmeet-ai/tts-demo/blob/main/README.md)
* [Websocket语音合成接口](https://github.com/zmeet-ai/tts-demo/blob/main/docs/README.md)

# ZmeetAI 语音合成优势

* 多种模特语音发音模型，每种模型分别带有不同的感情音色风格
* 支持Http[s]风格和WebsocketAPI风格，适应不同的工作场景
* 支持背景音替换

# 语音合成API文档

## 接口说明

支持各种感情的男女声音，支持实时和离线文本合成tts语音；支持单模特声音变声，语音速率调整，语音音量大小调整；支持自定义语音模型。

## 接口Demo

目前仅提供部分开发语言的demo，其他语言请参照下方接口文档进行开发。

## 接口要求

集成实时语音转写API时，需按照以下要求。

| 内容     | 说明                                                         |
| :------- | ------------------------------------------------------------ |
| 请求协议 | http[s] (为提高安全性，强烈推荐https)                        |
| 请求地址 | http[s]: //ai.abcpen.com/v1/tts/long?{请求参数} *注：服务器IP不固定，为保证您的接口稳定，请勿通过指定IP的方式调用接口，使用域名方式调用* |
| 接口鉴权 | 签名机制，详见 [signa生成](#signa生成)                       |
| 响应格式 | 统一采用JSON格式                                             |
| 开发语言 | 任意，只要可以向笔声云服务发起http[s]请求的均可              |



## 接口调用流程

*注：* 若需配置IP白名单，请发送邮件到support@abcpen.com

实时语音转写接口调用包括两个阶段：握手阶段和实时通信阶段。

### 握手阶段

接口地址

```text
    http://ai.abcpen.com/v1/tts/long?{请求参数}
    或
    https://ai.abcpen.com/v1/tts/long?{请求参数}
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
	https://ai.abcpen.com/v1/tts/long?appid=595f23df&ts=1512041814&signa=IrrzsJeOFk1NGfJHW6SkHUoN9CU=&pd=edu
```

#### 返回值

结果格式为json，字段说明如下：

| 参数   | 类型   | 说明                                            |
| :----- | :----- | :---------------------------------------------- |
| action | string | 结果标识，started:握手，result:结果，error:异常 |
| code   | string | 结果码(具体见[错误码](#错误码))                 |
| data   | string | 结果数据                                        |
| desc   | string | 描述                                            |
| sid    | string | 会话ID                                          |

其中sid字段主要用于DEBUG追查问题，如果出现问题，可以提供sid帮助确认问题。
