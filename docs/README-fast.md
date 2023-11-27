## 语音合成极速版API
### 中英文语音极速合成版本
* 100毫秒极速识别
* 支持中英文混合文本
* 支持感情表达
#### URL
**`https://tts-dev.abcpen.com/v3/tts/emotion1/gen`**
中英文的语音合成

#### KEY
* 使用之前，请向商务申请appKey和appSecret, 以正常服务请求。
#### 请求参数：

* 以HTTPS POST(**x-www-form-urlencoded**)请求发送

|    参数    | 数据类型 | 是否必须 | 说明                                                         | 默认值    |
| :--------: | -------- | -------- | :----------------------------------------------------------- | --------- |
|    text    | String   | 是       | 待合成的中英文文本，需统一编码成utf-8格式                    |           |
|  emotion   | String   | 否       | 该段文本的情感， "neutral" (普通),  "happy"（开心）, "angry"（生气）, "sad"（悲伤）, "fear"（恐惧）, "hate"（厌恶）, "surprise"(惊讶) | "普通"    |
| speaker_id | String   | 否       | 语音合成的具体的模特发音id号，支持2000多种模特发音           | "1000"    |
|   speed    | float    | 语速     | 分为： " x-slow", "slow",  "medium", " fast",  "x-fast", "default" | "default" |

#### 返回参数

```json
{"code":"0","msg":"success","data":{"audio_url":"https://zos.abcpen.com/tts/test1/20231127/audio/28386da3-92c5-484b-9a4d-73a29d215be1.wav","time":0.132161537068896}}
```
#### 代码示例
参考 (极速版本代码示例)[./python/fast_tts.py]



