
## 使用ZMeet-AI JavaSDK

### 添加引用

#### gradle

```groovy
repositories {
    ...
    maven {
        url "https://nexus.abcpen.com/repository/cloud/"
    }
}

dependencies {
    api "com.abcpen.cloud:ai-cloud:0.0.2"
}
```

#### maven

```xml

<!--仓库地址 -->
<repositories>
    <repository>
        <id>abcpen</id>
        <name>Nexus</name>
        <url>ttps://nexus.abcpen.com/repository/cloud/</url>
    </repository>
</repositories>

 <!-- 引用 -->
<dependency>
<groupId>com.abcpen.cloud</groupId>
<artifactId>ai-cloud</artifactId>
<version>0.0.1</version>
</dependency>
```

### 初始化 `ZMeetAiTemplate`

```java

EngineConfig config = new EngineConfig()
         //申请的appid
        .setAppid(appId)
         //申请的secret
        .setSecret(secret)
        //endpoint  默认为 ai.abcpen.com
        .setEndpoint(endPoint)

ZMeetAiTemplate aiTemplate = new ZMeetAiTemplate(config)

```


### 使用TTS服务
```java
TTSService ttsService =  aiTemplate.getTtsService()


public interface TTSService {

    //v2 版本tts 
    TTSResult ttsV2(TTSRequestV2 ttsRequestV2) throws ZMeetException;

    //v1 版本tts
    TTSResult ttsV1(TTSRequestV1 ttsRequest) throws ZMeetException;
}

```
[TTSRequestV1版本文档](https://github.com/zmeet-ai/tts-demo/blob/main/docs/README-v1.md#3%E8%AF%B7%E6%B1%82%E5%8F%82%E6%95%B0)

[TTSRequestV2参数参考](https://github.com/zmeet-ai/tts-demo/blob/main/docs/README-v1.md#3%E8%AF%B7%E6%B1%82%E5%8F%82%E6%95%B0)

`TTSResult` 包含生产`TTS`转换的`audioUrl`


### 使用ASR服务
```java
AsrService asrService = aiTemplate.getAsrService()

public interface AsrService {


    /**
     * 同步识别 默认无超时时间
     *
     * @param asrRequest
     * @return
     */
    AsrResult recognizeSync(AsrRequest asrRequest) throws ZMeetException;

    /**
     * 同步识别 超时时间
     *
     * @param asrRequest
     * @param timeout 超时时间
     * @return
     */
    AsrResult recognizeSync(AsrRequest asrRequest, Duration timeout) throws ZMeetException;

    /**
     * 异步识别
     *   识别完成后 通过回调通知
     * @param asrRequest
     * @return
     * @throws ZMeetException
     */
    void recognize(AsrRequest asrRequest, AsrResultCallBack<AsrResult> asrResultCallBack);

}

```
### AsrResult 识别结果
```
// 开始时间
private long startTime;
// 结束时间
private long endTime;
// 文字
private String word;
// 说话人标识 0,1,2,3... 每个值代表当前音频唯一的声纹
private String speakerTag;
```
## 使用标签摘要服务

```java
LabelService labelService = aiTemplate.getLabelService()



public interface LabelService {


   /**
    * 通过输入文本提取对应标签信息
    */
    LabelResult extractTxtLabel(ExtractTxtLabelRequest extractTxtLabelRequest) throws ZMeetException;

}


```

### 结果说明
```

public class LabelResult {

    /**
     * 摘要
     */
    private String summary;

    /**
     * 标签列表
     */
    private List<LabelItem> labelItems;

    @Override
    public String toString() {
        return "LabelResult{" +
                "\nsummary='" + summary + ''' +
                "\nlabelItems=" + labelItems +
                '}';
    }

}



public static class LabelItem {
    //标签名称
    private String label;
    
    //标签分数
    private Double score;

    @Override
    public String toString() {
        return "\nLabelItem{" +
                "label='" + label + ''' +
                ", score=" + score +
                '}';
    }
}


```



## 运行Demo
### 编译
 ```
 gradle shaowJar
 ```
生成jar包 在 `build/libs/zmeet-cloud.jar`

### 运行jar
 ```
 java -jar zmeet-cloud.jar 
 usage: 认证信息
  -a,--appId <arg>      appId
  -asr,--asr            使用asr  -asr -help 查看
  -e,--endpoint <arg>   endpoint 为对应域名地址
                       列如 ai.abcpen.com 私有化部署的请填写 自己对应域名
  -s,--secret <arg>     app Secret
  -tts,--tts            使用tts -tts -help 查看
 ```


| 参数 |  描述 | 是否必填|  默认值|
| --- | --- | --- | --- |
| -a/--appId  | 申请的Appid | 是 |
| -s/--secret  | 申请的秘钥 | 是 |
| -e/--endpoint  | 申请的Appid | 否 | ai.abcpen.com
| -tts |  使用tts | 否 | 无值
| -asr |  使用asr | 否 | 无值


### 使用 `tts`

通过 `--help` 查看对应说明
 ```
java -jar zmeet-cloud.jar -a appid -s secret -tts --help
usage: tts 使用方式
 -at,--at <arg>           audio_type可不填，不填时默认为 3。
                          audioType=3 返回 16K 采样率的 mp3
                          audioType=4 返回 8K 采样率的 mp3
                          audioType=5 返回 24K 采样率的 mp3
                          audioType=6 返回 48k采样率的mp3
                          audioType=7 返回 16K 采样率的 pcm 格式
                          audioType=8 返回 8K 采样率的 pcm 格式
                          audioType=9 返回 24k 采样率的pcm格式
                          audioType=10 返回 8K 采样率的 wav 格式
                          audioType=11 返回 16K 采样率的 wav 格式
 -c,--content <arg>       tts content 转换文本 必填
 -f,--file <arg>          tts 以文件形式输入 文件地址
 -sid,--spkid <arg>       tts spk id 转换语音包id
                          ====================
                          0：女声（柔和）；
                          1，女声（正式）；
                          2，女生（柔和带正式）；
                          3：男声（柔和）
                          4：男声（柔和带正式）；
                          5：男声（闽南话）;
                          6：女生（闽南话）。
                          7. yunxiao 云霄
                          8. yunni 云妮
                          9. yunbei 云北
                          10. yunyang 云阳
                          11. yunxia 云夏
                          12. yunxi 云曦
                          13. yunjian 云剑
                          14. yunyi 云逸
                          ====================
 -speed,--speed <arg>     语速 0.75-1.25 可以不设置,默认是 1.0
 -v,--version <arg>       api 使用版本  默认为v1 可选择 v2
 -volume,--volume <arg>   音量 0.75-1.25 可以不设置,默认是 1.0

 ```
样例 省略部分为`appid` 和 `secret` 填写


```
 java -jar zmeet-cloud.jar -a <appid>  -s <secret>   -tts  -f ~/Downloads/tts.txt 
 
 
 java -jar zmeet-cloud.jar -a <appid>  -s <secret>   -tts  -c 你好中国
 
21:24:08.327 [main] INFO  com.abcpen.ai.TTSCommand - ========use v1 api tts========
21:24:08.330 [main] INFO  com.abcpen.ai.TTSCommand - args is TTSRequestV1(spkid=0, audioType=3, speed=1.0, volume=1.0, style=flytek, content=2022年海南省普通高考普通类考生成绩分布表显示，900分的考生全海南省一共3名。25日，男孩母亲回暂未作出决定，打算先选好专业)
21:24:11.895 [main] INFO  com.abcpen.ai.Main - tts result:TTSResult{
 content='2022年海南省普通高考普通类考生成绩分布表显示，900分的考生全海南省一共3名。25日，男孩母亲回应称，得知成绩时自己很开心，清华 北大两所高校都已取得联系，孩子目前有些迷茫，暂未作出决定，打算先选好专业'
 speakId='0'
 audioUrl='https://zos.abcpen.com/tts/1545/20221208/c997ee45-592a-4edd-8b6f-5b15515a9108.mp3'}

```


### 使用 `Asr`
```
java -jar zmeet-cloud.jar -a appid -s secret -asr --help
usage: asr 使用方式
 -audio,--audio <arg>   音频文件地址
 
```
样例 省略部分为`appid` 和 `secret` 填写  
`java -jar ...  -asr -audio https://zos.abcpen.com/test/asr/7ec28385-5bab-4ff2-bc8f-1b2b8fc15fbf.wav`

```
 java -jar zmeet-cloud.jar  -a <appid> -s <secret>  -asr -audio https://zos.abcpen.com/test/asr/7ec28385-5bab-4ff2-bc8f-1b2b8fc15fbf.wav

## 返回结果
21:25:29.600 [main] INFO  com.abcpen.ai.Main - 时间 [3240-7200] 说话人:0 结果:当我以为苍蓝绝就这样永远完结的时候，
21:25:29.601 [main] INFO  com.abcpen.ai.Main - 时间 [7200-12760] 说话人:0 结果:我却在江西上饶望仙谷回到了故事的开头。
21:25:29.601 [main] INFO  com.abcpen.ai.Main - 时间 [12760-14720] 说话人:0 结果:悬挂在崖壁上的世界，
21:25:29.602 [main] INFO  com.abcpen.ai.Main - 时间 [14720-19800] 说话人:0 结果:像是水云天上那颗懵懂的兰花草生活的私密店，
21:25:29.602 [main] INFO  com.abcpen.ai.Main - 时间 [19800-22380] 说话人:0 结果:横亘在峡谷两侧的蓝月月桥，
21:25:29.602 [main] INFO  com.abcpen.ai.Main - 时间 [22380-25100] 说话人:0 结果:像是沧沿海那座相思桥。
21:25:29.602 [main] INFO  com.abcpen.ai.Main - 时间 [25100-30940] 说话人:0 结果:许清望月遇见你一首定情的幼稚，
21:25:29.602 [main] INFO  com.abcpen.ai.Main - 时间 [30940-34620] 说话人:0 结果:倒也成了信念，
21:25:29.602 [main] INFO  com.abcpen.ai.Main - 时间 [34620-40140] 说话人:1 结果:一直风都落在水月中，
21:25:29.602 [main] INFO  com.abcpen.ai.Main - 时间 [40140-41900] 说话人:0 结果:十里红妆的嫁娶中，
21:25:29.602 [main] INFO  com.abcpen.ai.Main - 时间 [41900-44640] 说话人:0 结果:藏在回到了云梦泽那天，
21:25:29.603 [main] INFO  com.abcpen.ai.Main - 时间 [44640-48600] 说话人:0 结果:那位东方园外牵着她的小兰花走进洞房，
21:25:29.603 [main] INFO  com.abcpen.ai.Main - 时间 [48600-53240] 说话人:0 结果:花烛夜望仙往前，
21:25:29.603 [main] INFO  com.abcpen.ai.Main - 时间 [53240-56160] 说话人:0 结果:原来世上真有这样一个地方，
21:25:29.603 [main] INFO  com.abcpen.ai.Main - 时间 [56160-57040] 说话人:0 结果:依然在上演。
21:25:29.603 [main] INFO  com.abcpen.ai.Main - 时间 [57040-61760] 说话人:0 结果:苍蓝泉把那天上人间的故事成全，
21:25:29.603 [main] INFO  com.abcpen.ai.Main - 时间 [61760-62980] 说话人:1 结果:他看过身边。
```